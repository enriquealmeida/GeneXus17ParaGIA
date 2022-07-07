import { EventEmitter } from "../../stencil-public-runtime";
export declare class GxgListbox {
    /**
     * This event emmits the items that are currently selected. event.detail contains the selected items as objects. Each object contains the item idex and the item value. If value was not provided, the value will be the item innerText.
     */
    selectionChanged: EventEmitter;
    el: HTMLElement;
    /**
     * The listbox title that appears on the header
     */
    theTitle: string;
    /**
     * The listbox width
     */
    width: string;
    /**
     * The prescence of this attribute will display a checkbox for every item
     */
    checkboxes: boolean;
    componentWillLoad(): void;
    itemClickedHandler(e: any): void;
    checkboxClickedHandler(e: any): void;
    emmitSelectedItems(): void;
    render(): any;
}
