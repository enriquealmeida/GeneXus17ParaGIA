import { EventEmitter } from "../../stencil-public-runtime";
export declare class GxgFormCheckbox {
    el: HTMLElement;
    checkboxInput: HTMLInputElement;
    /*********************************
    PROPERTIES & STATE
    *********************************/
    /**
     * The checkbox id
     */
    checkboxId: string;
    /**
     * The presence of this attribute makes the checkbox checked by default
     */
    checked: boolean;
    /**
     * The presence of this attribute makes the checkbox indeterminate
     */
    indeterminate: boolean;
    /**
     * The presence of this attribute disables the checkbox
     */
    disabled: boolean;
    /**
     * The checkbox label
     */
    label: string;
    /**
     * The checkbox value
     */
    value: string;
    /**
     * The checkbox name
     */
    name: string;
    change: EventEmitter;
    /*********************************
    METHODS
    *********************************/
    compontentDidLoad(): void;
    changed(): void;
    handlerOnKeyUp(event: any): void;
    ariaChecked(): "true" | "false";
    handleInputClick(e: any): void;
    render(): any;
}
