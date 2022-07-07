<gx-navbar
  caption="{{ appBarNavigation.caption }}"
  [cssClass]="appBarNavigation.className"
  [showToggleButton]="appBarNavigation.navigationStyle === 'slide' && leftComponent !== null"
  [showBackButton]="appBarNavigation.showBackButton"
  [backButtonLabel]="app.translate('Back')"
  [singleLine]="applicationBarSingleLine"
  toggleButtonLabel="{{ appBarNavigation.toggleButtonLabel }}"
  (toggleButtonClick)="handleToggleButtonClick()"
  (backButtonClick)="handleBackButtonClick()"
  gx-form-properties
  caption=""
  *ngIf="topApplicationBarVisible"
>
  <gx-navbar-item
    *ngFor="let item of appBarNavigation.items; trackBy: trackAppBarItemById"
    (click)="item.onClick()"
    cssClass="{{ item.className }}"
    iconSrc="{{ item.iconSrc }}"
    slot="navigation"
    [@appBarItemEnterLeaveAnimation]
  >
    {{ item.caption }}
  </gx-navbar-item>
  <gx-navbar-item
    *ngFor="let item of appBarActions; trackBy: trackAppBarItemById"
    (click)="item.onClick()"
    cssClass="{{ item?.class }}"
    iconSrc="{{ item?.iconSrc }}"
    slot="{{ item?.slotName }}"
  >
    {{ item?.caption }}
  </gx-navbar-item>
</gx-navbar>
<gx-messages [message]="appMessage"></gx-messages>
<gx-layout
  [ngClass]="{
    'main-layout': true
  }"
  [topHidden]="!topVisible"
  [rightHidden]="!rightVisible"
  [bottomHidden]="!bottomVisible"
  [leftHidden]="!leftVisible"
  (leftHiddenChange)="handleLeftTargetHiddenChange(\$event)"
  (rightHiddenChange)="handleRightTargetHiddenChange(\$event)"
  (verticalTargetsBreakpointMatchChange)="
    handleTargetsBreakpointMatchCange(\$event)
  "
>
  <div class="ph-item" *ngIf="isLoading">
    <div class="ph-col-12">
      <div class="ph-picture"></div>
      <div class="ph-row">
        <div class="ph-col-6 big"></div>
        <div class="ph-col-4 empty big"></div>
        <div class="ph-col-2 big"></div>
        <div class="ph-col-4"></div>
        <div class="ph-col-8 empty"></div>
        <div class="ph-col-6"></div>
        <div class="ph-col-6 empty"></div>
        <div class="ph-col-12"></div>
      </div>
    </div>
    <div class="ph-col-12">
      <div class="ph-picture"></div>
      <div class="ph-row">
        <div class="ph-col-6 big"></div>
        <div class="ph-col-4 empty big"></div>
        <div class="ph-col-2 big"></div>
        <div class="ph-col-4"></div>
        <div class="ph-col-8 empty"></div>
        <div class="ph-col-6"></div>
        <div class="ph-col-6 empty"></div>
        <div class="ph-col-12"></div>
      </div>
    </div>
  </div>
$if (data.MasterPageObjectFullName)$
  <$data.MasterPageObjectFullName$>
  <router-outlet
    *ngIf="!isLoading"
    [@contentEnterLeaveAnimation]
  ></router-outlet>
  </$data.MasterPageObjectFullName$>
$else$
  <router-outlet
    *ngIf="!isLoading"
    [@contentEnterLeaveAnimation]
  ></router-outlet>
$endif$
  <gx-component-host
    slot="top"
    #topOutlet
    id="top"
    [componentUri]="topComponent"
  >
  </gx-component-host>
  <gx-progress [updateSettings]="appProgress"></gx-progress>
  <gx-component-host
    slot="left"
    #leftOutlet
    id="left"
    [componentUri]="leftComponent"
    class="vertical-target"
  >
  </gx-component-host>
  <gx-component-host
    slot="right"
    #rightOutlet
    id="right"
    [componentUri]="rightComponent"
    class="vertical-target"
  >
  </gx-component-host>
  <gx-component-host
    slot="bottom"
    #bottomOutlet
    id="bottom"
    [componentUri]="bottomComponent"
  >
  </gx-component-host>
</gx-layout>
<gx-navbar
  [cssClass]="appBarNavigation.className"
  position="bottom"
  gx-form-properties
  *ngIf="bottomApplicationBarVisible"
>
  <gx-navbar-item
    *ngFor="let item of appBarNavigation.items; trackBy: trackAppBarItemById"
    (click)="item.onClick()"
    cssClass="{{ item.className }}"
    iconSrc="{{ item.iconSrc }}"
    slot="navigation"
    [@appBarItemEnterLeaveAnimation]
  >
    {{ item.caption }}
  </gx-navbar-item>
</gx-navbar>
<gx-modal [opened]="popupVisible" (close)="onPopupClose()" [attr.width]="popupStyle?.width" [attr.height]="popupStyle?.height">
  <div slot="body">
    <gx-component-host
      #popupOutlet
      id="popup"
      [ngStyle]="popupStyle"
      [componentUri]="popupComponent"
    >
    </gx-component-host>
  </div>
</gx-modal>
