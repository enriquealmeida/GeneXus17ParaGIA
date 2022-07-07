'use strict';

const index = require('./index-1115561c.js');

function requiredLabel(component) {
    if (component.required) {
        return index.h("span", { class: "required" }, "*");
    }
}
function formMessage(children) {
    /* The following snipet of jsx is intended to be inserted at the end of every form-x component.
    The purpose of this jsx snippet is to show error or warning messages,
    inserted on the "message" slot with a gxg-form-message component
    */
    return (index.h("div", { class: "messages-wrapper", role: "alert", "aria-live": "assertive" },
        index.h("slot", { name: "message" }),
        children));
}
function formHandleChange(component, formElement) {
    const hasError = !formElement.validity.valid;
    component.error = hasError;
    component.isRequiredError = component.error;
    //If validity is false, show message
    if (hasError) {
        component.requiredMessage =
            component.requiredMessage || formElement.validationMessage;
    }
}

exports.formHandleChange = formHandleChange;
exports.formMessage = formMessage;
exports.requiredLabel = requiredLabel;
