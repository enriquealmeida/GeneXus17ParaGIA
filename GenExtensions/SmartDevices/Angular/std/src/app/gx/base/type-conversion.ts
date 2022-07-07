import { GxCollectionData } from "./gxcollection.dt";
import { plainToClassFromExist, classToPlain } from "class-transformer";
import { gxRowNumberId } from './grid-data';
import { isEmpty } from '@genexus/web-standard-functions/dist/lib-esm/date/isEmpty';

export class TypeConversions {

  ///////////////////////////////////////////////////////////////////////////////////////
  // Builtin array to GxCollection
  static arrayToCollection<T>(items?: Array<any>, c?: { new(): T; }): GxCollectionData<T> {
    const collection = new GxCollectionData<T>();
    if (items) {
      let idx = 0;
      items.forEach(element => {
        const item = TypeConversions.objectToClass<T>(element, c);
        item[gxRowNumberId] = idx++;
        collection.push(item);
      });
    }
    return collection;

  }

  ///////////////////////////////////////////////////////////////////////////////////////
  // Object <-> Class conversion and serialization 
  static objectToClass<T>(obj: any, c: { new(): T; }): any {
    let x = plainToClassFromExist(new c(), obj)
    return TypeConversions.convertBuiltinTypes(new c(), x);
  }

  static convertBuiltinTypes(c, obj) {
    if (c instanceof Date) {
      return TypeConversions.datetimeFromISOString(obj);
    }
    return obj;

  }

  static classToObject(obj: any) {
    return classToPlain(obj, { excludePrefixes: ['_gx'] });
  }

  static serializeClass(obj): string {
    return JSON.stringify(this.classToObject(obj));
  }

  ///////////////////////////////////////////////////////////////////////////////////////
  // Serialization date/datetime/time functions
  //    * Empty date = '0000-00-00'
  //    * Empty datetime = '0000-00-00T00:00:00'
  //    * Empty time = '00:00:00'
  static SerializeDatetimeToISOString(d: Date): string {
    if (!isEmpty(d)) {
      return TypeConversions.datetimeToISOString(d);
    }
    return "0000-00-00T00:00:00";
  }

  ///////////////////////////////////////////////////////////////////////////////////////
  // UI date/datetime/time functions
  //    * Empty date/datetime/time = ''
  static UIDateToISOString(d: Date): string {
    if (!isEmpty(d)) {
      const s = TypeConversions.dateToISOString(d);
      return TypeConversions.getDateUIValue(d, s);
    }
    return "";
  }

  static UIDatetimeToISOString(d: Date): string {
    if (!isEmpty(d)) {
      const s = TypeConversions.datetimeToISOString(d);
      return TypeConversions.getDateUIValue(d, s);
    }
    return "";
  }

  static UITimeToISOString(d: Date): string {
    if (!isEmpty(d)) {
      const s = TypeConversions.timeToISOString(d);
      return TypeConversions.getDateUIValue(d, s);
    }
    return "";
  }

  static UIDateFromISOString(s: string): Date {
    let d = TypeConversions.datetimeFromISOString(s);
    TypeConversions.setDateUIValue(d, s);
    return d;
  }

  static setDateUIValue(d: Date, s: string) {
    d["UI_VALUE"] = s;
  }

  static getDateUIValue(d: Date, s: string): string {
    if (!d["UI_VALUE"]) {
      d["UI_VALUE"] = s;
    }
    return d["UI_VALUE"];
  }

  static UISetValidDate(d: Date, newD: Date): Date {
    if (newD) {
      return newD;
    }
    else {
      return d;
    }
  }

  ///////////////////////////////////////////////////////////////////////////////////////
  // date/datetime/time conversion auxiliary functions
  static timeToISOString(d: Date): string {
    if (d) {
      const a = TypeConversions.dateToArray(d);
      return TypeConversions.dateArrayToTimeString(a);
    }
    return '';
  }

  static dateToISOString(d: Date): string {
    if (d) {
      const a = TypeConversions.dateToArray(d);
      return TypeConversions.dateArrayToDateString(a);
    }
    return '';
  }

  static datetimeToISOString(d: Date): string {
    if (d) {
      const a = TypeConversions.dateToArray(d);
      return TypeConversions.dateArrayToDatetimeString(a);
    }
    return '';
  }

