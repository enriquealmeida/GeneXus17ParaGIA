import { r as registerInstance, h, H as Host, g as getElement } from './index-09b1517f.js';
var tabBarCss = ":root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{--tabBarMenuHeight:0;display:-ms-inline-flexbox;display:inline-flex;text-align:right;position:relative;display:-ms-flexbox;display:flex;-ms-flex-pack:justify;justify-content:space-between}:host .nav{display:-ms-flexbox;display:flex;-ms-flex-pack:justify;justify-content:space-between;width:100%}:host .tab-bar{list-style-type:none;padding-left:0;display:-ms-flexbox !important;display:flex !important;margin-bottom:0;margin-top:0;font-family:var(--font-family-primary);background-color:var(--color-background)}:host .tab-bar__menu{display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center}:host .tab-bar-menu{border-radius:var(--border-width-md);-webkit-box-shadow:var(--box-shadow-01);box-shadow:var(--box-shadow-01);height:var(--tabBarMenuHeight);position:absolute;z-index:10;background-color:var(--color-on-primary);right:0;margin:0;padding:0;list-style:none;display:-ms-inline-flexbox;display:inline-flex;-ms-flex-direction:column;flex-direction:column;overflow:hidden;-webkit-transition-property:height;transition-property:height;-webkit-transition-duration:var(--ui-animaton-speed);transition-duration:var(--ui-animaton-speed);-webkit-transition-timing-function:ease;transition-timing-function:ease}:host .tab-bar-menu--collapsed{height:0}:host .tab-bar-menu.bottom{bottom:0}:host .tab-bar-menu.right{bottom:0}:host(.rtl) .tab-bar-menu{left:0;right:auto}";
var GxgTabBar = /** @class */ (function () {
    function GxgTabBar(hostRef) {
        registerInstance(this, hostRef);
        this.appendedButtons = 0;
        this.tabBarMenuHeight = "100px";
        /**
         * Reading direction
         */
        this.rtl = false;
        this.detectClickOutsideTabBarMenu = this.detectClickOutsideTabBarMenu.bind(this);
    }
    GxgTabBar.prototype.toggleMenu = function (event) {
        event.stopPropagation();
        this.tabBarMenu.classList.toggle("tab-bar-menu--collapsed");
        document.addEventListener("click", this.detectClickOutsideTabBarMenu);
    };
    GxgTabBar.prototype.tabActivatedHandler = function () {
        var tabMenuContainer = this.el.shadowRoot.querySelector(".tab-bar-menu");
        tabMenuContainer.classList.add("tab-bar-menu--collapsed");
    };
    GxgTabBar.prototype.appendTabItemsToMenu = function () {
        //This function appends tab-buttons into a tab-menu, as long as the tab-buttons are too tight
        var gxgTabsPosition = this.el.parentElement.getAttribute("position");
        var buttonHeight = this.el.children.item(0).clientHeight;
        if (gxgTabsPosition === "top" || gxgTabsPosition === "bottom") {
            var tabBarWidth = this.el.shadowRoot.querySelector(".tab-bar").offsetWidth;
            var tabsWidth = this.el.parentElement.offsetWidth;
            if (tabBarWidth + 20 > tabsWidth) {
                var tabButtons = this.el.querySelectorAll("[slot=tab-bar]");
                //get the last item of the nodeList
                var lastTabButton = tabButtons[tabButtons.length - 1];
                //add "menu-button" class to button component, in order to stylize the buttons inside the menu differently
                lastTabButton.classList.add("menu-button");
                lastTabButton.setAttribute("slot", "tab-menu");
                this.appendedButtons++;
                //}
            }
            else {
                var menuButtons = this.el.querySelectorAll("[slot=tab-menu]");
                var menuFirstButton = menuButtons[0];
                if (menuFirstButton !== undefined) {
                    if (tabsWidth >
                        tabBarWidth +
                            menuFirstButton.offsetWidth) {
                        menuFirstButton.classList.remove("menu-button");
                        menuFirstButton.setAttribute("slot", "tab-bar");
                        this.appendedButtons--;
                    }
                }
            }
        }
        else if (gxgTabsPosition === "left" || gxgTabsPosition === "right") {
            var gxgTabsHeight = this.el.parentElement.offsetHeight;
            var tabBarHeight = this.el.offsetWidth;
            if (tabBarHeight > gxgTabsHeight) {
                //tabBarHeight is higher than gxgTabsHeight
                var tabButtons = this.el.querySelectorAll("[slot=tab-bar]");
                //get the last item of the nodeList
                var lastTabButton = tabButtons[tabButtons.length - 1];
                //add "menu-button" class to button component, in order to stylize the buttons inside the menu differently
                lastTabButton.classList.add("menu-button");
                lastTabButton.setAttribute("slot", "tab-menu");
                this.appendedButtons++;
            }
            else {
                //tabBarHeight is lower than gxgTabsHeight
                var menuButtons = this.el.querySelectorAll("[slot=tab-menu]");
                var menuFirstButton = menuButtons[0];
                if (menuFirstButton !== undefined) {
                    if (gxgTabsHeight >
                        tabBarHeight +
                            menuFirstButton.offsetWidth) {
                        menuFirstButton.classList.remove("menu-button");
                        menuFirstButton.setAttribute("slot", "tab-bar");
                        this.appendedButtons--;
                    }
                }
            }
        }
        this.tabBarMenuHeight = this.appendedButtons * buttonHeight + "px";
    };
    GxgTabBar.prototype.componentDidLoad = function () {
        var _this = this;
        //Reading Direction
        var dirHtml = document
            .getElementsByTagName("html")[0]
            .getAttribute("dir");
        var dirBody = document
            .getElementsByTagName("body")[0]
            .getAttribute("dir");
        if (dirHtml === "rtl" || dirBody === "rtl") {
            this.rtl = true;
        }
        requestAnimationFrame(function () { return _this.appendTabItemsToMenu(); });
        var myObserver = new ResizeObserver(function (entries) {
            entries.forEach(function () {
                //get any button space between text and button border
                _this.appendTabItemsToMenu();
            });
        });
        var tabs = this.el.parentElement;
        myObserver.observe(tabs);
        //Collapse buttons if they dont't fit in the available space already
        setTimeout(function () {
            var numberOfButtons = this.el.querySelectorAll("gxg-tab-button")
                .length;
            for (var i = 0; i <= numberOfButtons; i++) {
                this.appendTabItemsToMenu();
            }
        }.bind(this), 100);
        //Tabbar menu on bottom
        var gxgTabsPosition = this.el.parentElement.getAttribute("position");
        if (gxgTabsPosition === "bottom") {
            var tabBarMenu = this.el.shadowRoot.querySelector(".tab-bar-menu");
            console.log("tabBarMenu", tabBarMenu);
            tabBarMenu.classList.add("bottom");
        }
        //Tabbar menu on right
        if (gxgTabsPosition === "right") {
            var tabBarMenu = this.el.shadowRoot.querySelector(".tab-bar-menu");
            tabBarMenu.classList.add("right");
        }
        this.setIndexToTabButtons();
    };
    GxgTabBar.prototype.setIndexToTabButtons = function () {
        var tabButtons = this.el.querySelectorAll("gxg-tab-button");
        tabButtons.forEach(function (tabButton, index) {
            tabButton.setAttribute("data-index", index.toString());
        });
    };
    GxgTabBar.prototype.renderTabBarMenu = function () {
        if (this.appendedButtons > 0) {
            return (h("div", { class: "tab-bar__menu" }, h("gxg-button", { onClick: this.toggleMenu.bind(this), type: "tertiary", icon: "gemini-tools/show-more-vertical" })));
        }
    };
    GxgTabBar.prototype.detectClickOutsideTabBarMenu = function (event) {
        var tabMenuContainer = this.el.shadowRoot.querySelector(".tab-bar-menu");
        var x = event.x;
        var y = event.y;
        //card main container coordinates
        var tabMenu = tabMenuContainer.getBoundingClientRect();
        if (x > tabMenu.left &&
            x < tabMenu.right &&
            y > tabMenu.top &&
            y < tabMenu.bottom)
            ;
        else {
            //Click happened outside the menu
            tabMenuContainer.classList.add("tab-bar-menu--collapsed");
        }
    };
    GxgTabBar.prototype.componentDidUnload = function () {
        document.removeEventListener("click", this.detectClickOutsideTabBarMenu);
    };
    GxgTabBar.prototype.render = function () {
        var _this = this;
        return (h(Host, { class: {
                rtl: this.rtl,
            } }, h("nav", { class: {
                nav: true,
            } }, h("ul", { class: "tab-bar" }, h("slot", { name: "tab-bar" })), this.renderTabBarMenu(), h("ul", { class: "tab-bar-menu tab-bar-menu--collapsed", style: { "--tabBarMenuHeight": this.tabBarMenuHeight }, ref: function (el) { return (_this.tabBarMenu = el); } }, h("slot", { name: "tab-menu" })))));
    };
    Object.defineProperty(GxgTabBar.prototype, "el", {
        get: function () { return getElement(this); },
        enumerable: false,
        configurable: true
    });
    return GxgTabBar;
}());
GxgTabBar.style = tabBarCss;
export { GxgTabBar as gxg_tab_bar };
