import { Component, Prop, h } from "@stencil/core";
export class GxgMoreInfo {
    constructor() {
        /**
         the tooltip position
         */
        this.position = "top";
        /**
         * (Optional) The "more-info" label. This property goes along with "url" attribute
         */
        this.moreInfoLabel = "more info";
        /**
         * (Optional) The "more-info" url.
         */
        this.url = null;
        /**
         * The url target
         */
        this.target = "_blank";
    }
    render() {
        return (h("span", { class: "more-info" },
            h("gxg-icon", { color: "primary-enabled", size: "small", type: "gemini-tools/notice" }),
            h("div", { class: "more-info__text" },
                h("span", { class: "more-info__text__content" },
                    this.label,
                    this.url !== null ? h("br", null) : null,
                    this.url !== null ? (h("a", { class: "more-info__text__content__url", href: this.url, target: this.target }, this.moreInfoLabel)) : null))));
    }
    static get is() { return "gxg-more-info"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["more-info.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["more-info.css"]
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
        "moreInfoLabel": {
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
                "text": "(Optional) The \"more-info\" label. This property goes along with \"url\" attribute"
            },
            "attribute": "more-info-label",
            "reflect": false,
            "defaultValue": "\"more info\""
        },
        "url": {
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
                "text": "(Optional) The \"more-info\" url."
            },
            "attribute": "url",
            "reflect": false,
            "defaultValue": "null"
        },
        "target": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "target",
                "resolved": "\"_blank\" | \"self\"",
                "references": {
                    "target": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The url target"
            },
            "attribute": "target",
            "reflect": false,
            "defaultValue": "\"_blank\""
        }
    }; }
}
