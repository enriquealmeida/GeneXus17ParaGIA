import { EventEmitter } from "../../stencil-public-runtime";
import { Padding } from "../drag-box/drag-box";
import { DraggableComponent } from "../../utils/makeDraggable";
export declare class GxgDragContainer implements DraggableComponent {
    el: HTMLElement;
    /**
     * The presence of this attribute adds a "delete" button to each gxg-drag-box. When pressed, the "deleted" event is emmited.
     */
    deletable: boolean;
    /**
     * The max-width of the box container
     */
    maxWidth: string;
    /**
     * The padding (internal spacing) of the gxg-drag-boxes
     */
    padding: Padding;
    itemDragStart: EventEmitter;
    itemDrop: EventEmitter;
    itemDragOver: EventEmitter;
    itemDragLeave: EventEmitter;
    itemDragEnter: EventEmitter;
    clickedHandler(event: any): void;
    private dndCleanup;
    getDraggableElements(): NodeListOf<HTMLGxgDragBoxElement>;
    componentDidLoad(): void;
    disconnectedCallback(): void;
    render(): any;
}
