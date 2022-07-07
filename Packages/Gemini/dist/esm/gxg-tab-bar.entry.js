import { r as registerInstance, h, H as Host, g as getElement } from './index-09b1517f.js';

const tabBarCss = ":root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{--tabBarMenuHeight:0;display:-ms-inline-flexbox;display:inline-flex;text-align:right;position:relative;display:-ms-flexbox;display:flex;-ms-flex-pack:justify;justify-content:space-between}:host .nav{display:-ms-flexbox;display:flex;-ms-flex-pack:justify;justify-content:space-between;width:100%}:host .tab-bar{list-style-type:none;padding-left:0;display:-ms-flexbox !important;display:flex !important;margin-bottom:0;margin-top:0;font-family:var(--font-family-primary);background-color:var(--color-background)}:host .tab-bar__menu{display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center}:host .tab-bar-menu{border-radius:var(--border-width-md);-webkit-box-shadow:var(--box-shadow-01);box-shadow:var(--box-shadow-01);height:var(--tabBarMenuHeight);position:absolute;z-index:10;background-color:var(--color-on-primary);right:0;margin:0;padding:0;list-style:none;display:-ms-inline-flexbox;display:inline-flex;-ms-flex-direction:column;flex-direction:column;overflow:hidden;-webkit-transition-property:height;transition-property:height;-webkit-transition-duration:var(--ui-animaton-speed);transition-duration:var(--ui-animaton-speed);-webkit-transition-timing-function:ease;transition-timing-function:ease}:host .tab-bar-menu--collapsed{height:0}:host .tab-bar-menu.bottom{bottom:0}:host .tab-bar-menu.right{bottom:0}:host(.rtl) .tab-bar-menu{left:0;right:auto}";

