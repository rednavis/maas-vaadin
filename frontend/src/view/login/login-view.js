import {PolymerElement} from "@polymer/polymer/polymer-element.js";
import {html} from "@polymer/polymer/lib/utils/html-tag.js";
import {styles} from "./login-view-styles";
import 'fontawesome-icon';
import "@vaadin/vaadin-button/vaadin-button";
import "@vaadin/vaadin-text-field/vaadin-text-field";
import "@vaadin/vaadin-text-field/vaadin-password-field";
import "@vaadin/vaadin-ordered-layout/vaadin-vertical-layout";
import "@vaadin/vaadin-ordered-layout/vaadin-horizontal-layout";

class LoginView extends PolymerElement {
  static get template() {
    return html`      
      ${styles}
      <vaadin-vertical-layout class="dialog">
        <h2 class="dialog-header">Sign in</h2>
        <vaadin-button id="btnSignByFacebook" class="btn-primary text-left">
          <fontawesome-icon class="social-icon" prefix="fab" name="facebook" fixed-width slot="prefix"></fontawesome-icon>
            Sign in with <b>Facebook</b>
        </vaadin-button>
        <vaadin-button id="btnSignByGoogle" class="btn-danger text-left">
          <fontawesome-icon class="social-icon" prefix="fab" name="google" fixed-width slot="prefix"></fontawesome-icon>
          Sign in with <b>Google</b>
        </vaadin-button>      
        <div class="or-seperator">
          <i>or</i>         
        </div>
        <vaadin-text-field id="username" placeholder="Enter login or email" clear-button-visible></vaadin-text-field>
        <vaadin-password-field id="password" placeholder="Enter password" clear-button-visible></vaadin-password-field>
        <vaadin-button id="btnSignIn" class="btn-signin">Sign In</vaadin-button>
        <vaadin-horizontal-layout class="addon">
          <vaadin-checkbox id="saveUser">Remember me</vaadin-checkbox>
            <a href="#" class="pull-right text-success">Forgot Password?</a>
        </vaadin-horizontal-layout>
      </vaadin-vertical-layout>
      <div class="hint-text">Don't have an account? <a href="#" class="text-success">Register Now!</a></div>
    </div>`;
  }

  static get is() {
    return "login-view";
  }
}

customElements.define(LoginView.is, LoginView);