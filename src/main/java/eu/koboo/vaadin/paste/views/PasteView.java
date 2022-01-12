package eu.koboo.vaadin.paste.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import de.f0rce.ace.AceEditor;
import de.f0rce.ace.enums.AceMode;
import de.f0rce.ace.enums.AceTheme;
import eu.koboo.vaadin.paste.repository.Paste;
import eu.koboo.vaadin.paste.repository.PasteService;
import eu.koboo.vaadin.paste.utility.Clipboard;
import eu.koboo.vaadin.paste.utility.Cookies;
import eu.koboo.vaadin.paste.utility.Date;
import eu.koboo.vaadin.paste.views.dialog.InfoDialog;
import eu.koboo.vaadin.paste.utility.Param;
import eu.koboo.vaadin.paste.views.dialog.SettingsDialog;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@PageTitle("Paste")
@Route(value = "")
@RouteAlias(value = "paste")
public class PasteView extends VerticalLayout implements AfterNavigationObserver {

  PasteService service;
  AceEditor editor;
  Clipboard clipboard;
  Dialog progressDialog;

  public PasteView(PasteService service) {
    this.service = service;
    setPadding(false);
    setSpacing(false);
    addClassName("paste-editor");
    setSizeFull();

    clipboard = new Clipboard();

    editor = new AceEditor();
    editor.setLiveAutocompletion(true);
    editor.setAutoComplete(true);
    editor.setWrap(true);
    editor.addClassName("ace-editor");
    editor.setTheme(AceTheme.terminal);
    editor.setMode(AceMode.java);
    editor.setInitialFocus(true);
    editor.setHighlightActiveLine(true);

    progressDialog = new Dialog();
    progressDialog.setMaxWidth("400px");
    progressDialog.setWidthFull();
    ProgressBar progressBar = new ProgressBar();
    progressBar.setWidthFull();
    progressBar.setIndeterminate(true);
    progressDialog.add(progressBar);
    progressDialog.addDialogCloseActionListener(e -> {
      if(progressBar.isIndeterminate()) {
        progressDialog.open();
      }
    });

    Button saveButton = new Button(VaadinIcon.DISC.create());
    saveButton.addClassName("button");
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    saveButton.getElement().setProperty("title", "Save (CTRL + S)");
    saveButton.addClickListener(e -> {
      progressDialog.open();
      try {
        // Here it needs a short sleep, because AceEditor needs time for something.
        // Further investigation planned. Pretty dirty work-around, but now it works. :(
        Thread.sleep(700);
      } catch (InterruptedException exc) {
        // Silent ignore
      }
      String text = editor.getValue();
      if (text == null) {
        Notification n = new Notification();
        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
        n.setText("Please submit some text, before saving!");
        n.setDuration(2500);
        n.open();
        progressBar.setIndeterminate(false);
        progressBar.setValue(1);
        return;
      }
      Paste paste = new Paste(
          UUID.randomUUID().toString(),
          Base64.getEncoder().encodeToString(text.getBytes()),
          editor.getTheme(),
          editor.getMode(),
          LocalDate.now().format(Date.FORMATTER)
      );
      service.getRepository().save(paste);
      clipboard.copyCode(paste, () -> {
        progressBar.setIndeterminate(false);
        progressBar.setValue(1);
        UI.getCurrent().navigate("show", Param.with("p", paste.getPasteId()).build());
        progressDialog.close();
      });
    });
    Shortcuts.addShortcutListener(this, saveButton::clickInClient, Key.KEY_S, KeyModifier.CONTROL);

    Button settingsButton = new Button(VaadinIcon.COG.create());
    settingsButton.addClassName("button");
    settingsButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    settingsButton.getElement().setProperty("title", "Settings (CTRL + ALT + S)");
    SettingsDialog settingsDialog = new SettingsDialog(editor);
    settingsButton.addClickListener(e -> settingsDialog.open());
    Shortcuts.addShortcutListener(this, settingsButton::clickInClient, Key.KEY_B, KeyModifier.CONTROL);

    Button infoButton = new Button(VaadinIcon.INFO_CIRCLE_O.create());
    infoButton.addClassName("button");
    infoButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    infoButton.getElement().setProperty("title", "Info (CTRL + I)");
    InfoDialog infoDialog = new InfoDialog();
    infoButton.addClickListener(e -> infoDialog.open());
    Shortcuts.addShortcutListener(this, infoButton::clickInClient, Key.KEY_I, KeyModifier.CONTROL);

    HorizontalLayout menuLayout = new HorizontalLayout();
    menuLayout.setSpacing(false);
    menuLayout.addClassName("menu-layout");
    menuLayout.add(saveButton, settingsButton, infoButton);

    add(menuLayout);
    addAndExpand(editor);
  }

  @Override
  public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
    try {
      AceMode mode = AceMode.valueOf(Cookies.getCookieValue("PREF_MODE"));
      editor.setMode(mode);
      AceTheme theme = AceTheme.valueOf(Cookies.getCookieValue("PREF_THEME"));
      editor.setTheme(theme);
    } catch (Exception e) {
      // Silent ignore
    }

    String id = Param.getParam(afterNavigationEvent, "p");
    if(id == null) {
      return;
    }
    Optional<Paste> optional = service.getRepository().findById(id);
    if(!optional.isPresent()) {
      return;
    }
    Paste paste = optional.get();
    editor.setValue(paste.getText());
    editor.setMode(paste.getMode());
    editor.setTheme(paste.getTheme());
  }
}
