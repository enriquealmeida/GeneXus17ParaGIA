System.register([ "./p-d8a20c31.system.js", "./p-08984d7d.system.js" ], (function(t) {
  "use strict";
  var s, e, r, n;
  return {
    setters: [ function(t) {
      s = t.r, e = t.h, r = t.H;
    }, function(t) {
      n = t.s;
    } ],
    execute: function() {
      t("gxg_test", /** @class */ function() {
        function class_1(t) {
          s(this, t);
        }
        return class_1.prototype.render = function() {
          return e(r, {
            class: {
              large: n.large
            }
          }, "hola");
        }, class_1;
      }()).style = ":host{background-color:red}:host(.large){background-color:yellow}";
    }
  };
}));