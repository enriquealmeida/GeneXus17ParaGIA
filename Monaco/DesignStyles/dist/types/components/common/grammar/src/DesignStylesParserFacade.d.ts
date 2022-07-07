import { Token } from 'antlr4/Token.js';
export declare function createLexer(input: String): any;
export declare function getTokens(input: String): Token[];
export declare function parseTreeStr(input: any): any;
export declare class Error {
    startLine: number;
    endLine: number;
    startCol: number;
    endCol: number;
    message: string;
    constructor(startLine: number, endLine: number, startCol: number, endCol: number, message: string);
}
export declare function validate(input: any): Error[];
