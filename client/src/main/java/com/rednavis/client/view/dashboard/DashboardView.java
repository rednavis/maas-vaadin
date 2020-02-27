package com.rednavis.client.view.dashboard;

import static com.rednavis.client.ConstantUtils.PAGE_DASHBOARD_TITLE;
import static com.rednavis.client.ConstantUtils.PAGE_DASHBOARD_URL;

import com.rednavis.backend.service.AuthService;
import com.rednavis.client.view.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@PageTitle(PAGE_DASHBOARD_TITLE)
@Route(value = PAGE_DASHBOARD_URL, layout = MainView.class)
@Secured("ADMIN")
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

    Button btnCurrentUser = new Button("Current User", e -> Notification.show(authService.getCurrentUser().toString()));
    btnCurrentUser.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    add(btnCurrentUser);
  }
}