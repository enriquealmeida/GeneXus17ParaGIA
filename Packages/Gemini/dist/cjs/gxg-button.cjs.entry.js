'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

const index = require('./index-1115561c.js');
const store = require('./store-49c65768.js');

const buttonCss = "/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{display:inline-block;line-height:0;position:relative}:host .ghost-icon{width:0}:host(.button) .button-native{--svg-icon-regular-scale:var(\n    --svg-icon-small-scale\n  );height:20px;position:relative;font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em;background:var(--color-primary-enabled);border:0;-webkit-box-shadow:inset 0px 0px 0px 1px transparent;box-shadow:inset 0px 0px 0px 1px transparent;border-radius:var(--border-radius-xs);display:-ms-inline-flexbox;display:inline-flex;-ms-flex-align:center;align-items:center;-ms-flex-pack:center;justify-content:center;padding-top:2.5px;padding-right:var(--spacing-comp-03);padding-bottom:2.5px;padding-left:var(--spacing-comp-03);line-height:var(--line-height-regular);-webkit-transition:background var(--ui-animaton-speed) ease, color var(--ui-animaton-speed) ease, -webkit-box-shadow var(--ui-animaton-speed) ease;transition:background var(--ui-animaton-speed) ease, color var(--ui-animaton-speed) ease, -webkit-box-shadow var(--ui-animaton-speed) ease;transition:background var(--ui-animaton-speed) ease, color var(--ui-animaton-speed) ease, box-shadow var(--ui-animaton-speed) ease;transition:background var(--ui-animaton-speed) ease, color var(--ui-animaton-speed) ease, box-shadow var(--ui-animaton-speed) ease, -webkit-box-shadow var(--ui-animaton-speed) ease;}:host(.button) .button-native:hover:not([disabled]){background:var(--color-primary-hover);cursor:pointer}:host(.button) .button-native:focus:not([disabled]){background:var(--color-primary-enabled);-webkit-box-shadow:var(--box-shadow-values) var(--color-primary-active);box-shadow:var(--box-shadow-values) var(--color-primary-active);outline:none}:host(.button) .button-native:active:not([disabled]){background:var(--color-primary-active)}:host(.button.button--disabled) .button-native{background:var(--color-primary-disabled);color:var(--color-primary-disabled);pointer-events:none}:host(.button.button--disabled){pointer-events:none}:host(.button--primary-text-only) .button-native{color:var(--color-on-primary);-webkit-padding-start:var(--spacing-comp-04);padding-inline-start:var(--spacing-comp-04);-webkit-padding-end:var(--spacing-comp-04);padding-inline-end:var(--spacing-comp-04)}:host(.button--primary-text-only.button--disabled) .button-native{color:var(--color-on-disabled)}:host(.button--primary-text-icon) .button-native{color:var(--color-on-primary);-webkit-padding-start:var(--spacing-comp-01);padding-inline-start:var(--spacing-comp-01);-webkit-padding-end:var(--spacing-comp-02);padding-inline-end:var(--spacing-comp-02)}:host(.button--primary-text-icon) .button-native gxg-icon:not(.ghost-icon){-webkit-padding-end:var(--spacing-comp-01);padding-inline-end:var(--spacing-comp-01)}:host(.button--primary-text-icon.button--disabled) .button-native{color:var(--color-on-disabled)}:host(.button--primary-icon-only) .button-native{background:var(--color-primary-enabled);color:var(--color-on-primary);width:20px;height:20px;padding-left:0;padding-right:0}:host(.button--primary-icon-only) .button-native:hover:not([disabled]){background:var(--color-primary-hover);color:var(--color-primary-hover)}:host(.button--primary-icon-only) .button-native:focus:not([disabled]){background:var(--color-primary-enabled);color:var(--color-primary-hover)}:host(.button--primary-icon-only) .button-native:active:not([disabled]){background:var(--color-primary-active);color:var(--color-on-primary)}:host(.button--primary-icon-only) .button-native:focus{outline:none}:host(.button--secondary-text-only) .button-native{background:transparent;-webkit-padding-start:var(--spacing-comp-04);padding-inline-start:var(--spacing-comp-04);-webkit-padding-end:var(--spacing-comp-04);padding-inline-end:var(--spacing-comp-04)}:host(.button--secondary-text-only) .button-native:hover:not([disabled]){color:var(--color-primary-hover);background:transparent}:host(.button--secondary-text-only) .button-native:focus:not([disabled]){color:var(--color-primary-active);background:transparent;-webkit-box-shadow:var(--box-shadow-values) var(--color-primary-active);box-shadow:var(--box-shadow-values) var(--color-primary-active)}:host(.button--secondary-text-only) .button-native:active:not([disabled]){color:var(--color-primary-active);-webkit-box-shadow:none;box-shadow:none}:host(.button--secondary-text-only) .button-native:focus{outline:none}:host(.button--secondary-text-only.button--disabled) .button-native{background:transparent;color:var(--color-primary-disabled)}:host(.button--secondary-text-icon) .button-native{background:transparent;-webkit-padding-start:0;padding-inline-start:0;-webkit-padding-start:var(--spacing-comp-01);padding-inline-start:var(--spacing-comp-01);-webkit-padding-end:var(--spacing-comp-02);padding-inline-end:var(--spacing-comp-02)}:host(.button--secondary-text-icon) .button-native gxg-icon:not(.ghost-icon){-webkit-padding-end:var(--spacing-comp-01);padding-inline-end:var(--spacing-comp-01)}:host(.button--secondary-text-icon) .button-native:hover:not([disabled]){color:var(--color-primary-hover);background:transparent}:host(.button--secondary-text-icon) .button-native:focus:not([disabled]){color:var(--color-primary-active);background:transparent;-webkit-box-shadow:var(--box-shadow-values) var(--color-primary-active);box-shadow:var(--box-shadow-values) var(--color-primary-active)}:host(.button--secondary-text-icon) .button-native:active:not([disabled]){color:var(--color-primary-active);-webkit-box-shadow:none;box-shadow:none}:host(.button--secondary-text-icon.button--disabled) .button-native{background:transparent;color:var(--color-primary-disabled)}:host(.button--secondary-text-icon.button--disabled) .button-native gxg-icon{--icon-color:var(--color-primary-disabled)}:host(.button--secondary-icon-only) .button-native{background:transparent;-webkit-padding-end:0;padding-inline-end:0;-webkit-padding-start:0;padding-inline-start:0;width:20px;height:20px}:host(.button--secondary-icon-only) .button-native:hover:not([disabled]){background:transparent}:host(.button--secondary-icon-only) .button-native:focus:not([disabled]){background:transparent;-webkit-box-shadow:var(--box-shadow-values) var(--color-primary-active);box-shadow:var(--box-shadow-values) var(--color-primary-active)}:host(.button--secondary-icon-only) .button-native:active:not([disabled]){background:transparent}:host(.button--secondary-icon-only.button--disabled) .button-native{background:transparent}:host(.button--outlined) .button-native{-webkit-box-shadow:inset 0px 0px 0px 1px var(--color-primary-enabled);box-shadow:inset 0px 0px 0px 1px var(--color-primary-enabled);background:var(--color-background);-webkit-padding-start:var(--spacing-comp-04);padding-inline-start:var(--spacing-comp-04);-webkit-padding-end:var(--spacing-comp-04);padding-inline-end:var(--spacing-comp-04)}:host(.button--outlined) .button-native:hover:not([disabled]){color:var(--color-primary-hover);background:var(--color-primary-hover-opacity-01);-webkit-box-shadow:inset 0px 0px 0px 1px var(--color-primary-hover);box-shadow:inset 0px 0px 0px 1px var(--color-primary-hover)}:host(.button--outlined) .button-native:focus:not([disabled]){color:var(--color-primary-active);background:transparent;-webkit-box-shadow:inset 0px 0px 0px 2px var(--color-primary-active);box-shadow:inset 0px 0px 0px 2px var(--color-primary-active)}:host(.button--outlined) .button-native:active:not([disabled]){color:var(--color-primary-active);background:transparent;-webkit-box-shadow:inset 0px 0px 0px 1px var(--color-primary-active);box-shadow:inset 0px 0px 0px 1px var(--color-primary-active)}:host(.button--outlined.button--disabled) .button-native{-webkit-box-shadow:inset 0px 0px 0px 1px var(--color-primary-disabled);box-shadow:inset 0px 0px 0px 1px var(--color-primary-disabled);background:var(--color-transparent);color:var(--color-primary-disabled)}:host(.button--tertiary) .button-native{background:transparent;-webkit-padding-end:0;padding-inline-end:0;-webkit-padding-start:0;padding-inline-start:0;height:20px;width:20px}:host(.button--tertiary) .button-native:hover:not([disabled]){background:transparent}:host(.button--tertiary) .button-native:hover:not([disabled]) gxg-icon{-webkit-transform:scale(0.75);transform:scale(0.75)}:host(.button--tertiary) .button-native:focus:not([disabled]){background:transparent;-webkit-box-shadow:var(--box-shadow-values) var(--color-primary-active);box-shadow:var(--box-shadow-values) var(--color-primary-active)}:host(.button--tertiary) .button-native:active:not([disabled]){background:transparent}:host(.button--tertiary.button--disabled) .button-native{background:transparent}:host(.button--fullwidth){width:100%}:host(.button--fullwidth) .button-native{width:100%}:host .icon-tooltip{position:absolute;border-width:0.5px;border-style:solid;border-color:var(--gray-04);color:var(--color-on-background);background-color:var(--gray-01);font-size:8px;font-weight:var(--font-weight-regular);font-family:var(--font-family-primary);padding:1px 5px;width:-webkit-max-content;width:-moz-max-content;width:max-content;left:0;top:20px;display:none;opacity:0;-webkit-transition-property:opacity;transition-property:opacity;-webkit-transition-duration:var(--ui-animaton-speed);transition-duration:var(--ui-animaton-speed);-webkit-transition-timing-function:ease;transition-timing-function:ease}:host(.display-tooltip) .icon-tooltip{display:block}:host(.show-tooltip) .icon-tooltip{opacity:1}:host(.large) .button-native{height:var(--spacing-comp-05);font-size:var(--font-size-lg) !important}:host(.button--primary-text-icon.large) .button-native{-webkit-padding-start:var(--spacing-comp-02);padding-inline-start:var(--spacing-comp-02);-webkit-padding-end:var(--spacing-comp-03);padding-inline-end:var(--spacing-comp-03)}:host(.button--primary-icon-only.large) .button-native{width:var(--spacing-comp-05);height:var(--spacing-comp-05)}:host(.button--secondary-text-icon.large) .button-native{-webkit-padding-start:var(--spacing-comp-02);padding-inline-start:var(--spacing-comp-02);-webkit-padding-end:var(--spacing-comp-03);padding-inline-end:var(--spacing-comp-03)}:host(.button--secondary-icon-only.large) .button-native{width:var(--spacing-comp-05);height:var(--spacing-comp-05)}:host(.button--tertiary.large) .button-native{height:var(--spacing-comp-05);width:var(--spacing-comp-05)}";

