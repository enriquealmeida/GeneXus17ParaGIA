/*
 Stencil Client Platform v1.17.4 | MIT Licensed | https://stenciljs.com
 */
/**
 * @license
 * Copyright Google Inc. All Rights Reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file at https://angular.io/license
 *
 * This file is a port of shadowCSS from webcomponents.js to TypeScript.
 * https://github.com/webcomponents/webcomponentsjs/blob/4efecd7e0e/src/ShadowCSS/ShadowCSS.js
 * https://github.com/angular/angular/blob/master/packages/compiler/src/shadow_css.ts
 */
const s = /(-shadowcsshost)(?:\(((?:\([^)(]*\)|[^)(]*)+?)\))?([^,{]*)/gim, t = /(-shadowcsscontext)(?:\(((?:\([^)(]*\)|[^)(]*)+?)\))?([^,{]*)/gim, o = /(-shadowcssslotted)(?:\(((?:\([^)(]*\)|[^)(]*)+?)\))?([^,{]*)/gim, n = /-shadowcsshost-no-combinator([^\s]*)/, c = [ /::shadow/g, /::content/g ], e = /-shadowcsshost/gim, r = /:host/gim, h = /::slotted/gim, a = /:host-context/gim, d = /\/\*\s*[\s\S]*?\*\//g, l = /\/\*\s*#\s*source(Mapping)?URL=[\s\S]+?\*\//g, i = /(\s*)([^;\{\}]+?)(\s*)((?:{%BLOCK%}?\s*;?)|(?:\s*;))/g, g = /([{}])/g, processRules = (s, t) => {
  const o = escapeBlocks(s);
  let n = 0;
  return o.escapedString.replace(i, (...s) => {
    const c = s[2];
    let e = "", r = s[4], h = "";
    r && r.startsWith("{%BLOCK%") && (e = o.blocks[n++], r = r.substring(8), h = "{");
    const a = t({
      selector: c,
      content: e
    });
    return `${s[1]}${a.selector}${s[3]}${h}${a.content}${r}`;
  });
}, escapeBlocks = s => {
  const t = s.split(g), o = [], n = [];
  let c = 0, e = [];
  for (let r = 0; r < t.length; r++) {
    const s = t[r];
    "}" === s && c--, c > 0 ? e.push(s) : (e.length > 0 && (n.push(e.join("")), o.push("%BLOCK%"), 
    e = []), o.push(s)), "{" === s && c++;
  }
  e.length > 0 && (n.push(e.join("")), o.push("%BLOCK%"));
  return {
    escapedString: o.join(""),
    blocks: n
  };
}, convertColonRule = (s, t, o) => s.replace(t, (...s) => {
  if (s[2]) {
    const t = s[2].split(","), n = [];
    for (let c = 0; c < t.length; c++) {
      const e = t[c].trim();
      if (!e) break;
      n.push(o("-shadowcsshost-no-combinator", e, s[3]));
    }
    return n.join(",");
  }
  return "-shadowcsshost-no-combinator" + s[3];
}), colonHostPartReplacer = (s, t, o) => s + t.replace("-shadowcsshost", "") + o, colonHostContextPartReplacer = (s, t, o) => t.indexOf("-shadowcsshost") > -1 ? colonHostPartReplacer(s, t, o) : s + t + o + ", " + t + " " + s + o, selectorNeedsScoping = (s, t) => !(s => (s = s.replace(/\[/g, "\\[").replace(/\]/g, "\\]"), 
RegExp("^(" + s + ")([>\\s~+[.,{:][\\s\\S]*)?$", "m")))(t).test(s), applyStrictSelectorScope = (s, t, o) => {
  const c = "." + (t = t.replace(/\[is=([^\]]*)\]/g, (s, ...t) => t[0])), _scopeSelectorPart = s => {
    let r = s.trim();
    if (!r) return "";
    if (s.indexOf("-shadowcsshost-no-combinator") > -1) r = ((s, t, o) => {
      if (
      // In Android browser, the lastIndex is not reset when the regex is used in String.replace()
      e.lastIndex = 0, e.test(s)) {
        const t = "." + o;
        return s.replace(n, (s, o) => o.replace(/([^:]*)(:*)(.*)/, (s, o, n, c) => o + t + n + c)).replace(e, t + " ");
      }
      return t + " " + s;
    })(s, t, o); else {
      // remove :host since it should be unnecessary
      const t = s.replace(e, "");
      if (t.length > 0) {
        const s = t.match(/([^:]*)(:*)(.*)/);
        s && (r = s[1] + c + s[2] + s[3]);
      }
    }
    return r;
  }, r = (s => {
    const t = [];
    let o, n = 0;
    // Replaces the expression in `:nth-child(2n + 1)` with a placeholder.
    // WS and "+" would otherwise be interpreted as selector separators.
    o = (
    // Replaces attribute selectors with placeholders.
    // The WS in [attr="va lue"] would otherwise be interpreted as a selector separator.
    s = s.replace(/(\[[^\]]*\])/g, (s, o) => {
      const c = `__ph-${n}__`;
      return t.push(o), n++, c;
    })).replace(/(:nth-[-\w]+)(\([^)]+\))/g, (s, o, c) => {
      const e = `__ph-${n}__`;
      return t.push(c), n++, o + e;
    });
    return {
      content: o,
      placeholders: t
    };
  })(s);
  let h, a = "", d = 0;
  const l = /( |>|\+|~(?!=))\s*/g;
  // If a selector appears before :host it should not be shimmed as it
  // matches on ancestor elements and not on elements in the host's shadow
  // `:host-context(div)` is transformed to
  // `-shadowcsshost-no-combinatordiv, div -shadowcsshost-no-combinator`
  // the `div` is not part of the component in the 2nd selectors and should not be scoped.
  // Historically `component-tag:host` was matching the component so we also want to preserve
  // this behavior to avoid breaking legacy apps (it should not match).
  // The behavior should be:
  // - `tag:host` -> `tag[h]` (this is to avoid breaking legacy apps, should not match anything)
  // - `tag :host` -> `tag [h]` (`tag` is not scoped because it's considered part of a
  //   `:host-context(tag)`)
    // Only scope parts after the first `-shadowcsshost-no-combinator` when it is present
  let i = !((s = r.content).indexOf("-shadowcsshost-no-combinator") > -1);
  for (;null !== (h = l.exec(s)); ) {
    const t = h[1], o = s.slice(d, h.index).trim();
    i = i || o.indexOf("-shadowcsshost-no-combinator") > -1;
    a += `${i ? _scopeSelectorPart(o) : o} ${t} `, d = l.lastIndex;
  }
  const g = s.substring(d);
  // replace the placeholders with their original values
  return i = i || g.indexOf("-shadowcsshost-no-combinator") > -1, a += i ? _scopeSelectorPart(g) : g, 
  ((s, t) => t.replace(/__ph-(\d+)__/g, (t, o) => s[+o]))(r.placeholders, a);
}, scopeSelectors = (s, t, o, n, c) => processRules(s, s => {
  let c = s.selector, e = s.content;
  "@" !== s.selector[0] ? c = ((s, t, o, n) => s.split(",").map(s => n && s.indexOf("." + n) > -1 ? s.trim() : selectorNeedsScoping(s, t) ? applyStrictSelectorScope(s, t, o).trim() : s.trim()).join(", "))(s.selector, t, o, n) : (s.selector.startsWith("@media") || s.selector.startsWith("@supports") || s.selector.startsWith("@page") || s.selector.startsWith("@document")) && (e = scopeSelectors(s.content, t, o, n));
  return {
    selector: c.replace(/\s{2,}/g, " ").trim(),
    content: e
  };
}), scopeCssText = (n, e, d, l, i) => {
  const g = ((s, t) => {
    const n = "." + t + " > ", c = [];
    return s = s.replace(o, (...s) => {
      if (s[2]) {
        const t = s[2].trim(), o = s[3], e = n + t + o;
        let r = "";
        for (let n = s[4] - 1; n >= 0; n--) {
          const t = s[5][n];
          if ("}" === t || "," === t) break;
          r = t + r;
        }
        const h = r + e, a = `${r.trimRight()}${e.trim()}`;
        if (h.trim() !== a.trim()) {
          const s = `${a}, ${h}`;
          c.push({
            orgSelector: h,
            updatedSelector: s
          });
        }
        return e;
      }
      return "-shadowcsshost-no-combinator" + s[3];
    }), {
      selectors: c,
      cssText: s
    };
  })(n = (s => convertColonRule(s, t, colonHostContextPartReplacer))(n = (t => convertColonRule(t, s, colonHostPartReplacer))(n = (s => s = s.replace(a, "-shadowcsscontext").replace(r, "-shadowcsshost").replace(h, "-shadowcssslotted"))(n))), l);
  return n = (s => c.reduce((s, t) => s.replace(t, " "), s))(n = g.cssText), e && (n = scopeSelectors(n, e, d, l)), 
  {
    cssText: (n = (n = n.replace(/-shadowcsshost-no-combinator/g, "." + d)).replace(/>\s*\*\s+([^{, ]+)/gm, " $1 ")).trim(),
    slottedSelectors: g.selectors
  };
}, scopeCss = (s, t, o) => {
  const n = t + "-h", c = t + "-s", e = (s => s.match(l) || [])(s);
  s = (s => s.replace(d, ""))(s);
  const r = [];
  if (o) {
    const processCommentedSelector = s => {
      const t = `/*!@___${r.length}___*/`, o = `/*!@${s.selector}*/`;
      return r.push({
        placeholder: t,
        comment: o
      }), s.selector = t + s.selector, s;
    };
    s = processRules(s, s => "@" !== s.selector[0] ? processCommentedSelector(s) : s.selector.startsWith("@media") || s.selector.startsWith("@supports") || s.selector.startsWith("@page") || s.selector.startsWith("@document") ? (s.content = processRules(s.content, processCommentedSelector), 
    s) : s);
  }
  const h = scopeCssText(s, t, n, c);
  return s = [ h.cssText, ...e ].join("\n"), o && r.forEach(({placeholder: t, comment: o}) => {
    s = s.replace(t, o);
  }), h.slottedSelectors.forEach(t => {
    s = s.replace(t.orgSelector, t.updatedSelector);
  }), s;
};

export { scopeCss }