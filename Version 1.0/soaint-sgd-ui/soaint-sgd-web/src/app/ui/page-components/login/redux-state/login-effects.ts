import {Injectable} from '@angular/core';
import {Effect, Actions, toPayload} from '@ngrx/effects';
import {Action, Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/mergeMap';
import * as login from './login-actions';
import {LoginSandbox} from './login-sandbox';
import {go} from '@ngrx/router-store'
import {LoadSuccessAction as FuncionarioAutenticatedAction } from '../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-actions';
import {tassign} from 'tassign';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {ClearTasksAction} from "../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-actions";



@Injectable()
export class LoginEffects {

  constructor(private actions$: Actions,
              private loginSandbox: LoginSandbox,
              private store:Store<RootState>
              ) {
  }

  @Effect()
  login: Observable<Action> = this.actions$
    .ofType(login.ActionTypes.LOGIN)
    .map(toPayload)
    .switchMap(
      (payload) => this.loginSandbox.login({login: payload.username, password: payload.password})
         .mergeMap((response: any) => [
          new login.LoginSuccessAction(tassign(response, { credentials: payload})),
          new FuncionarioAutenticatedAction(response.profile),
          go('/home')
        ])
        .catch(error => Observable.of(new login.LoginFailAction({error: error})))
    );

  @Effect({dispatch: false})
  loginSuccess: Observable<Action> = this.actions$
    .ofType(login.ActionTypes.LOGIN_SUCCESS)
    .map(toPayload)
    .do( r => {

      if(r.noSaveSession)
        return;

       const sessionData =Object.assign(r,{});

       const password = sessionData.credentials.password;

       sessionData.credentials.password = btoa( sessionData.credentials.password);
       localStorage.setItem("session",JSON.stringify(sessionData));

      sessionData.credentials.password = password;
    });

  @Effect()
  logout: Observable<Action> = this.actions$
    .ofType(login.ActionTypes.LOGOUT)
    .map(toPayload)
    .do( _ => {
      localStorage.removeItem("session");
      localStorage.removeItem("lastActivity");
      go('login');
    }).
    map( () => new ClearTasksAction());

}
