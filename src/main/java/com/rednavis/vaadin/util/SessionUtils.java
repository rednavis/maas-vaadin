package com.rednavis.vaadin.util;

import static com.rednavis.vaadin.util.CookieEnum.ACCESS_TOKEN;
import static com.rednavis.vaadin.util.CookieEnum.REFRESH_TOKEN;

import com.vaadin.flow.server.VaadinSession;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SessionUtils {

  public static String getAccessToken(VaadinSession vaadinSession) {
    return (String) vaadinSession.getAttribute(ACCESS_TOKEN.name());
  }

  public static String getRefreshToken(VaadinSession vaadinSession) {
    return (String) vaadinSession.getAttribute(REFRESH_TOKEN.name());
  }

  public static void setAccessToken(VaadinSession vaadinSession, String accessToken) {
    vaadinSession.setAttribute(ACCESS_TOKEN.name(), accessToken);
  }

  public static void setRefreshToken(VaadinSession vaadinSession, String refreshToken) {
    vaadinSession.setAttribute(REFRESH_TOKEN.name(), refreshToken);
  }
}
