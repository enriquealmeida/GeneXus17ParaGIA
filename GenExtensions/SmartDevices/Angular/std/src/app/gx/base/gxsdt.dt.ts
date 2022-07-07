import { GxCollectionData } from "./gxcollection.dt";
import { plainToClassFromExist, classToPlain } from "class-transformer";

export class GxSdtData extends GxCollectionData<any> {

  static fromJson(obj: any, json: string,) {
    plainToClassFromExist(obj, JSON.parse(json));
  }

  static toJson(obj: any): string {
    return JSON.stringify(classToPlain(obj));
  }
}
