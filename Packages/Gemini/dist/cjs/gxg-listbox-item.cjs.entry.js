'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

const index = require('./index-1115561c.js');

const listboxItemCss = ":host{display:block;font-family:var(--font-family-primary);font-size:var(--font-size-sm);height:var(--spacing-comp-05);padding:0 var(--spacing-comp-02);color:var(--color-on-background);cursor:pointer;display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center}:host .icon{-webkit-margin-end:var(--spacing-comp-01);margin-inline-end:var(--spacing-comp-01)}:host ::slotted(gxg-form-checkbox){-webkit-margin-end:var(--spacing-comp-01);margin-inline-end:var(--spacing-comp-01)}:host(:hover),:host(.selected:hover){background-color:var(--color-secondary-hover);color:var(--color-always-white)}:host(.has-icon.no-checkbox){-webkit-padding-start:var(--spacing-comp-01);padding-inline-start:var(--spacing-comp-01)}:host(.selected){background-color:var(--color-secondary-active);color:var(--color-always-white)}:host(:focus){outline-width:var(--border-width-md);outline-color:var(--color-primary-active);outline-style:solid;outline-offset:-2px}";

const GxgListboxItem = class {
    constructor(hostRef) {
        index.registerInstance(this, hostRef);
        this.itemClicked = index.createEvent(this, "itemClicked", 7);
        this.checkboxClicked = index.createEvent(this, "checkboxClicked", 7);
        /**
         * Any icon that belongs to Gemini icon library: https://gx-gemini.netlify.app/?path=/story/icons
         */
        this.icon = undefined;
        /**
         * The item value. If value is not provided, the value will be the item innerHTML.
         */
        this.value = undefined;
        /**
         * (This prop is for internal use).
         */
        this.iconColor = "auto";
        this.checkboxes = false;
        this.itemHasFocus = false;
    }
    componentWillLoad() {
        const listbox = this.el.parentElement;
        const listboxCheckboxes = listbox.getAttribute("checkboxes");
        if (listboxCheckboxes !== null) {
            this.checkboxes = true;
            const checkbox = this.el.querySelector("gxg-form-checkbox");
            checkbox.addEventListener("click", () => {
                const index = this.el.getAttribute("index");
                this.checkboxClicked.emit({
                    index: parseInt(index, 10),
                });
            });
        }
    }
    itemClickedFunc(e) {
        if (!this.checkboxes) {
            const index = this.el.getAttribute("index");
            this.itemClicked.emit({
                index: parseInt(index, 10),
                crtlKey: e.ctrlKey,
                cmdKey: e.metaKey,
                mouseClicked: true,
            });
        }
    }
    onKeyDown(e) {
        e.preventDefault();
        if (e.code === "ArrowDown") {
            const nextItem = this.el.nextElementSibling;
            if (nextItem !== null) {
                nextItem.focus();
            }
        }
        else if (e.code === "ArrowUp") {
            const prevItem = this.el.previousElementSibling;
            if (prevItem !== null) {
                prevItem.focus();
            }
        }
        if (e.code === "Enter") {
            const index = this.el.getAttribute("index");
            this.itemClicked.emit({
                index: parseInt(index, 10),
                crtlKey: e.ctrlKey,
                cmdKey: e.metaKey,
                mouseClicked: false,
            });
        }
    }
    onMouseOver() {
        this.iconColor = "negative";
    }
    onMouseOut() {
        const itemIsSelected = this.el.classList.contains("selected");
        if (!itemIsSelected) {
            this.iconColor = "auto";
        }
    }
    render() {
        return (index.h(index.Host, { class: {
                "has-icon": this.icon !== undefined,
                "no-checkbox": !this.checkboxes,
            }, onClick: this.itemClickedFunc.bind(this), onKeyDown: this.onKeyDown.bind(this), onMouseOver: this.onMouseOver.bind(this), onMouseOut: this.onMouseOut.bind(this) }, index.h("slot", { name: "checkbox" }), this.icon !== undefined ? (index.h("gxg-icon", { class: "icon", color: this.iconColor, size: "regular", type: this.icon })) : null, index.h("slot", null)));
    }
    get el() { return index.getElement(this); }
};
GxgListboxItem.style = listboxItemCss;

exports.gxg_listbox_item = GxgListboxItem;
