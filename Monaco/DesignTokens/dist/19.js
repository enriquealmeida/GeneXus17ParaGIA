(window["webpackJsonp"] = window["webpackJsonp"] || []).push([[19],{

/***/ "../node_modules/@genexus/design-tokens-editor/dist/esm-es5/gxg-tab.entry.js":
/*!***********************************************************************************!*\
  !*** ../node_modules/@genexus/design-tokens-editor/dist/esm-es5/gxg-tab.entry.js ***!
  \***********************************************************************************/
/*! exports provided: gxg_tab */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"gxg_tab\", function() { return Tab; });\n/* harmony import */ var _core_f1fc1d91_js__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./core-f1fc1d91.js */ \"../node_modules/@genexus/design-tokens-editor/dist/esm-es5/core-f1fc1d91.js\");\n\nvar Tab = /** @class */ (function () {\n    function Tab(hostRef) {\n        Object(_core_f1fc1d91_js__WEBPACK_IMPORTED_MODULE_0__[\"r\"])(this, hostRef);\n        this.isSelected = false;\n    }\n    Tab.prototype.render = function () {\n        return this.isSelected ? (Object(_core_f1fc1d91_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(_core_f1fc1d91_js__WEBPACK_IMPORTED_MODULE_0__[\"H\"], { class: \"selected\" }, Object(_core_f1fc1d91_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(\"section\", { class: {\n                tab: true,\n                \"tab--selected\": this.isSelected === true\n            } }, Object(_core_f1fc1d91_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(\"slot\", null)))) : (Object(_core_f1fc1d91_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(_core_f1fc1d91_js__WEBPACK_IMPORTED_MODULE_0__[\"H\"], { class: \"not-selected\" }));\n    };\n    Object.defineProperty(Tab, \"style\", {\n        get: function () { return \":host{height:100%;display:block}:host .tab{display:-ms-flexbox;display:flex;-ms-flex-flow:column;flex-flow:column;height:100%;font-weight:var(--font-weight-regular);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:left;line-height:1.455em}.tab,:host .tab{font-family:var(--font-family-primary)}.tab{padding:var(--spacing-lay-02)}:host(.not-selected){display:none}\"; },\n        enumerable: true,\n        configurable: true\n    });\n    return Tab;\n}());\n\n\n\n//# sourceURL=webpack:///../node_modules/@genexus/design-tokens-editor/dist/esm-es5/gxg-tab.entry.js?");

/***/ })

}]);