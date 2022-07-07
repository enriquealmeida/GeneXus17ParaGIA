import { Component, Element, Listen, h, State, Prop } from "@stencil/core";
export class GxgTabs {
    constructor() {
        this.position = "top";
        this.height = "auto";
        this.activeTab = "";
    }
    tabActivatedHandler(event) {
        //first, get the active tab, and set it´s outer-container overflow to hidden
        const activeTab = this.element.querySelector("gxg-tab[is-selected]");
        if (activeTab !== null) {
            const outerContainer = activeTab.shadowRoot.querySelector(".outer-container");
            outerContainer.style.overflow = "hidden";
        }
        this.updateActiveChildren(event.target.tab, "gxg-tab-button");
        this.updateActiveChildren(event.target.tab, "gxg-tab");
        //hide tab menu
    }
    updateActiveChildren(activeTab, tagName) {
        const children = Array.from(this.element.querySelectorAll(tagName));
        for (const child of children) {
            child.isSelected = activeTab === child.tab;
            if (activeTab === child.tab) {
                let outerContainer = child.shadowRoot.querySelector(".outer-container");
                if (child.tagName === "GXG-TAB") {
                    outerContainer = child.shadowRoot.querySelector(".outer-container");
                    setTimeout(function () {
                        outerContainer.style.overflow = "visible";
                    }, 100);
                }
            }
        }
    }
    render() {
        return (h("div", { class: "main-container" }, this.position === "bottom"
            ? [
                h("div", { class: "tabs-container" },
                    h("slot", null)),
                h("div", { class: "tab-bar-container" },
                    h("slot", { name: "tab-bar" })),
            ]
            : [
                h("div", { class: "tab-bar-container" },
                    h("slot", { name: "tab-bar" })),
                h("div", { class: "tabs-container" },
                    h("slot", null)),
            ]));
    }
    static get is() { return "gxg-tabs"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["tabs.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["tabs.css"]
    }; }
    static get properties() { return {
        "position": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "TabsPosition",
                "resolved": "\"bottom\" | \"left\" | \"right\" | \"top\"",
                "references": {
                    "TabsPosition": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": ""
            },
            "attribute": "position",
            "reflect": false,
            "defaultValue": "\"top\""
        },
        "height": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "Height",
                "resolved": "\"100%\" | \"auto\"",
                "references": {
                    "Height": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": ""
            },
            "attribute": "height",
            "reflect": true,
            "defaultValue": "\"auto\""
        }
    }; }
    static get states() { return {
        "activeTab": {}
    }; }
    static get elementRef() { return "element"; }
    static get listeners() { return [{
            "name": "tabActivated",
            "method": "tabActivatedHandler",
            "target": undefined,
            "capture": false,
            "passive": false
        }]; }
}
