import { EventEmitter } from "../../stencil-public-runtime";
export declare class GxgDragBox {
    /**
     * The presence of this attribute makes this box active
     */
    active: boolean;
    /**
     * The presence of this attribute adds a "delete" button that, when pressed, triggers the "deleted" event
     */
    deletable: boolean;
    /**
     * The padding (internal spacing) of the drag-box (Set it on the drag-container to apply the same padding to all of the gxg-drag-box items)
     */
    padding: Padding;
    /**
     * The title
     */
    title: string;
    el: HTMLElement;
    /**
     * This event is for internal use
     */
    clicked: EventEmitter;
    /**
     * This event fires when the "delete" button is pressed
     */
    deleted: EventEmitter;
    clickedHandler(): void;
    deleteHandler(event: any): void;
    handlerOnKeyDown(event: any): void;
    componentDidLoad(): void;
    render(): any;
}
export declare type Padding = "0" | "xs" | "s" | "m" | "l" | "xl" | "xxl" | "xxxl";
