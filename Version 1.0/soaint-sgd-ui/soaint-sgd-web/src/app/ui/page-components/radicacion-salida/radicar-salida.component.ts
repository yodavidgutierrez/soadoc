import {
  AfterContentInit, AfterViewInit, ChangeDetectorRef, Component, EventEmitter, OnDestroy, OnInit, Output,
  ViewChild
} from '@angular/core';
import {ComunicacionOficialDTO} from 'app/domain/comunicacionOficialDTO';
import {Sandbox as TaskSandBox} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import {Observable} from 'rxjs/Observable';
import {Store} from '@ngrx/store';
import {State as RootState} from '../../../infrastructure/redux-store/redux-reducers';
import {getActiveTask} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {Subscription} from 'rxjs/Subscription';
import {TareaDTO} from '../../../domain/tareaDTO';
import {TaskForm} from '../../../shared/interfaces/task-form.interface';
import {TaskTypes} from '../../../shared/type-cheking-clasess/class-types';
import 'rxjs/add/operator/skipWhile';
import {
    getSelectedDependencyGroupFuncionario
} from 'app/infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {ViewFilterHook} from "../../../shared/ViewHooksHelper";
import {RadicacionSalidaDTV} from "../../../shared/data-transformers/radicacionSalidaDTV";
import {AbstractControl, Validators} from "@angular/forms";
import {
  COMUNICACION_EXTERNA,
  DESTINATARIO_EXTERNO,
  DESTINATARIO_INTERNO,
  PERSONA_JURIDICA,
  PERSONA_NATURAL,
  RADICACION_DOC_PRODUCIDO,
  RADICACION_SALIDA
} from "../../../shared/bussiness-properties/radicacion-properties";
import * as moment from "moment";
import {RadicarSuccessAction} from "app/infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-actions";
import {RsTicketRadicado} from "./components/rs-ticket-radicado/rs-ticket-radicado.component";
import {RadicacionSalidaService} from "../../../infrastructure/api/radicacion-salida.service";
import {DependenciaDTO} from "../../../domain/dependenciaDTO";
import {LoadNextTaskPayload} from "../../../shared/interfaces/start-process-payload,interface";
import {ScheduleNextTaskAction} from "../../../infrastructure/state-management/tareasDTO-state/tareasDTO-actions";
import {PushNotificationAction} from "../../../infrastructure/state-management/notifications-state/notifications-actions";
import {isNullOrUndefined} from "util";
import {DomToImageService} from "../../../infrastructure/api/dom-to-image";
import {getMediosRecepcionVentanillaData} from "../../../infrastructure/state-management/constanteDTO-state/selectors/medios-recepcion-selectors";
import {RadicadoDTO} from "../../../domain/radicadoDTO";
import {ConstanteDTO} from "../../../domain/constanteDTO";
import {ConfirmationService} from "primeng/primeng";
import {environment} from "../../../../environments/environment";
import {Utils} from "../../../shared/helpers";
import {go} from "@ngrx/router-store";
import {ROUTES_PATH} from "../../../app.route-names";
import {UnidadDocumentalApiService} from "../../../infrastructure/api/unidad-documental.api";
import {Guid} from "../../../infrastructure/utils/guid-generator";
import {ApiConsultaDocumentos} from "../../../infrastructure/api/api-consulta-documentos";
declare const require: any;
const printStyles = require('app/ui/page-components/radicacion-salida/components/rs-ticket-radicado/rs-ticket-radicado.component.css');

@Component({
  selector: 'app-radicar-salida',
  templateUrl: './radicar-salida.component.html',
  styleUrls: ['./radicar-salida.component.css']
})
export class RadicarSalidaComponent implements OnInit, AfterContentInit, AfterViewInit, OnDestroy, TaskForm {

  type = TaskTypes.TASK_FORM;

  tabIndex = 0;
  editable = true;
  printStyle: string = printStyles;

  @ViewChild('datosGenerales') datosGenerales;
  @ViewChild('datosContacto') datosContacto;
  @ViewChild('ticketRadicado') ticketRadicado;
  @ViewChild('datosRemitente') datosRemitente;
  @ViewChild('datosEnvio') datosEnvio;

