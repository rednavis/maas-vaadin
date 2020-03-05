package com.rednavis.vaadin.view;

import static com.rednavis.vaadin.util.ConstantUtils.PAGE_LOGIN_URL;
import static com.rednavis.vaadin.util.ConstantUtils.VIEW_PORT;

import com.rednavis.vaadin.view.dashboard.DashboardView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import java.util.ArrayList;
import java.util.List;

/**
 * The main view contains a button and a click listener.
 */
@PWA(name = "MAAS Vaadin",
    shortName = "MAAS Vaadin",
    startPath = PAGE_LOGIN_URL,
    backgroundColor = "#227aef",
    themeColor = "#227aef",
    offlinePath = "offline-page.html",
    offlineResources = {"images/offline-login-banner.jpg"})
@JsModule("./styles/shared-styles.js")
@Viewport(VIEW_PORT)
@Theme(value = Lumo.class, variant = Lumo.LIGHT)
public class MainView extends AppLayout {

  private final Tabs menu;

  public MainView() {
    menu = createMenuTabs();
    addToNavbar(menu);
  }

  private static Tabs createMenuTabs() {
    final Tabs tabs = new Tabs();
    tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
    tabs.add(getAvailableTabs());
    return tabs;
  }

  private static Tab[] getAvailableTabs() {
    final List<Tab> tabs = new ArrayList<>();
    tabs.add(createTab("Dashboard", DashboardView.class));
    return tabs.toArray(new Tab[tabs.size()]);
  }

  private static Tab createTab(String title, Class<? extends Component> viewClass) {
    return createTab(populateLink(new RouterLink(null, viewClass), title));
  }

  private static Tab createTab(Component content) {
    final Tab tab = new Tab();
    tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
    tab.add(content);
    return tab;
  }

  private static <T extends HasComponents> T populateLink(T a, String title) {
    a.add(title);
    return a;
  }
}
