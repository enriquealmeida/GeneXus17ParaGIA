import { r as e, h as t, H as r, g as s } from "./p-eb9dc661.js";

const l = class {
  constructor(t) {
    e(this, t);
  }
  componentWillLoad() {
    this.setIndexToBreadcrumb();
  }
  setIndexToBreadcrumb() {
    this.el.querySelectorAll("gxg-breadcrumb").forEach((e, t) => {
      e.setAttribute("data-index", t.toString());
    });
  }
  render() {
    return t(r, null, t("slot", null));
  }
  get el() {
    return s(this);
  }
};

l.style = ":host{display:-ms-flexbox;display:flex}";

export { l as gxg_breadcrumbs }