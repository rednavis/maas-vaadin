package com.rednavis.backend.service;

import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_CURRENTUSER;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_REFRESH_TOKEN;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_SIGNIN;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_SIGNUP;

import com.nimbusds.jwt.SignedJWT;
import com.rednavis.backend.jwt.JwtTokenService;
import com.rednavis.shared.rest.ApiResponse;
import com.rednavis.shared.rest.request.RefreshTokenRequest;
import com.rednavis.shared.rest.request.SignInRequest;
import com.rednavis.shared.rest.request.SignUpRequest;
import com.rednavis.shared.rest.response.SignInResponse;
import com.rednavis.shared.rest.response.SignUpResponse;
import com.rednavis.shared.security.CurrentUser;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

  private static String AUTH;

  @Autowired
  private JwtTokenService jwtTokenService;
  @Value("${maas.api.server}")
  private String maasApiServer;

  @Autowired
  private RestTemplateBuilder restTemplateBuilder;

  @Override
  public CurrentUser getCurrentUser() {
    String url = createUrl(AUTH_URL_CURRENTUSER);
    log.info("GET: {}", url);

    ApiResponse<CurrentUser> currentUserApiResponse = get(url,
        new ParameterizedTypeReference<>() {
        });
    log.info("currentUserApiResponse: {}", currentUserApiResponse);
    return currentUserApiResponse.getPayloads();
  }

  @Override
  public boolean signIn(SignInRequest signInRequest) {
    String url = createUrl(AUTH_URL_SIGNIN);
    log.info("POST: {}", url);
    ApiResponse<SignInResponse> signInResponseApiResponse = post(url, signInRequest,
        new ParameterizedTypeReference<>() {
        });
    log.info("signInResponseApiResponse: {}", signInResponseApiResponse);

    if (signInResponseApiResponse.getSuccess()) {
      SignInResponse signInResponse = signInResponseApiResponse.getPayloads();
      AUTH = signInResponse.getAccessToken();
      SignedJWT signedJwt = jwtTokenService.checkToken(AUTH);
      Authentication authentication = jwtTokenService.createAuthentication(signedJwt);
      logout();
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    return signInResponseApiResponse.getSuccess();
  }

  @Override
  public ApiResponse<SignUpResponse> signUp(SignUpRequest signUpRequest) {
    String url = createUrl(AUTH_URL_SIGNUP);
    log.info("POST: {}", url);
    return ApiResponse.createSuccessResponse(SignUpResponse.builder()
        .build());
  }

  @Override
  public ApiResponse<SignUpResponse> refreshToken(RefreshTokenRequest refreshTokenRequest) {
    String url = createUrl(AUTH_URL_REFRESH_TOKEN);
    log.info("POST: {}", url);
    ResponseEntity<ApiResponse> result = restTemplateBuilder.build()
        .postForEntity(url, refreshTokenRequest, ApiResponse.class);
    return result.getBody();
  }

  @Override
  public void logout() {
    SecurityContextHolder.getContext().setAuthentication(null);
  }

  private String createUrl(String restPoint) {
    return maasApiServer + AUTH_URL + restPoint;
  }

  /**
   * get.
   *
   * @param url      url
   * @param response response
   * @param <K>      K
   * @return
   */
  public <K> ApiResponse<K> get(String url, ParameterizedTypeReference<ApiResponse<K>> response) {
    //set up the basic authentication header
    String authorizationHeader = "Bearer " + AUTH;

    //setting up the request headers
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_JSON);
    requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    requestHeaders.add("Authorization", authorizationHeader);

    //request entity is created with request body and headers
    HttpEntity requestEntity = new HttpEntity<>(null, requestHeaders);

    ResponseEntity<ApiResponse<K>> responseEntity = restTemplateBuilder.build()
        .exchange(
            url,
            HttpMethod.GET,
            requestEntity,
            response
        );

    if (responseEntity.getStatusCode() == HttpStatus.OK) {
      return responseEntity.getBody();
    } else {
      return null;
    }
  }

  /**
   * post.
   *
   * @param url      url
   * @param request  request
   * @param response response
   * @param <T>      T
   * @param <K>      K
   * @return
   */
  public <T, K> ApiResponse<K> post(String url, T request, ParameterizedTypeReference<ApiResponse<K>> response) {
    //set up the basic authentication header
    //String authorizationHeader = "Basic " + DatatypeConverter.printBase64Binary((username + ":" + password).getBytes());

    //setting up the request headers
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_JSON);
    requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    //requestHeaders.add("Authorization", authorizationHeader);

    //request entity is created with request body and headers
    HttpEntity<T> requestEntity = new HttpEntity<>(request, requestHeaders);

    ResponseEntity<ApiResponse<K>> responseEntity = restTemplateBuilder.build()
        .exchange(
            url,
            HttpMethod.POST,
            requestEntity,
            response
        );

    if (responseEntity.getStatusCode() == HttpStatus.OK) {
      return responseEntity.getBody();
    } else {
      return null;
    }
  }
}
