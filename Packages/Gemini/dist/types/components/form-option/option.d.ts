import { EventEmitter } from "../../stencil-public-runtime";
export declare class GxgFormOption {
    /**
     * The value
     */
    value: string;
    /**
     * The presence of this attribute makes the option selected by default
     */
    selected: boolean;
    optionIsSelected: EventEmitter;
    watchHandler(newValue: boolean): void;
    render(): any;
}