  private retry;

  radicadoPadre:RadicadoDTO;

  task: TareaDTO;
  taskFilter?:string;
  radicacion: ComunicacionOficialDTO;
  barCodeVisible = false;

  formsTabOrder: Array<any> = [];
  debeReintecntar: Observable<any>;
  subscriptions:Subscription[] = [];
  dependencySelected?:DependenciaDTO;
  printButtonEnabled:boolean = false;
  dataReintentar: any;
  responseReintentar: any;
  showReintentarF: boolean;
  ticketRad:any;
  firmas:any;
  binaryString:string;

  mediosRecepcionDefaultSelection$: Observable<any>;

   protected tipoRadicacion = RADICACION_SALIDA;

   protected filterTipoComunicacion = (tipoComunicacion:ConstanteDTO) => tipoComunicacion.codigo == 'SE';

   @Output() onFinalizar: EventEmitter<any> = new EventEmitter;

  constructor(
    protected _store: Store<RootState>
    ,protected _changeDetectorRef: ChangeDetectorRef
    ,protected _sandbox:RadicacionSalidaService
    ,protected _taskSandbox:TaskSandBox
   ,protected  _domToImage:DomToImageService
    ,protected confirmService:ConfirmationService
  ) {

  }

  ngOnInit() {

    this.subscriptions.push(this._store.select(getActiveTask).subscribe(activeTask => {
      this.task = activeTask;

      if(this.task !== null){

        console.log(this.task);

        this.taskFilter = this.task.nombre+'-datos-contactos-show-form';

        ViewFilterHook.addFilter(this.taskFilter,() => false);
      }
      this.restore();


    }));

    this.subscriptions.push(  this._store.select(getSelectedDependencyGroupFuncionario).subscribe( dependency => {

      this.dependencySelected = dependency;
    }));


    this.mediosRecepcionDefaultSelection$ = this._store.select(getMediosRecepcionVentanillaData).map( medio => medio.codigo);

   this._changeDetectorRef.detectChanges();
  }



  ngAfterContentInit() {
   this.formsTabOrder.push(this.datosGenerales);
   this.formsTabOrder.push(this.datosContacto);
    console.log('AFTER VIEW INIT...');
  }

  ngAfterViewInit() {
    console.log('AFTER VIEW INIT...');

       this.subscriptions.push(this.datosGenerales.form.valueChanges
        .subscribe(value => {
          console.log(value);
          // Habilitando o desabilitando la tarea que se ejecutar� secuencialmente a la actual
          if (!value.reqDigit && value.adjuntarDocumento) {
            const payload: LoadNextTaskPayload = {
              idProceso: this.task.idProceso,
              idInstanciaProceso: this.task.idInstanciaProceso,
              idDespliegue: this.task.idDespliegue
            };
            this._store.dispatch(new ScheduleNextTaskAction(payload));
          } else {
            this._store.dispatch(new ScheduleNextTaskAction(null));
          }
        }));

  }

