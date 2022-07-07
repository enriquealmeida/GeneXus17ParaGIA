import { Component, Prop, h, Host, Element, Watch, State } from "@stencil/core";
export class GxgContextualMenu {
    constructor() {
        /**
         * The presence of this attribute makes the menu visible
         */
        this.visible = false;
        this.firstRightClick = true;
        this.contextualMenuSizes = {
            width: 0,
            height: 0
        };
        this.detectClickOutsideMenu = this.detectClickOutsideMenu.bind(this);
    }
    watchHandler(newValue) {
        if (newValue) {
            //Get contextualMenu height and width
            const contextualMenuWidth = (this.contextualMenuSizes.width = this.el.offsetWidth);
            const contextualMenuHeight = (this.contextualMenuSizes.height = this.el.offsetHeight);
            //Get available height and width from the mouse pointer to the right and bottom side of the document body, respectively.
            const availableWidth = window.innerWidth - this.leftPosition;
            const availableHeight = window.innerHeight - this.topPosition;
            this.widthOverflow = availableWidth < contextualMenuWidth ? true : false;
            this.heightOverflow =
                availableHeight < contextualMenuHeight ? true : false;
        }
    }
    detectClickOutsideMenu(event) {
        //Get mouse coordinates
        this.topPosition = event.clientY;
        this.leftPosition = event.clientX;
        const contextualMenu = this.el.shadowRoot.querySelector(".contextual-menu-list");
        const x = event.x;
        const y = event.y;
        //Contextual menu coordinates
        const contextualMenuArea = contextualMenu.getBoundingClientRect();
        if (x > contextualMenuArea.left &&
            x < contextualMenuArea.right &&
            y > contextualMenuArea.top &&
            y < contextualMenuArea.bottom) {
            //Click happened inside the menu
        }
        else {
            //Click happened outside the menu
            if (!this.firstRightClick) {
                this.visible = false;
                this.firstRightClick = true;
            }
            else if (this.visible) {
                this.firstRightClick = false;
            }
        }
    }
    saveMouseCoordinates(e) {
        this.topPosition = e.clientX;
        this.leftPosition = e.clientY;
    }
    componentDidLoad() {
        document.addEventListener("click", this.detectClickOutsideMenu);
        document.addEventListener("contextmenu", this.detectClickOutsideMenu);
    }
    componentDidUnload() {
        document.removeEventListener("click", this.detectClickOutsideMenu);
        document.removeEventListener("contextmenu", this.detectClickOutsideMenu);
    }
    render() {
        return (h(Host, { visible: this.visible, class: {
                "width-overflow": this.widthOverflow,
                "height-overflow": this.heightOverflow
            }, style: {
                top: `${this.topPosition}px`,
                left: `${this.leftPosition}px`
            } },
            h("ul", { class: "contextual-menu-list" },
                h("slot", null))));
    }
    static get is() { return "gxg-contextual-menu"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["contextual-menu.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["contextual-menu.css"]
    }; }
    static get properties() { return {
        "visible": {
            "type": "boolean",
            "mutable": false,
            "complexType": {
                "original": "boolean",
                "resolved": "boolean",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The presence of this attribute makes the menu visible"
            },
            "attribute": "visible",
            "reflect": true,
            "defaultValue": "false"
        }
    }; }
    static get states() { return {
        "widthOverflow": {},
        "heightOverflow": {},
        "firstRightClick": {},
        "topPosition": {},
        "leftPosition": {}
    }; }
    static get elementRef() { return "el"; }
    static get watchers() { return [{
            "propName": "visible",
            "methodName": "watchHandler"
        }]; }
}
