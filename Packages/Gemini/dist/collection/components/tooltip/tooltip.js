import { Component, Prop, h, Host } from "@stencil/core";
export class GxgTooltip {
    constructor() {
        /**
         the tooltip position
         */
        this.position = "top";
        /**
         * This presence of this property removes the border under the text
         */
        this.noBorder = false;
    }
    render() {
        return (h(Host, { role: "tooltip" },
            h("span", { class: "tooltip" },
                h("slot", null),
                h("div", { class: "tooltiptext" },
                    h("span", { class: "tooltiptext__content" }, this.label)))));
    }
    static get is() { return "gxg-tooltip"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["tooltip.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["tooltip.css"]
    }; }
    static get properties() { return {
        "position": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "position",
                "resolved": "\"bottom\" | \"left\" | \"right\" | \"top\"",
                "references": {
                    "position": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "the tooltip position"
            },
            "attribute": "position",
            "reflect": true,
            "defaultValue": "\"top\""
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
                "text": "The label"
            },
            "attribute": "label",
            "reflect": false
        },
        "noBorder": {
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
                "text": "This presence of this property removes the border under the text"
            },
            "attribute": "no-border",
            "reflect": true,
            "defaultValue": "false"
        }
    }; }
}
