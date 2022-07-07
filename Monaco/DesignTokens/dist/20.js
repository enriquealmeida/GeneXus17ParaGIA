(window["webpackJsonp"] = window["webpackJsonp"] || []).push([[20],{

/***/ "../node_modules/@genexus/design-tokens-editor/dist/esm-es5/gxg-tabs.entry.js":
/*!************************************************************************************!*\
  !*** ../node_modules/@genexus/design-tokens-editor/dist/esm-es5/gxg-tabs.entry.js ***!
  \************************************************************************************/
/*! exports provided: gxg_tabs */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"gxg_tabs\", function() { return Tabs; });\n/* harmony import */ var _core_f1fc1d91_js__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./core-f1fc1d91.js */ \"../node_modules/@genexus/design-tokens-editor/dist/esm-es5/core-f1fc1d91.js\");\n\nvar Tabs = /** @class */ (function () {\n    function Tabs(hostRef) {\n        Object(_core_f1fc1d91_js__WEBPACK_IMPORTED_MODULE_0__[\"r\"])(this, hostRef);\n        this.activeTab = \"\";\n    }\n    Tabs.prototype.tabActivatedHandler = function (event) {\n        this.updateActiveChildren(event.target.tab, \"gxg-tab-button\");\n        this.updateActiveChildren(event.target.tab, \"gxg-tab\");\n    };\n    Tabs.prototype.updateActiveChildren = function (activeTab, tagName) {\n        var children = Array.from(this.element.querySelectorAll(tagName));\n        for (var _i = 0, children_1 = children; _i < children_1.length; _i++) {\n            var child = children_1[_i];\n            child.isSelected = activeTab === child.tab;\n        }\n    };\n    Tabs.prototype.render = function () {\n        return (Object(_core_f1fc1d91_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(\"nav\", { class: \"tabs-container\" }, Object(_core_f1fc1d91_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(\"slot\", null)));\n    };\n    Object.defineProperty(Tabs.prototype, \"element\", {\n        get: function () { return Object(_core_f1fc1d91_js__WEBPACK_IMPORTED_MODULE_0__[\"d\"])(this); },\n        enumerable: true,\n        configurable: true\n    });\n    Object.defineProperty(Tabs, \"style\", {\n        get: function () { return \":host{display:block;height:100%;background:var(--color-background);border-radius:var(--border-width-md);-webkit-box-shadow:var(--box-shadow-01);box-shadow:var(--box-shadow-01)}\"; },\n        enumerable: true,\n        configurable: true\n    });\n    return Tabs;\n}());\n\n\n\n//# sourceURL=webpack:///../node_modules/@genexus/design-tokens-editor/dist/esm-es5/gxg-tabs.entry.js?");

/***/ })

}]);