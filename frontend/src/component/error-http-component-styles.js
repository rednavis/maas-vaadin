import {html} from "@polymer/polymer/lib/utils/html-tag.js";

export const styles = html`
    <style>
      :host {
        min-width: 500px;
        background-color: white;
      }

      @keyframes popDialog {
        0% {
          transform: rotateZ(0)
        }
        25% {
          transform: rotateZ(3deg)
        }
        75% {
          transform: rotateZ(-3deg)
        }
        100% {
          transform: rotateZ(0)
        }
      }

      .error-http {
        max-width: 64.0rem;
        padding: 3.2rem;
        border-radius: .2rem;
        box-shadow: 0 0 0 0.1rem rgba(0, 0, 0, 0.1), 0 0.2rem 0.4rem rgba(0, 0, 0, 0.1), 0 0.8rem 3.2rem -0.8rem rgba(0, 0, 0, 0.2);
        transform-origin: center;
        animation: popDialog 0.3s ease 2
      }

      .error-http-icon {
        display: block;
        margin: 0 auto;
        width: 6.4rem;
        height: 6.4rem;
        background-size: 100%;
        background-image: url("data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAxMDI0IDEwMjQiPjxwYXRoIGZpbGw9IiNFNzRDM0MiIGQ9Ik01MTIgMGMtMjgyLjMwNCAwLTUxMiAyMjkuNjk2LTUxMiA1MTJzMjI5LjY5NiA1MTIgNTEyIDUxMiA1MTItMjI5LjY5NiA1MTItNTEyLTIyOS42OTYtNTEyLTUxMi01MTJ6bTAgOTYwYy0yNDYuOTc2IDAtNDQ4LTIwMS4wMjQtNDQ4LTQ0OHMyMDEuMDI0LTQ0OCA0NDgtNDQ4IDQ0OCAyMDEuMDI0IDQ0OCA0NDgtMjAxLjAyNCA0NDgtNDQ4IDQ0OHptMC0zODRjLTgyLjI0IDAtMTU5LjE2OCAzOC4yMDgtMjE2LjY0IDEwNy41ODQtMTEuMzI4IDEzLjYzMi05LjM0NCAzMy43OTIgNC4xNiA0NS4wNTYgMTMuNjMyIDExLjI2NCAzMy43OTIgOS4zNDQgNDUuMDU2LTQuMTYgNDUuMTg0LTU0LjQ2NCAxMDQuNjQtODQuNDggMTY3LjQyNC04NC40OHMxMjIuMjQgMzAuMDE2IDE2Ny4zNiA4NC40MTZjNi4zMzYgNy42MTYgMTUuNDI0IDExLjU4NCAyNC42NCAxMS41ODQgNy4xNjggMCAxNC40NjQtMi40MzIgMjAuNDE2LTcuMzYgMTMuNTY4LTExLjI2NCAxNS40ODgtMzEuNDI0IDQuMTYtNDUuMDU2LTU3LjQwOC02OS4zNzYtMTM0LjMzNi0xMDcuNTg0LTIxNi41NzYtMTA3LjU4NHptLTEyOC0xOTJjMCAzNS4zNDYtMjguNjU0IDY0LTY0IDY0cy02NC0yOC42NTQtNjQtNjRjMC0zNS4zNDYgMjguNjU0LTY0IDY0LTY0czY0IDI4LjY1NCA2NCA2NHptMzg0IDBjMCAzNS4zNDYtMjguNjU0IDY0LTY0IDY0cy02NC0yOC42NTQtNjQtNjRjMC0zNS4zNDYgMjguNjU0LTY0IDY0LTY0czY0IDI4LjY1NCA2NCA2NHoiLz48L3N2Zz4=")
      }

      .error-http-title {
        text-align: center;
        font-size: 3.2rem;
        line-height: 1;
        font-weight: 300;
        margin: 2.4rem 0
      }

      .error-http p {
        font-size: 1.6rem;
        line-height: 1.5;
        margin: 0 0 .8rem 0;
        color: #404040
      }

      .error-http-buttons {
        text-align: center;
        margin-top: 3.2rem
      }

      .error-http-button {
        -webkit-appearance: none;
        background: transparent;
        cursor: pointer;
        display: inline-block;
        font: inherit;
        line-height: normal;
        position: relative;
        text-align: center;
        text-decoration: none;
        -moz-user-select: none;
        -ms-user-select: none;
        -webkit-user-select: none;
        user-select: none;
        vertical-align: middle;
        white-space: nowrap;
        padding: 1.6rem 3.2rem;
        border: 0;
        background-color: #2D7BBB;
        color: #FFF;
        box-shadow: 0 0 0 0.1rem rgba(0, 0, 0, 0.05) inset;
        border-radius: .2rem;
        font-size: 1.6rem;
        transition: .2s ease;
        font-weight: bold;
        min-width: 50%
      }

      .error-http-button:focus {
        outline: none;
        box-shadow: 0 0 0 0.2rem #399bea inset, 0 0.1rem 0.2rem rgba(0, 0, 0, 0.1) !important;
        background-color: #399BEA
      }

      .error-http-button:hover {
        background-color: #399BEA;
        box-shadow: 0 0 0 0.1rem rgba(0, 0, 0, 0.1) inset, 0 0.1rem 0.2rem rgba(0, 0, 0, 0.15)
      }

      .error-http-button:active {
        background-color: #2D7BBB;
        box-shadow: 0 0 0 0.1rem rgba(0, 0, 0, 0.1) inset, 0 0.2rem 0.2rem rgba(0, 0, 0, 0.1) inset !important
      }

      .error-http-details {
        margin: 1.6rem 0;
        overflow: hidden;
        border-radius: 2px;
        border: 1px solid rgba(0, 0, 0, 0.1);
      }

      .error-http-details h2 {
        font-size: 1.4rem;
        line-height: 1.6rem;
        font-weight: normal;
        color: #404040;
        margin: 0;
        padding: 16px;
        border-bottom: 1px solid rgba(0, 0, 0, 0.1);
        background-color: #FAFAFA;
        font-weight: bold;
      }

      .error-http-details ul {
        margin: 0;
        list-style: none;
        padding: 0;
        overflow: auto;
        font-family: 'Lucida Sans Console', 'Monaco', 'Menlo', monospace;
        font-size: 1.2rem;
        line-height: 1.5;
        color: #404040;
        background-color: #FFF;
        max-height: 30vh;
        overflow: auto;
      }

      .error-http-details li {
        padding: 8px 16px;
        display: block;
      }

      .error-http-details li:not(:last-child) {
        border-bottom: 1px solid rgba(0, 0, 0, 0.1);
      }

      .error-http-details pre {
        font-family: inherit;
        line-height: inherit;
        font-size: 1.2rem;
      }

      @media screen and (max-width: 768px), (max-height: 768px) {
        .error-http-details ul {
          max-height: none;
        }
      }

      .text-center {
        text-align: center;
      }
    </style>`;