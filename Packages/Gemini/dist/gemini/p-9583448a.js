function unwrapExports(r) {
  return r && r.__esModule && Object.prototype.hasOwnProperty.call(r, "default") ? r.default : r;
}

function createCommonjsModule(r, e, o) {
  return r(o = {
    path: e,
    exports: {},
    require: function(r, e) {
      return function() {
        throw new Error("Dynamic requires are not currently supported by @rollup/plugin-commonjs");
      }();
    }
  }, o.exports), o.exports;
}

export { createCommonjsModule as c, unwrapExports as u }