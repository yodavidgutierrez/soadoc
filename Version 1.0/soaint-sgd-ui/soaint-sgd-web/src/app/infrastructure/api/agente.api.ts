import {Injectable} from "@angular/core";
import {ApiBase} from "./api-base";
import {environment} from "../../../environments/environment";
import {isNullOrUndefined} from "util";

@Injectable()
export class AgenteApi {


  constructor(private _apiBase:ApiBase){}

   getAgenteByNroIdentificacion(nroIdentificacion:string,tipoPersona:string){

      return this._apiBase.list(`${environment.agenteByNroIdentificacionEndPoint}${nroIdentificacion}/${tipoPersona}`);

   }
}
