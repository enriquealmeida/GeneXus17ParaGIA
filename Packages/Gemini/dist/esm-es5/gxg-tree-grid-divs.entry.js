import { r as registerInstance, c as createEvent, h, H as Host, g as getElement } from './index-09b1517f.js';
var gxgTreeGridDivsCss = ":host{display:block}:host .table{display:table;background-color:var(--color-background);font-family:var(--font-family-primary);font-size:8.5px}:host .tr{display:table-row;cursor:pointer}:host .tr:hover{background-color:var(--color-secondary-enabled)}:host .tr.selected{background-color:var(--color-secondary-active)}:host .tbody{display:table-row-group}:host .td,:host .th{display:table-cell}:host .th:hover{background-color:var(--gray-03)}:host .td{padding:5px 8px;border-bottom:1px solid var(--color-on-disabled);border-left:1px solid var(--color-on-disabled);border-right:1px solid var(--color-on-disabled);color:var(--color-on-background)}:host .th{padding:10px var(--spacing-comp-02);text-transform:uppercase;font-weight:bold;background-color:var(--gray-02);color:var(--color-on-background);letter-spacing:var(--letter-spacing-sm)}:host .icon-text-container{position:relative;padding-left:12px}:host .icon-text-container gxg-icon{position:absolute;left:-6px;top:-4px;cursor:pointer}";
var GxgTreeGridDivs = /** @class */ (function () {
    function GxgTreeGridDivs(hostRef) {
        registerInstance(this, hostRef);
        this.selectedRows = createEvent(this, "selectedRows", 7);
        this.addRow = createEvent(this, "addRow", 7);
        this.removeRow = createEvent(this, "removeRow", 7);
        this.width = "100%";
        this.displayChildren = "all";
        this.rowsBuffer = [];
        this.thInPixels = false;
        this.displayRowChildrenIds = [];
    }
    GxgTreeGridDivs.prototype.componentWillLoad = function () {
        var _this = this;
        //Check if th width is in percentages or pixels
        this.checkThWidthUnit();
        //Table width - if th widths are in pixels, table width should be auto.
        if (this.thInPixels) {
            this.width = "auto";
        }
        //Set th width leftover (only if th widhts are in percentages)
        if (!this.thInPixels) {
            this.calculateThWitdhLeftover();
        }
        //Display children rows
        if (this.displayChildren === "all") {
            this.displayChildrenRows(this.rows);
        }
        //Parse rows
        this.rows.map(function (row, i) {
            _this.parseRows(row, 0, i);
        });
    };
    GxgTreeGridDivs.prototype.displayChildrenRows = function (rows) {
        var _this = this;
        rows.map(function (row) {
            if (row.hasOwnProperty("children")) {
                _this.displayRowChildrenIds.push(row["id"]);
                _this.displayChildrenRows(row["children"]);
            }
            else {
                return;
            }
        });
    };
    GxgTreeGridDivs.prototype.componentDidLoad = function () {
        //Spliter
        // var name = this.el.shadowRoot.getElementById("name");
        // var type = this.el.shadowRoot.getElementById("type");
        // var telephone = this.el.shadowRoot.getElementById("telephone");
        // Split([name, type, telephone], {
        //   gutterSize: 2,
        //   cursor: "col-resize",
        // });
    };
    GxgTreeGridDivs.prototype.checkThWidthUnit = function () {
        for (var i = 0; i < this.columns.length; i++) {
            if (this.columns[i].hasOwnProperty("width")) {
                if (this.columns[i]["width"].includes("px")) {
                    this.thInPixels = true;
                    break;
                }
            }
        }
    };
    GxgTreeGridDivs.prototype.parseRows = function (row, level, i) {
        var _this = this;
        var hasChildren = false;
        if (row.hasOwnProperty("children")) {
            hasChildren = true;
        }
        this.rowsBuffer.push(h("div", { class: { tr: true }, onClick: function (e) { return _this.trClick(e, row); }, id: row.id }, Object.keys(row["cells"]).map(function (td, i) { return (h("div", { class: { td: true }, style: {
                paddingLeft: _this.tdPaddingLeft(i, level),
            }, "data-value-type": td }, hasChildren && i === 0 ? (h("div", { class: { "icon-text-container": true } }, _this.arrowIcon(i, hasChildren, row), 
        //row["cells"][td]
        _this.renderTd(row.id, td, row["cells"][td], i))) : (
        //row["cells"][td]
        _this.renderTd(row.id, td, row["cells"][td], i)))); })));
        var displayRowIdFound = this.displayRowChildrenIds.find(function (id) { return id === row["id"]; });
        if (row.hasOwnProperty("children") && displayRowIdFound !== undefined) {
            row["children"].map(function (row) {
                _this.parseRows(row, level + 1, i);
            });
        }
        else {
            return;
        }
    };
    GxgTreeGridDivs.prototype.renderTd = function (rowId, columnName, value, columnNumber) {
        //si esta definido y es una funcion
        if (this.columns[columnNumber]["render"]) {
            return this.columns[columnNumber]["render"](rowId, columnName, value);
        }
        else {
            return value;
        }
    };
    GxgTreeGridDivs.prototype.trClick = function (e, row) {
        //if ctrl key was not pressed
        if (!e.ctrlKey && !e.shiftKey) {
            //remove previously added classses
            var rows = this.el.shadowRoot.querySelectorAll(".tbody .tr");
            rows.forEach(function (row) {
                row.classList.remove("selected");
            });
        }
        //Add 'selected' class to the currently clicked tr
        var rowClicked = this.el.shadowRoot.getElementById(row.id);
        //if ctrl key was pressed
        if (e.ctrlKey) {
            if (rowClicked.classList.contains("selected")) {
                rowClicked.classList.remove("selected");
            }
            else {
                rowClicked.classList.add("selected");
            }
        }
        else {
            rowClicked.classList.add("selected");
        }
        //if shift key was pressed
        if (e.shiftKey) {
            var itemsSelected = this.el.shadowRoot.querySelectorAll(".tbody .tr.selected");
            var firstRowSelectedId_1 = itemsSelected[0].getAttribute("id");
            var lastRowSelectedId_1 = itemsSelected[1].getAttribute("id");
            var allRows = this.el.shadowRoot.querySelectorAll(".tbody .tr");
            allRows.forEach(function (row) {
                var rowId = row.getAttribute("id");
                if (rowId > firstRowSelectedId_1 && rowId < lastRowSelectedId_1) {
                    row.classList.add("selected");
                }
            });
        }
        //Emmit event with the table rows that are selected
        var selectedRows = this.el.shadowRoot.querySelectorAll(".tbody .tr.selected");
        var dataArray = [];
        selectedRows.forEach(function (row) {
            var rowData = {};
            var tds = row.querySelectorAll(".td");
            tds.forEach(function (td) {
                rowData["id"] = row.getAttribute("id");
                var dataValueType = td.getAttribute("data-value-type");
                var dataValue = td.innerHTML;
                rowData[dataValueType] = dataValue;
            });
            dataArray.push(rowData);
        });
        this.selectedRows.emit(dataArray);
    };
    GxgTreeGridDivs.prototype.arrowIcon = function (i, hasChildren, row) {
        if (i === 0 && hasChildren) {
            var rowId_1 = row["id"];
            var rowIdFoundOndisplayRowChildrenIds = this.displayRowChildrenIds.find(function (id) { return id === rowId_1; });
            var iconType = void 0;
            if (rowIdFoundOndisplayRowChildrenIds !== undefined) {
                iconType = "navigation/chevron-down";
            }
            else {
                iconType = "navigation/chevron-right";
            }
            return (h("gxg-icon", { type: iconType, onClick: this.toggleRow.bind(this) }));
        }
    };
    GxgTreeGridDivs.prototype.toggleRow = function (e) {
        var _this = this;
        var trId = e.target.closest(".tr").getAttribute("id");
        var trIdFoundOndisplayRowChildrenIds = this.displayRowChildrenIds.find(function (id) { return id === trId; });
        if (trIdFoundOndisplayRowChildrenIds !== undefined) {
            var index = this.displayRowChildrenIds.indexOf(trId);
            if (index > -1) {
                this.displayRowChildrenIds.splice(index, 1);
            }
        }
        else {
            this.displayRowChildrenIds.push(trId);
        }
        this.rowsBuffer = [];
        this.rows.map(function (row, i) {
            _this.parseRows(row, 0, i);
        });
    };
    GxgTreeGridDivs.prototype.tdPaddingLeft = function (i, level) {
        if (i === 0 && level !== 0) {
            return level * 20 + "px";
        }
    };
    GxgTreeGridDivs.prototype.calculateThWitdhLeftover = function () {
        var totalThWidthDefined = 0;
        var numberOfThWithoutWidthDefined = 0;
        this.columns.forEach(function (th) {
            if (th["width"] !== undefined) {
                totalThWidthDefined += parseInt(th["width"].substring(0, th["width"].length - 1));
            }
            else {
                numberOfThWithoutWidthDefined++;
            }
        });
        this.thWidthLeftover =
            (100 - totalThWidthDefined) / numberOfThWithoutWidthDefined + "%";
    };
    GxgTreeGridDivs.prototype.thWidth = function (th) {
        if (th["width"] !== undefined) {
            return th["width"];
        }
        else {
            return this.thWidthLeftover;
        }
    };
    GxgTreeGridDivs.prototype.render = function () {
        var _this = this;
        return (h(Host, null, h("div", { class: { table: true }, style: { width: this.width } }, h("div", { class: { tr: true } }, this.columns.map(function (th) {
            return (h("div", { class: { th: true }, style: {
                    width: _this.thWidth(th),
                }, id: th["name"] }, th["displayName"]));
        })), h("div", { class: {
                tbody: true,
            } }, this.rowsBuffer))));
    };
    Object.defineProperty(GxgTreeGridDivs.prototype, "el", {
        get: function () { return getElement(this); },
        enumerable: false,
        configurable: true
    });
    return GxgTreeGridDivs;
}());
GxgTreeGridDivs.style = gxgTreeGridDivsCss;
export { GxgTreeGridDivs as gxg_tree_grid_divs };
