import { Component, Element, Event, Prop, h, Host } from "@stencil/core";
export class GxgToggle {
    constructor() {
        /*********************************
        PROPERTIES & STATE
        *********************************/
        /**
         * The state of the toggle. Whether is disabled or not.
         */
        this.disabled = false;
        /**
         * The label
         */
        this.label = "Label";
        /**
         * If the toggle is active or not
         */
        this.on = false;
    }
    /*********************************
    METHODS
    *********************************/
    componentWillLoad() {
        // Create a new 'change' event
    }
    onKeyUp(e) {
        if (e.which == 13) {
            //"enter" key was pressed
            this.switchToggle();
        }
    }
    switchToggle() {
        if (this.disabled !== true) {
            if (this.on === true) {
                this.on = false;
            }
            else {
                this.on = true;
            }
        }
        this.toggleSwitched.emit(this.on);
    }
    state() {
        if (this.on) {
            return "true";
        }
        else {
            return "false";
        }
    }
    render() {
        return (h(Host, { role: "switch", "aria-checked": this.state(), class: {
                toggle: true
            }, onClick: this.switchToggle.bind(this), onKeyup: this.onKeyUp.bind(this), tabindex: "0" },
            h("div", { class: "main-container" },
                h("div", { class: "toggle__container" },
                    h("span", { class: "toggle__container__knob" })),
                h("span", { class: "toggle__label" }, this.label))));
    }
    static get is() { return "gxg-toggle"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["toggle.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["toggle.css"]
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
                "text": "The state of the toggle. Whether is disabled or not."
            },
            "attribute": "disabled",
            "reflect": true,
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
                "text": "The label"
            },
            "attribute": "label",
            "reflect": false,
            "defaultValue": "\"Label\""
        },
        "on": {
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
                "text": "If the toggle is active or not"
            },
            "attribute": "on",
            "reflect": true,
            "defaultValue": "false"
        }
    }; }
    static get events() { return [{
            "method": "toggleSwitched",
            "name": "toggleSwitched",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": "This event is triggered when the toggle is switched. 'event.detail' will display true when the toggle is on, or false when the toggle is off."
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }]; }
    static get elementRef() { return "el"; }
}
