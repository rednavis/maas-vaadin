import {html} from "@polymer/polymer/lib/utils/html-tag.js";

//https://www.tutorialrepublic.com/codelab.php?topic=bootstrap&file=sign-in-from-with-social-login-button
export const styles = html`
      <style>
        :host {
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          width: 100%;
          height: 100%;
        }
        
        .dialog {
          width: 340px;          
          background: #f7f7f7;
          box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
          padding: 30px;
          margin-bottom: 15px;
        }
        
        .dialog .dialog-header {
          text-align: center;
          margin: 0 0 15px;
        }
        
        .dialog *{
          width: 100%;
        }
        
        .dialog vaadin-button{
          border-radius: 0px;
          color: #fff;
        }
        
        :host(.text-left) .vaadin-button-container {
          justify-content: left;
        }

        .social-icon {
          font-size: 18px;
        }
        
        .btn-primary {
          background-color: #337ab7;
          border: 1px solid #2e6da4;
        }
        
        .btn-danger {
          background-color: #d9534f;
          border: 1px solid #d43f3a;
        }
        
        .btn-signin {
          font-weight: bold;
          background-color: #5cb85c;
          border: 1px solid #4cae4c;
        }
        
        .or-seperator {
          margin: 20px 0 0;
          text-align: center;
          border-top: 1px solid #ccc;
        }
        
        .or-seperator i {
          padding: 0 10px;
          background: #f7f7f7;
          position: relative;
          top: -14px;
          z-index: 1;
        }
      
        .hint-text {
          color: #777;
          padding-bottom: 15px;
          text-align: center;
          font-size: 85%;
        }
      </style>`;