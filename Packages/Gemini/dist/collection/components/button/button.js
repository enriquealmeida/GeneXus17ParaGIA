import { Component, Element, h, Host, Prop, Listen, State, } from "@stencil/core";
import state from "../store";
export class GxgButton {
    constructor() {
        /*********************************
        PROPERTIES & STATE
        *********************************/
        /**
         * The prescence of this attribute makes the icon always black
         */
        this.alwaysBlack = false;
        /**
         * The state of the button, whether it is disabled or not
         */
        this.disabled = false;
        /**
         * The presence of this attribute makes the component full-width
         */
        this.fullWidth = false;
        /**
         * The prescence of this attribute turns the icon white
         */
        this.negative = false;
        /**
         * The kind of button
         */
        this.type = "primary-text-only";
        /**
         * The presence of this attribute lets the button styles be editable from outside of the component by referencing the "native-button" part.
         */
        this.buttonStylesEditable = false;
        this.mouseEnter = false;
        this.focusIn = false;
    }
    /*********************************
    METHODS
    *********************************/
    componentDidLoad() {
        // Set aria-label to host
        if (this.type === "primary-icon-only" ||
            this.type === "secondary-icon-only"
        //If button type is icon-only, aria-label must be provided in order to inform the user the button purpose.
        ) {
            if (this.el.querySelector(":scope > [slot='icon']")) {
                //Also, an icon must be provided, in order to know the button purpose.
                //The icon purpose is defined from the icon "type" property.
                const iconAriaLabel = this.el
                    .querySelector(":scope > [slot='icon']")
                    .getAttribute("type");
                this.el.setAttribute("aria-label", iconAriaLabel);
            }
        }
        //Set a part attribute to the button if buttonStylesEditable is true
        if (this.buttonStylesEditable) {
            this.el.shadowRoot
                .querySelector("button")
                .setAttribute("part", "native-button");
        }
    }
    ghostIcon() {
        return (h("gxg-icon", { class: "ghost-icon", type: "gemini-tools/empty", size: "regular" }));
        //This is a workaround I found for alligning vertically the buttons that have no icon, with the buttons that do have icons.
    }
    regularIcon() {
        if (this.type !== "primary-text-only" &&
            this.type !== "secondary-text-only" &&
            this.icon !== undefined) {
            return (h("gxg-icon", { type: this.icon, color: this.iconColor(), size: this.iconSize() }));
        }
    }
    iconSize() {
        let iSize;
        if (this.type === "secondary-icon-only" || this.type === "tertiary") {
            iSize = "regular";
        }
        else {
            iSize = "small";
        }
        return iSize;
    }
    iconColor() {
        let iColor;
        if (this.type.includes("primary")) {
            iColor = "negative";
            if (this.disabled) {
                iColor = "ondisabled";
            }
        }
        else if (this.type.includes("secondary")) {
            if (this.disabled) {
                iColor = "disabled";
            }
            else {
                if (this.mouseEnter) {
                    iColor = "primary-hover";
                }
                else {
                    if (this.focusIn) {
                        iColor = "primary-active";
                    }
                    else {
                        iColor = "primary-enabled";
                    }
                }
            }
        }
        else if (this.type.includes("tertiary")) {
            if (this.disabled) {
                iColor = "disabled";
            }
            else {
                if (this.alwaysBlack) {
                    iColor = "alwaysblack";
                }
                else if (this.negative) {
                    iColor = "negative";
                }
                else {
                    iColor = "onbackground";
                }
            }
        }
        return iColor;
    }
    clickHandler(e) {
        if (this.disabled) {
            e.preventDefault();
        }
    }
    handleFocus(focusEvent) {
        if (focusEvent.target !== this.el) {
            return;
        }
        this.button.focus();
    }
    onMouseEnter() {
        this.mouseEnter = true;
    }
    onMouseLeave() {
        this.mouseEnter = false;
    }
    onFocusIn() {
        this.focusIn = true;
        this.mouseEnter = false;
    }
    onFocusOut() {
        this.focusIn = false;
    }
    render() {
        return (h(Host, { role: "button", class: {
                button: true,
                "button--primary-text-only": this.type === "primary-text-only",
                "button--primary-text-icon": this.type === "primary-text-icon",
                "button--primary-icon-only": this.type === "primary-icon-only",
                "button--secondary-text-only": this.type === "secondary-text-only",
                "button--secondary-text-icon": this.type === "secondary-text-icon",
                "button--secondary-icon-only": this.type === "secondary-icon-only",
                "button--outlined": this.type === "outlined",
                "button--disabled": this.disabled === true,
                "button--tertiary": this.type === "tertiary",
                "button--fullwidth": this.fullWidth === true,
                large: state.large,
            }, onClick: this.clickHandler.bind(this), onMouseEnter: this.onMouseEnter.bind(this), onMouseLeave: this.onMouseLeave.bind(this), onfocusin: this.onFocusIn.bind(this), onfocusout: this.onFocusOut.bind(this) },
            this.disabled ? h("div", { class: "disabled-layer" }) : null,
            h("button", { class: "button-native gxg-text-general", disabled: this.disabled === true, ref: (el) => (this.button = el), tabindex: "0" },
                this.ghostIcon(),
                this.regularIcon(),
                this.type.includes("text") || this.type === "outlined" ? (h("span", { class: "text" },
                    h("slot", null))) : null)));
    }
    static get is() { return "gxg-button"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["button.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["button.css"]
    }; }
    static get properties() { return {
        "alwaysBlack": {
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
                "text": "The prescence of this attribute makes the icon always black"
            },
            "attribute": "always-black",
            "reflect": false,
            "defaultValue": "false"
        },
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
                "text": "The state of the button, whether it is disabled or not"
            },
            "attribute": "disabled",
            "reflect": false,
            "defaultValue": "false"
        },
        "fullWidth": {
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
                "text": "The presence of this attribute makes the component full-width"
            },
            "attribute": "full-width",
            "reflect": false,
            "defaultValue": "false"
        },
        "icon": {
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
                "text": "The button icon"
            },
            "attribute": "icon",
            "reflect": false
        },
        "negative": {
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
                "text": "The prescence of this attribute turns the icon white"
            },
            "attribute": "negative",
            "reflect": false,
            "defaultValue": "false"
        },
        "type": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "ButtonType",
                "resolved": "\"outlined\" | \"primary-icon-only\" | \"primary-text-icon\" | \"primary-text-only\" | \"secondary-icon-only\" | \"secondary-text-icon\" | \"secondary-text-only\" | \"tertiary\"",
                "references": {
                    "ButtonType": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The kind of button"
            },
            "attribute": "type",
            "reflect": false,
            "defaultValue": "\"primary-text-only\""
        },
        "buttonStylesEditable": {
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
                "text": "The presence of this attribute lets the button styles be editable from outside of the component by referencing the \"native-button\" part."
            },
            "attribute": "button-styles-editable",
            "reflect": false,
            "defaultValue": "false"
        }
    }; }
    static get states() { return {
        "mouseEnter": {},
        "focusIn": {}
    }; }
    static get elementRef() { return "el"; }
    static get listeners() { return [{
            "name": "focus",
            "method": "handleFocus",
            "target": undefined,
            "capture": false,
            "passive": false
        }]; }
}
