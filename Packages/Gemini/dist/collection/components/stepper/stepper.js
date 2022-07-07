import { Component, Event, Element, Prop, h, Host, Watch, State } from "@stencil/core";
export class GxgStepper {
    constructor() {
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
        return (h(Host, { class: {
                rtl: this.rtl
            } },
            h("label", { class: "label" }, this.label),
            h("div", { class: "outer-wrapper" },
                h("button", { ref: el => (this.minusButton = el), class: "button button--minus", onClick: this.minus.bind(this), tabindex: "0" }, "-"),
                h("span", { class: "value-container" },
                    h("span", { class: "value-container__value" }, this.value)),
                h("button", { ref: el => (this.plusButton = el), class: "button button--plus", onClick: this.plus.bind(this), tabindex: "0" }, "+"))));
    }
    static get is() { return "gxg-stepper"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["stepper.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["stepper.css"]
    }; }
    static get properties() { return {
        "disabled": {
            "type": "boolean",
            "mutable": false,
            "complexType": {
                "original": "boolean",
                "resolved": "boolean",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The state of the stepper, whether is disabled or not."
            },
            "attribute": "disabled",
            "reflect": true,
            "defaultValue": "false"
        },
        "label": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "string",
                "resolved": "string",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The label"
            },
            "attribute": "label",
            "reflect": false,
            "defaultValue": "\"Label\""
        },
        "labelPosition": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "LabelPosition",
                "resolved": "\"above\" | \"start\"",
                "references": {
                    "LabelPosition": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The label position"
            },
            "attribute": "label-position",
            "reflect": true,
            "defaultValue": "\"above\""
        },
        "value": {
            "type": "number",
            "mutable": false,
            "complexType": {
                "original": "number",
                "resolved": "number",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The initial vaule"
            },
            "attribute": "value",
            "reflect": true,
            "defaultValue": "0"
        },
        "max": {
            "type": "number",
            "mutable": false,
            "complexType": {
                "original": "number",
                "resolved": "number",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The max. value"
            },
            "attribute": "max",
            "reflect": true,
            "defaultValue": "10000"
        },
        "min": {
            "type": "number",
            "mutable": false,
            "complexType": {
                "original": "number",
                "resolved": "number",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The min. value"
            },
            "attribute": "min",
            "reflect": true,
            "defaultValue": "0"
        }
    }; }
    static get states() { return {
        "rtl": {}
    }; }
    static get events() { return [{
            "method": "stepperInput",
            "name": "stepperInput",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": ""
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }]; }
    static get elementRef() { return "el"; }
    static get watchers() { return [{
            "propName": "value",
            "methodName": "watchHandler"
        }]; }
}
