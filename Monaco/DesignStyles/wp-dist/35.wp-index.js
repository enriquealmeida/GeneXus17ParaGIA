(window["webpackJsonp"] = window["webpackJsonp"] || []).push([[35],{

/***/ "../../node_modules/@genexus/gemini/dist/esm-es5/gxg-breadcrumbs.entry.js":
/*!*************************************************************************************************************!*\
  !*** /__w/gx-web-editors/gx-web-editors/node_modules/@genexus/gemini/dist/esm-es5/gxg-breadcrumbs.entry.js ***!
  \*************************************************************************************************************/
/*! exports provided: gxg_breadcrumbs */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"gxg_breadcrumbs\", function() { return GxgBreadcrumbs; });\n/* harmony import */ var _index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./index-09b1517f.js */ \"../../node_modules/@genexus/gemini/dist/esm-es5/index-09b1517f.js\");\n\nvar breadcrumbsCss = \":host{display:-ms-flexbox;display:flex}\";\nvar GxgBreadcrumbs = /** @class */ (function () {\n    function GxgBreadcrumbs(hostRef) {\n        Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"r\"])(this, hostRef);\n    }\n    GxgBreadcrumbs.prototype.componentWillLoad = function () {\n        this.setIndexToBreadcrumb();\n    };\n    GxgBreadcrumbs.prototype.setIndexToBreadcrumb = function () {\n        var breadcrumbs = this.el.querySelectorAll(\"gxg-breadcrumb\");\n        breadcrumbs.forEach(function (breadcrumb, index) {\n            breadcrumb.setAttribute(\"data-index\", index.toString());\n        });\n    };\n    GxgBreadcrumbs.prototype.render = function () {\n        return (Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"H\"], null, Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(\"slot\", null)));\n    };\n    Object.defineProperty(GxgBreadcrumbs.prototype, \"el\", {\n        get: function () { return Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"g\"])(this); },\n        enumerable: false,\n        configurable: true\n    });\n    return GxgBreadcrumbs;\n}());\nGxgBreadcrumbs.style = breadcrumbsCss;\n\n\n\n//# sourceURL=webpack:////__w/gx-web-editors/gx-web-editors/node_modules/@genexus/gemini/dist/esm-es5/gxg-breadcrumbs.entry.js?");

/***/ })

}]);