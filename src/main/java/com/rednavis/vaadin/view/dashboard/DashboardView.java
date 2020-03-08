package com.rednavis.vaadin.view.dashboard;

import static com.rednavis.shared.util.RoleUtils.ROLE_ADMIN;
import static com.rednavis.shared.util.RoleUtils.ROLE_USER;
import static com.rednavis.vaadin.util.ConstantUtils.PAGE_DASHBOARD_TITLE;
import static com.rednavis.vaadin.util.ConstantUtils.PAGE_DASHBOARD_URL;
import static java.time.Instant.now;

import com.rednavis.vaadin.annotation.AccessToken;
import com.rednavis.vaadin.annotation.ActualUser;
import com.rednavis.vaadin.service.AuthService;
import com.rednavis.vaadin.view.MainView;
import com.rednavis.vaadin.view.login.LoginView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

@Secured({ROLE_ADMIN, ROLE_USER})
@PageTitle(PAGE_DASHBOARD_TITLE)
@Route(value = PAGE_DASHBOARD_URL, layout = MainView.class)
public class DashboardView extends Div {

  private final transient AuthService authService;

  /**
   * DashboardView.
   *
   * @param actualUser  actualUser
   * @param accessToken accessToken
   * @param authService authService
   */
  @Autowired
  public DashboardView(ActualUser actualUser, AccessToken accessToken, AuthService authService) {
    this.authService = authService;

    add(new H2(actualUser.getCurrentUser().toString()),
        new H2(accessToken.getAccessToken()),
        new H2(DateTimeFormatter.ISO_INSTANT.format(now())),
        createLogoutLink());
  }

  private Button createLogoutLink() {
    Button logout = new Button("LOGOUT", VaadinIcon.ARROW_RIGHT.create());
    logout.addClickListener(event -> {
      authService.signOut();
      getUI().ifPresent(ui -> ui.navigate(LoginView.class));
    });
    return logout;
  }
}