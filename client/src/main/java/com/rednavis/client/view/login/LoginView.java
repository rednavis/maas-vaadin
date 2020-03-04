package com.rednavis.client.view.login;

import static com.rednavis.client.ConstantUtils.PAGE_LOGIN_TITLE;
import static com.rednavis.client.ConstantUtils.PAGE_LOGIN_URL;
import static com.rednavis.client.ConstantUtils.VIEW_PORT;

import com.rednavis.backend.service.AuthService;
import com.rednavis.client.util.SecurityUtils;
import com.rednavis.client.view.dashboard.DashboardView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.templatemodel.TemplateModel;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Theme(value = Lumo.class, variant = Lumo.LIGHT)
@PageTitle(PAGE_LOGIN_TITLE)
@Route(PAGE_LOGIN_URL)
@RouteAlias("")
@Tag("login-view")
@JsModule("./styles/shared-styles.js")
@JsModule("./src/view/login/login-view.js")
@Viewport(VIEW_PORT)
public class LoginView extends PolymerTemplate<TemplateModel> implements BeforeEnterObserver {

  private final AuthService authService;

  @Id("btnSignByFacebook")
  private Button btnSignByFacebook;
  @Id("btnSignByGoogle")
  private Button btnSignByGoogle;
  @Id("btnSignIn")
  private Button btnSignIn;

  public LoginView(AuthService authService) {
    this.authService = authService;

    prepareClickHandlers();
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    if (SecurityUtils.isUserLoggedIn()) {
      event.forwardTo(DashboardView.class);
    }
  }

  private void prepareClickHandlers() {
    btnSignByFacebook.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> Notification.show("Sign with Facebook"));
    btnSignByGoogle.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> Notification.show("Sign with Google"));
    btnSignIn.addClickListener((ComponentEventListener<ClickEvent<Button>>) event -> Notification.show("Sign with Email"));
  }
}