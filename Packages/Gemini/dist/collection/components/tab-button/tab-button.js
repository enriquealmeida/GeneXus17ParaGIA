import { Component, Prop, h, Event, Element, } from "@stencil/core";
export class GxgTabButton {
    constructor() {
        /**
         * The button label
         */
        this.tabLabel = null;
        /**
         * The tab id. Must be unique, and match the "tab" value of the correlative "gxg-tab" element
         */
        this.tab = null;
        /**
         * Provide this attribute to make this button selected by default
         */
        this.isSelected = false;
        /**
         * Provide this attribute to make this button disabled
         */
        this.disabled = false;
        /**
         * (Optional) provide an icon to this button
         */
        this.icon = null;
    }
    //Click functions
    tabButtonClicked() {
        this.isSelected = true;
        const index = parseInt(this.el.getAttribute("data-index"), 10);
        this.tabActivated.emit({
            tab: this.tab,
            index: index,
        });
    }
    printIcon() {
        if (this.icon !== null) {
            if (this.disabled) {
                return h("gxg-icon", { color: "disabled", type: this.icon });
            }
            return h("gxg-icon", { type: this.icon });
        }
    }
    componentDidLoad() {
        //Set the active tab for this tab-button if this is selected by default
        if (this.isSelected) {
            this.tabActivated.emit();
        }
    }
    render() {
        return (h("li", { class: "tab-item" },
            h("button", { disabled: this.disabled, class: {
                    "tab-button": true,
                    "tab-button--selected": this.isSelected === true,
                    "tab-button--text-icon": this.tabLabel !== null && this.icon !== null,
                }, onClick: this.tabButtonClicked.bind(this) },
                this.printIcon(),
                h("span", { class: "tab-button__text" }, this.tabLabel)),
            h("slot", null)));
    }
    static get is() { return "gxg-tab-button"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["tab-button.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["tab-button.css"]
    }; }
    static get properties() { return {
        "tabLabel": {
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
                "text": "The button label"
            },
            "attribute": "tab-label",
            "reflect": false,
            "defaultValue": "null"
        },
        "tab": {
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
                "text": "The tab id. Must be unique, and match the \"tab\" value of the correlative \"gxg-tab\" element"
            },
            "attribute": "tab",
            "reflect": false,
            "defaultValue": "null"
        },
        "isSelected": {
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
                "text": "Provide this attribute to make this button selected by default"
            },
            "attribute": "is-selected",
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
                "text": "Provide this attribute to make this button disabled"
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
                "text": "(Optional) provide an icon to this button"
            },
            "attribute": "icon",
            "reflect": false,
            "defaultValue": "null"
        }
    }; }
    static get events() { return [{
            "method": "tabActivated",
            "name": "tabActivated",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": ""
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }]; }
    static get elementRef() { return "el"; }
}
