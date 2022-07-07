'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

const index = require('./index-1115561c.js');

const iconContent = new Map();
const requests = new Map();
function getSvgContent(url) {
    // see if we already have a request for this url
    let request = requests.get(url);
    if (!request) {
        // we don't already have a request
        request = fetch(url).then((response) => {
            if (response.ok) {
                return response.text().then((svgContent) => {
                    iconContent.set(url, svgContent);
                    return svgContent;
                });
            }
            iconContent.set(url, "");
        });
        // cache for the same requests
        requests.set(url, request);
    }
    return request;
}

const iconCss = ":host(:not([auto-color])){}:host(:not([auto-color])) svg *{fill:var(--icon-color) !important}:host(:not([auto-color])) path.icons01{fill:var(--icons01-enabled)}:host(:not([auto-color])) path.icons02{fill:var(--icons02-enabled)}:host(:not([auto-color])) path.icons03{fill:var(--icons03-enabled)}:host(:not([auto-color])) path.icons04{fill:var(--icons04-enabled)}:host(:not([auto-color])) path.icons05{fill:var(--icons05-enabled)}:host(:not([auto-color])) path.icons06{fill:var(--icons06-enabled)}:host(:not([auto-color])) path.icons07{fill:var(--icons07-enabled)}:host(:not([auto-color])) path.icons08{fill:var(--icons08-enabled)}:host(:not([auto-color])) path.icons09{fill:var(--icons09-enabled)}:host{display:-ms-inline-flexbox;display:inline-flex;line-height:0}:host svg{width:var(--icon-size);height:var(--icon-size)}";

const ChIcon = class {
    constructor(hostRef) {
        index.registerInstance(this, hostRef);
        /*********************************
        PROPERTIES & STATE
        *********************************/
        /**
         * If enabled, the icon will be loaded lazily when it's visible in the viewport.
         */
        this.lazy = false;
        /**
         * If enabled, the icon will display its inherent/natural color
         */
        this.autoColor = false;
        /**
         * The URL of the icon.
         */
        this.src = "";
        this.isVisible = false;
    }
    /*********************************
    METHODS
    *********************************/
    connectedCallback() {
        // purposely do not return the promise here because loading
        // the svg file should not hold up loading the app
        // only load the svg if it's visible
        this.waitUntilVisible(this.element, "50px", () => {
            this.isVisible = true;
            this.getIcon();
        });
    }
    disconnectedCallback() {
        if (this.io !== undefined) {
            this.io.disconnect();
            this.io = undefined;
        }
    }
    waitUntilVisible(el, rootMargin, callback) {
        if (this.lazy &&
            typeof window !== "undefined" &&
            window.IntersectionObserver) {
            const io = (this.io = new window.IntersectionObserver((data) => {
                if (data[0].isIntersecting) {
                    io.disconnect();
                    this.io = undefined;
                    callback();
                }
            }, { rootMargin }));
            io.observe(el);
        }
        else {
            // browser doesn't support IntersectionObserver
            // so just fallback to always show it
            callback();
        }
    }
    async getIcon() {
        if (this.isVisible) {
            if (this.src) {
                if (iconContent.has(this.src)) {
                    this.svgContent = iconContent.get(this.src);
                }
                else {
                    this.svgContent = await getSvgContent(this.src);
                }
            }
            else {
                this.svgContent = "";
                return;
            }
        }
    }
    render() {
        return index.h("div", { innerHTML: this.svgContent });
    }
    static get assetsDirs() { return ["ch-icon-assets"]; }
    get element() { return index.getElement(this); }
    static get watchers() { return {
        "src": ["getIcon"]
    }; }
};
ChIcon.style = iconCss;

const iconCss$1 = "/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{display:inline-block;line-height:0;height:20px;width:20px;display:-ms-inline-flexbox;display:inline-flex;-ms-flex-align:center;align-items:center;-ms-flex-pack:center;justify-content:center}";

/*********************************
CONSTANTS
*********************************/
const COLOR_MAPPINGS = {
    alwaysblack: "color-always-black",
    disabled: "color-primary-disabled",
    ondisabled: "color-on-disabled",
    error: "color-error-dark",
    negative: "color-on-primary",
    onbackground: "color-on-background",
    "primary-enabled": "color-primary-enabled",
    "primary-active": "color-primary-active",
    "primary-hover": "color-primary-hover",
    success: "color-success-dark",
    warning: "color-warning-dark",
};
const GxgIcon = class {
    constructor(hostRef) {
        index.registerInstance(this, hostRef);
        /**
         * The size of the icon. Possible values: regular, small.
         */
        this.size = "regular";
    }
    /*********************************
    METHODS
    *********************************/
    getSrcPath() {
        return index.getAssetPath(`icon-assets/${this.type}.svg`);
    }
    iconSize() {
        if (this.size === "regular") {
            return "16px";
        }
        else if (this.size === "small") {
            return "12px";
        }
    }
    render() {
        return (index.h(index.Host, null, index.h("ch-icon", { style: {
                "--icon-color": this.mapColorToCssVar(COLOR_MAPPINGS[this.color]),
                "--icon-size": this.iconSize(),
            }, "auto-color": this.color === "auto", src: this.getSrcPath() })));
    }
    mapColorToCssVar(color) {
        if (color) {
            return `var(--${color})`;
        }
        else {
            //default color
            return `var(--color-on-background)`;
        }
    }
    static get assetsDirs() { return ["icon-assets"]; }
    get element() { return index.getElement(this); }
};
GxgIcon.style = iconCss$1;

exports.ch_icon = ChIcon;
exports.gxg_icon = GxgIcon;
