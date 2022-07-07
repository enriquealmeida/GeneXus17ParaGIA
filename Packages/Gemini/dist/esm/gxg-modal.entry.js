import { r as registerInstance, e as getAssetPath, h, H as Host, g as getElement } from './index-09b1517f.js';

const modalCss = "/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host .modal{display:-ms-flexbox;display:flex;-ms-flex-direction:column;flex-direction:column;opacity:0;background:var(--color-on-primary);-webkit-box-shadow:var(--box-shadow-03);box-shadow:var(--box-shadow-03);border-radius:var(--border-radius-md);position:absolute;left:50%;top:50%;margin-top:30px;-webkit-transform:translateY(-50%) translateX(-50%);transform:translateY(-50%) translateX(-50%);font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}:host .modal--visible{opacity:1;margin-top:0}:host .modal--transition{-webkit-transition-property:opacity, margin-top;transition-property:opacity, margin-top;-webkit-transition-duration:var(--ui-animaton-speed);transition-duration:var(--ui-animaton-speed);-webkit-transition-timing-function:ease;transition-timing-function:ease}:host .modal__header{display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;-ms-flex-pack:justify;justify-content:space-between}:host .modal__header__title{font-weight:var(--font-weight-bold)}:host .modal__container{padding:var(--spacing-comp-04) 0}:host .modal__footer{display:-ms-flexbox;display:flex}:host .modal__footer ::slotted([slot=footer]) *{margin-left:var(--spacing-comp-02);margin-right:var(--spacing-comp-02)}:host([padding=\"0\"]) .modal{padding:0}:host([padding=xs]) .modal{padding:var(--spacing-comp-01)}:host([padding=s]) .modal{padding:var(--spacing-comp-02)}:host([padding=m]) .modal{padding:var(--spacing-comp-03)}:host([padding=l]) .modal{padding:var(--spacing-comp-04)}:host([padding=xl]) .modal{padding:var(--spacing-comp-05)}:host([padding=xxl]) .modal{padding:var(--spacing-comp-06)}:host([padding=xxxl]) .modal{padding:var(--spacing-comp-07)}:host .layer{position:absolute;width:100%;height:100%;top:0;left:0;background-color:black;opacity:0;-webkit-transition-property:opacity;transition-property:opacity;-webkit-transition-duration:var(--ui-animaton-speed);transition-duration:var(--ui-animaton-speed);-webkit-transition-timing-function:ease;transition-timing-function:ease}:host .layer--visible{opacity:0.25}:host(.footer-justify-end) .modal__footer{-ms-flex-pack:end;justify-content:flex-end}:host(.footer-justify-space-between) .modal__footer{-ms-flex-pack:justify;justify-content:space-between}";

const GxgModal = class {
    constructor(hostRef) {
        registerInstance(this, hostRef);
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
            } }, h("div", { class: {
                modal: true,
                "modal--visible": this.modalVisible,
                "modal--transition": this.modalTransition,
            }, style: { width: this.width, "z-index": this.zIndex + 1 } }, h("header", { class: "modal__header" }, h("span", { class: "modal__header__title" }, this.modalTitle), h("gxg-button", { icon: "gemini-tools/close", type: "secondary-icon-only", onClick: this.closeModal.bind(this) })), h("div", { class: "modal__container" }, h("slot", null)), h("footer", { class: "modal__footer" }, h("slot", { name: "footer" }))), h("div", { class: {
                layer: true,
                "layer--visible": this.layerVisible,
            }, style: { "z-index": this.zIndex } }))) : null;
    }
    static get assetsDirs() { return ["modal-assets"]; }
    get el() { return getElement(this); }
    static get watchers() { return {
        "visible": ["watchVisibleHandler"]
    }; }
};
GxgModal.style = modalCss;

export { GxgModal as gxg_modal };
