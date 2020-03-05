package com.rednavis.vaadin.view.login;

import static com.rednavis.vaadin.util.ConstantUtils.PAGE_LOGIN_TITLE;
import static com.rednavis.vaadin.util.ConstantUtils.PAGE_LOGIN_URL;
import static com.rednavis.vaadin.util.ConstantUtils.VIEW_PORT;

import com.rednavis.shared.rest.ApiResponse;
import com.rednavis.shared.rest.request.SignInRequest;
import com.rednavis.shared.rest.response.SignInResponse;
import com.rednavis.vaadin.service.AuthService;
import com.rednavis.vaadin.util.SecurityUtils;
import com.rednavis.vaadin.view.dashboard.DashboardView;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import javax.servlet.http.Cookie;

@PageTitle(PAGE_LOGIN_TITLE)
@Route(PAGE_LOGIN_URL)
@RouteAlias("")
@Tag("login-view")
@JsModule("./styles/shared-styles.js")
@JsModule("./src/view/login/login-view.js")
@Viewport(VIEW_PORT)
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
public class LoginView extends PolymerTemplate<TemplateModel> implements BeforeEnterObserver {

  private final AuthService authService;

  @Id("btnSignByFacebook")
  private Button btnSignByFacebook;
  @Id("btnSignByGoogle")
  private Button btnSignByGoogle;
  @Id("btnSignIn")
  private Button btnSignIn;
  @Id("username")
  private EmailField username;
  @Id("password")
  private PasswordField password;

  private final Binder<SignInRequest> signInRequestBinder;

  /**
   * LoginView.
   *
   * @param authService authService
   */
  public LoginView(AuthService authService) {
    this.authService = authService;

    signInRequestBinder = new Binder<>();
    prepareBinder();
    prepareClickHandlers();
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    if (SecurityUtils.isUserLoggedIn()) {
      event.forwardTo(DashboardView.class);
    }
  }

  private void prepareBinder() {
    signInRequestBinder.forField(username)
        .asRequired("Email address can't be empty")
        .bind(SignInRequest::getEmail, SignInRequest::setEmail);
    signInRequestBinder.forField(password)
        .asRequired("Password can't be empty")
        .bind(SignInRequest::getPassword, SignInRequest::setPassword);
  }

  private void prepareClickHandlers() {
    btnSignByFacebook.addClickListener(event -> Notification.show("Sign with Facebook"));
    btnSignByGoogle.addClickListener(event -> Notification.show("Sign with Google"));
    btnSignIn.addClickListener(event -> {
      if (signInRequestBinder.validate().isOk()) {
        SignInRequest signInRequest = signInRequestBinder.getBean();
        ApiResponse<SignInResponse> signInResponseApiResponse = authService.signIn(signInRequest);
        if (signInResponseApiResponse.isSuccess()) {
          SignInResponse signInResponse = signInResponseApiResponse.getPayloads();
          Cookie assessCookie = new Cookie("accessToken", signInResponse.getAccessToken());
          assessCookie.setMaxAge(signInResponse.getAccessTokenExpiration()); // define after how many *seconds* the cookie should expire
          assessCookie.setPath("/"); // single slash means the cookie is set for your whole application.
          VaadinService.getCurrentResponse().addCookie(assessCookie);
          Cookie refreshCookie = new Cookie("refreshToken", signInResponse.getRefreshToken());
          refreshCookie.setMaxAge(signInResponse.getRefreshTokenExpiration()); // define after how many *seconds* the cookie should expire
          refreshCookie.setPath("/"); // single slash means the cookie is set for your whole application.
          VaadinService.getCurrentResponse().addCookie(refreshCookie);
          getUI().ifPresent(ui -> ui.navigate(DashboardView.class));
        } else {
          Notification.show("Error");
        }
      }
    });
  }
}