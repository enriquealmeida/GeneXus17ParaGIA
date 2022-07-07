import { WidgetHelper } from "./utils/widget-helper";
export class InitializationHelper {
    static afterInitialize(editor) {
        if (editor) {
            editor.onDidFocusEditorText(() => {
                WidgetHelper.closeReferencesWidget(editor);
            });
        }
    }
}
