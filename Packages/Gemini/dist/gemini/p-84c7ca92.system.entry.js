var __awaiter = this && this.__awaiter || function(t, e, o, r) {
  return new (o || (o = Promise))((function(n, i) {
    function fulfilled(t) {
      try {
        step(r.next(t));
      } catch (e) {
        i(e);
      }
    }
    function rejected(t) {
      try {
        step(r.throw(t));
      } catch (e) {
        i(e);
      }
    }
    function step(t) {
      var e;
      t.done ? n(t.value) : (e = t.value, e instanceof o ? e : new o((function(t) {
        t(e);
      }))).then(fulfilled, rejected);
    }
    step((r = r.apply(t, e || [])).next());
  }));
}, __generator = this && this.__generator || function(t, e) {
  var o, r, n, i, a = {
    label: 0,
    sent: function() {
      if (1 & n[0]) throw n[1];
      return n[1];
    },
    trys: [],
    ops: []
  };
  return i = {
    next: verb(0),
    throw: verb(1),
    return: verb(2)
  }, "function" == typeof Symbol && (i[Symbol.iterator] = function() {
    return this;
  }), i;
  function verb(i) {
    return function(l) {
      return function(i) {
        if (o) throw new TypeError("Generator is already executing.");
        for (;a; ) try {
          if (o = 1, r && (n = 2 & i[0] ? r.return : i[0] ? r.throw || ((n = r.return) && n.call(r), 
          0) : r.next) && !(n = n.call(r, i[1])).done) return n;
          switch (r = 0, n && (i = [ 2 & i[0], n.value ]), i[0]) {
           case 0:
           case 1:
            n = i;
            break;

           case 4:
            return a.label++, {
              value: i[1],
              done: !1
            };

           case 5:
            a.label++, r = i[1], i = [ 0 ];
            continue;

           case 7:
            i = a.ops.pop(), a.trys.pop();
            continue;

           default:
            if (!(n = a.trys, (n = n.length > 0 && n[n.length - 1]) || 6 !== i[0] && 2 !== i[0])) {
              a = 0;
              continue;
            }
            if (3 === i[0] && (!n || i[1] > n[0] && i[1] < n[3])) {
              a.label = i[1];
              break;
            }
            if (6 === i[0] && a.label < n[1]) {
              a.label = n[1], n = i;
              break;
            }
            if (n && a.label < n[2]) {
              a.label = n[2], a.ops.push(i);
              break;
            }
            n[2] && a.ops.pop(), a.trys.pop();
            continue;
          }
          i = e.call(t, a);
        } catch (l) {
          i = [ 6, l ], r = 0;
        } finally {
          o = n = 0;
        }
        if (5 & i[0]) throw i[1];
        return {
          value: i[0] ? i[1] : void 0,
          done: !0
        };
      }([ i, l ]);
    };
  }
};

