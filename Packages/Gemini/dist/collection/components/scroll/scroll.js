import { Component, Prop, h } from "@stencil/core";
export class GxgScroll {
    constructor() {
        /**
         * Max height
         */
        this.maxHeight = "100%";
    }
    render() {
        return (h("div", { class: "gxg-scroll", style: { maxHeight: this.maxHeight } },
            h("slot", null)));
    }
    static get is() { return "gxg-scroll"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["scroll.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["scroll.css"]
    }; }
    static get properties() { return {
        "maxHeight": {
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
                "text": "Max height"
            },
            "attribute": "max-height",
            "reflect": false,
            "defaultValue": "\"100%\""
        }
    }; }
}
