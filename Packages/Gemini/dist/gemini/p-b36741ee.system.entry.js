System.register([ "./p-d8a20c31.system.js" ], (function(r) {
  "use strict";
  var e, n, t, i, c;
  return {
    setters: [ function(r) {
      e = r.r, n = r.c, t = r.h, i = r.H, c = r.g;
    } ],
    execute: function() {
      r("gxg_breadcrumb", /** @class */ function() {
        function class_1(r) {
          e(this, r), this.breadcrumbClicked = n(this, "breadcrumbClicked", 7);
        }
        return class_1.prototype.breadcrumbClickedFunc = function() {
          var r = parseInt(this.el.getAttribute("data-index"), 10);
          this.breadcrumbClicked.emit({
            index: r
          });
        }, class_1.prototype.render = function() {
          var r = this;
          return t(i, null, t("div", {
            class: {
              container: !0
            },
            onClick: function() {
              return r.breadcrumbClickedFunc();
            }
          }, void 0 !== this.icon ? t("gxg-icon", {
            class: {
              "custom-icon": !0
            },
            type: this.icon,
            size: "regular",
            color: "auto"
          }) : null, t("slot", null)), t("gxg-icon", {
            class: {
              "arrow-icon": !0
            },
            type: "navigation/chevron-right"
          }));
        }, Object.defineProperty(class_1.prototype, "el", {
          get: function() {
            return c(this);
          },
          enumerable: !1,
          configurable: !0
        }), class_1;
      }()).style = ":host{display:-ms-inline-flexbox;display:inline-flex;-ms-flex-align:center;align-items:center}:host .container{display:-ms-flexbox;display:flex;font-family:var(--font-family-primary);font-size:var(--font-size-sm);background-color:var(--gray-01);height:var(--spacing-comp-05);border-radius:var(--border-radius-lg);color:var(--color-on-secondary);-ms-flex-align:center;align-items:center;-ms-flex-pack:center;justify-content:center;padding-left:var(--spacing-comp-02);padding-right:var(--spacing-comp-02);cursor:pointer}:host .container:hover{background-color:var(--gray-02)}:host .container .custom-icon{margin-right:var(--spacing-comp-01)}:host(:last-child) .arrow-icon{display:none}";
    }
  };
}));