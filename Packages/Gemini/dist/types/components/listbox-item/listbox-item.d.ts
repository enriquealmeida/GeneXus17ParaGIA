import { EventEmitter } from "../../stencil-public-runtime";
import { Color } from "../icon/icon";
export declare class GxgListboxItem {
    el: HTMLElement;
    /**
     * Any icon that belongs to Gemini icon library: https://gx-gemini.netlify.app/?path=/story/icons
     */
    icon: string;
    /**
     * (This event is for internal use.)
     */
    itemClicked: EventEmitter;
    /**
     * (This event is for internal use.)
     */
    checkboxClicked: EventEmitter;
    /**
     * The item value. If value is not provided, the value will be the item innerHTML.
     */
    value: any;
    /**
     * (This prop is for internal use).
     */
    iconColor: Color;
    checkboxes: boolean;
    itemHasFocus: boolean;
    componentWillLoad(): void;
    itemClickedFunc(e: any): void;
    onKeyDown(e: any): void;
    onMouseOver(): void;
    onMouseOut(): void;
    render(): any;
}
