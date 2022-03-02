package eu.koboo.vaadin.paste.utility;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;

public class FloatButton extends Button {

  public FloatButton() {
    init();
  }

  public FloatButton(Component icon) {
    super(icon);
    init();
  }

  public FloatButton(String text, Component icon) {
    super(text, icon);
    init();
  }

  private void init() {
    addClassName("float-button");
    addThemeVariants(ButtonVariant.LUMO_PRIMARY);
  }
}