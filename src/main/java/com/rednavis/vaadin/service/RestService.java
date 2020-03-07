package com.rednavis.vaadin.service;

import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_CURRENTUSER;
import static com.rednavis.shared.util.StringUtils.BEARER_SPACE;

import com.rednavis.shared.rest.ApiResponse;
import com.rednavis.shared.security.CurrentUser;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestService {

  private final RestTemplate restTemplate;

  @Value("${maas.api.server}")
  private String maasApiServer;

  public String createAuthUrl(String restPoint) {
    return maasApiServer + AUTH_URL + restPoint;
  }

  public <R> ApiResponse<R> get(String url, Class<R> responseClass) {
    return restTemplate.exchange(url, HttpMethod.GET, null, typeReference(responseClass))
        .getBody();
  }

  public <R> ApiResponse<R> getWithToken(String url, String token, Class<R> responseClass) {
    //request entity is created with request body and headers
    HttpHeaders httpHeaders = createAuthorizationHeaders(token);
    HttpEntity requestEntity = new HttpEntity<>(null, httpHeaders);
    return restTemplate.exchange(url, HttpMethod.GET, requestEntity, typeReference(responseClass))
        .getBody();
  }

  public <T, R> ApiResponse<R> post(String url, T request, Class<R> responseClass) {
    HttpEntity<T> requestEntity = new HttpEntity<>(request, new HttpHeaders());
    ParameterizedTypeReference<ApiResponse<R>> typeRef = typeReference(responseClass);
    return restTemplate.exchange(url, HttpMethod.POST, requestEntity, typeRef)
        .getBody();
  }

  public <T, R> ApiResponse<R> postWithToken(String url, T request, String token, Class<R> responseClass) {
    //request entity is created with request body and headers
    HttpHeaders httpHeaders = createAuthorizationHeaders(token);
    HttpEntity<T> requestEntity = new HttpEntity<>(request, httpHeaders);
    return restTemplate.exchange(url, HttpMethod.POST, requestEntity, typeReference(responseClass))
        .getBody();
  }

  public CurrentUser getCurrenUser(String accessToken) {
    String url = createAuthUrl(AUTH_URL_CURRENTUSER);
    ApiResponse<CurrentUser> currentUserApiResponse = getWithToken(url, accessToken, CurrentUser.class);
    CurrentUser currentUser = currentUserApiResponse.getPayloads();
    log.info("currentUser [currentUser: {}]", currentUser);
    return currentUser;
  }

  private <R> ParameterizedTypeReference<ApiResponse<R>> typeReference(Class<R> responseClass) {
    return new ParameterizedTypeReference<>() {
      @Override
      public Type getType() {
        Type type = super.getType();
        if (type instanceof ParameterizedType) {
          Type[] responseWrapperActualTypes = {responseClass};
          return TypeUtils.parameterize(ApiResponse.class, responseWrapperActualTypes);
        }
        return type;
      }
    };
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
}
