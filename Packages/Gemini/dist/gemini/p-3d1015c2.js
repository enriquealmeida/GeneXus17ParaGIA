/*
 Stencil Client Patch v1.17.4 | MIT Licensed | https://stenciljs.com
 */
self, function() {
  var t = new Set("annotation-xml color-profile font-face font-face-src font-face-uri font-face-format font-face-name missing-glyph".split(" "));
  function g(i) {
    var o = t.has(i);
    return i = /^[a-z][.0-9_a-z]*-[\-.0-9_a-z]*$/.test(i), !o && i;
  }
  function l(t) {
    var i = t.isConnected;
    if (void 0 !== i) return i;
    for (;t && !(t.__CE_isImportDocument || t instanceof Document); ) t = t.parentNode || (window.ShadowRoot && t instanceof ShadowRoot ? t.host : void 0);
    return !(!t || !(t.__CE_isImportDocument || t instanceof Document));
  }
  function n(t, i) {
    for (;i && i !== t && !i.nextSibling; ) i = i.parentNode;
    return i && i !== t ? i.nextSibling : null;
  }
  function p(t, i, o) {
    o = void 0 === o ? new Set : o;
    for (var f = t; f; ) {
      if (f.nodeType === Node.ELEMENT_NODE) {
        var s = f;
        i(s);
        var h = s.localName;
        if ("link" === h && "import" === s.getAttribute("rel")) {
          if ((f = s.import) instanceof Node && !o.has(f)) for (o.add(f), f = f.firstChild; f; f = f.nextSibling) p(f, i, o);
          f = n(t, s);
          continue;
        }
        if ("template" === h) {
          f = n(t, s);
          continue;
        }
        if (s = s.__CE_shadowRoot) for (s = s.firstChild; s; s = s.nextSibling) p(s, i, o);
      }
      f = f.firstChild ? f.firstChild : n(t, f);
    }
  }
  function r(t, i, o) {
    t[i] = o;
  }
  function u() {
    this.a = new Map, this.g = new Map, this.c = [], this.f = [], this.b = !1;
  }
  function v(t, i) {
    t.b && p(i, (function(i) {
      return w(t, i);
    }));
  }
  function w(t, i) {
    if (t.b && !i.__CE_patched) {
      i.__CE_patched = !0;
      for (var o = 0; o < t.c.length; o++) t.c[o](i);
      for (o = 0; o < t.f.length; o++) t.f[o](i);
    }
  }
  function x(t, i) {
    var o = [];
    for (p(i, (function(t) {
      return o.push(t);
    })), i = 0; i < o.length; i++) {
      var f = o[i];
      1 === f.__CE_state ? t.connectedCallback(f) : y(t, f);
    }
  }
  function z(t, i) {
    var o = [];
    for (p(i, (function(t) {
      return o.push(t);
    })), i = 0; i < o.length; i++) {
      var f = o[i];
      1 === f.__CE_state && t.disconnectedCallback(f);
    }
  }
  function A(t, i, o) {
    var f = (o = void 0 === o ? {} : o).u || new Set, s = o.i || function(i) {
      return y(t, i);
    }, h = [];
    if (p(i, (function(i) {
      if ("link" === i.localName && "import" === i.getAttribute("rel")) {
        var o = i.import;
        o instanceof Node && (o.__CE_isImportDocument = !0, o.__CE_hasRegistry = !0), o && "complete" === o.readyState ? o.__CE_documentLoadHandled = !0 : i.addEventListener("load", (function() {
          var o = i.import;
          if (!o.__CE_documentLoadHandled) {
            o.__CE_documentLoadHandled = !0;
            var h = new Set(f);
            h.delete(o), A(t, o, {
              u: h,
              i: s
            });
          }
        }));
      } else h.push(i);
    }), f), t.b) for (i = 0; i < h.length; i++) w(t, h[i]);
    for (i = 0; i < h.length; i++) s(h[i]);
  }
  function y(t, i) {
    if (void 0 === i.__CE_state) {
      var o = i.ownerDocument;
      if ((o.defaultView || o.__CE_isImportDocument && o.__CE_hasRegistry) && (o = t.a.get(i.localName))) {
        o.constructionStack.push(i);
        var f = o.constructorFunction;
        try {
          try {
            if (new f !== i) throw Error("The custom element constructor did not produce the element being upgraded.");
          } finally {
            o.constructionStack.pop();
          }
        } catch (m) {
          throw i.__CE_state = 2, m;
        }
        if (i.__CE_state = 1, i.__CE_definition = o, o.attributeChangedCallback) for (o = o.observedAttributes, 
        f = 0; f < o.length; f++) {
          var s = o[f], h = i.getAttribute(s);
          null !== h && t.attributeChangedCallback(i, s, null, h, null);
        }
        l(i) && t.connectedCallback(i);
      }
    }
  }
  function B(t) {
    var i = document;
    this.c = t, this.a = i, this.b = void 0, A(this.c, this.a), "loading" === this.a.readyState && (this.b = new MutationObserver(this.f.bind(this)), 
    this.b.observe(this.a, {
      childList: !0,
      subtree: !0
    }));
  }
  function C(t) {
    t.b && t.b.disconnect();
  }
  function ea() {
    var t = this;
    this.b = this.a = void 0, this.c = new Promise((function(i) {
      t.b = i, t.a && i(t.a);
    }));
  }
  function D(t) {
    if (t.a) throw Error("Already resolved.");
    t.a = void 0, t.b && t.b(void 0);
  }
  function E(t) {
    this.c = !1, this.a = t, this.j = new Map, this.f = function(t) {
      return t();
    }, this.b = !1, this.g = [], this.o = new B(t);
  }
  u.prototype.connectedCallback = function(t) {
    var i = t.__CE_definition;
    i.connectedCallback && i.connectedCallback.call(t);
  }, u.prototype.disconnectedCallback = function(t) {
    var i = t.__CE_definition;
    i.disconnectedCallback && i.disconnectedCallback.call(t);
  }, u.prototype.attributeChangedCallback = function(t, i, o, f, s) {
    var h = t.__CE_definition;
    h.attributeChangedCallback && -1 < h.observedAttributes.indexOf(i) && h.attributeChangedCallback.call(t, i, o, f, s);
  }, B.prototype.f = function(t) {
    var i = this.a.readyState;
    for ("interactive" !== i && "complete" !== i || C(this), i = 0; i < t.length; i++) for (var o = t[i].addedNodes, f = 0; f < o.length; f++) A(this.c, o[f]);
  }, E.prototype.l = function(t, i) {
    var o = this;
    if (!(i instanceof Function)) throw new TypeError("Custom element constructors must be functions.");
    if (!g(t)) throw new SyntaxError("The element name '" + t + "' is not valid.");
    if (this.a.a.get(t)) throw Error("A custom element with name '" + t + "' has already been defined.");
    if (this.c) throw Error("A custom element is already being defined.");
    this.c = !0;
    try {
      var c = function(t) {
        var i = f[t];
        if (void 0 !== i && !(i instanceof Function)) throw Error("The '" + t + "' callback must be a function.");
        return i;
      }, f = i.prototype;
      if (!(f instanceof Object)) throw new TypeError("The custom element constructor's prototype is not an object.");
      var s = c("connectedCallback"), h = c("disconnectedCallback"), m = c("adoptedCallback"), T = c("attributeChangedCallback"), j = i.observedAttributes || [];
    } catch (N) {
      return;
    } finally {
      this.c = !1;
    }
    i = {
      localName: t,
      constructorFunction: i,
      connectedCallback: s,
      disconnectedCallback: h,
      adoptedCallback: m,
      attributeChangedCallback: T,
      observedAttributes: j,
      constructionStack: []
    }, function(t, i, o) {
      t.a.set(i, o), t.g.set(o.constructorFunction, o);
    }(this.a, t, i), this.g.push(i), this.b || (this.b = !0, this.f((function() {
      return function(t) {
        if (!1 !== t.b) {
          t.b = !1;
          for (var i = t.g, o = [], f = new Map, s = 0; s < i.length; s++) f.set(i[s].localName, []);
          for (A(t.a, document, {
            i: function(i) {
              if (void 0 === i.__CE_state) {
                var s = i.localName, h = f.get(s);
                h ? h.push(i) : t.a.a.get(s) && o.push(i);
              }
            }
          }), s = 0; s < o.length; s++) y(t.a, o[s]);
          for (;0 < i.length; ) {
            var h = i.shift();
            s = h.localName, h = f.get(h.localName);
            for (var m = 0; m < h.length; m++) y(t.a, h[m]);
            (s = t.j.get(s)) && D(s);
          }
        }
      }(o);
    })));
  }, E.prototype.i = function(t) {
    A(this.a, t);
  }, E.prototype.get = function(t) {
    if (t = this.a.a.get(t)) return t.constructorFunction;
  }, E.prototype.m = function(t) {
    if (!g(t)) return Promise.reject(new SyntaxError("'" + t + "' is not a valid custom element name."));
    var i = this.j.get(t);
    return i || (i = new ea, this.j.set(t, i), this.a.a.get(t) && !this.g.some((function(i) {
      return i.localName === t;
    })) && D(i)), i.c;
  }, E.prototype.s = function(t) {
    C(this.o);
    var i = this.f;
    this.f = function(o) {
      return t((function() {
        return i(o);
      }));
    };
  }, window.CustomElementRegistry = E, E.prototype.define = E.prototype.l, E.prototype.upgrade = E.prototype.i, 
  E.prototype.get = E.prototype.get, E.prototype.whenDefined = E.prototype.m, E.prototype.polyfillWrapFlushCallback = E.prototype.s;
  var i = window.Document.prototype.createElement, o = window.Document.prototype.createElementNS, f = window.Document.prototype.importNode, s = window.Document.prototype.prepend, h = window.Document.prototype.append, m = window.DocumentFragment.prototype.prepend, T = window.DocumentFragment.prototype.append, j = window.Node.prototype.cloneNode, N = window.Node.prototype.appendChild, O = window.Node.prototype.insertBefore, M = window.Node.prototype.removeChild, k = window.Node.prototype.replaceChild, L = Object.getOwnPropertyDescriptor(window.Node.prototype, "textContent"), S = window.Element.prototype.attachShadow, F = Object.getOwnPropertyDescriptor(window.Element.prototype, "innerHTML"), H = window.Element.prototype.getAttribute, P = window.Element.prototype.setAttribute, R = window.Element.prototype.removeAttribute, $ = window.Element.prototype.getAttributeNS, _ = window.Element.prototype.setAttributeNS, I = window.Element.prototype.removeAttributeNS, U = window.Element.prototype.insertAdjacentElement, W = window.Element.prototype.insertAdjacentHTML, q = window.Element.prototype.prepend, G = window.Element.prototype.append, J = window.Element.prototype.before, K = window.Element.prototype.after, Q = window.Element.prototype.replaceWith, V = window.Element.prototype.remove, X = window.HTMLElement, Z = Object.getOwnPropertyDescriptor(window.HTMLElement.prototype, "innerHTML"), tt = window.HTMLElement.prototype.insertAdjacentElement, nt = window.HTMLElement.prototype.insertAdjacentHTML, it = new function() {};
  function Y(t, i, o) {
    function c(i) {
      return function(o) {
        for (var f = [], s = 0; s < arguments.length; ++s) f[s] = arguments[s];
        s = [];
        for (var h = [], m = 0; m < f.length; m++) {
          var T = f[m];
          if (T instanceof Element && l(T) && h.push(T), T instanceof DocumentFragment) for (T = T.firstChild; T; T = T.nextSibling) s.push(T); else s.push(T);
        }
        for (i.apply(this, f), f = 0; f < h.length; f++) z(t, h[f]);
        if (l(this)) for (f = 0; f < s.length; f++) (h = s[f]) instanceof Element && x(t, h);
      };
    }
    void 0 !== o.h && (i.prepend = c(o.h)), void 0 !== o.append && (i.append = c(o.append));
  }
  var et = window.customElements;
  if (!et || et.forcePolyfill || "function" != typeof et.define || "function" != typeof et.get) {
    var rt = new u;
    !function() {
      var t = rt;
      window.HTMLElement = function() {
        function b() {
          var o = this.constructor, f = t.g.get(o);
          if (!f) throw Error("The custom element being constructed was not registered with `customElements`.");
          var s = f.constructionStack;
          if (0 === s.length) return s = i.call(document, f.localName), Object.setPrototypeOf(s, o.prototype), 
          s.__CE_state = 1, s.__CE_definition = f, w(t, s), s;
          var h = s[f = s.length - 1];
          if (h === it) throw Error("The HTMLElement constructor was either called reentrantly for this constructor or called multiple times.");
          return s[f] = it, Object.setPrototypeOf(h, o.prototype), w(t, h), h;
        }
        return b.prototype = X.prototype, Object.defineProperty(b.prototype, "constructor", {
          writable: !0,
          configurable: !0,
          enumerable: !1,
          value: b
        }), b;
      }();
    }(), function() {
      var t = rt;
      r(Document.prototype, "createElement", (function(o) {
        if (this.__CE_hasRegistry) {
          var f = t.a.get(o);
          if (f) return new f.constructorFunction;
        }
        return o = i.call(this, o), w(t, o), o;
      })), r(Document.prototype, "importNode", (function(i, o) {
        return i = f.call(this, i, !!o), this.__CE_hasRegistry ? A(t, i) : v(t, i), i;
      })), r(Document.prototype, "createElementNS", (function(i, f) {
        if (this.__CE_hasRegistry && (null === i || "http://www.w3.org/1999/xhtml" === i)) {
          var s = t.a.get(f);
          if (s) return new s.constructorFunction;
        }
        return i = o.call(this, i, f), w(t, i), i;
      })), Y(t, Document.prototype, {
        h: s,
        append: h
      });
    }(), Y(rt, DocumentFragment.prototype, {
      h: m,
      append: T
    }), function() {
      function a(i, o) {
        Object.defineProperty(i, "textContent", {
          enumerable: o.enumerable,
          configurable: !0,
          get: o.get,
          set: function(i) {
            if (this.nodeType === Node.TEXT_NODE) o.set.call(this, i); else {
              var f = void 0;
              if (this.firstChild) {
                var s = this.childNodes, h = s.length;
                if (0 < h && l(this)) {
                  f = Array(h);
                  for (var m = 0; m < h; m++) f[m] = s[m];
                }
              }
              if (o.set.call(this, i), f) for (i = 0; i < f.length; i++) z(t, f[i]);
            }
          }
        });
      }
      var t = rt;
      r(Node.prototype, "insertBefore", (function(i, o) {
        if (i instanceof DocumentFragment) {
          var f = Array.prototype.slice.apply(i.childNodes);
          if (i = O.call(this, i, o), l(this)) for (o = 0; o < f.length; o++) x(t, f[o]);
          return i;
        }
        return f = l(i), o = O.call(this, i, o), f && z(t, i), l(this) && x(t, i), o;
      })), r(Node.prototype, "appendChild", (function(i) {
        if (i instanceof DocumentFragment) {
          var o = Array.prototype.slice.apply(i.childNodes);
          if (i = N.call(this, i), l(this)) for (var f = 0; f < o.length; f++) x(t, o[f]);
          return i;
        }
        return o = l(i), f = N.call(this, i), o && z(t, i), l(this) && x(t, i), f;
      })), r(Node.prototype, "cloneNode", (function(i) {
        return i = j.call(this, !!i), this.ownerDocument.__CE_hasRegistry ? A(t, i) : v(t, i), 
        i;
      })), r(Node.prototype, "removeChild", (function(i) {
        var o = l(i), f = M.call(this, i);
        return o && z(t, i), f;
      })), r(Node.prototype, "replaceChild", (function(i, o) {
        if (i instanceof DocumentFragment) {
          var f = Array.prototype.slice.apply(i.childNodes);
          if (i = k.call(this, i, o), l(this)) for (z(t, o), o = 0; o < f.length; o++) x(t, f[o]);
          return i;
        }
        f = l(i);
        var s = k.call(this, i, o), h = l(this);
        return h && z(t, o), f && z(t, i), h && x(t, i), s;
      })), L && L.get ? a(Node.prototype, L) : function(t, i) {
        t.b = !0, t.c.push(i);
      }(t, (function(t) {
        a(t, {
          enumerable: !0,
          configurable: !0,
          get: function() {
            for (var t = [], i = 0; i < this.childNodes.length; i++) {
              var o = this.childNodes[i];
              o.nodeType !== Node.COMMENT_NODE && t.push(o.textContent);
            }
            return t.join("");
          },
          set: function(t) {
            for (;this.firstChild; ) M.call(this, this.firstChild);
            null != t && "" !== t && N.call(this, document.createTextNode(t));
          }
        });
      }));
    }(), function() {
      function a(i, o) {
        Object.defineProperty(i, "innerHTML", {
          enumerable: o.enumerable,
          configurable: !0,
          get: o.get,
          set: function(i) {
            var f = this, s = void 0;
            if (l(this) && (s = [], p(this, (function(t) {
              t !== f && s.push(t);
            }))), o.set.call(this, i), s) for (var h = 0; h < s.length; h++) {
              var m = s[h];
              1 === m.__CE_state && t.disconnectedCallback(m);
            }
            return this.ownerDocument.__CE_hasRegistry ? A(t, this) : v(t, this), i;
          }
        });
      }
      function b(i, o) {
        r(i, "insertAdjacentElement", (function(i, f) {
          var s = l(f);
          return i = o.call(this, i, f), s && z(t, f), l(i) && x(t, f), i;
        }));
      }
      function d(i, o) {
        function e(i, o) {
          for (var f = []; i !== o; i = i.nextSibling) f.push(i);
          for (o = 0; o < f.length; o++) A(t, f[o]);
        }
        r(i, "insertAdjacentHTML", (function(t, i) {
          if ("beforebegin" === (t = t.toLowerCase())) {
            var f = this.previousSibling;
            o.call(this, t, i), e(f || this.parentNode.firstChild, this);
          } else if ("afterbegin" === t) f = this.firstChild, o.call(this, t, i), e(this.firstChild, f); else if ("beforeend" === t) f = this.lastChild, 
          o.call(this, t, i), e(f || this.firstChild, null); else {
            if ("afterend" !== t) throw new SyntaxError("The value provided (" + t + ") is not one of 'beforebegin', 'afterbegin', 'beforeend', or 'afterend'.");
            f = this.nextSibling, o.call(this, t, i), e(this.nextSibling, f);
          }
        }));
      }
      var t = rt;
      S && r(Element.prototype, "attachShadow", (function(i) {
        i = S.call(this, i);
        var o = t;
        if (o.b && !i.__CE_patched) {
          i.__CE_patched = !0;
          for (var f = 0; f < o.c.length; f++) o.c[f](i);
        }
        return this.__CE_shadowRoot = i;
      })), F && F.get ? a(Element.prototype, F) : Z && Z.get ? a(HTMLElement.prototype, Z) : function(t, i) {
        t.b = !0, t.f.push(i);
      }(t, (function(t) {
        a(t, {
          enumerable: !0,
          configurable: !0,
          get: function() {
            return j.call(this, !0).innerHTML;
          },
          set: function(t) {
            var i = "template" === this.localName, f = i ? this.content : this, s = o.call(document, this.namespaceURI, this.localName);
            for (s.innerHTML = t; 0 < f.childNodes.length; ) M.call(f, f.childNodes[0]);
            for (t = i ? s.content : s; 0 < t.childNodes.length; ) N.call(f, t.childNodes[0]);
          }
        });
      })), r(Element.prototype, "setAttribute", (function(i, o) {
        if (1 !== this.__CE_state) return P.call(this, i, o);
        var f = H.call(this, i);
        P.call(this, i, o), o = H.call(this, i), t.attributeChangedCallback(this, i, f, o, null);
      })), r(Element.prototype, "setAttributeNS", (function(i, o, f) {
        if (1 !== this.__CE_state) return _.call(this, i, o, f);
        var s = $.call(this, i, o);
        _.call(this, i, o, f), f = $.call(this, i, o), t.attributeChangedCallback(this, o, s, f, i);
      })), r(Element.prototype, "removeAttribute", (function(i) {
        if (1 !== this.__CE_state) return R.call(this, i);
        var o = H.call(this, i);
        R.call(this, i), null !== o && t.attributeChangedCallback(this, i, o, null, null);
      })), r(Element.prototype, "removeAttributeNS", (function(i, o) {
        if (1 !== this.__CE_state) return I.call(this, i, o);
        var f = $.call(this, i, o);
        I.call(this, i, o);
        var s = $.call(this, i, o);
        f !== s && t.attributeChangedCallback(this, o, f, s, i);
      })), tt ? b(HTMLElement.prototype, tt) : U ? b(Element.prototype, U) : console.warn("Custom Elements: `Element#insertAdjacentElement` was not patched."), 
      nt ? d(HTMLElement.prototype, nt) : W ? d(Element.prototype, W) : console.warn("Custom Elements: `Element#insertAdjacentHTML` was not patched."), 
      Y(t, Element.prototype, {
        h: q,
        append: G
      }), function(t) {
        function b(i) {
          return function(o) {
            for (var f = [], s = 0; s < arguments.length; ++s) f[s] = arguments[s];
            s = [];
            for (var h = [], m = 0; m < f.length; m++) {
              var T = f[m];
              if (T instanceof Element && l(T) && h.push(T), T instanceof DocumentFragment) for (T = T.firstChild; T; T = T.nextSibling) s.push(T); else s.push(T);
            }
            for (i.apply(this, f), f = 0; f < h.length; f++) z(t, h[f]);
            if (l(this)) for (f = 0; f < s.length; f++) (h = s[f]) instanceof Element && x(t, h);
          };
        }
        var i = Element.prototype;
        void 0 !== J && (i.before = b(J)), void 0 !== J && (i.after = b(K)), void 0 !== Q && r(i, "replaceWith", (function(i) {
          for (var o = [], f = 0; f < arguments.length; ++f) o[f] = arguments[f];
          f = [];
          for (var s = [], h = 0; h < o.length; h++) {
            var m = o[h];
            if (m instanceof Element && l(m) && s.push(m), m instanceof DocumentFragment) for (m = m.firstChild; m; m = m.nextSibling) f.push(m); else f.push(m);
          }
          for (h = l(this), Q.apply(this, o), o = 0; o < s.length; o++) z(t, s[o]);
          if (h) for (z(t, this), o = 0; o < f.length; o++) (s = f[o]) instanceof Element && x(t, s);
        })), void 0 !== V && r(i, "remove", (function() {
          var i = l(this);
          V.call(this), i && z(t, this);
        }));
      }(t);
    }(), document.__CE_hasRegistry = !0;
    var ot = new E(rt);
    Object.defineProperty(window, "customElements", {
      configurable: !0,
      enumerable: !0,
      value: ot
    });
  }
}(), 
// Polyfill document.baseURI
"string" != typeof document.baseURI && Object.defineProperty(Document.prototype, "baseURI", {
  enumerable: !0,
  configurable: !0,
  get: function() {
    var t = document.querySelector("base");
    return t && t.href ? t.href : document.URL;
  }
}), 
// Polyfill CustomEvent
"function" != typeof window.CustomEvent && (window.CustomEvent = function(t, i) {
  i = i || {
    bubbles: !1,
    cancelable: !1,
    detail: void 0
  };
  var o = document.createEvent("CustomEvent");
  return o.initCustomEvent(t, i.bubbles, i.cancelable, i.detail), o;
}, window.CustomEvent.prototype = window.Event.prototype), 
// Event.composedPath
function(t, i, o) {
  t.composedPath || (t.composedPath = function() {
    if (this.path) return this.path;
    var t = this.target;
    for (this.path = []; null !== t.parentNode; ) this.path.push(t), t = t.parentNode;
    return this.path.push(i, o), this.path;
  });
}(Event.prototype, document, window), 
/*!
Element.closest and Element.matches
https://github.com/jonathantneal/closest
Creative Commons Zero v1.0 Universal
*/
function(t) {
  "function" != typeof t.matches && (t.matches = t.msMatchesSelector || t.mozMatchesSelector || t.webkitMatchesSelector || function(t) {
    t = (this.document || this.ownerDocument).querySelectorAll(t);
    for (var i = 0; t[i] && t[i] !== this; ) ++i;
    return !!t[i];
  }), "function" != typeof t.closest && (t.closest = function(t) {
    for (var i = this; i && 1 === i.nodeType; ) {
      if (i.matches(t)) return i;
      i = i.parentNode;
    }
    return null;
  });
}(window.Element.prototype), 
/*!
Element.getRootNode()
*/
function(t) {
  function b(t) {
    return t && t.parentNode ? b(t.parentNode) : t;
  }
  "function" != typeof t.getRootNode && (t.getRootNode = function(t) {
    return t && t.composed ? function d(t) {
      return (t = b(t)) && 11 === t.nodeType ? d(t.host) : t;
    }(this) : b(this);
  });
}(Element.prototype), 
/*!
Element.isConnected()
*/
function(t) {
  "isConnected" in t || Object.defineProperty(t, "isConnected", {
    configurable: !0,
    enumerable: !0,
    get: function() {
      var t = this.getRootNode({
        composed: !0
      });
      return t && 9 === t.nodeType;
    }
  });
}(Element.prototype), [ Element.prototype, CharacterData.prototype, DocumentType.prototype ].forEach((function(t) {
  t.hasOwnProperty("remove") || Object.defineProperty(t, "remove", {
    configurable: !0,
    enumerable: !0,
    writable: !0,
    value: function() {
      null !== this.parentNode && this.parentNode.removeChild(this);
    }
  });
})), function(t) {
  "classList" in t || Object.defineProperty(t, "classList", {
    get: function() {
      var t = this, i = (t.getAttribute("class") || "").replace(/^\s+|\s$/g, "").split(/\s+/g);
      function n() {
        i.length > 0 ? t.setAttribute("class", i.join(" ")) : t.removeAttribute("class");
      }
      return "" === i[0] && i.splice(0, 1), i.toggle = function(t, o) {
        void 0 !== o ? o ? i.add(t) : i.remove(t) : -1 !== i.indexOf(t) ? i.splice(i.indexOf(t), 1) : i.push(t), 
        n();
      }, i.add = function() {
        for (var t = [].slice.call(arguments), o = 0, f = t.length; o < f; o++) -1 === i.indexOf(t[o]) && i.push(t[o]);
        n();
      }, i.remove = function() {
        for (var t = [].slice.call(arguments), o = 0, f = t.length; o < f; o++) -1 !== i.indexOf(t[o]) && i.splice(i.indexOf(t[o]), 1);
        n();
      }, i.item = function(t) {
        return i[t];
      }, i.contains = function(t) {
        return -1 !== i.indexOf(t);
      }, i.replace = function(t, o) {
        -1 !== i.indexOf(t) && i.splice(i.indexOf(t), 1, o), n();
      }, i.value = t.getAttribute("class") || "", i;
    }
  });
}(Element.prototype), 
/*!
DOMTokenList
*/
function(t) {
  try {
    document.body.classList.add();
  } catch (f) {
    var i = t.add, o = t.remove;
    t.add = function() {
      for (var t = 0; t < arguments.length; t++) i.call(this, arguments[t]);
    }, t.remove = function() {
      for (var t = 0; t < arguments.length; t++) o.call(this, arguments[t]);
    };
  }
}(DOMTokenList.prototype);