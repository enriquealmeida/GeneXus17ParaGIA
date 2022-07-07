'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

const index = require('./index-1115561c.js');

const breadcrumbsCss = ":host{display:-ms-flexbox;display:flex}";

const GxgBreadcrumbs = class {
    constructor(hostRef) {
        index.registerInstance(this, hostRef);
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
        return (index.h(index.Host, null, index.h("slot", null)));
    }
    get el() { return index.getElement(this); }
};
GxgBreadcrumbs.style = breadcrumbsCss;

exports.gxg_breadcrumbs = GxgBreadcrumbs;
