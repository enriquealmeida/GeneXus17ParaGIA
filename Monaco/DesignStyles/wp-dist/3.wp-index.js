(window["webpackJsonp"] = window["webpackJsonp"] || []).push([[3],{

/***/ "./node_modules/monaco-editor/esm/vs/basic-languages/typescript/typescript.js":
/*!************************************************************************************!*\
  !*** ./node_modules/monaco-editor/esm/vs/basic-languages/typescript/typescript.js ***!
  \************************************************************************************/
/*! exports provided: conf, language */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"conf\", function() { return conf; });\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"language\", function() { return language; });\n/* harmony import */ var _fillers_monaco_editor_core_js__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../fillers/monaco-editor-core.js */ \"./node_modules/monaco-editor/esm/vs/basic-languages/fillers/monaco-editor-core.js\");\n/*---------------------------------------------------------------------------------------------\r\n *  Copyright (c) Microsoft Corporation. All rights reserved.\r\n *  Licensed under the MIT License. See License.txt in the project root for license information.\r\n *--------------------------------------------------------------------------------------------*/\r\n\r\nvar conf = {\r\n    wordPattern: /(-?\\d*\\.\\d\\w*)|([^\\`\\~\\!\\@\\#\\%\\^\\&\\*\\(\\)\\-\\=\\+\\[\\{\\]\\}\\\\\\|\\;\\:\\'\\\"\\,\\.\\<\\>\\/\\?\\s]+)/g,\r\n    comments: {\r\n        lineComment: '//',\r\n        blockComment: ['/*', '*/']\r\n    },\r\n    brackets: [\r\n        ['{', '}'],\r\n        ['[', ']'],\r\n        ['(', ')']\r\n    ],\r\n    onEnterRules: [\r\n        {\r\n            // e.g. /** | */\r\n            beforeText: /^\\s*\\/\\*\\*(?!\\/)([^\\*]|\\*(?!\\/))*$/,\r\n            afterText: /^\\s*\\*\\/$/,\r\n            action: {\r\n                indentAction: _fillers_monaco_editor_core_js__WEBPACK_IMPORTED_MODULE_0__[\"languages\"].IndentAction.IndentOutdent,\r\n                appendText: ' * '\r\n            }\r\n        },\r\n        {\r\n            // e.g. /** ...|\r\n            beforeText: /^\\s*\\/\\*\\*(?!\\/)([^\\*]|\\*(?!\\/))*$/,\r\n            action: {\r\n                indentAction: _fillers_monaco_editor_core_js__WEBPACK_IMPORTED_MODULE_0__[\"languages\"].IndentAction.None,\r\n                appendText: ' * '\r\n            }\r\n        },\r\n        {\r\n            // e.g.  * ...|\r\n            beforeText: /^(\\t|(\\ \\ ))*\\ \\*(\\ ([^\\*]|\\*(?!\\/))*)?$/,\r\n            action: {\r\n                indentAction: _fillers_monaco_editor_core_js__WEBPACK_IMPORTED_MODULE_0__[\"languages\"].IndentAction.None,\r\n                appendText: '* '\r\n            }\r\n        },\r\n        {\r\n            // e.g.  */|\r\n            beforeText: /^(\\t|(\\ \\ ))*\\ \\*\\/\\s*$/,\r\n            action: {\r\n                indentAction: _fillers_monaco_editor_core_js__WEBPACK_IMPORTED_MODULE_0__[\"languages\"].IndentAction.None,\r\n                removeText: 1\r\n            }\r\n        }\r\n    ],\r\n    autoClosingPairs: [\r\n        { open: '{', close: '}' },\r\n        { open: '[', close: ']' },\r\n        { open: '(', close: ')' },\r\n        { open: '\"', close: '\"', notIn: ['string'] },\r\n        { open: \"'\", close: \"'\", notIn: ['string', 'comment'] },\r\n        { open: '`', close: '`', notIn: ['string', 'comment'] },\r\n        { open: '/**', close: ' */', notIn: ['string'] }\r\n    ],\r\n    folding: {\r\n        markers: {\r\n            start: new RegExp('^\\\\s*//\\\\s*#?region\\\\b'),\r\n            end: new RegExp('^\\\\s*//\\\\s*#?endregion\\\\b')\r\n        }\r\n    }\r\n};\r\nvar language = {\r\n    // Set defaultToken to invalid to see what you do not tokenize yet\r\n    defaultToken: 'invalid',\r\n    tokenPostfix: '.ts',\r\n    keywords: [\r\n        // Should match the keys of textToKeywordObj in\r\n        // https://github.com/microsoft/TypeScript/blob/master/src/compiler/scanner.ts\r\n        'abstract',\r\n        'any',\r\n        'as',\r\n        'asserts',\r\n        'bigint',\r\n        'boolean',\r\n        'break',\r\n        'case',\r\n        'catch',\r\n        'class',\r\n        'continue',\r\n        'const',\r\n        'constructor',\r\n        'debugger',\r\n        'declare',\r\n        'default',\r\n        'delete',\r\n        'do',\r\n        'else',\r\n        'enum',\r\n        'export',\r\n        'extends',\r\n        'false',\r\n        'finally',\r\n        'for',\r\n        'from',\r\n        'function',\r\n        'get',\r\n        'if',\r\n        'implements',\r\n        'import',\r\n        'in',\r\n        'infer',\r\n        'instanceof',\r\n        'interface',\r\n        'is',\r\n        'keyof',\r\n        'let',\r\n        'module',\r\n        'namespace',\r\n        'never',\r\n        'new',\r\n        'null',\r\n        'number',\r\n        'object',\r\n        'package',\r\n        'private',\r\n        'protected',\r\n        'public',\r\n        'override',\r\n        'readonly',\r\n        'require',\r\n        'global',\r\n        'return',\r\n        'set',\r\n        'static',\r\n        'string',\r\n        'super',\r\n        'switch',\r\n        'symbol',\r\n        'this',\r\n        'throw',\r\n        'true',\r\n        'try',\r\n        'type',\r\n        'typeof',\r\n        'undefined',\r\n        'unique',\r\n        'unknown',\r\n        'var',\r\n        'void',\r\n        'while',\r\n        'with',\r\n        'yield',\r\n        'async',\r\n        'await',\r\n        'of'\r\n    ],\r\n    operators: [\r\n        '<=',\r\n        '>=',\r\n        '==',\r\n        '!=',\r\n        '===',\r\n        '!==',\r\n        '=>',\r\n        '+',\r\n        '-',\r\n        '**',\r\n        '*',\r\n        '/',\r\n        '%',\r\n        '++',\r\n        '--',\r\n        '<<',\r\n        '</',\r\n        '>>',\r\n        '>>>',\r\n        '&',\r\n        '|',\r\n        '^',\r\n        '!',\r\n        '~',\r\n        '&&',\r\n        '||',\r\n        '??',\r\n        '?',\r\n        ':',\r\n        '=',\r\n        '+=',\r\n        '-=',\r\n        '*=',\r\n        '**=',\r\n        '/=',\r\n        '%=',\r\n        '<<=',\r\n        '>>=',\r\n        '>>>=',\r\n        '&=',\r\n        '|=',\r\n        '^=',\r\n        '@'\r\n    ],\r\n    // we include these common regular expressions\r\n    symbols: /[=><!~?:&|+\\-*\\/\\^%]+/,\r\n    escapes: /\\\\(?:[abfnrtv\\\\\"']|x[0-9A-Fa-f]{1,4}|u[0-9A-Fa-f]{4}|U[0-9A-Fa-f]{8})/,\r\n    digits: /\\d+(_+\\d+)*/,\r\n    octaldigits: /[0-7]+(_+[0-7]+)*/,\r\n    binarydigits: /[0-1]+(_+[0-1]+)*/,\r\n    hexdigits: /[[0-9a-fA-F]+(_+[0-9a-fA-F]+)*/,\r\n    regexpctl: /[(){}\\[\\]\\$\\^|\\-*+?\\.]/,\r\n    regexpesc: /\\\\(?:[bBdDfnrstvwWn0\\\\\\/]|@regexpctl|c[A-Z]|x[0-9a-fA-F]{2}|u[0-9a-fA-F]{4})/,\r\n    // The main tokenizer for our languages\r\n    tokenizer: {\r\n        root: [[/[{}]/, 'delimiter.bracket'], { include: 'common' }],\r\n        common: [\r\n            // identifiers and keywords\r\n            [\r\n                /[a-z_$][\\w$]*/,\r\n                {\r\n                    cases: {\r\n                        '@keywords': 'keyword',\r\n                        '@default': 'identifier'\r\n                    }\r\n                }\r\n            ],\r\n            [/[A-Z][\\w\\$]*/, 'type.identifier'],\r\n            // [/[A-Z][\\w\\$]*/, 'identifier'],\r\n            // whitespace\r\n            { include: '@whitespace' },\r\n            // regular expression: ensure it is terminated before beginning (otherwise it is an opeator)\r\n            [\r\n                /\\/(?=([^\\\\\\/]|\\\\.)+\\/([dgimsuy]*)(\\s*)(\\.|;|,|\\)|\\]|\\}|$))/,\r\n                { token: 'regexp', bracket: '@open', next: '@regexp' }\r\n            ],\r\n            // delimiters and operators\r\n            [/[()\\[\\]]/, '@brackets'],\r\n            [/[<>](?!@symbols)/, '@brackets'],\r\n            [/!(?=([^=]|$))/, 'delimiter'],\r\n            [\r\n                /@symbols/,\r\n                {\r\n                    cases: {\r\n                        '@operators': 'delimiter',\r\n                        '@default': ''\r\n                    }\r\n                }\r\n            ],\r\n            // numbers\r\n            [/(@digits)[eE]([\\-+]?(@digits))?/, 'number.float'],\r\n            [/(@digits)\\.(@digits)([eE][\\-+]?(@digits))?/, 'number.float'],\r\n            [/0[xX](@hexdigits)n?/, 'number.hex'],\r\n            [/0[oO]?(@octaldigits)n?/, 'number.octal'],\r\n            [/0[bB](@binarydigits)n?/, 'number.binary'],\r\n            [/(@digits)n?/, 'number'],\r\n            // delimiter: after number because of .\\d floats\r\n            [/[;,.]/, 'delimiter'],\r\n            // strings\r\n            [/\"([^\"\\\\]|\\\\.)*$/, 'string.invalid'],\r\n            [/'([^'\\\\]|\\\\.)*$/, 'string.invalid'],\r\n            [/\"/, 'string', '@string_double'],\r\n            [/'/, 'string', '@string_single'],\r\n            [/`/, 'string', '@string_backtick']\r\n        ],\r\n        whitespace: [\r\n            [/[ \\t\\r\\n]+/, ''],\r\n            [/\\/\\*\\*(?!\\/)/, 'comment.doc', '@jsdoc'],\r\n            [/\\/\\*/, 'comment', '@comment'],\r\n            [/\\/\\/.*$/, 'comment']\r\n        ],\r\n        comment: [\r\n            [/[^\\/*]+/, 'comment'],\r\n            [/\\*\\//, 'comment', '@pop'],\r\n            [/[\\/*]/, 'comment']\r\n        ],\r\n        jsdoc: [\r\n            [/[^\\/*]+/, 'comment.doc'],\r\n            [/\\*\\//, 'comment.doc', '@pop'],\r\n            [/[\\/*]/, 'comment.doc']\r\n        ],\r\n        // We match regular expression quite precisely\r\n        regexp: [\r\n            [\r\n                /(\\{)(\\d+(?:,\\d*)?)(\\})/,\r\n                ['regexp.escape.control', 'regexp.escape.control', 'regexp.escape.control']\r\n            ],\r\n            [\r\n                /(\\[)(\\^?)(?=(?:[^\\]\\\\\\/]|\\\\.)+)/,\r\n                ['regexp.escape.control', { token: 'regexp.escape.control', next: '@regexrange' }]\r\n            ],\r\n            [/(\\()(\\?:|\\?=|\\?!)/, ['regexp.escape.control', 'regexp.escape.control']],\r\n            [/[()]/, 'regexp.escape.control'],\r\n            [/@regexpctl/, 'regexp.escape.control'],\r\n            [/[^\\\\\\/]/, 'regexp'],\r\n            [/@regexpesc/, 'regexp.escape'],\r\n            [/\\\\\\./, 'regexp.invalid'],\r\n            [\r\n                /(\\/)([dgimsuy]*)/,\r\n                [{ token: 'regexp', bracket: '@close', next: '@pop' }, 'keyword.other']\r\n            ]\r\n        ],\r\n        regexrange: [\r\n            [/-/, 'regexp.escape.control'],\r\n            [/\\^/, 'regexp.invalid'],\r\n            [/@regexpesc/, 'regexp.escape'],\r\n            [/[^\\]]/, 'regexp'],\r\n            [\r\n                /\\]/,\r\n                {\r\n                    token: 'regexp.escape.control',\r\n                    next: '@pop',\r\n                    bracket: '@close'\r\n                }\r\n            ]\r\n        ],\r\n        string_double: [\r\n            [/[^\\\\\"]+/, 'string'],\r\n            [/@escapes/, 'string.escape'],\r\n            [/\\\\./, 'string.escape.invalid'],\r\n            [/\"/, 'string', '@pop']\r\n        ],\r\n        string_single: [\r\n            [/[^\\\\']+/, 'string'],\r\n            [/@escapes/, 'string.escape'],\r\n            [/\\\\./, 'string.escape.invalid'],\r\n            [/'/, 'string', '@pop']\r\n        ],\r\n        string_backtick: [\r\n            [/\\$\\{/, { token: 'delimiter.bracket', next: '@bracketCounting' }],\r\n            [/[^\\\\`$]+/, 'string'],\r\n            [/@escapes/, 'string.escape'],\r\n            [/\\\\./, 'string.escape.invalid'],\r\n            [/`/, 'string', '@pop']\r\n        ],\r\n        bracketCounting: [\r\n            [/\\{/, 'delimiter.bracket', '@bracketCounting'],\r\n            [/\\}/, 'delimiter.bracket', '@pop'],\r\n            { include: 'common' }\r\n        ]\r\n    }\r\n};\r\n\n\n//# sourceURL=webpack:///./node_modules/monaco-editor/esm/vs/basic-languages/typescript/typescript.js?");

/***/ })

}]);