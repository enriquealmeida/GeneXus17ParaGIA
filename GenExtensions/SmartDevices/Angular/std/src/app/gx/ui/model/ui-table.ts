import { UICommonActionsElement } from './ui-common-actions';

export interface ITableElement {

  class: string;
  visible: boolean;
  enabled: boolean;

}

export class UITableElement extends UICommonActionsElement implements ITableElement {

  class = null;
  visible = null;
  enabled = null;

}
