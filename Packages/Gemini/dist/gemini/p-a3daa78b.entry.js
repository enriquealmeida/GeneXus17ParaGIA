import { r as e, c as o, h as i, H as t, g as s } from "./p-eb9dc661.js";

const c = class {
  constructor(i) {
    e(this, i), this.itemClicked = o(this, "itemClicked", 7), this.checkboxClicked = o(this, "checkboxClicked", 7), 
    /**
         * Any icon that belongs to Gemini icon library: https://gx-gemini.netlify.app/?path=/story/icons
         */
    this.icon = void 0, 
    /**
         * The item value. If value is not provided, the value will be the item innerHTML.
         */
    this.value = void 0, 
    /**
         * (This prop is for internal use).
         */
    this.iconColor = "auto", this.checkboxes = !1, this.itemHasFocus = !1;
  }
  componentWillLoad() {
    if (null !== this.el.parentElement.getAttribute("checkboxes")) {
      this.checkboxes = !0;
      this.el.querySelector("gxg-form-checkbox").addEventListener("click", () => {
        const e = this.el.getAttribute("index");
        this.checkboxClicked.emit({
          index: parseInt(e, 10)
        });
      });
    }
  }
  itemClickedFunc(e) {
    if (!this.checkboxes) {
      const o = this.el.getAttribute("index");
      this.itemClicked.emit({
        index: parseInt(o, 10),
        crtlKey: e.ctrlKey,
        cmdKey: e.metaKey,
        mouseClicked: !0
      });
    }
  }
  onKeyDown(e) {
    if (e.preventDefault(), "ArrowDown" === e.code) {
      const e = this.el.nextElementSibling;
      null !== e && e.focus();
    } else if ("ArrowUp" === e.code) {
      const e = this.el.previousElementSibling;
      null !== e && e.focus();
    }
    if ("Enter" === e.code) {
      const o = this.el.getAttribute("index");
      this.itemClicked.emit({
        index: parseInt(o, 10),
        crtlKey: e.ctrlKey,
        cmdKey: e.metaKey,
        mouseClicked: !1
      });
    }
  }
  onMouseOver() {
    this.iconColor = "negative";
  }
  onMouseOut() {
    this.el.classList.contains("selected") || (this.iconColor = "auto");
  }
  render() {
    return i(t, {
      class: {
        "has-icon": void 0 !== this.icon,
        "no-checkbox": !this.checkboxes
      },
      onClick: this.itemClickedFunc.bind(this),
      onKeyDown: this.onKeyDown.bind(this),
      onMouseOver: this.onMouseOver.bind(this),
      onMouseOut: this.onMouseOut.bind(this)
    }, i("slot", {
      name: "checkbox"
    }), void 0 !== this.icon ? i("gxg-icon", {
      class: "icon",
      color: this.iconColor,
      size: "regular",
      type: this.icon
    }) : null, i("slot", null));
  }
  get el() {
    return s(this);
  }
};

c.style = ":host{display:block;font-family:var(--font-family-primary);font-size:var(--font-size-sm);height:var(--spacing-comp-05);padding:0 var(--spacing-comp-02);color:var(--color-on-background);cursor:pointer;display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center}:host .icon{-webkit-margin-end:var(--spacing-comp-01);margin-inline-end:var(--spacing-comp-01)}:host ::slotted(gxg-form-checkbox){-webkit-margin-end:var(--spacing-comp-01);margin-inline-end:var(--spacing-comp-01)}:host(:hover),:host(.selected:hover){background-color:var(--color-secondary-hover);color:var(--color-always-white)}:host(.has-icon.no-checkbox){-webkit-padding-start:var(--spacing-comp-01);padding-inline-start:var(--spacing-comp-01)}:host(.selected){background-color:var(--color-secondary-active);color:var(--color-always-white)}:host(:focus){outline-width:var(--border-width-md);outline-color:var(--color-primary-active);outline-style:solid;outline-offset:-2px}";

export { c as gxg_listbox_item }