import { Component, Element, Prop, h, Host, State, Watch, getAssetPath, } from "@stencil/core";
export class GxgModal {
    constructor() {
        /*The accordion padding (internal spacing)*/
        this.padding = "s";
        /**
         * The footer justify content type
         */
        this.footerJustifyContent = "flex-end";
        /**
         * The modal width
         */
        this.width = "300px";
        /**
         * Wether the modal is visible or not
         */
        this.visible = false;
        /**
         * The z-index value of the modal
         */
        this.zIndex = "100";
        /**
         * The presence of this attribute removes the sound that plays when the modal appears
         */
        this.silent = false;
        this.layerVisible = false;
        this.modalVisible = false;
        this.modalTransition = false;
    }
    componentDidLoad() {
        this.el.style.display = "block";
        this.el.style.zIndex = "-1";
    }
    closeModal() {
        this.layerVisible = false;
        this.modalVisible = false;
        setTimeout(function () {
            this.visible = false;
            this.modalTransition = false;
        }.bind(this), 250);
    }
    watchVisibleHandler() {
        if (this.visible === true) {
            if (!this.silent) {
                const audio = new Audio(getAssetPath("./modal-assets/prompt.mp3"));
                setTimeout(function () {
                    audio.play();
                }, 100);
            }
            setTimeout(function () {
                this.modalTransition = true;
                setTimeout(function () {
                    this.layerVisible = true;
                    this.modalVisible = true;
                }.bind(this), 50);
            }.bind(this), 50);
        }
    }
    modalHidden() {
        if (this.visible) {
            return "false";
        }
        else {
            return "true";
        }
    }
    render() {
        return this.visible === true ? (h(Host, { role: "dialog", "aria-hidden": this.modalHidden(), class: {
                "footer-justify-end": this.footerJustifyContent === "flex-end",
                "footer-justify-space-between": this.footerJustifyContent === "space-between",
            } },
            h("div", { class: {
                    modal: true,
                    "modal--visible": this.modalVisible,
                    "modal--transition": this.modalTransition,
                }, style: { width: this.width, "z-index": this.zIndex + 1 } },
                h("header", { class: "modal__header" },
                    h("span", { class: "modal__header__title" }, this.modalTitle),
                    h("gxg-button", { icon: "gemini-tools/close", type: "secondary-icon-only", onClick: this.closeModal.bind(this) })),
                h("div", { class: "modal__container" },
                    h("slot", null)),
                h("footer", { class: "modal__footer" },
                    h("slot", { name: "footer" }))),
            h("div", { class: {
                    layer: true,
                    "layer--visible": this.layerVisible,
                }, style: { "z-index": this.zIndex } }))) : null;
    }
    static get is() { return "gxg-modal"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["modal.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["modal.css"]
    }; }
    static get assetsDirs() { return ["modal-assets"]; }
    static get properties() { return {
        "padding": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "padding",
                "resolved": "\"0\" | \"l\" | \"m\" | \"s\" | \"xl\" | \"xs\" | \"xxl\" | \"xxxl\"",
                "references": {
                    "padding": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": ""
            },
            "attribute": "padding",
            "reflect": true,
            "defaultValue": "\"s\""
        },
        "footerJustifyContent": {
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
                "text": "The footer justify content type"
            },
            "attribute": "footer-justify-content",
            "reflect": false,
            "defaultValue": "\"flex-end\""
        },
        "modalTitle": {
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
                "text": "The modal title"
            },
            "attribute": "modal-title",
            "reflect": false
        },
        "width": {
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
                "text": "The modal width"
            },
            "attribute": "width",
            "reflect": false,
            "defaultValue": "\"300px\""
        },
        "visible": {
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
                "text": "Wether the modal is visible or not"
            },
            "attribute": "visible",
            "reflect": false,
            "defaultValue": "false"
        },
        "zIndex": {
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
                "text": "The z-index value of the modal"
            },
            "attribute": "z-index",
            "reflect": false,
            "defaultValue": "\"100\""
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
                "text": "The presence of this attribute removes the sound that plays when the modal appears"
            },
            "attribute": "silent",
            "reflect": false,
            "defaultValue": "false"
        }
    }; }
    static get states() { return {
        "layerVisible": {},
        "modalVisible": {},
        "modalTransition": {}
    }; }
    static get elementRef() { return "el"; }
    static get watchers() { return [{
            "propName": "visible",
            "methodName": "watchVisibleHandler"
        }]; }
}
