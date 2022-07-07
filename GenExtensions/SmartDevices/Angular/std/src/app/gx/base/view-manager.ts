import { Observable, Subscription, fromEvent } from "rxjs";
import { Settings } from "app/app.settings";
import { GeneXusClientClientInformation } from "@genexus/web-standard-functions/dist/lib-esm/gxcore/client/client-information";

export enum NavigationStyle {
  Cascade = "cascade",
  Flip = "flip",
  Slide = "slide",
  Split = "split",
}

export class ViewManager {
  resizeObservable: Observable<Event>;
  resizeSubscription: Subscription;

  views: ComponentViewDefinition[] = [];
  view: string = null;
  type = "";

  oldMode = "";

  start(viewVariants: ComponentViewDefinition[]) {
    this.views = viewVariants || [];
    this.updateViewManager();
    this.resizeObservable = fromEvent(window, "resize");
    this.resizeSubscription = this.resizeObservable.subscribe(evt => {
      this.updateViewManager();
    });
  }

  bindApplicationBar() {
    const currentView = this.selectView();
    const currentNavigationStyle = this.selectNavigationStyle(currentView);

    if (currentView !== undefined) {
      if (currentView.appBarBindFn !== undefined) {
        currentView.appBarBindFn(currentNavigationStyle);
      }
    }
  }

  update(m: string) {
    if (this.oldMode !== m) {
      if (m !== "") {
        this.type = "edit";
      } else {
        this.type = "";
      }
      this.updateViewManager();
      this.oldMode = m;
    }
  }

  updateViewManager() {
    const currentView = this.selectView();
    const currentNavigationStyle = this.selectNavigationStyle(currentView);

    if (currentView !== undefined && currentView.name !== this.view) {
      if (currentView.appBarInitFn !== undefined) {
        // Set view to show
        this.view = currentView.name;
        // Init view model
        currentView.appBarInitFn();
        // Setup and bind view actions
        currentView.appBarBindFn(currentNavigationStyle);
      }
    }
  }

  selectView() {
    const forWeb = GeneXusClientClientInformation.platformName() === "Web";
    return this.views
      .filter((platform) => { return forWeb && platform.os === "Web" || !forWeb && platform.os !== "Web" })
      .filter((platform) => this.type === "" || platform.type === this.type)
      .find(viewMatchesViewport(window))
      || this.views
        .filter((platform) => this.type === "" || platform.type === this.type)
        .find(viewMatchesViewport(window))
      || this.views[0];
  }

  selectNavigationStyle(currentView: ComponentViewDefinition) {
    return currentView !== undefined &&
      currentView.navigationStyle !== "default"
      ? currentView.navigationStyle
      : Settings.DEFAULT_NAVIGATION_STYLE;
  }

  end() {
    if (this.resizeSubscription) {
      this.resizeSubscription.unsubscribe();
    }
    const viewDef = this.getViewDefinition();
    if (viewDef !== undefined && viewDef.appBarResetFn !== undefined) {
      viewDef.appBarResetFn();
    }
  }

  getUIModelDefaults(containerName?: string) {
    const viewDef = this.getViewDefinition();
    if (viewDef !== undefined && viewDef.appBarResetFn !== undefined) {
      return viewDef.UIModelDefaults(containerName, this.selectNavigationStyle(viewDef));
    }
    return [];
  }

  getViewDefinition(): ComponentViewDefinition {
    return this.views.find((viewDef) => viewDef.name === this.view);
  }
}

const viewMatchesViewport = (win: Window) => (platform: ViewBounds) => {
  const shortestBound = Math.min(win.innerWidth, win.innerHeight);
  const longestBound = Math.max(win.innerWidth, win.innerHeight);
  return (
    (platform.minShortestBound > 0
      ? platform.minShortestBound < shortestBound
      : true) &&
    (platform.maxShortestBound > 0
      ? platform.maxShortestBound > shortestBound
      : true) &&
    (platform.minLongestBound > 0
      ? platform.minLongestBound < longestBound
      : true) &&
    (platform.maxLongestBound > 0
      ? platform.maxLongestBound > longestBound
      : true)
  );
};

export interface ViewBounds {
  minShortestBound: number;
  maxShortestBound: number;
  minLongestBound: number;
  maxLongestBound: number;
}

export interface ComponentViewDefinition extends ViewBounds {
  name: string;
  type: string;
  os: string;
  minShortestBound: number;
  maxShortestBound: number;
  minLongestBound: number;
  maxLongestBound: number;
  navigationStyle: string;
  appBarInitFn?: () => void;
  appBarBindFn?: (navigationStyle: string) => void;
  appBarResetFn?: () => void;
  UIModelDefaults?: (containerName: string, navigationStyle: string) => any;
}

export interface ApplicationViewDefinition extends ViewBounds {
  navigationStyle: NavigationStyle | string;
}
