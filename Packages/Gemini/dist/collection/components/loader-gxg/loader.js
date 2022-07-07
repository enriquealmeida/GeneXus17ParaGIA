import { Component, Prop, h, Host, State, Watch } from "@stencil/core";
export class GxgLodaer {
    constructor() {
        /**
         * The prescence of this attribute shows the loader
         */
        this.show = false;
        /**
         * The z-index positive value you want for the loader when visible (default: 100)
         */
        this.visibleZIndex = "100";
        this.layerOpacity100 = false;
        this.squaresOpacity100 = false;
        this.textOpacity100 = false;
        this.sendLayerBack = true;
    }
    showHandler() {
        if (this.show) {
            this.sendLayerBack = false;
            setTimeout(function () {
                this.layerOpacity100 = true;
                setTimeout(function () {
                    this.squaresOpacity100 = true;
                    setTimeout(function () {
                        this.textOpacity100 = true;
                    }.bind(this), 250);
                }.bind(this), 250);
            }.bind(this), 250);
        }
        else {
            setTimeout(function () {
                this.textOpacity100 = false;
                setTimeout(function () {
                    this.squaresOpacity100 = false;
                    setTimeout(function () {
                        this.layerOpacity100 = false;
                        setTimeout(function () {
                            this.sendLayerBack = true;
                        }.bind(this), 250);
                    }.bind(this), 250);
                }.bind(this), 250);
            }.bind(this), 250);
        }
    }
    render() {
        return (h(Host, { style: { zIndex: this.visibleZIndex }, class: {
                sendLayerBack: this.sendLayerBack,
                layerOpacity100: this.layerOpacity100,
                squaresOpacity100: this.squaresOpacity100,
                textOpacity100: this.textOpacity100,
            } },
            h("div", { class: "layer" },
                h("div", { class: "loader" },
                    h("div", { class: "box" }),
                    h("div", { class: "box" }),
                    h("div", { class: "box" }),
                    h("div", { class: "box" })),
                this.text !== undefined ? (h("span", { class: "loader-text" },
                    this.text,
                    h("span", { class: "dot1" }, "."),
                    h("span", { class: "dot2" }, "."),
                    h("span", { class: "dot3" }, "."))) : null)));
    }
    static get is() { return "gxg-loader"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["loader.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["loader.css"]
    }; }
    static get properties() { return {
        "text": {
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
                "text": "The text you want to show under the loader (optional)"
            },
            "attribute": "text",
            "reflect": false
        },
        "show": {
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
                "text": "The prescence of this attribute shows the loader"
            },
            "attribute": "show",
            "reflect": false,
            "defaultValue": "false"
        },
        "visibleZIndex": {
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
                "text": "The z-index positive value you want for the loader when visible (default: 100)"
            },
            "attribute": "visible-z-index",
            "reflect": false,
            "defaultValue": "\"100\""
        }
    }; }
    static get states() { return {
        "layerOpacity100": {},
        "squaresOpacity100": {},
        "textOpacity100": {},
        "sendLayerBack": {}
    }; }
    static get watchers() { return [{
            "propName": "show",
            "methodName": "showHandler"
        }]; }
}
