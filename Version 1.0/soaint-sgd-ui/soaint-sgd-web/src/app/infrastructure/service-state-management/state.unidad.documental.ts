import { FormGroup, FormBuilder, Validators, FormControl} from '@angular/forms';
import {VALIDATION_MESSAGES} from 'app/shared/validation-messages';
import { UnidadDocumentalApiService } from 'app/infrastructure/api/unidad-documental.api';
import { Observable } from 'rxjs/Observable';
import { Store } from '@ngrx/store';
import { State } from 'app/infrastructure/redux-store/redux-reducers';
import { SerieSubserieApiService } from 'app/infrastructure/api/serie-subserie.api';
import { SerieDTO } from 'app/domain/serieDTO';
import { SubserieDTO } from 'app/domain/subserieDTO';
import { UnidadDocumentalAccion } from 'app/ui/page-components/unidades-documentales/models/enums/unidad.documental.accion.enum';
import { TaskForm } from 'app/shared/interfaces/task-form.interface';
import { TareaDTO } from 'app/domain/tareaDTO';
import { Sandbox } from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import { getActiveTask } from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import { async } from '@angular/core/testing';
import { Sandbox as TaskSandBox } from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import { UnidadDocumentalDTO } from '../../domain/unidadDocumentalDTO';
import { DisposicionFinalDTO } from '../../domain/DisposicionFinalDTO';
import { ConfirmationService } from 'primeng/primeng';
import { PushNotificationAction } from '../../infrastructure/state-management/notifications-state/notifications-actions';
import { MensajeRespuestaDTO } from 'app/domain/MensajeRespuestaDTO';
import { ChangeDetectorRef, Injectable, ApplicationRef } from '@angular/core';
import { isNullOrUndefined } from 'util';
import { ConstanteDTO } from 'app/domain/constanteDTO';
import { sedeDestinatarioEntradaSelector } from '../../infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-selectors';
import {Sandbox as DependenciaGrupoSandbox} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {LoadAction as SedeAdministrativaLoadAction} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-actions';
import {Subscription} from 'rxjs/Subscription';
import { DependenciaApiService } from '../../infrastructure/api/dependencia.api';
import { DependenciaDTO } from 'app/domain/dependenciaDTO';
import {Subject} from 'rxjs/Subject';
import { ROUTES_PATH } from '../../app.route-names';
import { go } from '@ngrx/router-store';
import {LoadingService} from './../../infrastructure/utils/loading.service';
import {Sandbox as ProccesSandbox} from "../state-management/procesoDTO-state/procesoDTO-sandbox";
import {ProcesoDTO} from "../../domain/procesoDTO";
import {startTaskSuccesEvent} from "../state-management/tareasDTO-state/tareasDTO-effects";
import {getSelectedDependencyGroupFuncionario} from "../state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import * as actions from "../state-management/procesoDTO-state/procesoDTO-actions";


@Injectable()
export class StateUnidadDocumentalService {

    ListadoUnidadDocumental: UnidadDocumentalDTO[] = [];
    unidadesSeleccionadas: UnidadDocumentalDTO[] = [];
    selectedIndex ;

        //
    private ListadoActualizadoSubject = new Subject<any>();
    public ListadoActualizado$ = this.ListadoActualizadoSubject.asObservable();

    // dropdowns
    tiposDisposicionFinal$: Observable<ConstanteDTO[]> = Observable.of([]);
    sedes$: Observable<ConstanteDTO[]> = Observable.of([]);
    dependencias: DependenciaDTO[] = [];
    ListadoSeries: SerieDTO[];
    ListadoSubseries: SubserieDTO[];

    // generales
    UnidadDocumentalSeleccionada: UnidadDocumentalDTO;
    EsSubserieRequerido: boolean;
    NoUnidadesSeleccionadas = 'No hay unidades documentales seleccionadas';
    validations: any = {};
    subscribers: Array<Subscription> = [];

