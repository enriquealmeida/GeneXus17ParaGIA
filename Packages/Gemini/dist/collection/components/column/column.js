import { Component, Prop, h, Host } from "@stencil/core";
export class GxgColumn {
    constructor() {
        /**
         * The column width value
         */
        this.width = "fluid";
    }
    render() {
        return (h(Host, { class: "column" },
            h("div", { part: "inner-container", class: "inner-container" },
                h("slot", null))));
    }
    static get is() { return "gxg-column"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["column.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["column.css"]
    }; }
    static get properties() { return {
        "width": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "WidthType",
                "resolved": "\"1/2\" | \"1/3\" | \"1/4\" | \"1/5\" | \"2/3\" | \"2/5\" | \"3/4\" | \"3/5\" | \"4/5\" | \"content\" | \"fluid\"",
                "references": {
                    "WidthType": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The column width value"
            },
            "attribute": "width",
            "reflect": true,
            "defaultValue": "\"fluid\""
        }
    }; }
}
