export declare class GxgButtonGroup {
    el: HTMLElement;
    /*********************************
    PROPERTIES & STATE
    *********************************/
    /**
    The button-group title
    */
    buttonGroupTitle: string;
    /**
    The button group title alignment
    */
    titleAlignment: TitleAlignment;
    /**
    The id of the button that you would like to be active by default
    */
    defaultSelectedBtnId: string;
    /**
    Wether the button group is disabled or not
    */
    disabled: boolean;
    /**
     * The presence of this attribute makes the component full-width
     */
    fullWidth: boolean;
    /**
     * The presence of this attribute makes the button group outlined
     */
    outlined: boolean;
    /**
    The value of the current selected button
    */
    value: string;
    /**
     * Reading direction
     */
    rtl: boolean;
    componentDidLoad(): void;
    /*********************************
    METHODS
    *********************************/
    componentWillLoad(): void;
    setActiveButton(event: MouseEvent): void;
    setInitialActiveValue(): void;
    detectFocus(event: any): void;
    tabIndex(): "0" | "-1";
    render(): any;
}
export declare type TitleAlignment = "left" | "center" | "right";
