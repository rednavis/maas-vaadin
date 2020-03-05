package com.rednavis.vaadin.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ConstantUtils {

  public static final String VIEW_PORT = "width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover";

  public static final String CSS_PARAM_NAME_DISPLAY = "display";

  public static final String PAGE_ERROR401_TITLE = "Unauthorized error";
  public static final String PAGE_ERROR403_TITLE = "Forbidden error";
  public static final String PAGE_ERROR404_TITLE = "Not Found error";
  public static final String PAGE_ERROR500_TITLE = "Internal Server Error";

  public static final String PAGE_ROOT = "/";

  public static final String PAGE_LOGIN_TITLE = "Login";
  public static final String PAGE_LOGIN_URL = "loginView";

  public static final String PAGE_DASHBOARD_TITLE = "Dashboard";
  public static final String PAGE_DASHBOARD_URL = "dashboardView";

  public static final String ACCESS_DENIED_TITLE = "Access denied";
  public static final String NOT_FOUND_TITLE = "Page was not found";
}
