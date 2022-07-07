import { r as registerInstance, c as createEvent, h, H as Host, g as getElement } from './index-09b1517f.js';
var listboxCss = ":host{display:block}:host .header{height:var(--spacing-comp-06);background-color:var(--gray-02);display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;padding:0 var(--spacing-comp-02);font-family:var(--font-family-primary);text-transform:uppercase;font-size:var(--font-size-sm);font-weight:var(--font-weight-bold);color:var(--color-on-background)}:host .main{border-style:var(--border-style-regular);border-width:var(--border-radius-xs);border-color:var(--gray-02)}:host .icon{-webkit-margin-end:var(--spacing-comp-01);margin-inline-end:var(--spacing-comp-01)}:host .checkbox{-webkit-margin-end:var(--spacing-comp-01);margin-inline-end:var(--spacing-comp-01)}:host.selected{background-color:var(--color-secondary-active);color:var(--color-always-white)}";
var GxgListbox = /** @class */ (function () {
    function GxgListbox(hostRef) {
        registerInstance(this, hostRef);
        this.selectionChanged = createEvent(this, "selectionChanged", 7);
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
    GxgListbox.prototype.componentWillLoad = function () {
        //Set checkboxes
        if (this.checkboxes) {
            var items = this.el.querySelectorAll("gxg-listbox-item");
            items.forEach(function (item) {
                var checkbox = document.createElement("gxg-form-checkbox");
                checkbox.setAttribute("slot", "checkbox");
                checkbox.setAttribute("tabindex", "-1");
                item.prepend(checkbox);
            });
        }
        //Set index and Tabindex
        var itemsNodeList = this.el.querySelectorAll("gxg-listbox-item");
        itemsNodeList.forEach(function (item, i) {
            //index
            var itemHtmlElement = item;
            itemHtmlElement.setAttribute("index", i.toString());
            //tabindex
            item.setAttribute("tabindex", "0");
        });
    };
    GxgListbox.prototype.itemClickedHandler = function (e) {
        if (!e.detail.crtlKey && !e.detail.cmdKey && !this.checkboxes) {
            var actualSelectedItems = this.el.querySelectorAll(".selected");
            if (actualSelectedItems.length > 0) {
                actualSelectedItems.forEach(function (item) {
                    item.classList.remove("selected");
                    //set icon color to auto
                    item.iconColor = "auto";
                    //set checkbox checked to false
                    var checkbox = item.querySelector("gxg-form-checkbox");
                    if (checkbox !== null) {
                        checkbox.checked = false;
                    }
                });
            }
        }
        var actualSelectedItem = this.el.querySelector("[index='" + e.detail["index"] + "']");
        //checkbox
        var actualSelectedItemCheckbox = actualSelectedItem.querySelector("gxg-form-checkbox");
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
    };
    GxgListbox.prototype.checkboxClickedHandler = function (e) {
        var actualSelectedItem = this.el.querySelector("[index='" + e.detail["index"] + "']");
        if (actualSelectedItem.classList.contains("selected")) {
            actualSelectedItem.classList.remove("selected");
        }
        else {
            actualSelectedItem.classList.add("selected");
        }
        this.emmitSelectedItems();
    };
    GxgListbox.prototype.emmitSelectedItems = function () {
        var selectedItems = this.el.querySelectorAll(".selected");
        var selectedItemsArray = [];
        selectedItems.forEach(function (item) {
            var itemIndex = item.getAttribute("index");
            var itemValue = item.getAttribute("value");
            if (itemValue === null) {
                itemValue = item.textContent;
            }
            else {
                itemValue = itemValue.toString();
            }
            var itemObj = {
                index: itemIndex,
                value: itemValue,
            };
            selectedItemsArray.push(itemObj);
        });
        this.selectionChanged.emit(Object.assign({}, selectedItemsArray));
    };
    GxgListbox.prototype.render = function () {
        return (h(Host, null, h("div", { style: { width: this.width }, class: { container: true } }, h("header", { class: { header: true } }, this.theTitle), h("main", { class: { main: true } }, h("slot", null)))));
    };
    Object.defineProperty(GxgListbox.prototype, "el", {
        get: function () { return getElement(this); },
        enumerable: false,
        configurable: true
    });
    return GxgListbox;
}());
GxgListbox.style = listboxCss;
export { GxgListbox as gxg_listbox };
