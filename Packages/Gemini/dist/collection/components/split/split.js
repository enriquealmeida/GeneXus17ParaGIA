import { Component, Host, h } from "@stencil/core";
export class GxgSplit {
    render() {
        return (h(Host, null,
            h("slot", null)));
    }
    static get is() { return "gxg-split"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["split.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["split.css"]
    }; }
}
