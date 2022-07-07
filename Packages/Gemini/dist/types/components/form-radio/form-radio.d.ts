import { EventEmitter } from "../../stencil-public-runtime";
export declare class GxgFormRadio {
    /**
     * Styles the radio-button with error attributes
     */
    error: boolean;
    /**
     * Returns an object with the radio value, and radio id
     */
    change: EventEmitter;
    /**
     * (This event is for internal use)
     */
    changeInternal: EventEmitter;
    /**
     * (This event is for internal use)
     */
    keyPressed: EventEmitter;
    el: HTMLElement;
    radioInput: HTMLInputElement;
    /*********************************
    PROPERTIES & STATE
    *********************************/
    /**
     * The radio id
     */
    RadioId: string;
    /**
     * The presence of this attribute makes the radio selected by default
     */
    checked: boolean;
    /**
     * The presence of this attribute disables the radio
     */
    disabled: boolean;
    /**
     * The radio label
     */
    label: string;
    /**
     * The radio name (should be the same for every radio of the same radio-group)
     */
    name: string;
    /**
     * The radio value
     */
    value: string;
    /*********************************
    METHODS
    *********************************/
    componentDidLoad(): void;
    watchHandler(newValue: boolean): void;
    selectRadio(): void;
    handlerOnKeyDown(event: any): void;
    render(): any;
}
