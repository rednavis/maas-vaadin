package com.rednavis.vaadin.view.user;

import static com.rednavis.shared.util.RoleUtils.ROLE_ADMIN;
import static com.rednavis.vaadin.util.ConstantUtils.PAGE_USER_CAPTION;
import static com.rednavis.vaadin.util.ConstantUtils.PAGE_USER_TITLE;
import static com.rednavis.vaadin.util.ConstantUtils.PAGE_USER_URL;

import com.github.appreciated.app.layout.annotations.Caption;
import com.github.appreciated.app.layout.annotations.Icon;
import com.rednavis.shared.dto.user.User;
import com.rednavis.vaadin.annotation.AccessToken;
import com.rednavis.vaadin.service.UserService;
import com.rednavis.vaadin.view.MainView;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.crudui.crud.impl.GridCrud;

@Secured({ROLE_ADMIN})
@PageTitle(PAGE_USER_TITLE)
@Caption(PAGE_USER_CAPTION)
@Route(value = PAGE_USER_URL, layout = MainView.class)
@Icon(VaadinIcon.USER)
@Tag("user-view")
@JsModule("./src/view/user/user-view.js")
public class UserView extends PolymerTemplate<TemplateModel> {

  private final transient AccessToken accessToken;
  private final transient UserService userService;

  /**
   * UserView.
   *
   * @param accessToken accessToken
   * @param userService userService
   */
  public UserView(AccessToken accessToken, UserService userService) {
    this.accessToken = accessToken;
    this.userService = userService;

    prepareUI();
  }

  private void prepareUI() {
    GridCrud<User> crud = new GridCrud<>(User.class);
    Grid<User> userGrid = crud.getGrid();
    userGrid.setColumns(User.Fields.firstName, User.Fields.lastName, User.Fields.email, User.Fields.userName);
    crud.setFindAllOperation(() -> userService.findAll(accessToken.getAccessToken()));

    getElement().appendChild(crud.getElement());
  }
}
