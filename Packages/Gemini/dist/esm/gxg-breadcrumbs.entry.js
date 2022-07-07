import { r as registerInstance, h, H as Host, g as getElement } from './index-09b1517f.js';

const breadcrumbsCss = ":host{display:-ms-flexbox;display:flex}";

const GxgBreadcrumbs = class {
    constructor(hostRef) {
        registerInstance(this, hostRef);
    }
    componentWillLoad() {
        this.setIndexToBreadcrumb();
    }
    setIndexToBreadcrumb() {
        const breadcrumbs = this.el.querySelectorAll("gxg-breadcrumb");
        breadcrumbs.forEach((breadcrumb, index) => {
            breadcrumb.setAttribute("data-index", index.toString());
        });
    }
    render() {
        return (h(Host, null, h("slot", null)));
    }
    get el() { return getElement(this); }
};
GxgBreadcrumbs.style = breadcrumbsCss;

export { GxgBreadcrumbs as gxg_breadcrumbs };
