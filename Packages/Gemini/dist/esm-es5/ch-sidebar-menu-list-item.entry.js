import { r as registerInstance, c as createEvent, e as getAssetPath, h, H as Host, g as getElement } from './index-09b1517f.js';
var chSidebarMenuListItemCss = ":host{display:block;cursor:pointer}:host .icon{display:inline-block;line-height:0}:host .main-container{-webkit-transition:background-color 0.25s;transition:background-color 0.25s;position:relative}:host .main-container:hover{background-color:var(--item-hover-color)}:host .text{color:var(--text-color)}:host(.collapsable){overflow:hidden}:host(.item--active) .main-container{background-color:var(--item-active-color)}:host(.item) .main-container:active{background-color:var(--item-active-color)}:host(.list-one__item) .main-container{display:-ms-flexbox;display:flex;-ms-flex-pack:justify;justify-content:space-between;padding:8px 8px 8px 16px;min-height:40px;-webkit-box-sizing:border-box;box-sizing:border-box;-ms-flex-align:start;align-items:start}:host(.list-one__item) .main-container .left-container{display:-ms-flexbox;display:flex;-ms-flex-align:start;align-items:start;-webkit-padding-end:8px;padding-inline-end:8px}:host(.list-one__item) .main-container .left-container .custom-icon{-webkit-margin-end:8px;margin-inline-end:8px}:host(.list-one__item) .main-container .arrow-icon{-webkit-transform:rotate(180deg);transform:rotate(180deg);-webkit-transition:-webkit-transform 0.25s;transition:-webkit-transform 0.25s;transition:transform 0.25s;transition:transform 0.25s, -webkit-transform 0.25s}:host(.list-one__item.uncollapsed) .arrow-icon{-webkit-transform:rotate(0);transform:rotate(0)}:host(.list-one__item.speed-zero){-webkit-transition-duration:0.01s !important;transition-duration:0.01s !important}:host(.switch-order) .main-container{-ms-flex-direction:row-reverse;flex-direction:row-reverse}:host(.switch-order) .left-container{-ms-flex-direction:row-reverse;flex-direction:row-reverse;-webkit-padding-end:0 !important;padding-inline-end:0 !important}:host(.switch-order) .text{opacity:0}:host(.switch-order) .custom-icon{-webkit-margin-end:0 !important;margin-inline-end:0 !important}:host(.list-two__item) .main-container{display:-ms-flexbox;display:flex;-ms-flex-align:self-start;align-items:self-start;-webkit-padding-start:44px;padding-inline-start:44px;padding-top:6px;padding-bottom:6px;min-height:20px}:host(.list-two__item) .main-container .arrow-icon{-webkit-transform:rotate(0);transform:rotate(0);-webkit-transition:-webkit-transform 0.25s;transition:-webkit-transform 0.25s;transition:transform 0.25s;transition:transform 0.25s, -webkit-transform 0.25s;-webkit-margin-end:4px;margin-inline-end:4px}:host(.list-two__item) .main-container .text{font-size:14px;font-weight:700;letter-spacing:0.38px;text-transform:uppercase}:host(.list-two__item:not(.collapsable)) .main-container{-webkit-padding-start:51px;padding-inline-start:51px}:host(.list-two__item.uncollapsed) .arrow-icon{-webkit-transform:rotate(90deg);transform:rotate(90deg)}:host(.list-three__item) .main-container{-webkit-padding-start:73px;padding-inline-start:73px;padding-top:5px;padding-bottom:5px}";
var ChSidebarMenuListItem = /** @class */ (function () {
    function ChSidebarMenuListItem(hostRef) {
        registerInstance(this, hostRef);
        this.itemClickedEvent = createEvent(this, "itemClickedEvent", 7);
        this.listOneIcon = getAssetPath("./sidebar-menu-list-item-assets/projects.svg");
        this.arrowTop = getAssetPath("./sidebar-menu-list-item-assets/arrow-top.svg");
        this.arrowRight = getAssetPath("./sidebar-menu-list-item-assets/arrow-right.svg");
        this.collapsable = false;
    }
    ChSidebarMenuListItem.prototype.componentWillLoad = function () {
        //SET THE TPYE OF ITEM
        if (this.el.parentElement.classList.contains("list-one")) {
            this.listTypeItem = "one";
        }
        else if (this.el.parentElement.classList.contains("list-two")) {
            this.listTypeItem = "two";
        }
        else {
            this.listTypeItem = "three";
        }
        //SET COLLAPSABLE OR NOT
        if (this.el.querySelector("ch-sidebar-menu-list") !== null) {
            this.collapsable = true;
        }
    };
    ChSidebarMenuListItem.prototype.itemClicked = function () {
        var chSidebarMenu = document.querySelector("ch-sidebar-menu");
        var menu = chSidebarMenu.shadowRoot.getElementById("menu");
        if (!menu.classList.contains("collapsed")) {
            var itemId = this.el.getAttribute("id");
            this.itemClickedEvent.emit({ "item-id": itemId });
        }
    };
    ChSidebarMenuListItem.prototype.firstListItemIcon = function () {
        if (this.itemIconSrc !== undefined) {
            return this.itemIconSrc;
        }
        else {
            return this.listOneIcon;
        }
    };
    ChSidebarMenuListItem.prototype.listItemContent = function () {
        if (this.listTypeItem === "one")
            return [
                h("div", { class: "main-container", onClick: this.itemClicked.bind(this) }, h("div", { class: "left-container" }, h("span", { class: "icon custom-icon" }, h("ch-icon", { src: this.firstListItemIcon(), style: {
                        "--icon-size": "20px",
                        "--icon-color": "var(--first-list-icon-color)",
                    } })), h("span", { class: "text" }, h("slot", null))), this.collapsable ? (h("span", { class: "icon arrow-icon" }, h("ch-icon", { src: this.arrowTop, style: {
                        "--icon-size": "20px",
                        "--icon-color": "var(--first-list-arrow-color)",
                    } }))) : null),
                h("slot", { name: "list" }),
            ];
        if (this.listTypeItem === "two")
            return [
                h("div", { class: "main-container", onClick: this.itemClicked.bind(this) }, this.collapsable ? (h("span", { class: "icon arrow-icon" }, h("ch-icon", { src: this.arrowRight, style: {
                        "--icon-size": "20px",
                        "--icon-color": "var(--second-list-arrow-color)",
                    } }))) : null, h("span", { class: "text" }, h("slot", null))),
                h("slot", { name: "list" }),
            ];
        if (this.listTypeItem === "three")
            return (h("div", { class: "main-container", onClick: this.itemClicked.bind(this) }, h("span", { class: "text" }, h("slot", null))));
    };
    ChSidebarMenuListItem.prototype.render = function () {
        return (h(Host, { class: {
                item: true,
                collapsable: this.collapsable,
                "list-one__item": this.listTypeItem === "one",
                "list-two__item": this.listTypeItem === "two",
                "list-three__item": this.listTypeItem === "three",
            } }, this.listItemContent()));
    };
    Object.defineProperty(ChSidebarMenuListItem, "assetsDirs", {
        get: function () { return ["sidebar-menu-list-item-assets"]; },
        enumerable: false,
        configurable: true
    });
    Object.defineProperty(ChSidebarMenuListItem.prototype, "el", {
        get: function () { return getElement(this); },
        enumerable: false,
        configurable: true
    });
    return ChSidebarMenuListItem;
}());
ChSidebarMenuListItem.style = chSidebarMenuListItemCss;
export { ChSidebarMenuListItem as ch_sidebar_menu_list_item };
