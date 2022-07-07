import { CommonTokenStream } from 'antlr4/CommonTokenStream.js';
import { DefaultErrorStrategy } from 'antlr4/error/ErrorStrategy.js';
import { ConsoleErrorListener, ErrorListener } from 'antlr4/error/ErrorListener.js';
import { InputStream } from 'antlr4/InputStream.js';
import { DesignStylesLexer, DesignStylesParser } from '../generated/grammars.js';
class MyErrorListener extends ErrorListener {
    syntaxError(recognizer, offendingSymbol, line, column, msg, e) {
        console.log("ERROR " + msg);
        console.log(recognizer + offendingSymbol + line + column + e);
    }
}
export function createLexer(input) {
    const chars = new InputStream(input);
    const lexer = new DesignStylesLexer(chars);
    lexer.strictMode = false;
    return lexer;
}
export function getTokens(input) {
    return createLexer(input).getAllTokens();
}
function createParser(input) {
    const lexer = createLexer(input);
    return createParserFromLexer(lexer);
}
function createParserFromLexer(lexer) {
    const tokens = new CommonTokenStream(lexer);
    return new DesignStylesParser(tokens);
}
export function parseTreeStr(input) {
    const lexer = createLexer(input);
    lexer.removeErrorListeners();
    lexer.addErrorListener(new MyErrorListener());
    const parser = createParserFromLexer(lexer);
    parser.removeErrorListeners();
    parser.addErrorListener(new MyErrorListener());
    const tree = parser.compilationUnit();
    return tree.toStringTree(parser.ruleNames);
}
export class Error {
    constructor(startLine, endLine, startCol, endCol, message) {
        this.startLine = startLine;
        this.endLine = endLine;
        this.startCol = startCol;
        this.endCol = endCol;
        this.message = message;
    }
}
class CollectorErrorListener extends ErrorListener {
    constructor(errors) {
        super();
        this.errors = [];
        this.errors = errors;
    }
    syntaxError(recognizer, offendingSymbol, line, column, msg, e) {
        console.log(recognizer + e);
        var endColumn = column + 1;
        if (offendingSymbol.text !== null) {
            endColumn = column + offendingSymbol.text.length;
        }
        this.errors.push(new Error(line, line, column, endColumn, msg));
    }
}
export function validate(input) {
    let errors = [];
    const lexer = createLexer(input);
    lexer.removeErrorListeners();
    lexer.addErrorListener(new ConsoleErrorListener());
    const parser = createParserFromLexer(lexer);
    parser.removeErrorListeners();
    parser.addErrorListener(new CollectorErrorListener(errors));
    const tree = parser.designstyles();
    return errors;
}
class CalcErrorStrategy extends DefaultErrorStrategy {
    constructor() {
        super(...arguments);
        this.getExpectedTokens = function (recognizer) {
            return recognizer.getExpectedTokens();
        };
        this.reportMatch = function (recognizer) {
            this.endErrorCondition(recognizer);
        };
    }
    reportUnwantedToken(recognizer) {
        return super.reportUnwantedToken(recognizer);
    }
    singleTokenDeletion(recognizer) {
        var nextTokenType = recognizer.getTokenStream().LA(2);
        if (recognizer.getTokenStream().LA(1) == DesignStylesParser.NL) {
            console.log("Null");
            return null;
        }
        var expecting = this.getExpectedTokens(recognizer);
        if (expecting.contains(nextTokenType)) {
            this.reportUnwantedToken(recognizer);
            recognizer.consume();
            var matchedSymbol = recognizer.getCurrentToken();
            this.reportMatch(recognizer);
            return matchedSymbol;
        }
        else {
            return null;
        }
    }
}
