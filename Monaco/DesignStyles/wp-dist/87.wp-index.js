(window["webpackJsonp"] = window["webpackJsonp"] || []).push([[87],{

/***/ "../../node_modules/@stencil/router/dist/esm/legacy/stencil-route-title.entry.js":
/*!********************************************************************************************************************!*\
  !*** /__w/gx-web-editors/gx-web-editors/node_modules/@stencil/router/dist/esm/legacy/stencil-route-title.entry.js ***!
  \********************************************************************************************************************/
/*! exports provided: stencil_route_title */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"stencil_route_title\", function() { return RouteTitle; });\n/* harmony import */ var _stencilrouter_1307249c_js__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./stencilrouter-1307249c.js */ \"../../node_modules/@stencil/router/dist/esm/legacy/stencilrouter-1307249c.js\");\n/* harmony import */ var _chunk_cfc6485e_js__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./chunk-cfc6485e.js */ \"../../node_modules/@stencil/router/dist/esm/legacy/chunk-cfc6485e.js\");\n\n\n/**\n  * Updates the document title when found.\n  *\n  * @name RouteTitle\n  * @description\n */\nvar RouteTitle = /** @class */ (function () {\n    function RouteTitle(hostRef) {\n        Object(_stencilrouter_1307249c_js__WEBPACK_IMPORTED_MODULE_0__[\"r\"])(this, hostRef);\n        this.titleSuffix = '';\n        this.pageTitle = '';\n    }\n    RouteTitle.prototype.updateDocumentTitle = function () {\n        var el = this.el;\n        if (el.ownerDocument) {\n            el.ownerDocument.title = \"\" + this.pageTitle + (this.titleSuffix || '');\n        }\n    };\n    RouteTitle.prototype.componentWillLoad = function () {\n        this.updateDocumentTitle();\n    };\n    Object.defineProperty(RouteTitle.prototype, \"el\", {\n        get: function () { return Object(_stencilrouter_1307249c_js__WEBPACK_IMPORTED_MODULE_0__[\"g\"])(this); },\n        enumerable: true,\n        configurable: true\n    });\n    Object.defineProperty(RouteTitle, \"watchers\", {\n        get: function () {\n            return {\n                \"pageTitle\": [\"updateDocumentTitle\"]\n            };\n        },\n        enumerable: true,\n        configurable: true\n    });\n    return RouteTitle;\n}());\n_chunk_cfc6485e_js__WEBPACK_IMPORTED_MODULE_1__[\"A\"].injectProps(RouteTitle, [\n    'titleSuffix',\n]);\n\n\n\n//# sourceURL=webpack:////__w/gx-web-editors/gx-web-editors/node_modules/@stencil/router/dist/esm/legacy/stencil-route-title.entry.js?");

/***/ })

}]);