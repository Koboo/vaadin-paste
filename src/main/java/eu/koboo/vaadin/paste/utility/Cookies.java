package eu.koboo.vaadin.paste.utility;

import com.vaadin.flow.server.VaadinService;
import javax.servlet.http.Cookie;

public class Cookies {

  public static void setCookie(String name, String value, int age) {
    Cookie activationCodeCookie = new Cookie(name, value);
    activationCodeCookie.setMaxAge(age);
    activationCodeCookie.setPath(VaadinService.getCurrentRequest().getContextPath());
    VaadinService.getCurrentResponse().addCookie(activationCodeCookie);
  }

  public static void setCookie(String name, String value) {
    Cookie activationCodeCookie = new Cookie(name, value);
    activationCodeCookie.setPath(VaadinService.getCurrentRequest().getContextPath());
    VaadinService.getCurrentResponse().addCookie(activationCodeCookie);
  }

  public static Cookie getCookie(String name) {
    for (Cookie cookie : VaadinService.getCurrentRequest().getCookies()) {
      if(!cookie.getName().equalsIgnoreCase(name)) {
        continue;
      }
      return cookie;
    }
    return null;
  }

  public static void removeCookie(String name) {
    Cookie cookie = new Cookie(name, "");
    cookie.setPath("/");
    cookie.setMaxAge(0);
    VaadinService.getCurrentResponse().addCookie(cookie);
  }

  public static String getCookieValue(String name) {
    Cookie cookie = getCookie(name);
    return cookie == null ? null : cookie.getValue();
  }
}