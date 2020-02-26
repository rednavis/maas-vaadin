package com.rednavis.backend.service;

import com.rednavis.shared.dto.auth.SignInRequest;
import com.rednavis.shared.dto.auth.SignInResponse;
import com.rednavis.shared.dto.auth.SignUpRequest;
import com.rednavis.shared.dto.auth.SignUpResponse;
import com.rednavis.shared.dto.auth.TestResponse;
import com.rednavis.shared.security.CurrentUser;

public interface AuthService {

  TestResponse testPost();

  TestResponse testGet();

  CurrentUser getCurrentUser();

  SignInResponse signIn(SignInRequest signInRequest);

  SignUpResponse signUp(SignUpRequest signUpRequest);
}
