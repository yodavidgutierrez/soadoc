import {Injectable} from '@angular/core';
import {environment} from 'environments/environment';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import * as actions from './tareasDTO-actions';
import {back, go} from '@ngrx/router-store';
import {tassign} from 'tassign';
import {TareaDTO} from '../../../domain/tareaDTO';
import {isArray} from 'rxjs/util/isArray';
import {ApiBase} from '../../api/api-base';
import {
  TASK_APROBAR_DOCUMENTO,
  TASK_CARGAR_PLANILLA_ENTRADA,
  TASK_DIGITALIZAR_DOCUMENTO,
  TASK_DOCUMENTOS_TRAMITES,
  TASK_GENERAR_PLANILLA_ENTRADA,
  TASK_GESTION_PRODUCCION_MULTIPLE,
  TASK_GESTIONAR_UNIDADES_DOCUMENTALES,
  TASK_PRODUCIR_DOCUMENTO,
  TASK_RADICACION_ENTRADA,
  TASK_REVISAR_DOCUMENTO,
  TASK_GESTIONAR_DEVOLUCIONES,
  TASK_CORREGIR_RADICACION,
  TASK_RADICACION_SALIDA,
  TASK_RADICACION_DOCUMENTO_SALIDA,
  TASK_ARCHIVAR_DOCUMENTO,
  TASK_CREAR_UNIDAD_DOCUMENTAL,
  TASK_APROBAR_DISPOSICION_FINAL,
  TASK_ADJUNTAR_DOCUMENTO,
  TASK_COMPLETAR_DATOS_DISTRIBUCION,
  TASK_VERIFICAR_TRANSFERENCIA_DOCUMENTAL,
  TASK_APROBAR_TRANSFERENCIA_DOCUMENTAL,
  TASK_CARGAR_PLANILLA_SALIDA,
  TASK_UBICAR_TRANSFERENCIA_SECUENDARIA,
  TASK_UBICAR_TRANSFERENCIA_PRIMARIA,
  TASK_DETALLAR_ANEXO,
  TASK_DIGITALIZAR_DOCUMENTO_EPEHSOFT, TASK_ADJUNTAR_DOCUMENTO1, TASK_CARGAR_PLANILLA_INTERNA
} from './task-properties';
import {StartProcessAction} from '../procesoDTO-state/procesoDTO-actions';
import {Subscription} from 'rxjs/Subscription';
import {createSelector} from 'reselect';
import {ROUTES_PATH} from '../../../app.route-names';
import {getSelectedDependencyGroupFuncionario} from '../funcionarioDTO-state/funcionarioDTO-selectors';
import {Observable} from 'rxjs/Observable';
import {getActiveTask} from "./tareasDTO-selectors";
import {Headers, RequestOptions, RequestOptionsArgs} from '@angular/http';
import {profileStore} from '../../../ui/page-components/login/redux-state/login-selectors';
import {isNullOrUndefined} from "util";

@Injectable()
export class Sandbox {

  routingStartState = false;

  authPayload: { usuario: string, pass: string } | any;
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

  loadData(payload: any, dependency?: any) {

    const clonePayload = tassign(payload, {
      estados: [
        'RESERVADO',
        'ENPROGRESO',
        'LISTO'
      ],
      parametros: {
        codDependencia: (dependency)? dependency.codigo: null,

      }
    });
    return this.getHeaders().switchMap(options =>
      this._api.post(environment.tasksForStatus_endpoint,Object.assign({},clonePayload, this.authPayload), options));
  }

  getTaskStats() {

    const payload = {
      parametros: {
        usuario: this.authPayload.usuario
      }
    };
    return this.getHeaders().switchMap(options => this._api.post(environment.tasksStats_endpoint,
      Object.assign({}, payload, this.authPayload), options));

  }


  getTaskVariables(payload: any) {
    const overPayload = this.extractProcessVariablesPayload(payload);
    return this._api.post(environment.obtenerVariablesTarea,
      Object.assign({}, overPayload, this.authPayload));
  }

