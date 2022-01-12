package eu.koboo.vaadin.paste;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The entry point of the Spring Boot application.
 * <p>
 * Use the @PWA annotation make the application installable on phones, tablets and some desktop
 * browsers.
 */
@SpringBootApplication
@Theme(value = "vaadinpaste", variant = Lumo.DARK)
@PWA(name = "VaadinPaste",
    shortName = "VaadinPaste",
    description = "Lightweight PasteService with Vaadin and MongoDB",
    iconPath = "images/logo.png",
    offlineResources = {"images/logo.png"})
@NpmPackage(value = "line-awesome", version = "1.3.0")
@EnableScheduling
public class Application extends SpringBootServletInitializer implements AppShellConfigurator {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}
