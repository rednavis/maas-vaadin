import {PolymerElement} from "@polymer/polymer/polymer-element.js";
import {html} from "@polymer/polymer/lib/utils/html-tag.js";
import {styles} from "./login-view-styles";
import 'fontawesome-icon';
import "@vaadin/vaadin-button/vaadin-button";
import "@vaadin/vaadin-text-field/vaadin-email-field";
import "@vaadin/vaadin-text-field/vaadin-password-field";
import "@vaadin/vaadin-ordered-layout/vaadin-vertical-layout";

class LoginView extends PolymerElement {
  static get template() {
    return html`      
      ${styles}
      <div class="dialog">
        <h2 class="dialog-header">Sign in</h2>
        <vaadin-vertical-layout>
          <vaadin-button id="btnSignByFacebook" class="btn-primary text-left">
            <fontawesome-icon class="social-icon" prefix="fab" name="facebook" fixed-width slot="prefix"></fontawesome-icon>Sign in with <b>Facebook</b>
          </vaadin-button>
          <vaadin-button id="btnSignByGoogle" class="btn-danger text-left">
            <fontawesome-icon class="social-icon" prefix="fab" name="google" fixed-width slot="prefix"></fontawesome-icon>Sign in with <b>Google</b>
          </vaadin-button>      
          <div class="or-seperator">
            <i>or</i>         
          </div>
          <vaadin-email-field placeholder="Enter email" error-message="Please enter a valid email address" clear-button-visible></vaadin-email-field>
          <vaadin-password-field placeholder="Enter password"></vaadin-password-field>
          <vaadin-button id="btnSignIn" class="btn-signin">Sign In</vaadin-button>
        </vaadin-vertical-layout>
      </div>
      <div class="hint-text small">Don't have an account? <a href="#" class="text-success">Register Now!</a></div>
    </div>`;
  }

  static get is() {
    return "login-view";
  }
}

customElements.define(LoginView.is, LoginView);