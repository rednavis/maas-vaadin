package com.rednavis.vaadin.annotation;

import com.rednavis.shared.security.CurrentUser;

@FunctionalInterface
public interface ActualUser {

  CurrentUser getCurrentUser();
}