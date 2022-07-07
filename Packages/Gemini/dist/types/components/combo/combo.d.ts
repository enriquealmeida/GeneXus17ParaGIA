import { IconPosition } from "../form-text/form-text";
export declare class GxgCombo {
    textInput: HTMLInputElement;
    el: HTMLElement;
    /**
     * The combo width
     */
    width: string;
    itemsNodeList: NodeList;
    selectedItemValue: string;
    inputTextValue: string;
    showItems: boolean;
    inputTextIcon: string;
    inputTextIconPosition: IconPosition;
    noMatch: boolean;
    slottedContent: HTMLElement;
    componentWillLoad(): void;
    itemClickedHandler(event: any): void;
    onInputGxgformText(e: any): void;
    onKeyDownGxgformText(e: any): void;
    toggleItems(): void;
    detectClickOutsideCombo(event: any): void;
    componentDidLoad(): void;
    componentDidUnload(): void;
    selecteItemFunc(): string;
    onInputFocus(): void;
    clearInputFunc(): void;
    render(): any;
}
