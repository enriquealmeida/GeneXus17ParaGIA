/* eslint-disable */
/* tslint:disable */
/**
 * This is an autogenerated file created by the Stencil compiler.
 * It contains typing information for all components that exist in this project.
 */
import { HTMLStencilElement, JSXBase } from "./stencil-public-runtime";
import { EditorConfiguration } from "./components/common/configuration";
export namespace Components {
    interface GxDsoStylesEditor {
        "configuration": EditorConfiguration;
        "dispose": () => Promise<void>;
        "getTextEditor": () => Promise<any>;
        "monaco": any;
        "readonly": boolean;
        "readonlymessage": string;
        "usePlatformConfiguration": boolean;
        "value": string;
    }
}
declare global {
    interface HTMLGxDsoStylesEditorElement extends Components.GxDsoStylesEditor, HTMLStencilElement {
    }
    var HTMLGxDsoStylesEditorElement: {
        prototype: HTMLGxDsoStylesEditorElement;
        new (): HTMLGxDsoStylesEditorElement;
    };
    interface HTMLElementTagNameMap {
        "gx-dso-styles-editor": HTMLGxDsoStylesEditorElement;
    }
}
declare namespace LocalJSX {
    interface GxDsoStylesEditor {
        "configuration"?: EditorConfiguration;
        "monaco"?: any;
        "readonly"?: boolean;
        "readonlymessage"?: string;
        "usePlatformConfiguration"?: boolean;
        "value"?: string;
    }
    interface IntrinsicElements {
        "gx-dso-styles-editor": GxDsoStylesEditor;
    }
}
export { LocalJSX as JSX };
declare module "@stencil/core" {
    export namespace JSX {
        interface IntrinsicElements {
            "gx-dso-styles-editor": LocalJSX.GxDsoStylesEditor & JSXBase.HTMLAttributes<HTMLGxDsoStylesEditorElement>;
        }
    }
}
