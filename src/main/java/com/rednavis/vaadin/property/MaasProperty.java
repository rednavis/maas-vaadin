package com.rednavis.vaadin.property;

import static com.rednavis.shared.util.RestUrlUtils.AUTH_URL;
import static com.rednavis.shared.util.RestUrlUtils.BOOK_URL;
import static com.rednavis.shared.util.RestUrlUtils.USER_URL;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("maas")
public class MaasProperty {

  private Api api;

  public String createAuthUrl(String restPoint) {
    return getApi().getServer() + AUTH_URL + restPoint;
  }

  public String createUserUrl(String restPoint) {
    return getApi().getServer() + USER_URL + restPoint;
  }

  public String createBookUrl(String restPoint) {
    return getApi().getServer() + BOOK_URL + restPoint;
  }

  @Data
  public static class Api {

    private String server;
  }
}
