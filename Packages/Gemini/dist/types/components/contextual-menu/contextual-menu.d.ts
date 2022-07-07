export declare class GxgContextualMenu {
    constructor();
    /**
     * The presence of this attribute makes the menu visible
     */
    visible: boolean;
    widthOverflow: boolean;
    heightOverflow: boolean;
    firstRightClick: boolean;
    topPosition: number;
    leftPosition: number;
    private contextualMenuSizes;
    el: HTMLElement;
    watchHandler(newValue: boolean): void;
    detectClickOutsideMenu(event: any): void;
    saveMouseCoordinates(e: any): void;
    componentDidLoad(): void;
    componentDidUnload(): void;
    render(): any;
}
