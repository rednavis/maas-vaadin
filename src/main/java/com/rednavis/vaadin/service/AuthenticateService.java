package com.rednavis.vaadin.service;

import static com.rednavis.vaadin.util.CookieEnum.ACCESS_TOKEN;
import static com.rednavis.vaadin.util.CookieEnum.REFRESH_TOKEN;

import javax.servlet.http.Cookie;
import com.rednavis.shared.rest.response.SignInResponse;
import com.rednavis.shared.security.CurrentUser;
import com.rednavis.vaadin.util.SecurityUtils;
import com.rednavis.vaadin.util.SessionUtils;
import com.vaadin.flow.server.VaadinSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class AuthenticateService {

  private final CookieService cookieService;

  public void authenticate(CurrentUser currentUser, String accessToken, String refreshToken) {
    SecurityUtils.authenticate(currentUser);
    VaadinSession vaadinSession = VaadinSession.getCurrent();
    SessionUtils.setAccessToken(vaadinSession, accessToken);
    SessionUtils.setRefreshToken(vaadinSession, refreshToken);
  }

  public void reject() {
    cookieService.removeCookie(ACCESS_TOKEN);
    cookieService.removeCookie(REFRESH_TOKEN);

    VaadinSession vaadinSession = VaadinSession.getCurrent();
    SessionUtils.removeAccessToken(vaadinSession);
    SessionUtils.removeRefreshToken(vaadinSession);

    SecurityUtils.reject();
  }

  public void refreshToken(CurrentUser currentUser, SignInResponse signInResponse) {
    authenticate(currentUser, signInResponse.getAccessToken(), signInResponse.getRefreshToken());

    Cookie accessCookie = cookieService.getCookie(ACCESS_TOKEN);
    if (accessCookie != null) {
      cookieService.addCookie(ACCESS_TOKEN, signInResponse.getAccessToken(), signInResponse.getAccessTokenExpiration());
    }
    Cookie refreshCookie = cookieService.getCookie(REFRESH_TOKEN);
    if (refreshCookie != null) {
      cookieService.addCookie(REFRESH_TOKEN, signInResponse.getRefreshToken(), signInResponse.getRefreshTokenExpiration());
    }
  }
}
