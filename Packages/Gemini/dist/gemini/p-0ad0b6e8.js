/*
 Stencil Client Patch v1.17.4 | MIT Licensed | https://stenciljs.com
 */
var StyleNode = function() {
  this.start = 0, this.end = 0, this.previous = null, this.parent = null, this.rules = null, 
  this.parsedCssText = "", this.cssText = "", this.atRule = !1, this.type = 0, this.keyframesName = "", 
  this.selector = "", this.parsedSelector = "";
};

function parse(c) {
  return function parseCss(n, r) {
    var c = r.substring(n.start, n.end - 1);
    if (n.parsedCssText = n.cssText = c.trim(), n.parent) {
      var f = n.previous ? n.previous.end : n.parent.start;
      c = (c = (c = function(t) {
        return t.replace(/\\([0-9a-f]{1,6})\s/gi, (function() {
          for (var t = arguments[1], n = 6 - t.length; n--; ) t = "0" + t;
          return "\\" + t;
        }));
      }(c = r.substring(f, n.start - 1))).replace(i.multipleSpaces, " ")).substring(c.lastIndexOf(";") + 1);
      var l = n.parsedSelector = n.selector = c.trim();
      n.atRule = 0 === l.indexOf(a), n.atRule ? 0 === l.indexOf(u) ? n.type = t.MEDIA_RULE : l.match(i.keyframesRule) && (n.type = t.KEYFRAMES_RULE, 
      n.keyframesName = n.selector.split(i.multipleSpaces).pop()) : 0 === l.indexOf(o) ? n.type = t.MIXIN_RULE : n.type = t.STYLE_RULE;
    }
    var v = n.rules;
    if (v) for (var p = 0, h = v.length, m = void 0; p < h && (m = v[p]); p++) parseCss(m, r);
    return n;
  }(function(t) {
    var i = new StyleNode;
    i.start = 0, i.end = t.length;
    for (var o = i, u = 0, a = t.length; u < a; u++) if (t[u] === n) {
      o.rules || (o.rules = []);
      var c = o, f = c.rules[c.rules.length - 1] || null;
      (o = new StyleNode).start = u + 1, o.parent = c, o.previous = f, c.rules.push(o);
    } else t[u] === r && (o.end = u + 1, o = o.parent || i);
    return i;
  }(c = function(t) {
    return t.replace(i.comments, "").replace(i.port, "");
  }(c)), c);
}

