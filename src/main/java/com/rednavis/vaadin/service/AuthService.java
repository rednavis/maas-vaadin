package com.rednavis.vaadin.service;

import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_SIGNIN;
import static com.rednavis.vaadin.util.CookieEnum.ACCESS_TOKEN;
import static com.rednavis.vaadin.util.CookieEnum.REFRESH_TOKEN;

import com.rednavis.shared.rest.request.SignInRequest;
import com.rednavis.shared.rest.request.SignUpRequest;
import com.rednavis.shared.rest.response.SignInResponse;
import com.rednavis.shared.rest.response.SignUpResponse;
import com.rednavis.shared.security.CurrentUser;
import com.rednavis.vaadin.dto.SignInClient;
import com.rednavis.vaadin.util.SecurityUtils;
import com.rednavis.vaadin.util.SessionUtils;
import com.vaadin.flow.server.VaadinSession;
import javax.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

  private final RestService restService;
  private final CookieService cookieService;

  /**
   * signIn.
   *
   * @param signInClient signInClient
   * @return
   */
  public boolean signIn(SignInClient signInClient) {
    log.info("signIn [signInClient: {}]", signInClient);
    String url = restService.createAuthUrl(AUTH_URL_SIGNIN);
    SignInRequest signInRequest = SignInRequest.builder()
        .userName(signInClient.getUserName())
        .password(signInClient.getPassword())
        .build();
    SignInResponse signInResponse = restService.post(url, signInRequest, SignInResponse.class);
    restService.authenticateActualUser(signInResponse.getAccessToken(), signInResponse.getRefreshToken());
    setAccessToken(signInResponse.getAccessToken(), signInResponse.getAccessTokenExpiration(), signInClient.isSaveUser());
    setRefreshToken(signInResponse.getRefreshToken(), signInResponse.getRefreshTokenExpiration(), signInClient.isSaveUser());
    return true;
  }

  /**
   * signInFromCookie.
   *
   * @param vaadinSession vaadinSession
   */
  public void signInFromCookie(VaadinSession vaadinSession) {
    Cookie accessCookie = cookieService.getCookie(ACCESS_TOKEN);
    Cookie refreshCookie = cookieService.getCookie(REFRESH_TOKEN);
    if (accessCookie != null && refreshCookie != null) {
      log.info("AccessCookies exists");
      CurrentUser currentUser = restService.getCurrenUser(accessCookie.getValue());
      SecurityUtils.createAuthentication(currentUser);
      SessionUtils.setAccessToken(vaadinSession, accessCookie.getValue());
      SessionUtils.setRefreshToken(vaadinSession, refreshCookie.getValue());
    }
  }

  public SignUpResponse signUp(SignUpRequest signUpRequest) {
    return null;
  }

  /**
   * signOut.
   */
  public void signOut() {
    VaadinSession vaadinSession = VaadinSession.getCurrent();
    SessionUtils.setAccessToken(vaadinSession, null);
    SessionUtils.setRefreshToken(vaadinSession, null);

    cookieService.removeCookie(ACCESS_TOKEN);
    cookieService.removeCookie(REFRESH_TOKEN);
    SecurityContextHolder.getContext().setAuthentication(null);
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
