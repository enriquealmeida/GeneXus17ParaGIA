(window["webpackJsonp"] = window["webpackJsonp"] || []).push([[28],{

/***/ "../../node_modules/@genexus/gemini/dist/esm-es5/ch-sidebar-menu-list.entry.js":
/*!******************************************************************************************************************!*\
  !*** /__w/gx-web-editors/gx-web-editors/node_modules/@genexus/gemini/dist/esm-es5/ch-sidebar-menu-list.entry.js ***!
  \******************************************************************************************************************/
/*! exports provided: ch_sidebar_menu_list */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"ch_sidebar_menu_list\", function() { return ChSidebarMenuList; });\n/* harmony import */ var _index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./index-09b1517f.js */ \"../../node_modules/@genexus/gemini/dist/esm-es5/index-09b1517f.js\");\n\nvar chSidebarMenuListCss = \":host{display:block}:host ul{padding:0;margin:0}\";\nvar ChSidebarMenuList = /** @class */ (function () {\n    function ChSidebarMenuList(hostRef) {\n        Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"r\"])(this, hostRef);\n        /*******************\n         * STATE\n         *******************/\n        this.listType = \"list-one\";\n    }\n    ChSidebarMenuList.prototype.componentWillLoad = function () {\n        /*\n        Set the type of list\n        list-three : has two parent lists\n        list-two : has one parent list\n        list-one : has no parent lists. It is the main list (this is the default)\n        */\n        if (this.el.parentNode.parentElement !== null) {\n            if (this.el.parentNode.parentElement.nodeName === \"CH-SIDEBAR-MENU-LIST\") {\n                this.listType = \"list-two\";\n            }\n        }\n        if (this.el.parentNode.parentElement.parentElement.parentElement !== null) {\n            if (this.el.parentNode.parentElement.parentNode.parentElement.nodeName ===\n                \"CH-SIDEBAR-MENU-LIST\") {\n                this.listType = \"list-three\";\n            }\n        }\n    };\n    ChSidebarMenuList.prototype.render = function () {\n        return (Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"H\"], { class: {\n                \"list-one\": this.listType === \"list-one\",\n                \"list-two\": this.listType === \"list-two\",\n                \"list-three\": this.listType === \"list-three\",\n            } }, Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(\"slot\", null)));\n    };\n    Object.defineProperty(ChSidebarMenuList.prototype, \"el\", {\n        get: function () { return Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"g\"])(this); },\n        enumerable: false,\n        configurable: true\n    });\n    return ChSidebarMenuList;\n}());\nChSidebarMenuList.style = chSidebarMenuListCss;\n\n\n\n//# sourceURL=webpack:////__w/gx-web-editors/gx-web-editors/node_modules/@genexus/gemini/dist/esm-es5/ch-sidebar-menu-list.entry.js?");

/***/ })

}]);