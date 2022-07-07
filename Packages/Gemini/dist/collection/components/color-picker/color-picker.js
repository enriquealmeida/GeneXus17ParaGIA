import { Component, h, Element, Event, Host, Prop, State, Watch } from "@stencil/core";
import Pickr from "@simonwep/pickr";
export class GxgColorPicker {
    constructor() {
        /**
        The label of the color picker (optional)
        */
        this.label = "";
        /**
        The color value, such as "red", #CCDDEE, or rgba(220,140,40,.5)
        */
        this.value = "white";
        this.colorRepresentation = "HEXA";
        this.colorInputValue = "";
        this.colorChangedFromInput = false;
    }
    //Lyfe cycles
    componentDidLoad() {
        //Detect color representation
        if (this.value.includes("rgb")) {
            this.colorRepresentation = "RGBA";
        }
        else if (this.value.includes("#")) {
            this.colorRepresentation = "HEXA";
        }
        const colorPickerEl = this.element.shadowRoot.querySelector(".color-picker");
        const colorPickerMainCtEl = this.element.shadowRoot.querySelector(".color-picker-main-container");
        this.pickr = new Pickr({
            el: colorPickerEl,
            theme: "nano",
            container: colorPickerMainCtEl,
            inline: true,
            showAlways: true,
            default: this.value,
            // useAsButton: true,
            components: {
                // Main components
                preview: true,
                opacity: true,
                hue: true,
                // Input / output Options
                interaction: {
                    // hex: true,
                    // rgb: true,
                    input: false
                    // save: true
                }
            }
        });
        this.pickr.on("change", color => {
            this.colorObject = color;
            if (this.colorRepresentation === "HEXA") {
                this.value = this.colorObject.toHEXA().toString();
            }
            else if (this.colorRepresentation === "RGBA") {
                this.value = this.colorObject.toRGBA().toString(0);
            }
            this.change.emit(this.value);
        });
        this.pickr.on("show", () => {
            this.colorObject = this.pickr.getColor();
        });
        const options = {
            root: document.querySelector("body"),
            rootMargin: "0px",
            threshold: 1.0
        };
        const observer = new IntersectionObserver(() => {
            this.pickr.setColor(this.value); //We have to set the color by force, because we need to get the color at this time, and pickr seems to defer it.
        }, options);
        observer.observe(this.element);
    }
    componentDidUnload() {
        this.pickr.destroy();
    }
    watchHandler(newValue) {
        this.pickr.setColor(newValue);
    }
    //Button Methods
    handleHexaButtonClick() {
        this.colorChangedFromInput = false;
        this.colorRepresentation = "HEXA";
        this.value = this.colorObject.toHEXA().toString();
        this.change.emit(this.value);
    }
    handleRgbaButtonClick() {
        this.colorChangedFromInput = false;
        this.colorRepresentation = "RGBA";
        this.value = this.colorObject.toRGBA().toString(0);
        this.change.emit(this.value);
    }
    handleTitleValueChange(ev) {
        const element = ev.target;
        this.label = element.value;
    }
    handleColorValueChange(ev) {
        this.colorChangedFromInput = true;
        const element = ev.target;
        this.colorInputValue = element.value;
        this.pickr.setColor(element.value);
    }
    colorValue() {
        if (!this.colorChangedFromInput) {
            //We only want to update the color value on the input if the pick was changed directly by handling the color picker window, not by changing the input color value
            if (this.colorObject === undefined) {
                return "";
            }
            if (this.colorRepresentation === "HEXA") {
                return this.colorObject.toHEXA().toString();
            }
            else if (this.colorRepresentation === "RGBA") {
                return this.colorObject.toRGBA().toString(0);
            }
        }
        this.colorChangedFromInput = true;
        return this.colorInputValue;
    }
    setActiveButton() {
        if (this.value.includes("rgb")) {
            return "rgba";
        }
        else {
            return "hexa";
        }
    }
    render() {
        return (h(Host, null,
            h("h1", { class: "label" }, this.label),
            h("div", { class: {
                    "color-picker-main-container": true
                }, id: "color-picker-main-container" },
                h("div", { class: "color-picker" }),
                h("div", { class: "cp-gxg-buttons before-color-value", slot: "editable" },
                    h("gxg-button-group", { "default-selected-btn-id": this.setActiveButton() },
                        h("button", { id: "rgba", onClick: this.handleRgbaButtonClick.bind(this) }, "RGBA"),
                        h("button", { id: "hexa", onClick: this.handleHexaButtonClick.bind(this) }, "HEXA"))),
                h("input", { type: "text", name: "cp-color-value", id: "cp-color-value", value: this.colorValue(), class: "color-picker-main-container-textbox", onInput: this.handleColorValueChange.bind(this) }))));
    }
    static get is() { return "gxg-color-picker"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["color-picker.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["color-picker.css"]
    }; }
    static get properties() { return {
        "label": {
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
                "text": "The label of the color picker (optional)"
            },
            "attribute": "label",
            "reflect": false,
            "defaultValue": "\"\""
        },
        "value": {
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
                "text": "The color value, such as \"red\", #CCDDEE, or rgba(220,140,40,.5)"
            },
            "attribute": "value",
            "reflect": true,
            "defaultValue": "\"white\""
        }
    }; }
    static get states() { return {
        "colorRepresentation": {},
        "colorInputValue": {},
        "colorObject": {}
    }; }
    static get events() { return [{
            "method": "save",
            "name": "save",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": ""
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }, {
            "method": "nameInputEvent",
            "name": "nameInputEvent",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": ""
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
                "text": ""
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }]; }
    static get elementRef() { return "element"; }
    static get watchers() { return [{
            "propName": "value",
            "methodName": "watchHandler"
        }]; }
}
