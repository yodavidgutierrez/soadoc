import {Injectable} from '@angular/core';
import {Effect, Actions, toPayload} from '@ngrx/effects';
import {Store, Action} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/mergeMap';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/pairwise';
import 'rxjs/add/observable/combineLatest';
import 'rxjs/add/operator/filter';
import 'rxjs/add/operator/let';
import 'rxjs/add/operator/withLatestFrom';
import 'rxjs/add/operator/distinctUntilChanged';

import * as actions from './constanteDTO-actions';
import {Sandbox} from './constanteDTO-sandbox';
import {go} from '@ngrx/router-store'
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';

function isEmptyObject(obj) {
  return (obj && (Object.keys(obj).length === 0));
}

function isLoaded() {
  return (source) =>
    source.filter(([action, state]) => {
      console.log(action);
      return state.constantes[action.payload.key].ids !== [];
    })
}


@Injectable()
export class Effects {

  constructor(private actions$: Actions,
              private _store$: Store<RootState>,
              private _sandbox: Sandbox) {
  }

  @Effect()
  loadSingleConstant: Observable<Action> = this.actions$
    .ofType(actions.ActionTypes.LOAD)
    .map(toPayload)
    .withLatestFrom(this._store$, (payload, state) => [payload, state.constantes[payload.key].ids])
    .filter(([payload, values]) => values.length === 0)
    .switchMap(
      ([payload]) => this._sandbox.loadData(payload)
        .map((response) => new actions.LoadSuccessAction({key: payload.key, data: response}))
        .catch((error) => Observable.of(new actions.LoadFailAction({error}))
        )
    );

  @Effect()
  loadDatosGeneralesContansts = this.actions$
    .ofType(actions.ActionTypes.LOAD_DATOS_GENERALES)
    .map<Action, void>(toPayload)
    .distinctUntilChanged()
    .switchMap(() => Observable.combineLatest(
      this._sandbox.loadData({key: 'tipoComunicacion'}),
      this._sandbox.loadData({key: 'mediosRecepcion'}),
      this._sandbox.loadData({key: 'tipoAnexos'}),
      this._sandbox.loadData({key: 'unidadTiempo'}),
      this._sandbox.loadData({key: 'tipoDocumento'}),
      this._sandbox.loadData({key: 'tipologiaDocumental'}),
      this._sandbox.loadData({key: 'soporteAnexo'}),
      this._sandbox.loadData({key: 'claseEnvio'}),
      this._sandbox.loadData({key: 'modalidadCorreo'}),

      (tipoComunicacion,
       mediosRecepcion,
       tipoAnexos,
       unidadTiempo,
       tipoDocumento,
       tipologiaDocumental,
       soporteAnexo,
       claseEnvio,
       modalidadCorreo
      ) => {
        return {
          tipoComunicacion: {key: 'tipoComunicacion', data: tipoComunicacion},
          mediosRecepcion: {key: 'mediosRecepcion', data: mediosRecepcion},
          tipoAnexos: {key: 'tipoAnexos', data: tipoAnexos},
          unidadTiempo: {key: 'unidadTiempo', data: unidadTiempo},
          tipoDocumento: {key: 'tipoDocumento', data: tipoDocumento},
          tipologiaDocumental: {key: 'tipologiaDocumental', data: tipologiaDocumental},
          soporteAnexo: {key: 'soporteAnexo', data: soporteAnexo},
          claseEnvio: {key: 'claseEnvio', data: claseEnvio},
          modalidadCorreo: {key: 'modalidadCorreo', data: modalidadCorreo},
        }
      }).take(1)
      .mergeMap((data: any) => {
        return [
          new actions.LoadSuccessAction(data.tipoComunicacion),
          new actions.LoadSuccessAction(data.mediosRecepcion),
          new actions.LoadSuccessAction(data.tipoAnexos),
          new actions.LoadSuccessAction(data.unidadTiempo),
          new actions.LoadSuccessAction(data.tipoDocumento),
          new actions.LoadSuccessAction(data.tipologiaDocumental),
          new actions.LoadSuccessAction(data.soporteAnexo),
          new actions.LoadSuccessAction(data.claseEnvio),
          new actions.LoadSuccessAction(data.modalidadCorreo),
        ];
      })
      .catch(error => Observable.of(new actions.LoadFailAction({error})))
    );

