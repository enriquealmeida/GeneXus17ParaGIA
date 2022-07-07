var __awaiter = this && this.__awaiter || function(e, t, i, o) {
  return new (i || (i = Promise))((function(r, n) {
    function fulfilled(e) {
      try {
        step(o.next(e));
      } catch (t) {
        n(t);
      }
    }
    function rejected(e) {
      try {
        step(o.throw(e));
      } catch (t) {
        n(t);
      }
    }
    function step(e) {
      var t;
      e.done ? r(e.value) : (t = e.value, t instanceof i ? t : new i((function(e) {
        e(t);
      }))).then(fulfilled, rejected);
    }
    step((o = o.apply(e, t || [])).next());
  }));
}, __generator = this && this.__generator || function(e, t) {
  var i, o, r, n, a = {
    label: 0,
    sent: function() {
      if (1 & r[0]) throw r[1];
      return r[1];
    },
    trys: [],
    ops: []
  };
  return n = {
    next: verb(0),
    throw: verb(1),
    return: verb(2)
  }, "function" == typeof Symbol && (n[Symbol.iterator] = function() {
    return this;
  }), n;
  function verb(n) {
    return function(l) {
      return function(n) {
        if (i) throw new TypeError("Generator is already executing.");
        for (;a; ) try {
          if (i = 1, o && (r = 2 & n[0] ? o.return : n[0] ? o.throw || ((r = o.return) && r.call(o), 
          0) : o.next) && !(r = r.call(o, n[1])).done) return r;
          switch (o = 0, r && (n = [ 2 & n[0], r.value ]), n[0]) {
           case 0:
           case 1:
            r = n;
            break;

           case 4:
            return a.label++, {
              value: n[1],
              done: !1
            };

           case 5:
            a.label++, o = n[1], n = [ 0 ];
            continue;

           case 7:
            n = a.ops.pop(), a.trys.pop();
            continue;

           default:
            if (!(r = a.trys, (r = r.length > 0 && r[r.length - 1]) || 6 !== n[0] && 2 !== n[0])) {
              a = 0;
              continue;
            }
            if (3 === n[0] && (!r || n[1] > r[0] && n[1] < r[3])) {
              a.label = n[1];
              break;
            }
            if (6 === n[0] && a.label < r[1]) {
              a.label = r[1], r = n;
              break;
            }
            if (r && a.label < r[2]) {
              a.label = r[2], a.ops.push(n);
              break;
            }
            r[2] && a.ops.pop(), a.trys.pop();
            continue;
          }
          n = t.call(e, a);
        } catch (l) {
          n = [ 6, l ], o = 0;
        } finally {
          i = r = 0;
        }
        if (5 & n[0]) throw n[1];
        return {
          value: n[0] ? n[1] : void 0,
          done: !0
        };
      }([ n, l ]);
    };
  }
};

