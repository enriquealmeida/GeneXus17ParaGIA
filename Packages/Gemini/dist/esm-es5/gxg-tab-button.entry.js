import { r as registerInstance, c as createEvent, h, g as getElement } from './index-09b1517f.js';
var tabButtonCss = ":root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{display:block;-ms-flex-item-align:center !important;align-self:center !important;line-height:0}:host .tab-button{display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;-ms-flex-pack:center;justify-content:center;font-size:var(--font-size-sm);font-weight:var(--font-weight-semibold);font-family:var(--font-family-primary);text-transform:uppercase;color:var(--color-on-background);letter-spacing:var(--letter-spacing-regular);padding-top:0;padding-right:var(--spacing-comp-05);padding-bottom:0;padding-left:var(--spacing-comp-05);margin-top:0;margin-right:0;margin-bottom:0;margin-left:0;border:0;width:100%;min-height:var(--spacing-comp-06);position:relative;-webkit-box-shadow:none;-moz-box-shadow:none;box-shadow:none;-webkit-transition:-webkit-box-shadow 0.2s ease-in-out;transition:-webkit-box-shadow 0.2s ease-in-out;transition:box-shadow 0.2s ease-in-out;transition:box-shadow 0.2s ease-in-out, -webkit-box-shadow 0.2s ease-in-out;background:transparent;-webkit-transition-property:-webkit-box-shadow;transition-property:-webkit-box-shadow;transition-property:box-shadow;transition-property:box-shadow, -webkit-box-shadow;-webkit-transition-duration:var(--ui-animaton-speed);transition-duration:var(--ui-animaton-speed);-webkit-transition-timing-function:ease;transition-timing-function:ease}:host .tab-button:after{content:\"\";width:100%;height:var(--spacing-comp-01);left:0;bottom:0;position:absolute;background-color:transparent;-webkit-transition-property:background-color;transition-property:background-color;-webkit-transition-duration:var(--ui-animaton-speed);transition-duration:var(--ui-animaton-speed);-webkit-transition-timing-function:ease;transition-timing-function:ease}:host .tab-button:hover{cursor:pointer}:host .tab-button:hover:after{background-color:var(--color-primary-hover)}:host .tab-button:active:after{background-color:var(--color-primary-active)}:host .tab-button:focus{outline:none;-webkit-box-shadow:inset 0px 0px 0px 1px var(--color-primary-enabled);-moz-box-shadow:inset 0px 0px 0px 1px var(--color-primary-enabled);box-shadow:inset 0px 0px 0px 1px var(--color-primary-enabled)}:host .tab-button[disabled]{color:var(--color-primary-disabled);pointer-events:none}:host .tab-button--selected:after{background-color:var(--color-primary-active)}:host .tab-button--text-icon gxg-icon{margin-right:var(--spacing-comp-02)}:host .tab-button__text{display:inline-block}:host(.menu-button){width:100%;margin:0 var(--spacing-comp-04);-webkit-transition-property:background-color;transition-property:background-color;-webkit-transition-duration:var(--ui-animaton-speed);transition-duration:var(--ui-animaton-speed);-webkit-transition-timing-function:ease;transition-timing-function:ease}:host(.menu-button) .tab-button:after{display:none}:host(.menu-button:hover){background:var(--color-secondary)}";
var GxgTabButton = /** @class */ (function () {
    function GxgTabButton(hostRef) {
        registerInstance(this, hostRef);
        this.tabActivated = createEvent(this, "tabActivated", 7);
        /**
         * The button label
         */
        this.tabLabel = null;
        /**
         * The tab id. Must be unique, and match the "tab" value of the correlative "gxg-tab" element
         */
        this.tab = null;
        /**
         * Provide this attribute to make this button selected by default
         */
        this.isSelected = false;
        /**
         * Provide this attribute to make this button disabled
         */
        this.disabled = false;
        /**
         * (Optional) provide an icon to this button
         */
        this.icon = null;
    }
    //Click functions
    GxgTabButton.prototype.tabButtonClicked = function () {
        this.isSelected = true;
        var index = parseInt(this.el.getAttribute("data-index"), 10);
        this.tabActivated.emit({
            tab: this.tab,
            index: index,
        });
    };
    GxgTabButton.prototype.printIcon = function () {
        if (this.icon !== null) {
            if (this.disabled) {
                return h("gxg-icon", { color: "disabled", type: this.icon });
            }
            return h("gxg-icon", { type: this.icon });
        }
    };
    GxgTabButton.prototype.componentDidLoad = function () {
        //Set the active tab for this tab-button if this is selected by default
        if (this.isSelected) {
            this.tabActivated.emit();
        }
    };
    GxgTabButton.prototype.render = function () {
        return (h("li", { class: "tab-item" }, h("button", { disabled: this.disabled, class: {
                "tab-button": true,
                "tab-button--selected": this.isSelected === true,
                "tab-button--text-icon": this.tabLabel !== null && this.icon !== null,
            }, onClick: this.tabButtonClicked.bind(this) }, this.printIcon(), h("span", { class: "tab-button__text" }, this.tabLabel)), h("slot", null)));
    };
    Object.defineProperty(GxgTabButton.prototype, "el", {
        get: function () { return getElement(this); },
        enumerable: false,
        configurable: true
    });
    return GxgTabButton;
}());
GxgTabButton.style = tabButtonCss;
export { GxgTabButton as gxg_tab_button };
