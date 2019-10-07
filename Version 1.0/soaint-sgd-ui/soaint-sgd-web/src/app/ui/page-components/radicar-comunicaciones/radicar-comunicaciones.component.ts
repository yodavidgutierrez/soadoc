import {
  AfterContentInit,
  AfterViewInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  OnDestroy,
  OnInit,
  ViewChild,
  ViewEncapsulation
} from '@angular/core';
import {ComunicacionOficialDTO} from 'app/domain/comunicacionOficialDTO';
import {Sandbox as RadicarComunicacionesSandBox} from 'app/infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-sandbox';
import {Sandbox as TaskSandBox} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import * as moment from 'moment';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from '../../../domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State as RootState} from '../../../infrastructure/redux-store/redux-reducers';
import {sedeDestinatarioEntradaSelector, tipoDestinatarioEntradaSelector} from '../../../infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-selectors';
import {getArrayData as DependenciaGrupoSelector} from '../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-selectors';
import {getActiveTask, getNextTask} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {Subscription} from 'rxjs/Subscription';
import {ScheduleNextTaskAction} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-actions';
import {TareaDTO} from '../../../domain/tareaDTO';
import {TaskForm} from '../../../shared/interfaces/task-form.interface';
import {LoadDatosRemitenteAction} from '../../../infrastructure/state-management/constanteDTO-state/constanteDTO-actions';
import {TaskTypes} from '../../../shared/type-cheking-clasess/class-types';
import {getMediosRecepcionVentanillaData} from '../../../infrastructure/state-management/constanteDTO-state/selectors/medios-recepcion-selectors';
import {LoadNextTaskPayload} from '../../../shared/interfaces/start-process-payload,interface';
import {getDestinatarioPrincial} from '../../../infrastructure/state-management/constanteDTO-state/selectors/tipo-destinatario-selectors';
import {RadicarSuccessAction} from '../../../infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-actions';
import 'rxjs/add/operator/skipWhile';
import {ComunicacionOficialEntradaDTV} from '../../../shared/data-transformers/comunicacionOficialEntradaDTV';
import {Sandbox as ComunicacionOficialSandbox} from '../../../infrastructure/state-management/comunicacionOficial-state/comunicacionOficialDTO-sandbox';
import {isNullOrUndefined} from "util";
import {DatosContactoApi} from "../../../infrastructure/api/datos-contacto.api";
import {getTipoPersonaArrayData} from "../../../infrastructure/state-management/constanteDTO-state/selectors/tipo-persona-selectors";
import {
  COMUNICACION_EXTERNA, DESTINATARIO_PRINCIPAL, PERSONA_ANONIMA,
  PERSONA_JURIDICA,
  PERSONA_NATURAL, RADICACION_ENTRADA, ROL_ASIGNADOR, TIPO_AGENTE_DESTINATARIO, TPDOC_NRO_IDENTIFICACION_TRIBUTARIO
} from "../../../shared/bussiness-properties/radicacion-properties";
import {RadicadoDTO} from "../../../domain/radicadoDTO";
import {ConfirmationService} from "primeng/primeng";
import {PushNotificationAction} from "../../../infrastructure/state-management/notifications-state/notifications-actions";
import {combineLatest} from "rxjs/observable/combineLatest";
import {
  getAuthenticatedFuncionario,
 } from "../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {Utils} from "../../../shared/helpers";
import {AgenteApi} from "../../../infrastructure/api/agente.api";



declare const require: any;
const printStyles = require('app/ui/bussiness-components/ticket-radicado/ticket-radicado.component.css');

