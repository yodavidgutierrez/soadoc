import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs/Observable';
import {Store} from "@ngrx/store";
import {State as RootState} from "../../infrastructure/redux-store/redux-reducers";
import {getAuthenticatedFuncionario} from "../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {isArray, isNullOrUndefined} from "util";
import {ROUTES_PATH} from "../../app.route-names";
import {combineLatest} from "rxjs/observable/combineLatest";
import {go} from "@ngrx/router-store";
import {getActiveTask} from "../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors";


@Injectable()
export class BpmmanagerGuard implements CanActivate {

  constructor(private _store:Store<RootState>){}


  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {


    return  combineLatest(
      this._store.select(getAuthenticatedFuncionario),
      this._store.select(getActiveTask)
      ).map( ([funcionario,task]) => {

       if( /^\/task\/.+$/.test(state.url) && !task){
          this._store.dispatch(go('/home'));

          return false;
        }


      if(isNullOrUndefined(funcionario) )
        return false;

      const indexParam =  next.url[0].path == 'task' ? 1 : 0;

      const path = next.url[indexParam].path;

      const routePath = Object.keys( ROUTES_PATH)
                              .filter( key => path == (ROUTES_PATH[key].url || ROUTES_PATH[key]))
                               .map(key => ROUTES_PATH[key])
                               .find( (_,index) => index === 0);

      if(isNullOrUndefined(routePath.rol) && isNullOrUndefined(routePath.roles))
         return true;

      let rolUrl;

      if(!isNullOrUndefined(routePath.rol)){

        rolUrl = routePath.rol;
      }

      if(!isNullOrUndefined(routePath.roles)){

         const pathParam = next.url[indexParam + 1].path;

         rolUrl = routePath.roles[pathParam];
      }
      return funcionario.roles.some( rol => {

        if(isArray(rolUrl))
          return  rolUrl.includes(rol.rol);

         return  rol.rol == rolUrl
        })
    });
  }
}
