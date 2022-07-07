import { Component, Element, Host, h, Prop, State, Listen, } from "@stencil/core";
export class GxgFilter {
    constructor() {
        /**
         * The top position of the filter, relative to the closest parent with relative position. (optional)
         */
        this.top = undefined;
        /**
         * The left position of the filter, relative to the closest parent with relative position. (optional)
         */
        this.left = undefined;
    }
    componentWillLoad() {
        this.itemsNodeList = this.el.querySelectorAll("gxg-filter-item");
        //Set position
        if (this.top !== undefined && this.left !== undefined) {
            this.el.style.top = this.top;
            this.el.style.left = this.left;
            this.el.style.position = "absolute";
        }
    }
    onInputGxgformText(e) {
        const noMatchSpan = this.el.shadowRoot.getElementById("no-match");
        const filterValue = e.detail.toLowerCase();
        this.itemsNodeList.forEach((item) => {
            const itemHtmlElement = item;
            const itemContent = itemHtmlElement.innerText.toLowerCase();
            if (!itemContent.includes(filterValue)) {
                itemHtmlElement.classList.add("opacity-zero");
                itemHtmlElement.classList.add("height-zero");
            }
            else {
                itemHtmlElement.classList.remove("height-zero");
                itemHtmlElement.classList.remove("opacity-zero");
            }
            if (itemContent === filterValue) {
                itemHtmlElement.classList.add("exact-match");
            }
            else {
                itemHtmlElement.classList.remove("exact-match");
            }
        });
        const numberOfHiddenItems = this.el.getElementsByClassName("opacity-zero")
            .length;
        if (this.itemsNodeList.length === numberOfHiddenItems) {
            noMatchSpan.classList.remove("height-zero");
            noMatchSpan.classList.remove("opacity-zero");
        }
        else {
            noMatchSpan.classList.add("opacity-zero");
            noMatchSpan.classList.add("height-zero");
        }
    }
    closeFilter() {
        this.el.addEventListener("animationend", () => {
            this.el.remove();
        });
        this.el.classList.add("hide");
    }
    handleItemClickedEvent() {
        //When an item has been clicked, hide the filter
        this.el.addEventListener("animationend", () => {
            this.el.remove();
        });
        this.el.classList.add("hide");
    }
    render() {
        return (h(Host, null,
            h("header", { id: "header" },
                h("div", { class: "search-container" },
                    h("gxg-form-text", { icon: "gemini-tools/search", "icon-position": "start", onInput: this.onInputGxgformText.bind(this) })),
                h("gxg-button", { class: "close-icon", icon: "gemini-tools/close", type: "tertiary", onClick: this.closeFilter.bind(this) })),
            h("main", { id: "main" },
                h("slot", null),
                h("span", { id: "no-match", class: "opacity-zero height-zero" },
                    "No ocurrences found ",
                    h("br", null),
                    h("span", { class: "hint" }, "ctrl + backspace to erase")))));
    }
    static get is() { return "gxg-filter"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["gxg-filter.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["gxg-filter.css"]
    }; }
    static get properties() { return {
        "top": {
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
                "text": "The top position of the filter, relative to the closest parent with relative position. (optional)"
            },
            "attribute": "top",
            "reflect": false,
            "defaultValue": "undefined"
        },
        "left": {
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
                "text": "The left position of the filter, relative to the closest parent with relative position. (optional)"
            },
            "attribute": "left",
            "reflect": false,
            "defaultValue": "undefined"
        }
    }; }
    static get states() { return {
        "itemsNodeList": {}
    }; }
    static get elementRef() { return "el"; }
    static get listeners() { return [{
            "name": "itemClickedEvent",
            "method": "handleItemClickedEvent",
            "target": undefined,
            "capture": false,
            "passive": false
        }]; }
}
