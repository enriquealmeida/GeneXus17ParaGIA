import { UICommonActionsElement } from './ui-common-actions';

export interface IImageElement {

  class: string;
  caption: string;
  visible: boolean;
  enabled: boolean;

}

export class UIImageElement extends UICommonActionsElement implements IImageElement {

  class = null;
  caption = null;
  visible = null;
  enabled = null;

}

