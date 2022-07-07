'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

const index = require('./index-1115561c.js');
const patch = require('./patch-2d0072ac.js');

const defineCustomElements = (win, options) => {
  if (typeof window === 'undefined') return Promise.resolve();
  return patch.patchEsm().then(() => {
  return index.bootstrapLazy(JSON.parse("[[\"gxg-combo.cjs\",[[1,\"gxg-combo\",{\"width\":[1],\"itemsNodeList\":[32],\"selectedItemValue\":[32],\"inputTextValue\":[32],\"showItems\":[32],\"inputTextIcon\":[32],\"inputTextIconPosition\":[32],\"noMatch\":[32],\"slottedContent\":[32]},[[0,\"itemClicked\",\"itemClickedHandler\"]]]]],[\"gxg-filter.cjs\",[[1,\"gxg-filter\",{\"top\":[8],\"left\":[8],\"itemsNodeList\":[32]},[[0,\"itemClickedEvent\",\"handleItemClickedEvent\"]]]]],[\"gxg-accordion-item.cjs\",[[1,\"gxg-accordion-item\",{\"disabled\":[516],\"editableTitle\":[4,\"editable-title\"],\"mode\":[513],\"itemId\":[1,\"item-id\"],\"itemTitle\":[1,\"item-title\"],\"itemSubtitle\":[1,\"item-subtitle\"],\"titleIcon\":[1,\"title-icon\"],\"status\":[513],\"nestedAccordion\":[32],\"minHeight\":[32],\"accordionMode\":[32],\"hasSlottedMeta\":[32]}]]],[\"gxg-alert.cjs\",[[1,\"gxg-alert\",{\"active\":[516],\"activeTime\":[1,\"active-time\"],\"position\":[1],\"title\":[1],\"type\":[1],\"fullWidth\":[4,\"full-width\"],\"leftRight\":[1,\"left-right\"],\"bottom\":[1],\"width\":[1],\"silent\":[4],\"rtl\":[32]}]]],[\"gxg-demo.cjs\",[[1,\"gxg-demo\",{\"layerZIndex\":[2,\"layer-z-index\"],\"initiateDemo\":[4,\"initiate-demo\"],\"modalMessage\":[1,\"modal-message\"],\"gxgDemoItems\":[32],\"numberOfItems\":[32],\"currentItem\":[32],\"message\":[32],\"position\":[32],\"leftPosition\":[32],\"rightPosition\":[32],\"topPosition\":[32],\"layerVisible\":[32],\"instructionVisible\":[32],\"modalVisible\":[32],\"nextItemClicked\":[32],\"disableNextButton\":[32],\"rtl\":[32],\"currentItemZIndex\":[32],\"currentItemPosition\":[32],\"currentItemBoxShadow\":[32],\"currentItemPointerEvents\":[32]}]]],[\"gxg-drag-box.cjs\",[[1,\"gxg-drag-box\",{\"active\":[516],\"deletable\":[4],\"padding\":[513],\"title\":[1]}]]],[\"gxg-form-radio-group.cjs\",[[1,\"gxg-form-radio-group\",{\"label\":[1],\"requiredMessage\":[1025,\"required-message\"],\"required\":[4]},[[0,\"keyPressed\",\"keyPressedHandler\"],[0,\"changeInternal\",\"radioClickedHandler\"]]]]],[\"gxg-form-textarea.cjs\",[[1,\"gxg-form-textarea\",{\"disabled\":[4],\"error\":[1028],\"requiredMessage\":[1025,\"required-message\"],\"label\":[513],\"maxWidth\":[1,\"max-width\"],\"placeholder\":[1],\"required\":[4],\"value\":[513],\"rows\":[2],\"warning\":[4]}]]],[\"gxg-modal.cjs\",[[1,\"gxg-modal\",{\"padding\":[513],\"footerJustifyContent\":[1,\"footer-justify-content\"],\"modalTitle\":[1,\"modal-title\"],\"width\":[1],\"visible\":[4],\"zIndex\":[1,\"z-index\"],\"silent\":[4],\"layerVisible\":[32],\"modalVisible\":[32],\"modalTransition\":[32]}]]],[\"gxg-tab-bar.cjs\",[[1,\"gxg-tab-bar\",{\"appendedButtons\":[32],\"tabBarMenuHeight\":[32],\"rtl\":[32]},[[0,\"tabActivated\",\"tabActivatedHandler\"]]]]],[\"gxg-tree-item.cjs\",[[1,\"gxg-tree-item\",{\"checkbox\":[4],\"checked\":[4],\"download\":[4],\"downloading\":[4],\"downloaded\":[4],\"leftIcon\":[1,\"left-icon\"],\"rightIcon\":[1,\"right-icon\"],\"opened\":[4],\"selected\":[4],\"isLeaf\":[4,\"is-leaf\"],\"hasChildTree\":[4,\"has-child-tree\"],\"firstTreeItem\":[4,\"first-tree-item\"],\"indeterminate\":[4],\"disabled\":[4],\"numberOfParentTrees\":[32],\"itemPaddingLeft\":[32],\"horizontalLinePaddingLeft\":[32],\"lastTreeItem\":[32],\"firstTreeItemOfParentTree\":[32],\"lastTreeItemOfParentTree\":[32],\"rightIconColor\":[32],\"numberOfDirectTreeItemsDescendants\":[32],\"updateTreeVerticalLineHeight\":[64]}]]],[\"gxg-window.cjs\",[[1,\"gxg-window\",{\"initialHeight\":[1,\"initial-height\"],\"initialWidth\":[1,\"initial-width\"],\"minHeight\":[1,\"min-height\"],\"minWidth\":[1,\"min-width\"],\"maxHeight\":[1,\"max-height\"],\"maxWidth\":[1,\"max-width\"],\"leftPosition\":[1,\"left-position\"],\"topPosition\":[1,\"top-position\"],\"windowTitle\":[1,\"window-title\"],\"titleIcon\":[1,\"title-icon\"],\"displayWindow\":[4,\"display-window\"],\"showWindow\":[32],\"customPosition\":[32]}]]],[\"gxg-breadcrumb.cjs\",[[1,\"gxg-breadcrumb\",{\"icon\":[1]}]]],[\"gxg-combo-item.cjs\",[[1,\"gxg-combo-item\",{\"icon\":[1],\"value\":[8],\"iconColor\":[1,\"icon-color\"]}]]],[\"gxg-contextual-menu-item.cjs\",[[1,\"gxg-contextual-menu-item\",{\"icon\":[1]}]]],[\"gxg-drop-down.cjs\",[[1,\"gxg-drop-down\",{\"width\":[1],\"maxHeight\":[1,\"max-height\"],\"label\":[1],\"icon\":[1],\"showContent\":[4,\"show-content\"],\"initialButtonText\":[32],\"detectClickOutsideDropDownVar\":[32]}]]],[\"gxg-filter-item.cjs\",[[1,\"gxg-filter-item\",{\"itemId\":[8,\"item-id\"],\"type\":[8],\"icon\":[1]}]]],[\"gxg-listbox-item.cjs\",[[1,\"gxg-listbox-item\",{\"icon\":[1],\"value\":[8],\"iconColor\":[1,\"icon-color\"],\"checkboxes\":[32],\"itemHasFocus\":[32]}]]],[\"gxg-menu-item.cjs\",[[1,\"gxg-menu-item\",{\"label\":[1],\"icon\":[1],\"active\":[516]}]]],[\"gxg-more-info.cjs\",[[1,\"gxg-more-info\",{\"position\":[513],\"label\":[1],\"moreInfoLabel\":[1,\"more-info-label\"],\"url\":[1],\"target\":[1]}]]],[\"gxg-pill.cjs\",[[1,\"gxg-pill\",{\"disabled\":[4],\"icon\":[1],\"heightAuto\":[4,\"height-auto\"],\"type\":[513]}]]],[\"gxg-tab-button.cjs\",[[1,\"gxg-tab-button\",{\"tabLabel\":[1,\"tab-label\"],\"tab\":[1],\"isSelected\":[4,\"is-selected\"],\"disabled\":[4],\"icon\":[1]}]]],[\"gxg-toolbar.cjs\",[[1,\"gxg-toolbar\",{\"position\":[1],\"subtitle\":[1],\"toolbarTitle\":[1,\"toolbar-title\"],\"rtl\":[32]}]]],[\"gxg-toolbar-item.cjs\",[[1,\"gxg-toolbar-item\",{\"disabled\":[4],\"icon\":[1],\"subtitle\":[1],\"toolbarItemTitle\":[1,\"toolbar-item-title\"]}]]],[\"gxg-tree-grid-divs.cjs\",[[1,\"gxg-tree-grid-divs\",{\"columns\":[16],\"rows\":[16],\"width\":[1],\"displayChildren\":[1,\"display-children\"],\"selectedRowsIds\":[1026,\"selected-rows-ids\"],\"editCell\":[16],\"thWidthLeftover\":[32],\"rowsBuffer\":[32],\"thInPixels\":[32],\"displayRowChildrenIds\":[32],\"columnClicked\":[32],\"columnOrder\":[32]}]]],[\"ch-sidebar-menu.cjs\",[[1,\"ch-sidebar-menu\",{\"menuTitle\":[1,\"menu-title\"],\"singleListOpen\":[4,\"single-list-open\"],\"indicator\":[32]}]]],[\"ch-sidebar-menu-list-item.cjs\",[[1,\"ch-sidebar-menu-list-item\",{\"itemIconSrc\":[1,\"item-icon-src\"],\"collapsable\":[32],\"listTypeItem\":[32]}]]],[\"gxg-color-picker.cjs\",[[1,\"gxg-color-picker\",{\"label\":[1025],\"value\":[1537],\"colorRepresentation\":[32],\"colorInputValue\":[32],\"colorObject\":[32]}]]],[\"gxg-listbox.cjs\",[[1,\"gxg-listbox\",{\"theTitle\":[1,\"the-title\"],\"width\":[1],\"checkboxes\":[4]},[[0,\"itemClicked\",\"itemClickedHandler\"],[0,\"checkboxClicked\",\"checkboxClickedHandler\"]]]]],[\"gxg-split_2.cjs\",[[1,\"gxg-split\"],[1,\"gxg-splitter\",{\"direction\":[1],\"forceCollapseZero\":[4,\"force-collapse-zero\"],\"knob\":[513],\"minSize\":[1,\"min-size\"],\"sizes\":[1],\"split\":[32],\"minSizeArray\":[32],\"sizesArray\":[32],\"idsArray\":[32],\"currentSizes\":[32],\"leftSplitReachedMin\":[32],\"rightSplitReachedMin\":[32],\"collapsedToZero\":[32],\"collapse\":[64]}]]],[\"ch-sidebar-menu-list.cjs\",[[1,\"ch-sidebar-menu-list\",{\"listType\":[32]}]]],[\"gxg-accordion.cjs\",[[1,\"gxg-accordion\",{\"disabled\":[4],\"singleItemOpen\":[4,\"single-item-open\"],\"mode\":[1],\"maxWidth\":[1,\"max-width\"],\"noPadding\":[4,\"no-padding\"],\"accordions\":[32]},[[0,\"accordionItemClicked\",\"itemClickedHandler\"],[0,\"accordionItemLoaded\",\"itemLoadedHandler\"]]]]],[\"gxg-box.cjs\",[[1,\"gxg-box\",{\"background\":[513],\"border\":[4],\"padding\":[513],\"minHeight\":[1,\"min-height\"],\"maxWidth\":[1,\"max-width\"]}]]],[\"gxg-breadcrumbs.cjs\",[[1,\"gxg-breadcrumbs\"]]],[\"gxg-card.cjs\",[[1,\"gxg-card\",{\"elevation\":[513],\"background\":[513],\"padding\":[513],\"minHeight\":[1,\"min-height\"],\"maxWidth\":[1,\"max-width\"]}]]],[\"gxg-column.cjs\",[[1,\"gxg-column\",{\"width\":[513]}]]],[\"gxg-columns.cjs\",[[1,\"gxg-columns\",{\"alignY\":[513,\"align-y\"],\"collapseBellow\":[513,\"collapse-bellow\"],\"space\":[513]}]]],[\"gxg-contextual-menu.cjs\",[[1,\"gxg-contextual-menu\",{\"visible\":[516],\"widthOverflow\":[32],\"heightOverflow\":[32],\"firstRightClick\":[32],\"topPosition\":[32],\"leftPosition\":[32]}]]],[\"gxg-contextual-menu-submenu.cjs\",[[1,\"gxg-contextual-menu-submenu\"]]],[\"gxg-date-picker.cjs\",[[1,\"gxg-date-picker\",{\"alwaysShow\":[4,\"always-show\"],\"defaultDate\":[1,\"default-date\"],\"label\":[1],\"noWeekends\":[4,\"no-weekends\"],\"minDate\":[1,\"min-date\"],\"maxDate\":[1,\"max-date\"],\"maxWidth\":[1,\"max-width\"],\"rtl\":[32]}]]],[\"gxg-drag-container.cjs\",[[1,\"gxg-drag-container\",{\"deletable\":[4],\"maxWidth\":[1,\"max-width\"],\"padding\":[1]},[[0,\"clicked\",\"clickedHandler\"]]]]],[\"gxg-form-radio.cjs\",[[1,\"gxg-form-radio\",{\"error\":[4],\"RadioId\":[1,\"radio-id\"],\"checked\":[516],\"disabled\":[4],\"label\":[1],\"name\":[1],\"value\":[1]}]]],[\"gxg-loader.cjs\",[[1,\"gxg-loader\",{\"text\":[1],\"show\":[4],\"visibleZIndex\":[1,\"visible-z-index\"],\"layerOpacity100\":[32],\"squaresOpacity100\":[32],\"textOpacity100\":[32],\"sendLayerBack\":[32]}]]],[\"gxg-menu.cjs\",[[1,\"gxg-menu\",{\"menuTitle\":[1,\"menu-title\"],\"tabs\":[516]},[[0,\"menuItemActive\",\"menuItemActiveHandler\"]]]]],[\"gxg-option.cjs\",[[1,\"gxg-option\",{\"value\":[1],\"selected\":[4]}]]],[\"gxg-progress-bar.cjs\",[[1,\"gxg-progress-bar\",{\"disabled\":[516],\"label\":[1],\"value\":[514],\"maxWidth\":[1,\"max-width\"],\"silent\":[4]}]]],[\"gxg-scroll.cjs\",[[1,\"gxg-scroll\",{\"maxHeight\":[1,\"max-height\"]}]]],[\"gxg-select.cjs\",[[1,\"gxg-select\",{\"disabled\":[516],\"error\":[4],\"label\":[1],\"labelPosition\":[513,\"label-position\"],\"minimal\":[4],\"required\":[516],\"size\":[1],\"value\":[1],\"warning\":[4],\"maxWidth\":[1,\"max-width\"],\"rtl\":[32],\"rerender\":[32]},[[0,\"optionIsSelected\",\"todoCompletedHandler\"]]]]],[\"gxg-separator.cjs\",[[1,\"gxg-separator\",{\"type\":[513],\"margin\":[513]}]]],[\"gxg-slider.cjs\",[[1,\"gxg-slider\",{\"disabled\":[516],\"label\":[1],\"max\":[2],\"value\":[514],\"maxWidth\":[1,\"max-width\"],\"rtl\":[32]}]]],[\"gxg-spacer-layout.cjs\",[[1,\"gxg-spacer-layout\",{\"fullHeight\":[4,\"full-height\"],\"space\":[1],\"orientation\":[1],\"justifyContent\":[1,\"justify-content\"]}]]],[\"gxg-spacer-one.cjs\",[[1,\"gxg-spacer-one\",{\"space\":[1]}]]],[\"gxg-stack.cjs\",[[1,\"gxg-stack\",{\"space\":[513]}]]],[\"gxg-stepper.cjs\",[[1,\"gxg-stepper\",{\"disabled\":[516],\"label\":[1],\"labelPosition\":[513,\"label-position\"],\"value\":[514],\"max\":[514],\"min\":[514],\"rtl\":[32]}]]],[\"gxg-tab.cjs\",[[1,\"gxg-tab\",{\"tab\":[1],\"isSelected\":[516,\"is-selected\"]}]]],[\"gxg-tabs.cjs\",[[1,\"gxg-tabs\",{\"position\":[1],\"height\":[513],\"activeTab\":[32]},[[0,\"tabActivated\",\"tabActivatedHandler\"]]]]],[\"gxg-template.cjs\",[[1,\"gxg-template\",{\"name\":[1]}]]],[\"gxg-test.cjs\",[[1,\"gxg-test\"]]],[\"gxg-text.cjs\",[[1,\"gxg-text\",{\"href\":[1],\"target\":[1],\"type\":[1]}]]],[\"gxg-title.cjs\",[[1,\"gxg-title\",{\"type\":[1]}]]],[\"gxg-toggle.cjs\",[[1,\"gxg-toggle\",{\"disabled\":[516],\"label\":[1],\"on\":[516]}]]],[\"gxg-tooltip.cjs\",[[1,\"gxg-tooltip\",{\"position\":[513],\"label\":[1],\"noBorder\":[516,\"no-border\"]}]]],[\"gxg-tree.cjs\",[[1,\"gxg-tree\",{\"checkbox\":[4],\"checked\":[4],\"toggleCheckboxes\":[4,\"toggle-checkboxes\"],\"nestedTree\":[32],\"mainTree\":[32]},[[0,\"liItemClicked\",\"liItemClickedHandler\"],[0,\"toggleIconClicked\",\"toggleIconClickedHandler\"],[0,\"checkboxClickedEvent\",\"checkboxClickedEventHandler\"]]]]],[\"gxg-button-group.cjs\",[[1,\"gxg-button-group\",{\"buttonGroupTitle\":[1,\"button-group-title\"],\"titleAlignment\":[513,\"title-alignment\"],\"defaultSelectedBtnId\":[1,\"default-selected-btn-id\"],\"disabled\":[516],\"fullWidth\":[516,\"full-width\"],\"outlined\":[516],\"value\":[32],\"rtl\":[32]}]]],[\"gxg-form-checkbox.cjs\",[[1,\"gxg-form-checkbox\",{\"checkboxId\":[1,\"checkbox-id\"],\"checked\":[4],\"indeterminate\":[516],\"disabled\":[4],\"label\":[1],\"value\":[1],\"name\":[1]}]]],[\"ch-icon_2.cjs\",[[1,\"gxg-icon\",{\"color\":[1],\"size\":[1],\"type\":[8]}],[1,\"ch-icon\",{\"lazy\":[4],\"autoColor\":[516,\"auto-color\"],\"src\":[513],\"isVisible\":[32],\"svgContent\":[32]}]]],[\"gxg-form-message.cjs\",[[1,\"gxg-form-message\",{\"type\":[1]}]]],[\"gxg-form-text.cjs\",[[1,\"gxg-form-text\",{\"clearButton\":[4,\"clear-button\"],\"disabled\":[4],\"error\":[1028],\"icon\":[8],\"iconPosition\":[513,\"icon-position\"],\"label\":[1],\"labelPosition\":[513,\"label-position\"],\"minimal\":[516],\"overDarkBackground\":[516,\"over-dark-background\"],\"placeholder\":[1],\"required\":[516],\"requiredMessage\":[1025,\"required-message\"],\"value\":[513],\"warning\":[4],\"maxWidth\":[1,\"max-width\"],\"textStyle\":[513,\"text-style\"],\"password\":[4],\"cursorInside\":[32],\"inputSize\":[32],\"mouseCoordinates\":[32],\"rtl\":[32]},[[0,\"focus\",\"handleFocus\"]]]]],[\"gxg-button.cjs\",[[1,\"gxg-button\",{\"alwaysBlack\":[4,\"always-black\"],\"disabled\":[4],\"fullWidth\":[4,\"full-width\"],\"icon\":[8],\"negative\":[4],\"type\":[1],\"buttonStylesEditable\":[4,\"button-styles-editable\"],\"mouseEnter\":[32],\"focusIn\":[32]},[[0,\"focus\",\"handleFocus\"]]]]]]"), options);
  });
};

exports.defineCustomElements = defineCustomElements;