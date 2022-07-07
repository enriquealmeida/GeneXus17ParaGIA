import { Component, Host, h, Element, Prop, State, Watch } from "@stencil/core";
export class GxgWindow {
    constructor() {
        /**
         * The window starting height
         */
        this.initialHeight = "200px";
        /**
         * The window starting width
         */
        this.initialWidth = "360px";
        /**
         * The window min. height
         */
        this.minHeight = "200px";
        /**
         * The window min. width
         */
        this.minWidth = "360px";
        /**
         * The window max. height
         */
        this.maxHeight = "420px";
        /**
         * The window max. width
         */
        this.maxWidth = "520px";
        /**
         * The window left position. By default the window is horizontally centered
         */
        this.leftPosition = "0px";
        /**
         * The window top position. By default the window is vertically centered
         */
        this.topPosition = "0px";
        /**
         * The window title
         */
        this.windowTitle = "";
        /**
         * The window title icon
         */
        this.titleIcon = "";
        /**
         * Displays the window
         */
        this.displayWindow = false;
        this.showWindow = false;
        this.customPosition = false;
    }
    dragElement(elm, mainElm) {
        let p1 = 0, p2 = 0, p3 = 0, p4 = 0;
        function dragElm(e) {
            p1 = p3 - e.clientX;
            p2 = p4 - e.clientY;
            p3 = e.clientX;
            p4 = e.clientY;
            mainElm.style.top = mainElm.offsetTop - p2 + "px";
            mainElm.style.left = mainElm.offsetLeft - p1 + "px";
            if (mainElm.offsetTop < 0) {
                mainElm.style.top = 0;
            }
            if (mainElm.offsetLeft < 0) {
                mainElm.style.left = 0;
            }
            if (mainElm.offsetTop + mainElm.offsetHeight > window.innerHeight) {
                mainElm.style.top = window.innerHeight - mainElm.offsetHeight + "px";
            }
            if (mainElm.offsetLeft + mainElm.offsetWidth > window.innerWidth) {
                mainElm.style.left = window.innerWidth - mainElm.offsetWidth + "px";
            }
            elm.style.cursor = "grabbing";
        }
        function dropElm() {
            document.onmousemove = null;
            document.onmouseup = null;
            elm.style.cursor = "grab";
        }
        function dragMouseDown(e) {
            p3 = e.clientX;
            p4 = e.clientY;
            document.onmousemove = dragElm;
            document.onmouseup = dropElm;
        }
        if (elm) {
            elm.onmousedown = dragMouseDown;
        }
    }
    resizeElement(elm, mainElm) {
        //resize top
        let oldT = 0, oldH = 0, oldCY = 0;
        //drop resize element
        function dropElm() {
            document.onmousemove = null;
            document.onmouseup = null;
            document.querySelector("html").style.cursor = "default";
            elm[4].style.cursor = "default";
        }
        function dragElmTop(e) {
            mainElm.style.top = e.clientY + "px";
            elm[4].style.height = oldH - (e.clientY - oldCY) + "px";
            if (mainElm.offsetHeight < 214) {
                mainElm.style.top = oldT + "px";
            }
            document.querySelector("html").style.cursor = "ns-resize";
            elm[4].style.cursor = "ns-resize";
        }
        function dragMouseDownTop(e) {
            oldT = mainElm.offsetTop + mainElm.offsetHeight - 212;
            oldH = elm[4].offsetHeight;
            oldCY = e.clientY;
            document.onmousemove = dragElmTop;
            document.onmouseup = dropElm;
        }
        //resize left
        let oldL = 0, oldW = 0, oldCX = 0;
        function dragElmLeft(e) {
            mainElm.style.left = e.clientX + "px";
            elm[4].style.width = oldW - (e.clientX - oldCX) + "px";
            if (mainElm.offsetWidth < 213) {
                mainElm.style.left = oldL + "px";
            }
            document.querySelector("html").style.cursor = "ew-resize";
            elm[4].style.cursor = "ew-resize";
        }
        function dragMouseDownLeft(e) {
            oldL = mainElm.offsetLeft + mainElm.offsetWidth - 212;
            oldW = elm[4].offsetWidth;
            oldCX = e.clientX;
            document.onmousemove = dragElmLeft;
            document.onmouseup = dropElm;
        }
        if (elm[0]) {
            elm[0].onmousedown = dragMouseDownTop;
        }
        if (elm[1]) {
            elm[1].onmousedown = dragMouseDownLeft;
        }
        function dragElmBottom(e) {
            elm[4].style.height = e.clientY - mainElm.offsetTop - 12 + "px";
            document.querySelector("html").style.cursor = "ns-resize";
            elm[4].style.cursor = "ns-resize";
        }
        function dragMouseDownBottom() {
            document.onmousemove = dragElmBottom;
            document.onmouseup = dropElm;
        }
        //resize bottom
        if (elm[2]) {
            elm[2].onmousedown = dragMouseDownBottom;
        }
        function dragElmRight(e) {
            elm[4].style.width = e.clientX - mainElm.offsetLeft - 10 + "px";
            document.querySelector("html").style.cursor = "ew-resize";
            elm[4].style.cursor = "ew-resize";
        }
        function dragMouseDownRight() {
            document.onmousemove = dragElmRight;
            document.onmouseup = dropElm;
        }
        //resize right
        if (elm[3]) {
            elm[3].onmousedown = dragMouseDownRight;
        }
    }
    componentWillLoad() {
        if (this.leftPosition !== "0px" && this.topPosition !== "0px") {
            this.customPosition = true;
        }
    }
    componentDidLoad() {
        const childrenDivs = this.el.shadowRoot.querySelectorAll("#draggable-resizable-div>div");
        this.dragElement(this.header, this.draggableResizableDiv);
        this.resizeElement(childrenDivs, this.draggableResizableDiv);
        (function (elm) {
            if (this.leftPosition === "0px" && this.topPosition === "0px") {
                elm.style.left = window.innerWidth / 2 - elm.offsetWidth / 2 + "px";
                elm.style.top = window.innerHeight / 2 - elm.offsetHeight / 2 + "px";
            }
        }.bind(this)(this.draggableResizableDiv));
    }
    closeWindow() {
        this.showWindow = false;
        setTimeout(function () {
            this.displayWindow = false;
        }.bind(this), 200);
    }
    displayWindowHandler() {
        if (this.displayWindow === true) {
            setTimeout(function () {
                this.showWindow = true;
            }.bind(this), 50);
        }
    }
    setInitialWidth() {
        const initialWidthInt = parseInt(this.initialWidth.substring(0, this.initialWidth.length - 2));
        const minWidthInt = parseInt(this.minWidth.substring(0, this.minWidth.length - 2));
        const maxWidthInt = parseInt(this.maxWidth.substring(0, this.maxWidth.length - 2));
        if (initialWidthInt < minWidthInt) {
            return this.minWidth;
        }
        else if (initialWidthInt > maxWidthInt) {
            return this.maxWidth;
        }
        else {
            return this.initialWidth;
        }
    }
    setInitialHeight() {
        const initialHeightInt = parseInt(this.initialHeight.substring(0, this.initialHeight.length - 2));
        const minHeightInt = parseInt(this.minHeight.substring(0, this.minHeight.length - 2));
        const maxHeightInt = parseInt(this.maxHeight.substring(0, this.maxHeight.length - 2));
        if (initialHeightInt < minHeightInt) {
            return this.minHeight;
        }
        else if (initialHeightInt > maxHeightInt) {
            return this.maxHeight;
        }
        else {
            return this.initialHeight;
        }
    }
    render() {
        return (h(Host, { class: {
                display: this.displayWindow,
                visible: this.showWindow,
                "custom-position": this.customPosition,
            } },
            h("div", { id: "draggable-resizable-div", ref: (el) => (this.draggableResizableDiv = el), style: {
                    maxWidth: this.maxWidth,
                    minWidth: this.minWidth,
                    minHeight: this.minHeight,
                    maxHeight: this.maxHeight,
                    left: this.leftPosition,
                    top: this.topPosition,
                } },
                h("div", { id: "top" }),
                h("div", { id: "left" }),
                h("div", { id: "bottom" }),
                h("div", { id: "right" }),
                h("div", { id: "center", style: {
                        width: this.setInitialWidth(),
                        height: this.setInitialHeight(),
                        maxWidth: this.maxWidth,
                        minWidth: this.minWidth,
                        minHeight: this.minHeight,
                        maxHeight: this.maxHeight,
                    } },
                    h("div", { id: "header", style: { cursor: "grab" }, ref: (el) => (this.header = el) },
                        h("div", { class: { "title-container": true } },
                            this.titleIcon !== "" ? (h("gxg-icon", { class: "title-icon", color: "auto", size: "regular", type: this.titleIcon })) : null,
                            this.windowTitle !== "" ? this.windowTitle : null),
                        h("gxg-button", { class: { "delete-icon": true }, icon: "menus/delete", type: "tertiary", onClick: () => this.closeWindow() })),
                    h("div", { id: "content" },
                        h("slot", null))))));
    }
    static get is() { return "gxg-window"; }
    static get encapsulation() { return "shadow"; }
    static get originalStyleUrls() { return {
        "$": ["window.scss"]
    }; }
    static get styleUrls() { return {
        "$": ["window.css"]
    }; }
    static get properties() { return {
        "initialHeight": {
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
                "text": "The window starting height"
            },
            "attribute": "initial-height",
            "reflect": false,
            "defaultValue": "\"200px\""
        },
        "initialWidth": {
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
                "text": "The window starting width"
            },
            "attribute": "initial-width",
            "reflect": false,
            "defaultValue": "\"360px\""
        },
        "minHeight": {
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
                "text": "The window min. height"
            },
            "attribute": "min-height",
            "reflect": false,
            "defaultValue": "\"200px\""
        },
        "minWidth": {
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
                "text": "The window min. width"
            },
            "attribute": "min-width",
            "reflect": false,
            "defaultValue": "\"360px\""
        },
        "maxHeight": {
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
                "text": "The window max. height"
            },
            "attribute": "max-height",
            "reflect": false,
            "defaultValue": "\"420px\""
        },
        "maxWidth": {
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
                "text": "The window max. width"
            },
            "attribute": "max-width",
            "reflect": false,
            "defaultValue": "\"520px\""
        },
        "leftPosition": {
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
                "text": "The window left position. By default the window is horizontally centered"
            },
            "attribute": "left-position",
            "reflect": false,
            "defaultValue": "\"0px\""
        },
        "topPosition": {
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
                "text": "The window top position. By default the window is vertically centered"
            },
            "attribute": "top-position",
            "reflect": false,
            "defaultValue": "\"0px\""
        },
        "windowTitle": {
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
                "text": "The window title"
            },
            "attribute": "window-title",
            "reflect": false,
            "defaultValue": "\"\""
        },
        "titleIcon": {
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
                "text": "The window title icon"
            },
            "attribute": "title-icon",
            "reflect": false,
            "defaultValue": "\"\""
        },
        "displayWindow": {
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
                "text": "Displays the window"
            },
            "attribute": "display-window",
            "reflect": false,
            "defaultValue": "false"
        }
    }; }
    static get states() { return {
        "showWindow": {},
        "customPosition": {}
    }; }
    static get elementRef() { return "el"; }
    static get watchers() { return [{
            "propName": "displayWindow",
            "methodName": "displayWindowHandler"
        }]; }
}
