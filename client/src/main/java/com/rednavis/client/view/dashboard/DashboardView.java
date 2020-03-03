package com.rednavis.client.view.dashboard;

import static com.rednavis.client.ConstantUtils.PAGE_DASHBOARD_TITLE;
import static com.rednavis.client.ConstantUtils.PAGE_DASHBOARD_URL;
import static com.rednavis.shared.util.RoleUtils.ROLE_ADMIN;

import com.rednavis.backend.service.AuthService;
import com.rednavis.client.view.MainView;
import com.rednavis.client.view.login.LoginView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@PageTitle(PAGE_DASHBOARD_TITLE)
@Route(value = PAGE_DASHBOARD_URL, layout = MainView.class)
@Secured(ROLE_ADMIN)
public class DashboardView extends Div {

  private final transient AuthService authService;

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
    add(btnCurrentUser, createLogoutLink());
  }

  private Button createLogoutLink() {
    Button logout = new Button("LOGOUT", VaadinIcon.ARROW_RIGHT.create());
    logout.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> {
      authService.logout();
      getUI().ifPresent(ui -> ui.navigate(LoginView.class));
    });
    return logout;
  }

}