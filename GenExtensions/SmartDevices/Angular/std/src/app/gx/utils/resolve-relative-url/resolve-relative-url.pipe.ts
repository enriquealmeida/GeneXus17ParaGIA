import { Pipe, PipeTransform } from "@angular/core";
import { AppContainer } from 'app/gx/base/app-container';

@Pipe({
  name: 'resolveRelativeUrl'
})
export class ResolveRelativeUrlPipe implements PipeTransform {

  constructor(private app: AppContainer) { }

  transform(url: string, args?: any): any {
    return this.app.resolveRelativeURL(url);
  }
}
