export class GxImage {
  id: string;
  uri: string;

  constructor() {
    this.id = "";
    this.uri = "";
  }

  toString() {
    return this.uri;
  }

  fromString(url: string) {
    this.uri = url;
  }

  isEmpty() {
    return !this.uri && !this.id;
  }

  setEmpty() {
    this.uri = "";
    this.id = "";
  }

  toBase64String() {
    console.log('toBase64String - Not implemented')
  }

  fromBase64String(s: string) {
    console.log('fromBase64String - Not implemented')
  }

  static createFromURL(url: string) {
    const b = new GxImage();
    b.uri = url;
    return b;
  }

  static createFromID(id: string) {
    const b = new GxImage();
    b.id = id;
    return b;
  }

  static create(i1: GxImage) {
    const i2 = new GxImage();
    i2.uri = i1.uri;
    i2.id = i1.id;
    return i2;
  }

  static createImage(id: string, url: string) {
    const i = new GxImage();
    i.id = id;
    i.uri = url;
    return i;
  }

}