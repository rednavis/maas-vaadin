package com.rednavis.vaadin.view.login;

import static com.rednavis.vaadin.util.ConstantUtils.PAGE_LOGIN_TITLE;
import static com.rednavis.vaadin.util.ConstantUtils.PAGE_LOGIN_URL;
import static com.rednavis.vaadin.util.ConstantUtils.VIEW_PORT;

import com.rednavis.vaadin.dto.SignInClient;
import com.rednavis.vaadin.service.AuthService;
import com.rednavis.vaadin.util.SecurityUtils;
import com.rednavis.vaadin.view.dashboard.DashboardView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@PageTitle(PAGE_LOGIN_TITLE)
@Route(PAGE_LOGIN_URL)
@Tag("login-view")
@JsModule("./styles/shared-styles.js")
@JsModule("./src/view/login/login-view.js")
@Viewport(VIEW_PORT)
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
public class LoginView extends PolymerTemplate<TemplateModel> implements BeforeEnterObserver {

  private final transient AuthService authService;

  @Id("btnSignByFacebook")
  private Button btnSignByFacebook;
  @Id("btnSignByGoogle")
  private Button btnSignByGoogle;
  @Id("btnSignIn")
  private Button btnSignIn;
  @Id("username")
  private TextField username;
  @Id("password")
  private PasswordField password;
  private final Binder<SignInClient> signInClientBinder;
  @Id("saveUser")
  private Checkbox saveUser;

  /**
   * LoginView.
   *
   * @param authService authService
   */
  public LoginView(AuthService authService) {
    this.authService = authService;

    signInClientBinder = new Binder<>(SignInClient.class);
    signInClientBinder.setBean(new SignInClient());

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
    signInClientBinder.forField(username)
        .asRequired("Email address or username can't be empty")
        .bind(SignInClient::getUserName, SignInClient::setUserName);
    signInClientBinder.forField(password)
        .asRequired("Password can't be empty")
        .bind(SignInClient::getPassword, SignInClient::setPassword);
    signInClientBinder.forField(saveUser)
        .bind(SignInClient::isSaveUser, SignInClient::setSaveUser);
  }

  private void prepareClickHandlers() {
    btnSignByFacebook.addClickListener(event -> Notification.show("Sign with Facebook"));
    btnSignByGoogle.addClickListener(event -> Notification.show("Sign with Google"));
    btnSignIn.addClickListener(event -> {
      signInClientBinder.validate();
      if (signInClientBinder.isValid()) {
        SignInClient signInClient = signInClientBinder.getBean();
        if (authService.signIn(signInClient)) {
          getUI().ifPresent(ui -> ui.navigate(DashboardView.class));
        }
      }
    });

    username.addKeyUpListener(Key.ENTER, event -> btnSignIn.click());
    password.addKeyUpListener(Key.ENTER, event -> btnSignIn.click());
  }
}