import '@polymer/polymer/lib/elements/custom-style.js';

const documentContainer = document.createElement('template');

documentContainer.innerHTML = `<custom-style>
      <style>
        html {
          --lumo-font-family: 'Panton';
          --lumo-font-weight: normal;
        }
      </style>
    </custom-style>`;

document.head.appendChild(documentContainer.content);