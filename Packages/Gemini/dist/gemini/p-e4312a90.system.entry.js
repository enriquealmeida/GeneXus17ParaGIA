System.register([ "./p-d8a20c31.system.js", "./p-08984d7d.system.js" ], (function(t) {
  "use strict";
  var o, e, r, i, n;
  return {
    setters: [ function(t) {
      o = t.r, e = t.h, r = t.H, i = t.g;
    }, function(t) {
      n = t.s;
    } ],
    execute: function() {
      t("gxg_pill", /** @class */ function() {
        function class_1(t) {
          o(this, t), 
          /**
                     * The presence of this attribute disables the pillgit a
                     */
          this.disabled = !1, 
          /**
                     * The icon
                     */
          this.icon = void 0, 
          /**
                     * The presence of this attribute sets auto-height. Usefull when the text overflows.
                     */
          this.heightAuto = !1, 
          /**
                     * The type of pill
                     */
          this.type = "static";
        }
        return class_1.prototype.removeButtonFunc = function() {
          var t = this;
          this.el.classList.add("hide"), setTimeout((function() {
            t.el.remove();
          }), 250);
        }, class_1.prototype.iconType = function() {
          return void 0 !== this.icon ? this.icon : (console.log("empty icon"), "gemini-tools/empty");
        }, class_1.prototype.iconColor = function() {
          return this.disabled ? "disabled" : "success";
        }, class_1.prototype.render = function() {
          return e(r, {
            tabindex: "0",
            class: {
              "no-icon": void 0 === this.icon,
              "has-icon": void 0 !== this.icon,
              large: n.large
            }
          }, e("gxg-icon", {
            class: "custom",
            type: this.iconType(),
            size: "regular",
            color: this.iconColor()
          }), e("span", {
            class: "title"
          }, e("slot", null)), "button-with-action" === this.type || "static-with-action" === this.type ? e("gxg-icon", {
            class: "clear-button",
            type: "gemini-tools/close",
            size: "small",
            color: "onbackground",
            onClick: this.removeButtonFunc.bind(this)
          }) : null);
        }, Object.defineProperty(class_1.prototype, "el", {
          get: function() {
            return i(this);
          },
          enumerable: !1,
          configurable: !0
        }), class_1;
      }()).style = "/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{display:-ms-inline-flexbox;display:inline-flex;-ms-flex-align:center;align-items:center;border-color:var(--gray-02);border-width:var(--border-width-sm);border-style:var(--border-style-regular);border-radius:11px;-webkit-padding-start:var(--spacing-comp-02);padding-inline-start:var(--spacing-comp-02);-webkit-padding-end:var(--spacing-comp-03);padding-inline-end:var(--spacing-comp-03);opacity:1;height:var(--spacing-comp-04);-webkit-box-sizing:border-box;box-sizing:border-box;-webkit-transition-property:opacity;transition-property:opacity;-webkit-transition-duration:var(--ui-animaton-speed);transition-duration:var(--ui-animaton-speed);-webkit-transition-timing-function:ease;transition-timing-function:ease}:host .title{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}:host .clear-button{-webkit-margin-start:10px;margin-inline-start:10px;cursor:pointer;opacity:0.7}:host .clear-button:hover{opacity:1}:host gxg-icon{-webkit-transform:scale(0.84);transform:scale(0.84)}:host gxg-icon.clear-button{-webkit-transform:scale(0.65);transform:scale(0.65)}:host(.no-icon) gxg-icon.custom{width:0}:host(.has-icon){-webkit-padding-start:0;padding-inline-start:0}:host([type=static]),:host([type=static-with-action]){background-color:var(--gray-01);border-color:var(--gray-01);font-style:italic}:host([type=static-with-action]),:host([type=button-with-action]){-webkit-padding-end:0;padding-inline-end:0}:host([type=button]){-webkit-padding-end:var(--spacing-comp-02);padding-inline-end:var(--spacing-comp-02)}:host([type=button]:hover),:host([type=button-with-action]:hover){border-color:var(--gray-07);cursor:pointer}:host([type=button]:focus),:host([type=button-with-action]:focus){border-color:var(--gray-07)}:host([type=button]:active),:host([type=button-with-action]:active){border-color:var(--gray-07)}:host([type=button-with-action]){-webkit-padding-end:2px;padding-inline-end:2px}:host([type=button-with-action]) .clear-button{cursor:pointer;opacity:0.5;-webkit-margin-start:8px;margin-inline-start:8px}:host([type=button-with-action]) .clear-button:hover{opacity:0.75}:host(.hide){opacity:0}:host(:focus){outline:none}:host([disabled]){pointer-events:none}:host([disabled]) .title{color:var(--gray-03)}:host([disabled]) gxg-icon{opacity:0.3}:host([disabled][type=button]),:host([disabled][type=button-with-action]){border-color:var(--gray-03)}:host([disabled]:focus){-webkit-box-shadow:none;box-shadow:none}:host([height-auto]){height:auto}:host(.large){height:20px}:host(.large) .title{font-size:var(--font-size-lg)}:host(.large) gxg-icon.clear-button{-webkit-transform:scale(0.85);transform:scale(0.85)}";
    }
  };
}));