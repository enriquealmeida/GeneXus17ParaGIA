import { Component, Element, State, h, Prop, Listen } from "@stencil/core";
export class GxgTree {
    constructor() {
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
            } },
            h("div", { class: "main-tree-container" },
                h("ul", { ref: (el) => (this.ulTree = el) },
                    h("slot", null))))) : (h("div", { class: {
                tree: true,
                "nested-tree": true,
            } },
            h("ul", { ref: (el) => (this.ulTree = el) },
                h("slot", null))));
    }
    static get is() { return "gxg-tree"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["tree.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["tree.css"]
    }; }
    static get properties() { return {
        "checkbox": {
            "type": "boolean",
            "mutable": false,
            "complexType": {
                "original": "boolean",
                "resolved": "boolean",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "Set this attribute if you want all this tree tree-items to have a checkbox"
            },
            "attribute": "checkbox",
            "reflect": false,
            "defaultValue": "false"
        },
        "checked": {
            "type": "boolean",
            "mutable": false,
            "complexType": {
                "original": "boolean",
                "resolved": "boolean",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "Set this attribute if you want all this tree tree-items to have the checkbox checked"
            },
            "attribute": "checked",
            "reflect": false,
            "defaultValue": "false"
        },
        "toggleCheckboxes": {
            "type": "boolean",
            "mutable": false,
            "complexType": {
                "original": "boolean",
                "resolved": "boolean",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "Set this attribute if you want all the childen item's checkboxes to be checked when the parent item checkbox is checked, or to be unchecked when the parent item checkbox is unckecked."
            },
            "attribute": "toggle-checkboxes",
            "reflect": false,
            "defaultValue": "false"
        }
    }; }
    static get states() { return {
        "nestedTree": {},
        "mainTree": {}
    }; }
    static get elementRef() { return "el"; }
    static get listeners() { return [{
            "name": "liItemClicked",
            "method": "liItemClickedHandler",
            "target": undefined,
            "capture": false,
            "passive": false
        }, {
            "name": "toggleIconClicked",
            "method": "toggleIconClickedHandler",
            "target": undefined,
            "capture": false,
            "passive": false
        }, {
            "name": "checkboxClickedEvent",
            "method": "checkboxClickedEventHandler",
            "target": undefined,
            "capture": false,
            "passive": false
        }]; }
}
