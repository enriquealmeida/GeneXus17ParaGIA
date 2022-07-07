import { EditorConfiguration } from '../configuration';
export declare class DSOCodeLensesProvider {
    private monaco;
    private editor;
    private configuration;
    private language;
    constructor(monaco: any, editor: any, configuration: EditorConfiguration, language: string);
    getCodeLensProvider(commandId: string): {
        provideCodeLenses: (model: any, token: any) => Promise<{
            lenses: any[];
            dispose: () => void;
        }>;
        resolveCodeLens: (model: any, codeLens: any, token: any) => any;
    };
    private static _getRerencesLabel;
    getCodeLensCommand(): (ctx: any, parms: any) => void;
}
export declare class DSOCodeLensesIDs {
    static readonly MY_CONTENT_WIDGET_ID = "my.content.widget";
    static readonly REFERENCES_POPUP_ID = "ReferencesPopup";
}
export declare class DSOCodeLensesGemini {
    static readonly GXG_FILTER = "gxg-filter";
    static readonly GXG_FILTER_ITEM = "gxg-filter-item";
    static readonly TYPE = "type";
    static readonly ICON = "icon";
    static readonly ITEM_TYPE = "item-type";
    static readonly ITEM_TEXT = "item-text";
    static readonly ITEM_CLICKED_EVENT = "itemClickedEvent";
}
