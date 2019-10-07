import {Injectable, OnDestroy, OnInit} from '@angular/core';
import {environment} from 'environments/environment';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {createSelector} from 'reselect';
import * as selectors from './procesoDTO-selectors';
import * as actions from './procesoDTO-actions';
import {ApiBase} from '../../api/api-base';
import {Subscription} from 'rxjs/Subscription';
import {isNullOrUndefined, log} from 'util';
import {Headers, RequestOptions, RequestOptionsArgs} from '@angular/http';
import {getAuthenticatedFuncionario} from '../funcionarioDTO-state/funcionarioDTO-selectors';
import {profileStore} from '../../../ui/page-components/login/redux-state/login-selectors';
import {Observable} from 'rxjs/Observable';

@Injectable()
export class Sandbox implements OnDestroy {

  authPayload: { usuario: string, pass: string } |  {};
  authPayloadUnsubscriber: Subscription;

  constructor(private _store: Store<State>,
              private _api: ApiBase) {
    this.authPayloadUnsubscriber = this._store.select(createSelector((s: State) => s.auth.profile, (profile) => {
      return profile ? {usuario: profile.username, pass: profile.password} : {};
    })).subscribe((value) => {
      this.authPayload = value;
    });
  }

  getHeaders():Observable<RequestOptionsArgs>{
    const options:RequestOptionsArgs  = new RequestOptions();
    options.headers = new Headers();
    return this._store.select(profileStore)
      .map(p => {
        const user = {
          user: p.username,
          password: p.password
        };
        options.headers.append('Authorization', 'Basic ' + btoa(user.user + ':' + user.password));

        return options;
      });
  }

  loadData(payload: any) {

   const  loginName = payload.loginName;
    delete payload.loginName;
    return this.getHeaders().switchMap( options => this._api.list(`${environment.proceso_endpoint}/${loginName}`, payload,options))
  }

  startProcess(payload: any, dependency?: any) {

    let  params =  payload.customParams || {};

    params.codDependencia = dependency.codigo;
    return this.getHeaders().switchMap(options => this._api.post(environment.startProcess_endpoint,
      Object.assign({}, {
        idProceso: payload.codigoProceso,
        idDespliegue: payload.idDespliegue,
        estados: [
          'LISTO'
        ],
        parametros: params
      }, this.authPayload), options))
  }

  IniciarProcesso(payload: any) {
    return this._api.post(environment.startProcess_endpoint,
      Object.assign({}, payload, this.authPayload));
  }

  loadTasksInsideProcess(payload: any) {
    const params = payload.data || payload;
    return this.getHeaders().switchMap( options => this._api.post(environment.tasksInsideProcess_endpoint,
      Object.assign({}, {
        idProceso: params.nombreProceso || params.idProceso,
        instanciaProceso: params.instance.instanceId,

        estados: [
          'RESERVADO',
          'COMPLETADO',
          'ENPROGRESO',
          'LISTO'
        ]
      }, this.authPayload), options))
  }

  filterDispatch(target, query) {
    this._store.dispatch(new actions.FilterAction({key: target, data: query}));
  }

  loadDispatch(payload?) {
    this._store.dispatch(new actions.LoadAction(payload));
  }

  initProcessDispatch(entity) {
    this._store.dispatch(new actions.StartProcessAction(entity));
  }

  selectorMenuOptions() {
    return createSelector(selectors.getEntities, selectors.getGrupoIds, (entities, ids) => {
      return ids.map(id => {
        return {
          label: entities[id].nombreProceso, icon: 'assignment',
          command: () => this._store.dispatch(new actions.StartProcessAction(entities[id]))
        }
      })
    });
  }

  ngOnDestroy() {
    this.authPayloadUnsubscriber.unsubscribe();
  }
}

