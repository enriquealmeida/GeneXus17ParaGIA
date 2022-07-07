import { Component, Prop, h } from "@stencil/core";
export class GxgStack {
    constructor() {
        /**
         * The spacing value
         */
        this.space = "xs";
    }
    render() {
        return h("slot", null);
    }
    static get is() { return "gxg-stack"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["stack.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["stack.css"]
    }; }
    static get properties() { return {
        "space": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "Space",
                "resolved": "\"l\" | \"m\" | \"s\" | \"xl\" | \"xs\"",
                "references": {
                    "Space": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The spacing value"
            },
            "attribute": "space",
            "reflect": true,
            "defaultValue": "\"xs\""
        }
    }; }
}
