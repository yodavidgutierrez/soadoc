import {ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Store} from '@ngrx/store';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {createSelector} from 'reselect';
import {getActiveTask} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {Sandbox as TaskSandBox} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import {PdMessageService} from './providers/PdMessageService';
import {Subscription} from 'rxjs/Subscription';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {TaskForm} from 'app/shared/interfaces/task-form.interface';
import {Observable} from 'rxjs/Observable';
import {TareaDTO} from 'app/domain/tareaDTO';
import {ProduccionDocumentalApiService} from '../../../infrastructure/api/produccion-documental.api';
import {StatusDTO, VariablesTareaDTO} from './models/StatusDTO';
import {getAuthenticatedFuncionario} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {FuncionarioDTO} from '../../../domain/funcionarioDTO';
import {ProyectorDTO} from '../../../domain/ProyectorDTO';
import {ActivatedRoute} from '@angular/router';
import {PushNotificationAction} from '../../../infrastructure/state-management/notifications-state/notifications-actions';
import {DestinatarioDTO} from '../../../domain/destinatarioDTO';
import {AgentDTO} from '../../../domain/agentDTO';
import {MessagingService} from '../../../shared/providers/MessagingService';
import {DocumentDownloaded} from './events/DocumentDownloaded';
import {environment} from '../../../../environments/environment';
import {DocumentUploaded} from './events/DocumentUploaded';
import {afterTaskComplete} from "../../../infrastructure/state-management/tareasDTO-state/tareasDTO-reducers";
import {AlertComponent} from "../../bussiness-components/notifications/alert/alert.component";
import {isNullOrUndefined} from "util";
import {ROUTES_PATH} from "../../../app.route-names";
import {go} from "@ngrx/router-store";
import {PdObservacionApi} from "../../../infrastructure/api/pd-observacion.api";
import {PdObservacionDTO} from "../../../domain/pdObservacionDTO";
import {
  AGENTE_INTERNO, COMUNICACION_EXTERNA_ENVIADA,
  COMUNICACION_INTERNA_ENVIADA, DESTINATARIO_PRINCIPAL,
  TIPOLOGIA_MEMORANDO, TIPOLOGIA_OFICIO
} from "../../../shared/bussiness-properties/radicacion-properties";
import {RolDTO} from "../../../domain/rolesDTO";
import {ConfirmationService} from "primeng/primeng";

@Component({
  selector: 'produccion-documental',
  templateUrl: './produccion-documental.component.html',
  styleUrls: ['produccion-documental.component.css'],
})

export class ProduccionDocumentalComponent implements OnInit, OnDestroy, TaskForm {

  task: TareaDTO;
  taskCurrentStatus: StatusDTO;
  taskVariables: VariablesTareaDTO;
  idEstadoTarea = '0000';
  status = 1;
  closedTask: Observable<boolean> ;
  tabIndex = 0;

  observaciones:PdObservacionDTO[];

  observacionesVisible = false;

  sectionsVisible = {interno:true,externo:true};

  @ViewChild('datosGenerales') datosGenerales;
  @ViewChild('datosContacto') datosContacto;
  @ViewChild('gestionarProduccion') gestionarProduccion;
  @ViewChild('messageAlert') messageAlert:AlertComponent;

  tipoComunicacionSelected: ConstanteDTO;
  funcionarioLog: FuncionarioDTO;
  subscription: Subscription;
  rolAprobador:  RolDTO[];
  errorAprobar: string;
  destruirmensaje: number = 0;
  noPuedeAprobar: boolean = false;
  idecmRadicado: string;
  pdfViewer = false;



  authPayload: { usuario: string, pass: string } | {};
  authPayloadUnsubscriber: Subscription;
  documentSubscription: Subscription;
  completeTaskSubscription:Subscription;

