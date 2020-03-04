package com.rednavis.client.config;

import static com.rednavis.client.ConstantUtils.PAGE_LOGIN_URL;
import static com.rednavis.client.ConstantUtils.PAGE_ROOT;

import com.rednavis.client.util.SecurityUtils;
import com.rednavis.shared.dto.user.RoleEnum;
import com.rednavis.shared.util.EnumUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Require login to access internal pages and configure login form.
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .csrf().disable()
        .formLogin().disable()
        .httpBasic().disable()
        .authorizeRequests()
        // Allow all flow internal requests.
        .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
        .mvcMatchers(PAGE_ROOT, PAGE_ROOT + PAGE_LOGIN_URL).permitAll()
        // Allow all requests by logged in users.
        .anyRequest().hasAnyAuthority(EnumUtils.getNames(RoleEnum.class));
  }

  /**
   * Allows access to static resources, bypassing Spring security.
   */
  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers(
        // Vaadin Flow static resources
        "/VAADIN/**",
        // the standard favicon URI
        "/favicon.ico",
        // the robots exclusion standard
        "/robots.txt",
        // web application manifest
        "/manifest.webmanifest",
        "/sw.js",
        "/offline-page.html",
        // icons and images
        "/icons/**",
        "/images/**",
        // (development mode) static resources
        "/frontend/**",
        // (development mode) webjars
        "/webjars/**",
        // (production mode) static resources
        "/frontend-es5/**", "/frontend-es6/**");
  }
}
