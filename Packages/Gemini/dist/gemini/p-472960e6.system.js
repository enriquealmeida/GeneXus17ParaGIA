System.register([], (function(r) {
  "use strict";
  return {
    execute: function() {
      r({
        c: function(r, e, t) {
          return r(t = {
            path: e,
            exports: {},
            require: function(r, e) {
              return function() {
                throw new Error("Dynamic requires are not currently supported by @rollup/plugin-commonjs");
              }();
            }
          }, t.exports), t.exports;
        },
        u: function(r) {
          return r && r.__esModule && Object.prototype.hasOwnProperty.call(r, "default") ? r.default : r;
        }
      });
    }
  };
}));