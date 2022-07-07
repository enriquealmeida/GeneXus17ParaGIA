import { r as registerInstance, h, g as getElement } from './index-09b1517f.js';

const treeCss = "/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{display:-ms-flexbox;display:flex}:host ul{margin:0;padding-left:0;width:100%}:host .nested-tree{width:100%}:host(.main-tree){overflow:hidden}.main-tree-container{position:relative;width:100%}";

const GxgTree = class {
    constructor(hostRef) {
        registerInstance(this, hostRef);
        //PROPS
        /**
         * Set this attribute if you want all this tree tree-items to have a checkbox
         */
        this.checkbox = false;
        /**
         * Set this attribute if you want all this tree tree-items to have the checkbox checked
         */
        this.checked = false;
        /**
         * Set this attribute if you want all the childen item's checkboxes to be checked when the parent item checkbox is checked, or to be unchecked when the parent item checkbox is unckecked.
         */
        this.toggleCheckboxes = false;
        //STATE
        this.nestedTree = false;
        this.mainTree = false;
    }
    componentWillLoad() {
        //Check if this tree is nested
        const parentElementTagName = this.el.parentElement.tagName;
        if (parentElementTagName === "GXG-TREE-ITEM") {
            this.nestedTree = true;
        }
        //if this is the main tree...
        const parentTreeTagName = this.el.parentElement.tagName;
        if (parentTreeTagName !== "GXG-TREE-ITEM") {
            this.mainTree = true;
        }
        if (this.toggleCheckboxes) {
            //This property should be set one time on the mainTree by the developer using the component. If present, apply to all the child trees.
            const childrenTrees = this.el.querySelectorAll("gxg-tree");
            childrenTrees.forEach(function (tree) {
                tree.toggleCheckboxes = true;
            });
        }
        //If this tree has been added with appendChild, set toggleCheckboxes to true if the parent tree toggleCheckboxes property is set to true
        const closestTree = this.el.parentElement.parentElement;
        if (closestTree !== null && closestTree.tagName === "GXG-TREE") {
            if (closestTree.toggleCheckboxes) {
                this.toggleCheckboxes = true;
            }
        }
    }
    componentDidLoad() {
        if (this.checkbox) {
            //Add a checkbox to all this tree direct tree-items children
            const directTreeItemChildren = this.el.querySelectorAll("gxg-tree-item");
            directTreeItemChildren.forEach((treeItem) => {
                treeItem.setAttribute("checkbox", "checkbox");
                //If checked attribute is present, also set the checkboxes to be checked
                if (this.checked) {
                    treeItem.setAttribute("checked", "checked");
                }
            });
            //Finally, also check the parent tree-item that is above this tree
            const treeItemAboveTree = this.el.parentElement;
            treeItemAboveTree.setAttribute("checkbox", "checkbox");
            if (this.checked) {
                treeItemAboveTree.setAttribute("checked", "checked");
            }
        }
    }
    liItemClickedHandler() {
        //Remove 'selected' state from previous selected item
        const gxgTreeItems = this.el.querySelectorAll("gxg-tree-item");
        gxgTreeItems.forEach((gxgTreeItem) => {
            gxgTreeItem.selected = false;
        });
    }
    toggleIconClickedHandler() {
        //Update not leaf tree items vertical line height
        const treeItems = this.el.querySelectorAll("gxg-tree-item.not-leaf");
        treeItems.forEach((treeItem) => {
            treeItem.updateTreeVerticalLineHeight();
        });
    }
    checkboxClickedEventHandler() {
        if (this.toggleCheckboxes) {
            const childTreeItems = this.el.querySelectorAll("gxg-tree-item");
            let allCheckboxesChecked = true;
            let allCheckboxesUnchecked = true;
            childTreeItems.forEach(function (treeItem) {
                if (treeItem.checked) {
                    allCheckboxesUnchecked = false;
                }
                else {
                    allCheckboxesChecked = false;
                }
            });
            const parentTreeItem = this.el.parentElement;
            const tagName = parentTreeItem.tagName;
            if (tagName === "GXG-TREE-ITEM") {
                if (allCheckboxesChecked) {
                    parentTreeItem.checked = true;
                    parentTreeItem.indeterminate = false;
                }
                else if (allCheckboxesUnchecked) {
                    parentTreeItem.checked = false;
                    parentTreeItem.indeterminate = false;
                }
                else if (!allCheckboxesChecked && !allCheckboxesUnchecked) {
                    parentTreeItem.checked = true;
                    parentTreeItem.indeterminate = true;
                }
            }
        }
    }
    render() {
        return this.mainTree ? (h("div", { class: {
                tree: true,
                "main-tree": true,
            } }, h("div", { class: "main-tree-container" }, h("ul", { ref: (el) => (this.ulTree = el) }, h("slot", null))))) : (h("div", { class: {
                tree: true,
                "nested-tree": true,
            } }, h("ul", { ref: (el) => (this.ulTree = el) }, h("slot", null))));
    }
    get el() { return getElement(this); }
};
GxgTree.style = treeCss;

export { GxgTree as gxg_tree };
