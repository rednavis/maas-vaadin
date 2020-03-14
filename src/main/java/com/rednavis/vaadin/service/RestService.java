package com.rednavis.vaadin.service;

import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_CURRENTUSER;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_REFRESH_TOKEN;
import static com.rednavis.shared.util.RestUrlUtils.USER_URL;
import static com.rednavis.shared.util.StringUtils.BEARER_SPACE;

import com.google.gson.Gson;
import com.rednavis.shared.rest.request.RefreshTokenRequest;
import com.rednavis.shared.rest.response.ErrorResponse;
import com.rednavis.shared.rest.response.SignInResponse;
import com.rednavis.shared.security.CurrentUser;
import com.rednavis.vaadin.exceptions.MaasVaadinException;
import com.rednavis.vaadin.property.MaasProperty;
import com.rednavis.vaadin.util.SessionUtils;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.VaadinSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
class RestService {

  private final RestTemplate restTemplate;
  private final AuthenticateService authenticateService;
  private final MaasProperty maasProperty;
  private final Gson gson;

  public <R> R get(String url, Class<R> responseClass) {
    try {
      return restTemplate.exchange(url, HttpMethod.GET, null, responseClass)
          .getBody();
    } catch (RestClientResponseException e) {
      parseException(e);
      throw new MaasVaadinException("get - something wrong with communication with MaasAPI");
    }
  }

  /**
   * getWithToken.
   *
   * @param url           url
   * @param token         token
   * @param responseClass responseClass
   * @param <R>           R
   * @return
   */
  public <R> R getWithToken(String url, String token, Class<R> responseClass) {
    //request entity is created with request body and headers
    HttpHeaders httpHeaders = createAuthorizationHeaders(token);
    HttpEntity requestEntity = new HttpEntity<>(null, httpHeaders);
    try {
      return restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseClass)
          .getBody();
    } catch (RestClientResponseException e) {
      parseException(e);
      throw new MaasVaadinException("getWithToken - something wrong with communication with MaasAPI");
    }
  }

  /**
   * post.
   *
   * @param url           url
   * @param request       request
   * @param responseClass responseClass
   * @param <T>           T
   * @param <R>           R
   * @return
   */
  public <T, R> R post(String url, T request, Class<R> responseClass) {
    HttpEntity<T> requestEntity = new HttpEntity<>(request, new HttpHeaders());
    try {
      return restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseClass)
          .getBody();
    } catch (RestClientResponseException e) {
      parseException(e);
      throw new MaasVaadinException("post - something wrong with communication with MaasAPI");
    }
  }

  /**
   * postWithToken.
   *
   * @param url           url
   * @param request       request
   * @param token         token
   * @param responseClass responseClass
   * @param <T>           T
   * @param <R>           R
   * @return
   */
  public <T, R> R postWithToken(String url, T request, String token, Class<R> responseClass) {
    //request entity is created with request body and headers
    HttpHeaders httpHeaders = createAuthorizationHeaders(token);
    HttpEntity<T> requestEntity = new HttpEntity<>(request, httpHeaders);
    try {
      return restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseClass)
          .getBody();
    } catch (RestClientResponseException e) {
      parseException(e);
      throw new MaasVaadinException("postWithToken - something wrong with communication with MaasAPI");
    }
  }

  public String createAuthUrl(String restPoint) {
    return maasProperty.getApi()
        .getServer() + AUTH_URL + restPoint;
  }

  public String createUserUrl(String restPoint) {
    return maasProperty.getApi()
        .getServer() + USER_URL + restPoint;
  }

  public CurrentUser getCurrenUser(String accessToken) {
    String url = createAuthUrl(AUTH_URL_CURRENTUSER);
    CurrentUser currentUser = getWithToken(url, accessToken, CurrentUser.class);
    log.info("currentUser [currentUser: {}]", currentUser);
    return currentUser;
  }

  private HttpHeaders createAuthorizationHeaders(String accessToken) {
    log.info("Current accessToken [accessToken: {}]", accessToken);
    //set up the basic authentication header
    String authorizationHeader = BEARER_SPACE + accessToken;

    //setting up the request headers
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.add(HttpHeaders.AUTHORIZATION, authorizationHeader);
    return requestHeaders;
  }

  private void parseException(RestClientResponseException ex) {
    log.error("RestClientResponseException {} ", ex.getMessage());
    ErrorResponse errorResponse = gson.fromJson(ex.getResponseBodyAsString(), ErrorResponse.class);
    Notification.show(errorResponse.getMessage());

    if (errorResponse.getExceptionId().equals("JwtAccessTokenExpiredException")) {
      refreshToken();
    }
    if (errorResponse.getExceptionId().equals("JwtRefreshTokenExpiredException")) {
      authenticateService.reject();
    }
  }

  private void refreshToken() {
    String refreshToken = SessionUtils.getRefreshToken(VaadinSession.getCurrent());
    log.info("refreshToken [refreshToken: {}]", refreshToken);
    String url = createAuthUrl(AUTH_URL_REFRESH_TOKEN);
    RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder()
        .refreshToken(refreshToken)
        .build();
    SignInResponse signInResponse = post(url, refreshTokenRequest, SignInResponse.class);
    CurrentUser currentUser = getCurrenUser(signInResponse.getAccessToken());
    authenticateService.refreshToken(currentUser, signInResponse);
  }
}
