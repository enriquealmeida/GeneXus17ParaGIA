import { Component, Element, h, Host, Listen, State, Prop, } from "@stencil/core";
export class GxgAccordion {
    constructor() {
        /**
         * The presence of this attribute makes all of the accordion-items disabled and not focusable
         */
        this.disabled = false;
        /**
         * If this attribute is present, only one accordion-item can be open at the same time
         */
        this.singleItemOpen = false;
        /**
         * The accordion flavor
         */
        this.mode = "classical";
        /**
         * The accordion max-width
         */
        this.maxWidth = "100%";
        /**
         * The presence of this attribues removes the padding (internal spacing) from the accordion items containers. This property only applies for the "classical" or "boxed" modes.
         */
        this.noPadding = false;
    }
    itemClickedHandler(event) {
        event.stopPropagation();
        this.accordions.forEach((accordion) => {
            const id = accordion.itemId;
            if (this.singleItemOpen) {
                if (id === event.detail) {
                    if (accordion.status === "open") {
                        accordion.status = "closed";
                    }
                    else {
                        accordion.status = "open";
                    }
                }
                else {
                    accordion.status = "closed";
                }
            }
            else {
                if (id === event.detail) {
                    if (accordion.status === "open") {
                        accordion.status = "closed";
                    }
                    else {
                        accordion.status = "open";
                    }
                }
            }
        });
    }
    itemLoadedHandler() {
        this.setupAccordions();
    }
    componentWillLoad() {
        this.setupAccordions();
    }
    setupAccordions() {
        this.accordions = Array.from(this.el.querySelectorAll(":scope > gxg-accordion-item"));
        //Disabled
        if (this.disabled) {
            this.accordions.forEach((accordion) => {
                accordion.status = "closed";
            });
        }
        if (this.singleItemOpen) {
            /* If "single-item-open" is true, and more than one accordion has the "open" property,
            show only the first accordion open.*/
            let numberOfOpenAccordions = 0;
            this.accordions.forEach((accordion) => {
                if (accordion.getAttribute("status") ===
                    "open") {
                    numberOfOpenAccordions++;
                }
            });
            if (numberOfOpenAccordions > 1) {
                let firstOpenAccordionFound = false;
                this.accordions.forEach((accordion) => {
                    if (accordion.getAttribute("status") === "open") {
                        if (firstOpenAccordionFound) {
                            accordion.setAttribute("status", "closed");
                        }
                        else {
                            firstOpenAccordionFound = true;
                        }
                    }
                });
            }
        }
        this.accordions.forEach((accordion) => {
            accordion.setAttribute("mode", this.mode);
            if (this.noPadding) {
                accordion.setAttribute("no-padding", "");
            }
            if (this.disabled) {
                accordion.setAttribute("disabled", "disabled");
            }
        });
    }
    render() {
        return (h(Host, { style: {
                "max-width": this.maxWidth,
            } },
            h("slot", null)));
    }
    static get is() { return "gxg-accordion"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["accordion.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["accordion.css"]
    }; }
    static get properties() { return {
        "disabled": {
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
                "text": "The presence of this attribute makes all of the accordion-items disabled and not focusable"
            },
            "attribute": "disabled",
            "reflect": false,
            "defaultValue": "false"
        },
        "singleItemOpen": {
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
                "text": "If this attribute is present, only one accordion-item can be open at the same time"
            },
            "attribute": "single-item-open",
            "reflect": false,
            "defaultValue": "false"
        },
        "mode": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "mode",
                "resolved": "\"boxed\" | \"classical\" | \"minimal\" | \"slim\"",
                "references": {
                    "mode": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The accordion flavor"
            },
            "attribute": "mode",
            "reflect": false,
            "defaultValue": "\"classical\""
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
                "text": "The accordion max-width"
            },
            "attribute": "max-width",
            "reflect": false,
            "defaultValue": "\"100%\""
        },
        "noPadding": {
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
                "text": "The presence of this attribues removes the padding (internal spacing) from the accordion items containers. This property only applies for the \"classical\" or \"boxed\" modes."
            },
            "attribute": "no-padding",
            "reflect": false,
            "defaultValue": "false"
        }
    }; }
    static get states() { return {
        "accordions": {}
    }; }
    static get elementRef() { return "el"; }
    static get listeners() { return [{
            "name": "accordionItemClicked",
            "method": "itemClickedHandler",
            "target": undefined,
            "capture": false,
            "passive": false
        }, {
            "name": "accordionItemLoaded",
            "method": "itemLoadedHandler",
            "target": undefined,
            "capture": false,
            "passive": false
        }]; }
}
