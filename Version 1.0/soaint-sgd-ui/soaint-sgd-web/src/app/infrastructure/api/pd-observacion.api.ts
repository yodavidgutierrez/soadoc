import {Injectable} from "@angular/core";
import {ApiBase} from "./api-base";
import {Observable} from "rxjs/Observable";
import {environment} from "../../../environments/environment";
import {PdObservacionDTO} from "../../domain/pdObservacionDTO";

@Injectable()
export class PdObservacionApi {

  constructor(private  _api:ApiBase){}

  listar(idInstancia):Observable<any>{

    return this._api.list(`${environment.pdObservacionesListarEndPoint}${idInstancia}`);
  }

  actualizar(observaciones:PdObservacionDTO[]):Observable<any>
  {
    return this._api.put(environment.pdObservacionesActualizarEndPoint,observaciones);
  }
}