  constructor(private _store: Store<RootState>,
              private _taskSandBox: TaskSandBox,
              private route: ActivatedRoute,
              private _produccionDocumentalApi: ProduccionDocumentalApiService,
              private _changeDetectorRef: ChangeDetectorRef,
              private messagingService: MessagingService,
              private pdMessageService: PdMessageService,
              private _pdObservacionesApi:PdObservacionApi,
              private confirmService: ConfirmationService

  ) {


      this.route.params.subscribe( params => {
          this.status = parseInt(params.status, 10) || 0;
      } );

  }

  private initCurrentStatus() {
      this.taskCurrentStatus = {
          datosGenerales: {
              tipoComunicacion: null,
              tipoPlantilla: null,
              listaVersionesDocumento: [],
              listaAnexos: []
          },
          datosContacto: {
              distFisica: false,
              distElectronica:false,
              responderRemitente: false,
              hasDestinatarioPrincipal: false,
              issetListDestinatarioBackend: false,
              listaDestinatariosInternos: [],
              listaDestinatariosExternos: []
          },
          gestionarProduccion: {
              startIndex: this.gestionarProduccion.listaProyectores.length,
              listaProyectores: this.gestionarProduccion.listaProyectores,
              cantObservaciones: this.gestionarProduccion.listaObservaciones.length,
              listaObservaciones: this.gestionarProduccion.listaObservaciones
          }
      };
  }

  ngOnInit(): void {

    this.closedTask = afterTaskComplete.map(() => true).startWith(false);

    this.documentSubscription = this.messagingService.of(DocumentUploaded).subscribe(() => {
      this.refreshView();
      this.guardarEstadoTarea();
    });
    this.subscription = this.pdMessageService.getMessage().subscribe(tipoComunicacion => {
      this.tipoComunicacionSelected = tipoComunicacion;
    });
    this.authPayloadUnsubscriber = this._store.select(createSelector((s: RootState) => s.auth.profile, (profile) => {
      return profile ? {usuario: profile.username, pass: profile.password} : {};
    })).subscribe((value) => {
      this.authPayload = value;
    });

    this.completeTaskSubscription = afterTaskComplete.subscribe( _ => {

      this.datosGenerales.activeTaskUnsubscriber.unsubscribe();
      this._store.dispatch(go(['/' + ROUTES_PATH.workspace]));
    });


        this._store.select(getAuthenticatedFuncionario).subscribe((funcionario) => {
          this.funcionarioLog = funcionario;
        });
        this._store.select(getActiveTask).take(1).subscribe(activeTask => {
      this.task = activeTask;

        if (activeTask.variables && activeTask.variables.numeroRadicado) {
            console.log('Buscando documento asociado')
            this._produccionDocumentalApi.obtenerDatosDocXnroRadicado({id: activeTask.variables.numeroRadicado}).subscribe(
                res => {
                    if (res.ideEcm) {
                        this.idecmRadicado = res.ideEcm ;
                        this.pdfViewer = true;
                        this.refreshView();
                    } else {
                        this._store.dispatch(new PushNotificationAction({severity: 'error', summary: `No se encontro ningún documento asociado al radicado: ${activeTask.variables.numeroRadicado}`}));
                    }
                },
                error => this._store.dispatch(new PushNotificationAction({severity: 'warn', summary: error}))
            );
        }

          this._produccionDocumentalApi.obtenerEstadoTarea({
            idInstanciaProceso: this.task.idInstanciaProceso,
            idTareaProceso: this.idEstadoTarea
          })
            .map( response => JSON.parse(response))
            .catch( _ => {
              this.gestionarProduccion.initProyeccionLista(activeTask.variables.listaProyector || [], 'proyector');
              this.gestionarProduccion.initProyeccionLista(activeTask.variables.listaRevisor || [], 'revisor');
              this.gestionarProduccion.initProyeccionLista(activeTask.variables.listaAprobador || [], 'aprobador');
              this.initCurrentStatus();

              return  Observable.of(this.taskCurrentStatus);
            })
            .subscribe(
              (status: StatusDTO) => {
              if (status) {
                    this.taskCurrentStatus = status;
                    this.datosGenerales.updateStatus(status);
                    this.datosContacto.updateStatus(status);
                    this.gestionarProduccion.updateStatus(status);
                                 } else {
                    this.gestionarProduccion.initProyeccionLista(activeTask.variables.listaProyector || [], 'proyector');
                    this.gestionarProduccion.initProyeccionLista(activeTask.variables.listaRevisor || [], 'revisor');
                    this.gestionarProduccion.initProyeccionLista(activeTask.variables.listaAprobador || [], 'aprobador');
                    this.initCurrentStatus();
              }
            }
          )
            ;
        });
  }

