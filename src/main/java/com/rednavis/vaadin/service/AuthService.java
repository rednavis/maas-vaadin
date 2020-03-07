package com.rednavis.vaadin.service;

import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_SIGNIN;

import com.rednavis.shared.rest.ApiResponse;
import com.rednavis.shared.rest.request.RefreshTokenRequest;
import com.rednavis.shared.rest.request.SignInRequest;
import com.rednavis.shared.rest.request.SignUpRequest;
import com.rednavis.shared.rest.response.SignInResponse;
import com.rednavis.shared.rest.response.SignUpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

  private final RestService restService;

  /**
   * signIn.
   *
   * @param signInRequest signInRequest
   * @return
   */
  public SignInResponse signIn(SignInRequest signInRequest) {
    log.info("signIn [signInRequest: {}]", signInRequest);
    String url = restService.createAuthUrl(AUTH_URL_SIGNIN);
    ApiResponse<SignInResponse> signInResponseApiResponse = restService.post(url, signInRequest, SignInResponse.class);
    return (signInResponseApiResponse.isSuccess()) ? signInResponseApiResponse.getPayloads() : SignInResponse.builder().build();
  }

  public ApiResponse<SignUpResponse> signUp(SignUpRequest signUpRequest) {
    return null;
  }

  public ApiResponse<SignUpResponse> refreshToken(RefreshTokenRequest refreshTokenRequest) {
    return null;
  }
}
