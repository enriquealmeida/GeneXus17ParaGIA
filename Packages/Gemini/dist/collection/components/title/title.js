import { Component, Host, Prop, h } from "@stencil/core";
import state from "../store";
export class GxgTitle {
    constructor() {
        /**
         * Title type
         */
        this.type = "title-01";
    }
    titleType() {
        let title;
        switch (this.type) {
            case "title-01":
                title = h("h1", { class: "gxg-title-01" }, h("slot", null));
                break;
            case "title-02":
                title = h("h2", { class: "gxg-title-02" }, h("slot", null));
                break;
            case "title-03":
                title = h("h3", { class: "gxg-title-03" }, h("slot", null));
                break;
            case "title-04":
                title = h("h4", { class: "gxg-title-04" }, h("slot", null));
                break;
            case "title-05":
                title = h("h5", { class: "gxg-title-05" }, h("slot", null));
                break;
            default:
                title = h("h1", { class: "gxg-title-01" }, h("slot", null));
        }
        return title;
    }
    render() {
        return h(Host, { class: { large: state.large } },
            this.titleType(),
            " ");
    }
    static get is() { return "gxg-title"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["title.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["title.css"]
    }; }
    static get properties() { return {
        "type": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "TitleType",
                "resolved": "\"title-01\" | \"title-02\" | \"title-03\" | \"title-04\" | \"title-05\"",
                "references": {
                    "TitleType": {
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
            "defaultValue": "\"title-01\""
        }
    }; }
}
