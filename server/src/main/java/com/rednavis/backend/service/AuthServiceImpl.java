package com.rednavis.backend.service;

import static com.rednavis.backend.util.RestUtil.AUTH;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_CURRENTUSER;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_REFRESH_TOKEN;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_SIGNIN;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_SIGNUP;

import com.rednavis.backend.util.RestUtil;
import com.rednavis.shared.rest.ApiResponse;
import com.rednavis.shared.rest.request.RefreshTokenRequest;
import com.rednavis.shared.rest.request.SignInRequest;
import com.rednavis.shared.rest.request.SignUpRequest;
import com.rednavis.shared.rest.response.SignInResponse;
import com.rednavis.shared.rest.response.SignUpResponse;
import com.rednavis.shared.security.CurrentUser;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final RestUtil restUtil;

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
      CurrentUser currentUser = getCurrentUser();
      Authentication authentication = createAuthentication(currentUser);
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

  /**
   * createAuthentication.
   *
   * @param currentUser currentUser
   * @return
   */
  public Authentication createAuthentication(CurrentUser currentUser) {
    Collection<? extends GrantedAuthority> authorities = currentUser.getRoles()
        .stream()
        .map(role -> new SimpleGrantedAuthority(role.name()))
        .collect(Collectors.toList());
    return new UsernamePasswordAuthenticationToken(currentUser, null, authorities);
  }
}
