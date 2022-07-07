export declare class GxgTabBar {
    constructor();
    el: HTMLElement;
    tabBarMenu: HTMLElement;
    appendedButtons: number;
    tabBarMenuHeight: string;
    /**
     * Reading direction
     */
    rtl: boolean;
    toggleMenu(event: any): void;
    tabActivatedHandler(): void;
    appendTabItemsToMenu(): void;
    componentDidLoad(): void;
    setIndexToTabButtons(): void;
    renderTabBarMenu(): any;
    detectClickOutsideTabBarMenu(event: any): void;
    componentDidUnload(): void;
    render(): any;
}
