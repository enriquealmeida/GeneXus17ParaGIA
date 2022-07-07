import { Component, Event, Prop, h, Host } from "@stencil/core";
export class GxgMenuItem {
    constructor() {
        this.icon = null;
        this.active = false;
    }
    includeIcon() {
        if (this.icon !== null) {
            return h("gxg-icon", { slot: "icon", type: this.icon, size: "small" });
        }
    }
    setActive() {
        this.menuItemActive.emit(this.label);
    }
    render() {
        return (h(Host, { onClick: this.setActive.bind(this) },
            h("li", { class: "menu-item", ref: el => (this.listItem = el) },
                h("div", { class: "menu-item__container" },
                    this.includeIcon(),
                    this.label))));
    }
    static get is() { return "gxg-menu-item"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["menu-item.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["menu-item.css"]
    }; }
    static get properties() { return {
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
                "text": ""
            },
            "attribute": "label",
            "reflect": false
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
                "text": ""
            },
            "attribute": "icon",
            "reflect": false,
            "defaultValue": "null"
        },
        "active": {
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
                "text": ""
            },
            "attribute": "active",
            "reflect": true,
            "defaultValue": "false"
        }
    }; }
    static get events() { return [{
            "method": "menuItemActive",
            "name": "menuItemActive",
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
}
