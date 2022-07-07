export declare class GxgProgressBar {
    /**
     * The state of the progress-bar, whether it is disabled or not.
     */
    disabled: boolean;
    /**
     * The progress-bar label
     */
    label: string;
    /**
     * The progress value (percentage)
     */
    value: number;
    /**
     * The max. width
     */
    maxWidth: string;
    /**
     * The presence of this attribute removes the sound that plays when the progress-bar finishes
     */
    silent: boolean;
    watchValue(): void;
    render(): any;
}
