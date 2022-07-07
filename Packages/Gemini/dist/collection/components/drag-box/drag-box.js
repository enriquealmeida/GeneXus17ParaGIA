import { Component, Element, Event, h, Host, Prop, } from "@stencil/core";
export class GxgDragBox {
    constructor() {
        /**
         * The presence of this attribute makes this box active
         */
        this.active = false;
        /**
         * The presence of this attribute adds a "delete" button that, when pressed, triggers the "deleted" event
         */
        this.deletable = false;
        /**
         * The padding (internal spacing) of the drag-box (Set it on the drag-container to apply the same padding to all of the gxg-drag-box items)
         */
        this.padding = "s";
    }
    clickedHandler() {
        this.clicked.emit(this.el.getAttribute("id"));
    }
    deleteHandler(event) {
        event.stopPropagation();
        this.deleted.emit("box was deleted");
    }
    handlerOnKeyDown(event) {
        if (event.keyCode == 13) {
            //enter key was pressed
            this.active = true;
            this.clicked.emit(this.el.getAttribute("id"));
        }
        else if (event.keyCode === 9 && event.shiftKey) {
            //tab and shift keys were pressed
            const previousElement = this.el.previousElementSibling;
            event.preventDefault();
            previousElement.focus();
        }
        else if (event.keyCode == 9) {
            //only tab key was pressed
            if (!this.active) {
                const nextElement = this.el.nextElementSibling;
                event.preventDefault();
                nextElement.focus();
            }
        }
    }
    componentDidLoad() {
        if (this.title !== undefined) {
            const title = this.el.shadowRoot.querySelector(".container-content__title");
            this.el.prepend(title);
        }
    }
    render() {
        return (h(Host, { tabindex: "0", onClick: this.clickedHandler.bind(this), onKeyDown: this.handlerOnKeyDown.bind(this) },
            this.active ? null : h("div", { class: "cover" }),
            h("span", { class: "border" }),
            h("div", { class: "drag-icon-container" },
                h("gxg-icon", { size: "regular", type: "navigation/drag" })),
            h("div", { class: "container-content" },
                this.title !== undefined ? (h("span", { class: "container-content__title" }, this.title)) : null,
                h("slot", null)),
            h("div", { class: "delete-button-container" }, this.deletable ? (h("gxg-button", { "button-styles-editable": true, icon: "gemini-tools/delete", onClick: this.deleteHandler.bind(this), type: "secondary-icon-only" })) : null)));
    }
    static get is() { return "gxg-drag-box"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["drag-box.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["drag-box.css"]
    }; }
    static get properties() { return {
        "active": {
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
                "text": "The presence of this attribute makes this box active"
            },
            "attribute": "active",
            "reflect": true,
            "defaultValue": "false"
        },
        "deletable": {
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
                "text": "The presence of this attribute adds a \"delete\" button that, when pressed, triggers the \"deleted\" event"
            },
            "attribute": "deletable",
            "reflect": false,
            "defaultValue": "false"
        },
        "padding": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "Padding",
                "resolved": "\"0\" | \"l\" | \"m\" | \"s\" | \"xl\" | \"xs\" | \"xxl\" | \"xxxl\"",
                "references": {
                    "Padding": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The padding (internal spacing) of the drag-box (Set it on the drag-container to apply the same padding to all of the gxg-drag-box items)"
            },
            "attribute": "padding",
            "reflect": true,
            "defaultValue": "\"s\""
        },
        "title": {
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
                "text": "The title"
            },
            "attribute": "title",
            "reflect": false
        }
    }; }
    static get events() { return [{
            "method": "clicked",
            "name": "clicked",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": "This event is for internal use"
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }, {
            "method": "deleted",
            "name": "deleted",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": "This event fires when the \"delete\" button is pressed"
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }]; }
    static get elementRef() { return "el"; }
}
