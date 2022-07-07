import { r as e, c as t, h as r, H as o, g as a } from "./p-eb9dc661.js";

import { s as i } from "./p-cdd40237.js";

import { f as l, r as s } from "./p-e8e2654d.js";

const n = class {
  constructor(r) {
    e(this, r), this.change = t(this, "change", 7), 
    /********************************
        PROPERTIES & STATE
        *********************************/
    /**
         * The presence of this attribute disables the component
         */
    this.disabled = !1, 
    /**
         * The presence of this attribute stylizes the component with error attributes
         */
    this.error = !1, 
    /**
         * The input label
         */
    this.labelPosition = "above", 
    /**
         * The presence of this attribute hides the border, and sets the background to transparent when the element has no focus
         */
    this.minimal = !0, 
    /**
         * The presence of this attribute makes this input required
         */
    this.required = !1, 
    /**
         * The presence of this attribute stylizes the component with warning attributes
         */
    this.warning = !1, 
    /**
         * The select max. width
         */
    this.maxWidth = "100%", 
    /**
         * Reading direction
         */
    this.rtl = !1, this.rerender = !1;
  }
  todoCompletedHandler(e) {
    this.value = e.detail;
  }
  /*********************************
    METHODS
    *********************************/  clickTest(e, t, r) {
    let o, a, i;
    const l = e.srcElement, s = l.parentNode.parentNode.getElementsByTagName("select")[0], n = l.parentNode.previousSibling;
    for (a = 0; a < t.length; a++) if (t[a].innerHTML == l.innerHTML) {
      for (r(l.getAttribute("value")), s.selectedIndex = a, n.innerHTML = l.innerHTML, 
      o = l.parentNode.getElementsByClassName("same-as-selected"), i = 0; i < o.length; i++) o[i].removeAttribute("class");
      l.setAttribute("class", "same-as-selected"), l.setAttribute("aria-selected", "true");
      break;
    }
    n.click();
  }
  componentDidLoad() {
    this.el.shadowRoot.querySelectorAll("slot")[0].addEventListener("slotchange", function() {
      this.el.shadowRoot.querySelector(".select-selected").remove(), this.el.shadowRoot.querySelector(".select-items").remove(), 
      this.selectCore();
    }.bind(this));
    //Reading Direction
    const e = document.getElementsByTagName("html")[0].getAttribute("dir"), t = document.getElementsByTagName("body")[0].getAttribute("dir");
    "rtl" !== e && "rtl" !== t || (this.rtl = !0), this.selectCore();
  }
  selectCore() {
    const updateValue = e => {
      this.value = e;
    };
    let e, t, r, o, a;
    const i = this.el.shadowRoot.querySelectorAll(".custom-select");
    //set the selected option as "active"
        for (this.el.shadowRoot.querySelector(".select-items"), e = 0; e < i.length; e++) {
      r = document.createElement("DIV"), r.setAttribute("class", "select-selected"), r.setAttribute("tabindex", "0"), 
      r.setAttribute("role", "listbox"), r.addEventListener("keydown", e => {
        //event.preventDefault();
        const t = this.el.shadowRoot.querySelector(".select-items");
        let r = t.querySelector(".same-as-selected");
        if (13 === e.keyCode ? 
        //enter key was pressed
        t.classList.toggle("select-hide") : 38 === e.keyCode ? 
        //key up pressed
        null !== r && null !== r.previousElementSibling && (r.classList.remove("same-as-selected"), 
        r.previousElementSibling.classList.add("same-as-selected")) : 40 === e.keyCode ? 
        //key down pressed
        null !== r ? null !== r.nextElementSibling && (r.classList.remove("same-as-selected"), 
        r.nextElementSibling.classList.add("same-as-selected")) : (r = t.querySelector("div[role='option']:first-child"), 
        r.classList.add("same-as-selected")) : 27 === e.keyCode && 
        //escape key was pressed
        t.classList.add("select-hide"), null !== r) {
          const e = this.el.shadowRoot.querySelector(".select-items .same-as-selected");
          this.el.shadowRoot.querySelector(".select-selected").textContent = e.textContent;
        }
      });
      const l = this.el.querySelectorAll("gxg-option");
      let s = l[0];
      this.disabled && r.setAttribute("disabled", "disabled");
      for (let e = 0; e < l.length; e++) !0 === l[e].selected && (s = l[e]);
      r.innerHTML = s.innerHTML, this.value = s.value;
      const n = this.el.querySelectorAll("gxg-option");
      for (i[e].appendChild(r), o = document.createElement("DIV"), o.setAttribute("class", "select-items select-hide"), 
      t = 0; t < n.length; t++) {
        a = document.createElement("DIV"), a.setAttribute("role", "option"), a.innerHTML = n[t].innerHTML;
        const e = document.createAttribute("value");
        e.value = n[t].value, a.setAttributeNode(e), a.addEventListener("click", e => this.clickTest(e, n, updateValue)), 
        o.appendChild(a);
      }
      i[e].appendChild(o), r.addEventListener("click", (function(e) {
        e.stopPropagation(), this.nextSibling.classList.toggle("select-hide"), this.classList.toggle("select-arrow-active");
      })), this.el.shadowRoot.querySelector(".select-items div[role='option'][value='" + this.value + "']").classList.add("same-as-selected");
    }
    document.addEventListener("click", function(e) {
      let t;
      const r = [], o = this.el.shadowRoot.querySelectorAll(".select-items"), a = this.el.shadowRoot.querySelectorAll(".select-selected");
      for (t = 0; t < a.length; t++) e == a[t] ? r.push(t) : a[t].classList.remove("select-arrow-active");
      for (t = 0; t < o.length; t++) r.indexOf(t) && o[t].classList.add("select-hide");
    }.bind(this));
  }
  valueHandler(e, t) {
    if (void 0 !== t && this.change.emit(e), void 0 !== t) {
      //update visible value innerHTML
      const t = this.el.querySelector("gxg-option[value='" + e + "']");
      this.el.shadowRoot.querySelector(".select-selected").innerHTML = t.innerHTML;
    }
  }
  handlerOnKeyDown(e) {
    9 == e.keyCode && e.shiftKey && 
    //shift key was also pressed
    this.el.previousSibling.previousSibling.focus();
  }
  render() {
    return r(o, {
      style: {
        maxWidth: this.maxWidth,
        "--size": this.size
      },
      class: {
        rtl: this.rtl,
        large: i.large
      },
      onKeyDown: this.handlerOnKeyDown.bind(this)
    }, r("div", {
      class: "outer-wrapper"
    }, void 0 !== this.label ? r("label", {
      class: {
        label: !0
      }
    }, this.label, s(this)) : "", r("div", {
      class: {
        "custom-select": !0,
        select: !0,
        "select--error": !0 === this.error,
        "select--warning": !0 === this.warning
      },
      ref: e => this.select = e
    }, r("select", {
      id: "original"
    }, r("slot", null)))), l());
  }
  get el() {
    return a(this);
  }
  static get watchers() {
    return {
      value: [ "valueHandler" ]
    };
  }
};

