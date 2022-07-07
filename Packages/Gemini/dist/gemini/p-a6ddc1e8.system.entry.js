System.register([ "./p-d8a20c31.system.js" ], (function(e) {
  "use strict";
  var t, i, s, c, o;
  return {
    setters: [ function(e) {
      t = e.r, i = e.c, s = e.h, c = e.H, o = e.g;
    } ],
    execute: function() {
      e("gxg_listbox", /** @class */ function() {
        function class_1(e) {
          t(this, e), this.selectionChanged = i(this, "selectionChanged", 7), 
          /**
                     * The listbox title that appears on the header
                     */
          this.theTitle = "", 
          /**
                     * The listbox width
                     */
          this.width = "280px", 
          /**
                     * The prescence of this attribute will display a checkbox for every item
                     */
          this.checkboxes = !1;
        }
        return class_1.prototype.componentWillLoad = function() {
          //Set checkboxes
          this.checkboxes && this.el.querySelectorAll("gxg-listbox-item").forEach((function(e) {
            var t = document.createElement("gxg-form-checkbox");
            t.setAttribute("slot", "checkbox"), t.setAttribute("tabindex", "-1"), e.prepend(t);
          }));
          //Set index and Tabindex
                    this.el.querySelectorAll("gxg-listbox-item").forEach((function(e, t) {
            e.setAttribute("index", t.toString()), 
            //tabindex
            e.setAttribute("tabindex", "0");
          }));
        }, class_1.prototype.itemClickedHandler = function(e) {
          if (!e.detail.crtlKey && !e.detail.cmdKey && !this.checkboxes) {
            var t = this.el.querySelectorAll(".selected");
            t.length > 0 && t.forEach((function(e) {
              e.classList.remove("selected"), 
              //set icon color to auto
              e.iconColor = "auto";
              //set checkbox checked to false
              var t = e.querySelector("gxg-form-checkbox");
              null !== t && (t.checked = !1);
            }));
          }
          var i = this.el.querySelector("[index='" + e.detail.index + "']"), s = i.querySelector("gxg-form-checkbox");
          //checkbox
                    i.classList.contains("selected") ? (i.classList.remove("selected"), 
          //set icon color to auto
          e.detail.mouseClicked || (i.iconColor = "auto", null !== s && (s.checked = !1))) : (i.classList.add("selected"), 
          //set icon color to negative
          i.iconColor = "negative", null !== s && (s.checked = !0)), this.emmitSelectedItems();
        }, class_1.prototype.checkboxClickedHandler = function(e) {
          var t = this.el.querySelector("[index='" + e.detail.index + "']");
          t.classList.contains("selected") ? t.classList.remove("selected") : t.classList.add("selected"), 
          this.emmitSelectedItems();
        }, class_1.prototype.emmitSelectedItems = function() {
          var e = this.el.querySelectorAll(".selected"), t = [];
          e.forEach((function(e) {
            var i = e.getAttribute("index"), s = e.getAttribute("value"), c = {
              index: i,
              value: s = null === s ? e.textContent : s.toString()
            };
            t.push(c);
          })), this.selectionChanged.emit(Object.assign({}, t));
        }, class_1.prototype.render = function() {
          return s(c, null, s("div", {
            style: {
              width: this.width
            },
            class: {
              container: !0
            }
          }, s("header", {
            class: {
              header: !0
            }
          }, this.theTitle), s("main", {
            class: {
              main: !0
            }
          }, s("slot", null))));
        }, Object.defineProperty(class_1.prototype, "el", {
          get: function() {
            return o(this);
          },
          enumerable: !1,
          configurable: !0
        }), class_1;
      }()).style = ":host{display:block}:host .header{height:var(--spacing-comp-06);background-color:var(--gray-02);display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;padding:0 var(--spacing-comp-02);font-family:var(--font-family-primary);text-transform:uppercase;font-size:var(--font-size-sm);font-weight:var(--font-weight-bold);color:var(--color-on-background)}:host .main{border-style:var(--border-style-regular);border-width:var(--border-radius-xs);border-color:var(--gray-02)}:host .icon{-webkit-margin-end:var(--spacing-comp-01);margin-inline-end:var(--spacing-comp-01)}:host .checkbox{-webkit-margin-end:var(--spacing-comp-01);margin-inline-end:var(--spacing-comp-01)}:host.selected{background-color:var(--color-secondary-active);color:var(--color-always-white)}";
    }
  };
}));