import { Pipe, PipeTransform } from "@angular/core";

import { AppContainer } from "../base/app-container";

@Pipe({
    name: 'imageSource'
})
export class ImageSourcePipe implements PipeTransform {

  constructor( private app: AppContainer ) { }
  
  transform(name: string, context: any): string {
    return this.app.getImageSource(name);
  }
}