  radicarSalida(force:boolean = false) {

   const valueGeneral = this.datosGenerales.form.value;

    if(!force && !valueGeneral.reqDigit && !valueGeneral.adjuntarDocumento && this.tipoRadicacion == RADICACION_SALIDA)
    {

      this.confirmService.confirm({
        message: "¿Está seguro que no requiere digitalizar o adjuntar documento a este registro?",
        header: "Confirmacion",
        icon : 'fa fa-question-circle',
        accept: _ => { this.radicarSalida(true)},
        reject: _ => {}
      });

      return;
    }

    const radicacionEntradaFormPayload: any =  this.buildPayload();

    if(this.radicadoPadre)
      radicacionEntradaFormPayload.radicadoPadre = this.radicadoPadre.numeroRadicado.split('-')[1];

    const comunicacionOficialDTV = new RadicacionSalidaDTV(radicacionEntradaFormPayload, this._store);

    const  radicacion = comunicacionOficialDTV.getComunicacionOficial();

    if(comunicacionOficialDTV.hasError){

      this.hideTicketRadicado();

      const message = (<any>comunicacionOficialDTV.hasError).error;

      this._store.dispatch(new PushNotificationAction({severity: 'error', summary: `${message} Revise porfavor!`}));

      return false;
    }

    this.radicacion = radicacion;

    this.callRadicarSalida(this.radicacion).subscribe((response) => {

      this.barCodeVisible = true;
      this.radicacion = response;
      this.radicacion.ppdDocumentoList= [{
        ideEcm:comunicacionOficialDTV.getDocumento().ideEcm,
        asunto:"",
        nroFolios:0,
        nroAnexos:0

      }];

      this.dataReintentar = comunicacionOficialDTV.getDocumento().ideEcm;

      this.editable = false;
      this.datosGenerales.form.get('fechaRadicacion').setValue(this.radicacion.correspondencia.fecRadicado);
      this.datosGenerales.form.get('nroRadicado').setValue(this.radicacion.correspondencia.nroRadicado);

      const valueGeneral = this.datosGenerales.form.value;
      let predicate = (destinatario) => destinatario.tipoDestinatario.nombre == "Principal";
      let destinatarioPrincipal = this.datosContacto.listaDestinatariosInternos.find(predicate);
      if(destinatarioPrincipal === undefined){
        destinatarioPrincipal = this.datosContacto.listaDestinatariosExternos.find(predicate);
        if(destinatarioPrincipal !== undefined) {
          this.ticketRad = this.createTicketDestExterno(destinatarioPrincipal);
          this.ticketRadicado.setDataTicketRadicado(this.ticketRad);
        }
      }
      else{

        this.ticketRadicado.setDataTicketRadicado(this.createTicketDestInterno(destinatarioPrincipal));
      }

      this._changeDetectorRef.detectChanges();
      const self = this;
      if(this.mustSendImage(valueGeneral))
      setTimeout( () =>{ self.uploadTemplate(
        self.radicacion.correspondencia.codDependencia,
        self.radicacion.correspondencia.nroRadicado,
        comunicacionOficialDTV.getDocumento().ideEcm
      )},1000);

      else{
       this.removeBorders();
      }
      this.disableEditionOnForms();

      this._store.dispatch(new RadicarSuccessAction({
        tipoComunicacion: valueGeneral.tipoComunicacion,
        numeroRadicado: response.correspondencia.nroRadicado ? response.correspondencia.nroRadicado : null
      }));
      if(this.task.nombre == 'Radicar COF Salida'){
           this._taskSandbox.completeTaskDispatch({
          idProceso: this.task.idProceso,
          idDespliegue: this.task.idDespliegue,
          idTarea: this.task.idTarea,
          parametros: this.buildTaskCompleteParameters(valueGeneral,response.correspondencia.nroRadicado ? response.correspondencia.nroRadicado : null)

        });
      }

    },() => {

      this.radicacion = null;
      this.hideTicketRadicado();
      this._changeDetectorRef.detectChanges();
    });

      }

  protected  callRadicarSalida(radicacion): Observable<any>{

    return this._sandbox.radicar(this.radicacion)
  }

  protected mustSendImage(general:any):boolean{
   // return  general.adjuntarDocumento
    return false;
  }

  private removeBorders(){

    const element:any =  document.querySelector('#ticket-rad  .ticket-content');
    element.style.border = '0';
    element.style.padding = '0';
    element.style.margin = '0';

    this.printButtonEnabled = true;
    this._changeDetectorRef.detectChanges();
  }

  protected uploadTemplate(codDependencia,nroRadicado,ideEcm){

      const node = document.getElementById("ticket-rad");

      if(!isNullOrUndefined(node))
        this._domToImage.convertToBlob(node).then(blob => {

          let formData = new FormData();
          formData.append("documento", blob, "etiqueta.png");
          if (!isNullOrUndefined(ideEcm))
            formData.append("idDocumento", ideEcm);
          formData.append("nroRadicado", nroRadicado);
          formData.append("codigoDependencia", codDependencia);
          this.processFormData(formData);



          if (isNullOrUndefined(this.responseReintentar)) {
            formData.append("reintentar", "false");
          }else {
            formData.append("reintentar", "true");
          }

          this.firmarDocumento(formData,nroRadicado);


          /*this._sandbox.uploadTemplate(formData).subscribe(response =>this.proccessImage(response),() => {
            this._store.dispatch(new PushNotificationAction({severity: 'error', summary: 'Etiqueta no subida!'}));
          });*/

          this.removeBorders();
        });



  }

