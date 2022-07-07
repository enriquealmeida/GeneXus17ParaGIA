import { UIEditElement } from './ui-edit';
import { GxCollectionData } from 'app/gx/base/gxcollection.dt';

export interface ISelectElement {
  addItem(value: any, description: any);
  clear();
  removeItem(value: any);
}

export class UISelectElement extends UIEditElement implements ISelectElement {
  items = new GxCollectionData<[]>();

  addItem(value: any, description: any) {
    GxCollectionData.add(this.items, [value, description]);
  }

  removeItem(value: any) {
    for (let i = 1; i <= this.items.Count; i++) {
      if (GxCollectionData.item(this.items, i)[0] === value) {
        GxCollectionData.remove(this.items, i);
        return;
      }
    }
    GxCollectionData.remove(this.items, value);
  }

  value(ix: number) {
    return GxCollectionData.item(this.items, ix)[0];
  }

  text(ix: number) {
    return GxCollectionData.item(this.items, ix)[1];
  }

  clear() {
    GxCollectionData.clear(this.items);
  }

}