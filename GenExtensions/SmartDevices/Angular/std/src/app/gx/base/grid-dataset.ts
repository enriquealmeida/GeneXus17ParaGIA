import { GxCollectionData } from "app/gx/base/gxcollection.dt";
import { UIListElementItem } from "app/gx/ui/model/ui-list";
import { UIListLoadingState } from "app/gx/ui/model/ui-list";
import { of, timer } from "rxjs";
import { takeWhile } from "rxjs/operators";
import { GridControllerData } from "app/gx/base/grid-data";
import { PanelComponent } from "app/gx/base/panel.component";
import { gxRowNumberId } from "app/gx/base/grid-data";

export abstract class GridDataset<D, U extends UIListElementItem> {
  gridName: string;
  gridItemType: { new(): U };
  identityName: string;
  typeMap = {};

  gridData = new GxCollectionData<D>();
  gridControllerData = new GridControllerData<U>();

  uiUpdater = null;
  gridDataUI = new GxCollectionData<D>();
  gridData$ = of(this.gridDataUI);


  constructor(gm: string, git: { new(): U }, tm: any, identityName?: string) {
    this.gridName = gm;
    this.gridItemType = git;
    this.typeMap = tm;
    this.identityName = identityName;
  }

  identify = (index: number, item: UIListElementItem) => {
    return this.firstDefinedValue(item[this.identityName], item[gxRowNumberId], index);
  }

  firstDefinedValue = (...args: any[]) => {
    for (var i = 0; i < args.length; i++) {
      if (args[i] !== undefined)
        return args[i];
    }
    return null;
  }

  balanceModels() {
    let dataLength = this.gridData.length;
    let controllerLength = this.gridControllerData.length;
    for (let i = controllerLength; i < dataLength; i++) {
      let element = this.createDataElement<U>([], [], this.getItemType(this.gridName));
      this.gridControllerData.push(element);
    }
  }

  initState() {
    this.gridData = new GxCollectionData<D>();
    this.gridControllerData = new GridControllerData<U>();
    this.initUI();
  }

  initUI() {
    this.gridDataUI = new GxCollectionData<D>();
    this.gridData$ = of(this.gridDataUI);
  }

  pushToUI() {
    if (this.uiUpdater) {
      this.uiUpdater.unsubscribe();
    }
    const limit = this.gridData.length;

    this.gridDataUI = this.gridData.slice(0, 50) as GxCollectionData<D>;

    this.gridDataUI = this.gridData;
    this.gridData$ = of(this.gridDataUI);

    if (this.gridDataUI.length === this.gridData.length) {
      return;
    }

    this.uiUpdater = timer(1500, 500).pipe(takeWhile(() => this.gridDataUI.length < this.gridData.length)).subscribe(() => {
      const loaded = this.gridDataUI.length;
      for (let i = loaded; i < this.gridData.length && i - loaded < limit; i++) {
        this.gridDataUI.push(this.gridData[i]);
      }
    });

  }

  refreshUI() {
    this.initUI();
    this.pushToUI();
  }

  createDataElement<E>(defaultProps, dynpropsObject, c: { new(): E }) {
    const element = new c();
    this.applyElementProperties(defaultProps, dynpropsObject, element);
    return element;
  }

  applyElementProperties(defaultProps, dynpropsObject, element) {
    // Apply defaults
    for (let j = 0; j < defaultProps.length; j++) {
      this.setGridControlProperty(defaultProps, [defaultProps[j][0], defaultProps[j][1], defaultProps[j][2]], element);
    }
    // Apply dyanamic properties
    for (let j = 0; j < dynpropsObject.length; j++) {
      this.setGridControlProperty(defaultProps, [PanelComponent.toControlName(dynpropsObject[j][0]), dynpropsObject[j][1], dynpropsObject[j][2]], element);
    }
  }

