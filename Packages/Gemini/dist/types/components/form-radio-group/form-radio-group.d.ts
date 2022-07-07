export declare class GxgFormRadioGroup {
    isRequiredError: boolean;
    el: HTMLElement;
    /*********************************
    PROPERTIES & STATE
    *********************************/
    /**
     * The radio group label
     */
    label: string;
    /**
     * The required message if this input is required and no value is provided (optional). If this is not provided, the default browser required message will show up
     *
     */
    requiredMessage: string;
    /**
     * Make the radio-buttons required
     */
    required: boolean;
    /*********************************
    METHODS
    *********************************/
    keyPressedHandler(event: CustomEvent): void;
    radioClickedHandler(event: CustomEvent): void;
    componentDidLoad(): void;
    labelFunc(): any;
    render(): any;
}
