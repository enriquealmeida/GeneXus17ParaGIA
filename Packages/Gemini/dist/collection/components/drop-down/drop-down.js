import { Component, Host, h, Prop, Element, State, Event, Watch, } from "@stencil/core";
export class GxgDropDown {
    constructor() {
        /**
         * the dropdown width
         */
        this.width = "240px";
        /**
         * the dropdown max. height
         */
        this.maxHeight = "120px";
        /**
         * the dropdown label (optional)
         */
        this.label = "";
        /**
         * the dropdown icon (optional)
         */
        this.icon = "";
        /**
         * Displays the dropdown content
         */
        this.showContent = false;
        this.initialButtonText = "";
        this.detectClickOutsideDropDownVar = this.detectClickOutsideDropDown.bind(this);
    }
    componentWillLoad() {
        const slotButton = this.el.querySelector("[slot=button]");
        if (slotButton === null) {
            this.initialButtonText = "Select item";
        }
    }
    toggleContent() {
        if (this.showContent === true) {
            this.showContent = false;
            document.removeEventListener("click", this.detectClickOutsideDropDownVar);
        }
        else {
            this.showContent = true;
            setTimeout(function () {
                document.addEventListener("click", this.detectClickOutsideDropDownVar);
            }.bind(this), 100);
        }
    }
    detectClickOutsideDropDown(event) {
        const dropDownContentContainer = this.el.shadowRoot.querySelector(".content-container");
        const x = event.x;
        const y = event.y;
        //Contextual menu coordinates
        const dropDownContentContainerArea = dropDownContentContainer.getBoundingClientRect();
        if (x > dropDownContentContainerArea.left &&
            x < dropDownContentContainerArea.right &&
            y > dropDownContentContainerArea.top &&
            y < dropDownContentContainerArea.bottom) {
            //Click happened inside the dropdown
        }
        else {
            this.showContent = false;
            document.removeEventListener("click", this.detectClickOutsideDropDownVar);
            //Click happened outside the dropdown
        }
    }
    componentDidUnload() {
        document.removeEventListener("click", this.detectClickOutsideDropDown);
    }
    watchHandler(newValue) {
        console.log("newValue", newValue);
        if (newValue === true) {
            console.log("emmit opened");
            this.opened.emit(true);
        }
        else {
            console.log("emmit closed");
            this.closed.emit(true);
        }
    }
    render() {
        return (h(Host, null,
            h("div", { class: { "main-container": true }, style: { width: this.width } },
                this.label !== "" ? (h("label", { class: { label: true } }, this.label)) : null,
                h("div", { class: {
                        "select-container": true,
                        "nothing-selected": this.initialButtonText === "Select item",
                        focus: this.showContent,
                    }, onClick: () => this.toggleContent() },
                    this.icon !== "" ? (h("gxg-icon", { class: "icon", type: this.icon, color: "auto", size: "small" })) : null,
                    this.initialButtonText !== "" ? this.initialButtonText : null,
                    h("slot", { name: "button" }),
                    h("span", { class: "layer" })),
                this.showContent ? (h("div", { class: {
                        "content-container": true,
                    }, style: {
                        maxHeight: this.maxHeight,
                    } },
                    h("slot", null))) : null)));
    }
    static get is() { return "gxg-drop-down"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["drop-down.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["drop-down.css"]
    }; }
    static get properties() { return {
        "width": {
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
                "text": "the dropdown width"
            },
            "attribute": "width",
            "reflect": false,
            "defaultValue": "\"240px\""
        },
        "maxHeight": {
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
                "text": "the dropdown max. height"
            },
            "attribute": "max-height",
            "reflect": false,
            "defaultValue": "\"120px\""
        },
        "label": {
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
                "text": "the dropdown label (optional)"
            },
            "attribute": "label",
            "reflect": false,
            "defaultValue": "\"\""
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
                "text": "the dropdown icon (optional)"
            },
            "attribute": "icon",
            "reflect": false,
            "defaultValue": "\"\""
        },
        "showContent": {
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
                "text": "Displays the dropdown content"
            },
            "attribute": "show-content",
            "reflect": false,
            "defaultValue": "false"
        }
    }; }
    static get states() { return {
        "initialButtonText": {},
        "detectClickOutsideDropDownVar": {}
    }; }
    static get events() { return [{
            "method": "opened",
            "name": "opened",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": "This events gets fired when the dropdown is opened"
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }, {
            "method": "closed",
            "name": "closed",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": "This events gets fired when the dropdown is closed"
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }]; }
    static get elementRef() { return "el"; }
    static get watchers() { return [{
            "propName": "showContent",
            "methodName": "watchHandler"
        }]; }
}
