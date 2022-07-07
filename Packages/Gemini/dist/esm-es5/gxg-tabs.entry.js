import { r as registerInstance, h, g as getElement } from './index-09b1517f.js';
var tabsCss = ":host{display:block;height:auto;background:var(--color-background);border-radius:var(--border-width-md);-webkit-box-shadow:var(--box-shadow-01);box-shadow:var(--box-shadow-01)}:host([height=\"100%\"]){height:100%}.main-container{height:100%;display:-ms-flexbox;display:flex;-ms-flex-direction:column;flex-direction:column;-ms-flex-pack:justify;justify-content:space-between}.tabs-container{height:calc(100% - 32px);overflow-y:auto}::-webkit-scrollbar{width:6px}::-webkit-scrollbar-track{background-color:var(--gray-02);border-radius:10px}::-webkit-scrollbar-thumb{background:var(--gray-05);border-radius:10px}::-webkit-scrollbar-thumb:hover{background:var(--gray-04);cursor:pointer}:host([position=left]) .tab-bar-container{-webkit-transform:rotate(-90deg) translate(-100%, 0);transform:rotate(-90deg) translate(-100%, 0);-webkit-transform-origin:left top;transform-origin:left top;position:absolute;z-index:10}:host([position=left]) .tabs-container{position:absolute;left:32px;height:100%;z-index:1}:host([position=right]) .tab-bar-container{-webkit-transform:rotate(-90deg) translate(-100%, 0);transform:rotate(-90deg) translate(-100%, 0);-webkit-transform-origin:left top;transform-origin:left top;position:absolute;z-index:10;left:calc(100% - 32px)}:host([position=right]) .tabs-container{position:absolute;left:0;height:100%;z-index:1;width:calc(100% - 32px)}:host([position=right]) .tab-bar-menu{background-color:red}";
var GxgTabs = /** @class */ (function () {
    function GxgTabs(hostRef) {
        registerInstance(this, hostRef);
        this.position = "top";
        this.height = "auto";
        this.activeTab = "";
    }
    GxgTabs.prototype.tabActivatedHandler = function (event) {
        //first, get the active tab, and set it´s outer-container overflow to hidden
        var activeTab = this.element.querySelector("gxg-tab[is-selected]");
        if (activeTab !== null) {
            var outerContainer = activeTab.shadowRoot.querySelector(".outer-container");
            outerContainer.style.overflow = "hidden";
        }
        this.updateActiveChildren(event.target.tab, "gxg-tab-button");
        this.updateActiveChildren(event.target.tab, "gxg-tab");
        //hide tab menu
    };
    GxgTabs.prototype.updateActiveChildren = function (activeTab, tagName) {
        var children = Array.from(this.element.querySelectorAll(tagName));
        var _loop_1 = function (child) {
            child.isSelected = activeTab === child.tab;
            if (activeTab === child.tab) {
                var outerContainer_1 = child.shadowRoot.querySelector(".outer-container");
                if (child.tagName === "GXG-TAB") {
                    outerContainer_1 = child.shadowRoot.querySelector(".outer-container");
                    setTimeout(function () {
                        outerContainer_1.style.overflow = "visible";
                    }, 100);
                }
            }
        };
        for (var _i = 0, children_1 = children; _i < children_1.length; _i++) {
            var child = children_1[_i];
            _loop_1(child);
        }
    };
    GxgTabs.prototype.render = function () {
        return (h("div", { class: "main-container" }, this.position === "bottom"
            ? [
                h("div", { class: "tabs-container" }, h("slot", null)),
                h("div", { class: "tab-bar-container" }, h("slot", { name: "tab-bar" })),
            ]
            : [
                h("div", { class: "tab-bar-container" }, h("slot", { name: "tab-bar" })),
                h("div", { class: "tabs-container" }, h("slot", null)),
            ]));
    };
    Object.defineProperty(GxgTabs.prototype, "element", {
        get: function () { return getElement(this); },
        enumerable: false,
        configurable: true
    });
    return GxgTabs;
}());
GxgTabs.style = tabsCss;
export { GxgTabs as gxg_tabs };
