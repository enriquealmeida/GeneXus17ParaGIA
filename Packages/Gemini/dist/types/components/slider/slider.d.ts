export declare class GxgSlider {
    el: HTMLElement;
    /**
     * The state of the slider, whether is disabled or not.
     */
    disabled: boolean;
    /**
     * The label
     */
    label: string;
    /**
     * The max. value
     */
    max: number;
    /**
     * The initial value
     */
    value: number;
    /**
     * The slider max. width
     */
    maxWidth: string;
    /**
     * Reading direction
     */
    rtl: boolean;
    watchHandler(): void;
    componentDidLoad(): void;
    updateLabel(): void;
    updateBoxValue(): void;
    calculateWidth(): number;
    rangeSliderChanged(): void;
    render(): any;
}
export declare type type = "percentual" | "numeric";
