(window["webpackJsonp"] = window["webpackJsonp"] || []).push([[79],{

/***/ "../../node_modules/@genexus/gemini/dist/esm-es5/gxg-tree.entry.js":
/*!******************************************************************************************************!*\
  !*** /__w/gx-web-editors/gx-web-editors/node_modules/@genexus/gemini/dist/esm-es5/gxg-tree.entry.js ***!
  \******************************************************************************************************/
/*! exports provided: gxg_tree */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"gxg_tree\", function() { return GxgTree; });\n/* harmony import */ var _index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./index-09b1517f.js */ \"../../node_modules/@genexus/gemini/dist/esm-es5/index-09b1517f.js\");\n\nvar treeCss = \"/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{display:-ms-flexbox;display:flex}:host ul{margin:0;padding-left:0;width:100%}:host .nested-tree{width:100%}:host(.main-tree){overflow:hidden}.main-tree-container{position:relative;width:100%}\";\nvar GxgTree = /** @class */ (function () {\n    function GxgTree(hostRef) {\n        Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"r\"])(this, hostRef);\n        //PROPS\n        /**\n         * Set this attribute if you want all this tree tree-items to have a checkbox\n         */\n        this.checkbox = false;\n        /**\n         * Set this attribute if you want all this tree tree-items to have the checkbox checked\n         */\n        this.checked = false;\n        /**\n         * Set this attribute if you want all the childen item's checkboxes to be checked when the parent item checkbox is checked, or to be unchecked when the parent item checkbox is unckecked.\n         */\n        this.toggleCheckboxes = false;\n        //STATE\n        this.nestedTree = false;\n        this.mainTree = false;\n    }\n    GxgTree.prototype.componentWillLoad = function () {\n        //Check if this tree is nested\n        var parentElementTagName = this.el.parentElement.tagName;\n        if (parentElementTagName === \"GXG-TREE-ITEM\") {\n            this.nestedTree = true;\n        }\n        //if this is the main tree...\n        var parentTreeTagName = this.el.parentElement.tagName;\n        if (parentTreeTagName !== \"GXG-TREE-ITEM\") {\n            this.mainTree = true;\n        }\n        if (this.toggleCheckboxes) {\n            //This property should be set one time on the mainTree by the developer using the component. If present, apply to all the child trees.\n            var childrenTrees = this.el.querySelectorAll(\"gxg-tree\");\n            childrenTrees.forEach(function (tree) {\n                tree.toggleCheckboxes = true;\n            });\n        }\n        //If this tree has been added with appendChild, set toggleCheckboxes to true if the parent tree toggleCheckboxes property is set to true\n        var closestTree = this.el.parentElement.parentElement;\n        if (closestTree !== null && closestTree.tagName === \"GXG-TREE\") {\n            if (closestTree.toggleCheckboxes) {\n                this.toggleCheckboxes = true;\n            }\n        }\n    };\n    GxgTree.prototype.componentDidLoad = function () {\n        var _this = this;\n        if (this.checkbox) {\n            //Add a checkbox to all this tree direct tree-items children\n            var directTreeItemChildren = this.el.querySelectorAll(\"gxg-tree-item\");\n            directTreeItemChildren.forEach(function (treeItem) {\n                treeItem.setAttribute(\"checkbox\", \"checkbox\");\n                //If checked attribute is present, also set the checkboxes to be checked\n                if (_this.checked) {\n                    treeItem.setAttribute(\"checked\", \"checked\");\n                }\n            });\n            //Finally, also check the parent tree-item that is above this tree\n            var treeItemAboveTree = this.el.parentElement;\n            treeItemAboveTree.setAttribute(\"checkbox\", \"checkbox\");\n            if (this.checked) {\n                treeItemAboveTree.setAttribute(\"checked\", \"checked\");\n            }\n        }\n    };\n    GxgTree.prototype.liItemClickedHandler = function () {\n        //Remove 'selected' state from previous selected item\n        var gxgTreeItems = this.el.querySelectorAll(\"gxg-tree-item\");\n        gxgTreeItems.forEach(function (gxgTreeItem) {\n            gxgTreeItem.selected = false;\n        });\n    };\n    GxgTree.prototype.toggleIconClickedHandler = function () {\n        //Update not leaf tree items vertical line height\n        var treeItems = this.el.querySelectorAll(\"gxg-tree-item.not-leaf\");\n        treeItems.forEach(function (treeItem) {\n            treeItem.updateTreeVerticalLineHeight();\n        });\n    };\n    GxgTree.prototype.checkboxClickedEventHandler = function () {\n        if (this.toggleCheckboxes) {\n            var childTreeItems = this.el.querySelectorAll(\"gxg-tree-item\");\n            var allCheckboxesChecked_1 = true;\n            var allCheckboxesUnchecked_1 = true;\n            childTreeItems.forEach(function (treeItem) {\n                if (treeItem.checked) {\n                    allCheckboxesUnchecked_1 = false;\n                }\n                else {\n                    allCheckboxesChecked_1 = false;\n                }\n            });\n            var parentTreeItem = this.el.parentElement;\n            var tagName = parentTreeItem.tagName;\n            if (tagName === \"GXG-TREE-ITEM\") {\n                if (allCheckboxesChecked_1) {\n                    parentTreeItem.checked = true;\n                    parentTreeItem.indeterminate = false;\n                }\n                else if (allCheckboxesUnchecked_1) {\n                    parentTreeItem.checked = false;\n                    parentTreeItem.indeterminate = false;\n                }\n                else if (!allCheckboxesChecked_1 && !allCheckboxesUnchecked_1) {\n                    parentTreeItem.checked = true;\n                    parentTreeItem.indeterminate = true;\n                }\n            }\n        }\n    };\n    GxgTree.prototype.render = function () {\n        var _this = this;\n        return this.mainTree ? (Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(\"div\", { class: {\n                tree: true,\n                \"main-tree\": true,\n            } }, Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(\"div\", { class: \"main-tree-container\" }, Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(\"ul\", { ref: function (el) { return (_this.ulTree = el); } }, Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(\"slot\", null))))) : (Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(\"div\", { class: {\n                tree: true,\n                \"nested-tree\": true,\n            } }, Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(\"ul\", { ref: function (el) { return (_this.ulTree = el); } }, Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(\"slot\", null))));\n    };\n    Object.defineProperty(GxgTree.prototype, \"el\", {\n        get: function () { return Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"g\"])(this); },\n        enumerable: false,\n        configurable: true\n    });\n    return GxgTree;\n}());\nGxgTree.style = treeCss;\n\n\n\n//# sourceURL=webpack:////__w/gx-web-editors/gx-web-editors/node_modules/@genexus/gemini/dist/esm-es5/gxg-tree.entry.js?");

/***/ })

}]);