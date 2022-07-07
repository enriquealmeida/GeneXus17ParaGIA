(window["webpackJsonp"] = window["webpackJsonp"] || []).push([[90],{

/***/ "../../node_modules/@stencil/router/dist/esm/legacy/stencil-router-redirect.entry.js":
/*!************************************************************************************************************************!*\
  !*** /__w/gx-web-editors/gx-web-editors/node_modules/@stencil/router/dist/esm/legacy/stencil-router-redirect.entry.js ***!
  \************************************************************************************************************************/
/*! exports provided: stencil_router_redirect */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"stencil_router_redirect\", function() { return Redirect; });\n/* harmony import */ var _stencilrouter_1307249c_js__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./stencilrouter-1307249c.js */ \"../../node_modules/@stencil/router/dist/esm/legacy/stencilrouter-1307249c.js\");\n/* harmony import */ var _chunk_cfc6485e_js__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./chunk-cfc6485e.js */ \"../../node_modules/@stencil/router/dist/esm/legacy/chunk-cfc6485e.js\");\n\n\n// Get the URL for this route link without the root from the router\nvar getUrl = function (url, root) {\n    // Don't allow double slashes\n    if (url.charAt(0) == '/' && root.charAt(root.length - 1) == '/') {\n        return root.slice(0, root.length - 1) + url;\n    }\n    return root + url;\n};\nvar Redirect = /** @class */ (function () {\n    function Redirect(hostRef) {\n        Object(_stencilrouter_1307249c_js__WEBPACK_IMPORTED_MODULE_0__[\"r\"])(this, hostRef);\n    }\n    Redirect.prototype.componentWillLoad = function () {\n        if (this.history && this.root && this.url) {\n            return this.history.replace(getUrl(this.url, this.root));\n        }\n    };\n    Object.defineProperty(Redirect.prototype, \"el\", {\n        get: function () { return Object(_stencilrouter_1307249c_js__WEBPACK_IMPORTED_MODULE_0__[\"g\"])(this); },\n        enumerable: true,\n        configurable: true\n    });\n    return Redirect;\n}());\n_chunk_cfc6485e_js__WEBPACK_IMPORTED_MODULE_1__[\"A\"].injectProps(Redirect, [\n    'history',\n    'root'\n]);\n\n\n\n//# sourceURL=webpack:////__w/gx-web-editors/gx-web-editors/node_modules/@stencil/router/dist/esm/legacy/stencil-router-redirect.entry.js?");

/***/ })

}]);