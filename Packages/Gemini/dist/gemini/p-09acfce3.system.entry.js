var __assign = this && this.__assign || function() {
  return (__assign = Object.assign || function(t) {
    for (var n, s = 1, d = arguments.length; s < d; s++) for (var h in n = arguments[s]) Object.prototype.hasOwnProperty.call(n, h) && (t[h] = n[h]);
    return t;
  }).apply(this, arguments);
}, __spreadArrays = this && this.__spreadArrays || function() {
  for (var t = 0, n = 0, s = arguments.length; n < s; n++) t += arguments[n].length;
  var d = Array(t), h = 0;
  for (n = 0; n < s; n++) for (var f = arguments[n], g = 0, v = f.length; g < v; g++, 
  h++) d[h] = f[g];
  return d;
};

System.register([ "./p-d8a20c31.system.js", "./p-472960e6.system.js" ], (function(t) {
  "use strict";
  var n, s, d, h, f, g, v;
  return {
    setters: [ function(t) {
      n = t.r, s = t.c, d = t.h, h = t.H, f = t.g;
    }, function(t) {
      g = t.c, v = t.u;
    } ],
    execute: function() {
      var m = g((function(t, n) {
        /*! Pickr 1.7.4 MIT | https://github.com/Simonwep/pickr */
        window, t.exports = function(t) {
          var n = {};
          function o(s) {
            if (n[s]) return n[s].exports;
            var d = n[s] = {
              i: s,
              l: !1,
              exports: {}
            };
            return t[s].call(d.exports, d, d.exports, o), d.l = !0, d.exports;
          }
          return o.m = t, o.c = n, o.d = function(t, n, s) {
            o.o(t, n) || Object.defineProperty(t, n, {
              enumerable: !0,
              get: s
            });
          }, o.r = function(t) {
            "undefined" != typeof Symbol && Symbol.toStringTag && Object.defineProperty(t, Symbol.toStringTag, {
              value: "Module"
            }), Object.defineProperty(t, "__esModule", {
              value: !0
            });
          }, o.t = function(t, n) {
            if (1 & n && (t = o(t)), 8 & n) return t;
            if (4 & n && "object" == typeof t && t && t.__esModule) return t;
            var s = Object.create(null);
            if (o.r(s), Object.defineProperty(s, "default", {
              enumerable: !0,
              value: t
            }), 2 & n && "string" != typeof t) for (var d in t) o.d(s, d, function(n) {
              return t[n];
            }.bind(null, d));
            return s;
          }, o.n = function(t) {
            var n = t && t.__esModule ? function() {
              return t.default;
            } : function() {
              return t;
            };
            return o.d(n, "a", n), n;
          }, o.o = function(t, n) {
            return Object.prototype.hasOwnProperty.call(t, n);
          }, o.p = "", o(o.s = 1);
        }([ function(t) {
          t.exports = JSON.parse('{"a":"1.7.4"}');
        }, function(t, n, s) {
          s.r(n);
          var d = {};
          function i(t, n, s, d, h) {
            void 0 === h && (h = {}), n instanceof HTMLCollection || n instanceof NodeList ? n = Array.from(n) : Array.isArray(n) || (n = [ n ]), 
            Array.isArray(s) || (s = [ s ]);
            for (var f = 0, g = n; f < g.length; f++) for (var v = g[f], m = 0, x = s; m < x.length; m++) {
              var C = x[m];
              v[t](C, d, __assign({
                capture: !1
              }, h));
            }
            return Array.prototype.slice.call(arguments, 1);
          }
          s.r(d), s.d(d, "on", (function() {
            return h;
          })), s.d(d, "off", (function() {
            return f;
          })), s.d(d, "createElementFromString", (function() {
            return a;
          })), s.d(d, "createFromTemplate", (function() {
            return c;
          })), s.d(d, "eventPath", (function() {
            return l;
          })), s.d(d, "resolveElement", (function() {
            return p;
          })), s.d(d, "adjustableInputNumbers", (function() {
            return u;
          }));
          var h = i.bind(null, "addEventListener"), f = i.bind(null, "removeEventListener");
          function a(t) {
            var n = document.createElement("div");
            return n.innerHTML = t.trim(), n.firstElementChild;
          }
          function c(t) {
            var e = function(t, n) {
              var s = t.getAttribute(n);
              return t.removeAttribute(n), s;
            }, o = function(t, n) {
              void 0 === n && (n = {});
              var s = e(t, ":obj"), d = e(t, ":ref"), h = s ? n[s] = {} : n;
              d && (n[d] = t);
              for (var f = 0, g = Array.from(t.children); f < g.length; f++) {
                var v = g[f], m = e(v, ":arr"), x = o(v, m ? {} : h);
                m && (h[m] || (h[m] = [])).push(Object.keys(x).length ? x : v);
              }
              return n;
            };
            return o(a(t));
          }
          function l(t) {
            var n = t.path || t.composedPath && t.composedPath();
            if (n) return n;
            var s = t.target.parentElement;
            for (n = [ t.target, s ]; s = s.parentElement; ) n.push(s);
            return n.push(document, window), n;
          }
          function p(t) {
            return t instanceof Element ? t : "string" == typeof t ? t.split(/>>/g).reduce((function(t, n, s, d) {
              return t = t.querySelector(n), s < d.length - 1 ? t.shadowRoot : t;
            }), document) : null;
          }
          function u(t, n) {
            function o(s) {
              var d = [ .001, .01, .1 ][Number(s.shiftKey || 2 * s.ctrlKey)] * (s.deltaY < 0 ? 1 : -1), h = 0, f = t.selectionStart;
              t.value = t.value.replace(/[\d.]+/g, (function(t, s) {
                return s <= f && s + t.length >= f ? (f = s, n(Number(t), d, h)) : (h++, t);
              })), t.focus(), t.setSelectionRange(f, f), s.preventDefault(), t.dispatchEvent(new Event("input"));
            }
            void 0 === n && (n = function(t) {
              return t;
            }), h(t, "focus", (function() {
              return h(window, "wheel", o, {
                passive: !1
              });
            })), h(t, "blur", (function() {
              return f(window, "wheel", o);
            }));
          }
          var g = s(0), v = Math.min, m = Math.max, x = Math.floor, C = Math.round;
          function b(t, n, s) {
            n /= 100, s /= 100;
            var d = x(t = t / 360 * 6), h = t - d, f = s * (1 - n), g = s * (1 - h * n), v = s * (1 - (1 - h) * n), m = d % 6;
            return [ 255 * [ s, g, f, f, v, s ][m], 255 * [ v, s, s, g, f, f ][m], 255 * [ f, f, v, s, s, g ][m] ];
          }
          function y(t, n, s) {
            var d, h, f = v(t /= 255, n /= 255, s /= 255), g = m(t, n, s), x = g - f;
            if (0 === x) d = h = 0; else {
              h = x / g;
              var C = ((g - t) / 6 + x / 2) / x, H = ((g - n) / 6 + x / 2) / x, z = ((g - s) / 6 + x / 2) / x;
              t === g ? d = z - H : n === g ? d = 1 / 3 + C - z : s === g && (d = 2 / 3 + H - C), 
              d < 0 ? d += 1 : d > 1 && (d -= 1);
            }
            return [ 360 * d, 100 * h, 100 * g ];
          }
          function _(t, n, s, d) {
            return n /= 100, s /= 100, __spreadArrays(y(255 * (1 - v(1, (t /= 100) * (1 - (d /= 100)) + d)), 255 * (1 - v(1, n * (1 - d) + d)), 255 * (1 - v(1, s * (1 - d) + d))));
          }
          function w(t, n, s) {
            n /= 100;
            var d = 2 * (n *= (s /= 100) < .5 ? s : 1 - s) / (s + n) * 100, h = 100 * (s + n);
            return [ t, isNaN(d) ? 0 : d, h ];
          }
          function A(t) {
            return y.apply(void 0, t.match(/.{2}/g).map((function(t) {
              return parseInt(t, 16);
            })));
          }
          function k(t, n, s, d) {
            void 0 === t && (t = 0), void 0 === n && (n = 0), void 0 === s && (s = 0), void 0 === d && (d = 1);
            var i = function(t, n) {
              return function(s) {
                return void 0 === s && (s = -1), n(~s ? t.map((function(t) {
                  return Number(t.toFixed(s));
                })) : t);
              };
            }, h = {
              h: t,
              s: n,
              v: s,
              a: d,
              toHSVA: function() {
                var t = [ h.h, h.s, h.v, h.a ];
                return t.toString = i(t, (function(t) {
                  return "hsva(".concat(t[0], ", ").concat(t[1], "%, ").concat(t[2], "%, ").concat(h.a, ")");
                })), t;
              },
              toHSLA: function() {
                var t = __spreadArrays(function(t, n, s) {
                  var d = (2 - (n /= 100)) * (s /= 100) / 2;
                  return 0 !== d && (n = 1 === d ? 0 : d < .5 ? n * s / (2 * d) : n * s / (2 - 2 * d)), 
                  [ t, 100 * n, 100 * d ];
                }(h.h, h.s, h.v), [ h.a ]);
                return t.toString = i(t, (function(t) {
                  return "hsla(".concat(t[0], ", ").concat(t[1], "%, ").concat(t[2], "%, ").concat(h.a, ")");
                })), t;
              },
              toRGBA: function() {
                var t = __spreadArrays(b(h.h, h.s, h.v), [ h.a ]);
                return t.toString = i(t, (function(t) {
                  return "rgba(".concat(t[0], ", ").concat(t[1], ", ").concat(t[2], ", ").concat(h.a, ")");
                })), t;
              },
              toCMYK: function() {
                var t = function(t, n, s) {
                  var d = b(t, n, s), h = d[0] / 255, f = d[1] / 255, g = d[2] / 255, m = v(1 - h, 1 - f, 1 - g);
                  return [ 100 * (1 === m ? 0 : (1 - h - m) / (1 - m)), 100 * (1 === m ? 0 : (1 - f - m) / (1 - m)), 100 * (1 === m ? 0 : (1 - g - m) / (1 - m)), 100 * m ];
                }(h.h, h.s, h.v);
                return t.toString = i(t, (function(t) {
                  return "cmyk(".concat(t[0], "%, ").concat(t[1], "%, ").concat(t[2], "%, ").concat(t[3], "%)");
                })), t;
              },
              toHEXA: function() {
                var t = function(t, n, s) {
                  return b(t, n, s).map((function(t) {
                    return C(t).toString(16).padStart(2, "0");
                  }));
                }(h.h, h.s, h.v), n = h.a >= 1 ? "" : Number((255 * h.a).toFixed(0)).toString(16).toUpperCase().padStart(2, "0");
                return n && t.push(n), t.toString = function() {
                  return "#".concat(t.join("").toUpperCase());
                }, t;
              },
              clone: function() {
                return k(h.h, h.s, h.v, h.a);
              }
            };
            return h;
          }
          var S = function(t) {
            return Math.max(Math.min(t, 1), 0);
          };
          function O(t) {
            var n = {
              options: Object.assign({
                lock: null,
                onchange: function() {
                  return 0;
                },
                onstop: function() {
                  return 0;
                }
              }, t),
              _keyboard: function(t) {
                var s = n.options, d = t.type, h = t.key;
                if (document.activeElement === s.wrapper) {
                  var f = n.options.lock, g = "ArrowUp" === h, v = "ArrowRight" === h, m = "ArrowDown" === h, x = "ArrowLeft" === h;
                  if ("keydown" === d && (g || v || m || x)) {
                    var C = 0, H = 0;
                    "v" === f ? C = g || v ? 1 : -1 : "h" === f ? C = g || v ? -1 : 1 : (H = g ? -1 : m ? 1 : 0, 
                    C = x ? -1 : v ? 1 : 0), n.update(S(n.cache.x + .01 * C), S(n.cache.y + .01 * H)), 
                    t.preventDefault();
                  } else h.startsWith("Arrow") && (n.options.onstop(), t.preventDefault());
                }
              },
              _tapstart: function(t) {
                h(document, [ "mouseup", "touchend", "touchcancel" ], n._tapstop), h(document, [ "mousemove", "touchmove" ], n._tapmove), 
                t.cancelable && t.preventDefault(), n._tapmove(t);
              },
              _tapmove: function(t) {
                var s = n.options, d = n.cache, h = s.lock, f = s.element, g = s.wrapper.getBoundingClientRect(), v = 0, m = 0;
                if (t) {
                  var x = t && t.touches && t.touches[0];
                  v = t ? (x || t).clientX : 0, m = t ? (x || t).clientY : 0, v < g.left ? v = g.left : v > g.left + g.width && (v = g.left + g.width), 
                  m < g.top ? m = g.top : m > g.top + g.height && (m = g.top + g.height), v -= g.left, 
                  m -= g.top;
                } else d && (v = d.x * g.width, m = d.y * g.height);
                "h" !== h && (f.style.left = "calc(".concat(v / g.width * 100, "% - ").concat(f.offsetWidth / 2, "px)")), 
                "v" !== h && (f.style.top = "calc(".concat(m / g.height * 100, "% - ").concat(f.offsetHeight / 2, "px)")), 
                n.cache = {
                  x: v / g.width,
                  y: m / g.height
                };
                var C = S(v / g.width), H = S(m / g.height);
                switch (h) {
                 case "v":
                  return s.onchange(C);

                 case "h":
                  return s.onchange(H);

                 default:
                  return s.onchange(C, H);
                }
              },
              _tapstop: function() {
                n.options.onstop(), f(document, [ "mouseup", "touchend", "touchcancel" ], n._tapstop), 
                f(document, [ "mousemove", "touchmove" ], n._tapmove);
              },
              trigger: function() {
                n._tapmove();
              },
              update: function(t, s) {
                void 0 === t && (t = 0), void 0 === s && (s = 0);
                var d = n.options.wrapper.getBoundingClientRect(), h = d.left, f = d.top, g = d.width, v = d.height;
                "h" === n.options.lock && (s = t), n._tapmove({
                  clientX: h + g * t,
                  clientY: f + v * s
                });
              },
              destroy: function() {
                var t = n.options, s = n._tapstart, d = n._keyboard;
                f(document, [ "keydown", "keyup" ], d), f([ t.wrapper, t.element ], "mousedown", s), 
                f([ t.wrapper, t.element ], "touchstart", s, {
                  passive: !1
                });
              }
            }, s = n.options, d = n._tapstart, g = n._keyboard;
            return h([ s.wrapper, s.element ], "mousedown", d), h([ s.wrapper, s.element ], "touchstart", d, {
              passive: !1
            }), h(document, [ "keydown", "keyup" ], g), n;
          }
          function E(t) {
            void 0 === t && (t = {}), t = Object.assign({
              onchange: function() {
                return 0;
              },
              className: "",
              elements: []
            }, t);
            var n = h(t.elements, "click", (function(n) {
              t.elements.forEach((function(s) {
                return s.classList[n.target === s ? "add" : "remove"](t.className);
              })), t.onchange(n), n.stopPropagation();
            }));
            return {
              destroy: function() {
                return f.apply(void 0, n);
              }
            };
          }
          /*! NanoPop 2.1.0 MIT | https://github.com/Simonwep/nanopop */          var H = {
            variantFlipOrder: {
              start: "sme",
              middle: "mse",
              end: "ems"
            },
            positionFlipOrder: {
              top: "tbrl",
              right: "rltb",
              bottom: "btrl",
              left: "lrbt"
            },
            position: "bottom",
            margin: 8
          };
          function j(t, n, s) {
            return n in t ? Object.defineProperty(t, n, {
              value: s,
              enumerable: !0,
              configurable: !0,
              writable: !0
            }) : t[n] = s, t;
          }
          var z = /** @class */ function() {
            function B(t) {
              var n = this;
              j(this, "_initializingActive", !0), j(this, "_recalc", !0), j(this, "_nanopop", null), 
              j(this, "_root", null), j(this, "_color", k()), j(this, "_lastColor", k()), j(this, "_swatchColors", []), 
              j(this, "_eventListener", {
                init: [],
                save: [],
                hide: [],
                show: [],
                clear: [],
                change: [],
                changestop: [],
                cancel: [],
                swatchselect: []
              }), this.options = t = Object.assign(__assign({}, B.DEFAULT_OPTIONS), t);
              var s = t.swatches, d = t.components, h = t.theme, f = t.sliders, g = t.lockOpacity, v = t.padding;
              [ "nano", "monolith" ].includes(h) && !f && (t.sliders = "h"), d.interaction || (d.interaction = {});
              var m = d.preview, x = d.opacity, C = d.hue, z = d.palette;
              d.opacity = !g && x, d.palette = z || m || x || C, this._preBuild(), this._buildComponents(), 
              this._bindEvents(), this._finalBuild(), s && s.length && s.forEach((function(t) {
                return n.addSwatch(t);
              }));
              var V = this._root, R = V.button, M = V.app;
              this._nanopop = function(t, n, s) {
                var d = "object" != typeof t || t instanceof HTMLElement ? __assign({
                  reference: t,
                  popper: n
                }, s) : t;
                return {
                  update: function(t) {
                    void 0 === t && (t = d);
                    var n = Object.assign(d, t), s = n.reference, h = n.popper;
                    if (!h || !s) throw new Error("Popper- or reference-element missing.");
                    return function(t, n, s) {
                      var d = __assign(__assign({
                        container: document.documentElement.getBoundingClientRect()
                      }, H), s), h = d.container, f = d.margin, g = d.position, v = d.variantFlipOrder, m = d.positionFlipOrder, x = n.style, C = x.left, z = x.top;
                      n.style.left = "0", n.style.top = "0";
                      for (var V = t.getBoundingClientRect(), R = n.getBoundingClientRect(), M = {
                        t: V.top - R.height - f,
                        b: V.bottom + f,
                        r: V.right + f,
                        l: V.left - R.width - f
                      }, L = {
                        vs: V.left,
                        vm: V.left + V.width / 2 + -R.width / 2,
                        ve: V.left + V.width - R.width,
                        hs: V.top,
                        hm: V.bottom - V.height / 2 - R.height / 2,
                        he: V.bottom - R.height
                      }, D = g.split("-"), P = D[0], F = D[1], Z = void 0 === F ? "middle" : F, I = m[P], N = v[Z], T = h.top, U = h.left, X = h.bottom, G = h.right, q = 0, Y = I; q < Y.length; q++) {
                        var K = Y[q], W = "t" === K || "b" === K, $ = M[K], J = W ? [ "top", "left" ] : [ "left", "top" ], Q = J[0], tt = J[1], et = W ? [ R.height, R.width ] : [ R.width, R.height ], rt = et[1], ot = W ? [ X, G ] : [ G, X ], nt = ot[1], it = W ? [ T, U ] : [ U, T ], at = it[1];
                        if (!($ < it[0] || $ + et[0] > ot[0])) for (var ct = 0, st = N; ct < st.length; ct++) {
                          var lt = st[ct], pt = L[(W ? "v" : "h") + lt];
                          if (!(pt < at || pt + rt > nt)) return n.style[tt] = pt - R[tt] + "px", n.style[Q] = $ - R[Q] + "px", 
                          K + lt;
                        }
                      }
                      return n.style.left = C, n.style.top = z, null;
                    }(s, h, d);
                  }
                };
              }(R, M, {
                margin: v
              }), R.setAttribute("role", "button"), R.setAttribute("aria-label", this._t("btn:toggle"));
              var L = this;
              requestAnimationFrame((function e() {
                if (!M.offsetWidth) return requestAnimationFrame(e);
                L.setColor(t.default), L._rePositioningPicker(), t.defaultRepresentation && (L._representation = t.defaultRepresentation, 
                L.setColorRepresentation(L._representation)), t.showAlways && L.show(), L._initializingActive = !1, 
                L._emit("init");
              }));
            }
            return B.prototype._preBuild = function() {
              for (var t = this.options, n = 0, s = [ "el", "container" ]; n < s.length; n++) {
                var d = s[n];
                t[d] = p(t[d]);
              }
              this._root = function(t) {
                var n = t.options, s = n.components, d = n.useAsButton, h = n.inline, f = n.appClass, g = n.theme, v = n.lockOpacity, a = function(t) {
                  return t ? "" : 'style="display:none" hidden';
                }, l = function(n) {
                  return t._t(n);
                }, m = c('\n      <div :ref="root" class="pickr">\n\n        '.concat(d ? "" : '<button type="button" :ref="button" class="pcr-button"></button>', '\n\n        <div :ref="app" class="pcr-app ').concat(f || "", '" data-theme="').concat(g, '" ').concat(h ? 'style="position: unset"' : "", ' aria-label="').concat(l("ui:dialog"), '" role="window">\n          <div class="pcr-selection" ').concat(a(s.palette), '>\n            <div :obj="preview" class="pcr-color-preview" ').concat(a(s.preview), '>\n              <button type="button" :ref="lastColor" class="pcr-last-color" aria-label="').concat(l("btn:last-color"), '"></button>\n              <div :ref="currentColor" class="pcr-current-color"></div>\n            </div>\n\n            <div :obj="palette" class="pcr-color-palette">\n              <div :ref="picker" class="pcr-picker"></div>\n              <div :ref="palette" class="pcr-palette" tabindex="0" aria-label="').concat(l("aria:palette"), '" role="listbox"></div>\n            </div>\n\n            <div :obj="hue" class="pcr-color-chooser" ').concat(a(s.hue), '>\n              <div :ref="picker" class="pcr-picker"></div>\n              <div :ref="slider" class="pcr-hue pcr-slider" tabindex="0" aria-label="').concat(l("aria:hue"), '" role="slider"></div>\n            </div>\n\n            <div :obj="opacity" class="pcr-color-opacity" ').concat(a(s.opacity), '>\n              <div :ref="picker" class="pcr-picker"></div>\n              <div :ref="slider" class="pcr-opacity pcr-slider" tabindex="0" aria-label="').concat(l("aria:opacity"), '" role="slider"></div>\n            </div>\n          </div>\n\n          <div class="pcr-swatches ').concat(s.palette ? "" : "pcr-last", '" :ref="swatches"></div>\n\n          <div :obj="interaction" class="pcr-interaction" ').concat(a(Object.keys(s.interaction).length), '>\n            <input :ref="result" class="pcr-result" type="text" spellcheck="false" ').concat(a(s.interaction.input), ' aria-label="').concat(l("aria:input"), '">\n\n            <input :arr="options" class="pcr-type" data-type="HEXA" value="').concat(v ? "HEX" : "HEXA", '" type="button" ').concat(a(s.interaction.hex), '>\n            <input :arr="options" class="pcr-type" data-type="RGBA" value="').concat(v ? "RGB" : "RGBA", '" type="button" ').concat(a(s.interaction.rgba), '>\n            <input :arr="options" class="pcr-type" data-type="HSLA" value="').concat(v ? "HSL" : "HSLA", '" type="button" ').concat(a(s.interaction.hsla), '>\n            <input :arr="options" class="pcr-type" data-type="HSVA" value="').concat(v ? "HSV" : "HSVA", '" type="button" ').concat(a(s.interaction.hsva), '>\n            <input :arr="options" class="pcr-type" data-type="CMYK" value="CMYK" type="button" ').concat(a(s.interaction.cmyk), '>\n\n            <input :ref="save" class="pcr-save" value="').concat(l("btn:save"), '" type="button" ').concat(a(s.interaction.save), ' aria-label="').concat(l("aria:btn:save"), '">\n            <input :ref="cancel" class="pcr-cancel" value="').concat(l("btn:cancel"), '" type="button" ').concat(a(s.interaction.cancel), ' aria-label="').concat(l("aria:btn:cancel"), '">\n            <input :ref="clear" class="pcr-clear" value="').concat(l("btn:clear"), '" type="button" ').concat(a(s.interaction.clear), ' aria-label="').concat(l("aria:btn:clear"), '">\n          </div>\n        </div>\n      </div>\n    ')), x = m.interaction;
                return x.options.find((function(t) {
                  return !t.hidden && !t.classList.add("active");
                })), x.type = function() {
                  return x.options.find((function(t) {
                    return t.classList.contains("active");
                  }));
                }, m;
              }(this), t.useAsButton && (this._root.button = t.el), t.container.appendChild(this._root.root);
            }, B.prototype._finalBuild = function() {
              var t = this.options, n = this._root;
              if (t.container.removeChild(n.root), t.inline) {
                var s = t.el.parentElement;
                t.el.nextSibling ? s.insertBefore(n.app, t.el.nextSibling) : s.appendChild(n.app);
              } else t.container.appendChild(n.app);
              t.useAsButton ? t.inline && t.el.remove() : t.el.parentNode.replaceChild(n.root, t.el), 
              t.disabled && this.disable(), t.comparison || (n.button.style.transition = "none", 
              t.useAsButton || (n.preview.lastColor.style.transition = "none")), this.hide();
            }, B.prototype._buildComponents = function() {
              var t = this, n = this, s = this.options.components, d = (n.options.sliders || "v").repeat(2), h = d.match(/^[vh]+$/g) ? d : [], f = h[0], g = h[1], r = function() {
                return t._color || (t._color = t._lastColor.clone());
              }, v = {
                palette: O({
                  element: n._root.palette.picker,
                  wrapper: n._root.palette.palette,
                  onstop: function() {
                    return n._emit("changestop", n);
                  },
                  onchange: function(t, d) {
                    if (s.palette) {
                      var h = r(), f = n._root, g = n.options, v = f.preview, m = v.lastColor, x = v.currentColor;
                      n._recalc && (h.s = 100 * t, h.v = 100 - 100 * d, h.v < 0 && (h.v = 0), n._updateOutput());
                      var C = h.toRGBA().toString(0);
                      this.element.style.background = C, this.wrapper.style.background = "\n                        linear-gradient(to top, rgba(0, 0, 0, ".concat(h.a, "), transparent),\n                        linear-gradient(to left, hsla(").concat(h.h, ", 100%, 50%, ").concat(h.a, "), rgba(255, 255, 255, ").concat(h.a, "))\n                    "), 
                      g.comparison ? g.useAsButton || n._lastColor || (m.style.color = C) : (f.button.style.color = C, 
                      f.button.classList.remove("clear"));
                      for (var H = h.toHEXA().toString(), z = 0, V = n._swatchColors; z < V.length; z++) {
                        var R = V[z], M = R.el, L = R.color;
                        M.classList[H === L.toHEXA().toString() ? "add" : "remove"]("pcr-active");
                      }
                      x.style.color = C;
                    }
                  }
                }),
                hue: O({
                  lock: "v" === g ? "h" : "v",
                  element: n._root.hue.picker,
                  wrapper: n._root.hue.slider,
                  onstop: function() {
                    return n._emit("changestop", n);
                  },
                  onchange: function(t) {
                    if (s.hue && s.palette) {
                      var d = r();
                      n._recalc && (d.h = 360 * t), this.element.style.backgroundColor = "hsl(".concat(d.h, ", 100%, 50%)"), 
                      v.palette.trigger();
                    }
                  }
                }),
                opacity: O({
                  lock: "v" === f ? "h" : "v",
                  element: n._root.opacity.picker,
                  wrapper: n._root.opacity.slider,
                  onstop: function() {
                    return n._emit("changestop", n);
                  },
                  onchange: function(t) {
                    if (s.opacity && s.palette) {
                      var d = r();
                      n._recalc && (d.a = Math.round(100 * t) / 100), this.element.style.background = "rgba(0, 0, 0, ".concat(d.a, ")"), 
                      v.palette.trigger();
                    }
                  }
                }),
                selectable: E({
                  elements: n._root.interaction.options,
                  className: "active",
                  onchange: function(t) {
                    n._representation = t.target.getAttribute("data-type").toUpperCase(), n._recalc && n._updateOutput();
                  }
                })
              };
              this._components = v;
            }, B.prototype._bindEvents = function() {
              var t = this, n = this._root, s = this.options, d = [ h(n.interaction.clear, "click", (function() {
                return t._clearColor();
              })), h([ n.interaction.cancel, n.preview.lastColor ], "click", (function() {
                t._emit("cancel", t), t.setHSVA.apply(t, __spreadArrays((t._lastColor || t._color).toHSVA(), [ !0 ]));
              })), h(n.interaction.save, "click", (function() {
                !t.applyColor() && !s.showAlways && t.hide();
              })), h(n.interaction.result, [ "keyup", "input" ], (function(n) {
                t.setColor(n.target.value, !0) && !t._initializingActive && t._emit("change", t._color), 
                n.stopImmediatePropagation();
              })), h(n.interaction.result, [ "focus", "blur" ], (function(n) {
                t._recalc = "blur" === n.type, t._recalc && t._updateOutput();
              })), h([ n.palette.palette, n.palette.picker, n.hue.slider, n.hue.picker, n.opacity.slider, n.opacity.picker ], [ "mousedown", "touchstart" ], (function() {
                return t._recalc = !0;
              }), {
                passive: !0
              }) ];
              if (!s.showAlways) {
                var f = s.closeWithKey;
                d.push(h(n.button, "click", (function() {
                  return t.isOpen() ? t.hide() : t.show();
                })), h(document, "keyup", (function(n) {
                  return t.isOpen() && (n.key === f || n.code === f) && t.hide();
                })), h(document, [ "touchstart", "mousedown" ], (function(s) {
                  t.isOpen() && !l(s).some((function(t) {
                    return t === n.app || t === n.button;
                  })) && t.hide();
                }), {
                  capture: !0
                }));
              }
              if (s.adjustableNumbers) {
                var g = {
                  rgba: [ 255, 255, 255, 1 ],
                  hsva: [ 360, 100, 100, 1 ],
                  hsla: [ 360, 100, 100, 1 ],
                  cmyk: [ 100, 100, 100, 100 ]
                };
                u(n.interaction.result, (function(n, s, d) {
                  var h = g[t.getColorRepresentation().toLowerCase()];
                  if (h) {
                    var f = h[d], v = n + (f >= 100 ? 1e3 * s : s);
                    return v <= 0 ? 0 : Number((v < f ? v : f).toPrecision(3));
                  }
                  return n;
                }));
              }
              if (s.autoReposition && !s.inline) {
                var v = null, m = this;
                d.push(h(window, [ "scroll", "resize" ], (function() {
                  m.isOpen() && (s.closeOnScroll && m.hide(), null === v ? (v = setTimeout((function() {
                    return v = null;
                  }), 100), requestAnimationFrame((function e() {
                    m._rePositioningPicker(), null !== v && requestAnimationFrame(e);
                  }))) : (clearTimeout(v), v = setTimeout((function() {
                    return v = null;
                  }), 100)));
                }), {
                  capture: !0
                }));
              }
              this._eventBindings = d;
            }, B.prototype._rePositioningPicker = function() {
              var t = this.options;
              if (!t.inline && !this._nanopop.update({
                container: document.body.getBoundingClientRect(),
                position: t.position
              })) {
                var n = this._root.app, s = n.getBoundingClientRect();
                n.style.top = "".concat((window.innerHeight - s.height) / 2, "px"), n.style.left = "".concat((window.innerWidth - s.width) / 2, "px");
              }
            }, B.prototype._updateOutput = function() {
              var t = this._root, n = this._color, s = this.options;
              if (t.interaction.type()) {
                var d = "to".concat(t.interaction.type().getAttribute("data-type"));
                t.interaction.result.value = "function" == typeof n[d] ? n[d]().toString(s.outputPrecision) : "";
              }
              !this._initializingActive && this._recalc && this._emit("change", n);
            }, B.prototype._clearColor = function(t) {
              void 0 === t && (t = !1);
              var n = this._root, s = this.options;
              s.useAsButton || (n.button.style.color = "rgba(0, 0, 0, 0.15)"), n.button.classList.add("clear"), 
              s.showAlways || this.hide(), this._lastColor = null, this._initializingActive || t || (this._emit("save", null), 
              this._emit("clear", this));
            }, B.prototype._parseLocalColor = function(t) {
              var n = function(t) {
                t = t.match(/^[a-zA-Z]+$/) ? function(t) {
                  if ("black" === t.toLowerCase()) return "#000";
                  var n = document.createElement("canvas").getContext("2d");
                  return n.fillStyle = t, "#000" === n.fillStyle ? null : n.fillStyle;
                }(t) : t;
                var n, s = {
                  cmyk: /^cmyk[\D]+([\d.]+)[\D]+([\d.]+)[\D]+([\d.]+)[\D]+([\d.]+)/i,
                  rgba: /^((rgba)|rgb)[\D]+([\d.]+)[\D]+([\d.]+)[\D]+([\d.]+)[\D]*?([\d.]+|$)/i,
                  hsla: /^((hsla)|hsl)[\D]+([\d.]+)[\D]+([\d.]+)[\D]+([\d.]+)[\D]*?([\d.]+|$)/i,
                  hsva: /^((hsva)|hsv)[\D]+([\d.]+)[\D]+([\d.]+)[\D]+([\d.]+)[\D]*?([\d.]+|$)/i,
                  hexa: /^#?(([\dA-Fa-f]{3,4})|([\dA-Fa-f]{6})|([\dA-Fa-f]{8}))$/i
                }, o = function(t) {
                  return t.map((function(t) {
                    return /^(|\d+)\.\d+|\d+$/.test(t) ? Number(t) : void 0;
                  }));
                };
                t: for (var d in s) if (n = s[d].exec(t)) {
                  var r_2 = function(t) {
                    return !!n[2] == ("number" == typeof t);
                  };
                  switch (d) {
                   case "cmyk":
                    var h = o(n), f = h[1], g = h[2], v = h[3], m = h[4];
                    if (f > 100 || g > 100 || v > 100 || m > 100) break t;
                    return {
                      values: _(f, g, v, m),
                      type: d
                    };

                   case "rgba":
                    var x = o(n), C = x[3], H = x[4], z = x[5], V = x[6];
                    if (C > 255 || H > 255 || z > 255 || V < 0 || V > 1 || !r_2(V)) break t;
                    return {
                      values: __spreadArrays(y(C, H, z), [ V ]),
                      a: V,
                      type: d
                    };

                   case "hexa":
                    var R = n[1];
                    4 !== R.length && 3 !== R.length || (R = R.split("").map((function(t) {
                      return t + t;
                    })).join(""));
                    var M = R.substring(0, 6), L = R.substring(6);
                    return L = L ? parseInt(L, 16) / 255 : void 0, {
                      values: __spreadArrays(A(M), [ L ]),
                      a: L,
                      type: d
                    };

                   case "hsla":
                    var D = o(n), P = D[3], F = D[4], Z = D[5], I = D[6];
                    if (P > 360 || F > 100 || Z > 100 || I < 0 || I > 1 || !r_2(I)) break t;
                    return {
                      values: __spreadArrays(w(P, F, Z), [ I ]),
                      a: I,
                      type: d
                    };

                   case "hsva":
                    var N = o(n), T = N[3], U = N[4], X = N[5], G = N[6];
                    if (T > 360 || U > 100 || X > 100 || G < 0 || G > 1 || !r_2(G)) break t;
                    return {
                      values: [ T, U, X, G ],
                      a: G,
                      type: d
                    };
                  }
                }
                return {
                  values: null,
                  type: null
                };
              }(t), s = n.values, d = n.type, h = n.a, f = this.options.lockOpacity, g = void 0 !== h && 1 !== h;
              return s && 3 === s.length && (s[3] = void 0), {
                values: !s || f && g ? null : s,
                type: d
              };
            }, B.prototype._t = function(t) {
              return this.options.i18n[t] || B.I18N_DEFAULTS[t];
            }, B.prototype._emit = function(t) {
              for (var n = this, s = [], d = 1; d < arguments.length; d++) s[d - 1] = arguments[d];
              this._eventListener[t].forEach((function(t) {
                return t.apply(void 0, __spreadArrays(s, [ n ]));
              }));
            }, B.prototype.on = function(t, n) {
              return this._eventListener[t].push(n), this;
            }, B.prototype.off = function(t, n) {
              var s = this._eventListener[t] || [], d = s.indexOf(n);
              return ~d && s.splice(d, 1), this;
            }, B.prototype.addSwatch = function(t) {
              var n = this, s = this._parseLocalColor(t).values;
              if (s) {
                var d = this._swatchColors, f = this._root, g = k.apply(void 0, s), v = a('<button type="button" style="color: '.concat(g.toRGBA().toString(0), '" aria-label="').concat(this._t("btn:swatch"), '"/>'));
                return f.swatches.appendChild(v), d.push({
                  el: v,
                  color: g
                }), this._eventBindings.push(h(v, "click", (function() {
                  n.setHSVA.apply(n, __spreadArrays(g.toHSVA(), [ !0 ])), n._emit("swatchselect", g), 
                  n._emit("change", g);
                }))), !0;
              }
              return !1;
            }, B.prototype.removeSwatch = function(t) {
              var n = this._swatchColors[t];
              if (n) {
                var s = n.el;
                return this._root.swatches.removeChild(s), this._swatchColors.splice(t, 1), !0;
              }
              return !1;
            }, B.prototype.applyColor = function(t) {
              void 0 === t && (t = !1);
              var n = this._root, s = n.preview, d = n.button, h = this._color.toRGBA().toString(0);
              return s.lastColor.style.color = h, this.options.useAsButton || (d.style.color = h), 
              d.classList.remove("clear"), this._lastColor = this._color.clone(), this._initializingActive || t || this._emit("save", this._color), 
              this;
            }, B.prototype.destroy = function() {
              var t = this;
              this._eventBindings.forEach((function(t) {
                return f.apply(void 0, t);
              })), Object.keys(this._components).forEach((function(n) {
                return t._components[n].destroy();
              }));
            }, B.prototype.destroyAndRemove = function() {
              var t = this;
              this.destroy();
              var n = this._root, s = n.root, d = n.app;
              s.parentElement && s.parentElement.removeChild(s), d.parentElement.removeChild(d), 
              Object.keys(this).forEach((function(n) {
                return t[n] = null;
              }));
            }, B.prototype.hide = function() {
              return this._root.app.classList.remove("visible"), this._emit("hide", this), this;
            }, B.prototype.show = function() {
              return this.options.disabled || (this._root.app.classList.add("visible"), this._rePositioningPicker(), 
              this._emit("show", this)), this;
            }, B.prototype.isOpen = function() {
              return this._root.app.classList.contains("visible");
            }, B.prototype.setHSVA = function(t, n, s, d, h) {
              void 0 === t && (t = 360), void 0 === n && (n = 0), void 0 === s && (s = 0), void 0 === d && (d = 1), 
              void 0 === h && (h = !1);
              var f = this._recalc;
              if (this._recalc = !1, t < 0 || t > 360 || n < 0 || n > 100 || s < 0 || s > 100 || d < 0 || d > 1) return !1;
              this._color = k(t, n, s, d);
              var g = this._components, v = g.hue, m = g.opacity, x = g.palette;
              return v.update(t / 360), m.update(d), x.update(n / 100, 1 - s / 100), h || this.applyColor(), 
              f && this._updateOutput(), this._recalc = f, !0;
            }, B.prototype.setColor = function(t, n) {
              if (void 0 === n && (n = !1), null === t) return this._clearColor(n), !0;
              var s = this._parseLocalColor(t), d = s.values, h = s.type;
              if (d) {
                var f = h.toUpperCase(), g = this._root.interaction.options, v = g.find((function(t) {
                  return t.getAttribute("data-type") === f;
                }));
                if (v && !v.hidden) for (var m = 0, x = g; m < x.length; m++) {
                  var C = x[m];
                  C.classList[C === v ? "add" : "remove"]("active");
                }
                return !!this.setHSVA.apply(this, __spreadArrays(d, [ n ])) && this.setColorRepresentation(f);
              }
              return !1;
            }, B.prototype.setColorRepresentation = function(t) {
              return t = t.toUpperCase(), !!this._root.interaction.options.find((function(n) {
                return n.getAttribute("data-type").startsWith(t) && !n.click();
              }));
            }, B.prototype.getColorRepresentation = function() {
              return this._representation;
            }, B.prototype.getColor = function() {
              return this._color;
            }, B.prototype.getSelectedColor = function() {
              return this._lastColor;
            }, B.prototype.getRoot = function() {
              return this._root;
            }, B.prototype.disable = function() {
              return this.hide(), this.options.disabled = !0, this._root.button.classList.add("disabled"), 
              this;
            }, B.prototype.enable = function() {
              return this.options.disabled = !1, this._root.button.classList.remove("disabled"), 
              this;
            }, B;
          }();
          j(z, "utils", d), j(z, "version", g.a), j(z, "I18N_DEFAULTS", {
            "ui:dialog": "color picker dialog",
            "btn:toggle": "toggle color picker dialog",
            "btn:swatch": "color swatch",
            "btn:last-color": "use previous color",
            "btn:save": "Save",
            "btn:cancel": "Cancel",
            "btn:clear": "Clear",
            "aria:btn:save": "save and close",
            "aria:btn:cancel": "cancel and close",
            "aria:btn:clear": "clear and close",
            "aria:input": "color input field",
            "aria:palette": "color selection area",
            "aria:hue": "hue selection slider",
            "aria:opacity": "selection slider"
          }), j(z, "DEFAULT_OPTIONS", {
            appClass: null,
            theme: "classic",
            useAsButton: !1,
            padding: 8,
            disabled: !1,
            comparison: !0,
            closeOnScroll: !1,
            outputPrecision: 0,
            lockOpacity: !1,
            autoReposition: !0,
            container: "body",
            components: {
              interaction: {}
            },
            i18n: {},
            swatches: null,
            inline: !1,
            sliders: null,
            default: "#42445a",
            defaultRepresentation: null,
            position: "bottom-middle",
            adjustableNumbers: !0,
            showAlways: !1,
            closeWithKey: "Escape"
          }), j(z, "create", (function(t) {
            return new z(t);
          })), n.default = z;
        } ]).default;
      })), x = v(m);
      t("gxg_color_picker", /** @class */ function() {
        function class_1(t) {
          n(this, t), this.save = s(this, "save", 7), this.nameInputEvent = s(this, "nameInputEvent", 7), 
          this.change = s(this, "change", 7), 
          /**
                    The label of the color picker (optional)
                    */
          this.label = "", 
          /**
                    The color value, such as "red", #CCDDEE, or rgba(220,140,40,.5)
                    */
          this.value = "white", this.colorRepresentation = "HEXA", this.colorInputValue = "", 
          this.colorChangedFromInput = !1;
        }
        //Lyfe cycles
                return class_1.prototype.componentDidLoad = function() {
          var t = this;
          //Detect color representation
                    this.value.includes("rgb") ? this.colorRepresentation = "RGBA" : this.value.includes("#") && (this.colorRepresentation = "HEXA");
          var n = this.element.shadowRoot.querySelector(".color-picker"), s = this.element.shadowRoot.querySelector(".color-picker-main-container");
          this.pickr = new x({
            el: n,
            theme: "nano",
            container: s,
            inline: !0,
            showAlways: !0,
            default: this.value,
            // useAsButton: true,
            components: {
              // Main components
              preview: !0,
              opacity: !0,
              hue: !0,
              // Input / output Options
              interaction: {
                // hex: true,
                // rgb: true,
                input: !1
              }
            }
          }), this.pickr.on("change", (function(n) {
            t.colorObject = n, "HEXA" === t.colorRepresentation ? t.value = t.colorObject.toHEXA().toString() : "RGBA" === t.colorRepresentation && (t.value = t.colorObject.toRGBA().toString(0)), 
            t.change.emit(t.value);
          })), this.pickr.on("show", (function() {
            t.colorObject = t.pickr.getColor();
          }));
          var d = {
            root: document.querySelector("body"),
            rootMargin: "0px",
            threshold: 1
          };
          new IntersectionObserver((function() {
            t.pickr.setColor(t.value);
 //We have to set the color by force, because we need to get the color at this time, and pickr seems to defer it.
                    }), d).observe(this.element);
        }, class_1.prototype.componentDidUnload = function() {
          this.pickr.destroy();
        }, class_1.prototype.watchHandler = function(t) {
          this.pickr.setColor(t);
        }, 
        //Button Methods
        class_1.prototype.handleHexaButtonClick = function() {
          this.colorChangedFromInput = !1, this.colorRepresentation = "HEXA", this.value = this.colorObject.toHEXA().toString(), 
          this.change.emit(this.value);
        }, class_1.prototype.handleRgbaButtonClick = function() {
          this.colorChangedFromInput = !1, this.colorRepresentation = "RGBA", this.value = this.colorObject.toRGBA().toString(0), 
          this.change.emit(this.value);
        }, class_1.prototype.handleTitleValueChange = function(t) {
          var n = t.target;
          this.label = n.value;
        }, class_1.prototype.handleColorValueChange = function(t) {
          this.colorChangedFromInput = !0;
          var n = t.target;
          this.colorInputValue = n.value, this.pickr.setColor(n.value);
        }, class_1.prototype.colorValue = function() {
          if (!this.colorChangedFromInput) {
            //We only want to update the color value on the input if the pick was changed directly by handling the color picker window, not by changing the input color value
            if (void 0 === this.colorObject) return "";
            if ("HEXA" === this.colorRepresentation) return this.colorObject.toHEXA().toString();
            if ("RGBA" === this.colorRepresentation) return this.colorObject.toRGBA().toString(0);
          }
          return this.colorChangedFromInput = !0, this.colorInputValue;
        }, class_1.prototype.setActiveButton = function() {
          return this.value.includes("rgb") ? "rgba" : "hexa";
        }, class_1.prototype.render = function() {
          return d(h, null, d("h1", {
            class: "label"
          }, this.label), d("div", {
            class: {
              "color-picker-main-container": !0
            },
            id: "color-picker-main-container"
          }, d("div", {
            class: "color-picker"
          }), d("div", {
            class: "cp-gxg-buttons before-color-value",
            slot: "editable"
          }, d("gxg-button-group", {
            "default-selected-btn-id": this.setActiveButton()
          }, d("button", {
            id: "rgba",
            onClick: this.handleRgbaButtonClick.bind(this)
          }, "RGBA"), d("button", {
            id: "hexa",
            onClick: this.handleHexaButtonClick.bind(this)
          }, "HEXA"))), d("input", {
            type: "text",
            name: "cp-color-value",
            id: "cp-color-value",
            value: this.colorValue(),
            class: "color-picker-main-container-textbox",
            onInput: this.handleColorValueChange.bind(this)
          })));
        }, Object.defineProperty(class_1.prototype, "element", {
          get: function() {
            return f(this);
          },
          enumerable: !1,
          configurable: !0
        }), Object.defineProperty(class_1, "watchers", {
          get: function() {
            return {
              value: [ "watchHandler" ]
            };
          },
          enumerable: !1,
          configurable: !0
        }), class_1;
      }()).style = '/*! Pickr 1.7.4 MIT | https://github.com/Simonwep/pickr */.pickr{position:relative;overflow:visible;-webkit-transform:translateY(0);transform:translateY(0)}.pickr *{-webkit-box-sizing:border-box;box-sizing:border-box;outline:none;border:none;-webkit-appearance:none}.pickr .pcr-button{position:relative;height:2em;width:2em;padding:.5em;cursor:pointer;font-family:-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Helvetica Neue,Arial,sans-serif;border-radius:.15em;background:url(\'data:image/svg+xml;utf8, <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 50 50" stroke="%2342445A" stroke-width="5px" stroke-linecap="round"><path d="M45,45L5,5"></path><path d="M45,5L5,45"></path></svg>\') no-repeat 50%;background-size:0;-webkit-transition:all .3s;transition:all .3s}.pickr .pcr-button:before{background:url(\'data:image/svg+xml;utf8, <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 2 2"><path fill="white" d="M1,0H2V1H1V0ZM0,1H1V2H0V1Z"/><path fill="gray" d="M0,0H1V1H0V0ZM1,1H2V2H1V1Z"/></svg>\');background-size:.5em;z-index:-1;z-index:auto}.pickr .pcr-button:after,.pickr .pcr-button:before{position:absolute;content:"";top:0;left:0;width:100%;height:100%;border-radius:.15em}.pickr .pcr-button:after{-webkit-transition:background .3s;transition:background .3s;background:currentColor}.pickr .pcr-button.clear{background-size:70%}.pickr .pcr-button.clear:before{opacity:0}.pickr .pcr-button.clear:focus{-webkit-box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px currentColor;box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px currentColor}.pickr .pcr-button.disabled{cursor:not-allowed}.pcr-app *,.pickr *{-webkit-box-sizing:border-box;box-sizing:border-box;outline:none;border:none;-webkit-appearance:none}.pcr-app button.pcr-active,.pcr-app button:focus,.pcr-app input.pcr-active,.pcr-app input:focus,.pickr button.pcr-active,.pickr button:focus,.pickr input.pcr-active,.pickr input:focus{-webkit-box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px currentColor;box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px currentColor}.pcr-app .pcr-palette,.pcr-app .pcr-slider,.pickr .pcr-palette,.pickr .pcr-slider{-webkit-transition:-webkit-box-shadow .3s;transition:-webkit-box-shadow .3s;transition:box-shadow .3s;transition:box-shadow .3s, -webkit-box-shadow .3s}.pcr-app .pcr-palette:focus,.pcr-app .pcr-slider:focus,.pickr .pcr-palette:focus,.pickr .pcr-slider:focus{-webkit-box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px rgba(0,0,0,.25);box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px rgba(0,0,0,.25)}.pcr-app{position:fixed;display:-ms-flexbox;display:flex;-ms-flex-direction:column;flex-direction:column;z-index:10000;border-radius:.1em;background:#fff;opacity:0;visibility:hidden;-webkit-transition:opacity .3s,visibility 0s .3s;transition:opacity .3s,visibility 0s .3s;font-family:-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Helvetica Neue,Arial,sans-serif;-webkit-box-shadow:0 .15em 1.5em 0 rgba(0,0,0,.1),0 0 1em 0 rgba(0,0,0,.03);box-shadow:0 .15em 1.5em 0 rgba(0,0,0,.1),0 0 1em 0 rgba(0,0,0,.03);left:0;top:0}.pcr-app.visible{-webkit-transition:opacity .3s;transition:opacity .3s;visibility:visible;opacity:1}.pcr-app .pcr-swatches{display:-ms-flexbox;display:flex;-ms-flex-wrap:wrap;flex-wrap:wrap;margin-top:.75em}.pcr-app .pcr-swatches.pcr-last{margin:0}@supports (display:grid){.pcr-app .pcr-swatches{display:grid;-ms-flex-align:center;align-items:center;grid-template-columns:repeat(auto-fit,1.75em)}}.pcr-app .pcr-swatches>button{font-size:1em;position:relative;width:calc(1.75em - 5px);height:calc(1.75em - 5px);border-radius:.15em;cursor:pointer;margin:2.5px;-ms-flex-negative:0;flex-shrink:0;justify-self:center;-webkit-transition:all .15s;transition:all .15s;overflow:hidden;background:transparent;z-index:1}.pcr-app .pcr-swatches>button:before{position:absolute;content:"";top:0;left:0;width:100%;height:100%;background:url(\'data:image/svg+xml;utf8, <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 2 2"><path fill="white" d="M1,0H2V1H1V0ZM0,1H1V2H0V1Z"/><path fill="gray" d="M0,0H1V1H0V0ZM1,1H2V2H1V1Z"/></svg>\');background-size:6px;border-radius:.15em;z-index:-1}.pcr-app .pcr-swatches>button:after{content:"";position:absolute;top:0;left:0;width:100%;height:100%;background:currentColor;border:1px solid rgba(0,0,0,.05);border-radius:.15em;-webkit-box-sizing:border-box;box-sizing:border-box}.pcr-app .pcr-swatches>button:hover{-webkit-filter:brightness(1.05);filter:brightness(1.05)}.pcr-app .pcr-swatches>button:not(.pcr-active){-webkit-box-shadow:none;box-shadow:none}.pcr-app .pcr-interaction{display:-ms-flexbox;display:flex;-ms-flex-wrap:wrap;flex-wrap:wrap;-ms-flex-align:center;align-items:center;margin:0 -.2em}.pcr-app .pcr-interaction>*{margin:0 .2em}.pcr-app .pcr-interaction input{letter-spacing:.07em;font-size:.75em;text-align:center;cursor:pointer;color:#75797e;background:#f1f3f4;border-radius:.15em;-webkit-transition:all .15s;transition:all .15s;padding:.45em .5em;margin-top:.75em}.pcr-app .pcr-interaction input:hover{-webkit-filter:brightness(.975);filter:brightness(.975)}.pcr-app .pcr-interaction input:focus{-webkit-box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px rgba(66,133,244,.75);box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px rgba(66,133,244,.75)}.pcr-app .pcr-interaction .pcr-result{color:#75797e;text-align:left;-ms-flex:1 1 8em;flex:1 1 8em;min-width:8em;-webkit-transition:all .2s;transition:all .2s;border-radius:.15em;background:#f1f3f4;cursor:text}.pcr-app .pcr-interaction .pcr-result::-moz-selection{background:#4285f4;color:#fff}.pcr-app .pcr-interaction .pcr-result::selection{background:#4285f4;color:#fff}.pcr-app .pcr-interaction .pcr-type.active{color:#fff;background:#4285f4}.pcr-app .pcr-interaction .pcr-cancel,.pcr-app .pcr-interaction .pcr-clear,.pcr-app .pcr-interaction .pcr-save{width:auto;color:#fff}.pcr-app .pcr-interaction .pcr-cancel:hover,.pcr-app .pcr-interaction .pcr-clear:hover,.pcr-app .pcr-interaction .pcr-save:hover{-webkit-filter:brightness(.925);filter:brightness(.925)}.pcr-app .pcr-interaction .pcr-save{background:#4285f4}.pcr-app .pcr-interaction .pcr-cancel,.pcr-app .pcr-interaction .pcr-clear{background:#f44250}.pcr-app .pcr-interaction .pcr-cancel:focus,.pcr-app .pcr-interaction .pcr-clear:focus{-webkit-box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px rgba(244,66,80,.75);box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px rgba(244,66,80,.75)}.pcr-app .pcr-selection .pcr-picker{position:absolute;height:18px;width:18px;border:2px solid #fff;border-radius:100%;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none}.pcr-app .pcr-selection .pcr-color-chooser,.pcr-app .pcr-selection .pcr-color-opacity,.pcr-app .pcr-selection .pcr-color-palette{position:relative;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;display:-ms-flexbox;display:flex;-ms-flex-direction:column;flex-direction:column;cursor:grab;cursor:-webkit-grab}.pcr-app .pcr-selection .pcr-color-chooser:active,.pcr-app .pcr-selection .pcr-color-opacity:active,.pcr-app .pcr-selection .pcr-color-palette:active{cursor:grabbing;cursor:-webkit-grabbing}.pcr-app[data-theme=nano]{width:14.25em;max-width:95vw}.pcr-app[data-theme=nano] .pcr-swatches{margin-top:.6em;padding:0 .6em}.pcr-app[data-theme=nano] .pcr-interaction{padding:0 .6em .6em}.pcr-app[data-theme=nano] .pcr-selection{display:grid;grid-gap:.6em;grid-template-columns:1fr 4fr;grid-template-rows:5fr auto auto;-ms-flex-align:center;align-items:center;height:10.5em;width:100%;align-self:flex-start}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-preview{grid-area:2/1/4/1;height:100%;width:100%;display:-ms-flexbox;display:flex;-ms-flex-direction:row;flex-direction:row;-ms-flex-pack:center;justify-content:center;margin-left:.6em}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-preview .pcr-last-color{display:none}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-preview .pcr-current-color{position:relative;background:currentColor;width:2em;height:2em;border-radius:50em;overflow:hidden}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-preview .pcr-current-color:before{position:absolute;content:"";top:0;left:0;width:100%;height:100%;background:url(\'data:image/svg+xml;utf8, <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 2 2"><path fill="white" d="M1,0H2V1H1V0ZM0,1H1V2H0V1Z"/><path fill="gray" d="M0,0H1V1H0V0ZM1,1H2V2H1V1Z"/></svg>\');background-size:.5em;border-radius:.15em;z-index:-1}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-palette{grid-area:1/1/2/3;width:100%;height:100%;z-index:1}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-palette .pcr-palette{border-radius:.15em;width:100%;height:100%}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-palette .pcr-palette:before{position:absolute;content:"";top:0;left:0;width:100%;height:100%;background:url(\'data:image/svg+xml;utf8, <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 2 2"><path fill="white" d="M1,0H2V1H1V0ZM0,1H1V2H0V1Z"/><path fill="gray" d="M0,0H1V1H0V0ZM1,1H2V2H1V1Z"/></svg>\');background-size:.5em;border-radius:.15em;z-index:-1}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-chooser{grid-area:2/2/2/2}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-opacity{grid-area:3/2/3/2}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-chooser,.pcr-app[data-theme=nano] .pcr-selection .pcr-color-opacity{height:.5em;margin:0 .6em}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-chooser .pcr-picker,.pcr-app[data-theme=nano] .pcr-selection .pcr-color-opacity .pcr-picker{top:50%;-webkit-transform:translateY(-50%);transform:translateY(-50%)}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-chooser .pcr-slider,.pcr-app[data-theme=nano] .pcr-selection .pcr-color-opacity .pcr-slider{-ms-flex-positive:1;flex-grow:1;border-radius:50em}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-chooser .pcr-slider{background:-webkit-gradient(linear,left top, right top,from(red),color-stop(#ff0),color-stop(#0f0),color-stop(#0ff),color-stop(#00f),color-stop(#f0f),to(red));background:linear-gradient(90deg,red,#ff0,#0f0,#0ff,#00f,#f0f,red)}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-opacity .pcr-slider{background:-webkit-gradient(linear,left top, right top,from(transparent),to(#000)),url(\'data:image/svg+xml;utf8, <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 2 2"><path fill="white" d="M1,0H2V1H1V0ZM0,1H1V2H0V1Z"/><path fill="gray" d="M0,0H1V1H0V0ZM1,1H2V2H1V1Z"/></svg>\');background:linear-gradient(90deg,transparent,#000),url(\'data:image/svg+xml;utf8, <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 2 2"><path fill="white" d="M1,0H2V1H1V0ZM0,1H1V2H0V1Z"/><path fill="gray" d="M0,0H1V1H0V0ZM1,1H2V2H1V1Z"/></svg>\');background-size:100%,.25em}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{width:230px;display:block}.label{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;display:-ms-flexbox;display:flex;margin-bottom:var(--spacing-lay-xs)}.color-picker-main-container{-webkit-box-shadow:var(--box-shadow-01);box-shadow:var(--box-shadow-01);z-index:10;--cardAnimationDelay:0;position:relative;-webkit-animation-name:slideUp;animation-name:slideUp;-webkit-animation-duration:0.75s;animation-duration:0.75s;-webkit-animation-iteration-count:1;animation-iteration-count:1;-webkit-animation-delay:var(--cardAnimationDelay);animation-delay:var(--cardAnimationDelay);background:var(--color-background);position:relative;margin-top:-1px;-webkit-transition:max-height 0.5s ease-in-out;transition:max-height 0.5s ease-in-out;padding:var(--spacing-comp-02) var(--card-padding) 0 var(--card-padding)}@-webkit-keyframes slideUp{0%{top:5px;opacity:0}100%{top:0px;opacity:100%}}@keyframes slideUp{0%{top:5px;opacity:0}100%{top:0px;opacity:100%}}.color-picker-main-container-textbox{font-family:var(--font-family-primary);font-size:var(--font-size-md);color:var(--color--black);padding-left:var(--spacing-comp-02);padding-right:var(--spacing-comp-02);padding-top:var(--spacing-comp-01);padding-bottom:var(--spacing-comp-01);background:var(--color-background);border-width:var(--border-width-sm);border-style:var(--border-style-regular);border-color:var(--gray-04);letter-spacing:0.018em;height:var(--spacing-comp-05);border-radius:0;width:100%;margin-bottom:var(--spacing-comp-02);-webkit-box-sizing:border-box;box-sizing:border-box}.color-picker-main-container--visible{max-height:400px}.pcr-app{background:var(--color-background)}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-palette .pcr-palette{border-radius:0}.pickr{display:none}.pcr-app{max-width:100%;width:100% !important;-webkit-box-shadow:none;box-shadow:none}.cp-gxg-buttons{padding:var(--spacing-comp-02)}.color-picker-main-container-textbox{margin:0 var(--spacing-comp-02);width:calc(100% - 16px);margin-bottom:var(--spacing-comp-02)}.cp-gxg-buttons.before-color-value gxg-button{margin-right:var(--spacing-comp-02)}.cp-gxg-buttons.after-color-value{text-align:right}.pcr-color-chooser,.pcr-color-opacity{width:85%}';
    }
  };
}));