  guardarEstadoTarea(currentStatus?: StatusDTO) {
    const tareaDTO = {
      idTareaProceso: this.idEstadoTarea,
      idInstanciaProceso: this.task.idInstanciaProceso,
      payload:JSON.stringify(currentStatus || this.getCurrentStatus()),
    };
    this._produccionDocumentalApi.guardarEstadoTarea(tareaDTO).subscribe(_ => {

      this.actualizarObservaciones();
    });
  }

  updateEstadoTarea() {
      const currentStatus = this.getCurrentStatus();
      currentStatus.gestionarProduccion.startIndex = currentStatus.gestionarProduccion.listaProyectores.length;
      currentStatus.gestionarProduccion.cantObservaciones = currentStatus.gestionarProduccion.listaObservaciones.length;
      this.guardarEstadoTarea(currentStatus);
  }

  getCurrentStatus(): StatusDTO {
      this.taskCurrentStatus.datosGenerales.tipoComunicacion = this.datosGenerales.form.get('tipoComunicacion').value;
      this.taskCurrentStatus.datosGenerales.tipoPlantilla = this.datosGenerales.form.get('tipoPlantilla').value;
      this.taskCurrentStatus.datosGenerales.listaVersionesDocumento = this.datosGenerales.listaVersionesDocumento;
      this.taskCurrentStatus.datosGenerales.listaAnexos = this.datosGenerales.listaAnexos;

      this.taskCurrentStatus.datosContacto.distFisica = this.datosContacto.form.get('distFisica').value;
      this.taskCurrentStatus.datosContacto.distElectronica = this.datosContacto.form.get('distElectronica').value;
      this.taskCurrentStatus.datosContacto.responderRemitente = this.datosContacto.form.get('responderRemitente').value;
      this.taskCurrentStatus.datosContacto.hasDestinatarioPrincipal = this.datosContacto.hasDestinatarioPrincipal;
      this.taskCurrentStatus.datosContacto.issetListDestinatarioBackend = this.datosContacto.issetListDestinatarioBacken;
      this.taskCurrentStatus.datosContacto.listaDestinatariosInternos = this.datosContacto.listaDestinatariosInternos;
      this.taskCurrentStatus.datosContacto.listaDestinatariosExternos = this.datosContacto.listaDestinatariosExternos;

      this.taskCurrentStatus.gestionarProduccion.listaProyectores = this.gestionarProduccion.listaProyectores;
      this.taskCurrentStatus.gestionarProduccion.startIndex = this.gestionarProduccion.startIndex;
      this.taskCurrentStatus.gestionarProduccion.listaObservaciones = this.gestionarProduccion.listaObservaciones;
      this.taskCurrentStatus.gestionarProduccion.cantObservaciones = this.gestionarProduccion.cantObservaciones;

      console.log("before save:",this.taskCurrentStatus);
      return this.taskCurrentStatus;
  }

