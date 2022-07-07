import { r as t, c as n, h as s, H as h, g as d } from "./p-eb9dc661.js";

import { c as m, u as f } from "./p-9583448a.js";

const v = f(m((function(t, n) {
  /*! Pickr 1.7.4 MIT | https://github.com/Simonwep/pickr */
  window, t.exports = function(t) {
    var n = {};
    function o(s) {
      if (n[s]) return n[s].exports;
      var h = n[s] = {
        i: s,
        l: !1,
        exports: {}
      };
      return t[s].call(h.exports, h, h.exports, o), h.l = !0, h.exports;
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
      }), 2 & n && "string" != typeof t) for (var h in t) o.d(s, h, function(n) {
        return t[n];
      }.bind(null, h));
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
    var h = {};
    function i(t, n, s, h, d = {}) {
      n instanceof HTMLCollection || n instanceof NodeList ? n = Array.from(n) : Array.isArray(n) || (n = [ n ]), 
      Array.isArray(s) || (s = [ s ]);
      for (const m of n) for (const n of s) m[t](n, h, {
        capture: !1,
        ...d
      });
      return Array.prototype.slice.call(arguments, 1);
    }
    s.r(h), s.d(h, "on", (function() {
      return d;
    })), s.d(h, "off", (function() {
      return m;
    })), s.d(h, "createElementFromString", (function() {
      return a;
    })), s.d(h, "createFromTemplate", (function() {
      return c;
    })), s.d(h, "eventPath", (function() {
      return l;
    })), s.d(h, "resolveElement", (function() {
      return p;
    })), s.d(h, "adjustableInputNumbers", (function() {
      return u;
    }));
    const d = i.bind(null, "addEventListener"), m = i.bind(null, "removeEventListener");
    function a(t) {
      const n = document.createElement("div");
      return n.innerHTML = t.trim(), n.firstElementChild;
    }
    function c(t) {
      const e = (t, n) => {
        const s = t.getAttribute(n);
        return t.removeAttribute(n), s;
      }, o = (t, n = {}) => {
        const s = e(t, ":obj"), h = e(t, ":ref"), d = s ? n[s] = {} : n;
        h && (n[h] = t);
        for (const m of Array.from(t.children)) {
          const t = e(m, ":arr"), n = o(m, t ? {} : d);
          t && (d[t] || (d[t] = [])).push(Object.keys(n).length ? n : m);
        }
        return n;
      };
      return o(a(t));
    }
    function l(t) {
      let n = t.path || t.composedPath && t.composedPath();
      if (n) return n;
      let s = t.target.parentElement;
      for (n = [ t.target, s ]; s = s.parentElement; ) n.push(s);
      return n.push(document, window), n;
    }
    function p(t) {
      return t instanceof Element ? t : "string" == typeof t ? t.split(/>>/g).reduce((t, n, s, h) => (t = t.querySelector(n), 
      s < h.length - 1 ? t.shadowRoot : t), document) : null;
    }
    function u(t, n = (t => t)) {
      function o(s) {
        const h = [ .001, .01, .1 ][Number(s.shiftKey || 2 * s.ctrlKey)] * (s.deltaY < 0 ? 1 : -1);
        let d = 0, m = t.selectionStart;
        t.value = t.value.replace(/[\d.]+/g, (t, s) => s <= m && s + t.length >= m ? (m = s, 
        n(Number(t), h, d)) : (d++, t)), t.focus(), t.setSelectionRange(m, m), s.preventDefault(), 
        t.dispatchEvent(new Event("input"));
      }
      d(t, "focus", () => d(window, "wheel", o, {
        passive: !1
      })), d(t, "blur", () => m(window, "wheel", o));
    }
    var f = s(0);
    const {min: v, max: x, floor: C, round: H} = Math;
    function b(t, n, s) {
      n /= 100, s /= 100;
      const h = C(t = t / 360 * 6), d = t - h, m = s * (1 - n), f = s * (1 - d * n), v = s * (1 - (1 - d) * n), x = h % 6;
      return [ 255 * [ s, f, m, m, v, s ][x], 255 * [ v, s, s, f, m, m ][x], 255 * [ m, m, v, s, s, f ][x] ];
    }
    function g(t, n, s) {
      const h = (2 - (n /= 100)) * (s /= 100) / 2;
      return 0 !== h && (n = 1 === h ? 0 : h < .5 ? n * s / (2 * h) : n * s / (2 - 2 * h)), 
      [ t, 100 * n, 100 * h ];
    }
    function y(t, n, s) {
      const h = v(t /= 255, n /= 255, s /= 255), d = x(t, n, s), m = d - h;
      let f, C;
      if (0 === m) f = C = 0; else {
        C = m / d;
        const h = ((d - t) / 6 + m / 2) / m, v = ((d - n) / 6 + m / 2) / m, x = ((d - s) / 6 + m / 2) / m;
        t === d ? f = x - v : n === d ? f = 1 / 3 + h - x : s === d && (f = 2 / 3 + v - h), 
        f < 0 ? f += 1 : f > 1 && (f -= 1);
      }
      return [ 360 * f, 100 * C, 100 * d ];
    }
    function _(t, n, s, h) {
      return n /= 100, s /= 100, [ ...y(255 * (1 - v(1, (t /= 100) * (1 - (h /= 100)) + h)), 255 * (1 - v(1, n * (1 - h) + h)), 255 * (1 - v(1, s * (1 - h) + h))) ];
    }
    function w(t, n, s) {
      n /= 100;
      const h = 2 * (n *= (s /= 100) < .5 ? s : 1 - s) / (s + n) * 100, d = 100 * (s + n);
      return [ t, isNaN(h) ? 0 : h, d ];
    }
    function A(t) {
      return y(...t.match(/.{2}/g).map(t => parseInt(t, 16)));
    }
    function k(t = 0, n = 0, s = 0, h = 1) {
      const i = (t, n) => (s = -1) => n(~s ? t.map(t => Number(t.toFixed(s))) : t), d = {
        h: t,
        s: n,
        v: s,
        a: h,
        toHSVA() {
          const t = [ d.h, d.s, d.v, d.a ];
          return t.toString = i(t, t => "hsva(".concat(t[0], ", ").concat(t[1], "%, ").concat(t[2], "%, ").concat(d.a, ")")), 
          t;
        },
        toHSLA() {
          const t = [ ...g(d.h, d.s, d.v), d.a ];
          return t.toString = i(t, t => "hsla(".concat(t[0], ", ").concat(t[1], "%, ").concat(t[2], "%, ").concat(d.a, ")")), 
          t;
        },
        toRGBA() {
          const t = [ ...b(d.h, d.s, d.v), d.a ];
          return t.toString = i(t, t => "rgba(".concat(t[0], ", ").concat(t[1], ", ").concat(t[2], ", ").concat(d.a, ")")), 
          t;
        },
        toCMYK() {
          const t = function(t, n, s) {
            const h = b(t, n, s), d = h[0] / 255, m = h[1] / 255, f = h[2] / 255, x = v(1 - d, 1 - m, 1 - f);
            return [ 100 * (1 === x ? 0 : (1 - d - x) / (1 - x)), 100 * (1 === x ? 0 : (1 - m - x) / (1 - x)), 100 * (1 === x ? 0 : (1 - f - x) / (1 - x)), 100 * x ];
          }(d.h, d.s, d.v);
          return t.toString = i(t, t => "cmyk(".concat(t[0], "%, ").concat(t[1], "%, ").concat(t[2], "%, ").concat(t[3], "%)")), 
          t;
        },
        toHEXA() {
          const t = function(t, n, s) {
            return b(t, n, s).map(t => H(t).toString(16).padStart(2, "0"));
          }(d.h, d.s, d.v), n = d.a >= 1 ? "" : Number((255 * d.a).toFixed(0)).toString(16).toUpperCase().padStart(2, "0");
          return n && t.push(n), t.toString = () => "#".concat(t.join("").toUpperCase()), 
          t;
        },
        clone: () => k(d.h, d.s, d.v, d.a)
      };
      return d;
    }
    const S = t => Math.max(Math.min(t, 1), 0);
    function O(t) {
      const n = {
        options: Object.assign({
          lock: null,
          onchange: () => 0,
          onstop: () => 0
        }, t),
        _keyboard(t) {
          const {options: s} = n, {type: h, key: d} = t;
          if (document.activeElement === s.wrapper) {
            const {lock: s} = n.options, m = "ArrowUp" === d, f = "ArrowRight" === d, v = "ArrowDown" === d, x = "ArrowLeft" === d;
            if ("keydown" === h && (m || f || v || x)) {
              let h = 0, d = 0;
              "v" === s ? h = m || f ? 1 : -1 : "h" === s ? h = m || f ? -1 : 1 : (d = m ? -1 : v ? 1 : 0, 
              h = x ? -1 : f ? 1 : 0), n.update(S(n.cache.x + .01 * h), S(n.cache.y + .01 * d)), 
              t.preventDefault();
            } else d.startsWith("Arrow") && (n.options.onstop(), t.preventDefault());
          }
        },
        _tapstart(t) {
          d(document, [ "mouseup", "touchend", "touchcancel" ], n._tapstop), d(document, [ "mousemove", "touchmove" ], n._tapmove), 
          t.cancelable && t.preventDefault(), n._tapmove(t);
        },
        _tapmove(t) {
          const {options: s, cache: h} = n, {lock: d, element: m, wrapper: f} = s, v = f.getBoundingClientRect();
          let x = 0, C = 0;
          if (t) {
            const n = t && t.touches && t.touches[0];
            x = t ? (n || t).clientX : 0, C = t ? (n || t).clientY : 0, x < v.left ? x = v.left : x > v.left + v.width && (x = v.left + v.width), 
            C < v.top ? C = v.top : C > v.top + v.height && (C = v.top + v.height), x -= v.left, 
            C -= v.top;
          } else h && (x = h.x * v.width, C = h.y * v.height);
          "h" !== d && (m.style.left = "calc(".concat(x / v.width * 100, "% - ").concat(m.offsetWidth / 2, "px)")), 
          "v" !== d && (m.style.top = "calc(".concat(C / v.height * 100, "% - ").concat(m.offsetHeight / 2, "px)")), 
          n.cache = {
            x: x / v.width,
            y: C / v.height
          };
          const H = S(x / v.width), z = S(C / v.height);
          switch (d) {
           case "v":
            return s.onchange(H);

           case "h":
            return s.onchange(z);

           default:
            return s.onchange(H, z);
          }
        },
        _tapstop() {
          n.options.onstop(), m(document, [ "mouseup", "touchend", "touchcancel" ], n._tapstop), 
          m(document, [ "mousemove", "touchmove" ], n._tapmove);
        },
        trigger() {
          n._tapmove();
        },
        update(t = 0, s = 0) {
          const {left: h, top: d, width: m, height: f} = n.options.wrapper.getBoundingClientRect();
          "h" === n.options.lock && (s = t), n._tapmove({
            clientX: h + m * t,
            clientY: d + f * s
          });
        },
        destroy() {
          const {options: t, _tapstart: s, _keyboard: h} = n;
          m(document, [ "keydown", "keyup" ], h), m([ t.wrapper, t.element ], "mousedown", s), 
          m([ t.wrapper, t.element ], "touchstart", s, {
            passive: !1
          });
        }
      }, {options: s, _tapstart: h, _keyboard: f} = n;
      return d([ s.wrapper, s.element ], "mousedown", h), d([ s.wrapper, s.element ], "touchstart", h, {
        passive: !1
      }), d(document, [ "keydown", "keyup" ], f), n;
    }
    function E(t = {}) {
      t = Object.assign({
        onchange: () => 0,
        className: "",
        elements: []
      }, t);
      const n = d(t.elements, "click", n => {
        t.elements.forEach(s => s.classList[n.target === s ? "add" : "remove"](t.className)), 
        t.onchange(n), n.stopPropagation();
      });
      return {
        destroy: () => m(...n)
      };
    }
    /*! NanoPop 2.1.0 MIT | https://github.com/Simonwep/nanopop */    const z = {
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
    class V {
      constructor(t) {
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
        }), this.options = t = Object.assign({
          ...V.DEFAULT_OPTIONS
        }, t);
        const {swatches: n, components: s, theme: h, sliders: d, lockOpacity: m, padding: f} = t;
        [ "nano", "monolith" ].includes(h) && !d && (t.sliders = "h"), s.interaction || (s.interaction = {});
        const {preview: v, opacity: x, hue: C, palette: H} = s;
        s.opacity = !m && x, s.palette = H || v || x || C, this._preBuild(), this._buildComponents(), 
        this._bindEvents(), this._finalBuild(), n && n.length && n.forEach(t => this.addSwatch(t));
        const {button: B, app: R} = this._root;
        this._nanopop = ((t, n, s) => {
          const h = "object" != typeof t || t instanceof HTMLElement ? {
            reference: t,
            popper: n,
            ...s
          } : t;
          return {
            update(t = h) {
              const {reference: n, popper: s} = Object.assign(h, t);
              if (!s || !n) throw new Error("Popper- or reference-element missing.");
              return ((t, n, s) => {
                const {container: h, margin: d, position: m, variantFlipOrder: f, positionFlipOrder: v} = {
                  container: document.documentElement.getBoundingClientRect(),
                  ...z,
                  ...s
                }, {left: x, top: C} = n.style;
                n.style.left = "0", n.style.top = "0";
                const H = t.getBoundingClientRect(), V = n.getBoundingClientRect(), B = {
                  t: H.top - V.height - d,
                  b: H.bottom + d,
                  r: H.right + d,
                  l: H.left - V.width - d
                }, R = {
                  vs: H.left,
                  vm: H.left + H.width / 2 + -V.width / 2,
                  ve: H.left + H.width - V.width,
                  hs: H.top,
                  hm: H.bottom - H.height / 2 - V.height / 2,
                  he: H.bottom - V.height
                }, [M, L = "middle"] = m.split("-"), D = v[M], F = f[L], {top: P, left: Z, bottom: I, right: N} = h;
                for (const z of D) {
                  const t = "t" === z || "b" === z, s = B[z], [h, d] = t ? [ "top", "left" ] : [ "left", "top" ], [m, f] = t ? [ V.height, V.width ] : [ V.width, V.height ], [v, x] = t ? [ I, N ] : [ N, I ], [C, H] = t ? [ P, Z ] : [ Z, P ];
                  if (!(s < C || s + m > v)) for (const B of F) {
                    const m = R[(t ? "v" : "h") + B];
                    if (!(m < H || m + f > x)) return n.style[d] = m - V[d] + "px", n.style[h] = s - V[h] + "px", 
                    z + B;
                  }
                }
                return n.style.left = x, n.style.top = C, null;
              })(n, s, h);
            }
          };
        })(B, R, {
          margin: f
        }), B.setAttribute("role", "button"), B.setAttribute("aria-label", this._t("btn:toggle"));
        const M = this;
        requestAnimationFrame((function e() {
          if (!R.offsetWidth) return requestAnimationFrame(e);
          M.setColor(t.default), M._rePositioningPicker(), t.defaultRepresentation && (M._representation = t.defaultRepresentation, 
          M.setColorRepresentation(M._representation)), t.showAlways && M.show(), M._initializingActive = !1, 
          M._emit("init");
        }));
      }
      _preBuild() {
        const {options: t} = this;
        for (const n of [ "el", "container" ]) t[n] = p(t[n]);
        this._root = (t => {
          const {components: n, useAsButton: s, inline: h, appClass: d, theme: m, lockOpacity: f} = t.options, a = t => t ? "" : 'style="display:none" hidden', l = n => t._t(n), v = c('\n      <div :ref="root" class="pickr">\n\n        '.concat(s ? "" : '<button type="button" :ref="button" class="pcr-button"></button>', '\n\n        <div :ref="app" class="pcr-app ').concat(d || "", '" data-theme="').concat(m, '" ').concat(h ? 'style="position: unset"' : "", ' aria-label="').concat(l("ui:dialog"), '" role="window">\n          <div class="pcr-selection" ').concat(a(n.palette), '>\n            <div :obj="preview" class="pcr-color-preview" ').concat(a(n.preview), '>\n              <button type="button" :ref="lastColor" class="pcr-last-color" aria-label="').concat(l("btn:last-color"), '"></button>\n              <div :ref="currentColor" class="pcr-current-color"></div>\n            </div>\n\n            <div :obj="palette" class="pcr-color-palette">\n              <div :ref="picker" class="pcr-picker"></div>\n              <div :ref="palette" class="pcr-palette" tabindex="0" aria-label="').concat(l("aria:palette"), '" role="listbox"></div>\n            </div>\n\n            <div :obj="hue" class="pcr-color-chooser" ').concat(a(n.hue), '>\n              <div :ref="picker" class="pcr-picker"></div>\n              <div :ref="slider" class="pcr-hue pcr-slider" tabindex="0" aria-label="').concat(l("aria:hue"), '" role="slider"></div>\n            </div>\n\n            <div :obj="opacity" class="pcr-color-opacity" ').concat(a(n.opacity), '>\n              <div :ref="picker" class="pcr-picker"></div>\n              <div :ref="slider" class="pcr-opacity pcr-slider" tabindex="0" aria-label="').concat(l("aria:opacity"), '" role="slider"></div>\n            </div>\n          </div>\n\n          <div class="pcr-swatches ').concat(n.palette ? "" : "pcr-last", '" :ref="swatches"></div>\n\n          <div :obj="interaction" class="pcr-interaction" ').concat(a(Object.keys(n.interaction).length), '>\n            <input :ref="result" class="pcr-result" type="text" spellcheck="false" ').concat(a(n.interaction.input), ' aria-label="').concat(l("aria:input"), '">\n\n            <input :arr="options" class="pcr-type" data-type="HEXA" value="').concat(f ? "HEX" : "HEXA", '" type="button" ').concat(a(n.interaction.hex), '>\n            <input :arr="options" class="pcr-type" data-type="RGBA" value="').concat(f ? "RGB" : "RGBA", '" type="button" ').concat(a(n.interaction.rgba), '>\n            <input :arr="options" class="pcr-type" data-type="HSLA" value="').concat(f ? "HSL" : "HSLA", '" type="button" ').concat(a(n.interaction.hsla), '>\n            <input :arr="options" class="pcr-type" data-type="HSVA" value="').concat(f ? "HSV" : "HSVA", '" type="button" ').concat(a(n.interaction.hsva), '>\n            <input :arr="options" class="pcr-type" data-type="CMYK" value="CMYK" type="button" ').concat(a(n.interaction.cmyk), '>\n\n            <input :ref="save" class="pcr-save" value="').concat(l("btn:save"), '" type="button" ').concat(a(n.interaction.save), ' aria-label="').concat(l("aria:btn:save"), '">\n            <input :ref="cancel" class="pcr-cancel" value="').concat(l("btn:cancel"), '" type="button" ').concat(a(n.interaction.cancel), ' aria-label="').concat(l("aria:btn:cancel"), '">\n            <input :ref="clear" class="pcr-clear" value="').concat(l("btn:clear"), '" type="button" ').concat(a(n.interaction.clear), ' aria-label="').concat(l("aria:btn:clear"), '">\n          </div>\n        </div>\n      </div>\n    ')), x = v.interaction;
          return x.options.find(t => !t.hidden && !t.classList.add("active")), x.type = () => x.options.find(t => t.classList.contains("active")), 
          v;
        })(this), t.useAsButton && (this._root.button = t.el), t.container.appendChild(this._root.root);
      }
      _finalBuild() {
        const t = this.options, n = this._root;
        if (t.container.removeChild(n.root), t.inline) {
          const s = t.el.parentElement;
          t.el.nextSibling ? s.insertBefore(n.app, t.el.nextSibling) : s.appendChild(n.app);
        } else t.container.appendChild(n.app);
        t.useAsButton ? t.inline && t.el.remove() : t.el.parentNode.replaceChild(n.root, t.el), 
        t.disabled && this.disable(), t.comparison || (n.button.style.transition = "none", 
        t.useAsButton || (n.preview.lastColor.style.transition = "none")), this.hide();
      }
      _buildComponents() {
        const t = this, n = this.options.components, s = (t.options.sliders || "v").repeat(2), [h, d] = s.match(/^[vh]+$/g) ? s : [], r = () => this._color || (this._color = this._lastColor.clone()), m = {
          palette: O({
            element: t._root.palette.picker,
            wrapper: t._root.palette.palette,
            onstop: () => t._emit("changestop", t),
            onchange(s, h) {
              if (!n.palette) return;
              const d = r(), {_root: m, options: f} = t, {lastColor: v, currentColor: x} = m.preview;
              t._recalc && (d.s = 100 * s, d.v = 100 - 100 * h, d.v < 0 && (d.v = 0), t._updateOutput());
              const C = d.toRGBA().toString(0);
              this.element.style.background = C, this.wrapper.style.background = "\n                        linear-gradient(to top, rgba(0, 0, 0, ".concat(d.a, "), transparent),\n                        linear-gradient(to left, hsla(").concat(d.h, ", 100%, 50%, ").concat(d.a, "), rgba(255, 255, 255, ").concat(d.a, "))\n                    "), 
              f.comparison ? f.useAsButton || t._lastColor || (v.style.color = C) : (m.button.style.color = C, 
              m.button.classList.remove("clear"));
              const H = d.toHEXA().toString();
              for (const {el: n, color: z} of t._swatchColors) n.classList[H === z.toHEXA().toString() ? "add" : "remove"]("pcr-active");
              x.style.color = C;
            }
          }),
          hue: O({
            lock: "v" === d ? "h" : "v",
            element: t._root.hue.picker,
            wrapper: t._root.hue.slider,
            onstop: () => t._emit("changestop", t),
            onchange(s) {
              if (!n.hue || !n.palette) return;
              const h = r();
              t._recalc && (h.h = 360 * s), this.element.style.backgroundColor = "hsl(".concat(h.h, ", 100%, 50%)"), 
              m.palette.trigger();
            }
          }),
          opacity: O({
            lock: "v" === h ? "h" : "v",
            element: t._root.opacity.picker,
            wrapper: t._root.opacity.slider,
            onstop: () => t._emit("changestop", t),
            onchange(s) {
              if (!n.opacity || !n.palette) return;
              const h = r();
              t._recalc && (h.a = Math.round(100 * s) / 100), this.element.style.background = "rgba(0, 0, 0, ".concat(h.a, ")"), 
              m.palette.trigger();
            }
          }),
          selectable: E({
            elements: t._root.interaction.options,
            className: "active",
            onchange(n) {
              t._representation = n.target.getAttribute("data-type").toUpperCase(), t._recalc && t._updateOutput();
            }
          })
        };
        this._components = m;
      }
      _bindEvents() {
        const {_root: t, options: n} = this, s = [ d(t.interaction.clear, "click", () => this._clearColor()), d([ t.interaction.cancel, t.preview.lastColor ], "click", () => {
          this._emit("cancel", this), this.setHSVA(...(this._lastColor || this._color).toHSVA(), !0);
        }), d(t.interaction.save, "click", () => {
          !this.applyColor() && !n.showAlways && this.hide();
        }), d(t.interaction.result, [ "keyup", "input" ], t => {
          this.setColor(t.target.value, !0) && !this._initializingActive && this._emit("change", this._color), 
          t.stopImmediatePropagation();
        }), d(t.interaction.result, [ "focus", "blur" ], t => {
          this._recalc = "blur" === t.type, this._recalc && this._updateOutput();
        }), d([ t.palette.palette, t.palette.picker, t.hue.slider, t.hue.picker, t.opacity.slider, t.opacity.picker ], [ "mousedown", "touchstart" ], () => this._recalc = !0, {
          passive: !0
        }) ];
        if (!n.showAlways) {
          const h = n.closeWithKey;
          s.push(d(t.button, "click", () => this.isOpen() ? this.hide() : this.show()), d(document, "keyup", t => this.isOpen() && (t.key === h || t.code === h) && this.hide()), d(document, [ "touchstart", "mousedown" ], n => {
            this.isOpen() && !l(n).some(n => n === t.app || n === t.button) && this.hide();
          }, {
            capture: !0
          }));
        }
        if (n.adjustableNumbers) {
          const n = {
            rgba: [ 255, 255, 255, 1 ],
            hsva: [ 360, 100, 100, 1 ],
            hsla: [ 360, 100, 100, 1 ],
            cmyk: [ 100, 100, 100, 100 ]
          };
          u(t.interaction.result, (t, s, h) => {
            const d = n[this.getColorRepresentation().toLowerCase()];
            if (d) {
              const n = d[h], m = t + (n >= 100 ? 1e3 * s : s);
              return m <= 0 ? 0 : Number((m < n ? m : n).toPrecision(3));
            }
            return t;
          });
        }
        if (n.autoReposition && !n.inline) {
          let t = null;
          const h = this;
          s.push(d(window, [ "scroll", "resize" ], () => {
            h.isOpen() && (n.closeOnScroll && h.hide(), null === t ? (t = setTimeout(() => t = null, 100), 
            requestAnimationFrame((function e() {
              h._rePositioningPicker(), null !== t && requestAnimationFrame(e);
            }))) : (clearTimeout(t), t = setTimeout(() => t = null, 100)));
          }, {
            capture: !0
          }));
        }
        this._eventBindings = s;
      }
      _rePositioningPicker() {
        const {options: t} = this;
        if (!t.inline && !this._nanopop.update({
          container: document.body.getBoundingClientRect(),
          position: t.position
        })) {
          const t = this._root.app, n = t.getBoundingClientRect();
          t.style.top = "".concat((window.innerHeight - n.height) / 2, "px"), t.style.left = "".concat((window.innerWidth - n.width) / 2, "px");
        }
      }
      _updateOutput() {
        const {_root: t, _color: n, options: s} = this;
        if (t.interaction.type()) {
          const h = "to".concat(t.interaction.type().getAttribute("data-type"));
          t.interaction.result.value = "function" == typeof n[h] ? n[h]().toString(s.outputPrecision) : "";
        }
        !this._initializingActive && this._recalc && this._emit("change", n);
      }
      _clearColor(t = !1) {
        const {_root: n, options: s} = this;
        s.useAsButton || (n.button.style.color = "rgba(0, 0, 0, 0.15)"), n.button.classList.add("clear"), 
        s.showAlways || this.hide(), this._lastColor = null, this._initializingActive || t || (this._emit("save", null), 
        this._emit("clear", this));
      }
      _parseLocalColor(t) {
        const {values: n, type: s, a: h} = function(t) {
          t = t.match(/^[a-zA-Z]+$/) ? function(t) {
            if ("black" === t.toLowerCase()) return "#000";
            const n = document.createElement("canvas").getContext("2d");
            return n.fillStyle = t, "#000" === n.fillStyle ? null : n.fillStyle;
          }(t) : t;
          const n = {
            cmyk: /^cmyk[\D]+([\d.]+)[\D]+([\d.]+)[\D]+([\d.]+)[\D]+([\d.]+)/i,
            rgba: /^((rgba)|rgb)[\D]+([\d.]+)[\D]+([\d.]+)[\D]+([\d.]+)[\D]*?([\d.]+|$)/i,
            hsla: /^((hsla)|hsl)[\D]+([\d.]+)[\D]+([\d.]+)[\D]+([\d.]+)[\D]*?([\d.]+|$)/i,
            hsva: /^((hsva)|hsv)[\D]+([\d.]+)[\D]+([\d.]+)[\D]+([\d.]+)[\D]*?([\d.]+|$)/i,
            hexa: /^#?(([\dA-Fa-f]{3,4})|([\dA-Fa-f]{6})|([\dA-Fa-f]{8}))$/i
          }, o = t => t.map(t => /^(|\d+)\.\d+|\d+$/.test(t) ? Number(t) : void 0);
          let s;
          t: for (const h in n) {
            if (!(s = n[h].exec(t))) continue;
            const r = t => !!s[2] == ("number" == typeof t);
            switch (h) {
             case "cmyk":
              {
                const [, t, n, d, m] = o(s);
                if (t > 100 || n > 100 || d > 100 || m > 100) break t;
                return {
                  values: _(t, n, d, m),
                  type: h
                };
              }

             case "rgba":
              {
                const [, , , t, n, d, m] = o(s);
                if (t > 255 || n > 255 || d > 255 || m < 0 || m > 1 || !r(m)) break t;
                return {
                  values: [ ...y(t, n, d), m ],
                  a: m,
                  type: h
                };
              }

             case "hexa":
              {
                let [, t] = s;
                4 !== t.length && 3 !== t.length || (t = t.split("").map(t => t + t).join(""));
                const n = t.substring(0, 6);
                let d = t.substring(6);
                return d = d ? parseInt(d, 16) / 255 : void 0, {
                  values: [ ...A(n), d ],
                  a: d,
                  type: h
                };
              }

             case "hsla":
              {
                const [, , , t, n, d, m] = o(s);
                if (t > 360 || n > 100 || d > 100 || m < 0 || m > 1 || !r(m)) break t;
                return {
                  values: [ ...w(t, n, d), m ],
                  a: m,
                  type: h
                };
              }

             case "hsva":
              {
                const [, , , t, n, d, m] = o(s);
                if (t > 360 || n > 100 || d > 100 || m < 0 || m > 1 || !r(m)) break t;
                return {
                  values: [ t, n, d, m ],
                  a: m,
                  type: h
                };
              }
            }
          }
          return {
            values: null,
            type: null
          };
        }(t), {lockOpacity: d} = this.options, m = void 0 !== h && 1 !== h;
        return n && 3 === n.length && (n[3] = void 0), {
          values: !n || d && m ? null : n,
          type: s
        };
      }
      _t(t) {
        return this.options.i18n[t] || V.I18N_DEFAULTS[t];
      }
      _emit(t, ...n) {
        this._eventListener[t].forEach(t => t(...n, this));
      }
      on(t, n) {
        return this._eventListener[t].push(n), this;
      }
      off(t, n) {
        const s = this._eventListener[t] || [], h = s.indexOf(n);
        return ~h && s.splice(h, 1), this;
      }
      addSwatch(t) {
        const {values: n} = this._parseLocalColor(t);
        if (n) {
          const {_swatchColors: t, _root: s} = this, h = k(...n), m = a('<button type="button" style="color: '.concat(h.toRGBA().toString(0), '" aria-label="').concat(this._t("btn:swatch"), '"/>'));
          return s.swatches.appendChild(m), t.push({
            el: m,
            color: h
          }), this._eventBindings.push(d(m, "click", () => {
            this.setHSVA(...h.toHSVA(), !0), this._emit("swatchselect", h), this._emit("change", h);
          })), !0;
        }
        return !1;
      }
      removeSwatch(t) {
        const n = this._swatchColors[t];
        if (n) {
          const {el: s} = n;
          return this._root.swatches.removeChild(s), this._swatchColors.splice(t, 1), !0;
        }
        return !1;
      }
      applyColor(t = !1) {
        const {preview: n, button: s} = this._root, h = this._color.toRGBA().toString(0);
        return n.lastColor.style.color = h, this.options.useAsButton || (s.style.color = h), 
        s.classList.remove("clear"), this._lastColor = this._color.clone(), this._initializingActive || t || this._emit("save", this._color), 
        this;
      }
      destroy() {
        this._eventBindings.forEach(t => m(...t)), Object.keys(this._components).forEach(t => this._components[t].destroy());
      }
      destroyAndRemove() {
        this.destroy();
        const {root: t, app: n} = this._root;
        t.parentElement && t.parentElement.removeChild(t), n.parentElement.removeChild(n), 
        Object.keys(this).forEach(t => this[t] = null);
      }
      hide() {
        return this._root.app.classList.remove("visible"), this._emit("hide", this), this;
      }
      show() {
        return this.options.disabled || (this._root.app.classList.add("visible"), this._rePositioningPicker(), 
        this._emit("show", this)), this;
      }
      isOpen() {
        return this._root.app.classList.contains("visible");
      }
      setHSVA(t = 360, n = 0, s = 0, h = 1, d = !1) {
        const m = this._recalc;
        if (this._recalc = !1, t < 0 || t > 360 || n < 0 || n > 100 || s < 0 || s > 100 || h < 0 || h > 1) return !1;
        this._color = k(t, n, s, h);
        const {hue: f, opacity: v, palette: x} = this._components;
        return f.update(t / 360), v.update(h), x.update(n / 100, 1 - s / 100), d || this.applyColor(), 
        m && this._updateOutput(), this._recalc = m, !0;
      }
      setColor(t, n = !1) {
        if (null === t) return this._clearColor(n), !0;
        const {values: s, type: h} = this._parseLocalColor(t);
        if (s) {
          const t = h.toUpperCase(), {options: d} = this._root.interaction, m = d.find(n => n.getAttribute("data-type") === t);
          if (m && !m.hidden) for (const n of d) n.classList[n === m ? "add" : "remove"]("active");
          return !!this.setHSVA(...s, n) && this.setColorRepresentation(t);
        }
        return !1;
      }
      setColorRepresentation(t) {
        return t = t.toUpperCase(), !!this._root.interaction.options.find(n => n.getAttribute("data-type").startsWith(t) && !n.click());
      }
      getColorRepresentation() {
        return this._representation;
      }
      getColor() {
        return this._color;
      }
      getSelectedColor() {
        return this._lastColor;
      }
      getRoot() {
        return this._root;
      }
      disable() {
        return this.hide(), this.options.disabled = !0, this._root.button.classList.add("disabled"), 
        this;
      }
      enable() {
        return this.options.disabled = !1, this._root.button.classList.remove("disabled"), 
        this;
      }
    }
    j(V, "utils", h), j(V, "version", f.a), j(V, "I18N_DEFAULTS", {
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
    }), j(V, "DEFAULT_OPTIONS", {
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
    }), j(V, "create", t => new V(t)), n.default = V;
  } ]).default;
}))), x = class {
  constructor(s) {
    t(this, s), this.save = n(this, "save", 7), this.nameInputEvent = n(this, "nameInputEvent", 7), 
    this.change = n(this, "change", 7), 
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
  componentDidLoad() {
    //Detect color representation
    this.value.includes("rgb") ? this.colorRepresentation = "RGBA" : this.value.includes("#") && (this.colorRepresentation = "HEXA");
    const t = this.element.shadowRoot.querySelector(".color-picker"), n = this.element.shadowRoot.querySelector(".color-picker-main-container");
    this.pickr = new v({
      el: t,
      theme: "nano",
      container: n,
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
    }), this.pickr.on("change", t => {
      this.colorObject = t, "HEXA" === this.colorRepresentation ? this.value = this.colorObject.toHEXA().toString() : "RGBA" === this.colorRepresentation && (this.value = this.colorObject.toRGBA().toString(0)), 
      this.change.emit(this.value);
    }), this.pickr.on("show", () => {
      this.colorObject = this.pickr.getColor();
    });
    const s = {
      root: document.querySelector("body"),
      rootMargin: "0px",
      threshold: 1
    };
    new IntersectionObserver(() => {
      this.pickr.setColor(this.value);
 //We have to set the color by force, because we need to get the color at this time, and pickr seems to defer it.
        }, s).observe(this.element);
  }
  componentDidUnload() {
    this.pickr.destroy();
  }
  watchHandler(t) {
    this.pickr.setColor(t);
  }
  //Button Methods
  handleHexaButtonClick() {
    this.colorChangedFromInput = !1, this.colorRepresentation = "HEXA", this.value = this.colorObject.toHEXA().toString(), 
    this.change.emit(this.value);
  }
  handleRgbaButtonClick() {
    this.colorChangedFromInput = !1, this.colorRepresentation = "RGBA", this.value = this.colorObject.toRGBA().toString(0), 
    this.change.emit(this.value);
  }
  handleTitleValueChange(t) {
    const n = t.target;
    this.label = n.value;
  }
  handleColorValueChange(t) {
    this.colorChangedFromInput = !0;
    const n = t.target;
    this.colorInputValue = n.value, this.pickr.setColor(n.value);
  }
  colorValue() {
    if (!this.colorChangedFromInput) {
      //We only want to update the color value on the input if the pick was changed directly by handling the color picker window, not by changing the input color value
      if (void 0 === this.colorObject) return "";
      if ("HEXA" === this.colorRepresentation) return this.colorObject.toHEXA().toString();
      if ("RGBA" === this.colorRepresentation) return this.colorObject.toRGBA().toString(0);
    }
    return this.colorChangedFromInput = !0, this.colorInputValue;
  }
  setActiveButton() {
    return this.value.includes("rgb") ? "rgba" : "hexa";
  }
  render() {
    return s(h, null, s("h1", {
      class: "label"
    }, this.label), s("div", {
      class: {
        "color-picker-main-container": !0
      },
      id: "color-picker-main-container"
    }, s("div", {
      class: "color-picker"
    }), s("div", {
      class: "cp-gxg-buttons before-color-value",
      slot: "editable"
    }, s("gxg-button-group", {
      "default-selected-btn-id": this.setActiveButton()
    }, s("button", {
      id: "rgba",
      onClick: this.handleRgbaButtonClick.bind(this)
    }, "RGBA"), s("button", {
      id: "hexa",
      onClick: this.handleHexaButtonClick.bind(this)
    }, "HEXA"))), s("input", {
      type: "text",
      name: "cp-color-value",
      id: "cp-color-value",
      value: this.colorValue(),
      class: "color-picker-main-container-textbox",
      onInput: this.handleColorValueChange.bind(this)
    })));
  }
  get element() {
    return d(this);
  }
  static get watchers() {
    return {
      value: [ "watchHandler" ]
    };
  }
};

x.style = '/*! Pickr 1.7.4 MIT | https://github.com/Simonwep/pickr */.pickr{position:relative;overflow:visible;-webkit-transform:translateY(0);transform:translateY(0)}.pickr *{-webkit-box-sizing:border-box;box-sizing:border-box;outline:none;border:none;-webkit-appearance:none}.pickr .pcr-button{position:relative;height:2em;width:2em;padding:.5em;cursor:pointer;font-family:-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Helvetica Neue,Arial,sans-serif;border-radius:.15em;background:url(\'data:image/svg+xml;utf8, <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 50 50" stroke="%2342445A" stroke-width="5px" stroke-linecap="round"><path d="M45,45L5,5"></path><path d="M45,5L5,45"></path></svg>\') no-repeat 50%;background-size:0;-webkit-transition:all .3s;transition:all .3s}.pickr .pcr-button:before{background:url(\'data:image/svg+xml;utf8, <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 2 2"><path fill="white" d="M1,0H2V1H1V0ZM0,1H1V2H0V1Z"/><path fill="gray" d="M0,0H1V1H0V0ZM1,1H2V2H1V1Z"/></svg>\');background-size:.5em;z-index:-1;z-index:auto}.pickr .pcr-button:after,.pickr .pcr-button:before{position:absolute;content:"";top:0;left:0;width:100%;height:100%;border-radius:.15em}.pickr .pcr-button:after{-webkit-transition:background .3s;transition:background .3s;background:currentColor}.pickr .pcr-button.clear{background-size:70%}.pickr .pcr-button.clear:before{opacity:0}.pickr .pcr-button.clear:focus{-webkit-box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px currentColor;box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px currentColor}.pickr .pcr-button.disabled{cursor:not-allowed}.pcr-app *,.pickr *{-webkit-box-sizing:border-box;box-sizing:border-box;outline:none;border:none;-webkit-appearance:none}.pcr-app button.pcr-active,.pcr-app button:focus,.pcr-app input.pcr-active,.pcr-app input:focus,.pickr button.pcr-active,.pickr button:focus,.pickr input.pcr-active,.pickr input:focus{-webkit-box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px currentColor;box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px currentColor}.pcr-app .pcr-palette,.pcr-app .pcr-slider,.pickr .pcr-palette,.pickr .pcr-slider{-webkit-transition:-webkit-box-shadow .3s;transition:-webkit-box-shadow .3s;transition:box-shadow .3s;transition:box-shadow .3s, -webkit-box-shadow .3s}.pcr-app .pcr-palette:focus,.pcr-app .pcr-slider:focus,.pickr .pcr-palette:focus,.pickr .pcr-slider:focus{-webkit-box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px rgba(0,0,0,.25);box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px rgba(0,0,0,.25)}.pcr-app{position:fixed;display:-ms-flexbox;display:flex;-ms-flex-direction:column;flex-direction:column;z-index:10000;border-radius:.1em;background:#fff;opacity:0;visibility:hidden;-webkit-transition:opacity .3s,visibility 0s .3s;transition:opacity .3s,visibility 0s .3s;font-family:-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Helvetica Neue,Arial,sans-serif;-webkit-box-shadow:0 .15em 1.5em 0 rgba(0,0,0,.1),0 0 1em 0 rgba(0,0,0,.03);box-shadow:0 .15em 1.5em 0 rgba(0,0,0,.1),0 0 1em 0 rgba(0,0,0,.03);left:0;top:0}.pcr-app.visible{-webkit-transition:opacity .3s;transition:opacity .3s;visibility:visible;opacity:1}.pcr-app .pcr-swatches{display:-ms-flexbox;display:flex;-ms-flex-wrap:wrap;flex-wrap:wrap;margin-top:.75em}.pcr-app .pcr-swatches.pcr-last{margin:0}@supports (display:grid){.pcr-app .pcr-swatches{display:grid;-ms-flex-align:center;align-items:center;grid-template-columns:repeat(auto-fit,1.75em)}}.pcr-app .pcr-swatches>button{font-size:1em;position:relative;width:calc(1.75em - 5px);height:calc(1.75em - 5px);border-radius:.15em;cursor:pointer;margin:2.5px;-ms-flex-negative:0;flex-shrink:0;justify-self:center;-webkit-transition:all .15s;transition:all .15s;overflow:hidden;background:transparent;z-index:1}.pcr-app .pcr-swatches>button:before{position:absolute;content:"";top:0;left:0;width:100%;height:100%;background:url(\'data:image/svg+xml;utf8, <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 2 2"><path fill="white" d="M1,0H2V1H1V0ZM0,1H1V2H0V1Z"/><path fill="gray" d="M0,0H1V1H0V0ZM1,1H2V2H1V1Z"/></svg>\');background-size:6px;border-radius:.15em;z-index:-1}.pcr-app .pcr-swatches>button:after{content:"";position:absolute;top:0;left:0;width:100%;height:100%;background:currentColor;border:1px solid rgba(0,0,0,.05);border-radius:.15em;-webkit-box-sizing:border-box;box-sizing:border-box}.pcr-app .pcr-swatches>button:hover{-webkit-filter:brightness(1.05);filter:brightness(1.05)}.pcr-app .pcr-swatches>button:not(.pcr-active){-webkit-box-shadow:none;box-shadow:none}.pcr-app .pcr-interaction{display:-ms-flexbox;display:flex;-ms-flex-wrap:wrap;flex-wrap:wrap;-ms-flex-align:center;align-items:center;margin:0 -.2em}.pcr-app .pcr-interaction>*{margin:0 .2em}.pcr-app .pcr-interaction input{letter-spacing:.07em;font-size:.75em;text-align:center;cursor:pointer;color:#75797e;background:#f1f3f4;border-radius:.15em;-webkit-transition:all .15s;transition:all .15s;padding:.45em .5em;margin-top:.75em}.pcr-app .pcr-interaction input:hover{-webkit-filter:brightness(.975);filter:brightness(.975)}.pcr-app .pcr-interaction input:focus{-webkit-box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px rgba(66,133,244,.75);box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px rgba(66,133,244,.75)}.pcr-app .pcr-interaction .pcr-result{color:#75797e;text-align:left;-ms-flex:1 1 8em;flex:1 1 8em;min-width:8em;-webkit-transition:all .2s;transition:all .2s;border-radius:.15em;background:#f1f3f4;cursor:text}.pcr-app .pcr-interaction .pcr-result::-moz-selection{background:#4285f4;color:#fff}.pcr-app .pcr-interaction .pcr-result::selection{background:#4285f4;color:#fff}.pcr-app .pcr-interaction .pcr-type.active{color:#fff;background:#4285f4}.pcr-app .pcr-interaction .pcr-cancel,.pcr-app .pcr-interaction .pcr-clear,.pcr-app .pcr-interaction .pcr-save{width:auto;color:#fff}.pcr-app .pcr-interaction .pcr-cancel:hover,.pcr-app .pcr-interaction .pcr-clear:hover,.pcr-app .pcr-interaction .pcr-save:hover{-webkit-filter:brightness(.925);filter:brightness(.925)}.pcr-app .pcr-interaction .pcr-save{background:#4285f4}.pcr-app .pcr-interaction .pcr-cancel,.pcr-app .pcr-interaction .pcr-clear{background:#f44250}.pcr-app .pcr-interaction .pcr-cancel:focus,.pcr-app .pcr-interaction .pcr-clear:focus{-webkit-box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px rgba(244,66,80,.75);box-shadow:0 0 0 1px hsla(0,0%,100%,.85),0 0 0 3px rgba(244,66,80,.75)}.pcr-app .pcr-selection .pcr-picker{position:absolute;height:18px;width:18px;border:2px solid #fff;border-radius:100%;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none}.pcr-app .pcr-selection .pcr-color-chooser,.pcr-app .pcr-selection .pcr-color-opacity,.pcr-app .pcr-selection .pcr-color-palette{position:relative;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;display:-ms-flexbox;display:flex;-ms-flex-direction:column;flex-direction:column;cursor:grab;cursor:-webkit-grab}.pcr-app .pcr-selection .pcr-color-chooser:active,.pcr-app .pcr-selection .pcr-color-opacity:active,.pcr-app .pcr-selection .pcr-color-palette:active{cursor:grabbing;cursor:-webkit-grabbing}.pcr-app[data-theme=nano]{width:14.25em;max-width:95vw}.pcr-app[data-theme=nano] .pcr-swatches{margin-top:.6em;padding:0 .6em}.pcr-app[data-theme=nano] .pcr-interaction{padding:0 .6em .6em}.pcr-app[data-theme=nano] .pcr-selection{display:grid;grid-gap:.6em;grid-template-columns:1fr 4fr;grid-template-rows:5fr auto auto;-ms-flex-align:center;align-items:center;height:10.5em;width:100%;align-self:flex-start}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-preview{grid-area:2/1/4/1;height:100%;width:100%;display:-ms-flexbox;display:flex;-ms-flex-direction:row;flex-direction:row;-ms-flex-pack:center;justify-content:center;margin-left:.6em}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-preview .pcr-last-color{display:none}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-preview .pcr-current-color{position:relative;background:currentColor;width:2em;height:2em;border-radius:50em;overflow:hidden}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-preview .pcr-current-color:before{position:absolute;content:"";top:0;left:0;width:100%;height:100%;background:url(\'data:image/svg+xml;utf8, <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 2 2"><path fill="white" d="M1,0H2V1H1V0ZM0,1H1V2H0V1Z"/><path fill="gray" d="M0,0H1V1H0V0ZM1,1H2V2H1V1Z"/></svg>\');background-size:.5em;border-radius:.15em;z-index:-1}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-palette{grid-area:1/1/2/3;width:100%;height:100%;z-index:1}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-palette .pcr-palette{border-radius:.15em;width:100%;height:100%}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-palette .pcr-palette:before{position:absolute;content:"";top:0;left:0;width:100%;height:100%;background:url(\'data:image/svg+xml;utf8, <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 2 2"><path fill="white" d="M1,0H2V1H1V0ZM0,1H1V2H0V1Z"/><path fill="gray" d="M0,0H1V1H0V0ZM1,1H2V2H1V1Z"/></svg>\');background-size:.5em;border-radius:.15em;z-index:-1}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-chooser{grid-area:2/2/2/2}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-opacity{grid-area:3/2/3/2}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-chooser,.pcr-app[data-theme=nano] .pcr-selection .pcr-color-opacity{height:.5em;margin:0 .6em}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-chooser .pcr-picker,.pcr-app[data-theme=nano] .pcr-selection .pcr-color-opacity .pcr-picker{top:50%;-webkit-transform:translateY(-50%);transform:translateY(-50%)}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-chooser .pcr-slider,.pcr-app[data-theme=nano] .pcr-selection .pcr-color-opacity .pcr-slider{-ms-flex-positive:1;flex-grow:1;border-radius:50em}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-chooser .pcr-slider{background:-webkit-gradient(linear,left top, right top,from(red),color-stop(#ff0),color-stop(#0f0),color-stop(#0ff),color-stop(#00f),color-stop(#f0f),to(red));background:linear-gradient(90deg,red,#ff0,#0f0,#0ff,#00f,#f0f,red)}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-opacity .pcr-slider{background:-webkit-gradient(linear,left top, right top,from(transparent),to(#000)),url(\'data:image/svg+xml;utf8, <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 2 2"><path fill="white" d="M1,0H2V1H1V0ZM0,1H1V2H0V1Z"/><path fill="gray" d="M0,0H1V1H0V0ZM1,1H2V2H1V1Z"/></svg>\');background:linear-gradient(90deg,transparent,#000),url(\'data:image/svg+xml;utf8, <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 2 2"><path fill="white" d="M1,0H2V1H1V0ZM0,1H1V2H0V1Z"/><path fill="gray" d="M0,0H1V1H0V0ZM1,1H2V2H1V1Z"/></svg>\');background-size:100%,.25em}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{width:230px;display:block}.label{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;display:-ms-flexbox;display:flex;margin-bottom:var(--spacing-lay-xs)}.color-picker-main-container{-webkit-box-shadow:var(--box-shadow-01);box-shadow:var(--box-shadow-01);z-index:10;--cardAnimationDelay:0;position:relative;-webkit-animation-name:slideUp;animation-name:slideUp;-webkit-animation-duration:0.75s;animation-duration:0.75s;-webkit-animation-iteration-count:1;animation-iteration-count:1;-webkit-animation-delay:var(--cardAnimationDelay);animation-delay:var(--cardAnimationDelay);background:var(--color-background);position:relative;margin-top:-1px;-webkit-transition:max-height 0.5s ease-in-out;transition:max-height 0.5s ease-in-out;padding:var(--spacing-comp-02) var(--card-padding) 0 var(--card-padding)}@-webkit-keyframes slideUp{0%{top:5px;opacity:0}100%{top:0px;opacity:100%}}@keyframes slideUp{0%{top:5px;opacity:0}100%{top:0px;opacity:100%}}.color-picker-main-container-textbox{font-family:var(--font-family-primary);font-size:var(--font-size-md);color:var(--color--black);padding-left:var(--spacing-comp-02);padding-right:var(--spacing-comp-02);padding-top:var(--spacing-comp-01);padding-bottom:var(--spacing-comp-01);background:var(--color-background);border-width:var(--border-width-sm);border-style:var(--border-style-regular);border-color:var(--gray-04);letter-spacing:0.018em;height:var(--spacing-comp-05);border-radius:0;width:100%;margin-bottom:var(--spacing-comp-02);-webkit-box-sizing:border-box;box-sizing:border-box}.color-picker-main-container--visible{max-height:400px}.pcr-app{background:var(--color-background)}.pcr-app[data-theme=nano] .pcr-selection .pcr-color-palette .pcr-palette{border-radius:0}.pickr{display:none}.pcr-app{max-width:100%;width:100% !important;-webkit-box-shadow:none;box-shadow:none}.cp-gxg-buttons{padding:var(--spacing-comp-02)}.color-picker-main-container-textbox{margin:0 var(--spacing-comp-02);width:calc(100% - 16px);margin-bottom:var(--spacing-comp-02)}.cp-gxg-buttons.before-color-value gxg-button{margin-right:var(--spacing-comp-02)}.cp-gxg-buttons.after-color-value{text-align:right}.pcr-color-chooser,.pcr-color-opacity{width:85%}';

export { x as gxg_color_picker }