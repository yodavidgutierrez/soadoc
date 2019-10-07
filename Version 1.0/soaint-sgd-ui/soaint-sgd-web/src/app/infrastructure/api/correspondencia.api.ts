import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/Observable';
import {UnidadDocumentalDTO} from '../../domain/unidadDocumentalDTO';
import { MensajeRespuestaDTO } from '../../domain/MensajeRespuestaDTO';
import { AnexoFullDTO } from '../../domain/anexoFullDTO';
import { Subscription } from 'rxjs';
import { ComunicacionOficialDTO } from '../../domain/comunicacionOficialDTO';
import {ComunicacionOficialSalidaFullDTO} from "../../domain/comunicacionOficialSalidaFullDTO";

@Injectable()
export class CorrespondenciaApiService {

  constructor(private _api: ApiBase) {

  }

  ListarComunicacionesSalidaDistibucionFisicaExterna(payload: any): Observable<ComunicacionOficialSalidaFullDTO[]> {
    return  this._api.list(environment.listarCorrespondencia_salida_distribucion_fisica_externa_endpoint, payload)
 }

  ListarComunicacionesSalidaDistibucionFisica(payload: any): Observable<ComunicacionOficialDTO[]> {
    return  this._api.list(environment.listarCorrespondencia_salida_distribucion_fisica_endpoint, payload)
  }


  actualizarComunicacion(payload: any): Observable<any> {
     return  this._api.put(environment.actualizarComunicacion_endpoint, payload);
  }

  actualizarInstanciaGestionDevoluciones(payload: any ): Observable<any>{

    return this._api.put(environment.actualizarInstanciaDevolucion, payload);
  }

  recibirDocumentosFisicos(payload){

    return this._api.put(environment.recibirDocumentosFisicos_endpoint, payload);
  }
}
