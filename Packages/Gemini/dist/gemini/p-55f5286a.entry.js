import { r as t, h as e, e as r, H as o, g as a } from "./p-eb9dc661.js";

const i = class {
  constructor(e) {
    t(this, e), 
    /*********************************
        PROPERTIES & STATE
        *********************************/
    /**
         * Wether the alert is active (visible) or hidden
         */
    this.active = !1, 
    /**
         * The amount of time the alert is visible before hidding under the document
         */
    this.activeTime = "regular", 
    /**
         * The alert position on the X axis
         */
    this.position = "start", 
    /**
         * The alert flavor
         */
    this.type = "notice", 
    /**
         * The presence of this attribute makes the component full-width
         */
    this.fullWidth = !1, 
    /**
         * The spacing between the alert, and the left or right side of the document
         */
    this.leftRight = "xs", 
    /**
         * The spacing between the alert and the bottom side of the document
         */
    this.bottom = "xs", 
    /**
         * The alert width
         */
    this.width = "350px", 
    /**
         * The presence of this attribute removes the sound on the 'warning' or 'error' alert
         */
    this.silent = !1, 
    /**
         * Reading direction
         */
    this.rtl = !1;
  }
  /*********************************
    METHODS
    *********************************/  componentDidLoad() {
    this.el.removeAttribute("hidden");
    //Reading Direction
    const t = document.getElementsByTagName("html")[0].getAttribute("dir"), e = document.getElementsByTagName("body")[0].getAttribute("dir");
    "rtl" !== t && "rtl" !== e || (this.rtl = !0);
  }
  iconColor() {
    return "notice" === this.type ? "negative" : "error" === this.type ? "error" : "warning" === this.type ? "warning" : "success" === this.type ? "success" : void 0;
  }
  closeIconColor() {
    return "notice" === this.type ? "negative" : "onbackground";
  }
  printTitle() {
    if (void 0 !== this.title) return e("h2", {
      class: "alert-message--title"
    }, this.title);
  }
  setAlertInactive() {
    this.active = !1, this.el.removeAttribute("role");
  }
  validateName(t) {
    //timing
    let e;
    switch (this.activeTime) {
     case "xxslow":
      e = "10";
      break;

     case "xslow":
      e = "09";
      break;

     case "slow":
      e = "08";
      break;

     case "regular":
      e = "07";
      break;

     case "fast":
      e = "06";
      break;

     case "xfast":
      e = "05";
      break;

     case "xxfast":
      e = "04";
      break;

     default:
      e = "04";
    }
    !0 === t && (this.el.setAttribute("role", "alert"), setTimeout(() => {
      this.setAlertInactive();
    }, parseInt(getComputedStyle(document.documentElement).getPropertyValue("--timing-" + e))));
  }
  defineWidth() {
    if (this.fullWidth) {
      const t = getComputedStyle(document.documentElement).getPropertyValue("--spacing-lay-" + this.leftRight);
      //remove "px" to multiply, since we want the spacing value from left, and from right.
            return "calc(100% - " + 2 * parseInt(t.replace("px", ""), 10) + "px)";
    }
    return this.width;
  }
  alertHidden() {
    if (this.active) {
      if (!this.silent) {
        let t;
        "warning" === this.type ? t = new Audio(r("./alert-assets/warning.mp3")) : "error" === this.type && (t = new Audio(r("./alert-assets/error.mp3"))), 
        setTimeout((function() {
          t.play();
        }), 100);
      }
      return "false";
    }
    return "true";
  }
  transform(t) {
    return "center" === this.position ? this.rtl ? "translateY(-" + t + ") translateX(50%)" : "translateY(-" + t + ") translateX(-50%)" : "translateY(-" + t + ")";
  }
  render() {
    let t, r;
    if ("no-space" === this.leftRight) t = "0"; else {
      t = getComputedStyle(document.body).getPropertyValue("--spacing-lay-" + this.leftRight).replace(/\s/g, "");
    }
    if ("no-space" === this.bottom) r = "0"; else {
      r = getComputedStyle(document.body).getPropertyValue("--spacing-lay-" + this.bottom).replace(/\s/g, "");
    }
    return e(o, {
      role: "alert",
      "aria-hidden": this.alertHidden(),
      hidden: !0,
      class: {
        rtl: this.rtl
      },
      style: {
        width: this.defineWidth(),
        left: t,
        right: t,
        transform: this.transform(r)
      }
    }, e("div", {
      class: {
        "alert-message": !0,
        "alert-message--notice": "notice" === this.type,
        "alert-message--error": "error" === this.type,
        "alert-message--warning": "warning" === this.type,
        "alert-message--success": "success" === this.type
      }
    }, e("div", {
      class: "alert-message--container"
    }, e("div", {
      class: "alert-message--icon"
    }, e("gxg-icon", {
      color: this.iconColor(),
      slot: "icon",
      type: "gemini-tools/" + this.type
    })), e("div", {
      class: "alert-message-title-description"
    }, this.printTitle(), e("p", {
      class: "alert-message--description"
    }, e("slot", null)))), e("div", {
      class: "alert-message-close"
    }, "notice" === this.type ? e("gxg-button", {
      type: "tertiary",
      icon: "gemini-tools/close",
      onClick: this.setAlertInactive.bind(this),
      negative: !0
    }) : e("gxg-button", {
      type: "tertiary",
      icon: "gemini-tools/close",
      onClick: this.setAlertInactive.bind(this),
      "always-black": !0
    }))));
  }
  static get assetsDirs() {
    return [ "alert-assets" ];
  }
  get el() {
    return a(this);
  }
  static get watchers() {
    return {
      active: [ "validateName" ]
    };
  }
};

