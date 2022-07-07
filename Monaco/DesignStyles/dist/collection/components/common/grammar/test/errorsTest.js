let assert = require('assert');
let parserFacade = require('../generated/DesignTokensParserFacade.js');

function checkError(actualError, expectedError) {
    it('should have startLine ' + expectedError.startLine, function () {
        assert.equal(actualError.startLine, expectedError.startLine);
    });
    it('should have endLine ' + expectedError.endLine, function () {
        assert.equal(actualError.endLine, expectedError.endLine);
    });
    it('should have startCol ' + expectedError.startCol, function () {
        assert.equal(actualError.startCol, expectedError.startCol);
    });
    it('should have endCol ' + expectedError.endCol, function () {
        assert.equal(actualError.endCol, expectedError.endCol);
    });
    it('should have message ' + expectedError.message, function () {
        assert.equal(actualError.message, expectedError.message);
    });
}
function checkErrors(actualErrors, expectedErrors) {
    it('should have ' + expectedErrors.length  + ' error(s)', function (){
        assert.equal(actualErrors.length, expectedErrors.length);
    });
    var i;
    for (i = 0; i < expectedErrors.length; i++) {
        checkError(actualErrors[i], expectedErrors[i]);
    }
}
function parseAndCheckErrors(input, expectedErrors) {
    //console.log("parseAndCheck: ", input);
    let errors = parserFacade.validate(input);

    console.log(errors)
   // console.log("Errors: ", errors);
    checkErrors(errors, expectedErrors);
}
describe('Validation of simple errors on single lines', function () {
    describe('should have recognize missing operand', function () {
        parseAndCheckErrors("o = i + \n", [
            new parserFacade.Error(1, 1, 8, 9, "mismatched input '\\n' expecting {NUMBER_LIT, ID, '(', '-'}")
        ]);
    });
    describe('should have recognize extra operator', function () {
        parseAndCheckErrors("o = i +* 2 \n", [
            new parserFacade.Error(1, 1, 7, 8, "extraneous input '*' expecting {NUMBER_LIT, ID, '(', '-'}")
        ]);
    });
});
// describe('Validation of simple errors in small scripts', function () {
//     describe('should have recognize missing operand', function () {
//         let input = "input i\no = i + \noutput o\n";
//         parseAndCheckErrors(input, [
//             new parserFacade.Error(2, 2, 8, 9, "mismatched input '\\n' expecting {NUMBER_LIT, ID, '(', '-'}")
//         ]);
//     });
//     describe('should have recognize extra operator', function () {
//         let input = "input i\no = i +* 2 \noutput o\n";
//         parseAndCheckErrors(input, [
//             new parserFacade.Error(2, 2, 7, 8, "extraneous input '*' expecting {NUMBER_LIT, ID, '(', '-'}")
//         ]);
//     });
// });
// describe('Validation of examples being edited', function () {
//     describe('deleting number from division', function () {
//         let input = "input a\n" +
//             "b = a * 2\n" +
//             "c = (a - b) / \n" +
//             "output c\n";
//         parseAndCheckErrors(input, [
//             new parserFacade.Error(3, 3, 14, 15, "mismatched input '\\n' expecting {NUMBER_LIT, ID, '(', '-'}")
//         ]);
//     });
//     describe('deleting number from multiplication', function () {
//         let input = "input a\n" +
//             "b = a * \n" +
//             "c = (a - b) / 3\n" +
//             "output c\n";
//         parseAndCheckErrors(input, [
//             new parserFacade.Error(2, 2, 8, 9, "mismatched input '\\n' expecting {NUMBER_LIT, ID, '(', '-'}")
//         ]);
//     });
//     describe('adding plus to expression', function () {
//         let input = "input a\n" +
//             "b = a * 2 +\n" +
//             "c = (a - b) / 3\n" +
//             "output c\n";
//         parseAndCheckErrors(input, [
//             new parserFacade.Error(2, 2, 11, 12, "mismatched input '\\n' expecting {NUMBER_LIT, ID, '(', '-'}")
//         ]);
//     });
// });