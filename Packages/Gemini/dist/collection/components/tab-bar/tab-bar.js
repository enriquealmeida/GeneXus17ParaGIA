import { Component, h, Host, Element, State, Listen } from "@stencil/core";
export class GxgTabBar {
    constructor() {
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
            return (h("div", { class: "tab-bar__menu" },
                h("gxg-button", { onClick: this.toggleMenu.bind(this), type: "tertiary", icon: "gemini-tools/show-more-vertical" })));
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
            y < tabMenu.bottom) {
            //Click happened inside the menu
        }
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
            } },
            h("nav", { class: {
                    nav: true,
                } },
                h("ul", { class: "tab-bar" },
                    h("slot", { name: "tab-bar" })),
                this.renderTabBarMenu(),
                h("ul", { class: "tab-bar-menu tab-bar-menu--collapsed", style: { "--tabBarMenuHeight": this.tabBarMenuHeight }, ref: (el) => (this.tabBarMenu = el) },
                    h("slot", { name: "tab-menu" })))));
    }
    static get is() { return "gxg-tab-bar"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["tab-bar.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["tab-bar.css"]
    }; }
    static get states() { return {
        "appendedButtons": {},
        "tabBarMenuHeight": {},
        "rtl": {}
    }; }
    static get elementRef() { return "el"; }
    static get listeners() { return [{
            "name": "tabActivated",
            "method": "tabActivatedHandler",
            "target": undefined,
            "capture": false,
            "passive": false
        }]; }
}
