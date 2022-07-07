import { Pipe, PipeTransform } from "@angular/core";
import { GxBinary } from "app/gx/base/gxbinary.dt";
import { AppContainer } from 'app/gx/base/app-container';

@Pipe({
    name: 'binaryToURL'
})
export class BinaryToURLPipe implements PipeTransform {

  constructor(private app: AppContainer) { }
  
  transform(image: GxBinary): any {
    return this.app.binaryToURL(image);
  }
}
