System.register([ "./p-d8a20c31.system.js" ], (function(e) {
  "use strict";
  var r;
  return {
    setters: [ function(e) {
      r = e.h;
    } ],
    execute: function() {
      e({
        a: function(e, r) {
          var s = !r.validity.valid;
          //If validity is false, show message
          e.error = s, e.isRequiredError = e.error, s && (e.requiredMessage = e.requiredMessage || r.validationMessage);
        },
        f: function(e) {
          /* The following snipet of jsx is intended to be inserted at the end of every form-x component.
                The purpose of this jsx snippet is to show error or warning messages,
                inserted on the "message" slot with a gxg-form-message component
                */
          return r("div", {
            class: "messages-wrapper",
            role: "alert",
            "aria-live": "assertive"
          }, r("slot", {
            name: "message"
          }), e);
        },
        r: function(e) {
          if (e.required) return r("span", {
            class: "required"
          }, "*");
        }
      });
    }
  };
}));