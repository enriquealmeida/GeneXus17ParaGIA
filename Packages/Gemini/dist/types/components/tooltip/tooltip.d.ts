export declare class GxgTooltip {
    /**
     the tooltip position
     */
    position: position;
    /**
     * The label
     */
    label: string;
    /**
     * This presence of this property removes the border under the text
     */
    noBorder: boolean;
    render(): any;
}
export declare type position = "top" | "right" | "bottom" | "left";
