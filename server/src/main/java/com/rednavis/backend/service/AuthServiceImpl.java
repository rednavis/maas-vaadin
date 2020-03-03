package com.rednavis.backend.service;

import static com.rednavis.backend.util.RestUtil.AUTH;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_CURRENTUSER;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_REFRESH_TOKEN;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_SIGNIN;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_SIGNUP;

import com.nimbusds.jwt.SignedJWT;
import com.rednavis.backend.jwt.JwtTokenService;
import com.rednavis.backend.util.RestUtil;
import com.rednavis.shared.rest.ApiResponse;
import com.rednavis.shared.rest.request.RefreshTokenRequest;
import com.rednavis.shared.rest.request.SignInRequest;
import com.rednavis.shared.rest.request.SignUpRequest;
import com.rednavis.shared.rest.response.SignInResponse;
import com.rednavis.shared.rest.response.SignUpResponse;
import com.rednavis.shared.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  @Autowired
  private JwtTokenService jwtTokenService;
  @Autowired
  private RestUtil restUtil;

  @Override
  public CurrentUser getCurrentUser() {
    String url = restUtil.createAuthUrl(AUTH_URL_CURRENTUSER);
    ApiResponse<CurrentUser> currentUserApiResponse = restUtil.get(url, new ParameterizedTypeReference<>() {
    }, true);
    return currentUserApiResponse.getPayloads();
  }

  @Override
  public boolean signIn(SignInRequest signInRequest) {
    String url = restUtil.createAuthUrl(AUTH_URL_SIGNIN);
    ApiResponse<SignInResponse> signInResponseApiResponse = restUtil.post(url, signInRequest, new ParameterizedTypeReference<>() {
    }, false);
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
    String url = restUtil.createAuthUrl(AUTH_URL_SIGNUP);
    return ApiResponse.createSuccessResponse(SignUpResponse.builder()
        .build());
  }

  @Override
  public ApiResponse<SignUpResponse> refreshToken(RefreshTokenRequest refreshTokenRequest) {
    String url = restUtil.createAuthUrl(AUTH_URL_REFRESH_TOKEN);
    return ApiResponse.createSuccessResponse(SignUpResponse.builder()
        .build());
  }

  @Override
  public void logout() {
    SecurityContextHolder.getContext().setAuthentication(null);
  }
}
