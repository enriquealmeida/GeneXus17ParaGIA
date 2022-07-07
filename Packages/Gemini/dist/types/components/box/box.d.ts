export declare class GxgBox {
    /*********************************
    PROPERTIES & STATE
    *********************************/
    /**
     * The background color
     */
    background: background;
    /**
     * Wether the box has border or not
     */
    border: boolean;
    /**
     * The box padding
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
    /*********************************
    METHODS
    *********************************/
    render(): any;
}
export declare type padding = "0" | "xs" | "s" | "m" | "l" | "xl" | "xxl" | "xxxl";
export declare type background = "white" | "gray-01" | "gray-02";
