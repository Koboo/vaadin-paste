package eu.koboo.vaadin.paste.utility;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class Date {

  public static DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder().appendPattern("dd.MM.yyyy").toFormatter(
      Locale.GERMAN);

}
