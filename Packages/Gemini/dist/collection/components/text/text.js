import { Component, Host, Prop, h } from "@stencil/core";
import state from "../store";
export class GxgText {
    constructor() {
        /**
         * The target (for "link" or "link-gray" types
         * */
        this.target = "_self";
        /**
         * Title type
         */
        this.type = "text-regular";
    }
    textType() {
        let text;
        switch (this.type) {
            case "text-regular":
                text = h("p", { class: "gxg-text" }, h("slot", null));
                break;
            case "text-gray":
                text = h("p", { class: "gxg-text--gray" }, h("slot", null));
                break;
            case "text-quote":
                text = h("q", { class: "gxg-quote" }, h("slot", null));
                break;
            case "text-link":
                text = (h("a", { href: this.href, target: this.target, class: "gxg-link" }, h("slot", null)));
                break;
            case "text-link-gray":
                text = (h("a", { href: this.href, target: this.target, class: "gxg-link-gray" }, h("slot", null)));
                break;
            case "text-alert-error":
                text = h("p", { class: "gxg-alert-error" }, h("slot", null));
                break;
            case "text-alert-warning":
                text = h("p", { class: "gxg-alert-warning" }, h("slot", null));
                break;
            case "text-alert-success":
                text = h("p", { class: "gxg-alert-success" }, h("slot", null));
                break;
            default:
                text = h("p", { class: "gxg-text" }, h("slot", null));
        }
        return text;
    }
    render() {
        return h(Host, { class: { large: state.large } },
            this.textType(),
            " ");
    }
    static get is() { return "gxg-text"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["text.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["text.css"]
    }; }
    static get properties() { return {
        "href": {
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
                "text": "The href (for \"link\" or \"link-gray\" types"
            },
            "attribute": "href",
            "reflect": false
        },
        "target": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "TargetType",
                "resolved": "\"_blank\" | \"_self\"",
                "references": {
                    "TargetType": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The target (for \"link\" or \"link-gray\" types"
            },
            "attribute": "target",
            "reflect": false,
            "defaultValue": "\"_self\""
        },
        "type": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "TextType",
                "resolved": "\"text-alert-error\" | \"text-alert-success\" | \"text-alert-warning\" | \"text-gray\" | \"text-link\" | \"text-link-gray\" | \"text-quote\" | \"text-regular\"",
                "references": {
                    "TextType": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "Title type"
            },
            "attribute": "type",
            "reflect": false,
            "defaultValue": "\"text-regular\""
        }
    }; }
}
