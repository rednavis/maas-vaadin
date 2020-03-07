package com.rednavis.vaadin.service;

import static com.rednavis.vaadin.util.CookieEnum.ACCESS_TOKEN;
import static com.rednavis.vaadin.util.CookieEnum.REFRESH_TOKEN;

import com.rednavis.shared.rest.response.SignInResponse;
import com.rednavis.shared.security.CurrentUser;
import com.rednavis.vaadin.annotation.AccessToken;
import com.rednavis.vaadin.annotation.ActualUser;
import com.rednavis.vaadin.util.SecurityUtils;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@VaadinSessionScope
@RequiredArgsConstructor
public class ActualUserService {

  private final RestService restService;
  private final CookieService cookieService;

  /**
   * actualUser.
   *
   * @return
   */
  @Bean
  @VaadinSessionScope
  public ActualUser actualUser() {
    return () -> {
      // Anonymous or no authentication.
      CurrentUser currentUser = null;
      SecurityContext context = SecurityContextHolder.getContext();
      if (context != null && context.getAuthentication() != null) {
        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof CurrentUser) {
          currentUser = (CurrentUser) context.getAuthentication().getPrincipal();
        }
      }
      return currentUser;
    };
  }

  /**
   * getAccessToken.
   *
   * @return
   */
  @Bean
  @VaadinSessionScope
  public AccessToken getAccessToken() {
    return () -> {
      VaadinSession vaadinSession = VaadinSession.getCurrent();
      return (String) vaadinSession.getAttribute(ACCESS_TOKEN.name());
    };
  }

  /**
   * authenticateActualUser.
   *
   * @param signInResponse signInResponse
   * @param saveUser       saveUser
   * @return
   */
  public boolean authenticateActualUser(SignInResponse signInResponse, boolean saveUser) {
    if (signInResponse.getAccessToken().isBlank() || signInResponse.getRefreshToken().isBlank()) {
      return false;
    }
    CurrentUser currentUser = restService.getCurrenUser(signInResponse.getAccessToken());
    SecurityUtils.createAuthentication(currentUser);
    VaadinSession vaadinSession = VaadinSession.getCurrent();
    vaadinSession.setAttribute(ACCESS_TOKEN.name(), signInResponse.getAccessToken());
    setAccessToken(signInResponse.getAccessToken(), signInResponse.getAccessTokenExpiration(), saveUser);
    setRefreshToken(signInResponse.getRefreshToken(), signInResponse.getRefreshTokenExpiration(), saveUser);
    return true;
  }

  /**
   * disproveActualUser.
   */
  public void disproveActualUser() {
    VaadinSession vaadinSession = VaadinSession.getCurrent();
    vaadinSession.setAttribute(ACCESS_TOKEN.name(), null);

    cookieService.removeCookie(ACCESS_TOKEN);
    cookieService.removeCookie(REFRESH_TOKEN);
    SecurityContextHolder.getContext().setAuthentication(null);
  }

  private void setAccessToken(String accessToken, int accessTokenExpiration, boolean saveUser) {
    if (saveUser) {
      cookieService.addCookie(ACCESS_TOKEN, accessToken, accessTokenExpiration);
    } else {
      cookieService.removeCookie(ACCESS_TOKEN);
    }
  }

  private void setRefreshToken(String refreshToken, int refreshTokenExpiration, boolean saveUser) {
    if (saveUser) {
      cookieService.addCookie(REFRESH_TOKEN, refreshToken, refreshTokenExpiration);
    } else {
      cookieService.removeCookie(REFRESH_TOKEN);
    }
  }
}
