import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/Observable';
import {ComunicacionOficialSalidaFullDTO} from "../../domain/comunicacionOficialSalidaFullDTO";

@Injectable()
export class trasladoInternoApiService {

  constructor(private _api: ApiBase) {
  }

  listarComunicaciones(payload: any){
    return this._api.list(environment.listarComunicacionesSalidaDistribucionInterna, payload)


  }
  actualizarEnvioInterno(payload: any){
    return this._api.put(environment.actualizarDatosEnvioInterno, payload)


  }

}
