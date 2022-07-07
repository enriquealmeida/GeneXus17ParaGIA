import { r as e, h as t, H as s, g as l } from "./p-eb9dc661.js";

const n = class {
  constructor(t) {
    e(this, t), 
    /*******************
         * STATE
         *******************/
    this.listType = "list-one";
  }
  componentWillLoad() {
    /*
        Set the type of list
        list-three : has two parent lists
        list-two : has one parent list
        list-one : has no parent lists. It is the main list (this is the default)
        */
    null !== this.el.parentNode.parentElement && "CH-SIDEBAR-MENU-LIST" === this.el.parentNode.parentElement.nodeName && (this.listType = "list-two"), 
    null !== this.el.parentNode.parentElement.parentElement.parentElement && "CH-SIDEBAR-MENU-LIST" === this.el.parentNode.parentElement.parentNode.parentElement.nodeName && (this.listType = "list-three");
  }
  render() {
    return t(s, {
      class: {
        "list-one": "list-one" === this.listType,
        "list-two": "list-two" === this.listType,
        "list-three": "list-three" === this.listType
      }
    }, t("slot", null));
  }
  get el() {
    return l(this);
  }
};

n.style = ":host{display:block}:host ul{padding:0;margin:0}";

export { n as ch_sidebar_menu_list }