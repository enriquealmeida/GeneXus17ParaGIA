System.register([ "./p-d8a20c31.system.js" ], (function(t) {
  "use strict";
  var e, r, a, o;
  return {
    setters: [ function(t) {
      e = t.r, r = t.h, a = t.H, o = t.g;
    } ],
    execute: function() {
      t("gxg_tab_bar", /** @class */ function() {
        function class_1(t) {
          e(this, t), this.appendedButtons = 0, this.tabBarMenuHeight = "100px", 
          /**
                     * Reading direction
                     */
          this.rtl = !1, this.detectClickOutsideTabBarMenu = this.detectClickOutsideTabBarMenu.bind(this);
        }
        return class_1.prototype.toggleMenu = function(t) {
          t.stopPropagation(), this.tabBarMenu.classList.toggle("tab-bar-menu--collapsed"), 
          document.addEventListener("click", this.detectClickOutsideTabBarMenu);
        }, class_1.prototype.tabActivatedHandler = function() {
          this.el.shadowRoot.querySelector(".tab-bar-menu").classList.add("tab-bar-menu--collapsed");
        }, class_1.prototype.appendTabItemsToMenu = function() {
          //This function appends tab-buttons into a tab-menu, as long as the tab-buttons are too tight
          var t = this.el.parentElement.getAttribute("position"), e = this.el.children.item(0).clientHeight;
          if ("top" === t || "bottom" === t) {
            var r = this.el.shadowRoot.querySelector(".tab-bar").offsetWidth, a = this.el.parentElement.offsetWidth;
            if (r + 20 > a) 
            //add "menu-button" class to button component, in order to stylize the buttons inside the menu differently
            (i = (o = this.el.querySelectorAll("[slot=tab-bar]"))[o.length - 1]).classList.add("menu-button"), 
            i.setAttribute("slot", "tab-menu"), this.appendedButtons++; else void 0 !== (n = this.el.querySelectorAll("[slot=tab-menu]")[0]) && a > r + n.offsetWidth && (n.classList.remove("menu-button"), 
            n.setAttribute("slot", "tab-bar"), this.appendedButtons--);
          } else if ("left" === t || "right" === t) {
            var o, i, n, l = this.el.parentElement.offsetHeight, s = this.el.offsetWidth;
            if (s > l) 
            //add "menu-button" class to button component, in order to stylize the buttons inside the menu differently
            (i = (o = this.el.querySelectorAll("[slot=tab-bar]"))[o.length - 1]).classList.add("menu-button"), 
            i.setAttribute("slot", "tab-menu"), this.appendedButtons++; else void 0 !== (n = this.el.querySelectorAll("[slot=tab-menu]")[0]) && l > s + n.offsetWidth && (n.classList.remove("menu-button"), 
            n.setAttribute("slot", "tab-bar"), this.appendedButtons--);
          }
          this.tabBarMenuHeight = this.appendedButtons * e + "px";
        }, class_1.prototype.componentDidLoad = function() {
          var t = this, e = document.getElementsByTagName("html")[0].getAttribute("dir"), r = document.getElementsByTagName("body")[0].getAttribute("dir");
          //Reading Direction
                    "rtl" !== e && "rtl" !== r || (this.rtl = !0), requestAnimationFrame((function() {
            return t.appendTabItemsToMenu();
          }));
          var a = new ResizeObserver((function(e) {
            e.forEach((function() {
              //get any button space between text and button border
              t.appendTabItemsToMenu();
            }));
          })), o = this.el.parentElement;
          a.observe(o), 
          //Collapse buttons if they dont't fit in the available space already
          setTimeout(function() {
            for (var t = this.el.querySelectorAll("gxg-tab-button").length, e = 0; e <= t; e++) this.appendTabItemsToMenu();
          }.bind(this), 100);
          //Tabbar menu on bottom
          var i = this.el.parentElement.getAttribute("position");
          if ("bottom" === i) {
            var n = this.el.shadowRoot.querySelector(".tab-bar-menu");
            console.log("tabBarMenu", n), n.classList.add("bottom");
          }
          //Tabbar menu on right
                    "right" === i && (n = this.el.shadowRoot.querySelector(".tab-bar-menu")).classList.add("right");
          this.setIndexToTabButtons();
        }, class_1.prototype.setIndexToTabButtons = function() {
          this.el.querySelectorAll("gxg-tab-button").forEach((function(t, e) {
            t.setAttribute("data-index", e.toString());
          }));
        }, class_1.prototype.renderTabBarMenu = function() {
          if (this.appendedButtons > 0) return r("div", {
            class: "tab-bar__menu"
          }, r("gxg-button", {
            onClick: this.toggleMenu.bind(this),
            type: "tertiary",
            icon: "gemini-tools/show-more-vertical"
          }));
        }, class_1.prototype.detectClickOutsideTabBarMenu = function(t) {
          var e = this.el.shadowRoot.querySelector(".tab-bar-menu"), r = t.x, a = t.y, o = e.getBoundingClientRect();
          r > o.left && r < o.right && a > o.top && a < o.bottom || 
          //Click happened outside the menu
          e.classList.add("tab-bar-menu--collapsed");
        }, class_1.prototype.componentDidUnload = function() {
          document.removeEventListener("click", this.detectClickOutsideTabBarMenu);
        }, class_1.prototype.render = function() {
          var t = this;
          return r(a, {
            class: {
              rtl: this.rtl
            }
          }, r("nav", {
            class: {
              nav: !0
            }
          }, r("ul", {
            class: "tab-bar"
          }, r("slot", {
            name: "tab-bar"
          })), this.renderTabBarMenu(), r("ul", {
            class: "tab-bar-menu tab-bar-menu--collapsed",
            style: {
              "--tabBarMenuHeight": this.tabBarMenuHeight
            },
            ref: function(e) {
              return t.tabBarMenu = e;
            }
          }, r("slot", {
            name: "tab-menu"
          }))));
        }, Object.defineProperty(class_1.prototype, "el", {
          get: function() {
            return o(this);
          },
          enumerable: !1,
          configurable: !0
        }), class_1;
      }()).style = ":root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{--tabBarMenuHeight:0;display:-ms-inline-flexbox;display:inline-flex;text-align:right;position:relative;display:-ms-flexbox;display:flex;-ms-flex-pack:justify;justify-content:space-between}:host .nav{display:-ms-flexbox;display:flex;-ms-flex-pack:justify;justify-content:space-between;width:100%}:host .tab-bar{list-style-type:none;padding-left:0;display:-ms-flexbox !important;display:flex !important;margin-bottom:0;margin-top:0;font-family:var(--font-family-primary);background-color:var(--color-background)}:host .tab-bar__menu{display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center}:host .tab-bar-menu{border-radius:var(--border-width-md);-webkit-box-shadow:var(--box-shadow-01);box-shadow:var(--box-shadow-01);height:var(--tabBarMenuHeight);position:absolute;z-index:10;background-color:var(--color-on-primary);right:0;margin:0;padding:0;list-style:none;display:-ms-inline-flexbox;display:inline-flex;-ms-flex-direction:column;flex-direction:column;overflow:hidden;-webkit-transition-property:height;transition-property:height;-webkit-transition-duration:var(--ui-animaton-speed);transition-duration:var(--ui-animaton-speed);-webkit-transition-timing-function:ease;transition-timing-function:ease}:host .tab-bar-menu--collapsed{height:0}:host .tab-bar-menu.bottom{bottom:0}:host .tab-bar-menu.right{bottom:0}:host(.rtl) .tab-bar-menu{left:0;right:auto}";
    }
  };
}));