  mensajeFirmaDigital(dato:boolean){

    if(dato == true){
      this._store.dispatch(new PushNotificationAction({severity:'error',summary:'No se pudo radicar el documento, vuelva a intentarlo, y/o consulte con el administrador'}));
    }else{
      this._store.dispatch(new PushNotificationAction({severity:'success',summary:'El documento fue radicado satisfactoriamente'}));
    }

  }

  protected  processFormData(formData: FormData){

  }

  protected proccessImage(response){

  }

  protected  buildPayload(): any{

    return {
      generales: this.datosGenerales.form.value,
      descripcionAnexos: this.datosGenerales.descripcionAnexos,
      radicadosReferidos: this.datosGenerales.radicadosReferidos,
      task: this.task,
      destinatarioInterno:this.datosContacto.listaDestinatariosInternos,
      destinatarioExt:this.datosContacto.listaDestinatariosExternos,
      remitente:this.datosRemitente.form.value,
      datosEnvio:this.datosEnvio !== undefined ? this.datosEnvio.form.value: undefined
    };
  }

  protected buildTaskCompleteParameters(generales:any,noRadicado:any):any{

    return {
      codDependencia:this.dependencySelected.codigo,
      codDependencia1:this.datosRemitente.form.value.dependenciaGrupo.codigo,
      numeroRadicado:noRadicado,
      usuarioProyectorRadicador: this.datosRemitente.form.value.funcionarioGrupo.loginName,
      requiereDigitalizacion:generales.reqDigit? 1: 2,
      requiereDistribucionDemanda:generales.reqDistFisica ? 1: 2,
      adjuntarDocumento:generales.adjuntarDocumento? 1: 2,
      requiereDistribucion:generales.reqDistElect? 1 : 2
    }
  }

  private createTicketDestInterno(destinatario:any):RsTicketRadicado{

    const valueGeneral = this.datosGenerales.form.value;
    const valueRemitente = this.datosRemitente.form.value;

     return new RsTicketRadicado(
       valueRemitente.dependenciaGrupo.abreviatura,
      DESTINATARIO_INTERNO,
      this.datosGenerales.descripcionAnexos.length.toString(),
       valueGeneral.numeroFolio || '',
      this.radicacion.correspondencia.nroRadicado.toString(),
      this.radicacion.correspondencia.fecRadicado.toString(),
      valueRemitente.funcionarioGrupo.id,
      valueRemitente.sedeAdministrativa,
      valueRemitente.dependenciaGrupo.codigo,
      destinatario.dependencia.nombre ,
      destinatario.sede.nombre,
      destinatario.dependencia.codigo
     );
  }

  private createTicketDestExterno(destinatario): RsTicketRadicado{

    const valueGeneral = this.datosGenerales.form.value;
    const valueRemitente = this.datosRemitente.form.value;


    return new RsTicketRadicado(
      valueRemitente.dependenciaGrupo.abreviatura,
      DESTINATARIO_EXTERNO,
     this.datosGenerales.descripcionAnexos.length.toString(),
      valueGeneral.numeroFolio || '',
     this.radicacion.correspondencia.nroRadicado.toString(),
     this.radicacion.correspondencia.fecRadicado.toString(),
      valueRemitente.funcionarioGrupo.id,
      valueRemitente.sedeAdministrativa,
      valueRemitente.dependenciaGrupo.codigo,
      destinatario.tipoPersona.codigo  == PERSONA_NATURAL ?  destinatario.nombre : destinatario.razonSocial
  );
  }


  hideTicketRadicado() {
    this.barCodeVisible = false;
  }

  showTicketRadicado() {
    this.barCodeVisible = true;
  }

  disableEditionOnForms() {
    this.editable = false;
    this.datosGenerales.form.disable();
  }

  openNext() {
    this.tabIndex =  (this.tabIndex + 1)%4;
  }

  openPrev() {
    this.tabIndex = (this.tabIndex +3) % 4;
  }

