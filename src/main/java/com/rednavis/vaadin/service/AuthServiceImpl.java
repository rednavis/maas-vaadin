package com.rednavis.vaadin.service;

import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_CURRENTUSER;
import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL_SIGNIN;
import static com.rednavis.vaadin.util.RestUtils.auth;

import com.rednavis.shared.rest.ApiResponse;
import com.rednavis.shared.rest.request.RefreshTokenRequest;
import com.rednavis.shared.rest.request.SignInRequest;
import com.rednavis.shared.rest.request.SignUpRequest;
import com.rednavis.shared.rest.response.SignInResponse;
import com.rednavis.shared.rest.response.SignUpResponse;
import com.rednavis.shared.security.CurrentUser;
import com.rednavis.vaadin.util.RestUtils;
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

  private final RestUtils restUtils;

  @Override
  public CurrentUser getCurrentUser() {
    String url = restUtils.createAuthUrl(AUTH_URL_CURRENTUSER);
    ApiResponse<CurrentUser> currentUserApiResponse = restUtils.get(url, new ParameterizedTypeReference<>() {
    }, true);
    return currentUserApiResponse.getPayloads();
  }

  @Override
  public ApiResponse<SignInResponse> signIn(SignInRequest signInRequest) {
    String url = restUtils.createAuthUrl(AUTH_URL_SIGNIN);
    ApiResponse<SignInResponse> signInResponseApiResponse = restUtils.post(url, signInRequest, new ParameterizedTypeReference<>() {
    }, false);
    if (signInResponseApiResponse.isSuccess()) {
      SignInResponse signInResponse = signInResponseApiResponse.getPayloads();
      auth = signInResponse.getAccessToken();
      CurrentUser currentUser = getCurrentUser();
      Authentication authentication = createAuthentication(currentUser);
      logout();
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    return signInResponseApiResponse;
  }

  @Override
  public ApiResponse<SignUpResponse> signUp(SignUpRequest signUpRequest) {
    return ApiResponse.createSuccessResponse(SignUpResponse.builder()
        .build());
  }

  @Override
  public ApiResponse<SignUpResponse> refreshToken(RefreshTokenRequest refreshTokenRequest) {
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
