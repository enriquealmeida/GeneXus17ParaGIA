var __spreadArrays = this && this.__spreadArrays || function() {
  for (var t = 0, r = 0, e = arguments.length; r < e; r++) t += arguments[r].length;
  var n = Array(t), s = 0;
  for (r = 0; r < e; r++) for (var o = arguments[r], c = 0, a = o.length; c < a; c++, 
  s++) n[s] = o[c];
  return n;
};

System.register([], (function(t) {
  "use strict";
  return {
    execute: function() {
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
      var r = ")(?:\\(((?:\\([^)(]*\\)|[^)(]*)+?)\\))?([^,{]*)", e = new RegExp("(-shadowcsshost" + r, "gim"), n = new RegExp("(-shadowcsscontext" + r, "gim"), s = new RegExp("(-shadowcssslotted" + r, "gim"), o = /-shadowcsshost-no-combinator([^\s]*)/, c = [ /::shadow/g, /::content/g ], a = /-shadowcsshost/gim, i = /:host/gim, u = /::slotted/gim, l = /:host-context/gim, h = /\/\*\s*[\s\S]*?\*\//g, f = /\/\*\s*#\s*source(Mapping)?URL=[\s\S]+?\*\//g, p = /(\s*)([^;\{\}]+?)(\s*)((?:{%BLOCK%}?\s*;?)|(?:\s*;))/g, d = /([{}])/g, processRules = function(t, r) {
        var e = escapeBlocks(t), n = 0;
        return e.escapedString.replace(p, (function() {
          for (var t = [], s = 0; s < arguments.length; s++) t[s] = arguments[s];
          var o = t[2], c = "", a = t[4], i = "";
          a && a.startsWith("{%BLOCK%") && (c = e.blocks[n++], a = a.substring("%BLOCK%".length + 1), 
          i = "{");
          var u = {
            selector: o,
            content: c
          }, l = r(u);
          return "" + t[1] + l.selector + t[3] + i + l.content + a;
        }));
      }, escapeBlocks = function(t) {
        for (var r = t.split(d), e = [], n = [], s = 0, o = [], c = 0; c < r.length; c++) {
          var a = r[c];
          "}" === a && s--, s > 0 ? o.push(a) : (o.length > 0 && (n.push(o.join("")), e.push("%BLOCK%"), 
          o = []), e.push(a)), "{" === a && s++;
        }
        return o.length > 0 && (n.push(o.join("")), e.push("%BLOCK%")), {
          escapedString: e.join(""),
          blocks: n
        };
      }, convertColonRule = function(t, r, e) {
        // m[1] = :host(-context), m[2] = contents of (), m[3] rest of rule
        return t.replace(r, (function() {
          for (var t = [], r = 0; r < arguments.length; r++) t[r] = arguments[r];
          if (t[2]) {
            for (var n = t[2].split(","), s = [], o = 0; o < n.length; o++) {
              var c = n[o].trim();
              if (!c) break;
              s.push(e("-shadowcsshost-no-combinator", c, t[3]));
            }
            return s.join(",");
          }
          return "-shadowcsshost-no-combinator" + t[3];
        }));
      }, colonHostPartReplacer = function(t, r, e) {
        return t + r.replace("-shadowcsshost", "") + e;
      }, colonHostContextPartReplacer = function(t, r, e) {
        return r.indexOf("-shadowcsshost") > -1 ? colonHostPartReplacer(t, r, e) : t + r + e + ", " + r + " " + t + e;
      }, selectorNeedsScoping = function(t, r) {
        return !function(t) {
          return t = t.replace(/\[/g, "\\[").replace(/\]/g, "\\]"), new RegExp("^(" + t + ")([>\\s~+[.,{:][\\s\\S]*)?$", "m");
        }(r).test(t);
      }, applyStrictSelectorScope = function(t, r, e) {
        for (var n, s = "." + (r = r.replace(/\[is=([^\]]*)\]/g, (function(t) {
          for (var r = [], e = 1; e < arguments.length; e++) r[e - 1] = arguments[e];
          return r[0];
        }))), _scopeSelectorPart = function(t) {
          var n = t.trim();
          if (!n) return "";
          if (t.indexOf("-shadowcsshost-no-combinator") > -1) n = function(t, r, e) {
            if (
            // In Android browser, the lastIndex is not reset when the regex is used in String.replace()
            a.lastIndex = 0, a.test(t)) {
              var n = "." + e;
              return t.replace(o, (function(t, r) {
                return r.replace(/([^:]*)(:*)(.*)/, (function(t, r, e, s) {
                  return r + n + e + s;
                }));
              })).replace(a, n + " ");
            }
            return r + " " + t;
          }(t, r, e); else {
            // remove :host since it should be unnecessary
            var c = t.replace(a, "");
            if (c.length > 0) {
              var i = c.match(/([^:]*)(:*)(.*)/);
              i && (n = i[1] + s + i[2] + i[3]);
            }
          }
          return n;
        }, c = function(t) {
          var r = [], e = 0;
          return {
            content: (
            // Replaces attribute selectors with placeholders.
            // The WS in [attr="va lue"] would otherwise be interpreted as a selector separator.
            t = t.replace(/(\[[^\]]*\])/g, (function(t, n) {
              var s = "__ph-" + e + "__";
              return r.push(n), e++, s;
            }))).replace(/(:nth-[-\w]+)(\([^)]+\))/g, (function(t, n, s) {
              var o = "__ph-" + e + "__";
              return r.push(s), e++, n + o;
            })),
            placeholders: r
          };
        }(t), i = "", u = 0, l = /( |>|\+|~(?!=))\s*/g, h = !((t = c.content).indexOf("-shadowcsshost-no-combinator") > -1); null !== (n = l.exec(t)); ) {
          var f = n[1], p = t.slice(u, n.index).trim();
          i += ((h = h || p.indexOf("-shadowcsshost-no-combinator") > -1) ? _scopeSelectorPart(p) : p) + " " + f + " ", 
          u = l.lastIndex;
        }
        var d, g = t.substring(u);
        // replace the placeholders with their original values
        return i += (h = h || g.indexOf("-shadowcsshost-no-combinator") > -1) ? _scopeSelectorPart(g) : g, 
        d = c.placeholders, i.replace(/__ph-(\d+)__/g, (function(t, r) {
          return d[+r];
        }));
      }, scopeSelectors = function(t, r, e, n, s) {
        return processRules(t, (function(t) {
          var s = t.selector, o = t.content;
          return "@" !== t.selector[0] ? s = function(t, r, e, n) {
            return t.split(",").map((function(t) {
              return n && t.indexOf("." + n) > -1 ? t.trim() : selectorNeedsScoping(t, r) ? applyStrictSelectorScope(t, r, e).trim() : t.trim();
            })).join(", ");
          }(t.selector, r, e, n) : (t.selector.startsWith("@media") || t.selector.startsWith("@supports") || t.selector.startsWith("@page") || t.selector.startsWith("@document")) && (o = scopeSelectors(t.content, r, e, n)), 
          {
            selector: s.replace(/\s{2,}/g, " ").trim(),
            content: o
          };
        }));
      }, scopeCssText = function(t, r, o, a, h) {
        var f = function(t, r) {
          var e = "." + r + " > ", n = [];
          return t = t.replace(s, (function() {
            for (var t = [], r = 0; r < arguments.length; r++) t[r] = arguments[r];
            if (t[2]) {
              for (var s = t[2].trim(), o = t[3], c = e + s + o, a = "", i = t[4] - 1; i >= 0; i--) {
                var u = t[5][i];
                if ("}" === u || "," === u) break;
                a = u + a;
              }
              var l = a + c, h = "" + a.trimRight() + c.trim();
              if (l.trim() !== h.trim()) {
                var f = h + ", " + l;
                n.push({
                  orgSelector: l,
                  updatedSelector: f
                });
              }
              return c;
            }
            return "-shadowcsshost-no-combinator" + t[3];
          })), {
            selectors: n,
            cssText: t
          };
        }(t = function(t) {
          return convertColonRule(t, n, colonHostContextPartReplacer);
        }(t = function(t) {
          return convertColonRule(t, e, colonHostPartReplacer);
        }(t = t.replace(l, "-shadowcsscontext").replace(i, "-shadowcsshost").replace(u, "-shadowcssslotted"))), a);
        return t = function(t) {
          return c.reduce((function(t, r) {
            return t.replace(r, " ");
          }), t);
        }(t = f.cssText), r && (t = scopeSelectors(t, r, o, a)), {
          cssText: (t = (t = t.replace(/-shadowcsshost-no-combinator/g, "." + o)).replace(/>\s*\*\s+([^{, ]+)/gm, " $1 ")).trim(),
          slottedSelectors: f.selectors
        };
      };
      t("scopeCss", (function(t, r, e) {
        var n = r + "-h", s = r + "-s", o = t.match(f) || [];
        t = function(t) {
          return t.replace(h, "");
        }(t);
        var c = [];
        if (e) {
          var processCommentedSelector_1 = function(t) {
            var r = "/*!@___" + c.length + "___*/", e = "/*!@" + t.selector + "*/";
            return c.push({
              placeholder: r,
              comment: e
            }), t.selector = r + t.selector, t;
          };
          t = processRules(t, (function(t) {
            return "@" !== t.selector[0] ? processCommentedSelector_1(t) : t.selector.startsWith("@media") || t.selector.startsWith("@supports") || t.selector.startsWith("@page") || t.selector.startsWith("@document") ? (t.content = processRules(t.content, processCommentedSelector_1), 
            t) : t;
          }));
        }
        var a = scopeCssText(t, r, n, s);
        return t = __spreadArrays([ a.cssText ], o).join("\n"), e && c.forEach((function(r) {
          var e = r.placeholder, n = r.comment;
          t = t.replace(e, n);
        })), a.slottedSelectors.forEach((function(r) {
          t = t.replace(r.orgSelector, r.updatedSelector);
        })), t;
      }));
    }
  };
}));