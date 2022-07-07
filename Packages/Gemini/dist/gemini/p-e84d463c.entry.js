import { r as e, c as t, h as s, H as i, g as c } from "./p-eb9dc661.js";

const o = class {
  constructor(s) {
    e(this, s), this.selectionChanged = t(this, "selectionChanged", 7), 
    /**
         * The listbox title that appears on the header
         */
    this.theTitle = "", 
    /**
         * The listbox width
         */
    this.width = "280px", 
    /**
         * The prescence of this attribute will display a checkbox for every item
         */
    this.checkboxes = !1;
  }
  componentWillLoad() {
    //Set checkboxes
    if (this.checkboxes) {
      this.el.querySelectorAll("gxg-listbox-item").forEach(e => {
        const t = document.createElement("gxg-form-checkbox");
        t.setAttribute("slot", "checkbox"), t.setAttribute("tabindex", "-1"), e.prepend(t);
      });
    }
    //Set index and Tabindex
        this.el.querySelectorAll("gxg-listbox-item").forEach((e, t) => {
      e.setAttribute("index", t.toString()), 
      //tabindex
      e.setAttribute("tabindex", "0");
    });
  }
  itemClickedHandler(e) {
    if (!e.detail.crtlKey && !e.detail.cmdKey && !this.checkboxes) {
      const e = this.el.querySelectorAll(".selected");
      e.length > 0 && e.forEach(e => {
        e.classList.remove("selected"), 
        //set icon color to auto
        e.iconColor = "auto";
        //set checkbox checked to false
        const t = e.querySelector("gxg-form-checkbox");
        null !== t && (t.checked = !1);
      });
    }
    const t = this.el.querySelector("[index='" + e.detail.index + "']"), s = t.querySelector("gxg-form-checkbox");
    //checkbox
        t.classList.contains("selected") ? (t.classList.remove("selected"), 
    //set icon color to auto
    e.detail.mouseClicked || (t.iconColor = "auto", null !== s && (s.checked = !1))) : (t.classList.add("selected"), 
    //set icon color to negative
    t.iconColor = "negative", null !== s && (s.checked = !0)), this.emmitSelectedItems();
  }
  checkboxClickedHandler(e) {
    const t = this.el.querySelector("[index='" + e.detail.index + "']");
    t.classList.contains("selected") ? t.classList.remove("selected") : t.classList.add("selected"), 
    this.emmitSelectedItems();
  }
  emmitSelectedItems() {
    const e = this.el.querySelectorAll(".selected"), t = [];
    e.forEach(e => {
      const s = e.getAttribute("index");
      let i = e.getAttribute("value");
      i = null === i ? e.textContent : i.toString();
      const c = {
        index: s,
        value: i
      };
      t.push(c);
    }), this.selectionChanged.emit(Object.assign({}, t));
  }
  render() {
    return s(i, null, s("div", {
      style: {
        width: this.width
      },
      class: {
        container: !0
      }
    }, s("header", {
      class: {
        header: !0
      }
    }, this.theTitle), s("main", {
      class: {
        main: !0
      }
    }, s("slot", null))));
  }
  get el() {
    return c(this);
  }
};

o.style = ":host{display:block}:host .header{height:var(--spacing-comp-06);background-color:var(--gray-02);display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;padding:0 var(--spacing-comp-02);font-family:var(--font-family-primary);text-transform:uppercase;font-size:var(--font-size-sm);font-weight:var(--font-weight-bold);color:var(--color-on-background)}:host .main{border-style:var(--border-style-regular);border-width:var(--border-radius-xs);border-color:var(--gray-02)}:host .icon{-webkit-margin-end:var(--spacing-comp-01);margin-inline-end:var(--spacing-comp-01)}:host .checkbox{-webkit-margin-end:var(--spacing-comp-01);margin-inline-end:var(--spacing-comp-01)}:host.selected{background-color:var(--color-secondary-active);color:var(--color-always-white)}";

export { o as gxg_listbox }