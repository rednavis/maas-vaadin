package com.rednavis.vaadin.service;

import com.rednavis.shared.rest.ApiResponse;
import com.rednavis.shared.rest.request.RefreshTokenRequest;
import com.rednavis.shared.rest.request.SignInRequest;
import com.rednavis.shared.rest.request.SignUpRequest;
import com.rednavis.shared.rest.response.SignInResponse;
import com.rednavis.shared.rest.response.SignUpResponse;
import com.rednavis.shared.security.CurrentUser;

public interface AuthService {

  CurrentUser getCurrentUser();

  ApiResponse<SignInResponse> signIn(SignInRequest signInRequest);

  ApiResponse<SignUpResponse> signUp(SignUpRequest signUpRequest);

  ApiResponse<SignUpResponse> refreshToken(RefreshTokenRequest refreshTokenRequest);

  void logout();
}
