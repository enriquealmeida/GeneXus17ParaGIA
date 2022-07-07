System.register([ "./p-d8a20c31.system.js" ], (function(t) {
  "use strict";
  var e, o;
  return {
    setters: [ function(t) {
      e = t.r, o = t.h;
    } ],
    execute: function() {
      t("gxg_demo", /** @class */ function() {
        function class_1(t) {
          e(this, t), this.layerZIndex = 100, this.initiateDemo = !1, this.modalMessage = "Welcome to the demo!", 
          this.numberOfItems = 0, this.currentItem = 0, this.message = "Set the message for this item", 
          this.position = "top-end", this.layerVisible = !1, this.instructionVisible = !1, 
          this.modalVisible = !1, this.disableNextButton = !1, this.rtl = !1;
        }
        return class_1.prototype.componentDidLoad = function() {
          //Retrieve all the demo items
          this.gxgDemoItems = document.querySelectorAll("*[gxg-demo-item]"), this.numberOfItems = this.gxgDemoItems.length, 
          this.setCoordinates(this.gxgDemoItems[0]), this.resizeObserver();
          //Reading Direction
          var t = document.getElementsByTagName("html")[0].getAttribute("dir"), e = document.getElementsByTagName("body")[0].getAttribute("dir");
          "rtl" !== t && "rtl" !== e || (this.rtl = !0);
        }, class_1.prototype.resizeObserver = function() {
          var t = this, e = new ResizeObserver((function() {
            t.setCoordinates(t.gxgDemoItems[t.currentItem]);
          })), o = document.querySelector("body");
          e.observe(o);
        }, class_1.prototype.initiateDemoHandler = function() {
          //Show overlay, instrucction and modal
          !0 === this.initiateDemo && (setTimeout(function() {
            this.layerVisible = !0;
          }.bind(this), 100), setTimeout(function() {
            this.instructionVisible = !0;
          }.bind(this), 200), setTimeout(function() {
            this.modalVisible = !0;
          }.bind(this), 800), this.saveCurrentItemStyles(this.gxgDemoItems[0]), this.setItemStyles(this.gxgDemoItems[0]));
        }, class_1.prototype.saveCurrentItemStyles = function(t) {
          this.currentItemZIndex = t.style.zIndex, this.currentItemPosition = t.style.position, 
          this.currentItemBoxShadow = t.style.boxShadow, this.currentItemPointerEvents = t.style.pointerEvents;
        }, class_1.prototype.setCoordinates = function(t) {
          //MESSAGE POSITION
          var e = t.getBoundingClientRect(), o = e.x, i = e.y, r = e.right, n = e.height, s = e.top, a = e.width;
          this.position = t.getAttribute("position");
          switch (this.position) {
           case "bottom-start":
            this.topPosition = i + n + 7 + "px", this.rtl ? (this.leftPosition = "auto", this.rightPosition = window.innerWidth - r + "px") : (this.leftPosition = o + "px", 
            this.rightPosition = "auto");
            break;

           case "bottom-center":
            this.leftPosition = o + a / 2 + "px", this.rightPosition = "auto", this.topPosition = i + n + 7 + "px";
            break;

           case "bottom-end":
            this.topPosition = i + n + 7 + "px", this.rtl ? (this.leftPosition = o + "px", this.rightPosition = "auto") : (this.leftPosition = "auto", 
            this.rightPosition = window.innerWidth - r + "px");
            break;

           case "top-start":
            this.topPosition = s - 7 + "px", this.rtl ? (this.leftPosition = "auto", this.rightPosition = window.innerWidth - r + "px") : (this.leftPosition = o + "px", 
            this.rightPosition = "auto");
            break;

           case "top-center":
            this.leftPosition = o + a / 2 + "px", this.topPosition = s - 7 + "px", this.rightPosition = "auto";
            break;

           case "top-end":
            this.topPosition = s - 7 + "px", this.rtl ? (this.leftPosition = o + "px", this.rightPosition = "auto") : (this.leftPosition = "auto", 
            this.rightPosition = window.innerWidth - r + "px");
          }
          //Message
                    this.message = t.getAttribute("message");
        }, class_1.prototype.setItemStyles = function(t) {
          setTimeout(function() {
            t.style.zIndex = this.layerZIndex + 1, t.style.position = "relative", t.style.boxShadow = "0px 0px 8px 3px rgba(255,255,255,1)", 
            t.style.pointerEvents = "none";
          }.bind(this), 50);
        }, class_1.prototype.removeStyles = function(t) {
          t.style.zIndex = this.currentItemZIndex, t.style.position = this.currentItemPosition, 
          t.style.boxShadow = this.currentItemBoxShadow, t.style.pointerEvents = this.currentItemPointerEvents;
        }, class_1.prototype.previousItem = function() {
          this.nextItemClicked = !1, this.nextOrPrevItem();
        }, class_1.prototype.nextItem = function() {
          this.currentItem + 1 === this.numberOfItems ? this.endDemo() : (this.nextItemClicked = !0, 
          this.nextOrPrevItem()), 
          //Dehabilitate momentarily Next button to prevent more than one item to be focused
          this.disableNextButton = !0, setTimeout(function() {
            this.disableNextButton = !1;
          }.bind(this), 250);
        }, class_1.prototype.nextOrPrevItem = function() {
          //remove styles from previous item
          this.removeStyles(this.gxgDemoItems[this.currentItem]), this.instructionVisible = !1, 
          setTimeout(function() {
            var t;
            t = this.nextItemClicked ? this.gxgDemoItems[++this.currentItem] : this.gxgDemoItems[--this.currentItem], 
            //save styles from current new item
            this.saveCurrentItemStyles(t), this.setCoordinates(t), this.setItemStyles(t), setTimeout(function() {
              this.instructionVisible = !0;
            }.bind(this), 250);
          }.bind(this), 250);
        }, class_1.prototype.endDemo = function() {
          this.instructionVisible = !1, setTimeout(function() {
            this.removeStyles(this.gxgDemoItems[this.currentItem]), setTimeout(function() {
              this.modalVisible = !1, setTimeout(function() {
                this.layerVisible = !1, setTimeout(function() {
                  this.initiateDemo = !1, this.currentItem = 0, this.setCoordinates(this.gxgDemoItems[this.currentItem]);
                }.bind(this), 250);
              }.bind(this), 250);
            }.bind(this), 250);
          }.bind(this), 250);
        }, class_1.prototype.render = function() {
          if (!0 === this.initiateDemo) return [ o("div", {
            class: {
              tooltip: !0,
              visible: this.instructionVisible,
              rtl: this.rtl,
              "bottom-start": "bottom-start" === this.position,
              "bottom-center": "bottom-center" === this.position,
              "bottom-end": "bottom-end" === this.position,
              "top-start": "top-start" === this.position,
              "top-center": "top-center" === this.position,
              "top-end": "top-end" === this.position
            },
            style: {
              zIndex: (this.layerZIndex + 2).toString(),
              left: this.leftPosition,
              right: this.rightPosition,
              top: this.topPosition
            }
          }, o("div", {
            class: "tooltip__number"
          }, this.currentItem + 1), o("div", {
            class: "tooltip__message"
          }, this.message)), o("div", {
            class: {
              modal: !0,
              visible: this.modalVisible
            },
            style: {
              zIndex: (this.layerZIndex + 1).toString()
            }
          }, o("div", {
            class: "col-left"
          }, o("p", null, this.modalMessage)), o("div", {
            class: "col-right"
          }, o("gxg-button", {
            type: "outlined",
            onClick: this.endDemo.bind(this)
          }, "End demo"), o("gxg-button", {
            type: "primary-text-only",
            onClick: this.previousItem.bind(this),
            disabled: 0 === this.currentItem
          }, "Previous"), o("gxg-button", {
            type: "primary-text-only",
            onClick: this.nextItem.bind(this),
            class: {
              "next-button": !0,
              disabled: !0 === this.disableNextButton
            }
          }, this.currentItem + 1 !== this.numberOfItems ? "Next" : "Finish"))), o("div", {
            class: {
              layer: !0,
              visible: this.layerVisible
            },
            style: {
              zIndex: this.layerZIndex.toString()
            }
          }) ];
        }, Object.defineProperty(class_1, "watchers", {
          get: function() {
            return {
              initiateDemo: [ "initiateDemoHandler" ]
            };
          },
          enumerable: !1,
          configurable: !0
        }), class_1;
      }()).style = '/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host .layer{position:fixed;width:100%;height:100%;top:0;left:0;background-color:black;opacity:0;-webkit-transition-property:opacity;transition-property:opacity;-webkit-transition-duration:var(--ui-animaton-speed);transition-duration:var(--ui-animaton-speed);-webkit-transition-timing-function:ease;transition-timing-function:ease}:host .layer.visible{opacity:0.5}:host .tooltip{position:absolute;background:var(--gray-07);color:var(--color-on-primary);font-family:var(--font-family-primary);font-size:var(--font-size-sm);padding:var(--spacing-comp-03);max-width:300px;display:-ms-flexbox;display:flex;opacity:0;-webkit-transition-property:opacity;transition-property:opacity;-webkit-transition-duration:var(--ui-animaton-speed);transition-duration:var(--ui-animaton-speed);-webkit-transition-timing-function:ease;transition-timing-function:ease}:host .tooltip.visible{opacity:1}:host .tooltip:before{content:"";width:0;height:0;border-left:5px solid transparent;border-right:5px solid transparent;border-bottom:5px solid var(--gray-07);position:absolute}:host .tooltip.bottom-start:before{top:-5px;left:5px}:host .tooltip.bottom-center{-webkit-transform:translateX(-50%);transform:translateX(-50%)}:host .tooltip.bottom-center:before{top:-5px;left:50%;margin-left:-2.5px}:host .tooltip.bottom-end:before{top:-5px;right:5px}:host .tooltip.top-start{-webkit-transform:translateY(-100%);transform:translateY(-100%)}:host .tooltip.top-start:before{bottom:-5px;-webkit-transform:rotate(180deg);transform:rotate(180deg);left:5px}:host .tooltip.top-center{-webkit-transform:translateY(-100%) translateX(-50%);transform:translateY(-100%) translateX(-50%)}:host .tooltip.top-center:before{bottom:-5px;-webkit-transform:rotate(180deg);transform:rotate(180deg);left:50%;margin-left:-2.5px}:host .tooltip.top-end{-webkit-transform:translateY(-100%);transform:translateY(-100%)}:host .tooltip.top-end:before{right:5px;bottom:-5px;-webkit-transform:rotate(180deg);transform:rotate(180deg)}:host .tooltip.rtl.bottom-start:before{right:5px;left:auto}:host .tooltip.rtl.bottom-end:before{left:5px;right:auto}:host .tooltip.rtl.top-start:before{right:5px;left:auto}:host .tooltip.rtl.top-end:before{left:5px;right:auto}:host .tooltip__number{font-weight:var(--font-weight-bold);font-size:20px;-webkit-padding-end:var(--spacing-comp-03);padding-inline-end:var(--spacing-comp-03);line-height:1em}:host .tooltip__message{line-height:1.6em}:host .modal{position:fixed;bottom:-100px;-webkit-transition-property:bottom;transition-property:bottom;-webkit-transition-duration:0.5s;transition-duration:0.5s;-webkit-transition-timing-function:ease;transition-timing-function:ease;left:50%;height:40px;background-color:var(--color-background);width:500px;margin-left:-250px;border-radius:var(--border-radius-md);-webkit-box-shadow:var(--box-shadow-02);box-shadow:var(--box-shadow-02);display:-ms-flexbox;display:flex;padding:0 var(--spacing-comp-02)}:host .modal.visible{bottom:20px}:host .modal>div{display:-ms-flexbox;display:flex;width:100%;-ms-flex-align:center;align-items:center;-ms-flex-pack:justify;justify-content:space-between}:host .modal p{font-family:var(--font-family-primary);font-size:var(--font-size-sm);color:var(-color-on-background)}:host .modal .col-left{width:35%}:host .modal .col-right{width:65%;display:-ms-flexbox;display:flex;-ms-flex-pack:end;justify-content:flex-end}:host .modal gxg-button{margin-right:var(--spacing-comp-02)}:host .next-button.disabled{pointer-events:none}';
    }
  };
}));