  setGridControlProperty(
    defaultProps: any,
    propToApply: any,
    uiModel: UIListElementItem
  ): boolean {
    let handled = true;
    const propName = propToApply[1].toLowerCase();
    switch (propName) {
      case "itemlayout":
        uiModel.Itemlayout = propToApply[2];
        break;
      case "gxprops":
        const grid = propToApply[0];
        const gridUiModelName = this.toGridUiModelName(propToApply[0]);
        uiModel[gridUiModelName] = new GridControllerData<any>();
        const gridPropToApply = this.jsonParseEx(propToApply[2]);
        for (let i = 0; i < gridPropToApply.length; i++) {
          let element = this.createDataElement<any>(defaultProps, gridPropToApply[i], this.getItemType(grid));
          this.applyElementProperties(defaultProps, gridPropToApply[i], element);
          uiModel[gridUiModelName].push(element);
        }
        break;
      default:
        handled = PanelComponent.setProperty(propToApply[0], propToApply[1], propToApply[2], uiModel);
    }
    return handled;
  }

  getItemType(typeName: string) {
    if (typeName === this.gridName) {
      return this.gridItemType;
    }

    let ret = this.typeMap[typeName];
    if (!ret) {
      ret = Object;
    }
    return ret.itemType;
  }

  getControllerType(typeName: string) {
    let ret = this.typeMap[typeName];
    if (!ret) {
      return null;
    }
    return ret.controllerType;
  }

  toGridUiModelName(name: string): string {
    return "_" + PanelComponent.toControlName(name) + "Items";
  }

  jsonParseEx(json: string) {
    let Gxprops_pos = json.indexOf("Gxprops");
    if (Gxprops_pos >= 0) {
      // Fix nested Gxprops
      let balancerResult = this.findBalanced(json, Gxprops_pos, "[", "]");
      balancerResult[2] = JSON.parse(balancerResult[2]).toString();
      balancerResult[2] = balancerResult[2].replace(/"/g, "\\\"");
      json = json.slice(0, balancerResult[0] + 1) + balancerResult[2] + json.slice(balancerResult[1]);
    }
    return JSON.parse(json);
  }

  findBalanced(s: string, start_from: number, start_char: string, end_char: string): Array<any> {
    let cnt = 0;
    let started = false;
    let start_at = -1;
    let end_at = -1;
    let result = "";
    let i = start_from;
    while (i < s.length) {
      let c = s[i];
      if (c === start_char && !started) {
        cnt++;
        started = true;
        start_at = i;
      } else if (c === start_char) {
        cnt++;
      } else if (started && c === end_char) {
        cnt--;
        if (cnt === 0) {
          end_at = i;
        }
      }
      if (started) {
        result += c;
      }
      if (start_at >= 0 && end_at >= 0) {
        break;
      }
      i++;
    }
    return [start_at, end_at, result];
  }
}

export class SdtGridController<D, U extends UIListElementItem> extends GridDataset<D, U>
{
  loadFromSDT(data: GxCollectionData<any>, controllerData: GridControllerData<U>, dynprops: string, defaultProps: any = []) {
    this.gridData = data;
    if (controllerData) {
      this.gridControllerData = controllerData;
      if (dynprops) {
        const collectionDynProps = JSON.parse(dynprops);
        this.gridControllerData.splice(0, this.gridControllerData.length);
        for (let i = 0; i < this.gridData.length; i++) {
          const dynpropsItem = collectionDynProps.length > i ? collectionDynProps[i] : [];
          const dynpropsObject = this.jsonParseEx(dynpropsItem);
          const element = this.createDataElement<U>(defaultProps, dynpropsObject, this.getItemType(this.gridName));
          this.gridControllerData.push(element);
        }
      }
    }
    this.gridData.forEach((item, index) => {
      if (typeof item === 'object') {
        item[gxRowNumberId] = index
      }
    });
  }

  setState(data: GxCollectionData<D>, controllerData: GridControllerData<U>) {
    this.gridData = data;
    this.gridControllerData = controllerData;
    this.refreshUI();
  }
}

