import { Component, Prop, h, Host } from "@stencil/core";
export class GxgSpacerLayout {
    constructor() {
        /**
         * Add this attribute to make the spacer-layout full height
         */
        this.fullHeight = false;
        /**
         * The spacing value, taken from the "token-spacing" global values
         */
        this.space = "xs";
        /**
         * The orientation
         */
        this.orientation = "horizontal";
        /**
         * Content justify
         */
        this.justifyContent = "start";
    }
    render() {
        return (h(Host, { class: {
                "flex-start": this.justifyContent === "start",
                "flex-end": this.justifyContent === "end",
                center: this.justifyContent === "center",
                "space-between": this.justifyContent === "space-between",
                "space-around": this.justifyContent === "space-around",
                horizontal: this.orientation === "horizontal",
                vertical: this.orientation === "vertical",
                xs: this.space === "xs",
                s: this.space === "s",
                m: this.space === "m",
                l: this.space === "l",
                xl: this.space === "xl",
            } },
            h("slot", null)));
    }
    static get is() { return "gxg-spacer-layout"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["spacer-layout.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["spacer-layout.css"]
    }; }
    static get properties() { return {
        "fullHeight": {
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
                "text": "Add this attribute to make the spacer-layout full height"
            },
            "attribute": "full-height",
            "reflect": false,
            "defaultValue": "false"
        },
        "space": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "Space",
                "resolved": "\"l\" | \"m\" | \"s\" | \"xl\" | \"xs\"",
                "references": {
                    "Space": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The spacing value, taken from the \"token-spacing\" global values"
            },
            "attribute": "space",
            "reflect": false,
            "defaultValue": "\"xs\""
        },
        "orientation": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "Orientation",
                "resolved": "\"horizontal\" | \"vertical\"",
                "references": {
                    "Orientation": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The orientation"
            },
            "attribute": "orientation",
            "reflect": false,
            "defaultValue": "\"horizontal\""
        },
        "justifyContent": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "JustifyContent",
                "resolved": "\"center\" | \"end\" | \"space-around\" | \"space-between\" | \"start\"",
                "references": {
                    "JustifyContent": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "Content justify"
            },
            "attribute": "justify-content",
            "reflect": false,
            "defaultValue": "\"start\""
        }
    }; }
}
