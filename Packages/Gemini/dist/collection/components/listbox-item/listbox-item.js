import { Component, Host, h, Prop, Element, Event, State, } from "@stencil/core";
export class GxgListboxItem {
    constructor() {
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
        return (h(Host, { class: {
                "has-icon": this.icon !== undefined,
                "no-checkbox": !this.checkboxes,
            }, onClick: this.itemClickedFunc.bind(this), onKeyDown: this.onKeyDown.bind(this), onMouseOver: this.onMouseOver.bind(this), onMouseOut: this.onMouseOut.bind(this) },
            h("slot", { name: "checkbox" }),
            this.icon !== undefined ? (h("gxg-icon", { class: "icon", color: this.iconColor, size: "regular", type: this.icon })) : null,
            h("slot", null)));
    }
    static get is() { return "gxg-listbox-item"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["listbox-item.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["listbox-item.css"]
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
                "text": "The item value. If value is not provided, the value will be the item innerHTML."
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
    static get states() { return {
        "checkboxes": {},
        "itemHasFocus": {}
    }; }
    static get events() { return [{
            "method": "itemClicked",
            "name": "itemClicked",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": "(This event is for internal use.)"
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }, {
            "method": "checkboxClicked",
            "name": "checkboxClicked",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": "(This event is for internal use.)"
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }]; }
    static get elementRef() { return "el"; }
}
