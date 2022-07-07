"use strict";
var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (Object.prototype.hasOwnProperty.call(b, p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        if (typeof b !== "function" && b !== null)
            throw new TypeError("Class extends value " + String(b) + " is not a constructor or null");
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
Object.defineProperty(exports, "__esModule", { value: true });
exports.validate = exports.Error = exports.parseTreeStr = exports.getTokens = exports.createLexer = void 0;
var index_js_1 = require("antlr4/index.js");
var ErrorStrategy_js_1 = require("antlr4/error/ErrorStrategy.js");
var ErrorListener_js_1 = require("antlr4/error/ErrorListener.js");
var DesignStylesLexer_js_1 = require("../generated/DesignStylesLexer.js");
var DesignStylesParser_js_1 = require("../generated/DesignStylesParser.js");
var MyErrorListener = /** @class */ (function (_super) {
    __extends(MyErrorListener, _super);
    function MyErrorListener() {
        return _super !== null && _super.apply(this, arguments) || this;
    }
    MyErrorListener.prototype.syntaxError = function (recognizer, offendingSymbol, line, column, msg, e) {
        console.log("ERROR " + msg);
    };
    return MyErrorListener;
}(index_js_1.error.ErrorListener));
function createLexer(input) {
    var chars = new index_js_1.InputStream(input);
    var lexer = new DesignStylesLexer_js_1.DesignStylesLexer(chars);
    lexer.strictMode = false;
    return lexer;
}
exports.createLexer = createLexer;
function getTokens(input) {
    return createLexer(input).getAllTokens();
}
exports.getTokens = getTokens;
function createParser(input) {
    var lexer = createLexer(input);
    return createParserFromLexer(lexer);
}
function createParserFromLexer(lexer) {
    var tokens = new index_js_1.CommonTokenStream(lexer);
    return new DesignStylesParser_js_1.DesignStylesParser(tokens);
}
// function parseTree(input) {
//     const parser = createParser(input);
//     return parser.compilationUnit();
// }
function parseTreeStr(input) {
    var lexer = createLexer(input);
    lexer.removeErrorListeners();
    lexer.addErrorListener(new MyErrorListener());
    var parser = createParserFromLexer(lexer);
    parser.removeErrorListeners();
    parser.addErrorListener(new MyErrorListener());
    var tree = parser.compilationUnit();
    return tree.toStringTree(parser.ruleNames);
}
exports.parseTreeStr = parseTreeStr;
var Error = /** @class */ (function () {
    function Error(startLine, endLine, startCol, endCol, message) {
        this.startLine = startLine;
        this.endLine = endLine;
        this.startCol = startCol;
        this.endCol = endCol;
        this.message = message;
    }
    return Error;
}());
exports.Error = Error;
var CollectorErrorListener = /** @class */ (function (_super) {
    __extends(CollectorErrorListener, _super);
    function CollectorErrorListener(errors) {
        var _this = _super.call(this) || this;
        _this.errors = [];
        _this.errors = errors;
        return _this;
    }
    CollectorErrorListener.prototype.syntaxError = function (recognizer, offendingSymbol, line, column, msg, e) {
        var endColumn = column + 1;
        if (offendingSymbol.text !== null) {
            endColumn = column + offendingSymbol.text.length;
        }
        this.errors.push(new Error(line, line, column, endColumn, msg));
    };
    return CollectorErrorListener;
}(index_js_1.error.ErrorListener));
function validate(input) {
    var errors = [];
    var lexer = createLexer(input);
    lexer.removeErrorListeners();
    lexer.addErrorListener(new ErrorListener_js_1.ConsoleErrorListener());
    var parser = createParserFromLexer(lexer);
    parser.removeErrorListeners();
    parser.addErrorListener(new CollectorErrorListener(errors));
    parser._errHandler = new CalcErrorStrategy();
    var tree = parser.designstyles();
    return errors;
}
exports.validate = validate;
var CalcErrorStrategy = /** @class */ (function (_super) {
    __extends(CalcErrorStrategy, _super);
    function CalcErrorStrategy() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this.getExpectedTokens = function (recognizer) {
            return recognizer.getExpectedTokens();
        };
        _this.reportMatch = function (recognizer) {
            this.endErrorCondition(recognizer);
        };
        return _this;
    }
    CalcErrorStrategy.prototype.reportUnwantedToken = function (recognizer) {
        return _super.prototype.reportUnwantedToken.call(this, recognizer);
    };
    CalcErrorStrategy.prototype.singleTokenDeletion = function (recognizer) {
        var nextTokenType = recognizer.getTokenStream().LA(2);
        if (recognizer.getTokenStream().LA(1) == DesignStylesParser_js_1.DesignStylesParser.NL) {
            console.log("Null");
            return null;
        }
        var expecting = this.getExpectedTokens(recognizer);
        if (expecting.contains(nextTokenType)) {
            this.reportUnwantedToken(recognizer);
            // print("recoverFromMismatchedToken deleting " \
            // + str(recognizer.getTokenStream().LT(1)) \
            // + " since " + str(recognizer.getTokenStream().LT(2)) \
            // + " is what we want", file=sys.stderr)
            recognizer.consume(); // simply delete extra token
            // we want to return the token we're actually matching
            var matchedSymbol = recognizer.getCurrentToken();
            this.reportMatch(recognizer); // we know current token is correct
            return matchedSymbol;
        }
        else {
            return null;
        }
    };
    return CalcErrorStrategy;
}(ErrorStrategy_js_1.DefaultErrorStrategy));
//# sourceMappingURL=DesignStylesParserFacade.js.map