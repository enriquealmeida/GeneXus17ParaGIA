import { EventEmitter } from "../../stencil-public-runtime";
export declare class GxgTabButton {
    el: HTMLElement;
    /**
     * The button label
     */
    tabLabel: string;
    /**
     * The tab id. Must be unique, and match the "tab" value of the correlative "gxg-tab" element
     */
    tab: string;
    /**
     * Provide this attribute to make this button selected by default
     */
    isSelected: boolean;
    /**
     * Provide this attribute to make this button disabled
     */
    disabled: boolean;
    /**
     * (Optional) provide an icon to this button
     */
    icon: string;
    tabActivated: EventEmitter;
    tabButtonClicked(): void;
    printIcon(): any;
    componentDidLoad(): void;
    render(): any;
}