const GxgTabBar = class {
    constructor(hostRef) {
        registerInstance(this, hostRef);
        this.appendedButtons = 0;
        this.tabBarMenuHeight = "100px";
        /**
         * Reading direction
         */
        this.rtl = false;
        this.detectClickOutsideTabBarMenu = this.detectClickOutsideTabBarMenu.bind(this);
    }
    toggleMenu(event) {
        event.stopPropagation();
        this.tabBarMenu.classList.toggle("tab-bar-menu--collapsed");
        document.addEventListener("click", this.detectClickOutsideTabBarMenu);
    }
    tabActivatedHandler() {
        const tabMenuContainer = this.el.shadowRoot.querySelector(".tab-bar-menu");
        tabMenuContainer.classList.add("tab-bar-menu--collapsed");
    }
    appendTabItemsToMenu() {
        //This function appends tab-buttons into a tab-menu, as long as the tab-buttons are too tight
        const gxgTabsPosition = this.el.parentElement.getAttribute("position");
        const buttonHeight = this.el.children.item(0).clientHeight;
        if (gxgTabsPosition === "top" || gxgTabsPosition === "bottom") {
            const tabBarWidth = this.el.shadowRoot.querySelector(".tab-bar").offsetWidth;
            const tabsWidth = this.el.parentElement.offsetWidth;
            if (tabBarWidth + 20 > tabsWidth) {
                const tabButtons = this.el.querySelectorAll("[slot=tab-bar]");
                //get the last item of the nodeList
                const lastTabButton = tabButtons[tabButtons.length - 1];
                //add "menu-button" class to button component, in order to stylize the buttons inside the menu differently
                lastTabButton.classList.add("menu-button");
                lastTabButton.setAttribute("slot", "tab-menu");
                this.appendedButtons++;
                //}
            }
            else {
                const menuButtons = this.el.querySelectorAll("[slot=tab-menu]");
                const menuFirstButton = menuButtons[0];
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
            const gxgTabsHeight = this.el.parentElement.offsetHeight;
            const tabBarHeight = this.el.offsetWidth;
            if (tabBarHeight > gxgTabsHeight) {
                //tabBarHeight is higher than gxgTabsHeight
                const tabButtons = this.el.querySelectorAll("[slot=tab-bar]");
                //get the last item of the nodeList
                const lastTabButton = tabButtons[tabButtons.length - 1];
                //add "menu-button" class to button component, in order to stylize the buttons inside the menu differently
                lastTabButton.classList.add("menu-button");
                lastTabButton.setAttribute("slot", "tab-menu");
                this.appendedButtons++;
            }
            else {
                //tabBarHeight is lower than gxgTabsHeight
                const menuButtons = this.el.querySelectorAll("[slot=tab-menu]");
                const menuFirstButton = menuButtons[0];
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
    }
    componentDidLoad() {
        //Reading Direction
        const dirHtml = document
            .getElementsByTagName("html")[0]
            .getAttribute("dir");
        const dirBody = document
            .getElementsByTagName("body")[0]
            .getAttribute("dir");
        if (dirHtml === "rtl" || dirBody === "rtl") {
            this.rtl = true;
        }
        requestAnimationFrame(() => this.appendTabItemsToMenu());
        const myObserver = new ResizeObserver((entries) => {
            entries.forEach(() => {
                //get any button space between text and button border
                this.appendTabItemsToMenu();
            });
        });
        const tabs = this.el.parentElement;
        myObserver.observe(tabs);
        //Collapse buttons if they dont't fit in the available space already
        setTimeout(function () {
            const numberOfButtons = this.el.querySelectorAll("gxg-tab-button")
                .length;
            for (let i = 0; i <= numberOfButtons; i++) {
                this.appendTabItemsToMenu();
            }
        }.bind(this), 100);
        //Tabbar menu on bottom
        const gxgTabsPosition = this.el.parentElement.getAttribute("position");
        if (gxgTabsPosition === "bottom") {
            const tabBarMenu = this.el.shadowRoot.querySelector(".tab-bar-menu");
            console.log("tabBarMenu", tabBarMenu);
            tabBarMenu.classList.add("bottom");
        }
        //Tabbar menu on right
        if (gxgTabsPosition === "right") {
            const tabBarMenu = this.el.shadowRoot.querySelector(".tab-bar-menu");
            tabBarMenu.classList.add("right");
        }
        this.setIndexToTabButtons();
    }
    setIndexToTabButtons() {
        const tabButtons = this.el.querySelectorAll("gxg-tab-button");
        tabButtons.forEach((tabButton, index) => {
            tabButton.setAttribute("data-index", index.toString());
        });
    }
    renderTabBarMenu() {
        if (this.appendedButtons > 0) {
            return (h("div", { class: "tab-bar__menu" }, h("gxg-button", { onClick: this.toggleMenu.bind(this), type: "tertiary", icon: "gemini-tools/show-more-vertical" })));
        }
    }
    detectClickOutsideTabBarMenu(event) {
        const tabMenuContainer = this.el.shadowRoot.querySelector(".tab-bar-menu");
        const x = event.x;
        const y = event.y;
        //card main container coordinates
        const tabMenu = tabMenuContainer.getBoundingClientRect();
        if (x > tabMenu.left &&
            x < tabMenu.right &&
            y > tabMenu.top &&
            y < tabMenu.bottom) ;
        else {
            //Click happened outside the menu
            tabMenuContainer.classList.add("tab-bar-menu--collapsed");
        }
    }
    componentDidUnload() {
        document.removeEventListener("click", this.detectClickOutsideTabBarMenu);
    }
    render() {
        return (h(Host, { class: {
                rtl: this.rtl,
            } }, h("nav", { class: {
                nav: true,
            } }, h("ul", { class: "tab-bar" }, h("slot", { name: "tab-bar" })), this.renderTabBarMenu(), h("ul", { class: "tab-bar-menu tab-bar-menu--collapsed", style: { "--tabBarMenuHeight": this.tabBarMenuHeight }, ref: (el) => (this.tabBarMenu = el) }, h("slot", { name: "tab-menu" })))));
    }
    get el() { return getElement(this); }
};
GxgTabBar.style = tabBarCss;

export { GxgTabBar as gxg_tab_bar };
