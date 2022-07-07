(window["webpackJsonp"] = window["webpackJsonp"] || []).push([[162],{

/***/ "../text-editor/dist/esm-es5/context-consumer.entry.js":
/*!*************************************************************!*\
  !*** ../text-editor/dist/esm-es5/context-consumer.entry.js ***!
  \*************************************************************/
/*! exports provided: context_consumer */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"context_consumer\", function() { return ContextConsumer; });\n/* harmony import */ var _index_7f90bc21_js__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./index-7f90bc21.js */ \"../text-editor/dist/esm-es5/index-7f90bc21.js\");\n\nvar ContextConsumer = /** @class */ (function () {\n    function ContextConsumer(hostRef) {\n        Object(_index_7f90bc21_js__WEBPACK_IMPORTED_MODULE_0__[\"r\"])(this, hostRef);\n        this.context = {};\n        this.renderer = function () { return null; };\n    }\n    ContextConsumer.prototype.connectedCallback = function () {\n        if (this.subscribe != null) {\n            this.unsubscribe = this.subscribe(this.el, 'context');\n        }\n    };\n    ContextConsumer.prototype.disconnectedCallback = function () {\n        if (this.unsubscribe != null) {\n            this.unsubscribe();\n        }\n    };\n    ContextConsumer.prototype.render = function () {\n        return this.renderer(Object.assign({}, this.context));\n    };\n    Object.defineProperty(ContextConsumer.prototype, \"el\", {\n        get: function () { return Object(_index_7f90bc21_js__WEBPACK_IMPORTED_MODULE_0__[\"g\"])(this); },\n        enumerable: false,\n        configurable: true\n    });\n    return ContextConsumer;\n}());\n\n\n\n//# sourceURL=webpack:///../text-editor/dist/esm-es5/context-consumer.entry.js?");

/***/ })

}]);