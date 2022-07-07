import * as antlr4 from 'antlr4';
import { DesignStylesLexer, DesignStylesParser } from '../generated/grammars';
import { DesignStylesListenerImpl } from './DesignStylesListenerImpl';
var listener = null;
var editorContentCache = "";
export function getDesignStyles(editorContent) {
    createListener(editorContent);
    return listener.getDesignStyles();
}
;
export function getClasses(editorContent) {
    createListener(editorContent);
    return listener.getClasses();
}
;
function createListener(editorContent) {
    if (editorContentCache.trim() != editorContent.trim()) {
        editorContentCache = editorContent.trim();
        var input = editorContent;
        var chars = new antlr4.InputStream(input);
        var lexer = new DesignStylesLexer(chars);
        var Styles = new antlr4.CommonTokenStream(lexer);
        var parser = new DesignStylesParser(Styles);
        parser.buildParseTrees = true;
        var tree = parser.designstyles();
        listener = new DesignStylesListenerImpl("");
        antlr4.tree.ParseTreeWalker.DEFAULT.walk(listener, tree);
    }
}
