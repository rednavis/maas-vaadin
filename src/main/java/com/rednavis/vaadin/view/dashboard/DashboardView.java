package com.rednavis.vaadin.view.dashboard;

import static com.rednavis.shared.util.RoleUtils.ROLE_ADMIN;
import static com.rednavis.vaadin.util.ConstantUtils.PAGE_DASHBOARD_TITLE;
import static com.rednavis.vaadin.util.ConstantUtils.PAGE_DASHBOARD_URL;

import com.rednavis.vaadin.service.AuthService;
import com.rednavis.vaadin.view.MainView;
import com.rednavis.vaadin.view.login.LoginView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;
import java.util.Arrays;
import javax.servlet.http.Cookie;
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
    logout.addClickListener(event -> {
      authService.logout();
      Cookie[] cookies = VaadinRequest.getCurrent().getCookies();
      Arrays.stream(cookies)
          .filter(cookie -> (cookie.getName().equals("accessToken") || cookie.getName().equals("refreshToken")))
          .forEach(cookie -> {
            cookie.setValue(null);
            // By setting the cookie maxAge to 0 it will deleted immediately
            cookie.setMaxAge(0);
            VaadinService.getCurrentResponse().addCookie(cookie);
          });
      getUI().ifPresent(ui -> ui.navigate(LoginView.class));
    });
    return logout;
  }

}