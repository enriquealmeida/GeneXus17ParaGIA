System.register([ "./p-d8a20c31.system.js" ], (function(e, t) {
  "use strict";
  var r, n, s, o, i, c;
  return {
    setters: [ function(e) {
      r = e.C, n = e.p, s = e.w, o = e.a, i = e.d, c = e.N;
    } ],
    execute: function() {
      /*
             Stencil Client Patch v1.17.4 | MIT Licensed | https://stenciljs.com
             */
      var a = "undefined" != typeof Deno, u = !(a || "undefined" == typeof global || "function" != typeof require || !global.process || "string" != typeof __filename || global.origin && "string" == typeof global.origin), p = (a && Deno.build.os, 
      u ? process.cwd : a && Deno.cwd, u ? process.exit : a && Deno.exit, e("a", (function() {
        // NOTE!! This fn cannot use async/await!
        // @ts-ignore
        return r && r.supports && r.supports("color", "var(--c)") ? o() : t.import(/* webpackChunkName: "polyfills-css-shim" */ "./p-fb53799e.system.js").then((function() {
          return (n.$cssShim$ = s.__cssshim) ? n.$cssShim$.i() : 0;
        }));
      })), e("p", (function() {
        // shim css vars
        n.$cssShim$ = s.__cssshim;
        // @ts-ignore
        var e = Array.from(i.querySelectorAll("script")).find((function(e) {
          return new RegExp("/" + c + "(\\.esm)?\\.js($|\\?|#)").test(e.src) || e.getAttribute("data-stencil-namespace") === c;
        })), r = e["data-opts"] || {};
        return "onbeforeload" in e && !history.scrollRestoration /* IS_ESM_BUILD */ ? {
          then: function() {
            /* promise noop */}
        } : (r.resourcesUrl = new URL(".", new URL(e.getAttribute("data-resources-url") || e.src, s.location.href)).href, 
        p(r.resourcesUrl, e), s.customElements ? o(r) : t.import(/* webpackChunkName: "polyfills-dom" */ "./p-75076c56.system.js").then((function() {
          return r;
        })));
      })), function(e, t) {
        var r = "__sc_import_" + c.replace(/\s|-/g, "_");
        try {
          // test if this browser supports dynamic imports
          // There is a caching issue in V8, that breaks using import() in Function
          // By generating a random string, we can workaround it
          // Check https://bugs.chromium.org/p/chromium/issues/detail?id=990810 for more info
          s[r] = new Function("w", "return import(w);//" + Math.random());
        } catch (o) {
          // this shim is specifically for browsers that do support "esm" imports
          // however, they do NOT support "dynamic" imports
          // basically this code is for old Edge, v18 and below
          var n = new Map;
          s[r] = function(o) {
            var c = new URL(o, e).href, a = n.get(c);
            if (!a) {
              var u = i.createElement("script");
              u.type = "module", u.crossOrigin = t.crossOrigin, u.src = URL.createObjectURL(new Blob([ "import * as m from '" + c + "'; window." + r + ".m = m;" ], {
                type: "application/javascript"
              })), a = new Promise((function(e) {
                u.onload = function() {
                  e(s[r].m), u.remove();
                };
              })), n.set(c, a), i.head.appendChild(u);
            }
            return a;
          };
        }
      });
    }
  };
}));