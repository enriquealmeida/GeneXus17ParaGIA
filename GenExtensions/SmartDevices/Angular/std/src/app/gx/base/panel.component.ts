import {
  OnInit,
  OnChanges,
  AfterViewInit,
  Input,
  ViewChildren,
  QueryList,
  Directive,
  ChangeDetectorRef
} from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { Subscription, BehaviorSubject, combineLatest } from "rxjs";
import { AppContainer } from "app/gx/base/app-container";
import { ViewManager, ComponentViewDefinition } from "app/gx/base/view-manager";
import { TypeConversions } from "app/gx/base/type-conversion";
import { ComponentHostComponent } from "app/gx/ui/controls/component-host/component-host.component";
import { msg } from "@genexus/web-standard-functions/dist/lib-esm/misc/msg";
import { CompositeNavigation } from "app/gx/navigation/composite-navigation";
import { IStateContainer } from "app/gx/navigation/navigation-state";
import { PanelNavigationState } from "app/gx/navigation/panel-navigation-state.dt";
import { NavigationHelper } from "app/gx/navigation/navigation-helper";
import { ActionState } from "app/gx/base/action-state.dt";
import { OutletsHelper } from "app/gx/navigation/outlets-helper";
import { GUID as stdGUID } from '@genexus/web-standard-functions/dist/lib-esm/types/guid';
import { Settings } from "../../app.settings";
import { GxCollectionData } from "./gxcollection.dt";
import { GAMService } from "app/gx/auth/gam.service";

/**
 * Class implementing the base panel behaviour
 */
