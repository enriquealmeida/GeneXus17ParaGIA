import { Pipe, PipeTransform } from "@angular/core";

import { AppContainer } from "../base/app-container";
import { Gauge } from "@genexus/web-standard-functions/dist/lib-esm/types/gauge";

@Pipe({
    name: 'jsonToGauge'
})
export class JsonToGaugePipe implements PipeTransform {

  constructor( private app: AppContainer ) { }
  
  transform(json: string): Gauge {
    if (json && json !== "") {
      return new Gauge(json);
    }
  }
}