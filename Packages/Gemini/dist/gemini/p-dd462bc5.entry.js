import { r, c as i, h as e, H as n, g as a } from "./p-eb9dc661.js";

const c = class {
  constructor(e) {
    r(this, e), this.breadcrumbClicked = i(this, "breadcrumbClicked", 7);
  }
  breadcrumbClickedFunc() {
    const r = parseInt(this.el.getAttribute("data-index"), 10);
    this.breadcrumbClicked.emit({
      index: r
    });
  }
  render() {
    return e(n, null, e("div", {
      class: {
        container: !0
      },
      onClick: () => this.breadcrumbClickedFunc()
    }, void 0 !== this.icon ? e("gxg-icon", {
      class: {
        "custom-icon": !0
      },
      type: this.icon,
      size: "regular",
      color: "auto"
    }) : null, e("slot", null)), e("gxg-icon", {
      class: {
        "arrow-icon": !0
      },
      type: "navigation/chevron-right"
    }));
  }
  get el() {
    return a(this);
  }
};

c.style = ":host{display:-ms-inline-flexbox;display:inline-flex;-ms-flex-align:center;align-items:center}:host .container{display:-ms-flexbox;display:flex;font-family:var(--font-family-primary);font-size:var(--font-size-sm);background-color:var(--gray-01);height:var(--spacing-comp-05);border-radius:var(--border-radius-lg);color:var(--color-on-secondary);-ms-flex-align:center;align-items:center;-ms-flex-pack:center;justify-content:center;padding-left:var(--spacing-comp-02);padding-right:var(--spacing-comp-02);cursor:pointer}:host .container:hover{background-color:var(--gray-02)}:host .container .custom-icon{margin-right:var(--spacing-comp-01)}:host(:last-child) .arrow-icon{display:none}";

export { c as gxg_breadcrumb }