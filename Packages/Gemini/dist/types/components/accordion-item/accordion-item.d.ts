import { EventEmitter } from "../../stencil-public-runtime";
import { mode } from "../accordion/accordion";
export declare class GxgAccordionItem {
    el: HTMLElement;
    /**
     * The presence of this attribute makes the accordion-item disabled and not focusable
     */
    disabled: boolean;
    /**
     * The presence of this attribute makes the accordion title editable
     */
    editableTitle: boolean;
    /**
     * The accordion flavor (No need to set this attribute on each of the the accordion-item's, only once at gxg-accordion)
     */
    mode: mode;
    /**
     * The accordion id
     */
    itemId: string;
    /**
     * The accordion title
     */
    itemTitle: string;
    /**
     * The accordion subtitle (optional)
     */
    itemSubtitle: string;
    /**
     * The accordion title icon
     */
    titleIcon: string;
    /**
     * Set the status to "open" if you want the accordion-item open by default
     */
    status: status;
    /**
     * This event is for internal use
     */
    accordionItemClicked: EventEmitter;
    /**
     * This event is for internal use
     */
    accordionItemLoaded: EventEmitter;
    /**
     * Subscribe to this event to know when the "title" was clicked
     */
    accordionTitleClicked: EventEmitter;
    /**
     * If "editable-title" attribute is present, this event emmits the title value when it has changed
     */
    titleChanged: EventEmitter;
    nestedAccordion: boolean;
    minHeight: any;
    accordionMode: any;
    hasSlottedMeta: any;
    itemClickedHandler(e: any): void;
    titleClickedHandler(): void;
    printIcon(): any;
    componentWillLoad(): void;
    componentDidLoad(): void;
    ariaExpanded(): "true" | "false";
    ariaDisabled(): "true" | "false";
    gxgFormTextClickedHandler(e: any): void;
    changedTitleHandler(event: any): void;
    keyDownHandler(e: any): void;
    itemSubtitleTrimmed(subtitle: any): any;
    render(): any;
}
export declare type status = "open" | "closed";
