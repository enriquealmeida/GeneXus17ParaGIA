export declare class EditorConfiguration {
    handlers: EditorHandlers;
    setExternalListeners: boolean;
}
export declare class EditorHandlers {
    getClasses: Function;
    getReferences: Function;
    getImages: Function;
    getTokens: Function;
    goToReference: Function;
    getGxProperties: Function;
    onChange: Function;
    getFiles: Function;
    getAllDSOs: Function;
}
export declare function getEditorConfiguration(): EditorConfiguration;
