import { EventEmitter } from "../../stencil-public-runtime";
export declare class GxgTreeGridDivs {
    el: HTMLElement;
    columns: Array<object>;
    rows: Array<object>;
    width: string;
    displayChildren: DisplayChildren;
    selectedRowsIds: number;
    editCell: {
        rowId: number;
        columnName: string;
    };
    /**
     * The width in percetage asigned to each of the th's that no width was asigned on initialization.
     */
    thWidthLeftover: string;
    rowsBuffer: any[];
    thInPixels: boolean;
    displayRowChildrenIds: any[];
    columnClicked: string;
    columnOrder: string;
    selectedRows: EventEmitter;
    addRow: EventEmitter;
    removeRow: EventEmitter;
    componentWillLoad(): void;
    displayChildrenRows(rows: any): void;
    componentDidLoad(): void;
    checkThWidthUnit(): void;
    parseRows(row: any, level: any, i: any): void;
    renderTd(rowId: any, columnName: any, value: any, columnNumber: any): any;
    trClick(e: any, row: any): void;
    arrowIcon(i: any, hasChildren: any, row: any): any;
    toggleRow(e: any): void;
    tdPaddingLeft(i: any, level: any): string;
    calculateThWitdhLeftover(): void;
    thWidth(th: any): any;
    render(): any;
}
export declare type DisplayChildren = "all" | "none";
export declare type ColumnOrder = "ascendant" | "descendant" | "default";
