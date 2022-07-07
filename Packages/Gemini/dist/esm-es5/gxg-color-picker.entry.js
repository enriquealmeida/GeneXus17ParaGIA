var __assign = (this && this.__assign) || function () {
    __assign = Object.assign || function(t) {
        for (var s, i = 1, n = arguments.length; i < n; i++) {
            s = arguments[i];
            for (var p in s) if (Object.prototype.hasOwnProperty.call(s, p))
                t[p] = s[p];
        }
        return t;
    };
    return __assign.apply(this, arguments);
};
var __spreadArrays = (this && this.__spreadArrays) || function () {
    for (var s = 0, i = 0, il = arguments.length; i < il; i++) s += arguments[i].length;
    for (var r = Array(s), k = 0, i = 0; i < il; i++)
        for (var a = arguments[i], j = 0, jl = a.length; j < jl; j++, k++)
            r[k] = a[j];
    return r;
};
import { r as registerInstance, c as createEvent, h, H as Host, g as getElement } from './index-09b1517f.js';
import { c as createCommonjsModule, u as unwrapExports } from './_commonjsHelpers-870c1a1c.js';
var pickr_min = createCommonjsModule(function (module, exports) {
    /*! Pickr 1.7.4 MIT | https://github.com/Simonwep/pickr */
    !function (t, e) { module.exports = e(); }(window, (function () {
        return function (t) { var e = {}; function o(n) { if (e[n])
            return e[n].exports; var i = e[n] = { i: n, l: !1, exports: {} }; return t[n].call(i.exports, i, i.exports, o), i.l = !0, i.exports; } return o.m = t, o.c = e, o.d = function (t, e, n) { o.o(t, e) || Object.defineProperty(t, e, { enumerable: !0, get: n }); }, o.r = function (t) { "undefined" != typeof Symbol && Symbol.toStringTag && Object.defineProperty(t, Symbol.toStringTag, { value: "Module" }), Object.defineProperty(t, "__esModule", { value: !0 }); }, o.t = function (t, e) { if (1 & e && (t = o(t)), 8 & e)
            return t; if (4 & e && "object" == typeof t && t && t.__esModule)
            return t; var n = Object.create(null); if (o.r(n), Object.defineProperty(n, "default", { enumerable: !0, value: t }), 2 & e && "string" != typeof t)
            for (var i in t)
                o.d(n, i, function (e) { return t[e]; }.bind(null, i)); return n; }, o.n = function (t) { var e = t && t.__esModule ? function () { return t.default; } : function () { return t; }; return o.d(e, "a", e), e; }, o.o = function (t, e) { return Object.prototype.hasOwnProperty.call(t, e); }, o.p = "", o(o.s = 1); }([function (t) { t.exports = JSON.parse('{"a":"1.7.4"}'); }, function (t, e, o) {
                o.r(e);
                var n = {};
                function i(t, e, o, n, i) {
                    if (i === void 0) { i = {}; }
                    e instanceof HTMLCollection || e instanceof NodeList ? e = Array.from(e) : Array.isArray(e) || (e = [e]), Array.isArray(o) || (o = [o]);
                    for (var _i = 0, e_1 = e; _i < e_1.length; _i++) {
                        var r_1 = e_1[_i];
                        for (var _a = 0, o_1 = o; _a < o_1.length; _a++) {
                            var e_2 = o_1[_a];
                            r_1[t](e_2, n, __assign({ capture: !1 }, i));
                        }
                    }
                    return Array.prototype.slice.call(arguments, 1);
                }
                o.r(n), o.d(n, "on", (function () { return r; })), o.d(n, "off", (function () { return s; })), o.d(n, "createElementFromString", (function () { return a; })), o.d(n, "createFromTemplate", (function () { return c; })), o.d(n, "eventPath", (function () { return l; })), o.d(n, "resolveElement", (function () { return p; })), o.d(n, "adjustableInputNumbers", (function () { return u; }));
                var r = i.bind(null, "addEventListener"), s = i.bind(null, "removeEventListener");
                function a(t) { var e = document.createElement("div"); return e.innerHTML = t.trim(), e.firstElementChild; }
                function c(t) { var e = function (t, e) { var o = t.getAttribute(e); return t.removeAttribute(e), o; }, o = function (t, n) {
                    if (n === void 0) { n = {}; }
                    var i = e(t, ":obj"), r = e(t, ":ref"), s = i ? n[i] = {} : n;
                    r && (n[r] = t);
                    for (var _i = 0, _a = Array.from(t.children); _i < _a.length; _i++) {
                        var n_1 = _a[_i];
                        var t_1 = e(n_1, ":arr"), i_1 = o(n_1, t_1 ? {} : s);
                        t_1 && (s[t_1] || (s[t_1] = [])).push(Object.keys(i_1).length ? i_1 : n_1);
                    }
                    return n;
                }; return o(a(t)); }
                function l(t) { var e = t.path || t.composedPath && t.composedPath(); if (e)
                    return e; var o = t.target.parentElement; for (e = [t.target, o]; o = o.parentElement;)
                    e.push(o); return e.push(document, window), e; }
                function p(t) { return t instanceof Element ? t : "string" == typeof t ? t.split(/>>/g).reduce(function (t, e, o, n) { return (t = t.querySelector(e), o < n.length - 1 ? t.shadowRoot : t); }, document) : null; }
                function u(t, e) {
                    if (e === void 0) { e = (function (t) { return t; }); }
                    function o(o) { var n = [.001, .01, .1][Number(o.shiftKey || 2 * o.ctrlKey)] * (o.deltaY < 0 ? 1 : -1); var i = 0, r = t.selectionStart; t.value = t.value.replace(/[\d.]+/g, function (t, o) { return o <= r && o + t.length >= r ? (r = o, e(Number(t), n, i)) : (i++, t); }), t.focus(), t.setSelectionRange(r, r), o.preventDefault(), t.dispatchEvent(new Event("input")); }
                    r(t, "focus", function () { return r(window, "wheel", o, { passive: !1 }); }), r(t, "blur", function () { return s(window, "wheel", o); });
                }
                var h = o(0);
                var d = Math.min, f = Math.max, m = Math.floor, v = Math.round;
                function b(t, e, o) { e /= 100, o /= 100; var n = m(t = t / 360 * 6), i = t - n, r = o * (1 - e), s = o * (1 - i * e), a = o * (1 - (1 - i) * e), c = n % 6; return [255 * [o, s, r, r, a, o][c], 255 * [a, o, o, s, r, r][c], 255 * [r, r, a, o, o, s][c]]; }
                function g(t, e, o) { var n = (2 - (e /= 100)) * (o /= 100) / 2; return 0 !== n && (e = 1 === n ? 0 : n < .5 ? e * o / (2 * n) : e * o / (2 - 2 * n)), [t, 100 * e, 100 * n]; }
                function y(t, e, o) { var n = d(t /= 255, e /= 255, o /= 255), i = f(t, e, o), r = i - n; var s, a; if (0 === r)
                    s = a = 0;
                else {
                    a = r / i;
                    var n_2 = ((i - t) / 6 + r / 2) / r, c_1 = ((i - e) / 6 + r / 2) / r, l_1 = ((i - o) / 6 + r / 2) / r;
                    t === i ? s = l_1 - c_1 : e === i ? s = 1 / 3 + n_2 - l_1 : o === i && (s = 2 / 3 + c_1 - n_2), s < 0 ? s += 1 : s > 1 && (s -= 1);
                } return [360 * s, 100 * a, 100 * i]; }
                function _(t, e, o, n) { e /= 100, o /= 100; return __spreadArrays(y(255 * (1 - d(1, (t /= 100) * (1 - (n /= 100)) + n)), 255 * (1 - d(1, e * (1 - n) + n)), 255 * (1 - d(1, o * (1 - n) + n)))); }
                function w(t, e, o) { e /= 100; var n = 2 * (e *= (o /= 100) < .5 ? o : 1 - o) / (o + e) * 100, i = 100 * (o + e); return [t, isNaN(n) ? 0 : n, i]; }
                function A(t) { return y.apply(void 0, t.match(/.{2}/g).map(function (t) { return parseInt(t, 16); })); }
                function C(t) { t = t.match(/^[a-zA-Z]+$/) ? function (t) { if ("black" === t.toLowerCase())
                    return "#000"; var e = document.createElement("canvas").getContext("2d"); return e.fillStyle = t, "#000" === e.fillStyle ? null : e.fillStyle; }(t) : t; var e = { cmyk: /^cmyk[\D]+([\d.]+)[\D]+([\d.]+)[\D]+([\d.]+)[\D]+([\d.]+)/i, rgba: /^((rgba)|rgb)[\D]+([\d.]+)[\D]+([\d.]+)[\D]+([\d.]+)[\D]*?([\d.]+|$)/i, hsla: /^((hsla)|hsl)[\D]+([\d.]+)[\D]+([\d.]+)[\D]+([\d.]+)[\D]*?([\d.]+|$)/i, hsva: /^((hsva)|hsv)[\D]+([\d.]+)[\D]+([\d.]+)[\D]+([\d.]+)[\D]*?([\d.]+|$)/i, hexa: /^#?(([\dA-Fa-f]{3,4})|([\dA-Fa-f]{6})|([\dA-Fa-f]{8}))$/i }, o = function (t) { return t.map(function (t) { return /^(|\d+)\.\d+|\d+$/.test(t) ? Number(t) : void 0; }); }; var n; t: for (var i_2 in e) {
                    if (!(n = e[i_2].exec(t)))
                        continue;
                    var r_2 = function (t) { return !!n[2] == ("number" == typeof t); };
                    switch (i_2) {
                        case "cmyk": {
                            var _a = o(n), t_2 = _a[1], e_3 = _a[2], r_3 = _a[3], s_1 = _a[4];
                            if (t_2 > 100 || e_3 > 100 || r_3 > 100 || s_1 > 100)
                                break t;
                            return { values: _(t_2, e_3, r_3, s_1), type: i_2 };
                        }
                        case "rgba": {
                            var _b = o(n), t_3 = _b[3], e_4 = _b[4], s_2 = _b[5], a_1 = _b[6];
                            if (t_3 > 255 || e_4 > 255 || s_2 > 255 || a_1 < 0 || a_1 > 1 || !r_2(a_1))
                                break t;
                            return { values: __spreadArrays(y(t_3, e_4, s_2), [a_1]), a: a_1, type: i_2 };
                        }
                        case "hexa": {
                            var t_4 = n[1];
                            4 !== t_4.length && 3 !== t_4.length || (t_4 = t_4.split("").map(function (t) { return t + t; }).join(""));
                            var e_5 = t_4.substring(0, 6);
                            var o_2 = t_4.substring(6);
                            return o_2 = o_2 ? parseInt(o_2, 16) / 255 : void 0, { values: __spreadArrays(A(e_5), [o_2]), a: o_2, type: i_2 };
                        }
                        case "hsla": {
                            var _c = o(n), t_5 = _c[3], e_6 = _c[4], s_3 = _c[5], a_2 = _c[6];
                            if (t_5 > 360 || e_6 > 100 || s_3 > 100 || a_2 < 0 || a_2 > 1 || !r_2(a_2))
                                break t;
                            return { values: __spreadArrays(w(t_5, e_6, s_3), [a_2]), a: a_2, type: i_2 };
                        }
                        case "hsva": {
                            var _d = o(n), t_6 = _d[3], e_7 = _d[4], s_4 = _d[5], a_3 = _d[6];
                            if (t_6 > 360 || e_7 > 100 || s_4 > 100 || a_3 < 0 || a_3 > 1 || !r_2(a_3))
                                break t;
                            return { values: [t_6, e_7, s_4, a_3], a: a_3, type: i_2 };
                        }
                    }
                } return { values: null, type: null }; }
                function k(t, e, o, n) {
                    if (t === void 0) { t = 0; }
                    if (e === void 0) { e = 0; }
                    if (o === void 0) { o = 0; }
                    if (n === void 0) { n = 1; }
                    var i = function (t, e) { return function (o) {
                        if (o === void 0) { o = -1; }
                        return e(~o ? t.map(function (t) { return Number(t.toFixed(o)); }) : t);
                    }; }, r = { h: t, s: e, v: o, a: n, toHSVA: function () { var t = [r.h, r.s, r.v, r.a]; return t.toString = i(t, function (t) { return "hsva(".concat(t[0], ", ").concat(t[1], "%, ").concat(t[2], "%, ").concat(r.a, ")"); }), t; }, toHSLA: function () { var t = __spreadArrays(g(r.h, r.s, r.v), [r.a]); return t.toString = i(t, function (t) { return "hsla(".concat(t[0], ", ").concat(t[1], "%, ").concat(t[2], "%, ").concat(r.a, ")"); }), t; }, toRGBA: function () { var t = __spreadArrays(b(r.h, r.s, r.v), [r.a]); return t.toString = i(t, function (t) { return "rgba(".concat(t[0], ", ").concat(t[1], ", ").concat(t[2], ", ").concat(r.a, ")"); }), t; }, toCMYK: function () { var t = function (t, e, o) { var n = b(t, e, o), i = n[0] / 255, r = n[1] / 255, s = n[2] / 255, a = d(1 - i, 1 - r, 1 - s); return [100 * (1 === a ? 0 : (1 - i - a) / (1 - a)), 100 * (1 === a ? 0 : (1 - r - a) / (1 - a)), 100 * (1 === a ? 0 : (1 - s - a) / (1 - a)), 100 * a]; }(r.h, r.s, r.v); return t.toString = i(t, function (t) { return "cmyk(".concat(t[0], "%, ").concat(t[1], "%, ").concat(t[2], "%, ").concat(t[3], "%)"); }), t; }, toHEXA: function () { var t = function (t, e, o) { return b(t, e, o).map(function (t) { return v(t).toString(16).padStart(2, "0"); }); }(r.h, r.s, r.v), e = r.a >= 1 ? "" : Number((255 * r.a).toFixed(0)).toString(16).toUpperCase().padStart(2, "0"); return e && t.push(e), t.toString = function () { return "#".concat(t.join("").toUpperCase()); }, t; }, clone: function () { return k(r.h, r.s, r.v, r.a); } };
                    return r;
                }
                var S = function (t) { return Math.max(Math.min(t, 1), 0); };
                function O(t) { var e = { options: Object.assign({ lock: null, onchange: function () { return 0; }, onstop: function () { return 0; } }, t), _keyboard: function (t) { var o = e.options, n = t.type, i = t.key; if (document.activeElement === o.wrapper) {
                        var o_3 = e.options.lock, r_4 = "ArrowUp" === i, s_5 = "ArrowRight" === i, a_4 = "ArrowDown" === i, c_2 = "ArrowLeft" === i;
                        if ("keydown" === n && (r_4 || s_5 || a_4 || c_2)) {
                            var n_3 = 0, i_3 = 0;
                            "v" === o_3 ? n_3 = r_4 || s_5 ? 1 : -1 : "h" === o_3 ? n_3 = r_4 || s_5 ? -1 : 1 : (i_3 = r_4 ? -1 : a_4 ? 1 : 0, n_3 = c_2 ? -1 : s_5 ? 1 : 0), e.update(S(e.cache.x + .01 * n_3), S(e.cache.y + .01 * i_3)), t.preventDefault();
                        }
                        else
                            i.startsWith("Arrow") && (e.options.onstop(), t.preventDefault());
                    } }, _tapstart: function (t) { r(document, ["mouseup", "touchend", "touchcancel"], e._tapstop), r(document, ["mousemove", "touchmove"], e._tapmove), t.cancelable && t.preventDefault(), e._tapmove(t); }, _tapmove: function (t) { var o = e.options, n = e.cache, i = o.lock, r = o.element, s = o.wrapper, a = s.getBoundingClientRect(); var c = 0, l = 0; if (t) {
                        var e_8 = t && t.touches && t.touches[0];
                        c = t ? (e_8 || t).clientX : 0, l = t ? (e_8 || t).clientY : 0, c < a.left ? c = a.left : c > a.left + a.width && (c = a.left + a.width), l < a.top ? l = a.top : l > a.top + a.height && (l = a.top + a.height), c -= a.left, l -= a.top;
                    }
                    else
                        n && (c = n.x * a.width, l = n.y * a.height); "h" !== i && (r.style.left = "calc(".concat(c / a.width * 100, "% - ").concat(r.offsetWidth / 2, "px)")), "v" !== i && (r.style.top = "calc(".concat(l / a.height * 100, "% - ").concat(r.offsetHeight / 2, "px)")), e.cache = { x: c / a.width, y: l / a.height }; var p = S(c / a.width), u = S(l / a.height); switch (i) {
                        case "v": return o.onchange(p);
                        case "h": return o.onchange(u);
                        default: return o.onchange(p, u);
                    } }, _tapstop: function () { e.options.onstop(), s(document, ["mouseup", "touchend", "touchcancel"], e._tapstop), s(document, ["mousemove", "touchmove"], e._tapmove); }, trigger: function () { e._tapmove(); }, update: function (t, o) {
                        if (t === void 0) { t = 0; }
                        if (o === void 0) { o = 0; }
                        var _a = e.options.wrapper.getBoundingClientRect(), n = _a.left, i = _a.top, r = _a.width, s = _a.height;
                        "h" === e.options.lock && (o = t), e._tapmove({ clientX: n + r * t, clientY: i + s * o });
                    }, destroy: function () { var t = e.options, o = e._tapstart, n = e._keyboard; s(document, ["keydown", "keyup"], n), s([t.wrapper, t.element], "mousedown", o), s([t.wrapper, t.element], "touchstart", o, { passive: !1 }); } }, o = e.options, n = e._tapstart, i = e._keyboard; return r([o.wrapper, o.element], "mousedown", n), r([o.wrapper, o.element], "touchstart", n, { passive: !1 }), r(document, ["keydown", "keyup"], i), e; }
                function E(t) {
                    if (t === void 0) { t = {}; }
                    t = Object.assign({ onchange: function () { return 0; }, className: "", elements: [] }, t);
                    var e = r(t.elements, "click", function (e) { t.elements.forEach(function (o) { return o.classList[e.target === o ? "add" : "remove"](t.className); }), t.onchange(e), e.stopPropagation(); });
                    return { destroy: function () { return s.apply(void 0, e); } };
                }
                /*! NanoPop 2.1.0 MIT | https://github.com/Simonwep/nanopop */
                var L = { variantFlipOrder: { start: "sme", middle: "mse", end: "ems" }, positionFlipOrder: { top: "tbrl", right: "rltb", bottom: "btrl", left: "lrbt" }, position: "bottom", margin: 8 }, x = function (t, e, o) { var n = "object" != typeof t || t instanceof HTMLElement ? __assign({ reference: t, popper: e }, o) : t; return { update: function (t) {
                        if (t === void 0) { t = n; }
                        var _a = Object.assign(n, t), e = _a.reference, o = _a.popper;
                        if (!o || !e)
                            throw new Error("Popper- or reference-element missing.");
                        return (function (t, e, o) { var _a = __assign(__assign({ container: document.documentElement.getBoundingClientRect() }, L), o), n = _a.container, i = _a.margin, r = _a.position, s = _a.variantFlipOrder, a = _a.positionFlipOrder, _b = e.style, c = _b.left, l = _b.top; e.style.left = "0", e.style.top = "0"; var p = t.getBoundingClientRect(), u = e.getBoundingClientRect(), h = { t: p.top - u.height - i, b: p.bottom + i, r: p.right + i, l: p.left - u.width - i }, d = { vs: p.left, vm: p.left + p.width / 2 + -u.width / 2, ve: p.left + p.width - u.width, hs: p.top, hm: p.bottom - p.height / 2 - u.height / 2, he: p.bottom - u.height }, _c = r.split("-"), f = _c[0], _d = _c[1], m = _d === void 0 ? "middle" : _d, v = a[f], b = s[m], g = n.top, y = n.left, _ = n.bottom, w = n.right; for (var _i = 0, v_1 = v; _i < v_1.length; _i++) {
                            var t_7 = v_1[_i];
                            var o_4 = "t" === t_7 || "b" === t_7, n_4 = h[t_7], _e = o_4 ? ["top", "left"] : ["left", "top"], i_4 = _e[0], r_5 = _e[1], _f = o_4 ? [u.height, u.width] : [u.width, u.height], s_6 = _f[0], a_5 = _f[1], _g = o_4 ? [_, w] : [w, _], c_3 = _g[0], l_2 = _g[1], _h = o_4 ? [g, y] : [y, g], p_1 = _h[0], f_1 = _h[1];
                            if (!(n_4 < p_1 || n_4 + s_6 > c_3))
                                for (var _j = 0, b_1 = b; _j < b_1.length; _j++) {
                                    var s_7 = b_1[_j];
                                    var c_4 = d[(o_4 ? "v" : "h") + s_7];
                                    if (!(c_4 < f_1 || c_4 + a_5 > l_2))
                                        return e.style[r_5] = c_4 - u[r_5] + "px", e.style[i_4] = n_4 - u[i_4] + "px", t_7 + s_7;
                                }
                        } return e.style.left = c, e.style.top = l, null; })(e, o, n);
                    } }; };
                function j(t, e, o) { return e in t ? Object.defineProperty(t, e, { value: o, enumerable: !0, configurable: !0, writable: !0 }) : t[e] = o, t; }
                var B = /** @class */ (function () {
                    function B(t) {
                        var _this = this;
                        j(this, "_initializingActive", !0), j(this, "_recalc", !0), j(this, "_nanopop", null), j(this, "_root", null), j(this, "_color", k()), j(this, "_lastColor", k()), j(this, "_swatchColors", []), j(this, "_eventListener", { init: [], save: [], hide: [], show: [], clear: [], change: [], changestop: [], cancel: [], swatchselect: [] }), this.options = t = Object.assign(__assign({}, B.DEFAULT_OPTIONS), t);
                        var e = t.swatches, o = t.components, n = t.theme, i = t.sliders, r = t.lockOpacity, s = t.padding;
                        ["nano", "monolith"].includes(n) && !i && (t.sliders = "h"), o.interaction || (o.interaction = {});
                        var a = o.preview, c = o.opacity, l = o.hue, p = o.palette;
                        o.opacity = !r && c, o.palette = p || a || c || l, this._preBuild(), this._buildComponents(), this._bindEvents(), this._finalBuild(), e && e.length && e.forEach(function (t) { return _this.addSwatch(t); });
                        var _a = this._root, u = _a.button, h = _a.app;
                        this._nanopop = x(u, h, { margin: s }), u.setAttribute("role", "button"), u.setAttribute("aria-label", this._t("btn:toggle"));
                        var d = this;
                        requestAnimationFrame((function e() { if (!h.offsetWidth)
                            return requestAnimationFrame(e); d.setColor(t.default), d._rePositioningPicker(), t.defaultRepresentation && (d._representation = t.defaultRepresentation, d.setColorRepresentation(d._representation)), t.showAlways && d.show(), d._initializingActive = !1, d._emit("init"); }));
                    }
                    B.prototype._preBuild = function () { var t = this.options; for (var _i = 0, _a = ["el", "container"]; _i < _a.length; _i++) {
                        var e_9 = _a[_i];
                        t[e_9] = p(t[e_9]);
                    } this._root = (function (t) { var _a = t.options, e = _a.components, o = _a.useAsButton, n = _a.inline, i = _a.appClass, r = _a.theme, s = _a.lockOpacity, a = function (t) { return t ? "" : 'style="display:none" hidden'; }, l = function (e) { return t._t(e); }, p = c('\n      <div :ref="root" class="pickr">\n\n        '.concat(o ? "" : '<button type="button" :ref="button" class="pcr-button"></button>', '\n\n        <div :ref="app" class="pcr-app ').concat(i || "", '" data-theme="').concat(r, '" ').concat(n ? 'style="position: unset"' : "", ' aria-label="').concat(l("ui:dialog"), '" role="window">\n          <div class="pcr-selection" ').concat(a(e.palette), '>\n            <div :obj="preview" class="pcr-color-preview" ').concat(a(e.preview), '>\n              <button type="button" :ref="lastColor" class="pcr-last-color" aria-label="').concat(l("btn:last-color"), '"></button>\n              <div :ref="currentColor" class="pcr-current-color"></div>\n            </div>\n\n            <div :obj="palette" class="pcr-color-palette">\n              <div :ref="picker" class="pcr-picker"></div>\n              <div :ref="palette" class="pcr-palette" tabindex="0" aria-label="').concat(l("aria:palette"), '" role="listbox"></div>\n            </div>\n\n            <div :obj="hue" class="pcr-color-chooser" ').concat(a(e.hue), '>\n              <div :ref="picker" class="pcr-picker"></div>\n              <div :ref="slider" class="pcr-hue pcr-slider" tabindex="0" aria-label="').concat(l("aria:hue"), '" role="slider"></div>\n            </div>\n\n            <div :obj="opacity" class="pcr-color-opacity" ').concat(a(e.opacity), '>\n              <div :ref="picker" class="pcr-picker"></div>\n              <div :ref="slider" class="pcr-opacity pcr-slider" tabindex="0" aria-label="').concat(l("aria:opacity"), '" role="slider"></div>\n            </div>\n          </div>\n\n          <div class="pcr-swatches ').concat(e.palette ? "" : "pcr-last", '" :ref="swatches"></div>\n\n          <div :obj="interaction" class="pcr-interaction" ').concat(a(Object.keys(e.interaction).length), '>\n            <input :ref="result" class="pcr-result" type="text" spellcheck="false" ').concat(a(e.interaction.input), ' aria-label="').concat(l("aria:input"), '">\n\n            <input :arr="options" class="pcr-type" data-type="HEXA" value="').concat(s ? "HEX" : "HEXA", '" type="button" ').concat(a(e.interaction.hex), '>\n            <input :arr="options" class="pcr-type" data-type="RGBA" value="').concat(s ? "RGB" : "RGBA", '" type="button" ').concat(a(e.interaction.rgba), '>\n            <input :arr="options" class="pcr-type" data-type="HSLA" value="').concat(s ? "HSL" : "HSLA", '" type="button" ').concat(a(e.interaction.hsla), '>\n            <input :arr="options" class="pcr-type" data-type="HSVA" value="').concat(s ? "HSV" : "HSVA", '" type="button" ').concat(a(e.interaction.hsva), '>\n            <input :arr="options" class="pcr-type" data-type="CMYK" value="CMYK" type="button" ').concat(a(e.interaction.cmyk), '>\n\n            <input :ref="save" class="pcr-save" value="').concat(l("btn:save"), '" type="button" ').concat(a(e.interaction.save), ' aria-label="').concat(l("aria:btn:save"), '">\n            <input :ref="cancel" class="pcr-cancel" value="').concat(l("btn:cancel"), '" type="button" ').concat(a(e.interaction.cancel), ' aria-label="').concat(l("aria:btn:cancel"), '">\n            <input :ref="clear" class="pcr-clear" value="').concat(l("btn:clear"), '" type="button" ').concat(a(e.interaction.clear), ' aria-label="').concat(l("aria:btn:clear"), '">\n          </div>\n        </div>\n      </div>\n    ')), u = p.interaction; return u.options.find(function (t) { return !t.hidden && !t.classList.add("active"); }), u.type = function () { return u.options.find(function (t) { return t.classList.contains("active"); }); }, p; })(this), t.useAsButton && (this._root.button = t.el), t.container.appendChild(this._root.root); };
                    B.prototype._finalBuild = function () { var t = this.options, e = this._root; if (t.container.removeChild(e.root), t.inline) {
                        var o_5 = t.el.parentElement;
                        t.el.nextSibling ? o_5.insertBefore(e.app, t.el.nextSibling) : o_5.appendChild(e.app);
                    }
                    else
                        t.container.appendChild(e.app); t.useAsButton ? t.inline && t.el.remove() : t.el.parentNode.replaceChild(e.root, t.el), t.disabled && this.disable(), t.comparison || (e.button.style.transition = "none", t.useAsButton || (e.preview.lastColor.style.transition = "none")), this.hide(); };
                    B.prototype._buildComponents = function () {
                        var _this = this;
                        var t = this, e = this.options.components, o = (t.options.sliders || "v").repeat(2), _a = o.match(/^[vh]+$/g) ? o : [], n = _a[0], i = _a[1], r = function () { return _this._color || (_this._color = _this._lastColor.clone()); }, s = { palette: O({ element: t._root.palette.picker, wrapper: t._root.palette.palette, onstop: function () { return t._emit("changestop", t); }, onchange: function (o, n) { if (!e.palette)
                                    return; var i = r(), s = t._root, a = t.options, _a = s.preview, c = _a.lastColor, l = _a.currentColor; t._recalc && (i.s = 100 * o, i.v = 100 - 100 * n, i.v < 0 && (i.v = 0), t._updateOutput()); var p = i.toRGBA().toString(0); this.element.style.background = p, this.wrapper.style.background = "\n                        linear-gradient(to top, rgba(0, 0, 0, ".concat(i.a, "), transparent),\n                        linear-gradient(to left, hsla(").concat(i.h, ", 100%, 50%, ").concat(i.a, "), rgba(255, 255, 255, ").concat(i.a, "))\n                    "), a.comparison ? a.useAsButton || t._lastColor || (c.style.color = p) : (s.button.style.color = p, s.button.classList.remove("clear")); var u = i.toHEXA().toString(); for (var _i = 0, _b = t._swatchColors; _i < _b.length; _i++) {
                                    var _c = _b[_i], e_10 = _c.el, o_6 = _c.color;
                                    e_10.classList[u === o_6.toHEXA().toString() ? "add" : "remove"]("pcr-active");
                                } l.style.color = p; } }), hue: O({ lock: "v" === i ? "h" : "v", element: t._root.hue.picker, wrapper: t._root.hue.slider, onstop: function () { return t._emit("changestop", t); }, onchange: function (o) { if (!e.hue || !e.palette)
                                    return; var n = r(); t._recalc && (n.h = 360 * o), this.element.style.backgroundColor = "hsl(".concat(n.h, ", 100%, 50%)"), s.palette.trigger(); } }), opacity: O({ lock: "v" === n ? "h" : "v", element: t._root.opacity.picker, wrapper: t._root.opacity.slider, onstop: function () { return t._emit("changestop", t); }, onchange: function (o) { if (!e.opacity || !e.palette)
                                    return; var n = r(); t._recalc && (n.a = Math.round(100 * o) / 100), this.element.style.background = "rgba(0, 0, 0, ".concat(n.a, ")"), s.palette.trigger(); } }), selectable: E({ elements: t._root.interaction.options, className: "active", onchange: function (e) { t._representation = e.target.getAttribute("data-type").toUpperCase(), t._recalc && t._updateOutput(); } }) };
                        this._components = s;
                    };
                    B.prototype._bindEvents = function () {
                        var _this = this;
                        var _a = this, t = _a._root, e = _a.options, o = [r(t.interaction.clear, "click", function () { return _this._clearColor(); }), r([t.interaction.cancel, t.preview.lastColor], "click", function () { _this._emit("cancel", _this), _this.setHSVA.apply(_this, __spreadArrays((_this._lastColor || _this._color).toHSVA(), [!0])); }), r(t.interaction.save, "click", function () { !_this.applyColor() && !e.showAlways && _this.hide(); }), r(t.interaction.result, ["keyup", "input"], function (t) { _this.setColor(t.target.value, !0) && !_this._initializingActive && _this._emit("change", _this._color), t.stopImmediatePropagation(); }), r(t.interaction.result, ["focus", "blur"], function (t) { _this._recalc = "blur" === t.type, _this._recalc && _this._updateOutput(); }), r([t.palette.palette, t.palette.picker, t.hue.slider, t.hue.picker, t.opacity.slider, t.opacity.picker], ["mousedown", "touchstart"], function () { return _this._recalc = !0; }, { passive: !0 })];
                        if (!e.showAlways) {
                            var n_5 = e.closeWithKey;
                            o.push(r(t.button, "click", function () { return _this.isOpen() ? _this.hide() : _this.show(); }), r(document, "keyup", function (t) { return _this.isOpen() && (t.key === n_5 || t.code === n_5) && _this.hide(); }), r(document, ["touchstart", "mousedown"], function (e) { _this.isOpen() && !l(e).some(function (e) { return e === t.app || e === t.button; }) && _this.hide(); }, { capture: !0 }));
                        }
                        if (e.adjustableNumbers) {
                            var e_11 = { rgba: [255, 255, 255, 1], hsva: [360, 100, 100, 1], hsla: [360, 100, 100, 1], cmyk: [100, 100, 100, 100] };
                            u(t.interaction.result, function (t, o, n) { var i = e_11[_this.getColorRepresentation().toLowerCase()]; if (i) {
                                var e_12 = i[n], r_6 = t + (e_12 >= 100 ? 1e3 * o : o);
                                return r_6 <= 0 ? 0 : Number((r_6 < e_12 ? r_6 : e_12).toPrecision(3));
                            } return t; });
                        }
                        if (e.autoReposition && !e.inline) {
                            var t_8 = null;
                            var n_6 = this;
                            o.push(r(window, ["scroll", "resize"], function () { n_6.isOpen() && (e.closeOnScroll && n_6.hide(), null === t_8 ? (t_8 = setTimeout(function () { return t_8 = null; }, 100), requestAnimationFrame((function e() { n_6._rePositioningPicker(), null !== t_8 && requestAnimationFrame(e); }))) : (clearTimeout(t_8), t_8 = setTimeout(function () { return t_8 = null; }, 100))); }, { capture: !0 }));
                        }
                        this._eventBindings = o;
                    };
                    B.prototype._rePositioningPicker = function () { var t = this.options; if (!t.inline) {
                        if (!this._nanopop.update({ container: document.body.getBoundingClientRect(), position: t.position })) {
                            var t_9 = this._root.app, e_13 = t_9.getBoundingClientRect();
                            t_9.style.top = "".concat((window.innerHeight - e_13.height) / 2, "px"), t_9.style.left = "".concat((window.innerWidth - e_13.width) / 2, "px");
                        }
                    } };
                    B.prototype._updateOutput = function () { var _a = this, t = _a._root, e = _a._color, o = _a.options; if (t.interaction.type()) {
                        var n_7 = "to".concat(t.interaction.type().getAttribute("data-type"));
                        t.interaction.result.value = "function" == typeof e[n_7] ? e[n_7]().toString(o.outputPrecision) : "";
                    } !this._initializingActive && this._recalc && this._emit("change", e); };
                    B.prototype._clearColor = function (t) {
                        if (t === void 0) { t = !1; }
                        var _a = this, e = _a._root, o = _a.options;
                        o.useAsButton || (e.button.style.color = "rgba(0, 0, 0, 0.15)"), e.button.classList.add("clear"), o.showAlways || this.hide(), this._lastColor = null, this._initializingActive || t || (this._emit("save", null), this._emit("clear", this));
                    };
                    B.prototype._parseLocalColor = function (t) { var _a = C(t), e = _a.values, o = _a.type, n = _a.a, i = this.options.lockOpacity, r = void 0 !== n && 1 !== n; return e && 3 === e.length && (e[3] = void 0), { values: !e || i && r ? null : e, type: o }; };
                    B.prototype._t = function (t) { return this.options.i18n[t] || B.I18N_DEFAULTS[t]; };
                    B.prototype._emit = function (t) {
                        var _this = this;
                        var e = [];
                        for (var _i = 1; _i < arguments.length; _i++) {
                            e[_i - 1] = arguments[_i];
                        }
                        this._eventListener[t].forEach(function (t) { return t.apply(void 0, __spreadArrays(e, [_this])); });
                    };
                    B.prototype.on = function (t, e) { return this._eventListener[t].push(e), this; };
                    B.prototype.off = function (t, e) { var o = this._eventListener[t] || [], n = o.indexOf(e); return ~n && o.splice(n, 1), this; };
                    B.prototype.addSwatch = function (t) {
                        var _this = this;
                        var e = this._parseLocalColor(t).values;
                        if (e) {
                            var _a = this, t_10 = _a._swatchColors, o_7 = _a._root, n_8 = k.apply(void 0, e), i_5 = a('<button type="button" style="color: '.concat(n_8.toRGBA().toString(0), '" aria-label="').concat(this._t("btn:swatch"), '"/>'));
                            return o_7.swatches.appendChild(i_5), t_10.push({ el: i_5, color: n_8 }), this._eventBindings.push(r(i_5, "click", function () { _this.setHSVA.apply(_this, __spreadArrays(n_8.toHSVA(), [!0])), _this._emit("swatchselect", n_8), _this._emit("change", n_8); })), !0;
                        }
                        return !1;
                    };
                    B.prototype.removeSwatch = function (t) { var e = this._swatchColors[t]; if (e) {
                        var o_8 = e.el;
                        return this._root.swatches.removeChild(o_8), this._swatchColors.splice(t, 1), !0;
                    } return !1; };
                    B.prototype.applyColor = function (t) {
                        if (t === void 0) { t = !1; }
                        var _a = this._root, e = _a.preview, o = _a.button, n = this._color.toRGBA().toString(0);
                        return e.lastColor.style.color = n, this.options.useAsButton || (o.style.color = n), o.classList.remove("clear"), this._lastColor = this._color.clone(), this._initializingActive || t || this._emit("save", this._color), this;
                    };
                    B.prototype.destroy = function () {
                        var _this = this;
                        this._eventBindings.forEach(function (t) { return s.apply(void 0, t); }), Object.keys(this._components).forEach(function (t) { return _this._components[t].destroy(); });
                    };
                    B.prototype.destroyAndRemove = function () {
                        var _this = this;
                        this.destroy();
                        var _a = this._root, t = _a.root, e = _a.app;
                        t.parentElement && t.parentElement.removeChild(t), e.parentElement.removeChild(e), Object.keys(this).forEach(function (t) { return _this[t] = null; });
                    };
                    B.prototype.hide = function () { return this._root.app.classList.remove("visible"), this._emit("hide", this), this; };
                    B.prototype.show = function () { return this.options.disabled || (this._root.app.classList.add("visible"), this._rePositioningPicker(), this._emit("show", this)), this; };
                    B.prototype.isOpen = function () { return this._root.app.classList.contains("visible"); };
                    B.prototype.setHSVA = function (t, e, o, n, i) {
                        if (t === void 0) { t = 360; }
                        if (e === void 0) { e = 0; }
                        if (o === void 0) { o = 0; }
                        if (n === void 0) { n = 1; }
                        if (i === void 0) { i = !1; }
                        var r = this._recalc;
                        if (this._recalc = !1, t < 0 || t > 360 || e < 0 || e > 100 || o < 0 || o > 100 || n < 0 || n > 1)
                            return !1;
                        this._color = k(t, e, o, n);
                        var _a = this._components, s = _a.hue, a = _a.opacity, c = _a.palette;
                        return s.update(t / 360), a.update(n), c.update(e / 100, 1 - o / 100), i || this.applyColor(), r && this._updateOutput(), this._recalc = r, !0;
                    };
                    B.prototype.setColor = function (t, e) {
                        if (e === void 0) { e = !1; }
                        if (null === t)
                            return this._clearColor(e), !0;
                        var _a = this._parseLocalColor(t), o = _a.values, n = _a.type;
                        if (o) {
                            var t_11 = n.toUpperCase(), i_7 = this._root.interaction.options, r_7 = i_7.find(function (e) { return e.getAttribute("data-type") === t_11; });
                            if (r_7 && !r_7.hidden)
                                for (var _i = 0, i_6 = i_7; _i < i_6.length; _i++) {
                                    var t_12 = i_6[_i];
                                    t_12.classList[t_12 === r_7 ? "add" : "remove"]("active");
                                }
                            return !!this.setHSVA.apply(this, __spreadArrays(o, [e])) && this.setColorRepresentation(t_11);
                        }
                        return !1;
                    };
                    B.prototype.setColorRepresentation = function (t) { return t = t.toUpperCase(), !!this._root.interaction.options.find(function (e) { return e.getAttribute("data-type").startsWith(t) && !e.click(); }); };
                    B.prototype.getColorRepresentation = function () { return this._representation; };
                    B.prototype.getColor = function () { return this._color; };
                    B.prototype.getSelectedColor = function () { return this._lastColor; };
                    B.prototype.getRoot = function () { return this._root; };
                    B.prototype.disable = function () { return this.hide(), this.options.disabled = !0, this._root.button.classList.add("disabled"), this; };
                    B.prototype.enable = function () { return this.options.disabled = !1, this._root.button.classList.remove("disabled"), this; };
                    return B;
                }());
                j(B, "utils", n), j(B, "version", h.a), j(B, "I18N_DEFAULTS", { "ui:dialog": "color picker dialog", "btn:toggle": "toggle color picker dialog", "btn:swatch": "color swatch", "btn:last-color": "use previous color", "btn:save": "Save", "btn:cancel": "Cancel", "btn:clear": "Clear", "aria:btn:save": "save and close", "aria:btn:cancel": "cancel and close", "aria:btn:clear": "clear and close", "aria:input": "color input field", "aria:palette": "color selection area", "aria:hue": "hue selection slider", "aria:opacity": "selection slider" }), j(B, "DEFAULT_OPTIONS", { appClass: null, theme: "classic", useAsButton: !1, padding: 8, disabled: !1, comparison: !0, closeOnScroll: !1, outputPrecision: 0, lockOpacity: !1, autoReposition: !0, container: "body", components: { interaction: {} }, i18n: {}, swatches: null, inline: !1, sliders: null, default: "#42445a", defaultRepresentation: null, position: "bottom-middle", adjustableNumbers: !0, showAlways: !1, closeWithKey: "Escape" }), j(B, "create", function (t) { return new B(t); });
                e.default = B;
            }]).default;
    }));
});
var Pickr = unwrapExports(pickr_min);
var colorPickerCss = "/*! Pickr 1.7.4 MIT | https://github.com/Simonwep/pickr */.pickr{position:relative;overflow:visible;-webkit-transform:translateY(0);transform:translateY(0)}.pickr *{-webkit-box-sizing:border-box;box-sizing:border-box;outline:none;border:none;-webkit-appearance:none}.pickr .pcr-button{position:relative;height:2em;width:2em;padding:.5em;cursor:pointer;font-family:-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Helvetica Neue,Arial,sans-serif;border-radius:.15em;background:url('data:image/svg+xml;utf8, <svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 50 50\" stroke=\"%2342445A\" stroke-width=\"5px\" stroke-linecap=\"round\"><path d=\"M45,45L5,5\"></path><path d=\"M45,5L5,45\"></path></svg>') no-repeat 50%;background-size:0;-webkit-transition:all .3s;transition:all .3s}.pickr .pcr-button:before{background:url('data:image/svg+xml;utf8, <svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 2 2\"><path fill=\"white\" d=\"M1,0H2V1H1V0ZM0,1H1V2H0V1Z\"/><path fill=\"gray\" d=\"M0,0H1V1H0V0ZM1,1H2V2H1V1Z\"/></svg>');background-size:.5em;z-index:-1;z-index:auto}.pickr .pcr-button:after,.pickr .pcr-button:before{position:absolute;content:\"\";top:0;left:0;width:100%;height:100%;border-radius:.15em}.pickr .pcr-button:after{-webkit-transition:background .3s;transition:background .3s;background:currentColor}.pickr .pcr-button.clear{background-size:70%}.pickr .pcr-button.clear:before{opacity:0}.pickr .pcr-button.clear:focus{-webkit-box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px currentColor;box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px currentColor}.pickr .pcr-button.disabled{cursor:not-allowed}.pcr-app *,.pickr *{-webkit-box-sizing:border-box;box-sizing:border-box;outline:none;border:none;-webkit-appearance:none}.pcr-app button.pcr-active,.pcr-app button:focus,.pcr-app input.pcr-active,.pcr-app input:focus,.pickr button.pcr-active,.pickr button:focus,.pickr input.pcr-active,.pickr input:focus{-webkit-box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px currentColor;box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px currentColor}.pcr-app .pcr-palette,.pcr-app .pcr-slider,.pickr .pcr-palette,.pickr .pcr-slider{-webkit-transition:-webkit-box-shadow .3s;transition:-webkit-box-shadow .3s;transition:box-shadow .3s;transition:box-shadow .3s, -webkit-box-shadow .3s}.pcr-app .pcr-palette:focus,.pcr-app .pcr-slider:focus,.pickr .pcr-palette:focus,.pickr .pcr-slider:focus{-webkit-box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px rgba(0,0,0,.25);box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px rgba(0,0,0,.25)}.pcr-app{position:fixed;display:-ms-flexbox;display:flex;-ms-flex-direction:column;flex-direction:column;z-index:10000;border-radius:.1em;background:#fff;opacity:0;visibility:hidden;-webkit-transition:opacity .3s,visibility 0s .3s;transition:opacity .3s,visibility 0s .3s;font-family:-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Helvetica Neue,Arial,sans-serif;-webkit-box-shadow:0 .15em 1.5em 0 rgba(0,0,0,.1),0 0 1em 0 rgba(0,0,0,.03);box-shadow:0 .15em 1.5em 0 rgba(0,0,0,.1),0 0 1em 0 rgba(0,0,0,.03);left:0;top:0}.pcr-app.visible{-webkit-transition:opacity .3s;transition:opacity .3s;visibility:visible;opacity:1}.pcr-app .pcr-swatches{display:-ms-flexbox;display:flex;-ms-flex-wrap:wrap;flex-wrap:wrap;margin-top:.75em}.pcr-app .pcr-swatches.pcr-last{margin:0}@supports (display:grid){.pcr-app .pcr-swatches{display:grid;-ms-flex-align:center;align-items:center;grid-template-columns:repeat(auto-fit,1.75em)}}.pcr-app .pcr-swatches>button{font-size:1em;position:relative;width:calc(1.75em - 5px);height:calc(1.75em - 5px);border-radius:.15em;cursor:pointer;margin:2.5px;-ms-flex-negative:0;flex-shrink:0;justify-self:center;-webkit-transition:all .15s;transition:all .15s;overflow:hidden;background:transparent;z-index:1}.pcr-app .pcr-swatches>button:before{position:absolute;content:\"\";top:0;left:0;width:100%;height:100%;background:url('data:image/svg+xml;utf8, <svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 2 2\"><path fill=\"white\" d=\"M1,0H2V1H1V0ZM0,1H1V2H0V1Z\"/><path fill=\"gray\" d=\"M0,0H1V1H0V0ZM1,1H2V2H1V1Z\"/></svg>');background-size:6px;border-radius:.15em;z-index:-1}.pcr-app .pcr-swatches>button:after{content:\"\";position:absolute;top:0;left:0;width:100%;height:100%;background:currentColor;border:1px solid rgba(0,0,0,.05);border-radius:.15em;-webkit-box-sizing:border-box;box-sizing:border-box}.pcr-app .pcr-swatches>button:hover{-webkit-filter:brightness(1.05);filter:brightness(1.05)}.pcr-app .pcr-swatches>button:not(.pcr-active){-webkit-box-shadow:none;box-shadow:none}.pcr-app .pcr-interaction{display:-ms-flexbox;display:flex;-ms-flex-wrap:wrap;flex-wrap:wrap;-ms-flex-align:center;align-items:center;margin:0 -.2em}.pcr-app .pcr-interaction>*{margin:0 .2em}.pcr-app .pcr-interaction input{letter-spacing:.07em;font-size:.75em;text-align:center;cursor:pointer;color:#75797e;background:#f1f3f4;border-radius:.15em;-webkit-transition:all .15s;transition:all .15s;padding:.45em .5em;margin-top:.75em}.pcr-app .pcr-interaction input:hover{-webkit-filter:brightness(.975);filter:brightness(.975)}.pcr-app .pcr-interaction input:focus{-webkit-box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px rgba(66,133,244,.75);box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px rgba(66,133,244,.75)}.pcr-app .pcr-interaction .pcr-result{color:#75797e;text-align:left;-ms-flex:1 1 8em;flex:1 1 8em;min-width:8em;-webkit-transition:all .2s;transition:all .2s;border-radius:.15em;background:#f1f3f4;cursor:text}.pcr-app .pcr-interaction .pcr-result::-moz-selection{background:#4285f4;color:#fff}.pcr-app .pcr-interaction .pcr-result::selection{background:#4285f4;color:#fff}.pcr-app .pcr-interaction .pcr-type.active{color:#fff;background:#4285f4}.pcr-app .pcr-interaction .pcr-cancel,.pcr-app .pcr-interaction .pcr-clear,.pcr-app .pcr-interaction .pcr-save{width:auto;color:#fff}.pcr-app .pcr-interaction .pcr-cancel:hover,.pcr-app .pcr-interaction .pcr-clear:hover,.pcr-app .pcr-interaction .pcr-save:hover{-webkit-filter:brightness(.925);filter:brightness(.925)}.pcr-app .pcr-interaction .pcr-save{background:#4285f4}.pcr-app .pcr-interaction .pcr-cancel,.pcr-app .pcr-interaction .pcr-clear{background:#f44250}.pcr-app .pcr-interaction .pcr-cancel:focus,.pcr-app .pcr-interaction .pcr-clear:focus{-webkit-box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px rgba(244,66,80,.75);box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px rgba(244,66,80,.75)}.pcr-app .pcr-selection .pcr-picker{position:absolute;height:18px;width:18px;border:2px solid #fff;border-radius:100%;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none}.pcr-app .pcr-selection .pcr-color-chooser,.pcr-app .pcr-selection .pcr-color-opacity,.pcr-app .pcr-selection .pcr-color-palette{position:relative;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;display:-ms-flexbox;display:flex;-ms-flex-direction:column;flex-direction:column;cursor:grab;cursor:-webkit-grab}.pcr-app .pcr-selection .pcr-color-chooser:active,.pcr-app .pcr-selection .pcr-color-opacity:active,.pcr-app .pcr-selection .pcr-color-palette:active{cursor:grabbing;cursor:-webkit-grabbing}.pcr-app[data-theme=nano]{width:14.25em;max-width:95vw}.pcr-app[data-theme=nano] .pcr-swatches{margin-top:.6em;padding:0 .6em}.pcr-app[data-theme=nano] .pcr-interaction{padding:0 .6em .6em}.pcr-app[data-theme=nano] .pcr-selection{display:grid;grid-gap:.6em;grid-template-columns:1fr 4fr;grid-template-rows:5fr auto auto;-ms-flex-align:center;align-items:center;height:10.5em;width:100%;align-self:flex-start}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-preview{grid-area:2/1/4/1;height:100%;width:100%;display:-ms-flexbox;display:flex;-ms-flex-direction:row;flex-direction:row;-ms-flex-pack:center;justify-content:center;margin-left:.6em}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-preview .pcr-last-color{display:none}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-preview .pcr-current-color{position:relative;background:currentColor;width:2em;height:2em;border-radius:50em;overflow:hidden}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-preview .pcr-current-color:before{position:absolute;content:\"\";top:0;left:0;width:100%;height:100%;background:url('data:image/svg+xml;utf8, <svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 2 2\"><path fill=\"white\" d=\"M1,0H2V1H1V0ZM0,1H1V2H0V1Z\"/><path fill=\"gray\" d=\"M0,0H1V1H0V0ZM1,1H2V2H1V1Z\"/></svg>');background-size:.5em;border-radius:.15em;z-index:-1}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-palette{grid-area:1/1/2/3;width:100%;height:100%;z-index:1}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-palette .pcr-palette{border-radius:.15em;width:100%;height:100%}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-palette .pcr-palette:before{position:absolute;content:\"\";top:0;left:0;width:100%;height:100%;background:url('data:image/svg+xml;utf8, <svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 2 2\"><path fill=\"white\" d=\"M1,0H2V1H1V0ZM0,1H1V2H0V1Z\"/><path fill=\"gray\" d=\"M0,0H1V1H0V0ZM1,1H2V2H1V1Z\"/></svg>');background-size:.5em;border-radius:.15em;z-index:-1}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-chooser{grid-area:2/2/2/2}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-opacity{grid-area:3/2/3/2}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-chooser,.pcr-app[data-theme=nano] .pcr-selection .pcr-color-opacity{height:.5em;margin:0 .6em}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-chooser .pcr-picker,.pcr-app[data-theme=nano] .pcr-selection .pcr-color-opacity .pcr-picker{top:50%;-webkit-transform:translateY(-50%);transform:translateY(-50%)}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-chooser .pcr-slider,.pcr-app[data-theme=nano] .pcr-selection .pcr-color-opacity .pcr-slider{-ms-flex-positive:1;flex-grow:1;border-radius:50em}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-chooser .pcr-slider{background:-webkit-gradient(linear,left top, right top,from(red),color-stop(#ff0),color-stop(#0f0),color-stop(#0ff),color-stop(#00f),color-stop(#f0f),to(red));background:linear-gradient(90deg,red,#ff0,#0f0,#0ff,#00f,#f0f,red)}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-opacity .pcr-slider{background:-webkit-gradient(linear,left top, right top,from(transparent),to(#000)),url('data:image/svg+xml;utf8, <svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 2 2\"><path fill=\"white\" d=\"M1,0H2V1H1V0ZM0,1H1V2H0V1Z\"/><path fill=\"gray\" d=\"M0,0H1V1H0V0ZM1,1H2V2H1V1Z\"/></svg>');background:linear-gradient(90deg,transparent,#000),url('data:image/svg+xml;utf8, <svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 2 2\"><path fill=\"white\" d=\"M1,0H2V1H1V0ZM0,1H1V2H0V1Z\"/><path fill=\"gray\" d=\"M0,0H1V1H0V0ZM1,1H2V2H1V1Z\"/></svg>');background-size:100%,.25em}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{width:230px;display:block}.label{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;display:-ms-flexbox;display:flex;margin-bottom:var(--spacing-lay-xs)}.color-picker-main-container{-webkit-box-shadow:var(--box-shadow-01);box-shadow:var(--box-shadow-01);z-index:10;--cardAnimationDelay:0;position:relative;-webkit-animation-name:slideUp;animation-name:slideUp;-webkit-animation-duration:0.75s;animation-duration:0.75s;-webkit-animation-iteration-count:1;animation-iteration-count:1;-webkit-animation-delay:var(--cardAnimationDelay);animation-delay:var(--cardAnimationDelay);background:var(--color-background);position:relative;margin-top:-1px;-webkit-transition:max-height 0.5s ease-in-out;transition:max-height 0.5s ease-in-out;padding:var(--spacing-comp-02) var(--card-padding) 0 var(--card-padding)}@-webkit-keyframes slideUp{0%{top:5px;opacity:0}100%{top:0px;opacity:100%}}@keyframes slideUp{0%{top:5px;opacity:0}100%{top:0px;opacity:100%}}.color-picker-main-container-textbox{font-family:var(--font-family-primary);font-size:var(--font-size-md);color:var(--color--black);padding-left:var(--spacing-comp-02);padding-right:var(--spacing-comp-02);padding-top:var(--spacing-comp-01);padding-bottom:var(--spacing-comp-01);background:var(--color-background);border-width:var(--border-width-sm);border-style:var(--border-style-regular);border-color:var(--gray-04);letter-spacing:0.018em;height:var(--spacing-comp-05);border-radius:0;width:100%;margin-bottom:var(--spacing-comp-02);-webkit-box-sizing:border-box;box-sizing:border-box}.color-picker-main-container--visible{max-height:400px}.pcr-app{background:var(--color-background)}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-palette .pcr-palette{border-radius:0}.pickr{display:none}.pcr-app{max-width:100%;width:100% !important;-webkit-box-shadow:none;box-shadow:none}.cp-gxg-buttons{padding:var(--spacing-comp-02)}.color-picker-main-container-textbox{margin:0 var(--spacing-comp-02);width:calc(100% - 16px);margin-bottom:var(--spacing-comp-02)}.cp-gxg-buttons.before-color-value gxg-button{margin-right:var(--spacing-comp-02)}.cp-gxg-buttons.after-color-value{text-align:right}.pcr-color-chooser,.pcr-color-opacity{width:85%}";
var GxgColorPicker = /** @class */ (function () {
    function GxgColorPicker(hostRef) {
        registerInstance(this, hostRef);
        this.save = createEvent(this, "save", 7);
        this.nameInputEvent = createEvent(this, "nameInputEvent", 7);
        this.change = createEvent(this, "change", 7);
        /**
        The label of the color picker (optional)
        */
        this.label = "";
        /**
        The color value, such as "red", #CCDDEE, or rgba(220,140,40,.5)
        */
        this.value = "white";
        this.colorRepresentation = "HEXA";
        this.colorInputValue = "";
        this.colorChangedFromInput = false;
    }
    //Lyfe cycles
    GxgColorPicker.prototype.componentDidLoad = function () {
        var _this = this;
        //Detect color representation
        if (this.value.includes("rgb")) {
            this.colorRepresentation = "RGBA";
        }
        else if (this.value.includes("#")) {
            this.colorRepresentation = "HEXA";
        }
        var colorPickerEl = this.element.shadowRoot.querySelector(".color-picker");
        var colorPickerMainCtEl = this.element.shadowRoot.querySelector(".color-picker-main-container");
        this.pickr = new Pickr({
            el: colorPickerEl,
            theme: "nano",
            container: colorPickerMainCtEl,
            inline: true,
            showAlways: true,
            default: this.value,
            // useAsButton: true,
            components: {
                // Main components
                preview: true,
                opacity: true,
                hue: true,
                // Input / output Options
                interaction: {
                    // hex: true,
                    // rgb: true,
                    input: false
                    // save: true
                }
            }
        });
        this.pickr.on("change", function (color) {
            _this.colorObject = color;
            if (_this.colorRepresentation === "HEXA") {
                _this.value = _this.colorObject.toHEXA().toString();
            }
            else if (_this.colorRepresentation === "RGBA") {
                _this.value = _this.colorObject.toRGBA().toString(0);
            }
            _this.change.emit(_this.value);
        });
        this.pickr.on("show", function () {
            _this.colorObject = _this.pickr.getColor();
        });
        var options = {
            root: document.querySelector("body"),
            rootMargin: "0px",
            threshold: 1.0
        };
        var observer = new IntersectionObserver(function () {
            _this.pickr.setColor(_this.value); //We have to set the color by force, because we need to get the color at this time, and pickr seems to defer it.
        }, options);
        observer.observe(this.element);
    };
    GxgColorPicker.prototype.componentDidUnload = function () {
        this.pickr.destroy();
    };
    GxgColorPicker.prototype.watchHandler = function (newValue) {
        this.pickr.setColor(newValue);
    };
    //Button Methods
    GxgColorPicker.prototype.handleHexaButtonClick = function () {
        this.colorChangedFromInput = false;
        this.colorRepresentation = "HEXA";
        this.value = this.colorObject.toHEXA().toString();
        this.change.emit(this.value);
    };
    GxgColorPicker.prototype.handleRgbaButtonClick = function () {
        this.colorChangedFromInput = false;
        this.colorRepresentation = "RGBA";
        this.value = this.colorObject.toRGBA().toString(0);
        this.change.emit(this.value);
    };
    GxgColorPicker.prototype.handleTitleValueChange = function (ev) {
        var element = ev.target;
        this.label = element.value;
    };
    GxgColorPicker.prototype.handleColorValueChange = function (ev) {
        this.colorChangedFromInput = true;
        var element = ev.target;
        this.colorInputValue = element.value;
        this.pickr.setColor(element.value);
    };
    GxgColorPicker.prototype.colorValue = function () {
        if (!this.colorChangedFromInput) {
            //We only want to update the color value on the input if the pick was changed directly by handling the color picker window, not by changing the input color value
            if (this.colorObject === undefined) {
                return "";
            }
            if (this.colorRepresentation === "HEXA") {
                return this.colorObject.toHEXA().toString();
            }
            else if (this.colorRepresentation === "RGBA") {
                return this.colorObject.toRGBA().toString(0);
            }
        }
        this.colorChangedFromInput = true;
        return this.colorInputValue;
    };
    GxgColorPicker.prototype.setActiveButton = function () {
        if (this.value.includes("rgb")) {
            return "rgba";
        }
        else {
            return "hexa";
        }
    };
    GxgColorPicker.prototype.render = function () {
        return (h(Host, null, h("h1", { class: "label" }, this.label), h("div", { class: {
                "color-picker-main-container": true
            }, id: "color-picker-main-container" }, h("div", { class: "color-picker" }), h("div", { class: "cp-gxg-buttons before-color-value", slot: "editable" }, h("gxg-button-group", { "default-selected-btn-id": this.setActiveButton() }, h("button", { id: "rgba", onClick: this.handleRgbaButtonClick.bind(this) }, "RGBA"), h("button", { id: "hexa", onClick: this.handleHexaButtonClick.bind(this) }, "HEXA"))), h("input", { type: "text", name: "cp-color-value", id: "cp-color-value", value: this.colorValue(), class: "color-picker-main-container-textbox", onInput: this.handleColorValueChange.bind(this) }))));
    };
    Object.defineProperty(GxgColorPicker.prototype, "element", {
        get: function () { return getElement(this); },
        enumerable: false,
        configurable: true
    });
    Object.defineProperty(GxgColorPicker, "watchers", {
        get: function () {
            return {
                "value": ["watchHandler"]
            };
        },
        enumerable: false,
        configurable: true
    });
    return GxgColorPicker;
}());
GxgColorPicker.style = colorPickerCss;
export { GxgColorPicker as gxg_color_picker };
