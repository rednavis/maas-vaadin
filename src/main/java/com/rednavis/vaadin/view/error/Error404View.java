package com.rednavis.vaadin.view.error;

import static com.rednavis.vaadin.util.ConstantUtils.PAGE_ERROR404_TITLE;
import static com.rednavis.vaadin.view.component.ErrorHttpComponent.ErrorHttpEnum.ERROR404;

import com.rednavis.vaadin.property.InfoProperty;
import com.rednavis.vaadin.view.component.ErrorHttpComponent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.templatemodel.TemplateModel;
import javax.servlet.http.HttpServletResponse;

@PageTitle(PAGE_ERROR404_TITLE)
@Tag("error-view")
@JsModule("./src/view/error/error-view.js")
public class Error404View extends PolymerTemplate<TemplateModel> implements HasErrorParameter<NotFoundException> {

  public Error404View(InfoProperty infoProperty) {
    ErrorHttpComponent errorHttpComponent = new ErrorHttpComponent(ERROR404, infoProperty.getRednavis().getEmail());
    getElement().appendChild(errorHttpComponent.getElement());
  }

  @Override
  public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
    return HttpServletResponse.SC_NOT_FOUND;
  }

}
