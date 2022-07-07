'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

const index = require('./index-1115561c.js');

const gxgFilterItemCss = ":host{display:block;font-family:var(--font-family-primary);font-size:var(--font-size-sm);padding:0 var(--spacing-comp-01) 0 var(--spacing-comp-01);cursor:pointer;display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;height:var(--spacing-comp-05);opacity:1;-webkit-transition:all 0.25s;transition:all 0.25s;overflow:hidden;color:var(--color-on-background)}:host gxg-icon{-webkit-margin-end:var(--spacing-comp-01);margin-inline-end:var(--spacing-comp-01)}:host .text{text-overflow:ellipsis;white-space:nowrap;overflow:hidden;width:calc(100% - 24px)}:host(:hover){background-color:var(--color-secondary-enabled)}:host(.opacity-zero){opacity:0}:host(.height-zero){height:0}:host(.exact-match){color:var(--color-success-dark)}:host(.exact-match:hover){color:var(--color-success-dark);background-color:var(--color-success-light)}:host(:focus){outline:none;outline-color:var(--color-primary-active);-webkit-box-shadow:var(--box-shadow-values) var(--color-primary-active);box-shadow:var(--box-shadow-values) var(--color-primary-active)}";

const GxgFilterItem = class {
    constructor(hostRef) {
        index.registerInstance(this, hostRef);
        this.itemClickedEvent = index.createEvent(this, "itemClickedEvent", 7);
        /**
         * The item-id (required if you want to know that this item was clicked)
         */
        this.itemId = undefined;
        /**
         * The type (optional)
         */
        this.type = undefined;
        /**
         * Any icon that belongs to Gemini icon library: https://gx-gemini.netlify.app/?path=/story/icons
         */
        this.icon = undefined;
    }
    itemClicked() {
        this.itemClickedEvent.emit({
            "item-id": this.itemId,
            "item-text": this.el.innerHTML,
            "item-type": this.type,
        });
    }
    render() {
        return (index.h(index.Host, { onClick: this.itemClicked.bind(this), tabindex: "0" }, this.icon !== undefined ? (index.h("gxg-icon", { color: "auto", size: "small", type: this.icon })) : null, index.h("div", { class: "text" }, index.h("slot", null))));
    }
    get el() { return index.getElement(this); }
};
GxgFilterItem.style = gxgFilterItemCss;

exports.gxg_filter_item = GxgFilterItem;
