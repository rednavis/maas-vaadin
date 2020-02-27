package com.rednavis.client.view.login;

import static com.rednavis.client.ConstantUtils.PAGE_LOGIN_TITLE;
import static com.rednavis.client.ConstantUtils.PAGE_LOGIN_URL;
import static com.rednavis.client.ConstantUtils.VIEW_PORT;

import com.rednavis.backend.service.AuthService;
import com.rednavis.client.security.SecurityUtils;
import com.rednavis.client.view.dashboard.DashboardView;
import com.rednavis.shared.rest.ApiResponse;
import com.rednavis.shared.rest.request.SignInRequest;
import com.rednavis.shared.rest.response.SignInResponse;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Theme(value = Lumo.class, variant = Lumo.LIGHT)
@PageTitle(PAGE_LOGIN_TITLE)
@Route(PAGE_LOGIN_URL)
@RouteAlias(value = "")
@Tag("login-view")
//@JsModule("./src/view/login/login-view.js")
@Viewport(VIEW_PORT)
@Slf4j
public class LoginView extends Div implements AfterNavigationObserver, BeforeEnterObserver {

  private final AuthService authService;

  private final LoginForm loginForm;

  /**
   * LoginView.
   *
   * @param authService authService
   */
  @Autowired
  public LoginView(AuthService authService) {
    this.authService = authService;

    loginForm = new LoginForm();
    loginForm.addLoginListener(login -> {
      SignInRequest signInRequest = SignInRequest.builder()
          .email(login.getUsername())
          .password(login.getPassword())
          .build();
      ApiResponse<SignInResponse> signInResponseApiResponse = authService.signIn(signInRequest);
      log.info("SignInResponse: {}", signInResponseApiResponse);
      if (signInResponseApiResponse.getSuccess()) {
        getUI().ifPresent(ui -> ui.navigate(DashboardView.class));
      } else {
        loginForm.setError(true);
      }
    });

    add(loginForm);
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    if (SecurityUtils.isUserLoggedIn()) {
      event.forwardTo(DashboardView.class);
    }
  }

  @Override
  public void afterNavigation(AfterNavigationEvent event) {
    loginForm.setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
  }
}