  @Effect()
  loadRemitenteConstants = this.actions$
    .ofType(actions.ActionTypes.LOAD_DATOS_REMITENTE)
    .map<Action, void>(toPayload)
    .distinctUntilChanged()
    .switchMap(() => Observable.combineLatest(
      this._sandbox.loadData({key: 'prefijoCuadrante'}),
      this._sandbox.loadData({key: 'tipoTelefono'}),
      this._sandbox.loadData({key: 'tipoPersona'}),
      this._sandbox.loadData({key: 'tipoDestinatario'}),
      this._sandbox.loadData({key: 'tratamientoCortesia'}),
      this._sandbox.loadData({key: 'tipoVia'}),
      this._sandbox.loadData({key: 'orientacion'}),
      this._sandbox.loadData({key: 'bis'}),
      this._sandbox.loadData({key: 'tipoComplemento'}),
      this._sandbox.loadData({key: 'actuaCalidad'}),
      this._sandbox.loadData({key: 'tipoContacto'}),


      (prefijoCuadrante,
       tipoTelefono,
       tipoPersona,
       tipoDestinatario,
       tratamientoCortesia,
       tipoVia,
       orientacion,
       bis,
       tipoComplemento,
       actuaCalidad,
       tipoContacto,
       ) => {
        return {
          prefijoCuadrante: {key: 'prefijoCuadrante', data: prefijoCuadrante},
          tipoTelefono: {key: 'tipoTelefono', data: tipoTelefono},
          tipoPersona: {key: 'tipoPersona', data: tipoPersona},
          tipoDestinatario: {key: 'tipoDestinatario', data: tipoDestinatario},
          tratamientoCortesia: {key: 'tratamientoCortesia', data: tratamientoCortesia},
          tipoVia: {key: 'tipoVia', data: tipoVia},
          orientacion: {key: 'orientacion', data: orientacion},
          bis: {key: 'bis', data: bis},
          tipoComplemento: {key: 'tipoComplemento', data: tipoComplemento},
          actuaCalidad: {key: 'actuaCalidad', data: actuaCalidad},
          tipoContacto:{key: 'tipoContacto',data:tipoContacto}
        }
      }).take(1)
      .mergeMap((data: any) => {
        return [
          new actions.LoadSuccessAction(data.prefijoCuadrante),
          new actions.LoadSuccessAction(data.tipoTelefono),
          new actions.LoadSuccessAction(data.tipoPersona),
          new actions.LoadSuccessAction(data.tipoDestinatario),
          new actions.LoadSuccessAction(data.tratamientoCortesia),
          new actions.LoadSuccessAction(data.tipoVia),
          new actions.LoadSuccessAction(data.orientacion),
          new actions.LoadSuccessAction(data.bis),
          new actions.LoadSuccessAction(data.tipoComplemento),
          new actions.LoadSuccessAction(data.actuaCalidad),
          new actions.LoadSuccessAction(data.tipoContacto)
        ];
      })
      .catch(error => Observable.of(new actions.LoadFailAction({error})))
    );

  @Effect()
  filter: Observable<Action> = this.actions$
    .ofType(actions.ActionTypes.FILTER)
    .withLatestFrom(this._store$, (action, state) => state.constantes[action.payload.key].ids)
    .filter((action, values) => {
      return true;
    })
    .map(toPayload)
    .switchMap(
      (payload) => this._sandbox.loadData(payload)
        .map((response) => new actions.LoadSuccessAction({key: payload.key, data: response}))
        .catch((error) => Observable.of(new actions.LoadFailAction({error}))
        )
    );

  @Effect()
    loadDatosEnvio: Observable<Action> = this.actions$
      .ofType(actions.ActionTypes.LOAD_DATOS_ENVIO)
      .map<Action, void>(toPayload)
      .distinctUntilChanged()
      .switchMap(() => Observable.combineLatest(
        this._sandbox.loadData({key:'modalidadCorreo'}),
        this._sandbox.loadData({key:'claseEnvio'}),
        (modalidadCorreo,claseEnvio) => {
          return {
            claseEnvio : {key:"claseEnvio",data:claseEnvio},
            modalidadCorreo: {key:"modalidadCorreo",data:modalidadCorreo},
          }
        })
        .take(1)
        .mergeMap((data:any) => {
          return [
            new actions.LoadSuccessAction(data.claseEnvio),
            new actions.LoadSuccessAction(data.modalidadCorreo),

          ];
        })
        .catch(error => Observable.of(new actions.LoadFailAction({error})))
      );

  @Effect()
  loadMotivoNoCreacionUD: Observable<Action> = this.actions$
    .ofType(actions.ActionTypes.LOAD_MOTIVO_NO_CREACION_UD)
    .map<Action, void>(toPayload)
    .distinctUntilChanged()
    .switchMap(() => Observable.combineLatest(
      this._sandbox.loadData({key:'motivoNoCreacionUd'}),
      motivo => {
        return {
          motivoNoCreacionUD : {key:"motivoNoCreacionUD",data:motivo},
        }
      })
      .take(1)
      .mergeMap((data:any) => {
        return [
          new actions.LoadSuccessAction(data.motivoNoCreacionUD),
        ];
      })
      .catch(error => Observable.of(new actions.LoadFailAction({error})))
    );

}
