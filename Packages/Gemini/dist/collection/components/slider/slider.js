import { Component, Prop, Element, h, Host, State, Watch } from "@stencil/core";
export class GxgSlider {
    constructor() {
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
    watchHandler() {
        this.updateLabel();
    }
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
        this.updateLabel();
        //Resize Observer
        const myObserver = new ResizeObserver(entries => {
            entries.forEach(() => {
                this.updateLabel();
            });
        });
        myObserver.observe(this.el.shadowRoot.querySelector(".range-slider"));
    }
    updateLabel() {
        const rangeLabel = this.el.shadowRoot.querySelector(".rs-label");
        const labelPosition = this.value / this.max;
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
    }
    updateBoxValue() {
        this.el.shadowRoot.getElementById("actual-value").innerHTML = this.value.toString();
    }
    calculateWidth() {
        return this.el.shadowRoot.querySelector(".range-slider").clientWidth - 22;
    }
    rangeSliderChanged() {
        this.value = parseInt(this.el.shadowRoot.getElementById("rs-range-line")
            .value);
        this.updateLabel();
    }
    render() {
        return (h(Host, { class: {
                disabled: this.disabled
            }, style: { maxWidth: this.maxWidth } },
            h("div", { class: "container" },
                h("div", { class: "range-slider" },
                    h("span", { class: "rs-label" }, "0"),
                    h("input", { onInput: this.rangeSliderChanged.bind(this), id: "rs-range-line", class: "rs-range", type: "range", min: "0", max: this.max, value: this.value, step: "1", "aria-valuemin": "0", "aria-valuemax": this.max, "aria-valuenow": this.value })),
                h("div", { class: "box-value" },
                    h("label", { class: "label" }, this.label),
                    h("span", { id: "actual-value" })))));
    }
    static get is() { return "gxg-slider"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["slider.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["slider.css"]
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
                "text": "The state of the slider, whether is disabled or not."
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
            "reflect": false,
            "defaultValue": "100"
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
                "text": "The initial value"
            },
            "attribute": "value",
            "reflect": true,
            "defaultValue": "0"
        },
        "maxWidth": {
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
                "text": "The slider max. width"
            },
            "attribute": "max-width",
            "reflect": false,
            "defaultValue": "\"100%\""
        }
    }; }
    static get states() { return {
        "rtl": {}
    }; }
    static get elementRef() { return "el"; }
    static get watchers() { return [{
            "propName": "value",
            "methodName": "watchHandler"
        }]; }
}
