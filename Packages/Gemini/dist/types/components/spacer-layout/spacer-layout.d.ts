export declare class GxgSpacerLayout {
    /**
     * Add this attribute to make the spacer-layout full height
     */
    fullHeight: boolean;
    /**
     * The spacing value, taken from the "token-spacing" global values
     */
    space: Space;
    /**
     * The orientation
     */
    orientation: Orientation;
    /**
     * Content justify
     */
    justifyContent: JustifyContent;
    render(): any;
}
export declare type Space = "xs" | "s" | "m" | "l" | "xl";
export declare type Orientation = "horizontal" | "vertical";
export declare type JustifyContent = "start" | "end" | "center" | "space-between" | "space-around";
