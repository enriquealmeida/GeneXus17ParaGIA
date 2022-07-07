import { EventEmitter } from "../../stencil-public-runtime";
export declare class GxgSplitter {
    /**
     * This event is fired when the gutter is being dragged
     */
    dragging: EventEmitter;
    /**
     * This event is fired when the dragging has stopped
     */
    dragEnded: EventEmitter;
    el: HTMLElement;
    /**
     * The splitter direction
     */
    direction: Direction;
    /**
     * The splitter direction
     */
    forceCollapseZero: boolean;
    /**
     * The type of knob
     */
    knob: Knob;
    /**
     * The splitter min. sizes in pixels
     */
    minSize: string;
    /**
     * The splitter initial sizes, in percentages. The sum should equal 100
     */
    sizes: string;
    split: any;
    minSizeArray: Array<number>;
    sizesArray: Array<number>;
    idsArray: Array<string>;
    currentSizes: number;
    leftSplitReachedMin: boolean;
    rightSplitReachedMin: boolean;
    collapsedToZero: boolean;
    makeId(length: any): string;
    componentWillLoad(): void;
    componentDidLoad(): void;
    convertStringPropertiesToArray(): void;
    getIds(): void;
    validateSizes(): void;
    knobLeftClicked(): void;
    knobRightClicked(): void;
    knobLeftOver(): void;
    knobLeftOut(): void;
    knobRightOver(): void;
    knobRightOut(): void;
    onDragStartFunc(): void;
    onDragFunc(): void;
    onDragEndFunc(): void;
    detectDragEndReachedMinimum(): void;
    /**
     * This method allows to collapse the split passsed as argument
     */
    collapse(split: number, forceCollapseZero?: boolean): Promise<void>;
    render(): any;
}
export declare type Direction = "horizontal" | "vertical";
export declare type Knob = "bidirectional" | "none";
