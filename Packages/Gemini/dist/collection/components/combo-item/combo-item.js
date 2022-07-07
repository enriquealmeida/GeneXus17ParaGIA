import { Component, Host, h, Prop, Event, Element, } from "@stencil/core";
export class GxgComboItem {
    constructor() {
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
        return (h(Host, { onClick: this.itemClickedFunc.bind(this), onKeyDown: this.onKeyDown.bind(this), onMouseOver: this.onMouseOver.bind(this), onMouseOut: this.onMouseOut.bind(this) },
            h("div", { class: "content" },
                this.icon !== undefined ? (h("gxg-icon", { color: this.iconColor, size: "small", type: this.icon })) : null,
                h("slot", null))));
    }
    static get is() { return "gxg-combo-item"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["combo-item.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["combo-item.css"]
    }; }
    static get properties() { return {
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
        },
        "value": {
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
                "text": "The item value. This is what the filter with search for. If value is not provided, the filter will search by the item innerHTML."
            },
            "attribute": "value",
            "reflect": false,
            "defaultValue": "undefined"
        },
        "iconColor": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "Color",
                "resolved": "\"alwaysblack\" | \"auto\" | \"disabled\" | \"error\" | \"negative\" | \"onbackground\" | \"ondisabled\" | \"primary-active\" | \"primary-enabled\" | \"primary-hover\" | \"success\" | \"warning\"",
                "references": {
                    "Color": {
                        "location": "import",
                        "path": "../icon/icon"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "(This prop is for internal use)."
            },
            "attribute": "icon-color",
            "reflect": false,
            "defaultValue": "\"auto\""
        }
    }; }
    static get events() { return [{
            "method": "itemClicked",
            "name": "itemClicked",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": "This event is triggered when the user clicks on an item. event.detail contains the item index, item value, and item icon."
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }]; }
    static get elementRef() { return "el"; }
}
