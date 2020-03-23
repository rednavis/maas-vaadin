package com.rednavis.vaadin.config.listener;

import java.util.concurrent.atomic.AtomicInteger;
import com.rednavis.vaadin.exceptions.ForbiddenError;
import com.rednavis.vaadin.service.AuthService;
import com.rednavis.vaadin.util.SecurityUtils;
import com.rednavis.vaadin.view.component.OfflineBanner;
import com.rednavis.vaadin.view.login.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.VaadinServletService;
import com.vaadin.flow.server.VaadinSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Adds before enter listener to check access to views. Adds the Offline banner.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MaasVaadinInitListener implements VaadinServiceInitListener {

  private final transient AuthService authService;

  @Override
  public void serviceInit(ServiceInitEvent event) {
    final AtomicInteger sessionsCount = new AtomicInteger(0);

    //Strange behaviour
    //https://vaadin.com/forum/thread/17604535/com-vaadin-flow-server-vaadinserviceinitlistener
    //https://github.com/vaadin/spring/issues/531
    //https://github.com/vaadin/flow/issues/6454
    final VaadinServletService servletService = (VaadinServletService) event.getSource();
    final VaadinServlet vaadinServlet = servletService.getServlet();
    log.info("Service init for {} of type {} with id {}", vaadinServlet.getServletName(), vaadinServlet.getClass().getName(),
        System.identityHashCode(vaadinServlet));

    final VaadinService vaadinService = event.getSource();
    vaadinService.addSessionInitListener(
        sessionInitEvent -> {
          log.info("New Vaadin session created. Current count is: {}", sessionsCount.incrementAndGet());
          VaadinSession vaadinSession = sessionInitEvent.getSession();
          vaadinSession.setErrorHandler(errorEvent -> {
            log.error("VaadinSession ErrorHandler - {}", errorEvent.getThrowable().getMessage(), errorEvent.getThrowable());
            Notification.show("VaadinSession ErrorHandler - " + errorEvent.getThrowable().getMessage());
          });
          authService.signInFromCookie(vaadinSession);
          log.info("restoreSession - FINISH");
        });
    vaadinService.addSessionDestroyListener(
        sessionDestroyEvent -> log.info("Vaadin session destroyed. Current count is {} ", sessionsCount.decrementAndGet()));

    event.getSource().addUIInitListener(uiInitEvent -> {
      UI ui = uiInitEvent.getUI();
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
        event.rerouteToError(ForbiddenError.class);
      } else {
        event.rerouteTo(LoginView.class);
      }
    }
    log.info("beforeEnter - FINISH");
  }
}