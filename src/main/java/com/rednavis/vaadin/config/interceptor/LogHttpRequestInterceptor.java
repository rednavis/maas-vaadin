package com.rednavis.vaadin.config.interceptor;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

@Slf4j
public class LogHttpRequestInterceptor implements ClientHttpRequestInterceptor {

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    logRequestDetails(request, body);
    ClientHttpResponse clientHttpResponse = execution.execute(request, body);
    logResponseDetails(clientHttpResponse);
    return clientHttpResponse;
  }

  private void logRequestDetails(HttpRequest request, byte[] body) throws IOException {
    log.info("=========================REQUEST=========================");
    log.info("URI         : {}", request.getURI());
    log.info("Method      : {}", request.getMethod());
    log.info("Headers     : {}", request.getHeaders());
    log.info("Request body: {}", new String(body, StandardCharsets.UTF_8));
    log.info("==================================================");
  }

  private void logResponseDetails(ClientHttpResponse response) throws IOException {
    log.info("=========================RESPONSE=========================");
    log.info("Status code  : {}", response.getStatusCode());
    log.info("Status text  : {}", response.getStatusText());
    log.info("Headers      : {}", response.getHeaders());
    log.info("Response body: {}", StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
    log.info("==================================================");
  }
}