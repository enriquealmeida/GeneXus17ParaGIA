import { EventEmitter } from "../../stencil-public-runtime";
import { FormComponent } from "../../common";
export declare class GxgFormTextarea implements FormComponent {
    isRequiredError: boolean;
    textArea: HTMLTextAreaElement;
    /*********************************
    PROPERTIES & STATE
    *********************************/
    /**
     * The presence of this attribute makes the textarea disabled
     */
    disabled: boolean;
    /**
     * The presence of this attribute gives the component error styles
     */
    error: boolean;
    /**
     * The required message if this input is required and no value is provided (optional). If this is not provided, the default browser required message will show up
     */
    requiredMessage: string;
    /**
     * The textarea label
     */
    label: string;
    /**
     * The max-width
     */
    maxWidth: string;
    /**
     * The textarea placeholder
     */
    placeholder: string;
    /**
     * The presence of this attribute makes the textarea required
     */
    required: boolean;
    /**
     * The textarea value
     */
    value: string;
    /**
     * The number of rows
     */
    rows: number;
    /**
     * The presence of this attribute gives the component warning styles
     */
    warning: boolean;
    /**
     * Returns the textarea value
     */
    input: EventEmitter;
    /**
     * Returns the textarea value
     */
    change: EventEmitter;
    /*********************************
    METHODS
    *********************************/
    updateTextareaValue(): void;
    handleInput(e: any): void;
    handleChange(e: any): void;
    render(): any;
}
