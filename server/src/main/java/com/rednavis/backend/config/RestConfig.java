package com.rednavis.backend.config;

import com.rednavis.backend.config.interceptor.HeaderHttpRequestInterceptor;
import com.rednavis.backend.config.interceptor.LogHttpRequestInterceptor;
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

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    RequestFactorySupplier requestFactorySupplier = new RequestFactorySupplier();
    HeaderHttpRequestInterceptor headerHttpRequestInterceptor = new HeaderHttpRequestInterceptor();
    LogHttpRequestInterceptor logHttpRequestInterceptor = new LogHttpRequestInterceptor();
    return builder
        .requestFactory(requestFactorySupplier)
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
