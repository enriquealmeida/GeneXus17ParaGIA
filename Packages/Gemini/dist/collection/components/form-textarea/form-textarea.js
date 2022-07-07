import { Component, Host, Prop, h, Event } from "@stencil/core";
import { requiredLabel, formMessage, formHandleChange, } from "../../common";
import state from "../store";
export class GxgFormTextarea {
    constructor() {
        this.isRequiredError = false;
        /*********************************
        PROPERTIES & STATE
        *********************************/
        /**
         * The presence of this attribute makes the textarea disabled
         */
        this.disabled = false;
        /**
         * The presence of this attribute gives the component error styles
         */
        this.error = false;
        /**
         * The max-width
         */
        this.maxWidth = "100%";
        /**
         * The presence of this attribute makes the textarea required
         */
        this.required = false;
        /**
         * The number of rows
         */
        this.rows = 4;
        /**
         * The presence of this attribute gives the component warning styles
         */
        this.warning = false;
    }
    /*********************************
    METHODS
    *********************************/
    updateTextareaValue() {
        this.value = this.textArea.value;
    }
    handleInput(e) {
        formHandleChange(this, e.target);
        const target = e.target;
        this.value = target.value;
        this.input.emit(this.value);
    }
    handleChange(e) {
        formHandleChange(this, e.target);
        this.change.emit(this.value);
    }
    render() {
        return (h(Host, { role: "textbox", "aria-label": this.label, style: { maxWidth: this.maxWidth }, class: {
                large: state.large,
            } },
            this.label !== undefined ? (h("label", { class: {
                    label: true,
                } },
                this.label,
                requiredLabel(this))) : (""),
            h("textarea", { ref: (el) => (this.textArea = el), class: {
                    textarea: true,
                    "textarea--error": this.error === true,
                    "textarea--warning": this.warning === true,
                }, placeholder: this.placeholder, disabled: this.disabled, onInput: this.handleInput.bind(this), onChange: this.handleChange.bind(this), value: this.value, rows: this.rows, required: this.required }),
            formMessage(this.isRequiredError ? (h("gxg-form-message", { type: "error", key: "required-error" }, this.requiredMessage)) : null)));
    }
    static get is() { return "gxg-form-textarea"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["form-textarea.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["form-textarea.css"]
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
                "text": "The presence of this attribute makes the textarea disabled"
            },
            "attribute": "disabled",
            "reflect": false,
            "defaultValue": "false"
        },
        "error": {
            "type": "boolean",
            "mutable": true,
            "complexType": {
                "original": "boolean",
                "resolved": "boolean",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The presence of this attribute gives the component error styles"
            },
            "attribute": "error",
            "reflect": false,
            "defaultValue": "false"
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
                "text": "The textarea label"
            },
            "attribute": "label",
            "reflect": true
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
                "text": "The max-width"
            },
            "attribute": "max-width",
            "reflect": false,
            "defaultValue": "\"100%\""
        },
        "placeholder": {
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
                "text": "The textarea placeholder"
            },
            "attribute": "placeholder",
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
                "text": "The presence of this attribute makes the textarea required"
            },
            "attribute": "required",
            "reflect": false,
            "defaultValue": "false"
        },
        "value": {
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
                "text": "The textarea value"
            },
            "attribute": "value",
            "reflect": true
        },
        "rows": {
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
                "text": "The number of rows"
            },
            "attribute": "rows",
            "reflect": false,
            "defaultValue": "4"
        },
        "warning": {
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
                "text": "The presence of this attribute gives the component warning styles"
            },
            "attribute": "warning",
            "reflect": false,
            "defaultValue": "false"
        }
    }; }
    static get events() { return [{
            "method": "input",
            "name": "input",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": "Returns the textarea value"
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }, {
            "method": "change",
            "name": "change",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": "Returns the textarea value"
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }]; }
}
