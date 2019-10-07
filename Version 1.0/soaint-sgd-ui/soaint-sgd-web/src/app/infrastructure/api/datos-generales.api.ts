import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {Observable} from 'rxjs/Observable';
import {environment} from '../../../environments/environment';
import {RulesServer} from '../../shared/drools-config-properties/drools-properties';
import {CacheResponse} from "../../shared/cache-response";

@Injectable()
export class DatosGeneralesApiService  extends CacheResponse{

  constructor(private _api: ApiBase) {

    super();
  }

  private mockRules(tipologiaDocumental){

    const responses ={
      'TL-DOCOF': {
        tiempoRespuesta: '10',
        codUnidaTiempo: 'UNID-TID',
        inicioConteo: 'DSH'
      },
      'TL-DOCT': {
        tiempoRespuesta: '5',
        codUnidaTiempo: 'UNID-TIH',
        inicioConteo: 'DSH'
      },
      'TL-DOCDP': {
        tiempoRespuesta: '3',
        codUnidaTiempo: 'UNID-TID',
        inicioConteo: 'DSH'
      },
      'TL-DOCII': {
        tiempoRespuesta: '10',
        codUnidaTiempo: 'UNID-TID',
        inicioConteo: 'DSH'
      },
      'TL-DOCDM': {
        tiempoRespuesta: '6',
        codUnidaTiempo: 'UNID-TIH',
        inicioConteo: 'DSH'
      },
      'TL-DOCF': {
        tiempoRespuesta: '4',
        codUnidaTiempo: 'UNID-TID',
        inicioConteo: 'DSH'
      },
      'ANE-EX': {
        tiempoRespuesta: '4',
        codUnidaTiempo: 'UNID-TID',
        inicioConteo: 'DSH'
      },
      'TL-DOCDN': {
        tiempoRespuesta: '4',
        codUnidaTiempo: 'UNID-TID',
        inicioConteo: 'DSH'
      },
      'TL-DOCR': {
        tiempoRespuesta: '4',
        codUnidaTiempo: 'UNID-TID',
        inicioConteo: 'DSH'
      },
      'TL-DOCP': {
        tiempoRespuesta: '4',
        codUnidaTiempo: 'UNID-TID',
        inicioConteo: 'DSH'
      },
      'TL-DOCA': {
        tiempoRespuesta: '4',
        codUnidaTiempo: 'UNID-TID',
        inicioConteo: 'DSH'
      },
      'TL-DOCC': {
        tiempoRespuesta: '4',
        codUnidaTiempo: 'UNID-TID',
        inicioConteo: 'DSH'
      },
      'TL-DOCM': {
        tiempoRespuesta: '4',
        codUnidaTiempo: 'UNID-TID',
        inicioConteo: 'DSH'
      },
    };

  return Observable.of(responses[tipologiaDocumental]);

}

  loadMetricasTiempo(tipologiaDocumental) {
    // const end_point = environment.metricasTiempoRadicacion_rule_endpoint;
    // const payload = RulesServer.requestPayload(tipologiaDocumental);
    // return this.getResponse({payload: JSON.stringify(payload)},this._api.list(end_point, {payload: JSON.stringify(payload)})
    //   .map(response => {
    //     const res = RulesServer.extractFromResponse(response, 'co.com.soaint.sgd.model.MedioRecepcion');
    //
    //    return res;
    //
    //   }),end_point) ;

    return this.mockRules(tipologiaDocumental);
  }
}
