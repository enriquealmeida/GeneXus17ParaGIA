import { Component, Element, Host, Prop, h, Event, } from "@stencil/core";
export class GxgFormCheckbox {
    constructor() {
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
        return (h(Host, { role: "checkbox", value: this.value, "aria-checked": this.ariaChecked, "aria-label": this.label },
            h("label", { class: "label" },
                h("input", { ref: (el) => (this.checkboxInput = el), type: "checkbox", checked: this.checked, class: "input", id: this.checkboxId, name: this.name, value: this.value, disabled: this.disabled, onChange: this.changed.bind(this), onKeyUp: this.handlerOnKeyUp.bind(this), tabindex: "0", onClick: this.handleInputClick }),
                h("span", { class: { checkmark: true, "no-label": !this.label }, role: "checkbox" }),
                this.label ? this.label : null)));
    }
    static get is() { return "gxg-form-checkbox"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["form-checkbox.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["form-checkbox.css"]
    }; }
    static get properties() { return {
        "checkboxId": {
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
                "text": "The checkbox id"
            },
            "attribute": "checkbox-id",
            "reflect": false
        },
        "checked": {
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
                "text": "The presence of this attribute makes the checkbox checked by default"
            },
            "attribute": "checked",
            "reflect": false,
            "defaultValue": "false"
        },
        "indeterminate": {
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
                "text": "The presence of this attribute makes the checkbox indeterminate"
            },
            "attribute": "indeterminate",
            "reflect": true,
            "defaultValue": "false"
        },
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
                "text": "The presence of this attribute disables the checkbox"
            },
            "attribute": "disabled",
            "reflect": false,
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
                "text": "The checkbox label"
            },
            "attribute": "label",
            "reflect": false
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
                "text": "The checkbox value"
            },
            "attribute": "value",
            "reflect": false
        },
        "name": {
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
                "text": "The checkbox name"
            },
            "attribute": "name",
            "reflect": false
        }
    }; }
    static get events() { return [{
            "method": "change",
            "name": "change",
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
}
