import { Pipe, PipeTransform } from "@angular/core";
import { TypeConversions } from "app/gx/base/type-conversion";

@Pipe({
    name: 'dateToISOString'
})
export class DateToISOStringPipe implements PipeTransform {

  constructor() { }
  
  transform(value) {
    return TypeConversions.UIDateToISOString(value);
  }
}
