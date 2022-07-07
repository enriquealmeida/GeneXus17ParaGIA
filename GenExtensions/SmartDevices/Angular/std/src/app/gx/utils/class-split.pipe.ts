import { Pipe, PipeTransform } from "@angular/core";

@Pipe({
    name: 'classSplit'
})
export class ClassSplitPipe implements PipeTransform {

  constructor() { }
  
  transform(value: any, args?: any): any {
    return value.replace(/,/g, ' ');
  }
}
