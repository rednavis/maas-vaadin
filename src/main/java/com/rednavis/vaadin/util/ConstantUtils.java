package com.rednavis.vaadin.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ConstantUtils {

  public static final String VIEW_PORT = "width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover";
  public static final String JSESSIONID = "JSESSIONID";

  public static final String PAGE_ERROR401_TITLE = "Unauthorized error";
  public static final String PAGE_ERROR403_TITLE = "Forbidden error";
  public static final String PAGE_ERROR404_TITLE = "Not Found error";
  public static final String PAGE_ERROR500_TITLE = "Internal Server Error";

  public static final String PAGE_ROOT = "/";
  public static final String PAGE_SIGNOUT = "signOut";

  public static final String PAGE_LOGIN_TITLE = "Login";
  public static final String PAGE_LOGIN_URL = "loginView";

  public static final String PAGE_DASHBOARD_TITLE = "Dashboard";
  public static final String PAGE_DASHBOARD_URL = "dashboardView";

  public static final String PAGE_USER_TITLE = "Users";
  public static final String PAGE_USER_CAPTION = "Users";
  public static final String PAGE_USER_URL = "userView";

  public static final String PAGE_BOOK_TITLE = "Books";
  public static final String PAGE_BOOK_CAPTION = "Books";
  public static final String PAGE_BOOK_URL = "bookView";
}