  construirListas() {

    if(isNullOrUndefined(this.taskVariables.listaProyector))
       this.taskVariables.listaProyector = [];
    if(isNullOrUndefined(this.taskVariables.listaRevisor))
      this.taskVariables.listaRevisor = [];
    if(isNullOrUndefined(this.taskVariables.listaAprobador))
      this.taskVariables.listaAprobador = [];



      this.gestionarProduccion.getListaProyectores().forEach(el => {

          if (el.rol.rol === 'proyector') {
              this.taskVariables.listaProyector.push(el.funcionario.loginName.concat(':').concat(el.dependencia.codigo));
          } else
          if (el.rol.rol === 'revisor') {
              this.taskVariables.listaRevisor.push(el.funcionario.loginName.concat(':').concat(el.dependencia.codigo));
          } else
          if (el.rol.rol === 'aprobador') {
              this.taskVariables.listaAprobador.push(el.funcionario.loginName.concat(':').concat(el.dependencia.codigo));
          }

      });

      this.taskVariables.remitente = {ideFunci: this.funcionarioLog.id,codTipAgent:AGENTE_INTERNO};




      if(!isNullOrUndefined(this.task.variables.numeroRadicado))
        this.taskVariables.nroRadicado = this.task.variables.numeroRadicado;
  }

    continuarProceso() {

      if(!this.ExistsDestinatario()){

        this._store.dispatch(new PushNotificationAction({severity: 'error', summary: 'Debe introducir al menos un destinatario principal'}));
        return false;
      }

       if (!this.hasAprobador()) {
            console.log(`No hay aprobador`);
            this._store.dispatch(new PushNotificationAction({severity: 'error', summary: 'Debe especificar al menos un aprobador'}));
            return false;
        }

        const indexFuncionario = this.gestionarProduccion.listaProyectores.findIndex( proyector => proyector.funcionario.id == this.funcionarioLog.id);
        let rolFilter = this.gestionarProduccion.listaProyectores[indexFuncionario].rol.rol;
        let destinatario = null;

        if(indexFuncionario >= 0){

          destinatario =  this.gestionarProduccion.listaProyectores.find( (proyector,idx) => idx > indexFuncionario  && proyector.rol.rol == rolFilter);

          while(isNullOrUndefined(destinatario) && rolFilter != 'aprobador'){
           rolFilter = rolFilter == 'proyector' ? 'revisor' : 'aprobador';
            destinatario =  this.gestionarProduccion.listaProyectores.find( proyector => proyector.rol.rol == rolFilter);
          }

        }

        this.gestionarProduccion.listaProyectores = this.gestionarProduccion.listaProyectores
                                                      .map( proyector => { proyector.blocked = true; return proyector});

        this.taskVariables =  {aprobado : this.status == 1 ? 0 : 1, listaProyector : [], listaRevisor : [], listaAprobador : [] };

      if(!isNullOrUndefined(destinatario) ){
        this.taskVariables.destinatario = { ideFunci:<number>(destinatario.funcionario.id),codTipAgent : AGENTE_INTERNO};
        this.taskVariables.notifiable = true;

        switch ( destinatario.rol.rol) {
          case 'aprobador' :  this.taskVariables.nombreTarea = 'Aprobar Documento';
           break;
          case 'revisor' :    this.taskVariables.nombreTarea = 'Revisar Documento';
           break;
          case 'proyector' :  this.taskVariables.nombreTarea = 'Producir Documento';
        }

      }

        if(this.status == 2)
           this.taskVariables.requiereAjustes = 0;

        if(this.status == 1)
           this.taskVariables.fechaProd = new Date().getTime();

        this.construirListas();
        this.updateEstadoTarea();
        this.terminarTarea();
        return true;
    }

    devolverDocumento() {

      if(this.observaciones.every( observacion => observacion.idTarea != this.task.idTarea)){

        this._store.dispatch(new PushNotificationAction({severity:"error",summary:"Debe de agregar alguna observación en esta sección"}));

        return;
      }

        this.taskVariables = {};
        if (this.status === 2) {
            this.taskVariables = { requiereAjustes: 1 };
        } else if (this.status === 3) {
            this.taskVariables = { aprobado: 0 };
        }

      const destinatario =   this.gestionarProduccion.listaProyectores[0];
      this.taskVariables.destinatario = { ideFunci:<number>(destinatario.funcionario.id),codTipAgent : AGENTE_INTERNO};
      this.taskVariables.nombreTarea = 'Documento devuelto';
      this.taskVariables.notifiable = true;

        this.construirListas();
        this.updateEstadoTarea();
        this.terminarTarea();
    }

