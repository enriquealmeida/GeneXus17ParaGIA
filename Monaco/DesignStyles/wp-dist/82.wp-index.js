(window["webpackJsonp"] = window["webpackJsonp"] || []).push([[82],{

/***/ "../../node_modules/@stencil/router/dist/esm/legacy/context-consumer.entry.js":
/*!*****************************************************************************************************************!*\
  !*** /__w/gx-web-editors/gx-web-editors/node_modules/@stencil/router/dist/esm/legacy/context-consumer.entry.js ***!
  \*****************************************************************************************************************/
/*! exports provided: context_consumer */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"context_consumer\", function() { return ContextConsumer; });\n/* harmony import */ var _stencilrouter_1307249c_js__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./stencilrouter-1307249c.js */ \"../../node_modules/@stencil/router/dist/esm/legacy/stencilrouter-1307249c.js\");\n\nvar ContextConsumer = /** @class */ (function () {\n    function ContextConsumer(hostRef) {\n        Object(_stencilrouter_1307249c_js__WEBPACK_IMPORTED_MODULE_0__[\"r\"])(this, hostRef);\n        this.context = {};\n        this.renderer = function () { return null; };\n    }\n    ContextConsumer.prototype.connectedCallback = function () {\n        if (this.subscribe != null) {\n            this.unsubscribe = this.subscribe(this.el, 'context');\n        }\n    };\n    ContextConsumer.prototype.disconnectedCallback = function () {\n        if (this.unsubscribe != null) {\n            this.unsubscribe();\n        }\n    };\n    ContextConsumer.prototype.render = function () {\n        return this.renderer(Object.assign({}, this.context));\n    };\n    Object.defineProperty(ContextConsumer.prototype, \"el\", {\n        get: function () { return Object(_stencilrouter_1307249c_js__WEBPACK_IMPORTED_MODULE_0__[\"g\"])(this); },\n        enumerable: true,\n        configurable: true\n    });\n    return ContextConsumer;\n}());\n\n\n\n//# sourceURL=webpack:////__w/gx-web-editors/gx-web-editors/node_modules/@stencil/router/dist/esm/legacy/context-consumer.entry.js?");

/***/ })

}]);