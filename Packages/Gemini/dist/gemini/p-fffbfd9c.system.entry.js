System.register([ "./p-d8a20c31.system.js" ], (function(t) {
  "use strict";
  var e, i, o, a, r;
  return {
    setters: [ function(t) {
      e = t.r, i = t.c, o = t.h, a = t.H, r = t.g;
    } ],
    execute: function() {
      t("gxg_accordion_item", /** @class */ function() {
        function class_1(t) {
          e(this, t), this.accordionItemClicked = i(this, "accordionItemClicked", 7), this.accordionItemLoaded = i(this, "accordionItemLoaded", 7), 
          this.accordionTitleClicked = i(this, "accordionTitleClicked", 7), this.titleChanged = i(this, "titleChanged", 7), 
          /**
                     * The presence of this attribute makes the accordion-item disabled and not focusable
                     */
          this.disabled = !1, 
          /**
                     * The presence of this attribute makes the accordion title editable
                     */
          this.editableTitle = !1, 
          /**
                     * The accordion flavor (No need to set this attribute on each of the the accordion-item's, only once at gxg-accordion)
                     */
          this.mode = "classical", 
          /**
                     * The accordion subtitle (optional)
                     */
          this.itemSubtitle = null, 
          /**
                     * The accordion title icon
                     */
          this.titleIcon = null, 
          /**
                     * Set the status to "open" if you want the accordion-item open by default
                     */
          this.status = "closed", 
          /*
                     *If this accordion is nested into an accordion-item
                     */
          this.nestedAccordion = !1, 
          /*
                     *accordion min-height
                     */
          this.minHeight = null, 
          /*
                     *accordion mode
                     */
          this.accordionMode = null, 
          /*
                     * Has slotted meta
                     */
          this.hasSlottedMeta = null;
        }
        return class_1.prototype.itemClickedHandler = function(t) {
          0 != t.detail && this.accordionItemClicked.emit(this.itemId);
        }, class_1.prototype.titleClickedHandler = function() {
          this.accordionTitleClicked.emit("title clicked");
        }, class_1.prototype.printIcon = function() {
          var t, e;
          return "open" !== this.status || this.disabled ? (
          //item closed
          t = "navigation/chevron-down", e = "classical" === this.mode ? this.disabled ? "ondisabled" : "alwaysblack" : "boxed" === this.mode || "minimal" === this.mode ? this.disabled ? "disabled" : "onbackground" : "negative") : (t = "navigation/chevron-up", 
          e = "classical" === this.mode ? "alwaysblack" : "boxed" === this.mode || "minimal" === this.mode ? "onbackground" : "negative"), 
          o("gxg-icon", {
            slot: "icon",
            size: "regular",
            type: t,
            color: e
          });
        }, class_1.prototype.componentWillLoad = function() {
          this.itemId || console.warn("gxg-accordion-item 'itemId' property is mandatory."), 
          this.accordionItemLoaded.emit(this.itemId), null !== this.el.querySelector("[slot=meta]") && (this.hasSlottedMeta = !0);
        }, class_1.prototype.componentDidLoad = function() {
          //Get accordion mode
          this.accordionMode = this.el.parentElement.getAttribute("mode"), null !== this.el.querySelector("gxg-accordion") && (this.nestedAccordion = !0);
          //Detect click on input ".outer-wrapper"
          var t = this.el.shadowRoot.querySelector("gxg-form-text");
          null !== t && t.shadowRoot.querySelector(".outer-wrapper").addEventListener("click", function(t) {
            t.path[0].classList.contains("input") || (this.status = "closed");
          }.bind(this));
        }, class_1.prototype.ariaExpanded = function() {
          return "closed" === this.status ? "false" : "true";
        }, class_1.prototype.ariaDisabled = function() {
          return this.disabled ? "true" : "false";
        }, class_1.prototype.gxgFormTextClickedHandler = function(t) {
          t.stopPropagation();
        }, class_1.prototype.changedTitleHandler = function(t) {
          this.editableTitle && (this.itemTitle = t.detail, this.titleChanged.emit(this.itemTitle));
        }, class_1.prototype.keyDownHandler = function(t) {
          "Enter" === t.code && this.accordionItemClicked.emit(this.itemId);
        }, class_1.prototype.itemSubtitleTrimmed = function(t) {
          return t.length > 50 ? t : "";
        }, class_1.prototype.render = function() {
          var t = this;
          return o(a, {
            class: {
              "nested-acordion": this.nestedAccordion,
              "has-subtitle": null !== this.itemSubtitle
            }
          }, 
          //disabled layer prevents interacting with the component and enables to use "not-allowed" cursor
          this.disabled ? o("div", {
            class: "disabled-layer"
          }) : null, o("div", {
            class: {
              item: !0,
              "item--disabled": !0 === this.disabled
            }
          }, o("header", {
            class: "item__header"
          }, o("div", {
            class: "item__header__button",
            id: "accordion-" + this.itemId,
            onClick: this.itemClickedHandler.bind(this),
            tabindex: this.disabled ? -1 : 0,
            "aria-expanded": this.ariaExpanded(),
            "aria-controls": this.itemId,
            "aria-disabled": this.ariaDisabled(),
            onKeyDown: this.keyDownHandler.bind(this)
          }, !this.editableTitle || "classical" !== this.mode && "boxed" !== this.mode ? null : 
          //div.cover prevents the editable-title to be edited when the accordion-item is closed
          o("div", {
            class: {
              cover: !0,
              hidden: "open" === this.status
            }
          }), o("div", {
            class: "item__header__button__title-subtitle"
          }, o("h1", {
            class: "item__header__button__title-subtitle__title",
            onClick: this.titleClickedHandler.bind(this)
          }, null === this.titleIcon || "classical" !== this.accordionMode && "boxed" !== this.accordionMode ? null : o("div", {
            class: "item__header__button__title-subtitle__title__icon"
          }, o("gxg-icon", {
            size: "regular",
            type: this.titleIcon,
            color: this.disabled ? "ondisabled" : "auto"
          })), !this.editableTitle || "classical" !== this.mode && "boxed" !== this.mode ? this.itemTitle : 
          //editable title should only be available for "classical" or "boxed" modes
          o("gxg-form-text", {
            onChange: function(e) {
              return t.changedTitleHandler(e);
            },
            minimal: !0,
            "over-dark-background": this.disabled && "classical" === this.mode,
            value: this.itemTitle,
            onClick: this.gxgFormTextClickedHandler.bind(this),
            "text-style": "title-02",
            class: "input"
          })), "classical" !== this.mode && "boxed" !== this.mode || null === this.itemSubtitle ? null : o("h2", {
            class: "item__header__button__title-subtitle__subtitle",
            title: this.itemSubtitleTrimmed(this.itemSubtitle)
          }, this.itemSubtitle.length > 50 ? this.itemSubtitle.slice(0, 50) + "..." : this.itemSubtitle)), o("div", {
            class: "item__header__button__meta-icon-wrapper"
          }, !this.hasSlottedMeta || "classical" !== this.mode && "boxed" !== this.mode ? null : o("div", {
            class: "item__header__button__meta-icon-wrapper__meta"
          }, o("slot", {
            name: "meta"
          })), o("div", {
            class: "item__header__button__meta-icon-wrapper__icon"
          }, this.printIcon())))), "open" !== this.status || this.disabled ? "" : o("main", {
            class: "item__container",
            id: this.itemId,
            role: "region",
            "aria-labelledby": "accordion-" + this.itemId
          }, o("slot", null))));
        }, Object.defineProperty(class_1.prototype, "el", {
          get: function() {
            return r(this);
          },
          enumerable: !1,
          configurable: !0
        }), class_1;
      }()).style = '@charset "UTF-8";/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{position:relative;outline:none;display:block;margin-bottom:var(--spacing-lay-xs);font-family:"Times New Roman", Times, serif}:host:last-child{margin-bottom:0}:host .item__header__button{display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;border-width:var(--border-width-md);border-style:solid;border-color:transparent}:host .item__header__button:focus{outline:none;border-color:var(--color-primary-active)}:host .item__header__button__title-subtitle__title{margin:0}:host .item__header__button__title-subtitle__subtitle{margin:0}:host .item__header__button__meta-icon-wrapper__icon{display:-ms-flexbox;display:flex}:host .item__header__button:hover{cursor:pointer}:host .item__container{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;text-align:start}:host .disabled-layer{width:100%;height:100%;position:absolute;z-index:99;cursor:not-allowed}:host([mode=classical]) .item__header__button,:host([mode=boxed]) .item__header__button{-ms-flex-pack:justify;justify-content:space-between;padding-top:0;-webkit-padding-end:var(--spacing-comp-01);padding-inline-end:var(--spacing-comp-01);padding-bottom:0;-webkit-padding-start:var(--spacing-comp-02);padding-inline-start:var(--spacing-comp-02);min-height:20px;-webkit-transition-property:background-color;transition-property:background-color;-webkit-transition-duration:var(--ui-animaton-speed);transition-duration:var(--ui-animaton-speed);-webkit-transition-timing-function:ease;transition-timing-function:ease}:host([mode=classical]) .item__header__button .cover,:host([mode=boxed]) .item__header__button .cover{position:absolute;height:100%;width:100%;left:0;top:0;background-color:transparent;z-index:10}:host([mode=classical]) .item__header__button .cover.hidden,:host([mode=boxed]) .item__header__button .cover.hidden{display:none}:host([mode=classical]) .item__header__button__title-subtitle,:host([mode=boxed]) .item__header__button__title-subtitle{-webkit-margin-end:var(--spacing-comp-04);margin-inline-end:var(--spacing-comp-04);width:100%}:host([mode=classical]) .item__header__button__title-subtitle__title,:host([mode=boxed]) .item__header__button__title-subtitle__title{display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;text-align:start;font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;letter-spacing:0.06em}:host([mode=classical]) .item__header__button__title-subtitle__title gxg-form-text::part(input),:host([mode=boxed]) .item__header__button__title-subtitle__title gxg-form-text::part(input){letter-spacing:0.06em}:host([mode=classical]) .item__header__button__title-subtitle__title__icon,:host([mode=boxed]) .item__header__button__title-subtitle__title__icon{display:-ms-flexbox;display:flex;-webkit-margin-end:var(--spacing-comp-01);margin-inline-end:var(--spacing-comp-01)}:host([mode=classical]) .item__header__button__title-subtitle__subtitle,:host([mode=boxed]) .item__header__button__title-subtitle__subtitle{padding-bottom:2px;max-width:300px;font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;text-align:start}:host([mode=classical]) .item__header__button__meta-icon-wrapper,:host([mode=boxed]) .item__header__button__meta-icon-wrapper{display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;-ms-flex-negative:0;flex-shrink:0}:host([mode=classical]) .item__header__button__meta-icon-wrapper__meta,:host([mode=boxed]) .item__header__button__meta-icon-wrapper__meta{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;display:-ms-flexbox;display:flex;margin-right:var(--spacing-comp-02)}:host([mode=classical]) .item__container,:host([mode=boxed]) .item__container{padding-top:var(--spacing-comp-02);-webkit-padding-end:var(--spacing-comp-03);padding-inline-end:var(--spacing-comp-03);padding-bottom:var(--spacing-comp-02);-webkit-padding-start:var(--spacing-comp-03);padding-inline-start:var(--spacing-comp-03)}:host([mode=classical].has-subtitle) .item__header__button,:host([mode=boxed].has-subtitle) .item__header__button{-ms-flex-align:start;align-items:start}:host([mode=classical][no-padding]) .item__container,:host([mode=boxed][no-padding]) .item__container{padding:0}:host([mode=classical]) .item__header__button{background-color:var(--color-secondary-enabled)}:host([mode=classical]) .item__header__button:hover{background-color:var(--color-secondary-hover)}:host([mode=classical]) .item__container{background-color:var(--color-background)}:host([mode=classical]) .item--disabled{cursor:not-allowed}:host([mode=classical]) .item--disabled .item__header__button{background-color:var(--color-primary-disabled)}:host([mode=classical]) .item--disabled .item__header__button .icon-wrapper{-webkit-padding-end:2px;padding-inline-end:2px}:host([mode=classical]) .item--disabled .item__header__button__title-subtitle__title,:host([mode=classical]) .item--disabled .item__header__button__title-subtitle__subtitle{color:var(--color-on-disabled)}:host([mode=classical]) .item--disabled .item__header__button__meta-icon-wrapper__meta{color:var(--color-on-primary);opacity:0.3}:host([mode=classical][editable-title]) .item__header__button__title-subtitle__subtitle{-webkit-margin-start:5px;margin-inline-start:5px}:host([mode=classical][editable-title]) .item--disabled .item__header__button__title-subtitle__title{opacity:0.3}:host([mode=boxed]) .item{background-color:var(--gray-01);-webkit-transition-property:border-color;transition-property:border-color;-webkit-transition-duration:var(--ui-animaton-speed);transition-duration:var(--ui-animaton-speed);-webkit-transition-timing-function:ease;transition-timing-function:ease}:host([mode=boxed]) .item__header__button{background:transparent}:host([mode=boxed]) .item__header__button:hover{background-color:var(--gray-02)}:host([mode=boxed]) .item__container{background:transparent;padding-top:var(--spacing-comp-01)}:host([mode=boxed]) .item:hover{border-color:var(--color-primary-hover)}:host([mode=boxed]) .item--disabled .item__header__button__title-subtitle__title,:host([mode=boxed]) .item--disabled .item__header__button__title-subtitle__subtitle{color:var(--color-primary-disabled)}:host([mode=boxed]) .item--disabled .item__header__button__meta-icon-wrapper__meta{opacity:0.5}:host([mode=boxed][editable-title]) .item__header__button__title-subtitle__subtitle{-webkit-margin-start:5px;margin-inline-start:5px}:host([mode=boxed][editable-title]) .item--disabled .item__header__button__title-subtitle__title{opacity:0.5}:host([mode=slim]) .item__header__button,:host([mode=minimal]) .item__header__button{-ms-flex-pack:end;justify-content:flex-end;-ms-flex-direction:row-reverse;flex-direction:row-reverse}:host([mode=slim]) .item__container,:host([mode=minimal]) .item__container{padding-top:var(--spacing-comp-01);-webkit-padding-end:0;padding-inline-end:0;padding-bottom:var(--spacing-comp-01);-webkit-padding-start:0;padding-inline-start:0}:host([mode=slim][no-padding]) .item__container,:host([mode=minimal][no-padding]) .item__container{padding:0;-webkit-margin-start:0;margin-inline-start:0}:host([mode=slim]){margin-bottom:var(--spacing-lay-xs)}:host([mode=slim]) .item__header__button{padding:2px 0}:host([mode=slim]) .item__header__button:hover gxg-icon{background-color:var(--color-primary-hover)}:host([mode=slim]) .item__header__button:active gxg-icon{background-color:var(--color-primary-active)}:host([mode=slim]) .item__header__button gxg-icon{background:var(--color-primary-enabled);border-radius:50%;-webkit-margin-end:var(--spacing-comp-02);margin-inline-end:var(--spacing-comp-02);width:16px;height:16px}:host([mode=slim]) .item__header__button__title-subtitle__title{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}:host([mode=slim]) .item__container{-webkit-margin-start:26px;margin-inline-start:26px}:host([mode=slim]) .item--open gxg-icon{background:var(--color-primary-active)}:host([mode=slim]) .item--disabled{cursor:not-allowed;pointer-events:none}:host([mode=slim]) .item--disabled gxg-icon{background:var(--color-primary-disabled)}:host([mode=minimal]){margin-bottom:var(--spacing-lay-xs)}:host([mode=minimal]) .item__header__button gxg-icon{-webkit-margin-end:var(--spacing-comp-01);margin-inline-end:var(--spacing-comp-01)}:host([mode=minimal]) .item__header__button__title-subtitle__title{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}:host([mode=minimal]) .item__container{-webkit-margin-start:26px;margin-inline-start:26px}';
    }
  };
}));