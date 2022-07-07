export declare class GxgToolbarItem {
    /**
     * The state of the toolbar-item, whether it is disabled or not
     */
    disabled: boolean;
    /**
     * The toolbar-item icon
     */
    icon: string;
    /**
     * The toolbar-item subtitle
     */
    subtitle: string;
    /**
     * The toolbar-item title
     */
    toolbarItemTitle: string;
    includeIcon(): any;
    tabIndex(): "-1" | "1";
    render(): any[];
}
