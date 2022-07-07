System.register([ "./p-d8a20c31.system.js" ], (function(n) {
  "use strict";
  var t, e;
  return {
    setters: [ function(n) {
      t = n.f, e = n.i;
    } ],
    execute: function() {
      var r, o, u, c, i, f, s, a, isConnected = function(n) {
        return !("isConnected" in n) || n.isConnected;
      }, g = (r = function(n) {
        for (var t = 0, e = n.keys(); t < e.length; t++) {
          var r = e[t];
          n.set(r, n.get(r).filter(isConnected));
        }
      }, o = 2e3, function() {
        for (var n = [], t = 0; t < arguments.length; t++) n[t] = arguments[t];
        u && clearTimeout(u), u = setTimeout((function() {
          u = 0, r.apply(void 0, n);
        }), o);
      }), removeFromArray = function(n, t) {
        var e = n.indexOf(t);
        e >= 0 && (n[e] = n[n.length - 1], n.length--);
      }, l = (c = {
        large: document.documentElement.classList.contains("gxg-large")
      }, a = function(n, t) {
        void 0 === t && (t = function(n, t) {
          return n !== t;
        });
        var e = new Map(Object.entries(null != n ? n : {})), r = {
          dispose: [],
          get: [],
          set: [],
          reset: []
        }, reset = function() {
          e = new Map(Object.entries(null != n ? n : {})), r.reset.forEach((function(n) {
            return n();
          }));
        }, get = function(n) {
          return r.get.forEach((function(t) {
            return t(n);
          })), e.get(n);
        }, set = function(n, o) {
          var u = e.get(n);
          t(o, u, n) && (e.set(n, o), r.set.forEach((function(t) {
            return t(n, o, u);
          })));
        }, o = "undefined" == typeof Proxy ? {} : new Proxy(n, {
          get: function(n, t) {
            return get(t);
          },
          ownKeys: function(n) {
            return Array.from(e.keys());
          },
          getOwnPropertyDescriptor: function() {
            return {
              enumerable: !0,
              configurable: !0
            };
          },
          has: function(n, t) {
            return e.has(t);
          },
          set: function(n, t, e) {
            return set(t, e), !0;
          }
        }), on = function(n, t) {
          return r[n].push(t), function() {
            removeFromArray(r[n], t);
          };
        };
        return {
          state: o,
          get: get,
          set: set,
          on: on,
          onChange: function(t, e) {
            var r = on("set", (function(n, r) {
              n === t && e(r);
            })), o = on("reset", (function() {
              return e(n[t]);
            }));
            return function() {
              r(), o();
            };
          },
          use: function() {
            for (var n = [], t = 0; t < arguments.length; t++) n[t] = arguments[t];
            return n.forEach((function(n) {
              n.set && on("set", n.set), n.get && on("get", n.get), n.reset && on("reset", n.reset);
            }));
          },
          dispose: function() {
            // Call first dispose as resetting the state would
            // cause less updates ;)
            r.dispose.forEach((function(n) {
              return n();
            })), reset();
          },
          reset: reset,
          forceUpdate: function(n) {
            var t = e.get(n);
            r.set.forEach((function(e) {
              return e(n, t, t);
            }));
          }
        };
      }(c, i), f = a.on, s = new Map, "function" == typeof t && (
      // If we are not in a stencil project, we do nothing.
      // This function is not really exported by @stencil/core.
      f("dispose", (function() {
        s.clear();
      })), f("get", (function(n) {
        var e = t();
        e && function(n, t, e) {
          var r = n.get(t);
          r ? r.includes(e) || r.push(e) : n.set(t, [ e ]);
        }(s, n, e);
      })), f("set", (function(n) {
        var t = s.get(n);
        t && s.set(n, t.filter(e)), g(s);
      })), f("reset", (function() {
        s.forEach((function(n) {
          return n.forEach(e);
        })), g(s);
      }))), a).state;
      n("s", l);
    }
  };
}));