  updateTabIndex(event) {
    this.tabIndex = event.index;
  }

  getTask(): TareaDTO {
    return this.task;
  }

  abort(force = false) {

    if(!force){

      this.confirmService.confirm({
        message: "¿Está seguro que desea cancelar el flujo de trabajo?",
        header: "Confirmacion",
        icon : 'fa fa-question-circle',
        accept: _ => { this.abort(true)},
        reject: _ => {}
      });

      return;

    }


    this.dropSubscriptions();
    console.log('ABORT...');
    this._taskSandbox.abortTaskDispatch({
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      instanciaProceso: this.task.idInstanciaProceso
    });
  }

  protected buildTaskDataForSave(){

    const payload: any = {
      radicadoPadre:this.radicadoPadre,
      generales: this.datosGenerales.form.value,
      remitente: this.datosRemitente.form.value,
      descripcionAnexos: this.datosGenerales.descripcionAnexos,
      radicadosReferidos: this.datosGenerales.radicadosReferidos,
      destinatariosInternos:this.datosContacto.listaDestinatariosInternos,
      destinatariosExternos:this.datosContacto.listaDestinatariosExternos,
      firmas:this.firmas,
      showReintentarF:this.showReintentarF,
      radicacion:this.radicacion,
      dataReintentar:this.dataReintentar,
      responseReintentar:this.responseReintentar,
      ticketRad:this.ticketRad,
      binaryString:this.binaryString,
    };

    if (this.datosRemitente.datosContactos) {
      payload.datosContactos = this.datosRemitente.datosContactos.contacts;
      // payload.contactInProgress = this.datosRemitente.datosContactos.form.value
    }

    if(!isNullOrUndefined(this.datosEnvio))
      payload.datosEnvio = this.datosEnvio.form.value;

    return payload;

  }

  save(): Observable<any> {
    const payload: any = this.buildTaskDataForSave();
    const tareaDto: any = {
      idTareaProceso: this.task.idTarea,
      idInstanciaProceso: this.task.idInstanciaProceso,
      payload: JSON.stringify(payload)
    };

    return this._sandbox.quickSave(tareaDto);
  }

  saveTask(){

  this.subscriptions.push( this.save().subscribe());
  }

  restore() {
    console.log('RESTORE...');
    if (this.task) {

      this.subscriptions.push(this._sandbox.quickRestore(this.task.idInstanciaProceso, this.task.idTarea).take(1).subscribe(response => {

        if(isNullOrUndefined(response.payload))
          return;
        const results = JSON.parse(response.payload);
        if (!results) {
          return;
        }
        this.restoreByPayload(results);

      }));

    }
  }

  protected restoreByPayload(results){

    console.log(results);
    if(!isNullOrUndefined(results.generales))
      this.datosGenerales.form.patchValue(results.generales);
    this.datosGenerales.descripcionAnexos = results.descripcionAnexos;
    this.datosGenerales.radicadosReferidos = results.radicadosReferidos;

    this.firmas = results.firmas;
    this.showReintentarF = results.showReintentarF;
    this.radicacion = results.radicacion;
    this.dataReintentar = results.dataReintentar;
    this.responseReintentar = results.responseReintentar;
    this.ticketRad = results.ticketRad;



    this.datosGenerales.form.get('fechaRadicacion').setValue(this.radicacion.correspondencia.fecRadicado);
    this.datosGenerales.form.get('nroRadicado').setValue(this.radicacion.correspondencia.nroRadicado);

    this.ticketRadicado.setDataTicketRadicado(this.ticketRad);
    this.proccessImage(this.responseReintentar);
    this._changeDetectorRef.detectChanges();
    this.removeBorders();

    // remitente
    if(!isNullOrUndefined(results.remitente))
      this.datosRemitente.form.patchValue(results.remitente);

    // destinatario
    if(!isNullOrUndefined(results.destinatariosExternos))
      this.datosContacto.listaDestinatariosExternos = results.destinatariosExternos;
    if(!isNullOrUndefined(results.destinatariosInternos))
      this.datosContacto.listaDestinatariosInternos= results.destinatariosInternos;

    if(!isNullOrUndefined(results.datosEnvio))
      this.datosEnvio.form.patchValue(results.datosEnvio);

    if(!isNullOrUndefined(results.radicadoPadre))
       this.radicadoPadre = results.radicadoPadre;


       this.retry = setInterval(() => {
        if (results.datosContactos) {
          if (typeof this.datosRemitente.datosContactos !== 'undefined') {
            this.datosRemitente.datosContactos.contacts = [...results.datosContactos];
            clearInterval(this.retry);
          }
        }

        if(this.showReintentarF)
           this.disableEditionOnForms();

        this._changeDetectorRef.detectChanges();
      }, 400);
  }

