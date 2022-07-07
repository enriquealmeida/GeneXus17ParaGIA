import { h } from './index-09b1517f.js';
function requiredLabel(component) {
    if (component.required) {
        return h("span", { class: "required" }, "*");
    }
}
function formMessage(children) {
    /* The following snipet of jsx is intended to be inserted at the end of every form-x component.
    The purpose of this jsx snippet is to show error or warning messages,
    inserted on the "message" slot with a gxg-form-message component
    */
    return (h("div", { class: "messages-wrapper", role: "alert", "aria-live": "assertive" }, h("slot", { name: "message" }), children));
}
function formHandleChange(component, formElement) {
    var hasError = !formElement.validity.valid;
    component.error = hasError;
    component.isRequiredError = component.error;
    //If validity is false, show message
    if (hasError) {
        component.requiredMessage =
            component.requiredMessage || formElement.validationMessage;
    }
}
export { formHandleChange as a, formMessage as f, requiredLabel as r };
