System.register([ "./p-d8a20c31.system.js" ], (function(t) {
  "use strict";
  var e, o, r, i;
  return {
    setters: [ function(t) {
      e = t.r, o = t.h, r = t.H, i = t.g;
    } ],
    execute: function() {
      t("gxg_combo", /** @class */ function() {
        function class_1(t) {
          e(this, t), 
          /**
                     * The combo width
                     */
          this.width = "240px", this.selectedItemValue = "", this.inputTextValue = "", this.showItems = !1, 
          this.inputTextIcon = void 0, this.inputTextIconPosition = null, this.noMatch = !1, 
          this.slottedContent = null;
        }
        return class_1.prototype.componentWillLoad = function() {
          this.itemsNodeList = this.el.querySelectorAll("gxg-combo-item"), this.itemsNodeList.forEach((function(t, e) {
            var o = t;
            o.setAttribute("index", e.toString()), o.setAttribute("tabindex", "0");
          }));
        }, class_1.prototype.itemClickedHandler = function(t) {
          var e = this.el.querySelector(".selected");
          null !== e && (e.classList.remove("selected"), 
          //set icon color to auto
          e.iconColor = "auto"), this.el.querySelector("[index='" + t.detail.index + "']").classList.add("selected"), 
          this.selectedItemValue = t.detail.value, this.inputTextValue = t.detail.value, null !== t.detail.icon ? (this.inputTextIcon = t.detail.icon, 
          this.inputTextIconPosition = "start") : (this.inputTextIcon = null, this.inputTextIconPosition = null), 
          this.el.querySelectorAll(".hidden").forEach((function(t) {
            t.classList.remove("hidden");
          }));
          //remove exact match class
          var o = this.el.querySelector(".exact-match");
          null !== o && o.classList.remove("exact-match"), this.showItems = !1;
        }, class_1.prototype.onInputGxgformText = function(t) {
          this.inputTextValue = t.detail, this.inputTextIcon = null, this.inputTextIconPosition = null;
          var e = this.el.querySelector(".selected");
          null !== e && e.classList.remove("selected");
          var o = t.detail.toLowerCase();
          this.itemsNodeList.forEach((function(t) {
            var e = t, r = e.getAttribute("value");
            null === r && (r = e.innerText), (r = r.toLowerCase()).includes(o) ? e.classList.remove("hidden") : e.classList.add("hidden"), 
            r === o ? e.classList.add("exact-match") : e.classList.remove("exact-match");
          }));
          var r = this.el.getElementsByClassName("hidden").length;
          this.itemsNodeList.length === r ? this.noMatch = !0 : this.noMatch = !1;
        }, class_1.prototype.onKeyDownGxgformText = function(t) {
          "Escape" === t.key && (this.showItems = !1, this.textInput.blur());
        }, class_1.prototype.toggleItems = function() {
          !0 === this.showItems ? this.showItems = !1 : this.showItems = !0;
        }, class_1.prototype.detectClickOutsideCombo = function(t) {
          var e = this.el.shadowRoot.querySelector(".main-container"), o = t.x, r = t.y, i = e.getBoundingClientRect();
          o > i.left && o < i.right && r > i.top && r < i.bottom || 0 === t.screenX && 0 === t.screenY && 0 === t.clientX && 0 === t.clientY || (this.showItems = !1);
        }, class_1.prototype.componentDidLoad = function() {
          document.addEventListener("click", this.detectClickOutsideCombo.bind(this));
        }, class_1.prototype.componentDidUnload = function() {
          document.removeEventListener("click", this.detectClickOutsideCombo);
        }, class_1.prototype.selecteItemFunc = function() {
          return this.selectedItemValue;
        }, class_1.prototype.onInputFocus = function() {
          this.showItems = !0;
        }, class_1.prototype.clearInputFunc = function() {
          this.selectedItemValue = "", this.inputTextValue = "";
          var t = this.el.shadowRoot.getElementById("gxgFormText");
          t.value = "", t.focus(), this.inputTextIconPosition = null, this.el.querySelectorAll(".hidden").forEach((function(t) {
            t.classList.remove("hidden");
          }));
          var e = this.el.querySelector(".selected");
          null !== e && (e.shadowRoot.querySelector("gxg-icon").color = "auto", e.classList.remove("selected"));
          var o = this.el.querySelector(".exact-match");
          null !== o && o.classList.remove("exact-match"), this.showItems = !0;
        }, class_1.prototype.render = function() {
          var t = this;
          return o(r, null, o("div", {
            class: {
              "main-container": !0
            },
            style: {
              width: this.width
            }
          }, o("div", {
            class: {
              "search-container": !0
            }
          }, o("gxg-form-text", {
            placeholder: "Search item",
            onInput: this.onInputGxgformText.bind(this),
            onKeyDown: this.onKeyDownGxgformText.bind(this),
            onFocus: function() {
              return t.onInputFocus();
            },
            value: this.selectedItemValue,
            id: "gxgFormText",
            icon: this.inputTextIcon,
            iconPosition: this.inputTextIconPosition,
            ref: function(e) {
              return t.textInput = e;
            }
          }), "" !== this.inputTextValue ? o("gxg-button", {
            class: {
              "delete-icon": !0
            },
            icon: "menus/delete",
            type: "tertiary",
            onClick: function() {
              return t.clearInputFunc();
            }
          }) : null, o("gxg-button", {
            class: {
              "arrow-down-icon": !0
            },
            icon: "navigation/arrow-down",
            type: "tertiary",
            onClick: function() {
              return t.toggleItems();
            }
          }), o("span", {
            class: "layer"
          })), this.showItems ? o("div", {
            class: {
              "items-container": !0
            }
          }, o("slot", null), this.noMatch ? o("div", {
            class: "no-courrences-found"
          }, "No occurrences found", o("span", null, "ctrl/cmd + backspace to erase")) : null) : null));
        }, Object.defineProperty(class_1.prototype, "el", {
          get: function() {
            return i(this);
          },
          enumerable: !1,
          configurable: !0
        }), class_1;
      }()).style = "/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{display:block}:host ::-webkit-scrollbar{width:6px}:host ::-webkit-scrollbar-track{background-color:var(--gray-02);border-radius:10px}:host ::-webkit-scrollbar-thumb{background:var(--gray-05);border-radius:10px}:host ::-webkit-scrollbar-thumb:hover{background:var(--gray-04);cursor:pointer}:host .gxg-scroll{display:block;overflow-y:auto;padding-right:2px}:host .main-container .search-container{position:relative}:host .main-container .search-container .arrow-down-icon{position:absolute;right:0;top:0}:host .main-container .search-container .delete-icon{position:absolute;right:15px;top:0;-webkit-margin-end:var(--spacing-comp-01);margin-inline-end:var(--spacing-comp-01)}:host .main-container .search-container .arrow-down-icon,:host .main-container .search-container .delete-icon{cursor:pointer;z-index:2}:host .main-container .search-container .arrow-down-icon:hover,:host .main-container .search-container .delete-icon:hover{opacity:0.75}:host .main-container .search-container .layer{position:absolute;display:inline-block;width:40px;height:calc(100% - 2px);background-color:var(--color-background);right:1px;top:1px;z-index:1}:host .main-container .items-container{border-width:var(--border-width-sm);border-style:solid;border-color:var(--gray-04);border-radius:var(--border-radius-md);border-top-left-radius:0;border-top-right-radius:0;border-top:0;max-height:144px;overflow-y:auto;background-color:var(--color-background)}:host .main-container .items-container .no-courrences-found{font-family:var(--font-family-primary);font-size:var(--font-size-sm);color:var(--color-on-background);padding:var(--spacing-comp-01);background-color:var(--color-background)}:host .main-container .items-container .no-courrences-found span{display:block;color:var(--gray-04)}:host .main-container .items-container.exact-match{overflow-y:hidden}";
    }
  };
}));