  ngOnDestroy() {
    console.log('ON DESTROY...');

    ViewFilterHook.removeFilter(this.taskFilter);

     this.dropSubscriptions();

     setTimeout(() => {
       this._store.dispatch(new ScheduleNextTaskAction(null));
     }, 500);

  }

  radicacionButtonIsShown():boolean{

      const conditions:boolean[] = [
      this.datosGenerales.form.valid,
      this.datosRemitente.form.valid,
      !this.datosGenerales.form.get("reqDistFisica").value || ( this.datosEnvio !== undefined && this.datosEnvio.form.valid),
      this.datosContacto.listaDestinatariosExternos.length + this.datosContacto.listaDestinatariosInternos.length > 0,
      !isNullOrUndefined(this.task)
    ];

    return  conditions.every( condition => condition) ;
  }


  dropSubscriptions(){
    clearTimeout(this.retry);
    this.subscriptions.forEach(s => s.unsubscribe());
  }

  asociarRadicado(radicado:any){

    this.radicadoPadre = radicado;

    this._store.dispatch(new PushNotificationAction({severity:'success',summary:'Se ha asociado el radicado padre'}));

  }

  reintentarFirmaDigital(){

   const formData = new FormData();

    if (!isNullOrUndefined(this.dataReintentar))
      formData.append("idDocumento", this.dataReintentar);
    formData.append("nroRadicado",  this.radicacion.correspondencia.nroRadicado);
    formData.append("codigoDependencia",  this.radicacion.correspondencia.codDependencia);
    formData.append("reintentar", "true")
    this.processFormData(formData);

    this.firmarDocumento(formData, this.radicacion.correspondencia.nroRadicado);

  }

 protected firmarDocumento(formData: FormData,nroRadicado){

    this._sandbox.uploadTemplate(formData).subscribe( res=>{

      this.proccessImage(res);
      console.log(res);
      this.responseReintentar = res;
      this.mensajeFirmaDigital(this.showReintentarF);
      this._taskSandbox.completeTaskDispatch({
        idProceso: this.task.idProceso,
        idDespliegue: this.task.idDespliegue,
        idTarea: this.task.idTarea,
        parametros: this.buildTaskCompleteParameters(this.datosGenerales.form.value, nroRadicado ? nroRadicado : null)

      });
      /*
      if (this.responseReintentar.codMensaje == "0000"){
        console.log("Firmado");
        this.showReintentarF = false;
        this.mensajeFirmaDigital(this.showReintentarF);
        this._taskSandbox.completeTaskDispatch({
          idProceso: this.task.idProceso,
          idDespliegue: this.task.idDespliegue,
          idTarea: this.task.idTarea,
          parametros: this.buildTaskCompleteParameters(this.datosGenerales.form.value, nroRadicado ? nroRadicado : null)

        });
      }else if (this.responseReintentar.codMensaje == "4004") {
        console.log("No firmado");

        this.mensajeFirmaDigital(true);

        if(this.showReintentarF)
           return;

        this.showReintentarF = true;

        this.saveTask();

      } else {
        this._store.dispatch(new PushNotificationAction({severity:'error',summary:res.mensaje}));
      }*/


    },() => {
      this._store.dispatch(new PushNotificationAction({severity: 'error', summary: 'Etiqueta no subida!'}));
    });
  }



  navigateBack() {
    this.dropSubscriptions();

    this.onFinalizar.emit();

    this._store.dispatch(go(['/' + ROUTES_PATH.workspace]));
  }

}
