export declare class GxgToolbar {
    /**
     * The toggle arrow position
     */
    position: position;
    /**
     * The toolbar title
     */
    subtitle: string;
    /**
     * The toolbar subtitle
     */
    toolbarTitle: string;
    /**
     * Reading direction
     */
    rtl: boolean;
    componentDidLoad(): void;
    render(): any;
}
export declare type position = "start" | "top" | "bottom";
