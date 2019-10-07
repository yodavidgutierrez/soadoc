import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import * as actions from './distrubucionFisicaDTO-actions';
import {ApiBase} from '../../api/api-base';
import {Observable} from "rxjs/Observable";


@Injectable()
export class Sandbox {

  constructor(private _store: Store<State>,
              private _api: ApiBase) {
  }

  loadData(payload: any) {

    const newPayload = {};

    Object.keys(payload).forEach( k => {

      if(payload[k])
         newPayload[k] = payload[k];
    });

      return this._api.list(environment.listarDistrubucion_endpoint,newPayload );
  }

  filterDispatch(query) {
    this._store.dispatch(new actions.FilterAction(query));
  }

  loadDispatch(payload) {
    this._store.dispatch(new actions.LoadAction(payload));
  }

  clearDispatch(){
    this._store.dispatch( new actions.ClearAction());
  }

}

