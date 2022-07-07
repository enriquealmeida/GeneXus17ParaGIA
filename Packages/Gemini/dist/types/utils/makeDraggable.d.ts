import { EventEmitter } from "../stencil-public-runtime";
export declare function makeDraggable(component: DraggableComponent): Function;
export interface DraggableComponent {
    itemDrop: EventEmitter;
    itemDragEnter: EventEmitter;
    itemDragLeave: EventEmitter;
    itemDragStart: EventEmitter;
    itemDragOver: EventEmitter;
    getDraggableElements(): NodeListOf<Element>;
}
