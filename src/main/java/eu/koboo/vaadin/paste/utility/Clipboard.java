package eu.koboo.vaadin.paste.utility;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import eu.koboo.vaadin.paste.repository.Paste;

public class Clipboard {

  public Clipboard() {
    String javaScriptFunction = "window.copyToClipboard = (str) => {\n"
        + "  const textarea = document.createElement(\"textarea\");\n"
        + "  textarea.value = str;\n"
        + "  textarea.style.position = \"absolute\";\n"
        + "  textarea.style.opacity = \"0\";\n"
        + "  document.body.appendChild(textarea);\n"
        + "  textarea.select();\n"
        + "  document.execCommand(\"copy\");\n"
        + "  document.body.removeChild(textarea);\n"
        + "};";
    UI.getCurrent().getPage().executeJs(javaScriptFunction);
  }

  public void copyTo(String value) {
    UI.getCurrent().getPage().executeJs("window.copyToClipboard($0)", value);
  }

  public void copyCode(Paste paste, Runnable callback) {
    UI.getCurrent().getPage().fetchCurrentURL(url -> {
      String domain;
      if(url.getHost().equalsIgnoreCase("localhost")) {
        domain = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort() + "/";
      } else {
        domain = url.getProtocol() + "://" + url.getHost() + "/";
      }
      copyTo(domain + "show?p=" + paste.getPasteId());

      Notification n = Notification.show("Copied URL to your clipboard!", 2500,
          Position.BOTTOM_STRETCH);
      n.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
      n.open();
      if(callback != null) {
        callback.run();
      }
    });
  }

}
