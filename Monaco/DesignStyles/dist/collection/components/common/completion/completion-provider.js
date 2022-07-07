import { StaticProvider } from "./static-providers/static-provider";
import { DynamicProvider } from "./dynamic-providers/dynamic-provider";
export class DSOCompletionItemProvider {
    constructor(monaco, getEditor, configuration) {
        this.getCompletionItemProvider = () => {
            let dynamicProvider = new DynamicProvider(this.monaco, this.configuration);
            let monaco = this.monaco;
            let configuration = this.configuration;
            let getEditor = this.getEditor;
            let calculateSuggestions = this._calculateSuggestions;
            return {
                async provideCompletionItems(model, position) {
                    let editor = await getEditor();
                    let staticProvider = new StaticProvider(monaco, configuration, editor, dynamicProvider);
                    let storageService = editor.getContribution("editor.contrib.suggestController").widget._value._storageService;
                    if (storageService)
                        storageService.store('expandSuggestionDocs', true, 0);
                    var textUntilPosition = model.getValueInRange({ startLineNumber: 1, startColumn: 1, endLineNumber: position.lineNumber, endColumn: position.column });
                    return { suggestions: await calculateSuggestions(false, textUntilPosition, dynamicProvider, staticProvider) };
                },
                triggerCharacters: ["$", ":", "(", "@"]
            };
        };
        this.monaco = monaco;
        this.getEditor = getEditor;
        this.configuration = configuration;
    }
    async caluclateSuggestionsTest(completeText) {
        return await this._calculateSuggestions(true, completeText, null, null);
    }
    async checkIfTest(isTest, location) {
        if (isTest)
            return location;
    }
    async _calculateSuggestions(isTest, completeText, dynamicProvider, staticProvider) {
        var text = CompletionUtils.removeIrrelevantText(completeText);
        var regexInGxImage = RegExp("gx-image\\(\\s*$");
        var regexInGxFile = RegExp("gx-file\\(\\s*$");
        var regexInInclude = RegExp("@include[^;]*$");
        var regexInMedia = RegExp("@media\\s*(?<dollar>\\$?)*$");
        var regexInMediaQueryValue = RegExp("@media\\s*\\$mediaQueries\\.$");
        var regexInImport = RegExp("@import[^;]*$");
        var regexInPropValueFunction = RegExp("\\s*.*:\\s*.*\\(.*?(?<dollar>\\$?)*$");
        var regexInPropValueTokenGroup = RegExp("\\s*.*:\\s*.*?(\\$(?<group>[^\n\\s]*)\\..*)$");
        var regexInPropValue = RegExp("\\s*(?<propName>[^\n\\s]*):\\s*.*?(?<dollar>\\$)?[^\\.\n\\s]*$");
        var regexInClass = RegExp(".+\\s*{[^}]*$");
        var regexInStart = RegExp("^\\s*$");
        var regexInEmpty = RegExp("^\\s*{");
        if (regexInGxFile.test(text)) {
            if (isTest)
                return "regexInGxFile";
            return dynamicProvider.getAllFiles();
        }
        else if (regexInImport.test(text)) {
            if (isTest)
                return "regexInImport";
            var suggestions = [];
            suggestions = suggestions.concat(staticProvider.getImportSuggestions());
            suggestions = suggestions.concat(await dynamicProvider.getAllDSOs());
            return suggestions;
        }
        else if (regexInGxImage.test(text)) {
            if (isTest)
                return "regexInGxImage";
            return dynamicProvider.getAllImages();
        }
        else if (regexInInclude.test(text)) {
            if (isTest)
                return "regexInInclude";
            return dynamicProvider.getAllClasses();
        }
        else if (regexInPropValueFunction.test(text)) {
            if (isTest)
                return "regexInPropValueFunction";
            if (regexInPropValueTokenGroup.test(text)) {
                var res = text.match(regexInPropValueTokenGroup);
                return dynamicProvider.getAllTokensFromGroup(res.groups["group"]);
            }
            else {
                var res = text.match(regexInPropValueFunction);
                var hasDollar = false;
                if (res.groups["dollar"] != null)
                    hasDollar = true;
                return staticProvider.getAllTokenGroupsSugestions(hasDollar, false);
            }
        }
        else if (regexInPropValueTokenGroup.test(text)) {
            if (isTest)
                return "regexInPropValueTokenGroup";
            var res = text.match(regexInPropValueTokenGroup);
            return dynamicProvider.getAllTokensFromGroup(res.groups["group"]);
        }
        else if (regexInPropValue.test(text)) {
            if (isTest)
                return "regexInPropValue";
            var res = text.match(regexInPropValue);
            var suggestions = [];
            if (res.groups["dollar"] != null)
                suggestions = suggestions.concat(staticProvider.getAllTokenGroupsSugestions(true, true));
            else if (res.groups["propName"].startsWith("gx-"))
                suggestions = suggestions.concat(await staticProvider.getGxPropertyValues(res.groups["propName"]));
            else
                suggestions = suggestions.concat(await staticProvider.getCssPropertyValues(res.groups["propName"]));
            return suggestions;
        }
        else if (regexInClass.test(text)) {
            if (isTest)
                return "regexInClass";
            return staticProvider.getAllCSSProperties();
        }
        else if (regexInMedia.test(text)) {
            if (isTest)
                return "regexInMedia";
            var res = text.match(regexInMedia);
            return staticProvider.getMediaSuggestion(res.groups["dollar"] != null);
        }
        else if (regexInMediaQueryValue.test(text)) {
            if (isTest)
                return "regexInMediaQueryValue";
            return dynamicProvider.getAllTokensFromGroup("mediaQueries");
        }
        else if (regexInEmpty.test(text)) {
            if (isTest)
                return "regexInEmpty";
            return staticProvider.getBaseSuggestions();
        }
        else if (regexInStart.test(text)) {
            if (isTest)
                return "regexInStart";
            return staticProvider.getStarterSuggestion();
        }
        else {
            if (isTest)
                return "noMatch";
            return staticProvider.getBaseSuggestions();
        }
    }
}
class CompletionUtils {
    static removeIrrelevantText(text) {
        var cleanText = text;
        var regExp_InitialStylesWord = RegExp("^styles [^{]*(\\s|\n)*", 'g');
        var regExp_Comments = RegExp("(\/\\*(.|\n)*?\\*\/)|(\/\/.*)", 'g');
        var regExp_Regions = RegExp("\#region[^{\n]*|\#endregion", 'g');
        var regExp_MultiLineFeed = RegExp("^\\s*$", 'm');
        cleanText = cleanText.replace(regExp_InitialStylesWord, "");
        cleanText = cleanText.replace(regExp_Comments, "");
        cleanText = cleanText.replace(regExp_Regions, "");
        cleanText = cleanText.replace(regExp_MultiLineFeed, "");
        return cleanText;
    }
}
