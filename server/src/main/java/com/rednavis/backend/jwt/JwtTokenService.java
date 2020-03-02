package com.rednavis.backend.jwt;

import static java.time.Instant.now;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.rednavis.shared.dto.user.RoleEnum;
import com.rednavis.shared.exception.JwtException;
import com.rednavis.shared.security.CurrentUser;
import java.text.ParseException;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Configuration
public class JwtTokenService {

  @Autowired
  private JwtConfiguration jwtConfiguration;

  /**
   * checkToken.
   *
   * @param token token
   * @return
   */
  public SignedJWT checkToken(String token) {
    // Parse the JWS and verify its RSA signature
    SignedJWT signedJwt;
    try {
      signedJwt = SignedJWT.parse(token);
      JWSVerifier verifier = new MACVerifier(jwtConfiguration.getJwtAccessTokenSecret());
      if (signedJwt.verify(verifier)) {
        return signedJwt;
      } else {
        throw new JwtException("Can't verify token [token: " + token + "]");
      }
    } catch (ParseException | JOSEException e) {
      throw new JwtException("Can't parse token [token: " + token + "]");
    }
  }

  /**
   * createAuthentication.
   *
   * @param signedJwt signedJwt
   * @return
   */
  public Authentication createAuthentication(SignedJWT signedJwt) {
    if (checkExpiration(signedJwt)) {
      throw new JwtException("Current token is expired [token: " + signedJwt.serialize() + "]");
    }

    try {
      String subject = signedJwt.getJWTClaimsSet()
          .getSubject();
      String id = signedJwt.getJWTClaimsSet()
          .getStringClaim(CurrentUser.Fields.id);
      String firstName = signedJwt.getJWTClaimsSet()
          .getStringClaim(CurrentUser.Fields.firstName);
      String lastName = signedJwt.getJWTClaimsSet()
          .getStringClaim(CurrentUser.Fields.lastName);
      String roleInLine = signedJwt.getJWTClaimsSet()
          .getStringClaim(CurrentUser.Fields.roles);
      Set<RoleEnum> roles = Stream.of(roleInLine.split(","))
          .map(RoleEnum::valueOf)
          .collect(Collectors.toSet());
      CurrentUser currentUser = new CurrentUser(id, firstName, lastName, subject, roles);

      Collection<? extends GrantedAuthority> authorities = Stream.of(roleInLine.split(","))
          .map(SimpleGrantedAuthority::new)
          .collect(Collectors.toList());
      return new UsernamePasswordAuthenticationToken(currentUser, null, authorities);
    } catch (ParseException e) {
      throw new JwtException("Can't parse signedJwt [signedJwt: " + signedJwt.serialize() + "]");
    }
  }

  /**
   * checkExpiration.
   *
   * @param signedJwt signedJwt
   */
  private boolean checkExpiration(SignedJWT signedJwt) {
    try {
      Date expiration = signedJwt.getJWTClaimsSet().getExpirationTime();
      Instant expirationInstant = expiration.toInstant();
      return expirationInstant.isBefore(now());
    } catch (ParseException e) {
      throw new JwtException("Can't parse signedJwt [signedJwt: " + signedJwt.serialize() + "]");
    }
  }
}
