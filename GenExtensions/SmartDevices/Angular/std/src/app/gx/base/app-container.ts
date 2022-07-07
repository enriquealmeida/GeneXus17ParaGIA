import { getOption as DesignSystemGetOption, getOptions as DesignSystemGetOptions } from "@genexus/web-standard-functions/dist/lib-esm/gxcore/common/ui/designsystem";

import { ClientStorage } from '../std/client-storage';
import { CookieService } from "ngx-cookie-service";
import { GUID } from "@genexus/web-standard-functions/dist/lib-esm/types/guid";
import { ImageService } from "./image.service";
import { Injectable } from "@angular/core";
import { Settings } from "./../../app.settings";
import { Subject } from 'rxjs';
import { TranslationService } from "./../base/translation.service";
import { msg } from "@genexus/web-standard-functions/dist/lib-esm/misc/msg";
import { setLanguage } from "@genexus/web-standard-functions/dist/lib-esm/misc/setLanguage";
import { UriCacheService } from "app/gx/utils/uri-cache/uri-cache.service";
import { GxImage } from "./gximage.dt";
import { GxBinary } from "./gxbinary.dt";
import { LocationStrategy } from "@angular/common";

@Injectable({
  providedIn: "root"
})
export class AppContainer {

  constructor(
    protected _translations: TranslationService,
    protected _images: ImageService,
    private cookieService: CookieService,
    protected uriCacheService: UriCacheService,
    private locationStrategy: LocationStrategy
  ) { }

  private _errorCode = 0;
  private _errorString = "";

  private currentLanguage = "English";

  uiContextKey = "";
  uiContextSubject = new Subject();

  initApp(id: string) {
    const appId = ClientStorage.Get('gx.APP.id');
    if (!appId || appId != id) {
      ClientStorage.Clear();
      ClientStorage.Set('gx.APP.id', id);
    }
  }

  setLanguage(language: string) {
    this.currentLanguage = language;
    setLanguage(language);
    StyleManager.setDirection(this._translations.getLanguageDirection(language));

    this.refreshUIContext();
  }

  async initTheme(theme: string) {
    let currentTheme = this.getTheme();
    if (!currentTheme || currentTheme === "") {
      currentTheme = theme.toLowerCase();
      ClientStorage.Set('gx.APP.theme', currentTheme);
    }
    await this._images.load(this.currentLanguage, currentTheme);
    StyleManager.setStyle(currentTheme);
    StyleManager.setDirection(this._translations.getLanguageDirection(this.currentLanguage));
  }

  getTheme() {
    return ClientStorage.Get('gx.APP.theme') || '';
  }

  setTheme(theme: string) {
    const currentTheme = this.getTheme();
    if (currentTheme !== theme.toLowerCase()) {
      ClientStorage.Set('gx.APP.theme', theme.toLowerCase());
      window.location.reload();
    }
  }

  getColorScheme(): string {
    const colorScheme = DesignSystemGetOption('color-scheme')?.toLowerCase();

    if (!colorScheme && window.matchMedia) {
      if (window.matchMedia('(prefers-color-scheme: dark)').matches) {
        return "dark";
      } else {
        return "light";
      }
    } else {
      return colorScheme || "light";
    }
  }

  getThemeOptions(): { name: string; value: string; }[] {
    return DesignSystemGetOptions().filter(option => option.name.toLowerCase() != 'color-scheme');
  }

  refreshUIContext() {
    this.uiContextKey = `${this.currentLanguage}_${this.getTheme()}_${this.getColorScheme()}_${this.getThemeOptions().map(option => `${option.name}_${option.value}`).join('_')}`;
    this.uiContextSubject.next();
  }

  async initTranslations() {
    await this._translations.load(
      this.currentLanguage
    );
  }

  setMessage(message: string) {
    msg(message);
  }

  translate(key: string): string {
    return this._translations.translate(key);
  }

  getBaseUrl() {
    return Settings.WEBAPP_BASE;
  }

  setSession() {
    let ngSessionId = this.cookieService.get("gx-ng-session");
    if (!ngSessionId) {
      ngSessionId = GUID.newGuid().toString();
      this.cookieService.set("gx-ng-session", ngSessionId);
    }
  }

  setError(errorCode: number, errorString = "") {
    this._errorCode = errorCode;
    this._errorString = errorString;
  }

  get err() { return this._errorCode; }
  get errMsg() { return this._errorString; }