const GxgButton = class {
    constructor(hostRef) {
        index.registerInstance(this, hostRef);
        /*********************************
        PROPERTIES & STATE
        *********************************/
        /**
         * The prescence of this attribute makes the icon always black
         */
        this.alwaysBlack = false;
        /**
         * The state of the button, whether it is disabled or not
         */
        this.disabled = false;
        /**
         * The presence of this attribute makes the component full-width
         */
        this.fullWidth = false;
        /**
         * The prescence of this attribute turns the icon white
         */
        this.negative = false;
        /**
         * The kind of button
         */
        this.type = "primary-text-only";
        /**
         * The presence of this attribute lets the button styles be editable from outside of the component by referencing the "native-button" part.
         */
        this.buttonStylesEditable = false;
        this.mouseEnter = false;
        this.focusIn = false;
    }
    /*********************************
    METHODS
    *********************************/
    componentDidLoad() {
        // Set aria-label to host
        if (this.type === "primary-icon-only" ||
            this.type === "secondary-icon-only"
        //If button type is icon-only, aria-label must be provided in order to inform the user the button purpose.
        ) {
            if (this.el.querySelector(":scope > [slot='icon']")) {
                //Also, an icon must be provided, in order to know the button purpose.
                //The icon purpose is defined from the icon "type" property.
                const iconAriaLabel = this.el
                    .querySelector(":scope > [slot='icon']")
                    .getAttribute("type");
                this.el.setAttribute("aria-label", iconAriaLabel);
            }
        }
        //Set a part attribute to the button if buttonStylesEditable is true
        if (this.buttonStylesEditable) {
            this.el.shadowRoot
                .querySelector("button")
                .setAttribute("part", "native-button");
        }
    }
    ghostIcon() {
        return (index.h("gxg-icon", { class: "ghost-icon", type: "gemini-tools/empty", size: "regular" }));
        //This is a workaround I found for alligning vertically the buttons that have no icon, with the buttons that do have icons.
    }
    regularIcon() {
        if (this.type !== "primary-text-only" &&
            this.type !== "secondary-text-only" &&
            this.icon !== undefined) {
            return (index.h("gxg-icon", { type: this.icon, color: this.iconColor(), size: this.iconSize() }));
        }
    }
    iconSize() {
        let iSize;
        if (this.type === "secondary-icon-only" || this.type === "tertiary") {
            iSize = "regular";
        }
        else {
            iSize = "small";
        }
        return iSize;
    }
    iconColor() {
        let iColor;
        if (this.type.includes("primary")) {
            iColor = "negative";
            if (this.disabled) {
                iColor = "ondisabled";
            }
        }
        else if (this.type.includes("secondary")) {
            if (this.disabled) {
                iColor = "disabled";
            }
            else {
                if (this.mouseEnter) {
                    iColor = "primary-hover";
                }
                else {
                    if (this.focusIn) {
                        iColor = "primary-active";
                    }
                    else {
                        iColor = "primary-enabled";
                    }
                }
            }
        }
        else if (this.type.includes("tertiary")) {
            if (this.disabled) {
                iColor = "disabled";
            }
            else {
                if (this.alwaysBlack) {
                    iColor = "alwaysblack";
                }
                else if (this.negative) {
                    iColor = "negative";
                }
                else {
                    iColor = "onbackground";
                }
            }
        }
        return iColor;
    }
    clickHandler(e) {
        if (this.disabled) {
            e.preventDefault();
        }
    }
    handleFocus(focusEvent) {
        if (focusEvent.target !== this.el) {
            return;
        }
        this.button.focus();
    }
    onMouseEnter() {
        this.mouseEnter = true;
    }
    onMouseLeave() {
        this.mouseEnter = false;
    }
    onFocusIn() {
        this.focusIn = true;
        this.mouseEnter = false;
    }
    onFocusOut() {
        this.focusIn = false;
    }
    render() {
        return (index.h(index.Host, { role: "button", class: {
                button: true,
                "button--primary-text-only": this.type === "primary-text-only",
                "button--primary-text-icon": this.type === "primary-text-icon",
                "button--primary-icon-only": this.type === "primary-icon-only",
                "button--secondary-text-only": this.type === "secondary-text-only",
                "button--secondary-text-icon": this.type === "secondary-text-icon",
                "button--secondary-icon-only": this.type === "secondary-icon-only",
                "button--outlined": this.type === "outlined",
                "button--disabled": this.disabled === true,
                "button--tertiary": this.type === "tertiary",
                "button--fullwidth": this.fullWidth === true,
                large: store.state.large,
            }, onClick: this.clickHandler.bind(this), onMouseEnter: this.onMouseEnter.bind(this), onMouseLeave: this.onMouseLeave.bind(this), onfocusin: this.onFocusIn.bind(this), onfocusout: this.onFocusOut.bind(this) }, this.disabled ? index.h("div", { class: "disabled-layer" }) : null, index.h("button", { class: "button-native gxg-text-general", disabled: this.disabled === true, ref: (el) => (this.button = el), tabindex: "0" }, this.ghostIcon(), this.regularIcon(), this.type.includes("text") || this.type === "outlined" ? (index.h("span", { class: "text" }, index.h("slot", null))) : null)));
    }
    get el() { return index.getElement(this); }
};
GxgButton.style = buttonCss;

exports.gxg_button = GxgButton;
