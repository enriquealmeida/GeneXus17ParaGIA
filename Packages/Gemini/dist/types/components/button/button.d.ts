import { Color } from "../icon/icon";
import { Size } from "../icon/icon";
export declare class GxgButton {
    el: HTMLElement;
    button: HTMLButtonElement;
    /*********************************
    PROPERTIES & STATE
    *********************************/
    /**
     * The prescence of this attribute makes the icon always black
     */
    alwaysBlack: boolean;
    /**
     * The state of the button, whether it is disabled or not
     */
    disabled: boolean;
    /**
     * The presence of this attribute makes the component full-width
     */
    fullWidth: boolean;
    /**
     * The button icon
     */
    icon: any;
    /**
     * The prescence of this attribute turns the icon white
     */
    negative: boolean;
    /**
     * The kind of button
     */
    type: ButtonType;
    /**
     * The presence of this attribute lets the button styles be editable from outside of the component by referencing the "native-button" part.
     */
    buttonStylesEditable: boolean;
    mouseEnter: boolean;
    focusIn: boolean;
    /*********************************
    METHODS
    *********************************/
    componentDidLoad(): void;
    ghostIcon(): any;
    regularIcon(): any;
    iconSize(): Size;
    iconColor(): Color;
    clickHandler(e: any): void;
    handleFocus(focusEvent: Event): void;
    onMouseEnter(): void;
    onMouseLeave(): void;
    onFocusIn(): void;
    onFocusOut(): void;
    render(): any;
}
export declare type ButtonType = "primary-text-only" | "primary-text-icon" | "primary-icon-only" | "secondary-text-only" | "secondary-text-icon" | "secondary-icon-only" | "outlined" | "tertiary";
