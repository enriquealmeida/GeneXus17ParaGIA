import { UIEditElement } from './ui-edit';

export interface IFacebookButtonElement {
  setOnUserInfoUpdatedAction(action: any);
  setOnUserLoggedInAction(action: any);
  setOnUserLoggedOutAction(action: any);
  setOnErrorAction(action: any);
}


export class UIFacebookButtonElement extends UIEditElement implements IFacebookButtonElement {

  setOnUserInfoUpdatedAction(action: any) {
  }

  setOnUserLoggedInAction(action: any) {
  }

  setOnUserLoggedOutAction(action: any) {
  }

  setOnErrorAction(action: any) {
  }
}