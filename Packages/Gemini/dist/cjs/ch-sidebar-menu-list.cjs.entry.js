'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

const index = require('./index-1115561c.js');

const chSidebarMenuListCss = ":host{display:block}:host ul{padding:0;margin:0}";

const ChSidebarMenuList = class {
    constructor(hostRef) {
        index.registerInstance(this, hostRef);
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
        if (this.el.parentNode.parentElement !== null) {
            if (this.el.parentNode.parentElement.nodeName === "CH-SIDEBAR-MENU-LIST") {
                this.listType = "list-two";
            }
        }
        if (this.el.parentNode.parentElement.parentElement.parentElement !== null) {
            if (this.el.parentNode.parentElement.parentNode.parentElement.nodeName ===
                "CH-SIDEBAR-MENU-LIST") {
                this.listType = "list-three";
            }
        }
    }
    render() {
        return (index.h(index.Host, { class: {
                "list-one": this.listType === "list-one",
                "list-two": this.listType === "list-two",
                "list-three": this.listType === "list-three",
            } }, index.h("slot", null)));
    }
    get el() { return index.getElement(this); }
};
ChSidebarMenuList.style = chSidebarMenuListCss;

exports.ch_sidebar_menu_list = ChSidebarMenuList;
