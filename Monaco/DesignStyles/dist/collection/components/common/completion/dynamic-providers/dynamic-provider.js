export class DynamicProvider {
    constructor(monaco, configuration) {
        this.monaco = monaco;
        this.configuration = configuration;
    }
    async getAllClasses() {
        var classes = await this.configuration.handlers.getClasses();
        var suggestions = [];
        classes.forEach(cls => {
            var insertText = cls + ";";
            var item = {
                label: cls,
                kind: this.monaco.languages.CompletionItemKind.Class,
                insertText: insertText
            };
            suggestions.push(item);
        });
        return suggestions;
    }
    async getAllImages() {
        if (this.configuration.handlers.getImages) {
            var images = await this.configuration.handlers.getImages();
            var suggestions = [];
            images.forEach(image => {
                var insertText = image + ");";
                var item = {
                    label: image,
                    kind: this.monaco.languages.CompletionItemKind.Class,
                    insertText: insertText
                };
                suggestions.push(item);
            });
            return suggestions;
        }
        else
            return null;
    }
    async getAllFiles() {
        if (this.configuration.handlers.getFiles) {
            var files = await this.configuration.handlers.getFiles();
            var suggestions = [];
            files.forEach(files => {
                var insertText = files + ");";
                var item = {
                    label: files,
                    kind: this.monaco.languages.CompletionItemKind.Class,
                    insertText: insertText
                };
                suggestions.push(item);
            });
            return suggestions;
        }
        else
            return null;
    }
    async getAllDSOs() {
        if (this.configuration.handlers.getAllDSOs) {
            var files = await this.configuration.handlers.getAllDSOs();
            var suggestions = [];
            files.forEach(dsoName => {
                var item = {
                    label: dsoName,
                    kind: this.monaco.languages.CompletionItemKind.Class,
                    insertText: dsoName
                };
                suggestions.push(item);
            });
            return suggestions;
        }
        else
            return null;
    }
    async getAllTokensFromGroup(group) {
        if (this.configuration.handlers.getTokens != null) {
            var tokens = await this.configuration.handlers.getTokens(group);
            var suggestions = [];
            if (tokens !== null) {
                tokens.forEach(token => {
                    var insertText = token;
                    var item = {
                        label: token,
                        kind: this.monaco.languages.CompletionItemKind.Value,
                        insertText: insertText
                    };
                    suggestions.push(item);
                });
            }
            return suggestions;
        }
        else
            return null;
    }
}
