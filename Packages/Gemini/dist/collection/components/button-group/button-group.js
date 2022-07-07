import { Component, Element, h, Host, Prop, State } from "@stencil/core";
import state from "../store";
export class GxgButtonGroup {
    constructor() {
        /**
        The button group title alignment
        */
        this.titleAlignment = "left";
        /**
        Wether the button group is disabled or not
        */
        this.disabled = false;
        /**
         * The presence of this attribute makes the component full-width
         */
        this.fullWidth = false;
        /**
         * The presence of this attribute makes the button group outlined
         */
        this.outlined = false;
        /**
        The value of the current selected button
        */
        this.value = "";
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
    /*********************************
    METHODS
    *********************************/
    componentWillLoad() {
        if (!this.disabled) {
            this.setInitialActiveValue();
        }
        if (this.disabled) {
            this.value = null;
        }
        //if disabled, set buttons tabindex equal to "-1"
        if (this.disabled) {
            const buttonsHtmlCollection = this.el.children;
            Array.from(buttonsHtmlCollection).forEach(function (button) {
                button.setAttribute("tab-index", "-1");
                button.style.pointerEvents = "none";
            });
        }
    }
    setActiveButton(event) {
        const buttonsHtmlCollection = this.el.children;
        Array.from(buttonsHtmlCollection).forEach(function (button) {
            button.removeAttribute("data-active");
            button.setAttribute("aria-pressed", "false");
        });
        event.target.setAttribute("data-active", "");
        event.target.setAttribute("aria-pressed", "true");
        this.value = event.target.value;
    }
    setInitialActiveValue() {
        let buttonValue = "";
        //get id of all buttons into array
        const buttonsHtmlCollection = this.el.children;
        const buttonsIdsArray = [];
        Array.from(buttonsHtmlCollection).forEach((button) => {
            const b = button;
            b.setAttribute("tabindex", "0");
            if (b.getAttribute("id") !== "") {
                buttonsIdsArray.push(b.getAttribute("id"));
            }
        });
        //if defaultSelectedBtnId is equal to any id of the array, set that button as active
        if (this.defaultSelectedBtnId === undefined ||
            this.defaultSelectedBtnId.replace(/\s/g, "") === "" ||
            !buttonsIdsArray.includes(this.defaultSelectedBtnId)) {
            // defaultSelectedBtnId is not part of any button id
            //By default, set the first button as the active button
            this.el.children[0].setAttribute("data-active", "");
            buttonValue = this.el.children[0].getAttribute("value");
            this.el.children[0].setAttribute("aria-pressed", "true");
        }
        else {
            if (buttonsIdsArray.includes(this.defaultSelectedBtnId)) {
                Array.from(buttonsHtmlCollection).forEach((button) => {
                    const b = button;
                    if (b.id == this.defaultSelectedBtnId) {
                        //set the value to the active button value
                        buttonValue = this.defaultSelectedBtnId = b.getAttribute("value");
                        b.setAttribute("data-active", "");
                        b.setAttribute("aria-pressed", "true");
                    }
                });
            }
        }
        this.value = buttonValue;
    }
    detectFocus(event) {
        if (this.el === document.activeElement) {
            const buttonsHtmlCollection = this.el.children;
            const n = buttonsHtmlCollection.length;
            if (event.keyCode === 9 && event.shiftKey) {
                //set focus to the last button
                buttonsHtmlCollection.item(n).focus();
            }
            else if (event.keyCode === 9) {
                //set focus to the first button
                buttonsHtmlCollection.item(0).focus();
            }
        }
    }
    tabIndex() {
        if (this.disabled) {
            return "-1";
        }
        else {
            return "0";
        }
    }
    render() {
        let header = null;
        if (this.buttonGroupTitle !== undefined) {
            header = (h("header", { class: "button-group-header" },
                h("label", { class: "button-group-header-title" }, this.buttonGroupTitle)));
        }
        return (h(Host, { tabindex: this.tabIndex(), role: "group", "aria-label": this.buttonGroupTitle, class: {
                "button-group": true,
                rtl: this.rtl,
                large: state.large,
            }, value: this.value, "title-alignment": this.titleAlignment, onKeyUp: this.detectFocus.bind(this) },
            header,
            h("div", { class: "button-group-container", onClick: this.setActiveButton.bind(this) },
                this.disabled ? h("div", { class: "disabled-layer" }) : null,
                h("slot", null))));
    }
    static get is() { return "gxg-button-group"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["button-group.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["button-group.css"]
    }; }
    static get properties() { return {
        "buttonGroupTitle": {
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
                "text": "The button-group title"
            },
            "attribute": "button-group-title",
            "reflect": false
        },
        "titleAlignment": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "TitleAlignment",
                "resolved": "\"center\" | \"left\" | \"right\"",
                "references": {
                    "TitleAlignment": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The button group title alignment"
            },
            "attribute": "title-alignment",
            "reflect": true,
            "defaultValue": "\"left\""
        },
        "defaultSelectedBtnId": {
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
                "text": "The id of the button that you would like to be active by default"
            },
            "attribute": "default-selected-btn-id",
            "reflect": false
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
                "text": "Wether the button group is disabled or not"
            },
            "attribute": "disabled",
            "reflect": true,
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
            "reflect": true,
            "defaultValue": "false"
        },
        "outlined": {
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
                "text": "The presence of this attribute makes the button group outlined"
            },
            "attribute": "outlined",
            "reflect": true,
            "defaultValue": "false"
        }
    }; }
    static get states() { return {
        "value": {},
        "rtl": {}
    }; }
    static get elementRef() { return "el"; }
}
