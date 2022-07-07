System.register([ "./p-d8a20c31.system.js" ], (function(t) {
  "use strict";
  var o, e, i, r, s;
  return {
    setters: [ function(t) {
      o = t.r, e = t.c, i = t.h, r = t.H, s = t.g;
    } ],
    execute: function() {
      t("gxg_filter_item", /** @class */ function() {
        function class_1(t) {
          o(this, t), this.itemClickedEvent = e(this, "itemClickedEvent", 7), 
          /**
                     * The item-id (required if you want to know that this item was clicked)
                     */
          this.itemId = void 0, 
          /**
                     * The type (optional)
                     */
          this.type = void 0, 
          /**
                     * Any icon that belongs to Gemini icon library: https://gx-gemini.netlify.app/?path=/story/icons
                     */
          this.icon = void 0;
        }
        return class_1.prototype.itemClicked = function() {
          this.itemClickedEvent.emit({
            "item-id": this.itemId,
            "item-text": this.el.innerHTML,
            "item-type": this.type
          });
        }, class_1.prototype.render = function() {
          return i(r, {
            onClick: this.itemClicked.bind(this),
            tabindex: "0"
          }, void 0 !== this.icon ? i("gxg-icon", {
            color: "auto",
            size: "small",
            type: this.icon
          }) : null, i("div", {
            class: "text"
          }, i("slot", null)));
        }, Object.defineProperty(class_1.prototype, "el", {
          get: function() {
            return s(this);
          },
          enumerable: !1,
          configurable: !0
        }), class_1;
      }()).style = ":host{display:block;font-family:var(--font-family-primary);font-size:var(--font-size-sm);padding:0 var(--spacing-comp-01) 0 var(--spacing-comp-01);cursor:pointer;display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;height:var(--spacing-comp-05);opacity:1;-webkit-transition:all 0.25s;transition:all 0.25s;overflow:hidden;color:var(--color-on-background)}:host gxg-icon{-webkit-margin-end:var(--spacing-comp-01);margin-inline-end:var(--spacing-comp-01)}:host .text{text-overflow:ellipsis;white-space:nowrap;overflow:hidden;width:calc(100% - 24px)}:host(:hover){background-color:var(--color-secondary-enabled)}:host(.opacity-zero){opacity:0}:host(.height-zero){height:0}:host(.exact-match){color:var(--color-success-dark)}:host(.exact-match:hover){color:var(--color-success-dark);background-color:var(--color-success-light)}:host(:focus){outline:none;outline-color:var(--color-primary-active);-webkit-box-shadow:var(--box-shadow-values) var(--color-primary-active);box-shadow:var(--box-shadow-values) var(--color-primary-active)}";
    }
  };
}));