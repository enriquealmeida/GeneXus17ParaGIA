export declare class GxgColumns {
    el: HTMLElement;
    /**
     * The vertical alignment
     */
    alignY: AlignY;
    /**
     * The collapse breakpoint
     */
    collapseBellow: CollapseBellow;
    /**
     * The spacing between columns
     */
    space: Space;
    componentDidLoad(): void;
    render(): any;
}
export declare type Space = "0" | "xs" | "s" | "m" | "l" | "xl";
export declare type AlignY = "top" | "center" | "bottom";
export declare type CollapseBellow = "lg";
