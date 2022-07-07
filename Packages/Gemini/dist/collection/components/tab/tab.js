import { Component, Element, Prop, h, Host } from "@stencil/core";
export class GxgTab {
    constructor() {
        /**
         * The selected tab
         */
        this.isSelected = false;
    }
    render() {
        return (h(Host, { class: { open: this.isSelected, "not-selected": !this.isSelected } },
            h("div", { class: "outer-container" },
                h("div", { class: "inner-container" },
                    h("slot", null)))));
    }
    static get is() { return "gxg-tab"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["tab.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["tab.css"]
    }; }
    static get properties() { return {
        "tab": {
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
                "text": "The tab id. Should match the \"tab\" value of the correlative \"gxg-tab\""
            },
            "attribute": "tab",
            "reflect": false
        },
        "isSelected": {
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
                "text": "The selected tab"
            },
            "attribute": "is-selected",
            "reflect": true,
            "defaultValue": "false"
        }
    }; }
    static get elementRef() { return "el"; }
}
