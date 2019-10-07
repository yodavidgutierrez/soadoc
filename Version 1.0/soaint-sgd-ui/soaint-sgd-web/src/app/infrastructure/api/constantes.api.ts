import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/Observable';
import { SerieDTO } from 'app/domain/serieDTO';
import { SubserieDTO } from 'app/domain/subserieDTO';
import { ContenidoDependenciaTrdDTO } from 'app/domain/ContenidoDependenciaTrdDTO';
import { ConstanteDTO } from '../../domain/constanteDTO';
import { CacheResponse } from '../../shared/cache-response';

@Injectable()
export class ConstanteApiService extends CacheResponse {

  constructor(private _api: ApiBase) {
    super();
  }

  Listar(payload: any): Observable<ConstanteDTO[]> {
    let endpoint = null;
    let cacheable = false;
    switch (payload.key) {
      case 'tipoComunicacion':
        endpoint = environment.tipoComunicacion_endpoint;
        cacheable = true;
        break;
      case 'tipoTelefono':
        endpoint = environment.tipoTelefono_endpoint;
        cacheable = true;
        break;
      case 'tipoPersona':
        endpoint = environment.tipoPersona_endpoint;
        cacheable = true;
        break;
      case 'tipoAnexos':
        endpoint = environment.tipoAnexos_endpoint;
        break;
      case 'tipoDocumento':
        endpoint = environment.tipoDocumento_endpoint;
        break;
      case 'tipoDestinatario':
        endpoint = environment.tipoDestinatario_endpoint;
        cacheable = true;
        break;
      case 'unidadTiempo':
        endpoint = environment.unidadTiempo_endpoint;
        cacheable = true;
        break;
      case 'mediosRecepcion':
        endpoint = environment.mediosRecepcion_endpoint;
        break;
      case 'tratamientoCortesia':
        endpoint = environment.tratamientoCortesia_endpoint;
        cacheable = true;
        break;
      case 'actuaCalidad':
        endpoint = environment.actuaCalidad_endpoint;
        break;
      case 'tipologiaDocumental':
        endpoint = environment.tipologiaDocumental_endpoint;
        break;
      case 'tipoVia':
        endpoint = environment.tipoVia_endpoint;
        break;
      case 'orientacion':
        endpoint = environment.orientacion_endpoint;
        break;
      case 'bis':
        endpoint = environment.bis_endpoint;
        break;
      case 'tipoComplemento':
        endpoint = environment.tipoComplemento_endpoint;
        break;
      case 'prefijoCuadrante':
        endpoint = environment.prefijoCuadrante_endpoint;
        break;
      case 'soporteAnexo':
        endpoint = environment.soporteAnexo_endpoint;
        cacheable = true;
        break;
      case 'modalidadCorreo':
        endpoint = environment.modalidadCorreo_endpoint;
        break;
      case 'claseEnvio':
        endpoint = environment.claseEnvio_endpoint;
        break;
      case 'estadosEntregaSalida':
        endpoint = environment.estadoEntregaSalidaEndPoint;
        break;
      case 'estadosEntregaEntrada':
        endpoint = environment.estadoEntregaEntradaEndPoint;
        break;
    }

    const observable = cacheable  ? this.getResponse(payload, this._api.list(endpoint, payload),endpoint) :  this._api.list(endpoint, payload);

    if (endpoint !== null) {
      return  observable.map( _map => _map.constantes);
    }

  }

  addConstante(constante:ConstanteDTO):Observable<any>{
    return this._api.post(environment.listas.create,constante);
  }

  editConstante(constante:ConstanteDTO){

     return this._api.put(environment.listas.edit,constante);
  }

  deleteConstante(idConstante){

     return this._api.delete(`${environment.listas.delete}/${idConstante}`)
  }

  getListasEditablesGenericas():Observable<any>{

    return  this.getResponse(null,this._api.list(environment.listas.getListasGenericas),environment.listas.getListasGenericas)
                .map( _map => _map.constantes);
  }

  getListasEditables( payload?:any):Observable<any>{

    return this._api.list(environment.listas.getListas,payload);
  }

}
