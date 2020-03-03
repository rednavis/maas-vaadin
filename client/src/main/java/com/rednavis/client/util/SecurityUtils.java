package com.rednavis.client.util;

import com.rednavis.client.view.error.AccessDeniedView;
import com.rednavis.client.view.error.CustomRouteNotFoundError;
import com.rednavis.client.view.login.LoginView;
import com.rednavis.shared.security.CurrentUser;
import com.vaadin.flow.server.ServletHelper.RequestType;
import com.vaadin.flow.shared.ApplicationConstants;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * SecurityUtils takes care of all such static operations that have to do with security and querying rights from different beans of the UI.
 */
@Slf4j
@UtilityClass
public class SecurityUtils {

  /**
   * Gets the user name of the currently signed in user.
   *
   * @return the user name of the current user or <code>null</code> if the user has not signed in
   */
  public static CurrentUser getCurrentUser() {
    SecurityContext context = SecurityContextHolder.getContext();
    Object principal = context.getAuthentication()
        .getPrincipal();
    if (principal instanceof CurrentUser) {
      return (CurrentUser) context.getAuthentication()
          .getPrincipal();
    }
    return null;
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

    log.info("GrantedAuthority: {}", userAuthentication.getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(", "))
    );
    List<String> allowedRoles = Arrays.asList(secured.value());
    return userAuthentication.getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .anyMatch(allowedRoles::contains);
  }

  /**
   * Checks if the user is logged in.
   *
   * @return true if the user is logged in. False otherwise.
   */
  public static boolean isUserLoggedIn() {
    boolean isUserLoggedIn = isUserLoggedIn(SecurityContextHolder.getContext().getAuthentication());
    log.info("isUserLoggedIn: {}", isUserLoggedIn);
    return isUserLoggedIn;
  }

  private static boolean isUserLoggedIn(Authentication authentication) {
    return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
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
    return parameterValue != null && Stream.of(RequestType.values()).anyMatch(r -> r.getIdentifier().equals(parameterValue));
  }

}