import { Component, Prop, h, Host, Watch, Event } from "@stencil/core";
export class GxgFormOption {
    watchHandler(newValue) {
        if (newValue === true) {
            this.optionIsSelected.emit(this.value);
        }
    }
    render() {
        return h(Host, { value: this.value, role: "option" });
    }
    static get is() { return "gxg-option"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["option.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["option.css"]
    }; }
    static get properties() { return {
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
                "text": "The value"
            },
            "attribute": "value",
            "reflect": false
        },
        "selected": {
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
                "text": "The presence of this attribute makes the option selected by default"
            },
            "attribute": "selected",
            "reflect": false
        }
    }; }
    static get events() { return [{
            "method": "optionIsSelected",
            "name": "optionIsSelected",
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
    static get watchers() { return [{
            "propName": "selected",
            "methodName": "watchHandler"
        }]; }
}
