import { r as registerInstance, c as createEvent, e as getAssetPath, h, H as Host, g as getElement } from './index-09b1517f.js';

const chSidebarMenuCss = ":host{--menu-font-family:\"Source Sans Pro\", sans-serif;--menu-background-color:var(--whiteSmoke);--text-color:var(--black);--item-hover-color:var(--lightGray);--item-active-color:var(--silverGray);--first-list-icon-color:var(--black);--first-list-arrow-color:var(--black);--second-list-arrow-color:var(--black);--footer-line-color:var(--black);--indicator-color:var(--black);--scrollbar-track:var(--lightGray);--scrollbar-thumb:var(--darkGray);display:block;}:host #menu{background-color:var(--menu-background-color);font-family:var(--menu-font-family);width:240px;position:fixed;height:100vh;left:0;top:0;z-index:5;-webkit-transition:left 0.6s;transition:left 0.6s;-webkit-transition-timing-function:cubic-bezier(0.25, 0.1, 0.25, 1);transition-timing-function:cubic-bezier(0.25, 0.1, 0.25, 1)}:host #menu #title{text-transform:uppercase;font-weight:700;color:var(--text-color);font-size:14px;letter-spacing:0.378px;padding:16px;margin:0;position:fixed;z-index:20;width:240px;-webkit-box-sizing:border-box;box-sizing:border-box}:host #menu #title:before{content:\"\";position:absolute;width:2px;height:10px;background-color:var(--menu-background-color);left:0;height:100%;top:0}:host #menu #main{overflow-y:auto;position:fixed;width:240px;z-index:10}:host #menu .text{color:var(--text-color)}:host #menu #footer{position:fixed;bottom:0;left:0;width:240px;-webkit-box-sizing:border-box;box-sizing:border-box;z-index:20}:host #menu #footer #custom-content{padding:32px 16px 8px 16px;position:relative}:host #menu #footer #custom-content:before{content:\"\";position:absolute;width:2px;height:10px;background-color:var(--menu-background-color);left:0;height:100%;top:0}:host #menu #footer #collapse-menu{cursor:pointer;height:50px;display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;padding:0 16px;-webkit-transition:var(--menu-background-color) 0.25s;transition:var(--menu-background-color) 0.25s;border-top:1px solid var(--footer-line-color);-webkit-transition:background-color 0.25s;transition:background-color 0.25s;position:relative}:host #menu #footer #collapse-menu:hover{background-color:var(--item-hover-color)}:host #menu #footer #collapse-menu:hover:before{background-color:var(--item-hover-color)}:host #menu #footer #collapse-menu:active{background-color:var(--item-active-color)}:host #menu #footer #collapse-menu:active:before{background-color:var(--item-active-color)}:host #menu #footer #collapse-menu:before{content:\"\";position:absolute;width:2px;height:10px;background-color:var(--menu-background-color);left:0;height:100%;top:0;-webkit-transition:background-color 0.25s;transition:background-color 0.25s}:host #menu.collapsed #footer #custom-content{display:none}:host #menu.collapsed #footer #collapse-menu{padding:0 10px}:host #menu.collapsed,:host #menu.collapsed #title,:host #menu.collapsed #footer{left:-202px}:host #menu.collapsing,:host #menu.collapsing #title,:host #menu.collapsing #footer{left:-240px !important}:host #menu.collapse-faster,:host #menu.collapse-faster #title,:host #menu.collapse-faster #footer{-webkit-transition:left 0.3s;transition:left 0.3s;-webkit-transition-timing-function:cubic-bezier(0.25, 0.1, 0.25, 1);transition-timing-function:cubic-bezier(0.25, 0.1, 0.25, 1)}:host #menu.collapsed #collapse-menu{-ms-flex-direction:row-reverse;flex-direction:row-reverse}:host #menu.collapsed #collapse-menu ch-icon{-webkit-transform:rotate(180deg);transform:rotate(180deg)}:host #menu,:host #footer{-webkit-transition:left 0.6s;transition:left 0.6s;-webkit-transition-timing-function:cubic-bezier(0.25, 0.1, 0.25, 1);transition-timing-function:cubic-bezier(0.25, 0.1, 0.25, 1)}:host .tooltip{position:fixed;left:50px;opacity:0;-webkit-transition:opacity 0.25s;transition:opacity 0.25s;background-color:var(--menu-background-color);padding:4px 8px;border-radius:6px;z-index:0}:host .tooltip.visible{opacity:1}:host #indicator{width:2px;background-color:var(--indicator-color);position:fixed;left:0;height:0;-webkit-transition:top 0.4s, height 0.5s, opacity 0.35s;transition:top 0.4s, height 0.5s, opacity 0.35s;opacity:1}:host #indicator.speed-zero{-webkit-transition-duration:0s;transition-duration:0s}:host #indicator.hide{opacity:0}:host ::-webkit-scrollbar{width:6px}:host ::-webkit-scrollbar-track{background:var(--scrollbar-track);border-radius:3px}:host ::-webkit-scrollbar-thumb{background:var(--scrollbar-thumb);border-radius:3px}:host ::-webkit-scrollbar-thumb:hover{background:var(--scrollbar-thumb);cursor:pointer}";

