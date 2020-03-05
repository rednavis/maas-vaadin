package com.rednavis.vaadin.util;

import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL;
import static com.rednavis.shared.util.StringUtils.BEARER_SPACE;

import com.rednavis.shared.rest.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@SuppressWarnings("MethodTypeParameterName")
public class RestUtils {

  public static String auth;

  private final RestTemplate restTemplate;

  @Value("${maas.api.server}")
  private String maasApiServer;

  public String createAuthUrl(String restPoint) {
    return maasApiServer + AUTH_URL + restPoint;
  }

  /**
   * get.
   *
   * @param url        url
   * @param response   response
   * @param token      token
   * @param <RESPONSE> RESPONSE
   * @return
   */
  public <RESPONSE> ApiResponse<RESPONSE> get(String url, ParameterizedTypeReference<ApiResponse<RESPONSE>> response, boolean token) {
    ResponseEntity<ApiResponse<RESPONSE>> responseEntity;
    if (token) {
      responseEntity = getWithToken(url, response, auth);
    } else {
      responseEntity = getWithoutToken(url, response);
    }
    return responseEntity.getBody();
  }

  private <RESPONSE> ResponseEntity<ApiResponse<RESPONSE>> getWithoutToken(String url,
      ParameterizedTypeReference<ApiResponse<RESPONSE>> response) {
    return restTemplate.exchange(url, HttpMethod.GET, null, response);
  }

  private <RESPONSE> ResponseEntity<ApiResponse<RESPONSE>> getWithToken(String url,
      ParameterizedTypeReference<ApiResponse<RESPONSE>> response, String accessToken) {
    //request entity is created with request body and headers
    HttpHeaders httpHeaders = createAuthorizationHeaders(accessToken);
    HttpEntity requestEntity = new HttpEntity<>(null, httpHeaders);

    return restTemplate.exchange(url, HttpMethod.GET, requestEntity, response);
  }

  /**
   * post.
   *
   * @param url        url
   * @param request    request
   * @param response   response
   * @param token      token
   * @param <REQUEST>  REQUEST
   * @param <RESPONSE> RESPONSE
   * @return
   */
  public <REQUEST, RESPONSE> ApiResponse<RESPONSE> post(String url, REQUEST request,
      ParameterizedTypeReference<ApiResponse<RESPONSE>> response, boolean token) {
    ResponseEntity<ApiResponse<RESPONSE>> responseEntity;
    if (token) {
      responseEntity = postWithToken(url, request, response, auth);
    } else {
      responseEntity = postWithoutToken(url, request, response);
    }
    return responseEntity.getBody();
  }

  private <REQUEST, RESPONSE> ResponseEntity<ApiResponse<RESPONSE>> postWithoutToken(String url, REQUEST request,
      ParameterizedTypeReference<ApiResponse<RESPONSE>> response) {
    HttpEntity<REQUEST> requestEntity = new HttpEntity<>(request, new HttpHeaders());
    return restTemplate.exchange(url, HttpMethod.POST, requestEntity, response);
  }

  private <REQUEST, RESPONSE> ResponseEntity<ApiResponse<RESPONSE>> postWithToken(String url, REQUEST request,
      ParameterizedTypeReference<ApiResponse<RESPONSE>> response, String accessToken) {
    //request entity is created with request body and headers
    HttpHeaders httpHeaders = createAuthorizationHeaders(accessToken);
    HttpEntity<REQUEST> requestEntity = new HttpEntity<>(request, httpHeaders);

    return restTemplate.exchange(url, HttpMethod.POST, requestEntity, response);
  }

  private HttpHeaders createAuthorizationHeaders(String accessToken) {
    //set up the basic authentication header
    String authorizationHeader = BEARER_SPACE + accessToken;

    //setting up the request headers
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.add(HttpHeaders.AUTHORIZATION, authorizationHeader);
    return requestHeaders;
  }
}
