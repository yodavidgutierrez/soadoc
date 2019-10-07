import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import * as actions from './funcionarioDTO-actions';
import {ApiBase} from '../../api/api-base';


@Injectable()
export class Sandbox {

  constructor(private _store: Store<State>,
              private _api: ApiBase) {
  }

  loadAllFuncionarios(payload?: any) {
    const endpoint = `${environment.listarFuncionarios_endpoint}/${payload}`;
    return this._api.list(endpoint);
  }

  loadAllFuncionariosByRol(payload?: any) {
    const endpoint = `${environment.listarFuncionarios_endpoint}/${payload.codDependencia}/${payload.rol}`;
    return this._api.list(endpoint);
  }

  loadAllFuncionariosDispatch(payload?) {
    this._store.dispatch(new actions.LoadAllAction(payload));
  }

  loadAllFuncionariosByRolDispatch(payload?) {
    this._store.dispatch(new actions.LoadAllByRolAction(payload));
  }

  updateFuncinarioDispatch(payload){

    this._store.dispatch(new actions.LoadSuccessAction(payload));
  }


}