i.style = "/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{position:fixed;bottom:0;z-index:100;-webkit-transition-property:-webkit-transform;transition-property:-webkit-transform;transition-property:transform;transition-property:transform, -webkit-transform;-webkit-transition-duration:var(--ui-animaton-speed);transition-duration:var(--ui-animaton-speed);-webkit-transition-timing-function:ease;transition-timing-function:ease}:host .alert-message{background-color:var(--gray-07);color:var(--color-on-primary);border-width:0;border-top-width:var(--border-width-lg);border-style:solid;border-color:var(--gray-06);padding:var(--spacing-comp-03);display:-ms-flexbox;display:flex;-ms-flex-align:start;align-items:flex-start;-ms-flex-pack:justify;justify-content:space-between;font-family:var(--font-family-primary);}:host .alert-message--error{border-color:var(--color-error-dark);background-color:var(--color-error-light);color:var(--color-on-secondary)}:host .alert-message--warning{border-color:var(--color-warning-dark);background-color:var(--color-warning-light);color:var(--color-on-secondary)}:host .alert-message--success{border-color:var(--color-success-dark);background-color:var(--color-success-light);color:var(--color-on-secondary)}:host .alert-message__close{display:-ms-flexbox;display:flex}:host .alert-message--title{margin:0 0 var(--spacing-comp-02) 0;font-size:var(--font-size-xs);text-transform:uppercase}:host .alert-message--icon{-webkit-margin-end:var(--spacing-comp-02);margin-inline-end:var(--spacing-comp-02);display:-ms-flexbox;display:flex}:host .alert-message--container{display:-ms-flexbox;display:flex;-ms-flex-align:top;align-items:top}:host .alert-message--description{font-size:var(--font-size-md);margin-top:0;margin-bottom:0;line-height:1.5em}:host([position=end]:not(.rtl)){left:auto !important}:host(:not([active])){-webkit-transform:translateY(100%) !important;transform:translateY(100%) !important}:host([position=center]){left:50% !important;-webkit-transform:translateX(-50%);transform:translateX(-50%)}:host(:not([active])[position=center]){-webkit-transform:translateY(100%) translateX(-50%) !important;transform:translateY(100%) translateX(-50%) !important}:host([position=center].rtl){right:50% !important;-webkit-transform:translateX(50%);transform:translateX(50%)}:host(:not([active])[position=center].rtl){-webkit-transform:translateY(100%) translateX(50%) !important;transform:translateY(100%) translateX(50%) !important}:host([position=end].rtl){right:auto !important}";

export { i as gxg_alert }