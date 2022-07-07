import { UICommonActionsElement } from './ui-common-actions';

export interface ILayoutGroupElement {

  caption: string;
  class: string;
  visible: boolean;
  enabled: boolean;
}

export class UILayoutGroupElement extends UICommonActionsElement implements ILayoutGroupElement {

  caption = null;
  class = null;
  visible = null;
  enabled = null;

}
