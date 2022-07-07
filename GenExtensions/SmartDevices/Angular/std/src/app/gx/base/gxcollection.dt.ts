export class GxCollectionData<T> extends Array<T> {
  CurrentItem: T;

  constructor() {
    super();
  }

  get Count(): number {
    return this.length;
  }

  add(element: any, position?: number) {
    if (position) {
      this.splice(position - 1, 0, element);
    } else {
      this.push(element);
    }
  }

  item(ix: number): any {
    return this[ix - 1];
  }

  static add(array: Array<any>, element: any, position?: number) {
    if (position) {
      array.splice(position - 1, 0, element);
    } else {
      array.push(element);
    }
  }

  static fromArray<T>(array: Array<T>): GxCollectionData<T> {
    const collection = new GxCollectionData<T>();
    for (const itm of array) {
      collection.add(itm);
    }
    return collection;
  }

  static clear(array: Array<any>) {
    array.splice(0, array.length);
  }

  static clone(array: Array<any>): Array<any> {
    return array.slice(0);
  }

  static indexOf(array: Array<any>, element: any): number {
    return array.indexOf(element) + 1;
  }

  static item(array: Array<any>, ix: number): any {
    return array[ix - 1];
  }

  static remove(array: Array<any>, ix: number) {
    array.splice(ix - 1, 1);
  }

  static tojson(array: Array<any>): any {
    return JSON.stringify(array);
  }

  static fromjson(obj: any, json: string) {
    for (const item of JSON.parse(json)) {
      obj.add(item)
    }
  }
}

export class GxCollectionOperation {
  type: string;
  element: any;
  info: Array<any>;

  constructor(t, e, i) {
    this.type = t;
    this.element = e;
    this.info = i;
  }

  static createAddOperation(element: any, ix: number) {
    return new GxCollectionOperation('add', element, ix ? [ix] : null);
  }

  static apply(collection: any, operation: GxCollectionOperation): any {
    if (operation.type === 'add') {
      collection.add(operation.element, operation.info);
      return collection;
    }
  }
}