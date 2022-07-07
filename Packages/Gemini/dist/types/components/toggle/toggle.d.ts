import { EventEmitter } from "../../stencil-public-runtime";
export declare class GxgToggle {
    el: HTMLElement;
    /*********************************
    PROPERTIES & STATE
    *********************************/
    /**
     * The state of the toggle. Whether is disabled or not.
     */
    disabled: boolean;
    /**
     * The label
     */
    label: string;
    /**
     * If the toggle is active or not
     */
    on: boolean;
    /*********************************
    EVENTS
    *********************************/
    /**
     * This event is triggered when the toggle is switched. 'event.detail' will display true when the toggle is on, or false when the toggle is off.
     */
    toggleSwitched: EventEmitter;
    /*********************************
    METHODS
    *********************************/
    componentWillLoad(): void;
    onKeyUp(e: any): void;
    switchToggle(): void;
    state(): "true" | "false";
    render(): any;
}
