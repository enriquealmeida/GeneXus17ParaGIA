System.register([ "./p-d8a20c31.system.js" ], (function(t) {
  "use strict";
  var e, r, n, s;
  return {
    setters: [ function(t) {
      e = t.r, r = t.h, n = t.H, s = t.g;
    } ],
    execute: function() {
      t("gxg_breadcrumbs", /** @class */ function() {
        function class_1(t) {
          e(this, t);
        }
        return class_1.prototype.componentWillLoad = function() {
          this.setIndexToBreadcrumb();
        }, class_1.prototype.setIndexToBreadcrumb = function() {
          this.el.querySelectorAll("gxg-breadcrumb").forEach((function(t, e) {
            t.setAttribute("data-index", e.toString());
          }));
        }, class_1.prototype.render = function() {
          return r(n, null, r("slot", null));
        }, Object.defineProperty(class_1.prototype, "el", {
          get: function() {
            return s(this);
          },
          enumerable: !1,
          configurable: !0
        }), class_1;
      }()).style = ":host{display:-ms-flexbox;display:flex}";
    }
  };
}));