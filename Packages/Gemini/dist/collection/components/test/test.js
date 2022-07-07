import { Component, h, Host } from "@stencil/core";
import state from "../store";
export class GxgTest {
    render() {
        return (h(Host, { class: {
                large: state.large,
            } }, "hola"));
    }
    static get is() { return "gxg-test"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["test.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["test.css"]
    }; }
}
