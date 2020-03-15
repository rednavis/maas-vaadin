package com.rednavis.vaadin.view.dashboard;

import static com.rednavis.shared.util.RoleUtils.ROLE_ADMIN;
import static com.rednavis.shared.util.RoleUtils.ROLE_USER;
import static com.rednavis.vaadin.util.ConstantUtils.PAGE_DASHBOARD_TITLE;
import static com.rednavis.vaadin.util.ConstantUtils.PAGE_DASHBOARD_URL;
import static java.time.Instant.now;

import com.rednavis.vaadin.annotation.AccessToken;
import com.rednavis.vaadin.annotation.ActualUser;
import com.rednavis.vaadin.service.AuthService;
import com.rednavis.vaadin.service.UserService;
import com.rednavis.vaadin.view.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
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
  private final transient UserService userService;
  private final transient AccessToken accessToken;

  /**
   * DashboardView.
   *
   * @param actualUser  actualUser
   * @param accessToken accessToken //* @param authService authService
   */
  @Autowired
  public DashboardView(ActualUser actualUser, AccessToken accessToken, AuthService authService, UserService userService) {
    this.authService = authService;
    this.userService = userService;
    this.accessToken = accessToken;

    VerticalLayout verticalLayout = new VerticalLayout(new H2(actualUser.getCurrentUser().toString()),
        new TextArea("accessToken", accessToken.getAccessToken(), "empty token"),
        new H2(DateTimeFormatter.ISO_INSTANT.format(now())),
        createUserButton(),
        createAdminButton(),
        createLogoutButton());
    add(verticalLayout);
  }

  private Button createUserButton() {
    return new Button("User", event -> Notification.show("111 = " + userService.user(accessToken.getAccessToken())));
  }

  private Button createAdminButton() {
    return new Button("Admin", event -> Notification.show("222 = " + userService.admin(accessToken.getAccessToken())));
  }

  private Button createLogoutButton() {
    Button logout = new Button("LOGOUT", VaadinIcon.ARROW_RIGHT.create());
    logout.addClickListener(event -> authService.signOut());
    return logout;
  }
}