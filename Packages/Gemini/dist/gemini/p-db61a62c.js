import { C as e, p as o, w as n, a as s, d as t, N as r } from "./p-eb9dc661.js";

/*
 Stencil Client Patch v1.17.4 | MIT Licensed | https://stenciljs.com
 */ const a = "undefined" != typeof Deno, i = !(a || "undefined" == typeof global || "function" != typeof require || !global.process || "string" != typeof __filename || global.origin && "string" == typeof global.origin), p = (a && Deno.build.os, 
i ? process.cwd : a && Deno.cwd, i ? process.exit : a && Deno.exit, () => 
// NOTE!! This fn cannot use async/await!
// @ts-ignore
e && e.supports && e.supports("color", "var(--c)") ? s() : __sc_import_gemini(/* webpackChunkName: "polyfills-css-shim" */ "./p-0ad0b6e8.js").then(() => (o._$$cssShim$$_ = n.__cssshim) ? o._$$cssShim$$_.i() : 0)), patchBrowser = () => {
  // shim css vars
  o._$$cssShim$$_ = n.__cssshim;
  // @ts-ignore
  const e = Array.from(t.querySelectorAll("script")).find(e => RegExp(`/${r}(\\.esm)?\\.js($|\\?|#)`).test(e.src) || e.getAttribute("data-stencil-namespace") === r), a = e["data-opts"] || {};
  return "onbeforeload" in e && !history.scrollRestoration /* IS_ESM_BUILD */ ? {
    then() {
      /* promise noop */}
  } : (a.resourcesUrl = new URL(".", new URL(e.getAttribute("data-resources-url") || e.src, n.location.href)).href, 
  patchDynamicImport(a.resourcesUrl, e), n.customElements ? s(a) : __sc_import_gemini(/* webpackChunkName: "polyfills-dom" */ "./p-3d1015c2.js").then(() => a));
}, patchDynamicImport = (e, o) => {
  const s = (e => "__sc_import_" + e.replace(/\s|-/g, "_"))(r);
  try {
    // test if this browser supports dynamic imports
    // There is a caching issue in V8, that breaks using import() in Function
    // By generating a random string, we can workaround it
    // Check https://bugs.chromium.org/p/chromium/issues/detail?id=990810 for more info
    n[s] = Function("w", "return import(w);//" + Math.random());
  } catch (a) {
    // this shim is specifically for browsers that do support "esm" imports
    // however, they do NOT support "dynamic" imports
    // basically this code is for old Edge, v18 and below
    const r = new Map;
    n[s] = a => {
      const i = new URL(a, e).href;
      let p = r.get(i);
      if (!p) {
        const e = t.createElement("script");
        e.type = "module", e.crossOrigin = o.crossOrigin, e.src = URL.createObjectURL(new Blob([ `import * as m from '${i}'; window.${s}.m = m;` ], {
          type: "application/javascript"
        })), p = new Promise(o => {
          e.onload = () => {
            o(n[s].m), e.remove();
          };
        }), r.set(i, p), t.head.appendChild(e);
      }
      return p;
    };
  }
};

export { p as a, patchBrowser as p }