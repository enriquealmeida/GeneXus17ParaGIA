import { r as t, h as e, H as r, g as a } from "./p-eb9dc661.js";

const o = class {
  constructor(e) {
    t(this, e), this.appendedButtons = 0, this.tabBarMenuHeight = "100px", 
    /**
         * Reading direction
         */
    this.rtl = !1, this.detectClickOutsideTabBarMenu = this.detectClickOutsideTabBarMenu.bind(this);
  }
  toggleMenu(t) {
    t.stopPropagation(), this.tabBarMenu.classList.toggle("tab-bar-menu--collapsed"), 
    document.addEventListener("click", this.detectClickOutsideTabBarMenu);
  }
  tabActivatedHandler() {
    this.el.shadowRoot.querySelector(".tab-bar-menu").classList.add("tab-bar-menu--collapsed");
  }
  appendTabItemsToMenu() {
    //This function appends tab-buttons into a tab-menu, as long as the tab-buttons are too tight
    const t = this.el.parentElement.getAttribute("position"), e = this.el.children.item(0).clientHeight;
    if ("top" === t || "bottom" === t) {
      const t = this.el.shadowRoot.querySelector(".tab-bar").offsetWidth, e = this.el.parentElement.offsetWidth;
      if (t + 20 > e) {
        const t = this.el.querySelectorAll("[slot=tab-bar]"), e = t[t.length - 1];
        //get the last item of the nodeList
                //add "menu-button" class to button component, in order to stylize the buttons inside the menu differently
        e.classList.add("menu-button"), e.setAttribute("slot", "tab-menu"), this.appendedButtons++;
      } else {
        const r = this.el.querySelectorAll("[slot=tab-menu]")[0];
        void 0 !== r && e > t + r.offsetWidth && (r.classList.remove("menu-button"), r.setAttribute("slot", "tab-bar"), 
        this.appendedButtons--);
      }
    } else if ("left" === t || "right" === t) {
      const t = this.el.parentElement.offsetHeight, e = this.el.offsetWidth;
      if (e > t) {
        //tabBarHeight is higher than gxgTabsHeight
        const t = this.el.querySelectorAll("[slot=tab-bar]"), e = t[t.length - 1];
        //get the last item of the nodeList
                //add "menu-button" class to button component, in order to stylize the buttons inside the menu differently
        e.classList.add("menu-button"), e.setAttribute("slot", "tab-menu"), this.appendedButtons++;
      } else {
        //tabBarHeight is lower than gxgTabsHeight
        const r = this.el.querySelectorAll("[slot=tab-menu]")[0];
        void 0 !== r && t > e + r.offsetWidth && (r.classList.remove("menu-button"), r.setAttribute("slot", "tab-bar"), 
        this.appendedButtons--);
      }
    }
    this.tabBarMenuHeight = this.appendedButtons * e + "px";
  }
  componentDidLoad() {
    //Reading Direction
    const t = document.getElementsByTagName("html")[0].getAttribute("dir"), e = document.getElementsByTagName("body")[0].getAttribute("dir");
    "rtl" !== t && "rtl" !== e || (this.rtl = !0), requestAnimationFrame(() => this.appendTabItemsToMenu());
    const r = new ResizeObserver(t => {
      t.forEach(() => {
        //get any button space between text and button border
        this.appendTabItemsToMenu();
      });
    }), a = this.el.parentElement;
    r.observe(a), 
    //Collapse buttons if they dont't fit in the available space already
    setTimeout(function() {
      const t = this.el.querySelectorAll("gxg-tab-button").length;
      for (let e = 0; e <= t; e++) this.appendTabItemsToMenu();
    }.bind(this), 100);
    //Tabbar menu on bottom
    const o = this.el.parentElement.getAttribute("position");
    if ("bottom" === o) {
      const t = this.el.shadowRoot.querySelector(".tab-bar-menu");
      console.log("tabBarMenu", t), t.classList.add("bottom");
    }
    //Tabbar menu on right
        if ("right" === o) {
      this.el.shadowRoot.querySelector(".tab-bar-menu").classList.add("right");
    }
    this.setIndexToTabButtons();
  }
  setIndexToTabButtons() {
    this.el.querySelectorAll("gxg-tab-button").forEach((t, e) => {
      t.setAttribute("data-index", e.toString());
    });
  }
  renderTabBarMenu() {
    if (this.appendedButtons > 0) return e("div", {
      class: "tab-bar__menu"
    }, e("gxg-button", {
      onClick: this.toggleMenu.bind(this),
      type: "tertiary",
      icon: "gemini-tools/show-more-vertical"
    }));
  }
  detectClickOutsideTabBarMenu(t) {
    const e = this.el.shadowRoot.querySelector(".tab-bar-menu"), r = t.x, a = t.y, o = e.getBoundingClientRect();
    r > o.left && r < o.right && a > o.top && a < o.bottom || 
    //Click happened outside the menu
    e.classList.add("tab-bar-menu--collapsed");
  }
  componentDidUnload() {
    document.removeEventListener("click", this.detectClickOutsideTabBarMenu);
  }
  render() {
    return e(r, {
      class: {
        rtl: this.rtl
      }
    }, e("nav", {
      class: {
        nav: !0
      }
    }, e("ul", {
      class: "tab-bar"
    }, e("slot", {
      name: "tab-bar"
    })), this.renderTabBarMenu(), e("ul", {
      class: "tab-bar-menu tab-bar-menu--collapsed",
      style: {
        "--tabBarMenuHeight": this.tabBarMenuHeight
      },
      ref: t => this.tabBarMenu = t
    }, e("slot", {
      name: "tab-menu"
    }))));
  }
  get el() {
    return a(this);
  }
};

o.style = ":root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{--tabBarMenuHeight:0;display:-ms-inline-flexbox;display:inline-flex;text-align:right;position:relative;display:-ms-flexbox;display:flex;-ms-flex-pack:justify;justify-content:space-between}:host .nav{display:-ms-flexbox;display:flex;-ms-flex-pack:justify;justify-content:space-between;width:100%}:host .tab-bar{list-style-type:none;padding-left:0;display:-ms-flexbox !important;display:flex !important;margin-bottom:0;margin-top:0;font-family:var(--font-family-primary);background-color:var(--color-background)}:host .tab-bar__menu{display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center}:host .tab-bar-menu{border-radius:var(--border-width-md);-webkit-box-shadow:var(--box-shadow-01);box-shadow:var(--box-shadow-01);height:var(--tabBarMenuHeight);position:absolute;z-index:10;background-color:var(--color-on-primary);right:0;margin:0;padding:0;list-style:none;display:-ms-inline-flexbox;display:inline-flex;-ms-flex-direction:column;flex-direction:column;overflow:hidden;-webkit-transition-property:height;transition-property:height;-webkit-transition-duration:var(--ui-animaton-speed);transition-duration:var(--ui-animaton-speed);-webkit-transition-timing-function:ease;transition-timing-function:ease}:host .tab-bar-menu--collapsed{height:0}:host .tab-bar-menu.bottom{bottom:0}:host .tab-bar-menu.right{bottom:0}:host(.rtl) .tab-bar-menu{left:0;right:auto}";

export { o as gxg_tab_bar }