const ChSidebarMenu = class {
    constructor(hostRef) {
        registerInstance(this, hostRef);
        this.itemClicked = createEvent(this, "itemClicked", 7);
        this.iconArrowLeft = getAssetPath(`./sidebar-menu-assets/arrow-left.svg`);
        this.topHeightSpeed = 300;
        this.speedDivisionValue = 400;
        /**
         * The presence of this attribute allows the menu to have only one list opened at the same time
         */
        this.singleListOpen = false;
    }
    componentWillLoad() { }
    componentDidLoad() {
        const titleHeight = this.title.offsetHeight;
        const footerHeight = this.footer.offsetHeight;
        const titleAndFooterHeight = titleHeight + footerHeight + "px";
        const collapsableItems = this.el.querySelectorAll(".collapsable");
        // const items = document.getElementsByClassName("item");
        /*Set main height*/
        this.main.style.height = `calc(100vh - ${titleAndFooterHeight})`;
        /*Set main top position*/
        this.main.style.top = titleHeight + "px";
        //SET INITAL ITEMS MAX HEIGHT
        const items = this.el.querySelectorAll(".item");
        Array.from(items).forEach(function (item) {
            const mainContainer = item.shadowRoot.querySelector(".main-container");
            item.style.maxHeight =
                mainContainer.offsetHeight + "px";
        });
        /**************
        SET ACTIVE ITEM
        ***************/
        Array.from(items).forEach(function (item) {
            item.addEventListener("click", function () {
                if (!this.menu.classList.contains("collapsed")) {
                    //remove current active item class
                    const currentActiveItem = document.querySelector(".item--active");
                    if (currentActiveItem !== null) {
                        currentActiveItem.classList.remove("item--active");
                    }
                    //set current item as active
                    item.classList.add("item--active");
                    //Emmit event from the selected item
                }
            }.bind(this));
        }.bind(this));
        /****************************
        COLLAPSABLE LIST ITEMS LOGIC
        ****************************/
        Array.from(collapsableItems).forEach(function (item) {
            const mainContainer = item.shadowRoot.querySelector(".main-container");
            mainContainer.addEventListener("click", function () {
                if (item.classList.contains("list-one__item") &&
                    this.menu.classList.contains("collapsed")) {
                    //If item clicked is type 1, and menu is collapsed, just uncollapse the menu.
                    this.collapseButton.click();
                }
                else {
                    this.toggleIcon(item); //This function has to be before  uncollapseList or collapseList.
                    this.setTransitionSpeed(item);
                    if (item.classList.contains("uncollapsed")) {
                        this.uncollapseList(item);
                    }
                    else {
                        this.collapseList(item);
                    }
                    //If this item is type 2, then update list 1 transition speed and maxheight
                    if (item.classList.contains("list-two__item")) {
                        const parentItem1 = item.closest(".list-one__item");
                        let heightToTransition = item.querySelector(".list-three")
                            .offsetHeight;
                        this.updateListItem1TransitionSpeed(parentItem1, heightToTransition);
                        //Update list 1 max. height
                        this.updateListItem1MaxHeight(parentItem1);
                    }
                }
            }.bind(this));
        }.bind(this));
        /*******************************
        SINGLE UL1 OPEN AT A TIME LOGIC
        *******************************/
        //Only one list of type 1 can be open at the same time.
        //This is an optional parameter. Applies if 'data-single-ul1-open' attribute is present on #menu
        if (this.singleListOpen) {
            const collapsableListOneItems = document.querySelectorAll(".list-one__item");
            Array.from(collapsableListOneItems).forEach(function (item) {
                const mainContainer = item.shadowRoot.querySelector(".main-container");
                mainContainer.addEventListener("click", function () {
                    if (!this.menu.classList.contains("collapsed")) {
                        const lastUl1Opened = document.querySelector(".lastUl1Opened");
                        if (lastUl1Opened !== null &&
                            !item.classList.contains("lastUl1Opened")) {
                            const lastUl1OpenedMainContainer = lastUl1Opened.shadowRoot.querySelector(".main-container");
                            lastUl1OpenedMainContainer.click();
                        }
                        if (item.classList.contains("uncollapsed")) {
                            item.classList.add("lastUl1Opened");
                        }
                        else {
                            item.classList.remove("lastUl1Opened");
                        }
                    }
                }.bind(this));
            }.bind(this));
        }
        /*******************
        COLLAPSE MENU LOGIC
        *******************/
        this.collapseButton.addEventListener("click", function () {
            let setTimeOutDelay = 0;
            if (this.menu.classList.contains("collapsed")) {
                //if menu is collapsed, the animation that shows the menu should be quicker
                this.menu.classList.add("collapse-faster");
                setTimeOutDelay = 300; //This value should be the same as the #menu.collapse-faster transition speed value.
            }
            else {
                this.menu.classList.remove("collapse-faster");
                setTimeOutDelay = 600; //This value should be the same as the #menu without .collapse-faster transition speed value.
            }
            this.menu.classList.add("collapsing");
            this.hideIndicator();
            setTimeout(function () {
                if (this.menu.classList.contains("collapsed")) {
                    this.uncollapseCollapsedLists();
                    this.undoSwitchListOneOrder();
                    this.menu.classList.remove("collapsed");
                    setTimeout(function () {
                        this.repositionIndicatorAfterMenuUncollapse();
                    }.bind(this), 50);
                }
                else {
                    this.collapseUncollapsedLists1();
                    this.switchListOneOrder();
                    this.menu.classList.add("collapsed");
                    setTimeout(function () {
                        this.repositionIndicatorAfterMenuCollapse();
                    }.bind(this), 50);
                }
                this.menu.classList.remove("collapsing");
                setTimeout(function () {
                    this.showIndicator();
                }.bind(this), 400);
            }.bind(this), setTimeOutDelay);
        }.bind(this));
        /******************
        ITEMS TOOLTIP LOGIC
        *******************/
        var itemTooltip = document.createElement("DIV");
        itemTooltip.classList.add("tooltip");
        itemTooltip.style.zIndex = "0";
        this.menu.appendChild(itemTooltip);
        Array.from(items).forEach(function (item) {
            const mainContainer = item.shadowRoot.querySelector(".main-container");
            mainContainer.addEventListener("mouseenter", function () {
                if (this.menu.classList.contains("collapsed")) {
                    itemTooltip.classList.add("visible");
                    itemTooltip.innerHTML = item.childNodes[0].nodeValue;
                    const itemTopPosition = item.getBoundingClientRect().y;
                    itemTooltip.style.top = itemTopPosition + "px";
                }
            }.bind(this));
            mainContainer.addEventListener("mouseleave", function () {
                if (this.menu.classList.contains("collapsed")) {
                    itemTooltip.classList.remove("visible");
                }
            }.bind(this));
        }.bind(this));
        /**********************************
        LATERAL ACTIVE ITEM INDICATOR LOGIC
        ***********************************/
        this.indicator = document.createElement("DIV");
        this.indicator.setAttribute("id", "indicator");
        this.main.appendChild(this.indicator);
        Array.from(items).forEach(function (item) {
            item.addEventListener("click", function (e) {
                e.stopPropagation();
                if (!this.menu.classList.contains("collapsed")) {
                    const itemTopPosition = item.getBoundingClientRect().y;
                    const itemHeight = item.shadowRoot.querySelector(".main-container").offsetHeight;
                    if (this.singleListOpen &&
                        item.classList.contains("list-one__item")) {
                        let itemCopy = item;
                        let totalHeight = titleHeight;
                        while ((itemCopy = itemCopy.previousElementSibling) != null) {
                            const itemCopyMainContainer = itemCopy.shadowRoot.querySelector(".main-container");
                            totalHeight += itemCopyMainContainer.offsetHeight;
                        }
                        this.indicator.style.top = totalHeight + "px";
                        this.indicator.style.height = itemHeight + "px";
                    }
                    else {
                        this.indicator.style.top = itemTopPosition + "px";
                        this.indicator.style.height = itemHeight + "px";
                    }
                }
            }.bind(this));
        }.bind(this));
        //REPOSITION INDICATOR ON SCROLL
        var timer = null;
        this.main.addEventListener("scroll", function () {
            //get reference to current active item
            const currentActiveItem = document.querySelector(".item--active");
            if (currentActiveItem !== null) {
                let currentActiveItemTopPosition = currentActiveItem.getBoundingClientRect()
                    .y;
                this.indicator.classList.add("speed-zero");
                this.indicator.style.top = currentActiveItemTopPosition + "px";
                //detect when scrolling has stopped
                if (timer !== null) {
                    clearTimeout(timer);
                }
                timer = setTimeout(function () {
                    this.indicator.classList.remove("speed-zero");
                }.bind(this), 50);
            }
        }.bind(this));
    } // End of ComponentDidLoad
    //REPOSITION INDICATOR AFTER MENU COLLAPSE
    repositionIndicatorAfterMenuCollapse() {
        const activeItem = this.el.querySelector(".item--active");
        if (activeItem !== null) {
            const closestL1 = activeItem.closest(".list-one__item");
            if (closestL1 !== null) {
                const closestL1MainContainer = closestL1.shadowRoot.querySelector(".main-container");
                const topPosition = closestL1MainContainer.getBoundingClientRect().y;
                const height = closestL1MainContainer.offsetHeight;
                this.indicator.style.top = topPosition + "px";
                this.indicator.style.height = height + "px";
            }
            else {
                //else, the active item has no parent l ist. just reposition indicator
                const topPosition = activeItem.getBoundingClientRect().y;
                const height = activeItem.offsetHeight;
                this.indicator.style.top = topPosition + "px";
                this.indicator.style.height = height + "px";
            }
        }
    }
    //REPOSITION INDICATOR AFTER MENU UNCOLLAPSE
    repositionIndicatorAfterMenuUncollapse() {
        const activeItem = this.el.querySelector(".item--active");
        const activeItemMainContainer = activeItem.shadowRoot.querySelector(".main-container");
        const activeItemMainContainerTopPosition = activeItemMainContainer.getBoundingClientRect()
            .y;
        const activeItemMainContainerHeight = activeItemMainContainer
            .offsetHeight;
        this.indicator.style.top = activeItemMainContainerTopPosition + "px";
        this.indicator.style.height = activeItemMainContainerHeight + "px";
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
        let uncollapsedLists1Items = document.querySelectorAll(".list-one__item.uncollapsed");
        Array.from(uncollapsedLists1Items).forEach(function (item) {
            item.classList.add("speed-zero");
            item.setAttribute("data-uncollapsed-max-height", item.style.maxHeight);
            item.style.maxHeight =
                item.shadowRoot.querySelector(".main-container")
                    .offsetHeight + "px";
        });
    }
    uncollapseCollapsedLists() {
        let uncollapsedLists1Items = document.querySelectorAll(".list-one__item.uncollapsed");
        Array.from(uncollapsedLists1Items).forEach(function (item) {
            item.addEventListener("transitionend", removeSpeedZero);
            function removeSpeedZero(e) {
                if (e.propertyName === "max-height") {
                    item.classList.remove("speed-zero");
                    item.removeEventListener("transitionend", removeSpeedZero);
                }
            }
            const uncollapsedMaxHeight = item.getAttribute("data-uncollapsed-max-height");
            item.style.maxHeight = uncollapsedMaxHeight;
            item.removeAttribute("data-uncollapsed-max-height");
        });
    }
    switchListOneOrder() {
        let listOneItems = document.querySelectorAll(".list-one__item");
        Array.from(listOneItems).forEach(function (item) {
            item.classList.add("switch-order");
        });
    }
    undoSwitchListOneOrder() {
        let listOneItems = document.querySelectorAll(".list-one__item");
        Array.from(listOneItems).forEach(function (item) {
            item.classList.remove("switch-order");
        });
    }
    /*UPDATE LIST ITEM 1 TRANSITION SPEED*/
    updateListItem1TransitionSpeed(item, height) {
        if (height > this.topHeightSpeed) {
            height = this.topHeightSpeed;
        }
        item.style.transitionDuration = height / this.speedDivisionValue + "s";
    }
    /*UPDATE LIST ITEM 1 MAX HEIGHT*/
    updateListItem1MaxHeight(item) {
        const mainContainerHeight = item.shadowRoot.querySelector(".main-container")
            .clientHeight;
        const list2Items = item.querySelectorAll(":scope > ch-sidebar-menu-list > ch-sidebar-menu-list-item");
        let totalMaxHeight = mainContainerHeight;
        Array.from(list2Items).forEach(function (item) {
            totalMaxHeight += parseInt(item.style.maxHeight.slice(0, -2));
        });
        item.style.maxHeight = totalMaxHeight + "px";
    }
    /*TOGGLE ITEM ICON*/
    toggleIcon(item) {
        if (item.classList.contains("uncollapsed")) {
            item.classList.remove("uncollapsed");
        }
        else {
            item.classList.add("uncollapsed");
        }
    }
    /*SET ITEM TRANSITION SPEED*/
    setTransitionSpeed(item) {
        let transitionSpeed = 0;
        const childListHeight = item.querySelector("ch-sidebar-menu-list")
            .clientHeight;
        if (childListHeight > this.topHeightSpeed) {
            transitionSpeed = this.topHeightSpeed;
        }
        else {
            transitionSpeed = childListHeight;
        }
        item.style.transitionDuration =
            transitionSpeed / this.speedDivisionValue + "s";
    }
    /*COLLAPSE LIST*/
    collapseList(item) {
        const mainContainerHeight = item.shadowRoot.querySelector(".main-container")
            .offsetHeight;
        item.style.maxHeight = mainContainerHeight + "px";
    }
    /*UNCOLLAPSE LIST*/
    uncollapseList(item) {
        const mainContainerHeight = item.shadowRoot.querySelector(".main-container")
            .clientHeight;
        const childListHeight = item.querySelector("ch-sidebar-menu-list")
            .clientHeight;
        item.style.maxHeight = mainContainerHeight + childListHeight + "px";
    }
    render() {
        return (h(Host, null, h("nav", { id: "menu", ref: (el) => (this.menu = el) }, h("h1", { id: "title", ref: (el) => (this.title = el) }, this.menuTitle), h("main", { id: "main", ref: (el) => (this.main = el) }, h("slot", null)), h("footer", { id: "footer", ref: (el) => (this.footer = el) }, h("div", { id: "custom-content" }, h("slot", { name: "footer" })), h("div", { id: "collapse-menu", ref: (el) => (this.collapseButton = el) }, h("ch-icon", { style: {
                "--icon-color": "var(--first-list-arrow-color)",
                "--icon-size": "20px",
            }, src: this.iconArrowLeft }))))));
    }
    static get assetsDirs() { return ["sidebar-menu-assets"]; }
    get el() { return getElement(this); }
};
ChSidebarMenu.style = chSidebarMenuCss;

export { ChSidebarMenu as ch_sidebar_menu };
