import { PanelNavigationState } from 'app/gx/navigation/panel-navigation-state.dt';

export class NavigationState {
  current = {};

  save(iid: number, comp: IStateContainer) {
    if (comp) {
      const state = {
        _name: comp.constructor.name,
        _tst: comp._tst,
        navigation: comp._navigation
      };
      for (const prop of comp.stateMembers[0].concat(comp.stateMembers[1])) {
        state[prop] = comp[prop];
      }
      this.current[iid] = state;
    }
  }

  update(iid: number, comp: IStateContainer) {
    if (comp) {
      const state = this.current[iid];
      if (state) {
        for (const prop of comp.stateMembers[1]) {
          state[prop] = comp[prop];
        }
      }
    }
  }

  restore(iid: number, comp: IStateContainer): boolean {
    const state = this.current[iid];
    if (state) {
      for (const prop of comp.stateMembers[0].concat(comp.stateMembers[1])) {
        comp[prop] = state[prop];
      }
      comp._navigation = state.navigation;
      return true;
    }
    return false;
  }

  getInstance(iid: number): IStateContainer {
    return this.current[iid];
  }

}

export interface IStateContainer {
  stateMembers: Array<any>;
  _navigation: PanelNavigationState;
  _outlet: string;
  _tst: Date;
}