import { Component, Host, h, Element } from "@stencil/core";
export class GxgBreadcrumbs {
    componentWillLoad() {
        this.setIndexToBreadcrumb();
    }
    setIndexToBreadcrumb() {
        const breadcrumbs = this.el.querySelectorAll("gxg-breadcrumb");
        breadcrumbs.forEach((breadcrumb, index) => {
            breadcrumb.setAttribute("data-index", index.toString());
        });
    }
    render() {
        return (h(Host, null,
            h("slot", null)));
    }
    static get is() { return "gxg-breadcrumbs"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["breadcrumbs.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["breadcrumbs.css"]
    }; }
    static get elementRef() { return "el"; }
}
