import { UIEditElement } from './ui-edit';

export interface IScannerElement {
  setCodeReadAction(action: any);
}


export class UIScannerElement extends UIEditElement implements IScannerElement {
  setCodeReadAction(action: any) {
  }
}