  static datetimeFromISOString(s: string): Date {
    let d: Date;
    try {
      const da = TypeConversions.ISODateToDTA(s);
      d = TypeConversions.DTAToDate(da);
    }
    catch {

      throw new Error('Invalid date: "' + s + '"');
    }
    if (!TypeConversions.isValidDate(d)) {
      // Not ISO, try to convert from any supported fromat
      d = new Date(s);
    }
    return d;
  }

  static isValidDate(d: any) {
    return d instanceof Date && !isNaN(d.getTime());
  }

  static dateToArray(d: Date): Array<number> {
    return [d.getFullYear(), d.getMonth(), d.getDate(), d.getHours(), d.getMinutes(), d.getSeconds(), d.getMilliseconds()];
  }

  static dateArrayToDatetimeString(s: Array<number>): string {
    // yyyy-mm-ddThh:mm:ss
    return ("000" + s[0]).slice(-4) + "-" + ("0" + (s[1] + 1)).slice(-2) + "-" + ("0" + s[2]).slice(-2) + "T" + ("0" + s[3]).slice(-2) + ":" + ("0" + s[4]).slice(-2) + ":" + ("0" + s[5]).slice(-2);
  }

  static dateArrayToDateString(s: Array<number>): string {
    // yyyy-mm-dd
    return ("000" + s[0]).slice(-4) + "-" + ("0" + (s[1] + 1)).slice(-2) + "-" + ("0" + s[2]).slice(-2);
  }

  static dateArrayToTimeString(s: Array<number>): string {
    // hh:mm:ss
    return ("0" + s[3]).slice(-2) + ":" + ("0" + s[4]).slice(-2) + ":" + ("0" + s[5]).slice(-2);
  }

  static ISODateToDTA(s: string): Array<number> {
    // DTA = Array<numeric> = [date_format, year, month, day, hour, min, sec, millis]
    const datetimeS = s.split("T");
    if (datetimeS.length > 1) {
      const dateS = datetimeS[0].split("-");
      const timeS = datetimeS[1].split(":");
      let dt_sec = timeS[2];
      let dt_millis = "0";
      if (timeS.length === 3 && timeS[2].indexOf(".") > -1) {
        const sec_millis = timeS[2].split(".");
        dt_sec = sec_millis[0];
        dt_millis = sec_millis[1];
      }
      return [-1, +dateS[0], +dateS[1], +dateS[2], +timeS[0], +timeS[1], +dt_sec, +dt_millis];    // -1=DATE_AND_TIME
    }
    else if (s.indexOf("-") > 1) {
      const dateS = datetimeS[0].split("-");
      return [-2, +dateS[0], +dateS[1], +dateS[2], 0, 0, 0, 0];  // -2=ONLY_DATE
    }
    else if (s.indexOf(":") > -1) {
      const timeS = datetimeS[0].split(":");
      let dt_sec = timeS[2];
      let dt_millis = "0";
      if (timeS.length === 3 && timeS[2].indexOf(".") > -1) {
        const sec_millis = timeS[2].split(".");
        dt_sec = sec_millis[0];
        dt_millis = sec_millis[1];
      }
      return [-3, 0, 0, 0, +timeS[0], +timeS[1], +dt_sec, +dt_millis];  // -3=ONLY_TIME
    }
    return [0, 0, 0, 0, 0, 0, 0, 0]; // 0=INVALID_DATE
  }

  static DTAToDate(dta: Array<number>): Date {
    if (dta[0] === 0 ||     // INVALID_DATE
      (dta[1] === 0 && dta[2] === 0 && dta[3] === 0)) {  // or empty date
      return new Date(0, 0, 0);
    } else {  // DATE_AND_TIME
      const a = dta.slice(1);
      return TypeConversions.arrayToDate(a);
    }
  }

  static arrayToDate(a: Array<number>): Date {
    const d = new Date();
    d.setFullYear(+a[0], +a[1] - 1, +a[2]);
    d.setHours(+a[3]);
    d.setMinutes(+a[4]);
    d.setSeconds(+a[5]);
    d.setMilliseconds(+a[6]);
    return d;
  }

  ///////////////////////////////////////////////////////////////////////////////////////
  // DATE equality comparer 
  static eqDate(d1: Date, d2: Date): boolean {
    return d1.getTime() == d2.getTime();
  }
}
