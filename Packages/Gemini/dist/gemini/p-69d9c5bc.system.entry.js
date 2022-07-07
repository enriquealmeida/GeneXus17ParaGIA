System.register([ "./p-d8a20c31.system.js" ], (function(t) {
  "use strict";
  var o, e, i, s, r;
  return {
    setters: [ function(t) {
      o = t.r, e = t.c, i = t.h, s = t.H, r = t.g;
    } ],
    execute: function() {
      t("gxg_tree_grid_divs", /** @class */ function() {
        function class_1(t) {
          o(this, t), this.selectedRows = e(this, "selectedRows", 7), this.addRow = e(this, "addRow", 7), 
          this.removeRow = e(this, "removeRow", 7), this.width = "100%", this.displayChildren = "all", 
          this.rowsBuffer = [], this.thInPixels = !1, this.displayRowChildrenIds = [];
        }
        return class_1.prototype.componentWillLoad = function() {
          var t = this;
          //Check if th width is in percentages or pixels
                    this.checkThWidthUnit(), 
          //Table width - if th widths are in pixels, table width should be auto.
          this.thInPixels && (this.width = "auto"), 
          //Set th width leftover (only if th widhts are in percentages)
          this.thInPixels || this.calculateThWitdhLeftover(), 
          //Display children rows
          "all" === this.displayChildren && this.displayChildrenRows(this.rows), 
          //Parse rows
          this.rows.map((function(o, e) {
            t.parseRows(o, 0, e);
          }));
        }, class_1.prototype.displayChildrenRows = function(t) {
          var o = this;
          t.map((function(t) {
            t.hasOwnProperty("children") && (o.displayRowChildrenIds.push(t.id), o.displayChildrenRows(t.children));
          }));
        }, class_1.prototype.componentDidLoad = function() {
          //Spliter
          // var name = this.el.shadowRoot.getElementById("name");
          // var type = this.el.shadowRoot.getElementById("type");
          // var telephone = this.el.shadowRoot.getElementById("telephone");
          // Split([name, type, telephone], {
          //   gutterSize: 2,
          //   cursor: "col-resize",
          // });
        }, class_1.prototype.checkThWidthUnit = function() {
          for (var t = 0; t < this.columns.length; t++) if (this.columns[t].hasOwnProperty("width") && this.columns[t].width.includes("px")) {
            this.thInPixels = !0;
            break;
          }
        }, class_1.prototype.parseRows = function(t, o, e) {
          var s = this, r = !1;
          t.hasOwnProperty("children") && (r = !0), this.rowsBuffer.push(i("div", {
            class: {
              tr: !0
            },
            onClick: function(o) {
              return s.trClick(o, t);
            },
            id: t.id
          }, Object.keys(t.cells).map((function(e, n) {
            return i("div", {
              class: {
                td: !0
              },
              style: {
                paddingLeft: s.tdPaddingLeft(n, o)
              },
              "data-value-type": e
            }, r && 0 === n ? i("div", {
              class: {
                "icon-text-container": !0
              }
            }, s.arrowIcon(n, r, t), 
            //row["cells"][td]
            s.renderTd(t.id, e, t.cells[e], n)) : 
            //row["cells"][td]
            s.renderTd(t.id, e, t.cells[e], n));
          }))));
          var n = this.displayRowChildrenIds.find((function(o) {
            return o === t.id;
          }));
          t.hasOwnProperty("children") && void 0 !== n && t.children.map((function(t) {
            s.parseRows(t, o + 1, e);
          }));
        }, class_1.prototype.renderTd = function(t, o, e, i) {
          //si esta definido y es una funcion
          return this.columns[i].render ? this.columns[i].render(t, o, e) : e;
        }, class_1.prototype.trClick = function(t, o) {
          //if ctrl key was not pressed
          t.ctrlKey || t.shiftKey || this.el.shadowRoot.querySelectorAll(".tbody .tr").forEach((function(t) {
            t.classList.remove("selected");
          }));
          //Add 'selected' class to the currently clicked tr
                    var e = this.el.shadowRoot.getElementById(o.id);
          //if ctrl key was pressed
                    //if shift key was pressed
          if (t.ctrlKey && e.classList.contains("selected") ? e.classList.remove("selected") : e.classList.add("selected"), 
          t.shiftKey) {
            var i = this.el.shadowRoot.querySelectorAll(".tbody .tr.selected"), s = i[0].getAttribute("id"), r = i[1].getAttribute("id");
            this.el.shadowRoot.querySelectorAll(".tbody .tr").forEach((function(t) {
              var o = t.getAttribute("id");
              o > s && o < r && t.classList.add("selected");
            }));
          }
          //Emmit event with the table rows that are selected
                    var n = this.el.shadowRoot.querySelectorAll(".tbody .tr.selected"), d = [];
          n.forEach((function(t) {
            var o = {};
            t.querySelectorAll(".td").forEach((function(e) {
              o.id = t.getAttribute("id");
              var i = e.getAttribute("data-value-type"), s = e.innerHTML;
              o[i] = s;
            })), d.push(o);
          })), this.selectedRows.emit(d);
        }, class_1.prototype.arrowIcon = function(t, o, e) {
          if (0 === t && o) {
            var s = e.id, r = this.displayRowChildrenIds.find((function(t) {
              return t === s;
            }));
            return i("gxg-icon", {
              type: void 0 !== r ? "navigation/chevron-down" : "navigation/chevron-right",
              onClick: this.toggleRow.bind(this)
            });
          }
        }, class_1.prototype.toggleRow = function(t) {
          var o = this, e = t.target.closest(".tr").getAttribute("id");
          if (void 0 !== this.displayRowChildrenIds.find((function(t) {
            return t === e;
          }))) {
            var i = this.displayRowChildrenIds.indexOf(e);
            i > -1 && this.displayRowChildrenIds.splice(i, 1);
          } else this.displayRowChildrenIds.push(e);
          this.rowsBuffer = [], this.rows.map((function(t, e) {
            o.parseRows(t, 0, e);
          }));
        }, class_1.prototype.tdPaddingLeft = function(t, o) {
          if (0 === t && 0 !== o) return 20 * o + "px";
        }, class_1.prototype.calculateThWitdhLeftover = function() {
          var t = 0, o = 0;
          this.columns.forEach((function(e) {
            void 0 !== e.width ? t += parseInt(e.width.substring(0, e.width.length - 1)) : o++;
          })), this.thWidthLeftover = (100 - t) / o + "%";
        }, class_1.prototype.thWidth = function(t) {
          return void 0 !== t.width ? t.width : this.thWidthLeftover;
        }, class_1.prototype.render = function() {
          var t = this;
          return i(s, null, i("div", {
            class: {
              table: !0
            },
            style: {
              width: this.width
            }
          }, i("div", {
            class: {
              tr: !0
            }
          }, this.columns.map((function(o) {
            return i("div", {
              class: {
                th: !0
              },
              style: {
                width: t.thWidth(o)
              },
              id: o.name
            }, o.displayName);
          }))), i("div", {
            class: {
              tbody: !0
            }
          }, this.rowsBuffer)));
        }, Object.defineProperty(class_1.prototype, "el", {
          get: function() {
            return r(this);
          },
          enumerable: !1,
          configurable: !0
        }), class_1;
      }()).style = ":host{display:block}:host .table{display:table;background-color:var(--color-background);font-family:var(--font-family-primary);font-size:8.5px}:host .tr{display:table-row;cursor:pointer}:host .tr:hover{background-color:var(--color-secondary-enabled)}:host .tr.selected{background-color:var(--color-secondary-active)}:host .tbody{display:table-row-group}:host .td,:host .th{display:table-cell}:host .th:hover{background-color:var(--gray-03)}:host .td{padding:5px 8px;border-bottom:1px solid var(--color-on-disabled);border-left:1px solid var(--color-on-disabled);border-right:1px solid var(--color-on-disabled);color:var(--color-on-background)}:host .th{padding:10px var(--spacing-comp-02);text-transform:uppercase;font-weight:bold;background-color:var(--gray-02);color:var(--color-on-background);letter-spacing:var(--letter-spacing-sm)}:host .icon-text-container{position:relative;padding-left:12px}:host .icon-text-container gxg-icon{position:absolute;left:-6px;top:-4px;cursor:pointer}";
    }
  };
}));