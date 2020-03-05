package com.rednavis.vaadin.config;

import com.rednavis.vaadin.config.interceptor.HeaderHttpRequestInterceptor;
import com.rednavis.vaadin.config.interceptor.LogHttpRequestInterceptor;
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
    RequestFactorySupplier requestFactorySupplier = new RequestFactorySupplier();
    CustomErrorHandler customErrorHandler = new CustomErrorHandler();
    HeaderHttpRequestInterceptor headerHttpRequestInterceptor = new HeaderHttpRequestInterceptor();
    LogHttpRequestInterceptor logHttpRequestInterceptor = new LogHttpRequestInterceptor();

    return builder
        .requestFactory(requestFactorySupplier)
        .errorHandler(customErrorHandler)
        .additionalInterceptors(headerHttpRequestInterceptor,
            logHttpRequestInterceptor)
        .build();
  }

  class RequestFactorySupplier implements Supplier<ClientHttpRequestFactory> {

    @Override
    public ClientHttpRequestFactory get() {
      return new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
    }
  }
}
