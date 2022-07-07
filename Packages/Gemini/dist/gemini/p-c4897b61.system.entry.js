System.register([ "./p-d8a20c31.system.js", "./p-472960e6.system.js", "./p-08984d7d.system.js" ], (function(e) {
  "use strict";
  var t, r, o, a, i, l, z;
  return {
    setters: [ function(e) {
      t = e.r, r = e.h, o = e.H, a = e.g;
    }, function(e) {
      i = e.c, l = e.u;
    }, function(e) {
      z = e.s;
    } ],
    execute: function() {
      var T = i((function(e, t) {
        window, e.exports = function(e) {
          var t = {};
          function n(r) {
            if (t[r]) return t[r].exports;
            var o = t[r] = {
              i: r,
              l: !1,
              exports: {}
            };
            return e[r].call(o.exports, o, o.exports, n), o.l = !0, o.exports;
          }
          return n.m = e, n.c = t, n.d = function(e, t, r) {
            n.o(e, t) || Object.defineProperty(e, t, {
              enumerable: !0,
              get: r
            });
          }, n.r = function(e) {
            "undefined" != typeof Symbol && Symbol.toStringTag && Object.defineProperty(e, Symbol.toStringTag, {
              value: "Module"
            }), Object.defineProperty(e, "__esModule", {
              value: !0
            });
          }, n.t = function(e, t) {
            if (1 & t && (e = n(e)), 8 & t) return e;
            if (4 & t && "object" == typeof e && e && e.__esModule) return e;
            var r = Object.create(null);
            if (n.r(r), Object.defineProperty(r, "default", {
              enumerable: !0,
              value: e
            }), 2 & t && "string" != typeof e) for (var o in e) n.d(r, o, function(t) {
              return e[t];
            }.bind(null, o));
            return r;
          }, n.n = function(e) {
            var t = e && e.__esModule ? function() {
              return e.default;
            } : function() {
              return e;
            };
            return n.d(t, "a", t), t;
          }, n.o = function(e, t) {
            return Object.prototype.hasOwnProperty.call(e, t);
          }, n.p = "", n(n.s = 0);
        }([ function(e, t, r) {
          r.r(t), r(1);
          var o = [], a = [ "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" ], i = [ "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" ], l = {
            t: "top",
            r: "right",
            b: "bottom",
            l: "left",
            c: "centered"
          };
          function s() {}
          var z = [ "click", "focusin", "keydown", "input" ];
          function c(e) {
            z.forEach((function(t) {
              e.addEventListener(t, e === document ? Y : j);
            }));
          }
          function d(e) {
            return Array.isArray(e) ? e.map(d) : "[object Object]" === x(e) ? Object.keys(e).reduce((function(t, r) {
              return t[r] = d(e[r]), t;
            }), {}) : e;
          }
          function u(e, t) {
            var r = e.calendar.querySelector(".qs-overlay"), o = r && !r.classList.contains("qs-hidden");
            t = t || new Date(e.currentYear, e.currentMonth), e.calendar.innerHTML = [ h(t, e, o), f(t, e, o), v(e, o) ].join(""), 
            o && window.requestAnimationFrame((function() {
              M(!0, e);
            }));
          }
          function h(e, t, r) {
            return [ '<div class="qs-controls' + (r ? " qs-blur" : "") + '">', '<div class="qs-arrow qs-left"></div>', '<div class="qs-month-year">', '<span class="qs-month">' + t.months[e.getMonth()] + "</span>", '<span class="qs-year">' + e.getFullYear() + "</span>", "</div>", '<div class="qs-arrow qs-right"></div>', "</div>" ].join("");
          }
          function f(e, t, r) {
            var o = t.currentMonth, a = t.currentYear, i = t.dateSelected, l = t.maxDate, z = t.minDate, T = t.showAllDates, B = t.days, W = t.disabledDates, J = t.startDay, H = t.events, U = t.getRange ? t.getRange() : {}, G = +U.start, $ = +U.end, K = g(new Date(e).setDate(1)), Q = K.getDay() - J, V = Q < 0 ? 7 : 0;
            K.setMonth(K.getMonth() + 1), K.setDate(0);
            var X = K.getDate(), Z = [], ee = V + 7 * ((Q + X) / 7 | 0);
            ee += (Q + X) % 7 ? 7 : 0;
            for (var te = 1; te <= ee; te++) {
              var re = (te - 1) % 7, oe = B[re], ae = te - (Q >= 0 ? Q : 7 + Q), ne = new Date(a, o, ae), ie = H[+ne], se = ae < 1 || ae > X, le = se ? ae < 1 ? -1 : 1 : 0, ce = se && !T, de = ce ? "" : ne.getDate(), ue = 0 === re || 6 === re, fe = G !== $, ge = "qs-square " + oe;
              ie && !ce && (ge += " qs-event"), se && (ge += " qs-outside-current-month"), !T && se || (ge += " qs-num"), 
              +ne == +i && (ge += " qs-active"), (W[+ne] || t.disabler(ne) || ue && t.noWeekends || z && +ne < +z || l && +ne > +l) && !ce && (ge += " qs-disabled"), 
              +g(new Date) == +ne && (ge += " qs-current"), +ne === G && $ && fe && (ge += " qs-range-start"), 
              +ne > G && +ne < $ && (ge += " qs-range-middle"), +ne === $ && G && fe && (ge += " qs-range-end"), 
              ce && (ge += " qs-empty", de = ""), Z.push('<div class="' + ge + '" data-direction="' + le + '">' + de + "</div>");
            }
            var me = B.map((function(e) {
              return '<div class="qs-square qs-day">' + e + "</div>";
            })).concat(Z);
            return me.unshift('<div class="qs-squares' + (r ? " qs-blur" : "") + '">'), me.push("</div>"), 
            me.join("");
          }
          function v(e, t) {
            var r = e.overlayPlaceholder, o = e.overlayButton;
            return [ '<div class="qs-overlay' + (t ? "" : " qs-hidden") + '">', "<div>", '<input class="qs-overlay-year" placeholder="' + r + '" />', '<div class="qs-close">&#10005;</div>', "</div>", '<div class="qs-overlay-month-container">' + e.overlayMonths.map((function(e, t) {
              return '<div class="qs-overlay-month" data-month-num="' + t + '">' + e + "</div>";
            })).join("") + "</div>", '<div class="qs-submit qs-disabled">' + o + "</div>", "</div>" ].join("");
          }
          function m(e, t, r) {
            var o = t.el, a = t.calendar.querySelector(".qs-active"), i = e.textContent, l = t.sibling;
            (o.disabled || o.readOnly) && t.respectDisabledReadOnly || (t.dateSelected = r ? void 0 : new Date(t.currentYear, t.currentMonth, i), 
            a && a.classList.remove("qs-active"), r || e.classList.add("qs-active"), p(o, t, r), 
            r || q(t), l && (y({
              instance: t,
              deselect: r
            }), t.first && !l.dateSelected && (l.currentYear = t.currentYear, l.currentMonth = t.currentMonth, 
            l.currentMonthName = t.currentMonthName), u(t), u(l)), t.onSelect(t, r ? void 0 : new Date(t.dateSelected)));
          }
          function y(e) {
            var t = e.instance.first ? e.instance : e.instance.sibling, r = t.sibling;
            t === e.instance ? e.deselect ? (t.minDate = t.originalMinDate, r.minDate = r.originalMinDate) : r.minDate = t.dateSelected : e.deselect ? (r.maxDate = r.originalMaxDate, 
            t.maxDate = t.originalMaxDate) : t.maxDate = r.dateSelected;
          }
          function p(e, t, r) {
            if (!t.nonInput) return r ? e.value = "" : t.formatter !== s ? t.formatter(e, t.dateSelected, t) : void (e.value = t.dateSelected.toDateString());
          }
          function D(e, t, r, o) {
            r || o ? (r && (t.currentYear = +r), o && (t.currentMonth = +o)) : (t.currentMonth += e.contains("qs-right") ? 1 : -1, 
            12 === t.currentMonth ? (t.currentMonth = 0, t.currentYear++) : -1 === t.currentMonth && (t.currentMonth = 11, 
            t.currentYear--)), t.currentMonthName = t.months[t.currentMonth], u(t), t.onMonthChange(t);
          }
          function b(e) {
            if (!e.noPosition) {
              var t = e.position.top, r = e.position.right;
              if (e.position.centered) return e.calendarContainer.classList.add("qs-centered");
              var o = e.positionedEl.getBoundingClientRect(), a = e.el.getBoundingClientRect(), i = e.calendarContainer.getBoundingClientRect(), l = a.top - o.top + (t ? -1 * i.height : a.height) + "px", z = a.left - o.left + (r ? a.width - i.width : 0) + "px";
              e.calendarContainer.style.setProperty("top", l), e.calendarContainer.style.setProperty("left", z);
            }
          }
          function w(e) {
            return "[object Date]" === x(e) && "Invalid Date" !== e.toString();
          }
          function g(e) {
            if (w(e) || "number" == typeof e && !isNaN(e)) {
              var t = new Date(+e);
              return new Date(t.getFullYear(), t.getMonth(), t.getDate());
            }
          }
          function q(e) {
            e.disabled || !e.calendarContainer.classList.contains("qs-hidden") && !e.alwaysShow && (M(!0, e), 
            e.calendarContainer.classList.add("qs-hidden"), e.onHide(e));
          }
          function S(e) {
            e.disabled || (e.calendarContainer.classList.remove("qs-hidden"), b(e), e.onShow(e));
          }
          function M(e, t) {
            var r = t.calendar, o = r.querySelector(".qs-overlay"), a = o.querySelector(".qs-overlay-year"), i = r.querySelector(".qs-controls"), l = r.querySelector(".qs-squares");
            e ? (o.classList.add("qs-hidden"), i.classList.remove("qs-blur"), l.classList.remove("qs-blur"), 
            a.value = "") : (o.classList.remove("qs-hidden"), i.classList.add("qs-blur"), l.classList.add("qs-blur"), 
            a.focus());
          }
          function E(e, t, r, o) {
            var a = isNaN(+(new Date).setFullYear(t.value || void 0)), i = a ? null : t.value;
            13 === e.which || 13 === e.keyCode || "click" === e.type ? o ? D(null, r, i, o) : a || t.classList.contains("qs-disabled") || D(null, r, i) : r.calendar.contains(t) && r.calendar.querySelector(".qs-submit").classList[a ? "add" : "remove"]("qs-disabled");
          }
          function x(e) {
            return {}.toString.call(e);
          }
          function C(e) {
            o.forEach((function(t) {
              t !== e && q(t);
            }));
          }
          function Y(e) {
            if (!e.__qs_shadow_dom) {
              var t = e.which || e.keyCode, r = e.type, a = e.target, l = a.classList, z = o.filter((function(e) {
                return e.calendar.contains(a) || e.el === a;
              }))[0], T = z && z.calendar.contains(a);
              if (!(z && z.isMobile && z.disableMobile)) if ("click" === r) {
                if (!z) return o.forEach(q);
                if (z.disabled) return;
                var B = z.calendar, W = z.calendarContainer, J = z.disableYearOverlay, H = z.nonInput, U = B.querySelector(".qs-overlay-year"), G = !!B.querySelector(".qs-hidden"), $ = B.querySelector(".qs-month-year").contains(a), K = a.dataset.monthNum;
                if (z.noPosition && !T) (W.classList.contains("qs-hidden") ? S : q)(z); else if (l.contains("qs-arrow")) D(l, z); else if ($ || l.contains("qs-close")) J || M(!G, z); else if (K) E(e, U, z, K); else {
                  if (l.contains("qs-disabled")) return;
                  if (l.contains("qs-num")) {
                    var Q = a.textContent, V = +a.dataset.direction, X = new Date(z.currentYear, z.currentMonth + V, Q);
                    if (V) {
                      z.currentYear = X.getFullYear(), z.currentMonth = X.getMonth(), z.currentMonthName = i[z.currentMonth], 
                      u(z);
                      for (var Z, ee = z.calendar.querySelectorAll('[data-direction="0"]'), te = 0; !Z; ) {
                        var re = ee[te];
                        re.textContent === Q && (Z = re), te++;
                      }
                      a = Z;
                    }
                    return void (+X == +z.dateSelected ? m(a, z, !0) : a.classList.contains("qs-disabled") || m(a, z));
                  }
                  l.contains("qs-submit") ? E(e, U, z) : H && a === z.el && (S(z), C(z));
                }
              } else if ("focusin" === r && z) S(z), C(z); else if ("keydown" === r && 9 === t && z) q(z); else if ("keydown" === r && z && !z.disabled) {
                var oe = !z.calendar.querySelector(".qs-overlay").classList.contains("qs-hidden");
                13 === t && oe && T ? E(e, a, z) : 27 === t && oe && T && M(!0, z);
              } else if ("input" === r) {
                if (!z || !z.calendar.contains(a)) return;
                var ae = z.calendar.querySelector(".qs-submit"), ne = a.value.split("").reduce((function(e, t) {
                  return e || "0" !== t ? e + (t.match(/[0-9]/) ? t : "") : "";
                }), "").slice(0, 4);
                a.value = ne, ae.classList[4 === ne.length ? "remove" : "add"]("qs-disabled");
              }
            }
          }
          function j(e) {
            Y(e), e.__qs_shadow_dom = !0;
          }
          function L(e, t) {
            z.forEach((function(r) {
              e.removeEventListener(r, t);
            }));
          }
          function P() {
            S(this);
          }
          function k() {
            q(this);
          }
          function O(e, t) {
            var r = g(e), o = this.currentYear, a = this.currentMonth, i = this.sibling;
            if (null == e) return this.dateSelected = void 0, p(this.el, this, !0), i && (y({
              instance: this,
              deselect: !0
            }), u(i)), u(this), this;
            if (!w(e)) throw new Error("`setDate` needs a JavaScript Date object.");
            if (this.disabledDates[+r] || r < this.minDate || r > this.maxDate) throw new Error("You can't manually set a date that's disabled.");
            this.dateSelected = r, t && (this.currentYear = r.getFullYear(), this.currentMonth = r.getMonth(), 
            this.currentMonthName = this.months[r.getMonth()]), p(this.el, this), i && (y({
              instance: this
            }), u(i));
            var l = o === r.getFullYear() && a === r.getMonth();
            return l || t ? u(this, r) : l || u(this, new Date(o, a, 1)), this;
          }
          function N(e) {
            return I(this, e, !0);
          }
          function _(e) {
            return I(this, e);
          }
          function I(e, t, r) {
            var o = e.dateSelected, a = e.first, i = e.sibling, l = e.minDate, z = e.maxDate, T = g(t), B = r ? "Min" : "Max";
            function d() {
              return "original" + B + "Date";
            }
            function h() {
              return B.toLowerCase() + "Date";
            }
            function f() {
              return "set" + B;
            }
            function v() {
              throw new Error("Out-of-range date passed to " + f());
            }
            if (null == t) e[d()] = void 0, i ? (i[d()] = void 0, r ? (a && !o || !a && !i.dateSelected) && (e.minDate = void 0, 
            i.minDate = void 0) : (a && !i.dateSelected || !a && !o) && (e.maxDate = void 0, 
            i.maxDate = void 0)) : e[h()] = void 0; else {
              if (!w(t)) throw new Error("Invalid date passed to " + f());
              i ? ((a && r && T > (o || z) || a && !r && T < (i.dateSelected || l) || !a && r && T > (i.dateSelected || z) || !a && !r && T < (o || l)) && v(), 
              e[d()] = T, i[d()] = T, (r && (a && !o || !a && !i.dateSelected) || !r && (a && !i.dateSelected || !a && !o)) && (e[h()] = T, 
              i[h()] = T)) : ((r && T > (o || z) || !r && T < (o || l)) && v(), e[h()] = T);
            }
            return i && u(i), u(e), e;
          }
          function R() {
            var e = this.first ? this : this.sibling, t = e.sibling;
            return {
              start: e.dateSelected,
              end: t.dateSelected
            };
          }
          function A() {
            var e = this.shadowDom, t = this.positionedEl, r = this.calendarContainer, a = this.sibling, i = this;
            this.inlinePosition && (o.some((function(e) {
              return e !== i && e.positionedEl === t;
            })) || t.style.setProperty("position", null)), r.remove(), o = o.filter((function(e) {
              return e !== i;
            })), a && delete a.sibling, o.length || L(document, Y);
            var l = o.some((function(t) {
              return t.shadowDom === e;
            }));
            for (var T in e && !l && L(e, j), this) delete this[T];
            o.length || z.forEach((function(e) {
              document.removeEventListener(e, Y);
            }));
          }
          function F(e, t) {
            var r = new Date(e);
            if (!w(r)) throw new Error("`navigate` needs a JavaScript Date object.");
            this.currentYear = r.getFullYear(), this.currentMonth = r.getMonth(), u(this), t && this.onMonthChange(this);
          }
          t.default = function(e, t) {
            var r = function(e, t) {
              var r, z, T = function(e) {
                var t = d(e);
                t.events && (t.events = t.events.reduce((function(e, t) {
                  if (!w(t)) throw new Error('"options.events" must only contain valid JavaScript Date objects.');
                  return e[+g(t)] = !0, e;
                }), {})), [ "startDate", "dateSelected", "minDate", "maxDate" ].forEach((function(e) {
                  var r = t[e];
                  if (r && !w(r)) throw new Error('"options.' + e + '" needs to be a valid JavaScript Date object.');
                  t[e] = g(r);
                }));
                var r = t.position, i = t.maxDate, z = t.minDate, T = t.dateSelected, B = t.overlayPlaceholder, W = t.overlayButton, J = t.startDay, H = t.id;
                if (t.startDate = g(t.startDate || T || new Date), t.disabledDates = (t.disabledDates || []).reduce((function(e, t) {
                  var r = +g(t);
                  if (!w(t)) throw new Error('You supplied an invalid date to "options.disabledDates".');
                  if (r === +g(T)) throw new Error('"disabledDates" cannot contain the same date as "dateSelected".');
                  return e[r] = 1, e;
                }), {}), t.hasOwnProperty("id") && null == H) throw new Error("Id cannot be `null` or `undefined`");
                if (null != H) {
                  var U = o.filter((function(e) {
                    return e.id === H;
                  }));
                  if (U.length > 1) throw new Error("Only two datepickers can share an id.");
                  U.length ? (t.second = !0, t.sibling = U[0]) : t.first = !0;
                }
                var G = [ "tr", "tl", "br", "bl", "c" ].some((function(e) {
                  return r === e;
                }));
                if (r && !G) throw new Error('"options.position" must be one of the following: tl, tr, bl, br, or c.');
                function p(e) {
                  throw new Error('"dateSelected" in options is ' + (e ? "less" : "greater") + ' than "' + (e || "max") + 'Date".');
                }
                if (t.position = function(e) {
                  var t = e[0], r = e[1], o = {};
                  return o[l[t]] = 1, r && (o[l[r]] = 1), o;
                }(r || "bl"), i < z) throw new Error('"maxDate" in options is less than "minDate".');
                if (T && (z > T && p("min"), i < T && p()), [ "onSelect", "onShow", "onHide", "onMonthChange", "formatter", "disabler" ].forEach((function(e) {
                  "function" != typeof t[e] && (t[e] = s);
                })), [ "customDays", "customMonths", "customOverlayMonths" ].forEach((function(e, r) {
                  var o = t[e], a = r ? 12 : 7;
                  if (o) {
                    if (!Array.isArray(o) || o.length !== a || o.some((function(e) {
                      return "string" != typeof e;
                    }))) throw new Error('"' + e + '" must be an array with ${num} strings.');
                    t[r ? r < 2 ? "months" : "overlayMonths" : "days"] = o;
                  }
                })), J && J > 0 && J < 7) {
                  var $ = (t.customDays || a).slice(), K = $.splice(0, J);
                  t.customDays = $.concat(K), t.startDay = +J, t.weekendIndices = [ $.length - 1, $.length ];
                } else t.startDay = 0, t.weekendIndices = [ 6, 0 ];
                return "string" != typeof B && delete t.overlayPlaceholder, "string" != typeof W && delete t.overlayButton, 
                t;
              }(t || {
                startDate: g(new Date),
                position: "bl"
              }), B = e;
              if ("string" == typeof B) B = "#" === B[0] ? document.getElementById(B.slice(1)) : document.querySelector(B); else {
                if ("[object ShadowRoot]" === x(B)) throw new Error("Using a shadow DOM as your selector is not supported.");
                for (var W, J = ("getRootNode" in window.Node.prototype), H = B.parentNode; !W; ) {
                  var U = x(H);
                  if ("[object HTMLDocument]" === U) W = !0; else if ("[object ShadowRoot]" === U) {
                    if (W = !0, !J) throw new Error("The shadow DOM is not supported in your browser.");
                    r = H, z = H.host;
                  } else H = H.parentNode;
                }
              }
              if (!B) throw new Error("No selector / element found.");
              if (o.some((function(e) {
                return e.el === B;
              }))) throw new Error("A datepicker already exists on that element.");
              var G = B === document.body, $ = r ? B.parentElement || r : G ? document.body : B.parentElement, K = r ? B.parentElement || z : $, Q = document.createElement("div"), V = document.createElement("div");
              Q.className = "qs-datepicker-container qs-hidden", V.className = "qs-datepicker";
              var X = {
                shadowDom: r,
                customElement: z,
                positionedEl: K,
                el: B,
                parent: $,
                nonInput: "INPUT" !== B.nodeName,
                noPosition: G,
                position: !G && T.position,
                startDate: T.startDate,
                dateSelected: T.dateSelected,
                disabledDates: T.disabledDates,
                minDate: T.minDate,
                maxDate: T.maxDate,
                noWeekends: !!T.noWeekends,
                weekendIndices: T.weekendIndices,
                calendarContainer: Q,
                calendar: V,
                currentMonth: (T.startDate || T.dateSelected).getMonth(),
                currentMonthName: (T.months || i)[(T.startDate || T.dateSelected).getMonth()],
                currentYear: (T.startDate || T.dateSelected).getFullYear(),
                events: T.events || {},
                setDate: O,
                remove: A,
                setMin: N,
                setMax: _,
                show: P,
                hide: k,
                navigate: F,
                onSelect: T.onSelect,
                onShow: T.onShow,
                onHide: T.onHide,
                onMonthChange: T.onMonthChange,
                formatter: T.formatter,
                disabler: T.disabler,
                months: T.months || i,
                days: T.customDays || a,
                startDay: T.startDay,
                overlayMonths: T.overlayMonths || (T.months || i).map((function(e) {
                  return e.slice(0, 3);
                })),
                overlayPlaceholder: T.overlayPlaceholder || "4-digit year",
                overlayButton: T.overlayButton || "Submit",
                disableYearOverlay: !!T.disableYearOverlay,
                disableMobile: !!T.disableMobile,
                isMobile: "ontouchstart" in window,
                alwaysShow: !!T.alwaysShow,
                id: T.id,
                showAllDates: !!T.showAllDates,
                respectDisabledReadOnly: !!T.respectDisabledReadOnly,
                first: T.first,
                second: T.second
              };
              if (T.sibling) {
                var Z = T.sibling, ee = X, te = Z.minDate || ee.minDate, re = Z.maxDate || ee.maxDate;
                ee.sibling = Z, Z.sibling = ee, Z.minDate = te, Z.maxDate = re, ee.minDate = te, 
                ee.maxDate = re, Z.originalMinDate = te, Z.originalMaxDate = re, ee.originalMinDate = te, 
                ee.originalMaxDate = re, Z.getRange = R, ee.getRange = R;
              }
              T.dateSelected && p(B, X);
              var oe = getComputedStyle(K).position;
              G || oe && "static" !== oe || (X.inlinePosition = !0, K.style.setProperty("position", "relative"));
              var ae = o.filter((function(e) {
                return e.positionedEl === X.positionedEl;
              }));
              return ae.some((function(e) {
                return e.inlinePosition;
              })) && (X.inlinePosition = !0, ae.forEach((function(e) {
                e.inlinePosition = !0;
              }))), Q.appendChild(V), $.appendChild(Q), X.alwaysShow && S(X), X;
            }(e, t);
            if (o.length || c(document), r.shadowDom && (o.some((function(e) {
              return e.shadowDom === r.shadowDom;
            })) || c(r.shadowDom)), o.push(r), r.second) {
              var z = r.sibling;
              y({
                instance: r,
                deselect: !r.dateSelected
              }), y({
                instance: z,
                deselect: !z.dateSelected
              }), u(z);
            }
            return u(r, r.startDate || r.dateSelected), r.alwaysShow && b(r), r;
          };
        }, function(e, t, r) {} ]).default;
      })), B = l(T);
      e("gxg_date_picker", /** @class */ function() {
        function class_1(e) {
          t(this, e), 
          /**
                     * The presence of this attribute makes the date-picker always visible
                     */
          this.alwaysShow = !1, 
          /**
                     * no weekends available
                     */
          this.noWeekends = !1, 
          /**
                     * The max. width
                     */
          this.maxWidth = "100%", 
          /**
                     * Reading direction
                     */
          this.rtl = !1;
        }
        return class_1.prototype.componentDidLoad = function() {
          //Reading Direction
          var e = document.getElementsByTagName("html")[0].getAttribute("dir"), t = document.getElementsByTagName("body")[0].getAttribute("dir");
          "rtl" !== e && "rtl" !== t || (this.rtl = !0);
          //Datepicker Options
                    var r = new Date;
          if (void 0 !== this.defaultDate && "" !== this.defaultDate) {
            //default/initial date
            var o = new Date(this.defaultDate);
            "[object Date]" === Object.prototype.toString.call(o) && (
            // it is a date
            isNaN(o.getTime()) || (
            // date is valid
            r = new Date(this.defaultDate)));
          }
          //default date
                    var a = r.getFullYear(), i = r.getMonth(), l = r.getDate(), z = new Date(this.minDate), T = z.getFullYear(), W = z.getMonth(), J = z.getDate(), H = new Date(this.maxDate), U = H.getFullYear(), G = H.getMonth(), $ = H.getDate(), K = this.el.shadowRoot.querySelector("#date-picker");
          //picker.setDate(new Date(2099, 0, 1), true);
          B(K, {
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
            formatter: function(e, t) {
              // This will display the date as `1/1/2019`.
              e.value = t.toDateString();
            },
            position: "bl",
            startDay: 1,
            customDays: [ "S", "M", "T", "W", "T", "F", "S" ],
            customMonths: [ "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" ],
            customOverlayMonths: [ "😀", "😂", "😎", "😍", "🤩", "😜", "😬", "😳", "🤪", "🤓 ", "😝", "😮" ],
            overlayButton: "Go!",
            overlayPlaceholder: "Enter a 4-digit year",
            // Settings.
            alwaysShow: this.alwaysShow,
            dateSelected: new Date(a, i, l),
            maxDate: new Date(U, G, $),
            minDate: new Date(T, W, J),
            startDate: new Date,
            showAllDates: !0,
            // Disabling things.
            noWeekends: this.noWeekends,
            disabler: function(e) {
              return 2 === e.getDay() && 9 === e.getMonth();
            },
            disabledDates: [ new Date(2050, 0, 1), new Date(2050, 0, 3) ],
            disableMobile: !0,
            disableYearOverlay: !0,
            // ID - be sure to provide a 2nd picker with the same id to create a daterange pair.
            id: "date-picker"
          }).calendarContainer.style.setProperty("font-size", "9px");
        }, class_1.prototype.printLabel = function() {
          if (this.label) return r("label", {
            class: "label"
          }, this.label);
        }, class_1.prototype.render = function() {
          return r(o, {
            class: {
              rtl: this.rtl,
              large: z.large
            },
            style: {
              maxWidth: this.maxWidth
            }
          }, this.printLabel(), r("input", {
            type: "text",
            id: "date-picker"
          }));
        }, Object.defineProperty(class_1.prototype, "el", {
          get: function() {
            return a(this);
          },
          enumerable: !1,
          configurable: !0
        }), class_1;
      }()).style = '/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.qs-datepicker-container{font-size:1rem;font-family:sans-serif;color:black;position:absolute;width:15.625em;display:-ms-flexbox;display:flex;-ms-flex-direction:column;flex-direction:column;z-index:9001;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;border:1px solid gray;border-radius:0.263921875em;overflow:hidden;background:white;-webkit-box-shadow:0 1.25em 1.25em -0.9375em rgba(0, 0, 0, 0.3);box-shadow:0 1.25em 1.25em -0.9375em rgba(0, 0, 0, 0.3)}.qs-datepicker-container *{-webkit-box-sizing:border-box;box-sizing:border-box}.qs-centered{position:fixed;top:50%;left:50%;-webkit-transform:translate(-50%, -50%);transform:translate(-50%, -50%)}.qs-hidden{display:none}.qs-overlay{position:absolute;top:0;left:0;background:rgba(0, 0, 0, 0.75);color:white;width:100%;height:100%;padding:0.5em;z-index:1;opacity:1;-webkit-transition:opacity 0.3s;transition:opacity 0.3s;display:-ms-flexbox;display:flex;-ms-flex-direction:column;flex-direction:column}.qs-overlay.qs-hidden{opacity:0;z-index:-1}.qs-overlay .qs-overlay-year{border:none;background:transparent;border-bottom:1px solid white;border-radius:0;color:white;font-size:0.875em;padding:0.25em 0;width:80%;text-align:center;margin:0 auto;display:block}.qs-overlay .qs-overlay-year::-webkit-inner-spin-button{-webkit-appearance:none}.qs-overlay .qs-close{padding:0.5em;cursor:pointer;position:absolute;top:0;right:0}.qs-overlay .qs-submit{border:1px solid white;border-radius:0.263921875em;padding:0.5em;margin:0 auto auto;cursor:pointer;background:rgba(128, 128, 128, 0.4)}.qs-overlay .qs-submit.qs-disabled{color:gray;border-color:gray;cursor:not-allowed}.qs-overlay .qs-overlay-month-container{display:-ms-flexbox;display:flex;-ms-flex-wrap:wrap;flex-wrap:wrap;-ms-flex-positive:1;flex-grow:1}.qs-overlay .qs-overlay-month{display:-ms-flexbox;display:flex;-ms-flex-pack:center;justify-content:center;-ms-flex-align:center;align-items:center;width:calc(100% / 3);cursor:pointer;opacity:0.5;-webkit-transition:opacity 0.15s;transition:opacity 0.15s}.qs-overlay .qs-overlay-month.active,.qs-overlay .qs-overlay-month:hover{opacity:1}.qs-controls{width:100%;display:-ms-flexbox;display:flex;-ms-flex-pack:justify;justify-content:space-between;-ms-flex-align:center;align-items:center;-ms-flex-positive:1;flex-grow:1;-ms-flex-negative:0;flex-shrink:0;background:lightgray;-webkit-filter:blur(0px);filter:blur(0px);-webkit-transition:-webkit-filter 0.3s;transition:-webkit-filter 0.3s;transition:filter 0.3s;transition:filter 0.3s, -webkit-filter 0.3s}.qs-controls.qs-blur{-webkit-filter:blur(5px);filter:blur(5px)}.qs-arrow{height:1.5625em;width:1.5625em;position:relative;cursor:pointer;border-radius:0.263921875em;-webkit-transition:background 0.15s;transition:background 0.15s}.qs-arrow:hover{background:rgba(0, 0, 0, 0.1)}.qs-arrow:hover.qs-left:after{border-right-color:black}.qs-arrow:hover.qs-right:after{border-left-color:black}.qs-arrow:after{content:"";border:0.390625em solid transparent;position:absolute;top:50%;-webkit-transition:border 0.2s;transition:border 0.2s}.qs-arrow.qs-left:after{border-right-color:gray;right:50%;-webkit-transform:translate(25%, -50%);transform:translate(25%, -50%)}.qs-arrow.qs-right:after{border-left-color:gray;left:50%;-webkit-transform:translate(-25%, -50%);transform:translate(-25%, -50%)}.qs-month-year{font-weight:bold;-webkit-transition:border 0.2s;transition:border 0.2s;border-bottom:1px solid transparent;cursor:pointer}.qs-month-year:hover{border-bottom:1px solid gray}.qs-month-year:focus,.qs-month-year:active:focus{outline:none}.qs-month{padding-right:0.5ex}.qs-year{padding-left:0.5ex}.qs-squares{display:-ms-flexbox;display:flex;-ms-flex-wrap:wrap;flex-wrap:wrap;padding:0.3125em;-webkit-filter:blur(0px);filter:blur(0px);-webkit-transition:-webkit-filter 0.3s;transition:-webkit-filter 0.3s;transition:filter 0.3s;transition:filter 0.3s, -webkit-filter 0.3s}.qs-squares.qs-blur{-webkit-filter:blur(5px);filter:blur(5px)}.qs-square{width:calc(100% / 7);height:1.5625em;display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;-ms-flex-pack:center;justify-content:center;cursor:pointer;-webkit-transition:background 0.1s;transition:background 0.1s;border-radius:0.263921875em}.qs-square:not(.qs-empty):not(.qs-disabled):not(.qs-day):not(.qs-active):hover{background:orange}.qs-current{font-weight:bold;text-decoration:underline}.qs-active,.qs-range-start,.qs-range-end{background:lightblue}.qs-range-start:not(.qs-range-6){border-top-right-radius:0;border-bottom-right-radius:0}.qs-range-middle{background:#d4ebf2}.qs-range-middle:not(.qs-range-0):not(.qs-range-6){border-radius:0}.qs-range-middle.qs-range-0{border-top-right-radius:0;border-bottom-right-radius:0}.qs-range-middle.qs-range-6{border-top-left-radius:0;border-bottom-left-radius:0}.qs-range-end:not(.qs-range-0){border-top-left-radius:0;border-bottom-left-radius:0}.qs-disabled,.qs-outside-current-month{opacity:0.2}.qs-disabled{cursor:not-allowed}.qs-empty{cursor:default}.qs-day{cursor:default;font-weight:bold;color:gray}.qs-event{position:relative}.qs-event:after{content:"";position:absolute;width:0.46875em;height:0.46875em;border-radius:50%;background:#07f;bottom:0;right:0}:host{display:block}:host label{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;display:-ms-flexbox;display:flex;margin-bottom:var(--spacing-lay-xs)}#date-picker{width:100%;font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;border-width:var(--border-width-sm);border-color:var(--gray-02);border-style:var(--border-style-regular);border-radius:var(--border-radius-sm);padding-left:var(--spacing-comp-01);padding-right:var(--spacing-comp-01);color:var(--color-on-background);background-color:var(--color-background);height:20px;-webkit-box-sizing:border-box;box-sizing:border-box;padding:0 5px;cursor:pointer;z-index:100}#date-picker:focus{outline-style:solid;outline-color:var(--color-primary-active);outline-width:var(--border-width-md);outline-offset:-2px}.qs-datepicker{padding:4px 0 0 0}.qs-datepicker-container{border:none;-webkit-box-shadow:var(--box-shadow-01);box-shadow:var(--box-shadow-01);padding:0;width:181px;background:var(--color-background);margin-top:6px}.qs-datepicker .qs-controls{background-color:transparent;color:var(--color-on-background)}.qs-datepicker .qs-square{border:1px solid var(--gray-01);border-radius:0;width:var(--spacing-comp-05);height:var(--spacing-comp-05);margin:0.5px;color:var(--color-on-background);font-family:var(--font-family-primary)}.qs-month-year{font-size:var(--font-size-xs);font-family:var(--font-family-primary);text-transform:uppercase;font-weight:var(--font-weight-regular)}.qs-datepicker .qs-square.qs-day{color:var(--color-on-background);border:0;height:var(--spacing-comp-04);font-family:var(--font-family-primary);font-weight:var(--font-weight-extra-bold)}.qs-datepicker .qs-square.qs-active,.qs-datepicker .qs-square:not(.qs-empty):not(.qs-disabled):not(.qs-day):not(.qs-active):hover{background-color:var(--color-primary-hover-opacity-01);border-color:var(--color-primary-active)}.qs-datepicker .qs-arrow:after{width:3px;height:3px;border:1px solid black;top:4px}.qs-datepicker .qs-arrow:hover{background-color:transparent}.qs-datepicker .qs-arrow:hover:after{border-color:var(--color-primary-active)}.qs-datepicker .qs-arrow.qs-right:after,.qs-datepicker .qs-arrow.qs-left:after{border-color:var(--color-on-background)}.qs-datepicker .qs-arrow.qs-left:after{border-right:0;border-bottom:0;-webkit-transform:rotate(-45deg) !important;transform:rotate(-45deg) !important;left:13px}.qs-datepicker .qs-arrow.qs-right:after{border-left:0;border-bottom:0;-webkit-transform:rotate(45deg) !important;transform:rotate(45deg) !important;left:-4px}.qs-disabled,.qs-outside-current-month{opacity:0.4}:host(.rtl) .qs-datepicker-container{right:0}:host(.large) #date-picker{font-size:var(--font-size-lg);height:var(--spacing-comp-05)}:host(.large) label,:host(.large) .qs-month-year,:host(.large) .qs-square.qs-day,:host(.large) .qs-square{font-size:var(--font-size-lg)}';
    }
  };
}));