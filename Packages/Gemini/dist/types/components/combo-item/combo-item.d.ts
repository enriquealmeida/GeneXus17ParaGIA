import { EventEmitter } from "../../stencil-public-runtime";
import { Color } from "../icon/icon";
export declare class GxgComboItem {
    el: HTMLElement;
    /**
     * This event is triggered when the user clicks on an item. event.detail contains the item index, item value, and item icon.
     */
    itemClicked: EventEmitter;
    /**
     * Any icon that belongs to Gemini icon library: https://gx-gemini.netlify.app/?path=/story/icons
     */
    icon: string;
    /**
     * The item value. This is what the filter with search for. If value is not provided, the filter will search by the item innerHTML.
     */
    value: any;
    /**
     * (This prop is for internal use).
     */
    iconColor: Color;
    itemClickedFunc(): void;
    onKeyDown(e: any): void;
    onMouseOver(): void;
    onMouseOut(): void;
    render(): any;
}
