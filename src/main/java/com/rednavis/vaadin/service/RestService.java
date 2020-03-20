package com.rednavis.vaadin.service;

import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_CURRENTUSER;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_REFRESH_TOKEN;
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
import org.springframework.core.ParameterizedTypeReference;
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

  public <R> R get(String url, ParameterizedTypeReference<R> responseType) {
    try {
      return restTemplate.exchange(url, HttpMethod.GET, null, responseType)
          .getBody();
    } catch (RestClientResponseException e) {
      parseException(e);
      throw new MaasVaadinException("get - something wrong with communication with MaasAPI");
    }
  }

  public <R> R getWithToken(String url, String token, Class<R> responseClass) {
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

  public <R> R getWithToken(String url, String token, ParameterizedTypeReference<R> responseType) {
    HttpHeaders httpHeaders = createAuthorizationHeaders(token);
    HttpEntity requestEntity = new HttpEntity<>(null, httpHeaders);
    try {
      return restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType)
          .getBody();
    } catch (RestClientResponseException e) {
      parseException(e);
      throw new MaasVaadinException("getWithToken - something wrong with communication with MaasAPI");
    }
  }

  public <R> R delete(String url, Class<R> responseClass) {
    try {
      return restTemplate.exchange(url, HttpMethod.DELETE, null, responseClass)
          .getBody();
    } catch (RestClientResponseException e) {
      parseException(e);
      throw new MaasVaadinException("delete - something wrong with communication with MaasAPI");
    }
  }

  public <R> R delete(String url, ParameterizedTypeReference<R> responseType) {
    try {
      return restTemplate.exchange(url, HttpMethod.DELETE, null, responseType)
          .getBody();
    } catch (RestClientResponseException e) {
      parseException(e);
      throw new MaasVaadinException("delete - something wrong with communication with MaasAPI");
    }
  }

  public <R> R deleteWithToken(String url, String token, Class<R> responseClass) {
    HttpHeaders httpHeaders = createAuthorizationHeaders(token);
    HttpEntity requestEntity = new HttpEntity<>(null, httpHeaders);
    try {
      return restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, responseClass)
          .getBody();
    } catch (RestClientResponseException e) {
      parseException(e);
      throw new MaasVaadinException("deletetWithToken - something wrong with communication with MaasAPI");
    }
  }

  public <R> R deleteWithToken(String url, String token, ParameterizedTypeReference<R> responseType) {
    HttpHeaders httpHeaders = createAuthorizationHeaders(token);
    HttpEntity requestEntity = new HttpEntity<>(null, httpHeaders);
    try {
      return restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, responseType)
          .getBody();
    } catch (RestClientResponseException e) {
      parseException(e);
      throw new MaasVaadinException("deleteWithToken - something wrong with communication with MaasAPI");
    }
  }

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

  public <T, R> R post(String url, T request, ParameterizedTypeReference<R> responseType) {
    HttpEntity<T> requestEntity = new HttpEntity<>(request, new HttpHeaders());
    try {
      return restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType)
          .getBody();
    } catch (RestClientResponseException e) {
      parseException(e);
      throw new MaasVaadinException("post - something wrong with communication with MaasAPI");
    }
  }

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

  public <T, R> R postWithToken(String url, T request, String token, ParameterizedTypeReference<R> responseType) {
    //request entity is created with request body and headers
    HttpHeaders httpHeaders = createAuthorizationHeaders(token);
    HttpEntity<T> requestEntity = new HttpEntity<>(request, httpHeaders);
    try {
      return restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType)
          .getBody();
    } catch (RestClientResponseException e) {
      parseException(e);
      throw new MaasVaadinException("postWithToken - something wrong with communication with MaasAPI");
    }
  }

  public <T, R> R put(String url, T request, Class<R> responseClass) {
    HttpEntity<T> requestEntity = new HttpEntity<>(request, new HttpHeaders());
    try {
      return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, responseClass)
          .getBody();
    } catch (RestClientResponseException e) {
      parseException(e);
      throw new MaasVaadinException("put - something wrong with communication with MaasAPI");
    }
  }

  public <T, R> R put(String url, T request, ParameterizedTypeReference<R> responseType) {
    HttpEntity<T> requestEntity = new HttpEntity<>(request, new HttpHeaders());
    try {
      return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, responseType)
          .getBody();
    } catch (RestClientResponseException e) {
      parseException(e);
      throw new MaasVaadinException("put - something wrong with communication with MaasAPI");
    }
  }

  public <T, R> R putWithToken(String url, T request, String token, Class<R> responseClass) {
    //request entity is created with request body and headers
    HttpHeaders httpHeaders = createAuthorizationHeaders(token);
    HttpEntity<T> requestEntity = new HttpEntity<>(request, httpHeaders);
    try {
      return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, responseClass)
          .getBody();
    } catch (RestClientResponseException e) {
      parseException(e);
      throw new MaasVaadinException("putWithToken - something wrong with communication with MaasAPI");
    }
  }

  public <T, R> R putWithToken(String url, T request, String token, ParameterizedTypeReference<R> responseType) {
    //request entity is created with request body and headers
    HttpHeaders httpHeaders = createAuthorizationHeaders(token);
    HttpEntity<T> requestEntity = new HttpEntity<>(request, httpHeaders);
    try {
      return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, responseType)
          .getBody();
    } catch (RestClientResponseException e) {
      parseException(e);
      throw new MaasVaadinException("putWithToken - something wrong with communication with MaasAPI");
    }
  }

  public CurrentUser getCurrenUser(String accessToken) {
    String url = maasProperty.createAuthUrl(AUTH_URL_CURRENTUSER);
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
    String url = maasProperty.createAuthUrl(AUTH_URL_REFRESH_TOKEN);
    RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder()
        .refreshToken(refreshToken)
        .build();
    SignInResponse signInResponse = post(url, refreshTokenRequest, SignInResponse.class);
    CurrentUser currentUser = getCurrenUser(signInResponse.getAccessToken());
    authenticateService.refreshToken(currentUser, signInResponse);
  }
}
