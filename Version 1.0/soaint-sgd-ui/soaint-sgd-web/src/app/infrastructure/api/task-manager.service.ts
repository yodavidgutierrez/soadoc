import {Injectable} from "@angular/core";
import {State, State as RootState} from '../redux-store/redux-reducers';
import {Store} from "@ngrx/store";
import {isNullOrUndefined} from "util";
import {ApiBase} from "./api-base";
import {environment} from "../../../environments/environment";
import {createSelector} from "reselect";
import {Observable} from "rxjs/Observable";
import {getAuthenticatedFuncionario} from "../state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {TareaDTO} from "../../domain/tareaDTO";
import {Headers, RequestOptions, RequestOptionsArgs} from '@angular/http';
import {profileStore} from '../../ui/page-components/login/redux-state/login-selectors';
import {Subscription} from 'rxjs/Subscription';
import {tassign} from 'tassign';

@Injectable()
export class TaskManagerService{

  authPayload: { usuario: string, pass: string } | any;
  authPayloadUnsubscriber: Subscription;

  constructor(private _api:ApiBase,private _store:Store<RootState>){
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

  getTasksForUser(user:string, dependency?: any, paginado?:any){

    const payload = {
      estados: [
        'RESERVADO',
        'ENPROGRESO',
        'LISTO',
       ],
      parametros:{
        codDependencia: (dependency)? dependency.codigo: null,
      },
      page: paginado.page,
      pageSize: paginado.pageSize
    };
    return this.getHeaders().switchMap( options => this._api.post(environment.tasksForUser_endpoint, Object.assign(payload, this.authPayload), options));

  }

  reassignTask(task_id,user){

    const payload = {
      idTarea:task_id,
      parametros:{usuarioReasignar:user}

    };

   return this.postEndpoint(environment.reasignTask_endpoint,payload);
  }

  reassignTasks(tasks:TareaDTO[],user){

    const payload = tasks.map( task=> {

      return this.buildPayload({
        idTarea:task.idTarea,
        idProceso: task.idProceso,
        parametros:{usuarioReasignar:user}

      });

    });
    return this.getHeaders().switchMap( options => this._api.post(environment.reasignTasks_endpoint, payload, options));
  }

   private postEndpoint(endpoint:string,payload):Observable<any>{

    if(!isNullOrUndefined(this.authPayload)){
      return this._api.post(endpoint,Object.assign(payload,this.authPayload));
    }

      return this._api.post(endpoint,Object.assign(payload,this.authPayload));


  }

  private buildPayload(payload){

    return Object.assign(payload,this.authPayload);
  }


  getStadistics(payload:any, dependency?: any){
    const clonePayload = tassign({
      parametros: {
        codDependencia: (dependency)? dependency.codigo: null,
      }
    });

    return this.getHeaders().switchMap(options =>
      this._api.post(`${environment.taskStatistic_endpoint}${payload}`,Object.assign({},clonePayload, this.authPayload), options));

  }
}
