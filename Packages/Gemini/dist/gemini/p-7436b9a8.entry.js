import { r as t, c as e, h as o, H as i, g as r } from "./p-eb9dc661.js";

const a = class {
  constructor(o) {
    t(this, o), this.clicked = e(this, "clicked", 7), this.deleted = e(this, "deleted", 7), 
    /**
         * The presence of this attribute makes this box active
         */
    this.active = !1, 
    /**
         * The presence of this attribute adds a "delete" button that, when pressed, triggers the "deleted" event
         */
    this.deletable = !1, 
    /**
         * The padding (internal spacing) of the drag-box (Set it on the drag-container to apply the same padding to all of the gxg-drag-box items)
         */
    this.padding = "s";
  }
  clickedHandler() {
    this.clicked.emit(this.el.getAttribute("id"));
  }
  deleteHandler(t) {
    t.stopPropagation(), this.deleted.emit("box was deleted");
  }
  handlerOnKeyDown(t) {
    if (13 == t.keyCode) 
    //enter key was pressed
    this.active = !0, this.clicked.emit(this.el.getAttribute("id")); else if (9 === t.keyCode && t.shiftKey) {
      //tab and shift keys were pressed
      const e = this.el.previousElementSibling;
      t.preventDefault(), e.focus();
    } else if (9 == t.keyCode && !this.active) {
      const e = this.el.nextElementSibling;
      t.preventDefault(), e.focus();
    }
  }
  componentDidLoad() {
    if (void 0 !== this.title) {
      const t = this.el.shadowRoot.querySelector(".container-content__title");
      this.el.prepend(t);
    }
  }
  render() {
    return o(i, {
      tabindex: "0",
      onClick: this.clickedHandler.bind(this),
      onKeyDown: this.handlerOnKeyDown.bind(this)
    }, this.active ? null : o("div", {
      class: "cover"
    }), o("span", {
      class: "border"
    }), o("div", {
      class: "drag-icon-container"
    }, o("gxg-icon", {
      size: "regular",
      type: "navigation/drag"
    })), o("div", {
      class: "container-content"
    }, void 0 !== this.title ? o("span", {
      class: "container-content__title"
    }, this.title) : null, o("slot", null)), o("div", {
      class: "delete-button-container"
    }, this.deletable ? o("gxg-button", {
      "button-styles-editable": !0,
      icon: "gemini-tools/delete",
      onClick: this.deleteHandler.bind(this),
      type: "secondary-icon-only"
    }) : null));
  }
  get el() {
    return r(this);
  }
};

a.style = '/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{position:relative;font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;border-width:var(--border-width-sm);border-style:var(--border-style-regular);border-color:var(--gray-03);display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;background-color:var(--color-background);margin-bottom:var(--spacing-lay-s);cursor:pointer;opacity:1;-webkit-transition-property:opacity;transition-property:opacity;-webkit-transition-duration:var(--ui-animaton-speed);transition-duration:var(--ui-animaton-speed);-webkit-transition-timing-function:ease;transition-timing-function:ease}:host .cover{position:absolute;width:100%;height:100%;left:0;background-color:transparent;z-index:100}:host:before{content:"";display:block;height:4px;width:calc(100% + 2px);background-color:transparent;position:absolute;left:-1px;top:-1px}:host ::slotted(.container-content__title){display:block;font-weight:var(--font-weight-bold)}:host .container-content{width:100%}:host .drag-icon-container{display:-ms-flexbox;display:flex;-ms-flex-item-align:start;align-self:start;padding-top:2px;-webkit-padding-end:var(--spacing-comp-02);padding-inline-end:var(--spacing-comp-02)}:host .drag-icon-container gxg-icon{opacity:0}:host .delete-button-container{display:-ms-flexbox;display:flex;-ms-flex-item-align:start;align-self:flex-start;padding-top:2px}:host .delete-button-container gxg-button::part(native-button){opacity:0}:host .delete-button-container gxg-button::part(native-button):focus{opacity:1}:host(:hover):before{background-color:var(--gray-03)}:host(:hover) .drag-icon-container gxg-icon{opacity:1}:host([padding="0"]) .container-content{padding:0}:host([padding="0"]) .delete-button-container{padding-top:0}:host([padding=xs]){padding:var(--spacing-comp-01)}:host([padding=s]){padding:var(--spacing-comp-02)}:host([padding=m]){padding:var(--spacing-comp-03)}:host([padding=l]){padding:var(--spacing-comp-04)}:host([padding=xl]){padding:var(--spacing-comp-05)}:host([padding=xxl]){padding:var(--spacing-comp-06)}:host([padding=xxxl]){padding:var(--spacing-comp-07)}:host([active]):before{background-color:var(--color-primary-active)}:host([active]) .delete-button-container .delete-button-container gxg-button::part(native-button){opacity:1}:host([active]) .delete-button-container gxg-button::part(native-button){opacity:1}:host(:focus){outline:none;-webkit-box-shadow:inset 0px 0px 0px 1px var(--color-primary-active);box-shadow:inset 0px 0px 0px 1px var(--color-primary-active);border-color:var(--color-primary-active)}';

export { a as gxg_drag_box }