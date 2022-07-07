import { Router } from "@angular/router";
import { msg } from '@genexus/web-standard-functions/dist/lib-esm/misc/msg';
import { GAMService } from "app/gx/auth/gam.service";
import { AppContainer } from "./app-container";
import { GxBinary } from "./gxbinary.dt";
import { GxImage } from "./gximage.dt";
import { TypeConversions } from "./type-conversion";


export class PanelService {
  private router: Router;
  private loginService: GAMService;

  app: AppContainer;

  gxids = [];
  
  constructor(_router: Router, _loginService: GAMService, _app: AppContainer) {
    this.router = _router;
    this.loginService = _loginService;
    this.app = _app;
  }

  start() {
    this.gxids = [];
  }

  public async handleError(e: any): Promise<any> {
    let message = null;
    if (e && e.error && e.error.error && e.error.error.message) {
      message = e.error.error.message || e.message;
    } else if (e && e.message) {
      message = e.message;
    }
    if (message) {
      await msg(message);
    }
    return Promise.resolve(message || e);
  }

  getGxid(serviceId: number) {
    if (!this.gxids[serviceId]) {
      this.gxids[serviceId] = Math.floor(Math.random() * 100000000 + 1);
    }
    return this.gxids[serviceId];
  }

  dateToISOString(d: Date): string {
    //We should use Timezone offset. But in order to work with current services, we must remove timezone offset.
    return TypeConversions.datetimeToISOString(d);
  }
}
