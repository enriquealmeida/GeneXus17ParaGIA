'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

const index = require('./index-1115561c.js');

function makeDraggable(component) {
    const draggableItems = component.getDraggableElements();
    const items = Array.from(draggableItems);
    items.forEach(listItem => {
        listItem.setAttribute("draggable", "true");
    });
    let dragSrcEl;
    function dragStart(e) {
        const itemDragStartEvent = component.itemDragStart.emit();
        if (itemDragStartEvent.defaultPrevented) {
            return;
        }
        this.style.opacity = "0.3";
        dragSrcEl = this;
        e.dataTransfer.effectAllowed = "move";
        e.dataTransfer.setData("text/html", this.innerHTML);
    }
    function dragEnter() {
        const itemDragEnterEvent = component.itemDragEnter.emit(this);
        if (itemDragEnterEvent.defaultPrevented) {
            return;
        }
        this.classList.add("over");
    }
    function dragLeave(e) {
        const itemDragLeaveEvent = component.itemDragLeave.emit(this);
        if (itemDragLeaveEvent.defaultPrevented) {
            return;
        }
        e.stopPropagation();
        this.classList.remove("over");
    }
    function dragOver(e) {
        const itemDragOverEvent = component.itemDragOver.emit(this);
        if (itemDragOverEvent.defaultPrevented) {
            return;
        }
        e.preventDefault();
        e.dataTransfer.dropEffect = "move";
        return false;
    }
    function dragDrop(e) {
        const itemDropEvent = component.itemDrop.emit();
        if (itemDropEvent.defaultPrevented) {
            return;
        }
        if (dragSrcEl != this) {
            dragSrcEl.innerHTML = this.innerHTML;
            this.innerHTML = e.dataTransfer.getData("text/html");
        }
        return false;
    }
    function dragEnd() {
        items.forEach(item => item.classList.remove("over"));
        this.style.opacity = "1";
    }
    function addEventsDragAndDrop(el) {
        el.addEventListener("dragstart", dragStart, false);
        el.addEventListener("dragenter", dragEnter, false);
        el.addEventListener("dragover", dragOver, false);
        el.addEventListener("dragleave", dragLeave, false);
        el.addEventListener("drop", dragDrop, false);
        el.addEventListener("dragend", dragEnd, false);
    }
    function removeEventsDragAndDrop(el) {
        el.removeEventListener("dragstart", dragStart);
        el.removeEventListener("dragenter", dragEnter);
        el.removeEventListener("dragover", dragOver);
        el.removeEventListener("dragleave", dragLeave);
        el.removeEventListener("drop", dragDrop);
        el.removeEventListener("dragend", dragEnd);
    }
    items.forEach(item => addEventsDragAndDrop(item));
    return function cleanup() {
        items.forEach(item => removeEventsDragAndDrop(item));
    };
}

const dragContainerCss = "/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{display:block}:host ::slotted(*){will-change:transform;cursor:-webkit-grab;cursor:grab;-webkit-transition:all 200ms;transition:all 200ms;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;position:relative;width:100%;-webkit-box-sizing:border-box;box-sizing:border-box}:host ::slotted(.over){-webkit-transform:scale(1.02, 1.02);transform:scale(1.02, 1.02);cursor:pointer;-webkit-box-shadow:var(--box-shadow-03);box-shadow:var(--box-shadow-03)}:host :last-child{margin-bottom:0}:host .placeholder{display:none;height:50px;background-color:#ccc}:host .placeholder.visible{display:block}";

const GxgDragContainer = class {
    constructor(hostRef) {
        index.registerInstance(this, hostRef);
        this.itemDragStart = index.createEvent(this, "itemDragStart", 7);
        this.itemDrop = index.createEvent(this, "itemDrop", 7);
        this.itemDragOver = index.createEvent(this, "itemDragOver", 7);
        this.itemDragLeave = index.createEvent(this, "itemDragLeave", 7);
        this.itemDragEnter = index.createEvent(this, "itemDragEnter", 7);
        /**
         * The presence of this attribute adds a "delete" button to each gxg-drag-box. When pressed, the "deleted" event is emmited.
         */
        this.deletable = false;
        /**
         * The max-width of the box container
         */
        this.maxWidth = "100%";
        /**
         * The padding (internal spacing) of the gxg-drag-boxes
         */
        this.padding = undefined;
    }
    clickedHandler(event) {
        const boxes = this.el.querySelectorAll("*");
        boxes.forEach((item) => {
            if (event.detail === item.getAttribute("id")) {
                item.setAttribute("active", "active");
            }
            else {
                item.removeAttribute("active");
            }
        });
    }
    getDraggableElements() {
        return this.el.querySelectorAll("gxg-drag-box");
    }
    componentDidLoad() {
        this.dndCleanup = makeDraggable(this);
        //Set padding to each of the drag-boxes
        const dragBoxes = this.el.querySelectorAll("gxg-drag-box");
        dragBoxes.forEach((dragBox) => {
            if (this.padding !== undefined) {
                dragBox.setAttribute("padding", this.padding);
            }
        });
        //Deletable button for each of the child items
        if (this.deletable) {
            const items = this.el.querySelectorAll("*");
            items.forEach((item) => {
                item.setAttribute("deletable", "deletable");
            });
        }
    }
    disconnectedCallback() {
        this.dndCleanup();
    }
    render() {
        return (index.h(index.Host, { style: { maxWidth: this.maxWidth } }, index.h("slot", null), index.h("div", { class: "placeholder" })));
    }
    get el() { return index.getElement(this); }
};
GxgDragContainer.style = dragContainerCss;

exports.gxg_drag_container = GxgDragContainer;
