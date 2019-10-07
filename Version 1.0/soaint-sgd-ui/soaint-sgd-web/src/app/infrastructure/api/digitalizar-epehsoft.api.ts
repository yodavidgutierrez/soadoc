import {Injectable} from "@angular/core";
import {ApiBase} from "./api-base";
import {environment} from "../../../environments/environment";
import {isNullOrUndefined} from "util";
import {Observable} from "rxjs";

@Injectable()
export class DigitalizarEpehsoftApi {

  data;
  constructor(private _apiBas: ApiBase) {}

  digitalizarDocumento(payload?): Observable<any> {
    return this._apiBas.post(environment.digitalizacion_epehsoft_endponint, payload);
  }

}
