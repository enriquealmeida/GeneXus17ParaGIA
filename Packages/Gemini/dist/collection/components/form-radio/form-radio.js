import { Component, Prop, Element, Event, h, Host, Watch, } from "@stencil/core";
export class GxgFormRadio {
    constructor() {
        /**
         * Styles the radio-button with error attributes
         */
        this.error = false;
        /**
         * The presence of this attribute makes the radio selected by default
         */
        this.checked = false;
        /**
         * The presence of this attribute disables the radio
         */
        this.disabled = false;
    }
    /*********************************
    METHODS
    *********************************/
    componentDidLoad() {
        if (this.checked) {
            this.radioInput.setAttribute("checked", "checked");
        }
        if (this.disabled && this.checked) {
            this.radioInput.removeAttribute("checked");
            this.checked = false;
        }
    }
    watchHandler(newValue) {
        if (newValue === false) {
            this.radioInput.removeAttribute("checked");
        }
        if (newValue === true) {
            this.radioInput.setAttribute("checked", "checked");
            this.change.emit({
                id: this.RadioId,
                value: this.value,
            });
        }
    }
    selectRadio() {
        this.changeInternal.emit({
            id: this.RadioId,
            value: this.value,
        });
    }
    handlerOnKeyDown(event) {
        if (event.keyCode == 9) {
            //tab key was pressed
            if (event.shiftKey) {
                //shift key was also pressed
                this.keyPressed.emit({ direction: "previous-tab" });
            }
            else {
                this.keyPressed.emit({ direction: "next-tab" });
            }
        }
        else if (event.keyCode == 37 || event.keyCode == 38) {
            //arrow-left, or arrow-up key was pressed. focus should be positioned on the previous radiobtn.
            event.preventDefault();
            this.keyPressed.emit({ direction: "previous" });
        }
        if (event.keyCode == 39 || event.keyCode == 40) {
            //arrow-right, or arrow-down key was pressed. focus should be positioned on the next radiobtn
            event.preventDefault();
            this.keyPressed.emit({ direction: "next" });
        }
    }
    render() {
        return (h(Host, null,
            h("label", { class: "label" },
                h("input", { ref: (el) => (this.radioInput = el), type: "radio", name: this.name, id: this.RadioId, value: this.value, onClick: this.selectRadio.bind(this), disabled: this.disabled, onKeyDown: this.handlerOnKeyDown.bind(this) }),
                h("span", { class: { radiobtn: true, "radiobtn--error": this.error } }),
                this.label)));
    }
    static get is() { return "gxg-form-radio"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["form-radio.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["form-radio.css"]
    }; }
    static get properties() { return {
        "error": {
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
                "text": "Styles the radio-button with error attributes"
            },
            "attribute": "error",
            "reflect": false,
            "defaultValue": "false"
        },
        "RadioId": {
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
                "text": "The radio id"
            },
            "attribute": "radio-id",
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
                "text": "The presence of this attribute makes the radio selected by default"
            },
            "attribute": "checked",
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
                "text": "The presence of this attribute disables the radio"
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
                "text": "The radio label"
            },
            "attribute": "label",
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
                "text": "The radio name (should be the same for every radio of the same radio-group)"
            },
            "attribute": "name",
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
                "text": "The radio value"
            },
            "attribute": "value",
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
                "text": "Returns an object with the radio value, and radio id"
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }, {
            "method": "changeInternal",
            "name": "changeInternal",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": "(This event is for internal use)"
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }, {
            "method": "keyPressed",
            "name": "keyPressed",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": "(This event is for internal use)"
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }]; }
    static get elementRef() { return "el"; }
    static get watchers() { return [{
            "propName": "checked",
            "methodName": "watchHandler"
        }]; }
}
