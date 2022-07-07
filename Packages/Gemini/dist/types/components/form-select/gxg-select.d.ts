import { EventEmitter } from "../../stencil-public-runtime";
export declare class GxgFormSelect {
    slottedContent: HTMLCollection;
    select: HTMLElement;
    el: HTMLElement;
    /********************************
    PROPERTIES & STATE
    *********************************/
    /**
     * The presence of this attribute disables the component
     */
    disabled: boolean;
    /**
     * The presence of this attribute stylizes the component with error attributes
     */
    error: boolean;
    /**
     * The select label
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
     * The presence of this attribute makes this input required
     */
    required: boolean;
    /**
     * The maximum number of visible options
     */
    size: string;
    /**
     * This holds the value of the selected option
     */
    value: string;
    /**
     * The presence of this attribute stylizes the component with warning attributes
     */
    warning: boolean;
    /**
     * The select max. width
     */
    maxWidth: string;
    /**
     * Returns the value of the selected option
     */
    change: EventEmitter;
    /**
     * Reading direction
     */
    rtl: boolean;
    rerender: boolean;
    todoCompletedHandler(event: any): void;
    /*********************************
    METHODS
    *********************************/
    clickTest(event: MouseEvent, listOfOptions: any, updateValue: any): void;
    componentDidLoad(): void;
    selectCore(): void;
    valueHandler(newValue: any, oldValue: any): void;
    handlerOnKeyDown(event: any): void;
    render(): any;
}
export declare type LabelPosition = "start" | "above";
