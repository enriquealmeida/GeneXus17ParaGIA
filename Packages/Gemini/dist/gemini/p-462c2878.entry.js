import { r as t, c as o, h as e, H as r, g as i } from "./p-eb9dc661.js";

const n = class {
  constructor(e) {
    t(this, e), this.opened = o(this, "opened", 7), this.closed = o(this, "closed", 7), 
    /**
         * the dropdown width
         */
    this.width = "240px", 
    /**
         * the dropdown max. height
         */
    this.maxHeight = "120px", 
    /**
         * the dropdown label (optional)
         */
    this.label = "", 
    /**
         * the dropdown icon (optional)
         */
    this.icon = "", 
    /**
         * Displays the dropdown content
         */
    this.showContent = !1, this.initialButtonText = "", this.detectClickOutsideDropDownVar = this.detectClickOutsideDropDown.bind(this);
  }
  componentWillLoad() {
    null === this.el.querySelector("[slot=button]") && (this.initialButtonText = "Select item");
  }
  toggleContent() {
    !0 === this.showContent ? (this.showContent = !1, document.removeEventListener("click", this.detectClickOutsideDropDownVar)) : (this.showContent = !0, 
    setTimeout(function() {
      document.addEventListener("click", this.detectClickOutsideDropDownVar);
    }.bind(this), 100));
  }
  detectClickOutsideDropDown(t) {
    const o = this.el.shadowRoot.querySelector(".content-container"), e = t.x, r = t.y, i = o.getBoundingClientRect();
    e > i.left && e < i.right && r > i.top && r < i.bottom || (this.showContent = !1, 
    document.removeEventListener("click", this.detectClickOutsideDropDownVar));
  }
  componentDidUnload() {
    document.removeEventListener("click", this.detectClickOutsideDropDown);
  }
  watchHandler(t) {
    console.log("newValue", t), !0 === t ? (console.log("emmit opened"), this.opened.emit(!0)) : (console.log("emmit closed"), 
    this.closed.emit(!0));
  }
  render() {
    return e(r, null, e("div", {
      class: {
        "main-container": !0
      },
      style: {
        width: this.width
      }
    }, "" !== this.label ? e("label", {
      class: {
        label: !0
      }
    }, this.label) : null, e("div", {
      class: {
        "select-container": !0,
        "nothing-selected": "Select item" === this.initialButtonText,
        focus: this.showContent
      },
      onClick: () => this.toggleContent()
    }, "" !== this.icon ? e("gxg-icon", {
      class: "icon",
      type: this.icon,
      color: "auto",
      size: "small"
    }) : null, "" !== this.initialButtonText ? this.initialButtonText : null, e("slot", {
      name: "button"
    }), e("span", {
      class: "layer"
    })), this.showContent ? e("div", {
      class: {
        "content-container": !0
      },
      style: {
        maxHeight: this.maxHeight
      }
    }, e("slot", null)) : null));
  }
  get el() {
    return i(this);
  }
  static get watchers() {
    return {
      showContent: [ "watchHandler" ]
    };
  }
};

n.style = '/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{display:block;}:host ::-webkit-scrollbar{width:6px}:host ::-webkit-scrollbar-track{background-color:var(--gray-02);border-radius:10px}:host ::-webkit-scrollbar-thumb{background:var(--gray-05);border-radius:10px}:host ::-webkit-scrollbar-thumb:hover{background:var(--gray-04);cursor:pointer}:host .gxg-scroll{display:block;overflow-y:auto;padding-right:2px}:host .main-container{background-color:var(--color-background);position:relative}:host .label{font-family:var(--font-family-primary);font-size:var(--font-size-sm);color:var(--color-on-background);margin-bottom:var(--spacing-comp-01);display:inline-block}:host .select-container{border-width:var(--border-width-sm);border-style:solid;border-color:var(--gray-02);height:18px;font-family:var(--font-family-primary);font-size:var(--font-size-sm);display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;padding:0 var(--spacing-comp-01);position:relative;cursor:pointer;color:var(--color-on-background);}:host .select-container .icon{margin-left:-4px}:host .select-container.nothing-selected{color:var(--gray-04)}:host .select-container.focus{outline-style:solid;outline-color:var(--color-primary-active);outline-width:var(--border-width-md);outline-offset:-2px}:host .select-container:after,:host .select-container:before{position:absolute;content:"";right:6px;width:0;height:0;border:4px solid transparent;border-color:var(--gray-06) transparent transparent transparent}:host .select-container:after{top:11px}:host .select-container:before{top:0;-webkit-transform:rotate(180deg);transform:rotate(180deg)}:host .select-container:hover:after,:host .select-container:hover:before{border-color:var(--gray-04) transparent transparent transparent}:host .arrow-down-icon{position:absolute;right:0;pointer-events:none}:host .content-container{border-width:var(--border-width-sm);border-style:solid;border-color:var(--gray-02);border-top:0;padding:var(--spacing-comp-01);font-family:var(--font-family-primary);font-size:var(--font-size-sm);border-bottom-left-radius:var(--border-radius-sm);border-bottom-right-radius:var(--border-radius-sm);color:var(--color-on-background);overflow-y:auto;position:absolute;width:100%;-webkit-box-sizing:border-box;box-sizing:border-box;z-index:99;background-color:var(--color-background)}';

export { n as gxg_drop_down }