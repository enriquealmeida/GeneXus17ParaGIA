import { Component, Event, Host, h, Prop, State, Element, } from "@stencil/core";
// import Split from "split.js";
export class GxgTreeGridDivs {
    constructor() {
        this.width = "100%";
        this.displayChildren = "all";
        this.rowsBuffer = [];
        this.thInPixels = false;
        this.displayRowChildrenIds = [];
    }
    componentWillLoad() {
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
        this.rows.map((row, i) => {
            this.parseRows(row, 0, i);
        });
    }
    displayChildrenRows(rows) {
        rows.map((row) => {
            if (row.hasOwnProperty("children")) {
                this.displayRowChildrenIds.push(row["id"]);
                this.displayChildrenRows(row["children"]);
            }
            else {
                return;
            }
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
        for (let i = 0; i < this.columns.length; i++) {
            if (this.columns[i].hasOwnProperty("width")) {
                if (this.columns[i]["width"].includes("px")) {
                    this.thInPixels = true;
                    break;
                }
            }
        }
    }
    parseRows(row, level, i) {
        let hasChildren = false;
        if (row.hasOwnProperty("children")) {
            hasChildren = true;
        }
        this.rowsBuffer.push(h("div", { class: { tr: true }, onClick: (e) => this.trClick(e, row), id: row.id }, Object.keys(row["cells"]).map((td, i) => (h("div", { class: { td: true }, style: {
                paddingLeft: this.tdPaddingLeft(i, level),
            }, "data-value-type": td }, hasChildren && i === 0 ? (h("div", { class: { "icon-text-container": true } },
            this.arrowIcon(i, hasChildren, row),
            //row["cells"][td]
            this.renderTd(row.id, td, row["cells"][td], i))) : (
        //row["cells"][td]
        this.renderTd(row.id, td, row["cells"][td], i)))))));
        const displayRowIdFound = this.displayRowChildrenIds.find((id) => id === row["id"]);
        if (row.hasOwnProperty("children") && displayRowIdFound !== undefined) {
            row["children"].map((row) => {
                this.parseRows(row, level + 1, i);
            });
        }
        else {
            return;
        }
    }
    renderTd(rowId, columnName, value, columnNumber) {
        //si esta definido y es una funcion
        if (this.columns[columnNumber]["render"]) {
            return this.columns[columnNumber]["render"](rowId, columnName, value);
        }
        else {
            return value;
        }
    }
    trClick(e, row) {
        //if ctrl key was not pressed
        if (!e.ctrlKey && !e.shiftKey) {
            //remove previously added classses
            const rows = this.el.shadowRoot.querySelectorAll(".tbody .tr");
            rows.forEach((row) => {
                row.classList.remove("selected");
            });
        }
        //Add 'selected' class to the currently clicked tr
        const rowClicked = this.el.shadowRoot.getElementById(row.id);
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
            const itemsSelected = this.el.shadowRoot.querySelectorAll(".tbody .tr.selected");
            const firstRowSelectedId = itemsSelected[0].getAttribute("id");
            const lastRowSelectedId = itemsSelected[1].getAttribute("id");
            const allRows = this.el.shadowRoot.querySelectorAll(".tbody .tr");
            allRows.forEach((row) => {
                const rowId = row.getAttribute("id");
                if (rowId > firstRowSelectedId && rowId < lastRowSelectedId) {
                    row.classList.add("selected");
                }
            });
        }
        //Emmit event with the table rows that are selected
        const selectedRows = this.el.shadowRoot.querySelectorAll(".tbody .tr.selected");
        const dataArray = [];
        selectedRows.forEach((row) => {
            const rowData = {};
            const tds = row.querySelectorAll(".td");
            tds.forEach((td) => {
                rowData["id"] = row.getAttribute("id");
                const dataValueType = td.getAttribute("data-value-type");
                const dataValue = td.innerHTML;
                rowData[dataValueType] = dataValue;
            });
            dataArray.push(rowData);
        });
        this.selectedRows.emit(dataArray);
    }
    arrowIcon(i, hasChildren, row) {
        if (i === 0 && hasChildren) {
            const rowId = row["id"];
            const rowIdFoundOndisplayRowChildrenIds = this.displayRowChildrenIds.find((id) => id === rowId);
            let iconType;
            if (rowIdFoundOndisplayRowChildrenIds !== undefined) {
                iconType = "navigation/chevron-down";
            }
            else {
                iconType = "navigation/chevron-right";
            }
            return (h("gxg-icon", { type: iconType, onClick: this.toggleRow.bind(this) }));
        }
    }
    toggleRow(e) {
        const trId = e.target.closest(".tr").getAttribute("id");
        const trIdFoundOndisplayRowChildrenIds = this.displayRowChildrenIds.find((id) => id === trId);
        if (trIdFoundOndisplayRowChildrenIds !== undefined) {
            const index = this.displayRowChildrenIds.indexOf(trId);
            if (index > -1) {
                this.displayRowChildrenIds.splice(index, 1);
            }
        }
        else {
            this.displayRowChildrenIds.push(trId);
        }
        this.rowsBuffer = [];
        this.rows.map((row, i) => {
            this.parseRows(row, 0, i);
        });
    }
    tdPaddingLeft(i, level) {
        if (i === 0 && level !== 0) {
            return level * 20 + "px";
        }
    }
    calculateThWitdhLeftover() {
        let totalThWidthDefined = 0;
        let numberOfThWithoutWidthDefined = 0;
        this.columns.forEach((th) => {
            if (th["width"] !== undefined) {
                totalThWidthDefined += parseInt(th["width"].substring(0, th["width"].length - 1));
            }
            else {
                numberOfThWithoutWidthDefined++;
            }
        });
        this.thWidthLeftover =
            (100 - totalThWidthDefined) / numberOfThWithoutWidthDefined + "%";
    }
    thWidth(th) {
        if (th["width"] !== undefined) {
            return th["width"];
        }
        else {
            return this.thWidthLeftover;
        }
    }
    render() {
        return (h(Host, null,
            h("div", { class: { table: true }, style: { width: this.width } },
                h("div", { class: { tr: true } }, this.columns.map((th) => {
                    return (h("div", { class: { th: true }, style: {
                            width: this.thWidth(th),
                        }, id: th["name"] }, th["displayName"]));
                })),
                h("div", { class: {
                        tbody: true,
                    } }, this.rowsBuffer))));
    }
    static get is() { return "gxg-tree-grid-divs"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["gxg-tree-grid-divs.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["gxg-tree-grid-divs.css"]
    }; }
    static get properties() { return {
        "columns": {
            "type": "unknown",
            "mutable": false,
            "complexType": {
                "original": "Array<object>",
                "resolved": "object[]",
                "references": {
                    "Array": {
                        "location": "global"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": ""
            }
        },
        "rows": {
            "type": "unknown",
            "mutable": false,
            "complexType": {
                "original": "Array<object>",
                "resolved": "object[]",
                "references": {
                    "Array": {
                        "location": "global"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": ""
            }
        },
        "width": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "string",
                "resolved": "string",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": ""
            },
            "attribute": "width",
            "reflect": false,
            "defaultValue": "\"100%\""
        },
        "displayChildren": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "DisplayChildren",
                "resolved": "\"all\" | \"none\"",
                "references": {
                    "DisplayChildren": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": ""
            },
            "attribute": "display-children",
            "reflect": false,
            "defaultValue": "\"all\""
        },
        "selectedRowsIds": {
            "type": "number",
            "mutable": true,
            "complexType": {
                "original": "number",
                "resolved": "number",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": ""
            },
            "attribute": "selected-rows-ids",
            "reflect": false
        },
        "editCell": {
            "type": "unknown",
            "mutable": false,
            "complexType": {
                "original": "{ rowId: number; columnName: string }",
                "resolved": "{ rowId: number; columnName: string; }",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": ""
            }
        }
    }; }
    static get states() { return {
        "thWidthLeftover": {},
        "rowsBuffer": {},
        "thInPixels": {},
        "displayRowChildrenIds": {},
        "columnClicked": {},
        "columnOrder": {}
    }; }
    static get events() { return [{
            "method": "selectedRows",
            "name": "selectedRows",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": ""
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }, {
            "method": "addRow",
            "name": "addRow",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": ""
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }, {
            "method": "removeRow",
            "name": "removeRow",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": ""
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }]; }
    static get elementRef() { return "el"; }
}
