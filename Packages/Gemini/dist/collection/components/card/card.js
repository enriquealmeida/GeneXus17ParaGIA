import { Component, Prop, h, Host } from "@stencil/core";
export class GxgCard {
    constructor() {
        /**
         * The card box-shadow
         */
        this.elevation = "xs";
        /**
         * The background color
         */
        this.background = "white";
        /**
         * The card padding
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
    render() {
        return (h(Host, { role: "article", class: {
                card: true,
            }, style: { maxWidth: this.maxWidth, minHeight: this.minHeight } },
            h("slot", null)));
    }
    static get is() { return "gxg-card"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["card.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["card.css"]
    }; }
    static get properties() { return {
        "elevation": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "elevation",
                "resolved": "\"m\" | \"xs\"",
                "references": {
                    "elevation": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The card box-shadow"
            },
            "attribute": "elevation",
            "reflect": true,
            "defaultValue": "\"xs\""
        },
        "background": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "background",
                "resolved": "\"gray-01\" | \"white\"",
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
                "text": "The card padding"
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
