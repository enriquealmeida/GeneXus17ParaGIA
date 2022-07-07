import { CSSData } from "./css-data-provider";
export class StaticProvider {
    constructor(monaco, configuration, editor, dynamicProvider) {
        this.baseSuggestions = null;
        this.importSuggestions = null;
        this.allTokenGroupsSugestions = null;
        this.starterSuggestion = null;
        this.mediaSuggestion = null;
        this.allCSSProperties = null;
        this.allGXProperties = null;
        this.allGXPropertiesSuggestions = null;
        this.cssPropertyValues = [];
        this.monaco = monaco;
        this.configuration = configuration;
        this.editor = editor;
        this.dynamicProvider = dynamicProvider;
    }
    getBaseSuggestions() {
        if (this.baseSuggestions === null) {
            var suggestions = [];
            var classElement = {
                label: "Class",
                kind: this.monaco.languages.CompletionItemKind.Constructor,
                insertText: '.${1:className}\r\n{\r\n\t${2:poperty}:${3:value}; \r\n}\r\n',
                insertTextRules: this.monaco.languages.CompletionItemInsertTextRule.InsertAsSnippet
            };
            var fontFaceElement = {
                label: "Font Face",
                kind: this.monaco.languages.CompletionItemKind.Constructor,
                insertText: '@font-face\r\n{\r\n\tfont-family: ${1:Family}; \r\n\tsrc: ${2:Src};\r\n}\r\n',
                insertTextRules: this.monaco.languages.CompletionItemInsertTextRule.InsertAsSnippet
            };
            var regionSuggestion = {
                label: "Region",
                kind: this.monaco.languages.CompletionItemKind.Constructor,
                insertText: '#region ${1:Name}\n#endregion',
                insertTextRules: this.monaco.languages.CompletionItemInsertTextRule.InsertAsSnippet
            };
            var importSuggestion = {
                label: "Import",
                kind: this.monaco.languages.CompletionItemKind.Constructor,
                insertText: '@import ',
                insertTextRules: this.monaco.languages.CompletionItemInsertTextRule.InsertAsSnippet,
                command: this.editor._actions["editor.action.triggerSuggest"]
            };
            suggestions.push(importSuggestion);
            suggestions.push(classElement);
            suggestions.push(fontFaceElement);
            suggestions.push(regionSuggestion);
            this.baseSuggestions = suggestions;
        }
        return this.baseSuggestions;
    }
    getImportSuggestions() {
        if (this.importSuggestions === null) {
            var suggestions = [];
            var gxFileSuggestion = {
                label: "GxFile",
                kind: this.monaco.languages.CompletionItemKind.Constructor,
                insertText: 'gx-file(',
                command: this.editor._actions["editor.action.triggerSuggest"]
            };
            suggestions.push(gxFileSuggestion);
            this.importSuggestions = suggestions;
        }
        return this.importSuggestions;
    }
    getAllTokenGroupsSugestions(hasDollar, removeMedia) {
        console.log("getAllTokenGroupsSugestions");
        if (this.allTokenGroupsSugestions === null) {
            var tokenGroups = ["colors", "spacing", "fonts", "fontSizes", "zIndex", "borders", "shadows", "radius", "opacity", "times", "timingFunction", "mediaQueries", "custom"];
            var suggestions = [];
            tokenGroups.forEach(group => {
                if (!(removeMedia && group === "mediaQueries")) {
                    var insertText = group + ".";
                    if (!hasDollar) {
                        insertText = '$' + insertText;
                    }
                    var item = {
                        label: '$' + group,
                        kind: this.monaco.languages.CompletionItemKind.Class,
                        insertText: insertText,
                        command: this.editor._actions["editor.action.triggerSuggest"]
                    };
                    suggestions.push(item);
                }
            });
            this.allTokenGroupsSugestions = suggestions;
        }
        return this.allTokenGroupsSugestions;
    }
    getStarterSuggestion() {
        if (this.starterSuggestion === null) {
            var suggestions = [];
            var styleElement = {
                label: "Styles",
                kind: this.monaco.languages.CompletionItemKind.Constructor,
                insertText: 'styles ${1:Name} {\r\n\t \r\n}',
                insertTextRules: this.monaco.languages.CompletionItemInsertTextRule.InsertAsSnippet
            };
            suggestions.push(styleElement);
            this.starterSuggestion = suggestions;
        }
        return this.starterSuggestion;
    }
    getCssPropertyValues(propName) {
        console.log("getCssPropertyValues");
        var property = null;
        CSSData.properties.some(prop => {
            if (prop.name == propName) {
                property = prop;
                return true;
            }
        });
        var suggestions = [];
        if (property != null) {
            if (!this.cssPropertyValues[property.name]) {
                if (property.values != null) {
                    property.values.forEach(propValue => {
                        var relevance = propValue.name;
                        if (!propValue.name.startsWith('-'))
                            relevance = "$" + relevance;
                        var item = {
                            label: propValue.name,
                            kind: this.monaco.languages.CompletionItemKind.Field,
                            documentation: propValue.description,
                            insertText: propValue.name + ";",
                            sortText: "$$" + relevance
                        };
                        suggestions.push(item);
                    });
                }
                this.cssPropertyValues[property] = suggestions;
            }
            else
                suggestions = this.cssPropertyValues[property];
        }
        suggestions = suggestions.concat(this.getAllTokenGroupsSugestions(false, true));
        return suggestions;
    }
    async getGxPropertyValues(propName) {
        console.log("getGxPropertyValues");
        var suggestions = [];
        var property = null;
        if (this.allGXProperties === null)
            this.allGXProperties = await this.configuration.handlers.getGxProperties();
        this.allGXProperties.some(prop => {
            if (prop.Name == propName) {
                property = prop;
                return true;
            }
        });
        if (property != null) {
            if (property.IsClassRef)
                suggestions = await this.dynamicProvider.getAllClasses();
            else {
                if (property.Values != null) {
                    property.Values.forEach(propValue => {
                        var item = {
                            label: propValue,
                            kind: this.monaco.languages.CompletionItemKind.Field,
                            insertText: propValue + ";",
                            sortText: propValue
                        };
                        suggestions.push(item);
                    });
                }
            }
        }
        else
            suggestions = await this.dynamicProvider.getAllClasses();
        suggestions = suggestions.concat(this.getAllTokenGroupsSugestions(false, true));
        return suggestions;
    }
    async getAllCSSPredefinedProperties() {
        if (this.allGXPropertiesSuggestions === null) {
            if (this.configuration.handlers.getGxProperties) {
                if (this.allGXProperties === null)
                    this.allGXProperties = await this.configuration.handlers.getGxProperties();
                var suggestions = [];
                this.allGXProperties.forEach(property => {
                    var customPropertyId = property.Id;
                    var description = property.Description;
                    var kind = this.monaco.languages.CompletionItemKind.Method;
                    var appliesTo = "Both";
                    var apliesToDescription = "";
                    if (property.TypesSupport.includes("Smartdevice")) {
                        apliesToDescription = "(This property only applies to MOBILE classes)";
                        kind = this.monaco.languages.CompletionItemKind.Function;
                        appliesTo = "Smartdevice";
                    }
                    if (property.TypesSupport.includes("Web")) {
                        if (appliesTo === "Smartdevice") {
                            apliesToDescription = " (This property applies to both WEB and MOBILE classes)";
                            appliesTo = "Both";
                        }
                        else {
                            apliesToDescription = " (This property only applies to WEB classes)";
                            kind = this.monaco.languages.CompletionItemKind.Interface;
                            appliesTo = "Web";
                        }
                    }
                    description = description + " " + apliesToDescription;
                    var sortText = customPropertyId;
                    if (customPropertyId.startsWith("gx-"))
                        sortText = "$$$$";
                    var insertText = customPropertyId + ": ";
                    var label = customPropertyId + " (" + appliesTo + ")";
                    var item = {
                        label: label,
                        kind: kind,
                        documentation: description,
                        insertText: insertText,
                        insertTextRules: this.monaco.languages.CompletionItemInsertTextRule.InsertAsSnippet,
                        sortText: sortText,
                        command: this.editor._actions["editor.action.triggerSuggest"]
                    };
                    suggestions.push(item);
                });
                this.allGXPropertiesSuggestions = suggestions;
            }
        }
        return this.allGXPropertiesSuggestions;
    }
    async getAllCSSProperties() {
        if (this.allCSSProperties === null) {
            var suggestions = [];
            CSSData.properties.forEach(prop => {
                var relevance = 99 - prop.relevance;
                let strRelevance = relevance.toString();
                if (relevance < 10)
                    strRelevance = '0' + strRelevance;
                var item = {
                    label: prop.name,
                    kind: this.monaco.languages.CompletionItemKind.Field,
                    documentation: prop.description,
                    insertText: prop.name + ": ",
                    insertTextRules: this.monaco.languages.CompletionItemInsertTextRule.InsertAsSnippet,
                    sortText: strRelevance,
                    command: this.editor._actions["editor.action.triggerSuggest"]
                };
                suggestions.push(item);
            });
            var predefinedSelectors = await this.getAllCSSPredefinedProperties();
            if (predefinedSelectors != null)
                suggestions = suggestions.concat(predefinedSelectors);
            this.allCSSProperties = suggestions;
        }
        return this.allCSSProperties;
    }
    getMediaSuggestion(hasDollar) {
        if (this.mediaSuggestion === null) {
            var suggestions = [];
            var insertText = "mediaQueries.";
            if (!hasDollar) {
                insertText = '$' + insertText;
            }
            var item = {
                label: '$mediaQueries',
                kind: this.monaco.languages.CompletionItemKind.Class,
                insertText: insertText,
                command: this.editor._actions["editor.action.triggerSuggest"]
            };
            suggestions.push(item);
            this.mediaSuggestion = suggestions;
        }
        return this.mediaSuggestion;
    }
}
