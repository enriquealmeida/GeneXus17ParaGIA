;
export function getEditorConfiguration() {
    return {
        handlers: {
            getClasses: window.external.GetClasses,
            getReferences: window.external.GetClassesReferences,
            onChange: window.external.OnDocumentChanged,
            goToReference: window.external.GoToReference,
            getImages: window.external.GetImages,
            getFiles: window.external.GetFiles,
            getTokens: window.external.GetTokens,
            getAllDSOs: window.external.GetAllDSOs,
            getGxProperties: window.external.GetGxProperties
        },
        setExternalListeners: true
    };
}
