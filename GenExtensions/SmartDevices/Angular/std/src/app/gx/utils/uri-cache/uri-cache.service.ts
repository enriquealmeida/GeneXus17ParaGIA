import { Injectable } from "@angular/core";

@Injectable({
  providedIn: 'root',
}) export class UriCacheService {

  srcMap = new Map();
  cacheLimit = 50;


  get(uri: string): string {
    if (uri) {
      const uploadKeyPos = uri.indexOf('gxupload:');
      if (uploadKeyPos > -1) {
        const uploadKey = uri.substr(uploadKeyPos);
        if (this.srcMap.has(uploadKey)) {
          return this.srcMap.get(uploadKey);
        }
      }
    }
    return uri;
  }

  store(key: string, file: File): string {
    if (this.srcMap.size > this.cacheLimit) {
      const elder = this.srcMap.keys().next().value;
      this.srcMap.delete(elder);
    }
    const fileURL = URL.createObjectURL(file);
    this.srcMap.set(key, fileURL);
    return fileURL;
  }
}
