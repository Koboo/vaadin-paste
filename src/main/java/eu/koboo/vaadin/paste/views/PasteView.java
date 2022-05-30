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
import com.vaadin.flow.component.notification.Notification.Position;
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
import eu.koboo.vaadin.paste.utility.FloatButton;
import eu.koboo.vaadin.paste.utility.IconContextMenu;
import eu.koboo.vaadin.paste.utility.JavaScript;
import eu.koboo.vaadin.paste.utility.Param;
import eu.koboo.vaadin.paste.utility.Resolution;
import eu.koboo.vaadin.paste.views.dialog.InfoDialog;
import eu.koboo.vaadin.paste.views.dialog.SettingsDialog;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@PageTitle("Paste")
@Route(value = "")
@RouteAlias(value = "paste")
public class PasteView extends VerticalLayout implements AfterNavigationObserver {

  PasteService service;
  AceEditor editor;
  Clipboard clipboard;

  String editId;
  AtomicReference<String> lastChangedValue;

  public PasteView(PasteService service) {
    this.service = service;
    setPadding(false);
    setSpacing(false);
    addClassName("paste-editor");
    setSizeFull();

    clipboard = new Clipboard();

    editor = new AceEditor();
    editor.setLiveAutocompletion(false);
    editor.setAutoComplete(false);
    editor.addClassName("ace-editor");
    editor.setTheme(AceTheme.cobalt);
    editor.setMode(AceMode.java);
    editor.setInitialFocus(true);
    editor.setHighlightActiveLine(false);
    editor.setHighlightSelectedWord(true);
    editor.setSofttabs(true);

    Button saveButton = new Button(VaadinIcon.DISC.create());
    saveButton.addClassName("button");
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    saveButton.getElement().setProperty("title", "Save (CTRL + S)");
    saveButton.addClickListener(e -> savePaste(false));
    Shortcuts.addShortcutListener(this, saveButton::clickInClient, Key.KEY_S, KeyModifier.CONTROL);

    Button rawButton = new Button(VaadinIcon.NEWSPAPER.create());
    rawButton.addClassName("button");
    rawButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    rawButton.getElement().setProperty("title", "Open raw Link");
    rawButton.addClickListener(e -> savePaste(true));

    Button settingsButton = new Button(VaadinIcon.COG.create());
    settingsButton.addClassName("button");
    settingsButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    settingsButton.getElement().setProperty("title", "Settings (CTRL + B)");
    SettingsDialog settingsDialog = new SettingsDialog(editor);
    settingsButton.addClickListener(e -> settingsDialog.open());
    Shortcuts.addShortcutListener(this, settingsButton::clickInClient, Key.KEY_B,
        KeyModifier.CONTROL);

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
    menuLayout.add(saveButton, rawButton, settingsButton, infoButton);

    add(menuLayout);
    addAndExpand(editor);

    FloatButton floatButton = new FloatButton(VaadinIcon.ELLIPSIS_DOTS_V.create());
    IconContextMenu contextMenu = new IconContextMenu(floatButton);

    contextMenu.addContextItem(VaadinIcon.DISC, "Save", e -> savePaste(false));
    contextMenu.addContextItem(VaadinIcon.NEWSPAPER, "Save Raw", e -> savePaste(true));
    contextMenu.addContextItem(VaadinIcon.COG, "Settings", e -> settingsDialog.open());
    contextMenu.addContextItem(VaadinIcon.INFO_CIRCLE_O, "Info", e -> infoDialog.open());

    add(floatButton);

    lastChangedValue = new AtomicReference<>(null);

    Resolution.addResize((height, width) -> {
      if (width > 800) {
        // PC
        menuLayout.setVisible(true);
        floatButton.setVisible(false);
      } else {
        // Mobile
        menuLayout.setVisible(false);
        floatButton.setVisible(true);
      }
    });
    editor.addAceChangedListener(event -> {
      lastChangedValue.set(event.getValue());
    });
  }

  @Override
  public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
    try {
      AceMode mode = AceMode.valueOf(Cookies.getCookieValue("PREF_MODE"));
      editor.setMode(mode);
    } catch (Exception e) {
      // Silent ignore
    }

    editId = Param.getParam(afterNavigationEvent, "p");
    if (editId == null) {
      return;
    }
    Optional<Paste> optional = service.getRepository().findById(editId);
    if (!optional.isPresent()) {
      return;
    }
    Paste paste = optional.get();
    String text = new String(Base64.getDecoder().decode(paste.getText()), StandardCharsets.UTF_8);
    editor.setValue(text);
  }

  private void savePaste(boolean raw) {
    editor.setReadOnly(true);
    String value = lastChangedValue.get();
    if (value == null || value.equalsIgnoreCase("null") || value.trim().equalsIgnoreCase("")) {
      editor.setReadOnly(false);
      Notification n = Notification.show("Please submit some text, before saving!", 2500, Position.BOTTOM_STRETCH);
      n.addThemeVariants(NotificationVariant.LUMO_ERROR);
      n.open();
      return;
    }
    String id = editId;
    if(id == null) {
      id = UUID.randomUUID().toString();
    }
    Paste paste = new Paste(
        id,
        Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8)),
        editor.getTheme(),
        LocalDate.now().format(Date.FORMATTER)
    );
    service.getRepository().save(paste);
    clipboard.copyCode(paste, () -> {
      if(raw) {
        JavaScript.redirectToRaw(paste.getPasteId());
      } else {
        UI.getCurrent().navigate("show", Param.with("p", paste.getPasteId()).build());
      }
    });
  }
}