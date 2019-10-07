import {Injectable} from "@angular/core";
import {ApiBase} from "./api-base";
import {Observable} from "rxjs/Observable";
import {environment} from "../../../environments/environment";

@Injectable()
export class MegafServiceApi {


  constructor(private _apiService:ApiBase){

  }

  createUnidadDocumental():Observable<any>{

    return Observable.empty();
  }

  getDirectChilds(codDependencia,tipoArchivo,idPadre:any = 0): Observable<any>{
    return this._apiService.list(`${environment.megaf.hijosDirectos}/${codDependencia}/${tipoArchivo}/${idPadre}`);
  }

  getUnidadesFisicas(payload?):Observable<any>{
    return  this._apiService.list(environment.megaf.unidadesConservacion,payload);
  }

}
