package com.rednavis.vaadin.service;

import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_SIGNIN;
import static com.rednavis.shared.util.StringUtils.isNullOrBlank;

import com.rednavis.shared.rest.request.SignInRequest;
import com.rednavis.shared.rest.request.SignUpRequest;
import com.rednavis.shared.rest.response.SignInResponse;
import com.rednavis.shared.rest.response.SignUpResponse;
import com.rednavis.shared.security.CurrentUser;
import com.rednavis.vaadin.dto.SignInClient;
import com.rednavis.vaadin.util.SessionUtils;
import com.vaadin.flow.server.VaadinSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

  private final RestService restService;
  private final AuthenticateService authenticateService;

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
    if (signInResponse == null || isNullOrBlank(signInResponse.getAccessToken()) || isNullOrBlank(signInResponse.getRefreshToken())) {
      return false;
    }

    CurrentUser currentUser = restService.getCurrenUser(signInResponse.getAccessToken());
    authenticateService.authenticate(currentUser, signInResponse.getAccessToken(), signInResponse.getRefreshToken());
    authenticateService
        .setAccessToken(signInResponse.getAccessToken(), signInResponse.getAccessTokenExpiration(), signInClient.isSaveUser());
    authenticateService
        .setRefreshToken(signInResponse.getRefreshToken(), signInResponse.getRefreshTokenExpiration(), signInClient.isSaveUser());
    return true;
  }

  /**
   * signInFromCookie.
   *
   * @param vaadinSession vaadinSession
   */
  public void signInFromCookie(VaadinSession vaadinSession) {
    String accessToken = SessionUtils.getAccessToken(vaadinSession);
    if (!isNullOrBlank(accessToken)) {
      CurrentUser currentUser = restService.getCurrenUser(accessToken);
      authenticateService.signInFromCookie(currentUser, vaadinSession);
    }
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
}
