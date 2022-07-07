System.register([ "./p-d8a20c31.system.js" ], (function(t) {
  "use strict";
  var i, e, s, n, o, r;
  return {
    setters: [ function(t) {
      i = t.r, e = t.c, s = t.e, n = t.h, o = t.H, r = t.g;
    } ],
    execute: function() {
      t("ch_sidebar_menu_list_item", /** @class */ function() {
        function class_1(t) {
          i(this, t), this.itemClickedEvent = e(this, "itemClickedEvent", 7), this.listOneIcon = s("./sidebar-menu-list-item-assets/projects.svg"), 
          this.arrowTop = s("./sidebar-menu-list-item-assets/arrow-top.svg"), this.arrowRight = s("./sidebar-menu-list-item-assets/arrow-right.svg"), 
          this.collapsable = !1;
        }
        return class_1.prototype.componentWillLoad = function() {
          //SET THE TPYE OF ITEM
          this.el.parentElement.classList.contains("list-one") ? this.listTypeItem = "one" : this.el.parentElement.classList.contains("list-two") ? this.listTypeItem = "two" : this.listTypeItem = "three", 
          //SET COLLAPSABLE OR NOT
          null !== this.el.querySelector("ch-sidebar-menu-list") && (this.collapsable = !0);
        }, class_1.prototype.itemClicked = function() {
          if (!document.querySelector("ch-sidebar-menu").shadowRoot.getElementById("menu").classList.contains("collapsed")) {
            var t = this.el.getAttribute("id");
            this.itemClickedEvent.emit({
              "item-id": t
            });
          }
        }, class_1.prototype.firstListItemIcon = function() {
          return void 0 !== this.itemIconSrc ? this.itemIconSrc : this.listOneIcon;
        }, class_1.prototype.listItemContent = function() {
          return "one" === this.listTypeItem ? [ n("div", {
            class: "main-container",
            onClick: this.itemClicked.bind(this)
          }, n("div", {
            class: "left-container"
          }, n("span", {
            class: "icon custom-icon"
          }, n("ch-icon", {
            src: this.firstListItemIcon(),
            style: {
              "--icon-size": "20px",
              "--icon-color": "var(--first-list-icon-color)"
            }
          })), n("span", {
            class: "text"
          }, n("slot", null))), this.collapsable ? n("span", {
            class: "icon arrow-icon"
          }, n("ch-icon", {
            src: this.arrowTop,
            style: {
              "--icon-size": "20px",
              "--icon-color": "var(--first-list-arrow-color)"
            }
          })) : null), n("slot", {
            name: "list"
          }) ] : "two" === this.listTypeItem ? [ n("div", {
            class: "main-container",
            onClick: this.itemClicked.bind(this)
          }, this.collapsable ? n("span", {
            class: "icon arrow-icon"
          }, n("ch-icon", {
            src: this.arrowRight,
            style: {
              "--icon-size": "20px",
              "--icon-color": "var(--second-list-arrow-color)"
            }
          })) : null, n("span", {
            class: "text"
          }, n("slot", null))), n("slot", {
            name: "list"
          }) ] : "three" === this.listTypeItem ? n("div", {
            class: "main-container",
            onClick: this.itemClicked.bind(this)
          }, n("span", {
            class: "text"
          }, n("slot", null))) : void 0;
        }, class_1.prototype.render = function() {
          return n(o, {
            class: {
              item: !0,
              collapsable: this.collapsable,
              "list-one__item": "one" === this.listTypeItem,
              "list-two__item": "two" === this.listTypeItem,
              "list-three__item": "three" === this.listTypeItem
            }
          }, this.listItemContent());
        }, Object.defineProperty(class_1, "assetsDirs", {
          get: function() {
            return [ "sidebar-menu-list-item-assets" ];
          },
          enumerable: !1,
          configurable: !0
        }), Object.defineProperty(class_1.prototype, "el", {
          get: function() {
            return r(this);
          },
          enumerable: !1,
          configurable: !0
        }), class_1;
      }()).style = ":host{display:block;cursor:pointer}:host .icon{display:inline-block;line-height:0}:host .main-container{-webkit-transition:background-color 0.25s;transition:background-color 0.25s;position:relative}:host .main-container:hover{background-color:var(--item-hover-color)}:host .text{color:var(--text-color)}:host(.collapsable){overflow:hidden}:host(.item--active) .main-container{background-color:var(--item-active-color)}:host(.item) .main-container:active{background-color:var(--item-active-color)}:host(.list-one__item) .main-container{display:-ms-flexbox;display:flex;-ms-flex-pack:justify;justify-content:space-between;padding:8px 8px 8px 16px;min-height:40px;-webkit-box-sizing:border-box;box-sizing:border-box;-ms-flex-align:start;align-items:start}:host(.list-one__item) .main-container .left-container{display:-ms-flexbox;display:flex;-ms-flex-align:start;align-items:start;-webkit-padding-end:8px;padding-inline-end:8px}:host(.list-one__item) .main-container .left-container .custom-icon{-webkit-margin-end:8px;margin-inline-end:8px}:host(.list-one__item) .main-container .arrow-icon{-webkit-transform:rotate(180deg);transform:rotate(180deg);-webkit-transition:-webkit-transform 0.25s;transition:-webkit-transform 0.25s;transition:transform 0.25s;transition:transform 0.25s, -webkit-transform 0.25s}:host(.list-one__item.uncollapsed) .arrow-icon{-webkit-transform:rotate(0);transform:rotate(0)}:host(.list-one__item.speed-zero){-webkit-transition-duration:0.01s !important;transition-duration:0.01s !important}:host(.switch-order) .main-container{-ms-flex-direction:row-reverse;flex-direction:row-reverse}:host(.switch-order) .left-container{-ms-flex-direction:row-reverse;flex-direction:row-reverse;-webkit-padding-end:0 !important;padding-inline-end:0 !important}:host(.switch-order) .text{opacity:0}:host(.switch-order) .custom-icon{-webkit-margin-end:0 !important;margin-inline-end:0 !important}:host(.list-two__item) .main-container{display:-ms-flexbox;display:flex;-ms-flex-align:self-start;align-items:self-start;-webkit-padding-start:44px;padding-inline-start:44px;padding-top:6px;padding-bottom:6px;min-height:20px}:host(.list-two__item) .main-container .arrow-icon{-webkit-transform:rotate(0);transform:rotate(0);-webkit-transition:-webkit-transform 0.25s;transition:-webkit-transform 0.25s;transition:transform 0.25s;transition:transform 0.25s, -webkit-transform 0.25s;-webkit-margin-end:4px;margin-inline-end:4px}:host(.list-two__item) .main-container .text{font-size:14px;font-weight:700;letter-spacing:0.38px;text-transform:uppercase}:host(.list-two__item:not(.collapsable)) .main-container{-webkit-padding-start:51px;padding-inline-start:51px}:host(.list-two__item.uncollapsed) .arrow-icon{-webkit-transform:rotate(90deg);transform:rotate(90deg)}:host(.list-three__item) .main-container{-webkit-padding-start:73px;padding-inline-start:73px;padding-top:5px;padding-bottom:5px}";
    }
  };
}));