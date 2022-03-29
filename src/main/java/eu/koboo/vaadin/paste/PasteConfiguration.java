package eu.koboo.vaadin.paste;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@PropertySource("file:paste.properties")
public class PasteConfiguration {

}
