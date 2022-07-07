import { r as registerInstance, c as createEvent, h, H as Host, g as getElement } from './index-09b1517f.js';

const breadcrumbCss = ":host{display:-ms-inline-flexbox;display:inline-flex;-ms-flex-align:center;align-items:center}:host .container{display:-ms-flexbox;display:flex;font-family:var(--font-family-primary);font-size:var(--font-size-sm);background-color:var(--gray-01);height:var(--spacing-comp-05);border-radius:var(--border-radius-lg);color:var(--color-on-secondary);-ms-flex-align:center;align-items:center;-ms-flex-pack:center;justify-content:center;padding-left:var(--spacing-comp-02);padding-right:var(--spacing-comp-02);cursor:pointer}:host .container:hover{background-color:var(--gray-02)}:host .container .custom-icon{margin-right:var(--spacing-comp-01)}:host(:last-child) .arrow-icon{display:none}";

const GxgBreadcrumb = class {
    constructor(hostRef) {
        registerInstance(this, hostRef);
        this.breadcrumbClicked = createEvent(this, "breadcrumbClicked", 7);
    }
    breadcrumbClickedFunc() {
        const index = parseInt(this.el.getAttribute("data-index"), 10);
        this.breadcrumbClicked.emit({
            index: index,
        });
    }
    render() {
        return (h(Host, null, h("div", { class: { container: true }, onClick: () => this.breadcrumbClickedFunc() }, this.icon !== undefined ? (h("gxg-icon", { class: { "custom-icon": true }, type: this.icon, size: "regular", color: "auto" })) : null, h("slot", null)), h("gxg-icon", { class: { "arrow-icon": true }, type: "navigation/chevron-right" })));
    }
    get el() { return getElement(this); }
};
GxgBreadcrumb.style = breadcrumbCss;

export { GxgBreadcrumb as gxg_breadcrumb };
