import { Component, Prop, Element, Event, h, Host, Watch, State, Listen, } from "@stencil/core";
import { requiredLabel, formMessage } from "../../common.js";
import state from "../store";
export class GxgFormSelect {
    constructor() {
        /********************************
        PROPERTIES & STATE
        *********************************/
        /**
         * The presence of this attribute disables the component
         */
        this.disabled = false;
        /**
         * The presence of this attribute stylizes the component with error attributes
         */
        this.error = false;
        /**
         * The input label
         */
        this.labelPosition = "above";
        /**
         * The presence of this attribute hides the border, and sets the background to transparent when the element has no focus
         */
        this.minimal = true;
        /**
         * The presence of this attribute makes this input required
         */
        this.required = false;
        /**
         * The presence of this attribute stylizes the component with warning attributes
         */
        this.warning = false;
        /**
         * The select max. width
         */
        this.maxWidth = "100%";
        /**
         * Reading direction
         */
        this.rtl = false;
        this.rerender = false;
    }
    todoCompletedHandler(event) {
        this.value = event.detail;
    }
    /*********************************
    METHODS
    *********************************/
    clickTest(event, listOfOptions, updateValue) {
        let y, i, k;
        const cElement = event.srcElement;
        const parent = cElement.parentNode.parentNode;
        const s = parent.getElementsByTagName("select")[0];
        const h = cElement.parentNode.previousSibling;
        for (i = 0; i < listOfOptions.length; i++) {
            if (listOfOptions[i].innerHTML == cElement.innerHTML) {
                const selectedValue = cElement.getAttribute("value");
                updateValue(selectedValue);
                s.selectedIndex = i;
                h.innerHTML = cElement.innerHTML;
                y = cElement.parentNode.getElementsByClassName("same-as-selected");
                for (k = 0; k < y.length; k++) {
                    y[k].removeAttribute("class");
                }
                cElement.setAttribute("class", "same-as-selected");
                cElement.setAttribute("aria-selected", "true");
                break;
            }
        }
        h.click();
    }
    componentDidLoad() {
        const slots = this.el.shadowRoot.querySelectorAll("slot");
        slots[0].addEventListener("slotchange", function () {
            this.el.shadowRoot.querySelector(".select-selected").remove();
            this.el.shadowRoot.querySelector(".select-items").remove();
            this.selectCore();
        }.bind(this));
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
        this.selectCore();
    }
    selectCore() {
        const updateValue = (selectedOption) => {
            this.value = selectedOption;
        };
        let i, j, a, b, c;
        const x = this.el.shadowRoot.querySelectorAll(".custom-select");
        //set the selected option as "active"
        this.el.shadowRoot.querySelector(".select-items");
        for (i = 0; i < x.length; i++) {
            a = document.createElement("DIV");
            a.setAttribute("class", "select-selected");
            a.setAttribute("tabindex", "0");
            a.setAttribute("role", "listbox");
            a.addEventListener("keydown", (event) => {
                //event.preventDefault();
                const select = this.el.shadowRoot.querySelector(".select-items");
                let selected = select.querySelector(".same-as-selected");
                if (event.keyCode === 13) {
                    //enter key was pressed
                    select.classList.toggle("select-hide");
                }
                else if (event.keyCode === 38) {
                    //key up pressed
                    if (selected !== null) {
                        if (selected.previousElementSibling !== null) {
                            selected.classList.remove("same-as-selected");
                            selected.previousElementSibling.classList.add("same-as-selected");
                        }
                    }
                    else {
                        //do nothing
                    }
                }
                else if (event.keyCode === 40) {
                    //key down pressed
                    if (selected !== null) {
                        if (selected.nextElementSibling !== null) {
                            selected.classList.remove("same-as-selected");
                            selected.nextElementSibling.classList.add("same-as-selected");
                        }
                    }
                    else {
                        selected = select.querySelector("div[role='option']:first-child");
                        selected.classList.add("same-as-selected");
                    }
                }
                else if (event.keyCode === 27) {
                    //escape key was pressed
                    select.classList.add("select-hide");
                }
                if (selected !== null) {
                    const newSelectedOption = this.el.shadowRoot.querySelector(".select-items .same-as-selected");
                    this.el.shadowRoot.querySelector(".select-selected").textContent =
                        newSelectedOption.textContent;
                }
            });
            const optionsNodeList = this.el.querySelectorAll("gxg-option");
            let selectedOption = optionsNodeList[0];
            if (this.disabled) {
                a.setAttribute("disabled", "disabled");
            }
            for (let i = 0; i < optionsNodeList.length; i++) {
                if (optionsNodeList[i].selected === true) {
                    selectedOption = optionsNodeList[i];
                }
            }
            a.innerHTML = selectedOption.innerHTML;
            this.value = selectedOption.value;
            const listOfOptions = this.el.querySelectorAll("gxg-option");
            x[i].appendChild(a);
            b = document.createElement("DIV");
            b.setAttribute("class", "select-items select-hide");
            for (j = 0; j < listOfOptions.length; j++) {
                c = document.createElement("DIV");
                c.setAttribute("role", "option");
                c.innerHTML = listOfOptions[j].innerHTML;
                const optionValue = document.createAttribute("value");
                optionValue.value = listOfOptions[j].value;
                c.setAttributeNode(optionValue);
                c.addEventListener("click", (event) => this.clickTest(event, listOfOptions, updateValue));
                b.appendChild(c);
            }
            x[i].appendChild(b);
            a.addEventListener("click", function (e) {
                e.stopPropagation();
                this.nextSibling.classList.toggle("select-hide");
                this.classList.toggle("select-arrow-active");
            });
            this.el.shadowRoot
                .querySelector(".select-items div[role='option'][value='" + this.value + "']")
                .classList.add("same-as-selected");
        }
        function closeAllSelect(elmnt) {
            let i;
            const arrNo = [];
            const x = this.el.shadowRoot.querySelectorAll(".select-items");
            const y = this.el.shadowRoot.querySelectorAll(".select-selected");
            for (i = 0; i < y.length; i++) {
                if (elmnt == y[i]) {
                    arrNo.push(i);
                }
                else {
                    y[i].classList.remove("select-arrow-active");
                }
            }
            for (i = 0; i < x.length; i++) {
                if (arrNo.indexOf(i)) {
                    x[i].classList.add("select-hide");
                }
            }
        }
        document.addEventListener("click", closeAllSelect.bind(this));
    }
    valueHandler(newValue, oldValue) {
        if (oldValue !== undefined) {
            this.change.emit(newValue);
        }
        if (oldValue !== undefined) {
            //update visible value innerHTML
            const selectedGxgOption = this.el.querySelector("gxg-option[value='" + newValue + "']");
            this.el.shadowRoot.querySelector(".select-selected").innerHTML =
                selectedGxgOption.innerHTML;
        }
    }
    handlerOnKeyDown(event) {
        if (event.keyCode == 9) {
            //tab key was pressed
            if (event.shiftKey) {
                //shift key was also pressed
                this.el.previousSibling.previousSibling.focus();
            }
        }
    }
    render() {
        return (h(Host, { style: {
                maxWidth: this.maxWidth,
                "--size": this.size,
            }, class: {
                rtl: this.rtl,
                large: state.large,
            }, onKeyDown: this.handlerOnKeyDown.bind(this) },
            h("div", { class: "outer-wrapper" },
                this.label !== undefined ? (h("label", { class: {
                        label: true,
                    } },
                    this.label,
                    requiredLabel(this))) : (""),
                h("div", { class: {
                        "custom-select": true,
                        select: true,
                        "select--error": this.error === true,
                        "select--warning": this.warning === true,
                    }, ref: (el) => (this.select = el) },
                    h("select", { id: "original" },
                        h("slot", null)))),
            formMessage()));
    }
    static get is() { return "gxg-select"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["select.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["select.css"]
    }; }
    static get properties() { return {
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
                "text": "The presence of this attribute disables the component"
            },
            "attribute": "disabled",
            "reflect": true,
            "defaultValue": "false"
        },
        "error": {
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
                "text": "The presence of this attribute stylizes the component with error attributes"
            },
            "attribute": "error",
            "reflect": false,
            "defaultValue": "false"
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
                "text": "The select label"
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
            "reflect": true,
            "defaultValue": "\"above\""
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
            "reflect": false,
            "defaultValue": "true"
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
        "size": {
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
                "text": "The maximum number of visible options"
            },
            "attribute": "size",
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
                "text": "This holds the value of the selected option"
            },
            "attribute": "value",
            "reflect": false
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
                "text": "The presence of this attribute stylizes the component with warning attributes"
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
                "text": "The select max. width"
            },
            "attribute": "max-width",
            "reflect": false,
            "defaultValue": "\"100%\""
        }
    }; }
    static get states() { return {
        "rtl": {},
        "rerender": {}
    }; }
    static get events() { return [{
            "method": "change",
            "name": "change",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": "Returns the value of the selected option"
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
            "methodName": "valueHandler"
        }]; }
    static get listeners() { return [{
            "name": "optionIsSelected",
            "method": "todoCompletedHandler",
            "target": undefined,
            "capture": false,
            "passive": false
        }]; }
}
