export declare class GxgAccordion {
    /**
     * The presence of this attribute makes all of the accordion-items disabled and not focusable
     */
    disabled: boolean;
    /**
     * If this attribute is present, only one accordion-item can be open at the same time
     */
    singleItemOpen: boolean;
    /**
     * The accordion flavor
     */
    mode: mode;
    /**
     * The accordion max-width
     */
    maxWidth: string;
    /**
     * The presence of this attribues removes the padding (internal spacing) from the accordion items containers. This property only applies for the "classical" or "boxed" modes.
     */
    noPadding: boolean;
    accordions: HTMLGxgAccordionItemElement[];
    el: HTMLElement;
    itemClickedHandler(event: CustomEvent): void;
    itemLoadedHandler(): void;
    componentWillLoad(): void;
    setupAccordions(): void;
    render(): any;
}
export declare type mode = "classical" | "slim" | "boxed" | "minimal";
