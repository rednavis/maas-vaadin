package com.rednavis.vaadin.config.listener;

import com.rednavis.vaadin.exceptions.AccessDeniedException;
import com.rednavis.vaadin.service.AuthService;
import com.rednavis.vaadin.util.SecurityUtils;
import com.rednavis.vaadin.view.component.OfflineBanner;
import com.rednavis.vaadin.view.login.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServiceInitListener;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Adds before enter listener to check access to views. Adds the Offline banner.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MaasInitListener implements VaadinServiceInitListener {

  private final AuthService authService;

  @Override
  public void serviceInit(ServiceInitEvent event) {
    final AtomicInteger sessionsCount = new AtomicInteger(0);
    final VaadinService vaadinService = event.getSource();
    vaadinService.addSessionInitListener(
        sessionInitEvent -> {
          log.info("New Vaadin session created. Current count is: {}", sessionsCount.incrementAndGet());
          authService.signInFromCookie(sessionInitEvent.getSession());
          log.info("restoreSession - FINISH");
        });
    vaadinService.addSessionDestroyListener(
        sessionDestroyEvent -> log.info("Vaadin session destroyed. Current count is {} ", sessionsCount.decrementAndGet()));

    event.getSource().addUIInitListener(uiEvent -> {
      UI ui = uiEvent.getUI();
      ui.add(new OfflineBanner());
      ui.addBeforeEnterListener(this::beforeEnter);
    });
  }

  /**
   * Reroutes the user if she is not authorized to access the view.
   *
   * @param event before navigation event with event details
   */
  private void beforeEnter(BeforeEnterEvent event) {
    log.info("beforeEnter - START");
    boolean accessGranted = SecurityUtils.isAccessGranted(event.getNavigationTarget());
    if (!accessGranted) {
      if (SecurityUtils.isUserLoggedIn()) {
        event.rerouteToError(AccessDeniedException.class);
      } else {
        event.rerouteTo(LoginView.class);
      }
    }
  }
}