import { Component, h, Prop, Method, Element, Listen } from '@stencil/core';
import { getEditorConfiguration } from '../common/configuration';
import { DSOCompletionItemProvider } from '../common/completion/completion-provider';
import { DSOCodeLensesProvider } from '../common/code-lenses/codelens-provider';
import { InitializationHelper } from '../common/initialization-helper';
export class DSOStylesEditor {
    constructor() {
        this.usePlatformConfiguration = false;
        this.grammars = {
            'source.gx.object-design-system-styles': {
                language: 'gx_design_system_styles',
                path: 'object-design-system-styles.tmLanguage.json',
                injections: ['source.gx.common']
            }
        };
    }
    async dispose() {
        this.element.querySelector('gx-text-editor').dispose();
    }
    async getTextEditor() {
        return this.element.querySelector('gx-text-editor');
    }
    editorValueChangedListener() {
        this.configuration.handlers.onChange();
    }
    componentWillRender() {
        this._setConfiguration();
    }
    render() {
        if (this.monaco)
            return h("gx-text-editor", { monaco: this.monaco, creationOptions: {
                    value: this._getValue(),
                    minimap: {
                        enabled: true
                    },
                    language: DSOStylesEditor.language,
                    readOnly: this.readonly,
                    scrollBeyondLastLine: this.usePlatformConfiguration,
                }, grammars: this.grammars, completionItemProvider: this._getCompletionItemProvider.bind(this), codeLensProvider: this._getCodeLensProvider.bind(this), usePlatformConfiguration: this.usePlatformConfiguration, actions: this._getActions(), setExternalListeners: this.configuration.setExternalListeners, afterInitialize: InitializationHelper.afterInitialize, readonlymessage: this.readonlymessage, class: 'text-editor' });
    }
    _getValue() {
        if (this.value)
            return this.value;
        return '';
    }
    _setConfiguration() {
        if (this.configuration)
            return this.configuration;
        this.configuration = getEditorConfiguration();
    }
    _getCompletionItemProvider() {
        if (!this.completionItemProvider)
            this.completionItemProvider = new DSOCompletionItemProvider(this.monaco, this._getEditor.bind(this), this.configuration);
        return this.completionItemProvider.getCompletionItemProvider();
    }
    async _getEditor() {
        var _a;
        return await ((_a = this.element.querySelector('gx-text-editor')) === null || _a === void 0 ? void 0 : _a.getEditor());
    }
    _getCodeLensProvider(editor) {
        if (!this.codeLensesProvider) {
            this.codeLensesProvider = new DSOCodeLensesProvider(this.monaco, editor, this.configuration, DSOStylesEditor.language);
            this.codeLensCommandId = editor.addCommand(0, this.codeLensesProvider.getCodeLensCommand(), '');
        }
        return this.codeLensesProvider.getCodeLensProvider(this.codeLensCommandId);
    }
    _getActions() {
        return [];
    }
    static get is() { return "gx-dso-styles-editor"; }
    static get originalStyleUrls() { return {
        "$": ["dso-styles-editor.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["dso-styles-editor.css"]
    }; }
    static get properties() { return {
        "monaco": {
            "type": "any",
            "mutable": true,
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": ""
            },
            "attribute": "monaco",
            "reflect": false
        },
        "usePlatformConfiguration": {
            "type": "boolean",
            "mutable": false,
            "complexType": {
                "original": "boolean",
                "resolved": "boolean",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": ""
            },
            "attribute": "use-platform-configuration",
            "reflect": false,
            "defaultValue": "false"
        },
        "value": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "string",
                "resolved": "string",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": ""
            },
            "attribute": "value",
            "reflect": false
        },
        "configuration": {
            "type": "unknown",
            "mutable": false,
            "complexType": {
                "original": "EditorConfiguration",
                "resolved": "EditorConfiguration",
                "references": {
                    "EditorConfiguration": {
                        "location": "import",
                        "path": "../common/configuration"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": ""
            }
        },
        "readonlymessage": {
            "type": "string",
            "mutable": true,
            "complexType": {
                "original": "string",
                "resolved": "string",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": ""
            },
            "attribute": "readonlymessage",
            "reflect": false
        },
        "readonly": {
            "type": "boolean",
            "mutable": true,
            "complexType": {
                "original": "boolean",
                "resolved": "boolean",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": ""
            },
            "attribute": "readonly",
            "reflect": false
        }
    }; }
    static get methods() { return {
        "dispose": {
            "complexType": {
                "signature": "() => Promise<void>",
                "parameters": [],
                "references": {
                    "Promise": {
                        "location": "global"
                    }
                },
                "return": "Promise<void>"
            },
            "docs": {
                "text": "",
                "tags": []
            }
        },
        "getTextEditor": {
            "complexType": {
                "signature": "() => Promise<any>",
                "parameters": [],
                "references": {
                    "Promise": {
                        "location": "global"
                    }
                },
                "return": "Promise<any>"
            },
            "docs": {
                "text": "",
                "tags": []
            }
        }
    }; }
    static get elementRef() { return "element"; }
    static get listeners() { return [{
            "name": "editorValueChanged",
            "method": "editorValueChangedListener",
            "target": undefined,
            "capture": false,
            "passive": false
        }]; }
}
DSOStylesEditor.language = 'gx_design_system_styles';
