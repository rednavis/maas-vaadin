package com.rednavis.vaadin.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("info")
public class InfoProperty {

  private Rednavis rednavis;

  @Data
  public static class Rednavis {

    private String email;
  }
}
