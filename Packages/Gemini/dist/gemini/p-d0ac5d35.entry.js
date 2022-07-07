import { r as t, c as s, h as e, H as i, g as o } from "./p-eb9dc661.js";

const r = class {
  constructor(e) {
    t(this, e), this.selectedRows = s(this, "selectedRows", 7), this.addRow = s(this, "addRow", 7), 
    this.removeRow = s(this, "removeRow", 7), this.width = "100%", this.displayChildren = "all", 
    this.rowsBuffer = [], this.thInPixels = !1, this.displayRowChildrenIds = [];
  }
  componentWillLoad() {
    //Check if th width is in percentages or pixels
    this.checkThWidthUnit(), 
    //Table width - if th widths are in pixels, table width should be auto.
    this.thInPixels && (this.width = "auto"), 
    //Set th width leftover (only if th widhts are in percentages)
    this.thInPixels || this.calculateThWitdhLeftover(), 
    //Display children rows
    "all" === this.displayChildren && this.displayChildrenRows(this.rows), 
    //Parse rows
    this.rows.map((t, s) => {
      this.parseRows(t, 0, s);
    });
  }
  displayChildrenRows(t) {
    t.map(t => {
      t.hasOwnProperty("children") && (this.displayRowChildrenIds.push(t.id), this.displayChildrenRows(t.children));
    });
  }
  componentDidLoad() {
    //Spliter
    // var name = this.el.shadowRoot.getElementById("name");
    // var type = this.el.shadowRoot.getElementById("type");
    // var telephone = this.el.shadowRoot.getElementById("telephone");
    // Split([name, type, telephone], {
    //   gutterSize: 2,
    //   cursor: "col-resize",
    // });
  }
  checkThWidthUnit() {
    for (let t = 0; t < this.columns.length; t++) if (this.columns[t].hasOwnProperty("width") && this.columns[t].width.includes("px")) {
      this.thInPixels = !0;
      break;
    }
  }
  parseRows(t, s, i) {
    let o = !1;
    t.hasOwnProperty("children") && (o = !0), this.rowsBuffer.push(e("div", {
      class: {
        tr: !0
      },
      onClick: s => this.trClick(s, t),
      id: t.id
    }, Object.keys(t.cells).map((i, r) => e("div", {
      class: {
        td: !0
      },
      style: {
        paddingLeft: this.tdPaddingLeft(r, s)
      },
      "data-value-type": i
    }, o && 0 === r ? e("div", {
      class: {
        "icon-text-container": !0
      }
    }, this.arrowIcon(r, o, t), 
    //row["cells"][td]
    this.renderTd(t.id, i, t.cells[i], r)) : 
    //row["cells"][td]
    this.renderTd(t.id, i, t.cells[i], r)))));
    const r = this.displayRowChildrenIds.find(s => s === t.id);
    t.hasOwnProperty("children") && void 0 !== r && t.children.map(t => {
      this.parseRows(t, s + 1, i);
    });
  }
  renderTd(t, s, e, i) {
    //si esta definido y es una funcion
    return this.columns[i].render ? this.columns[i].render(t, s, e) : e;
  }
  trClick(t, s) {
    //if ctrl key was not pressed
    if (!t.ctrlKey && !t.shiftKey) {
      this.el.shadowRoot.querySelectorAll(".tbody .tr").forEach(t => {
        t.classList.remove("selected");
      });
    }
    //Add 'selected' class to the currently clicked tr
        const e = this.el.shadowRoot.getElementById(s.id);
    //if ctrl key was pressed
        //if shift key was pressed
    if (t.ctrlKey && e.classList.contains("selected") ? e.classList.remove("selected") : e.classList.add("selected"), 
    t.shiftKey) {
      const t = this.el.shadowRoot.querySelectorAll(".tbody .tr.selected"), s = t[0].getAttribute("id"), e = t[1].getAttribute("id");
      this.el.shadowRoot.querySelectorAll(".tbody .tr").forEach(t => {
        const i = t.getAttribute("id");
        i > s && i < e && t.classList.add("selected");
      });
    }
    //Emmit event with the table rows that are selected
        const i = this.el.shadowRoot.querySelectorAll(".tbody .tr.selected"), o = [];
    i.forEach(t => {
      const s = {};
      t.querySelectorAll(".td").forEach(e => {
        s.id = t.getAttribute("id");
        const i = e.getAttribute("data-value-type"), o = e.innerHTML;
        s[i] = o;
      }), o.push(s);
    }), this.selectedRows.emit(o);
  }
  arrowIcon(t, s, i) {
    if (0 === t && s) {
      const t = i.id;
      let s;
      return s = void 0 !== this.displayRowChildrenIds.find(s => s === t) ? "navigation/chevron-down" : "navigation/chevron-right", 
      e("gxg-icon", {
        type: s,
        onClick: this.toggleRow.bind(this)
      });
    }
  }
  toggleRow(t) {
    const s = t.target.closest(".tr").getAttribute("id");
    if (void 0 !== this.displayRowChildrenIds.find(t => t === s)) {
      const t = this.displayRowChildrenIds.indexOf(s);
      t > -1 && this.displayRowChildrenIds.splice(t, 1);
    } else this.displayRowChildrenIds.push(s);
    this.rowsBuffer = [], this.rows.map((t, s) => {
      this.parseRows(t, 0, s);
    });
  }
  tdPaddingLeft(t, s) {
    if (0 === t && 0 !== s) return 20 * s + "px";
  }
  calculateThWitdhLeftover() {
    let t = 0, s = 0;
    this.columns.forEach(e => {
      void 0 !== e.width ? t += parseInt(e.width.substring(0, e.width.length - 1)) : s++;
    }), this.thWidthLeftover = (100 - t) / s + "%";
  }
  thWidth(t) {
    return void 0 !== t.width ? t.width : this.thWidthLeftover;
  }
  render() {
    return e(i, null, e("div", {
      class: {
        table: !0
      },
      style: {
        width: this.width
      }
    }, e("div", {
      class: {
        tr: !0
      }
    }, this.columns.map(t => e("div", {
      class: {
        th: !0
      },
      style: {
        width: this.thWidth(t)
      },
      id: t.name
    }, t.displayName))), e("div", {
      class: {
        tbody: !0
      }
    }, this.rowsBuffer)));
  }
  get el() {
    return o(this);
  }
};

r.style = ":host{display:block}:host .table{display:table;background-color:var(--color-background);font-family:var(--font-family-primary);font-size:8.5px}:host .tr{display:table-row;cursor:pointer}:host .tr:hover{background-color:var(--color-secondary-enabled)}:host .tr.selected{background-color:var(--color-secondary-active)}:host .tbody{display:table-row-group}:host .td,:host .th{display:table-cell}:host .th:hover{background-color:var(--gray-03)}:host .td{padding:5px 8px;border-bottom:1px solid var(--color-on-disabled);border-left:1px solid var(--color-on-disabled);border-right:1px solid var(--color-on-disabled);color:var(--color-on-background)}:host .th{padding:10px var(--spacing-comp-02);text-transform:uppercase;font-weight:bold;background-color:var(--gray-02);color:var(--color-on-background);letter-spacing:var(--letter-spacing-sm)}:host .icon-text-container{position:relative;padding-left:12px}:host .icon-text-container gxg-icon{position:absolute;left:-6px;top:-4px;cursor:pointer}";

export { r as gxg_tree_grid_divs }