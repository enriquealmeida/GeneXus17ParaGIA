(window["webpackJsonp"] = window["webpackJsonp"] || []).push([[100],{

/***/ "./node_modules/monaco-editor/esm/vs/basic-languages/csharp/csharp.js":
/*!****************************************************************************!*\
  !*** ./node_modules/monaco-editor/esm/vs/basic-languages/csharp/csharp.js ***!
  \****************************************************************************/
/*! exports provided: conf, language */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"conf\", function() { return conf; });\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"language\", function() { return language; });\n/*---------------------------------------------------------------------------------------------\r\n *  Copyright (c) Microsoft Corporation. All rights reserved.\r\n *  Licensed under the MIT License. See License.txt in the project root for license information.\r\n *--------------------------------------------------------------------------------------------*/\r\nvar conf = {\r\n    wordPattern: /(-?\\d*\\.\\d\\w*)|([^\\`\\~\\!\\#\\$\\%\\^\\&\\*\\(\\)\\-\\=\\+\\[\\{\\]\\}\\\\\\|\\;\\:\\'\\\"\\,\\.\\<\\>\\/\\?\\s]+)/g,\r\n    comments: {\r\n        lineComment: '//',\r\n        blockComment: ['/*', '*/']\r\n    },\r\n    brackets: [\r\n        ['{', '}'],\r\n        ['[', ']'],\r\n        ['(', ')']\r\n    ],\r\n    autoClosingPairs: [\r\n        { open: '{', close: '}' },\r\n        { open: '[', close: ']' },\r\n        { open: '(', close: ')' },\r\n        { open: \"'\", close: \"'\", notIn: ['string', 'comment'] },\r\n        { open: '\"', close: '\"', notIn: ['string', 'comment'] }\r\n    ],\r\n    surroundingPairs: [\r\n        { open: '{', close: '}' },\r\n        { open: '[', close: ']' },\r\n        { open: '(', close: ')' },\r\n        { open: '<', close: '>' },\r\n        { open: \"'\", close: \"'\" },\r\n        { open: '\"', close: '\"' }\r\n    ],\r\n    folding: {\r\n        markers: {\r\n            start: new RegExp('^\\\\s*#region\\\\b'),\r\n            end: new RegExp('^\\\\s*#endregion\\\\b')\r\n        }\r\n    }\r\n};\r\nvar language = {\r\n    defaultToken: '',\r\n    tokenPostfix: '.cs',\r\n    brackets: [\r\n        { open: '{', close: '}', token: 'delimiter.curly' },\r\n        { open: '[', close: ']', token: 'delimiter.square' },\r\n        { open: '(', close: ')', token: 'delimiter.parenthesis' },\r\n        { open: '<', close: '>', token: 'delimiter.angle' }\r\n    ],\r\n    keywords: [\r\n        'extern',\r\n        'alias',\r\n        'using',\r\n        'bool',\r\n        'decimal',\r\n        'sbyte',\r\n        'byte',\r\n        'short',\r\n        'ushort',\r\n        'int',\r\n        'uint',\r\n        'long',\r\n        'ulong',\r\n        'char',\r\n        'float',\r\n        'double',\r\n        'object',\r\n        'dynamic',\r\n        'string',\r\n        'assembly',\r\n        'is',\r\n        'as',\r\n        'ref',\r\n        'out',\r\n        'this',\r\n        'base',\r\n        'new',\r\n        'typeof',\r\n        'void',\r\n        'checked',\r\n        'unchecked',\r\n        'default',\r\n        'delegate',\r\n        'var',\r\n        'const',\r\n        'if',\r\n        'else',\r\n        'switch',\r\n        'case',\r\n        'while',\r\n        'do',\r\n        'for',\r\n        'foreach',\r\n        'in',\r\n        'break',\r\n        'continue',\r\n        'goto',\r\n        'return',\r\n        'throw',\r\n        'try',\r\n        'catch',\r\n        'finally',\r\n        'lock',\r\n        'yield',\r\n        'from',\r\n        'let',\r\n        'where',\r\n        'join',\r\n        'on',\r\n        'equals',\r\n        'into',\r\n        'orderby',\r\n        'ascending',\r\n        'descending',\r\n        'select',\r\n        'group',\r\n        'by',\r\n        'namespace',\r\n        'partial',\r\n        'class',\r\n        'field',\r\n        'event',\r\n        'method',\r\n        'param',\r\n        'public',\r\n        'protected',\r\n        'internal',\r\n        'private',\r\n        'abstract',\r\n        'sealed',\r\n        'static',\r\n        'struct',\r\n        'readonly',\r\n        'volatile',\r\n        'virtual',\r\n        'override',\r\n        'params',\r\n        'get',\r\n        'set',\r\n        'add',\r\n        'remove',\r\n        'operator',\r\n        'true',\r\n        'false',\r\n        'implicit',\r\n        'explicit',\r\n        'interface',\r\n        'enum',\r\n        'null',\r\n        'async',\r\n        'await',\r\n        'fixed',\r\n        'sizeof',\r\n        'stackalloc',\r\n        'unsafe',\r\n        'nameof',\r\n        'when'\r\n    ],\r\n    namespaceFollows: ['namespace', 'using'],\r\n    parenFollows: ['if', 'for', 'while', 'switch', 'foreach', 'using', 'catch', 'when'],\r\n    operators: [\r\n        '=',\r\n        '??',\r\n        '||',\r\n        '&&',\r\n        '|',\r\n        '^',\r\n        '&',\r\n        '==',\r\n        '!=',\r\n        '<=',\r\n        '>=',\r\n        '<<',\r\n        '+',\r\n        '-',\r\n        '*',\r\n        '/',\r\n        '%',\r\n        '!',\r\n        '~',\r\n        '++',\r\n        '--',\r\n        '+=',\r\n        '-=',\r\n        '*=',\r\n        '/=',\r\n        '%=',\r\n        '&=',\r\n        '|=',\r\n        '^=',\r\n        '<<=',\r\n        '>>=',\r\n        '>>',\r\n        '=>'\r\n    ],\r\n    symbols: /[=><!~?:&|+\\-*\\/\\^%]+/,\r\n    // escape sequences\r\n    escapes: /\\\\(?:[abfnrtv\\\\\"']|x[0-9A-Fa-f]{1,4}|u[0-9A-Fa-f]{4}|U[0-9A-Fa-f]{8})/,\r\n    // The main tokenizer for our languages\r\n    tokenizer: {\r\n        root: [\r\n            // identifiers and keywords\r\n            [\r\n                /\\@?[a-zA-Z_]\\w*/,\r\n                {\r\n                    cases: {\r\n                        '@namespaceFollows': {\r\n                            token: 'keyword.$0',\r\n                            next: '@namespace'\r\n                        },\r\n                        '@keywords': {\r\n                            token: 'keyword.$0',\r\n                            next: '@qualified'\r\n                        },\r\n                        '@default': { token: 'identifier', next: '@qualified' }\r\n                    }\r\n                }\r\n            ],\r\n            // whitespace\r\n            { include: '@whitespace' },\r\n            // delimiters and operators\r\n            [\r\n                /}/,\r\n                {\r\n                    cases: {\r\n                        '$S2==interpolatedstring': {\r\n                            token: 'string.quote',\r\n                            next: '@pop'\r\n                        },\r\n                        '$S2==litinterpstring': {\r\n                            token: 'string.quote',\r\n                            next: '@pop'\r\n                        },\r\n                        '@default': '@brackets'\r\n                    }\r\n                }\r\n            ],\r\n            [/[{}()\\[\\]]/, '@brackets'],\r\n            [/[<>](?!@symbols)/, '@brackets'],\r\n            [\r\n                /@symbols/,\r\n                {\r\n                    cases: {\r\n                        '@operators': 'delimiter',\r\n                        '@default': ''\r\n                    }\r\n                }\r\n            ],\r\n            // numbers\r\n            [/[0-9_]*\\.[0-9_]+([eE][\\-+]?\\d+)?[fFdD]?/, 'number.float'],\r\n            [/0[xX][0-9a-fA-F_]+/, 'number.hex'],\r\n            [/0[bB][01_]+/, 'number.hex'],\r\n            [/[0-9_]+/, 'number'],\r\n            // delimiter: after number because of .\\d floats\r\n            [/[;,.]/, 'delimiter'],\r\n            // strings\r\n            [/\"([^\"\\\\]|\\\\.)*$/, 'string.invalid'],\r\n            [/\"/, { token: 'string.quote', next: '@string' }],\r\n            [/\\$\\@\"/, { token: 'string.quote', next: '@litinterpstring' }],\r\n            [/\\@\"/, { token: 'string.quote', next: '@litstring' }],\r\n            [/\\$\"/, { token: 'string.quote', next: '@interpolatedstring' }],\r\n            // characters\r\n            [/'[^\\\\']'/, 'string'],\r\n            [/(')(@escapes)(')/, ['string', 'string.escape', 'string']],\r\n            [/'/, 'string.invalid']\r\n        ],\r\n        qualified: [\r\n            [\r\n                /[a-zA-Z_][\\w]*/,\r\n                {\r\n                    cases: {\r\n                        '@keywords': { token: 'keyword.$0' },\r\n                        '@default': 'identifier'\r\n                    }\r\n                }\r\n            ],\r\n            [/\\./, 'delimiter'],\r\n            ['', '', '@pop']\r\n        ],\r\n        namespace: [\r\n            { include: '@whitespace' },\r\n            [/[A-Z]\\w*/, 'namespace'],\r\n            [/[\\.=]/, 'delimiter'],\r\n            ['', '', '@pop']\r\n        ],\r\n        comment: [\r\n            [/[^\\/*]+/, 'comment'],\r\n            // [/\\/\\*/,    'comment', '@push' ],    // no nested comments :-(\r\n            ['\\\\*/', 'comment', '@pop'],\r\n            [/[\\/*]/, 'comment']\r\n        ],\r\n        string: [\r\n            [/[^\\\\\"]+/, 'string'],\r\n            [/@escapes/, 'string.escape'],\r\n            [/\\\\./, 'string.escape.invalid'],\r\n            [/\"/, { token: 'string.quote', next: '@pop' }]\r\n        ],\r\n        litstring: [\r\n            [/[^\"]+/, 'string'],\r\n            [/\"\"/, 'string.escape'],\r\n            [/\"/, { token: 'string.quote', next: '@pop' }]\r\n        ],\r\n        litinterpstring: [\r\n            [/[^\"{]+/, 'string'],\r\n            [/\"\"/, 'string.escape'],\r\n            [/{{/, 'string.escape'],\r\n            [/}}/, 'string.escape'],\r\n            [/{/, { token: 'string.quote', next: 'root.litinterpstring' }],\r\n            [/\"/, { token: 'string.quote', next: '@pop' }]\r\n        ],\r\n        interpolatedstring: [\r\n            [/[^\\\\\"{]+/, 'string'],\r\n            [/@escapes/, 'string.escape'],\r\n            [/\\\\./, 'string.escape.invalid'],\r\n            [/{{/, 'string.escape'],\r\n            [/}}/, 'string.escape'],\r\n            [/{/, { token: 'string.quote', next: 'root.interpolatedstring' }],\r\n            [/\"/, { token: 'string.quote', next: '@pop' }]\r\n        ],\r\n        whitespace: [\r\n            [/^[ \\t\\v\\f]*#((r)|(load))(?=\\s)/, 'directive.csx'],\r\n            [/^[ \\t\\v\\f]*#\\w.*$/, 'namespace.cpp'],\r\n            [/[ \\t\\v\\f\\r\\n]+/, ''],\r\n            [/\\/\\*/, 'comment', '@comment'],\r\n            [/\\/\\/.*$/, 'comment']\r\n        ]\r\n    }\r\n};\r\n\n\n//# sourceURL=webpack:///./node_modules/monaco-editor/esm/vs/basic-languages/csharp/csharp.js?");

/***/ })

}]);