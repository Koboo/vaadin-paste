package eu.koboo.vaadin.paste.utility;

import com.vaadin.flow.component.html.Span;

public class Badge extends Span {

  public Badge(String text) {
    setText(text);
    getElement().getThemeList().add("badge contrast");
    addClassName("badge");
  }

}