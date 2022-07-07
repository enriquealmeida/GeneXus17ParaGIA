import { h as e } from "./p-eb9dc661.js";

function requiredLabel(r) {
  if (r.required) return e("span", {
    class: "required"
  }, "*");
}

function formMessage(r) {
  /* The following snipet of jsx is intended to be inserted at the end of every form-x component.
    The purpose of this jsx snippet is to show error or warning messages,
    inserted on the "message" slot with a gxg-form-message component
    */
  return e("div", {
    class: "messages-wrapper",
    role: "alert",
    "aria-live": "assertive"
  }, e("slot", {
    name: "message"
  }), r);
}

function formHandleChange(e, r) {
  const a = !r.validity.valid;
  e.error = a, e.isRequiredError = e.error, 
  //If validity is false, show message
  a && (e.requiredMessage = e.requiredMessage || r.validationMessage);
}

export { formHandleChange as a, formMessage as f, requiredLabel as r }