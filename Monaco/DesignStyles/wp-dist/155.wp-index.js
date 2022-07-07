(window["webpackJsonp"] = window["webpackJsonp"] || []).push([[155],{

/***/ "./node_modules/monaco-editor/esm/vs/basic-languages/st/st.js":
/*!********************************************************************!*\
  !*** ./node_modules/monaco-editor/esm/vs/basic-languages/st/st.js ***!
  \********************************************************************/
/*! exports provided: conf, language */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"conf\", function() { return conf; });\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"language\", function() { return language; });\n/*---------------------------------------------------------------------------------------------\r\n *  Copyright (c) Microsoft Corporation. All rights reserved.\r\n *  Licensed under the MIT License. See License.txt in the project root for license information.\r\n *--------------------------------------------------------------------------------------------*/\r\nvar conf = {\r\n    comments: {\r\n        lineComment: '//',\r\n        blockComment: ['(*', '*)']\r\n    },\r\n    brackets: [\r\n        ['{', '}'],\r\n        ['[', ']'],\r\n        ['(', ')'],\r\n        ['var', 'end_var'],\r\n        ['var_input', 'end_var'],\r\n        ['var_output', 'end_var'],\r\n        ['var_in_out', 'end_var'],\r\n        ['var_temp', 'end_var'],\r\n        ['var_global', 'end_var'],\r\n        ['var_access', 'end_var'],\r\n        ['var_external', 'end_var'],\r\n        ['type', 'end_type'],\r\n        ['struct', 'end_struct'],\r\n        ['program', 'end_program'],\r\n        ['function', 'end_function'],\r\n        ['function_block', 'end_function_block'],\r\n        ['action', 'end_action'],\r\n        ['step', 'end_step'],\r\n        ['initial_step', 'end_step'],\r\n        ['transaction', 'end_transaction'],\r\n        ['configuration', 'end_configuration'],\r\n        ['tcp', 'end_tcp'],\r\n        ['recource', 'end_recource'],\r\n        ['channel', 'end_channel'],\r\n        ['library', 'end_library'],\r\n        ['folder', 'end_folder'],\r\n        ['binaries', 'end_binaries'],\r\n        ['includes', 'end_includes'],\r\n        ['sources', 'end_sources']\r\n    ],\r\n    autoClosingPairs: [\r\n        { open: '[', close: ']' },\r\n        { open: '{', close: '}' },\r\n        { open: '(', close: ')' },\r\n        { open: '/*', close: '*/' },\r\n        { open: \"'\", close: \"'\", notIn: ['string_sq'] },\r\n        { open: '\"', close: '\"', notIn: ['string_dq'] },\r\n        { open: 'var_input', close: 'end_var' },\r\n        { open: 'var_output', close: 'end_var' },\r\n        { open: 'var_in_out', close: 'end_var' },\r\n        { open: 'var_temp', close: 'end_var' },\r\n        { open: 'var_global', close: 'end_var' },\r\n        { open: 'var_access', close: 'end_var' },\r\n        { open: 'var_external', close: 'end_var' },\r\n        { open: 'type', close: 'end_type' },\r\n        { open: 'struct', close: 'end_struct' },\r\n        { open: 'program', close: 'end_program' },\r\n        { open: 'function', close: 'end_function' },\r\n        { open: 'function_block', close: 'end_function_block' },\r\n        { open: 'action', close: 'end_action' },\r\n        { open: 'step', close: 'end_step' },\r\n        { open: 'initial_step', close: 'end_step' },\r\n        { open: 'transaction', close: 'end_transaction' },\r\n        { open: 'configuration', close: 'end_configuration' },\r\n        { open: 'tcp', close: 'end_tcp' },\r\n        { open: 'recource', close: 'end_recource' },\r\n        { open: 'channel', close: 'end_channel' },\r\n        { open: 'library', close: 'end_library' },\r\n        { open: 'folder', close: 'end_folder' },\r\n        { open: 'binaries', close: 'end_binaries' },\r\n        { open: 'includes', close: 'end_includes' },\r\n        { open: 'sources', close: 'end_sources' }\r\n    ],\r\n    surroundingPairs: [\r\n        { open: '{', close: '}' },\r\n        { open: '[', close: ']' },\r\n        { open: '(', close: ')' },\r\n        { open: '\"', close: '\"' },\r\n        { open: \"'\", close: \"'\" },\r\n        { open: 'var', close: 'end_var' },\r\n        { open: 'var_input', close: 'end_var' },\r\n        { open: 'var_output', close: 'end_var' },\r\n        { open: 'var_in_out', close: 'end_var' },\r\n        { open: 'var_temp', close: 'end_var' },\r\n        { open: 'var_global', close: 'end_var' },\r\n        { open: 'var_access', close: 'end_var' },\r\n        { open: 'var_external', close: 'end_var' },\r\n        { open: 'type', close: 'end_type' },\r\n        { open: 'struct', close: 'end_struct' },\r\n        { open: 'program', close: 'end_program' },\r\n        { open: 'function', close: 'end_function' },\r\n        { open: 'function_block', close: 'end_function_block' },\r\n        { open: 'action', close: 'end_action' },\r\n        { open: 'step', close: 'end_step' },\r\n        { open: 'initial_step', close: 'end_step' },\r\n        { open: 'transaction', close: 'end_transaction' },\r\n        { open: 'configuration', close: 'end_configuration' },\r\n        { open: 'tcp', close: 'end_tcp' },\r\n        { open: 'recource', close: 'end_recource' },\r\n        { open: 'channel', close: 'end_channel' },\r\n        { open: 'library', close: 'end_library' },\r\n        { open: 'folder', close: 'end_folder' },\r\n        { open: 'binaries', close: 'end_binaries' },\r\n        { open: 'includes', close: 'end_includes' },\r\n        { open: 'sources', close: 'end_sources' }\r\n    ],\r\n    folding: {\r\n        markers: {\r\n            start: new RegExp('^\\\\s*#pragma\\\\s+region\\\\b'),\r\n            end: new RegExp('^\\\\s*#pragma\\\\s+endregion\\\\b')\r\n        }\r\n    }\r\n};\r\nvar language = {\r\n    defaultToken: '',\r\n    tokenPostfix: '.st',\r\n    ignoreCase: true,\r\n    brackets: [\r\n        { token: 'delimiter.curly', open: '{', close: '}' },\r\n        { token: 'delimiter.parenthesis', open: '(', close: ')' },\r\n        { token: 'delimiter.square', open: '[', close: ']' }\r\n    ],\r\n    keywords: [\r\n        'if',\r\n        'end_if',\r\n        'elsif',\r\n        'else',\r\n        'case',\r\n        'of',\r\n        'to',\r\n        '__try',\r\n        '__catch',\r\n        '__finally',\r\n        'do',\r\n        'with',\r\n        'by',\r\n        'while',\r\n        'repeat',\r\n        'end_while',\r\n        'end_repeat',\r\n        'end_case',\r\n        'for',\r\n        'end_for',\r\n        'task',\r\n        'retain',\r\n        'non_retain',\r\n        'constant',\r\n        'with',\r\n        'at',\r\n        'exit',\r\n        'return',\r\n        'interval',\r\n        'priority',\r\n        'address',\r\n        'port',\r\n        'on_channel',\r\n        'then',\r\n        'iec',\r\n        'file',\r\n        'uses',\r\n        'version',\r\n        'packagetype',\r\n        'displayname',\r\n        'copyright',\r\n        'summary',\r\n        'vendor',\r\n        'common_source',\r\n        'from',\r\n        'extends'\r\n    ],\r\n    constant: ['false', 'true', 'null'],\r\n    defineKeywords: [\r\n        'var',\r\n        'var_input',\r\n        'var_output',\r\n        'var_in_out',\r\n        'var_temp',\r\n        'var_global',\r\n        'var_access',\r\n        'var_external',\r\n        'end_var',\r\n        'type',\r\n        'end_type',\r\n        'struct',\r\n        'end_struct',\r\n        'program',\r\n        'end_program',\r\n        'function',\r\n        'end_function',\r\n        'function_block',\r\n        'end_function_block',\r\n        'interface',\r\n        'end_interface',\r\n        'method',\r\n        'end_method',\r\n        'property',\r\n        'end_property',\r\n        'namespace',\r\n        'end_namespace',\r\n        'configuration',\r\n        'end_configuration',\r\n        'tcp',\r\n        'end_tcp',\r\n        'resource',\r\n        'end_resource',\r\n        'channel',\r\n        'end_channel',\r\n        'library',\r\n        'end_library',\r\n        'folder',\r\n        'end_folder',\r\n        'binaries',\r\n        'end_binaries',\r\n        'includes',\r\n        'end_includes',\r\n        'sources',\r\n        'end_sources',\r\n        'action',\r\n        'end_action',\r\n        'step',\r\n        'initial_step',\r\n        'end_step',\r\n        'transaction',\r\n        'end_transaction'\r\n    ],\r\n    typeKeywords: [\r\n        'int',\r\n        'sint',\r\n        'dint',\r\n        'lint',\r\n        'usint',\r\n        'uint',\r\n        'udint',\r\n        'ulint',\r\n        'real',\r\n        'lreal',\r\n        'time',\r\n        'date',\r\n        'time_of_day',\r\n        'date_and_time',\r\n        'string',\r\n        'bool',\r\n        'byte',\r\n        'word',\r\n        'dword',\r\n        'array',\r\n        'pointer',\r\n        'lword'\r\n    ],\r\n    operators: [\r\n        '=',\r\n        '>',\r\n        '<',\r\n        ':',\r\n        ':=',\r\n        '<=',\r\n        '>=',\r\n        '<>',\r\n        '&',\r\n        '+',\r\n        '-',\r\n        '*',\r\n        '**',\r\n        'MOD',\r\n        '^',\r\n        'or',\r\n        'and',\r\n        'not',\r\n        'xor',\r\n        'abs',\r\n        'acos',\r\n        'asin',\r\n        'atan',\r\n        'cos',\r\n        'exp',\r\n        'expt',\r\n        'ln',\r\n        'log',\r\n        'sin',\r\n        'sqrt',\r\n        'tan',\r\n        'sel',\r\n        'max',\r\n        'min',\r\n        'limit',\r\n        'mux',\r\n        'shl',\r\n        'shr',\r\n        'rol',\r\n        'ror',\r\n        'indexof',\r\n        'sizeof',\r\n        'adr',\r\n        'adrinst',\r\n        'bitadr',\r\n        'is_valid',\r\n        'ref',\r\n        'ref_to'\r\n    ],\r\n    builtinVariables: [],\r\n    builtinFunctions: [\r\n        'sr',\r\n        'rs',\r\n        'tp',\r\n        'ton',\r\n        'tof',\r\n        'eq',\r\n        'ge',\r\n        'le',\r\n        'lt',\r\n        'ne',\r\n        'round',\r\n        'trunc',\r\n        'ctd',\r\n        'сtu',\r\n        'ctud',\r\n        'r_trig',\r\n        'f_trig',\r\n        'move',\r\n        'concat',\r\n        'delete',\r\n        'find',\r\n        'insert',\r\n        'left',\r\n        'len',\r\n        'replace',\r\n        'right',\r\n        'rtc'\r\n    ],\r\n    // we include these common regular expressions\r\n    symbols: /[=><!~?:&|+\\-*\\/\\^%]+/,\r\n    // C# style strings\r\n    escapes: /\\\\(?:[abfnrtv\\\\\"']|x[0-9A-Fa-f]{1,4}|u[0-9A-Fa-f]{4}|U[0-9A-Fa-f]{8})/,\r\n    // The main tokenizer for our languages\r\n    tokenizer: {\r\n        root: [\r\n            [/(\\.\\.)/, 'delimiter'],\r\n            [/\\b(16#[0-9A-Fa-f\\_]*)+\\b/, 'number.hex'],\r\n            [/\\b(2#[01\\_]+)+\\b/, 'number.binary'],\r\n            [/\\b(8#[0-9\\_]*)+\\b/, 'number.octal'],\r\n            [/\\b\\d*\\.\\d+([eE][\\-+]?\\d+)?\\b/, 'number.float'],\r\n            [/\\b(L?REAL)#[0-9\\_\\.e]+\\b/, 'number.float'],\r\n            [/\\b(BYTE|(?:D|L)?WORD|U?(?:S|D|L)?INT)#[0-9\\_]+\\b/, 'number'],\r\n            [/\\d+/, 'number'],\r\n            [/\\b(T|DT|TOD)#[0-9:-_shmyd]+\\b/, 'tag'],\r\n            [/\\%(I|Q|M)(X|B|W|D|L)[0-9\\.]+/, 'tag'],\r\n            [/\\%(I|Q|M)[0-9\\.]*/, 'tag'],\r\n            [/\\b[A-Za-z]{1,6}#[0-9]+\\b/, 'tag'],\r\n            [/\\b(TO_|CTU_|CTD_|CTUD_|MUX_|SEL_)[A_Za-z]+\\b/, 'predefined'],\r\n            [/\\b[A_Za-z]+(_TO_)[A_Za-z]+\\b/, 'predefined'],\r\n            [/[;]/, 'delimiter'],\r\n            [/[.]/, { token: 'delimiter', next: '@params' }],\r\n            // identifiers and keywords\r\n            [\r\n                /[a-zA-Z_]\\w*/,\r\n                {\r\n                    cases: {\r\n                        '@operators': 'operators',\r\n                        '@keywords': 'keyword',\r\n                        '@typeKeywords': 'type',\r\n                        '@defineKeywords': 'variable',\r\n                        '@constant': 'constant',\r\n                        '@builtinVariables': 'predefined',\r\n                        '@builtinFunctions': 'predefined',\r\n                        '@default': 'identifier'\r\n                    }\r\n                }\r\n            ],\r\n            { include: '@whitespace' },\r\n            [/[{}()\\[\\]]/, '@brackets'],\r\n            [/\"([^\"\\\\]|\\\\.)*$/, 'string.invalid'],\r\n            [/\"/, { token: 'string.quote', bracket: '@open', next: '@string_dq' }],\r\n            [/'/, { token: 'string.quote', bracket: '@open', next: '@string_sq' }],\r\n            [/'[^\\\\']'/, 'string'],\r\n            [/(')(@escapes)(')/, ['string', 'string.escape', 'string']],\r\n            [/'/, 'string.invalid']\r\n        ],\r\n        params: [\r\n            [/\\b[A-Za-z0-9_]+\\b(?=\\()/, { token: 'identifier', next: '@pop' }],\r\n            [/\\b[A-Za-z0-9_]+\\b/, 'variable.name', '@pop']\r\n        ],\r\n        comment: [\r\n            [/[^\\/*]+/, 'comment'],\r\n            [/\\/\\*/, 'comment', '@push'],\r\n            ['\\\\*/', 'comment', '@pop'],\r\n            [/[\\/*]/, 'comment']\r\n        ],\r\n        comment2: [\r\n            [/[^\\(*]+/, 'comment'],\r\n            [/\\(\\*/, 'comment', '@push'],\r\n            ['\\\\*\\\\)', 'comment', '@pop'],\r\n            [/[\\(*]/, 'comment']\r\n        ],\r\n        whitespace: [\r\n            [/[ \\t\\r\\n]+/, 'white'],\r\n            [/\\/\\/.*$/, 'comment'],\r\n            [/\\/\\*/, 'comment', '@comment'],\r\n            [/\\(\\*/, 'comment', '@comment2']\r\n        ],\r\n        string_dq: [\r\n            [/[^\\\\\"]+/, 'string'],\r\n            [/@escapes/, 'string.escape'],\r\n            [/\\\\./, 'string.escape.invalid'],\r\n            [/\"/, { token: 'string.quote', bracket: '@close', next: '@pop' }]\r\n        ],\r\n        string_sq: [\r\n            [/[^\\\\']+/, 'string'],\r\n            [/@escapes/, 'string.escape'],\r\n            [/\\\\./, 'string.escape.invalid'],\r\n            [/'/, { token: 'string.quote', bracket: '@close', next: '@pop' }]\r\n        ]\r\n    }\r\n};\r\n\n\n//# sourceURL=webpack:///./node_modules/monaco-editor/esm/vs/basic-languages/st/st.js?");

/***/ })

}]);