export class DataGridController<D, U extends UIListElementItem> extends GridDataset<D, U>
{
  loadFromData(data: GxCollectionData<any>, controllerData = null, defaultProps: any = [], breakBy: string[] = null) {
    this.gridData = data;
    const dataWithDyn = data;
    if (controllerData) {
      this.gridControllerData = controllerData;
      const from = this.gridControllerData.length;
      for (let i = from; i < this.gridData.length; i++) {
        const dynpropsElement = dataWithDyn[i].Gxdynprop ? dataWithDyn[i].Gxdynprop : "[]";
        dataWithDyn[i].Gxdynprop = null; // Reset to prevent reloading on state recovery
        const dynProps = this.jsonParseEx(dynpropsElement);
        const element = this.createDataElement<U>(defaultProps, dynProps, this.getItemType(this.gridName));
        if (breakBy) {
          const currBreakValue = [];
          breakBy.forEach((bb) => currBreakValue.push(this.gridData[i][bb]));
          if (!this.compareBreakBy(currBreakValue, this.gridControllerData.lastBreakValue)) {
            element.Break = true;
            this.gridControllerData.lastBreakValue = currBreakValue;
          }
        }
        for (const pty in dataWithDyn[i]) {
          if (dataWithDyn[i][pty] && dataWithDyn[i][pty].constructor.name === 'Array') {
            // Sub-level
            const ctrlName = PanelComponent.toControlName(pty);
            const gridUiModelName = this.toGridUiModelName(pty);
            const controller = this.getControllerType(ctrlName);
            if (controller) {
              controller.loadFromLevel(dataWithDyn[i][pty], element[gridUiModelName]);
            }
          }
        }
        this.gridControllerData.push(element);
      }
      this.gridData.forEach((item, index) => {
        if (typeof item === 'object') {
          item[gxRowNumberId] = index
        }
      });
      this.pushToUI();
    }
  }

  setState(data: GxCollectionData<D>, controllerData: GridControllerData<U>) {
    this.gridData = data;
    this.gridControllerData = controllerData;
    this.refreshUI();
  }

  initPaging() {
    this.gridControllerData.start = 0;
    this.gridControllerData.count = 0;
    this.gridControllerData.loadingState = UIListLoadingState.loading;
  }

  updatePaging(
    gridCurrentRecordCount: number,
    gridPagingCount: number,
    loadingState: UIListLoadingState,
    event: any
  ) {
    this.gridControllerData.start = gridCurrentRecordCount;
    this.gridControllerData.count = gridCurrentRecordCount;
    this.gridControllerData.loadingState = loadingState;
    if (event && event.target && event.target.complete) {
      event.target.disabled = gridCurrentRecordCount % gridPagingCount !== 0;
      event.target.complete();
    }
  }

  compareBreakBy(a1: string[], a2: string[]) {
    if (!a1 || !a2)
      return false;

    if (a1.length !== a2.length)
      return false;

    for (let i = 0, l = a1.length; i < l; i++) {
      if (a1[i].toString() !== a2[i].toString()) {
        return false;
      }
    }
    return true;
  }

}

export class DataGridLevelController<D, U extends UIListElementItem> extends GridDataset<D, U>
{
  loadFromLevel(data: GxCollectionData<any>, controllerData = null, defaultProps: any = []) {
    this.gridData = data;
    const dataWithDyn = data;
    if (controllerData) {
      this.gridControllerData = controllerData;
      for (let i = 0; i < this.gridData.length; i++) {
        let dynpropsElement = dataWithDyn[i].Gxdynprop ? dataWithDyn[i].Gxdynprop : "[]";
        dataWithDyn[i].Gxdynprop = null; // Reset to prevent reloading on state recovery
        let dynProps = this.jsonParseEx(dynpropsElement);
        let element = this.createDataElement<U>(defaultProps, dynProps, this.getItemType(this.gridName));
        for (const pty in dataWithDyn[i]) {
          if (dataWithDyn[i][pty] && dataWithDyn[i][pty].constructor.name === 'Array') {
            // Sub-level
            const ctrlName = PanelComponent.toControlName(pty);
            const gridUiModelName = this.toGridUiModelName(pty);
            const controller = this.getControllerType(ctrlName);
            if (controller) {
              controller.loadFromLevel(dataWithDyn[i][pty], element[gridUiModelName]);
            }
          }
        }
        controllerData.push(element);
      }
      this.gridData.forEach((item, index) => {
        if (typeof item === 'object') {
          item[gxRowNumberId] = index
        }
      });
      this.pushToUI();
    }
  }

  setState(data: GxCollectionData<D>, controllerData: GridControllerData<U>) {
    this.gridData = data;
    this.gridControllerData = controllerData;
    this.refreshUI();
  }
}