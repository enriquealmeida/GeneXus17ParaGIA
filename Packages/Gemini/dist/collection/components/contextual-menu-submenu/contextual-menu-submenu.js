import { Component, h, Host, Element } from "@stencil/core";
export class GxgContextualMenuSubmenu {
    render() {
        return (h(Host, null,
            h("ul", { class: "contextual-menu-submenu" },
                h("slot", null))));
    }
    static get is() { return "gxg-contextual-menu-submenu"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["contextual-menu-submenu.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["contextual-menu-submenu.css"]
    }; }
    static get elementRef() { return "el"; }
}