    private ExistsDestinatario(){

    const lenght1 = isNullOrUndefined(this.datosContacto.listaDestinatariosInternos) ? 0 : this.datosContacto.listaDestinatariosInternos.filter(d => d.tipoDestinatario.codigo == DESTINATARIO_PRINCIPAL).length;

    const lenght2 = isNullOrUndefined(this.datosContacto.listaDestinatariosExternos) ? 0 : this.datosContacto.listaDestinatariosExternos.filter(d => d.tipoDestinatario.codigo == DESTINATARIO_PRINCIPAL).length;


     return  lenght1 + lenght2 > 0;

    }

    private actualizarObservaciones(){

       if(isNullOrUndefined(this.observaciones))
          this.observaciones = [];

      this._pdObservacionesApi.actualizar(this.observaciones).subscribe();
    }

  aprobarDocumento() {

    if(!this.ExistsDestinatario()){

      this._store.dispatch(new PushNotificationAction({severity: 'error', summary: 'Debe introducir al menos un destinatario'}));
      return ;
    }

    const listaProyectores = this.gestionarProduccion.listaProyectores;

    if(listaProyectores.length === 1 && !listaProyectores[0].funcionario.firmaPolitica){
      this._store.dispatch(new PushNotificationAction({severity: 'error', summary: 'Usted no tiene firma. Solicítela o agregue un aprobador'}));;

      return;
    }

      switch (this.status) {
          case 1 : {
              this.taskVariables = {aprobado : 1, listaProyector : [], listaRevisor : [],  };
              this.construirListas();
              break;
          }
          case 2 : {
              this.taskVariables = { requiereAjustes: 0, listaAprobador : [] };
              break;
          }
          case 3 : {
              this.taskVariables = { aprobado: 1, listaAprobador : [] };
              break;
          }
          default : {
              this.taskVariables = {};
              break;
          }
      }
    const destinatario = this.gestionarProduccion.listaProyectores[0];

    if(destinatario.funcionario.id != this.funcionarioLog.id){

      this.taskVariables.notifiable= true;
      this.taskVariables.destinatario = { ideFunci:<number>(destinatario.funcionario.id),codTipAgent : AGENTE_INTERNO};
      this.taskVariables.remitente = {ideFunci: this.funcionarioLog.id,codTipAgent : AGENTE_INTERNO};
      this.taskVariables.nombreTarea = "Radicar Documento Producido";
    }

      this.observaciones = [];

        this.updateEstadoTarea();
        this.terminarTarea();

  }

    cancelarTarea(force = false) {

    if(!force){

      this.confirmService.confirm({
        message: "¿Está seguro que desea cancelar el flujo de trabajo?",
        header: "Confirmacion",
        icon : 'fa fa-question-circle',
        accept: _ => { this.cancelarTarea(true)},
        reject: _ => {}
      });

      return;

    }



        this._taskSandBox.abortTaskDispatch({
            idProceso: this.task.idProceso,
            idDespliegue: this.task.idDespliegue,
            instanciaProceso: this.task.idInstanciaProceso
        });
    }


    terminarTarea() {
      this.taskVariables.usuarioProyectorRadicador = this.gestionarProduccion.listaProyectores[0].funcionario.loginName;
      this.taskVariables.codDependencia = this.gestionarProduccion.listaProyectores[0].dependencia.codigo;


      this._taskSandBox.completeTaskDispatch({
          idProceso: this.task.idProceso,
          idDespliegue: this.task.idDespliegue,
          idTarea: this.task.idTarea,
          parametros: this.taskVariables
      });
    }

