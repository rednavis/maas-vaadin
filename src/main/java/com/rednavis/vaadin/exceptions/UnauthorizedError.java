package com.rednavis.vaadin.exceptions;

public class UnauthorizedError extends MaasVaadinException {

  public UnauthorizedError() {
  }

  public UnauthorizedError(String message) {
    super(message);
  }

  public UnauthorizedError(String message, Throwable throwable) {
    super(message, throwable);
  }

}
