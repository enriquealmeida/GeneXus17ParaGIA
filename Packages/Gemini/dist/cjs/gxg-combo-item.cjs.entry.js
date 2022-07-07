'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

const index = require('./index-1115561c.js');

const comboItemCss = ":host{display:block;cursor:pointer;height:var(--spacing-comp-05);display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;font-family:var(--font-family-primary);font-size:var(--font-size-sm);background-color:var(--color-background);color:var(--color-on-background);padding:0 var(--spacing-comp-01);-webkit-transition:height 0.25s, opacity 0.25s;transition:height 0.25s, opacity 0.25s;opacity:1;overflow:hidden}:host .content{display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center}:host(.hidden){height:0;opacity:0}:host(.display-none){display:none}:host(:hover),:host(.selected:hover){background-color:var(--color-secondary-hover);color:var(--color-always-white)}:host(.selected){background-color:var(--color-secondary-active);color:var(--color-always-white)}:host(.exact-match){background-color:var(--color-success-light);color:var(--color-success-dark)}:host(:focus){outline-width:var(--border-width-md);outline-color:var(--color-primary-active);outline-style:solid;outline-offset:-2px}";

const GxgComboItem = class {
    constructor(hostRef) {
        index.registerInstance(this, hostRef);
        this.itemClicked = index.createEvent(this, "itemClicked", 7);
        /**
         * Any icon that belongs to Gemini icon library: https://gx-gemini.netlify.app/?path=/story/icons
         */
        this.icon = undefined;
        /**
         * The item value. This is what the filter with search for. If value is not provided, the filter will search by the item innerHTML.
         */
        this.value = undefined;
        /**
         * (This prop is for internal use).
         */
        this.iconColor = "auto";
    }
    itemClickedFunc() {
        const index = this.el.getAttribute("index");
        const icon = this.el.getAttribute("icon");
        let value = this.value;
        if (value === undefined) {
            value = this.el.innerHTML;
        }
        this.itemClicked.emit({
            index: parseInt(index, 10),
            value: value.toString(),
            icon: icon,
        });
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
            this.itemClickedFunc();
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
        return (index.h(index.Host, { onClick: this.itemClickedFunc.bind(this), onKeyDown: this.onKeyDown.bind(this), onMouseOver: this.onMouseOver.bind(this), onMouseOut: this.onMouseOut.bind(this) }, index.h("div", { class: "content" }, this.icon !== undefined ? (index.h("gxg-icon", { color: this.iconColor, size: "small", type: this.icon })) : null, index.h("slot", null))));
    }
    get el() { return index.getElement(this); }
};
GxgComboItem.style = comboItemCss;

exports.gxg_combo_item = GxgComboItem;
