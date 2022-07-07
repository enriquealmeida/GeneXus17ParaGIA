import { r as registerInstance, h, H as Host, g as getElement } from './index-09b1517f.js';
import { c as createCommonjsModule, u as unwrapExports } from './_commonjsHelpers-870c1a1c.js';
import { s as state } from './store-f5fbe254.js';
var datepicker_min = createCommonjsModule(function (module, exports) {
    !function (e, t) { module.exports = t(); }(window, (function () { return function (e) { var t = {}; function n(r) { if (t[r])
        return t[r].exports; var a = t[r] = { i: r, l: !1, exports: {} }; return e[r].call(a.exports, a, a.exports, n), a.l = !0, a.exports; } return n.m = e, n.c = t, n.d = function (e, t, r) { n.o(e, t) || Object.defineProperty(e, t, { enumerable: !0, get: r }); }, n.r = function (e) { "undefined" != typeof Symbol && Symbol.toStringTag && Object.defineProperty(e, Symbol.toStringTag, { value: "Module" }), Object.defineProperty(e, "__esModule", { value: !0 }); }, n.t = function (e, t) { if (1 & t && (e = n(e)), 8 & t)
        return e; if (4 & t && "object" == typeof e && e && e.__esModule)
        return e; var r = Object.create(null); if (n.r(r), Object.defineProperty(r, "default", { enumerable: !0, value: e }), 2 & t && "string" != typeof e)
        for (var a in e)
            n.d(r, a, function (t) { return e[t]; }.bind(null, a)); return r; }, n.n = function (e) { var t = e && e.__esModule ? function () { return e.default; } : function () { return e; }; return n.d(t, "a", t), t; }, n.o = function (e, t) { return Object.prototype.hasOwnProperty.call(e, t); }, n.p = "", n(n.s = 0); }([function (e, t, n) { n.r(t); n(1); var r = [], a = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"], o = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"], i = { t: "top", r: "right", b: "bottom", l: "left", c: "centered" }; function s() { } var l = ["click", "focusin", "keydown", "input"]; function c(e) { l.forEach((function (t) { e.addEventListener(t, e === document ? Y : j); })); } function d(e) { return Array.isArray(e) ? e.map(d) : "[object Object]" === x(e) ? Object.keys(e).reduce((function (t, n) { return t[n] = d(e[n]), t; }), {}) : e; } function u(e, t) { var n = e.calendar.querySelector(".qs-overlay"), r = n && !n.classList.contains("qs-hidden"); t = t || new Date(e.currentYear, e.currentMonth), e.calendar.innerHTML = [h(t, e, r), f(t, e, r), v(e, r)].join(""), r && window.requestAnimationFrame((function () { M(!0, e); })); } function h(e, t, n) { return ['<div class="qs-controls' + (n ? " qs-blur" : "") + '">', '<div class="qs-arrow qs-left"></div>', '<div class="qs-month-year">', '<span class="qs-month">' + t.months[e.getMonth()] + "</span>", '<span class="qs-year">' + e.getFullYear() + "</span>", "</div>", '<div class="qs-arrow qs-right"></div>', "</div>"].join(""); } function f(e, t, n) { var r = t.currentMonth, a = t.currentYear, o = t.dateSelected, i = t.maxDate, s = t.minDate, l = t.showAllDates, c = t.days, d = t.disabledDates, u = t.startDay, h = (t.events), f = t.getRange ? t.getRange() : {}, v = +f.start, m = +f.end, y = g(new Date(e).setDate(1)), p = y.getDay() - u, D = p < 0 ? 7 : 0; y.setMonth(y.getMonth() + 1), y.setDate(0); var b = y.getDate(), w = [], q = D + 7 * ((p + b) / 7 | 0); q += (p + b) % 7 ? 7 : 0; for (var S = 1; S <= q; S++) {
            var M = (S - 1) % 7, E = c[M], x = S - (p >= 0 ? p : 7 + p), C = new Date(a, r, x), Y = h[+C], j = x < 1 || x > b, L = j ? x < 1 ? -1 : 1 : 0, P = j && !l, k = P ? "" : C.getDate(), O = 0 === M || 6 === M, N = v !== m, _ = "qs-square " + E;
            Y && !P && (_ += " qs-event"), j && (_ += " qs-outside-current-month"), !l && j || (_ += " qs-num"), +C == +o && (_ += " qs-active"), (d[+C] || t.disabler(C) || O && t.noWeekends || s && +C < +s || i && +C > +i) && !P && (_ += " qs-disabled"), +g(new Date) == +C && (_ += " qs-current"), +C === v && m && N && (_ += " qs-range-start"), +C > v && +C < m && (_ += " qs-range-middle"), +C === m && v && N && (_ += " qs-range-end"), P && (_ += " qs-empty", k = ""), w.push('<div class="' + _ + '" data-direction="' + L + '">' + k + "</div>");
        } var I = c.map((function (e) { return '<div class="qs-square qs-day">' + e + "</div>"; })).concat(w); return I.unshift('<div class="qs-squares' + (n ? " qs-blur" : "") + '">'), I.push("</div>"), I.join(""); } function v(e, t) { var n = e.overlayPlaceholder, r = e.overlayButton; return ['<div class="qs-overlay' + (t ? "" : " qs-hidden") + '">', "<div>", '<input class="qs-overlay-year" placeholder="' + n + '" />', '<div class="qs-close">&#10005;</div>', "</div>", '<div class="qs-overlay-month-container">' + e.overlayMonths.map((function (e, t) { return '<div class="qs-overlay-month" data-month-num="' + t + '">' + e + "</div>"; })).join("") + "</div>", '<div class="qs-submit qs-disabled">' + r + "</div>", "</div>"].join(""); } function m(e, t, n) { var r = t.el, a = t.calendar.querySelector(".qs-active"), o = e.textContent, i = t.sibling; (r.disabled || r.readOnly) && t.respectDisabledReadOnly || (t.dateSelected = n ? void 0 : new Date(t.currentYear, t.currentMonth, o), a && a.classList.remove("qs-active"), n || e.classList.add("qs-active"), p(r, t, n), n || q(t), i && (y({ instance: t, deselect: n }), t.first && !i.dateSelected && (i.currentYear = t.currentYear, i.currentMonth = t.currentMonth, i.currentMonthName = t.currentMonthName), u(t), u(i)), t.onSelect(t, n ? void 0 : new Date(t.dateSelected))); } function y(e) { var t = e.instance.first ? e.instance : e.instance.sibling, n = t.sibling; t === e.instance ? e.deselect ? (t.minDate = t.originalMinDate, n.minDate = n.originalMinDate) : n.minDate = t.dateSelected : e.deselect ? (n.maxDate = n.originalMaxDate, t.maxDate = t.originalMaxDate) : t.maxDate = n.dateSelected; } function p(e, t, n) { if (!t.nonInput)
            return n ? e.value = "" : t.formatter !== s ? t.formatter(e, t.dateSelected, t) : void (e.value = t.dateSelected.toDateString()); } function D(e, t, n, r) { n || r ? (n && (t.currentYear = +n), r && (t.currentMonth = +r)) : (t.currentMonth += e.contains("qs-right") ? 1 : -1, 12 === t.currentMonth ? (t.currentMonth = 0, t.currentYear++) : -1 === t.currentMonth && (t.currentMonth = 11, t.currentYear--)), t.currentMonthName = t.months[t.currentMonth], u(t), t.onMonthChange(t); } function b(e) { if (!e.noPosition) {
            var t = e.position.top, n = e.position.right;
            if (e.position.centered)
                return e.calendarContainer.classList.add("qs-centered");
            var r = e.positionedEl.getBoundingClientRect(), a = e.el.getBoundingClientRect(), o = e.calendarContainer.getBoundingClientRect(), i = a.top - r.top + (t ? -1 * o.height : a.height) + "px", s = a.left - r.left + (n ? a.width - o.width : 0) + "px";
            e.calendarContainer.style.setProperty("top", i), e.calendarContainer.style.setProperty("left", s);
        } } function w(e) { return "[object Date]" === x(e) && "Invalid Date" !== e.toString(); } function g(e) { if (w(e) || "number" == typeof e && !isNaN(e)) {
            var t = new Date(+e);
            return new Date(t.getFullYear(), t.getMonth(), t.getDate());
        } } function q(e) { e.disabled || !e.calendarContainer.classList.contains("qs-hidden") && !e.alwaysShow && (M(!0, e), e.calendarContainer.classList.add("qs-hidden"), e.onHide(e)); } function S(e) { e.disabled || (e.calendarContainer.classList.remove("qs-hidden"), b(e), e.onShow(e)); } function M(e, t) { var n = t.calendar, r = n.querySelector(".qs-overlay"), a = r.querySelector(".qs-overlay-year"), o = n.querySelector(".qs-controls"), i = n.querySelector(".qs-squares"); e ? (r.classList.add("qs-hidden"), o.classList.remove("qs-blur"), i.classList.remove("qs-blur"), a.value = "") : (r.classList.remove("qs-hidden"), o.classList.add("qs-blur"), i.classList.add("qs-blur"), a.focus()); } function E(e, t, n, r) { var a = isNaN(+(new Date).setFullYear(t.value || void 0)), o = a ? null : t.value; if (13 === e.which || 13 === e.keyCode || "click" === e.type)
            r ? D(null, n, o, r) : a || t.classList.contains("qs-disabled") || D(null, n, o);
        else if (n.calendar.contains(t)) {
            n.calendar.querySelector(".qs-submit").classList[a ? "add" : "remove"]("qs-disabled");
        } } function x(e) { return {}.toString.call(e); } function C(e) { r.forEach((function (t) { t !== e && q(t); })); } function Y(e) { if (!e.__qs_shadow_dom) {
            var t = e.which || e.keyCode, n = e.type, a = e.target, i = a.classList, s = r.filter((function (e) { return e.calendar.contains(a) || e.el === a; }))[0], l = s && s.calendar.contains(a);
            if (!(s && s.isMobile && s.disableMobile))
                if ("click" === n) {
                    if (!s)
                        return r.forEach(q);
                    if (s.disabled)
                        return;
                    var c = s.calendar, d = s.calendarContainer, h = s.disableYearOverlay, f = s.nonInput, v = c.querySelector(".qs-overlay-year"), y = !!c.querySelector(".qs-hidden"), p = c.querySelector(".qs-month-year").contains(a), b = a.dataset.monthNum;
                    if (s.noPosition && !l)
                        (d.classList.contains("qs-hidden") ? S : q)(s);
                    else if (i.contains("qs-arrow"))
                        D(i, s);
                    else if (p || i.contains("qs-close"))
                        h || M(!y, s);
                    else if (b)
                        E(e, v, s, b);
                    else {
                        if (i.contains("qs-disabled"))
                            return;
                        if (i.contains("qs-num")) {
                            var w = a.textContent, g = +a.dataset.direction, x = new Date(s.currentYear, s.currentMonth + g, w);
                            if (g) {
                                s.currentYear = x.getFullYear(), s.currentMonth = x.getMonth(), s.currentMonthName = o[s.currentMonth], u(s);
                                for (var Y, j = s.calendar.querySelectorAll('[data-direction="0"]'), L = 0; !Y;) {
                                    var P = j[L];
                                    P.textContent === w && (Y = P), L++;
                                }
                                a = Y;
                            }
                            return void (+x == +s.dateSelected ? m(a, s, !0) : a.classList.contains("qs-disabled") || m(a, s));
                        }
                        i.contains("qs-submit") ? E(e, v, s) : f && a === s.el && (S(s), C(s));
                    }
                }
                else if ("focusin" === n && s)
                    S(s), C(s);
                else if ("keydown" === n && 9 === t && s)
                    q(s);
                else if ("keydown" === n && s && !s.disabled) {
                    var k = !s.calendar.querySelector(".qs-overlay").classList.contains("qs-hidden");
                    13 === t && k && l ? E(e, a, s) : 27 === t && k && l && M(!0, s);
                }
                else if ("input" === n) {
                    if (!s || !s.calendar.contains(a))
                        return;
                    var O = s.calendar.querySelector(".qs-submit"), N = a.value.split("").reduce((function (e, t) { return e || "0" !== t ? e + (t.match(/[0-9]/) ? t : "") : ""; }), "").slice(0, 4);
                    a.value = N, O.classList[4 === N.length ? "remove" : "add"]("qs-disabled");
                }
        } } function j(e) { Y(e), e.__qs_shadow_dom = !0; } function L(e, t) { l.forEach((function (n) { e.removeEventListener(n, t); })); } function P() { S(this); } function k() { q(this); } function O(e, t) { var n = g(e), r = this.currentYear, a = this.currentMonth, o = this.sibling; if (null == e)
            return this.dateSelected = void 0, p(this.el, this, !0), o && (y({ instance: this, deselect: !0 }), u(o)), u(this), this; if (!w(e))
            throw new Error("`setDate` needs a JavaScript Date object."); if (this.disabledDates[+n] || n < this.minDate || n > this.maxDate)
            throw new Error("You can't manually set a date that's disabled."); this.dateSelected = n, t && (this.currentYear = n.getFullYear(), this.currentMonth = n.getMonth(), this.currentMonthName = this.months[n.getMonth()]), p(this.el, this), o && (y({ instance: this }), u(o)); var i = r === n.getFullYear() && a === n.getMonth(); return i || t ? u(this, n) : i || u(this, new Date(r, a, 1)), this; } function N(e) { return I(this, e, !0); } function _(e) { return I(this, e); } function I(e, t, n) { var r = e.dateSelected, a = e.first, o = e.sibling, i = e.minDate, s = e.maxDate, l = g(t), c = n ? "Min" : "Max"; function d() { return "original" + c + "Date"; } function h() { return c.toLowerCase() + "Date"; } function f() { return "set" + c; } function v() { throw new Error("Out-of-range date passed to " + f()); } if (null == t)
            e[d()] = void 0, o ? (o[d()] = void 0, n ? (a && !r || !a && !o.dateSelected) && (e.minDate = void 0, o.minDate = void 0) : (a && !o.dateSelected || !a && !r) && (e.maxDate = void 0, o.maxDate = void 0)) : e[h()] = void 0;
        else {
            if (!w(t))
                throw new Error("Invalid date passed to " + f());
            o ? ((a && n && l > (r || s) || a && !n && l < (o.dateSelected || i) || !a && n && l > (o.dateSelected || s) || !a && !n && l < (r || i)) && v(), e[d()] = l, o[d()] = l, (n && (a && !r || !a && !o.dateSelected) || !n && (a && !o.dateSelected || !a && !r)) && (e[h()] = l, o[h()] = l)) : ((n && l > (r || s) || !n && l < (r || i)) && v(), e[h()] = l);
        } return o && u(o), u(e), e; } function R() { var e = this.first ? this : this.sibling, t = e.sibling; return { start: e.dateSelected, end: t.dateSelected }; } function A() { var e = this.shadowDom, t = this.positionedEl, n = this.calendarContainer, a = this.sibling, o = this; this.inlinePosition && (r.some((function (e) { return e !== o && e.positionedEl === t; })) || t.style.setProperty("position", null)); n.remove(), r = r.filter((function (e) { return e !== o; })), a && delete a.sibling, r.length || L(document, Y); var i = r.some((function (t) { return t.shadowDom === e; })); for (var s in e && !i && L(e, j), this)
            delete this[s]; r.length || l.forEach((function (e) { document.removeEventListener(e, Y); })); } function F(e, t) { var n = new Date(e); if (!w(n))
            throw new Error("`navigate` needs a JavaScript Date object."); this.currentYear = n.getFullYear(), this.currentMonth = n.getMonth(), u(this), t && this.onMonthChange(this); } t.default = function (e, t) { var n = function (e, t) { var n, l, c = function (e) { var t = d(e); t.events && (t.events = t.events.reduce((function (e, t) { if (!w(t))
            throw new Error('"options.events" must only contain valid JavaScript Date objects.'); return e[+g(t)] = !0, e; }), {})); ["startDate", "dateSelected", "minDate", "maxDate"].forEach((function (e) { var n = t[e]; if (n && !w(n))
            throw new Error('"options.' + e + '" needs to be a valid JavaScript Date object.'); t[e] = g(n); })); var n = t.position, o = t.maxDate, l = t.minDate, c = t.dateSelected, u = t.overlayPlaceholder, h = t.overlayButton, f = t.startDay, v = t.id; if (t.startDate = g(t.startDate || c || new Date), t.disabledDates = (t.disabledDates || []).reduce((function (e, t) { var n = +g(t); if (!w(t))
            throw new Error('You supplied an invalid date to "options.disabledDates".'); if (n === +g(c))
            throw new Error('"disabledDates" cannot contain the same date as "dateSelected".'); return e[n] = 1, e; }), {}), t.hasOwnProperty("id") && null == v)
            throw new Error("Id cannot be `null` or `undefined`"); if (null != v) {
            var m = r.filter((function (e) { return e.id === v; }));
            if (m.length > 1)
                throw new Error("Only two datepickers can share an id.");
            m.length ? (t.second = !0, t.sibling = m[0]) : t.first = !0;
        } var y = ["tr", "tl", "br", "bl", "c"].some((function (e) { return n === e; })); if (n && !y)
            throw new Error('"options.position" must be one of the following: tl, tr, bl, br, or c.'); function p(e) { throw new Error('"dateSelected" in options is ' + (e ? "less" : "greater") + ' than "' + (e || "max") + 'Date".'); } if (t.position = function (e) { var t = e[0], n = e[1], r = {}; r[i[t]] = 1, n && (r[i[n]] = 1); return r; }(n || "bl"), o < l)
            throw new Error('"maxDate" in options is less than "minDate".'); c && (l > c && p("min"), o < c && p()); if (["onSelect", "onShow", "onHide", "onMonthChange", "formatter", "disabler"].forEach((function (e) { "function" != typeof t[e] && (t[e] = s); })), ["customDays", "customMonths", "customOverlayMonths"].forEach((function (e, n) { var r = t[e], a = n ? 12 : 7; if (r) {
            if (!Array.isArray(r) || r.length !== a || r.some((function (e) { return "string" != typeof e; })))
                throw new Error('"' + e + '" must be an array with ${num} strings.');
            t[n ? n < 2 ? "months" : "overlayMonths" : "days"] = r;
        } })), f && f > 0 && f < 7) {
            var D = (t.customDays || a).slice(), b = D.splice(0, f);
            t.customDays = D.concat(b), t.startDay = +f, t.weekendIndices = [D.length - 1, D.length];
        }
        else
            t.startDay = 0, t.weekendIndices = [6, 0]; "string" != typeof u && delete t.overlayPlaceholder; "string" != typeof h && delete t.overlayButton; return t; }(t || { startDate: g(new Date), position: "bl" }), u = e; if ("string" == typeof u)
            u = "#" === u[0] ? document.getElementById(u.slice(1)) : document.querySelector(u);
        else {
            if ("[object ShadowRoot]" === x(u))
                throw new Error("Using a shadow DOM as your selector is not supported.");
            for (var h, f = ("getRootNode" in window.Node.prototype), v = u.parentNode; !h;) {
                var m = x(v);
                if ("[object HTMLDocument]" === m)
                    h = !0;
                else if ("[object ShadowRoot]" === m) {
                    if (h = !0, !f)
                        throw new Error("The shadow DOM is not supported in your browser.");
                    n = v, l = v.host;
                }
                else
                    v = v.parentNode;
            }
        } if (!u)
            throw new Error("No selector / element found."); if (r.some((function (e) { return e.el === u; })))
            throw new Error("A datepicker already exists on that element."); var y = u === document.body, D = n ? u.parentElement || n : y ? document.body : u.parentElement, b = n ? u.parentElement || l : D, q = document.createElement("div"), M = document.createElement("div"); q.className = "qs-datepicker-container qs-hidden", M.className = "qs-datepicker"; var E = { shadowDom: n, customElement: l, positionedEl: b, el: u, parent: D, nonInput: "INPUT" !== u.nodeName, noPosition: y, position: !y && c.position, startDate: c.startDate, dateSelected: c.dateSelected, disabledDates: c.disabledDates, minDate: c.minDate, maxDate: c.maxDate, noWeekends: !!c.noWeekends, weekendIndices: c.weekendIndices, calendarContainer: q, calendar: M, currentMonth: (c.startDate || c.dateSelected).getMonth(), currentMonthName: (c.months || o)[(c.startDate || c.dateSelected).getMonth()], currentYear: (c.startDate || c.dateSelected).getFullYear(), events: c.events || {}, setDate: O, remove: A, setMin: N, setMax: _, show: P, hide: k, navigate: F, onSelect: c.onSelect, onShow: c.onShow, onHide: c.onHide, onMonthChange: c.onMonthChange, formatter: c.formatter, disabler: c.disabler, months: c.months || o, days: c.customDays || a, startDay: c.startDay, overlayMonths: c.overlayMonths || (c.months || o).map((function (e) { return e.slice(0, 3); })), overlayPlaceholder: c.overlayPlaceholder || "4-digit year", overlayButton: c.overlayButton || "Submit", disableYearOverlay: !!c.disableYearOverlay, disableMobile: !!c.disableMobile, isMobile: "ontouchstart" in window, alwaysShow: !!c.alwaysShow, id: c.id, showAllDates: !!c.showAllDates, respectDisabledReadOnly: !!c.respectDisabledReadOnly, first: c.first, second: c.second }; if (c.sibling) {
            var C = c.sibling, Y = E, j = C.minDate || Y.minDate, L = C.maxDate || Y.maxDate;
            Y.sibling = C, C.sibling = Y, C.minDate = j, C.maxDate = L, Y.minDate = j, Y.maxDate = L, C.originalMinDate = j, C.originalMaxDate = L, Y.originalMinDate = j, Y.originalMaxDate = L, C.getRange = R, Y.getRange = R;
        } c.dateSelected && p(u, E); var I = getComputedStyle(b).position; y || I && "static" !== I || (E.inlinePosition = !0, b.style.setProperty("position", "relative")); var B = r.filter((function (e) { return e.positionedEl === E.positionedEl; })); B.some((function (e) { return e.inlinePosition; })) && (E.inlinePosition = !0, B.forEach((function (e) { e.inlinePosition = !0; }))); q.appendChild(M), D.appendChild(q), E.alwaysShow && S(E); return E; }(e, t); if (r.length || c(document), n.shadowDom && (r.some((function (e) { return e.shadowDom === n.shadowDom; })) || c(n.shadowDom)), r.push(n), n.second) {
            var l = n.sibling;
            y({ instance: n, deselect: !n.dateSelected }), y({ instance: l, deselect: !l.dateSelected }), u(l);
        } return u(n, n.startDate || n.dateSelected), n.alwaysShow && b(n), n; }; }, function (e, t, n) { }]).default; }));
});
var datepicker = unwrapExports(datepicker_min);
var datePickerCss = "/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.qs-datepicker-container{font-size:1rem;font-family:sans-serif;color:black;position:absolute;width:15.625em;display:-ms-flexbox;display:flex;-ms-flex-direction:column;flex-direction:column;z-index:9001;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;border:1px solid gray;border-radius:0.263921875em;overflow:hidden;background:white;-webkit-box-shadow:0 1.25em 1.25em -0.9375em rgba(0, 0, 0, 0.3);box-shadow:0 1.25em 1.25em -0.9375em rgba(0, 0, 0, 0.3)}.qs-datepicker-container *{-webkit-box-sizing:border-box;box-sizing:border-box}.qs-centered{position:fixed;top:50%;left:50%;-webkit-transform:translate(-50%, -50%);transform:translate(-50%, -50%)}.qs-hidden{display:none}.qs-overlay{position:absolute;top:0;left:0;background:rgba(0, 0, 0, 0.75);color:white;width:100%;height:100%;padding:0.5em;z-index:1;opacity:1;-webkit-transition:opacity 0.3s;transition:opacity 0.3s;display:-ms-flexbox;display:flex;-ms-flex-direction:column;flex-direction:column}.qs-overlay.qs-hidden{opacity:0;z-index:-1}.qs-overlay .qs-overlay-year{border:none;background:transparent;border-bottom:1px solid white;border-radius:0;color:white;font-size:0.875em;padding:0.25em 0;width:80%;text-align:center;margin:0 auto;display:block}.qs-overlay .qs-overlay-year::-webkit-inner-spin-button{-webkit-appearance:none}.qs-overlay .qs-close{padding:0.5em;cursor:pointer;position:absolute;top:0;right:0}.qs-overlay .qs-submit{border:1px solid white;border-radius:0.263921875em;padding:0.5em;margin:0 auto auto;cursor:pointer;background:rgba(128, 128, 128, 0.4)}.qs-overlay .qs-submit.qs-disabled{color:gray;border-color:gray;cursor:not-allowed}.qs-overlay .qs-overlay-month-container{display:-ms-flexbox;display:flex;-ms-flex-wrap:wrap;flex-wrap:wrap;-ms-flex-positive:1;flex-grow:1}.qs-overlay .qs-overlay-month{display:-ms-flexbox;display:flex;-ms-flex-pack:center;justify-content:center;-ms-flex-align:center;align-items:center;width:calc(100% / 3);cursor:pointer;opacity:0.5;-webkit-transition:opacity 0.15s;transition:opacity 0.15s}.qs-overlay .qs-overlay-month.active,.qs-overlay .qs-overlay-month:hover{opacity:1}.qs-controls{width:100%;display:-ms-flexbox;display:flex;-ms-flex-pack:justify;justify-content:space-between;-ms-flex-align:center;align-items:center;-ms-flex-positive:1;flex-grow:1;-ms-flex-negative:0;flex-shrink:0;background:lightgray;-webkit-filter:blur(0px);filter:blur(0px);-webkit-transition:-webkit-filter 0.3s;transition:-webkit-filter 0.3s;transition:filter 0.3s;transition:filter 0.3s, -webkit-filter 0.3s}.qs-controls.qs-blur{-webkit-filter:blur(5px);filter:blur(5px)}.qs-arrow{height:1.5625em;width:1.5625em;position:relative;cursor:pointer;border-radius:0.263921875em;-webkit-transition:background 0.15s;transition:background 0.15s}.qs-arrow:hover{background:rgba(0, 0, 0, 0.1)}.qs-arrow:hover.qs-left:after{border-right-color:black}.qs-arrow:hover.qs-right:after{border-left-color:black}.qs-arrow:after{content:\"\";border:0.390625em solid transparent;position:absolute;top:50%;-webkit-transition:border 0.2s;transition:border 0.2s}.qs-arrow.qs-left:after{border-right-color:gray;right:50%;-webkit-transform:translate(25%, -50%);transform:translate(25%, -50%)}.qs-arrow.qs-right:after{border-left-color:gray;left:50%;-webkit-transform:translate(-25%, -50%);transform:translate(-25%, -50%)}.qs-month-year{font-weight:bold;-webkit-transition:border 0.2s;transition:border 0.2s;border-bottom:1px solid transparent;cursor:pointer}.qs-month-year:hover{border-bottom:1px solid gray}.qs-month-year:focus,.qs-month-year:active:focus{outline:none}.qs-month{padding-right:0.5ex}.qs-year{padding-left:0.5ex}.qs-squares{display:-ms-flexbox;display:flex;-ms-flex-wrap:wrap;flex-wrap:wrap;padding:0.3125em;-webkit-filter:blur(0px);filter:blur(0px);-webkit-transition:-webkit-filter 0.3s;transition:-webkit-filter 0.3s;transition:filter 0.3s;transition:filter 0.3s, -webkit-filter 0.3s}.qs-squares.qs-blur{-webkit-filter:blur(5px);filter:blur(5px)}.qs-square{width:calc(100% / 7);height:1.5625em;display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;-ms-flex-pack:center;justify-content:center;cursor:pointer;-webkit-transition:background 0.1s;transition:background 0.1s;border-radius:0.263921875em}.qs-square:not(.qs-empty):not(.qs-disabled):not(.qs-day):not(.qs-active):hover{background:orange}.qs-current{font-weight:bold;text-decoration:underline}.qs-active,.qs-range-start,.qs-range-end{background:lightblue}.qs-range-start:not(.qs-range-6){border-top-right-radius:0;border-bottom-right-radius:0}.qs-range-middle{background:#d4ebf2}.qs-range-middle:not(.qs-range-0):not(.qs-range-6){border-radius:0}.qs-range-middle.qs-range-0{border-top-right-radius:0;border-bottom-right-radius:0}.qs-range-middle.qs-range-6{border-top-left-radius:0;border-bottom-left-radius:0}.qs-range-end:not(.qs-range-0){border-top-left-radius:0;border-bottom-left-radius:0}.qs-disabled,.qs-outside-current-month{opacity:0.2}.qs-disabled{cursor:not-allowed}.qs-empty{cursor:default}.qs-day{cursor:default;font-weight:bold;color:gray}.qs-event{position:relative}.qs-event:after{content:\"\";position:absolute;width:0.46875em;height:0.46875em;border-radius:50%;background:#07f;bottom:0;right:0}:host{display:block}:host label{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;display:-ms-flexbox;display:flex;margin-bottom:var(--spacing-lay-xs)}#date-picker{width:100%;font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;border-width:var(--border-width-sm);border-color:var(--gray-02);border-style:var(--border-style-regular);border-radius:var(--border-radius-sm);padding-left:var(--spacing-comp-01);padding-right:var(--spacing-comp-01);color:var(--color-on-background);background-color:var(--color-background);height:20px;-webkit-box-sizing:border-box;box-sizing:border-box;padding:0 5px;cursor:pointer;z-index:100}#date-picker:focus{outline-style:solid;outline-color:var(--color-primary-active);outline-width:var(--border-width-md);outline-offset:-2px}.qs-datepicker{padding:4px 0 0 0}.qs-datepicker-container{border:none;-webkit-box-shadow:var(--box-shadow-01);box-shadow:var(--box-shadow-01);padding:0;width:181px;background:var(--color-background);margin-top:6px}.qs-datepicker .qs-controls{background-color:transparent;color:var(--color-on-background)}.qs-datepicker .qs-square{border:1px solid var(--gray-01);border-radius:0;width:var(--spacing-comp-05);height:var(--spacing-comp-05);margin:0.5px;color:var(--color-on-background);font-family:var(--font-family-primary)}.qs-month-year{font-size:var(--font-size-xs);font-family:var(--font-family-primary);text-transform:uppercase;font-weight:var(--font-weight-regular)}.qs-datepicker .qs-square.qs-day{color:var(--color-on-background);border:0;height:var(--spacing-comp-04);font-family:var(--font-family-primary);font-weight:var(--font-weight-extra-bold)}.qs-datepicker .qs-square.qs-active,.qs-datepicker .qs-square:not(.qs-empty):not(.qs-disabled):not(.qs-day):not(.qs-active):hover{background-color:var(--color-primary-hover-opacity-01);border-color:var(--color-primary-active)}.qs-datepicker .qs-arrow:after{width:3px;height:3px;border:1px solid black;top:4px}.qs-datepicker .qs-arrow:hover{background-color:transparent}.qs-datepicker .qs-arrow:hover:after{border-color:var(--color-primary-active)}.qs-datepicker .qs-arrow.qs-right:after,.qs-datepicker .qs-arrow.qs-left:after{border-color:var(--color-on-background)}.qs-datepicker .qs-arrow.qs-left:after{border-right:0;border-bottom:0;-webkit-transform:rotate(-45deg) !important;transform:rotate(-45deg) !important;left:13px}.qs-datepicker .qs-arrow.qs-right:after{border-left:0;border-bottom:0;-webkit-transform:rotate(45deg) !important;transform:rotate(45deg) !important;left:-4px}.qs-disabled,.qs-outside-current-month{opacity:0.4}:host(.rtl) .qs-datepicker-container{right:0}:host(.large) #date-picker{font-size:var(--font-size-lg);height:var(--spacing-comp-05)}:host(.large) label,:host(.large) .qs-month-year,:host(.large) .qs-square.qs-day,:host(.large) .qs-square{font-size:var(--font-size-lg)}";
var GxgDatePicker = /** @class */ (function () {
    function GxgDatePicker(hostRef) {
        registerInstance(this, hostRef);
        /**
         * The presence of this attribute makes the date-picker always visible
         */
        this.alwaysShow = false;
        /**
         * no weekends available
         */
        this.noWeekends = false;
        /**
         * The max. width
         */
        this.maxWidth = "100%";
        /**
         * Reading direction
         */
        this.rtl = false;
    }
    GxgDatePicker.prototype.componentDidLoad = function () {
        //Reading Direction
        var dirHtml = document
            .getElementsByTagName("html")[0]
            .getAttribute("dir");
        var dirBody = document
            .getElementsByTagName("body")[0]
            .getAttribute("dir");
        if (dirHtml === "rtl" || dirBody === "rtl") {
            this.rtl = true;
        }
        //Datepicker Options
        var defaultDate = new Date();
        if (this.defaultDate !== undefined && this.defaultDate !== "") {
            //default/initial date
            var d = new Date(this.defaultDate);
            if (Object.prototype.toString.call(d) === "[object Date]") {
                // it is a date
                if (isNaN(d.getTime()))
                    ;
                else {
                    // date is valid
                    defaultDate = new Date(this.defaultDate);
                }
            }
        }
        //default date
        var defaultDateYear = defaultDate.getFullYear();
        var defaultDateMonth = defaultDate.getMonth();
        var defaultDateDay = defaultDate.getDate();
        //min date
        var minDate = new Date(this.minDate);
        var minDateYear = minDate.getFullYear();
        var minDateMonth = minDate.getMonth();
        var minDateDay = minDate.getDate();
        //max date
        var maxDate = new Date(this.maxDate);
        var maxDateYear = maxDate.getFullYear();
        var maxDateMonth = maxDate.getMonth();
        var maxDateDay = maxDate.getDate();
        var pickerSelector = this.el.shadowRoot.querySelector("#date-picker");
        var picker = datepicker(pickerSelector, {
            // Event callbacks.
            // onSelect: instance => {
            // },
            // onShow: instance => {
            // },
            // onHide: instance => {
            // },
            // onMonthChange: instance => {
            // },
            // Customizations.
            formatter: function (input, date) {
                // This will display the date as `1/1/2019`.
                input.value = date.toDateString();
            },
            position: "bl",
            startDay: 1,
            customDays: ["S", "M", "T", "W", "T", "F", "S"],
            customMonths: [
                "January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December",
            ],
            customOverlayMonths: [
                "😀",
                "😂",
                "😎",
                "😍",
                "🤩",
                "😜",
                "😬",
                "😳",
                "🤪",
                "🤓 ",
                "😝",
                "😮",
            ],
            overlayButton: "Go!",
            overlayPlaceholder: "Enter a 4-digit year",
            // Settings.
            alwaysShow: this.alwaysShow,
            dateSelected: new Date(defaultDateYear, defaultDateMonth, defaultDateDay),
            maxDate: new Date(maxDateYear, maxDateMonth, maxDateDay),
            minDate: new Date(minDateYear, minDateMonth, minDateDay),
            startDate: new Date(),
            showAllDates: true,
            // Disabling things.
            noWeekends: this.noWeekends,
            disabler: function (date) { return date.getDay() === 2 && date.getMonth() === 9; },
            disabledDates: [new Date(2050, 0, 1), new Date(2050, 0, 3)],
            disableMobile: true,
            disableYearOverlay: true,
            // ID - be sure to provide a 2nd picker with the same id to create a daterange pair.
            id: "date-picker",
        });
        //picker.setDate(new Date(2099, 0, 1), true);
        picker.calendarContainer.style.setProperty("font-size", "9px");
    };
    GxgDatePicker.prototype.printLabel = function () {
        if (this.label) {
            return h("label", { class: "label" }, this.label);
        }
    };
    GxgDatePicker.prototype.render = function () {
        return (h(Host, { class: {
                rtl: this.rtl,
                large: state.large,
            }, style: {
                maxWidth: this.maxWidth,
            } }, this.printLabel(), h("input", { type: "text", id: "date-picker" })));
    };
    Object.defineProperty(GxgDatePicker.prototype, "el", {
        get: function () { return getElement(this); },
        enumerable: false,
        configurable: true
    });
    return GxgDatePicker;
}());
GxgDatePicker.style = datePickerCss;
export { GxgDatePicker as gxg_date_picker };
