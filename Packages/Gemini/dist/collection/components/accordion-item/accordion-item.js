import { Component, Prop, h, Host, Element, Event, State, } from "@stencil/core";
export class GxgAccordionItem {
    constructor() {
        /**
         * The presence of this attribute makes the accordion-item disabled and not focusable
         */
        this.disabled = false;
        /**
         * The presence of this attribute makes the accordion title editable
         */
        this.editableTitle = false;
        /**
         * The accordion flavor (No need to set this attribute on each of the the accordion-item's, only once at gxg-accordion)
         */
        this.mode = "classical";
        /**
         * The accordion subtitle (optional)
         */
        this.itemSubtitle = null;
        /**
         * The accordion title icon
         */
        this.titleIcon = null;
        /**
         * Set the status to "open" if you want the accordion-item open by default
         */
        this.status = "closed";
        /*
         *If this accordion is nested into an accordion-item
         */
        this.nestedAccordion = false;
        /*
         *accordion min-height
         */
        this.minHeight = null;
        /*
         *accordion mode
         */
        this.accordionMode = null;
        /*
         * Has slotted meta
         */
        this.hasSlottedMeta = null;
    }
    itemClickedHandler(e) {
        if (e.detail != 0) {
            this.accordionItemClicked.emit(this.itemId);
        }
    }
    titleClickedHandler() {
        this.accordionTitleClicked.emit("title clicked");
    }
    printIcon() {
        let iType;
        let iColor;
        if (this.status === "open" && !this.disabled) {
            iType = "navigation/chevron-up";
            if (this.mode === "classical") {
                iColor = "alwaysblack";
            }
            else if (this.mode === "boxed" || this.mode === "minimal") {
                iColor = "onbackground";
            }
            else {
                iColor = "negative";
            }
        }
        else {
            //item closed
            iType = "navigation/chevron-down";
            if (this.mode === "classical") {
                if (this.disabled) {
                    iColor = "ondisabled";
                }
                else {
                    //item not disabled
                    iColor = "alwaysblack";
                }
            }
            else if (this.mode === "boxed" || this.mode === "minimal") {
                if (this.disabled) {
                    iColor = "disabled";
                }
                else {
                    iColor = "onbackground";
                }
            }
            else {
                //item alternate
                iColor = "negative";
            }
        }
        return (h("gxg-icon", { slot: "icon", size: "regular", type: iType, color: iColor }));
    }
    componentWillLoad() {
        if (!this.itemId) {
            console.warn("gxg-accordion-item 'itemId' property is mandatory.");
        }
        this.accordionItemLoaded.emit(this.itemId);
        const slottedMeta = this.el.querySelector("[slot=meta]");
        if (slottedMeta !== null) {
            this.hasSlottedMeta = true;
        }
    }
    componentDidLoad() {
        //Get accordion mode
        this.accordionMode = this.el.parentElement.getAttribute("mode");
        //Detect if this accordion-item has an accordion in it
        const accordion = this.el.querySelector("gxg-accordion");
        if (accordion !== null) {
            this.nestedAccordion = true;
        }
        //Detect click on input ".outer-wrapper"
        const gxgFormText = this.el.shadowRoot.querySelector("gxg-form-text");
        if (gxgFormText !== null) {
            const outerWrapper = gxgFormText.shadowRoot.querySelector(".outer-wrapper");
            outerWrapper.addEventListener("click", function (e) {
                if (!e.path[0].classList.contains("input")) {
                    this.status = "closed";
                }
            }.bind(this));
        }
    }
    ariaExpanded() {
        if (this.status === "closed") {
            return "false";
        }
        else {
            return "true";
        }
    }
    ariaDisabled() {
        if (this.disabled) {
            return "true";
        }
        else {
            return "false";
        }
    }
    gxgFormTextClickedHandler(e) {
        e.stopPropagation();
    }
    changedTitleHandler(event) {
        if (this.editableTitle) {
            this.itemTitle = event.detail;
            this.titleChanged.emit(this.itemTitle);
        }
    }
    keyDownHandler(e) {
        if (e.code === "Enter") {
            this.accordionItemClicked.emit(this.itemId);
        }
    }
    itemSubtitleTrimmed(subtitle) {
        if (subtitle.length > 50) {
            return subtitle;
        }
        else {
            return "";
        }
    }
    render() {
        return (h(Host, { class: {
                "nested-acordion": this.nestedAccordion,
                "has-subtitle": this.itemSubtitle !== null,
            } },
            //disabled layer prevents interacting with the component and enables to use "not-allowed" cursor
            this.disabled ? h("div", { class: "disabled-layer" }) : null,
            h("div", { class: {
                    item: true,
                    "item--disabled": this.disabled === true,
                } },
                h("header", { class: "item__header" },
                    h("div", { class: "item__header__button", id: "accordion-" + this.itemId, onClick: this.itemClickedHandler.bind(this), tabindex: !this.disabled ? 0 : -1, "aria-expanded": this.ariaExpanded(), "aria-controls": this.itemId, "aria-disabled": this.ariaDisabled(), onKeyDown: this.keyDownHandler.bind(this) },
                        this.editableTitle &&
                            (this.mode === "classical" || this.mode === "boxed") ? (
                        //div.cover prevents the editable-title to be edited when the accordion-item is closed
                        h("div", { class: {
                                cover: true,
                                hidden: this.status === "open",
                            } })) : null,
                        h("div", { class: "item__header__button__title-subtitle" },
                            h("h1", { class: "item__header__button__title-subtitle__title", onClick: this.titleClickedHandler.bind(this) },
                                this.titleIcon !== null &&
                                    (this.accordionMode === "classical" ||
                                        this.accordionMode === "boxed") ? (h("div", { class: "item__header__button__title-subtitle__title__icon" },
                                    h("gxg-icon", { size: "regular", type: this.titleIcon, color: this.disabled ? "ondisabled" : "auto" }))) : null,
                                this.editableTitle &&
                                    (this.mode === "classical" || this.mode === "boxed") ? (
                                //editable title should only be available for "classical" or "boxed" modes
                                h("gxg-form-text", { onChange: (event) => this.changedTitleHandler(event), minimal: true, "over-dark-background": true
                                        ? this.disabled && this.mode === "classical"
                                        : false, value: this.itemTitle, onClick: this.gxgFormTextClickedHandler.bind(this), "text-style": "title-02", class: "input" })) : (this.itemTitle)),
                            (this.mode === "classical" || this.mode === "boxed") &&
                                this.itemSubtitle !== null ? (h("h2", { class: "item__header__button__title-subtitle__subtitle", title: this.itemSubtitleTrimmed(this.itemSubtitle) }, this.itemSubtitle.length > 50
                                ? this.itemSubtitle.slice(0, 50) + "..."
                                : this.itemSubtitle)) : null),
                        h("div", { class: "item__header__button__meta-icon-wrapper" },
                            this.hasSlottedMeta &&
                                (this.mode === "classical" || this.mode === "boxed") ? (h("div", { class: "item__header__button__meta-icon-wrapper__meta" },
                                h("slot", { name: "meta" }))) : null,
                            h("div", { class: "item__header__button__meta-icon-wrapper__icon" }, this.printIcon())))),
                this.status === "open" && !this.disabled ? (h("main", { class: "item__container", id: this.itemId, role: "region", "aria-labelledby": "accordion-" + this.itemId },
                    h("slot", null))) : (""))));
    }
    static get is() { return "gxg-accordion-item"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["accordion-item.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["accordion-item.css"]
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
                "text": "The presence of this attribute makes the accordion-item disabled and not focusable"
            },
            "attribute": "disabled",
            "reflect": true,
            "defaultValue": "false"
        },
        "editableTitle": {
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
                "text": "The presence of this attribute makes the accordion title editable"
            },
            "attribute": "editable-title",
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
                        "location": "import",
                        "path": "../accordion/accordion"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The accordion flavor (No need to set this attribute on each of the the accordion-item's, only once at gxg-accordion)"
            },
            "attribute": "mode",
            "reflect": true,
            "defaultValue": "\"classical\""
        },
        "itemId": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "string",
                "resolved": "string",
                "references": {}
            },
            "required": true,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The accordion id"
            },
            "attribute": "item-id",
            "reflect": false
        },
        "itemTitle": {
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
                "text": "The accordion title"
            },
            "attribute": "item-title",
            "reflect": false
        },
        "itemSubtitle": {
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
                "text": "The accordion subtitle (optional)"
            },
            "attribute": "item-subtitle",
            "reflect": false,
            "defaultValue": "null"
        },
        "titleIcon": {
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
                "text": "The accordion title icon"
            },
            "attribute": "title-icon",
            "reflect": false,
            "defaultValue": "null"
        },
        "status": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "status",
                "resolved": "\"closed\" | \"open\"",
                "references": {
                    "status": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "Set the status to \"open\" if you want the accordion-item open by default"
            },
            "attribute": "status",
            "reflect": true,
            "defaultValue": "\"closed\""
        }
    }; }
    static get states() { return {
        "nestedAccordion": {},
        "minHeight": {},
        "accordionMode": {},
        "hasSlottedMeta": {}
    }; }
    static get events() { return [{
            "method": "accordionItemClicked",
            "name": "accordionItemClicked",
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
            "method": "accordionItemLoaded",
            "name": "accordionItemLoaded",
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
            "method": "accordionTitleClicked",
            "name": "accordionTitleClicked",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": "Subscribe to this event to know when the \"title\" was clicked"
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }, {
            "method": "titleChanged",
            "name": "titleChanged",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": "If \"editable-title\" attribute is present, this event emmits the title value when it has changed"
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }]; }
    static get elementRef() { return "el"; }
}
