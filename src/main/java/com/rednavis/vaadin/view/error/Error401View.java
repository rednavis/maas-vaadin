package com.rednavis.vaadin.view.error;

import static com.rednavis.vaadin.util.ConstantUtils.PAGE_ERROR401_TITLE;
import static com.rednavis.vaadin.view.component.ErrorHttpComponent.ErrorHttpEnum.ERROR401;

import com.rednavis.vaadin.exceptions.UnauthorizedError;
import com.rednavis.vaadin.property.InfoProperty;
import com.rednavis.vaadin.view.component.ErrorHttpComponent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.templatemodel.TemplateModel;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@PageTitle(PAGE_ERROR401_TITLE)
@Tag("error-view")
@JsModule("./src/view/error/error-view.js")
public class Error401View extends PolymerTemplate<TemplateModel> implements HasErrorParameter<UnauthorizedError> {

  public Error401View(InfoProperty infoProperty) {
    ErrorHttpComponent errorHttpComponent = new ErrorHttpComponent(ERROR401, infoProperty.getRednavis().getEmail());
    getElement().appendChild(errorHttpComponent.getElement());
  }

  @Override
  public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<UnauthorizedError> parameter) {
    log.error(ERROR401.name(), parameter.getException());
    return HttpServletResponse.SC_UNAUTHORIZED;
  }

}
