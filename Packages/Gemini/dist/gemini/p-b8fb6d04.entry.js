import { r as o, c as t, h as e, H as i, g as s } from "./p-eb9dc661.js";

const n = class {
  constructor(e) {
    o(this, e), this.itemClicked = t(this, "itemClicked", 7), 
    /**
         * Any icon that belongs to Gemini icon library: https://gx-gemini.netlify.app/?path=/story/icons
         */
    this.icon = void 0, 
    /**
         * The item value. This is what the filter with search for. If value is not provided, the filter will search by the item innerHTML.
         */
    this.value = void 0, 
    /**
         * (This prop is for internal use).
         */
    this.iconColor = "auto";
  }
  itemClickedFunc() {
    const o = this.el.getAttribute("index"), t = this.el.getAttribute("icon");
    let e = this.value;
    void 0 === e && (e = this.el.innerHTML), this.itemClicked.emit({
      index: parseInt(o, 10),
      value: e.toString(),
      icon: t
    });
  }
  onKeyDown(o) {
    if (o.preventDefault(), "ArrowDown" === o.code) {
      const o = this.el.nextElementSibling;
      null !== o && o.focus();
    } else if ("ArrowUp" === o.code) {
      const o = this.el.previousElementSibling;
      null !== o && o.focus();
    }
    "Enter" === o.code && this.itemClickedFunc();
  }
  onMouseOver() {
    this.iconColor = "negative";
  }
  onMouseOut() {
    this.el.classList.contains("selected") || (this.iconColor = "auto");
  }
  render() {
    return e(i, {
      onClick: this.itemClickedFunc.bind(this),
      onKeyDown: this.onKeyDown.bind(this),
      onMouseOver: this.onMouseOver.bind(this),
      onMouseOut: this.onMouseOut.bind(this)
    }, e("div", {
      class: "content"
    }, void 0 !== this.icon ? e("gxg-icon", {
      color: this.iconColor,
      size: "small",
      type: this.icon
    }) : null, e("slot", null)));
  }
  get el() {
    return s(this);
  }
};

n.style = ":host{display:block;cursor:pointer;height:var(--spacing-comp-05);display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;font-family:var(--font-family-primary);font-size:var(--font-size-sm);background-color:var(--color-background);color:var(--color-on-background);padding:0 var(--spacing-comp-01);-webkit-transition:height 0.25s, opacity 0.25s;transition:height 0.25s, opacity 0.25s;opacity:1;overflow:hidden}:host .content{display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center}:host(.hidden){height:0;opacity:0}:host(.display-none){display:none}:host(:hover),:host(.selected:hover){background-color:var(--color-secondary-hover);color:var(--color-always-white)}:host(.selected){background-color:var(--color-secondary-active);color:var(--color-always-white)}:host(.exact-match){background-color:var(--color-success-light);color:var(--color-success-dark)}:host(:focus){outline-width:var(--border-width-md);outline-color:var(--color-primary-active);outline-style:solid;outline-offset:-2px}";

export { n as gxg_combo_item }