  isTaskRoutingStarted() {
    return this.routingStartState;
  }

  taskRoutingStart() {
    this.routingStartState = true;
  }

  taskRoutingEnd() {
    this.routingStartState = false;
  }

  startTask(payload: any) {
    const overPayload = this.extractInitTaskPayload(payload);
    return this.getHeaders().switchMap( options =>this._api.post(environment.tasksStartProcess,
      Object.assign({}, overPayload, this.authPayload), options));
  }

  reserveTask(payload: any) {
    const overPayload = this.extractInitTaskPayload(payload);
    return this.getHeaders().switchMap( options => this._api.post(environment.tasksReserveProcess,
      Object.assign({}, overPayload, this.authPayload), options));
  }

  extractProcessVariablesPayload(payload) {
    let task = payload;
    if (isArray(payload) && payload.length > 0) {
      task = payload[0];
    }

    return {
      'idProceso': task.idProceso,
      'idDespliegue': task.idDespliegue,
      'instanciaProceso': task.idInstanciaProceso
    }
  }

  extractInitTaskPayload(payload) {
    let task = payload;
    if (isArray(payload) && payload.length > 0) {
      task = payload[0];
    }

    return {
      'idProceso': task.idProceso,
      'idDespliegue': task.idDespliegue,
      'idTarea': task.idTarea
    }
  }

  completeTask(payload: any) {
    console.log(payload);
    return this.getHeaders().switchMap( options => this._api.post(environment.tasksCompleteProcess,
      Object.assign({}, payload, this.authPayload), options));
  }

  abortTask(payload: any) {
    return this.getHeaders().switchMap( options => this._api.post(environment.tasksAbortProcess,
      Object.assign({}, payload, this.authPayload), options));
  }

  filterDispatch(query) {
    this._store.dispatch(new actions.FilterAction(query));
  }

