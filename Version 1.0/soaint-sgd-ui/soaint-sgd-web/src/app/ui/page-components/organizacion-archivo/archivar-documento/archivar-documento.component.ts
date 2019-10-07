import {ChangeDetectorRef, Component, OnChanges, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ArchivarDocumentoModel} from "./models/archivar-documento.model";
import {SolicitudCreacioUdModel} from "./models/solicitud-creacio-ud.model";
import {SolicitudCreacionUdService} from "../../../../infrastructure/api/solicitud-creacion-ud.service";
import {Sandbox as TaskSandbox} from "../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox";
import {TareaDTO} from "../../../../domain/tareaDTO";
import {State as RootState} from "../../../../infrastructure/redux-store/redux-reducers";
import {Store} from "@ngrx/store";
import {getActiveTask} from "../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors";
import {Subscription} from "rxjs/Subscription";
import {SeleccionarUnidadDocumentalComponent} from "./components/seleccionar-unidad-documental/seleccionar-unidad-documental.component";
import {ArchivarDocumentoApiService} from "../../../../infrastructure/api/archivar-documento.api";
import {isNullOrUndefined} from "util";
import {
  getAuthenticatedFuncionario,
  getSelectedDependencyGroupFuncionario
} from "../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {combineLatest} from "rxjs/observable/combineLatest";
import * as moment from "moment";
import {SeleccionarDocumentosComponent} from "./components/seleccionar-documentos/seleccionar-documentos.component";
import {Sandbox as AsigSandbox} from "../../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox";
import {Observable} from "rxjs/Observable";
import {go} from "@ngrx/router-store";
import {afterTaskComplete} from "../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-reducers";
import {AGENTE_INTERNO} from "../../../../shared/bussiness-properties/radicacion-properties";
import {FuncionariosService} from "../../../../infrastructure/api/funcionarios.service";
import {PushNotificationAction} from "../../../../infrastructure/state-management/notifications-state/notifications-actions";
import {FuncionarioDTO} from "../../../../domain/funcionarioDTO";
import {ROUTES_PATH} from "../../../../app.route-names";
import {ARCHIVISTA} from "../../../../app.roles";


@Component({
  selector: 'app-archivar-documento',
  templateUrl: './archivar-documento.component.html',
  styleUrls: ['./archivar-documento.component.css']
})
export class ArchivarDocumentoComponent implements OnInit,OnDestroy {

   archivarDocumentoModel:ArchivarDocumentoModel = new ArchivarDocumentoModel();

   solicitudUDModel: SolicitudCreacioUdModel;

   currentPage:number = 1;

   enableButtonNext:boolean = false;

   showSolicitarButton:boolean = false;

   subscriptions:Subscription[] = [];

   task:TareaDTO;

   nroRadicado ;
   selectUd:SeleccionarUnidadDocumentalComponent;

  idEstadoTarea = '0000';

  activeTarea:boolean = true;


  savedData: any;

  @ViewChild('selectDocument')
  seleccionarDocumentosComponent:SeleccionarDocumentosComponent;

  @ViewChild('selectUD') selectUD:SeleccionarUnidadDocumentalComponent;


   constructor( private _solicitudService:SolicitudCreacionUdService,
               private  _taskSandbox:TaskSandbox,
               private _store:Store<RootState>,
               private _archivarDocumentoApi:ArchivarDocumentoApiService,
               private _asignacionSandbox:AsigSandbox,
               private funcionarioApi:FuncionariosService
               ) {

    this.solicitudUDModel = new SolicitudCreacioUdModel(this._solicitudService);
  }

