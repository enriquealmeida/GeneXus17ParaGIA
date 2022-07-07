import { Component, Prop, h } from "@stencil/core";
export class GxgToolbarItem {
    constructor() {
        /**
         * The state of the toolbar-item, whether it is disabled or not
         */
        this.disabled = false;
        /**
         * The toolbar-item icon
         */
        this.icon = null;
    }
    includeIcon() {
        if (this.icon !== null) {
            return (h("gxg-icon", { slot: "icon", type: this.icon, color: "negative", size: "small" }));
        }
    }
    tabIndex() {
        if (this.disabled) {
            return "-1";
        }
        else {
            return "1";
        }
    }
    render() {
        return [
            this.disabled === true ? h("div", { class: "disabled-layer" }) : null,
            h("div", { class: {
                    "toolbar-item": true,
                    "toolbar-item--disabled": this.disabled === true,
                }, tabindex: this.tabIndex() },
                h("div", { class: "toolbar-item__label" },
                    h("div", { class: "toolbar-item__label__title" }, this.toolbarItemTitle),
                    ":",
                    h("div", { class: "toolbar-item__label__subtitle" }, this.subtitle)),
                this.includeIcon()),
        ];
    }
    static get is() { return "gxg-toolbar-item"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["toolbar-item.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["toolbar-item.css"]
    }; }
    static get properties() { return {
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
                "text": "The state of the toolbar-item, whether it is disabled or not"
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
                "text": "The toolbar-item icon"
            },
            "attribute": "icon",
            "reflect": false,
            "defaultValue": "null"
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
                "text": "The toolbar-item subtitle"
            },
            "attribute": "subtitle",
            "reflect": false
        },
        "toolbarItemTitle": {
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
                "text": "The toolbar-item title"
            },
            "attribute": "toolbar-item-title",
            "reflect": false
        }
    }; }
}
