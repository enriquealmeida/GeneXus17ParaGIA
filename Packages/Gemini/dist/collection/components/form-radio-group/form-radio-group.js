import { Component, Prop, h, Listen, Element, Host } from "@stencil/core";
import { requiredLabel, formMessage } from "../../common";
export class GxgFormRadioGroup {
    constructor() {
        this.isRequiredError = false;
        /**
         * Make the radio-buttons required
         */
        this.required = false;
    }
    /*********************************
    METHODS
    *********************************/
    keyPressedHandler(event) {
        const currentActiveRadio = this.el.querySelector("gxg-form-radio[checked]");
        currentActiveRadio.shadowRoot
            .querySelector("input")
            .setAttribute("tabindex", "-1");
        currentActiveRadio.removeAttribute("checked");
        if (event.detail.direction === "next") {
            const nextSibling = currentActiveRadio.nextElementSibling;
            if (nextSibling !== null) {
                nextSibling.shadowRoot
                    .querySelector("input")
                    .setAttribute("tabindex", "0");
                nextSibling.setAttribute("checked", "checked");
                nextSibling.shadowRoot.querySelector("input").focus();
            }
            else {
                const firstRadio = this.el.querySelector("gxg-form-radio:first-child");
                firstRadio.setAttribute("checked", "checked");
                firstRadio.shadowRoot.querySelector("input").focus();
            }
        }
        else if (event.detail.direction === "previous") {
            const prevSibling = currentActiveRadio.previousElementSibling;
            if (prevSibling !== null) {
                prevSibling.shadowRoot
                    .querySelector("input")
                    .setAttribute("tabindex", "0");
                prevSibling.setAttribute("checked", "checked");
                prevSibling.shadowRoot.querySelector("input").focus();
            }
            else {
                const lastRadio = this.el.querySelector("gxg-form-radio:last-child");
                lastRadio.setAttribute("checked", "checked");
                lastRadio.shadowRoot.querySelector("input").focus();
            }
        }
    }
    radioClickedHandler(event) {
        const radioButtonsNodeList = this.el.querySelectorAll("gxg-form-radio");
        radioButtonsNodeList.forEach(function (currentRadio) {
            if (event.detail["id"] === currentRadio.getAttribute("radio-id")) {
                currentRadio.setAttribute("checked", "checked");
                currentRadio.shadowRoot
                    .querySelector("input")
                    .setAttribute("tabindex", "0");
            }
            else {
                currentRadio.removeAttribute("checked");
                currentRadio.shadowRoot.querySelector("input");
                currentRadio.shadowRoot
                    .querySelector("input")
                    .setAttribute("tabindex", "-1");
            }
        }, "myThisArg");
    }
    componentDidLoad() {
        const firstRadioButton = this.el.querySelector("gxg-option:first-child");
        firstRadioButton.setAttribute("required", "required");
    }
    labelFunc() {
        if (this.label) {
            return (h("span", { class: "label" },
                this.label,
                requiredLabel(this)));
        }
    }
    render() {
        return (h(Host, null,
            this.labelFunc(),
            h("slot", null),
            formMessage(this.isRequiredError ? (h("gxg-form-message", { type: "error", key: "required-error" }, this.requiredMessage)) : null)));
    }
    static get is() { return "gxg-form-radio-group"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["form-radio-group.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["form-radio-group.css"]
    }; }
    static get properties() { return {
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
                "text": "The radio group label"
            },
            "attribute": "label",
            "reflect": false
        },
        "requiredMessage": {
            "type": "string",
            "mutable": true,
            "complexType": {
                "original": "string",
                "resolved": "string",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The required message if this input is required and no value is provided (optional). If this is not provided, the default browser required message will show up"
            },
            "attribute": "required-message",
            "reflect": false
        },
        "required": {
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
                "text": "Make the radio-buttons required"
            },
            "attribute": "required",
            "reflect": false,
            "defaultValue": "false"
        }
    }; }
    static get elementRef() { return "el"; }
    static get listeners() { return [{
            "name": "keyPressed",
            "method": "keyPressedHandler",
            "target": undefined,
            "capture": false,
            "passive": false
        }, {
            "name": "changeInternal",
            "method": "radioClickedHandler",
            "target": undefined,
            "capture": false,
            "passive": false
        }]; }
}
