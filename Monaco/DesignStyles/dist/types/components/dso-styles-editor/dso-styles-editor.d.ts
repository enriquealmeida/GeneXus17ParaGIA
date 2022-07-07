import { EditorConfiguration } from '../common/configuration';
export declare class DSOStylesEditor {
    monaco: any;
    usePlatformConfiguration: boolean;
    value: string;
    configuration: EditorConfiguration;
    readonlymessage: string;
    readonly: boolean;
    element: HTMLElement;
    private completionItemProvider;
    private codeLensesProvider;
    private codeLensCommandId;
    private static readonly language;
    private grammars;
    dispose(): Promise<void>;
    getTextEditor(): Promise<any>;
    editorValueChangedListener(): void;
    componentWillRender(): void;
    render(): any;
    private _getValue;
    private _setConfiguration;
    private _getCompletionItemProvider;
    private _getEditor;
    private _getCodeLensProvider;
    private _getActions;
}
