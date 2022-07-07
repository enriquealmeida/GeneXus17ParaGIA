import { IconHelper } from '../utils/icon-helper';
import { WidgetHelper } from '../utils/widget-helper';
export class DSOCodeLensesProvider {
    constructor(monaco, editor, configuration, language) {
        this.monaco = monaco;
        this.editor = editor;
        this.configuration = configuration;
        this.language = language;
    }
    getCodeLensProvider(commandId) {
        let configuration = this.configuration;
        return {
            provideCodeLenses: async function (model, token) {
                console.log(token);
                let lenses = [];
                let position = 0;
                let classReferences = await configuration.handlers.getReferences();
                let lines = model.getLinesContent();
                lines.forEach((lineValue, index) => {
                    let line = lineValue.trim();
                    if (DSOCodeLensesConstants.CLASS_REGEX.test(line)) {
                        let res = line.match(DSOCodeLensesConstants.CLASS_REGEX);
                        let references = null;
                        if (res.groups[DSOCodeLensesConstants.CLASS_NAME] != null)
                            references = classReferences[res.groups[DSOCodeLensesConstants.CLASS_NAME]];
                        if (references != null) {
                            let id;
                            let command = commandId;
                            if (references.length === 0)
                                command = "";
                            id = DSOCodeLensesProvider._getRerencesLabel(references);
                            position = index + 1;
                            let lense = {
                                range: {
                                    startLineNumber: position,
                                    startColumn: 1,
                                    endLineNumber: position,
                                    endColumn: 1
                                },
                                id: id,
                                command: {
                                    id: command,
                                    title: id,
                                    arguments: [{ refs: references, pos: position }],
                                }
                            };
                            lenses.push(lense);
                        }
                    }
                });
                return {
                    lenses: lenses,
                    dispose: () => { }
                };
            },
            resolveCodeLens: function (model, codeLens, token) {
                console.log(model + token);
                return codeLens;
            }
        };
    }
    static _getRerencesLabel(references) {
        if (references.length === 1)
            return references.length + DSOCodeLensesConstants.REFERENCE;
        return references.length + DSOCodeLensesConstants.REFERENCES;
    }
    getCodeLensCommand() {
        let monaco = this.monaco;
        let editor = this.editor;
        let configuration = this.configuration;
        return (ctx, parms) => {
            console.log(ctx);
            const contentWidget = {
                getId: function () {
                    return editor.getId() + DSOCodeLensesIDs.MY_CONTENT_WIDGET_ID;
                },
                getDomNode: function () {
                    let id = editor.getId() + DSOCodeLensesIDs.REFERENCES_POPUP_ID;
                    WidgetHelper.closeReferencesWidget(editor);
                    const filter = document.createElement(DSOCodeLensesGemini.GXG_FILTER);
                    filter.id = id;
                    parms[DSOCodeLensesConstants.REFS].forEach(ref => {
                        let filterItem1 = document.createElement(DSOCodeLensesGemini.GXG_FILTER_ITEM);
                        filterItem1.setAttribute(DSOCodeLensesGemini.TYPE, ref.Type);
                        filterItem1.setAttribute(DSOCodeLensesGemini.ICON, IconHelper.getIconFromGxName(ref.TypeName));
                        filterItem1.innerHTML = ref.Name;
                        filter.appendChild(filterItem1);
                    });
                    filter.addEventListener(DSOCodeLensesGemini.ITEM_CLICKED_EVENT, (info) => {
                        configuration.handlers.goToReference(info.detail[DSOCodeLensesGemini.ITEM_TYPE], info.detail[DSOCodeLensesGemini.ITEM_TEXT]);
                    });
                    document.activeElement.blur();
                    filter.focus();
                    return filter;
                },
                getPosition: function () {
                    return {
                        position: {
                            lineNumber: parms[DSOCodeLensesConstants.POS],
                            column: 4,
                        },
                        preference: [
                            monaco.editor.ContentWidgetPositionPreference.ABOVE,
                        ],
                    };
                },
            };
            editor.addContentWidget(contentWidget);
        };
    }
}
class DSOCodeLensesConstants {
}
DSOCodeLensesConstants.CLASS_REGEX = RegExp("^\\.(?<className>[A-Za-z0-9_-]*)");
DSOCodeLensesConstants.CLASS_NAME = "className";
DSOCodeLensesConstants.REFERENCE = " reference";
DSOCodeLensesConstants.REFERENCES = " references";
DSOCodeLensesConstants.REFS = "refs";
DSOCodeLensesConstants.POS = "pos";
export class DSOCodeLensesIDs {
}
DSOCodeLensesIDs.MY_CONTENT_WIDGET_ID = "my.content.widget";
DSOCodeLensesIDs.REFERENCES_POPUP_ID = "ReferencesPopup";
export class DSOCodeLensesGemini {
}
DSOCodeLensesGemini.GXG_FILTER = "gxg-filter";
DSOCodeLensesGemini.GXG_FILTER_ITEM = "gxg-filter-item";
DSOCodeLensesGemini.TYPE = "type";
DSOCodeLensesGemini.ICON = "icon";
DSOCodeLensesGemini.ITEM_TYPE = "item-type";
DSOCodeLensesGemini.ITEM_TEXT = "item-text";
DSOCodeLensesGemini.ITEM_CLICKED_EVENT = "itemClickedEvent";
