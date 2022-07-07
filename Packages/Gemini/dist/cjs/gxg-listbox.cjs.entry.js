'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

const index = require('./index-1115561c.js');

const listboxCss = ":host{display:block}:host .header{height:var(--spacing-comp-06);background-color:var(--gray-02);display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;padding:0 var(--spacing-comp-02);font-family:var(--font-family-primary);text-transform:uppercase;font-size:var(--font-size-sm);font-weight:var(--font-weight-bold);color:var(--color-on-background)}:host .main{border-style:var(--border-style-regular);border-width:var(--border-radius-xs);border-color:var(--gray-02)}:host .icon{-webkit-margin-end:var(--spacing-comp-01);margin-inline-end:var(--spacing-comp-01)}:host .checkbox{-webkit-margin-end:var(--spacing-comp-01);margin-inline-end:var(--spacing-comp-01)}:host.selected{background-color:var(--color-secondary-active);color:var(--color-always-white)}";

const GxgListbox = class {
    constructor(hostRef) {
        index.registerInstance(this, hostRef);
        this.selectionChanged = index.createEvent(this, "selectionChanged", 7);
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
        return (index.h(index.Host, null, index.h("div", { style: { width: this.width }, class: { container: true } }, index.h("header", { class: { header: true } }, this.theTitle), index.h("main", { class: { main: true } }, index.h("slot", null)))));
    }
    get el() { return index.getElement(this); }
};
GxgListbox.style = listboxCss;

exports.gxg_listbox = GxgListbox;
