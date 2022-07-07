'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

const index = require('./index-1115561c.js');
const store = require('./store-49c65768.js');
const common = require('./common-070daac4.js');

const formTextCss = "/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{display:-ms-flexbox;display:flex;-ms-flex-direction:column;flex-direction:column;width:100%}:host .outer-wrapper{position:relative;width:100%}:host .inner-wrapper{display:-ms-flexbox;display:flex;-ms-flex-direction:column;flex-direction:column;position:relative}:host .inner-wrapper gxg-icon{position:absolute}:host label{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;display:-ms-flexbox;display:flex;margin-bottom:var(--spacing-lay-xs)}:host label .required{-webkit-padding-start:2px;padding-inline-start:2px}:host input{position:relative;width:100%;-webkit-transition-property:padding-left;transition-property:padding-left;-webkit-transition-duration:var(--ui-animaton-speed);transition-duration:var(--ui-animaton-speed);-webkit-transition-timing-function:ease;transition-timing-function:ease}:host([label-position=start]) .outer-wrapper{display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center}:host([label-position=start]) .inner-wrapper{width:100%}:host([label-position=start]) .label{margin-bottom:0;-webkit-margin-end:var(--spacing-comp-01);margin-inline-end:var(--spacing-comp-01)}:host input[type=text],:host input[type=password]{border-width:var(--border-width-sm);border-color:var(--gray-02);border-style:var(--border-style-regular);border-radius:var(--border-radius-sm);padding-left:var(--spacing-comp-01);padding-right:var(--spacing-comp-01);color:var(--color-on-background);background-color:var(--color-background);height:20px;-webkit-box-sizing:border-box;box-sizing:border-box;-webkit-transition-property:padding-left;transition-property:padding-left;-webkit-transition-duration:var(--ui-animaton-speed);transition-duration:var(--ui-animaton-speed);-webkit-transition-timing-function:ease;transition-timing-function:ease}:host input[type=text]:focus,:host input[type=password]:focus{outline-style:solid;outline-color:var(--color-primary-active);outline-width:var(--border-width-sm);border-color:var(--color-primary-active);border-radius:0}:host input[disabled]{background-color:var(--gray-01);color:var(--color-on-disabled);cursor:not-allowed}:host input[disabled]::-webkit-input-placeholder{color:var(--gray-04)}:host .input--error[type=text],:host .input--error[type=password]{border-color:var(--color-error-dark);background-color:var(--color-error-light);margin-bottom:0;color:var(--color-always-black)}:host .input--warning[type=text],:host .input--warning[type=password]{border-color:var(--color-warning-dark);background-color:var(--color-warning-light);margin-bottom:0;color:var(--color-always-black)}:host([minimal]) input[type=text]:not(:focus),:host([minimal]) input[type=password]:not(:focus){border-color:transparent;background-color:transparent}:host([minimal]) input.cursor-inside{border-color:var(--gray-02) !important;background-color:var(--color-background)}:host([minimal]) input[type=text]:focus,:host([minimal]) input[type=password]:focus{border-color:transparent}:host([minimal]:not([icon-position=start])) input[type=text],:host([minimal]:not([icon-position=start])) input[type=password]{-webkit-padding-start:0;padding-inline-start:0}:host([minimal]:not([icon-position=start])) input.cursor-inside{-webkit-padding-start:var(--spacing-comp-01);padding-inline-start:var(--spacing-comp-01)}:host([minimal]:not([icon-position=start])) input[type=text]:focus,:host([minimal]:not([icon-position=start])) input[type=password]:focus{-webkit-padding-start:var(--spacing-comp-01);padding-inline-start:var(--spacing-comp-01)}:host([minimal][over-dark-background]) input[type=text]:not(:focus),:host([minimal][over-dark-background]) input[type=password]:not(:focus){color:var(--color-on-primary)}:host([icon-position=end]) .clear-button gxg-icon:not(.clear-button){right:16px}:host([icon-position=start]) input{-webkit-padding-start:20px;padding-inline-start:20px}:host([icon-position=end]) input{-webkit-padding-end:20px;padding-inline-end:20px}:host([icon-position=end]) .inner-wrapper{-ms-flex-direction:row-reverse;flex-direction:row-reverse}:host gxg-icon.clear-button{right:5px !important;left:auto;opacity:0.5;cursor:pointer}:host gxg-icon.clear-button:hover{opacity:1}:host([clear-button][icon-position=end]) gxg-icon:not(.clear-button){right:20px}:host([clear-button][icon-position=end]) input{-webkit-padding-end:36px;padding-inline-end:36px}:host([text-style=regular]) input{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}:host([text-style=quote]) input{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}:host([text-style=title-01]) input{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}:host([text-style=title-02]) input{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}:host([text-style=title-03]) input{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}:host([text-style=title-04]) input{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}:host([text-style=title-05]) input{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}:host(.rtl[icon-position=start]) gxg-icon{right:0;left:auto}:host(.rtl[icon-position=end]) gxg-icon{right:auto;left:0}:host(.large) label{font-size:var(--font-size-lg)}:host(.large) input{height:var(--spacing-comp-05);font-size:var(--font-size-lg)}:host(.large) input[type=text],:host(.large) input[type=password]{padding-left:7px;padding-right:7px}:host(.large[icon-position=start]) input{-webkit-padding-start:var(--spacing-comp-05);padding-inline-start:var(--spacing-comp-05)}:host(.large[icon-position=start]) input .inner-wrapper gxg-icon{top:2px;left:2px}:host(.large[icon-position=end]) input{-webkit-padding-end:var(--spacing-comp-05);padding-inline-end:var(--spacing-comp-05)}:host(.large[icon-position=end]) input .inner-wrapper gxg-icon{right:2px;left:auto}:host(.large[icon-position=start]) .inner-wrapper gxg-icon{top:2px;left:2px}:host(.large[icon-position=end]) .inner-wrapper gxg-icon{top:2px;right:2px;left:auto}:host(.large) gxg-icon.clear-button{right:5px !important;left:auto !important}:host(.large[icon-position=end][clear-button]) .inner-wrapper gxg-icon{right:21px}:host(.large[icon-position=start][clear-button]) input{-webkit-padding-end:26px;padding-inline-end:26px}:host(.large[icon-position=end][clear-button]) input{-webkit-padding-end:42px;padding-inline-end:42px}";

