package eu.koboo.vaadin.paste.utility;

import com.vaadin.flow.component.UI;
import java.util.function.BiConsumer;

public class Resolution {

  public static void addResize(BiConsumer<Integer, Integer> sizeConsumer) {
    UI.getCurrent().getPage().retrieveExtendedClientDetails(receiver -> sizeConsumer.accept(receiver.getScreenHeight(), receiver.getScreenWidth()));
    UI.getCurrent().getPage().addBrowserWindowResizeListener(resize -> sizeConsumer.accept(resize.getHeight(), resize.getWidth()));
  }

}
