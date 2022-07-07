import { r as t, h as e, H as r, g as o } from "./p-eb9dc661.js";

import { s as a } from "./p-cdd40237.js";

const i = class {
  constructor(e) {
    t(this, e), 
    /**
        The button group title alignment
        */
    this.titleAlignment = "left", 
    /**
        Wether the button group is disabled or not
        */
    this.disabled = !1, 
    /**
         * The presence of this attribute makes the component full-width
         */
    this.fullWidth = !1, 
    /**
         * The presence of this attribute makes the button group outlined
         */
    this.outlined = !1, 
    /**
        The value of the current selected button
        */
    this.value = "", 
    /**
         * Reading direction
         */
    this.rtl = !1;
  }
  componentDidLoad() {
    //Reading Direction
    const t = document.getElementsByTagName("html")[0].getAttribute("dir"), e = document.getElementsByTagName("body")[0].getAttribute("dir");
    "rtl" !== t && "rtl" !== e || (this.rtl = !0);
  }
  /*********************************
    METHODS
    *********************************/  componentWillLoad() {
    //if disabled, set buttons tabindex equal to "-1"
    if (this.disabled || this.setInitialActiveValue(), this.disabled && (this.value = null), 
    this.disabled) {
      const t = this.el.children;
      Array.from(t).forEach((function(t) {
        t.setAttribute("tab-index", "-1"), t.style.pointerEvents = "none";
      }));
    }
  }
  setActiveButton(t) {
    const e = this.el.children;
    Array.from(e).forEach((function(t) {
      t.removeAttribute("data-active"), t.setAttribute("aria-pressed", "false");
    })), t.target.setAttribute("data-active", ""), t.target.setAttribute("aria-pressed", "true"), 
    this.value = t.target.value;
  }
  setInitialActiveValue() {
    let t = "";
    //get id of all buttons into array
        const e = this.el.children, r = [];
    Array.from(e).forEach(t => {
      const e = t;
      e.setAttribute("tabindex", "0"), "" !== e.getAttribute("id") && r.push(e.getAttribute("id"));
    }), 
    //if defaultSelectedBtnId is equal to any id of the array, set that button as active
    void 0 !== this.defaultSelectedBtnId && "" !== this.defaultSelectedBtnId.replace(/\s/g, "") && r.includes(this.defaultSelectedBtnId) ? r.includes(this.defaultSelectedBtnId) && Array.from(e).forEach(e => {
      const r = e;
      r.id == this.defaultSelectedBtnId && (
      //set the value to the active button value
      t = this.defaultSelectedBtnId = r.getAttribute("value"), r.setAttribute("data-active", ""), 
      r.setAttribute("aria-pressed", "true"));
    }) : (
    // defaultSelectedBtnId is not part of any button id
    //By default, set the first button as the active button
    this.el.children[0].setAttribute("data-active", ""), t = this.el.children[0].getAttribute("value"), 
    this.el.children[0].setAttribute("aria-pressed", "true")), this.value = t;
  }
  detectFocus(t) {
    if (this.el === document.activeElement) {
      const e = this.el.children, r = e.length;
      9 === t.keyCode && t.shiftKey ? 
      //set focus to the last button
      e.item(r).focus() : 9 === t.keyCode && 
      //set focus to the first button
      e.item(0).focus();
    }
  }
  tabIndex() {
    return this.disabled ? "-1" : "0";
  }
  render() {
    let t = null;
    return void 0 !== this.buttonGroupTitle && (t = e("header", {
      class: "button-group-header"
    }, e("label", {
      class: "button-group-header-title"
    }, this.buttonGroupTitle))), e(r, {
      tabindex: this.tabIndex(),
      role: "group",
      "aria-label": this.buttonGroupTitle,
      class: {
        "button-group": !0,
        rtl: this.rtl,
        large: a.large
      },
      value: this.value,
      "title-alignment": this.titleAlignment,
      onKeyUp: this.detectFocus.bind(this)
    }, t, e("div", {
      class: "button-group-container",
      onClick: this.setActiveButton.bind(this)
    }, this.disabled ? e("div", {
      class: "disabled-layer"
    }) : null, e("slot", null)));
  }
  get el() {
    return o(this);
  }
};

