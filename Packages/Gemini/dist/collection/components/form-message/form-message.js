import { Component, Host, Prop, h } from "@stencil/core";
import state from "../store";
export class GxgFormMessage {
    iconColor() {
        if (this.type === "error") {
            return "error";
        }
        else if (this.type === "warning") {
            return "warning";
        }
    }
    iconType() {
        if (this.type === "error") {
            return "gemini-tools/error";
        }
        else if (this.type === "warning") {
            return "gemini-tools/warning";
        }
    }
    render() {
        return (h(Host, { class: {
                large: state.large,
            } },
            h("gxg-icon", { style: { "--icon-size": "15px" }, slot: "icon", size: "small", type: this.iconType(), color: this.iconColor() }),
            h("slot", null)));
    }
    static get is() { return "gxg-form-message"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["form-message.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["form-message.css"]
    }; }
    static get properties() { return {
        "type": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "Message",
                "resolved": "\"error\" | \"warning\"",
                "references": {
                    "Message": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The type of message"
            },
            "attribute": "type",
            "reflect": false
        }
    }; }
}