var t = {
  STYLE_RULE: 1,
  KEYFRAMES_RULE: 7,
  MEDIA_RULE: 4,
  MIXIN_RULE: 1e3
}, n = "{", r = "}", i = {
  comments: /\/\*[^*]*\*+([^/*][^*]*\*+)*\//gim,
  port: /@import[^;]*;/gim,
  customProp: /(?:^[^;\-\s}]+)?--[^;{}]*?:[^{};]*?(?:[;\n]|$)/gim,
  mixinProp: /(?:^[^;\-\s}]+)?--[^;{}]*?:[^{};]*?{[^}]*?}(?:[;\n]|$)?/gim,
  mixinApply: /@apply\s*\(?[^);]*\)?\s*(?:[;\n]|$)?/gim,
  varApply: /[^;:]*?:[^;]*?var\([^;]*\)(?:[;\n]|$)?/gim,
  keyframesRule: /^@[^\s]*keyframes/,
  multipleSpaces: /\s+/g
}, o = "--", u = "@media", a = "@";

function findRegex(t, n, r) {
  t.lastIndex = 0;
  var i = n.substring(r).match(t);
  if (i) {
    var o = r + i.index;
    return {
      start: o,
      end: o + i[0].length
    };
  }
  return null;
}

var c = /\bvar\(/, f = /\B--[\w-]+\s*:/, l = /\/\*[^*]*\*+([^/*][^*]*\*+)*\//gim, v = /^[\t ]+\n/gm;

function compileVar(t, n, r) {
  var i = function(t, n) {
    var r = findRegex(c, t, n);
    if (!r) return null;
    var i = function(t, n) {
      for (var r = 0, i = n; i < t.length; i++) {
        var o = t[i];
        if ("(" === o) r++; else if (")" === o && --r <= 0) return i + 1;
      }
      return i;
    }(t, r.start), o = t.substring(r.end, i - 1).split(","), u = o[0], a = o.slice(1);
    return {
      start: r.start,
      end: i,
      propName: u.trim(),
      fallback: a.length > 0 ? a.join(",").trim() : void 0
    };
  }(t, r);
  if (!i) return n.push(t.substring(r, t.length)), t.length;
  var o = i.propName, u = null != i.fallback ? compileTemplate(i.fallback) : void 0;
  return n.push(t.substring(r, i.start), (function(t) {
    return function(t, n, r) {
      return t[n] ? t[n] : r ? executeTemplate(r, t) : "";
    }(t, o, u);
  })), i.end;
}

function executeTemplate(t, n) {
  for (var r = "", i = 0; i < t.length; i++) {
    var o = t[i];
    r += "string" == typeof o ? o : o(n);
  }
  return r;
}

function findEndValue(t, n) {
  for (var r = !1, i = !1, o = n; o < t.length; o++) {
    var u = t[o];
    if (r) i && '"' === u && (r = !1), i || "'" !== u || (r = !1); else if ('"' === u) r = !0, 
    i = !0; else if ("'" === u) r = !0, i = !1; else {
      if (";" === u) return o + 1;
      if ("}" === u) return o;
    }
  }
  return o;
}

function compileTemplate(t) {
  var n = 0;
  t = function(t) {
    for (var n = "", r = 0; ;) {
      var i = findRegex(f, t, r), o = i ? i.start : t.length;
      if (n += t.substring(r, o), !i) break;
      r = findEndValue(t, o);
    }
    return n;
  }(t = t.replace(l, "")).replace(v, "");
  for (var r = []; n < t.length; ) n = compileVar(t, r, n);
  return r;
}

function resolveValues(t) {
  var n = {};
  t.forEach((function(t) {
    t.declarations.forEach((function(t) {
      n[t.prop] = t.value;
    }));
  }));
  for (var r = {}, i = Object.entries(n), s = function(t) {
    var n = !1;
    if (i.forEach((function(t) {
      var i = t[0], o = executeTemplate(t[1], r);
      o !== r[i] && (r[i] = o, n = !0);
    })), !n) return "break";
  }, o = 0; o < 10 && "break" !== s(); o++) ;
  return r;
}

function getSelectors(n, r) {
  if (void 0 === r && (r = 0), !n.rules) return [];
  var i = [];
  return n.rules.filter((function(n) {
    return n.type === t.STYLE_RULE;
  })).forEach((function(t) {
    var n = function(t) {
      for (var n, r = []; n = p.exec(t.trim()); ) {
        var i = normalizeValue(n[2]), o = i.value, u = i.important;
        r.push({
          prop: n[1].trim(),
          value: compileTemplate(o),
          important: u
        });
      }
      return r;
    }(t.cssText);
    n.length > 0 && t.parsedSelector.split(",").forEach((function(t) {
      t = t.trim(), i.push({
        selector: t,
        declarations: n,
        specificity: 1,
        nu: r
      });
    })), r++;
  })), i;
}

var p = /(?:^|[;\s{]\s*)(--[\w-]*?)\s*:\s*(?:((?:'(?:\\'|.)*?'|"(?:\\"|.)*?"|\([^)]*?\)|[^};{])+)|\{([^}]*)\}(?:(?=[;\s}])|$))/gm;

function normalizeValue(t) {
  var n = (t = t.replace(/\s+/gim, " ").trim()).endsWith("!important");
  return n && (t = t.substr(0, t.length - 10).trim()), {
    value: t,
    important: n
  };
}

function getSelectorsForScopes(t) {
  var n = [];
  return t.forEach((function(t) {
    n.push.apply(n, t.selectors);
  })), n;
}

function parseCSS(t) {
  var n = parse(t), r = compileTemplate(t);
  return {
    original: t,
    template: r,
    selectors: getSelectors(n),
    usesCssVars: r.length > 1
  };
}

function addGlobalStyle(t, n) {
  if (t.some((function(t) {
    return t.styleEl === n;
  }))) return !1;
  var r = parseCSS(n.textContent);
  return r.styleEl = n, t.push(r), !0;
}

function updateGlobalScopes(t) {
  var n = resolveValues(getSelectorsForScopes(t));
  t.forEach((function(t) {
    t.usesCssVars && (t.styleEl.textContent = executeTemplate(t.template, n));
  }));
}

function replaceScope(t, n, r) {
  return function(t, n, r) {
    return t.replace(RegExp(n, "g"), r);
  }(t, "\\." + n, "." + r);
}

function loadDocument(t, n) {
  return loadDocumentStyles(t, n), function(t, n) {
    for (var r = [], i = t.querySelectorAll('link[rel="stylesheet"][href]:not([data-no-shim])'), o = 0; o < i.length; o++) r.push(addGlobalLink(t, n, i[o]));
    return Promise.all(r);
  }(t, n).then((function() {
    updateGlobalScopes(n);
  }));
}

function loadDocumentStyles(t, n) {
  return Array.from(t.querySelectorAll("style:not([data-styles]):not([data-no-shim])")).map((function(t) {
    return addGlobalStyle(n, t);
  })).some(Boolean);
}

function addGlobalLink(t, n, r) {
  var i = r.href;
  return fetch(i).then((function(t) {
    return t.text();
  })).then((function(o) {
    if (function(t) {
      return t.indexOf("var(") > -1 || h.test(t);
    }(o) && r.parentNode) {
      (function(t) {
        return m.lastIndex = 0, m.test(t);
      })(o) && (o = function(t, n) {
        var r = n.replace(/[^/]*$/, "");
        return t.replace(m, (function(t, n) {
          var i = r + n;
          return t.replace(n, i);
        }));
      }(o, i));
      var u = t.createElement("style");
      u.setAttribute("data-styles", ""), u.textContent = o, addGlobalStyle(n, u), r.parentNode.insertBefore(u, r), 
      r.remove();
    }
  })).catch((function(t) {
    console.error(t);
  }));
}

var h = /[\s;{]--[-a-zA-Z0-9]+\s*:/m;

var m = /url[\s]*\([\s]*['"]?(?!(?:https?|data)\:|\/)([^\'\"\)]*)[\s]*['"]?\)[\s]*/gim;

var d = function() {
  function e(t, n) {
    this.win = t, this.doc = n, this.count = 0, this.hostStyleMap = new WeakMap, this.hostScopeMap = new WeakMap, 
    this.globalScopes = [], this.scopesMap = new Map, this.didInit = !1;
  }
  return e.prototype.i = function() {
    var t = this;
    return this.didInit || !this.win.requestAnimationFrame ? Promise.resolve() : (this.didInit = !0, 
    new Promise((function(n) {
      t.win.requestAnimationFrame((function() {
        (function(t, n) {
          "undefined" != typeof MutationObserver && new MutationObserver((function() {
            loadDocumentStyles(t, n) && updateGlobalScopes(n);
          })).observe(document.head, {
            childList: !0
          });
        })(t.doc, t.globalScopes), loadDocument(t.doc, t.globalScopes).then((function() {
          return n();
        }));
      }));
    })));
  }, e.prototype.addLink = function(t) {
    var n = this;
    return addGlobalLink(this.doc, this.globalScopes, t).then((function() {
      n.updateGlobal();
    }));
  }, e.prototype.addGlobalStyle = function(t) {
    addGlobalStyle(this.globalScopes, t) && this.updateGlobal();
  }, e.prototype.createHostStyle = function(t, n, r, i) {
    if (this.hostScopeMap.has(t)) throw Error("host style already created");
    var o = this.registerHostTemplate(r, n, i), u = this.doc.createElement("style");
    return u.setAttribute("data-no-shim", ""), o.usesCssVars ? i ? (u["s-sc"] = n = o.scopeId + "-" + this.count, 
    u.textContent = "/*needs update*/", this.hostStyleMap.set(t, u), this.hostScopeMap.set(t, function(t, n) {
      var r = t.template.map((function(r) {
        return "string" == typeof r ? replaceScope(r, t.scopeId, n) : r;
      })), i = t.selectors.map((function(r) {
        return Object.assign(Object.assign({}, r), {
          selector: replaceScope(r.selector, t.scopeId, n)
        });
      }));
      return Object.assign(Object.assign({}, t), {
        template: r,
        selectors: i,
        scopeId: n
      });
    }(o, n)), this.count++) : (o.styleEl = u, o.usesCssVars || (u.textContent = executeTemplate(o.template, {})), 
    this.globalScopes.push(o), this.updateGlobal(), this.hostScopeMap.set(t, o)) : u.textContent = r, 
    u;
  }, e.prototype.removeHost = function(t) {
    var n = this.hostStyleMap.get(t);
    n && n.remove(), this.hostStyleMap.delete(t), this.hostScopeMap.delete(t);
  }, e.prototype.updateHost = function(t) {
    var n = this.hostScopeMap.get(t);
    if (n && n.usesCssVars && n.isScoped) {
      var r = this.hostStyleMap.get(t);
      if (r) {
        var i = resolveValues(function(t, n, r) {
          var i = [], o = function(t, n) {
            for (var r = []; n; ) {
              var i = t.get(n);
              i && r.push(i), n = n.parentElement;
            }
            return r;
          }(n, t);
          return r.forEach((function(t) {
            return i.push(t);
          })), o.forEach((function(t) {
            return i.push(t);
          })), function(t) {
            return t.sort((function(t, n) {
              return t.specificity === n.specificity ? t.nu - n.nu : t.specificity - n.specificity;
            })), t;
          }(getSelectorsForScopes(i).filter((function(n) {
            return function(t, n) {
              return ":root" === n || "html" === n || t.matches(n);
            }(t, n.selector);
          })));
        }(t, this.hostScopeMap, this.globalScopes));
        r.textContent = executeTemplate(n.template, i);
      }
    }
  }, e.prototype.updateGlobal = function() {
    updateGlobalScopes(this.globalScopes);
  }, e.prototype.registerHostTemplate = function(t, n, r) {
    var i = this.scopesMap.get(n);
    return i || ((i = parseCSS(t)).scopeId = n, i.isScoped = r, this.scopesMap.set(n, i)), 
    i;
  }, e;
}();

!function(t) {
  !t || t.__cssshim || t.CSS && t.CSS.supports && t.CSS.supports("color", "var(--c)") || (t.__cssshim = new d(t, t.document));
}("undefined" != typeof window && window);