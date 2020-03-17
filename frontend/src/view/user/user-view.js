import {PolymerElement} from "@polymer/polymer/polymer-element.js";
import {html} from "@polymer/polymer/lib/utils/html-tag.js";
import {styles} from "../common-view-styles";

class UserView extends PolymerElement {
  static get template() {
    return html`
    ${styles}
    <slot></slot>`;
  }

  static get is() {
    return "user-view";
  }
}

customElements.define(UserView.is, UserView);