System.register([ "./p-d8a20c31.system.js" ], (function(t) {
  "use strict";
  var e, o, r, n, i;
  return {
    setters: [ function(t) {
      e = t.r, o = t.h, r = t.g, n = t.e, i = t.H;
    } ],
    execute: function() {
      var a = new Map, l = new Map;
      t("ch_icon", /** @class */ function() {
        function class_1(t) {
          e(this, t), 
          /*********************************
                    PROPERTIES & STATE
                    *********************************/
          /**
                     * If enabled, the icon will be loaded lazily when it's visible in the viewport.
                     */
          this.lazy = !1, 
          /**
                     * If enabled, the icon will display its inherent/natural color
                     */
          this.autoColor = !1, 
          /**
                     * The URL of the icon.
                     */
          this.src = "", this.isVisible = !1;
        }
        /*********************************
                METHODS
                *********************************/        return class_1.prototype.connectedCallback = function() {
          var t = this;
          // purposely do not return the promise here because loading
          // the svg file should not hold up loading the app
          // only load the svg if it's visible
                    this.waitUntilVisible(this.element, "50px", (function() {
            t.isVisible = !0, t.getIcon();
          }));
        }, class_1.prototype.disconnectedCallback = function() {
          void 0 !== this.io && (this.io.disconnect(), this.io = void 0);
        }, class_1.prototype.waitUntilVisible = function(t, e, o) {
          var r = this;
          if (this.lazy && "undefined" != typeof window && window.IntersectionObserver) {
            var n = this.io = new window.IntersectionObserver((function(t) {
              t[0].isIntersecting && (n.disconnect(), r.io = void 0, o());
            }), {
              rootMargin: e
            });
            n.observe(t);
          } else 
          // browser doesn't support IntersectionObserver
          // so just fallback to always show it
          o();
        }, class_1.prototype.getIcon = function() {
          return __awaiter(this, void 0, void 0, (function() {
            var t;
            return __generator(this, (function(e) {
              switch (e.label) {
               case 0:
                return this.isVisible ? this.src ? a.has(this.src) ? (this.svgContent = a.get(this.src), 
                [ 3 /*break*/ , 3 ]) : [ 3 /*break*/ , 1 ] : [ 3 /*break*/ , 4 ] : [ 3 /*break*/ , 5 ];

               case 1:
                return t = this, [ 4 /*yield*/ , (o = this.src, r = l.get(o), r || (
                // we don't already have a request
                r = fetch(o).then((function(t) {
                  if (t.ok) return t.text().then((function(t) {
                    return a.set(o, t), t;
                  }));
                  a.set(o, "");
                })), 
                // cache for the same requests
                l.set(o, r)), r) ];

               case 2:
                t.svgContent = e.sent(), e.label = 3;

               case 3:
                return [ 3 /*break*/ , 5 ];

               case 4:
                return this.svgContent = "", [ 2 /*return*/ ];

               case 5:
                return [ 2 /*return*/ ];
              }
              var o, r;
            }));
          }));
        }, class_1.prototype.render = function() {
          return o("div", {
            innerHTML: this.svgContent
          });
        }, Object.defineProperty(class_1, "assetsDirs", {
          get: function() {
            return [ "ch-icon-assets" ];
          },
          enumerable: !1,
          configurable: !0
        }), Object.defineProperty(class_1.prototype, "element", {
          get: function() {
            return r(this);
          },
          enumerable: !1,
          configurable: !0
        }), Object.defineProperty(class_1, "watchers", {
          get: function() {
            return {
              src: [ "getIcon" ]
            };
          },
          enumerable: !1,
          configurable: !0
        }), class_1;
      }()).style = ":host(:not([auto-color])){}:host(:not([auto-color])) svg *{fill:var(--icon-color) !important}:host(:not([auto-color])) path.icons01{fill:var(--icons01-enabled)}:host(:not([auto-color])) path.icons02{fill:var(--icons02-enabled)}:host(:not([auto-color])) path.icons03{fill:var(--icons03-enabled)}:host(:not([auto-color])) path.icons04{fill:var(--icons04-enabled)}:host(:not([auto-color])) path.icons05{fill:var(--icons05-enabled)}:host(:not([auto-color])) path.icons06{fill:var(--icons06-enabled)}:host(:not([auto-color])) path.icons07{fill:var(--icons07-enabled)}:host(:not([auto-color])) path.icons08{fill:var(--icons08-enabled)}:host(:not([auto-color])) path.icons09{fill:var(--icons09-enabled)}:host{display:-ms-inline-flexbox;display:inline-flex;line-height:0}:host svg{width:var(--icon-size);height:var(--icon-size)}";
      var s = {
        alwaysblack: "color-always-black",
        disabled: "color-primary-disabled",
        ondisabled: "color-on-disabled",
        error: "color-error-dark",
        negative: "color-on-primary",
        onbackground: "color-on-background",
        "primary-enabled": "color-primary-enabled",
        "primary-active": "color-primary-active",
        "primary-hover": "color-primary-hover",
        success: "color-success-dark",
        warning: "color-warning-dark"
      };
      /*********************************
            CONSTANTS
            *********************************/      t("gxg_icon", /** @class */ function() {
        function class_2(t) {
          e(this, t), 
          /**
                     * The size of the icon. Possible values: regular, small.
                     */
          this.size = "regular";
        }
        /*********************************
                METHODS
                *********************************/        return class_2.prototype.getSrcPath = function() {
          return n("icon-assets/" + this.type + ".svg");
        }, class_2.prototype.iconSize = function() {
          return "regular" === this.size ? "16px" : "small" === this.size ? "12px" : void 0;
        }, class_2.prototype.render = function() {
          return o(i, null, o("ch-icon", {
            style: {
              "--icon-color": this.mapColorToCssVar(s[this.color]),
              "--icon-size": this.iconSize()
            },
            "auto-color": "auto" === this.color,
            src: this.getSrcPath()
          }));
        }, class_2.prototype.mapColorToCssVar = function(t) {
          return t ? "var(--" + t + ")" : "var(--color-on-background)";
        }, Object.defineProperty(class_2, "assetsDirs", {
          get: function() {
            return [ "icon-assets" ];
          },
          enumerable: !1,
          configurable: !0
        }), Object.defineProperty(class_2.prototype, "element", {
          get: function() {
            return r(this);
          },
          enumerable: !1,
          configurable: !0
        }), class_2;
      }()).style = "/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{display:inline-block;line-height:0;height:20px;width:20px;display:-ms-inline-flexbox;display:inline-flex;-ms-flex-align:center;align-items:center;-ms-flex-pack:center;justify-content:center}";
    }
  };
}));