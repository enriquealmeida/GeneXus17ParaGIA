import { EventEmitter } from "../../stencil-public-runtime";
export declare class GxgColorPicker {
    element: HTMLElement;
    private pickr;
    /**
    The label of the color picker (optional)
    */
    label: string;
    /**
    The color value, such as "red", #CCDDEE, or rgba(220,140,40,.5)
    */
    value: string;
    colorRepresentation: "HEXA" | "RGBA";
    colorInputValue: string;
    colorObject: any;
    save: EventEmitter;
    nameInputEvent: EventEmitter;
    change: EventEmitter;
    private colorChangedFromInput;
    componentDidLoad(): void;
    componentDidUnload(): void;
    watchHandler(newValue: any): void;
    handleHexaButtonClick(): void;
    handleRgbaButtonClick(): void;
    handleTitleValueChange(ev: InputEvent): void;
    handleColorValueChange(ev: InputEvent): void;
    colorValue(): any;
    setActiveButton(): "rgba" | "hexa";
    render(): any;
}
