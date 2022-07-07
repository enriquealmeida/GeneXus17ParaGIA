import { r as t, c as e, e as i, h as o, H as s, g as n } from "./p-eb9dc661.js";

const r = class {
  constructor(o) {
    t(this, o), this.itemClicked = e(this, "itemClicked", 7), this.iconArrowLeft = i("./sidebar-menu-assets/arrow-left.svg"), 
    this.topHeightSpeed = 300, this.speedDivisionValue = 400, 
    /**
         * The presence of this attribute allows the menu to have only one list opened at the same time
         */
    this.singleListOpen = !1;
  }
  componentWillLoad() {}
  componentDidLoad() {
    const t = this.title.offsetHeight, e = this.footer.offsetHeight, i = t + e + "px", o = this.el.querySelectorAll(".collapsable");
    // const items = document.getElementsByClassName("item");
    /*Set main height*/
    this.main.style.height = `calc(100vh - ${i})`
    /*Set main top position*/ , this.main.style.top = t + "px";
    //SET INITAL ITEMS MAX HEIGHT
    const s = this.el.querySelectorAll(".item");
    /*******************************
        SINGLE UL1 OPEN AT A TIME LOGIC
        *******************************/
    //Only one list of type 1 can be open at the same time.
    //This is an optional parameter. Applies if 'data-single-ul1-open' attribute is present on #menu
    if (Array.from(s).forEach((function(t) {
      const e = t.shadowRoot.querySelector(".main-container");
      t.style.maxHeight = e.offsetHeight + "px";
    })), 
    /**************
        SET ACTIVE ITEM
        ***************/
    Array.from(s).forEach(function(t) {
      t.addEventListener("click", function() {
        if (!this.menu.classList.contains("collapsed")) {
          //remove current active item class
          const e = document.querySelector(".item--active");
          null !== e && e.classList.remove("item--active"), 
          //set current item as active
          t.classList.add("item--active");
        }
      }.bind(this));
    }.bind(this)), 
    /****************************
        COLLAPSABLE LIST ITEMS LOGIC
        ****************************/
    Array.from(o).forEach(function(t) {
      t.shadowRoot.querySelector(".main-container").addEventListener("click", function() {
        if (t.classList.contains("list-one__item") && this.menu.classList.contains("collapsed")) 
        //If item clicked is type 1, and menu is collapsed, just uncollapse the menu.
        this.collapseButton.click(); else 
        //If this item is type 2, then update list 1 transition speed and maxheight
        if (this.toggleIcon(t), //This function has to be before  uncollapseList or collapseList.
        this.setTransitionSpeed(t), t.classList.contains("uncollapsed") ? this.uncollapseList(t) : this.collapseList(t), 
        t.classList.contains("list-two__item")) {
          const e = t.closest(".list-one__item");
          let i = t.querySelector(".list-three").offsetHeight;
          this.updateListItem1TransitionSpeed(e, i), 
          //Update list 1 max. height
          this.updateListItem1MaxHeight(e);
        }
      }.bind(this));
    }.bind(this)), this.singleListOpen) {
      const t = document.querySelectorAll(".list-one__item");
      Array.from(t).forEach(function(t) {
        t.shadowRoot.querySelector(".main-container").addEventListener("click", function() {
          if (!this.menu.classList.contains("collapsed")) {
            const e = document.querySelector(".lastUl1Opened");
            if (null !== e && !t.classList.contains("lastUl1Opened")) {
              e.shadowRoot.querySelector(".main-container").click();
            }
            t.classList.contains("uncollapsed") ? t.classList.add("lastUl1Opened") : t.classList.remove("lastUl1Opened");
          }
        }.bind(this));
      }.bind(this));
    }
    /*******************
        COLLAPSE MENU LOGIC
        *******************/    this.collapseButton.addEventListener("click", function() {
      let t = 0;
      this.menu.classList.contains("collapsed") ? (
      //if menu is collapsed, the animation that shows the menu should be quicker
      this.menu.classList.add("collapse-faster"), t = 300) : (this.menu.classList.remove("collapse-faster"), 
      t = 600), this.menu.classList.add("collapsing"), this.hideIndicator(), setTimeout(function() {
        this.menu.classList.contains("collapsed") ? (this.uncollapseCollapsedLists(), this.undoSwitchListOneOrder(), 
        this.menu.classList.remove("collapsed"), setTimeout(function() {
          this.repositionIndicatorAfterMenuUncollapse();
        }.bind(this), 50)) : (this.collapseUncollapsedLists1(), this.switchListOneOrder(), 
        this.menu.classList.add("collapsed"), setTimeout(function() {
          this.repositionIndicatorAfterMenuCollapse();
        }.bind(this), 50)), this.menu.classList.remove("collapsing"), setTimeout(function() {
          this.showIndicator();
        }.bind(this), 400);
      }.bind(this), t);
    }.bind(this));
    /******************
        ITEMS TOOLTIP LOGIC
        *******************/
    var n = document.createElement("DIV");
    n.classList.add("tooltip"), n.style.zIndex = "0", this.menu.appendChild(n), Array.from(s).forEach(function(t) {
      const e = t.shadowRoot.querySelector(".main-container");
      e.addEventListener("mouseenter", function() {
        if (this.menu.classList.contains("collapsed")) {
          n.classList.add("visible"), n.innerHTML = t.childNodes[0].nodeValue;
          const e = t.getBoundingClientRect().y;
          n.style.top = e + "px";
        }
      }.bind(this)), e.addEventListener("mouseleave", function() {
        this.menu.classList.contains("collapsed") && n.classList.remove("visible");
      }.bind(this));
    }.bind(this)), 
    /**********************************
        LATERAL ACTIVE ITEM INDICATOR LOGIC
        ***********************************/
    this.indicator = document.createElement("DIV"), this.indicator.setAttribute("id", "indicator"), 
    this.main.appendChild(this.indicator), Array.from(s).forEach(function(e) {
      e.addEventListener("click", function(i) {
        if (i.stopPropagation(), !this.menu.classList.contains("collapsed")) {
          const i = e.getBoundingClientRect().y, o = e.shadowRoot.querySelector(".main-container").offsetHeight;
          if (this.singleListOpen && e.classList.contains("list-one__item")) {
            let i = e, s = t;
            for (;null != (i = i.previousElementSibling); ) {
              s += i.shadowRoot.querySelector(".main-container").offsetHeight;
            }
            this.indicator.style.top = s + "px", this.indicator.style.height = o + "px";
          } else this.indicator.style.top = i + "px", this.indicator.style.height = o + "px";
        }
      }.bind(this));
    }.bind(this));
    //REPOSITION INDICATOR ON SCROLL
    var r = null;
    this.main.addEventListener("scroll", function() {
      //get reference to current active item
      const t = document.querySelector(".item--active");
      if (null !== t) {
        let e = t.getBoundingClientRect().y;
        this.indicator.classList.add("speed-zero"), this.indicator.style.top = e + "px", 
        //detect when scrolling has stopped
        null !== r && clearTimeout(r), r = setTimeout(function() {
          this.indicator.classList.remove("speed-zero");
        }.bind(this), 50);
      }
    }.bind(this));
  }
  // End of ComponentDidLoad
  //REPOSITION INDICATOR AFTER MENU COLLAPSE
  repositionIndicatorAfterMenuCollapse() {
    const t = this.el.querySelector(".item--active");
    if (null !== t) {
      const e = t.closest(".list-one__item");
      if (null !== e) {
        const t = e.shadowRoot.querySelector(".main-container"), i = t.getBoundingClientRect().y, o = t.offsetHeight;
        this.indicator.style.top = i + "px", this.indicator.style.height = o + "px";
      } else {
        //else, the active item has no parent l ist. just reposition indicator
        const e = t.getBoundingClientRect().y, i = t.offsetHeight;
        this.indicator.style.top = e + "px", this.indicator.style.height = i + "px";
      }
    }
  }
  //REPOSITION INDICATOR AFTER MENU UNCOLLAPSE
  repositionIndicatorAfterMenuUncollapse() {
    const t = this.el.querySelector(".item--active").shadowRoot.querySelector(".main-container"), e = t.getBoundingClientRect().y, i = t.offsetHeight;
    this.indicator.style.top = e + "px", this.indicator.style.height = i + "px";
  }
  //HIDE INDICATOR
  hideIndicator() {
    this.indicator.classList.add("hide");
  }
  //SHOW INDICATOR
  showIndicator() {
    this.indicator.classList.remove("hide");
  }
  collapseUncollapsedLists1() {
    let t = document.querySelectorAll(".list-one__item.uncollapsed");
    Array.from(t).forEach((function(t) {
      t.classList.add("speed-zero"), t.setAttribute("data-uncollapsed-max-height", t.style.maxHeight), 
      t.style.maxHeight = t.shadowRoot.querySelector(".main-container").offsetHeight + "px";
    }));
  }
  uncollapseCollapsedLists() {
    let t = document.querySelectorAll(".list-one__item.uncollapsed");
    Array.from(t).forEach((function(t) {
      t.addEventListener("transitionend", (function removeSpeedZero(e) {
        "max-height" === e.propertyName && (t.classList.remove("speed-zero"), t.removeEventListener("transitionend", removeSpeedZero));
      }));
      const e = t.getAttribute("data-uncollapsed-max-height");
      t.style.maxHeight = e, t.removeAttribute("data-uncollapsed-max-height");
    }));
  }
  switchListOneOrder() {
    let t = document.querySelectorAll(".list-one__item");
    Array.from(t).forEach((function(t) {
      t.classList.add("switch-order");
    }));
  }
  undoSwitchListOneOrder() {
    let t = document.querySelectorAll(".list-one__item");
    Array.from(t).forEach((function(t) {
      t.classList.remove("switch-order");
    }));
  }
  /*UPDATE LIST ITEM 1 TRANSITION SPEED*/  updateListItem1TransitionSpeed(t, e) {
    e > this.topHeightSpeed && (e = this.topHeightSpeed), t.style.transitionDuration = e / this.speedDivisionValue + "s";
  }
  /*UPDATE LIST ITEM 1 MAX HEIGHT*/  updateListItem1MaxHeight(t) {
    const e = t.shadowRoot.querySelector(".main-container").clientHeight, i = t.querySelectorAll(":scope > ch-sidebar-menu-list > ch-sidebar-menu-list-item");
    let o = e;
    Array.from(i).forEach((function(t) {
      o += parseInt(t.style.maxHeight.slice(0, -2));
    })), t.style.maxHeight = o + "px";
  }
  /*TOGGLE ITEM ICON*/  toggleIcon(t) {
    t.classList.contains("uncollapsed") ? t.classList.remove("uncollapsed") : t.classList.add("uncollapsed");
  }
  /*SET ITEM TRANSITION SPEED*/  setTransitionSpeed(t) {
    let e = 0;
    const i = t.querySelector("ch-sidebar-menu-list").clientHeight;
    e = i > this.topHeightSpeed ? this.topHeightSpeed : i, t.style.transitionDuration = e / this.speedDivisionValue + "s";
  }
  /*COLLAPSE LIST*/  collapseList(t) {
    const e = t.shadowRoot.querySelector(".main-container").offsetHeight;
    t.style.maxHeight = e + "px";
  }
  /*UNCOLLAPSE LIST*/  uncollapseList(t) {
    const e = t.shadowRoot.querySelector(".main-container").clientHeight, i = t.querySelector("ch-sidebar-menu-list").clientHeight;
    t.style.maxHeight = e + i + "px";
  }
  render() {
    return o(s, null, o("nav", {
      id: "menu",
      ref: t => this.menu = t
    }, o("h1", {
      id: "title",
      ref: t => this.title = t
    }, this.menuTitle), o("main", {
      id: "main",
      ref: t => this.main = t
    }, o("slot", null)), o("footer", {
      id: "footer",
      ref: t => this.footer = t
    }, o("div", {
      id: "custom-content"
    }, o("slot", {
      name: "footer"
    })), o("div", {
      id: "collapse-menu",
      ref: t => this.collapseButton = t
    }, o("ch-icon", {
      style: {
        "--icon-color": "var(--first-list-arrow-color)",
        "--icon-size": "20px"
      },
      src: this.iconArrowLeft
    })))));
  }
  static get assetsDirs() {
    return [ "sidebar-menu-assets" ];
  }
  get el() {
    return n(this);
  }
};

