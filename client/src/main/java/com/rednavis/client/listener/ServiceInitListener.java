package com.rednavis.client.listener;

import com.rednavis.client.component.OfflineBanner;
import com.rednavis.client.exceptions.AccessDeniedException;
import com.rednavis.client.util.SecurityUtils;
import com.rednavis.client.view.login.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.spring.annotation.SpringComponent;

/**
 * Adds before enter listener to check access to views. Adds the Offline banner.
 */
@SpringComponent
public class ServiceInitListener implements VaadinServiceInitListener {

  @Override
  public void serviceInit(ServiceInitEvent event) {
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
