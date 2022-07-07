System.register([ "./p-d8a20c31.system.js" ], (function(e) {
  "use strict";
  var t, n, s, l;
  return {
    setters: [ function(e) {
      t = e.r, n = e.h, s = e.H, l = e.g;
    } ],
    execute: function() {
      e("ch_sidebar_menu_list", /** @class */ function() {
        function class_1(e) {
          t(this, e), 
          /*******************
                     * STATE
                     *******************/
          this.listType = "list-one";
        }
        return class_1.prototype.componentWillLoad = function() {
          /*
                    Set the type of list
                    list-three : has two parent lists
                    list-two : has one parent list
                    list-one : has no parent lists. It is the main list (this is the default)
                    */
          null !== this.el.parentNode.parentElement && "CH-SIDEBAR-MENU-LIST" === this.el.parentNode.parentElement.nodeName && (this.listType = "list-two"), 
          null !== this.el.parentNode.parentElement.parentElement.parentElement && "CH-SIDEBAR-MENU-LIST" === this.el.parentNode.parentElement.parentNode.parentElement.nodeName && (this.listType = "list-three");
        }, class_1.prototype.render = function() {
          return n(s, {
            class: {
              "list-one": "list-one" === this.listType,
              "list-two": "list-two" === this.listType,
              "list-three": "list-three" === this.listType
            }
          }, n("slot", null));
        }, Object.defineProperty(class_1.prototype, "el", {
          get: function() {
            return l(this);
          },
          enumerable: !1,
          configurable: !0
        }), class_1;
      }()).style = ":host{display:block}:host ul{padding:0;margin:0}";
    }
  };
}));