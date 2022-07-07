import { EditorConfiguration } from "../configuration";
export declare class DSOCompletionItemProvider {
    private monaco;
    private getEditor;
    private configuration;
    constructor(monaco: any, getEditor: Function, configuration: EditorConfiguration);
    getCompletionItemProvider: () => {
        provideCompletionItems(model: any, position: any): Promise<{
            suggestions: any;
        }>;
        triggerCharacters: string[];
    };
    caluclateSuggestionsTest(completeText: any): Promise<any>;
    private checkIfTest;
    private _calculateSuggestions;
}
