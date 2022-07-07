export declare class GxgMenu {
    el: HTMLElement;
    /**
     * The menu title
     */
    menuTitle: string;
    /**
     * Provide this attribute if you are using this menu on the tabs component
     */
    tabs: boolean;
    menuItemActiveHandler(event: CustomEvent): void;
    printTitle(): any;
    render(): any;
}
