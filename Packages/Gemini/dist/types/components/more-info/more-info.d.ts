export declare class GxgMoreInfo {
    /**
     the tooltip position
     */
    position: position;
    /**
     * The label
     */
    label: string;
    /**
     * (Optional) The "more-info" label. This property goes along with "url" attribute
     */
    moreInfoLabel: string;
    /**
     * (Optional) The "more-info" url.
     */
    url: string;
    /**
     * The url target
     */
    target: target;
    render(): any;
}
export declare type position = "top" | "right" | "bottom" | "left";
export declare type target = "_blank" | "self";
