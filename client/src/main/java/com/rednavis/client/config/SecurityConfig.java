package com.rednavis.client.config;

import static com.rednavis.client.ConstantUtils.PAGE_DASHBOARD_URL;

import com.rednavis.client.util.SecurityUtils;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private static final String LOGIN_URL = "/loginView";
  private static final String LOGOUT_SUCCESS_URL = "/" + PAGE_DASHBOARD_URL;

  /**
   * Require login to access internal pages and configure login form.
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    List<String> roles = List.of("ROLE_ADMIN", "ROLE_USER");

    //   Not using Spring CSRF here to be able to use plain HTML for the login page
    http.csrf().disable()
        .csrf().disable()
        .formLogin().disable()

        //     Register our CustomRequestCache, that saves unauthorized access attempts, so
        //     the user is redirected after login.
        .requestCache().requestCache(new CustomRequestCache())

        // Restrict access to our application.
        .and().authorizeRequests()

        // Allow all flow internal requests.
        .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
        .mvcMatchers(LOGIN_URL, "", "/").permitAll()

        // Allow all requests by logged in users.
        .anyRequest().hasAnyAuthority(roles.toArray(String[]::new));

    // Configure the login page.
    //.and().formLogin().loginPage(LOGIN_URL).permitAll().loginProcessingUrl(LOGIN_PROCESSING_URL)
    //.failureUrl(LOGIN_FAILURE_URL)

    // Register the success handler that redirects users to the page they last tried
    // to access
    //.successHandler(new SavedRequestAwareAuthenticationSuccessHandler())

    // Configure logout
    //.and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);
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