r.style = ':host{--menu-font-family:"Source Sans Pro", sans-serif;--menu-background-color:var(--whiteSmoke);--text-color:var(--black);--item-hover-color:var(--lightGray);--item-active-color:var(--silverGray);--first-list-icon-color:var(--black);--first-list-arrow-color:var(--black);--second-list-arrow-color:var(--black);--footer-line-color:var(--black);--indicator-color:var(--black);--scrollbar-track:var(--lightGray);--scrollbar-thumb:var(--darkGray);display:block;}:host #menu{background-color:var(--menu-background-color);font-family:var(--menu-font-family);width:240px;position:fixed;height:100vh;left:0;top:0;z-index:5;-webkit-transition:left 0.6s;transition:left 0.6s;-webkit-transition-timing-function:cubic-bezier(0.25, 0.1, 0.25, 1);transition-timing-function:cubic-bezier(0.25, 0.1, 0.25, 1)}:host #menu #title{text-transform:uppercase;font-weight:700;color:var(--text-color);font-size:14px;letter-spacing:0.378px;padding:16px;margin:0;position:fixed;z-index:20;width:240px;-webkit-box-sizing:border-box;box-sizing:border-box}:host #menu #title:before{content:"";position:absolute;width:2px;height:10px;background-color:var(--menu-background-color);left:0;height:100%;top:0}:host #menu #main{overflow-y:auto;position:fixed;width:240px;z-index:10}:host #menu .text{color:var(--text-color)}:host #menu #footer{position:fixed;bottom:0;left:0;width:240px;-webkit-box-sizing:border-box;box-sizing:border-box;z-index:20}:host #menu #footer #custom-content{padding:32px 16px 8px 16px;position:relative}:host #menu #footer #custom-content:before{content:"";position:absolute;width:2px;height:10px;background-color:var(--menu-background-color);left:0;height:100%;top:0}:host #menu #footer #collapse-menu{cursor:pointer;height:50px;display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;padding:0 16px;-webkit-transition:var(--menu-background-color) 0.25s;transition:var(--menu-background-color) 0.25s;border-top:1px solid var(--footer-line-color);-webkit-transition:background-color 0.25s;transition:background-color 0.25s;position:relative}:host #menu #footer #collapse-menu:hover{background-color:var(--item-hover-color)}:host #menu #footer #collapse-menu:hover:before{background-color:var(--item-hover-color)}:host #menu #footer #collapse-menu:active{background-color:var(--item-active-color)}:host #menu #footer #collapse-menu:active:before{background-color:var(--item-active-color)}:host #menu #footer #collapse-menu:before{content:"";position:absolute;width:2px;height:10px;background-color:var(--menu-background-color);left:0;height:100%;top:0;-webkit-transition:background-color 0.25s;transition:background-color 0.25s}:host #menu.collapsed #footer #custom-content{display:none}:host #menu.collapsed #footer #collapse-menu{padding:0 10px}:host #menu.collapsed,:host #menu.collapsed #title,:host #menu.collapsed #footer{left:-202px}:host #menu.collapsing,:host #menu.collapsing #title,:host #menu.collapsing #footer{left:-240px !important}:host #menu.collapse-faster,:host #menu.collapse-faster #title,:host #menu.collapse-faster #footer{-webkit-transition:left 0.3s;transition:left 0.3s;-webkit-transition-timing-function:cubic-bezier(0.25, 0.1, 0.25, 1);transition-timing-function:cubic-bezier(0.25, 0.1, 0.25, 1)}:host #menu.collapsed #collapse-menu{-ms-flex-direction:row-reverse;flex-direction:row-reverse}:host #menu.collapsed #collapse-menu ch-icon{-webkit-transform:rotate(180deg);transform:rotate(180deg)}:host #menu,:host #footer{-webkit-transition:left 0.6s;transition:left 0.6s;-webkit-transition-timing-function:cubic-bezier(0.25, 0.1, 0.25, 1);transition-timing-function:cubic-bezier(0.25, 0.1, 0.25, 1)}:host .tooltip{position:fixed;left:50px;opacity:0;-webkit-transition:opacity 0.25s;transition:opacity 0.25s;background-color:var(--menu-background-color);padding:4px 8px;border-radius:6px;z-index:0}:host .tooltip.visible{opacity:1}:host #indicator{width:2px;background-color:var(--indicator-color);position:fixed;left:0;height:0;-webkit-transition:top 0.4s, height 0.5s, opacity 0.35s;transition:top 0.4s, height 0.5s, opacity 0.35s;opacity:1}:host #indicator.speed-zero{-webkit-transition-duration:0s;transition-duration:0s}:host #indicator.hide{opacity:0}:host ::-webkit-scrollbar{width:6px}:host ::-webkit-scrollbar-track{background:var(--scrollbar-track);border-radius:3px}:host ::-webkit-scrollbar-thumb{background:var(--scrollbar-thumb);border-radius:3px}:host ::-webkit-scrollbar-thumb:hover{background:var(--scrollbar-thumb);cursor:pointer}';

export { r as ch_sidebar_menu }