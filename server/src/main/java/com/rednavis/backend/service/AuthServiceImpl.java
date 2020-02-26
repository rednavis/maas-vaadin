package com.rednavis.backend.service;

import static com.rednavis.shared.RestUrlUtils.AUTH_URL;
import static com.rednavis.shared.RestUrlUtils.AUTH_URL_CURRENTUSER;
import static com.rednavis.shared.RestUrlUtils.AUTH_URL_SIGNIN;
import static com.rednavis.shared.RestUrlUtils.AUTH_URL_TEST_GET;
import static com.rednavis.shared.RestUrlUtils.AUTH_URL_TEST_POST;

import com.rednavis.shared.dto.auth.SignInRequest;
import com.rednavis.shared.dto.auth.SignInResponse;
import com.rednavis.shared.dto.auth.SignUpRequest;
import com.rednavis.shared.dto.auth.SignUpResponse;
import com.rednavis.shared.dto.auth.TestRequest;
import com.rednavis.shared.dto.auth.TestResponse;
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
  public TestResponse testPost() {
    TestRequest testRequest = TestRequest.builder()
        .valueInput("Hello World!")
        .build();

    String url = createUrl(AUTH_URL_TEST_POST);
    log.info("POST: {}", url);
    ResponseEntity<TestResponse> result = restTemplateBuilder.build()
        .postForEntity(url, testRequest, TestResponse.class);
    return result.getBody();
  }

  @Override
  public TestResponse testGet() {
    String url = createUrl(AUTH_URL_TEST_GET);
    log.info("GET: {}", url);
    ResponseEntity<TestResponse> result = restTemplateBuilder.build()
        .getForEntity(url, TestResponse.class);
    return result.getBody();
  }

  @Override
  public CurrentUser getCurrentUser() {
    String url = createUrl(AUTH_URL_CURRENTUSER);
    log.info("GET: {}", url);
    ResponseEntity<CurrentUser> result = restTemplateBuilder.build()
        .getForEntity(url, CurrentUser.class);
    return result.getBody();
  }

  @Override
  public SignInResponse signIn(SignInRequest signInRequest) {
    String url = createUrl(AUTH_URL_SIGNIN);
    log.info("POST: {}", url);
    ResponseEntity<SignInResponse> result = restTemplateBuilder.build()
        .postForEntity(url, signInRequest, SignInResponse.class);
    return result.getBody();
  }

  @Override
  public SignUpResponse signUp(SignUpRequest signUpRequest) {
    return SignUpResponse.builder()
        .build();
  }

  private String createUrl(String restPoint) {
    return maasApiServer + AUTH_URL + restPoint;
  }
}