const GxgFormText = class {
    constructor(hostRef) {
        index.registerInstance(this, hostRef);
        this.input = index.createEvent(this, "input", 7);
        this.change = index.createEvent(this, "change", 7);
        this.clearButtonClicked = index.createEvent(this, "clearButtonClicked", 7);
        this.isRequiredError = false;
        /*********************************
        PROPERTIES & STATE
        *********************************/
        /**
         * The presence of this attribute displays a clear (cross) button-icon on the right side
         */
        this.clearButton = false;
        /**
         * The presence of this attribute makes the input disabled
         */
        this.disabled = false;
        /**
         * The presence of this attribute gives the component error styles
         */
        this.error = false;
        /**
         * The input icon (optional)
         */
        this.icon = null;
        /**
         * The input icon side
         */
        this.iconPosition = null;
        /**
         * The presence of this attribute hides the border, and sets the background to transparent when the element has no focus
         */
        this.minimal = false;
        /**
         * The presence of this attribute sets the text color to white. Usefull when "minimal" attribute is applied and the background behind the input is dark
         */
        this.overDarkBackground = false;
        /**
         * The presence of this attribute makes this input required
         */
        this.required = false;
        /**
         * The presence of this attribute gives the component warning styles
         */
        this.warning = false;
        /**
         * The input max. width
         */
        this.maxWidth = "100%";
        /**
         * The text style
         */
        this.textStyle = "regular";
        /**
         * The presence of this attribute sets the input type as password
         */
        this.password = false;
        this.cursorInside = false;
        this.inputSize = "auto";
        this.mouseCoordinates = {
            x: null,
            y: null,
        };
        /**
         * Reading direction
         */
        this.rtl = false;
    }
    watchHandler(newValue, oldValue) {
        if (newValue !== oldValue) {
            if (this.minimal) {
                this.updateGhostSpan();
            }
        }
    }
    /*********************************
    METHODS
    *********************************/
    iconPositionFunc() {
        if (this.iconPosition !== null && this.icon !== null) {
            return this.iconPosition;
        }
    }
    inputIcon() {
        if (this.iconPosition !== null && this.icon !== null) {
            if (this.warning) {
                return (index.h("gxg-icon", { type: this.icon, size: "small", color: "warning" }));
            }
            if (this.error) {
                return (index.h("gxg-icon", { type: this.icon, size: "small", color: "error" }));
            }
            return index.h("gxg-icon", { type: this.icon, size: "small", color: "auto" });
        }
    }
    handleInput(e) {
        e.stopPropagation();
        const target = e.target;
        this.value = target.value;
        this.input.emit(target.value);
        common.formHandleChange(this, e.target);
    }
    handleChange(e) {
        e.stopPropagation();
        const target = e.target;
        this.value = target.value;
        this.change.emit(target.value);
        common.formHandleChange(this, e.target);
    }
    clearButtonFunc() {
        const value = (this.el.shadowRoot.querySelector("input").value = "");
        this.change.emit(value);
        this.clearButtonClicked.emit("clear button was clicked");
    }
    updateGhostSpan() {
        if (this.minimal) {
            //Update ghost span content, and then get and apply the width from the ghost span
            const ghostSpan = this.el.shadowRoot.querySelector(".ghost-span");
            ghostSpan.innerHTML = this.value;
            //Then get the ghost span width
            const ghostSpanWidth = ghostSpan.offsetWidth + 5;
            const input = this.el.shadowRoot.querySelector(".input");
            //Finally set that width to the input!
            input.style.width = ghostSpanWidth + "px";
        }
    }
    mouseEnterHandler() {
        setTimeout(function () {
            const inputText = this.el.shadowRoot.querySelector(".input");
            //Contextual menu coordinates
            const inputTextArea = inputText.getBoundingClientRect();
            if (this.mouseCoordinates.x > inputTextArea.left &&
                this.mouseCoordinates.x < inputTextArea.right &&
                this.mouseCoordinates.y > inputTextArea.top &&
                this.mouseCoordinates.y < inputTextArea.bottom) {
                //Mouse pointer is inside the input text
                this.cursorInside = true;
            }
        }.bind(this), 500);
    }
    mouseOutHandler() {
        this.cursorInside = false;
    }
    mouseMoveHandler(e) {
        this.mouseCoordinates["x"] = e.clientX;
        this.mouseCoordinates["y"] = e.clientY;
    }
    componentDidLoad() {
        if (this.minimal) {
            document.addEventListener("mousemove", this.mouseMoveHandler.bind(this));
            /**************
            GHOST SPAN
            Ghost span for the "minimal" type input, in order to make the input width fit the content
            **************/
            const intersectionObserver = new IntersectionObserver(function (entries) {
                // If intersectionRatio is 0, the target is out of view
                // and we do not need to do anything.
                if (entries[0].intersectionRatio <= 0)
                    return;
                const input = this.el.shadowRoot.querySelector(".input");
                const inputComputedStyles = window.getComputedStyle(input);
                const ghostSpan = this.el.shadowRoot.querySelector(".ghost-span");
                //Set ghostSpan outside of the visible area
                ghostSpan.style.position = "fixed";
                ghostSpan.style.left = "-1000px";
                ghostSpan.style.top = "-1000px";
                ghostSpan.style.zIndex = "-1000";
                ghostSpan.style.opacity = "0";
                //Set input styles that affect the width to the ghost span
                ghostSpan.style.fontSize = inputComputedStyles.fontSize;
                ghostSpan.style.fontFamily = inputComputedStyles.fontFamily;
                ghostSpan.style.textTransform = inputComputedStyles.textTransform;
                ghostSpan.style.display = "inline-block";
                ghostSpan.style.paddingLeft = inputComputedStyles.paddingRight;
                ghostSpan.style.paddingRight = inputComputedStyles.paddingRight;
                ghostSpan.style.letterSpacing = inputComputedStyles.letterSpacing;
                ghostSpan.style.fontWeight = inputComputedStyles.fontWeight;
                //Then get the ghost span width
                const ghostSpanWidth = ghostSpan.offsetWidth + 5;
                input.style.width = "0px";
                //get inner-wrapper width
                const innerWrapperWidth = this.el.shadowRoot.querySelector(".inner-wrapper")
                    .offsetWidth - 5;
                //Finally set that width to the input!
                input.style.width = ghostSpanWidth + "px";
                input.style.maxWidth = innerWrapperWidth + "px";
                //Listen to resizeObserver to set the new max-width value on the on the input
                const resizeObserver = new ResizeObserver(() => {
                    input.style.width = "0px";
                    const innerWrapperWidth = this.el.shadowRoot.querySelector(".inner-wrapper").offsetWidth - 5;
                    input.style.maxWidth = innerWrapperWidth + "px";
                    input.style.width = ghostSpanWidth + "px";
                });
                const innerWrapper = this.el.shadowRoot.querySelector(".inner-wrapper");
                resizeObserver.observe(innerWrapper);
            }.bind(this));
            // start observing
            intersectionObserver.observe(this.el.shadowRoot.querySelector(".input"));
        } // if (this.minimal)
        //Reading Direction
        const dirHtml = document
            .getElementsByTagName("html")[0]
            .getAttribute("dir");
        const dirBody = document
            .getElementsByTagName("body")[0]
            .getAttribute("dir");
        if (dirHtml === "rtl" || dirBody === "rtl") {
            this.rtl = true;
        }
        //Offset error or warning message if label position is "start"
        if (this.labelPosition === "start") {
            //Get label width
            const label = this.el.shadowRoot.querySelector(".label");
            const labelWidth = label.offsetWidth;
            //Get messages wrapper
            const messagesWrapper = this.el.shadowRoot.querySelector(".messages-wrapper");
            messagesWrapper.style.paddingLeft = labelWidth + 5 + "px";
        }
    }
    componentDidUnload() {
        if (this.minimal) {
            document.removeEventListener("mousemove", this.mouseMoveHandler);
        }
    }
    /*********************************
    LISTEN
    *********************************/
    handleFocus(focusEvent) {
        if (focusEvent.target !== this.el) {
            return;
        }
        this.textInput.focus();
    }
    type() {
        if (this.password) {
            return "password";
        }
        else {
            return "text";
        }
    }
    render() {
        return (index.h(index.Host, { role: "textbox", "aria-label": this.label, "icon-position": this.iconPositionFunc(), style: {
                maxWidth: this.maxWidth,
            }, class: {
                rtl: this.rtl,
                large: store.state.large,
            }, tabindex: "0" }, this.minimal ? index.h("span", { class: "ghost-span" }, this.value) : null, index.h("div", { class: "outer-wrapper" }, this.label !== undefined ? (index.h("label", { class: {
                label: true,
            } }, this.label, common.requiredLabel(this))) : (""), index.h("div", { class: {
                "inner-wrapper": true,
                "clear-button": this.clearButton === true,
            } }, index.h("input", { part: "input", type: this.type(), value: this.value, class: {
                input: true,
                "input--error": this.error === true,
                "input--warning": this.warning === true,
                "cursor-inside": this.cursorInside,
            }, placeholder: this.placeholder, disabled: this.disabled, onInput: this.handleInput.bind(this), onChange: this.handleChange.bind(this), required: this.required, onMouseEnter: this.mouseEnterHandler.bind(this), onMouseOut: this.mouseOutHandler.bind(this), ref: (el) => (this.textInput = el) }), this.inputIcon(), this.clearButton ? (index.h("gxg-icon", { class: "clear-button", type: "gemini-tools/close", size: "small", color: "onbackground", onClick: this.clearButtonFunc.bind(this) })) : null)), common.formMessage(this.isRequiredError ? (index.h("gxg-form-message", { type: "error", key: "required-error" }, this.requiredMessage)) : null)));
    }
    get el() { return index.getElement(this); }
    static get watchers() { return {
        "value": ["watchHandler"]
    }; }
};
GxgFormText.style = formTextCss;

exports.gxg_form_text = GxgFormText;
