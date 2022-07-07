import { Component, Host, h, Prop, Event, Element, } from "@stencil/core";
export class GxgBreadcrumb {
    breadcrumbClickedFunc() {
        const index = parseInt(this.el.getAttribute("data-index"), 10);
        this.breadcrumbClicked.emit({
            index: index,
        });
    }
    render() {
        return (h(Host, null,
            h("div", { class: { container: true }, onClick: () => this.breadcrumbClickedFunc() },
                this.icon !== undefined ? (h("gxg-icon", { class: { "custom-icon": true }, type: this.icon, size: "regular", color: "auto" })) : null,
                h("slot", null)),
            h("gxg-icon", { class: { "arrow-icon": true }, type: "navigation/chevron-right" })));
    }
    static get is() { return "gxg-breadcrumb"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["breadcrumb.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["breadcrumb.css"]
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
                "text": "The breadcrumb icon (optional)"
            },
            "attribute": "icon",
            "reflect": false
        }
    }; }
    static get events() { return [{
            "method": "breadcrumbClicked",
            "name": "breadcrumbClicked",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": "This event emmits the breadcrumb index"
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }]; }
    static get elementRef() { return "el"; }
}
