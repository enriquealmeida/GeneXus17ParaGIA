import { Geography } from '@genexus/web-standard-functions/dist/lib-esm/types/geography';

export interface IMapElement {

  class: string;
  visible: boolean;
  enabled: boolean;

  currentItem: any;

  mapCenter: any;
  mapZoom: any;
  selectionLayer: boolean;

  onSelectionChanged(eventInfo: any);
  onControlValueChanged(eventInfo: any, geoLocation: any);
  onControlValueChanging(eventInfo: any, geoLocation: any);

  setSelectionChangedAction(action: any);
  setControlValueChangedAction(eventInfo: any);
  setControlValueChangingAction(eventInfo: any);
  setRefreshAction(action: any);

}

export class UIMapElement implements IMapElement {

  class = null;
  visible = null;
  enabled = null;

  currentItem: any;

  mapCenter: any;
  mapZoom: any;
  selectionLayer = null;

  onSelectionChangedAction: any;
  onControlValueChangedAction: any;
  onControlValueChangingActionAction: any;
  onRefreshAction: any;

  onSelectionChanged(eventInfo: any) {
    if (this.onSelectionChangedAction) {
      this.onSelectionChangedAction();
    }
  }

  onControlValueChanged(eventInfo: any) {
    const geoLocation = eventInfo.detail;

    if (this.onControlValueChangedAction) {
      this.onControlValueChangedAction(new Geography(geoLocation));
    }
  }

  onControlValueChanging(eventInfo: any) {
    const geoLocation = eventInfo.detail;

    if (this.onControlValueChangingActionAction) {
      this.onControlValueChangingActionAction(new Geography(geoLocation));
    }
  }

  setSelectionChangedAction(action: any) {
    this.onSelectionChangedAction = action;
  }

  setControlValueChangedAction(action: any) {
    this.onControlValueChangedAction = action;
  }

  setControlValueChangingAction(action: any) {
    this.onControlValueChangingActionAction = action;
  }

  setRefreshAction(action: any) {
    this.onRefreshAction = action;
  }

  setmapcenter(geopoint: Geography, zoom = 1) {
    this.mapCenter = `${geopoint.latitude},${geopoint.longitude}`;
    this.mapZoom = zoom;
  }

  refresh() {
    if (this.onRefreshAction) {
      this.onRefreshAction();
    }
  }
}