import { UICommonActionsElement } from './ui-common-actions';

export interface IEditElement {

  class: string;
  caption: string;
  visible: boolean;
  enabled: boolean;
  isPassword: boolean;

  onControlValueChanged(eventInfo: any);
  onControlValueChanging(eventInfo: any);

  setControlValueChangedAction(eventInfo: any);
  setControlValueChangingAction(eventInfo: any);
}

export class UIEditElement extends UICommonActionsElement implements IEditElement {

  class = null;
  caption = null;
  visible = null;
  enabled = null;
  isPassword = null;

  onControlValueChangedAction = null;
  onControlValueChangingAction = null;

  onControlValueChanged(eventInfo: any) {
    if (this.onControlValueChangedAction) {
      if (typeof(eventInfo.stopPropagation) === 'function') {
        eventInfo.stopPropagation();
      }
      this.onControlValueChangedAction();
    }
  }

  onControlValueChanging(eventInfo: any) {
    if (this.onControlValueChangingAction) {
      this.onControlValueChangingAction();
    }
  }

  setControlValueChangedAction(action: any) {
    this.onControlValueChangedAction = action;
  }
  setControlValueChangingAction(action: any) {
    this.onControlValueChangingAction = action
  }

}
