(window["webpackJsonp"] = window["webpackJsonp"] || []).push([[133],{

/***/ "./node_modules/monaco-editor/esm/vs/basic-languages/postiats/postiats.js":
/*!********************************************************************************!*\
  !*** ./node_modules/monaco-editor/esm/vs/basic-languages/postiats/postiats.js ***!
  \********************************************************************************/
/*! exports provided: conf, language */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"conf\", function() { return conf; });\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"language\", function() { return language; });\n/*---------------------------------------------------------------------------------------------\r\n *  Copyright (c) Artyom Shalkhakov. All rights reserved.\r\n *  Licensed under the MIT License. See License.txt in the project root for license information.\r\n *\r\n *  Based on the ATS/Postiats lexer by Hongwei Xi.\r\n *--------------------------------------------------------------------------------------------*/\r\nvar conf = {\r\n    comments: {\r\n        lineComment: '//',\r\n        blockComment: ['(*', '*)']\r\n    },\r\n    brackets: [\r\n        ['{', '}'],\r\n        ['[', ']'],\r\n        ['(', ')'],\r\n        ['<', '>']\r\n    ],\r\n    autoClosingPairs: [\r\n        { open: '\"', close: '\"', notIn: ['string', 'comment'] },\r\n        { open: '{', close: '}', notIn: ['string', 'comment'] },\r\n        { open: '[', close: ']', notIn: ['string', 'comment'] },\r\n        { open: '(', close: ')', notIn: ['string', 'comment'] }\r\n    ]\r\n};\r\nvar language = {\r\n    tokenPostfix: '.pats',\r\n    // TODO: staload and dynload are followed by a special kind of string literals\r\n    // with {$IDENTIFER} variables, and it also may make sense to highlight\r\n    // the punctuation (. and / and \\) differently.\r\n    // Set defaultToken to invalid to see what you do not tokenize yet\r\n    defaultToken: 'invalid',\r\n    // keyword reference: https://github.com/githwxi/ATS-Postiats/blob/master/src/pats_lexing_token.dats\r\n    keywords: [\r\n        //\r\n        'abstype',\r\n        'abst0ype',\r\n        'absprop',\r\n        'absview',\r\n        'absvtype',\r\n        'absviewtype',\r\n        'absvt0ype',\r\n        'absviewt0ype',\r\n        //\r\n        'as',\r\n        //\r\n        'and',\r\n        //\r\n        'assume',\r\n        //\r\n        'begin',\r\n        //\r\n        /*\r\n                \"case\", // CASE\r\n        */\r\n        //\r\n        'classdec',\r\n        //\r\n        'datasort',\r\n        //\r\n        'datatype',\r\n        'dataprop',\r\n        'dataview',\r\n        'datavtype',\r\n        'dataviewtype',\r\n        //\r\n        'do',\r\n        //\r\n        'end',\r\n        //\r\n        'extern',\r\n        'extype',\r\n        'extvar',\r\n        //\r\n        'exception',\r\n        //\r\n        'fn',\r\n        'fnx',\r\n        'fun',\r\n        //\r\n        'prfn',\r\n        'prfun',\r\n        //\r\n        'praxi',\r\n        'castfn',\r\n        //\r\n        'if',\r\n        'then',\r\n        'else',\r\n        //\r\n        'ifcase',\r\n        //\r\n        'in',\r\n        //\r\n        'infix',\r\n        'infixl',\r\n        'infixr',\r\n        'prefix',\r\n        'postfix',\r\n        //\r\n        'implmnt',\r\n        'implement',\r\n        //\r\n        'primplmnt',\r\n        'primplement',\r\n        //\r\n        'import',\r\n        //\r\n        /*\r\n                \"lam\", // LAM\r\n                \"llam\", // LLAM\r\n                \"fix\", // FIX\r\n        */\r\n        //\r\n        'let',\r\n        //\r\n        'local',\r\n        //\r\n        'macdef',\r\n        'macrodef',\r\n        //\r\n        'nonfix',\r\n        //\r\n        'symelim',\r\n        'symintr',\r\n        'overload',\r\n        //\r\n        'of',\r\n        'op',\r\n        //\r\n        'rec',\r\n        //\r\n        'sif',\r\n        'scase',\r\n        //\r\n        'sortdef',\r\n        /*\r\n        // HX: [sta] is now deprecated\r\n        */\r\n        'sta',\r\n        'stacst',\r\n        'stadef',\r\n        'static',\r\n        /*\r\n                \"stavar\", // T_STAVAR\r\n        */\r\n        //\r\n        'staload',\r\n        'dynload',\r\n        //\r\n        'try',\r\n        //\r\n        'tkindef',\r\n        //\r\n        /*\r\n                \"type\", // TYPE\r\n        */\r\n        'typedef',\r\n        'propdef',\r\n        'viewdef',\r\n        'vtypedef',\r\n        'viewtypedef',\r\n        //\r\n        /*\r\n                \"val\", // VAL\r\n        */\r\n        'prval',\r\n        //\r\n        'var',\r\n        'prvar',\r\n        //\r\n        'when',\r\n        'where',\r\n        //\r\n        /*\r\n                \"for\", // T_FOR\r\n                \"while\", // T_WHILE\r\n        */\r\n        //\r\n        'with',\r\n        //\r\n        'withtype',\r\n        'withprop',\r\n        'withview',\r\n        'withvtype',\r\n        'withviewtype' // WITHVIEWTYPE\r\n        //\r\n    ],\r\n    keywords_dlr: [\r\n        '$delay',\r\n        '$ldelay',\r\n        //\r\n        '$arrpsz',\r\n        '$arrptrsize',\r\n        //\r\n        '$d2ctype',\r\n        //\r\n        '$effmask',\r\n        '$effmask_ntm',\r\n        '$effmask_exn',\r\n        '$effmask_ref',\r\n        '$effmask_wrt',\r\n        '$effmask_all',\r\n        //\r\n        '$extern',\r\n        '$extkind',\r\n        '$extype',\r\n        '$extype_struct',\r\n        //\r\n        '$extval',\r\n        '$extfcall',\r\n        '$extmcall',\r\n        //\r\n        '$literal',\r\n        //\r\n        '$myfilename',\r\n        '$mylocation',\r\n        '$myfunction',\r\n        //\r\n        '$lst',\r\n        '$lst_t',\r\n        '$lst_vt',\r\n        '$list',\r\n        '$list_t',\r\n        '$list_vt',\r\n        //\r\n        '$rec',\r\n        '$rec_t',\r\n        '$rec_vt',\r\n        '$record',\r\n        '$record_t',\r\n        '$record_vt',\r\n        //\r\n        '$tup',\r\n        '$tup_t',\r\n        '$tup_vt',\r\n        '$tuple',\r\n        '$tuple_t',\r\n        '$tuple_vt',\r\n        //\r\n        '$break',\r\n        '$continue',\r\n        //\r\n        '$raise',\r\n        //\r\n        '$showtype',\r\n        //\r\n        '$vcopyenv_v',\r\n        '$vcopyenv_vt',\r\n        //\r\n        '$tempenver',\r\n        //\r\n        '$solver_assert',\r\n        '$solver_verify' // T_DLRSOLVERIFY\r\n    ],\r\n    keywords_srp: [\r\n        //\r\n        '#if',\r\n        '#ifdef',\r\n        '#ifndef',\r\n        //\r\n        '#then',\r\n        //\r\n        '#elif',\r\n        '#elifdef',\r\n        '#elifndef',\r\n        //\r\n        '#else',\r\n        '#endif',\r\n        //\r\n        '#error',\r\n        //\r\n        '#prerr',\r\n        '#print',\r\n        //\r\n        '#assert',\r\n        //\r\n        '#undef',\r\n        '#define',\r\n        //\r\n        '#include',\r\n        '#require',\r\n        //\r\n        '#pragma',\r\n        '#codegen2',\r\n        '#codegen3' // T_SRPCODEGEN3 // for level-3 codegen\r\n        //\r\n        // HX: end of special tokens\r\n        //\r\n    ],\r\n    irregular_keyword_list: [\r\n        'val+',\r\n        'val-',\r\n        'val',\r\n        'case+',\r\n        'case-',\r\n        'case',\r\n        'addr@',\r\n        'addr',\r\n        'fold@',\r\n        'free@',\r\n        'fix@',\r\n        'fix',\r\n        'lam@',\r\n        'lam',\r\n        'llam@',\r\n        'llam',\r\n        'viewt@ype+',\r\n        'viewt@ype-',\r\n        'viewt@ype',\r\n        'viewtype+',\r\n        'viewtype-',\r\n        'viewtype',\r\n        'view+',\r\n        'view-',\r\n        'view@',\r\n        'view',\r\n        'type+',\r\n        'type-',\r\n        'type',\r\n        'vtype+',\r\n        'vtype-',\r\n        'vtype',\r\n        'vt@ype+',\r\n        'vt@ype-',\r\n        'vt@ype',\r\n        'viewt@ype+',\r\n        'viewt@ype-',\r\n        'viewt@ype',\r\n        'viewtype+',\r\n        'viewtype-',\r\n        'viewtype',\r\n        'prop+',\r\n        'prop-',\r\n        'prop',\r\n        'type+',\r\n        'type-',\r\n        'type',\r\n        't@ype',\r\n        't@ype+',\r\n        't@ype-',\r\n        'abst@ype',\r\n        'abstype',\r\n        'absviewt@ype',\r\n        'absvt@ype',\r\n        'for*',\r\n        'for',\r\n        'while*',\r\n        'while'\r\n    ],\r\n    keywords_types: [\r\n        'bool',\r\n        'double',\r\n        'byte',\r\n        'int',\r\n        'short',\r\n        'char',\r\n        'void',\r\n        'unit',\r\n        'long',\r\n        'float',\r\n        'string',\r\n        'strptr'\r\n    ],\r\n    // TODO: reference for this?\r\n    keywords_effects: [\r\n        '0',\r\n        'fun',\r\n        'clo',\r\n        'prf',\r\n        'funclo',\r\n        'cloptr',\r\n        'cloref',\r\n        'ref',\r\n        'ntm',\r\n        '1' // all effects\r\n    ],\r\n    operators: [\r\n        '@',\r\n        '!',\r\n        '|',\r\n        '`',\r\n        ':',\r\n        '$',\r\n        '.',\r\n        '=',\r\n        '#',\r\n        '~',\r\n        //\r\n        '..',\r\n        '...',\r\n        //\r\n        '=>',\r\n        // \"=<\", // T_EQLT\r\n        '=<>',\r\n        '=/=>',\r\n        '=>>',\r\n        '=/=>>',\r\n        //\r\n        '<',\r\n        '>',\r\n        //\r\n        '><',\r\n        //\r\n        '.<',\r\n        '>.',\r\n        //\r\n        '.<>.',\r\n        //\r\n        '->',\r\n        //\"-<\", // T_MINUSLT\r\n        '-<>' // T_MINUSLTGT\r\n        //\r\n        /*\r\n                \":<\", // T_COLONLT\r\n        */\r\n    ],\r\n    brackets: [\r\n        { open: ',(', close: ')', token: 'delimiter.parenthesis' },\r\n        { open: '`(', close: ')', token: 'delimiter.parenthesis' },\r\n        { open: '%(', close: ')', token: 'delimiter.parenthesis' },\r\n        { open: \"'(\", close: ')', token: 'delimiter.parenthesis' },\r\n        { open: \"'{\", close: '}', token: 'delimiter.parenthesis' },\r\n        { open: '@(', close: ')', token: 'delimiter.parenthesis' },\r\n        { open: '@{', close: '}', token: 'delimiter.brace' },\r\n        { open: '@[', close: ']', token: 'delimiter.square' },\r\n        { open: '#[', close: ']', token: 'delimiter.square' },\r\n        { open: '{', close: '}', token: 'delimiter.curly' },\r\n        { open: '[', close: ']', token: 'delimiter.square' },\r\n        { open: '(', close: ')', token: 'delimiter.parenthesis' },\r\n        { open: '<', close: '>', token: 'delimiter.angle' }\r\n    ],\r\n    // we include these common regular expressions\r\n    symbols: /[=><!~?:&|+\\-*\\/\\^%]+/,\r\n    IDENTFST: /[a-zA-Z_]/,\r\n    IDENTRST: /[a-zA-Z0-9_'$]/,\r\n    symbolic: /[%&+-./:=@~`^|*!$#?<>]/,\r\n    digit: /[0-9]/,\r\n    digitseq0: /@digit*/,\r\n    xdigit: /[0-9A-Za-z]/,\r\n    xdigitseq0: /@xdigit*/,\r\n    INTSP: /[lLuU]/,\r\n    FLOATSP: /[fFlL]/,\r\n    fexponent: /[eE][+-]?[0-9]+/,\r\n    fexponent_bin: /[pP][+-]?[0-9]+/,\r\n    deciexp: /\\.[0-9]*@fexponent?/,\r\n    hexiexp: /\\.[0-9a-zA-Z]*@fexponent_bin?/,\r\n    irregular_keywords: /val[+-]?|case[+-]?|addr\\@?|fold\\@|free\\@|fix\\@?|lam\\@?|llam\\@?|prop[+-]?|type[+-]?|view[+-@]?|viewt@?ype[+-]?|t@?ype[+-]?|v(iew)?t@?ype[+-]?|abst@?ype|absv(iew)?t@?ype|for\\*?|while\\*?/,\r\n    ESCHAR: /[ntvbrfa\\\\\\?'\"\\(\\[\\{]/,\r\n    start: 'root',\r\n    // The main tokenizer for ATS/Postiats\r\n    // reference: https://github.com/githwxi/ATS-Postiats/blob/master/src/pats_lexing.dats\r\n    tokenizer: {\r\n        root: [\r\n            // lexing_blankseq0\r\n            { regex: /[ \\t\\r\\n]+/, action: { token: '' } },\r\n            // NOTE: (*) is an invalid ML-like comment!\r\n            { regex: /\\(\\*\\)/, action: { token: 'invalid' } },\r\n            {\r\n                regex: /\\(\\*/,\r\n                action: { token: 'comment', next: 'lexing_COMMENT_block_ml' }\r\n            },\r\n            {\r\n                regex: /\\(/,\r\n                action: '@brackets' /*{ token: 'delimiter.parenthesis' }*/\r\n            },\r\n            {\r\n                regex: /\\)/,\r\n                action: '@brackets' /*{ token: 'delimiter.parenthesis' }*/\r\n            },\r\n            {\r\n                regex: /\\[/,\r\n                action: '@brackets' /*{ token: 'delimiter.bracket' }*/\r\n            },\r\n            {\r\n                regex: /\\]/,\r\n                action: '@brackets' /*{ token: 'delimiter.bracket' }*/\r\n            },\r\n            {\r\n                regex: /\\{/,\r\n                action: '@brackets' /*{ token: 'delimiter.brace' }*/\r\n            },\r\n            {\r\n                regex: /\\}/,\r\n                action: '@brackets' /*{ token: 'delimiter.brace' }*/\r\n            },\r\n            // lexing_COMMA\r\n            {\r\n                regex: /,\\(/,\r\n                action: '@brackets' /*{ token: 'delimiter.parenthesis' }*/\r\n            },\r\n            { regex: /,/, action: { token: 'delimiter.comma' } },\r\n            { regex: /;/, action: { token: 'delimiter.semicolon' } },\r\n            // lexing_AT\r\n            {\r\n                regex: /@\\(/,\r\n                action: '@brackets' /* { token: 'delimiter.parenthesis' }*/\r\n            },\r\n            {\r\n                regex: /@\\[/,\r\n                action: '@brackets' /* { token: 'delimiter.bracket' }*/\r\n            },\r\n            {\r\n                regex: /@\\{/,\r\n                action: '@brackets' /*{ token: 'delimiter.brace' }*/\r\n            },\r\n            // lexing_COLON\r\n            {\r\n                regex: /:</,\r\n                action: { token: 'keyword', next: '@lexing_EFFECT_commaseq0' }\r\n            },\r\n            /*\r\n            lexing_DOT:\r\n\r\n            . // SYMBOLIC => lexing_IDENT_sym\r\n            . FLOATDOT => lexing_FLOAT_deciexp\r\n            . DIGIT => T_DOTINT\r\n            */\r\n            { regex: /\\.@symbolic+/, action: { token: 'identifier.sym' } },\r\n            // FLOATDOT case\r\n            {\r\n                regex: /\\.@digit*@fexponent@FLOATSP*/,\r\n                action: { token: 'number.float' }\r\n            },\r\n            { regex: /\\.@digit+/, action: { token: 'number.float' } },\r\n            // lexing_DOLLAR:\r\n            // '$' IDENTFST IDENTRST* => lexing_IDENT_dlr, _ => lexing_IDENT_sym\r\n            {\r\n                regex: /\\$@IDENTFST@IDENTRST*/,\r\n                action: {\r\n                    cases: {\r\n                        '@keywords_dlr': { token: 'keyword.dlr' },\r\n                        '@default': { token: 'namespace' } // most likely a module qualifier\r\n                    }\r\n                }\r\n            },\r\n            // lexing_SHARP:\r\n            // '#' IDENTFST IDENTRST* => lexing_ident_srp, _ => lexing_IDENT_sym\r\n            {\r\n                regex: /\\#@IDENTFST@IDENTRST*/,\r\n                action: {\r\n                    cases: {\r\n                        '@keywords_srp': { token: 'keyword.srp' },\r\n                        '@default': { token: 'identifier' }\r\n                    }\r\n                }\r\n            },\r\n            // lexing_PERCENT:\r\n            { regex: /%\\(/, action: { token: 'delimiter.parenthesis' } },\r\n            {\r\n                regex: /^%{(#|\\^|\\$)?/,\r\n                action: {\r\n                    token: 'keyword',\r\n                    next: '@lexing_EXTCODE',\r\n                    nextEmbedded: 'text/javascript'\r\n                }\r\n            },\r\n            { regex: /^%}/, action: { token: 'keyword' } },\r\n            // lexing_QUOTE\r\n            { regex: /'\\(/, action: { token: 'delimiter.parenthesis' } },\r\n            { regex: /'\\[/, action: { token: 'delimiter.bracket' } },\r\n            { regex: /'\\{/, action: { token: 'delimiter.brace' } },\r\n            [/(')(\\\\@ESCHAR|\\\\[xX]@xdigit+|\\\\@digit+)(')/, ['string', 'string.escape', 'string']],\r\n            [/'[^\\\\']'/, 'string'],\r\n            // lexing_DQUOTE\r\n            [/\"/, 'string.quote', '@lexing_DQUOTE'],\r\n            // lexing_BQUOTE\r\n            {\r\n                regex: /`\\(/,\r\n                action: '@brackets' /* { token: 'delimiter.parenthesis' }*/\r\n            },\r\n            // TODO: otherwise, try lexing_IDENT_sym\r\n            { regex: /\\\\/, action: { token: 'punctuation' } },\r\n            // lexing_IDENT_alp:\r\n            // NOTE: (?!regex) is syntax for \"not-followed-by\" regex\r\n            // to resolve ambiguity such as foreach$fwork being incorrectly lexed as [for] [each$fwork]!\r\n            {\r\n                regex: /@irregular_keywords(?!@IDENTRST)/,\r\n                action: { token: 'keyword' }\r\n            },\r\n            {\r\n                regex: /@IDENTFST@IDENTRST*[<!\\[]?/,\r\n                action: {\r\n                    cases: {\r\n                        // TODO: dynload and staload should be specially parsed\r\n                        // dynload whitespace+ \"special_string\"\r\n                        // this special string is really:\r\n                        //  '/' '\\\\' '.' => punctuation\r\n                        // ({\\$)([a-zA-Z_][a-zA-Z_0-9]*)(}) => punctuation,keyword,punctuation\r\n                        // [^\"] => identifier/literal\r\n                        '@keywords': { token: 'keyword' },\r\n                        '@keywords_types': { token: 'type' },\r\n                        '@default': { token: 'identifier' }\r\n                    }\r\n                }\r\n            },\r\n            // lexing_IDENT_sym:\r\n            {\r\n                regex: /\\/\\/\\/\\//,\r\n                action: { token: 'comment', next: '@lexing_COMMENT_rest' }\r\n            },\r\n            { regex: /\\/\\/.*$/, action: { token: 'comment' } },\r\n            {\r\n                regex: /\\/\\*/,\r\n                action: { token: 'comment', next: '@lexing_COMMENT_block_c' }\r\n            },\r\n            // AS-20160627: specifically for effect annotations\r\n            {\r\n                regex: /-<|=</,\r\n                action: { token: 'keyword', next: '@lexing_EFFECT_commaseq0' }\r\n            },\r\n            {\r\n                regex: /@symbolic+/,\r\n                action: {\r\n                    cases: {\r\n                        '@operators': 'keyword',\r\n                        '@default': 'operator'\r\n                    }\r\n                }\r\n            },\r\n            // lexing_ZERO:\r\n            // FIXME: this one is quite messy/unfinished yet\r\n            // TODO: lexing_INT_hex\r\n            // - testing_hexiexp => lexing_FLOAT_hexiexp\r\n            // - testing_fexponent_bin => lexing_FLOAT_hexiexp\r\n            // - testing_intspseq0 => T_INT_hex\r\n            // lexing_INT_hex:\r\n            {\r\n                regex: /0[xX]@xdigit+(@hexiexp|@fexponent_bin)@FLOATSP*/,\r\n                action: { token: 'number.float' }\r\n            },\r\n            { regex: /0[xX]@xdigit+@INTSP*/, action: { token: 'number.hex' } },\r\n            {\r\n                regex: /0[0-7]+(?![0-9])@INTSP*/,\r\n                action: { token: 'number.octal' }\r\n            },\r\n            //{regex: /0/, action: { token: 'number' } }, // INTZERO\r\n            // lexing_INT_dec:\r\n            // - testing_deciexp => lexing_FLOAT_deciexp\r\n            // - testing_fexponent => lexing_FLOAT_deciexp\r\n            // - otherwise => intspseq0 ([0-9]*[lLuU]?)\r\n            {\r\n                regex: /@digit+(@fexponent|@deciexp)@FLOATSP*/,\r\n                action: { token: 'number.float' }\r\n            },\r\n            {\r\n                regex: /@digit@digitseq0@INTSP*/,\r\n                action: { token: 'number.decimal' }\r\n            },\r\n            // DIGIT, if followed by digitseq0, is lexing_INT_dec\r\n            { regex: /@digit+@INTSP*/, action: { token: 'number' } }\r\n        ],\r\n        lexing_COMMENT_block_ml: [\r\n            [/[^\\(\\*]+/, 'comment'],\r\n            [/\\(\\*/, 'comment', '@push'],\r\n            [/\\(\\*/, 'comment.invalid'],\r\n            [/\\*\\)/, 'comment', '@pop'],\r\n            [/\\*/, 'comment']\r\n        ],\r\n        lexing_COMMENT_block_c: [\r\n            [/[^\\/*]+/, 'comment'],\r\n            // [/\\/\\*/, 'comment', '@push' ],    // nested C-style block comments not allowed\r\n            // [/\\/\\*/,    'comment.invalid' ],\t// NOTE: this breaks block comments in the shape of /* //*/\r\n            [/\\*\\//, 'comment', '@pop'],\r\n            [/[\\/*]/, 'comment']\r\n        ],\r\n        lexing_COMMENT_rest: [\r\n            [/$/, 'comment', '@pop'],\r\n            [/.*/, 'comment']\r\n        ],\r\n        // NOTE: added by AS, specifically for highlighting\r\n        lexing_EFFECT_commaseq0: [\r\n            {\r\n                regex: /@IDENTFST@IDENTRST+|@digit+/,\r\n                action: {\r\n                    cases: {\r\n                        '@keywords_effects': { token: 'type.effect' },\r\n                        '@default': { token: 'identifier' }\r\n                    }\r\n                }\r\n            },\r\n            { regex: /,/, action: { token: 'punctuation' } },\r\n            { regex: />/, action: { token: '@rematch', next: '@pop' } }\r\n        ],\r\n        lexing_EXTCODE: [\r\n            {\r\n                regex: /^%}/,\r\n                action: {\r\n                    token: '@rematch',\r\n                    next: '@pop',\r\n                    nextEmbedded: '@pop'\r\n                }\r\n            },\r\n            { regex: /[^%]+/, action: '' }\r\n        ],\r\n        lexing_DQUOTE: [\r\n            { regex: /\"/, action: { token: 'string.quote', next: '@pop' } },\r\n            // AS-20160628: additional hi-lighting for variables in staload/dynload strings\r\n            {\r\n                regex: /(\\{\\$)(@IDENTFST@IDENTRST*)(\\})/,\r\n                action: [\r\n                    { token: 'string.escape' },\r\n                    { token: 'identifier' },\r\n                    { token: 'string.escape' }\r\n                ]\r\n            },\r\n            { regex: /\\\\$/, action: { token: 'string.escape' } },\r\n            {\r\n                regex: /\\\\(@ESCHAR|[xX]@xdigit+|@digit+)/,\r\n                action: { token: 'string.escape' }\r\n            },\r\n            { regex: /[^\\\\\"]+/, action: { token: 'string' } }\r\n        ]\r\n    }\r\n};\r\n\n\n//# sourceURL=webpack:///./node_modules/monaco-editor/esm/vs/basic-languages/postiats/postiats.js?");

/***/ })

}]);