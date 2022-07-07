import { GxCollectionData } from "app/gx/base/gxcollection.dt";
import { UIListLoadingState } from "app/gx/ui/model/ui-list";

export class GridControllerData<U> extends GxCollectionData<U> {
  start = 0;
  count = 0;
  searchText = '';
  loadingState: UIListLoadingState = UIListLoadingState.loading;
  CurrentItem: U;
  lastBreakValue: Array<any>;
}

export class GridElementData<D, U> {
  data: D;
  uiModel: U;
}

export class LevelConfig<I, C> {
  itemType: { new(): I; };
  controllerType: C;

  constructor(it: { new(): I; }, ct: C) {
    this.itemType = it;
    this.controllerType = ct;
  }
}

export const gxRowNumberId: string = '_gxIndex';