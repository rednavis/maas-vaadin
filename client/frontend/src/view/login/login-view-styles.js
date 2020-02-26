import {html} from "@polymer/polymer/lib/utils/html-tag.js";

export const styles = html`
      <style>
        :host {
          --color-facebook: #3e5b96;
          --color-twitter: #2aa3ef;
          --color-google: #db4e47;
          display: flex;
          width: 400px;
          margin: 0 auto;
          flex-direction: column;
          padding-top: 200px;
          --lumo-border-radius-m: 0.25em;
          --lumo-font-size-s: .875rem;
          --lumo-secondary-text-color: hsla(214, 42%, 18%, 0.72);
          --lumo-primary-text-color: hsl(214, 90%, 52%); 
        }
        
        iron-form {
          box-shadow: 0 0 10px rgba(0,0,0,0.5);
          padding: 15px;
          background: lightslategray;
        }
        
        .login-form-container .text-upper {
          text-transform: uppercase;
          text-align: center;
          overflow: hidden;
        }
        
        .social-button iron-icon {
          position: absolute;
          left: .5rem;
          top: .44rem;
       }
       
       iron-icon {
          display: -ms-inline-flexbox;
          display: -webkit-inline-flex;
          display: inline-flex;
          -ms-flex-align: center;
          -webkit-align-items: center;
          align-items: center;
          -ms-flex-pack: center;
          -webkit-justify-content: center;
          justify-content: center;
       }
       .registration-dialog__social-button {
          position: relative;
          width: 100%;
          margin-top: .3rem;
       }
      .social-button {
          font-family: 'Montserrat';
          text-transform: none;
          border-radius: 50px;
          font-weight: 500;
          margin-bottom: .5rem;
          padding: .4rem .5rem;
          display: inline-block;
          text-align: center;
          outline: none;
      }
      .button--social-text {
         font-weight: 700;
         font-size: 15px;
      }
      button, input {
        color: inherit;
        font: inherit;
        margin: 0;
      }
      .button {
        padding: 0;
        border: 0;
        background: transparent;
        font-family: inherit;
        font-size: 1em;
        cursor: pointer;
        outline: 0;
        font-weight: 700;
        line-height: 1.6;
        color: #00b4f0;
        position: relative;
        display: inline-block;
        height: auto;
        text-decoration: none;
        margin: 1.5rem 0 8px;
        text-transform: uppercase;
        letter-spacing: .1em;
        transition: 100ms;
      }
      .button--bordered, .button--filled, .button--white {
        font-family: 'Montserrat';
        border: 0;
        padding: .8rem 1.6rem;
        background: #00b4f0;
        border-radius: 1000px;
        color: #fff;
        font-weight: 700;
        text-transform: uppercase;
        margin: 1rem 0 0;
      }
      .login-form-container .button--twitter {
        background-color: var(--color-twitter);
        color: #fff;
      } 
      .login-form-container .button--google {
        background-color: var(--color-google);
        color: #fff;
      }
      .login-form-container .button--facebook {
        background-color: var(--color-facebook);
        color: #fff;
      }
      vaadin-text-field, vaadin-password-field {
        width: 100%;
      }
      [part="label"]::after {
        content: "•";
        transition: opacity 0.2s;
        opacity: 0;
        color: var(--lumo-primary-text-color);
        position: absolute;
        right: 0;
        width: 1em;
        text-align: center;
      }
      [part="label"] {
        align-self: flex-start;
        color: var(--lumo-secondary-text-color);
        font-weight: 500;
        font-size: var(--lumo-font-size-s);
        margin-left: calc(var(--lumo-border-radius-m) / 4);
        transition: color 0.2s;
        line-height: 1;
        padding-bottom: 0.5em;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
        position: relative;
        max-width: 100%;
        box-sizing: border-box;
      }
      label {
        cursor: default;
      }
      .login-form-container .vc-input {
        width: 100%;
      }
      .plain-tabs {
        --paper-tabs-selection-bar-color: #34C65D;
        --paper-tabs: {
        font-family: 'Montserrat';
        color: #fff;
        font-size: 16px;
        font-weight: 700;
        display: inline-block;
        max-width: 100%;
        width: auto 
        }
      }
      .or-with-lines {
        text-align: center;
        overflow: hidden;
      }
      .or-with-lines > span:before {
        right: 100%;
        margin-right: 20px;
      }
      
      .or-with-lines > span:after {
        left: 100%;
        margin-left: 20px;
      }
      .or-with-lines > span:before, 
      .or-with-lines > span:after {
        content: "";
        position: absolute;
        height: 1px;
        top: 50%;
        background-color: #fff;
        width: 200px;
      }
      .or-with-lines > span {
        display: inline-block;
        position: relative;
        font-size: 14px;
        font-weight: 700;
      }
      .dialog-header {
        text-align: center;
        background-color: black;
      }
      </style>
`;