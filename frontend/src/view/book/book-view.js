import {PolymerElement} from "@polymer/polymer/polymer-element.js";
import {html} from "@polymer/polymer/lib/utils/html-tag.js";
import {styles} from "../common-view-styles";

class BookView extends PolymerElement {
  static get template() {
    return html`
    ${styles}
    <slot></slot>`;
  }

  static get is() {
    return "book-view";
  }
}

customElements.define(BookView.is, BookView);