import { EventEmitter } from "../../stencil-public-runtime";
export declare class GxgBreadcrumb {
    el: HTMLElement;
    /**
     * This event emmits the breadcrumb index
     */
    breadcrumbClicked: EventEmitter;
    /**
     * The breadcrumb icon (optional)
     */
    icon: string;
    breadcrumbClickedFunc(): void;
    render(): any;
}