    ultimolistarPayload: any;
    ultimoList2Payload:any;
    ultimolistarDisposicionPayload: DisposicionFinalDTO = {};

    // gestionar unidad documental
    OpcionSeleccionada: number;
    AbrirDetalle: boolean = false;
    FechaExtremaFinal: Date;
    MensajeIngreseFechaExtremaFinal = 'Por favor ingrese la fecha extrema final para proceder al cierre.';

    constructor(
        private unidadDocumentalApiService: UnidadDocumentalApiService,
        private serieSubserieApiService: SerieSubserieApiService,
        private _store: Store<State>,
        private _dependenciaSandbox: Sandbox,
        private _taskSandBox: TaskSandBox,
        private _processSandBox: ProccesSandbox,
        private confirmationService: ConfirmationService,
        private _appRef: ApplicationRef,
        private _dependenciaGrupoSandbox: DependenciaGrupoSandbox,
        private _dependenciaApiService: DependenciaApiService,
        private loading: LoadingService
    ) {
    }

    GetDetalleUnidadUnidadDocumental(index: string) {
        const unidadDocumentalIndex = this.ListadoUnidadDocumental[index];
        this.unidadDocumentalApiService.GetDetalleUnidadDocumental(unidadDocumentalIndex.id)
        .subscribe(response => {
            this.UnidadDocumentalSeleccionada = response;
            this.AbrirDetalle = true;
        });
    }

    GetListadoTiposDisposicion() {
        this.tiposDisposicionFinal$ = Observable.of([]);
    }

    GetListadoSedes() {
        this._store.dispatch(new SedeAdministrativaLoadAction());
        this.sedes$ = this._store.select(sedeDestinatarioEntradaSelector);
    }

    GetListadoDependencias(codSede: any) {
        this._dependenciaGrupoSandbox.loadData({codigo:codSede})
          .map( deps => deps.organigrama)
        .subscribe(resp => {
            this.dependencias = resp;
        });
    }

    GetListadosSeries(codigodependencia): void {
        this.serieSubserieApiService.ListarSerieSubserie({
            idOrgOfc: codigodependencia,
        })
        .map(map => map.listaSerie)
        .subscribe(response => {
            this.ListadoSeries = response;
        });
    }

    GetSubSeries(serie, codigodependencia: string): Observable<SubserieDTO[]>{
        this.ListadoSubseries = [];
          return this.serieSubserieApiService.ListarSerieSubserie({
                idOrgOfc: codigodependencia,
                codSerie: serie.codigoSerie,
                nomSerie:serie.nombreSerie
            })
            .map(map => map.listaSubSerie)
    }


    CerrarDetalle() {
        this.AbrirDetalle = false;
        this.selectedIndex = null;
    }

    Listar(payload?: any) {
        this.ultimolistarPayload = payload;
        this.unidadesSeleccionadas = [];
        this.unidadDocumentalApiService.ListarUnidadesDocumentalesTranferir(payload)
        .subscribe(response => {

            if(!isNullOrUndefined(response)){
              const unidadesDocumentales = response.unidadesDocumentales || []
              let ListadoMapeado = unidadesDocumentales.length ? unidadesDocumentales : [];
              this.ListadoUnidadDocumental = [...ListadoMapeado];
              this.ListadoActualizadoSubject.next(response);
            }


        });
    }

    ListarUnidadesDocumentalesVerificar(payload){

      this.unidadesSeleccionadas = [];
      this.ultimolistarPayload = payload;

      this.unidadDocumentalApiService.ListarUnidadesDocumentalesVerificar(payload)
        .subscribe(response => {
          let ListadoMapeado = response.length ? response : [];
          //this.ListadoUnidadDocumental = [...ListadoMapeado];
          this.ListadoUnidadDocumental = ListadoMapeado.map(unidad => {

            if(unidad.estado == 'Rechazado')
               unidad.blocked = true;
            return unidad;
          });
          this.ListadoActualizadoSubject.next();
        });

    }

