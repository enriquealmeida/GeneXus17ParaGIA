export class GxBinary {
  uri: string;

  constructor() {
    this.uri = "";
  }

  toString() {
    return this.uri;
  }

  fromString(uri: string) {
    this.uri = uri;
  }

  isEmpty() {
    return this.uri === "" || this.uri === null;
  }

  setEmpty() {
    this.uri = "";
  }

  toBase64String() {
    console.log('toBase64String - Not implemented')
  }

  fromBase64String(s: string) {
    console.log('fromBase64String - Not implemented')
  }

  static create(b1: GxBinary) {
    const b2 = new GxBinary();
    b2.uri = b1.uri;
    return b2;
  }

  static createBinary(url: string) {
    const b = new GxBinary();
    b.uri = url;
    return b;
  }
}