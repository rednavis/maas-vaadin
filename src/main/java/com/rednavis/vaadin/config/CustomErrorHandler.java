package com.rednavis.vaadin.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rednavis.shared.rest.ApiResponse;
import com.rednavis.shared.rest.response.ErrorResponse;
import com.vaadin.flow.component.notification.Notification;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomErrorHandler implements ResponseErrorHandler {

  private final Gson gson;

  @Override
  public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
    return new DefaultResponseErrorHandler().hasError(clientHttpResponse);
  }

  @Override
  public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
    String responseAsString = StreamUtils.copyToString(clientHttpResponse.getBody(), Charset.defaultCharset());
    log.error("Rest error from MAAS-API [Response body: {}]", responseAsString);
    ApiResponse<ErrorResponse> errorResponseApiResponse = gson.fromJson(responseAsString,
        new TypeToken<ApiResponse<ErrorResponse>>() {
        }.getType());
    Notification.show(errorResponseApiResponse.getPayloads().getMessage());
  }

  @Override
  public void handleError(URI url, HttpMethod method, ClientHttpResponse clientHttpResponse) throws IOException {
    log.error("Rest error from MAAS-API [URI: {}, Method: {}]", url, method);
    handleError(clientHttpResponse);
  }
}