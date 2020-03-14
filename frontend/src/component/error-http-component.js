import {PolymerElement} from "@polymer/polymer/polymer-element.js";
import {html} from "@polymer/polymer/lib/utils/html-tag.js";
import {styles} from "./error-http-component-styles";

class ErrorHttpComponent extends PolymerElement {
  static get template() {
    return html`
    ${styles}
    <div class="error-http">
      <span class="error-http-icon"></span>
      <h1 id="error-http-title" class="error-http-title"></h1>
      <p id="error-http-message" class="text-center"></p>
      <p class="text-center">Contact <a id="error-http-mail"></a></p>
      <div class="error-http-buttons">
        <button onclick="window.location.href='/loginView'" class="error-http-button" type="button">OK
        </button>
      </div>
    </div>`;
  }

  static get is() {
    return "error-http-component";
  }
}

customElements.define(ErrorHttpComponent.is, ErrorHttpComponent);