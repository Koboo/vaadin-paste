package eu.koboo.vaadin.paste.views.dialog;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import de.f0rce.ace.AceEditor;
import de.f0rce.ace.enums.AceMode;
import de.f0rce.ace.enums.AceTheme;
import eu.koboo.vaadin.paste.utility.Cookies;

public class SettingsDialog extends Dialog {

  public SettingsDialog(AceEditor editor) {
    setMaxWidth("700px");
    setWidthFull();
    setCloseOnEsc(true);
    setCloseOnOutsideClick(true);
    setResizable(false);
    setDraggable(true);

    H2 title = new H2("Settings");
    title.getStyle().set("margin", "unset");
    add(title);

    ComboBox<AceMode> modeComboBox = new ComboBox<>("Language:");
    modeComboBox.getElement().setProperty("title", "Change the specific language of the paste.");
    modeComboBox.setClearButtonVisible(false);
    modeComboBox.setItems(AceMode.values());
    modeComboBox.setItemLabelGenerator(AceMode::name);
    modeComboBox.setValue(AceMode.java);
    modeComboBox.setWidthFull();
    modeComboBox.addValueChangeListener(e -> editor.setMode(e.getValue()));
    add(modeComboBox);

    try {
      AceMode mode = AceMode.valueOf(Cookies.getCookieValue("PREF_MODE"));
      modeComboBox.setValue(mode);
    } catch (Exception e) {
      // Silent ignore
    }

    Button cookieButton = new Button("Save as preferences", VaadinIcon.CHECK.create());
    cookieButton.getStyle().set("margin-top", "1rem");
    cookieButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
    cookieButton.setWidthFull();
    cookieButton.addClickListener(e -> {
      Cookies.setCookie("PREF_MODE", modeComboBox.getValue().name(), 1_300_000);

      Notification n = Notification.show("Settings saved!", 2500, Position.BOTTOM_STRETCH);
      n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
      n.open();
      close();
    });
    add(cookieButton);

    Button closeButton = new Button("Close", VaadinIcon.CLOSE.create());
    closeButton.getStyle().set("margin-top", "1rem");
    closeButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
    closeButton.setWidthFull();
    closeButton.addClickListener(e -> close());
    add(closeButton);
  }
}
