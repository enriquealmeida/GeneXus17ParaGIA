export declare class GxgFilter {
    el: HTMLElement;
    /**
     * The top position of the filter, relative to the closest parent with relative position. (optional)
     */
    top: any;
    /**
     * The left position of the filter, relative to the closest parent with relative position. (optional)
     */
    left: any;
    itemsNodeList: NodeList;
    componentWillLoad(): void;
    onInputGxgformText(e: any): void;
    closeFilter(): void;
    handleItemClickedEvent(): void;
    render(): any;
}
