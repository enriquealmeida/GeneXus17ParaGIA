System.register([ "./p-d8a20c31.system.js" ], (function(e) {
  "use strict";
  var t, o, i, c, n;
  return {
    setters: [ function(e) {
      t = e.r, o = e.c, i = e.h, c = e.H, n = e.g;
    } ],
    execute: function() {
      e("gxg_listbox_item", /** @class */ function() {
        function class_1(e) {
          t(this, e), this.itemClicked = o(this, "itemClicked", 7), this.checkboxClicked = o(this, "checkboxClicked", 7), 
          /**
                     * Any icon that belongs to Gemini icon library: https://gx-gemini.netlify.app/?path=/story/icons
                     */
          this.icon = void 0, 
          /**
                     * The item value. If value is not provided, the value will be the item innerHTML.
                     */
          this.value = void 0, 
          /**
                     * (This prop is for internal use).
                     */
          this.iconColor = "auto", this.checkboxes = !1, this.itemHasFocus = !1;
        }
        return class_1.prototype.componentWillLoad = function() {
          var e = this;
          null !== this.el.parentElement.getAttribute("checkboxes") && (this.checkboxes = !0, 
          this.el.querySelector("gxg-form-checkbox").addEventListener("click", (function() {
            var t = e.el.getAttribute("index");
            e.checkboxClicked.emit({
              index: parseInt(t, 10)
            });
          })));
        }, class_1.prototype.itemClickedFunc = function(e) {
          if (!this.checkboxes) {
            var t = this.el.getAttribute("index");
            this.itemClicked.emit({
              index: parseInt(t, 10),
              crtlKey: e.ctrlKey,
              cmdKey: e.metaKey,
              mouseClicked: !0
            });
          }
        }, class_1.prototype.onKeyDown = function(e) {
          if (e.preventDefault(), "ArrowDown" === e.code) {
            var t = this.el.nextElementSibling;
            null !== t && t.focus();
          } else if ("ArrowUp" === e.code) {
            var o = this.el.previousElementSibling;
            null !== o && o.focus();
          }
          if ("Enter" === e.code) {
            var i = this.el.getAttribute("index");
            this.itemClicked.emit({
              index: parseInt(i, 10),
              crtlKey: e.ctrlKey,
              cmdKey: e.metaKey,
              mouseClicked: !1
            });
          }
        }, class_1.prototype.onMouseOver = function() {
          this.iconColor = "negative";
        }, class_1.prototype.onMouseOut = function() {
          this.el.classList.contains("selected") || (this.iconColor = "auto");
        }, class_1.prototype.render = function() {
          return i(c, {
            class: {
              "has-icon": void 0 !== this.icon,
              "no-checkbox": !this.checkboxes
            },
            onClick: this.itemClickedFunc.bind(this),
            onKeyDown: this.onKeyDown.bind(this),
            onMouseOver: this.onMouseOver.bind(this),
            onMouseOut: this.onMouseOut.bind(this)
          }, i("slot", {
            name: "checkbox"
          }), void 0 !== this.icon ? i("gxg-icon", {
            class: "icon",
            color: this.iconColor,
            size: "regular",
            type: this.icon
          }) : null, i("slot", null));
        }, Object.defineProperty(class_1.prototype, "el", {
          get: function() {
            return n(this);
          },
          enumerable: !1,
          configurable: !0
        }), class_1;
      }()).style = ":host{display:block;font-family:var(--font-family-primary);font-size:var(--font-size-sm);height:var(--spacing-comp-05);padding:0 var(--spacing-comp-02);color:var(--color-on-background);cursor:pointer;display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center}:host .icon{-webkit-margin-end:var(--spacing-comp-01);margin-inline-end:var(--spacing-comp-01)}:host ::slotted(gxg-form-checkbox){-webkit-margin-end:var(--spacing-comp-01);margin-inline-end:var(--spacing-comp-01)}:host(:hover),:host(.selected:hover){background-color:var(--color-secondary-hover);color:var(--color-always-white)}:host(.has-icon.no-checkbox){-webkit-padding-start:var(--spacing-comp-01);padding-inline-start:var(--spacing-comp-01)}:host(.selected){background-color:var(--color-secondary-active);color:var(--color-always-white)}:host(:focus){outline-width:var(--border-width-md);outline-color:var(--color-primary-active);outline-style:solid;outline-offset:-2px}";
    }
  };
}));