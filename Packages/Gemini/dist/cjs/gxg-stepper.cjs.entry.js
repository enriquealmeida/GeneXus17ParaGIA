'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

const index = require('./index-1115561c.js');

const stepperCss = "/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{display:block}:host .label{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;display:-ms-flexbox;display:flex;margin-bottom:var(--spacing-lay-xs)}:host .outer-wrapper{display:inline-block;background-color:var(--gray-01);border:1px solid transparent;border-radius:var(--border-radius-md);-webkit-transition-property:background-color;transition-property:background-color;-webkit-transition-duration:var(--ui-animaton-speed);transition-duration:var(--ui-animaton-speed);-webkit-transition-timing-function:ease;transition-timing-function:ease;transition-property:background-color;-webkit-transition-duration:0.1s;transition-duration:0.1s;transition-timing-function:ease}:host .value-container{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;display:-ms-flexbox;display:flex;margin-bottom:var(--spacing-lay-xs);-ms-flex-align:center;align-items:center;-ms-flex-pack:center;justify-content:center;width:36px;display:-ms-inline-flexbox;display:inline-flex;margin-bottom:0;background-color:transparent;position:relative;top:-2px}:host .button{display:-ms-inline-flexbox;display:inline-flex;width:var(--spacing-comp-05);height:20px;-ms-flex-align:center;align-items:center;-ms-flex-pack:center;justify-content:center;border:none;background-color:var(--color-primary-active);color:var(--color-on-primary);cursor:pointer;font-weight:var(--font-weight-regular);border:1px solid transparent;-webkit-box-shadow:0px 0px 0px 1px var(--color-primary-active);box-shadow:0px 0px 0px 1px var(--color-primary-active)}:host .button:focus{outline:none;border-color:var(--color-on-primary);-webkit-box-shadow:0px 0px 0px 1px var(--color-primary-enabled);box-shadow:0px 0px 0px 1px var(--color-primary-enabled)}:host .button--minus{border-top-left-radius:var(--border-radius-md);border-bottom-left-radius:var(--border-radius-md)}:host .button--plus{border-top-right-radius:var(--border-radius-md);border-bottom-right-radius:var(--border-radius-md)}:host .button:hover{background-color:var(--color-primary-hover);-webkit-box-shadow:0px 0px 0px 1px var(--color-primary-hover);box-shadow:0px 0px 0px 1px var(--color-primary-hover)}:host(.rtl) .button--minus{border-top-right-radius:var(--border-radius-md);border-bottom-right-radius:var(--border-radius-md);border-top-left-radius:0;border-bottom-left-radius:0}:host(.rtl) .button--plus{border-top-left-radius:var(--border-radius-md);border-bottom-left-radius:var(--border-radius-md);border-top-right-radius:0;border-bottom-right-radius:0}:host(:focus){outline:none}:host(:focus) .outer-wrapper{border-color:var(--color-on-primary);-webkit-box-shadow:0px 0px 0px 1px var(--color-primary-enabled);box-shadow:0px 0px 0px 1px var(--color-primary-enabled)}:host([disabled]) .outer-wrapper,:host([disabled]) .button{cursor:not-allowed}:host([disabled]) .button{background-color:var(--color-primary-disabled)}:host .button[disabled]{background-color:var(--color-primary-disabled);-webkit-box-shadow:0px 0px 0px 1px var(--color-primary-disabled);box-shadow:0px 0px 0px 1px var(--color-primary-disabled)}:host([label-position=start]){display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center}:host([label-position=start]) .label{margin-bottom:0;-webkit-margin-end:var(--spacing-comp-01);margin-inline-end:var(--spacing-comp-01)}";

const GxgStepper = class {
    constructor(hostRef) {
        index.registerInstance(this, hostRef);
        this.stepperInput = index.createEvent(this, "stepperInput", 7);
        /**
         * The state of the stepper, whether is disabled or not.
         */
        this.disabled = false;
        /**
         * The label
         */
        this.label = "Label";
        /**
         * The label position
         */
        this.labelPosition = "above";
        /**
         * The initial vaule
         */
        this.value = 0;
        /**
         * The max. value
         */
        this.max = 10000;
        /**
         * The min. value
         */
        this.min = 0;
        /**
         * Reading direction
         */
        this.rtl = false;
    }
    /*********************************
    METHODS
    *********************************/
    componentDidLoad() {
        //Reading Direction
        const dirHtml = document
            .getElementsByTagName("html")[0]
            .getAttribute("dir");
        const dirBody = document
            .getElementsByTagName("body")[0]
            .getAttribute("dir");
        if (dirHtml === "rtl" || dirBody === "rtl") {
            this.rtl = true;
        }
        //disabled
        if (this.disabled) {
            this.plusButton.setAttribute("disabled", "disabled");
            this.minusButton.setAttribute("disabled", "disabled");
        }
        //initial value
        if (this.value < this.min || this.value > this.max) {
            this.value = this.min;
        }
        //min value
        if (this.value === this.min) {
            this.minusButton.setAttribute("disabled", "disabled");
        }
        //max value
        if (this.value === this.max) {
            this.plusButton.setAttribute("disabled", "disabled");
        }
    }
    minus() {
        if (this.value === this.max) {
            this.plusButton.removeAttribute("disabled");
        }
        if (this.value >= this.min) {
            this.value = this.value - 1;
        }
        if (this.value === this.min) {
            this.minusButton.setAttribute("disabled", "disabled");
        }
    }
    plus() {
        if (this.value === this.min) {
            this.minusButton.removeAttribute("disabled");
        }
        if (this.value <= this.max) {
            this.value = this.value + 1;
        }
        if (this.value === this.max) {
            this.plusButton.setAttribute("disabled", "disabled");
        }
    }
    watchHandler(newValue) {
        this.stepperInput.emit(newValue);
    }
    render() {
        return (index.h(index.Host, { class: {
                rtl: this.rtl
            } }, index.h("label", { class: "label" }, this.label), index.h("div", { class: "outer-wrapper" }, index.h("button", { ref: el => (this.minusButton = el), class: "button button--minus", onClick: this.minus.bind(this), tabindex: "0" }, "-"), index.h("span", { class: "value-container" }, index.h("span", { class: "value-container__value" }, this.value)), index.h("button", { ref: el => (this.plusButton = el), class: "button button--plus", onClick: this.plus.bind(this), tabindex: "0" }, "+"))));
    }
    get el() { return index.getElement(this); }
    static get watchers() { return {
        "value": ["watchHandler"]
    }; }
};
GxgStepper.style = stepperCss;

exports.gxg_stepper = GxgStepper;
