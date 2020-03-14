package com.rednavis.vaadin.service;

import static com.rednavis.vaadin.util.CookieEnum.ACCESS_TOKEN;
import static com.rednavis.vaadin.util.CookieEnum.REFRESH_TOKEN;

import com.rednavis.shared.rest.response.SignInResponse;
import com.rednavis.shared.security.CurrentUser;
import com.rednavis.vaadin.util.SecurityUtils;
import com.rednavis.vaadin.util.SessionUtils;
import com.vaadin.flow.server.VaadinSession;
import javax.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class AuthenticateService {

  private final CookieService cookieService;

  public void authenticate(CurrentUser currentUser, SignInResponse signInResponse, boolean saveUser) {
    authenticateSession(currentUser, signInResponse.getAccessToken(), signInResponse.getRefreshToken());
    setAccessToken(signInResponse.getAccessToken(), signInResponse.getAccessTokenExpiration(), saveUser);
    setRefreshToken(signInResponse.getRefreshToken(), signInResponse.getRefreshTokenExpiration(), saveUser);
  }

  public void reject() {
    cookieService.removeCookie(ACCESS_TOKEN);
    cookieService.removeCookie(REFRESH_TOKEN);

    VaadinSession vaadinSession = VaadinSession.getCurrent();
    SessionUtils.setAccessToken(vaadinSession, null);
    SessionUtils.setRefreshToken(vaadinSession, null);

    SecurityUtils.reject();
  }

  public void refreshToken(CurrentUser currentUser, SignInResponse signInResponse) {
    authenticateSession(currentUser, signInResponse.getAccessToken(), signInResponse.getRefreshToken());

    Cookie accessCookie = cookieService.getCookie(ACCESS_TOKEN);
    setAccessToken(signInResponse.getAccessToken(), signInResponse.getAccessTokenExpiration(), accessCookie != null);
    Cookie refreshCookie = cookieService.getCookie(REFRESH_TOKEN);
    setRefreshToken(signInResponse.getRefreshToken(), signInResponse.getRefreshTokenExpiration(), refreshCookie != null);
  }

  public void signInFromCookie(CurrentUser currentUser, VaadinSession vaadinSession) {
    //String accessToken = SessionUtils.getAccessToken(vaadinSession);
    //if (!isNullOrBlank(accessToken)) {
    //  CurrentUser currentUser = restService.getCurrenUser(accessToken);
    //  authenticateService.signInFromCookie(currentUser, vaadinSession);
    //}
    //
    //SecurityUtils.createAuthentication(currentUser);
    //VaadinSession vaadinSession = VaadinSession.getCurrent();
    //SessionUtils.setAccessToken(vaadinSession, accessToken);
    //SessionUtils.setRefreshToken(vaadinSession, refreshToken);
    //
    //Cookie accessCookie = cookieService.getCookie(ACCESS_TOKEN);
    //Cookie refreshCookie = cookieService.getCookie(REFRESH_TOKEN);
    //if (accessCookie != null && refreshCookie != null) {
    //  log.info("AccessCookies exists");
    //  SessionUtils.setAccessToken(vaadinSession, accessCookie.getValue());
    //  SessionUtils.setRefreshToken(vaadinSession, refreshCookie.getValue());
    //}
  }

  private void authenticateSession(CurrentUser currentUser, String accessToken, String refreshToken) {
    SecurityUtils.authenticate(currentUser);
    VaadinSession vaadinSession = VaadinSession.getCurrent();
    SessionUtils.setAccessToken(vaadinSession, accessToken);
    SessionUtils.setRefreshToken(vaadinSession, refreshToken);
  }

  private void setAccessToken(String accessToken, int accessTokenExpiration, boolean saveUser) {
    if (saveUser) {
      cookieService.addCookie(ACCESS_TOKEN, accessToken, accessTokenExpiration);
    } else {
      cookieService.removeCookie(ACCESS_TOKEN);
    }
  }

  private void setRefreshToken(String refreshToken, int refreshTokenExpiration, boolean saveUser) {
    if (saveUser) {
      cookieService.addCookie(REFRESH_TOKEN, refreshToken, refreshTokenExpiration);
    } else {
      cookieService.removeCookie(REFRESH_TOKEN);
    }
  }
}
