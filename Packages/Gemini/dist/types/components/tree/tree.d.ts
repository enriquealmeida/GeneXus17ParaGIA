export declare class GxgTree {
    el: HTMLElement;
    ulTree: HTMLElement;
    /**
     * Set this attribute if you want all this tree tree-items to have a checkbox
     */
    checkbox: boolean;
    /**
     * Set this attribute if you want all this tree tree-items to have the checkbox checked
     */
    checked: boolean;
    /**
     * Set this attribute if you want all the childen item's checkboxes to be checked when the parent item checkbox is checked, or to be unchecked when the parent item checkbox is unckecked.
     */
    toggleCheckboxes: boolean;
    nestedTree: boolean;
    mainTree: boolean;
    componentWillLoad(): void;
    componentDidLoad(): void;
    liItemClickedHandler(): void;
    toggleIconClickedHandler(): void;
    checkboxClickedEventHandler(): void;
    render(): any;
}
