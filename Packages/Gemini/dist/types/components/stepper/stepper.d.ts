import { EventEmitter } from "../../stencil-public-runtime";
export declare class GxgStepper {
    /*********************************
    PROPERTIES & STATE
    *********************************/
    el: HTMLElement;
    plusButton: HTMLButtonElement;
    minusButton: HTMLButtonElement;
    /**
     * The state of the stepper, whether is disabled or not.
     */
    disabled: boolean;
    /**
     * The label
     */
    label: string;
    /**
     * The label position
     */
    labelPosition: LabelPosition;
    /**
     * The initial vaule
     */
    value: number;
    /**
     * The max. value
     */
    max: number;
    /**
     * The min. value
     */
    min: number;
    stepperInput: EventEmitter;
    /**
     * Reading direction
     */
    rtl: boolean;
    /*********************************
    METHODS
    *********************************/
    componentDidLoad(): void;
    minus(): void;
    plus(): void;
    watchHandler(newValue: any): void;
    render(): any;
}
export declare type LabelPosition = "start" | "above";
