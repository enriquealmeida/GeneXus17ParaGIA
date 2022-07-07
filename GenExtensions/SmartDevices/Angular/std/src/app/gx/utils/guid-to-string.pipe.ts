import { Pipe, PipeTransform } from "@angular/core";
import { GUID } from "@genexus/web-standard-functions/dist/lib-esm/types/guid";
import { AppContainer } from "../base/app-container";

@Pipe({
    name: 'guidToString'
})
export class GuidToStringPipe implements PipeTransform {

  constructor( private app: AppContainer ) { }
  
  transform(guid: GUID): any {
    return guid.toString() ;
  }
}