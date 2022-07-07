import { EventEmitter } from "../../stencil-public-runtime";
import { Color } from "../icon/icon";
export declare class GxgTreeItem {
    checkboxInput: HTMLInputElement;
    /**
     * Set this attribute if you want the gxg-treeitem to display a checkbox
     */
    checkbox: boolean;
    /**
     * Set this attribute if you want the gxg-treeitem checkbox to be checked by default
     */
    checked: boolean;
    /**
     * Set this attribute if this tree-item has a resource to be downloaded;
     */
    download: boolean;
    /**
     * Set this attribute when you are downloading a resource
     */
    downloading: boolean;
    /**
     * Set this attribute when you have downloaded the resource
     */
    downloaded: boolean;
    /**
     * Set the left side icon from the available Gemini icon set : https://gx-gemini.netlify.app/?path=/story/icons-icons--controls
     */
    leftIcon: string;
    /**
     * Set thhe right side icon from the available Gemini icon set : https://gx-gemini.netlify.app/?path=/story/icons-icons--controls
     */
    rightIcon: string;
    /**
     * If this tree-item has a nested tree, set this attribute to make the tree open by default
     */
    opened: boolean;
    /**
     * The presence of this attribute sets the tree-item as selected
     */
    selected: boolean;
    /**
     * The presence of this attribute displays a +/- icon to toggle/untoggle the tree
     */
    isLeaf: boolean;
    hasChildTree: boolean;
    firstTreeItem: boolean;
    indeterminate: boolean;
    disabled: boolean;
    numberOfParentTrees: number;
    itemPaddingLeft: any;
    horizontalLinePaddingLeft: number;
    lastTreeItem: boolean;
    firstTreeItemOfParentTree: boolean;
    lastTreeItemOfParentTree: boolean;
    rightIconColor: Color;
    numberOfDirectTreeItemsDescendants: number;
    liItemClicked: EventEmitter;
    toggleIconClicked: EventEmitter;
    checkboxClickedEvent: EventEmitter;
    el: HTMLElement;
    componentWillLoad(): void;
    getNumberOfVisibleDescendants(): void;
    setVisibleTreeItems(): void;
    componentDidLoad(): void;
    watchHandler(newValue: boolean): void;
    getParents(elem: any): number;
    toggleTreeIconClicked(): void;
    updateTreeVerticalLineHeight(): Promise<void>;
    liTextClicked(): void;
    liTextDoubleClicked(): void;
    liTextKeyDownPressed(e: any): void;
    returnToggleIconType(): "gemini-tools/add" | "gemini-tools/minus";
    returnPaddingLeft(): string;
    returnVerticalLineLeftPosition(): string;
    checkboxTabIndex(): number;
    liTextTabIndex(): number;
    setIndeterminate(): boolean;
    checkboxClicked(): void;
    toggleTreeItemsCheckboxes(checked: any): void;
    render(): any;
}
