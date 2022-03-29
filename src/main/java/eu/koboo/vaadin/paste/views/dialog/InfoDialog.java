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
    setMaxWidth("700px");
    setWidthFull();

    H2 title = new H2("Info");
    title.getStyle().set("margin", "unset");
    add(title);

    add(new Paragraph("Lightweight PasteService with Vaadin and MongoDB"));
    add(new Paragraph(""));
    add(new Paragraph(new Span("Hotkeys are only available for desktop users!")));
    add(new Paragraph(""));
    add(new Paragraph(new Span("Hotkeys ../paste")));
    add(new Paragraph(new Span("Save a paste  "), new Badge("CTRL + S")));
    add(new Paragraph(""));
    add(new Paragraph(new Span("Hotkeys ../show")));
    add(new Paragraph(new Span("Create new paste  "), new Badge("CTRL + S")));
    add(new Paragraph(new Span("Edit an existing paste  "), new Badge("CTRL + E")));
    add(new Paragraph(new Span("Copy link  "), new Badge("CTRL + C")));
    add(new Paragraph(""));
    add(new Paragraph(new Span("Global Hotkeys:")));
    add(new Paragraph(new Span("Open info  "), new Badge("CTRL + I")));
    add(new Paragraph(new Span("Open settings  "), new Badge("CTRL + B")));
    add(new Paragraph(""));
    add(new Paragraph(
        "Please note: Pastes are only stored temporarily and will be deleted after a specific amount of days. This can be adjusted when installing your own instance!"));
    add(new Paragraph(""));
    add(new Paragraph(new Anchor("https://github.com/Koboo/vaadin-paste", "Built with <3 by Koboo")));
    add(new Paragraph(new Anchor("mailto://admin@koboo.eu", "Contact me")));

    Button closeButton = new Button("Close", VaadinIcon.CLOSE.create());
    closeButton.getStyle().set("margin-top", "1rem");
    closeButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
    closeButton.setWidthFull();
    closeButton.addClickListener(e -> close());
    add(closeButton);
  }
}
