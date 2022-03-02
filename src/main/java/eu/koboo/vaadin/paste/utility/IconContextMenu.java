package eu.koboo.vaadin.paste.utility;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class IconContextMenu extends ContextMenu {

  public IconContextMenu(Button button) {
    super(button);
    setOpenOnClick(true);
  }

  public IconContextMenu() {
    super();
    setOpenOnClick(true);
  }

  public IconContextMenu addContextItem(VaadinIcon vaadinIcon, String text, ComponentEventListener<ClickEvent<MenuItem>> listener) {
    Icon icon = vaadinIcon.create();
    icon.addClassName("icon-context-menu");
    Span span = new Span(text);
    HorizontalLayout horizontalLayout = new HorizontalLayout();
    horizontalLayout.add(icon, span);
    addItem(horizontalLayout, listener);
    return this;
  }
}