i.style = "/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host(.button-group){display:inline-block;line-height:0;outline:none}:host(.button-group) .button-group-header-title{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;display:-ms-flexbox;display:flex;margin-bottom:var(--spacing-lay-xs)}:host(.button-group) .button-group-container{font-size:0;position:relative}:host(.button-group) ::slotted(button){font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em;background:var(--color-primary-enabled);border:0;-webkit-box-shadow:inset 0px 0px 0px 1px transparent;box-shadow:inset 0px 0px 0px 1px transparent;border-radius:var(--border-radius-xs);display:-ms-inline-flexbox;display:inline-flex;-ms-flex-align:center;align-items:center;-ms-flex-pack:center;justify-content:center;padding-top:2.5px;padding-right:var(--spacing-comp-03);padding-bottom:2.5px;padding-left:var(--spacing-comp-03);line-height:var(--line-height-regular);-webkit-transition:background var(--ui-animaton-speed) ease, color var(--ui-animaton-speed) ease, -webkit-box-shadow var(--ui-animaton-speed) ease;transition:background var(--ui-animaton-speed) ease, color var(--ui-animaton-speed) ease, -webkit-box-shadow var(--ui-animaton-speed) ease;transition:background var(--ui-animaton-speed) ease, color var(--ui-animaton-speed) ease, box-shadow var(--ui-animaton-speed) ease;transition:background var(--ui-animaton-speed) ease, color var(--ui-animaton-speed) ease, box-shadow var(--ui-animaton-speed) ease, -webkit-box-shadow var(--ui-animaton-speed) ease;color:var(--color-on-primary);border-radius:0;background-color:var(--gray-01);color:var(--color-primary);border-right:1px solid var(--gray-02);padding-left:var(--spacing-comp-03);padding-right:var(--spacing-comp-03);height:20px}:host(.button-group) ::slotted(button:first-child){border-top-left-radius:var(--border-radius-md);border-bottom-left-radius:var(--border-radius-md)}:host(.button-group) ::slotted(button:last-child){border-top-right-radius:var(--border-radius-md);border-bottom-right-radius:var(--border-radius-md)}:host(.button-group) ::slotted(button:hover){cursor:pointer;color:var(--color-on-primary);background:var(--color-primary-hover)}:host(.button-group) ::slotted(button:focus){outline:none}:host(.button-group) ::slotted(button[data-active]){color:var(--color-on-primary);background-color:var(--color-primary-active);border-right-color:var(--color-primary-active)}:host(.button-group) .disabled-layer{position:absolute;width:100%;height:100%;cursor:not-allowed;z-index:10}:host(.button-group:not([disabled])) ::slotted(button:focus){outline:none;-webkit-box-shadow:var(--box-shadow-values) var(--color-primary-active);box-shadow:var(--box-shadow-values) var(--color-primary-active)}:host(.rtl) ::slotted(button:last-child){border-top-left-radius:var(--border-radius-md);border-bottom-left-radius:var(--border-radius-md);border-top-right-radius:0;border-bottom-right-radius:0}:host(.rtl) ::slotted(button:first-child){border-top-right-radius:var(--border-radius-md);border-bottom-right-radius:var(--border-radius-md);border-top-left-radius:0;border-bottom-left-radius:0}:host(.rtl) ::slotted(button){border-left:1px solid var(--gray-02);border-right:0}:host(.rtl) ::slotted(button:last-child){border-left:0}:host(.button-group:not([outlined])) ::slotted(button:last-child){border-right:none}:host([outlined]) ::slotted(button){background:transparent;border-width:var(--border-width-sm);border-style:var(--border-style-regular);border-color:var(--color-primary-active)}:host([outlined]) ::slotted(button:not(:last-child)){border-right:0}:host(.button-group:not([outlined])) ::slotted(button:last-child){border-right:none}:host([outlined][disabled]) ::slotted(button){border-color:var(--color-primary-disabled);color:var(--color-primary-disabled);background-color:transparent}:host([title-alignment=center]){}:host([title-alignment=center]) .button-group-header-title{-ms-flex-pack:center;justify-content:center}:host([title-alignment=right]){}:host([title-alignment=right]) .button-group-header-title{-ms-flex-pack:end;justify-content:flex-end}:host([disabled]) ::slotted(button),:host([disabled]) ::slotted(button[data-active]){background-color:var(--color-primary-disabled);color:var(--color-on-disabled)}:host([disabled]) ::slotted(button){border-right-color:var(--color-on-disabled)}:host([full-width]){display:block;width:100%}:host([full-width]) .button-group-container{display:-ms-flexbox;display:flex}:host([full-width]) .button-group-container ::slotted(*){-ms-flex:1 1 auto !important;flex:1 1 auto !important}:host(.button-group.large) .button-group-header-title{font-size:var(--font-size-lg)}:host(.button-group.large) ::slotted(button){height:var(--spacing-comp-05);font-size:var(--font-size-lg) !important}";

export { i as gxg_button_group }