System.register([ "./p-d8a20c31.system.js" ], (function(e) {
  "use strict";
  var t, i, o, r, n;
  return {
    setters: [ function(e) {
      t = e.r, i = e.c, o = e.h, r = e.H, n = e.g;
    } ],
    execute: function() {
      e("gxg_tree_item", /** @class */ function() {
        function class_1(e) {
          t(this, e), this.liItemClicked = i(this, "liItemClicked", 7), this.toggleIconClicked = i(this, "toggleIconClicked", 7), 
          this.checkboxClickedEvent = i(this, "checkboxClickedEvent", 7), 
          //PROPS
          /**
                     * Set this attribute if you want the gxg-treeitem to display a checkbox
                     */
          this.checkbox = !1, 
          /**
                     * Set this attribute if you want the gxg-treeitem checkbox to be checked by default
                     */
          this.checked = !1, 
          /**
                     * Set this attribute if this tree-item has a resource to be downloaded;
                     */
          this.download = !1, 
          /**
                     * Set this attribute when you are downloading a resource
                     */
          this.downloading = !1, 
          /**
                     * Set this attribute when you have downloaded the resource
                     */
          this.downloaded = !1, 
          /**
                     * If this tree-item has a nested tree, set this attribute to make the tree open by default
                     */
          this.opened = !1, 
          /**
                     * The presence of this attribute sets the tree-item as selected
                     */
          this.selected = !1, 
          /**
                     * The presence of this attribute displays a +/- icon to toggle/untoggle the tree
                     */
          this.isLeaf = void 0, 
          //PROPS
          this.hasChildTree = !1, this.firstTreeItem = !1, this.disabled = !1, 
          //STATE
          this.numberOfParentTrees = 1, 
          //@State() verticalLineHeight: string;
          this.horizontalLinePaddingLeft = 0, this.lastTreeItem = !1, this.firstTreeItemOfParentTree = !1, 
          this.lastTreeItemOfParentTree = !1, this.rightIconColor = "auto", this.numberOfDirectTreeItemsDescendants = 0;
        }
        return class_1.prototype.componentWillLoad = function() {
          //Count number of parent trees in order to set the apporpiate padding-left
          this.numberOfParentTrees = this.getParents(this.el);
          //If tree item has not a tree-item inside, is leaf
          var e = this.el.querySelector('[slot="tree"]');
          //If is first item of parent Tree
          (void 0 === this.isLeaf && (null === e ? this.isLeaf = !0 : this.hasChildTree = !0), 
          null === this.el.previousElementSibling && (this.firstTreeItem = !0), null === this.el.nextElementSibling && (this.lastTreeItem = !0), 
          1 === this.numberOfParentTrees) && (null === this.el.previousElementSibling && (this.firstTreeItemOfParentTree = !0));
          //If is last item of parent Tree
                    1 === this.numberOfParentTrees && (null === this.el.nextElementSibling && (this.lastTreeItemOfParentTree = !0));
          //Set right icon color
                    this.download && this.rightIcon.includes("download") ? this.rightIconColor = "primary-enabled" : this.disabled && (this.rightIconColor = "disabled"), 
          //If this tree item has a source to download, this item has child tree, and is not leaf. Also, set the tree as not open
          this.download && (this.hasChildTree = !0, this.isLeaf = !1, this.opened = !1);
        }, class_1.prototype.getNumberOfVisibleDescendants = function() {
          var e = this.el.querySelector(":scope > gxg-tree");
          if (null !== e && this.opened) {
            //if tree item has a tree inside and is open...
            var t = e.querySelectorAll("gxg-tree-item.visible"), i = e.querySelectorAll(":scope > gxg-tree-item.visible"), o = i[i.length - 1], r = 0;
            //direct descendants
                        if (o.opened) r = o.querySelector(":scope > gxg-tree").querySelectorAll(":scope > gxg-tree-item").length;
            t.length > 0 && (this.numberOfDirectTreeItemsDescendants = t.length - r);
          }
        }, class_1.prototype.setVisibleTreeItems = function() {
          var e = this.el.querySelector(":scope > gxg-tree");
          if (null !== e) {
            var t = e.querySelectorAll(":scope > gxg-tree-item");
            this.opened ? t.forEach((function(e) {
              e.classList.remove("not-visible"), e.classList.add("visible");
            })) : t.forEach((function(e) {
              e.classList.remove("visible"), e.classList.add("not-visible");
            }));
          }
        }, class_1.prototype.componentDidLoad = function() {
          this.setVisibleTreeItems(), this.getNumberOfVisibleDescendants();
        }, class_1.prototype.watchHandler = function(e) {}, class_1.prototype.getParents = function(e) {
          // Push each parent element to the array
          for (
          // Returns the number of parent tree items in order to set the appropriate paddding-left
          // Set up a parent array
          var t = []; e && e !== document; e = e.parentNode) {
            "GXG-TREE" === e.tagName && t.push(e);
          }
          return t.length;
        }, class_1.prototype.toggleTreeIconClicked = function() {
          this.opened ? this.opened = !1 : this.opened = !0, this.setVisibleTreeItems(), this.toggleIconClicked.emit();
        }, class_1.prototype.updateTreeVerticalLineHeight = function() {
          return __awaiter(this, void 0, void 0, (function() {
            return __generator(this, (function(e) {
              return this.getNumberOfVisibleDescendants(), [ 2 /*return*/ ];
            }));
          }));
        }, class_1.prototype.liTextClicked = function() {
          this.liItemClicked.emit(), this.selected = !0;
        }, class_1.prototype.liTextDoubleClicked = function() {
          this.toggleTreeIconClicked();
        }, class_1.prototype.liTextKeyDownPressed = function(e) {
          //LEFT/RIGHT NAVIGATION
          if ("ArrowDown" !== e.key && "ArrowUp" !== e.key || e.preventDefault(), 
          //ENTER
          "Enter" === e.key && (
          //Enter should check/uncheck the checkbox (if present)
          this.checkboxClicked(), this.download && 
          //If the item has a resource to be downloaded, download.
          this.el.click()), "ArrowRight" === e.key && !this.isLeaf) {
            //Toggle the tree
            if (this.opened) this.el.querySelector("gxg-tree").querySelector("gxg-tree-item").shadowRoot.querySelector(".li-text").focus(); else this.opened = !0;
            this.setVisibleTreeItems(), this.toggleIconClicked.emit();
          }
          if ("ArrowLeft" === e.key) {
            var t = !1, i = this.el.parentElement.parentElement, o = null;
            if ("GXG-TREE-ITEM" === i.tagName && (t = !0, o = i.shadowRoot.querySelector(".li-text")), 
            this.isLeaf) t && o.focus(); else this.el.shadowRoot.querySelector("li").classList.contains("tree-open") ? this.opened = !1 : t && o.focus();
            this.setVisibleTreeItems(), this.toggleIconClicked.emit();
          }
          // UP/DOWN NAVIGATION
                    if (("ArrowUp" === e.key || "Tab" === e.key && e.shiftKey) && (e.preventDefault(), 
          !this.firstTreeItemOfParentTree)) {
            //Is not the first element of the parent
            //Set focus on the prev item
            var r = void 0, n = this.el.previousElementSibling;
            if (e.shiftKey && "Tab" !== e.key) 
            //if shift key was pressed, navigate to the previous sibling
            null !== n && (r = n.shadowRoot.querySelector("li .li-text")); else if (null === n) r = this.el.parentElement.parentElement.shadowRoot.querySelector("li .li-text"); else if (r = n.shadowRoot.querySelector("li .li-text"), 
            null !== n) if (n.hasChildTree) if (n.opened && !this.download) {
              //If preceding tree-item tree is opened, then the prev item is the last item of that tree
              var a = this.el.previousElementSibling.querySelector("gxg-tree");
              r = a.lastElementChild.hasChildTree && a.lastElementChild.shadowRoot.querySelector("li").classList.contains("tree-open") ? a.lastElementChild.querySelector("gxg-tree").lastElementChild.shadowRoot.querySelector("li .li-text") : a.lastElementChild.shadowRoot.querySelector("li .li-text");
              
                        } else 
            //The preciding item has a tree, but it is closed
            r = this.el.previousElementSibling.shadowRoot.querySelector("li .li-text");
            null != r && r.focus();
          }
          if ("ArrowDown" === e.key || "Tab" === e.key && !e.shiftKey) if (e.preventDefault(), 
          this.lastTreeItemOfParentTree) {
            //Last element of parent tree
            if (this.el.classList.contains("not-leaf") && this.el.shadowRoot.querySelector("li").classList.contains("tree-open")) this.el.firstElementChild.firstElementChild.shadowRoot.querySelector("li .li-text").focus();
          } else {
            //Set focus on the next item
            var l = void 0;
            if (e.shiftKey) 
            //if shift key was pressed, navigate to the next sibling
            null !== this.el.nextElementSibling && (l = this.el.nextElementSibling.shadowRoot.querySelector("li .li-text")); else if (this.lastTreeItem) if (this.hasChildTree && this.opened) l = this.el.firstElementChild.firstElementChild.shadowRoot.querySelector(".li-text"); else {
              var s = this.el.parentElement.parentElement, c = s.nextElementSibling;
              null === c ? null !== s.parentElement.parentElement.nextElementSibling && (l = s.parentElement.parentElement.nextElementSibling.shadowRoot.querySelector(".li-text")) : l = c.shadowRoot.querySelector(".li-text");
            } else l = this.hasChildTree && this.opened && !this.download ? this.el.querySelector("gxg-tree gxg-tree-item").shadowRoot.querySelector("li .li-text") : this.el.nextElementSibling.shadowRoot.querySelector(".li-text");
            null != l && l.focus();
          }
        }, class_1.prototype.returnToggleIconType = function() {
          //Returns the type of icon : gemini-tools/add or gemini-tools/minus
          return !this.opened || this.download ? "gemini-tools/add" : "gemini-tools/minus";
        }, class_1.prototype.returnPaddingLeft = function() {
          //returns the appropriate padding left to the .li-text element
          var e = 0;
          return e = 1 !== this.numberOfParentTrees ? 31 * (this.numberOfParentTrees - 1) + 5 : 5, 
          this.itemPaddingLeft = e, e + "px";
        }, class_1.prototype.returnVerticalLineLeftPosition = function() {
          //Returns the left position of the vertical line that associates the chid-items with the parent item
          return this.numberOfParentTrees, this.itemPaddingLeft + 5 + "px";
        }, class_1.prototype.checkboxTabIndex = function() {
          return -1;
        }, class_1.prototype.liTextTabIndex = function() {
          return 1;
        }, class_1.prototype.setIndeterminate = function() {
          return !!this.indeterminate;
        }, class_1.prototype.checkboxClicked = function() {
          this.checkbox && (this.checked ? (this.checked = !1, this.toggleTreeItemsCheckboxes(!1), 
          this.checkboxClickedEvent.emit(!1)) : (this.checked = !0, this.toggleTreeItemsCheckboxes(!0), 
          this.checkboxClickedEvent.emit(!0)));
        }, class_1.prototype.toggleTreeItemsCheckboxes = function(e) {
          if (this.el.parentElement.toggleCheckboxes) {
            this.indeterminate = !1;
            var t = this.el.querySelector("gxg-tree");
            if (null !== t) t.querySelectorAll("gxg-tree-item").forEach((function(t) {
              t.checked = !!e;
            }));
          }
        }, class_1.prototype.render = function() {
          return o(r, {
            class: {
              leaf: this.isLeaf,
              "not-leaf": !this.isLeaf
            }
          }, o("li", {
            class: {
              "tree-open": this.opened,
              disabled: this.disabled
            }
          }, o("div", {
            class: {
              "li-text": !0,
              "li-text--not-leaf": !this.isLeaf,
              "li-text--leaf": this.isLeaf,
              "li-text--first-tree-item": this.firstTreeItem,
              "li-text--has-child-tree": this.hasChildTree,
              "li-text--selected": this.selected
            },
            style: {
              paddingLeft: this.returnPaddingLeft()
            },
            onClick: this.liTextClicked.bind(this),
            onDblClick: this.liTextDoubleClicked.bind(this),
            onKeyDown: this.liTextKeyDownPressed.bind(this),
            tabIndex: this.liTextTabIndex()
          }, !this.isLeaf || this.download ? [ o("span", {
            class: {
              "vertical-line": !0
            },
            style: {
              //height: this.verticalLineHeight,
              height: 20 * this.numberOfDirectTreeItemsDescendants - 10 + "px",
              left: this.returnVerticalLineLeftPosition()
            }
          }), o("div", {
            class: {
              "closed-opened-icons": !0
            }
          }, o("gxg-icon", {
            type: this.returnToggleIconType(),
            size: "small",
            onClick: this.toggleTreeIconClicked.bind(this),
            class: "toggle-icon"
          })) ] : null, o("span", {
            class: {
              "horizontal-line": !0,
              "display-none": 1 === this.numberOfParentTrees
            },
            style: {
              left: this.itemPaddingLeft + "px"
            }
          }), this.checkbox ? o("gxg-form-checkbox", {
            checked: this.checked,
            class: {
              checkbox: !0
            },
            tabIndex: this.checkboxTabIndex(),
            indeterminate: this.setIndeterminate(),
            disabled: this.disabled,
            onClick: this.checkboxClicked.bind(this)
          }) : null, this.leftIcon ? o("gxg-icon", {
            size: "small",
            type: this.leftIcon,
            color: this.disabled ? "disabled" : "auto"
          }) : null, o("span", {
            class: "text"
          }, o("slot", null)), this.rightIcon ? o("gxg-icon", {
            size: "small",
            type: this.rightIcon,
            color: this.rightIconColor,
            class: {
              "right-icon": !0
            }
          }) : null, this.download ? o("span", {
            class: {
              loading: !0
            }
          }) : null), o("slot", {
            name: "tree"
          })));
        }, Object.defineProperty(class_1.prototype, "el", {
          get: function() {
            return n(this);
          },
          enumerable: !1,
          configurable: !0
        }), Object.defineProperty(class_1, "watchers", {
          get: function() {
            return {
              downloaded: [ "watchHandler" ]
            };
          },
          enumerable: !1,
          configurable: !0
        }), class_1;
      }()).style = "/*! normalize.css v8.0.1 | MIT License | github.com/necolas/normalize.css */html{line-height:1.15;-webkit-text-size-adjust:100%;}body{margin:0}main{display:block}h1{font-size:2em;margin:0.67em 0}hr{-webkit-box-sizing:content-box;box-sizing:content-box;height:0;overflow:visible;}pre{font-family:monospace, monospace;font-size:1em;}a{background-color:transparent}abbr[title]{border-bottom:none;text-decoration:underline;-webkit-text-decoration:underline dotted;text-decoration:underline dotted;}b,strong{font-weight:bolder}code,kbd,samp{font-family:monospace, monospace;font-size:1em;}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sub{bottom:-0.25em}sup{top:-0.5em}img{border-style:none}button,input,optgroup,select,textarea{font-family:inherit;font-size:100%;line-height:1.15;margin:0;}button,input{overflow:visible}button,select{text-transform:none}button,[type=button],[type=reset],[type=submit]{-webkit-appearance:button}button::-moz-focus-inner,[type=button]::-moz-focus-inner,[type=reset]::-moz-focus-inner,[type=submit]::-moz-focus-inner{border-style:none;padding:0}button:-moz-focusring,[type=button]:-moz-focusring,[type=reset]:-moz-focusring,[type=submit]:-moz-focusring{outline:1px dotted ButtonText}fieldset{padding:0.35em 0.75em 0.625em}legend{-webkit-box-sizing:border-box;box-sizing:border-box;color:inherit;display:table;max-width:100%;padding:0;white-space:normal;}progress{vertical-align:baseline}textarea{overflow:auto}[type=checkbox],[type=radio]{-webkit-box-sizing:border-box;box-sizing:border-box;padding:0;}[type=number]::-webkit-inner-spin-button,[type=number]::-webkit-outer-spin-button{height:auto}[type=search]{-webkit-appearance:textfield;outline-offset:-2px;}[type=search]::-webkit-search-decoration{-webkit-appearance:none}::-webkit-file-upload-button{-webkit-appearance:button;font:inherit;}details{display:block}summary{display:list-item}template{display:none}[hidden]{display:none}:root{--ui-animaton-speed:0.2s}.gxg-title-01{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-01--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-02{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-02--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-bold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-03{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em}.gxg-title-03--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-xs);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;text-transform:uppercase;line-height:1.556em;color:var(--color-on-primary)}.gxg-title-04{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-title-04--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-md);letter-spacing:var(--letter-spacing-md);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-title-05{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em}.gxg-title-05--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-sm);color:var(--color-on-background);text-align:start;line-height:1.556em;color:var(--color-on-primary)}.gxg-text{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em}.gxg-text--negative{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-on-primary)}.gxg-text--gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--gray-05)}.gxg-quote{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;font-style:italic}.gxg-quote--negative{color:var(--color-on-primary)}.gxg-link{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block}.gxg-link:hover{color:var(--color-primary-hover)}.gxg-link:active{color:var(--color-primary-active)}.gxg-link-gray{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04)}.gxg-link-gray:hover{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-primary);text-decoration:underline;cursor:pointer;display:inline-block;color:var(--gray-04);color:var(--gray-06)}.gxg-alert-error{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-error-dark);display:inline-block}.gxg-alert-warning{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-warning-dark);display:inline-block}.gxg-alert-success{font-family:var(--font-family-primary);font-weight:var(--font-weight-regular);font-size:var(--font-size-sm);letter-spacing:var(--letter-spacing-xs);color:var(--color-on-background);text-align:start;line-height:1.455em;color:var(--color-success-dark);display:inline-block}.gxg-tab--disabled{color:var(--color-primary-disabled);pointer-events:none}.gxg-tab--disabled[disabled]{color:var(--color-primary-disabled);pointer-events:none}.gxg-label{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}.gxg-label:hover{color:var(--color-primary-hover)}.gxg-label:focud{color:var(--color-primary-active)}.gxg-label:active{color:var(--color-primary-active)}.gxg-label[disabled]{color:var(--color-primary-disabled)}.gxg-label--negative{color:var(--color-on-primary)}.gxg-label--negative[disabled]{color:var(--color-on-disabled)}.gxg-button-styles{font-family:var(--font-family-primary) !important;font-weight:var(--font-weight-semibold);font-size:var(--font-size-sm) !important;letter-spacing:var(--letter-spacing-xs);color:var(--color-primary-enabled);text-align:center;line-height:1.455em}:host{display:block}:host li{list-style:none;font-family:var(--font-family-primary);font-size:var(--font-size-sm);font-weight:var(--font-weight-regular);color:var(--color-on-background);height:20px;overflow:hidden}:host li.tree-open{height:auto}:host li .li-text{height:100%;padding:0 var(--spacing-comp-02);cursor:pointer;display:-ms-flexbox;display:flex;-ms-flex-align:center;align-items:center;position:relative}:host li .li-text:hover{background-color:var(--gray-01)}:host li .li-text:active{background-color:var(--gray-02)}:host li .li-text:focus{outline:none;-webkit-box-shadow:inset 0px 0px 0px 2px var(--color-primary-active);box-shadow:inset 0px 0px 0px 2px var(--color-primary-active)}:host li .li-text--selected{background-color:var(--color-secondary-enabled)}:host li .li-text--selected:hover{background-color:var(--color-secondary-enabled)}:host li .vertical-line{position:absolute;width:1px;height:0;top:21px;left:15px;z-index:1;background-image:-webkit-gradient(linear, left top, left bottom, color-stop(28%, var(--gray-04)), color-stop(0%, rgba(255, 255, 255, 0)));background-image:linear-gradient(var(--gray-04) 28%, rgba(255, 255, 255, 0) 0%);background-position:right;background-size:1px 4px;background-repeat:repeat-y}:host li .horizontal-line{position:absolute;width:16px;height:1px;top:11px;z-index:1;margin-left:-23px;top:10px;background-image:-webkit-gradient(linear, left top, right top, color-stop(33%, var(--gray-04)), color-stop(0%, rgba(255, 255, 255, 0)));background-image:linear-gradient(to right, var(--gray-04) 33%, rgba(255, 255, 255, 0) 0%);background-position:bottom;background-size:3px 1px;background-repeat:repeat-x}:host li .horizontal-line.display-none{display:none}:host li .closed-opened-icons{position:relative;display:-ms-flexbox;display:flex;margin-left:-2px}:host li .toggle-icon{position:relative;z-index:2;-webkit-transition-property:opacity;transition-property:opacity;-webkit-transition-duration:0.5s;transition-duration:0.5s;-webkit-transition-timing-function:ease;transition-timing-function:ease}:host .checkbox{margin-right:var(--spacing-comp-01)}:host .right-icon{-webkit-margin-start:auto;margin-inline-start:auto;position:relative;top:0;-webkit-transition-property:top;transition-property:top;-webkit-transition-duration:0.5s;transition-duration:0.5s;-webkit-transition-timing-function:ease;transition-timing-function:ease}:host .loading{opacity:1;display:inline;right:11px;top:-20px;position:absolute;z-index:1;-webkit-transition-property:all;transition-property:all;-webkit-transition-duration:0.5s;transition-duration:0.5s;-webkit-transition-timing-function:ease;transition-timing-function:ease;border-radius:50%;width:8px;height:8px;border:0.18rem solid var(--gray-04);border-top-color:var(--gray-01);-webkit-animation:spin 1s infinite linear;animation:spin 1s infinite linear}:host .loading--double{border-style:double;border-width:0.5rem}@-webkit-keyframes spin{0%{-webkit-transform:rotate(0deg);transform:rotate(0deg)}100%{-webkit-transform:rotate(360deg);transform:rotate(360deg)}}@keyframes spin{0%{-webkit-transform:rotate(0deg);transform:rotate(0deg)}100%{-webkit-transform:rotate(360deg);transform:rotate(360deg)}}:host([disabled]) li .text{color:var(--gray-05)}:host([disabled]) .toggle-icon{pointer-events:none}:host([downloading]){pointer-events:none}:host([downloading]) .right-icon{top:20px}:host([downloading]) .loading{top:3px}:host([downloaded]) .loading,:host([downloaded]) .right-icon{opacity:0}";
    }
  };
}));