n.style = '/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{--size:3;display:block}:host .outer-wrapper{position:relative;width:100%}:host label{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;display:-ms-flexbox;display:flex;margin-bottom:var(--spacing-lay-xs)}:host label .required{padding-left:2px}:host textarea{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}:host{}:host .custom-select{position:relative;font-family:var(--font-family-primary)}:host .custom-select select{display:none;}:host .select-selected{background-color:var(--color-on-primary);border-width:var(--border-width-sm);border-color:var(--gray-02);border-style:var(--border-style-regular);height:10px}:host .select-selected:focus{outline-style:solid;outline-color:var(--color-primary-active);outline-width:var(--border-width-md);outline-offset:-2px;border-color:transparent}:host .select-selected:after,:host .select-selected:before{position:absolute;content:"";right:6px;width:0;height:0;border:4px solid transparent;border-color:var(--gray-06) transparent transparent transparent}:host .select-selected:after{top:12px}:host .select-selected:before{top:1px;-webkit-transform:rotate(180deg);transform:rotate(180deg)}:host .select-selected:hover:after,:host .select-selected:hover:before{border-color:var(--gray-04) transparent transparent transparent}:host .select-items div,:host .select-selected{color:var(--color-on-background);padding:var(--spacing-comp-01);cursor:pointer;font-size:var(--font-size-sm);line-height:0.95em}:host .select-items div{border-color:transparent transparent rgba(0, 0, 0, 0.1) transparent;border:1px solid transparent}:host .select-items{color:var(--color-on-background);position:absolute;background-color:var(--color-on-primary);top:100%;left:0;right:0;z-index:99;border-width:var(--border-width-sm);border-color:var(--gray-02);border-style:var(--border-style-regular);border-top:0;overflow-y:scroll;border-bottom-right-radius:var(--border-radius-md);border-bottom-left-radius:var(--border-radius-md);max-height:calc( var(--size) * 20px )}:host .select-items div{height:10px}:host .select-hide{display:none}:host .select-items div:hover{background-color:var(--color-secondary-enabled);color:var(--color-on-secondary)}:host .select-items div.same-as-selected{background-color:var(--color-secondary-hover);color:var(--color-on-secondary)}:host([disabled]) .select-selected{pointer-events:none;background-color:var(--gray-01);color:var(--color-on-disabled)}:host([disabled]) .custom-select{cursor:not-allowed}:host(.rtl) .select-selected:after,:host(.rtl) .select-selected:before{left:6px;right:auto}:host([minimal]) .select-selected:not(:focus){border-color:transparent;background-color:transparent}:host{}:host ::-webkit-scrollbar{width:6px}:host ::-webkit-scrollbar-track{background-color:var(--gray-02);border-radius:10px}:host ::-webkit-scrollbar-thumb{background:var(--gray-05);border-radius:10px}:host ::-webkit-scrollbar-thumb:hover{background:var(--gray-04);cursor:pointer}:host .select--error .select-selected{border-color:var(--color-error-dark);background-color:var(--color-error-light);margin-bottom:0;color:var(--color-always-black)}:host .select--warning .select-selected{border-color:var(--color-warning-dark);background-color:var(--color-warning-light);margin-bottom:0;color:var(--color-always-black)}:host([label-position=start]) .outer-wrapper{display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center}:host([label-position=start]) .custom-select{width:100%}:host([label-position=start]) .label{margin-bottom:0;-webkit-margin-end:var(--spacing-comp-02);margin-inline-end:var(--spacing-comp-02);-ms-flex-negative:0;flex-shrink:0}:host(.large) label{font-size:var(--font-size-lg)}:host(.large) .select-selected{height:var(--spacing-comp-05);-webkit-box-sizing:border-box;box-sizing:border-box;display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;font-size:var(--font-size-lg)}:host(.large) .select-selected:before{top:3px}:host(.large) .select-selected:after{top:14px}:host(.large) .select-items{max-height:calc(var(--size) * var(--spacing-comp-05))}:host(.large) .select-items div{height:14px;display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;font-size:var(--font-size-lg)}';

export { n as gxg_select }