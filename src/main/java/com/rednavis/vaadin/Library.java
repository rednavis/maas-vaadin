package com.rednavis.vaadin;

public class Library {

  public boolean someLibraryMethod() {
    return true;
  }

  public boolean someSharedLibraryMethod() {
    com.rednavis.shared.Library sharedLibrary = new com.rednavis.shared.Library();
    return sharedLibrary.someLibraryMethod();
  }
}
