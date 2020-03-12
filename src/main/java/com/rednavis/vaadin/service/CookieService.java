package com.rednavis.vaadin.service;

import com.rednavis.vaadin.util.CookieEnum;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;
import java.util.Arrays;
import javax.servlet.http.Cookie;
import org.springframework.stereotype.Service;

@Service
class CookieService {

  /**
   * addCookie.
   *
   * @param cookieEnum cookieEnum
   * @param cookie     cookie
   * @param maxAge     maxAge
   */
  public void addCookie(CookieEnum cookieEnum, String cookie, int maxAge) {
    // single slash means the cookie is set for your whole application.
    addCookie(cookieEnum, cookie, maxAge, "/");
  }

  /**
   * addCookie.
   *
   * @param cookieEnum cookieEnum
   * @param cookie     cookie
   * @param maxAge     maxAge
   * @param path       path
   */
  public void addCookie(CookieEnum cookieEnum, String cookie, int maxAge, String path) {
    Cookie assessCookie = new Cookie(cookieEnum.name(), cookie);
    assessCookie.setMaxAge(maxAge);
    assessCookie.setPath(path);
    VaadinService.getCurrentResponse().addCookie(assessCookie);
  }

  /**
   * removeCookie.
   *
   * @param cookieEnum cookieEnum
   */
  public void removeCookie(CookieEnum cookieEnum) {
    Cookie[] cookies = VaadinRequest.getCurrent().getCookies();
    // By setting the cookie maxAge to 0 it will deleted immediately
    Arrays.stream(cookies)
        .filter(cookie -> (cookie.getName().equals(cookieEnum.name())))
        .forEach(cookie -> addCookie(cookieEnum, null, 0));
  }

  /**
   * getCookie.
   *
   * @param cookieEnum cookieEnum
   * @return
   */
  public Cookie getCookie(CookieEnum cookieEnum) {
    Cookie[] cookies = VaadinRequest.getCurrent().getCookies();
    // By setting the cookie maxAge to 0 it will deleted immediately
    return Arrays.stream(cookies)
        .filter(cookie -> (cookie.getName().equals(cookieEnum.name())))
        .findFirst()
        .orElse(null);
  }
}
