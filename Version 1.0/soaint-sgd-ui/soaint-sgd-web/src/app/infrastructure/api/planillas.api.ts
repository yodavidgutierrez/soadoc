import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {environment} from '../../../environments/environment';
import {PlanillaDTO} from '../../domain/PlanillaDTO';

@Injectable()
export class PlanillasApiService {

  constructor(private _api: ApiBase) {
  }

  generarPlanillas(payload: PlanillaDTO) {
    return this._api.post(environment.generarPlanilla_endpoint, payload);
  }

  generarPlanillasSalida(payload: PlanillaDTO) {
    return this._api.post(environment.generarPlanillaSalida_endpoint, payload);
  }

  generarPlanillaInterna(payload: any) {
    return this._api.post(environment.generarPlanillaSalidaInterna_endpoint, payload)
  }

  exportarPlanilla(payload: any) {
    return this._api.list(environment.exportarPlanilla_endpoint, payload);
  }

  exportarPlanillaInterna(payload: any) {
    return this._api.list(environment.exportarPlanillaInterna_endpoint, payload);
  }

    exportarPlanillaSalida(payload: any) {
    return this._api.list(environment.exportarPlanillaSalida_endpoint, payload);
  }

  cargarPlanillas(payload: PlanillaDTO) {
    return this._api.post(environment.cargarPlanilla_endpoint, payload);
  }
}
