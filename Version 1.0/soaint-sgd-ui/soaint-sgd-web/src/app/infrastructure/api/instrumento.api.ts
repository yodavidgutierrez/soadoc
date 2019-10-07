import {Injectable} from "@angular/core";
import {ApiBase} from "./api-base";
import {environment} from "../../../environments/environment";
import {Observable} from "rxjs/Observable";
import {DependenciaDTO} from "../../domain/dependenciaDTO";
import {isNullOrUndefined} from "util";
import {oa_dataSource} from "../../ui/page-components/organizacion-archivo/data-source";
import {ConstanteDTO} from "../../domain/constanteDTO";

@Injectable()
export class InstrumentoApi {

  constructor(private _api: ApiBase) {}

  ListDependences(payload: any): Observable<DependenciaDTO[]> {
    const _endpoint = `${environment.dependenciaGrupo_endpoint}/${payload.value}`;

    return this._api.list(_endpoint).map(response => {
      return response.organigrama
    });
  }

  ListarDependencia(): Observable<DependenciaDTO[]>{
    return this._api.list(environment.listarDependneciaEndPoint).map( response => {
      return response.dependencias
    })
  }

  AsociarFuncionarioDependencias(payload):Observable<any>{

    return this._api.put(environment.asociarFuncionarioDependneciasEndPoint,payload);
  }

  ObtenerDependenciaRadicadora():Observable<any>{

    return this._api.list(environment.dependenciaRadicadoraEndpoint)
      .map( dep => isNullOrUndefined(dep.codigo) ? null : dep);
  }

  getTiplogiasDocumentales(payload):Observable<any>{

    return this._api.list(environment.listarTipologiasDocumentalesEndPoint, payload);
  }

  obtenerDependenciaPorCodigo(codDependencia): Observable<any>{

    return this._api.list(`${environment.obtenerDependenciaPorCodigoEndPoint}/${codDependencia}`);
  }

  getSubseries(codDep):Observable<any>{
    return this._api.list(
    `${environment.listarSubseriesEndPoint}/${codDep}`);
  }


}
