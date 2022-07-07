import { r as registerInstance, h, e as getAssetPath, H as Host, g as getElement } from './index-09b1517f.js';

const alertCss = "/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{position:fixed;bottom:0;z-index:100;-webkit-transition-property:-webkit-transform;transition-property:-webkit-transform;transition-property:transform;transition-property:transform, -webkit-transform;-webkit-transition-duration:var(--ui-animaton-speed);transition-duration:var(--ui-animaton-speed);-webkit-transition-timing-function:ease;transition-timing-function:ease}:host .alert-message{background-color:var(--gray-07);color:var(--color-on-primary);border-width:0;border-top-width:var(--border-width-lg);border-style:solid;border-color:var(--gray-06);padding:var(--spacing-comp-03);display:-ms-flexbox;display:flex;-ms-flex-align:start;align-items:flex-start;-ms-flex-pack:justify;justify-content:space-between;font-family:var(--font-family-primary);}:host .alert-message--error{border-color:var(--color-error-dark);background-color:var(--color-error-light);color:var(--color-on-secondary)}:host .alert-message--warning{border-color:var(--color-warning-dark);background-color:var(--color-warning-light);color:var(--color-on-secondary)}:host .alert-message--success{border-color:var(--color-success-dark);background-color:var(--color-success-light);color:var(--color-on-secondary)}:host .alert-message__close{display:-ms-flexbox;display:flex}:host .alert-message--title{margin:0 0 var(--spacing-comp-02) 0;font-size:var(--font-size-xs);text-transform:uppercase}:host .alert-message--icon{-webkit-margin-end:var(--spacing-comp-02);margin-inline-end:var(--spacing-comp-02);display:-ms-flexbox;display:flex}:host .alert-message--container{display:-ms-flexbox;display:flex;-ms-flex-align:top;align-items:top}:host .alert-message--description{font-size:var(--font-size-md);margin-top:0;margin-bottom:0;line-height:1.5em}:host([position=end]:not(.rtl)){left:auto !important}:host(:not([active])){-webkit-transform:translateY(100%) !important;transform:translateY(100%) !important}:host([position=center]){left:50% !important;-webkit-transform:translateX(-50%);transform:translateX(-50%)}:host(:not([active])[position=center]){-webkit-transform:translateY(100%) translateX(-50%) !important;transform:translateY(100%) translateX(-50%) !important}:host([position=center].rtl){right:50% !important;-webkit-transform:translateX(50%);transform:translateX(50%)}:host(:not([active])[position=center].rtl){-webkit-transform:translateY(100%) translateX(50%) !important;transform:translateY(100%) translateX(50%) !important}:host([position=end].rtl){right:auto !important}";

const GxgAlert = class {
    constructor(hostRef) {
        registerInstance(this, hostRef);
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
            } }, h("div", { class: {
                "alert-message": true,
                "alert-message--notice": this.type === "notice",
                "alert-message--error": this.type === "error",
                "alert-message--warning": this.type === "warning",
                "alert-message--success": this.type === "success",
            } }, h("div", { class: "alert-message--container" }, h("div", { class: "alert-message--icon" }, h("gxg-icon", { color: this.iconColor(), slot: "icon", type: "gemini-tools/" + this.type })), h("div", { class: "alert-message-title-description" }, this.printTitle(), h("p", { class: "alert-message--description" }, h("slot", null)))), h("div", { class: "alert-message-close" }, this.type === "notice" ? (h("gxg-button", { type: "tertiary", icon: "gemini-tools/close", onClick: this.setAlertInactive.bind(this), negative: true })) : (h("gxg-button", { type: "tertiary", icon: "gemini-tools/close", onClick: this.setAlertInactive.bind(this), "always-black": true }))))));
    }
    static get assetsDirs() { return ["alert-assets"]; }
    get el() { return getElement(this); }
    static get watchers() { return {
        "active": ["validateName"]
    }; }
};
GxgAlert.style = alertCss;

export { GxgAlert as gxg_alert };
