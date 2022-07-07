System.register([ "./p-d8a20c31.system.js" ], (function(t) {
  "use strict";
  var i, e, o;
  return {
    setters: [ function(t) {
      i = t.r, e = t.h, o = t.H;
    } ],
    execute: function() {
      t("gxg_loader", /** @class */ function() {
        function class_1(t) {
          i(this, t), 
          /**
                     * The prescence of this attribute shows the loader
                     */
          this.show = !1, 
          /**
                     * The z-index positive value you want for the loader when visible (default: 100)
                     */
          this.visibleZIndex = "100", this.layerOpacity100 = !1, this.squaresOpacity100 = !1, 
          this.textOpacity100 = !1, this.sendLayerBack = !0;
        }
        return class_1.prototype.showHandler = function() {
          this.show ? (this.sendLayerBack = !1, setTimeout(function() {
            this.layerOpacity100 = !0, setTimeout(function() {
              this.squaresOpacity100 = !0, setTimeout(function() {
                this.textOpacity100 = !0;
              }.bind(this), 250);
            }.bind(this), 250);
          }.bind(this), 250)) : setTimeout(function() {
            this.textOpacity100 = !1, setTimeout(function() {
              this.squaresOpacity100 = !1, setTimeout(function() {
                this.layerOpacity100 = !1, setTimeout(function() {
                  this.sendLayerBack = !0;
                }.bind(this), 250);
              }.bind(this), 250);
            }.bind(this), 250);
          }.bind(this), 250);
        }, class_1.prototype.render = function() {
          return e(o, {
            style: {
              zIndex: this.visibleZIndex
            },
            class: {
              sendLayerBack: this.sendLayerBack,
              layerOpacity100: this.layerOpacity100,
              squaresOpacity100: this.squaresOpacity100,
              textOpacity100: this.textOpacity100
            }
          }, e("div", {
            class: "layer"
          }, e("div", {
            class: "loader"
          }, e("div", {
            class: "box"
          }), e("div", {
            class: "box"
          }), e("div", {
            class: "box"
          }), e("div", {
            class: "box"
          })), void 0 !== this.text ? e("span", {
            class: "loader-text"
          }, this.text, e("span", {
            class: "dot1"
          }, "."), e("span", {
            class: "dot2"
          }, "."), e("span", {
            class: "dot3"
          }, ".")) : null));
        }, Object.defineProperty(class_1, "watchers", {
          get: function() {
            return {
              show: [ "showHandler" ]
            };
          },
          enumerable: !1,
          configurable: !0
        }), class_1;
      }()).style = "/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{position:fixed;width:100%;height:100%;opacity:1;-webkit-transition-property:opacity;transition-property:opacity;-webkit-transition-duration:var(--ui-animaton-speed);transition-duration:var(--ui-animaton-speed);-webkit-transition-timing-function:ease;transition-timing-function:ease;left:50%;top:50%;-webkit-transform:translate(-50%, -50%);transform:translate(-50%, -50%);display:-ms-flexbox;display:flex;-ms-flex-direction:column;flex-direction:column;-ms-flex-align:center;align-items:center}:host .loader{opacity:0;-webkit-transition-property:opacity;transition-property:opacity;-webkit-transition-duration:var(--ui-animaton-speed);transition-duration:var(--ui-animaton-speed);-webkit-transition-timing-function:ease;transition-timing-function:ease;position:fixed;display:block;width:60px;height:60px;-webkit-transform:translateY(-50%) translateX(-50%);transform:translateY(-50%) translateX(-50%);top:50%;left:50%}:host .box{width:45%;height:45%;background:var(--color-secondary-active);opacity:0;-webkit-animation:load 1s linear infinite;animation:load 1s linear infinite}:host .box:nth-of-type(1){position:absolute;top:2.5%;left:2.5%}:host .box:nth-of-type(2){position:absolute;top:2.5%;right:2.5%;-webkit-animation-delay:-0.25s;animation-delay:-0.25s}:host .box:nth-of-type(3){position:absolute;bottom:2.5%;right:2.5%;-webkit-animation-delay:-0.5s;animation-delay:-0.5s}:host .box:nth-of-type(4){position:absolute;bottom:2.5%;left:2.5%;-webkit-animation-delay:-0.75s;animation-delay:-0.75s}@-webkit-keyframes load{0%{opacity:0}30%{opacity:0}90%{opacity:1}100%{opacity:0}}@keyframes load{0%{opacity:0}30%{opacity:0}90%{opacity:1}100%{opacity:0}}:host .layer{opacity:0;-webkit-transition-property:opacity;transition-property:opacity;-webkit-transition-duration:var(--ui-animaton-speed);transition-duration:var(--ui-animaton-speed);-webkit-transition-timing-function:ease;transition-timing-function:ease;background-color:var(--color-background);position:fixed;width:100%;height:100%}:host .loader-text{opacity:0;-webkit-transition-property:opacity;transition-property:opacity;-webkit-transition-duration:var(--ui-animaton-speed);transition-duration:var(--ui-animaton-speed);-webkit-transition-timing-function:ease;transition-timing-function:ease;position:fixed;color:var(--gray-03);left:50%;top:50%;-webkit-transform:translateX(-50%);transform:translateX(-50%);margin-top:50px;font-family:var(--font-family-primary);font-size:var(--font-size-md)}:host .dot1{-webkit-animation:visibility 1s linear infinite;animation:visibility 1s linear infinite;margin-left:4px}@-webkit-keyframes visibility{0%{opacity:1}65%{opacity:1}66%{opacity:0}100%{opacity:0}}@keyframes visibility{0%{opacity:1}65%{opacity:1}66%{opacity:0}100%{opacity:0}}:host .dot2{-webkit-animation:visibility2 1s linear infinite;animation:visibility2 1s linear infinite}@-webkit-keyframes visibility2{0%{opacity:0}21%{opacity:0}22%{opacity:1}65%{opacity:1}66%{opacity:0}100%{opacity:0}}@keyframes visibility2{0%{opacity:0}21%{opacity:0}22%{opacity:1}65%{opacity:1}66%{opacity:0}100%{opacity:0}}:host .dot3{-webkit-animation:visibility3 1s linear infinite;animation:visibility3 1s linear infinite}@-webkit-keyframes visibility3{0%{opacity:0}43%{opacity:0}44%{opacity:1}65%{opacity:1}66%{opacity:0}100%{opacity:0}}@keyframes visibility3{0%{opacity:0}43%{opacity:0}44%{opacity:1}65%{opacity:1}66%{opacity:0}100%{opacity:0}}:host(.sendLayerBack){z-index:-999 !important}:host(.layerOpacity100) .layer{opacity:1}:host(.squaresOpacity100) .loader{opacity:1}:host(.textOpacity100) .loader-text{opacity:1}";
    }
  };
}));