    puedeAprobar() {

       const listaProyectores = this.gestionarProduccion.listaProyectores;

        if(this.status == 3 && listaProyectores.length > 0 ){

          const lastAprobador = listaProyectores
                                    .reduce((prev:ProyectorDTO,curr:ProyectorDTO) => (!isNullOrUndefined(curr) && curr.rol.nombre == 'Aprobador')?  curr :prev,null);

          if(lastAprobador.funcionario.id != this.funcionarioLog.id)
             return false;
        }

      let firma = this.funcionarioLog.firmaPolitica;
      this.rolAprobador = this.funcionarioLog.roles;

      let esAprobador = this.rolAprobador.find(function(element) {
        if (isNullOrUndefined(element.rol)){
          esAprobador = null;
        }

        return element.rol === "Aprobador";
      });

      if ( firma == "" || isNullOrUndefined(esAprobador)){

        if (this.destruirmensaje == 0){
          if (firma == "" && isNullOrUndefined(esAprobador)){
            this.errorAprobar = "El usuario no cuenta con el rol de aprobador ni con una firma digital."
          }
          else if (firma == "") {
            this.errorAprobar = "El usuario no cuenta con una firma digital."
          }
          else if (isNullOrUndefined(esAprobador)){
            this.errorAprobar = "El usuario no cuenta con el rol de Aprobador."
          }

          this.noPuedeAprobar = true;
          this.destruirmensaje = 1;
          this._store.dispatch(new PushNotificationAction({severity: 'error', summary: this.errorAprobar}));
        }

        return false;

      }

        let rules:boolean[] = [
          (this.status > 1 )  || (1 === this.status && 1 === listaProyectores.length),
          this.isValid(),
        ];

        return rules.every( condition => condition);
    }

  closenoPuedeAprobar(){
    this.noPuedeAprobar = false;
  }


    hasAprobador() {

    const listaProyectores = this.gestionarProduccion.getListaProyectores();

    if(listaProyectores.length   == 0 )
      return false;

     return  this.gestionarProduccion.getListaProyectores()
       .filter((el: ProyectorDTO) => el.funcionario.roles.some(rol => rol.rol == 'Aprobador')).length > 0;
    }

    showContinuarButton():boolean{

   if(isNullOrUndefined(this.task))
       return false;

    if(this.status > 1 || this.task.descripcion.indexOf("Ajustar Documento") != -1)
       return true;

      const listaProyectores = this.gestionarProduccion.getListaProyectores();

       return listaProyectores.some( proyector => proyector.rol.nombre == "Aprobador");

    }

    puedeContinuar(){
      if(this.status == 3 && this.gestionarProduccion.listaProyectores.length > 0 ){

        const lastAprobador = this.gestionarProduccion.listaProyectores
          .reduce((prev:ProyectorDTO,curr:ProyectorDTO) => (!isNullOrUndefined(curr) && curr.rol.nombre == 'Aprobador')?  curr :prev,null);

        if(lastAprobador.funcionario.id == this.funcionarioLog.id)
          return false;
      }

      return this.isValid();
    }

    isValid(): boolean {


       let valid = true;

      let rules:boolean[] = [
        valid && this.datosGenerales.isValid(),
      ];
      return rules.every( condition => condition);
    }

    ngOnDestroy(): void {
        this.authPayloadUnsubscriber.unsubscribe();
        this.documentSubscription.unsubscribe();
        this.subscription.unsubscribe();
        this.completeTaskSubscription.unsubscribe();
    }

    save(): Observable<any> {
        return Observable.of(true).delay(5000);
    }

    refreshView() {
        this._changeDetectorRef.detectChanges();
    }

    updateObservaciones(observaciones){
       this.observaciones = observaciones;
    }

  openNext() {
    this.tabIndex =  (this.tabIndex + 1)%4;
  }

  openPrev() {
    this.tabIndex = (this.tabIndex +3) % 4;
  }

  detectChangeOnTipoPlantillaComunicacion(event){
      this.sectionsVisible ={
        interno:event.tipoComunicacion != COMUNICACION_EXTERNA_ENVIADA,
        externo:  event.tipoComunicacion != COMUNICACION_INTERNA_ENVIADA
      };
  }


}
