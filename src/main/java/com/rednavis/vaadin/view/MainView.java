package com.rednavis.vaadin.view;

import static com.rednavis.vaadin.util.ConstantUtils.PAGE_LOGIN_URL;
import static com.rednavis.vaadin.view.component.ImageResource.REDNAVIS_LOGO;
import static javax.swing.ScrollPaneConstants.VIEWPORT;

import com.github.appreciated.app.layout.addons.notification.DefaultNotificationHolder;
import com.github.appreciated.app.layout.addons.notification.component.NotificationButton;
import com.github.appreciated.app.layout.component.appbar.AppBarBuilder;
import com.github.appreciated.app.layout.component.applayout.LeftLayouts;
import com.github.appreciated.app.layout.component.builder.AppLayoutBuilder;
import com.github.appreciated.app.layout.component.menu.left.builder.LeftAppMenuBuilder;
import com.github.appreciated.app.layout.component.menu.left.items.LeftNavigationItem;
import com.github.appreciated.app.layout.component.router.AppLayoutRouterLayout;
import com.github.appreciated.app.layout.entity.DefaultBadgeHolder;
import com.rednavis.shared.dto.user.RoleEnum;
import com.rednavis.shared.security.CurrentUser;
import com.rednavis.vaadin.annotation.ActualUser;
import com.rednavis.vaadin.view.book.BookView;
import com.rednavis.vaadin.view.dashboard.DashboardView;
import com.rednavis.vaadin.view.user.UserView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import lombok.Getter;

/**
 * The main view contains a button and a click listener.
 */
@Viewport(VIEWPORT)
@PWA(name = "MAAS API Client", shortName = "MAAS Vaadin",
    startPath = PAGE_LOGIN_URL,
    backgroundColor = "#227aef", themeColor = "#227aef",
    offlinePath = "offline-page.html",
    offlineResources = {"images/offline-login-banner.jpg"})
@JsModule("./styles/shared-styles.js")
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
public class MainView extends AppLayoutRouterLayout<LeftLayouts.LeftResponsive> {

  private final transient ActualUser actualUser;

  @Getter
  private final DefaultNotificationHolder notificationHolder;
  @Getter
  private final DefaultBadgeHolder badgeHolder;

  /**
   * MainView.
   *
   * @param actualUser actualUser
   */
  public MainView(ActualUser actualUser) {
    this.actualUser = actualUser;

    notificationHolder = new DefaultNotificationHolder();
    badgeHolder = new DefaultBadgeHolder(5);

    prepareListener();
    prepareUI();
  }

  private void prepareListener() {
    notificationHolder.addClickListener(notification -> {
    });
  }

  private void prepareUI() {
    init(AppLayoutBuilder.get(LeftLayouts.LeftResponsive.class)
        .withIconComponent(REDNAVIS_LOGO.create())
        .withTitle("Mass Vaadin")
        .withAppBar(prepareAppBar())
        .withAppMenu(prepareAppMenu())
        .build());
  }

  private Component prepareAppBar() {
    return AppBarBuilder.get()
        .add(new NotificationButton<>(VaadinIcon.BELL, notificationHolder))
        .build();
  }

  private Component prepareAppMenu() {
    LeftNavigationItem menuEntry = new LeftNavigationItem("Menu", VaadinIcon.MENU.create(), UserView.class);
    badgeHolder.bind(menuEntry.getBadge());

    CurrentUser currentUser = actualUser.getCurrentUser();

    LeftAppMenuBuilder leftAppMenuBuilder = LeftAppMenuBuilder.get();
    if (currentUser.getRoles().contains(RoleEnum.ROLE_ADMIN)) {
      leftAppMenuBuilder.add(new LeftNavigationItem(UserView.class));
    }
    if (currentUser.getRoles().contains(RoleEnum.ROLE_USER)) {
      leftAppMenuBuilder.add(new LeftNavigationItem(BookView.class));
    }
    leftAppMenuBuilder.add(new LeftNavigationItem("Home", VaadinIcon.DASHBOARD.create(), DashboardView.class));
    leftAppMenuBuilder.add(menuEntry);
    return leftAppMenuBuilder.build();
  }
}
