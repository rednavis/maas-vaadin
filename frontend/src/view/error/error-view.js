import {PolymerElement} from "@polymer/polymer/polymer-element.js";
import {html} from "@polymer/polymer/lib/utils/html-tag.js";
import {styles} from "./error-view-styles";

class ErrorView extends PolymerElement {
  static get template() {
    return html`
    ${styles}
    <slot></slot>`;
  }

  static get is() {
    return "error-view";
  }
}

customElements.define(ErrorView.is, ErrorView);