  ListarUnidadesDocumentalesUbicar(payload){

    this.unidadesSeleccionadas = [];
    this.ultimolistarPayload = payload;
    this.unidadDocumentalApiService.ListarUnidadesDocumentalesUbicar(payload)
      .subscribe(response => {
        let ListadoMapeado = response.length ? response : [];
        this.ListadoUnidadDocumental = [...ListadoMapeado];
        this.ListadoActualizadoSubject.next();
      });

  }

  ListarForGestionarUnidadDocumental(payload?: any) {
    const noShowNoFoundNotification = payload.noShowNoFoundNotification;
    delete  payload.noShowNoFoundNotification;
    this.ultimoList2Payload = payload;
    this.unidadesSeleccionadas = [];
    this.unidadDocumentalApiService.buscarUnidadesDocumentales(payload)
      .subscribe(response => {
        let ListadoMapeado = response.length ? response : [];
        this.ListadoUnidadDocumental = [...ListadoMapeado];

        if(this.ListadoUnidadDocumental.length == 0 && !noShowNoFoundNotification)
           this._store.dispatch(new PushNotificationAction({severity:'info',summary:'El sistema no encuentra la unidad documental que está buscando. Por favor, verifique los criterios de búsqueda'}))
        this.ListadoActualizadoSubject.next();
      });
  }

    ListarDisposicionFinal(payload?: DisposicionFinalDTO, value?: any) {
        this.ultimolistarDisposicionPayload = payload;
        this.unidadesSeleccionadas = [];
        this.unidadDocumentalApiService.listarUnidadesDocumentalesDisposicion(this.ultimolistarDisposicionPayload)
        .subscribe(response => {
            let ListadoMapeado = response.length ? response : [];
            this.ListadoUnidadDocumental = [...ListadoMapeado];
            this.ListadoActualizadoSubject.next();
        });
    }

    Agregar() {
        const unidadesSeleccionadas = this.GetUnidadesSeleccionadas();
        if (unidadesSeleccionadas.length) {
            const listadoMapeado = this.ListadoUnidadDocumental
            .reduce((listado, currenvalue: any) => {
                  const item = this.unidadesSeleccionadas.find(_item => _item.soporte === 'Físico' && _item.fechaCierre === null);
                  if (item) {
                      currenvalue.fechaCierre = this.FechaExtremaFinal;
                  }
                  listado.push(currenvalue);
                  return listado;
            }, []);
            this.ListadoUnidadDocumental = [...listadoMapeado];
        }
    }

    GetUnidadesSeleccionadas(): UnidadDocumentalDTO[] {
        const ListadoFiltrado = this.unidadesSeleccionadas
            if (!ListadoFiltrado.length) {
                this._store.dispatch(new PushNotificationAction({severity: 'warn', summary: this.NoUnidadesSeleccionadas}));
            }
        return ListadoFiltrado;
    }

    Abrir() {
        const unidadesSeleccionadas = this.GetUnidadesSeleccionadas();
        if (unidadesSeleccionadas.length) {
            const payload:any = unidadesSeleccionadas.map(_map => { return { id: _map.id, accion: 'abrir',soporte:_map.soporte } });
          this.ultimoList2Payload.noShowNoFoundNotification = true;
            this.GestionarUnidadesDocumentales(payload);
        }
    }

