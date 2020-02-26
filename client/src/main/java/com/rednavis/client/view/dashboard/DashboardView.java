package com.rednavis.client.view.dashboard;

import com.rednavis.backend.service.AuthService;
import com.rednavis.client.view.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Dashboard")
@Route(value = "dashboard", layout = MainView.class)
public class DashboardView extends Div {

  private final AuthService authService;

  /**
   * DashboardView.
   *
   * @param authService authService
   */
  @Autowired
  public DashboardView(AuthService authService) {
    this.authService = authService;

    Button buttonTestPost = new Button("Test post", e -> Notification.show(authService.testPost().toString()));
    buttonTestPost.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    Button buttonTestGet = new Button("Test get", e -> Notification.show(authService.testGet().toString()));
    buttonTestPost.addThemeVariants(ButtonVariant.LUMO_ERROR);
    add(buttonTestPost, buttonTestGet);
  }
}