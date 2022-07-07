export declare class GxgText {
    /**
     * The href (for "link" or "link-gray" types
     */
    href: string;
    /**
     * The target (for "link" or "link-gray" types
     * */
    target: TargetType;
    /**
     * Title type
     */
    type: TextType;
    textType(): any;
    render(): any;
}
export declare type TextType = "text-regular" | "text-gray" | "text-quote" | "text-link" | "text-link-gray" | "text-alert-error" | "text-alert-warning" | "text-alert-success";
export declare type TargetType = "_self" | "_blank";
