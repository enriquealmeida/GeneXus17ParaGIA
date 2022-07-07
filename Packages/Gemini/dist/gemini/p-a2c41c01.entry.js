import { r as t, c as i, e, h as s, H as n, g as o } from "./p-eb9dc661.js";

const r = class {
  constructor(s) {
    t(this, s), this.itemClickedEvent = i(this, "itemClickedEvent", 7), this.listOneIcon = e("./sidebar-menu-list-item-assets/projects.svg"), 
    this.arrowTop = e("./sidebar-menu-list-item-assets/arrow-top.svg"), this.arrowRight = e("./sidebar-menu-list-item-assets/arrow-right.svg"), 
    this.collapsable = !1;
  }
  componentWillLoad() {
    //SET THE TPYE OF ITEM
    this.el.parentElement.classList.contains("list-one") ? this.listTypeItem = "one" : this.el.parentElement.classList.contains("list-two") ? this.listTypeItem = "two" : this.listTypeItem = "three", 
    //SET COLLAPSABLE OR NOT
    null !== this.el.querySelector("ch-sidebar-menu-list") && (this.collapsable = !0);
  }
  itemClicked() {
    if (!document.querySelector("ch-sidebar-menu").shadowRoot.getElementById("menu").classList.contains("collapsed")) {
      const t = this.el.getAttribute("id");
      this.itemClickedEvent.emit({
        "item-id": t
      });
    }
  }
  firstListItemIcon() {
    return void 0 !== this.itemIconSrc ? this.itemIconSrc : this.listOneIcon;
  }
  listItemContent() {
    return "one" === this.listTypeItem ? [ s("div", {
      class: "main-container",
      onClick: this.itemClicked.bind(this)
    }, s("div", {
      class: "left-container"
    }, s("span", {
      class: "icon custom-icon"
    }, s("ch-icon", {
      src: this.firstListItemIcon(),
      style: {
        "--icon-size": "20px",
        "--icon-color": "var(--first-list-icon-color)"
      }
    })), s("span", {
      class: "text"
    }, s("slot", null))), this.collapsable ? s("span", {
      class: "icon arrow-icon"
    }, s("ch-icon", {
      src: this.arrowTop,
      style: {
        "--icon-size": "20px",
        "--icon-color": "var(--first-list-arrow-color)"
      }
    })) : null), s("slot", {
      name: "list"
    }) ] : "two" === this.listTypeItem ? [ s("div", {
      class: "main-container",
      onClick: this.itemClicked.bind(this)
    }, this.collapsable ? s("span", {
      class: "icon arrow-icon"
    }, s("ch-icon", {
      src: this.arrowRight,
      style: {
        "--icon-size": "20px",
        "--icon-color": "var(--second-list-arrow-color)"
      }
    })) : null, s("span", {
      class: "text"
    }, s("slot", null))), s("slot", {
      name: "list"
    }) ] : "three" === this.listTypeItem ? s("div", {
      class: "main-container",
      onClick: this.itemClicked.bind(this)
    }, s("span", {
      class: "text"
    }, s("slot", null))) : void 0;
  }
  render() {
    return s(n, {
      class: {
        item: !0,
        collapsable: this.collapsable,
        "list-one__item": "one" === this.listTypeItem,
        "list-two__item": "two" === this.listTypeItem,
        "list-three__item": "three" === this.listTypeItem
      }
    }, this.listItemContent());
  }
  static get assetsDirs() {
    return [ "sidebar-menu-list-item-assets" ];
  }
  get el() {
    return o(this);
  }
};

r.style = ":host{display:block;cursor:pointer}:host .icon{display:inline-block;line-height:0}:host .main-container{-webkit-transition:background-color 0.25s;transition:background-color 0.25s;position:relative}:host .main-container:hover{background-color:var(--item-hover-color)}:host .text{color:var(--text-color)}:host(.collapsable){overflow:hidden}:host(.item--active) .main-container{background-color:var(--item-active-color)}:host(.item) .main-container:active{background-color:var(--item-active-color)}:host(.list-one__item) .main-container{display:-ms-flexbox;display:flex;-ms-flex-pack:justify;justify-content:space-between;padding:8px 8px 8px 16px;min-height:40px;-webkit-box-sizing:border-box;box-sizing:border-box;-ms-flex-align:start;align-items:start}:host(.list-one__item) .main-container .left-container{display:-ms-flexbox;display:flex;-ms-flex-align:start;align-items:start;-webkit-padding-end:8px;padding-inline-end:8px}:host(.list-one__item) .main-container .left-container .custom-icon{-webkit-margin-end:8px;margin-inline-end:8px}:host(.list-one__item) .main-container .arrow-icon{-webkit-transform:rotate(180deg);transform:rotate(180deg);-webkit-transition:-webkit-transform 0.25s;transition:-webkit-transform 0.25s;transition:transform 0.25s;transition:transform 0.25s, -webkit-transform 0.25s}:host(.list-one__item.uncollapsed) .arrow-icon{-webkit-transform:rotate(0);transform:rotate(0)}:host(.list-one__item.speed-zero){-webkit-transition-duration:0.01s !important;transition-duration:0.01s !important}:host(.switch-order) .main-container{-ms-flex-direction:row-reverse;flex-direction:row-reverse}:host(.switch-order) .left-container{-ms-flex-direction:row-reverse;flex-direction:row-reverse;-webkit-padding-end:0 !important;padding-inline-end:0 !important}:host(.switch-order) .text{opacity:0}:host(.switch-order) .custom-icon{-webkit-margin-end:0 !important;margin-inline-end:0 !important}:host(.list-two__item) .main-container{display:-ms-flexbox;display:flex;-ms-flex-align:self-start;align-items:self-start;-webkit-padding-start:44px;padding-inline-start:44px;padding-top:6px;padding-bottom:6px;min-height:20px}:host(.list-two__item) .main-container .arrow-icon{-webkit-transform:rotate(0);transform:rotate(0);-webkit-transition:-webkit-transform 0.25s;transition:-webkit-transform 0.25s;transition:transform 0.25s;transition:transform 0.25s, -webkit-transform 0.25s;-webkit-margin-end:4px;margin-inline-end:4px}:host(.list-two__item) .main-container .text{font-size:14px;font-weight:700;letter-spacing:0.38px;text-transform:uppercase}:host(.list-two__item:not(.collapsable)) .main-container{-webkit-padding-start:51px;padding-inline-start:51px}:host(.list-two__item.uncollapsed) .arrow-icon{-webkit-transform:rotate(90deg);transform:rotate(90deg)}:host(.list-three__item) .main-container{-webkit-padding-start:73px;padding-inline-start:73px;padding-top:5px;padding-bottom:5px}";

export { r as ch_sidebar_menu_list_item }