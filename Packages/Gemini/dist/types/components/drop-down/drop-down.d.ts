import { EventEmitter } from "../../stencil-public-runtime";
export declare class GxgDropDown {
    el: HTMLElement;
    /**
     * the dropdown width
     */
    width: string;
    /**
     * the dropdown max. height
     */
    maxHeight: string;
    /**
     * the dropdown label (optional)
     */
    label: string;
    /**
     * the dropdown icon (optional)
     */
    icon: string;
    /**
     * Displays the dropdown content
     */
    showContent: boolean;
    initialButtonText: string;
    detectClickOutsideDropDownVar: any;
    /**
     * This events gets fired when the dropdown is opened
     */
    opened: EventEmitter;
    /**
     * This events gets fired when the dropdown is closed
     */
    closed: EventEmitter;
    componentWillLoad(): void;
    toggleContent(): void;
    detectClickOutsideDropDown(event: any): void;
    componentDidUnload(): void;
    watchHandler(newValue: boolean): void;
    render(): any;
}
