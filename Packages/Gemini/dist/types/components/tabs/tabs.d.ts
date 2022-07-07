export declare class GxgTabs {
    element: HTMLGxgTabsElement;
    position: TabsPosition;
    height: Height;
    activeTab: string;
    tabActivatedHandler(event: any): void;
    updateActiveChildren(activeTab: string, tagName: string): void;
    render(): any;
}
export declare type TabsPosition = "top" | "bottom" | "left" | "right";
export declare type Height = "auto" | "100%";
