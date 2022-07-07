import { HttpHeaders, HttpClient } from "@angular/common/http";
import { Observable, of, throwError } from "rxjs";
import { catchError } from 'rxjs/operators';
import { Settings } from "../../app.settings";
import { ClientStorage } from '../std/client-storage';
import { map } from 'rxjs/operators';
import { Injectable } from "@angular/core";
import { GeneXusClientClientInformation } from "@genexus/web-standard-functions/dist/lib-esm/gxcore/client/client-information";

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private _http: HttpClient) { }

  public loginAnonymous(): Observable<any> {
    const url = Settings.OAUTH_ENDPOINT + 'access_token';
    let body = "grant_type=device";
    body += "&client_id=" + Settings.GAM_CLIENT_ID;
    body += "&client_secret=" + Settings.GAM_CLIENT_SECRET;
    const headers = new HttpHeaders(
      {
        'Accept': 'application/json',
        'Content-Type': 'application/x-www-form-urlencoded',
        'DeviceId': GeneXusClientClientInformation.id(),
        'Genexus-Agent': 'SmartDevice Application'
      });

    return this._http.post(url, body, { headers: headers, withCredentials: true }).pipe(
      map((response: any) => {
        ClientStorage.Set('gx.GAM.access_token', response.access_token);
        if (response.refresh_token) {
          ClientStorage.Set('gx.GAM.refresh_token', response.refresh_token);
        } else {
          ClientStorage.Remove('gx.GAM.refresh_token');
        }
        this.getUserInfo(response.access_token).subscribe(() => response.access_token);
      }), catchError(err => {
        return throwError(err);
      })
    );
  }

  public login(username: string, password: string) {
    const url = Settings.OAUTH_ENDPOINT + 'access_token';
    let body = "client_id=" + Settings.GAM_CLIENT_ID;
    body += "&client_secret=" + Settings.GAM_CLIENT_SECRET;
    body += "&grant_type=password&username=" + username;
    body += "&password=" + password;
    body += "&scope=FullControl";
    const headers = new HttpHeaders(
      {
        'Accept': 'application/json',
        'Content-Type': 'application/x-www-form-urlencoded',
        'Genexus-Agent': 'SmartDevice Application'
      });

    return this._http.post(url, body, { headers: headers, withCredentials: true }).pipe(
      map((response: any) => {
        ClientStorage.Set('gx.GAM.access_token', response.access_token);
        if (response.refresh_token) {
          ClientStorage.Set('gx.GAM.refresh_token', response.refresh_token);
        } else {
          ClientStorage.Remove('gx.GAM.refresh_token');
        }
        this.getUserInfo(response.access_token).subscribe(() => response.access_token);
      }), catchError(err => {
        return throwError(err);
      })
    );
  }

  public loginExternal(type: string, username: string, password: string, additionalParameters: any = null) {
    const url = Settings.OAUTH_ENDPOINT + 'access_token';
    let body = "client_id=" + Settings.GAM_CLIENT_ID;
    body += "&client_secret=" + Settings.GAM_CLIENT_SECRET;
    body += "&grant_type=" + type;
    body += "&username=" + username;
    body += "&password=" + password
    body += "&scope=FullControl";
    if (additionalParameters) {
      body += "&additional_parameters=" + JSON.stringify(additionalParameters);
    }
    const headers = new HttpHeaders(
      {
        'Accept': 'application/json',
        'Content-Type': 'application/x-www-form-urlencoded',
        'Genexus-Agent': 'SmartDevice Application'
      });

    return this._http.post(url, body, { headers: headers, withCredentials: true }).pipe(
      map((response: any) => {
        ClientStorage.Set('gx.GAM.access_token', response.access_token);
        this.getUserInfo(response.access_token).subscribe(() => response.access_token);
      }), catchError(err => {
        return throwError(err);
      })
    );
  }

  public loginV2(username: string, password: string, additionalParameters: AdditionalParameters = null) {
    const url = Settings.OAUTH_ENDPOINT + 'gam/v2.0/access_token';
    let body = "client_id=" + Settings.GAM_CLIENT_ID;
    body += "&client_secret=" + Settings.GAM_CLIENT_SECRET;
    body += "&grant_type=password";
    body += "&username=" + username;
    body += "&password=" + password
    body += "&scope=gam_user_data";
    if (additionalParameters) {
      if (additionalParameters.Repository) {
        body += "&repository=" + additionalParameters.Repository;
      }
      if (additionalParameters.AuthenticationTypeName) {
        body += "&authentication_type_name=" + additionalParameters.AuthenticationTypeName;
      }
      if (additionalParameters.Properties && additionalParameters.Properties.length > 0) {
        body += "&properties=" + JSON.stringify(additionalParameters.Properties);
      }
    }
    const headers = new HttpHeaders(
      {
        'Accept': 'application/json',
        'Content-Type': 'application/x-www-form-urlencoded',
        'Genexus-Agent': 'SmartDevice Application'
      });

    return this._http.post(url, body, { headers: headers, withCredentials: true }).pipe(
      map((response: any) => {
        ClientStorage.Set('gx.GAM.access_token', response.access_token);
        if (response.refresh_token) {
          ClientStorage.Set('gx.GAM.refresh_token', response.refresh_token);
        } else {
          ClientStorage.Remove('gx.GAM.refresh_token');
        }
        this.getUserInfo(response.access_token).subscribe(() => response.access_token);
      }), catchError(err => {
        return throwError(err);
      })
    );
  }

  public refreshToken(): Observable<any> {
    const url = Settings.OAUTH_ENDPOINT + 'access_token';
    let body = "grant_type=refresh_token";
    body += "&client_id=" + Settings.GAM_CLIENT_ID;
    body += "&client_secret=" + Settings.GAM_CLIENT_SECRET;
    body += "&refresh_token=" + ClientStorage.Get('gx.GAM.refresh_token');
    const headers = new HttpHeaders(
      {
        'Accept': 'application/json',
        'Content-Type': 'application/x-www-form-urlencoded',
        'DeviceId': GeneXusClientClientInformation.id(),
        'Genexus-Agent': 'SmartDevice Application'
      });

    return this._http.post(url, body, { headers: headers, withCredentials: true }).pipe(
      map((response: any) => {
        ClientStorage.Set('gx.GAM.access_token', response.access_token);
        if (response.refresh_token) {
          ClientStorage.Set('gx.GAM.refresh_token', response.refresh_token);
        } else {
          ClientStorage.Remove('gx.GAM.refresh_token');
        }
        this.getUserInfo(response.access_token).subscribe(() => response.access_token);
      }), catchError(err => {
        return throwError(err);
      })
    );
  }

  public logout() {
    return this._http.post(Settings.OAUTH_ENDPOINT + 'logout', "{}").pipe(
      map(() => {
        ClientStorage.Remove('gx.GAM.access_token');
        window.location.reload();
      }), catchError(err => {
        return throwError(err);
      })
    );
  }

  public getUserInfo(token): Observable<any> {
    const url = Settings.OAUTH_ENDPOINT + 'userinfo';
    const body = "{}";
    const headers = new HttpHeaders(
      {
        'Content-Type': 'application/json',
        'DeviceId': GeneXusClientClientInformation.id(),
        'Authorization': 'OAuth ' + token,
        'Genexus-Agent': 'SmartDevice Application'
      });
    return this._http.post(url, body, { headers: headers }).pipe(
      map(response => {
        const userInfo = JSON.stringify(response)
        ClientStorage.Set("gx.GAM.gam_user", userInfo);
        return of(userInfo);
      }), catchError(err => {
        return throwError(err);
      })
    );
  }

  public getAuthToken(): string {
    return ClientStorage.Get('gx.GAM.access_token');
  }
}

class AdditionalParameters {
  Repository: string;
  AuthenticationTypeName: string;
  Properties: Array<AdditionalParametersProperty>;
}

class AdditionalParametersProperty {
  Id: string;
  Value: string;
}