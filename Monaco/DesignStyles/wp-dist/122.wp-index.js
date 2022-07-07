(window["webpackJsonp"] = window["webpackJsonp"] || []).push([[122],{

/***/ "./node_modules/monaco-editor/esm/vs/basic-languages/m3/m3.js":
/*!********************************************************************!*\
  !*** ./node_modules/monaco-editor/esm/vs/basic-languages/m3/m3.js ***!
  \********************************************************************/
/*! exports provided: conf, language */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"conf\", function() { return conf; });\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"language\", function() { return language; });\n/*---------------------------------------------------------------------------------------------\r\n *  Copyright (c) Microsoft Corporation. All rights reserved.\r\n *  Licensed under the MIT License. See License.txt in the project root for license information.\r\n *--------------------------------------------------------------------------------------------*/\r\nvar conf = {\r\n    comments: {\r\n        blockComment: ['(*', '*)']\r\n    },\r\n    brackets: [\r\n        ['{', '}'],\r\n        ['[', ']'],\r\n        ['(', ')']\r\n    ],\r\n    autoClosingPairs: [\r\n        { open: '[', close: ']' },\r\n        { open: '{', close: '}' },\r\n        { open: '(', close: ')' },\r\n        { open: '(*', close: '*)' },\r\n        { open: '<*', close: '*>' },\r\n        { open: \"'\", close: \"'\", notIn: ['string', 'comment'] },\r\n        { open: '\"', close: '\"', notIn: ['string', 'comment'] }\r\n    ]\r\n};\r\nvar language = {\r\n    defaultToken: '',\r\n    tokenPostfix: '.m3',\r\n    brackets: [\r\n        { token: 'delimiter.curly', open: '{', close: '}' },\r\n        { token: 'delimiter.parenthesis', open: '(', close: ')' },\r\n        { token: 'delimiter.square', open: '[', close: ']' }\r\n    ],\r\n    keywords: [\r\n        'AND',\r\n        'ANY',\r\n        'ARRAY',\r\n        'AS',\r\n        'BEGIN',\r\n        'BITS',\r\n        'BRANDED',\r\n        'BY',\r\n        'CASE',\r\n        'CONST',\r\n        'DIV',\r\n        'DO',\r\n        'ELSE',\r\n        'ELSIF',\r\n        'END',\r\n        'EVAL',\r\n        'EXCEPT',\r\n        'EXCEPTION',\r\n        'EXIT',\r\n        'EXPORTS',\r\n        'FINALLY',\r\n        'FOR',\r\n        'FROM',\r\n        'GENERIC',\r\n        'IF',\r\n        'IMPORT',\r\n        'IN',\r\n        'INTERFACE',\r\n        'LOCK',\r\n        'LOOP',\r\n        'METHODS',\r\n        'MOD',\r\n        'MODULE',\r\n        'NOT',\r\n        'OBJECT',\r\n        'OF',\r\n        'OR',\r\n        'OVERRIDES',\r\n        'PROCEDURE',\r\n        'RAISE',\r\n        'RAISES',\r\n        'READONLY',\r\n        'RECORD',\r\n        'REF',\r\n        'REPEAT',\r\n        'RETURN',\r\n        'REVEAL',\r\n        'SET',\r\n        'THEN',\r\n        'TO',\r\n        'TRY',\r\n        'TYPE',\r\n        'TYPECASE',\r\n        'UNSAFE',\r\n        'UNTIL',\r\n        'UNTRACED',\r\n        'VALUE',\r\n        'VAR',\r\n        'WHILE',\r\n        'WITH'\r\n    ],\r\n    reservedConstNames: [\r\n        'ABS',\r\n        'ADR',\r\n        'ADRSIZE',\r\n        'BITSIZE',\r\n        'BYTESIZE',\r\n        'CEILING',\r\n        'DEC',\r\n        'DISPOSE',\r\n        'FALSE',\r\n        'FIRST',\r\n        'FLOAT',\r\n        'FLOOR',\r\n        'INC',\r\n        'ISTYPE',\r\n        'LAST',\r\n        'LOOPHOLE',\r\n        'MAX',\r\n        'MIN',\r\n        'NARROW',\r\n        'NEW',\r\n        'NIL',\r\n        'NUMBER',\r\n        'ORD',\r\n        'ROUND',\r\n        'SUBARRAY',\r\n        'TRUE',\r\n        'TRUNC',\r\n        'TYPECODE',\r\n        'VAL'\r\n    ],\r\n    reservedTypeNames: [\r\n        'ADDRESS',\r\n        'ANY',\r\n        'BOOLEAN',\r\n        'CARDINAL',\r\n        'CHAR',\r\n        'EXTENDED',\r\n        'INTEGER',\r\n        'LONGCARD',\r\n        'LONGINT',\r\n        'LONGREAL',\r\n        'MUTEX',\r\n        'NULL',\r\n        'REAL',\r\n        'REFANY',\r\n        'ROOT',\r\n        'TEXT'\r\n    ],\r\n    operators: ['+', '-', '*', '/', '&', '^', '.'],\r\n    relations: ['=', '#', '<', '<=', '>', '>=', '<:', ':'],\r\n    delimiters: ['|', '..', '=>', ',', ';', ':='],\r\n    symbols: /[>=<#.,:;+\\-*/&^]+/,\r\n    escapes: /\\\\(?:[\\\\fnrt\"']|[0-7]{3})/,\r\n    tokenizer: {\r\n        root: [\r\n            // Identifiers and keywords\r\n            [/_\\w*/, 'invalid'],\r\n            [\r\n                /[a-zA-Z][a-zA-Z0-9_]*/,\r\n                {\r\n                    cases: {\r\n                        '@keywords': { token: 'keyword.$0' },\r\n                        '@reservedConstNames': { token: 'constant.reserved.$0' },\r\n                        '@reservedTypeNames': { token: 'type.reserved.$0' },\r\n                        '@default': 'identifier'\r\n                    }\r\n                }\r\n            ],\r\n            // Whitespace\r\n            { include: '@whitespace' },\r\n            [/[{}()\\[\\]]/, '@brackets'],\r\n            // Integer- and real literals\r\n            [/[0-9]+\\.[0-9]+(?:[DdEeXx][\\+\\-]?[0-9]+)?/, 'number.float'],\r\n            [/[0-9]+(?:\\_[0-9a-fA-F]+)?L?/, 'number'],\r\n            // Operators, relations, and delimiters\r\n            [\r\n                /@symbols/,\r\n                {\r\n                    cases: {\r\n                        '@operators': 'operators',\r\n                        '@relations': 'operators',\r\n                        '@delimiters': 'delimiter',\r\n                        '@default': 'invalid'\r\n                    }\r\n                }\r\n            ],\r\n            // Character literals\r\n            [/'[^\\\\']'/, 'string.char'],\r\n            [/(')(@escapes)(')/, ['string.char', 'string.escape', 'string.char']],\r\n            [/'/, 'invalid'],\r\n            // Text literals\r\n            [/\"([^\"\\\\]|\\\\.)*$/, 'invalid'],\r\n            [/\"/, 'string.text', '@text']\r\n        ],\r\n        text: [\r\n            [/[^\\\\\"]+/, 'string.text'],\r\n            [/@escapes/, 'string.escape'],\r\n            [/\\\\./, 'invalid'],\r\n            [/\"/, 'string.text', '@pop']\r\n        ],\r\n        comment: [\r\n            [/\\(\\*/, 'comment', '@push'],\r\n            [/\\*\\)/, 'comment', '@pop'],\r\n            [/./, 'comment']\r\n        ],\r\n        pragma: [\r\n            [/<\\*/, 'keyword.pragma', '@push'],\r\n            [/\\*>/, 'keyword.pragma', '@pop'],\r\n            [/./, 'keyword.pragma']\r\n        ],\r\n        whitespace: [\r\n            [/[ \\t\\r\\n]+/, 'white'],\r\n            [/\\(\\*/, 'comment', '@comment'],\r\n            [/<\\*/, 'keyword.pragma', '@pragma']\r\n        ]\r\n    }\r\n};\r\n\n\n//# sourceURL=webpack:///./node_modules/monaco-editor/esm/vs/basic-languages/m3/m3.js?");

/***/ })

}]);