import { Pipe, PipeTransform } from "@angular/core";
import { GxImage } from "app/gx/base/gximage.dt";
import { AppContainer } from 'app/gx/base/app-container';

@Pipe({
    name: 'imageToURL'
})
export class ImageToURLPipe implements PipeTransform {

  constructor(private app: AppContainer) { }
  
  transform(image: GxImage, context:any): any {
    return this.app.imageToURL(image);
  }
}
