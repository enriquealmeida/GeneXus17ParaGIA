import { r as registerInstance, c as createEvent, h, H as Host, g as getElement } from './index-09b1517f.js';

const treeItemCss = "/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{display:block}:host li{list-style:none;font-family:var(--font-family-primary);font-size:var(--font-size-sm);font-weight:var(--font-weight-regular);color:var(--color-on-background);height:20px;overflow:hidden}:host li.tree-open{height:auto}:host li .li-text{height:100%;padding:0 var(--spacing-comp-02);cursor:pointer;display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;position:relative}:host li .li-text:hover{background-color:var(--gray-01)}:host li .li-text:active{background-color:var(--gray-02)}:host li .li-text:focus{outline:none;-webkit-box-shadow:inset 0px 0px 0px 2px var(--color-primary-active);box-shadow:inset 0px 0px 0px 2px var(--color-primary-active)}:host li .li-text--selected{background-color:var(--color-secondary-enabled)}:host li .li-text--selected:hover{background-color:var(--color-secondary-enabled)}:host li .vertical-line{position:absolute;width:1px;height:0;top:21px;left:15px;z-index:1;background-image:-webkit-gradient(linear, left top, left bottom, color-stop(28%, var(--gray-04)), color-stop(0%, rgba(255, 255, 255, 0)));background-image:linear-gradient(var(--gray-04) 28%, rgba(255, 255, 255, 0) 0%);background-position:right;background-size:1px 4px;background-repeat:repeat-y}:host li .horizontal-line{position:absolute;width:16px;height:1px;top:11px;z-index:1;margin-left:-23px;top:10px;background-image:-webkit-gradient(linear, left top, right top, color-stop(33%, var(--gray-04)), color-stop(0%, rgba(255, 255, 255, 0)));background-image:linear-gradient(to right, var(--gray-04) 33%, rgba(255, 255, 255, 0) 0%);background-position:bottom;background-size:3px 1px;background-repeat:repeat-x}:host li .horizontal-line.display-none{display:none}:host li .closed-opened-icons{position:relative;display:-ms-flexbox;display:flex;margin-left:-2px}:host li .toggle-icon{position:relative;z-index:2;-webkit-transition-property:opacity;transition-property:opacity;-webkit-transition-duration:0.5s;transition-duration:0.5s;-webkit-transition-timing-function:ease;transition-timing-function:ease}:host .checkbox{margin-right:var(--spacing-comp-01)}:host .right-icon{-webkit-margin-start:auto;margin-inline-start:auto;position:relative;top:0;-webkit-transition-property:top;transition-property:top;-webkit-transition-duration:0.5s;transition-duration:0.5s;-webkit-transition-timing-function:ease;transition-timing-function:ease}:host .loading{opacity:1;display:inline;right:11px;top:-20px;position:absolute;z-index:1;-webkit-transition-property:all;transition-property:all;-webkit-transition-duration:0.5s;transition-duration:0.5s;-webkit-transition-timing-function:ease;transition-timing-function:ease;border-radius:50%;width:8px;height:8px;border:0.18rem solid var(--gray-04);border-top-color:var(--gray-01);-webkit-animation:spin 1s infinite linear;animation:spin 1s infinite linear}:host .loading--double{border-style:double;border-width:0.5rem}@-webkit-keyframes spin{0%{-webkit-transform:rotate(0deg);transform:rotate(0deg)}100%{-webkit-transform:rotate(360deg);transform:rotate(360deg)}}@keyframes spin{0%{-webkit-transform:rotate(0deg);transform:rotate(0deg)}100%{-webkit-transform:rotate(360deg);transform:rotate(360deg)}}:host([disabled]) li .text{color:var(--gray-05)}:host([disabled]) .toggle-icon{pointer-events:none}:host([downloading]){pointer-events:none}:host([downloading]) .right-icon{top:20px}:host([downloading]) .loading{top:3px}:host([downloaded]) .loading,:host([downloaded]) .right-icon{opacity:0}";

const GxgTreeItem = class {
    constructor(hostRef) {
        registerInstance(this, hostRef);
        this.liItemClicked = createEvent(this, "liItemClicked", 7);
        this.toggleIconClicked = createEvent(this, "toggleIconClicked", 7);
        this.checkboxClickedEvent = createEvent(this, "checkboxClickedEvent", 7);
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
        return (h(Host, { class: { leaf: this.isLeaf, "not-leaf": !this.isLeaf } }, h("li", { class: {
                "tree-open": this.opened,
                disabled: this.disabled,
            } }, h("div", { class: {
                "li-text": true,
                "li-text--not-leaf": !this.isLeaf,
                "li-text--leaf": this.isLeaf,
                "li-text--first-tree-item": this.firstTreeItem,
                "li-text--has-child-tree": this.hasChildTree,
                "li-text--selected": this.selected,
            }, style: { paddingLeft: this.returnPaddingLeft() }, onClick: this.liTextClicked.bind(this), onDblClick: this.liTextDoubleClicked.bind(this), onKeyDown: this.liTextKeyDownPressed.bind(this), tabIndex: this.liTextTabIndex() }, !this.isLeaf || this.download
            ? [
                h("span", { class: { "vertical-line": true }, style: {
                        //height: this.verticalLineHeight,
                        height: this.numberOfDirectTreeItemsDescendants * 20 -
                            10 +
                            "px",
                        left: this.returnVerticalLineLeftPosition(),
                    } }),
                h("div", { class: { "closed-opened-icons": true } }, h("gxg-icon", { type: this.returnToggleIconType(), size: "small", onClick: this.toggleTreeIconClicked.bind(this), class: "toggle-icon" })),
            ]
            : null, h("span", { class: {
                "horizontal-line": true,
                "display-none": this.numberOfParentTrees === 1,
            }, style: {
                left: this.itemPaddingLeft + "px",
            } }), this.checkbox ? (h("gxg-form-checkbox", { checked: this.checked, class: { checkbox: true }, tabIndex: this.checkboxTabIndex(), indeterminate: this.setIndeterminate(), disabled: this.disabled, onClick: this.checkboxClicked.bind(this) })) : null, this.leftIcon ? (h("gxg-icon", { size: "small", type: this.leftIcon, color: this.disabled ? "disabled" : "auto" })) : null, h("span", { class: "text" }, h("slot", null)), this.rightIcon ? (h("gxg-icon", { size: "small", type: this.rightIcon, color: this.rightIconColor, class: { "right-icon": true } })) : null, this.download ? h("span", { class: { loading: true } }) : null), h("slot", { name: "tree" }))));
    }
    get el() { return getElement(this); }
    static get watchers() { return {
        "downloaded": ["watchHandler"]
    }; }
};
GxgTreeItem.style = treeItemCss;

export { GxgTreeItem as gxg_tree_item };
