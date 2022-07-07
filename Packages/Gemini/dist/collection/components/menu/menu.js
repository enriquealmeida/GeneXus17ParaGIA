import { Component, Prop, h, Host, Element, Listen } from "@stencil/core";
export class GxgMenu {
    menuItemActiveHandler(event) {
        const children = Array.from(this.el.querySelectorAll("gxg-menu-item"));
        children.forEach(element => {
            element.removeAttribute("active");
            if (event.detail === element.getAttribute("label")) {
                element.setAttribute("active", "active");
            }
        });
    }
    printTitle() {
        if (this.menuTitle !== "undefined" &&
            this.menuTitle.replace(/\s/g, "") !== "") {
            return (h("header", { class: "menu__header" },
                h("h1", { class: "menu__header__title" }, this.menuTitle)));
        }
    }
    render() {
        return (h(Host, null,
            this.printTitle(),
            h("ul", { class: "menuList" },
                h("slot", null))));
    }
    static get is() { return "gxg-menu"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["menu.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["menu.css"]
    }; }
    static get properties() { return {
        "menuTitle": {
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
                "text": "The menu title"
            },
            "attribute": "menu-title",
            "reflect": false
        },
        "tabs": {
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
                "text": "Provide this attribute if you are using this menu on the tabs component"
            },
            "attribute": "tabs",
            "reflect": true
        }
    }; }
    static get elementRef() { return "el"; }
    static get listeners() { return [{
            "name": "menuItemActive",
            "method": "menuItemActiveHandler",
            "target": undefined,
            "capture": false,
            "passive": false
        }]; }
}
