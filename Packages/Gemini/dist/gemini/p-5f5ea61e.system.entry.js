System.register([ "./p-d8a20c31.system.js" ], (function(t) {
  "use strict";
  var e, o, r;
  return {
    setters: [ function(t) {
      e = t.r, o = t.h, r = t.g;
    } ],
    execute: function() {
      t("gxg_tabs", /** @class */ function() {
        function class_1(t) {
          e(this, t), this.position = "top", this.height = "auto", this.activeTab = "";
        }
        return class_1.prototype.tabActivatedHandler = function(t) {
          //first, get the active tab, and set it´s outer-container overflow to hidden
          var e = this.element.querySelector("gxg-tab[is-selected]");
          null !== e && (e.shadowRoot.querySelector(".outer-container").style.overflow = "hidden");
          this.updateActiveChildren(t.target.tab, "gxg-tab-button"), this.updateActiveChildren(t.target.tab, "gxg-tab");
        }, class_1.prototype.updateActiveChildren = function(t, e) {
          for (var _loop_1 = function(e) {
            if (e.isSelected = t === e.tab, t === e.tab) {
              var o = e.shadowRoot.querySelector(".outer-container");
              "GXG-TAB" === e.tagName && (o = e.shadowRoot.querySelector(".outer-container"), 
              setTimeout((function() {
                o.style.overflow = "visible";
              }), 100));
            }
          }, o = 0, r = Array.from(this.element.querySelectorAll(e)); o < r.length; o++) {
            _loop_1(r[o]);
          }
        }, class_1.prototype.render = function() {
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
        }, Object.defineProperty(class_1.prototype, "element", {
          get: function() {
            return r(this);
          },
          enumerable: !1,
          configurable: !0
        }), class_1;
      }()).style = ':host{display:block;height:auto;background:var(--color-background);border-radius:var(--border-width-md);-webkit-box-shadow:var(--box-shadow-01);box-shadow:var(--box-shadow-01)}:host([height="100%"]){height:100%}.main-container{height:100%;display:-ms-flexbox;display:flex;-ms-flex-direction:column;flex-direction:column;-ms-flex-pack:justify;justify-content:space-between}.tabs-container{height:calc(100% - 32px);overflow-y:auto}::-webkit-scrollbar{width:6px}::-webkit-scrollbar-track{background-color:var(--gray-02);border-radius:10px}::-webkit-scrollbar-thumb{background:var(--gray-05);border-radius:10px}::-webkit-scrollbar-thumb:hover{background:var(--gray-04);cursor:pointer}:host([position=left]) .tab-bar-container{-webkit-transform:rotate(-90deg) translate(-100%, 0);transform:rotate(-90deg) translate(-100%, 0);-webkit-transform-origin:left top;transform-origin:left top;position:absolute;z-index:10}:host([position=left]) .tabs-container{position:absolute;left:32px;height:100%;z-index:1}:host([position=right]) .tab-bar-container{-webkit-transform:rotate(-90deg) translate(-100%, 0);transform:rotate(-90deg) translate(-100%, 0);-webkit-transform-origin:left top;transform-origin:left top;position:absolute;z-index:10;left:calc(100% - 32px)}:host([position=right]) .tabs-container{position:absolute;left:0;height:100%;z-index:1;width:calc(100% - 32px)}:host([position=right]) .tab-bar-menu{background-color:red}';
    }
  };
}));