  ngOnInit() {

   this.subscriptions.push(
     this._store.select(getActiveTask).subscribe(task => {

       this.task = task;

       if(task){

         this.subscriptions.push(
           this._archivarDocumentoApi.getTaskData(task.idInstanciaProceso,'0000').subscribe( response => {

             if(!isNullOrUndefined(response.payload)){

               const payload = JSON.parse(response.payload);

               if(payload.tab == 1){
                 if(!isNullOrUndefined(payload.data))
                   this.selectUD.form.setValue(payload.data);
                 if(!isNullOrUndefined(payload.operation))
                   this.selectUD.operation = payload.operation;
                 if(!isNullOrUndefined(payload.solicitudes)){
                   this.solicitudUDModel.Solicitudes = payload.solicitudes;
                   if(payload.operation== "solicitarUnidadDocumental")
                    this.selectUD.unidadesDocumentales$ = Observable.of(this.solicitudUDModel.Solicitudes);
                 }
               }
               else{
                 this.archivarDocumentoModel = new ArchivarDocumentoModel(payload.model._unidadDocumental,payload.model._documentos) ;
                 this.currentPage = 2;
                 this.savedData = payload;
               }
             }
           })
         );

         if(!isNullOrUndefined(this.task.variables &&this.task.variables.numeroRadicado)){

           this.nroRadicado = this.task.variables.numeroRadicado;
         }
       }
     })
  );

   this.subscriptions.push(this._store.select(getSelectedDependencyGroupFuncionario).subscribe( () => {

     if(this.currentPage == 1)
       return;

     this.seleccionarDocumentosComponent.DropSubscriptions();

     this.currentPage = 1;

     this.enableButtonNext = false;

   }));

    this.subscriptions.push(afterTaskComplete.subscribe(() => {

      if(this.showSolicitarButton)
       this._store.dispatch(go(['/' + ROUTES_PATH.workspace]));
    }));
   }

  next(){
    this.currentPage++;
  }

  prev(){

    this.currentPage --;
    this.enableButtonNext = false;
    this.archivarDocumentoModel.Documentos = [];
  }

  save(){

    const tareaDTO = {
      idTareaProceso: this.idEstadoTarea,
      idInstanciaProceso: this.task.idInstanciaProceso,
      payload: {

      },
    };

     const payload = {currentPage: this.currentPage};

     if(this.currentPage == 1){

       tareaDTO.payload = JSON.stringify({
                           tab:this.currentPage,
                           data: this.selectUD.form.value,
                           operation: this.selectUD.operation,
                           solicitudes: this.solicitudUDModel.Solicitudes
                         });
     }
     else{
       tareaDTO.payload = JSON.stringify({
         tab:this.currentPage,
         documentosAdjuntos: this.seleccionarDocumentosComponent.documentosAdjuntos.map(doc => ({
            idDocumento: doc.idDocumento,
           nombreDocumento: doc.nombreDocumento,
           isAttachment:true,
           identificador:doc.identificador
         })),
         model:this.archivarDocumentoModel,
         documentos:  this.seleccionarDocumentosComponent.documentos.map(doc => ({
           idDocumento: doc.idDocumento,
           nombreDocumento: doc.nombreDocumento,
           isAttachment:true,
           identificador:doc.identificador,
           tipologiaDocumental: doc.tipologiaDocumental
         }))
       });

     }



     this._archivarDocumentoApi.guardarEstadoTarea(tareaDTO).subscribe(() => {

       this._store.dispatch(new PushNotificationAction({summary:'Se han salvado los datos de la tarea exitosamente',severity:'success'}))

       if(this.currentPage == 2)
         this.seleccionarDocumentosComponent.hasDefaultDocumentForSave = this.archivarDocumentoModel.Documentos.length >0;
     });
  }

