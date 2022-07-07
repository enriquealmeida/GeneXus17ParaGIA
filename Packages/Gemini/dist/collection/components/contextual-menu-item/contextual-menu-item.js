import { Component, Prop, h, Host, Element } from "@stencil/core";
export class ContextualMenuItem {
    constructor() {
        /**
         * Optional icon
         */
        this.icon = null;
    }
    printIcon() {
        if (this.icon !== null) {
            return (h("gxg-icon", { size: "small", type: this.icon, color: "onbackground" }));
        }
    }
    subMenuIcon() {
        const contextualMenuSubmenu = this.el.querySelector("gxg-contextual-menu-submenu");
        if (contextualMenuSubmenu !== null) {
            return (h("gxg-icon", { class: "show-more", size: "regular", type: "navigation/arrow-right", color: "onbackground" }));
        }
    }
    render() {
        return (h(Host, null,
            h("li", { class: {
                    "contextual-menu-item": true,
                    "contextual-menu-item--no-icon": this.icon === null,
                } },
                this.printIcon(),
                h("slot", null),
                this.subMenuIcon())));
    }
    static get is() { return "gxg-contextual-menu-item"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["contextual-menu-item.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["contextual-menu-item.css"]
    }; }
    static get properties() { return {
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
                "text": "Optional icon"
            },
            "attribute": "icon",
            "reflect": false,
            "defaultValue": "null"
        }
    }; }
    static get elementRef() { return "el"; }
}
