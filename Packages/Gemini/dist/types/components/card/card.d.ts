export declare class GxgCard {
    /**
     * The card box-shadow
     */
    elevation: elevation;
    /**
     * The background color
     */
    background: background;
    /**
     * The card padding
     */
    padding: padding;
    /**
     * The component min. height
     */
    minHeight: string;
    /**
     * The component max. width
     */
    maxWidth: string;
    render(): any;
}
export declare type elevation = "xs" | "m";
export declare type padding = "0" | "xs" | "s" | "m" | "l" | "xl" | "xxl" | "xxxl";
export declare type background = "white" | "gray-01";
