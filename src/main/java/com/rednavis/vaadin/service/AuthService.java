package com.rednavis.vaadin.service;

import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_SIGNIN;
import static com.rednavis.shared.util.StringUtils.isNullOrBlank;
import static com.rednavis.vaadin.util.CookieEnum.ACCESS_TOKEN;
import static com.rednavis.vaadin.util.CookieEnum.REFRESH_TOKEN;

import com.rednavis.shared.rest.request.SignInRequest;
import com.rednavis.shared.rest.request.SignUpRequest;
import com.rednavis.shared.rest.response.SignInResponse;
import com.rednavis.shared.rest.response.SignUpResponse;
import com.rednavis.shared.security.CurrentUser;
import com.rednavis.vaadin.dto.SignInClient;
import com.rednavis.vaadin.property.MaasProperty;
import com.vaadin.flow.server.VaadinSession;
import javax.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

  private final MaasProperty maasProperty;
  private final RestService restService;
  private final CookieService cookieService;
  private final AuthenticateService authenticateService;

  /**
   * signIn.
   *
   * @param signInClient signInClient
   * @return
   */
  public boolean signIn(SignInClient signInClient) {
    log.info("signIn [signInClient: {}]", signInClient);
    String url = maasProperty.createAuthUrl(AUTH_URL_SIGNIN);
    SignInRequest signInRequest = SignInRequest.builder()
        .userName(signInClient.getUserName())
        .password(signInClient.getPassword())
        .build();

    SignInResponse signInResponse = restService.post(url, signInRequest, SignInResponse.class);
    if (signInResponse == null || isNullOrBlank(signInResponse.getAccessToken()) || isNullOrBlank(signInResponse.getRefreshToken())) {
      return false;
    }

    CurrentUser currentUser = restService.getCurrenUser(signInResponse.getAccessToken());
    authenticateService.authenticate(currentUser, signInResponse.getAccessToken(), signInResponse.getRefreshToken());
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
    if (vaadinSession == null) {
      return;
    }

    Cookie accessCookie = cookieService.getCookie(ACCESS_TOKEN);
    Cookie refreshCookie = cookieService.getCookie(REFRESH_TOKEN);
    if (accessCookie == null || refreshCookie == null) {
      return;
    }

    String accessToken = accessCookie.getValue();
    String refreshToken = refreshCookie.getValue();
    if (isNullOrBlank(accessToken) || isNullOrBlank(refreshToken)) {
      return;
    }

    CurrentUser currentUser = restService.getCurrenUser(accessToken);
    authenticateService.authenticate(currentUser, accessToken, refreshToken);
  }

  public SignUpResponse signUp(SignUpRequest signUpRequest) {
    return null;
  }

  /**
   * signOut.
   */
  public void signOut() {
    authenticateService.reject();
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
