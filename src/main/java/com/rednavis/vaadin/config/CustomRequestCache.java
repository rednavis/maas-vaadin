package com.rednavis.vaadin.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.rednavis.vaadin.util.SecurityUtils;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

/**
 * HttpSessionRequestCache that avoids saving internal framework requests.
 */
class CustomRequestCache extends HttpSessionRequestCache {

  /**
   * {@inheritDoc}
   *
   * <p>If the method is considered an internal request from the framework, we skip saving it.
   *
   * @see SecurityUtils#isFrameworkInternalRequest(HttpServletRequest)
   */
  @Override
  public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
    if (!SecurityUtils.isFrameworkInternalRequest(request)) {
      super.saveRequest(request, response);
    }
  }

}