  open(id: string) {
    const uri = this.uriCacheService.get(id);
    window.open(uri, '_blank');
  }


  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Images and binaries 

  createFromID(id: string) {
    let img = GxImage.createFromID(id);
    img.uri = this.imageToURL(img);
    return img;
  }

  imageToURL(image: GxImage): string {
    let url = "";
    if (image.id) {
      url = this.getImageSource(image.id);
    } else if (image.uri.indexOf('gxupload:') > -1) {
      url = this.uriCacheService.get(image.uri);
    } else {
      url = image.uri;
    }
    return this.resolveRelativeURL(url);
  }

  serializeImage(image: GxImage): string {
    if (image.id) {
      return this.resolveRelativeURL(this.getImageSource(image.id));
    } else if (image.uri.indexOf('gxupload:') > -1) {
      return image.uri;
    } else {
      return this.resolveRelativeURL(image.uri);
    }
  }

  binaryToURL(binary: GxBinary): string {
    let url = "";
    if (binary.uri.indexOf('gxupload:') > -1) {
      url = this.uriCacheService.get(binary.uri);
    } else {
      url = binary.uri;
    }
    return this.resolveRelativeURL(url);
  }

  serializeBinary(binary: GxBinary): string {
    if (binary.uri.indexOf('gxupload:') > -1) {
      return binary.uri;
    } else {
      return this.resolveRelativeURL(binary.uri);
    }
  }

  createImageFromURL(key: string): GxImage {
    let id = "";
    let url = "";
    const isPath = key.indexOf("/") > -1;
    const isUploadedImage = key.indexOf("gxupload:") > -1;
    if (isPath || isUploadedImage) {
      const imgLowerC = key.toLowerCase();
      if (
        imgLowerC.startsWith("blob:") ||
        imgLowerC.startsWith("file:") ||
        imgLowerC.startsWith("data:")) {
        url = key.slice(5);
      } else {
        url = key;
      }
    } else {
      id = key;
    }
    return GxImage.createImage(id, url);
  }

  createBinaryFromURL(url: string): GxBinary {
    return GxBinary.createBinary(url);
  }

  getImageSource(key: string): string {
    const isPath = key.indexOf("/") > -1;
    if (isPath) {
      return key;
    } else {
      return this._images.getImageSource(
        key,
        this.currentLanguage,
        this.getTheme(),
        this.getColorScheme(),
        this.getThemeOptions()
      );
    }
  }

  resolveRelativeURL(url: string) {
    if (!url) return "";
    const dataURL = this.getBaseUrl();
    const imgLowerC = url.toLowerCase();
    if (imgLowerC.startsWith("assets/")) {
      const appBaseURL = window.location.origin + this.locationStrategy.getBaseHref();
      return appBaseURL + url;
    }
    else if (
      imgLowerC.startsWith("http://") ||
      imgLowerC.startsWith("https://") ||
      imgLowerC.startsWith("blob:") ||
      imgLowerC.startsWith("file:") ||
      imgLowerC.startsWith("data:") ||
      imgLowerC.startsWith("/" + dataURL.toLocaleLowerCase()) ||
      imgLowerC.startsWith(dataURL.toLocaleLowerCase())) {
      return url;
    } else {
      return dataURL + url;
    }
  }
}

class StyleManager {
  static setStyle(name: string) {
    this.getLinkElementForKey().setAttribute('href', name + ".css");
  }

  static setDirection(dir: string) {
    const bodyStyles = document.body.style;
    if (dir === 'rtl') {
      bodyStyles.setProperty('--direction', 'rtl');
    } else {
      bodyStyles.setProperty('--direction', 'ltr');

    }
  }

  static removeStyle(key: string) {
    const existingLinkElement = this.getExistingLinkElementByKey();
    if (existingLinkElement) {
      document.head.removeChild(existingLinkElement);
    }
  }

  static getLinkElementForKey() {
    return this.getExistingLinkElementByKey() || this.createLinkElementWithKey();
  }

  static getExistingLinkElementByKey() {
    return document.head.querySelector(`link[rel="stylesheet"].${"style-manager-theme"}`);
  }

  static createLinkElementWithKey() {
    const linkEl = document.createElement('link');
    linkEl.setAttribute('rel', 'stylesheet');
    linkEl.type = 'text/css';
    linkEl.classList.add("style-manager-theme");
    document.head.appendChild(linkEl);
    return linkEl;
  }
}