    Cerrar() {
        const unidadesSeleccionadas: UnidadDocumentalDTO[] = [...this.GetUnidadesSeleccionadas()];
        let payload = {};

        if (unidadesSeleccionadas.length) {
           const pendienteFechaYSoporte = unidadesSeleccionadas.find(item => (item.fechaExtremaFinal === null && item.soporte === 'Físico'));
          this.ultimoList2Payload.noShowNoFoundNotification = true;
           if (pendienteFechaYSoporte) {
               this.confirmationService.confirm({
                message: `<p style="text-align: center">¿Desea que la fecha extrema final sea la misma fecha del cierre del trámite Aceptar / Cancelar. ?</p>`,
                accept: () => {
                    payload = unidadesSeleccionadas.reduce((listado, current) => {
                      const item = {
                        id: current.id,
                        fechaExtremaFinal: (current.fechaExtremaFinal === null && current.soporte === 'Físico') ? current.fechaCierre : current.fechaExtremaFinal,
                        accion: 'cerrar',
                        soporte:current.soporte
                      };

                         listado.push(item);
                        return listado;
                    }, []);
                    this.SubmitCerrar(payload)
                },
                reject: () => {
                    this._store.dispatch(new PushNotificationAction({severity: 'info', summary: this.MensajeIngreseFechaExtremaFinal}));
                }
              });
           } else {
                 payload = unidadesSeleccionadas.map(_map => { return { id: _map.id, accion: 'cerrar',soporte:_map.soporte} });
                 this.SubmitCerrar(payload);
           }

        }
    }

    SubmitCerrar(payload: any) {
        this.GestionarUnidadesDocumentales(payload);
    }

    Reactivar() {
        const unidadesSeleccionadas = this.GetUnidadesSeleccionadas();
        if (unidadesSeleccionadas.length) {
            const payload:any = unidadesSeleccionadas.map(_map => { return { id: _map.id, accion: 'reactivar',soporte:_map.soporte} });
            payload.noShowNoFoundNotification = true;
            this.GestionarUnidadesDocumentales(payload);
        }
    }

    GestionarUnidadesDocumentales(payload: any) {
        this.unidadDocumentalApiService.gestionarUnidadesDocumentales(payload)
        .subscribe(response => {
           this.ManageActionResponse2(response);
        });
    }

    AplicarDisposicion(tipodisposicion: string) {

            const unidadesSeleccionadas = this.GetUnidadesSeleccionadas();
            const existeSeleccionar = this.unidadesSeleccionadas.find(_item => _item.disposicion === 'S');
            if (unidadesSeleccionadas.length) {
                if(existeSeleccionar) {
                        this.ListadoUnidadDocumental = this.ListadoUnidadDocumental.reduce((_listado, _current) => {
                            const item_seleccionado = this.unidadesSeleccionadas.find(_item => _item.id === _current.id && _item.disposicion === 'S')
                            _current.disposicion = item_seleccionado ? tipodisposicion : _current.disposicion;
                            _listado.push(_current);
                            return _listado;
                        }, []);
                        this.ListadoActualizadoSubject.next();
                        this._store.dispatch(new PushNotificationAction({severity: 'success', summary: 'Se aplicó la disposición final satisfactoriamente.'}));
                } else {
                    this._store.dispatch(new PushNotificationAction({severity: 'warn', summary: 'No hay unidades seleccionadas con disposición final "Seleccionar".'}));

                }
            }
    }

    ActualizarEstadoUD(estado: string,isDisposicion:boolean = false) {
        const unidadesSeleccionadas = this.GetUnidadesSeleccionadas();

        const requiereObservaciones = unidadesSeleccionadas.some(_item => (_item.observaciones === '' || _item.observaciones === null) && estado === 'Rechazado');
        if(unidadesSeleccionadas.length) {
            if (requiereObservaciones) {
                this._store.dispatch(new PushNotificationAction({severity: 'error', summary: 'Hay unidades documentales pendiente de notas.'}));
            } else if(isDisposicion && unidadesSeleccionadas.some(_item => _item.disposicion === 'S')) {
                this._store.dispatch(new PushNotificationAction({severity: 'error', summary: 'Hay unidades documentales con disposición final "Seleccionar". Recuerde actualizar.'}));
            } else {
                this.ListadoUnidadDocumental = this.ListadoUnidadDocumental.reduce((_listado, _current) => {
                    const item_seleccionado = this.unidadesSeleccionadas.find(_item => _item === _current)
                    _current.estado = item_seleccionado ? estado : _current.estado;
                    _listado.push(_current);
                    return _listado;
                }, []);


                const data = { datetime:new Date()};
                this.ListadoActualizadoSubject.next(data);

                this._store.dispatch(new PushNotificationAction({severity: 'success', summary: 'Se actualizó el estado de la unidad documental satisfactoriamente.'}));

            }
        }
    }


