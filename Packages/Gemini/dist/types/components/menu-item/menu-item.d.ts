import { EventEmitter } from "../../stencil-public-runtime";
export declare class GxgMenuItem {
    listItem: HTMLElement;
    label: string;
    icon: string;
    active: boolean;
    menuItemActive: EventEmitter;
    includeIcon(): any;
    setActive(): void;
    render(): any;
}
