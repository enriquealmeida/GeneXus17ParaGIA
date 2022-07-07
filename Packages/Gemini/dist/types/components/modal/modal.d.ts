export declare class GxgModal {
    el: HTMLElement;
    padding: padding;
    /**
     * The footer justify content type
     */
    footerJustifyContent: string;
    /**
     * The modal title
     */
    modalTitle: string;
    /**
     * The modal width
     */
    width: string;
    /**
     * Wether the modal is visible or not
     */
    visible: boolean;
    /**
     * The z-index value of the modal
     */
    zIndex: string;
    /**
     * The presence of this attribute removes the sound that plays when the modal appears
     */
    silent: boolean;
    layerVisible: boolean;
    modalVisible: boolean;
    modalTransition: boolean;
    componentDidLoad(): void;
    closeModal(): void;
    watchVisibleHandler(): void;
    modalHidden(): "true" | "false";
    render(): any;
}
export declare type padding = "0" | "xs" | "s" | "m" | "l" | "xl" | "xxl" | "xxxl";
export declare type footerJustifyContent = "flex-end" | "space-between";
