package com.rednavis.backend.service;

import com.rednavis.shared.dto.auth.TestResponse;

public interface AuthService {

  TestResponse testPost();

  TestResponse testGet();
}