  solicitarCreacionUd(){

     this.subscriptions.push(
       this._store.select(getAuthenticatedFuncionario).subscribe( funcionario => {

         if(funcionario.roles.some(rol => rol.rol == 'Archivista')){

           this.sendSolicitud(funcionario);
           return;
         }

         this.subscriptions.push(this._store.select(getSelectedDependencyGroupFuncionario)
                                    .switchMap( dependencia => this.funcionarioApi
                                                              .getFuncionarioBySpecification( funcionario => {
                                                         return funcionario.dependencias.some(dep => dep.codigo == dependencia.codigo)
                                                                &&  funcionario.roles.some(rol => rol.rol == 'Archivista')
                                                              })
                                  ).subscribe( funcionarios => {

                                    if(funcionarios.length == 0){
                                      this._store.dispatch(new PushNotificationAction({severity:"error",summary:"No existe archivist en esta dependencia"}));
                                      return;
                                    }

                                    this.sendSolicitud(funcionarios[0]);

             })
           )
       })
     );
   }

  private sendSolicitud(archivista:FuncionarioDTO){

    this.subscriptions.push(this.solicitudUDModel.Solicitar().subscribe( () => {

      this.solicitudUDModel.Solicitudes = [];

      this.subscriptions.push(
        combineLatest(this._store.select(getSelectedDependencyGroupFuncionario)
          ,this._store.select(getAuthenticatedFuncionario)).subscribe(([dependencia,funcionario]) => {
          this._taskSandbox.completeTaskDispatch({
            idProceso: this.task.idProceso,
            idDespliegue: this.task.idDespliegue,
            idTarea: this.task.idTarea,
            parametros: {
              creacionUD: 1,
              codSede: dependencia.codSede,
              codDependencia: dependencia.codigo,
              idSolicitante:funcionario.id,
              fechaSolicitud: new Date(),
              archivista: archivista.loginName,
              notifyToRol:ARCHIVISTA,
              notifyToDependencia:dependencia.codigo,
              remitente: {ideFunci:funcionario.id},
              nombreTarea:"Crear unidad Documental",

            }
          });

        })
      );
      this.selectUD.form.reset();
      this.activeTarea = true;

    }));
  }

  toggleEnableButtonNext(section:string){

    this.enableButtonNext = section == "bUnidadDocumental" && !isNullOrUndefined(this.archivarDocumentoModel.UnidadDocumental);

    this.showSolicitarButton = section != "bUnidadDocumental";
  }

  setEnableButtonNext(enable:boolean){
    this.enableButtonNext = !this.showSolicitarButton && enable;
  }


  finalizar(){

     if(!isNullOrUndefined(this.task)) {

       const parameters: any = {creacionUD: 0};

       if (!isNullOrUndefined(this.nroRadicado)) {

         this.subscriptions.push(combineLatest(this._store.select(getAuthenticatedFuncionario)
           , this._asignacionSandbox.obtenerComunicacionPorNroRadicado(this.nroRadicado))
           .subscribe(([funcionario, comunicacion]) => {
             parameters.nombreTarea = this.task.descripcion;
             parameters.nroRadicado = this.nroRadicado;
             parameters.remitente = {ideFunci: funcionario.id, codTipAgent: AGENTE_INTERNO};
             parameters.destinatario = {ideFunci: comunicacion.codFuncRadica, codTipAgent: AGENTE_INTERNO};
             parameters.notifiable = true;

             this.completeTask(parameters);
           })
         );
       }
       else
         this.completeTask(parameters);

       this.subscriptions.push(afterTaskComplete
         .subscribe(() =>  this._store.dispatch(go(['/' + ROUTES_PATH.workspace])) ));
     }


  }

  private completeTask( parameters){

    this._taskSandbox.completeTaskDispatch({
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      idTarea: this.task.idTarea,
      parametros:parameters
    });
  }

  ngOnDestroy(): void {

    // this.afterTaskCompleteSubscriptor.unsubscribe();

    this.subscriptions.forEach( subscrption => subscrption.unsubscribe());
  }

   loadSelectDocumentComponent(component:SeleccionarDocumentosComponent): void {

     if(isNullOrUndefined(this.savedData))
        return;

    const payload = this.savedData;
    component.documentos = payload.documentos;
    component.documentosAdjuntos = payload.documentosAdjuntos;
    component.hasDefaultDocumentForSave =component.documentos.length >0;
  }
}
