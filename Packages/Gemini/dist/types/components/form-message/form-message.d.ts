export declare class GxgFormMessage {
    /**
     * The type of message
     */
    type: Message;
    iconColor(): "error" | "warning";
    iconType(): "gemini-tools/error" | "gemini-tools/warning";
    render(): any;
}
export declare type Message = "error" | "warning";
