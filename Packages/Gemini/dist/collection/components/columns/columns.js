import { Component, Element, Prop, h, Host } from "@stencil/core";
// import { padding } from "../column/column";
export class GxgColumns {
    constructor() {
        /**
         * The vertical alignment
         */
        this.alignY = "top";
        // /*The padding to be applied to the child column components*/
        // @Prop({ reflect: true }) padding: padding = "xs";
        /**
         * The spacing between columns
         */
        this.space = "0";
    }
    componentDidLoad() {
        // const columns = this.el.querySelectorAll("gxg-column");
        // columns.forEach(column => {
        //   column.setAttribute("padding", this.padding);
        // });
    }
    render() {
        return (h(Host, { class: "columns" },
            h("div", { class: "columns-container" },
                h("slot", null))));
    }
    static get is() { return "gxg-columns"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["columns.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["columns.css"]
    }; }
    static get properties() { return {
        "alignY": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "AlignY",
                "resolved": "\"bottom\" | \"center\" | \"top\"",
                "references": {
                    "AlignY": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The vertical alignment"
            },
            "attribute": "align-y",
            "reflect": true,
            "defaultValue": "\"top\""
        },
        "collapseBellow": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "CollapseBellow",
                "resolved": "\"lg\"",
                "references": {
                    "CollapseBellow": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The collapse breakpoint"
            },
            "attribute": "collapse-bellow",
            "reflect": true
        },
        "space": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "Space",
                "resolved": "\"0\" | \"l\" | \"m\" | \"s\" | \"xl\" | \"xs\"",
                "references": {
                    "Space": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The spacing between columns"
            },
            "attribute": "space",
            "reflect": true,
            "defaultValue": "\"0\""
        }
    }; }
    static get elementRef() { return "el"; }
}
