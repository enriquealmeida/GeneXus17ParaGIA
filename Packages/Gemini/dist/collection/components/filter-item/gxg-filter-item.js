import { Component, Element, Event, Host, Prop, h, } from "@stencil/core";
export class GxgFilterItem {
    constructor() {
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
        return (h(Host, { onClick: this.itemClicked.bind(this), tabindex: "0" },
            this.icon !== undefined ? (h("gxg-icon", { color: "auto", size: "small", type: this.icon })) : null,
            h("div", { class: "text" },
                h("slot", null))));
    }
    static get is() { return "gxg-filter-item"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["gxg-filter-item.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["gxg-filter-item.css"]
    }; }
    static get properties() { return {
        "itemId": {
            "type": "any",
            "mutable": false,
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The item-id (required if you want to know that this item was clicked)"
            },
            "attribute": "item-id",
            "reflect": false,
            "defaultValue": "undefined"
        },
        "type": {
            "type": "any",
            "mutable": false,
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The type (optional)"
            },
            "attribute": "type",
            "reflect": false,
            "defaultValue": "undefined"
        },
        "icon": {
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
                "text": "Any icon that belongs to Gemini icon library: https://gx-gemini.netlify.app/?path=/story/icons"
            },
            "attribute": "icon",
            "reflect": false,
            "defaultValue": "undefined"
        }
    }; }
    static get events() { return [{
            "method": "itemClickedEvent",
            "name": "itemClickedEvent",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": "This event is fired when the user clicks on an item. event.detail carries the item id, type, and text."
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }]; }
    static get elementRef() { return "el"; }
}