    ActualizarDisposicionFinal(): void {

      const listado = this.ListadoUnidadDocumental
                        .map( unidad => { delete unidad['_$visited']; return unidad})
                        .filter( unidad => unidad.estado == 'Aprobado');


      if(listado.length == 0){

        this._store.dispatch(new PushNotificationAction({severity: 'error', summary: 'Debe aprobar o rechazar alguna unidad documental.'}));
        return;
      }

      if(listado.some( unidad => unidad.estado == 'Rechazado' && !unidad.observaciones))
      {
        this._store.dispatch(new PushNotificationAction({severity: 'error', summary: 'Verifique que las unidades rechazadas tengan su observación.'}));
        return;
      }


        this.unidadDocumentalApiService.aprobarRechazarUDDisposicion(listado)
        .subscribe(response => {

          if(isNullOrUndefined(this.ultimolistarDisposicionPayload))
            this.ultimolistarDisposicionPayload = {};

          this.ultimolistarDisposicionPayload.noShowNoFoundNotification = true;
            this.ManageActionResponseForDisposicionFinal(response);
            if(response.codMensaje === '0000') {

              this.subscribers.push(this._store.select(getSelectedDependencyGroupFuncionario)
                .subscribe(dependency => {

                  if(listado.some( u => u.disposicion == "CT" && u.estado == 'Aprobado')){

                    const first = listado[0];
                    const proceso:ProcesoDTO = {
                      codigoProceso:'proceso.transferencia-documentales',
                      nombreProceso: 'transferencia-documentales',
                      idDespliegue:'co.com.soaint.sgd.process:proceso-transferencia-documentales:1.0.0-SNAPSHOT',
                      estado: 'LISTO',
                      customParams:{
                        megaf:1,
                        nombreDependencia:dependency.nombre,
                        dependencia:first.codigoDependencia
                      }
                    };

                    this._processSandBox.startProcess(proceso,dependency)
                      .subscribe(_ => {
                        this._store.dispatch(go(['/' + ROUTES_PATH.workspace]));
                      },error => {this._store.dispatch(new actions.LoadFailAction({error}))});

                    return;
                  }

                  this._store.dispatch(go(['/' + ROUTES_PATH.workspace]));

                }))
            }

        });
    }

    ActualizarAprobarTransferencia(tipoTransferencia:string): Observable<any> {

      const estado = tipoTransferencia == 'primaria' ? 'Aprobado' : 'Ubicada';

      const listado = this.ListadoUnidadDocumental
                        .map( unidad => { delete unidad['_$visited']; return unidad})
                       .filter(unidad => unidad.estado == estado || unidad.estado == 'Rechazado' );

      if(listado.length === 0){

        this._store.dispatch(new PushNotificationAction({severity:"error",summary:"Debe aprobar o rechazar todas las unidades seleccionadas"}))
        return Observable.empty();
      }
      return    this.unidadDocumentalApiService.aprobarRechazarUDAprobarTransferencia(tipoTransferencia,listado)
          //.do(response =>  this.ManageActionResponse(response));

    }

    ActualizarVerificarTransferencia(): Observable<any>{

        const listadoVerificar = this.ListadoUnidadDocumental.filter(unidad => !unidad.blocked)
                                     .map( unidad => { delete unidad['_$visited']; return unidad})
                                    .filter(unidad => (unidad.estado == 'Confirmada' || unidad.estado == 'Rechazado' ) && !unidad.blocked);

      if(listadoVerificar.length === 0){

        this._store.dispatch(new PushNotificationAction({severity:"error",summary:"Debe aprobar o rechazar todas las unidades seleccionadas"}))
        return Observable.empty();
      }

       return this.unidadDocumentalApiService.aprobarRechazarUDVerificarTransferencia(listadoVerificar)
         // .do(response => this.ManageActionResponseVerificar(response))

    }

