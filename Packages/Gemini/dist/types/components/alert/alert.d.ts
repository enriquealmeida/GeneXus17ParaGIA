export declare class GxgAlert {
    el: HTMLElement;
    /*********************************
    PROPERTIES & STATE
    *********************************/
    /**
     * Wether the alert is active (visible) or hidden
     */
    active: boolean;
    /**
     * The amount of time the alert is visible before hidding under the document
     */
    activeTime: ActiveTime;
    /**
     * The alert position on the X axis
     */
    position: AlertPosition;
    /**
     * The alert title (optional)
     */
    title: string;
    /**
     * The alert flavor
     */
    type: AlertType;
    /**
     * The presence of this attribute makes the component full-width
     */
    fullWidth: boolean;
    /**
     * The spacing between the alert, and the left or right side of the document
     */
    leftRight: Spacing;
    /**
     * The spacing between the alert and the bottom side of the document
     */
    bottom: Spacing;
    /**
     * The alert width
     */
    width: string;
    /**
     * The presence of this attribute removes the sound on the 'warning' or 'error' alert
     */
    silent: boolean;
    /**
     * Reading direction
     */
    rtl: boolean;
    /*********************************
    METHODS
    *********************************/
    componentDidLoad(): void;
    iconColor(): "onbackground" | "negative" | "error" | "success" | "warning";
    closeIconColor(): "onbackground" | "negative";
    printTitle(): any;
    setAlertInactive(): void;
    validateName(newValue: boolean): void;
    defineWidth(): string;
    alertHidden(): "true" | "false";
    transform(bottomSpacingValue: any): string;
    render(): any;
}
export declare type AlertType = "notice" | "error" | "warning" | "success";
export declare type AlertPosition = "start" | "center" | "end";
export declare type ActiveTime = "xxslow" | "xslow" | "slow" | "regular" | "fast" | "xfast" | "xxfast";
export declare type Spacing = "no-space" | "xs" | "s" | "m" | "l" | "xl";
