package com.rednavis.vaadin.config;

import com.rednavis.vaadin.annotation.AccessToken;
import com.rednavis.vaadin.annotation.ActualUser;
import com.rednavis.vaadin.util.SecurityUtils;
import com.rednavis.vaadin.util.SessionUtils;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

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
    return () -> SecurityUtils.getCurrentUser();
  }

  /**
   * getAccessToken.
   *
   * @return
   */
  @Bean
  @VaadinSessionScope
  public AccessToken getAccessToken() {
    return () -> SessionUtils.getAccessToken(VaadinSession.getCurrent());
  }
}
