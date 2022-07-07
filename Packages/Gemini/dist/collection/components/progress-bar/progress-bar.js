import { Component, Prop, h, Host, Watch, getAssetPath } from "@stencil/core";
export class GxgProgressBar {
    constructor() {
        /**
         * The state of the progress-bar, whether it is disabled or not.
         */
        this.disabled = false;
        /**
         * The progress-bar label
         */
        this.label = "Label";
        /**
         * The progress value (percentage)
         */
        this.value = 0;
        /**
         * The max. width
         */
        this.maxWidth = "100%";
        /**
         * The presence of this attribute removes the sound that plays when the progress-bar finishes
         */
        this.silent = false;
    }
    watchValue() {
        if (this.value === 100 && !this.silent) {
            const audio = new Audio(getAssetPath("./progress-bar-assets/done.mp3"));
            setTimeout(function () {
                audio.play();
            }, 500);
        }
    }
    render() {
        return (h(Host, { style: { maxWidth: this.maxWidth }, role: "progressbar", "aria-valuenow": this.value, "aria-valuemin": "0", "aria-valuemax": "100" },
            h("div", { class: "outer-wrapper" },
                h("label", { class: "label" }, this.label),
                h("span", { class: "outer-bar" },
                    h("span", { class: "inner-bar", style: { width: this.value + "%" } })))));
    }
    static get is() { return "gxg-progress-bar"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["progress-bar.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["progress-bar.css"]
    }; }
    static get assetsDirs() { return ["progress-bar-assets"]; }
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
                "text": "The state of the progress-bar, whether it is disabled or not."
            },
            "attribute": "disabled",
            "reflect": true,
            "defaultValue": "false"
        },
        "label": {
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
                "text": "The progress-bar label"
            },
            "attribute": "label",
            "reflect": false,
            "defaultValue": "\"Label\""
        },
        "value": {
            "type": "number",
            "mutable": false,
            "complexType": {
                "original": "number",
                "resolved": "number",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The progress value (percentage)"
            },
            "attribute": "value",
            "reflect": true,
            "defaultValue": "0"
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
                "text": "The max. width"
            },
            "attribute": "max-width",
            "reflect": false,
            "defaultValue": "\"100%\""
        },
        "silent": {
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
                "text": "The presence of this attribute removes the sound that plays when the progress-bar finishes"
            },
            "attribute": "silent",
            "reflect": false,
            "defaultValue": "false"
        }
    }; }
    static get watchers() { return [{
            "propName": "value",
            "methodName": "watchValue"
        }]; }
}
