import { TextEditorConfiguration, DesignSystemTokensTextEditor, EditorHandlers  } from 'designtokenstexteditor';

declare class DesignSystemTokens {
    public static initialize(configuration:DesignSystemTokensConfiguration):TextEditorConfiguration;
    public static setValue(configuration:TextEditorConfiguration, value:string):void;
    public static getValue(editor:any):string;
    public static setViewMode(configuration:TextEditorConfiguration, viewMode:string):void;
}

declare class DesignSystemTokensContainer {
    public textEditor:any;
    public visualEditor:any;
}

declare class DesignSystemTokensConfiguration {
    public container:any;
    public setListeners: boolean;
    public textualEditorConfiguration: TextEditorConfiguration;
    public previewEditorConfiguration: PreviewConfiguration;
}

declare class DesignTokensPreview{
    public static initialize(configuration:PreviewConfiguration):any;
    public static renderFromSource(preview:any, source:string, isTokenDeleted:boolean):any;
    public static render(preview:any, source:any, isTokenDeleted:boolean):any;
    public static focusToken(preview:any, tokenId:string, tokenGroupd:string):any;
}

declare class PreviewConfiguration{
    public previewContainer:string;
    public textualEditor:any;
    public customResourcesPath:string;
    public shouldDefineCustomElements: boolean;
    public showDemo: boolean;
    public readonly: boolean;
}

export { 
    PreviewConfiguration,
    DesignTokensPreview,
    DesignSystemTokensConfiguration,
    DesignSystemTokensContainer,
    DesignSystemTokens,
    TextEditorConfiguration,
    DesignSystemTokensTextEditor,
    EditorHandlers
}