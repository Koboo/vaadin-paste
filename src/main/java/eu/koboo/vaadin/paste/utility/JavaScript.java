package eu.koboo.vaadin.paste.utility;

import com.vaadin.flow.component.UI;

public class JavaScript {

  public static void redirectToRaw(String pasteId) {
    UI.getCurrent().getPage().executeJs("window.open(window.location.href.toString().split(\"?\")[0].replace(\"show\", \"\") + \"raw?p=" + pasteId + "\", \"_self\")");
  }
}