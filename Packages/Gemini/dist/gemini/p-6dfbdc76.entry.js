import { r as t, h as o, g as e } from "./p-eb9dc661.js";

const r = class {
  constructor(o) {
    t(this, o), this.position = "top", this.height = "auto", this.activeTab = "";
  }
  tabActivatedHandler(t) {
    //first, get the active tab, and set it´s outer-container overflow to hidden
    const o = this.element.querySelector("gxg-tab[is-selected]");
    if (null !== o) {
      o.shadowRoot.querySelector(".outer-container").style.overflow = "hidden";
    }
    this.updateActiveChildren(t.target.tab, "gxg-tab-button"), this.updateActiveChildren(t.target.tab, "gxg-tab");
  }
  updateActiveChildren(t, o) {
    const e = Array.from(this.element.querySelectorAll(o));
    for (const r of e) if (r.isSelected = t === r.tab, t === r.tab) {
      let t = r.shadowRoot.querySelector(".outer-container");
      "GXG-TAB" === r.tagName && (t = r.shadowRoot.querySelector(".outer-container"), 
      setTimeout((function() {
        t.style.overflow = "visible";
      }), 100));
    }
  }
  render() {
    return o("div", {
      class: "main-container"
    }, "bottom" === this.position ? [ o("div", {
      class: "tabs-container"
    }, o("slot", null)), o("div", {
      class: "tab-bar-container"
    }, o("slot", {
      name: "tab-bar"
    })) ] : [ o("div", {
      class: "tab-bar-container"
    }, o("slot", {
      name: "tab-bar"
    })), o("div", {
      class: "tabs-container"
    }, o("slot", null)) ]);
  }
  get element() {
    return e(this);
  }
};

r.style = ':host{display:block;height:auto;background:var(--color-background);border-radius:var(--border-width-md);-webkit-box-shadow:var(--box-shadow-01);box-shadow:var(--box-shadow-01)}:host([height="100%"]){height:100%}.main-container{height:100%;display:-ms-flexbox;display:flex;-ms-flex-direction:column;flex-direction:column;-ms-flex-pack:justify;justify-content:space-between}.tabs-container{height:calc(100% - 32px);overflow-y:auto}::-webkit-scrollbar{width:6px}::-webkit-scrollbar-track{background-color:var(--gray-02);border-radius:10px}::-webkit-scrollbar-thumb{background:var(--gray-05);border-radius:10px}::-webkit-scrollbar-thumb:hover{background:var(--gray-04);cursor:pointer}:host([position=left]) .tab-bar-container{-webkit-transform:rotate(-90deg) translate(-100%, 0);transform:rotate(-90deg) translate(-100%, 0);-webkit-transform-origin:left top;transform-origin:left top;position:absolute;z-index:10}:host([position=left]) .tabs-container{position:absolute;left:32px;height:100%;z-index:1}:host([position=right]) .tab-bar-container{-webkit-transform:rotate(-90deg) translate(-100%, 0);transform:rotate(-90deg) translate(-100%, 0);-webkit-transform-origin:left top;transform-origin:left top;position:absolute;z-index:10;left:calc(100% - 32px)}:host([position=right]) .tabs-container{position:absolute;left:0;height:100%;z-index:1;width:calc(100% - 32px)}:host([position=right]) .tab-bar-menu{background-color:red}';

export { r as gxg_tabs }