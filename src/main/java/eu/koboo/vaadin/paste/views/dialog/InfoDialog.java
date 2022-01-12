package eu.koboo.vaadin.paste.views.dialog;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import eu.koboo.vaadin.paste.utility.Badge;

public class InfoDialog extends Dialog {

  public InfoDialog() {
    setMaxWidth("400px");
    setWidthFull();

    H2 title = new H2("Info");
    title.getStyle().set("margin", "unset");
    add(title);

    add(new Paragraph("Lightweight PasteService with Vaadin and MongoDB"));
    add(new Paragraph(new Span("Save a paste  "), new Badge("CTRL + S")));
    add(new Paragraph(new Span("Create new paste  "), new Badge("CTRL + S")));
    add(new Paragraph(new Span("Open info  "), new Badge("CTRL + I")));
    add(new Paragraph(new Span("Open settings  "), new Badge("CTRL + ALT + I")));
    add(new Paragraph(""));
    add(new Paragraph(
        "Please note: The created pastes are only stored temporarily and will be deleted after 1 week. This can be adjusted when installing your own instance!"));
    add(new Paragraph(""));
    add(new Paragraph("Built by Koboo with <3"));
    add(new Paragraph(new Anchor("https://github.com/Koboo/vaadin-paste")));
    add(new Paragraph("Feel free to leave a star. Contributions always welcome!"));

    Button closeButton = new Button("Close", VaadinIcon.CLOSE.create());
    closeButton.getStyle().set("margin-top", "1rem");
    closeButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
    closeButton.setWidthFull();
    closeButton.addClickListener(e -> close());
    add(closeButton);
  }
}
