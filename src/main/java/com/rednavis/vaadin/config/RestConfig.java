package com.rednavis.vaadin.config;

import com.rednavis.vaadin.config.interceptor.HeaderHttpRequestInterceptor;
import com.rednavis.vaadin.config.interceptor.LogHttpRequestInterceptor;
import java.util.List;
import java.util.function.Supplier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {

  /**
   * restTemplate.
   *
   * @param builder builder
   * @return
   */
  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder
        .requestFactory(new RequestFactorySupplier())
        .errorHandler(new CustomErrorHandler())
        .additionalInterceptors(List.of(new HeaderHttpRequestInterceptor(),
            new LogHttpRequestInterceptor()))
        .build();
  }

  class RequestFactorySupplier implements Supplier<ClientHttpRequestFactory> {

    @Override
    public ClientHttpRequestFactory get() {
      return new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
    }
  }
}
