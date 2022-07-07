import { r as t, h as e, H as i, g as o } from "./p-eb9dc661.js";

const r = class {
  constructor(e) {
    t(this, e), 
    /**
         * The window starting height
         */
    this.initialHeight = "200px", 
    /**
         * The window starting width
         */
    this.initialWidth = "360px", 
    /**
         * The window min. height
         */
    this.minHeight = "200px", 
    /**
         * The window min. width
         */
    this.minWidth = "360px", 
    /**
         * The window max. height
         */
    this.maxHeight = "420px", 
    /**
         * The window max. width
         */
    this.maxWidth = "520px", 
    /**
         * The window left position. By default the window is horizontally centered
         */
    this.leftPosition = "0px", 
    /**
         * The window top position. By default the window is vertically centered
         */
    this.topPosition = "0px", 
    /**
         * The window title
         */
    this.windowTitle = "", 
    /**
         * The window title icon
         */
    this.titleIcon = "", 
    /**
         * Displays the window
         */
    this.displayWindow = !1, this.showWindow = !1, this.customPosition = !1;
  }
  dragElement(t, e) {
    let i = 0, o = 0, r = 0, n = 0;
    function dragElm(a) {
      i = r - a.clientX, o = n - a.clientY, r = a.clientX, n = a.clientY, e.style.top = e.offsetTop - o + "px", 
      e.style.left = e.offsetLeft - i + "px", e.offsetTop < 0 && (e.style.top = 0), e.offsetLeft < 0 && (e.style.left = 0), 
      e.offsetTop + e.offsetHeight > window.innerHeight && (e.style.top = window.innerHeight - e.offsetHeight + "px"), 
      e.offsetLeft + e.offsetWidth > window.innerWidth && (e.style.left = window.innerWidth - e.offsetWidth + "px"), 
      t.style.cursor = "grabbing";
    }
    function dropElm() {
      document.onmousemove = null, document.onmouseup = null, t.style.cursor = "grab";
    }
    t && (t.onmousedown = function(t) {
      r = t.clientX, n = t.clientY, document.onmousemove = dragElm, document.onmouseup = dropElm;
    });
  }
  resizeElement(t, e) {
    //resize top
    let i = 0, o = 0, r = 0;
    //drop resize element
        function dropElm() {
      document.onmousemove = null, document.onmouseup = null, document.querySelector("html").style.cursor = "default", 
      t[4].style.cursor = "default";
    }
    function dragElmTop(n) {
      e.style.top = n.clientY + "px", t[4].style.height = o - (n.clientY - r) + "px", 
      e.offsetHeight < 214 && (e.style.top = i + "px"), document.querySelector("html").style.cursor = "ns-resize", 
      t[4].style.cursor = "ns-resize";
    }
    //resize left
    let n = 0, a = 0, s = 0;
    function dragElmLeft(i) {
      e.style.left = i.clientX + "px", t[4].style.width = a - (i.clientX - s) + "px", 
      e.offsetWidth < 213 && (e.style.left = n + "px"), document.querySelector("html").style.cursor = "ew-resize", 
      t[4].style.cursor = "ew-resize";
    }
    function dragElmBottom(i) {
      t[4].style.height = i.clientY - e.offsetTop - 12 + "px", document.querySelector("html").style.cursor = "ns-resize", 
      t[4].style.cursor = "ns-resize";
    }
    function dragElmRight(i) {
      t[4].style.width = i.clientX - e.offsetLeft - 10 + "px", document.querySelector("html").style.cursor = "ew-resize", 
      t[4].style.cursor = "ew-resize";
    }
    t[0] && (t[0].onmousedown = function(n) {
      i = e.offsetTop + e.offsetHeight - 212, o = t[4].offsetHeight, r = n.clientY, document.onmousemove = dragElmTop, 
      document.onmouseup = dropElm;
    }), t[1] && (t[1].onmousedown = function(i) {
      n = e.offsetLeft + e.offsetWidth - 212, a = t[4].offsetWidth, s = i.clientX, document.onmousemove = dragElmLeft, 
      document.onmouseup = dropElm;
    }), 
    //resize bottom
    t[2] && (t[2].onmousedown = function() {
      document.onmousemove = dragElmBottom, document.onmouseup = dropElm;
    }), 
    //resize right
    t[3] && (t[3].onmousedown = function() {
      document.onmousemove = dragElmRight, document.onmouseup = dropElm;
    });
  }
  componentWillLoad() {
    "0px" !== this.leftPosition && "0px" !== this.topPosition && (this.customPosition = !0);
  }
  componentDidLoad() {
    const t = this.el.shadowRoot.querySelectorAll("#draggable-resizable-div>div");
    this.dragElement(this.header, this.draggableResizableDiv), this.resizeElement(t, this.draggableResizableDiv), 
    function(t) {
      "0px" === this.leftPosition && "0px" === this.topPosition && (t.style.left = window.innerWidth / 2 - t.offsetWidth / 2 + "px", 
      t.style.top = window.innerHeight / 2 - t.offsetHeight / 2 + "px");
    }.bind(this)(this.draggableResizableDiv);
  }
  closeWindow() {
    this.showWindow = !1, setTimeout(function() {
      this.displayWindow = !1;
    }.bind(this), 200);
  }
  displayWindowHandler() {
    !0 === this.displayWindow && setTimeout(function() {
      this.showWindow = !0;
    }.bind(this), 50);
  }
  setInitialWidth() {
    const t = parseInt(this.initialWidth.substring(0, this.initialWidth.length - 2)), e = parseInt(this.minWidth.substring(0, this.minWidth.length - 2)), i = parseInt(this.maxWidth.substring(0, this.maxWidth.length - 2));
    return t < e ? this.minWidth : t > i ? this.maxWidth : this.initialWidth;
  }
  setInitialHeight() {
    const t = parseInt(this.initialHeight.substring(0, this.initialHeight.length - 2)), e = parseInt(this.minHeight.substring(0, this.minHeight.length - 2)), i = parseInt(this.maxHeight.substring(0, this.maxHeight.length - 2));
    return t < e ? this.minHeight : t > i ? this.maxHeight : this.initialHeight;
  }
  render() {
    return e(i, {
      class: {
        display: this.displayWindow,
        visible: this.showWindow,
        "custom-position": this.customPosition
      }
    }, e("div", {
      id: "draggable-resizable-div",
      ref: t => this.draggableResizableDiv = t,
      style: {
        maxWidth: this.maxWidth,
        minWidth: this.minWidth,
        minHeight: this.minHeight,
        maxHeight: this.maxHeight,
        left: this.leftPosition,
        top: this.topPosition
      }
    }, e("div", {
      id: "top"
    }), e("div", {
      id: "left"
    }), e("div", {
      id: "bottom"
    }), e("div", {
      id: "right"
    }), e("div", {
      id: "center",
      style: {
        width: this.setInitialWidth(),
        height: this.setInitialHeight(),
        maxWidth: this.maxWidth,
        minWidth: this.minWidth,
        minHeight: this.minHeight,
        maxHeight: this.maxHeight
      }
    }, e("div", {
      id: "header",
      style: {
        cursor: "grab"
      },
      ref: t => this.header = t
    }, e("div", {
      class: {
        "title-container": !0
      }
    }, "" !== this.titleIcon ? e("gxg-icon", {
      class: "title-icon",
      color: "auto",
      size: "regular",
      type: this.titleIcon
    }) : null, "" !== this.windowTitle ? this.windowTitle : null), e("gxg-button", {
      class: {
        "delete-icon": !0
      },
      icon: "menus/delete",
      type: "tertiary",
      onClick: () => this.closeWindow()
    })), e("div", {
      id: "content"
    }, e("slot", null)))));
  }
  get el() {
    return o(this);
  }
  static get watchers() {
    return {
      displayWindow: [ "displayWindowHandler" ]
    };
  }
};

