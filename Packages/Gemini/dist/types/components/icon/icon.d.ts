export declare class GxgIcon {
    element: HTMLElement;
    /*********************************
    PROPERTIES & STATE
    *********************************/
    /**
     * The color of the icon.
     *
     */
    color: Color;
    /**
     * The size of the icon. Possible values: regular, small.
     */
    size: Size;
    /**
     * The type of icon.
     */
    type: any;
    /*********************************
    METHODS
    *********************************/
    getSrcPath(): string;
    iconSize(): "16px" | "12px";
    render(): any;
    private mapColorToCssVar;
}
export declare type Color = "primary-enabled" | "primary-active" | "primary-hover" | "onbackground" | "negative" | "disabled" | "ondisabled" | "error" | "success" | "warning" | "alwaysblack" | "auto";
export declare type Size = "regular" | "small";
