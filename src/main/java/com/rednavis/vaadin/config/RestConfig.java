package com.rednavis.vaadin.config;

import com.rednavis.vaadin.config.interceptor.HeaderHttpRequestInterceptor;
import com.rednavis.vaadin.config.interceptor.LogHttpRequestInterceptor;
import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class RestConfig {

  private final CustomErrorHandler customErrorHandler;

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
        //.errorHandler(customErrorHandler)
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
