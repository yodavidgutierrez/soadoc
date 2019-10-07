import {Observable} from 'rxjs/Observable';
import {HttpHandler} from '../utils/http-handler';
import {Injectable} from '@angular/core';
import {Headers, RequestOptions, RequestOptionsArgs} from '@angular/http';
import {_ParseAST} from '@angular/compiler';

@Injectable()
export class ApiBase {

  constructor(protected _http: HttpHandler) {
  }

  public list(endpoint: string, payload = {}, options?: RequestOptionsArgs): Observable<any> {
    return this._http.get(endpoint, payload, options);
  }

  public post(endpoint: string, payload = {}, options?: RequestOptionsArgs): Observable<any> {
    return this._http.post(endpoint, payload, options);
  }

  public put(endpoint: string, payload = {}): Observable<any> {
    return this._http.put(endpoint, payload);
  }

  public delete(endpoint: string, payload = {}): Observable<any> {
    return this._http.delete(endpoint, payload);
  }

  public sendFile(endpoint: string, formData: FormData, pathParams: Array<string>): Observable<any> {

    let fullEndpoint = `${endpoint}`;
    pathParams.forEach((value) => {
      fullEndpoint += `/${value}`;
    });
    return this._http.putFile(fullEndpoint, formData);
  }

}
