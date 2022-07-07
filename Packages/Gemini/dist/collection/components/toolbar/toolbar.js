import { Component, Prop, h, Host, State } from "@stencil/core";
export class GxgToolbar {
    constructor() {
        /**
         * The toggle arrow position
         */
        this.position = "bottom";
        /**
         * Reading direction
         */
        this.rtl = false;
    }
    componentDidLoad() {
        //Reading Direction
        const dirHtml = document
            .getElementsByTagName("html")[0]
            .getAttribute("dir");
        const dirBody = document
            .getElementsByTagName("body")[0]
            .getAttribute("dir");
        if (dirHtml === "rtl" || dirBody === "rtl") {
            this.rtl = true;
        }
    }
    render() {
        return (h(Host, { class: { rtl: this.rtl } },
            h("div", { class: {
                    toolbar: true,
                    "toolbar--start": this.position === "start",
                    "toolbar--top": this.position === "top",
                    "toolbar--bottom": this.position === "bottom",
                } },
                h("div", { class: "left-container" },
                    h("gxg-icon", { slot: "icon", type: "navigation/drag", color: "negative", size: "regular" }),
                    h("div", { class: "left-container__title" },
                        h("strong", null,
                            this.toolbarTitle,
                            ":")),
                    h("div", { class: "left-container__subtitle" }, this.subtitle)),
                h("div", { class: "right-container" },
                    h("slot", null)))));
    }
    static get is() { return "gxg-toolbar"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["toolbar.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["toolbar.css"]
    }; }
    static get properties() { return {
        "position": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "position",
                "resolved": "\"bottom\" | \"start\" | \"top\"",
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
                "text": "The toggle arrow position"
            },
            "attribute": "position",
            "reflect": false,
            "defaultValue": "\"bottom\""
        },
        "subtitle": {
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
                "text": "The toolbar title"
            },
            "attribute": "subtitle",
            "reflect": false
        },
        "toolbarTitle": {
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
                "text": "The toolbar subtitle"
            },
            "attribute": "toolbar-title",
            "reflect": false
        }
    }; }
    static get states() { return {
        "rtl": {}
    }; }
}
