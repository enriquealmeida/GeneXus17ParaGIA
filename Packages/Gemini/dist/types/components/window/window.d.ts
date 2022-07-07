export declare class GxgWindow {
    /**
     * The window starting height
     */
    initialHeight: string;
    /**
     * The window starting width
     */
    initialWidth: string;
    /**
     * The window min. height
     */
    minHeight: string;
    /**
     * The window min. width
     */
    minWidth: string;
    /**
     * The window max. height
     */
    maxHeight: string;
    /**
     * The window max. width
     */
    maxWidth: string;
    /**
     * The window left position. By default the window is horizontally centered
     */
    leftPosition: string;
    /**
     * The window top position. By default the window is vertically centered
     */
    topPosition: string;
    /**
     * The window title
     */
    windowTitle: string;
    /**
     * The window title icon
     */
    titleIcon: string;
    /**
     * Displays the window
     */
    displayWindow: boolean;
    showWindow: boolean;
    customPosition: boolean;
    el: HTMLElement;
    header: HTMLElement;
    draggableResizableDiv: HTMLElement;
    dragElement(elm: any, mainElm: any): void;
    resizeElement(elm: any, mainElm: any): void;
    componentWillLoad(): void;
    componentDidLoad(): void;
    closeWindow(): void;
    displayWindowHandler(): void;
    setInitialWidth(): string;
    setInitialHeight(): string;
    render(): any;
}
