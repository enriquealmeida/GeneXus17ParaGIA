import { Component, Event, Element, Host, Prop, State, h, Watch, Method, } from "@stencil/core";
export class GxgTreeItem {
    constructor() {
        //PROPS
        /**
         * Set this attribute if you want the gxg-treeitem to display a checkbox
         */
        this.checkbox = false;
        /**
         * Set this attribute if you want the gxg-treeitem checkbox to be checked by default
         */
        this.checked = false;
        /**
         * Set this attribute if this tree-item has a resource to be downloaded;
         */
        this.download = false;
        /**
         * Set this attribute when you are downloading a resource
         */
        this.downloading = false;
        /**
         * Set this attribute when you have downloaded the resource
         */
        this.downloaded = false;
        /**
         * If this tree-item has a nested tree, set this attribute to make the tree open by default
         */
        this.opened = false;
        /**
         * The presence of this attribute sets the tree-item as selected
         */
        this.selected = false;
        /**
         * The presence of this attribute displays a +/- icon to toggle/untoggle the tree
         */
        this.isLeaf = undefined;
        //PROPS
        this.hasChildTree = false;
        this.firstTreeItem = false;
        this.disabled = false;
        //STATE
        this.numberOfParentTrees = 1;
        //@State() verticalLineHeight: string;
        this.horizontalLinePaddingLeft = 0;
        this.lastTreeItem = false;
        this.firstTreeItemOfParentTree = false;
        this.lastTreeItemOfParentTree = false;
        this.rightIconColor = "auto";
        this.numberOfDirectTreeItemsDescendants = 0;
    }
    componentWillLoad() {
        //Count number of parent trees in order to set the apporpiate padding-left
        this.numberOfParentTrees = this.getParents(this.el);
        //If tree item has not a tree-item inside, is leaf
        const treeItemHasTree = this.el.querySelector('[slot="tree"]');
        if (this.isLeaf === undefined) {
            if (treeItemHasTree === null) {
                this.isLeaf = true;
            }
            else {
                this.hasChildTree = true;
            }
        }
        //If is first item of tree
        const prevItem = this.el.previousElementSibling;
        if (prevItem === null) {
            this.firstTreeItem = true;
        }
        //If is last item of tree
        const nextItem = this.el.nextElementSibling;
        if (nextItem === null) {
            this.lastTreeItem = true;
        }
        //If is first item of parent Tree
        if (this.numberOfParentTrees === 1) {
            const prevItem = this.el.previousElementSibling;
            if (prevItem === null) {
                this.firstTreeItemOfParentTree = true;
            }
        }
        //If is last item of parent Tree
        if (this.numberOfParentTrees === 1) {
            const nextItem = this.el.nextElementSibling;
            if (nextItem === null) {
                this.lastTreeItemOfParentTree = true;
            }
        }
        //Set right icon color
        if (this.download && this.rightIcon.includes("download")) {
            this.rightIconColor = "primary-enabled";
        }
        else if (this.disabled) {
            this.rightIconColor = "disabled";
        }
        //If this tree item has a source to download, this item has child tree, and is not leaf. Also, set the tree as not open
        if (this.download) {
            this.hasChildTree = true;
            this.isLeaf = false;
            this.opened = false;
        }
    }
    getNumberOfVisibleDescendants() {
        const directTree = this.el.querySelector(":scope > gxg-tree");
        if (directTree !== null && this.opened) {
            //if tree item has a tree inside and is open...
            const visibleChildren = directTree.querySelectorAll("gxg-tree-item.visible");
            //direct descendants
            const directDescendants = directTree.querySelectorAll(":scope > gxg-tree-item.visible");
            //last direct descendant
            const lastDirectDescendant = directDescendants[directDescendants.length - 1];
            const lastDirectDescendantIsOpened = lastDirectDescendant
                .opened;
            let lastDirectDescendantTreeItemsLength = 0;
            if (lastDirectDescendantIsOpened) {
                const lastDirectDescendantTree = lastDirectDescendant.querySelector(":scope > gxg-tree");
                lastDirectDescendantTreeItemsLength = lastDirectDescendantTree.querySelectorAll(":scope > gxg-tree-item").length;
            }
            if (visibleChildren.length > 0) {
                this.numberOfDirectTreeItemsDescendants =
                    visibleChildren.length - lastDirectDescendantTreeItemsLength;
            }
        }
    }
    setVisibleTreeItems() {
        const directTree = this.el.querySelector(":scope > gxg-tree");
        if (directTree !== null) {
            const directTreeDirectTreeItems = directTree.querySelectorAll(":scope > gxg-tree-item");
            if (this.opened) {
                directTreeDirectTreeItems.forEach((item) => {
                    item.classList.remove("not-visible");
                    item.classList.add("visible");
                });
            }
            else {
                directTreeDirectTreeItems.forEach((item) => {
                    item.classList.remove("visible");
                    item.classList.add("not-visible");
                });
            }
        }
    }
    componentDidLoad() {
        this.setVisibleTreeItems();
        this.getNumberOfVisibleDescendants();
    }
    watchHandler(newValue) {
        if (newValue) {
            //this.updateTreeVerticalLineHeight();
        }
    }
    getParents(elem) {
        // Returns the number of parent tree items in order to set the appropriate paddding-left
        // Set up a parent array
        const numberOfTreeParents = [];
        // Push each parent element to the array
        for (; elem && elem !== document; elem = elem.parentNode) {
            const elemTagNAme = elem.tagName;
            if (elemTagNAme === "GXG-TREE") {
                numberOfTreeParents.push(elem);
            }
        }
        return numberOfTreeParents.length;
    }
    toggleTreeIconClicked() {
        if (this.opened) {
            this.opened = false;
        }
        else {
            this.opened = true;
        }
        this.setVisibleTreeItems();
        this.toggleIconClicked.emit();
    }
    async updateTreeVerticalLineHeight() {
        this.getNumberOfVisibleDescendants();
    }
    liTextClicked() {
        this.liItemClicked.emit();
        this.selected = true;
    }
    liTextDoubleClicked() {
        this.toggleTreeIconClicked();
    }
    liTextKeyDownPressed(e) {
        if (e.key === "ArrowDown" || e.key === "ArrowUp") {
            e.preventDefault(); //prevents scrolling
        }
        //ENTER
        if (e.key === "Enter") {
            //Enter should check/uncheck the checkbox (if present)
            this.checkboxClicked();
            if (this.download) {
                //If the item has a resource to be downloaded, download.
                this.el.click();
            }
        }
        //LEFT/RIGHT NAVIGATION
        if (e.key === "ArrowRight" && !this.isLeaf) {
            //Toggle the tree
            if (!this.opened) {
                this.opened = true;
            }
            else {
                const childTree = this.el.querySelector("gxg-tree");
                const childTreeFirstChildren = childTree.querySelector("gxg-tree-item");
                const childTreeFirstChildrenLiText = childTreeFirstChildren.shadowRoot.querySelector(".li-text");
                childTreeFirstChildrenLiText.focus();
            }
            this.setVisibleTreeItems();
            this.toggleIconClicked.emit(); //this recalculates the vertical line height
        }
        if (e.key === "ArrowLeft") {
            let hasParent = false;
            const parentGxgTreeItem = this.el.parentElement.parentElement;
            let parentGxgTreeItemLiText = null;
            if (parentGxgTreeItem.tagName === "GXG-TREE-ITEM") {
                hasParent = true;
                parentGxgTreeItemLiText = parentGxgTreeItem.shadowRoot.querySelector(".li-text");
            }
            if (this.isLeaf) {
                if (hasParent) {
                    parentGxgTreeItemLiText.focus();
                }
            }
            else {
                const li = this.el.shadowRoot.querySelector("li");
                if (li.classList.contains("tree-open")) {
                    this.opened = false;
                }
                else {
                    if (hasParent) {
                        parentGxgTreeItemLiText.focus();
                    }
                }
            }
            this.setVisibleTreeItems();
            this.toggleIconClicked.emit(); //this recalculates the vertical line height
        }
        // UP/DOWN NAVIGATION
        if (e.key === "ArrowUp" || (e.key === "Tab" && e.shiftKey)) {
            e.preventDefault();
            if (!this.firstTreeItemOfParentTree) {
                //Is not the first element of the parent
                //Set focus on the prev item
                let prevItem;
                const prevElementSibling = this.el.previousElementSibling;
                if (e.shiftKey && e.key !== "Tab") {
                    //if shift key was pressed, navigate to the previous sibling
                    if (prevElementSibling !== null) {
                        prevItem = prevElementSibling.shadowRoot.querySelector("li .li-text");
                    }
                }
                else {
                    if (prevElementSibling === null) {
                        const parentItem = this.el.parentElement;
                        const parentParentItem = parentItem.parentElement;
                        prevItem = parentParentItem.shadowRoot.querySelector("li .li-text");
                    }
                    else {
                        prevItem = prevElementSibling.shadowRoot.querySelector("li .li-text");
                        if (prevElementSibling !== null) {
                            //If the preceding tree-item has tree inside...
                            const prevElementSiblingHasChildTree = prevElementSibling
                                .hasChildTree;
                            if (prevElementSiblingHasChildTree) {
                                const prevElementSiblingHasOpenTree = prevElementSibling
                                    .opened;
                                if (prevElementSiblingHasOpenTree && !this.download) {
                                    //If preceding tree-item tree is opened, then the prev item is the last item of that tree
                                    const prevElemSiblingTreeItem = this.el
                                        .previousElementSibling;
                                    const prevElemSiblingTreeItemTree = prevElemSiblingTreeItem.querySelector("gxg-tree");
                                    //
                                    if (prevElemSiblingTreeItemTree.lastElementChild
                                        .hasChildTree) {
                                        if (prevElemSiblingTreeItemTree.lastElementChild.shadowRoot
                                            .querySelector("li")
                                            .classList.contains("tree-open")) {
                                            prevItem = prevElemSiblingTreeItemTree.lastElementChild
                                                .querySelector("gxg-tree")
                                                .lastElementChild.shadowRoot.querySelector("li .li-text");
                                        }
                                        else {
                                            prevItem = prevElemSiblingTreeItemTree.lastElementChild.shadowRoot.querySelector("li .li-text");
                                        }
                                    }
                                    else {
                                        prevItem = prevElemSiblingTreeItemTree.lastElementChild.shadowRoot.querySelector("li .li-text");
                                    }
                                    //
                                }
                                else {
                                    //The preciding item has a tree, but it is closed
                                    prevItem = this.el.previousElementSibling.shadowRoot.querySelector("li .li-text");
                                }
                            }
                        }
                    }
                }
                if (prevItem !== null && prevItem !== undefined) {
                    prevItem.focus();
                }
            }
        }
        if (e.key === "ArrowDown" || (e.key === "Tab" && !e.shiftKey)) {
            e.preventDefault();
            if (!this.lastTreeItemOfParentTree) {
                //Set focus on the next item
                let nextItem;
                if (e.shiftKey) {
                    //if shift key was pressed, navigate to the next sibling
                    if (this.el.nextElementSibling !== null) {
                        nextItem = this.el.nextElementSibling.shadowRoot.querySelector("li .li-text");
                    }
                }
                else {
                    if (this.lastTreeItem) {
                        if (this.hasChildTree && this.opened) {
                            nextItem = this.el.firstElementChild.firstElementChild.shadowRoot.querySelector(".li-text");
                        }
                        else {
                            const thisTree = this.el.parentElement;
                            const thisTreeParent = thisTree.parentElement;
                            const thisTreeParentNextTree = thisTreeParent.nextElementSibling;
                            if (thisTreeParentNextTree === null) {
                                if (thisTreeParent.parentElement.parentElement
                                    .nextElementSibling !== null) {
                                    nextItem = thisTreeParent.parentElement.parentElement.nextElementSibling.shadowRoot.querySelector(".li-text");
                                }
                            }
                            else {
                                nextItem = thisTreeParentNextTree.shadowRoot.querySelector(".li-text");
                            }
                        }
                    }
                    else {
                        if (this.hasChildTree && this.opened && !this.download) {
                            nextItem = this.el
                                .querySelector("gxg-tree gxg-tree-item")
                                .shadowRoot.querySelector("li .li-text");
                        }
                        else {
                            nextItem = this.el.nextElementSibling.shadowRoot.querySelector(".li-text");
                        }
                    }
                }
                if (nextItem !== null && nextItem !== undefined) {
                    nextItem.focus();
                }
            }
            else {
                //Last element of parent tree
                if (this.el.classList.contains("not-leaf") &&
                    this.el.shadowRoot.querySelector("li").classList.contains("tree-open")) {
                    const childTreeFirstTreeItem = this.el.firstElementChild.firstElementChild.shadowRoot.querySelector("li .li-text");
                    childTreeFirstTreeItem.focus();
                }
            }
        }
    }
    returnToggleIconType() {
        //Returns the type of icon : gemini-tools/add or gemini-tools/minus
        if (!this.opened || this.download) {
            return "gemini-tools/add";
        }
        else {
            return "gemini-tools/minus";
        }
    }
    returnPaddingLeft() {
        //returns the appropriate padding left to the .li-text element
        let paddingLeft = 0;
        if (this.numberOfParentTrees !== 1) {
            paddingLeft = (this.numberOfParentTrees - 1) * 31 + 5;
        }
        else {
            paddingLeft = 5;
        }
        if (!this.isLeaf && this.numberOfParentTrees !== 1) {
            //paddingLeft -= 5;
        }
        this.itemPaddingLeft = paddingLeft;
        return paddingLeft + "px";
    }
    returnVerticalLineLeftPosition() {
        //Returns the left position of the vertical line that associates the chid-items with the parent item
        if (this.numberOfParentTrees !== 1) {
            return this.itemPaddingLeft + 5 + "px";
        }
        else {
            return this.itemPaddingLeft + 5 + "px";
        }
    }
    checkboxTabIndex() {
        return -1;
    }
    liTextTabIndex() {
        return 1;
    }
    setIndeterminate() {
        if (this.indeterminate) {
            return true;
        }
        else {
            return false;
        }
    }
    checkboxClicked() {
        if (this.checkbox) {
            if (this.checked) {
                this.checked = false;
                this.toggleTreeItemsCheckboxes(false);
                this.checkboxClickedEvent.emit(false);
            }
            else {
                this.checked = true;
                this.toggleTreeItemsCheckboxes(true);
                this.checkboxClickedEvent.emit(true);
            }
        }
    }
    toggleTreeItemsCheckboxes(checked) {
        //Only do if toggleCheckboxes property exists in parent tree
        const parentTree = this.el.parentElement;
        if (parentTree.toggleCheckboxes) {
            this.indeterminate = false;
            const childTree = this.el.querySelector("gxg-tree");
            if (childTree !== null) {
                const childTreeItems = childTree.querySelectorAll("gxg-tree-item");
                childTreeItems.forEach(function (treeItem) {
                    if (checked) {
                        treeItem.checked = true;
                    }
                    else {
                        treeItem.checked = false;
                    }
                });
            }
        }
    }
    render() {
        return (h(Host, { class: { leaf: this.isLeaf, "not-leaf": !this.isLeaf } },
            h("li", { class: {
                    "tree-open": this.opened,
                    disabled: this.disabled,
                } },
                h("div", { class: {
                        "li-text": true,
                        "li-text--not-leaf": !this.isLeaf,
                        "li-text--leaf": this.isLeaf,
                        "li-text--first-tree-item": this.firstTreeItem,
                        "li-text--has-child-tree": this.hasChildTree,
                        "li-text--selected": this.selected,
                    }, style: { paddingLeft: this.returnPaddingLeft() }, onClick: this.liTextClicked.bind(this), onDblClick: this.liTextDoubleClicked.bind(this), onKeyDown: this.liTextKeyDownPressed.bind(this), tabIndex: this.liTextTabIndex() },
                    !this.isLeaf || this.download
                        ? [
                            h("span", { class: { "vertical-line": true }, style: {
                                    //height: this.verticalLineHeight,
                                    height: this.numberOfDirectTreeItemsDescendants * 20 -
                                        10 +
                                        "px",
                                    left: this.returnVerticalLineLeftPosition(),
                                } }),
                            h("div", { class: { "closed-opened-icons": true } },
                                h("gxg-icon", { type: this.returnToggleIconType(), size: "small", onClick: this.toggleTreeIconClicked.bind(this), class: "toggle-icon" })),
                        ]
                        : null,
                    h("span", { class: {
                            "horizontal-line": true,
                            "display-none": this.numberOfParentTrees === 1,
                        }, style: {
                            left: this.itemPaddingLeft + "px",
                        } }),
                    this.checkbox ? (h("gxg-form-checkbox", { checked: this.checked, class: { checkbox: true }, tabIndex: this.checkboxTabIndex(), indeterminate: this.setIndeterminate(), disabled: this.disabled, onClick: this.checkboxClicked.bind(this) })) : null,
                    this.leftIcon ? (h("gxg-icon", { size: "small", type: this.leftIcon, color: this.disabled ? "disabled" : "auto" })) : null,
                    h("span", { class: "text" },
                        h("slot", null)),
                    this.rightIcon ? (h("gxg-icon", { size: "small", type: this.rightIcon, color: this.rightIconColor, class: { "right-icon": true } })) : null,
                    this.download ? h("span", { class: { loading: true } }) : null),
                h("slot", { name: "tree" }))));
    }
    static get is() { return "gxg-tree-item"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["tree-item.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["tree-item.css"]
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
                "text": "Set this attribute if you want the gxg-treeitem to display a checkbox"
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
                "text": "Set this attribute if you want the gxg-treeitem checkbox to be checked by default"
            },
            "attribute": "checked",
            "reflect": false,
            "defaultValue": "false"
        },
        "download": {
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
                "text": "Set this attribute if this tree-item has a resource to be downloaded;"
            },
            "attribute": "download",
            "reflect": false,
            "defaultValue": "false"
        },
        "downloading": {
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
                "text": "Set this attribute when you are downloading a resource"
            },
            "attribute": "downloading",
            "reflect": false,
            "defaultValue": "false"
        },
        "downloaded": {
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
                "text": "Set this attribute when you have downloaded the resource"
            },
            "attribute": "downloaded",
            "reflect": false,
            "defaultValue": "false"
        },
        "leftIcon": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "string",
                "resolved": "string",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "Set the left side icon from the available Gemini icon set : https://gx-gemini.netlify.app/?path=/story/icons-icons--controls"
            },
            "attribute": "left-icon",
            "reflect": false
        },
        "rightIcon": {
            "type": "string",
            "mutable": false,
            "complexType": {
                "original": "string",
                "resolved": "string",
                "references": {}
            },
            "required": false,
            "optional": false,
            "docs": {
                "tags": [],
                "text": "Set thhe right side icon from the available Gemini icon set : https://gx-gemini.netlify.app/?path=/story/icons-icons--controls"
            },
            "attribute": "right-icon",
            "reflect": false
        },
        "opened": {
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
                "text": "If this tree-item has a nested tree, set this attribute to make the tree open by default"
            },
            "attribute": "opened",
            "reflect": false,
            "defaultValue": "false"
        },
        "selected": {
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
                "text": "The presence of this attribute sets the tree-item as selected"
            },
            "attribute": "selected",
            "reflect": false,
            "defaultValue": "false"
        },
        "isLeaf": {
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
                "text": "The presence of this attribute displays a +/- icon to toggle/untoggle the tree"
            },
            "attribute": "is-leaf",
            "reflect": false,
            "defaultValue": "undefined"
        },
        "hasChildTree": {
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
                "text": ""
            },
            "attribute": "has-child-tree",
            "reflect": false,
            "defaultValue": "false"
        },
        "firstTreeItem": {
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
                "text": ""
            },
            "attribute": "first-tree-item",
            "reflect": false,
            "defaultValue": "false"
        },
        "indeterminate": {
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
                "text": ""
            },
            "attribute": "indeterminate",
            "reflect": false
        },
        "disabled": {
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
                "text": ""
            },
            "attribute": "disabled",
            "reflect": false,
            "defaultValue": "false"
        }
    }; }
    static get states() { return {
        "numberOfParentTrees": {},
        "itemPaddingLeft": {},
        "horizontalLinePaddingLeft": {},
        "lastTreeItem": {},
        "firstTreeItemOfParentTree": {},
        "lastTreeItemOfParentTree": {},
        "rightIconColor": {},
        "numberOfDirectTreeItemsDescendants": {}
    }; }
    static get events() { return [{
            "method": "liItemClicked",
            "name": "liItemClicked",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": ""
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }, {
            "method": "toggleIconClicked",
            "name": "toggleIconClicked",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": ""
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }, {
            "method": "checkboxClickedEvent",
            "name": "checkboxClickedEvent",
            "bubbles": true,
            "cancelable": true,
            "composed": true,
            "docs": {
                "tags": [],
                "text": ""
            },
            "complexType": {
                "original": "any",
                "resolved": "any",
                "references": {}
            }
        }]; }
    static get methods() { return {
        "updateTreeVerticalLineHeight": {
            "complexType": {
                "signature": "() => Promise<void>",
                "parameters": [],
                "references": {
                    "Promise": {
                        "location": "global"
                    }
                },
                "return": "Promise<void>"
            },
            "docs": {
                "text": "",
                "tags": []
            }
        }
    }; }
    static get elementRef() { return "el"; }
    static get watchers() { return [{
            "propName": "downloaded",
            "methodName": "watchHandler"
        }]; }
}
