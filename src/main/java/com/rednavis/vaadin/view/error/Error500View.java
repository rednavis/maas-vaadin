package com.rednavis.vaadin.view.error;

import static com.rednavis.vaadin.util.ConstantUtils.PAGE_ERROR500_TITLE;
import static com.rednavis.vaadin.view.component.ErrorHttpComponent.ErrorHttpEnum.ERROR500;

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
@PageTitle(PAGE_ERROR500_TITLE)
@Tag("error-view")
@JsModule("./src/view/error/error-view.js")
public class Error500View extends PolymerTemplate<TemplateModel> implements HasErrorParameter<Exception> {

  public Error500View(InfoProperty infoProperty) {
    ErrorHttpComponent errorHttpComponent = new ErrorHttpComponent(ERROR500, infoProperty.getRednavis().getEmail());
    getElement().appendChild(errorHttpComponent.getElement());
  }

  @Override
  public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<Exception> parameter) {
    log.error(ERROR500.name(), parameter.getException());
    return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
  }

}
