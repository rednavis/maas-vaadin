package com.rednavis.client.security;

import static com.rednavis.client.ConstantUtils.PAGE_DASHBOARD_URL;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

/**
 * Configures spring security. Doing the following:
 * <li>Bypass security checks for static resources,</li>
 * <li>Restrict access to the application, allowing only logged in users,</li>
 * <li>Set up the login form,</li>
 */
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private static final String LOGIN_PROCESSING_URL = "/loginView";
  private static final String LOGIN_FAILURE_URL = "/loginView?error";
  private static final String LOGIN_URL = "/loginView";
  private static final String LOGOUT_SUCCESS_URL = "/" + PAGE_DASHBOARD_URL;

  private final UserDetailsService userDetailsService;

  //@Autowired
  //private PasswordEncoder passwordEncoder;

  @Autowired
  public SecurityConfiguration(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  ///**
  // * The password encoder to use when encrypting passwords.
  // */
  //@Bean
  //public PasswordEncoder passwordEncoder() {
  //return new BCryptPasswordEncoder();
  //}

  //@Bean
  //@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  //public CurrentUser currentUser(UserRepository userRepository) {
  //final String username = com.gmail.app.security.SecurityUtils.getUsername();
  //User user =
  //username != null ? userRepository.findByEmailIgnoreCase(username) :
  //null;
  //return () -> user;
  //}

  ///**
  // * Registers our UserDetailsService and the password encoder to be used on login attempts.
  // */
  //@Override
  //protected void configure(AuthenticationManagerBuilder auth) throws Exception {
  //super.configure(auth);
  //auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
  //}

  /**
   * Require login to access internal pages and configure login form.
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    List<String> roles = List.of("ADMIN", "USER");
    // Not using Spring CSRF here to be able to use plain HTML for the login page
    http.csrf().disable()

        // Register our CustomRequestCache, that saves unauthorized access attempts, so
        // the user is redirected after login.
        .requestCache().requestCache(new CustomRequestCache())

        // Restrict access to our application.
        .and().authorizeRequests()

        // Allow all flow internal requests.
        .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()

        // Allow all requests by logged in users.
        .anyRequest().hasAnyAuthority(roles.toArray(String[]::new))

        // Configure the login page.
        .and().formLogin().loginPage(LOGIN_URL).permitAll().loginProcessingUrl(LOGIN_PROCESSING_URL)
        .failureUrl(LOGIN_FAILURE_URL)

        // Register the success handler that redirects users to the page they last tried
        // to access
        .successHandler(new SavedRequestAwareAuthenticationSuccessHandler())

        // Configure logout
        .and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);
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

        // (development mode) H2 debugging console
        "/h2-console/**",

        // (production mode) static resources
        "/frontend-es5/**", "/frontend-es6/**");
  }
}