  ActualizarUbicarTransferencia(): Observable<any>{

    const listadoVerificar = this.ListadoUnidadDocumental.filter(unidad => unidad.estado == 'Ubicada')
                                   .map( unidad => {
                                     delete unidad['_$visited'];

                                     if(!isNullOrUndefined(unidad.unidadesConservacion))
                                       unidad.unidadesConservacion = unidad.unidadesConservacion.map( uc => {
                                       delete (<any>uc)._$visited;
                                       return uc;
                                     });

                                     return unidad;
                                   }) ;

    if(listadoVerificar.length === 0){

      this._store.dispatch(new PushNotificationAction({severity:"error",summary:"Debe aprobar o rechazar todas las unidades seleccionadas"}))
      return Observable.empty();
    }

    return this.unidadDocumentalApiService.aprobarRechazarUDVerificarTransferencia(listadoVerificar)
     // .do(response => this.ManageActionResponseUbicar(response))

  }

    GuardarObservacion(unidadDocumental: UnidadDocumentalDTO, index: number) {
        this.ListadoUnidadDocumental[index] = unidadDocumental;
    }

    ManageActionResponse(response: MensajeRespuestaDTO) {
        const mensajeRespuesta: MensajeRespuestaDTO = response;
        const mensajeSeverity = (mensajeRespuesta.codMensaje === '0000') ? 'success' : 'error';
        this._store.dispatch(new PushNotificationAction({severity: mensajeSeverity, summary: mensajeRespuesta.mensaje}));
        this.Listar(this.ultimolistarPayload);
    }


  ManageActionResponseVerificar(response: MensajeRespuestaDTO) {
    const mensajeRespuesta: MensajeRespuestaDTO = response;
    const mensajeSeverity = (mensajeRespuesta.codMensaje === '0000') ? 'success' : 'error';
    this._store.dispatch(new PushNotificationAction({severity: mensajeSeverity, summary: mensajeRespuesta.mensaje}));
    this.ListarUnidadesDocumentalesVerificar(this.ultimolistarPayload);
  }

  ManageActionResponseUbicar(response: MensajeRespuestaDTO) {
    const mensajeRespuesta: MensajeRespuestaDTO = response;
    const mensajeSeverity = (mensajeRespuesta.codMensaje === '0000') ? 'success' : 'error';
    this._store.dispatch(new PushNotificationAction({severity: mensajeSeverity, summary: mensajeRespuesta.mensaje}));
    this.ListarUnidadesDocumentalesUbicar(this.ultimolistarPayload);
  }


  ManageActionResponse2(response: MensajeRespuestaDTO) {
    const mensajeRespuesta: MensajeRespuestaDTO = response;
    const mensajeSeverity = (mensajeRespuesta.codMensaje === '0000') ? 'success' : 'error';
    this._store.dispatch(new PushNotificationAction({severity: mensajeSeverity, summary: mensajeRespuesta.mensaje}));
    this.ListarForGestionarUnidadDocumental(this.ultimoList2Payload);
  }

  ManageActionResponseForDisposicionFinal(response: MensajeRespuestaDTO) {
    const mensajeRespuesta: MensajeRespuestaDTO = response;
    const mensajeSeverity = (mensajeRespuesta.codMensaje === '0000') ? 'success' : 'error';
    if(!this.ultimolistarDisposicionPayload.noShowNoFoundNotification)
    this._store.dispatch(new PushNotificationAction({severity: mensajeSeverity, summary: mensajeRespuesta.mensaje}));

    delete this.ultimolistarDisposicionPayload.noShowNoFoundNotification;
    this.ListarDisposicionFinal(this.ultimolistarDisposicionPayload);
  }



}
