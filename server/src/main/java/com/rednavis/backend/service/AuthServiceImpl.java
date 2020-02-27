package com.rednavis.backend.service;

import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_CURRENTUSER;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_SIGNIN;

import com.rednavis.shared.rest.ApiResponse;
import com.rednavis.shared.rest.request.SignInRequest;
import com.rednavis.shared.rest.request.SignUpRequest;
import com.rednavis.shared.rest.response.SignInResponse;
import com.rednavis.shared.rest.response.SignUpResponse;
import com.rednavis.shared.security.CurrentUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

  @Value("${maas.api.server}")
  private String maasApiServer;

  @Autowired
  private RestTemplateBuilder restTemplateBuilder;

  @Override
  public ApiResponse<CurrentUser> getCurrentUser() {
    String url = createUrl(AUTH_URL_CURRENTUSER);
    log.info("GET: {}", url);
    ResponseEntity<ApiResponse> result = restTemplateBuilder.build()
        .getForEntity(url, ApiResponse.class);
    return result.getBody();
  }

  @Override
  public ApiResponse<SignInResponse> signIn(SignInRequest signInRequest) {
    String url = createUrl(AUTH_URL_SIGNIN);
    log.info("POST: {}", url);
    ResponseEntity<ApiResponse> result = restTemplateBuilder.build()
        .postForEntity(url, signInRequest, ApiResponse.class);
    return result.getBody();
  }

  @Override
  public ApiResponse<SignUpResponse> signUp(SignUpRequest signUpRequest) {
    return ApiResponse.createSuccessResponse(SignUpResponse.builder()
        .build());
  }

  private String createUrl(String restPoint) {
    return maasApiServer + AUTH_URL + restPoint;
  }
}
