import { UICommonActionsElement } from './ui-common-actions';

export interface ITextblockElement{

  class: string;
  caption: string;
  visible: boolean;
  enabled: boolean;

}

export class UITextblockElement extends UICommonActionsElement implements ITextblockElement {

  class = null;
  caption = null;
  visible = null;
  enabled = null;

}

