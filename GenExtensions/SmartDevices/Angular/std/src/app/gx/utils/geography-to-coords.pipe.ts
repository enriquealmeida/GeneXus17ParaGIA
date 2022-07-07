import { Pipe, PipeTransform } from "@angular/core";

import { AppContainer } from "../base/app-container";
import { Geography } from "@genexus/web-standard-functions/dist/lib-esm/types/geography";

@Pipe({
    name: 'geographyToCoords'
})
export class GeographyToCoordsPipe implements PipeTransform {

  constructor( private app: AppContainer ) { }
  
  transform(geography: Geography): any {
    return `${geography.latitude},${geography.longitude}`;
  }
}