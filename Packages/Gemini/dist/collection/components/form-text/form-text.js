import { Component, Prop, Element, h, Host, Event, State, Watch, Listen, } from "@stencil/core";
import { requiredLabel, formMessage, formHandleChange, } from "../../common";
import state from "../store";
export class GxgFormText {
    constructor() {
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
                return (h("gxg-icon", { type: this.icon, size: "small", color: "warning" }));
            }
            if (this.error) {
                return (h("gxg-icon", { type: this.icon, size: "small", color: "error" }));
            }
            return h("gxg-icon", { type: this.icon, size: "small", color: "auto" });
        }
    }
    handleInput(e) {
        e.stopPropagation();
        const target = e.target;
        this.value = target.value;
        this.input.emit(target.value);
        formHandleChange(this, e.target);
    }
    handleChange(e) {
        e.stopPropagation();
        const target = e.target;
        this.value = target.value;
        this.change.emit(target.value);
        formHandleChange(this, e.target);
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
        return (h(Host, { role: "textbox", "aria-label": this.label, "icon-position": this.iconPositionFunc(), style: {
                maxWidth: this.maxWidth,
            }, class: {
                rtl: this.rtl,
                large: state.large,
            }, tabindex: "0" },
            this.minimal ? h("span", { class: "ghost-span" }, this.value) : null,
            h("div", { class: "outer-wrapper" },
                this.label !== undefined ? (h("label", { class: {
                        label: true,
                    } },
                    this.label,
                    requiredLabel(this))) : (""),
                h("div", { class: {
                        "inner-wrapper": true,
                        "clear-button": this.clearButton === true,
                    } },
                    h("input", { part: "input", type: this.type(), value: this.value, class: {
                            input: true,
                            "input--error": this.error === true,
                            "input--warning": this.warning === true,
                            "cursor-inside": this.cursorInside,
                        }, placeholder: this.placeholder, disabled: this.disabled, onInput: this.handleInput.bind(this), onChange: this.handleChange.bind(this), required: this.required, onMouseEnter: this.mouseEnterHandler.bind(this), onMouseOut: this.mouseOutHandler.bind(this), ref: (el) => (this.textInput = el) }),
                    this.inputIcon(),
                    this.clearButton ? (h("gxg-icon", { class: "clear-button", type: "gemini-tools/close", size: "small", color: "onbackground", onClick: this.clearButtonFunc.bind(this) })) : null)),
            formMessage(this.isRequiredError ? (h("gxg-form-message", { type: "error", key: "required-error" }, this.requiredMessage)) : null)));
    }
    static get is() { return "gxg-form-text"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["form-text.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["form-text.css"]
    }; }
    static get properties() { return {
        "clearButton": {
            "type": "boolean",
            "mutable": false,
            "complexType": {
                "original": "boolean",
                "resolved": "boolean",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The presence of this attribute displays a clear (cross) button-icon on the right side"
            },
            "attribute": "clear-button",
            "reflect": false,
            "defaultValue": "false"
        },
        "disabled": {
            "type": "boolean",
            "mutable": false,
            "complexType": {
                "original": "boolean",
                "resolved": "boolean",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The presence of this attribute makes the input disabled"
            },
            "attribute": "disabled",
            "reflect": false,
            "defaultValue": "false"
        },
        "error": {
            "type": "boolean",
            "mutable": true,
            "complexType": {
                "original": "boolean",
                "resolved": "boolean",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The presence of this attribute gives the component error styles"
            },
            "attribute": "error",
            "reflect": false,
            "defaultValue": "false"
        },
        "icon": {
            "type": "any",
            "mutable": false,
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The input icon (optional)"
            },
            "attribute": "icon",
            "reflect": false,
            "defaultValue": "null"
        },
        "iconPosition": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "IconPosition",
                "resolved": "\"end\" | \"start\"",
                "references": {
                    "IconPosition": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The input icon side"
            },
            "attribute": "icon-position",
            "reflect": true,
            "defaultValue": "null"
        },
        "label": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "string",
                "resolved": "string",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The input label"
            },
            "attribute": "label",
            "reflect": false
        },
        "labelPosition": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "LabelPosition",
                "resolved": "\"above\" | \"start\"",
                "references": {
                    "LabelPosition": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The input label"
            },
            "attribute": "label-position",
            "reflect": true
        },
        "minimal": {
            "type": "boolean",
            "mutable": false,
            "complexType": {
                "original": "boolean",
                "resolved": "boolean",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The presence of this attribute hides the border, and sets the background to transparent when the element has no focus"
            },
            "attribute": "minimal",
            "reflect": true,
            "defaultValue": "false"
        },
        "overDarkBackground": {
            "type": "boolean",
            "mutable": false,
            "complexType": {
                "original": "boolean",
                "resolved": "boolean",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The presence of this attribute sets the text color to white. Usefull when \"minimal\" attribute is applied and the background behind the input is dark"
            },
            "attribute": "over-dark-background",
            "reflect": true,
            "defaultValue": "false"
        },
        "placeholder": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "string",
                "resolved": "string",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The input placeholder"
            },
            "attribute": "placeholder",
            "reflect": false
        },
        "required": {
            "type": "boolean",
            "mutable": false,
            "complexType": {
                "original": "boolean",
                "resolved": "boolean",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The presence of this attribute makes this input required"
            },
            "attribute": "required",
            "reflect": true,
            "defaultValue": "false"
        },
        "requiredMessage": {
            "type": "string",
            "mutable": true,
            "complexType": {
                "original": "string",
                "resolved": "string",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The required message if this input is required and no value is provided (optional). If this is not provided, the default browser required message will show up"
            },
            "attribute": "required-message",
            "reflect": false
        },
        "value": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "string",
                "resolved": "string",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The input value"
            },
            "attribute": "value",
            "reflect": true
        },
        "warning": {
            "type": "boolean",
            "mutable": false,
            "complexType": {
                "original": "boolean",
                "resolved": "boolean",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The presence of this attribute gives the component warning styles"
            },
            "attribute": "warning",
            "reflect": false,
            "defaultValue": "false"
        },
        "maxWidth": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "string",
                "resolved": "string",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The input max. width"
            },
            "attribute": "max-width",
            "reflect": false,
            "defaultValue": "\"100%\""
        },
        "textStyle": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "Style",
                "resolved": "\"quote\" | \"regular\" | \"title-01\" | \"title-02\" | \"title-03\" | \"title-04\" | \"title-05\"",
                "references": {
                    "Style": {
                        "location": "local"
                    }
                }
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The text style"
            },
            "attribute": "text-style",
            "reflect": true,
            "defaultValue": "\"regular\""
        },
        "password": {
            "type": "boolean",
            "mutable": false,
            "complexType": {
                "original": "boolean",
                "resolved": "boolean",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "The presence of this attribute sets the input type as password"
            },
            "attribute": "password",
            "reflect": false,
            "defaultValue": "false"
        }
    }; }
    static get states() { return {
        "cursorInside": {},
        "inputSize": {},
        "mouseCoordinates": {},
        "rtl": {}
    }; }
    static get events() { return [{
            "method": "input",
            "name": "input",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": "Returns the input value"
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }, {
            "method": "change",
            "name": "change",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": "Returns the input value"
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }, {
            "method": "clearButtonClicked",
            "name": "clearButtonClicked",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": "The clear button was clicked"
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }]; }
    static get elementRef() { return "el"; }
    static get watchers() { return [{
            "propName": "value",
            "methodName": "watchHandler"
        }]; }
    static get listeners() { return [{
            "name": "focus",
            "method": "handleFocus",
            "target": undefined,
            "capture": false,
            "passive": false
        }]; }
}
