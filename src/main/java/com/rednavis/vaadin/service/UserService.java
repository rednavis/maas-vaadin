package com.rednavis.vaadin.service;

import static com.rednavis.shared.util.RestUrlUtils.USER_URL_ADMIN;
import static com.rednavis.shared.util.RestUrlUtils.USER_URL_FINDALL;
import static com.rednavis.shared.util.RestUrlUtils.USER_URL_USER;

import java.util.List;
import com.rednavis.shared.dto.user.User;
import com.rednavis.vaadin.property.MaasProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private final MaasProperty maasProperty;
  private final RestService restService;

  /**
   * user.
   *
   * @param accessToken accessToken
   * @return
   */
  public String user(String accessToken) {
    String url = maasProperty.createUserUrl(USER_URL_USER);
    return restService.getWithToken(url, accessToken, String.class);
  }

  /**
   * admin.
   *
   * @param accessToken accessToken
   * @return
   */
  public String admin(String accessToken) {
    String url = maasProperty.createUserUrl(USER_URL_ADMIN);
    return restService.getWithToken(url, accessToken, String.class);
  }

  /**
   * findAll.
   *
   * @param accessToken accessToken
   * @return
   */
  public List<User> findAll(String accessToken) {
    String url = maasProperty.createUserUrl(USER_URL_FINDALL);
    return restService.getWithToken(url, accessToken, new ParameterizedTypeReference<>() {
    });
  }
}
