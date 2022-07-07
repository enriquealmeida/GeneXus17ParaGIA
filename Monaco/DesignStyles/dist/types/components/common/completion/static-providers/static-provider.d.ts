export declare class StaticProvider {
    private baseSuggestions;
    private importSuggestions;
    private allTokenGroupsSugestions;
    private starterSuggestion;
    private mediaSuggestion;
    private allCSSProperties;
    private allGXProperties;
    private allGXPropertiesSuggestions;
    private cssPropertyValues;
    private monaco;
    private configuration;
    private editor;
    private dynamicProvider;
    constructor(monaco: any, configuration: any, editor: any, dynamicProvider: any);
    getBaseSuggestions(): any;
    getImportSuggestions(): any;
    getAllTokenGroupsSugestions(hasDollar: any, removeMedia: any): any;
    getStarterSuggestion(): any;
    getCssPropertyValues(propName: any): any[];
    getGxPropertyValues(propName: any): Promise<any[]>;
    getAllCSSPredefinedProperties(): Promise<any>;
    getAllCSSProperties(): Promise<any>;
    getMediaSuggestion(hasDollar: any): any;
}
