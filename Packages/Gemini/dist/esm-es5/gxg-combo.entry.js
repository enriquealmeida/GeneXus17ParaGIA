import { r as registerInstance, h, H as Host, g as getElement } from './index-09b1517f.js';
var comboCss = "/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{display:block}:host ::-webkit-scrollbar{width:6px}:host ::-webkit-scrollbar-track{background-color:var(--gray-02);border-radius:10px}:host ::-webkit-scrollbar-thumb{background:var(--gray-05);border-radius:10px}:host ::-webkit-scrollbar-thumb:hover{background:var(--gray-04);cursor:pointer}:host .gxg-scroll{display:block;overflow-y:auto;padding-right:2px}:host .main-container .search-container{position:relative}:host .main-container .search-container .arrow-down-icon{position:absolute;right:0;top:0}:host .main-container .search-container .delete-icon{position:absolute;right:15px;top:0;-webkit-margin-end:var(--spacing-comp-01);margin-inline-end:var(--spacing-comp-01)}:host .main-container .search-container .arrow-down-icon,:host .main-container .search-container .delete-icon{cursor:pointer;z-index:2}:host .main-container .search-container .arrow-down-icon:hover,:host .main-container .search-container .delete-icon:hover{opacity:0.75}:host .main-container .search-container .layer{position:absolute;display:inline-block;width:40px;height:calc(100% - 2px);background-color:var(--color-background);right:1px;top:1px;z-index:1}:host .main-container .items-container{border-width:var(--border-width-sm);border-style:solid;border-color:var(--gray-04);border-radius:var(--border-radius-md);border-top-left-radius:0;border-top-right-radius:0;border-top:0;max-height:144px;overflow-y:auto;background-color:var(--color-background)}:host .main-container .items-container .no-courrences-found{font-family:var(--font-family-primary);font-size:var(--font-size-sm);color:var(--color-on-background);padding:var(--spacing-comp-01);background-color:var(--color-background)}:host .main-container .items-container .no-courrences-found span{display:block;color:var(--gray-04)}:host .main-container .items-container.exact-match{overflow-y:hidden}";
var GxgCombo = /** @class */ (function () {
    function GxgCombo(hostRef) {
        registerInstance(this, hostRef);
        /**
         * The combo width
         */
        this.width = "240px";
        this.selectedItemValue = "";
        this.inputTextValue = "";
        this.showItems = false;
        this.inputTextIcon = undefined;
        this.inputTextIconPosition = null;
        this.noMatch = false;
        this.slottedContent = null;
    }
    GxgCombo.prototype.componentWillLoad = function () {
        this.itemsNodeList = this.el.querySelectorAll("gxg-combo-item");
        this.itemsNodeList.forEach(function (item, i) {
            var itemHtmlElement = item;
            itemHtmlElement.setAttribute("index", i.toString());
            itemHtmlElement.setAttribute("tabindex", "0");
        });
    };
    GxgCombo.prototype.itemClickedHandler = function (event) {
        var previouslyselectedItem = this.el.querySelector(".selected");
        if (previouslyselectedItem !== null) {
            previouslyselectedItem.classList.remove("selected");
            //set icon color to auto
            previouslyselectedItem.iconColor =
                "auto";
        }
        var actualSelectedItem = this.el.querySelector("[index='" + event.detail["index"] + "']");
        actualSelectedItem.classList.add("selected");
        this.selectedItemValue = event.detail.value;
        this.inputTextValue = event.detail.value;
        if (event.detail.icon !== null) {
            this.inputTextIcon = event.detail.icon;
            this.inputTextIconPosition = "start";
        }
        else {
            this.inputTextIcon = null;
            this.inputTextIconPosition = null;
        }
        // Display all hidden items
        var hiddenItems = this.el.querySelectorAll(".hidden");
        hiddenItems.forEach(function (hiddenItem) {
            var hiddenItemHTMLElement = hiddenItem;
            hiddenItemHTMLElement.classList.remove("hidden");
        });
        //remove exact match class
        var exactMatch = this.el.querySelector(".exact-match");
        if (exactMatch !== null) {
            exactMatch.classList.remove("exact-match");
        }
        this.showItems = false;
    };
    GxgCombo.prototype.onInputGxgformText = function (e) {
        this.inputTextValue = e.detail;
        this.inputTextIcon = null;
        this.inputTextIconPosition = null;
        var itemSelected = this.el.querySelector(".selected");
        if (itemSelected !== null) {
            itemSelected.classList.remove("selected");
        }
        var filterValue = e.detail.toLowerCase();
        this.itemsNodeList.forEach(function (item) {
            var itemHtmlElement = item;
            var itemValue = itemHtmlElement.getAttribute("value");
            if (itemValue === null) {
                itemValue = itemHtmlElement.innerText;
            }
            itemValue = itemValue.toLowerCase();
            if (!itemValue.includes(filterValue)) {
                itemHtmlElement.classList.add("hidden");
            }
            else {
                itemHtmlElement.classList.remove("hidden");
            }
            if (itemValue === filterValue) {
                itemHtmlElement.classList.add("exact-match");
            }
            else {
                itemHtmlElement.classList.remove("exact-match");
            }
        });
        var numberOfHiddenItems = this.el.getElementsByClassName("hidden").length;
        if (this.itemsNodeList.length === numberOfHiddenItems) {
            this.noMatch = true;
        }
        else {
            this.noMatch = false;
        }
    };
    GxgCombo.prototype.onKeyDownGxgformText = function (e) {
        if (e.key === "Escape") {
            this.showItems = false;
            this.textInput.blur();
        }
    };
    GxgCombo.prototype.toggleItems = function () {
        if (this.showItems === true) {
            this.showItems = false;
        }
        else {
            this.showItems = true;
        }
    };
    GxgCombo.prototype.detectClickOutsideCombo = function (event) {
        var comboMainContainer = this.el.shadowRoot.querySelector(".main-container");
        var x = event.x;
        var y = event.y;
        //Contextual menu coordinates
        var comboMainContainerArea = comboMainContainer.getBoundingClientRect();
        if ((x > comboMainContainerArea.left &&
            x < comboMainContainerArea.right &&
            y > comboMainContainerArea.top &&
            y < comboMainContainerArea.bottom) ||
            (event.screenX === 0 &&
                event.screenY === 0 &&
                event.clientX === 0 &&
                event.clientY === 0))
            ;
        else {
            this.showItems = false;
            //Click happened outside the combo
        }
    };
    GxgCombo.prototype.componentDidLoad = function () {
        document.addEventListener("click", this.detectClickOutsideCombo.bind(this));
    };
    GxgCombo.prototype.componentDidUnload = function () {
        document.removeEventListener("click", this.detectClickOutsideCombo);
    };
    GxgCombo.prototype.selecteItemFunc = function () {
        return this.selectedItemValue;
    };
    GxgCombo.prototype.onInputFocus = function () {
        this.showItems = true;
    };
    GxgCombo.prototype.clearInputFunc = function () {
        this.selectedItemValue = "";
        this.inputTextValue = "";
        var gxgFormText = this.el.shadowRoot.getElementById("gxgFormText");
        gxgFormText.value = "";
        gxgFormText.focus();
        this.inputTextIconPosition = null;
        var hiddenItems = this.el.querySelectorAll(".hidden");
        hiddenItems.forEach(function (item) {
            item.classList.remove("hidden");
        });
        var selectedItem = this.el.querySelector(".selected");
        if (selectedItem !== null) {
            //set icon color to auto
            var selectedItemIcon = selectedItem.shadowRoot.querySelector("gxg-icon");
            selectedItemIcon.color = "auto";
            selectedItem.classList.remove("selected");
        }
        var exactMatch = this.el.querySelector(".exact-match");
        if (exactMatch !== null) {
            exactMatch.classList.remove("exact-match");
        }
        this.showItems = true;
    };
    GxgCombo.prototype.render = function () {
        var _this = this;
        return (h(Host, null, h("div", { class: { "main-container": true }, style: { width: this.width } }, h("div", { class: { "search-container": true } }, h("gxg-form-text", { placeholder: "Search item", onInput: this.onInputGxgformText.bind(this), onKeyDown: this.onKeyDownGxgformText.bind(this), onFocus: function () { return _this.onInputFocus(); }, value: this.selectedItemValue, id: "gxgFormText", icon: this.inputTextIcon, iconPosition: this.inputTextIconPosition, ref: function (el) { return (_this.textInput = el); } }), this.inputTextValue !== "" ? (h("gxg-button", { class: { "delete-icon": true }, icon: "menus/delete", type: "tertiary", onClick: function () { return _this.clearInputFunc(); } })) : null, h("gxg-button", { class: { "arrow-down-icon": true }, icon: "navigation/arrow-down", type: "tertiary", onClick: function () { return _this.toggleItems(); } }), h("span", { class: "layer" })), this.showItems ? (h("div", { class: {
                "items-container": true,
            } }, h("slot", null), this.noMatch ? (h("div", { class: "no-courrences-found" }, "No occurrences found", h("span", null, "ctrl/cmd + backspace to erase"))) : null)) : null)));
    };
    Object.defineProperty(GxgCombo.prototype, "el", {
        get: function () { return getElement(this); },
        enumerable: false,
        configurable: true
    });
    return GxgCombo;
}());
GxgCombo.style = comboCss;
export { GxgCombo as gxg_combo };
