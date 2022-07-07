import { r as registerInstance, h, H as Host, g as getElement } from './index-09b1517f.js';
var sliderCss = "/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{width:100%;display:block}:host .container{display:-ms-inline-flexbox;display:inline-flex;width:100%}:host .box-value{margin-top:30px;display:-ms-flexbox;display:flex;-ms-flex-direction:column;flex-direction:column;font-size:20px;color:var(--color-on-secondary);-webkit-margin-start:10px;margin-inline-start:10px}:host .box-value #actual-value{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;display:-ms-flexbox;display:flex;margin-bottom:var(--spacing-lay-xs);display:-ms-inline-flexbox;display:inline-flex;-ms-flex-align:center;align-items:center;-ms-flex-pack:center;justify-content:center;width:var(--spacing-comp-06);height:20px;background-color:var(--color-on-primary);border-width:var(--border-width-sm);border-color:var(--gray-04);border-style:var(--border-style-regular)}:host .label{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}:host .range-slider{width:100%}:host .rs-range{width:100%;-webkit-appearance:none}:host .rs-range:focus{outline:none}:host .rs-range::-webkit-slider-runnable-track{width:100%;height:2px;cursor:pointer;-webkit-box-shadow:none;box-shadow:none;background-color:var(--color-secondary);border-radius:0px;border:0px solid #010101}:host .rs-range::-webkit-slider-thumb{height:var(--spacing-comp-03);width:var(--spacing-comp-03);border-radius:50%;background-color:var(--color-primary-active);cursor:pointer;-webkit-appearance:none;margin-top:-5px}:host .rs-label{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;display:-ms-flexbox;display:flex;margin-bottom:var(--spacing-lay-xs);color:var(--color-on-primary);font-size:var(--font-size-xs);position:relative;-webkit-transform-origin:center center;transform-origin:center center;display:flex;-ms-flex-align:center;align-items:center;-ms-flex-pack:center;justify-content:center;border-radius:var(--border-radius-md);width:var(--spacing-comp-05);height:var(--spacing-comp-04);margin-top:20px;margin-left:0;left:attr(value);background-color:var(--color-primary-active)}:host .rs-label::after{content:\"\";width:0px;height:0px;bottom:-5px;border-left:4px solid transparent;border-right:4px solid transparent;border-top:6px solid var(--color-primary-active);position:absolute;left:50%;margin-left:-4px}:host([disabled]){pointer-events:none}:host([disabled]) .rs-range::-webkit-slider-runnable-track{background-color:var(--color-on-disabled)}:host([disabled]) .rs-range::-webkit-slider-thumb{background-color:var(--gray-04)}:host([disabled]) .rs-label{color:var(--gray-04);background-color:var(--color-on-disabled)}:host([disabled]) .rs-label::after{border-top-color:var(--color-on-disabled)}:host([disabled]) .box-value #actual-value{color:var(--gray-04)}";
var GxgSlider = /** @class */ (function () {
    function GxgSlider(hostRef) {
        registerInstance(this, hostRef);
        /**
         * The state of the slider, whether is disabled or not.
         */
        this.disabled = false;
        /**
         * The label
         */
        this.label = "Label";
        /**
         * The max. value
         */
        this.max = 100;
        /**
         * The initial value
         */
        this.value = 0;
        /**
         * The slider max. width
         */
        this.maxWidth = "100%";
        /**
         * Reading direction
         */
        this.rtl = false;
    }
    GxgSlider.prototype.watchHandler = function () {
        this.updateLabel();
    };
    GxgSlider.prototype.componentDidLoad = function () {
        var _this = this;
        //Reading Direction
        var dirHtml = document
            .getElementsByTagName("html")[0]
            .getAttribute("dir");
        var dirBody = document
            .getElementsByTagName("body")[0]
            .getAttribute("dir");
        if (dirHtml === "rtl" || dirBody === "rtl") {
            this.rtl = true;
        }
        this.updateLabel();
        //Resize Observer
        var myObserver = new ResizeObserver(function (entries) {
            entries.forEach(function () {
                _this.updateLabel();
            });
        });
        myObserver.observe(this.el.shadowRoot.querySelector(".range-slider"));
    };
    GxgSlider.prototype.updateLabel = function () {
        var rangeLabel = this.el.shadowRoot.querySelector(".rs-label");
        var labelPosition = this.value / this.max;
        if (this.rtl) {
            rangeLabel.style.right =
                labelPosition * this.calculateWidth() + "px";
        }
        else {
            rangeLabel.style.left =
                labelPosition * this.calculateWidth() + "px";
        }
        rangeLabel.innerHTML = this.value.toString();
        this.updateBoxValue();
    };
    GxgSlider.prototype.updateBoxValue = function () {
        this.el.shadowRoot.getElementById("actual-value").innerHTML = this.value.toString();
    };
    GxgSlider.prototype.calculateWidth = function () {
        return this.el.shadowRoot.querySelector(".range-slider").clientWidth - 22;
    };
    GxgSlider.prototype.rangeSliderChanged = function () {
        this.value = parseInt(this.el.shadowRoot.getElementById("rs-range-line")
            .value);
        this.updateLabel();
    };
    GxgSlider.prototype.render = function () {
        return (h(Host, { class: {
                disabled: this.disabled
            }, style: { maxWidth: this.maxWidth } }, h("div", { class: "container" }, h("div", { class: "range-slider" }, h("span", { class: "rs-label" }, "0"), h("input", { onInput: this.rangeSliderChanged.bind(this), id: "rs-range-line", class: "rs-range", type: "range", min: "0", max: this.max, value: this.value, step: "1", "aria-valuemin": "0", "aria-valuemax": this.max, "aria-valuenow": this.value })), h("div", { class: "box-value" }, h("label", { class: "label" }, this.label), h("span", { id: "actual-value" })))));
    };
    Object.defineProperty(GxgSlider.prototype, "el", {
        get: function () { return getElement(this); },
        enumerable: false,
        configurable: true
    });
    Object.defineProperty(GxgSlider, "watchers", {
        get: function () {
            return {
                "value": ["watchHandler"]
            };
        },
        enumerable: false,
        configurable: true
    });
    return GxgSlider;
}());
GxgSlider.style = sliderCss;
export { GxgSlider as gxg_slider };
