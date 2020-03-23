package com.rednavis.vaadin.view.error;

import static com.rednavis.vaadin.util.ConstantUtils.PAGE_ERROR403_TITLE;
import static com.rednavis.vaadin.view.component.ErrorHttpComponent.ErrorHttpEnum.ERROR403;

import javax.servlet.http.HttpServletResponse;
import com.rednavis.vaadin.exceptions.ForbiddenError;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@PageTitle(PAGE_ERROR403_TITLE)
@Tag("error-view")
@JsModule("./src/view/error/error-view.js")
public class Error403View extends PolymerTemplate<TemplateModel> implements HasErrorParameter<ForbiddenError> {

  public Error403View(InfoProperty infoProperty) {
    ErrorHttpComponent errorHttpComponent = new ErrorHttpComponent(ERROR403, infoProperty.getRednavis().getEmail());
    getElement().appendChild(errorHttpComponent.getElement());
  }

  @Override
  public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<ForbiddenError> parameter) {
    log.error(ERROR403.name(), parameter.getException());
    return HttpServletResponse.SC_FORBIDDEN;
  }

}
