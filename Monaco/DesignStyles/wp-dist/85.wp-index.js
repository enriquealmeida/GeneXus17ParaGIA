(window["webpackJsonp"] = window["webpackJsonp"] || []).push([[85],{

/***/ "../../node_modules/@stencil/router/dist/esm/legacy/stencil-async-content.entry.js":
/*!**********************************************************************************************************************!*\
  !*** /__w/gx-web-editors/gx-web-editors/node_modules/@stencil/router/dist/esm/legacy/stencil-async-content.entry.js ***!
  \**********************************************************************************************************************/
/*! exports provided: stencil_async_content */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"stencil_async_content\", function() { return AsyncContent; });\n/* harmony import */ var _stencilrouter_1307249c_js__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./stencilrouter-1307249c.js */ \"../../node_modules/@stencil/router/dist/esm/legacy/stencilrouter-1307249c.js\");\n\nvar AsyncContent = /** @class */ (function () {\n    function AsyncContent(hostRef) {\n        Object(_stencilrouter_1307249c_js__WEBPACK_IMPORTED_MODULE_0__[\"r\"])(this, hostRef);\n        this.content = '';\n    }\n    AsyncContent.prototype.componentWillLoad = function () {\n        if (this.documentLocation != null) {\n            return this.fetchNewContent(this.documentLocation);\n        }\n    };\n    AsyncContent.prototype.fetchNewContent = function (newDocumentLocation) {\n        var _this = this;\n        return fetch(newDocumentLocation)\n            .then(function (response) { return response.text(); })\n            .then(function (data) {\n            _this.content = data;\n        });\n    };\n    AsyncContent.prototype.render = function () {\n        return (Object(_stencilrouter_1307249c_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(\"div\", { innerHTML: this.content }));\n    };\n    Object.defineProperty(AsyncContent, \"watchers\", {\n        get: function () {\n            return {\n                \"documentLocation\": [\"fetchNewContent\"]\n            };\n        },\n        enumerable: true,\n        configurable: true\n    });\n    return AsyncContent;\n}());\n\n\n\n//# sourceURL=webpack:////__w/gx-web-editors/gx-web-editors/node_modules/@stencil/router/dist/esm/legacy/stencil-async-content.entry.js?");

/***/ })

}]);