@Component({
  selector: 'app-radicar-comunicaciones',
  templateUrl: './radicar-comunicaciones.component.html',
  styleUrls: ['./radicar-comunicaciones.component.scss'],
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class RadicarComunicacionesComponent implements OnInit, AfterContentInit, AfterViewInit, OnDestroy, TaskForm {

  type = TaskTypes.TASK_FORM;

  @ViewChild('datosGenerales') datosGenerales;

  @ViewChild('datosRemitente') datosRemitente;

  @ViewChild('datosDestinatario') datosDestinatario;

  @ViewChild('ticketRadicado') ticketRadicado;

  filterTipoComunicacion = ( comunicacion:ConstanteDTO) => { return comunicacion.codigo == COMUNICACION_EXTERNA};

  formStatusIcon = 'assignment';

  radicadoPadre:RadicadoDTO;

  valueRemitente: any;

  valueDestinatario: any;

  valueGeneral: any;

  radicacion: ComunicacionOficialDTO;

  date: Date = new Date();

  barCodeVisible = false;

  editable = true;

  task: TareaDTO;

  printStyle: string = printStyles;

  tabIndex = 0;

  formsTabOrder: Array<any> = [];

  tipoDestinatarioSuggestions$: Observable<ConstanteDTO[]>;
  sedeDestinatarioSuggestions$: Observable<ConstanteDTO[]>;
  dependenciaGrupoSuggestions$: Observable<ConstanteDTO[]>;
  tipoDeRadicacion = RADICACION_ENTRADA;

  mediosRecepcionDefaultSelection$: Observable<any>;
  tipoDestinatarioDefaultSelection$: Observable<ConstanteDTO>;

  // Unsubscribers

  subscriptions:Subscription[] = [];
  reqDigitInmediataUnsubscriber: Subscription;

  zIndex = 1;


  constructor(private _sandbox: RadicarComunicacionesSandBox,
              private _coSandbox: ComunicacionOficialSandbox,
              private _store: Store<RootState>,
              private _taskSandBox: TaskSandBox,
              private  _datosContactoApi:DatosContactoApi,
              private _agentApi: AgenteApi,
              private confirmService:ConfirmationService,
              private _changeDectector:ChangeDetectorRef
              ) {
  }

  ngOnInit() {

    // Default Selection for Children Components bindings
    this.mediosRecepcionDefaultSelection$ = this._store.select(getMediosRecepcionVentanillaData).map(medio => !isNullOrUndefined(medio)? medio.codigo : null);
    this.tipoDestinatarioDefaultSelection$ = this._store.select(getDestinatarioPrincial);



    // Datalist Load bindings
    this.tipoDestinatarioSuggestions$ = this._store.select(tipoDestinatarioEntradaSelector);
    this.sedeDestinatarioSuggestions$ = this._store.select(sedeDestinatarioEntradaSelector);
    this.dependenciaGrupoSuggestions$ = this._store.select(DependenciaGrupoSelector);
    this.subscriptions.push(this._store.select(getActiveTask).subscribe(activeTask => {
      this.task = activeTask;
      this.restore();
    }));

  }

  ngAfterContentInit() {
    this.formsTabOrder.push(this.datosGenerales);
    this.formsTabOrder.push(this.datosRemitente);
    this.formsTabOrder.push(this.datosDestinatario);

  }

  ngAfterViewInit() {

    this._store.dispatch(new LoadDatosRemitenteAction());

    const sedeRemitente = this.datosRemitente.form.get('sedeAdministrativa');
    this.datosRemitente.setTipoComunicacion({codigo:COMUNICACION_EXTERNA});

   this.subscriptions.push(Observable.combineLatest(
      sedeRemitente.statusChanges,
      sedeRemitente.valueChanges
    )
      .filter(([status, value]) => status === 'VALID' || status === 'DISABLED')
      .distinctUntilChanged()
      .subscribe(([status, value]) => {
        if (status === 'VALID') {
          this.datosDestinatario.deleteDestinatarioIqualRemitente(value);
          this._sandbox.dispatchSedeDestinatarioEntradaFilter(value);
        } else if (status === 'DISABLED') {
          this._sandbox.dispatchSedeDestinatarioEntradaFilter(null);
        }
      }));

    this.subscriptions.push(this.datosGenerales.form.valueChanges
      .subscribe(value => {

         if(isNullOrUndefined(value)  || !this.editable)
           return;

        const conditions = [!value.reqDigit && value.adjuntarDocumento];

        if (conditions.some(t => t)) {
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


  destinatarioAddressIsActive(){
    if(this.datosRemitente && this.datosRemitente.datosContactos){
    console.log(this.datosRemitente.datosContactos.isActive)
      if(this.datosRemitente.datosContactos.isActive) return true
    } else return false
  }

  radicarComunicacion(force:boolean = false) {


    this.valueRemitente = this.datosRemitente.form.value;
    this.valueRemitente.ideAgente = this.datosRemitente.ideAgente;
    this.valueDestinatario = this.datosDestinatario.form.value;
    this.valueGeneral = this.datosGenerales.form.value;
    let completeIdAgente$:Observable<any> = Observable.of(this.valueRemitente.ideAgente);

    if(isNullOrUndefined( this.valueRemitente.ideAgente)){

      if(this.valueRemitente.tipoPersona == PERSONA_NATURAL){

        if(!!this.valueRemitente.nroDocumentoIdentidad ){
          completeIdAgente$ = this._agentApi.getAgenteByNroIdentificacion(this.valueRemitente.nroDocumentoIdentidad,PERSONA_NATURAL)
            .map( a => a.ideAgente || null);
        }
      }
      else if(this.valueRemitente.tipoPersona == PERSONA_JURIDICA){

        if(!!this.valueRemitente.nit){
          this._agentApi.getAgenteByNroIdentificacion(this.valueRemitente.nit, PERSONA_JURIDICA)
              .map( a => a.ideAgente || null);
        }
      }

    }
    if(!force && !this.valueGeneral.reqDigit && !this.valueGeneral.adjuntarDocumento)
    {

      this.confirmService.confirm({
        message: "¿Está seguro que no requiere digitalizar o adjuntar documento a este registro?",
        header: "Confirmacion",
        icon : 'fa fa-question-circle',
        accept: _ => { this.radicarComunicacion(true)},
        reject: _ => {}
      });

      return;
    }

    completeIdAgente$.subscribe(idAgente => {


      const radicacionEntradaFormPayload: any = {
        destinatario: this.datosDestinatario.form.value,
        generales: this.datosGenerales.form.value,
        remitente:{... this.valueRemitente, ideAgente : idAgente},
        descripcionAnexos: this.datosGenerales.descripcionAnexos,
        radicadosReferidos: this.datosGenerales.radicadosReferidos,
        agentesDestinatario: this.datosDestinatario.agentesDestinatario,
        task: this.task
      };

      if (this.datosRemitente.datosContactos) {
        radicacionEntradaFormPayload.datosContactos = this.datosRemitente.datosContactos.contacts;

        console.log(radicacionEntradaFormPayload.datosContactos);
      }

      if(this.radicadoPadre)
        radicacionEntradaFormPayload.radicadoPadre = this.radicadoPadre.numeroRadicado.split('-')[1];

      const comunicacionOficialDTV = new ComunicacionOficialEntradaDTV(radicacionEntradaFormPayload, this._store);
      this.radicacion = comunicacionOficialDTV.getComunicacionOficial();

      const dependenciaDestinatarios =  this.datosDestinatario.agentesDestinatario.map( agente => agente.dependenciaGrupo.codigo);

      this._sandbox.radicar(this.radicacion).subscribe((response) => {
          this.barCodeVisible = true;
          this.radicacion = response;
          this.editable = false;
          this.datosGenerales.form.get('fechaRadicacion').setValue(moment(this.radicacion.correspondencia.fecRadicado).format('DD/MM/YYYY hh:mm'));
          this.datosGenerales.form.get('nroRadicado').setValue(this.radicacion.correspondencia.nroRadicado);

          const ticketRadicado = {
            anexos: this.datosGenerales.descripcionAnexos.length,
            folios: this.valueGeneral.numeroFolio,
            noRadicado:Utils.niceRadicado(this.radicacion.correspondencia.nroRadicado) ,
            fecha: this.radicacion.correspondencia.fecRadicado,
            destinatarioSede: this.valueDestinatario.destinatarioPrincipal.sedeAdministrativa.nombre,
            destinatarioGrupo: this.valueDestinatario.destinatarioPrincipal.dependenciaGrupo.codigo,
            abreviatura: this.valueDestinatario.destinatarioPrincipal.dependenciaGrupo.abreviatura
          };
          if (comunicacionOficialDTV.isRemitenteInterno()) {
            ticketRadicado['remitenteSede'] = this.valueRemitente.sedeAdministrativa.nombre;
            ticketRadicado['remitenteGrupo'] = this.valueRemitente.dependenciaGrupo.nombre;
          } else {
            ticketRadicado['remitente'] = this.valueRemitente.tipoPersona == PERSONA_NATURAL ?  this.valueRemitente.nombreApellidos : this.valueRemitente.razonSocial;
          }

          this.ticketRadicado.setDataTicketRadicado(ticketRadicado);
          this.showTicketRadicado();
          this.disableEditionOnForms();

          this._store.dispatch(new RadicarSuccessAction({
            tipoComunicacion: this.valueGeneral.tipoComunicacion,
            numeroRadicado: response.correspondencia.nroRadicado ? response.correspondencia.nroRadicado : null
          }));


          let requiereDigitalizacion = this.valueGeneral.reqDigit ? 1 : 2;
          let adjuntarDocumentos = this.valueGeneral.adjuntarDocumento ? 1 : 2;
          let detalleAnexo = this.valueGeneral.detallarDescripcion && this.datosGenerales.descripcionAnexos.length ? 1: 2;

          const taskPayload:any = {
            idProceso: this.task.idProceso,
            idDespliegue: this.task.idDespliegue,
            idTarea: this.task.idTarea,
            parametros: {
              requiereDigitalizacion: requiereDigitalizacion,
              adjuntarDocumento:adjuntarDocumentos,
              numeroRadicado: Utils.niceRadicado(response.correspondencia.nroRadicado ? response.correspondencia.nroRadicado : null),
              detalleAnexo:detalleAnexo,
            }
          };

          combineLatest( this._store.select(getNextTask),this._store.select(getAuthenticatedFuncionario))
            .subscribe( ([hasNextTask,funcionario]) => {

              if(!hasNextTask){
                taskPayload.parametros.notifyToRol = ROL_ASIGNADOR;
                taskPayload.parametros.notifyToDependenciaList = dependenciaDestinatarios;
                taskPayload.parametros.remitente ={ideFunci : funcionario.id };
                taskPayload.parametros.nroRadicado = response.correspondencia.nroRadicado ? response.correspondencia.nroRadicado : null;
                taskPayload.parametros.nombreTarea = "Asignar Comunicaciones";
              }
              this._taskSandBox.completeTaskDispatch(taskPayload);
            })
            .unsubscribe();

        },
        _ => {
          this.radicacion = null;
        });

    });


  }




  hideTicketRadicado() {
    this.barCodeVisible = false;
  }

  showTicketRadicado() {
    this.barCodeVisible = true;
  }

  disableEditionOnForms() {
    this.editable = false;
    this.datosDestinatario.form.disable();
    this.datosRemitente.form.disable();
    this.datosGenerales.form.disable();
  }

  openNext() {
    this.tabIndex = (this.tabIndex + 1) % 4;
  }

  openPrev() {
    this.tabIndex = (this.tabIndex -1) % 4;
  }

  private changeZIndex(){

    this.zIndex = this.tabIndex == 2  ? -1 : 1;
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


    this._taskSandBox.abortTaskDispatch({
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      instanciaProceso: this.task.idInstanciaProceso
    });
  }

  save():Observable<any> {
    const payload: any = {
      radicadoPadre:this.radicadoPadre,
      destinatario: this.datosDestinatario.form.value,
      generales: this.datosGenerales.form.value,
      remitente: this.datosRemitente.form.value,
      descripcionAnexos: this.datosGenerales.descripcionAnexos,
      radicadosReferidos: this.datosGenerales.radicadosReferidos,
      agentesDestinatario: this.datosDestinatario.agentesDestinatario
    };

    if (this.datosRemitente.datosContactos) {
      payload.datosContactos = this.datosRemitente.datosContactos.contacts;
      // payload.contactInProgress = this.datosRemitente.datosContactos.form.value
    }


    const tareaDto: any = {
      idTareaProceso: this.task.idTarea,
      idInstanciaProceso: this.task.idInstanciaProceso,
      payload:JSON.stringify(payload)
    };

     return this._sandbox.quickSave(tareaDto);
  }

  restore() {
    if (this.task) {

      this._sandbox.quickRestore(this.task.idInstanciaProceso, this.task.idTarea).take(1).subscribe(response => {

        if(isNullOrUndefined(response.payload))
           return;
        const results = JSON.parse(response.payload);
        if (!results) {
          return;
        }

        this.radicadoPadre = results.radicadoPadre;

        // generales
        if(!isNullOrUndefined(results.generales))
        this.datosGenerales.form.patchValue(results.generales);
        this.datosGenerales.descripcionAnexos = results.descripcionAnexos || [];
        this.datosGenerales.radicadosReferidos = results.radicadosReferidos || [];

        // remitente
        if(!isNullOrUndefined(results.remitente))
        this.datosRemitente.form.patchValue(results.remitente);

        // destinatario
        if(!isNullOrUndefined(results.destinatario))
        this.datosDestinatario.form.patchValue(results.destinatario);
        this.datosDestinatario.agentesDestinatario = results.agentesDestinatario || [];

        if (results.datosContactos) {
          const retry = setInterval(() => {
            if (typeof this.datosRemitente.datosContactos !== 'undefined') {
              this.datosRemitente.datosContactos.contacts = [...results.datosContactos];
              clearInterval(retry);
            }
          }, 400);
        }

        // if (results.contactInProgress) {
        //   const retry = setInterval(() => {
        //     if (typeof this.datosRemitente.datosContactos !== 'undefined') {
        //       this.datosRemitente.datosContactos.form.patchValue(results.contactInProgress);
        //       clearInterval(retry);
        //     }
        //   }, 400)
        // }

      });
    }
  }

  testEditRadicado() {
    this._coSandbox.loadData({});
  }

  ngOnDestroy() {

    this.subscriptions.forEach( s => s.unsubscribe());

    setTimeout(() => {
      this._store.dispatch(new ScheduleNextTaskAction(null));
    },500);
  }


  asociarRadicado(radicado:any){

      this.radicadoPadre = radicado;

    this.datosRemitente.tipoPersonaSuggestions$ = this._store.select(getTipoPersonaArrayData).map(tiposPersona => tiposPersona.filter( tp => tp.codigo!= PERSONA_ANONIMA));

      this._store.dispatch(new PushNotificationAction({severity:'success',summary:'Se ha asociado el radicado padre'}));

      this._datosContactoApi.getDatosContatoByIdAgente(radicado.ideAgente)
          .subscribe(datosContactos => {

            this.datosRemitente.ideAgente  = radicado.ideAgente;

            this.datosRemitente.form.get('tipoPersona').setValue(radicado.codTipoPers);

               if(radicado.codTipoPers == PERSONA_NATURAL)
               this.datosRemitente.form.get('tipoDocumento').setValue(radicado.tipoDocumento);

             if(radicado.codTipoPers == PERSONA_JURIDICA)
               this.datosRemitente.form.get('actuaCalidad').setValue(radicado.codigoCalidad);

              this.datosRemitente.form.get('nit').setValue(radicado.NIT);
              this.datosRemitente.form.get('razonSocial').setValue(radicado.razonSocial);
              this.datosRemitente.form.get('nombreApellidos').setValue(radicado.nombre);
              this.datosRemitente.form.get('nroDocumentoIdentidad').setValue(radicado.numeroIdentificacion);

              this.datosRemitente.form.updateValueAndValidity();

              this._changeDectector.detectChanges();

              const datosContactAdapted=  datosContactos.map(c => {

                const contactAdpated:any = {};

                 Object.keys(c).forEach( key => {
                   switch (key){
                     case 'corrElectronico': contactAdpated.correoEle = c[key];
                       break;
                     case 'codMunicipio': contactAdpated.municipio = {codigo:c[key]};
                       break;
                     case 'codDepartamento': contactAdpated.departamento = {codigo:c[key]};
                       break;
                     case 'codPais': contactAdpated.pais = {codigo:c[key]};
                       break;
                     case 'tipoContacto':  contactAdpated.tipoContacto = {codigo: c[key]};
                       break;
                     case 'telFijo' : contactAdpated.numeroTel = c[key];
                       break;
                     default : contactAdpated[key] = c[key];
                       break;
                   }
                 });

                return contactAdpated;

              });

              if(!isNullOrUndefined(this.datosRemitente.datosContactos)) {
                this.datosRemitente.datosContactos.contacts = datosContactAdapted;
                this.datosRemitente.datosContactos.CompletarDatosContacto();
              }
              else
              this.datosRemitente.contacts = datosContactAdapted;

             });
  }

  enableRadicacion(){

       const conditions : boolean[] = [
         this.datosGenerales.form.valid,
         this.datosRemitente.form.valid,
         this.datosDestinatario.agentesDestinatario.some(agente => agente.tipoDestinatario.codigo == DESTINATARIO_PRINCIPAL)
       ];

      return  conditions.every(c => c);
  }


}
