import { DSOCodeLensesIDs, DSOCodeLensesGemini } from "../code-lenses/codelens-provider";
import { ElementFinder } from "./element-finder";
export class WidgetHelper {
    static closeReferencesWidget(editor) {
        ElementFinder.closeElement(editor.getDomNode(), DSOCodeLensesGemini.GXG_FILTER, editor.getId() + DSOCodeLensesIDs.REFERENCES_POPUP_ID);
    }
}
