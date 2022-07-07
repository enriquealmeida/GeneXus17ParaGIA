import { Component, Host, h, Prop, State, Element, Listen, } from "@stencil/core";
export class GxgCombo {
    constructor() {
        /**
         * The combo width
         */
        this.width = "240px";
        this.selectedItemValue = "";
        this.inputTextValue = "";
        this.showItems = false;
        this.inputTextIcon = undefined;
        this.inputTextIconPosition = null;
        this.noMatch = false;
        this.slottedContent = null;
    }
    componentWillLoad() {
        this.itemsNodeList = this.el.querySelectorAll("gxg-combo-item");
        this.itemsNodeList.forEach((item, i) => {
            const itemHtmlElement = item;
            itemHtmlElement.setAttribute("index", i.toString());
            itemHtmlElement.setAttribute("tabindex", "0");
        });
    }
    itemClickedHandler(event) {
        const previouslyselectedItem = this.el.querySelector(".selected");
        if (previouslyselectedItem !== null) {
            previouslyselectedItem.classList.remove("selected");
            //set icon color to auto
            previouslyselectedItem.iconColor =
                "auto";
        }
        const actualSelectedItem = this.el.querySelector("[index='" + event.detail["index"] + "']");
        actualSelectedItem.classList.add("selected");
        this.selectedItemValue = event.detail.value;
        this.inputTextValue = event.detail.value;
        if (event.detail.icon !== null) {
            this.inputTextIcon = event.detail.icon;
            this.inputTextIconPosition = "start";
        }
        else {
            this.inputTextIcon = null;
            this.inputTextIconPosition = null;
        }
        // Display all hidden items
        const hiddenItems = this.el.querySelectorAll(".hidden");
        hiddenItems.forEach((hiddenItem) => {
            const hiddenItemHTMLElement = hiddenItem;
            hiddenItemHTMLElement.classList.remove("hidden");
        });
        //remove exact match class
        const exactMatch = this.el.querySelector(".exact-match");
        if (exactMatch !== null) {
            exactMatch.classList.remove("exact-match");
        }
        this.showItems = false;
    }
    onInputGxgformText(e) {
        this.inputTextValue = e.detail;
        this.inputTextIcon = null;
        this.inputTextIconPosition = null;
        const itemSelected = this.el.querySelector(".selected");
        if (itemSelected !== null) {
            itemSelected.classList.remove("selected");
        }
        const filterValue = e.detail.toLowerCase();
        this.itemsNodeList.forEach((item) => {
            const itemHtmlElement = item;
            let itemValue = itemHtmlElement.getAttribute("value");
            if (itemValue === null) {
                itemValue = itemHtmlElement.innerText;
            }
            itemValue = itemValue.toLowerCase();
            if (!itemValue.includes(filterValue)) {
                itemHtmlElement.classList.add("hidden");
            }
            else {
                itemHtmlElement.classList.remove("hidden");
            }
            if (itemValue === filterValue) {
                itemHtmlElement.classList.add("exact-match");
            }
            else {
                itemHtmlElement.classList.remove("exact-match");
            }
        });
        const numberOfHiddenItems = this.el.getElementsByClassName("hidden").length;
        if (this.itemsNodeList.length === numberOfHiddenItems) {
            this.noMatch = true;
        }
        else {
            this.noMatch = false;
        }
    }
    onKeyDownGxgformText(e) {
        if (e.key === "Escape") {
            this.showItems = false;
            this.textInput.blur();
        }
    }
    toggleItems() {
        if (this.showItems === true) {
            this.showItems = false;
        }
        else {
            this.showItems = true;
        }
    }
    detectClickOutsideCombo(event) {
        const comboMainContainer = this.el.shadowRoot.querySelector(".main-container");
        const x = event.x;
        const y = event.y;
        //Contextual menu coordinates
        const comboMainContainerArea = comboMainContainer.getBoundingClientRect();
        if ((x > comboMainContainerArea.left &&
            x < comboMainContainerArea.right &&
            y > comboMainContainerArea.top &&
            y < comboMainContainerArea.bottom) ||
            (event.screenX === 0 &&
                event.screenY === 0 &&
                event.clientX === 0 &&
                event.clientY === 0)) {
            //Click happened inside the combo
        }
        else {
            this.showItems = false;
            //Click happened outside the combo
        }
    }
    componentDidLoad() {
        document.addEventListener("click", this.detectClickOutsideCombo.bind(this));
    }
    componentDidUnload() {
        document.removeEventListener("click", this.detectClickOutsideCombo);
    }
    selecteItemFunc() {
        return this.selectedItemValue;
    }
    onInputFocus() {
        this.showItems = true;
    }
    clearInputFunc() {
        this.selectedItemValue = "";
        this.inputTextValue = "";
        const gxgFormText = this.el.shadowRoot.getElementById("gxgFormText");
        gxgFormText.value = "";
        gxgFormText.focus();
        this.inputTextIconPosition = null;
        const hiddenItems = this.el.querySelectorAll(".hidden");
        hiddenItems.forEach((item) => {
            item.classList.remove("hidden");
        });
        const selectedItem = this.el.querySelector(".selected");
        if (selectedItem !== null) {
            //set icon color to auto
            const selectedItemIcon = selectedItem.shadowRoot.querySelector("gxg-icon");
            selectedItemIcon.color = "auto";
            selectedItem.classList.remove("selected");
        }
        const exactMatch = this.el.querySelector(".exact-match");
        if (exactMatch !== null) {
            exactMatch.classList.remove("exact-match");
        }
        this.showItems = true;
    }
    render() {
        return (h(Host, null,
            h("div", { class: { "main-container": true }, style: { width: this.width } },
                h("div", { class: { "search-container": true } },
                    h("gxg-form-text", { placeholder: "Search item", onInput: this.onInputGxgformText.bind(this), onKeyDown: this.onKeyDownGxgformText.bind(this), onFocus: () => this.onInputFocus(), value: this.selectedItemValue, id: "gxgFormText", icon: this.inputTextIcon, iconPosition: this.inputTextIconPosition, ref: (el) => (this.textInput = el) }),
                    this.inputTextValue !== "" ? (h("gxg-button", { class: { "delete-icon": true }, icon: "menus/delete", type: "tertiary", onClick: () => this.clearInputFunc() })) : null,
                    h("gxg-button", { class: { "arrow-down-icon": true }, icon: "navigation/arrow-down", type: "tertiary", onClick: () => this.toggleItems() }),
                    h("span", { class: "layer" })),
                this.showItems ? (h("div", { class: {
                        "items-container": true,
                    } },
                    h("slot", null),
                    this.noMatch ? (h("div", { class: "no-courrences-found" },
                        "No occurrences found",
                        h("span", null, "ctrl/cmd + backspace to erase"))) : null)) : null)));
    }
    static get is() { return "gxg-combo"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["combo.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["combo.css"]
    }; }
    static get properties() { return {
        "width": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "string",
                "resolved": "string",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The combo width"
            },
            "attribute": "width",
            "reflect": false,
            "defaultValue": "\"240px\""
        }
    }; }
    static get states() { return {
        "itemsNodeList": {},
        "selectedItemValue": {},
        "inputTextValue": {},
        "showItems": {},
        "inputTextIcon": {},
        "inputTextIconPosition": {},
        "noMatch": {},
        "slottedContent": {}
    }; }
    static get elementRef() { return "el"; }
    static get listeners() { return [{
            "name": "itemClicked",
            "method": "itemClickedHandler",
            "target": undefined,
            "capture": false,
            "passive": false
        }]; }
}
