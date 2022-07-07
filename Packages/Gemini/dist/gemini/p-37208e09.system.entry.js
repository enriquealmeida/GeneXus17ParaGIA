System.register([ "./p-d8a20c31.system.js" ], (function(t) {
  "use strict";
  var e, r, o, a, i;
  return {
    setters: [ function(t) {
      e = t.r, r = t.c, o = t.h, a = t.H, i = t.g;
    } ],
    execute: function() {
      function makeDraggable(t) {
        var e, r = t.getDraggableElements(), o = Array.from(r);
        function dragStart(r) {
          t.itemDragStart.emit().defaultPrevented || (this.style.opacity = "0.3", e = this, 
          r.dataTransfer.effectAllowed = "move", r.dataTransfer.setData("text/html", this.innerHTML));
        }
        function dragEnter() {
          t.itemDragEnter.emit(this).defaultPrevented || this.classList.add("over");
        }
        function dragLeave(e) {
          t.itemDragLeave.emit(this).defaultPrevented || (e.stopPropagation(), this.classList.remove("over"));
        }
        function dragOver(e) {
          if (!t.itemDragOver.emit(this).defaultPrevented) return e.preventDefault(), e.dataTransfer.dropEffect = "move", 
          !1;
        }
        function dragDrop(r) {
          if (!t.itemDrop.emit().defaultPrevented) return e != this && (e.innerHTML = this.innerHTML, 
          this.innerHTML = r.dataTransfer.getData("text/html")), !1;
        }
        function dragEnd() {
          o.forEach((function(t) {
            return t.classList.remove("over");
          })), this.style.opacity = "1";
        }
        return o.forEach((function(t) {
          t.setAttribute("draggable", "true");
        })), o.forEach((function(t) {
          return (e = t).addEventListener("dragstart", dragStart, !1), e.addEventListener("dragenter", dragEnter, !1), 
          e.addEventListener("dragover", dragOver, !1), e.addEventListener("dragleave", dragLeave, !1), 
          e.addEventListener("drop", dragDrop, !1), void e.addEventListener("dragend", dragEnd, !1);
          var e;
        })), function() {
          o.forEach((function(t) {
            return (e = t).removeEventListener("dragstart", dragStart), e.removeEventListener("dragenter", dragEnter), 
            e.removeEventListener("dragover", dragOver), e.removeEventListener("dragleave", dragLeave), 
            e.removeEventListener("drop", dragDrop), void e.removeEventListener("dragend", dragEnd);
            var e;
          }));
        };
      }
      t("gxg_drag_container", /** @class */ function() {
        function class_1(t) {
          e(this, t), this.itemDragStart = r(this, "itemDragStart", 7), this.itemDrop = r(this, "itemDrop", 7), 
          this.itemDragOver = r(this, "itemDragOver", 7), this.itemDragLeave = r(this, "itemDragLeave", 7), 
          this.itemDragEnter = r(this, "itemDragEnter", 7), 
          /**
                     * The presence of this attribute adds a "delete" button to each gxg-drag-box. When pressed, the "deleted" event is emmited.
                     */
          this.deletable = !1, 
          /**
                     * The max-width of the box container
                     */
          this.maxWidth = "100%", 
          /**
                     * The padding (internal spacing) of the gxg-drag-boxes
                     */
          this.padding = void 0;
        }
        return class_1.prototype.clickedHandler = function(t) {
          this.el.querySelectorAll("*").forEach((function(e) {
            t.detail === e.getAttribute("id") ? e.setAttribute("active", "active") : e.removeAttribute("active");
          }));
        }, class_1.prototype.getDraggableElements = function() {
          return this.el.querySelectorAll("gxg-drag-box");
        }, class_1.prototype.componentDidLoad = function() {
          var t = this;
          //Deletable button for each of the child items
          (this.dndCleanup = makeDraggable(this), this.el.querySelectorAll("gxg-drag-box").forEach((function(e) {
            void 0 !== t.padding && e.setAttribute("padding", t.padding);
          })), this.deletable) && this.el.querySelectorAll("*").forEach((function(t) {
            t.setAttribute("deletable", "deletable");
          }));
        }, class_1.prototype.disconnectedCallback = function() {
          this.dndCleanup();
        }, class_1.prototype.render = function() {
          return o(a, {
            style: {
              maxWidth: this.maxWidth
            }
          }, o("slot", null), o("div", {
            class: "placeholder"
          }));
        }, Object.defineProperty(class_1.prototype, "el", {
          get: function() {
            return i(this);
          },
          enumerable: !1,
          configurable: !0
        }), class_1;
      }()).style = "/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{display:block}:host ::slotted(*){will-change:transform;cursor:-webkit-grab;cursor:grab;-webkit-transition:all 200ms;transition:all 200ms;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;position:relative;width:100%;-webkit-box-sizing:border-box;box-sizing:border-box}:host ::slotted(.over){-webkit-transform:scale(1.02, 1.02);transform:scale(1.02, 1.02);cursor:pointer;-webkit-box-shadow:var(--box-shadow-03);box-shadow:var(--box-shadow-03)}:host :last-child{margin-bottom:0}:host .placeholder{display:none;height:50px;background-color:#ccc}:host .placeholder.visible{display:block}";
    }
  };
}));