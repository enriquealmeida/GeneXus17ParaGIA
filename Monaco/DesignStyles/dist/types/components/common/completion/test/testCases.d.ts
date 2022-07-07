/// <reference types="chai" />
export declare class TestCases {
    private completionProvider;
    private addSpacesAndNewLines;
    private addSpaces;
    inStartProvider(caseNumber: any, withSpacesAndNewLines: any): Promise<Chai.Assertion>;
    inEmptyProvider(caseNumber: any, withSpacesAndNewLines: any): Promise<Chai.Assertion>;
    inClassProvider(caseNumber: any, withSpacesAndNewLines: any): Promise<Chai.Assertion>;
    inPropValueProvider(caseNumber: any, withSpaces: any): Promise<Chai.Assertion>;
    inPropValueTokenGroupProvider(caseNumber: any, withSpaces: any): Promise<Chai.Assertion>;
    inPropValueFunction(caseNumber: any, withSpaces: any): Promise<Chai.Assertion>;
    inImport(caseNumber: any, withSpaces: any): Promise<Chai.Assertion>;
    inMediaQueryValue(caseNumber: any, withSpacesAndNewLines: any): Promise<Chai.Assertion>;
    inMedia(caseNumber: any, withSpaces: any): Promise<Chai.Assertion>;
    inInclude(caseNumber: any, withSpaces: any): Promise<Chai.Assertion>;
    inGxFile(caseNumber: any, withSpacesAndNewLines: any): Promise<Chai.Assertion>;
    inGxImage(caseNumber: any, withSpacesAndNewLines: any): Promise<Chai.Assertion>;
}
