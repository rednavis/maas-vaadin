package com.rednavis.vaadin.config.listener;

import static com.rednavis.vaadin.util.CookieEnum.ACCESS_TOKEN;

import com.rednavis.shared.security.CurrentUser;
import com.rednavis.vaadin.exceptions.AccessDeniedException;
import com.rednavis.vaadin.service.CookieService;
import com.rednavis.vaadin.service.RestService;
import com.rednavis.vaadin.util.SecurityUtils;
import com.rednavis.vaadin.view.component.OfflineBanner;
import com.rednavis.vaadin.view.login.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.http.Cookie;
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

  private final CookieService cookieService;
  private final RestService restService;

  @Override
  public void serviceInit(ServiceInitEvent event) {
    final AtomicInteger sessionsCount = new AtomicInteger(0);
    final VaadinService vaadinService = event.getSource();
    vaadinService.addSessionInitListener(sessionInitEvent -> {
      log.info("New Vaadin session created. Current count is: {}", sessionsCount.incrementAndGet());
      Cookie accessCookie = cookieService.getCookie(ACCESS_TOKEN);
      if (accessCookie != null) {
        log.info("AccessCookies exists");
        CurrentUser currentUser = restService.getCurrenUser(accessCookie.getValue());
        SecurityUtils.createAuthentication(currentUser);
        VaadinSession vaadinSession = sessionInitEvent.getSession();
        vaadinSession.setAttribute(ACCESS_TOKEN.name(), accessCookie.getValue());
      }
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