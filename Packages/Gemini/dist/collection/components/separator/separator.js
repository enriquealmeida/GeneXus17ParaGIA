import { Component, Prop, h } from "@stencil/core";
export class GxgSeparator {
    constructor() {
        /**
         * The hr style
         */
        this.type = "solid";
        /**
         * The hr top and bottom margin
         */
        this.margin = "xs";
    }
    render() {
        return h("hr", null);
    }
    static get is() { return "gxg-separator"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["separator.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["separator.css"]
    }; }
    static get properties() { return {
        "type": {
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
                "text": "The hr style"
            },
            "attribute": "type",
            "reflect": true,
            "defaultValue": "\"solid\""
        },
        "margin": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "margin",
                "resolved": "\"l\" | \"m\" | \"s\" | \"xl\" | \"xs\"",
                "references": {
                    "margin": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The hr top and bottom margin"
            },
            "attribute": "margin",
            "reflect": true,
            "defaultValue": "\"xs\""
        }
    }; }
}
