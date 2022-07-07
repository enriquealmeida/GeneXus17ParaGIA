'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

const index = require('./index-1115561c.js');

const formCheckboxCss = "/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{display:-ms-flexbox;display:flex}:host .label{display:block;margin-bottom:var(--spacing-lay-m)}:host .label:last-child{margin-bottom:0}:host input:focus+.checkmark{-webkit-box-shadow:0px 0px 0px var(--border-width-sm) var(--color-primary-active);box-shadow:0px 0px 0px var(--border-width-sm) var(--color-primary-active);border-color:var(--color-primary-active) !important}:host input[disabled]{cursor:not-allowed}:host input[disabled]+.checkmark{border-color:var(--gray-04) !important;pointer-events:none}:host input[disabled]+.checkmark:after{border-color:var(--gray-04) !important}:host([inline]) .outer-wrapper{display:-ms-flexbox;display:flex}:host([inline]) .label{-webkit-margin-end:var(--spacing-comp-04);margin-inline-end:var(--spacing-comp-04);margin-bottom:0}:host{}:host .label{display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;position:relative;cursor:pointer;font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;display:flex;margin-bottom:var(--spacing-lay-xs)}:host .label__required{font-size:var(--font-size-xs);padding-left:var(--spacing-lay-xs)}:host .label input{position:absolute;opacity:0;cursor:pointer;height:0;width:0}:host .label input[on-focus]+.checkmark{-webkit-box-shadow:inset 0px 0px 0px 1px var(--color-primary-active);box-shadow:inset 0px 0px 0px 1px var(--color-primary-active);border-color:var(--color-primary-active) !important}:host .checkmark:focus{-webkit-box-shadow:none;box-shadow:none}:host .checkmark{display:inline-block;position:relative;-webkit-margin-end:var(--spacing-comp-02);margin-inline-end:var(--spacing-comp-02);height:var(--spacing-comp-04);width:var(--spacing-comp-04);border-width:var(--border-width-sm);border-style:var(--border-style-regular);border-radius:var(--border-radius-md);border-color:var(--gray-04);-webkit-box-sizing:border-box;box-sizing:border-box;-ms-flex-negative:0;flex-shrink:0;background:var(--color-background)}:host .checkmark.no-label{-webkit-margin-end:0;margin-inline-end:0}:host .checkmark:after{content:\"\";position:absolute;display:none}:host .label input~.checkmark:after{display:block;border-color:transparent}:host .label input:checked~.checkmark:after{display:block;border-color:var(--color-primary-hover);-webkit-transition-property:border-color;transition-property:border-color;-webkit-transition-duration:var(--ui-animaton-speed);transition-duration:var(--ui-animaton-speed);-webkit-transition-timing-function:ease;transition-timing-function:ease}:host .label input:checked~.checkmark{border-color:var(--color-primary-hover)}:host .label .checkmark:after{left:5px;top:2px;width:3px;height:7px;border:solid;border-color:var(--color-primary-hover);border-width:0 2px 2px 0;-webkit-transform:rotate(45deg);-ms-transform:rotate(45deg);transform:rotate(45deg)}:host([indeterminate]) .label input:checked~.checkmark:after{display:block;width:10px;height:2px;-webkit-transform:rotate(0deg);transform:rotate(0deg);border:0;background-color:var(--color-primary-hover);left:2px;top:6px}:host([disabled=true]) label{cursor:not-allowed}";

const GxgFormCheckbox = class {
    constructor(hostRef) {
        index.registerInstance(this, hostRef);
        this.change = index.createEvent(this, "change", 7);
        /**
         * The presence of this attribute makes the checkbox checked by default
         */
        this.checked = false;
        /**
         * The presence of this attribute makes the checkbox indeterminate
         */
        this.indeterminate = false;
        /**
         * The presence of this attribute disables the checkbox
         */
        this.disabled = false;
    }
    /*********************************
    METHODS
    *********************************/
    compontentDidLoad() {
        if (this.checked && this.disabled) {
            this.checked = false;
            this.checkboxInput.removeAttribute("checked");
        }
    }
    changed() {
        this.checked = this.checkboxInput.checked;
        this.change.emit({
            "checkbox id": this.checkboxId,
            "checkbox value": this.checked,
        });
    }
    handlerOnKeyUp(event) {
        if (event.keyCode == 13) {
            //Enter key was pressed
            if (!this.checked) {
                this.el.setAttribute("checked", "true");
            }
            else {
                this.el.removeAttribute("checked");
            }
            this.change.emit({
                "checkbox id": this.checkboxId,
                "checkbox value": this.checked,
            });
        }
    }
    ariaChecked() {
        if (this.checked) {
            return "true";
        }
        else {
            return "false";
        }
    }
    handleInputClick(e) {
        e.stopPropagation();
    }
    render() {
        return (index.h(index.Host, { role: "checkbox", value: this.value, "aria-checked": this.ariaChecked, "aria-label": this.label }, index.h("label", { class: "label" }, index.h("input", { ref: (el) => (this.checkboxInput = el), type: "checkbox", checked: this.checked, class: "input", id: this.checkboxId, name: this.name, value: this.value, disabled: this.disabled, onChange: this.changed.bind(this), onKeyUp: this.handlerOnKeyUp.bind(this), tabindex: "0", onClick: this.handleInputClick }), index.h("span", { class: { checkmark: true, "no-label": !this.label }, role: "checkbox" }), this.label ? this.label : null)));
    }
    get el() { return index.getElement(this); }
};
GxgFormCheckbox.style = formCheckboxCss;

exports.gxg_form_checkbox = GxgFormCheckbox;
