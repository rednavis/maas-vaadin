package com.rednavis.vaadin.dto;

import lombok.Data;

@Data
public class SignInClient {

  private String userName;
  private String password;
  private boolean saveUser;
}
