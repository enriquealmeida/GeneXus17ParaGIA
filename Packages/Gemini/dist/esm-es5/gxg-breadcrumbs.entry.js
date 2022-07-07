import { r as registerInstance, h, H as Host, g as getElement } from './index-09b1517f.js';
var breadcrumbsCss = ":host{display:-ms-flexbox;display:flex}";
var GxgBreadcrumbs = /** @class */ (function () {
    function GxgBreadcrumbs(hostRef) {
        registerInstance(this, hostRef);
    }
    GxgBreadcrumbs.prototype.componentWillLoad = function () {
        this.setIndexToBreadcrumb();
    };
    GxgBreadcrumbs.prototype.setIndexToBreadcrumb = function () {
        var breadcrumbs = this.el.querySelectorAll("gxg-breadcrumb");
        breadcrumbs.forEach(function (breadcrumb, index) {
            breadcrumb.setAttribute("data-index", index.toString());
        });
    };
    GxgBreadcrumbs.prototype.render = function () {
        return (h(Host, null, h("slot", null)));
    };
    Object.defineProperty(GxgBreadcrumbs.prototype, "el", {
        get: function () { return getElement(this); },
        enumerable: false,
        configurable: true
    });
    return GxgBreadcrumbs;
}());
GxgBreadcrumbs.style = breadcrumbsCss;
export { GxgBreadcrumbs as gxg_breadcrumbs };
