import { EventEmitter } from "../../stencil-public-runtime";
import { FormComponent } from "../../common";
export declare class GxgFormText implements FormComponent {
    isRequiredError: boolean;
    textInput: HTMLInputElement;
    /*********************************
    PROPERTIES & STATE
    *********************************/
    /**
     * The presence of this attribute displays a clear (cross) button-icon on the right side
     */
    clearButton: boolean;
    /**
     * The presence of this attribute makes the input disabled
     */
    disabled: boolean;
    /**
     * The presence of this attribute gives the component error styles
     */
    error: boolean;
    /**
     * The input icon (optional)
     */
    icon: any;
    /**
     * The input icon side
     */
    iconPosition: IconPosition;
    /**
     * The input label
     */
    label: string;
    /**
     * The input label
     */
    labelPosition: LabelPosition;
    /**
     * The presence of this attribute hides the border, and sets the background to transparent when the element has no focus
     */
    minimal: boolean;
    /**
     * The presence of this attribute sets the text color to white. Usefull when "minimal" attribute is applied and the background behind the input is dark
     */
    overDarkBackground: boolean;
    /**
     * The input placeholder
     */
    placeholder: string;
    /**
     * The presence of this attribute makes this input required
     */
    required: boolean;
    /**
     * The required message if this input is required and no value is provided (optional). If this is not provided, the default browser required message will show up
     *
     */
    requiredMessage: string;
    /**
     * The input value
     */
    value: string;
    /**
     * The presence of this attribute gives the component warning styles
     */
    warning: boolean;
    /**
     * The input max. width
     */
    maxWidth: string;
    /**
     * The text style
     */
    textStyle: Style;
    /**
     * The presence of this attribute sets the input type as password
     */
    password: boolean;
    el: HTMLElement;
    /**
     * Returns the input value
     */
    input: EventEmitter;
    /**
     * Returns the input value
     */
    change: EventEmitter;
    /**
     * The clear button was clicked
     */
    clearButtonClicked: EventEmitter;
    cursorInside: boolean;
    inputSize: string;
    mouseCoordinates: object;
    /**
     * Reading direction
     */
    rtl: boolean;
    watchHandler(newValue: any, oldValue: any): void;
    /*********************************
    METHODS
    *********************************/
    iconPositionFunc(): IconPosition;
    inputIcon(): any;
    handleInput(e: any): void;
    handleChange(e: any): void;
    clearButtonFunc(): void;
    updateGhostSpan(): void;
    mouseEnterHandler(): void;
    mouseOutHandler(): void;
    mouseMoveHandler(e: any): void;
    componentDidLoad(): void;
    componentDidUnload(): void;
    /*********************************
    LISTEN
    *********************************/
    handleFocus(focusEvent: Event): void;
    type(): "text" | "password";
    render(): any;
}
export declare type IconPosition = "start" | "end";
export declare type LabelPosition = "start" | "above";
export declare type Style = "regular" | "quote" | "title-01" | "title-02" | "title-03" | "title-04" | "title-05";
