(window["webpackJsonp"] = window["webpackJsonp"] || []).push([[4],{

/***/ "../node_modules/@genexus/design-tokens-editor/dist/esm-es5/dom-76cc7c7d-0a082895.js":
/*!*******************************************************************************************!*\
  !*** ../node_modules/@genexus/design-tokens-editor/dist/esm-es5/dom-76cc7c7d-0a082895.js ***!
  \*******************************************************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

eval("(function () {\n    var aa = new Set(\"annotation-xml color-profile font-face font-face-src font-face-uri font-face-format font-face-name missing-glyph\".split(\" \"));\n    function g(a) { var b = aa.has(a); a = /^[a-z][.0-9_a-z]*-[\\-.0-9_a-z]*$/.test(a); return !b && a; }\n    function l(a) { var b = a.isConnected; if (void 0 !== b)\n        return b; for (; a && !(a.__CE_isImportDocument || a instanceof Document);)\n        a = a.parentNode || (window.ShadowRoot && a instanceof ShadowRoot ? a.host : void 0); return !(!a || !(a.__CE_isImportDocument || a instanceof Document)); }\n    function n(a, b) { for (; b && b !== a && !b.nextSibling;)\n        b = b.parentNode; return b && b !== a ? b.nextSibling : null; }\n    function p(a, b, d) { d = void 0 === d ? new Set : d; for (var c = a; c;) {\n        if (c.nodeType === Node.ELEMENT_NODE) {\n            var e = c;\n            b(e);\n            var f = e.localName;\n            if (\"link\" === f && \"import\" === e.getAttribute(\"rel\")) {\n                c = e.import;\n                if (c instanceof Node && !d.has(c))\n                    for (d.add(c), c = c.firstChild; c; c = c.nextSibling)\n                        p(c, b, d);\n                c = n(a, e);\n                continue;\n            }\n            else if (\"template\" === f) {\n                c = n(a, e);\n                continue;\n            }\n            if (e = e.__CE_shadowRoot)\n                for (e = e.firstChild; e; e = e.nextSibling)\n                    p(e, b, d);\n        }\n        c = c.firstChild ? c.firstChild : n(a, c);\n    } }\n    function r(a, b, d) { a[b] = d; }\n    function u() { this.a = new Map; this.g = new Map; this.c = []; this.f = []; this.b = !1; }\n    function ba(a, b, d) { a.a.set(b, d); a.g.set(d.constructorFunction, d); }\n    function ca(a, b) { a.b = !0; a.c.push(b); }\n    function da(a, b) { a.b = !0; a.f.push(b); }\n    function v(a, b) { a.b && p(b, function (b) { return w(a, b); }); }\n    function w(a, b) { if (a.b && !b.__CE_patched) {\n        b.__CE_patched = !0;\n        for (var d = 0; d < a.c.length; d++)\n            a.c[d](b);\n        for (d = 0; d < a.f.length; d++)\n            a.f[d](b);\n    } }\n    function x(a, b) { var d = []; p(b, function (b) { return d.push(b); }); for (b = 0; b < d.length; b++) {\n        var c = d[b];\n        1 === c.__CE_state ? a.connectedCallback(c) : y(a, c);\n    } }\n    function z(a, b) { var d = []; p(b, function (b) { return d.push(b); }); for (b = 0; b < d.length; b++) {\n        var c = d[b];\n        1 === c.__CE_state && a.disconnectedCallback(c);\n    } }\n    function A(a, b, d) {\n        d = void 0 === d ? {} : d;\n        var c = d.u || new Set, e = d.i || function (b) { return y(a, b); }, f = [];\n        p(b, function (b) { if (\"link\" === b.localName && \"import\" === b.getAttribute(\"rel\")) {\n            var d = b.import;\n            d instanceof Node && (d.__CE_isImportDocument = !0, d.__CE_hasRegistry = !0);\n            d && \"complete\" === d.readyState ? d.__CE_documentLoadHandled = !0 : b.addEventListener(\"load\", function () { var d = b.import; if (!d.__CE_documentLoadHandled) {\n                d.__CE_documentLoadHandled = !0;\n                var f = new Set(c);\n                f.delete(d);\n                A(a, d, { u: f, i: e });\n            } });\n        }\n        else\n            f.push(b); }, c);\n        if (a.b)\n            for (b =\n                0; b < f.length; b++)\n                w(a, f[b]);\n        for (b = 0; b < f.length; b++)\n            e(f[b]);\n    }\n    function y(a, b) {\n        if (void 0 === b.__CE_state) {\n            var d = b.ownerDocument;\n            if (d.defaultView || d.__CE_isImportDocument && d.__CE_hasRegistry)\n                if (d = a.a.get(b.localName)) {\n                    d.constructionStack.push(b);\n                    var c = d.constructorFunction;\n                    try {\n                        try {\n                            if (new c !== b)\n                                throw Error(\"The custom element constructor did not produce the element being upgraded.\");\n                        }\n                        finally {\n                            d.constructionStack.pop();\n                        }\n                    }\n                    catch (t) {\n                        throw b.__CE_state = 2, t;\n                    }\n                    b.__CE_state = 1;\n                    b.__CE_definition = d;\n                    if (d.attributeChangedCallback)\n                        for (d = d.observedAttributes, c = 0; c < d.length; c++) {\n                            var e = d[c], f = b.getAttribute(e);\n                            null !== f && a.attributeChangedCallback(b, e, null, f, null);\n                        }\n                    l(b) && a.connectedCallback(b);\n                }\n        }\n    }\n    u.prototype.connectedCallback = function (a) { var b = a.__CE_definition; b.connectedCallback && b.connectedCallback.call(a); };\n    u.prototype.disconnectedCallback = function (a) { var b = a.__CE_definition; b.disconnectedCallback && b.disconnectedCallback.call(a); };\n    u.prototype.attributeChangedCallback = function (a, b, d, c, e) { var f = a.__CE_definition; f.attributeChangedCallback && -1 < f.observedAttributes.indexOf(b) && f.attributeChangedCallback.call(a, b, d, c, e); };\n    function B(a) { var b = document; this.c = a; this.a = b; this.b = void 0; A(this.c, this.a); \"loading\" === this.a.readyState && (this.b = new MutationObserver(this.f.bind(this)), this.b.observe(this.a, { childList: !0, subtree: !0 })); }\n    function C(a) { a.b && a.b.disconnect(); }\n    B.prototype.f = function (a) { var b = this.a.readyState; \"interactive\" !== b && \"complete\" !== b || C(this); for (b = 0; b < a.length; b++)\n        for (var d = a[b].addedNodes, c = 0; c < d.length; c++)\n            A(this.c, d[c]); };\n    function ea() { var a = this; this.b = this.a = void 0; this.c = new Promise(function (b) { a.b = b; a.a && b(a.a); }); }\n    function D(a) { if (a.a)\n        throw Error(\"Already resolved.\"); a.a = void 0; a.b && a.b(void 0); }\n    function E(a) { this.c = !1; this.a = a; this.j = new Map; this.f = function (b) { return b(); }; this.b = !1; this.g = []; this.o = new B(a); }\n    E.prototype.l = function (a, b) {\n        var d = this;\n        if (!(b instanceof Function))\n            throw new TypeError(\"Custom element constructors must be functions.\");\n        if (!g(a))\n            throw new SyntaxError(\"The element name '\" + a + \"' is not valid.\");\n        if (this.a.a.get(a))\n            throw Error(\"A custom element with name '\" + a + \"' has already been defined.\");\n        if (this.c)\n            throw Error(\"A custom element is already being defined.\");\n        this.c = !0;\n        try {\n            var c = function (b) {\n                var a = e[b];\n                if (void 0 !== a && !(a instanceof Function))\n                    throw Error(\"The '\" + b + \"' callback must be a function.\");\n                return a;\n            }, e = b.prototype;\n            if (!(e instanceof Object))\n                throw new TypeError(\"The custom element constructor's prototype is not an object.\");\n            var f = c(\"connectedCallback\");\n            var t = c(\"disconnectedCallback\");\n            var k = c(\"adoptedCallback\");\n            var h = c(\"attributeChangedCallback\");\n            var m = b.observedAttributes || [];\n        }\n        catch (q) {\n            return;\n        }\n        finally {\n            this.c = !1;\n        }\n        b = { localName: a, constructorFunction: b, connectedCallback: f, disconnectedCallback: t, adoptedCallback: k, attributeChangedCallback: h, observedAttributes: m, constructionStack: [] };\n        ba(this.a, a, b);\n        this.g.push(b);\n        this.b || (this.b = !0, this.f(function () { return fa(d); }));\n    };\n    E.prototype.i = function (a) { A(this.a, a); };\n    function fa(a) { if (!1 !== a.b) {\n        a.b = !1;\n        for (var b = a.g, d = [], c = new Map, e = 0; e < b.length; e++)\n            c.set(b[e].localName, []);\n        A(a.a, document, { i: function (b) { if (void 0 === b.__CE_state) {\n                var e = b.localName, f = c.get(e);\n                f ? f.push(b) : a.a.a.get(e) && d.push(b);\n            } } });\n        for (e = 0; e < d.length; e++)\n            y(a.a, d[e]);\n        for (; 0 < b.length;) {\n            var f = b.shift();\n            e = f.localName;\n            f = c.get(f.localName);\n            for (var t = 0; t < f.length; t++)\n                y(a.a, f[t]);\n            (e = a.j.get(e)) && D(e);\n        }\n    } }\n    E.prototype.get = function (a) { if (a = this.a.a.get(a))\n        return a.constructorFunction; };\n    E.prototype.m = function (a) { if (!g(a))\n        return Promise.reject(new SyntaxError(\"'\" + a + \"' is not a valid custom element name.\")); var b = this.j.get(a); if (b)\n        return b.c; b = new ea; this.j.set(a, b); this.a.a.get(a) && !this.g.some(function (b) { return b.localName === a; }) && D(b); return b.c; };\n    E.prototype.s = function (a) { C(this.o); var b = this.f; this.f = function (d) { return a(function () { return b(d); }); }; };\n    window.CustomElementRegistry = E;\n    E.prototype.define = E.prototype.l;\n    E.prototype.upgrade = E.prototype.i;\n    E.prototype.get = E.prototype.get;\n    E.prototype.whenDefined = E.prototype.m;\n    E.prototype.polyfillWrapFlushCallback = E.prototype.s;\n    var F = window.Document.prototype.createElement, G = window.Document.prototype.createElementNS, ha = window.Document.prototype.importNode, ia = window.Document.prototype.prepend, ja = window.Document.prototype.append, ka = window.DocumentFragment.prototype.prepend, la = window.DocumentFragment.prototype.append, H = window.Node.prototype.cloneNode, I = window.Node.prototype.appendChild, J = window.Node.prototype.insertBefore, K = window.Node.prototype.removeChild, L = window.Node.prototype.replaceChild, M = Object.getOwnPropertyDescriptor(window.Node.prototype, \"textContent\"), N = window.Element.prototype.attachShadow, O = Object.getOwnPropertyDescriptor(window.Element.prototype, \"innerHTML\"), P = window.Element.prototype.getAttribute, Q = window.Element.prototype.setAttribute, R = window.Element.prototype.removeAttribute, S = window.Element.prototype.getAttributeNS, T = window.Element.prototype.setAttributeNS, U = window.Element.prototype.removeAttributeNS, ma = window.Element.prototype.insertAdjacentElement, na = window.Element.prototype.insertAdjacentHTML, oa = window.Element.prototype.prepend, pa = window.Element.prototype.append, V = window.Element.prototype.before, qa = window.Element.prototype.after, ra = window.Element.prototype.replaceWith, sa = window.Element.prototype.remove, ta = window.HTMLElement, W = Object.getOwnPropertyDescriptor(window.HTMLElement.prototype, \"innerHTML\"), ua = window.HTMLElement.prototype.insertAdjacentElement, va = window.HTMLElement.prototype.insertAdjacentHTML;\n    var wa = new function () { };\n    function xa() {\n        var a = X;\n        window.HTMLElement = function () {\n            function b() {\n                var b = this.constructor, c = a.g.get(b);\n                if (!c)\n                    throw Error(\"The custom element being constructed was not registered with `customElements`.\");\n                var e = c.constructionStack;\n                if (0 === e.length)\n                    return e = F.call(document, c.localName), Object.setPrototypeOf(e, b.prototype), e.__CE_state = 1, e.__CE_definition = c, w(a, e), e;\n                c = e.length - 1;\n                var f = e[c];\n                if (f === wa)\n                    throw Error(\"The HTMLElement constructor was either called reentrantly for this constructor or called multiple times.\");\n                e[c] = wa;\n                Object.setPrototypeOf(f, b.prototype);\n                w(a, f);\n                return f;\n            }\n            b.prototype = ta.prototype;\n            Object.defineProperty(b.prototype, \"constructor\", { writable: !0, configurable: !0, enumerable: !1, value: b });\n            return b;\n        }();\n    }\n    function Y(a, b, d) { function c(b) { return function (d) { for (var e = [], c = 0; c < arguments.length; ++c)\n        e[c] = arguments[c]; c = []; for (var f = [], m = 0; m < e.length; m++) {\n        var q = e[m];\n        q instanceof Element && l(q) && f.push(q);\n        if (q instanceof DocumentFragment)\n            for (q = q.firstChild; q; q = q.nextSibling)\n                c.push(q);\n        else\n            c.push(q);\n    } b.apply(this, e); for (e = 0; e < f.length; e++)\n        z(a, f[e]); if (l(this))\n        for (e = 0; e < c.length; e++)\n            f = c[e], f instanceof Element && x(a, f); }; } void 0 !== d.h && (b.prepend = c(d.h)); void 0 !== d.append && (b.append = c(d.append)); }\n    function ya() {\n        var a = X;\n        r(Document.prototype, \"createElement\", function (b) { if (this.__CE_hasRegistry) {\n            var d = a.a.get(b);\n            if (d)\n                return new d.constructorFunction;\n        } b = F.call(this, b); w(a, b); return b; });\n        r(Document.prototype, \"importNode\", function (b, d) { b = ha.call(this, b, !!d); this.__CE_hasRegistry ? A(a, b) : v(a, b); return b; });\n        r(Document.prototype, \"createElementNS\", function (b, d) {\n            if (this.__CE_hasRegistry && (null === b || \"http://www.w3.org/1999/xhtml\" === b)) {\n                var c = a.a.get(d);\n                if (c)\n                    return new c.constructorFunction;\n            }\n            b = G.call(this, b, d);\n            w(a, b);\n            return b;\n        });\n        Y(a, Document.prototype, { h: ia, append: ja });\n    }\n    function za() {\n        function a(a, c) { Object.defineProperty(a, \"textContent\", { enumerable: c.enumerable, configurable: !0, get: c.get, set: function (a) { if (this.nodeType === Node.TEXT_NODE)\n                c.set.call(this, a);\n            else {\n                var d = void 0;\n                if (this.firstChild) {\n                    var e = this.childNodes, k = e.length;\n                    if (0 < k && l(this)) {\n                        d = Array(k);\n                        for (var h = 0; h < k; h++)\n                            d[h] = e[h];\n                    }\n                }\n                c.set.call(this, a);\n                if (d)\n                    for (a = 0; a < d.length; a++)\n                        z(b, d[a]);\n            } } }); }\n        var b = X;\n        r(Node.prototype, \"insertBefore\", function (a, c) {\n            if (a instanceof DocumentFragment) {\n                var e = Array.prototype.slice.apply(a.childNodes);\n                a = J.call(this, a, c);\n                if (l(this))\n                    for (c = 0; c < e.length; c++)\n                        x(b, e[c]);\n                return a;\n            }\n            e = l(a);\n            c = J.call(this, a, c);\n            e && z(b, a);\n            l(this) && x(b, a);\n            return c;\n        });\n        r(Node.prototype, \"appendChild\", function (a) { if (a instanceof DocumentFragment) {\n            var c = Array.prototype.slice.apply(a.childNodes);\n            a = I.call(this, a);\n            if (l(this))\n                for (var e = 0; e < c.length; e++)\n                    x(b, c[e]);\n            return a;\n        } c = l(a); e = I.call(this, a); c && z(b, a); l(this) && x(b, a); return e; });\n        r(Node.prototype, \"cloneNode\", function (a) {\n            a = H.call(this, !!a);\n            this.ownerDocument.__CE_hasRegistry ? A(b, a) : v(b, a);\n            return a;\n        });\n        r(Node.prototype, \"removeChild\", function (a) { var c = l(a), e = K.call(this, a); c && z(b, a); return e; });\n        r(Node.prototype, \"replaceChild\", function (a, c) { if (a instanceof DocumentFragment) {\n            var e = Array.prototype.slice.apply(a.childNodes);\n            a = L.call(this, a, c);\n            if (l(this))\n                for (z(b, c), c = 0; c < e.length; c++)\n                    x(b, e[c]);\n            return a;\n        } e = l(a); var f = L.call(this, a, c), d = l(this); d && z(b, c); e && z(b, a); d && x(b, a); return f; });\n        M && M.get ? a(Node.prototype, M) : ca(b, function (b) {\n            a(b, { enumerable: !0, configurable: !0, get: function () {\n                    for (var a = [], b = 0; b < this.childNodes.length; b++) {\n                        var f = this.childNodes[b];\n                        f.nodeType !== Node.COMMENT_NODE && a.push(f.textContent);\n                    }\n                    return a.join(\"\");\n                }, set: function (a) { for (; this.firstChild;)\n                    K.call(this, this.firstChild); null != a && \"\" !== a && I.call(this, document.createTextNode(a)); } });\n        });\n    }\n    function Aa(a) {\n        function b(b) { return function (e) { for (var c = [], d = 0; d < arguments.length; ++d)\n            c[d] = arguments[d]; d = []; for (var k = [], h = 0; h < c.length; h++) {\n            var m = c[h];\n            m instanceof Element && l(m) && k.push(m);\n            if (m instanceof DocumentFragment)\n                for (m = m.firstChild; m; m = m.nextSibling)\n                    d.push(m);\n            else\n                d.push(m);\n        } b.apply(this, c); for (c = 0; c < k.length; c++)\n            z(a, k[c]); if (l(this))\n            for (c = 0; c < d.length; c++)\n                k = d[c], k instanceof Element && x(a, k); }; }\n        var d = Element.prototype;\n        void 0 !== V && (d.before = b(V));\n        void 0 !== V && (d.after = b(qa));\n        void 0 !== ra &&\n            r(d, \"replaceWith\", function (b) { for (var e = [], c = 0; c < arguments.length; ++c)\n                e[c] = arguments[c]; c = []; for (var d = [], k = 0; k < e.length; k++) {\n                var h = e[k];\n                h instanceof Element && l(h) && d.push(h);\n                if (h instanceof DocumentFragment)\n                    for (h = h.firstChild; h; h = h.nextSibling)\n                        c.push(h);\n                else\n                    c.push(h);\n            } k = l(this); ra.apply(this, e); for (e = 0; e < d.length; e++)\n                z(a, d[e]); if (k)\n                for (z(a, this), e = 0; e < c.length; e++)\n                    d = c[e], d instanceof Element && x(a, d); });\n        void 0 !== sa && r(d, \"remove\", function () { var b = l(this); sa.call(this); b && z(a, this); });\n    }\n    function Ba() {\n        function a(a, b) { Object.defineProperty(a, \"innerHTML\", { enumerable: b.enumerable, configurable: !0, get: b.get, set: function (a) { var e = this, d = void 0; l(this) && (d = [], p(this, function (a) { a !== e && d.push(a); })); b.set.call(this, a); if (d)\n                for (var f = 0; f < d.length; f++) {\n                    var t = d[f];\n                    1 === t.__CE_state && c.disconnectedCallback(t);\n                } this.ownerDocument.__CE_hasRegistry ? A(c, this) : v(c, this); return a; } }); }\n        function b(a, b) { r(a, \"insertAdjacentElement\", function (a, e) { var d = l(e); a = b.call(this, a, e); d && z(c, e); l(a) && x(c, e); return a; }); }\n        function d(a, b) {\n            function e(a, b) { for (var e = []; a !== b; a = a.nextSibling)\n                e.push(a); for (b = 0; b < e.length; b++)\n                A(c, e[b]); }\n            r(a, \"insertAdjacentHTML\", function (a, c) {\n                a = a.toLowerCase();\n                if (\"beforebegin\" === a) {\n                    var d = this.previousSibling;\n                    b.call(this, a, c);\n                    e(d || this.parentNode.firstChild, this);\n                }\n                else if (\"afterbegin\" === a)\n                    d = this.firstChild, b.call(this, a, c), e(this.firstChild, d);\n                else if (\"beforeend\" === a)\n                    d = this.lastChild, b.call(this, a, c), e(d || this.firstChild, null);\n                else if (\"afterend\" === a)\n                    d = this.nextSibling, b.call(this, a, c), e(this.nextSibling, d);\n                else\n                    throw new SyntaxError(\"The value provided (\" + String(a) + \") is not one of 'beforebegin', 'afterbegin', 'beforeend', or 'afterend'.\");\n            });\n        }\n        var c = X;\n        N && r(Element.prototype, \"attachShadow\", function (a) { a = N.call(this, a); var b = c; if (b.b && !a.__CE_patched) {\n            a.__CE_patched = !0;\n            for (var e = 0; e < b.c.length; e++)\n                b.c[e](a);\n        } return this.__CE_shadowRoot = a; });\n        O && O.get ? a(Element.prototype, O) : W && W.get ? a(HTMLElement.prototype, W) : da(c, function (b) {\n            a(b, { enumerable: !0, configurable: !0, get: function () { return H.call(this, !0).innerHTML; },\n                set: function (a) { var b = \"template\" === this.localName, c = b ? this.content : this, e = G.call(document, this.namespaceURI, this.localName); for (e.innerHTML = a; 0 < c.childNodes.length;)\n                    K.call(c, c.childNodes[0]); for (a = b ? e.content : e; 0 < a.childNodes.length;)\n                    I.call(c, a.childNodes[0]); } });\n        });\n        r(Element.prototype, \"setAttribute\", function (a, b) { if (1 !== this.__CE_state)\n            return Q.call(this, a, b); var e = P.call(this, a); Q.call(this, a, b); b = P.call(this, a); c.attributeChangedCallback(this, a, e, b, null); });\n        r(Element.prototype, \"setAttributeNS\", function (a, b, d) { if (1 !== this.__CE_state)\n            return T.call(this, a, b, d); var e = S.call(this, a, b); T.call(this, a, b, d); d = S.call(this, a, b); c.attributeChangedCallback(this, b, e, d, a); });\n        r(Element.prototype, \"removeAttribute\", function (a) { if (1 !== this.__CE_state)\n            return R.call(this, a); var b = P.call(this, a); R.call(this, a); null !== b && c.attributeChangedCallback(this, a, b, null, null); });\n        r(Element.prototype, \"removeAttributeNS\", function (a, b) {\n            if (1 !== this.__CE_state)\n                return U.call(this, a, b);\n            var d = S.call(this, a, b);\n            U.call(this, a, b);\n            var e = S.call(this, a, b);\n            d !== e && c.attributeChangedCallback(this, b, d, e, a);\n        });\n        ua ? b(HTMLElement.prototype, ua) : ma ? b(Element.prototype, ma) : console.warn(\"Custom Elements: `Element#insertAdjacentElement` was not patched.\");\n        va ? d(HTMLElement.prototype, va) : na ? d(Element.prototype, na) : console.warn(\"Custom Elements: `Element#insertAdjacentHTML` was not patched.\");\n        Y(c, Element.prototype, { h: oa, append: pa });\n        Aa(c);\n    }\n    var Z = window.customElements;\n    if (!Z || Z.forcePolyfill || \"function\" != typeof Z.define || \"function\" != typeof Z.get) {\n        var X = new u;\n        xa();\n        ya();\n        Y(X, DocumentFragment.prototype, { h: ka, append: la });\n        za();\n        Ba();\n        document.__CE_hasRegistry = !0;\n        var customElements = new E(X);\n        Object.defineProperty(window, \"customElements\", { configurable: !0, enumerable: !0, value: customElements });\n    }\n}).call(self);\n// Polyfill document.baseURI\nif (typeof document.baseURI !== 'string') {\n    Object.defineProperty(Document.prototype, 'baseURI', {\n        enumerable: true,\n        configurable: true,\n        get: function () {\n            var base = document.querySelector('base');\n            if (base && base.href) {\n                return base.href;\n            }\n            return document.URL;\n        }\n    });\n}\n// Polyfill CustomEvent\nif (typeof window.CustomEvent !== 'function') {\n    window.CustomEvent = function CustomEvent(event, params) {\n        params = params || { bubbles: false, cancelable: false, detail: undefined };\n        var evt = document.createEvent('CustomEvent');\n        evt.initCustomEvent(event, params.bubbles, params.cancelable, params.detail);\n        return evt;\n    };\n    window.CustomEvent.prototype = window.Event.prototype;\n}\n// Event.composedPath\n(function (E, d, w) {\n    if (!E.composedPath) {\n        E.composedPath = function () {\n            if (this.path) {\n                return this.path;\n            }\n            var target = this.target;\n            this.path = [];\n            while (target.parentNode !== null) {\n                this.path.push(target);\n                target = target.parentNode;\n            }\n            this.path.push(d, w);\n            return this.path;\n        };\n    }\n})(Event.prototype, document, window);\n/*!\nElement.closest and Element.matches\nhttps://github.com/jonathantneal/closest\nCreative Commons Zero v1.0 Universal\n*/\n(function (a) { \"function\" !== typeof a.matches && (a.matches = a.msMatchesSelector || a.mozMatchesSelector || a.webkitMatchesSelector || function (a) { a = (this.document || this.ownerDocument).querySelectorAll(a); for (var b = 0; a[b] && a[b] !== this;)\n    ++b; return !!a[b]; }); \"function\" !== typeof a.closest && (a.closest = function (a) { for (var b = this; b && 1 === b.nodeType;) {\n    if (b.matches(a))\n        return b;\n    b = b.parentNode;\n} return null; }); })(window.Element.prototype);\n/*!\nElement.getRootNode()\n*/\n(function (c) { function d(a) { a = b(a); return a && 11 === a.nodeType ? d(a.host) : a; } function b(a) { return a && a.parentNode ? b(a.parentNode) : a; } \"function\" !== typeof c.getRootNode && (c.getRootNode = function (a) { return a && a.composed ? d(this) : b(this); }); })(Element.prototype);\n/*!\nElement.isConnected()\n*/\n(function (prototype) {\n    if (!(\"isConnected\" in prototype)) {\n        Object.defineProperty(prototype, 'isConnected', {\n            configurable: true,\n            enumerable: true,\n            get: function () {\n                var root = this.getRootNode({ composed: true });\n                return root && root.nodeType === 9;\n            }\n        });\n    }\n})(Element.prototype);\n/*!\nElement.remove()\n*/\n(function (b) { b.forEach(function (a) { a.hasOwnProperty(\"remove\") || Object.defineProperty(a, \"remove\", { configurable: !0, enumerable: !0, writable: !0, value: function () { null !== this.parentNode && this.parentNode.removeChild(this); } }); }); })([Element.prototype, CharacterData.prototype, DocumentType.prototype]);\n/*!\nElement.classList\n*/\n!function (e) { 'classList' in e || Object.defineProperty(e, \"classList\", { get: function () { var e = this, t = (e.getAttribute(\"class\") || \"\").replace(/^\\s+|\\s$/g, \"\").split(/\\s+/g); function n() { t.length > 0 ? e.setAttribute(\"class\", t.join(\" \")) : e.removeAttribute(\"class\"); } return \"\" === t[0] && t.splice(0, 1), t.toggle = function (e, i) { void 0 !== i ? i ? t.add(e) : t.remove(e) : -1 !== t.indexOf(e) ? t.splice(t.indexOf(e), 1) : t.push(e), n(); }, t.add = function () { for (var e = [].slice.call(arguments), i = 0, s = e.length; i < s; i++)\n        -1 === t.indexOf(e[i]) && t.push(e[i]); n(); }, t.remove = function () { for (var e = [].slice.call(arguments), i = 0, s = e.length; i < s; i++)\n        -1 !== t.indexOf(e[i]) && t.splice(t.indexOf(e[i]), 1); n(); }, t.item = function (e) { return t[e]; }, t.contains = function (e) { return -1 !== t.indexOf(e); }, t.replace = function (e, i) { -1 !== t.indexOf(e) && t.splice(t.indexOf(e), 1, i), n(); }, t.value = e.getAttribute(\"class\") || \"\", t; } }); }(Element.prototype);\n/*!\nDOMTokenList\n*/\n(function (prototype) {\n    try {\n        document.body.classList.add();\n    }\n    catch (e) {\n        var originalAdd = prototype.add;\n        var originalRemove = prototype.remove;\n        prototype.add = function () {\n            for (var i = 0; i < arguments.length; i++) {\n                originalAdd.call(this, arguments[i]);\n            }\n        };\n        prototype.remove = function () {\n            for (var i = 0; i < arguments.length; i++) {\n                originalRemove.call(this, arguments[i]);\n            }\n        };\n    }\n}(DOMTokenList.prototype));\n\n\n//# sourceURL=webpack:///../node_modules/@genexus/design-tokens-editor/dist/esm-es5/dom-76cc7c7d-0a082895.js?");

/***/ })

}]);