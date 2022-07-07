import { r as registerInstance, c as createEvent, h, H as Host, g as getElement } from './index-09b1517f.js';
var listboxItemCss = ":host{display:block;font-family:var(--font-family-primary);font-size:var(--font-size-sm);height:var(--spacing-comp-05);padding:0 var(--spacing-comp-02);color:var(--color-on-background);cursor:pointer;display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center}:host .icon{-webkit-margin-end:var(--spacing-comp-01);margin-inline-end:var(--spacing-comp-01)}:host ::slotted(gxg-form-checkbox){-webkit-margin-end:var(--spacing-comp-01);margin-inline-end:var(--spacing-comp-01)}:host(:hover),:host(.selected:hover){background-color:var(--color-secondary-hover);color:var(--color-always-white)}:host(.has-icon.no-checkbox){-webkit-padding-start:var(--spacing-comp-01);padding-inline-start:var(--spacing-comp-01)}:host(.selected){background-color:var(--color-secondary-active);color:var(--color-always-white)}:host(:focus){outline-width:var(--border-width-md);outline-color:var(--color-primary-active);outline-style:solid;outline-offset:-2px}";
var GxgListboxItem = /** @class */ (function () {
    function GxgListboxItem(hostRef) {
        registerInstance(this, hostRef);
        this.itemClicked = createEvent(this, "itemClicked", 7);
        this.checkboxClicked = createEvent(this, "checkboxClicked", 7);
        /**
         * Any icon that belongs to Gemini icon library: https://gx-gemini.netlify.app/?path=/story/icons
         */
        this.icon = undefined;
        /**
         * The item value. If value is not provided, the value will be the item innerHTML.
         */
        this.value = undefined;
        /**
         * (This prop is for internal use).
         */
        this.iconColor = "auto";
        this.checkboxes = false;
        this.itemHasFocus = false;
    }
    GxgListboxItem.prototype.componentWillLoad = function () {
        var _this = this;
        var listbox = this.el.parentElement;
        var listboxCheckboxes = listbox.getAttribute("checkboxes");
        if (listboxCheckboxes !== null) {
            this.checkboxes = true;
            var checkbox = this.el.querySelector("gxg-form-checkbox");
            checkbox.addEventListener("click", function () {
                var index = _this.el.getAttribute("index");
                _this.checkboxClicked.emit({
                    index: parseInt(index, 10),
                });
            });
        }
    };
    GxgListboxItem.prototype.itemClickedFunc = function (e) {
        if (!this.checkboxes) {
            var index = this.el.getAttribute("index");
            this.itemClicked.emit({
                index: parseInt(index, 10),
                crtlKey: e.ctrlKey,
                cmdKey: e.metaKey,
                mouseClicked: true,
            });
        }
    };
    GxgListboxItem.prototype.onKeyDown = function (e) {
        e.preventDefault();
        if (e.code === "ArrowDown") {
            var nextItem = this.el.nextElementSibling;
            if (nextItem !== null) {
                nextItem.focus();
            }
        }
        else if (e.code === "ArrowUp") {
            var prevItem = this.el.previousElementSibling;
            if (prevItem !== null) {
                prevItem.focus();
            }
        }
        if (e.code === "Enter") {
            var index = this.el.getAttribute("index");
            this.itemClicked.emit({
                index: parseInt(index, 10),
                crtlKey: e.ctrlKey,
                cmdKey: e.metaKey,
                mouseClicked: false,
            });
        }
    };
    GxgListboxItem.prototype.onMouseOver = function () {
        this.iconColor = "negative";
    };
    GxgListboxItem.prototype.onMouseOut = function () {
        var itemIsSelected = this.el.classList.contains("selected");
        if (!itemIsSelected) {
            this.iconColor = "auto";
        }
    };
    GxgListboxItem.prototype.render = function () {
        return (h(Host, { class: {
                "has-icon": this.icon !== undefined,
                "no-checkbox": !this.checkboxes,
            }, onClick: this.itemClickedFunc.bind(this), onKeyDown: this.onKeyDown.bind(this), onMouseOver: this.onMouseOver.bind(this), onMouseOut: this.onMouseOut.bind(this) }, h("slot", { name: "checkbox" }), this.icon !== undefined ? (h("gxg-icon", { class: "icon", color: this.iconColor, size: "regular", type: this.icon })) : null, h("slot", null)));
    };
    Object.defineProperty(GxgListboxItem.prototype, "el", {
        get: function () { return getElement(this); },
        enumerable: false,
        configurable: true
    });
    return GxgListboxItem;
}());
GxgListboxItem.style = listboxItemCss;
export { GxgListboxItem as gxg_listbox_item };
