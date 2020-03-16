package com.rednavis.vaadin.view.user;

import static com.rednavis.shared.util.RoleUtils.ROLE_ADMIN;
import static com.rednavis.vaadin.util.ConstantUtils.PAGE_USER_CAPTION;
import static com.rednavis.vaadin.util.ConstantUtils.PAGE_USER_TITLE;
import static com.rednavis.vaadin.util.ConstantUtils.PAGE_USER_URL;

import com.github.appreciated.app.layout.annotations.Caption;
import com.github.appreciated.app.layout.annotations.Icon;
import com.rednavis.vaadin.view.MainView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

@Secured({ROLE_ADMIN})
@PageTitle(PAGE_USER_TITLE)
@Caption(PAGE_USER_CAPTION)
@Route(value = PAGE_USER_URL, layout = MainView.class)
@Icon(VaadinIcon.USER)
public class UserView extends Div {

}