@Directive()
export class PanelComponent
  implements OnInit, OnChanges, AfterViewInit, IStateContainer {
  @Input() Mode: string;
  @Input() sectionType: string;

  private _isDynComponent = false;
  private _dynCompoParameters: any;
  private _dynRefresh = false;
  showAsCard = true;
  canControlAppBar = false;

  _dataReady$ = new BehaviorSubject<boolean>(false);
  _viewInitialized$ = new BehaviorSubject<boolean>(false);
  layoutIsReady = false;

  viewManager = new ViewManager();
  views: ComponentViewDefinition[];

  _actionsEnabled = false;


  stateMembers = [[], []];
  _navigation = new PanelNavigationState();
  _outlet: string = null;
  _routingPath: string;
  _tst = new Date();

  private _subscriptions = new Subscription();

  static nextRuntimeActionId = 1;

  static activePanel = null;

  @ViewChildren(ComponentHostComponent, { emitDistinctChangesOnly: false }) childComponents: QueryList<ComponentHostComponent>;
  componentsController = new ComponentsController();

  constructor(
    public app: AppContainer,
    protected nvg: CompositeNavigation,
    protected activatedRoute: ActivatedRoute,
    protected cdr: ChangeDetectorRef
  ) { }

  ngOnInit() {
    this._actionsEnabled = false;

    this.viewManager.start(this.views);

    this._subscriptions.add(
      this.activatedRoute.params.subscribe(async (params) => {
        // Route parameters have changed
        this._actionsEnabled = false;

        if (this.nvg.restoringComponent()) {
          // Restoring component
          let stateFound = false;
          if (!this._dynRefresh) {
            // Pending action -> must wait for completion
            await this.nvg.waitForActionToComplete();
            const nid = this.nvg.getRestoringNID();
            stateFound = this.nvg.restoreState(
              this.constructor.name,
              this._outlet,
              nid,
              this
            );

            if (!stateFound) {
              // can't restore (no state found) -> init and load panel
              this.initState(this._isDynComponent ? this._dynCompoParameters : null);
              await this.loadPanel();
            } else {
              this.viewManager.update(this.Mode);
            }
          }
        } else {
          // Navigation state
          this._navigation = new PanelNavigationState();
          this._navigation.iid = NavigationHelper.newComponentInstanceId();
          this._navigation.fromAction = this.nvg.getRunningAction();
          this._navigation.outlet = this._outlet;
          this._navigation.nid = this.nvg.getNavigationId();
          this._navigation.navigationCommand = this.nvg.getNavigationCommand();
          PanelComponent.activePanel = this;

          if (this.sectionType === "inline") {
            this.initState(params);
          }
          else if (this.isOutletContent()) {
            // Push new navigation to outlet stack
            const nvgCommand: Array<any> = [this._routingPath];
            if (!NavigationHelper.emptyParams(params)) {
              nvgCommand.push(params);
            }
            this.nvg.pushToOutlet(
              this.nvg.getNavigationId(),
              this._outlet,
              nvgCommand
            );
            this.initState(this._isDynComponent ? this._dynCompoParameters : params);
          }
          else if (this._isDynComponent) {
            this.initState(this._dynCompoParameters);
          }
          else {
            this.initState(null);
          }

          await this.initPanel();
          await this._ClientStart();
          await this.loadPanel();
        }

        this.cdr.markForCheck();

        this._dynRefresh = false;
        this._actionsEnabled = true;
        this.viewManager.bindApplicationBar();
        this.initControllers();
        this.nvg.saveState(
          this.constructor.name,
          this._outlet,
          this.nvg.getNavigationId(),
          this
        );
        this._dataReady$.next(true);
      })
    );

    this._subscriptions.add(
      combineLatest([this._dataReady$, this._viewInitialized$]).subscribe(
        (params) => {
          // Data ready and view initialized -> Start inner components
          if (params[0] === true && params[1] === true) {
            this.layoutIsReady = true;
            this.componentsController.start(this.childComponents);
            this._dataReady$.complete();
            this._viewInitialized$.complete();
          }
        }
      )
    );

    this._subscriptions.add(
      this.app.uiContextSubject.subscribe(() => {
        this.Refresh();
      }
      ));

    this.startEvents();
  }

  ngOnChanges() {
  }

  ngAfterViewInit() {
    this._viewInitialized$.next(true);
  }

  ngOnDestroy() {
    this._subscriptions.unsubscribe();
    if (this.viewManager) this.viewManager.end();
    if (this.componentsController) this.componentsController.end();
    this.endEvents();
  }

  async _ClientStart(): Promise<any> { }
  async Refresh(type?: string) { } // TODO: Support type='keep'
  initState(params) {
    this.loadParams(params);
  }
  loadParams(params) { }
  async initPanel() { }
  async loadPanel() { }
  initControllers() { }
  startEvents() { }
  endEvents() { }

  async __Cancel() {
    const __aSt = this.startAction();
    await this.cancel(__aSt);
    this.endAction(__aSt);
  }

  private isOutletContent() {
    return (
      !this._isDynComponent || OutletsHelper.isSecondaryOutlet(this._outlet)
    );
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Panel loaded as dynamic component
  //
  initDynComponent(outlet: string, parameters: any) {
    this._isDynComponent = true;
    this._dynCompoParameters = parameters;
    this.showAsCard = false;
    this._outlet = outlet;
  }

  refreshDynComponent(outlet: string, parameters: any) {
    this.initDynComponent(outlet, parameters);
    this._dynRefresh = true;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Events processing
  //
  callAction = async (actionMethod: any, ...parms) => {
    await actionMethod.bind(this)(...parms);
  };

  startAction(): ActionState {
    const act = new ActionState();
    act.iid = this._navigation.iid;
    act.navigationState = this._navigation;
    act.actionId = PanelComponent.nextRuntimeActionId++;
    return act;
  }

  endAction(act: ActionState) {
    this.nvg.updateStateByIID(act.iid, this);
    this.nvg.endAction(act);
  }

  getRunningAction(outlet: string) { }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Navigation and state management
  //
  async navigate(nvgCommand: any, act?: ActionState): Promise<any> {
    return this.nvg.navigate(nvgCommand, act);
  }

  async navigateForResult(nvgCommand: any, act?: ActionState): Promise<any> {
    return new Promise<any>(async (complete) => {
      let result = await this.nvg.navigateForResult(nvgCommand, act);
      this.nvg.restoreStateByIID(act.iid, this);
      complete(result);
    });
  }

  async navigateForResultWithExtras(nvgCommand: any, extras: any, act?: ActionState): Promise<any> {
    return new Promise<any>(async (complete) => {
      let result = await this.nvg.navigateForResultWithExtras(nvgCommand, extras, act);
      this.nvg.restoreStateByIID(act.iid, this);
      complete(result);
    });
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // UIModel properties
  //   UImodel properties update method
  updateUIModel(uiModel: any, defaultValues: any = []) {
    for (let i = 0; i < defaultValues.length; i++) {
      PanelComponent.setProperty(defaultValues[i][0], defaultValues[i][1], defaultValues[i][2], uiModel);
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Dynamic properties
  //   dynamic properties are set in the server and arrive in the main dataprovider payload
  updateGxdynprops(entity: any, uiModel: any) {
    if (entity.Gxdynprop) {
      const dynProps = JSON.parse(entity.Gxdynprop);
      for (let i = 0; i < dynProps.length; i++) {
        PanelComponent.setProperty(PanelComponent.toControlName(dynProps[i][0]), dynProps[i][1], dynProps[i][2], uiModel);
      }
    }
    if (entity.Gxdyncall) {
      const dynCalls = JSON.parse(entity.Gxdyncall);
      for (let i = 0; i < dynCalls.length; i++) {
        this.nvg.dynamicCall(null, dynCalls[i]);
      }
    }
  }

  static setProperty(ctrlName: string, propName1: string, propValue: any, uiModel: any): boolean {
    let handled = false;
    const propName = propName1[0].toLowerCase() + propName1.substring(1);
    if (uiModel[ctrlName]) {
      if (
        typeof propValue === "string" &&
        (propValue.toLowerCase() === "false" ||
          propValue.toLowerCase() === "true")
      ) {
        uiModel[ctrlName][propName] = propValue.toLowerCase() === "true";
      } else if (Array.isArray(propValue)) {
        uiModel[ctrlName][propName] = GxCollectionData.fromArray(propValue);
      } else {
        uiModel[ctrlName][propName] = propValue;
      }
      handled = true;
    }

    if (!handled) {
      console.log(
        `Could not setProperty Control property: '${ctrlName}.${propName}'`
      );
    }
    return handled;
  }

  static toControlName(propControl: string): string {
    let ctrlName = propControl;
    if (ctrlName.startsWith("&")) {
      ctrlName = ctrlName.substring(1);
    }
    return (
      "ctrl" + ctrlName[0].toUpperCase() + ctrlName.substring(1).toLowerCase()
    );
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Translations
  //   Translation support for text and images

  translate(key: string) {
    return this.app.translate(key);
  }

  getImageSource(key: string) {
    return this.app.getImageSource(key);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Outlet management

  get outlet(): string {
    return this._outlet;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // SD Actions

  async return(result: any, act: any) {
    await this.nvg.back(act, result);
  }

  async cancel(act: any) {
    await this.nvg.back(act);
  }

  returnTo(url: string, act: any): void {
    this.nvg.backTo(url, act);
  }

  cancelTo(url: string, act: any): void {
    this.nvg.backTo(url, act);
  }

  goHome(act: any): void {
    this.nvg.goHome(act);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Interop

  OpenInBrowser(url: string) {
    window.location.href = url;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Composite events processing

  isCompositeExecution = false;

  processCompositeError(e: Error) {
    console.error(e.toString());
    if (!(e as CompositeCancel)) {
      this.app.setMessage(e.toString());
    }
  }

  compositeExecution(result, isComposite = true) {
    if (isComposite && this.app.err > 0) {
      throw new CompositeCancel("Composite cancelled");
    }
    return result;
  }

  raiseCompositeMessage(messages: any) {
    if (messages.length > 0) {
      this.app.setError(messages[0].Type);
      this.app.setMessage(messages[0].Description);
      throw new CompositeCancel("Composite cancelled");
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Hidecode

  validateHidecode(result: string[]): string {
    if (result.length > 1 && result[1] === "101") {
      msg(this.translate("GXM_keynfound"));
    } else {
      return result[0];
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Athentication

  public async checkAuthentication(gam: GAMService) {
    if (!gam.isLogged()) {
      if (Settings.GAM_ANONYMOUS_USER) {
        await gam.registerAnonymous();
      } else if (Settings.GAM_CLIENT_LOGIN) {
        const __aSt = this.startAction();
        await this.navigateForResult([Settings.GAM_CLIENT_LOGIN], __aSt);
        this.endAction(__aSt);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Methods used in templates

  dateToISOString = TypeConversions.UIDateToISOString;
  timeToISOString = TypeConversions.UITimeToISOString;
  datetimeToISOString = TypeConversions.UIDatetimeToISOString;
  dateFromISOString = TypeConversions.UIDateFromISOString;
  GUID = stdGUID;
  createImageFromURL = this.app.createImageFromURL;
  createBinaryFromURL = this.app.createBinaryFromURL;
}

/**
 * Composite cancel management
 */
export class CompositeCancel extends Error { }

/**
 * Panel GX components controller
 */
export class ComponentsController {
  state: any;
  components: QueryList<ComponentHostComponent>;

  _subscriptions = new Subscription();

  setState(s: any) {
    this.state = s;
  }

  start(c: QueryList<ComponentHostComponent>) {
    this.components = c;
    this.components.setDirty();
    this._subscriptions = this.components.changes.subscribe(
      (comps: QueryList<ComponentHostComponent>) => {
        // DOM changed
        if (this.state !== undefined) {
          comps.forEach((comp) => {
            comp.start(this.state);
          });
        }
      }
    );
  }

  end() {
    this._subscriptions.unsubscribe();
  }

  async refresh(componentId: string, data: any) {
    if (this.components) {
      // Started
      await this.components.forEach(async (comp) => {
        if (comp.id === componentId) {
          await comp.refresh(data);
          return;
        }
      });
    }
  }
}
