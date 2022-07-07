'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

const index = require('./index-1115561c.js');

const tabsCss = ":host{display:block;height:auto;background:var(--color-background);border-radius:var(--border-width-md);-webkit-box-shadow:var(--box-shadow-01);box-shadow:var(--box-shadow-01)}:host([height=\"100%\"]){height:100%}.main-container{height:100%;display:-ms-flexbox;display:flex;-ms-flex-direction:column;flex-direction:column;-ms-flex-pack:justify;justify-content:space-between}.tabs-container{height:calc(100% - 32px);overflow-y:auto}::-webkit-scrollbar{width:6px}::-webkit-scrollbar-track{background-color:var(--gray-02);border-radius:10px}::-webkit-scrollbar-thumb{background:var(--gray-05);border-radius:10px}::-webkit-scrollbar-thumb:hover{background:var(--gray-04);cursor:pointer}:host([position=left]) .tab-bar-container{-webkit-transform:rotate(-90deg) translate(-100%, 0);transform:rotate(-90deg) translate(-100%, 0);-webkit-transform-origin:left top;transform-origin:left top;position:absolute;z-index:10}:host([position=left]) .tabs-container{position:absolute;left:32px;height:100%;z-index:1}:host([position=right]) .tab-bar-container{-webkit-transform:rotate(-90deg) translate(-100%, 0);transform:rotate(-90deg) translate(-100%, 0);-webkit-transform-origin:left top;transform-origin:left top;position:absolute;z-index:10;left:calc(100% - 32px)}:host([position=right]) .tabs-container{position:absolute;left:0;height:100%;z-index:1;width:calc(100% - 32px)}:host([position=right]) .tab-bar-menu{background-color:red}";

const GxgTabs = class {
    constructor(hostRef) {
        index.registerInstance(this, hostRef);
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
        return (index.h("div", { class: "main-container" }, this.position === "bottom"
            ? [
                index.h("div", { class: "tabs-container" }, index.h("slot", null)),
                index.h("div", { class: "tab-bar-container" }, index.h("slot", { name: "tab-bar" })),
            ]
            : [
                index.h("div", { class: "tab-bar-container" }, index.h("slot", { name: "tab-bar" })),
                index.h("div", { class: "tabs-container" }, index.h("slot", null)),
            ]));
    }
    get element() { return index.getElement(this); }
};
GxgTabs.style = tabsCss;

exports.gxg_tabs = GxgTabs;
