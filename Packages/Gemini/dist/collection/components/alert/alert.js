import { Component, Element, Host, Prop, h, State, Watch, getAssetPath, } from "@stencil/core";
export class GxgAlert {
    constructor() {
        /*********************************
        PROPERTIES & STATE
        *********************************/
        /**
         * Wether the alert is active (visible) or hidden
         */
        this.active = false;
        /**
         * The amount of time the alert is visible before hidding under the document
         */
        this.activeTime = "regular";
        /**
         * The alert position on the X axis
         */
        this.position = "start";
        /**
         * The alert flavor
         */
        this.type = "notice";
        /**
         * The presence of this attribute makes the component full-width
         */
        this.fullWidth = false;
        /**
         * The spacing between the alert, and the left or right side of the document
         */
        this.leftRight = "xs";
        /**
         * The spacing between the alert and the bottom side of the document
         */
        this.bottom = "xs";
        /**
         * The alert width
         */
        this.width = "350px";
        /**
         * The presence of this attribute removes the sound on the 'warning' or 'error' alert
         */
        this.silent = false;
        /**
         * Reading direction
         */
        this.rtl = false;
    }
    /*********************************
    METHODS
    *********************************/
    componentDidLoad() {
        this.el.removeAttribute("hidden");
        //Reading Direction
        const dirHtml = document
            .getElementsByTagName("html")[0]
            .getAttribute("dir");
        const dirBody = document
            .getElementsByTagName("body")[0]
            .getAttribute("dir");
        if (dirHtml === "rtl" || dirBody === "rtl") {
            this.rtl = true;
        }
    }
    iconColor() {
        if (this.type === "notice")
            return "negative";
        if (this.type === "error")
            return "error";
        if (this.type === "warning")
            return "warning";
        if (this.type === "success")
            return "success";
    }
    closeIconColor() {
        if (this.type === "notice") {
            return "negative";
        }
        else {
            return "onbackground";
        }
    }
    printTitle() {
        if (this.title !== undefined) {
            return h("h2", { class: "alert-message--title" }, this.title);
        }
    }
    setAlertInactive() {
        this.active = false;
        this.el.removeAttribute("role");
    }
    validateName(newValue) {
        //timing
        let timingValue;
        switch (this.activeTime) {
            case "xxslow":
                timingValue = "10";
                break;
            case "xslow":
                timingValue = "09";
                break;
            case "slow":
                timingValue = "08";
                break;
            case "regular":
                timingValue = "07";
                break;
            case "fast":
                timingValue = "06";
                break;
            case "xfast":
                timingValue = "05";
                break;
            case "xxfast":
                timingValue = "04";
                break;
            default:
                timingValue = "04";
        }
        if (newValue === true) {
            this.el.setAttribute("role", "alert");
            setTimeout(() => {
                this.setAlertInactive();
            }, parseInt(getComputedStyle(document.documentElement).getPropertyValue("--timing-" + timingValue)));
        }
    }
    defineWidth() {
        if (this.fullWidth) {
            const lateralSpacingComputedValue = getComputedStyle(document.documentElement).getPropertyValue("--spacing-lay-" + this.leftRight);
            //remove "px" to multiply, since we want the spacing value from left, and from right.
            const lateralSpacingComputedValueInt = parseInt(lateralSpacingComputedValue.replace("px", ""), 10) * 2;
            return "calc(100% - " + lateralSpacingComputedValueInt + "px)";
        }
        else {
            return this.width;
        }
    }
    alertHidden() {
        if (this.active) {
            if (!this.silent) {
                let audio;
                if (this.type === "warning") {
                    audio = new Audio(getAssetPath("./alert-assets/warning.mp3"));
                }
                else if (this.type === "error") {
                    audio = new Audio(getAssetPath("./alert-assets/error.mp3"));
                }
                setTimeout(function () {
                    audio.play();
                }, 100);
            }
            return "false";
        }
        else {
            return "true";
        }
    }
    transform(bottomSpacingValue) {
        if (this.position === "center") {
            if (this.rtl) {
                return "translateY(-" + bottomSpacingValue + ") translateX(50%)";
            }
            else {
                return "translateY(-" + bottomSpacingValue + ") translateX(-50%)";
            }
        }
        else {
            return "translateY(-" + bottomSpacingValue + ")";
        }
    }
    render() {
        let lateralSpacingValue;
        if (this.leftRight === "no-space") {
            lateralSpacingValue = "0";
        }
        else {
            const bodyComputedStyles = getComputedStyle(document.body);
            lateralSpacingValue = bodyComputedStyles
                .getPropertyValue("--spacing-lay-" + this.leftRight)
                .replace(/\s/g, "");
        }
        let bottomSpacingValue;
        if (this.bottom === "no-space") {
            bottomSpacingValue = "0";
        }
        else {
            const bodyComputedStyles = getComputedStyle(document.body);
            bottomSpacingValue = bodyComputedStyles
                .getPropertyValue("--spacing-lay-" + this.bottom)
                .replace(/\s/g, "");
        }
        return (h(Host, { role: "alert", "aria-hidden": this.alertHidden(), hidden: true, class: { rtl: this.rtl }, style: {
                width: this.defineWidth(),
                left: lateralSpacingValue,
                right: lateralSpacingValue,
                transform: this.transform(bottomSpacingValue),
            } },
            h("div", { class: {
                    "alert-message": true,
                    "alert-message--notice": this.type === "notice",
                    "alert-message--error": this.type === "error",
                    "alert-message--warning": this.type === "warning",
                    "alert-message--success": this.type === "success",
                } },
                h("div", { class: "alert-message--container" },
                    h("div", { class: "alert-message--icon" },
                        h("gxg-icon", { color: this.iconColor(), slot: "icon", type: "gemini-tools/" + this.type })),
                    h("div", { class: "alert-message-title-description" },
                        this.printTitle(),
                        h("p", { class: "alert-message--description" },
                            h("slot", null)))),
                h("div", { class: "alert-message-close" }, this.type === "notice" ? (h("gxg-button", { type: "tertiary", icon: "gemini-tools/close", onClick: this.setAlertInactive.bind(this), negative: true })) : (h("gxg-button", { type: "tertiary", icon: "gemini-tools/close", onClick: this.setAlertInactive.bind(this), "always-black": true }))))));
    }
    static get is() { return "gxg-alert"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["alert.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["alert.css"]
    }; }
    static get assetsDirs() { return ["alert-assets"]; }
    static get properties() { return {
        "active": {
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
                "text": "Wether the alert is active (visible) or hidden"
            },
            "attribute": "active",
            "reflect": true,
            "defaultValue": "false"
        },
        "activeTime": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "ActiveTime",
                "resolved": "\"fast\" | \"regular\" | \"slow\" | \"xfast\" | \"xslow\" | \"xxfast\" | \"xxslow\"",
                "references": {
                    "ActiveTime": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The amount of time the alert is visible before hidding under the document"
            },
            "attribute": "active-time",
            "reflect": false,
            "defaultValue": "\"regular\""
        },
        "position": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "AlertPosition",
                "resolved": "\"center\" | \"end\" | \"start\"",
                "references": {
                    "AlertPosition": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The alert position on the X axis"
            },
            "attribute": "position",
            "reflect": false,
            "defaultValue": "\"start\""
        },
        "title": {
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
                "text": "The alert title (optional)"
            },
            "attribute": "title",
            "reflect": false
        },
        "type": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "AlertType",
                "resolved": "\"error\" | \"notice\" | \"success\" | \"warning\"",
                "references": {
                    "AlertType": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The alert flavor"
            },
            "attribute": "type",
            "reflect": false,
            "defaultValue": "\"notice\""
        },
        "fullWidth": {
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
                "text": "The presence of this attribute makes the component full-width"
            },
            "attribute": "full-width",
            "reflect": false,
            "defaultValue": "false"
        },
        "leftRight": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "Spacing",
                "resolved": "\"l\" | \"m\" | \"no-space\" | \"s\" | \"xl\" | \"xs\"",
                "references": {
                    "Spacing": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The spacing between the alert, and the left or right side of the document"
            },
            "attribute": "left-right",
            "reflect": false,
            "defaultValue": "\"xs\""
        },
        "bottom": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "Spacing",
                "resolved": "\"l\" | \"m\" | \"no-space\" | \"s\" | \"xl\" | \"xs\"",
                "references": {
                    "Spacing": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The spacing between the alert and the bottom side of the document"
            },
            "attribute": "bottom",
            "reflect": false,
            "defaultValue": "\"xs\""
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
                "text": "The alert width"
            },
            "attribute": "width",
            "reflect": false,
            "defaultValue": "\"350px\""
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
                "text": "The presence of this attribute removes the sound on the 'warning' or 'error' alert"
            },
            "attribute": "silent",
            "reflect": false,
            "defaultValue": "false"
        }
    }; }
    static get states() { return {
        "rtl": {}
    }; }
    static get elementRef() { return "el"; }
    static get watchers() { return [{
            "propName": "active",
            "methodName": "validateName"
        }]; }
}
