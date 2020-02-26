package com.rednavis.client.exceptions;

public class AccessDeniedException extends RuntimeException {

  public AccessDeniedException() {
  }

  public AccessDeniedException(String message) {
    super(message);
  }
}
