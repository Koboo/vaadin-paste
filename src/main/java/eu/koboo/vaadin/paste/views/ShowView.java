package eu.koboo.vaadin.paste.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouterLink;
import de.f0rce.ace.AceEditor;
import de.f0rce.ace.enums.AceMode;
import de.f0rce.ace.enums.AceTheme;
import eu.koboo.vaadin.paste.repository.Paste;
import eu.koboo.vaadin.paste.repository.PasteService;
import eu.koboo.vaadin.paste.utility.Clipboard;
import eu.koboo.vaadin.paste.utility.FloatButton;
import eu.koboo.vaadin.paste.utility.IconContextMenu;
import eu.koboo.vaadin.paste.utility.JavaScript;
import eu.koboo.vaadin.paste.utility.Param;
import eu.koboo.vaadin.paste.utility.Resolution;
import eu.koboo.vaadin.paste.views.dialog.InfoDialog;
import eu.koboo.vaadin.paste.views.dialog.SettingsDialog;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Locale;
import java.util.Optional;

@PageTitle("Show")
@Route(value = "show")
@RouteAlias(value = "show")
public class ShowView extends VerticalLayout implements AfterNavigationObserver {

  PasteService service;
  AceEditor editor;
  Clipboard clipboard;

  Paste paste;

  public ShowView(PasteService service) {
    this.service = service;
    setPadding(false);
    setSpacing(false);
    addClassName("paste-editor");
    setSizeFull();

    editor = new AceEditor();
    editor.setReadOnly(true);
    editor.setLiveAutocompletion(false);
    editor.setAutoComplete(false);
    editor.setWrap(true);
    editor.addClassName("ace-editor");
    editor.setTheme(AceTheme.cobalt);
    editor.setMode(AceMode.java);
    editor.setInitialFocus(false);
    editor.setHighlightActiveLine(false);

    clipboard = new Clipboard();

    Button newButton = new Button(VaadinIcon.PLUS.create());
    newButton.addClassName("button");
    newButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    newButton.getElement().setProperty("title", "New (CTRL + S)");
    newButton.addClickListener(e -> UI.getCurrent().getPage().open("", "_blank"));
    Shortcuts.addShortcutListener(this, newButton::clickInClient, Key.KEY_S, KeyModifier.CONTROL);

    Button editButton = new Button(VaadinIcon.PENCIL.create());
    editButton.addClassName("button");
    editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    editButton.getElement().setProperty("title", "Edit (CTRL + E)");
    editButton.addClickListener(e -> UI.getCurrent().navigate("", Param.with("p", paste.getPasteId()).build()));
    Shortcuts.addShortcutListener(this, editButton::clickInClient, Key.KEY_E, KeyModifier.CONTROL);

    Button rawButton = new Button(VaadinIcon.NEWSPAPER.create());
    rawButton.addClassName("button");
    rawButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    rawButton.getElement().setProperty("title", "Open raw Link");
    rawButton.addClickListener(e -> JavaScript.redirectToRaw(paste.getPasteId()));

    Button copyButton = new Button(VaadinIcon.COPY.create());
    copyButton.addClassName("button");
    copyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    copyButton.getElement().setProperty("title", "Copy Link (CTRL + C)");
    copyButton.addClickListener(e -> clipboard.copyCode(paste, null));
    Shortcuts.addShortcutListener(this, editButton::clickInClient, Key.KEY_C, KeyModifier.CONTROL);

    Button settingsButton = new Button(VaadinIcon.COG.create());
    settingsButton.addClassName("button");
    settingsButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    settingsButton.getElement().setProperty("title", "Settings (CTRL + B)");
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
    menuLayout.add(newButton, editButton, rawButton, copyButton, settingsButton, infoButton);

    add(menuLayout);
    addAndExpand(editor);

    FloatButton floatButton = new FloatButton(VaadinIcon.ELLIPSIS_DOTS_V.create());
    IconContextMenu contextMenu = new IconContextMenu(floatButton);

    contextMenu.addContextItem(VaadinIcon.PLUS, "New", e -> UI.getCurrent().navigate(""));
    contextMenu.addContextItem(VaadinIcon.PENCIL, "Edit", e -> UI.getCurrent().navigate("", Param.with("p", paste.getPasteId()).build()));
    contextMenu.addContextItem(VaadinIcon.NEWSPAPER, "Raw", e -> JavaScript.redirectToRaw(paste.getPasteId()));
    contextMenu.addContextItem(VaadinIcon.COPY, "Copy", e -> clipboard.copyCode(paste, null));
    contextMenu.addContextItem(VaadinIcon.COG, "Settings", e -> settingsDialog.open());
    contextMenu.addContextItem(VaadinIcon.INFO_CIRCLE_O, "Info", e -> infoDialog.open());

    add(floatButton);

    Resolution.addResize((height, width) -> {
      if(width > 800) {
        // PC
        menuLayout.setVisible(true);
        floatButton.setVisible(false);
      } else {
        // Mobile
        menuLayout.setVisible(false);
        floatButton.setVisible(true);
      }
    });
  }

  @Override
  public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
    String id = Param.getParam(afterNavigationEvent, "p");
    if (id == null) {
      UI.getCurrent().navigate("");
      return;
    }
    Optional<Paste> optional = service.getRepository().findById(id);
    if (!optional.isPresent()) {
      UI.getCurrent().navigate("");
      return;
    }
    paste = optional.get();
    String text = new String(Base64.getDecoder().decode(paste.getText()), StandardCharsets.UTF_8);
    editor.setValue(text);
  }
}