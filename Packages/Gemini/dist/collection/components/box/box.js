import { Component, Prop, h, Host } from "@stencil/core";
export class GxgBox {
    constructor() {
        /*********************************
        PROPERTIES & STATE
        *********************************/
        /**
         * The background color
         */
        this.background = "white";
        /**
         * Wether the box has border or not
         */
        this.border = true;
        /**
         * The box padding
         */
        this.padding = "xs";
        /**
         * The component min. height
         */
        this.minHeight = "auto";
        /**
         * The component max. width
         */
        this.maxWidth = "100%";
    }
    /*********************************
    METHODS
    *********************************/
    render() {
        return (h(Host, { role: "article", class: {
                card: true,
                border: this.border,
            }, style: { maxWidth: this.maxWidth, minHeight: this.minHeight } },
            h("slot", null)));
    }
    static get is() { return "gxg-box"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["box.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["box.css"]
    }; }
    static get properties() { return {
        "background": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "background",
                "resolved": "\"gray-01\" | \"gray-02\" | \"white\"",
                "references": {
                    "background": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The background color"
            },
            "attribute": "background",
            "reflect": true,
            "defaultValue": "\"white\""
        },
        "border": {
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
                "text": "Wether the box has border or not"
            },
            "attribute": "border",
            "reflect": false,
            "defaultValue": "true"
        },
        "padding": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "padding",
                "resolved": "\"0\" | \"l\" | \"m\" | \"s\" | \"xl\" | \"xs\" | \"xxl\" | \"xxxl\"",
                "references": {
                    "padding": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The box padding"
            },
            "attribute": "padding",
            "reflect": true,
            "defaultValue": "\"xs\""
        },
        "minHeight": {
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
                "text": "The component min. height"
            },
            "attribute": "min-height",
            "reflect": false,
            "defaultValue": "\"auto\""
        },
        "maxWidth": {
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
                "text": "The component max. width"
            },
            "attribute": "max-width",
            "reflect": false,
            "defaultValue": "\"100%\""
        }
    }; }
}
