(window["webpackJsonp"] = window["webpackJsonp"] || []).push([[32],{

/***/ "../../node_modules/@genexus/gemini/dist/esm-es5/gxg-alert.entry.js":
/*!*******************************************************************************************************!*\
  !*** /__w/gx-web-editors/gx-web-editors/node_modules/@genexus/gemini/dist/esm-es5/gxg-alert.entry.js ***!
  \*******************************************************************************************************/
/*! exports provided: gxg_alert */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"gxg_alert\", function() { return GxgAlert; });\n/* harmony import */ var _index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./index-09b1517f.js */ \"../../node_modules/@genexus/gemini/dist/esm-es5/index-09b1517f.js\");\n\nvar alertCss = \"/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{position:fixed;bottom:0;z-index:100;-webkit-transition-property:-webkit-transform;transition-property:-webkit-transform;transition-property:transform;transition-property:transform, -webkit-transform;-webkit-transition-duration:var(--ui-animaton-speed);transition-duration:var(--ui-animaton-speed);-webkit-transition-timing-function:ease;transition-timing-function:ease}:host .alert-message{background-color:var(--gray-07);color:var(--color-on-primary);border-width:0;border-top-width:var(--border-width-lg);border-style:solid;border-color:var(--gray-06);padding:var(--spacing-comp-03);display:-ms-flexbox;display:flex;-ms-flex-align:start;align-items:flex-start;-ms-flex-pack:justify;justify-content:space-between;font-family:var(--font-family-primary);}:host .alert-message--error{border-color:var(--color-error-dark);background-color:var(--color-error-light);color:var(--color-on-secondary)}:host .alert-message--warning{border-color:var(--color-warning-dark);background-color:var(--color-warning-light);color:var(--color-on-secondary)}:host .alert-message--success{border-color:var(--color-success-dark);background-color:var(--color-success-light);color:var(--color-on-secondary)}:host .alert-message__close{display:-ms-flexbox;display:flex}:host .alert-message--title{margin:0 0 var(--spacing-comp-02) 0;font-size:var(--font-size-xs);text-transform:uppercase}:host .alert-message--icon{-webkit-margin-end:var(--spacing-comp-02);margin-inline-end:var(--spacing-comp-02);display:-ms-flexbox;display:flex}:host .alert-message--container{display:-ms-flexbox;display:flex;-ms-flex-align:top;align-items:top}:host .alert-message--description{font-size:var(--font-size-md);margin-top:0;margin-bottom:0;line-height:1.5em}:host([position=end]:not(.rtl)){left:auto !important}:host(:not([active])){-webkit-transform:translateY(100%) !important;transform:translateY(100%) !important}:host([position=center]){left:50% !important;-webkit-transform:translateX(-50%);transform:translateX(-50%)}:host(:not([active])[position=center]){-webkit-transform:translateY(100%) translateX(-50%) !important;transform:translateY(100%) translateX(-50%) !important}:host([position=center].rtl){right:50% !important;-webkit-transform:translateX(50%);transform:translateX(50%)}:host(:not([active])[position=center].rtl){-webkit-transform:translateY(100%) translateX(50%) !important;transform:translateY(100%) translateX(50%) !important}:host([position=end].rtl){right:auto !important}\";\nvar GxgAlert = /** @class */ (function () {\n    function GxgAlert(hostRef) {\n        Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"r\"])(this, hostRef);\n        /*********************************\n        PROPERTIES & STATE\n        *********************************/\n        /**\n         * Wether the alert is active (visible) or hidden\n         */\n        this.active = false;\n        /**\n         * The amount of time the alert is visible before hidding under the document\n         */\n        this.activeTime = \"regular\";\n        /**\n         * The alert position on the X axis\n         */\n        this.position = \"start\";\n        /**\n         * The alert flavor\n         */\n        this.type = \"notice\";\n        /**\n         * The presence of this attribute makes the component full-width\n         */\n        this.fullWidth = false;\n        /**\n         * The spacing between the alert, and the left or right side of the document\n         */\n        this.leftRight = \"xs\";\n        /**\n         * The spacing between the alert and the bottom side of the document\n         */\n        this.bottom = \"xs\";\n        /**\n         * The alert width\n         */\n        this.width = \"350px\";\n        /**\n         * The presence of this attribute removes the sound on the 'warning' or 'error' alert\n         */\n        this.silent = false;\n        /**\n         * Reading direction\n         */\n        this.rtl = false;\n    }\n    /*********************************\n    METHODS\n    *********************************/\n    GxgAlert.prototype.componentDidLoad = function () {\n        this.el.removeAttribute(\"hidden\");\n        //Reading Direction\n        var dirHtml = document\n            .getElementsByTagName(\"html\")[0]\n            .getAttribute(\"dir\");\n        var dirBody = document\n            .getElementsByTagName(\"body\")[0]\n            .getAttribute(\"dir\");\n        if (dirHtml === \"rtl\" || dirBody === \"rtl\") {\n            this.rtl = true;\n        }\n    };\n    GxgAlert.prototype.iconColor = function () {\n        if (this.type === \"notice\")\n            return \"negative\";\n        if (this.type === \"error\")\n            return \"error\";\n        if (this.type === \"warning\")\n            return \"warning\";\n        if (this.type === \"success\")\n            return \"success\";\n    };\n    GxgAlert.prototype.closeIconColor = function () {\n        if (this.type === \"notice\") {\n            return \"negative\";\n        }\n        else {\n            return \"onbackground\";\n        }\n    };\n    GxgAlert.prototype.printTitle = function () {\n        if (this.title !== undefined) {\n            return Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(\"h2\", { class: \"alert-message--title\" }, this.title);\n        }\n    };\n    GxgAlert.prototype.setAlertInactive = function () {\n        this.active = false;\n        this.el.removeAttribute(\"role\");\n    };\n    GxgAlert.prototype.validateName = function (newValue) {\n        var _this = this;\n        //timing\n        var timingValue;\n        switch (this.activeTime) {\n            case \"xxslow\":\n                timingValue = \"10\";\n                break;\n            case \"xslow\":\n                timingValue = \"09\";\n                break;\n            case \"slow\":\n                timingValue = \"08\";\n                break;\n            case \"regular\":\n                timingValue = \"07\";\n                break;\n            case \"fast\":\n                timingValue = \"06\";\n                break;\n            case \"xfast\":\n                timingValue = \"05\";\n                break;\n            case \"xxfast\":\n                timingValue = \"04\";\n                break;\n            default:\n                timingValue = \"04\";\n        }\n        if (newValue === true) {\n            this.el.setAttribute(\"role\", \"alert\");\n            setTimeout(function () {\n                _this.setAlertInactive();\n            }, parseInt(getComputedStyle(document.documentElement).getPropertyValue(\"--timing-\" + timingValue)));\n        }\n    };\n    GxgAlert.prototype.defineWidth = function () {\n        if (this.fullWidth) {\n            var lateralSpacingComputedValue = getComputedStyle(document.documentElement).getPropertyValue(\"--spacing-lay-\" + this.leftRight);\n            //remove \"px\" to multiply, since we want the spacing value from left, and from right.\n            var lateralSpacingComputedValueInt = parseInt(lateralSpacingComputedValue.replace(\"px\", \"\"), 10) * 2;\n            return \"calc(100% - \" + lateralSpacingComputedValueInt + \"px)\";\n        }\n        else {\n            return this.width;\n        }\n    };\n    GxgAlert.prototype.alertHidden = function () {\n        if (this.active) {\n            if (!this.silent) {\n                var audio_1;\n                if (this.type === \"warning\") {\n                    audio_1 = new Audio(Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"e\"])(\"./alert-assets/warning.mp3\"));\n                }\n                else if (this.type === \"error\") {\n                    audio_1 = new Audio(Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"e\"])(\"./alert-assets/error.mp3\"));\n                }\n                setTimeout(function () {\n                    audio_1.play();\n                }, 100);\n            }\n            return \"false\";\n        }\n        else {\n            return \"true\";\n        }\n    };\n    GxgAlert.prototype.transform = function (bottomSpacingValue) {\n        if (this.position === \"center\") {\n            if (this.rtl) {\n                return \"translateY(-\" + bottomSpacingValue + \") translateX(50%)\";\n            }\n            else {\n                return \"translateY(-\" + bottomSpacingValue + \") translateX(-50%)\";\n            }\n        }\n        else {\n            return \"translateY(-\" + bottomSpacingValue + \")\";\n        }\n    };\n    GxgAlert.prototype.render = function () {\n        var lateralSpacingValue;\n        if (this.leftRight === \"no-space\") {\n            lateralSpacingValue = \"0\";\n        }\n        else {\n            var bodyComputedStyles = getComputedStyle(document.body);\n            lateralSpacingValue = bodyComputedStyles\n                .getPropertyValue(\"--spacing-lay-\" + this.leftRight)\n                .replace(/\\s/g, \"\");\n        }\n        var bottomSpacingValue;\n        if (this.bottom === \"no-space\") {\n            bottomSpacingValue = \"0\";\n        }\n        else {\n            var bodyComputedStyles = getComputedStyle(document.body);\n            bottomSpacingValue = bodyComputedStyles\n                .getPropertyValue(\"--spacing-lay-\" + this.bottom)\n                .replace(/\\s/g, \"\");\n        }\n        return (Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"H\"], { role: \"alert\", \"aria-hidden\": this.alertHidden(), hidden: true, class: { rtl: this.rtl }, style: {\n                width: this.defineWidth(),\n                left: lateralSpacingValue,\n                right: lateralSpacingValue,\n                transform: this.transform(bottomSpacingValue),\n            } }, Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(\"div\", { class: {\n                \"alert-message\": true,\n                \"alert-message--notice\": this.type === \"notice\",\n                \"alert-message--error\": this.type === \"error\",\n                \"alert-message--warning\": this.type === \"warning\",\n                \"alert-message--success\": this.type === \"success\",\n            } }, Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(\"div\", { class: \"alert-message--container\" }, Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(\"div\", { class: \"alert-message--icon\" }, Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(\"gxg-icon\", { color: this.iconColor(), slot: \"icon\", type: \"gemini-tools/\" + this.type })), Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(\"div\", { class: \"alert-message-title-description\" }, this.printTitle(), Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(\"p\", { class: \"alert-message--description\" }, Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(\"slot\", null)))), Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(\"div\", { class: \"alert-message-close\" }, this.type === \"notice\" ? (Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(\"gxg-button\", { type: \"tertiary\", icon: \"gemini-tools/close\", onClick: this.setAlertInactive.bind(this), negative: true })) : (Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"h\"])(\"gxg-button\", { type: \"tertiary\", icon: \"gemini-tools/close\", onClick: this.setAlertInactive.bind(this), \"always-black\": true }))))));\n    };\n    Object.defineProperty(GxgAlert, \"assetsDirs\", {\n        get: function () { return [\"alert-assets\"]; },\n        enumerable: false,\n        configurable: true\n    });\n    Object.defineProperty(GxgAlert.prototype, \"el\", {\n        get: function () { return Object(_index_09b1517f_js__WEBPACK_IMPORTED_MODULE_0__[\"g\"])(this); },\n        enumerable: false,\n        configurable: true\n    });\n    Object.defineProperty(GxgAlert, \"watchers\", {\n        get: function () {\n            return {\n                \"active\": [\"validateName\"]\n            };\n        },\n        enumerable: false,\n        configurable: true\n    });\n    return GxgAlert;\n}());\nGxgAlert.style = alertCss;\n\n\n\n//# sourceURL=webpack:////__w/gx-web-editors/gx-web-editors/node_modules/@genexus/gemini/dist/esm-es5/gxg-alert.entry.js?");

/***/ })

}]);