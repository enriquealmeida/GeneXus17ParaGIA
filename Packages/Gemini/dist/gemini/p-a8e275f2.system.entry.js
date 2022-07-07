System.register([ "./p-d8a20c31.system.js" ], (function(t) {
  "use strict";
  var e, r, o, a, i;
  return {
    setters: [ function(t) {
      e = t.r, r = t.c, o = t.h, a = t.H, i = t.g;
    } ],
    execute: function() {
      t("gxg_form_radio", /** @class */ function() {
        function class_1(t) {
          e(this, t), this.change = r(this, "change", 7), this.changeInternal = r(this, "changeInternal", 7), 
          this.keyPressed = r(this, "keyPressed", 7), 
          /**
                     * Styles the radio-button with error attributes
                     */
          this.error = !1, 
          /**
                     * The presence of this attribute makes the radio selected by default
                     */
          this.checked = !1, 
          /**
                     * The presence of this attribute disables the radio
                     */
          this.disabled = !1;
        }
        /*********************************
                METHODS
                *********************************/        return class_1.prototype.componentDidLoad = function() {
          this.checked && this.radioInput.setAttribute("checked", "checked"), this.disabled && this.checked && (this.radioInput.removeAttribute("checked"), 
          this.checked = !1);
        }, class_1.prototype.watchHandler = function(t) {
          !1 === t && this.radioInput.removeAttribute("checked"), !0 === t && (this.radioInput.setAttribute("checked", "checked"), 
          this.change.emit({
            id: this.RadioId,
            value: this.value
          }));
        }, class_1.prototype.selectRadio = function() {
          this.changeInternal.emit({
            id: this.RadioId,
            value: this.value
          });
        }, class_1.prototype.handlerOnKeyDown = function(t) {
          9 == t.keyCode ? 
          //tab key was pressed
          t.shiftKey ? 
          //shift key was also pressed
          this.keyPressed.emit({
            direction: "previous-tab"
          }) : this.keyPressed.emit({
            direction: "next-tab"
          }) : 37 != t.keyCode && 38 != t.keyCode || (
          //arrow-left, or arrow-up key was pressed. focus should be positioned on the previous radiobtn.
          t.preventDefault(), this.keyPressed.emit({
            direction: "previous"
          })), 39 != t.keyCode && 40 != t.keyCode || (
          //arrow-right, or arrow-down key was pressed. focus should be positioned on the next radiobtn
          t.preventDefault(), this.keyPressed.emit({
            direction: "next"
          }));
        }, class_1.prototype.render = function() {
          var t = this;
          return o(a, null, o("label", {
            class: "label"
          }, o("input", {
            ref: function(e) {
              return t.radioInput = e;
            },
            type: "radio",
            name: this.name,
            id: this.RadioId,
            value: this.value,
            onClick: this.selectRadio.bind(this),
            disabled: this.disabled,
            onKeyDown: this.handlerOnKeyDown.bind(this)
          }), o("span", {
            class: {
              radiobtn: !0,
              "radiobtn--error": this.error
            }
          }), this.label));
        }, Object.defineProperty(class_1.prototype, "el", {
          get: function() {
            return i(this);
          },
          enumerable: !1,
          configurable: !0
        }), Object.defineProperty(class_1, "watchers", {
          get: function() {
            return {
              checked: [ "watchHandler" ]
            };
          },
          enumerable: !1,
          configurable: !0
        }), class_1;
      }()).style = '/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{}:host input:focus+.radiobtn{-webkit-box-shadow:0px 0px 0px 1px var(--color-primary-active);box-shadow:0px 0px 0px 1px var(--color-primary-active)}:host .label{display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;position:relative;cursor:pointer;font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;display:flex;margin-bottom:var(--spacing-lay-xs)}:host .label input{position:absolute;opacity:0;cursor:pointer}:host .checkmark{position:absolute;top:0;left:0;height:25px;width:25px;background-color:transparent}:host .label:hover input~.checkmark{background-color:transparent}:host .label input:checked~.checkmark{background-color:#2196f3}:host .checkmark:after{content:"";position:absolute;display:none}:host .label input:checked~.checkmark:after{display:block}:host .label .checkmark:after{left:10px;top:6px;width:7px;height:12px;border:solid white;border-width:0 3px 3px 0;-webkit-transform:rotate(45deg);-ms-transform:rotate(45deg);transform:rotate(45deg)}:host .radiobtn{display:inline-block;height:var(--spacing-comp-04);width:var(--spacing-comp-04);background-color:transparent;border-radius:50%;border-color:var(--gray-04);border-style:var(--border-style-regular);border-width:var(--border-width-sm);position:relative;-webkit-margin-end:var(--spacing-comp-02);margin-inline-end:var(--spacing-comp-02);-ms-flex-negative:0;flex-shrink:0;-webkit-box-shadow:0px 0px 0px 1px transparent;box-shadow:0px 0px 0px 1px transparent;-webkit-box-sizing:border-box;box-sizing:border-box}:host .radiobtn--error{border-color:var(--color-error-dark) !important;background:var(--color-error-light) !important}:host .radiobtn--error:after{background-color:var(--color-error-dark) !important}:host input:focus+.radiobtn--error{-webkit-box-shadow:0px 0px 0px 1px var(--color-error-dark);box-shadow:0px 0px 0px 1px var(--color-error-dark)}:host .label:hover input~.radiobtn{background-color:transparent}:host .label input[checked]~.radiobtn{background-color:transparent;border-color:var(--color-primary-active)}:host .radiobtn:after{content:"";position:absolute;display:block;width:8px;height:8px;-webkit-transform:scale(0, 0);transform:scale(0, 0);-webkit-transition-property:-webkit-transform;transition-property:-webkit-transform;transition-property:transform;transition-property:transform, -webkit-transform;-webkit-transition-duration:var(--ui-animaton-speed);transition-duration:var(--ui-animaton-speed);-webkit-transition-timing-function:ease;transition-timing-function:ease}:host .label input[checked]~.radiobtn:after{display:block;-webkit-transform:scale(1, 1);transform:scale(1, 1)}:host .label input[disabled]~.radiobtn{border-color:var(--gray-04)}:host .label input[disabled]~.radiobtn:after{background-color:var(--gray-04)}:host .label .radiobtn:after{top:3px;left:3px;border-radius:50%;background:var(--color-primary-active)}:host([disabled=true]) label{cursor:not-allowed}';
    }
  };
}));