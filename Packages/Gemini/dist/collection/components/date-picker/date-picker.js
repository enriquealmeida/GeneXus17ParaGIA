import { Component, Prop, h, Element, Host, State } from "@stencil/core";
import datepicker from "js-datepicker";
import state from "../store";
export class GxgDatePicker {
    constructor() {
        /**
         * The presence of this attribute makes the date-picker always visible
         */
        this.alwaysShow = false;
        /**
         * no weekends available
         */
        this.noWeekends = false;
        /**
         * The max. width
         */
        this.maxWidth = "100%";
        /**
         * Reading direction
         */
        this.rtl = false;
    }
    componentDidLoad() {
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
        //Datepicker Options
        let defaultDate = new Date();
        if (this.defaultDate !== undefined && this.defaultDate !== "") {
            //default/initial date
            const d = new Date(this.defaultDate);
            if (Object.prototype.toString.call(d) === "[object Date]") {
                // it is a date
                if (isNaN(d.getTime())) {
                    // d.valueOf() could also work
                    // date is not valid
                }
                else {
                    // date is valid
                    defaultDate = new Date(this.defaultDate);
                }
            }
            else {
                // not a date
            }
        }
        //default date
        const defaultDateYear = defaultDate.getFullYear();
        const defaultDateMonth = defaultDate.getMonth();
        const defaultDateDay = defaultDate.getDate();
        //min date
        const minDate = new Date(this.minDate);
        const minDateYear = minDate.getFullYear();
        const minDateMonth = minDate.getMonth();
        const minDateDay = minDate.getDate();
        //max date
        const maxDate = new Date(this.maxDate);
        const maxDateYear = maxDate.getFullYear();
        const maxDateMonth = maxDate.getMonth();
        const maxDateDay = maxDate.getDate();
        const pickerSelector = this.el.shadowRoot.querySelector("#date-picker");
        const picker = datepicker(pickerSelector, {
            // Event callbacks.
            // onSelect: instance => {
            // },
            // onShow: instance => {
            // },
            // onHide: instance => {
            // },
            // onMonthChange: instance => {
            // },
            // Customizations.
            formatter: (input, date) => {
                // This will display the date as `1/1/2019`.
                input.value = date.toDateString();
            },
            position: "bl",
            startDay: 1,
            customDays: ["S", "M", "T", "W", "T", "F", "S"],
            customMonths: [
                "January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December",
            ],
            customOverlayMonths: [
                "😀",
                "😂",
                "😎",
                "😍",
                "🤩",
                "😜",
                "😬",
                "😳",
                "🤪",
                "🤓 ",
                "😝",
                "😮",
            ],
            overlayButton: "Go!",
            overlayPlaceholder: "Enter a 4-digit year",
            // Settings.
            alwaysShow: this.alwaysShow,
            dateSelected: new Date(defaultDateYear, defaultDateMonth, defaultDateDay),
            maxDate: new Date(maxDateYear, maxDateMonth, maxDateDay),
            minDate: new Date(minDateYear, minDateMonth, minDateDay),
            startDate: new Date(),
            showAllDates: true,
            // Disabling things.
            noWeekends: this.noWeekends,
            disabler: (date) => date.getDay() === 2 && date.getMonth() === 9,
            disabledDates: [new Date(2050, 0, 1), new Date(2050, 0, 3)],
            disableMobile: true,
            disableYearOverlay: true,
            // ID - be sure to provide a 2nd picker with the same id to create a daterange pair.
            id: "date-picker",
        });
        //picker.setDate(new Date(2099, 0, 1), true);
        picker.calendarContainer.style.setProperty("font-size", "9px");
    }
    printLabel() {
        if (this.label) {
            return h("label", { class: "label" }, this.label);
        }
    }
    render() {
        return (h(Host, { class: {
                rtl: this.rtl,
                large: state.large,
            }, style: {
                maxWidth: this.maxWidth,
            } },
            this.printLabel(),
            h("input", { type: "text", id: "date-picker" })));
    }
    static get is() { return "gxg-date-picker"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["date-picker.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["date-picker.css"]
    }; }
    static get properties() { return {
        "alwaysShow": {
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
                "text": "The presence of this attribute makes the date-picker always visible"
            },
            "attribute": "always-show",
            "reflect": false,
            "defaultValue": "false"
        },
        "defaultDate": {
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
                "text": "initial date"
            },
            "attribute": "default-date",
            "reflect": false
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
                "text": "The datepicker label"
            },
            "attribute": "label",
            "reflect": false
        },
        "noWeekends": {
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
                "text": "no weekends available"
            },
            "attribute": "no-weekends",
            "reflect": false,
            "defaultValue": "false"
        },
        "minDate": {
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
                "text": "The min. date"
            },
            "attribute": "min-date",
            "reflect": false
        },
        "maxDate": {
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
                "text": "The max. date"
            },
            "attribute": "max-date",
            "reflect": false
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
                "text": "The max. width"
            },
            "attribute": "max-width",
            "reflect": false,
            "defaultValue": "\"100%\""
        }
    }; }
    static get states() { return {
        "rtl": {}
    }; }
    static get elementRef() { return "el"; }
}
