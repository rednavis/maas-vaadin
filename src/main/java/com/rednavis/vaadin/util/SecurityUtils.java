package com.rednavis.vaadin.util;

import static com.vaadin.flow.server.ServletHelper.RequestType;

import com.rednavis.shared.security.CurrentUser;
import com.rednavis.vaadin.view.error.AccessDeniedView;
import com.rednavis.vaadin.view.error.CustomRouteNotFoundError;
import com.rednavis.vaadin.view.login.LoginView;
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
   * createAuthentication.
   *
   * @param currentUser currentUser
   */
  public void createAuthentication(CurrentUser currentUser) {
    log.info("createAuthentication [currentUser: {}]", currentUser);
    Collection<? extends GrantedAuthority> authorities = currentUser.getRoles()
        .stream()
        .map(role -> new SimpleGrantedAuthority(role.name()))
        .collect(Collectors.toList());
    Authentication authentication = new UsernamePasswordAuthenticationToken(currentUser, null, authorities);
    log.info("createAuthentication [authentication: {}]", authentication);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  /**
   * Checks if access is granted for the current user for the given secured view, defined by the view class.
   *
   * @param securedClass View class
   * @return true if access is granted, false otherwise.
   */
  public static boolean isAccessGranted(Class<?> securedClass) {
    final boolean publicView = LoginView.class.equals(securedClass)
        || AccessDeniedView.class.equals(securedClass)
        || CustomRouteNotFoundError.class.equals(securedClass);

    // Always allow access to public views
    if (publicView) {
      return true;
    }

    Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();

    // All other views require authentication
    if (!isUserLoggedIn(userAuthentication)) {
      return false;
    }

    // Allow if no roles are required.
    Secured secured = AnnotationUtils.findAnnotation(securedClass, Secured.class);
    if (secured == null) {
      return true;
    }

    List<String> allowedRoles = Arrays.asList(secured.value());
    return userAuthentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
        .anyMatch(allowedRoles::contains);
  }

  /**
   * Checks if the user is logged in.
   *
   * @return true if the usisFrameworkInternalRequester is logged in. False otherwise.
   */
  public static boolean isUserLoggedIn() {
    return isUserLoggedIn(SecurityContextHolder.getContext().getAuthentication());
  }

  private static boolean isUserLoggedIn(Authentication authentication) {
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
  public static boolean isFrameworkInternalRequest(HttpServletRequest request) {
    final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
    return parameterValue != null
        && Stream.of(RequestType.values()).anyMatch(r -> r.getIdentifier().equals(parameterValue));
  }

  public static void signOut() {
    new SecurityContextLogoutHandler().logout(VaadinServletRequest.getCurrent(), null, null);
  }
}