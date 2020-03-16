package com.rednavis.vaadin.util;

import com.rednavis.shared.security.CurrentUser;
import com.rednavis.vaadin.view.error.Error401View;
import com.rednavis.vaadin.view.error.Error403View;
import com.rednavis.vaadin.view.error.Error404View;
import com.rednavis.vaadin.view.error.Error500View;
import com.rednavis.vaadin.view.login.LoginView;
import com.vaadin.flow.server.ServletHelper.RequestType;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.shared.ApplicationConstants;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

/**
 * SecurityUtils takes care of all such static operations that have to do with security and querying rights from different beans of the UI.
 */
@Slf4j
@UtilityClass
public class SecurityUtils {

  /**
   * getCurrentUser.
   *
   * @return
   */
  public CurrentUser getCurrentUser() {
    // Anonymous or no authentication.
    CurrentUser currentUser = null;
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      Object principal = authentication.getPrincipal();
      if (principal instanceof CurrentUser) {
        currentUser = (CurrentUser) authentication.getPrincipal();
      }
    }
    log.info("getCurrentUser [currentUser: {}]", currentUser);
    return currentUser;
  }

  /**
   * createAuthentication.
   *
   * @param currentUser currentUser
   */
  public void authenticate(CurrentUser currentUser) {
    log.info("authenticate [currentUser: {}]", currentUser);
    Collection<? extends GrantedAuthority> authorities = currentUser.getRoles()
        .stream()
        .map(role -> new SimpleGrantedAuthority(role.name()))
        .collect(Collectors.toList());
    Authentication authentication = new UsernamePasswordAuthenticationToken(currentUser, null, authorities);
    log.info("createAuthentication [authentication: {}]", authentication);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  /**
   * reject.
   */
  public void reject() {
    log.info("reject");
    new SecurityContextLogoutHandler().logout(VaadinServletRequest.getCurrent(), null, null);
  }

  /**
   * Checks if access is granted for the current user for the given secured view, defined by the view class.
   *
   * @param securedClass View class
   * @return true if access is granted, false otherwise.
   */
  public boolean isAccessGranted(Class<?> securedClass) {
    final boolean publicView = LoginView.class.equals(securedClass)
        || Error401View.class.equals(securedClass)
        || Error403View.class.equals(securedClass)
        || Error404View.class.equals(securedClass)
        || Error500View.class.equals(securedClass);

    // Always allow access to public views
    if (publicView) {
      log.info("isAccessGranted - true");
      return true;
    }

    Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();

    // All other views require authentication
    if (!isUserLoggedIn(userAuthentication)) {
      log.info("isAccessGranted - false");
      return false;
    }

    // Allow if no roles are required.
    Secured secured = AnnotationUtils.findAnnotation(securedClass, Secured.class);
    if (secured == null) {
      log.info("isAccessGranted - true");
      return true;
    }

    List<String> allowedRoles = Arrays.asList(secured.value());
    boolean access = userAuthentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
        .anyMatch(allowedRoles::contains);
    log.info("isAccessGranted - {}", access);
    return access;
  }

  /**
   * Checks if the user is logged in.
   *
   * @return true if the usisFrameworkInternalRequester is logged in. False otherwise.
   */
  public boolean isUserLoggedIn() {
    boolean logged = isUserLoggedIn(SecurityContextHolder.getContext().getAuthentication());
    log.info("isUserLoggedIn - {}", logged);
    return logged;
  }

  private boolean isUserLoggedIn(Authentication authentication) {
    return authentication != null
        && !(authentication instanceof AnonymousAuthenticationToken);
  }

  /**
   * Tests if the request is an internal framework request. The test consists of checking if the request parameter is present and if its
   * value is consistent with any of the request types know.
   *
   * @param request {@link HttpServletRequest}
   * @return true if is an internal framework request. False otherwise.
   */
  public boolean isFrameworkInternalRequest(HttpServletRequest request) {
    final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
    return parameterValue != null
        && Stream.of(RequestType.values()).anyMatch(r -> r.getIdentifier().equals(parameterValue));
  }
}