export declare class GxgDatePicker {
    el: HTMLElement;
    /**
     * The presence of this attribute makes the date-picker always visible
     */
    alwaysShow: boolean;
    /**
     * initial date
     */
    defaultDate: string;
    /**
     * The datepicker label
     */
    label: string;
    /**
     * no weekends available
     */
    noWeekends: boolean;
    /**
     * The min. date
     */
    minDate: string;
    /**
     * The max. date
     */
    maxDate: string;
    /**
     * The max. width
     */
    maxWidth: string;
    /**
     * Reading direction
     */
    rtl: boolean;
    componentDidLoad(): void;
    printLabel(): any;
    render(): any;
}
