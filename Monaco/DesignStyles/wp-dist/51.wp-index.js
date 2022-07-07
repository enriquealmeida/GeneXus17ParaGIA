(window["webpackJsonp"] = window["webpackJsonp"] || []).push([[51],{

/***/ "../../node_modules/@genexus/gemini/dist/esm-es5/gxg-listbox-item.entry.js":
/*!**************************************************************************************************************!*\
  !*** /__w/gx-web-editors/gx-web-editors/node_modules/@genexus/gemini/dist/esm-es5/gxg-listbox-item.entry.js ***!
  \**************************************************************************************************************/
/*! exports provided: gxg_listbox_item */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"gxg_listbox_item\", function() { return GxgListboxItem; });\n/* harmony import */ var _index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./index-09b1517f.js */ \"../../node_modules/@genexus/gemini/dist/esm-es5/index-09b1517f.js\");\n\nvar listboxItemCss = \":host{display:block;font-family:var(--font-family-primary);font-size:var(--font-size-sm);height:var(--spacing-comp-05);padding:0 var(--spacing-comp-02);color:var(--color-on-background);cursor:pointer;display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;-moz-user-select:none;-webkit-user-select:none;-ms-user-select:none;user-select:none;-o-user-select:none}:host .icon{-webkit-margin-end:var(--spacing-comp-01);margin-inline-end:var(--spacing-comp-01)}:host ::slotted(gxg-form-checkbox){-webkit-margin-end:var(--spacing-comp-01);margin-inline-end:var(--spacing-comp-01)}:host(:hover),:host(.selected:hover){background-color:var(--color-secondary-hover);color:var(--color-always-white)}:host(.has-icon.no-checkbox){-webkit-padding-start:var(--spacing-comp-01);padding-inline-start:var(--spacing-comp-01)}:host(.selected){background-color:var(--color-secondary-active);color:var(--color-always-white)}:host(:focus){outline-width:var(--border-width-md);outline-color:var(--color-primary-active);outline-style:solid;outline-offset:-2px}\";\nvar GxgListboxItem = /** @class */ (function () {\n    function GxgListboxItem(hostRef) {\n        Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"r\"])(this, hostRef);\n        this.itemClicked = Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"c\"])(this, \"itemClicked\", 7);\n        this.KeyPressed = Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"c\"])(this, \"KeyPressed\", 7);\n        /**\n         * Any icon that belongs to Gemini icon library: https://gx-gemini.netlify.app/?path=/story/icons\n         */\n        this.icon = undefined;\n        /**\n         * The item value. If value is not provided, the value will be the item innerHTML.\n         */\n        this.value = undefined;\n        /**\n         * (This prop is for internal use).\n         */\n        this.iconColor = \"auto\";\n        this.checkboxes = false;\n    }\n    GxgListboxItem.prototype.componentWillLoad = function () {\n        var listbox = this.el.parentElement;\n        var listboxCheckboxes = listbox.getAttribute(\"checkboxes\");\n        if (listboxCheckboxes !== null) {\n            this.checkboxes = true;\n        }\n    };\n    GxgListboxItem.prototype.itemClickedFunc = function (e) {\n        var index = this.el.getAttribute(\"index\");\n        this.itemClicked.emit({\n            index: parseInt(index, 10),\n            crtlKey: e.ctrlKey,\n            cmdKey: e.metaKey,\n            shiftKey: e.shiftKey,\n        });\n    };\n    GxgListboxItem.prototype.onKeyDown = function (e) {\n        e.preventDefault();\n        if (e.code === \"ArrowDown\" || e.code === \"ArrowUp\" || e.code === \"Enter\") {\n            var index = this.el.getAttribute(\"index\");\n            this.KeyPressed.emit({\n                index: parseInt(index, 10),\n                crtlKey: e.ctrlKey,\n                cmdKey: e.metaKey,\n                shiftKey: e.shiftKey,\n                eCode: e.code,\n            });\n        }\n    };\n    GxgListboxItem.prototype.onMouseOver = function () {\n        this.iconColor = \"negative\";\n    };\n    GxgListboxItem.prototype.onMouseOut = function () {\n        var itemIsSelected = this.el.classList.contains(\"selected\");\n        if (!itemIsSelected) {\n            this.iconColor = \"auto\";\n        }\n    };\n    GxgListboxItem.prototype.render = function () {\n        return (Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"H\"], { class: {\n                \"has-icon\": this.icon !== undefined,\n                \"no-checkbox\": !this.checkboxes,\n            }, onClick: this.itemClickedFunc.bind(this), onKeyDown: this.onKeyDown.bind(this), onMouseOver: this.onMouseOver.bind(this), onMouseOut: this.onMouseOut.bind(this) }, Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(\"slot\", { name: \"checkbox\" }), this.icon !== undefined ? (Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(\"gxg-icon\", { class: \"icon\", color: this.iconColor, size: \"regular\", type: this.icon })) : null, Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(\"slot\", null)));\n    };\n    Object.defineProperty(GxgListboxItem.prototype, \"el\", {\n        get: function () { return Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"g\"])(this); },\n        enumerable: false,\n        configurable: true\n    });\n    return GxgListboxItem;\n}());\nGxgListboxItem.style = listboxItemCss;\n\n\n\n//# sourceURL=webpack:////__w/gx-web-editors/gx-web-editors/node_modules/@genexus/gemini/dist/esm-es5/gxg-listbox-item.entry.js?");

/***/ })

}]);