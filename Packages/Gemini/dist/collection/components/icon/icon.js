import { Component, Element, Host, Prop, getAssetPath, h } from "@stencil/core";
/*********************************
CONSTANTS
*********************************/
const COLOR_MAPPINGS = {
    alwaysblack: "color-always-black",
    disabled: "color-primary-disabled",
    ondisabled: "color-on-disabled",
    error: "color-error-dark",
    negative: "color-on-primary",
    onbackground: "color-on-background",
    "primary-enabled": "color-primary-enabled",
    "primary-active": "color-primary-active",
    "primary-hover": "color-primary-hover",
    success: "color-success-dark",
    warning: "color-warning-dark",
};
export class GxgIcon {
    constructor() {
        /**
         * The size of the icon. Possible values: regular, small.
         */
        this.size = "regular";
    }
    /*********************************
    METHODS
    *********************************/
    getSrcPath() {
        return getAssetPath(`icon-assets/${this.type}.svg`);
    }
    iconSize() {
        if (this.size === "regular") {
            return "16px";
        }
        else if (this.size === "small") {
            return "12px";
        }
    }
    render() {
        return (h(Host, null,
            h("ch-icon", { style: {
                    "--icon-color": this.mapColorToCssVar(COLOR_MAPPINGS[this.color]),
                    "--icon-size": this.iconSize(),
                }, "auto-color": this.color === "auto", src: this.getSrcPath() })));
    }
    mapColorToCssVar(color) {
        if (color) {
            return `var(--${color})`;
        }
        else {
            //default color
            return `var(--color-on-background)`;
        }
    }
    static get is() { return "gxg-icon"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["icon.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["icon.css"]
    }; }
    static get assetsDirs() { return ["icon-assets"]; }
    static get properties() { return {
        "color": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "Color",
                "resolved": "\"alwaysblack\" | \"auto\" | \"disabled\" | \"error\" | \"negative\" | \"onbackground\" | \"ondisabled\" | \"primary-active\" | \"primary-enabled\" | \"primary-hover\" | \"success\" | \"warning\"",
                "references": {
                    "Color": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The color of the icon."
            },
            "attribute": "color",
            "reflect": false
        },
        "size": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "Size",
                "resolved": "\"regular\" | \"small\"",
                "references": {
                    "Size": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The size of the icon. Possible values: regular, small."
            },
            "attribute": "size",
            "reflect": false,
            "defaultValue": "\"regular\""
        },
        "type": {
            "type": "any",
            "mutable": false,
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The type of icon."
            },
            "attribute": "type",
            "reflect": false
        }
    }; }
    static get elementRef() { return "element"; }
}
