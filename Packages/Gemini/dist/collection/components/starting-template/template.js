import { Component, Prop } from "@stencil/core";
export class GxgTemplate {
    render() {
        return [];
    }
    static get is() { return "gxg-template"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["template.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["template.css"]
    }; }
    static get properties() { return {
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
                "text": ""
            },
            "attribute": "name",
            "reflect": false
        }
    }; }
}
