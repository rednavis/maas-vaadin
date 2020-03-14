package com.rednavis.vaadin.exceptions;

public class MaasVaadinException extends RuntimeException {

  public MaasVaadinException() {
  }

  public MaasVaadinException(String message) {
    super(message);
  }

  public MaasVaadinException(String message, Throwable throwable) {
    super(message, throwable);
  }
}