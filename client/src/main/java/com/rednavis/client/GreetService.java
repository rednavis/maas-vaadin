package com.rednavis.client;

public class GreetService {

  /**
   * greet.
   *
   * @param name name
   * @return
   */
  public String greet(String name) {
    if (name == null || name.isEmpty()) {
      return "Hello anonymous user";
    } else {
      return "Hello " + name;
    }
  }
}
