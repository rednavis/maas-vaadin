package com.rednavis.vaadin.exceptions;

public class ForbiddenError extends MaasVaadinException {

  public ForbiddenError() {
  }

  public ForbiddenError(String message) {
    super(message);
  }

  public ForbiddenError(String message, Throwable throwable) {
    super(message, throwable);
  }

}
