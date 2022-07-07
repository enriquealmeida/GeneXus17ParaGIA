import { Component, Host, h, Prop, Element, Event, Listen, } from "@stencil/core";
export class GxgListbox {
    constructor() {
        /**
         * The listbox title that appears on the header
         */
        this.theTitle = "";
        /**
         * The listbox width
         */
        this.width = "280px";
        /**
         * The prescence of this attribute will display a checkbox for every item
         */
        this.checkboxes = false;
    }
    componentWillLoad() {
        //Set checkboxes
        if (this.checkboxes) {
            const items = this.el.querySelectorAll("gxg-listbox-item");
            items.forEach((item) => {
                const checkbox = document.createElement("gxg-form-checkbox");
                checkbox.setAttribute("slot", "checkbox");
                checkbox.setAttribute("tabindex", "-1");
                item.prepend(checkbox);
            });
        }
        //Set index and Tabindex
        const itemsNodeList = this.el.querySelectorAll("gxg-listbox-item");
        itemsNodeList.forEach((item, i) => {
            //index
            const itemHtmlElement = item;
            itemHtmlElement.setAttribute("index", i.toString());
            //tabindex
            item.setAttribute("tabindex", "0");
        });
    }
    itemClickedHandler(e) {
        if (!e.detail.crtlKey && !e.detail.cmdKey && !this.checkboxes) {
            const actualSelectedItems = this.el.querySelectorAll(".selected");
            if (actualSelectedItems.length > 0) {
                actualSelectedItems.forEach((item) => {
                    item.classList.remove("selected");
                    //set icon color to auto
                    item.iconColor = "auto";
                    //set checkbox checked to false
                    const checkbox = item.querySelector("gxg-form-checkbox");
                    if (checkbox !== null) {
                        checkbox.checked = false;
                    }
                });
            }
        }
        const actualSelectedItem = this.el.querySelector("[index='" + e.detail["index"] + "']");
        //checkbox
        const actualSelectedItemCheckbox = actualSelectedItem.querySelector("gxg-form-checkbox");
        if (actualSelectedItem.classList.contains("selected")) {
            actualSelectedItem.classList.remove("selected");
            //set icon color to auto
            if (!e.detail.mouseClicked) {
                actualSelectedItem.iconColor = "auto";
                if (actualSelectedItemCheckbox !== null) {
                    actualSelectedItemCheckbox.checked = false;
                }
            }
        }
        else {
            actualSelectedItem.classList.add("selected");
            //set icon color to negative
            actualSelectedItem.iconColor =
                "negative";
            if (actualSelectedItemCheckbox !== null) {
                actualSelectedItemCheckbox.checked = true;
            }
        }
        this.emmitSelectedItems();
    }
    checkboxClickedHandler(e) {
        const actualSelectedItem = this.el.querySelector("[index='" + e.detail["index"] + "']");
        if (actualSelectedItem.classList.contains("selected")) {
            actualSelectedItem.classList.remove("selected");
        }
        else {
            actualSelectedItem.classList.add("selected");
        }
        this.emmitSelectedItems();
    }
    emmitSelectedItems() {
        const selectedItems = this.el.querySelectorAll(".selected");
        const selectedItemsArray = [];
        selectedItems.forEach((item) => {
            const itemIndex = item.getAttribute("index");
            let itemValue = item.getAttribute("value");
            if (itemValue === null) {
                itemValue = item.textContent;
            }
            else {
                itemValue = itemValue.toString();
            }
            const itemObj = {
                index: itemIndex,
                value: itemValue,
            };
            selectedItemsArray.push(itemObj);
        });
        this.selectionChanged.emit(Object.assign({}, selectedItemsArray));
    }
    render() {
        return (h(Host, null,
            h("div", { style: { width: this.width }, class: { container: true } },
                h("header", { class: { header: true } }, this.theTitle),
                h("main", { class: { main: true } },
                    h("slot", null)))));
    }
    static get is() { return "gxg-listbox"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["listbox.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["listbox.css"]
    }; }
    static get properties() { return {
        "theTitle": {
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
                "text": "The listbox title that appears on the header"
            },
            "attribute": "the-title",
            "reflect": false,
            "defaultValue": "\"\""
        },
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
                "text": "The listbox width"
            },
            "attribute": "width",
            "reflect": false,
            "defaultValue": "\"280px\""
        },
        "checkboxes": {
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
                "text": "The prescence of this attribute will display a checkbox for every item"
            },
            "attribute": "checkboxes",
            "reflect": false,
            "defaultValue": "false"
        }
    }; }
    static get events() { return [{
            "method": "selectionChanged",
            "name": "selectionChanged",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": "This event emmits the items that are currently selected. event.detail contains the selected items as objects. Each object contains the item idex and the item value. If value was not provided, the value will be the item innerText."
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }]; }
    static get elementRef() { return "el"; }
    static get listeners() { return [{
            "name": "itemClicked",
            "method": "itemClickedHandler",
            "target": undefined,
            "capture": false,
            "passive": false
        }, {
            "name": "checkboxClicked",
            "method": "checkboxClickedHandler",
            "target": undefined,
            "capture": false,
            "passive": false
        }]; }
}