r.style = "/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{font-family:var(--font-family-primary);}:host ::-webkit-scrollbar{width:6px}:host ::-webkit-scrollbar-track{background-color:var(--gray-02);border-radius:10px}:host ::-webkit-scrollbar-thumb{background:var(--gray-05);border-radius:10px}:host ::-webkit-scrollbar-thumb:hover{background:var(--gray-04);cursor:pointer}:host .gxg-scroll{display:block;overflow-y:auto;padding-right:2px}:host #draggable-resizable-div{position:absolute;background-color:var(--gray-01);opacity:0;margin-top:30px;-webkit-transition-property:opacity, margin-top;transition-property:opacity, margin-top;-webkit-transition-duration:var(--ui-animaton-speed);transition-duration:var(--ui-animaton-speed);-webkit-transition-timing-function:ease;transition-timing-function:ease;z-index:-99}:host #top,:host #bottom{width:100%;height:5px;cursor:ns-resize}:host #left,:host #right{width:5px;height:100%;cursor:ew-resize}:host #top,:host #left{top:0;left:0}:host #bottom,:host #right{bottom:0;right:0}:host #top,:host #left,:host #bottom,:host #right{position:absolute;background:transparent}:host #header{height:var(--spacing-comp-06);background-color:var(--gray-02);font-size:var(--font-size-sm);text-transform:capitalize;display:-ms-flexbox;display:flex;-ms-flex-pack:justify;justify-content:space-between;-ms-flex-align:center;align-items:center;padding:0 var(--spacing-comp-02);color:var(--color-on-background)}:host #header .title-container{display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center}:host #header .title-container .title-icon{-webkit-margin-end:var(--spacing-comp-01);margin-inline-end:var(--spacing-comp-01)}:host #content{padding:var(--spacing-comp-03);font-size:var(--font-size-sm);padding:var(--spacing-comp-02);color:var(--color-on-background);height:calc(100% - 48px);overflow-y:auto;line-height:var(--line-height-regular)}:host(.display){display:block}:host(.visible) #draggable-resizable-div{opacity:1;margin-top:0;z-index:99}";

export { r as gxg_window }