package com.rednavis.vaadin.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("maas")
public class MaasProperty {

  private Api api;

  @Data
  public static class Api {

    private String server;
  }
}
