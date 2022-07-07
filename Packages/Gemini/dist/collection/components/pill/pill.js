import { Component, Element, Prop, h, Host } from "@stencil/core";
import state from "../store";
export class GxgPill {
    constructor() {
        /**
         * The presence of this attribute disables the pillgit a
         */
        this.disabled = false;
        /**
         * The icon
         */
        this.icon = undefined;
        /**
         * The presence of this attribute sets auto-height. Usefull when the text overflows.
         */
        this.heightAuto = false;
        /**
         * The type of pill
         */
        this.type = "static";
    }
    removeButtonFunc() {
        this.el.classList.add("hide");
        setTimeout(() => {
            this.el.remove();
        }, 250);
    }
    iconType() {
        if (this.icon !== undefined) {
            return this.icon;
        }
        else {
            console.log("empty icon");
            return "gemini-tools/empty";
        }
    }
    iconColor() {
        if (this.disabled) {
            return "disabled";
        }
        else {
            return "success";
        }
    }
    render() {
        return (h(Host, { tabindex: "0", class: {
                "no-icon": this.icon === undefined,
                "has-icon": this.icon !== undefined,
                large: state.large,
            } },
            h("gxg-icon", { class: "custom", type: this.iconType(), size: "regular", color: this.iconColor() }),
            h("span", { class: "title" },
                h("slot", null)),
            this.type === "button-with-action" ||
                this.type === "static-with-action" ? (h("gxg-icon", { class: "clear-button", type: "gemini-tools/close", size: "small", color: "onbackground", onClick: this.removeButtonFunc.bind(this) })) : null));
    }
    static get is() { return "gxg-pill"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["pill.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["pill.css"]
    }; }
    static get properties() { return {
        "disabled": {
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
                "text": "The presence of this attribute disables the pillgit a"
            },
            "attribute": "disabled",
            "reflect": false,
            "defaultValue": "false"
        },
        "icon": {
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
                "text": "The icon"
            },
            "attribute": "icon",
            "reflect": false,
            "defaultValue": "undefined"
        },
        "heightAuto": {
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
                "text": "The presence of this attribute sets auto-height. Usefull when the text overflows."
            },
            "attribute": "height-auto",
            "reflect": false,
            "defaultValue": "false"
        },
        "type": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "PillType",
                "resolved": "\"button\" | \"button-with-action\" | \"static\" | \"static-with-action\"",
                "references": {
                    "PillType": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The type of pill"
            },
            "attribute": "type",
            "reflect": true,
            "defaultValue": "\"static\""
        }
    }; }
    static get elementRef() { return "el"; }
}
