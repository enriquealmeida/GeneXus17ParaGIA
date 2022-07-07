import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from "@angular/common/http"
import { GAMUser } from "./gamuser.dt";
import { ProgressService } from '../ui/controls/progress/progress.service';
import { msg } from '@genexus/web-standard-functions/dist/lib-esm/misc/msg';
import { ClientStorage } from '../std/client-storage';
import { AuthService } from './auth.service';
import { plainToClassFromExist } from "class-transformer";
import { CompositeCancel, PanelComponent } from '../base/panel.component';
import { Settings } from 'app/app.settings';
import { CompositeNavigation } from "../navigation/composite-navigation";

@Injectable({
  providedIn: 'root',
})
export class GAMService {

  constructor(private _http: HttpClient,
    private _authService: AuthService,
    private progress: ProgressService,
    private _nvg: CompositeNavigation) {
  }

  public async getUser(): Promise<GAMUser> {
    let userInfo = ClientStorage.Get("gx.GAM.gam_user");
    if (!userInfo) {
      userInfo = await this._authService.getUserInfo(null).toPromise();
    }
    const o = JSON.parse(userInfo);
    return plainToClassFromExist(new GAMUser(), o);
  }

  public async login(username: string, password: string) {
    try {
      await this._authService.login(username, password).toPromise();
    } catch (e) {
      await this.progress.hide();
      if (e instanceof HttpErrorResponse && e.status === 401 && e.error.error.code === '23') {
        await msg(e.error.error.message);
        const __aSt = PanelComponent.activePanel.startAction();
        await this._nvg.navigate([Settings.GAM_CLIENT_CHANGEPASSWORD], __aSt);
        PanelComponent.activePanel.endAction(__aSt);
      } else {
        await msg(e.error.error.message);
        throw new CompositeCancel("Composite cancelled");
      }
    }
  }

  public async loginExternal(type: string, username: string, password: string, additionalParameters: any = null) {
    try {
      await this._authService.loginExternal(type, username, password, additionalParameters).toPromise();
    } catch (e) {
      await this.progress.hide();
      await msg(e.error.error.message);
      throw new CompositeCancel("Composite cancelled");
    }
  }

  public async registerAnonymous() {
    await this._authService.loginAnonymous().toPromise();
  }

  public async logout() {
    this._authService.logout().subscribe();
  }

  public async getEmail(): Promise<string> {
    const user = await this.getUser();
    return user.getEmail();
  }

  public async getId(): Promise<string> {
    const user = await this.getUser();
    return user.getId();
  }

  public async getName(): Promise<string> {
    const user = await this.getUser();
    return user.getName();
  }

  public async getLogin(): Promise<string> {
    const user = await this.getUser();
    return user.getLogin();
  }

  public async getExternalId(): Promise<string> {
    const user = await this.getUser();
    return user.getExternalId();
  }

  public async isAnonymous(): Promise<boolean> {
    const user = await this.getUser();
    return user.isAnonymous();
  }

  public isLogged(): boolean {
    const userToken = this._authService.getAuthToken();
    if (userToken && userToken.length > 0) {
      return true;
    }
    return false;
  }
}

export class ResponseError {
  error: Error;
}

export class Error {
  code: number;
  message: string;
}
