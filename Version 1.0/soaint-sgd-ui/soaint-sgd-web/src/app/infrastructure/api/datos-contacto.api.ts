import {Injectable} from "@angular/core";
import {ApiBase} from "./api-base";
import {environment} from "../../../environments/environment";
import {isNullOrUndefined} from "util";

@Injectable()
export class DatosContactoApi{

   constructor( private _api : ApiBase){}

    getDatosContatoByIdAgente(idAgente){

        return this._api.list(`${environment.datos_contacto}${idAgente}`);

    }

    getDatosContactoByIdentificaciob(tipoPersona:string,nroIdentificacion:string,tipoDocumentoIdentificacion?:string){

      let path = `${nroIdentificacion}/${tipoPersona}`;

      if(!isNullOrUndefined(tipoDocumentoIdentificacion))
         path = `${path}/${tipoDocumentoIdentificacion}`

      return this._api.list(`${environment.datos_contacto_id}${path}`);

    }
}