  initTaskDispatch(task: TareaDTO): any {
      console.log(task)
    switch (task.nombre) {

      case TASK_RADICACION_ENTRADA:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.radicarCofEntrada.url, task]));
        break; case TASK_DETALLAR_ANEXO:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.detallarAnexo.url, task]));
        break;
      case TASK_RADICACION_SALIDA:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.radicarCofSalida.url, task]));
        break;
      case TASK_RADICACION_DOCUMENTO_SALIDA:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.radicarDocumentoSalida.url, task]));
        break;
      case TASK_DIGITALIZAR_DOCUMENTO:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.digitalizarDocumento.url, task]));
        break;
      case TASK_ADJUNTAR_DOCUMENTO:
      case TASK_ADJUNTAR_DOCUMENTO1:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.adjuntarDocumento.url, task]));
        break;
      case TASK_GESTIONAR_DEVOLUCIONES:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.gestionarDevoluciones.url, task]));
        break;
      case TASK_CORREGIR_RADICACION:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.corregirRadicacion.url, task]));
        break;
      case TASK_APROBAR_DISPOSICION_FINAL:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.disposicionFinal.url, task]));
        break;
      case TASK_DOCUMENTOS_TRAMITES:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.documentosTramite.url, task]));
        break;
      case TASK_CARGAR_PLANILLA_ENTRADA:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.cargarPlanillas.url, task]));
        break;
      case TASK_CARGAR_PLANILLA_INTERNA:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.cargarPlanillaSalidaInterna.url, task]));
        break;
      case TASK_GESTION_PRODUCCION_MULTIPLE:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.produccionDocumentalMultiple.url, task]));
        break;
      case TASK_GESTIONAR_UNIDADES_DOCUMENTALES:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.gestionUnidadDocumental.url, task]));
        break;
      case TASK_PRODUCIR_DOCUMENTO:
          this._store.dispatch(go([`/${ROUTES_PATH.task}/${ROUTES_PATH.produccionDocumental.url}/1`, task]));
          break;
      case TASK_REVISAR_DOCUMENTO:
          this._store.dispatch(go([`/${ROUTES_PATH.task}/${ROUTES_PATH.produccionDocumental.url}/2`, task]));
          break;
      case TASK_APROBAR_DOCUMENTO:
          this._store.dispatch(go([`/${ROUTES_PATH.task}/${ROUTES_PATH.produccionDocumental.url}/3`, task]));
          break;
      case TASK_ARCHIVAR_DOCUMENTO :
        this._store.dispatch(go([`/${ROUTES_PATH.task}/${ROUTES_PATH.archivarDocumento}`, task]));
        break;

      case TASK_CREAR_UNIDAD_DOCUMENTAL :
        this._store.dispatch(go([`/${ROUTES_PATH.task}/${ROUTES_PATH.crearUnidadDocumental.url}`, task]));
        break;

      case TASK_COMPLETAR_DATOS_DISTRIBUCION:
        this._store.dispatch(go([`/${ROUTES_PATH.task}/${ROUTES_PATH.completarDatosDistribucion.url}`, task]));
        break;

       case TASK_UBICAR_TRANSFERENCIA_SECUENDARIA: this._store.dispatch(go([`/${ROUTES_PATH.task}/${ROUTES_PATH.transferenciasDocumentales.url}/4`, task]));
         break;
      case TASK_UBICAR_TRANSFERENCIA_PRIMARIA: this._store.dispatch(go([`/${ROUTES_PATH.task}/${ROUTES_PATH.transferenciasDocumentales.url}/3`, task]));
        break;
       case TASK_APROBAR_TRANSFERENCIA_DOCUMENTAL:
        this._store.dispatch(go([`/${ROUTES_PATH.task}/${ROUTES_PATH.transferenciasDocumentales.url}/1`, task]));
        break;

        case TASK_VERIFICAR_TRANSFERENCIA_DOCUMENTAL:
        this._store.dispatch(go([`/${ROUTES_PATH.task}/${ROUTES_PATH.transferenciasDocumentales.url}/2`, task]));
        break;

        case TASK_CARGAR_PLANILLA_SALIDA:
        this._store.dispatch(go([`/${ROUTES_PATH.task}/${ROUTES_PATH.cargarPlanillaSalida.url}`, task]));
        break;

        case TASK_DIGITALIZAR_DOCUMENTO_EPEHSOFT:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.digitalizarDocumentoEpehsoft, task]));
        break;


      default:
        this._store.dispatch(go(['/' + ROUTES_PATH.task + '/' + ROUTES_PATH.workspace, task]));
    }
  }

  completeTaskDispatch(payload: any) {
    this._store.dispatch(new actions.CompleteTaskAction(payload));
  }

   completeBackTaskDispatch(payload: any) {
    this._store.dispatch(new actions.CompleteBackTaskAction(payload));
  }

  abortTaskDispatch(payload: any) {
    this._store.dispatch(new actions.AbortTaskAction(payload));
  }

  dispatchNextTask(payload) {
    this._store.dispatch(new StartProcessAction(payload))
  }

  navigateToWorkspace() {
    this._store.dispatch(back());
  }

  startTaskDispatch(task?: TareaDTO) {
    if (task.estado === 'ENPROGRESO') {
      this._store.dispatch(new actions.StartInProgressTaskAction(task));
    } else if (task.estado === 'RESERVADO') {
      this._store.dispatch(new actions.StartTaskAction(task));
    }
  }

  reserveTaskDispatch(task?: TareaDTO) {
    this._store.dispatch(new actions.ReserveTaskAction(task));
  }

  loadDispatch(payload?) {
    this._store.dispatch(new actions.LoadAction(payload));
  }

  getTareaPersisted(idProceso,idTarea){

    return this._api.list(environment.taskStatus_endpoint+'/'+idProceso+'/'+idTarea);

  }

  guardarEstadoTarea(task: TareaDTO){
      return this._api.post(environment.taskStatus_endpoint, task);
  }


}

