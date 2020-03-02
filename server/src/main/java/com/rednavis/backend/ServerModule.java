package com.rednavis.backend;

import com.rednavis.backend.config.CustomRestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class ServerModule {

  @Bean
  public CustomRestTemplateCustomizer customRestTemplateCustomizer() {
    return new CustomRestTemplateCustomizer();
  }
}
