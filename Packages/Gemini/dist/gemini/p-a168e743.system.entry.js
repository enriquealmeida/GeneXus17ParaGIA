System.register([ "./p-d8a20c31.system.js" ], (function(o) {
  "use strict";
  var t, e, i, s, n;
  return {
    setters: [ function(o) {
      t = o.r, e = o.c, i = o.h, s = o.H, n = o.g;
    } ],
    execute: function() {
      o("gxg_combo_item", /** @class */ function() {
        function class_1(o) {
          t(this, o), this.itemClicked = e(this, "itemClicked", 7), 
          /**
                     * Any icon that belongs to Gemini icon library: https://gx-gemini.netlify.app/?path=/story/icons
                     */
          this.icon = void 0, 
          /**
                     * The item value. This is what the filter with search for. If value is not provided, the filter will search by the item innerHTML.
                     */
          this.value = void 0, 
          /**
                     * (This prop is for internal use).
                     */
          this.iconColor = "auto";
        }
        return class_1.prototype.itemClickedFunc = function() {
          var o = this.el.getAttribute("index"), t = this.el.getAttribute("icon"), e = this.value;
          void 0 === e && (e = this.el.innerHTML), this.itemClicked.emit({
            index: parseInt(o, 10),
            value: e.toString(),
            icon: t
          });
        }, class_1.prototype.onKeyDown = function(o) {
          if (o.preventDefault(), "ArrowDown" === o.code) {
            var t = this.el.nextElementSibling;
            null !== t && t.focus();
          } else if ("ArrowUp" === o.code) {
            var e = this.el.previousElementSibling;
            null !== e && e.focus();
          }
          "Enter" === o.code && this.itemClickedFunc();
        }, class_1.prototype.onMouseOver = function() {
          this.iconColor = "negative";
        }, class_1.prototype.onMouseOut = function() {
          this.el.classList.contains("selected") || (this.iconColor = "auto");
        }, class_1.prototype.render = function() {
          return i(s, {
            onClick: this.itemClickedFunc.bind(this),
            onKeyDown: this.onKeyDown.bind(this),
            onMouseOver: this.onMouseOver.bind(this),
            onMouseOut: this.onMouseOut.bind(this)
          }, i("div", {
            class: "content"
          }, void 0 !== this.icon ? i("gxg-icon", {
            color: this.iconColor,
            size: "small",
            type: this.icon
          }) : null, i("slot", null)));
        }, Object.defineProperty(class_1.prototype, "el", {
          get: function() {
            return n(this);
          },
          enumerable: !1,
          configurable: !0
        }), class_1;
      }()).style = ":host{display:block;cursor:pointer;height:var(--spacing-comp-05);display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;font-family:var(--font-family-primary);font-size:var(--font-size-sm);background-color:var(--color-background);color:var(--color-on-background);padding:0 var(--spacing-comp-01);-webkit-transition:height 0.25s, opacity 0.25s;transition:height 0.25s, opacity 0.25s;opacity:1;overflow:hidden}:host .content{display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center}:host(.hidden){height:0;opacity:0}:host(.display-none){display:none}:host(:hover),:host(.selected:hover){background-color:var(--color-secondary-hover);color:var(--color-always-white)}:host(.selected){background-color:var(--color-secondary-active);color:var(--color-always-white)}:host(.exact-match){background-color:var(--color-success-light);color:var(--color-success-dark)}:host(:focus){outline-width:var(--border-width-md);outline-color:var(--color-primary-active);outline-style:solid;outline-offset:-2px}";
    }
  };
}));