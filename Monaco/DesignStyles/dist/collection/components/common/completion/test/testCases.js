import { expect } from 'chai';
import { DSOCompletionItemProvider } from "../completion-provider";
export class TestCases {
    constructor() {
        this.completionProvider = new DSOCompletionItemProvider(null, null, null);
    }
    addSpacesAndNewLines(withSpacesAndNewLines, text) {
        let spaces = `  `;
        let newLine = `
        `;
        if (withSpacesAndNewLines)
            return text + spaces + spaces + newLine + newLine + spaces;
        else
            return text;
    }
    addSpaces(withSpaces, text) {
        let spaces = `  `;
        if (withSpaces)
            return text + spaces + spaces + spaces;
        else
            return text;
    }
    async inStartProvider(caseNumber, withSpacesAndNewLines) {
        let text = "";
        switch (caseNumber) {
            case 1:
                text = ``;
                break;
        }
        text = this.addSpacesAndNewLines(withSpacesAndNewLines, text);
        return expect(await this.completionProvider.caluclateSuggestionsTest(text)).to.equal("regexInStart");
    }
    ;
    async inEmptyProvider(caseNumber, withSpacesAndNewLines) {
        let text = "";
        switch (caseNumber) {
            case 1:
                text = `styles DSO
                {`;
                break;
            case 2:
                text = `styles DSO
                {
                .Gradient1
                {
                    background: linear-gradient(90deg, $colors.background, $colors.background, $colors.Linear_1_Style_Gradient_1 0%, $colors.Linear_1_Style_Gradient_2 50%, $colors.Linear_1_Style_Gradient_3 100%);
                }`;
                break;
            case 3:
                text = `styles DSORefactor {
                    // @import ColorScheme.Tokens;
                    // @import AppBlue.DSO.Tokens;
                    // @import AppRed.DSO.Tokens;
                
                    // @import "http://www.gxportal.com/test.css";
                    // @import gx-file(AppBlue.STYLE);
                    // @import gx-file(AppRed.STYLE);
                
                    // .etiqueta {
                    //     color: red;
                    //     background-color: blue;
                    //     gx-highlighted-background-color: skyblue;
                    //     margin-left: 5px;
                    // }
                
                    // .atributo {
                    //     color: blue;
                    //     background-color: red;
                    //     border-color: $colors.Primary;
                    //     gx-highlighted-background-color: orange;
                
                    //     gx-label-class: etiqueta;
                    //     gx-label-vertical-align: bottom;
                    //     gx-label-horizontal-align: justify;
                
                    //     width: 75%;
                    //     height: 200px;
                    //     margin: 5px;
                    // }
                
                    // .custom.selector {
                    //     color: black;
                    // }
                    
                    // @media $mediaQueries.Tv {
                    //     .atributo {
                    //         gx-highlighted-color: $colors.Primary;
                    //     }
                    //     .etiqueta {
                    //         gx-highlighted-color: $colors.Primary;
                    //     }
                    // }
                    
                    // @media (max-width: 900px) {
                    //     .atributo {
                    //         font-weight: 700;
                    //         gx-highlighted-color: lightgray;
                    //     }
                    //     .etiqueta {
                    //         font-weight: 700;
                    //         gx-highlighted-color: lightgray;
                    //     }
                    // }
                
                    // @media (max-width: 480px) {
                    //     .atributo {
                    //         font-weight: 700;
                    //         gx-highlighted-color: gray;
                    //     }
                    //     .etiqueta {
                    //         font-weight: 700;
                    //         gx-highlighted-color: gray;
                    //     }
                    // }
                    .test{
                        gx-accent-color: $colors.Primary;
                    }`;
                break;
        }
        text = this.addSpacesAndNewLines(withSpacesAndNewLines, text);
        return expect(await this.completionProvider.caluclateSuggestionsTest(text)).to.equal("regexInEmpty");
    }
    async inClassProvider(caseNumber, withSpacesAndNewLines) {
        let text = "";
        switch (caseNumber) {
            case 1:
                text = `styles DSO
                {
                     .Gradient1
                     {`;
                break;
            case 2:
                text = `styles DSO
                {
                    .ViewGrid  TR .WWActionColumn  A:focus
                    {`;
                break;
            case 3:
                text = `styles DSO
                {
                    .input-group  .form-control.FilterComboAttribute + span.input-group-btn > a.btn 
                    {`;
                break;
            case 4:
                text = `.input-group  .form-control:focus + span.input-group-btn > a.btn 
                {
                    border-width: 1px;
                    outline:  0 none;
                    box-shadow:  1px 1px 1px rgba(0, 0, 0, 0.075) inset, 0 0 8px rgba(102, 175, 233, 0.6);
                }
                .input-group  .form-control + span.input-group-btn > a.btn 
                {`;
                break;
        }
        text = this.addSpacesAndNewLines(withSpacesAndNewLines, text);
        return expect(await this.completionProvider.caluclateSuggestionsTest(text)).to.equal("regexInClass");
    }
    async inPropValueProvider(caseNumber, withSpaces) {
        let text = "";
        switch (caseNumber) {
            case 1:
                text = `styles DSO
                {
                    .className
                    {
                        poperty: $custom.test;
                        a:`;
                break;
            case 2:
                text = `styles DSO
                {
                    .className
                    {
                        padding: 10px `;
                break;
            case 3:
                text = `styles DSO
                {
                    .className
                    {
                        padding: $ `;
                break;
            case 4:
                text = `styles DSO
                {
                    .className
                    {
                        padding: $col`;
                break;
        }
        text = this.addSpaces(withSpaces, text);
        return expect(await this.completionProvider.caluclateSuggestionsTest(text)).to.equal("regexInPropValue");
    }
    async inPropValueTokenGroupProvider(caseNumber, withSpaces) {
        let text = "";
        switch (caseNumber) {
            case 1:
                text = `styles DSO
                {
                    .className
                    {
                        padding: $colors.`;
                break;
            case 2:
                text = `styles DSO
                {
                    .className
                    {
                        padding: $colors.bac`;
                break;
            case 3:
                text = `styles DSO
                {
                    .className
                    {
                        padding: $spacing.small $spacing.`;
                break;
            case 4:
                text = `styles DSO
                {
                    .className
                    {
                        padding: $spacing.small $spacing.bi`;
                break;
        }
        text = this.addSpaces(withSpaces, text);
        return expect(await this.completionProvider.caluclateSuggestionsTest(text)).to.equal("regexInPropValueTokenGroup");
    }
    async inPropValueFunction(caseNumber, withSpaces) {
        let text = "";
        switch (caseNumber) {
            case 1:
                text = `styles DSO
                {
                    .className
                    {
                        transform: linear-gradient(`;
                break;
            case 2:
                text = `styles DSO
                {
                    .className
                    {
                        transform: linear-gradient(90deg, `;
                break;
            case 3:
                text = `styles DSO
                {
                    .className
                    {
                        transform: gxAnchorPointX("left") translateX(10dip) rotate(`;
                break;
        }
        text = this.addSpaces(withSpaces, text);
        return expect(await this.completionProvider.caluclateSuggestionsTest(text)).to.equal("regexInPropValueFunction");
    }
    async inImport(caseNumber, withSpaces) {
        let text = "";
        switch (caseNumber) {
            case 1:
                text = `styles DSO
                {
                    @import `;
                break;
            case 2:
                text = `styles DSO
                {
                    @import "test.css" `;
                break;
            case 3:
                text = `styles DSO
                {
                    @import DesignSystem1  `;
                break;
        }
        text = this.addSpaces(withSpaces, text);
        return expect(await this.completionProvider.caluclateSuggestionsTest(text)).to.equal("regexInImport");
    }
    async inMediaQueryValue(caseNumber, withSpacesAndNewLines) {
        let text = "";
        switch (caseNumber) {
            case 1:
                text = `styles DSO
                {
                    @media $mediaQueries.`;
                break;
        }
        text = this.addSpacesAndNewLines(withSpacesAndNewLines, text);
        return expect(await this.completionProvider.caluclateSuggestionsTest(text)).to.equal("regexInMediaQueryValue");
    }
    async inMedia(caseNumber, withSpaces) {
        let text = "";
        switch (caseNumber) {
            case 1:
                text = `styles DSO
                {
                    @media`;
                break;
        }
        text = this.addSpaces(withSpaces, text);
        return expect(await this.completionProvider.caluclateSuggestionsTest(text)).to.equal("regexInMedia");
    }
    async inInclude(caseNumber, withSpaces) {
        let text = "";
        switch (caseNumber) {
            case 1:
                text = `styles DSO
                {
                    .class1
                    {
                        background: red;
                    }
                    .className
                    {
                        @include `;
                break;
            case 2:
                text = `styles DSO
                {
                    .class1
                    {
                        background: red;
                    }
                    .className
                    {
                        @include c`;
                break;
            case 3:
                text = `styles DSO
                {
                    .class1
                    {
                        background: red;
                    }
                    .className
                    {
                        @include class1 `;
                break;
        }
        text = this.addSpaces(withSpaces, text);
        return expect(await this.completionProvider.caluclateSuggestionsTest(text)).to.equal("regexInInclude");
    }
    async inGxFile(caseNumber, withSpacesAndNewLines) {
        let text = "";
        switch (caseNumber) {
            case 1:
                text = `styles DSO
                {
                    .className
                    {
                        background-image: gx-file(`;
                break;
            case 2:
                text = `styles DSO
                {
                    @import gx-file(`;
                break;
        }
        text = this.addSpacesAndNewLines(withSpacesAndNewLines, text);
        return expect(await this.completionProvider.caluclateSuggestionsTest(text)).to.equal("regexInGxFile");
    }
    async inGxImage(caseNumber, withSpacesAndNewLines) {
        let text = "";
        switch (caseNumber) {
            case 1:
                text = `styles DSO
                {
                    .className
                    {
                        background-image: gx-image(`;
                break;
        }
        text = this.addSpacesAndNewLines(withSpacesAndNewLines, text);
        return expect(await this.completionProvider.caluclateSuggestionsTest(text)).to.equal("regexInGxImage");
    }
}
