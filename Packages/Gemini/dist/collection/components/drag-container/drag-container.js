import { Component, Prop, Element, h, Host, Event, Listen, } from "@stencil/core";
import { makeDraggable } from "../../utils/makeDraggable";
export class GxgDragContainer {
    constructor() {
        /**
         * The presence of this attribute adds a "delete" button to each gxg-drag-box. When pressed, the "deleted" event is emmited.
         */
        this.deletable = false;
        /**
         * The max-width of the box container
         */
        this.maxWidth = "100%";
        /**
         * The padding (internal spacing) of the gxg-drag-boxes
         */
        this.padding = undefined;
    }
    clickedHandler(event) {
        const boxes = this.el.querySelectorAll("*");
        boxes.forEach((item) => {
            if (event.detail === item.getAttribute("id")) {
                item.setAttribute("active", "active");
            }
            else {
                item.removeAttribute("active");
            }
        });
    }
    getDraggableElements() {
        return this.el.querySelectorAll("gxg-drag-box");
    }
    componentDidLoad() {
        this.dndCleanup = makeDraggable(this);
        //Set padding to each of the drag-boxes
        const dragBoxes = this.el.querySelectorAll("gxg-drag-box");
        dragBoxes.forEach((dragBox) => {
            if (this.padding !== undefined) {
                dragBox.setAttribute("padding", this.padding);
            }
        });
        //Deletable button for each of the child items
        if (this.deletable) {
            const items = this.el.querySelectorAll("*");
            items.forEach((item) => {
                item.setAttribute("deletable", "deletable");
            });
        }
    }
    disconnectedCallback() {
        this.dndCleanup();
    }
    render() {
        return (h(Host, { style: { maxWidth: this.maxWidth } },
            h("slot", null),
            h("div", { class: "placeholder" })));
    }
    static get is() { return "gxg-drag-container"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["drag-container.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["drag-container.css"]
    }; }
    static get properties() { return {
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
                "text": "The presence of this attribute adds a \"delete\" button to each gxg-drag-box. When pressed, the \"deleted\" event is emmited."
            },
            "attribute": "deletable",
            "reflect": false,
            "defaultValue": "false"
        },
        "maxWidth": {
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
                "text": "The max-width of the box container"
            },
            "attribute": "max-width",
            "reflect": false,
            "defaultValue": "\"100%\""
        },
        "padding": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "Padding",
                "resolved": "\"0\" | \"l\" | \"m\" | \"s\" | \"xl\" | \"xs\" | \"xxl\" | \"xxxl\"",
                "references": {
                    "Padding": {
                        "location": "import",
                        "path": "../drag-box/drag-box"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The padding (internal spacing) of the gxg-drag-boxes"
            },
            "attribute": "padding",
            "reflect": false,
            "defaultValue": "undefined"
        }
    }; }
    static get events() { return [{
            "method": "itemDragStart",
            "name": "itemDragStart",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": ""
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }, {
            "method": "itemDrop",
            "name": "itemDrop",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": ""
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }, {
            "method": "itemDragOver",
            "name": "itemDragOver",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": ""
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }, {
            "method": "itemDragLeave",
            "name": "itemDragLeave",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": ""
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }, {
            "method": "itemDragEnter",
            "name": "itemDragEnter",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": ""
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }]; }
    static get elementRef() { return "el"; }
    static get listeners() { return [{
            "name": "clicked",
            "method": "clickedHandler",
            "target": undefined,
            "capture": false,
            "passive": false
        }]; }
}
