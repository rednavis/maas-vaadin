package com.rednavis.backend.jwt;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
class JwtConfiguration {

  @Value("${jwt.accessToken.secretKey}")
  private String jwtAccessTokenSecret;
}
