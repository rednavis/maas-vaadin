package com.rednavis.vaadin.service;

import static com.rednavis.shared.util.RestUrlUtils.USER_URL_ADMIN;
import static com.rednavis.shared.util.RestUrlUtils.USER_URL_FINDALL;
import static com.rednavis.shared.util.RestUrlUtils.USER_URL_USER;

import com.rednavis.shared.dto.user.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private final RestService restService;

  public String user(String accessToken) {
    String url = restService.createUserUrl(USER_URL_USER);
    return restService.getWithToken(url, accessToken, String.class);
  }

  public String admin(String accessToken) {
    String url = restService.createUserUrl(USER_URL_ADMIN);
    return restService.getWithToken(url, accessToken, String.class);
  }

  public List<User> findAll(String accessToken) {
    String url = restService.createUserUrl(USER_URL_FINDALL);
    List<User> userList = restService.getWithToken(url, accessToken, new ParameterizedTypeReference<>() {
    });
    return userList;
  }
}
