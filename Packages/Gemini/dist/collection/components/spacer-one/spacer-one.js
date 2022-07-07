import { Component, Prop, h, Host } from "@stencil/core";
export class GxgSpacerOne {
    render() {
        return (h(Host, { class: {
                xs: this.space === "xs",
                s: this.space === "s",
                m: this.space === "m",
                l: this.space === "l",
                xl: this.space === "xl"
            } }));
    }
    static get is() { return "gxg-spacer-one"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["spacer-one.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["spacer-one.css"]
    }; }
    static get properties() { return {
        "space": {
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
                "text": "The spacing value, taken from the \"token-spacing\" global values"
            },
            "attribute": "space",
            "reflect": false
        }
    }; }
}
