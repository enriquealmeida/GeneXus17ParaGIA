import { EventEmitter } from "../../stencil-public-runtime";
export declare class GxgFilterItem {
    el: HTMLElement;
    /**
     * The item-id (required if you want to know that this item was clicked)
     */
    itemId: any;
    /**
     * The type (optional)
     */
    type: any;
    /**
     * Any icon that belongs to Gemini icon library: https://gx-gemini.netlify.app/?path=/story/icons
     */
    icon: string;
    /**
     * This event is fired when the user clicks on an item. event.detail carries the item id, type, and text.
     */
    itemClickedEvent: EventEmitter;
    itemClicked(): void;
    render(): any;
}
