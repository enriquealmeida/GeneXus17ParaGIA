import { UICommonActionsElement } from './ui-common-actions';

export interface IGroupElement {

  class: string;
  caption: string;
  visible: boolean;
  enabled: boolean;

  show();
  hide();
}

export class UIGroupElement extends UICommonActionsElement implements IGroupElement {

  class = null;
  caption = null;
  visible = null;
  enabled = null;

  show() {
    this.visible = true;

  }
  hide() {
    this.visible = false;
  }

}
