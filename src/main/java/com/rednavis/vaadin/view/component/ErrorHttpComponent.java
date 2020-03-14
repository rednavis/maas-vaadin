package com.rednavis.vaadin.view.component;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;

@Tag("error-http-component")
@JsModule("./src/component/error-http-component.js")
public class ErrorHttpComponent extends PolymerTemplate<TemplateModel> {

  private static final String MAIL_TO_PARAMETER = "mailto:";

  @Id("error-http-title")
  private H1 errorTitle;

  @Id("error-http-message")
  private Paragraph errorMessage;

  @Id("error-http-mail")
  private Anchor errorMail;

  /**
   * ErrorHttpComponent.
   *
   * @param errorHttpEnum errorHttpEnum
   * @param email         email
   */
  public ErrorHttpComponent(ErrorHttpEnum errorHttpEnum, String email) {
    errorTitle.setText(errorHttpEnum.getTitle());
    errorMessage.setText(errorHttpEnum.getMessage());
    errorMail.setText(email);
    errorMail.setHref(MAIL_TO_PARAMETER + email);
  }

  public enum ErrorHttpEnum {
    ERROR401("401", "UNAUTHORIZED ERROR"),
    ERROR403("403", "FORBIDDEN ERROR"),
    ERROR404("404", "NOT FOUND ERROR"),
    ERROR500("500", "INTERNAL SERVER ERROR");

    private String title;
    private String message;

    ErrorHttpEnum(String title, String message) {
      this.title = title;
      this.message = message;
    }

    public String getTitle() {
      return title;
    }

    public String getMessage() {
      return message;
    }
  }

}
