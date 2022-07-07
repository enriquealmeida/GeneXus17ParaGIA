export declare class GxgPill {
    el: HTMLElement;
    /**
     * The presence of this attribute disables the pillgit a
     */
    disabled: boolean;
    /**
     * The icon
     */
    icon: string;
    /**
     * The presence of this attribute sets auto-height. Usefull when the text overflows.
     */
    heightAuto: boolean;
    /**
     * The type of pill
     */
    type: PillType;
    removeButtonFunc(): void;
    iconType(): string;
    iconColor(): "disabled" | "success";
    render(): any;
}
export declare type PillType = "static" | "static-with-action" | "button" | "button-with-action";
