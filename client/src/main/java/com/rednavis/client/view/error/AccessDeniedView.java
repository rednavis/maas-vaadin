package com.rednavis.client.view.error;

import static com.rednavis.client.ConstantUtils.ACCESS_DENIED_TITLE;

import com.rednavis.client.exceptions.AccessDeniedException;
import com.rednavis.client.view.MainView;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import javax.servlet.http.HttpServletResponse;

@Tag("access-denied-view")
@JsModule("./src/view/error/access-denied-view.js")
@ParentLayout(MainView.class)
@PageTitle(ACCESS_DENIED_TITLE)
@Route
public class AccessDeniedView extends PolymerTemplate<TemplateModel> implements HasErrorParameter<AccessDeniedException> {

  @Override
  public int setErrorParameter(BeforeEnterEvent beforeEnterEvent, ErrorParameter<AccessDeniedException> errorParameter) {
    return HttpServletResponse.SC_FORBIDDEN;
  }
}
