import { r as registerInstance, c as createEvent, h, H as Host, g as getElement } from './index-09b1517f.js';
import { s as state } from './store-f5fbe254.js';
import { f as formMessage, r as requiredLabel } from './common-d08cabb3.js';

const selectCss = "/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{--size:3;display:block}:host .outer-wrapper{position:relative;width:100%}:host label{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;display:-ms-flexbox;display:flex;margin-bottom:var(--spacing-lay-xs)}:host label .required{padding-left:2px}:host textarea{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}:host{}:host .custom-select{position:relative;font-family:var(--font-family-primary)}:host .custom-select select{display:none;}:host .select-selected{background-color:var(--color-on-primary);border-width:var(--border-width-sm);border-color:var(--gray-02);border-style:var(--border-style-regular);height:10px}:host .select-selected:focus{outline-style:solid;outline-color:var(--color-primary-active);outline-width:var(--border-width-md);outline-offset:-2px;border-color:transparent}:host .select-selected:after,:host .select-selected:before{position:absolute;content:\"\";right:6px;width:0;height:0;border:4px solid transparent;border-color:var(--gray-06) transparent transparent transparent}:host .select-selected:after{top:12px}:host .select-selected:before{top:1px;-webkit-transform:rotate(180deg);transform:rotate(180deg)}:host .select-selected:hover:after,:host .select-selected:hover:before{border-color:var(--gray-04) transparent transparent transparent}:host .select-items div,:host .select-selected{color:var(--color-on-background);padding:var(--spacing-comp-01);cursor:pointer;font-size:var(--font-size-sm);line-height:0.95em}:host .select-items div{border-color:transparent transparent rgba(0, 0, 0, 0.1) transparent;border:1px solid transparent}:host .select-items{color:var(--color-on-background);position:absolute;background-color:var(--color-on-primary);top:100%;left:0;right:0;z-index:99;border-width:var(--border-width-sm);border-color:var(--gray-02);border-style:var(--border-style-regular);border-top:0;overflow-y:scroll;border-bottom-right-radius:var(--border-radius-md);border-bottom-left-radius:var(--border-radius-md);max-height:calc( var(--size) * 20px )}:host .select-items div{height:10px}:host .select-hide{display:none}:host .select-items div:hover{background-color:var(--color-secondary-enabled);color:var(--color-on-secondary)}:host .select-items div.same-as-selected{background-color:var(--color-secondary-hover);color:var(--color-on-secondary)}:host([disabled]) .select-selected{pointer-events:none;background-color:var(--gray-01);color:var(--color-on-disabled)}:host([disabled]) .custom-select{cursor:not-allowed}:host(.rtl) .select-selected:after,:host(.rtl) .select-selected:before{left:6px;right:auto}:host([minimal]) .select-selected:not(:focus){border-color:transparent;background-color:transparent}:host{}:host ::-webkit-scrollbar{width:6px}:host ::-webkit-scrollbar-track{background-color:var(--gray-02);border-radius:10px}:host ::-webkit-scrollbar-thumb{background:var(--gray-05);border-radius:10px}:host ::-webkit-scrollbar-thumb:hover{background:var(--gray-04);cursor:pointer}:host .select--error .select-selected{border-color:var(--color-error-dark);background-color:var(--color-error-light);margin-bottom:0;color:var(--color-always-black)}:host .select--warning .select-selected{border-color:var(--color-warning-dark);background-color:var(--color-warning-light);margin-bottom:0;color:var(--color-always-black)}:host([label-position=start]) .outer-wrapper{display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center}:host([label-position=start]) .custom-select{width:100%}:host([label-position=start]) .label{margin-bottom:0;-webkit-margin-end:var(--spacing-comp-02);margin-inline-end:var(--spacing-comp-02);-ms-flex-negative:0;flex-shrink:0}:host(.large) label{font-size:var(--font-size-lg)}:host(.large) .select-selected{height:var(--spacing-comp-05);-webkit-box-sizing:border-box;box-sizing:border-box;display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;font-size:var(--font-size-lg)}:host(.large) .select-selected:before{top:3px}:host(.large) .select-selected:after{top:14px}:host(.large) .select-items{max-height:calc(var(--size) * var(--spacing-comp-05))}:host(.large) .select-items div{height:14px;display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;font-size:var(--font-size-lg)}";

const GxgFormSelect = class {
    constructor(hostRef) {
        registerInstance(this, hostRef);
        this.change = createEvent(this, "change", 7);
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
            }, onKeyDown: this.handlerOnKeyDown.bind(this) }, h("div", { class: "outer-wrapper" }, this.label !== undefined ? (h("label", { class: {
                label: true,
            } }, this.label, requiredLabel(this))) : (""), h("div", { class: {
                "custom-select": true,
                select: true,
                "select--error": this.error === true,
                "select--warning": this.warning === true,
            }, ref: (el) => (this.select = el) }, h("select", { id: "original" }, h("slot", null)))), formMessage()));
    }
    get el() { return getElement(this); }
    static get watchers() { return {
        "value": ["valueHandler"]
    }; }
};
GxgFormSelect.style = selectCss;

export { GxgFormSelect as gxg_select };
