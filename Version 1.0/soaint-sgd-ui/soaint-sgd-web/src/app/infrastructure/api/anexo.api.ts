import {Injectable} from "@angular/core";
import {ApiBase} from "./api-base";
import {environment} from "../../../environments/environment";

@Injectable()
export class AnexoApi{

  constructor(private _api:ApiBase){}

  updateAnexos(payload){

     return this._api.put(environment.anexoUpdate,payload);
  }
}
