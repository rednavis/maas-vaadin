package com.rednavis.vaadin.config;

import static com.rednavis.vaadin.util.CookieEnum.ACCESS_TOKEN;

import com.rednavis.shared.security.CurrentUser;
import com.rednavis.vaadin.annotation.AccessToken;
import com.rednavis.vaadin.annotation.ActualUser;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@VaadinSessionScope
public class ActualUserConfig {

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
}
