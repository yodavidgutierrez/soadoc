import { Component, OnInit, ChangeDetectorRef, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable } from 'rxjs/Observable';
import { Subscription } from 'rxjs/Subscription';
import { TaskForm } from '../../../shared/interfaces/task-form.interface';
import { TareaDTO } from '../../../domain/tareaDTO';
import { VariablesTareaDTO } from '../produccion-documental/models/StatusDTO';
import { StateUnidadDocumentalService } from 'app/infrastructure/service-state-management/state.unidad.documental';
import { Store } from '@ngrx/store';
import { State } from 'app/infrastructure/redux-store/redux-reducers';
import { Sandbox } from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import { Sandbox as TaskSandBox } from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import { getActiveTask } from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {VALIDATION_MESSAGES} from 'app/shared/validation-messages';
import { isNullOrUndefined } from 'util';
import { UnidadDocumentalAccion } from 'app/ui/page-components/unidades-documentales/models/enums/unidad.documental.accion.enum';
import { UnidadDocumentalDTO } from '../../../domain/unidadDocumentalDTO';
import {ActivatedRoute} from '@angular/router';
import { megaf } from 'environments/environment';
import { PushNotificationAction } from '../../../infrastructure/state-management/notifications-state/notifications-actions';
import {
  getAuthenticatedFuncionario,
  getSelectedDependencyGroupFuncionario
} from "../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {go} from "@ngrx/router-store";
import {ROUTES_PATH} from "../../../app.route-names";
import {afterTaskComplete} from "../../../infrastructure/state-management/tareasDTO-state/tareasDTO-reducers";
import {droolsPayload} from "../../../shared/drools-config-properties/drools-properties";
import {ARCHIVISTA_AC} from "../../../app.roles";
import {combineLatest} from "rxjs/observable/combineLatest";
import {AbortTaskAction} from "../../../infrastructure/state-management/tareasDTO-state/tareasDTO-actions";
import {InstrumentoApi} from "../../../infrastructure/api/instrumento.api";
import {UnidadConservacionDTO} from "../../../domain/UnidadConservacionDTO";

@Component({
  selector: 'app-transferencias-documentales',
  templateUrl: './transferencias-documentales.component.html',
})
export class TransferenciasDocumentalesComponent implements TaskForm, OnInit, OnDestroy {

  // contiene:
  // formulario, configuración y validación
  // operaciones sobre unidades documentales como: abrir, cerrar, reactivar, aprobar, rechazar
  State: StateUnidadDocumentalService;

  // tarea
  task: TareaDTO;
  taskVariables: VariablesTareaDTO = {};
  status: number = null;
  // form
  formTransferencia: FormGroup;
  validations: any = {};
  subscribers: Array<Subscription> = [];
  isUbicar  = false;
  tooltipJerarquica: string;
  tooltipProductora: string;

  // dialog
  abrirNotas = false;
  indexUnidadSeleccionada: number = null;
  unidadDocumetalUbicar: any = null;

  selectedIndex: number = null;


  constructor(
    private state: StateUnidadDocumentalService,
    private _store: Store<State>,
    private _taskSandBox: TaskSandBox,
    private _instrumentoApi:InstrumentoApi,
    private _detectChanges: ChangeDetectorRef,
    private fb: FormBuilder,
    private route: ActivatedRoute,

  ) {
    this.State = this.state;
    this.route.params.subscribe( params => {
      this.status = parseInt(params.status, 10) || 0;
    } );
  }


get fechaAprobacion(){ return this.formTransferencia.get('fechaAprobacion').value; }

  ngOnInit() {
    this.State.ListadoUnidadDocumental = [];
    this.InitForm();
    this.SetListadoSubscriptions(); // solucion para el problema de actualización del componente datatable de primeng
    this.InitPropiedadesTarea();

    this.subscribers.push(this._store.select(getAuthenticatedFuncionario).subscribe( funcionario => {

      this.formTransferencia.get('funcionarioResponsable')
        .setValue(`${funcionario.nombre} ${funcionario.valApellido1 ? funcionario.valApellido1: ''} ${funcionario.valApellido2 ? funcionario.valApellido2: '' }`)

      this.formTransferencia.get('cargoFuncionario').setValue(funcionario.cargo)
    }));

    this.subscribers.push(afterTaskComplete.subscribe(_ => {

      this._store.dispatch(go(['/' + ROUTES_PATH.workspace]));

    }));

    if(this.status == 1)
    this._store.select(getSelectedDependencyGroupFuncionario).subscribe( dependencia => {
      this.formTransferencia.get('seccion').setValue(dependencia.nomSede);
      this.formTransferencia.get('subseccion').setValue(dependencia.nombre);
      this.tooltipJerarquica = dependencia.nomSede;
      this.tooltipProductora = dependencia.nombre;
    });
  }

  InitForm() {

     let formCotrols:any = {
       fondo: ['SOAINT'],
       subfondo: ['NIVEL CENTRAL'],
       seccion: ['xxxxxxx'],
       tipoTransferencia: [''],
       funcionarioResponsable: [''],
       fechaAprobacion: [''],
       subseccion: ['xxxxxxx'],
       nroTransferencia: ['xxxxxxx'],
       cargoFuncionario: [''],
     };
     if(this.status == 2){

       formCotrols = Object.assign(formCotrols,{
         fisica: [false],
         electronica:[false],
        dependencia:['']
       })
     }

     if(this.status == 4){
       formCotrols.entidadRecibe = [''];
     }
    this.formTransferencia = this.fb.group(formCotrols);

     if(this.status == 2 ){

       this.formTransferencia.get("fisica").valueChanges.subscribe(value =>{

         const dependenciaControl =  this.formTransferencia.get('dependencia');

        dependenciaControl.clearValidators();

         if(value){
           dependenciaControl.setValidators(Validators.required);
         }

         dependenciaControl.updateValueAndValidity();
       })
     }
 }

 InitPropiedadesTarea() {

    this.subscribers.push(this._store.select(getActiveTask).subscribe((activeTask) => {
      this.task = activeTask;

      if(!isNullOrUndefined(activeTask.variables && activeTask.variables.nroTransferencia) )
         this.formTransferencia.get('nroTransferencia').setValue(activeTask.variables.nroTransferencia);
      this.route.params.subscribe( params => {
        this.status = parseInt(params.status, 10) || 1;

        const tipoTransferencia = this.status == 4 ? 'secundaria' : 'primaria';

        const payload:any = {};
        this.formTransferencia.get('tipoTransferencia').setValue(this.status < 4 ? 'Transferencia Primaria' : 'Transferencia Secundaria');

        if(!isNullOrUndefined(activeTask.variables && activeTask.variables.dependencia) || !isNullOrUndefined(activeTask.variables && activeTask.variables.dependecia)){

          const codDependencia = activeTask.variables.dependencia || activeTask.variables.dependecia;
          this._instrumentoApi.obtenerDependenciaPorCodigo(codDependencia).subscribe( dependencia => {
            this.formTransferencia.get('seccion').setValue(dependencia.nomSede);
            this.formTransferencia.get('subseccion').setValue(dependencia.nombre);
          })
        }



        switch (this.status){
          case 1:   payload.depCode = activeTask.variables.codDependencia;
                    payload.tipoTransferencia = tipoTransferencia;
                    this.State.Listar(payload);
               break;
          case 4  : payload.depCode = activeTask.variables.dependencia;
                    payload.tipoTransferencia = tipoTransferencia;
                    this.State.Listar(payload);
            break;
          case 2 : payload.numTransferencia =  activeTask.variables.nroTransferencia;
                   payload.depCode =  activeTask.variables.dependecia;
            this.state.ListarUnidadesDocumentalesVerificar(payload);
            break;
          case 3 :  payload.numTransferencia =  activeTask.variables.nroTransferencia;
                    payload.depCode =  activeTask.variables.dependecia;
            this.state.ListarUnidadesDocumentalesUbicar(payload);
            break;

        }



        if(this.status == 4) {
          this.formTransferencia.controls['entidadRecibe'].setValidators([Validators.required]);
          this.formTransferencia.updateValueAndValidity();
        }

      });
    }));

  }

  OnBlurEvents(control: string) {

    const formControl = this.formTransferencia.get(control);

    if(isNullOrUndefined(formControl))
       return;
    if(!isNullOrUndefined(formControl.value))
     formControl.setValue(formControl.value.toString().trim());
    this.SetValidationMessages(control);
  }

  SetValidationMessages(control: string) {
    const formControl = this.formTransferencia.get(control);
    if (formControl.touched && formControl.invalid) {
      const error_keys = Object.keys(formControl.errors);
      const last_error_key = error_keys[error_keys.length - 1];
      this.validations[control] = VALIDATION_MESSAGES[last_error_key];
    } else {
        this.validations[control] = '';
    }
  }

    SetListadoSubscriptions() {
      this.subscribers.push(this.State.ListadoActualizado$.subscribe((data)=>{

         if(!isNullOrUndefined(data)){

          const fechaHora = new Date().getTime();

          this.formTransferencia.get('fechaAprobacion').setValue(fechaHora);
         if(!isNullOrUndefined(data.consecutivo))
          this.formTransferencia.get('nroTransferencia').setValue(data.consecutivo);

         if(this.status == 1 || this.status == 4 ){
           this.state.ListadoUnidadDocumental = [... this.state.ListadoUnidadDocumental.map( ud => {
             if(ud.estado == 'Aprobado' || ud.estado == 'Ubicada')
                ud.consecutivoTransferencia = this.formTransferencia.get('nroTransferencia').value;

             return ud;
           })]
         }

        }

        if(this.task){
          this.restore();
        }
          this._detectChanges.detectChanges();
      }));
    }

   Finalizar() {
    if(this.State.ListadoUnidadDocumental.length) {

      const listado = this.state.ListadoUnidadDocumental.filter(unidad => {

        if (this.status === 2)
          return !unidad.blocked;

        return true;
      });

      const item_pendiente = (this.status == 2  && listado.length != this.state.ListadoUnidadDocumental.length) || listado.some(_item => {

        if(this.status != 3)
         return _item.estado == 'Rechazado' && (isNullOrUndefined(_item.observaciones) || _item.observaciones === '');

        else
          return  _item.estado == "Ubicada" &&  _item.soporte != 'Electronico'  &&  _item.unidadesConservacion.some( uc =>  isNullOrUndefined(uc.ideBodega));

      });
      if (item_pendiente) {

        let mensaje = "";

        switch (this.status){
          case 1: mensaje = "Revise que no existe alguna unidad con estado rechazado y sin observaciones";
           break;
          case 2 : mensaje = listado.length !=this.state.ListadoUnidadDocumental.length ? "Recuerde que debe aprobar/rechazar todas las unidades documentales": "Revise que no existe alguna unidad con estado rechazado y sin observaciones";
           break;
          case 3 : mensaje =  'Revise que todas las unidades física seleccionadas tenga todas sus unidades de conservación ubicadas';
           break;
        }

        this._store.dispatch(new PushNotificationAction({ severity: 'error', summary: mensaje}));

         return;
      }

      if(this.status == 2 && this.formTransferencia.invalid){

        this._store.dispatch(new PushNotificationAction({
          severity: 'error',
          summary: 'Debe de ingresar el lugar de verifición'
        }));
        return;
      }

      if(this.status == 4 && this.formTransferencia.invalid){

        this._store.dispatch(new PushNotificationAction({
          severity: 'error',
          summary: 'Debe de ingresar la entidad que recibe'
        }));
        return;
      }

      if (this.status === 1 || this.status === 4) {

        if(this.status == 4){
          this.state.ListadoUnidadDocumental = this.state.ListadoUnidadDocumental.map( u =>{
            if(u.estado == 'Ubicada')
             u.entidadRecibe = this.formTransferencia.get("entidadRecibe").value;
            return u;
          })
        }

        this.State.ActualizarAprobarTransferencia( this.status === 1  ? 'primaria' : 'secundaria')
          .subscribe(response => {

            if (response.codMensaje === '0000' ) {

              const statusForCheck = this.status ===  1 ? "Aprobado" :  'Ubicada' ;

              if((this.status == 1 && listado.some( u => u.estado == statusForCheck)) || (this.status == 4 && listado.every( u => u.estado == statusForCheck))){
                this.CompleteTask();
                return;
              }

              this._store.dispatch(go(['/' + ROUTES_PATH.workspace]));
            }
          });

        return;

      }
      if (this.status === 2) { // verficar transferencia
        this.State.ActualizarVerificarTransferencia()
          .subscribe(response => {

            if (response.codMensaje === '0000'  ) {
              if(listado.some( u => u.estado == 'Confirmada'))
                this.CompleteTask();
              else
                this.abort();
            }

          });

        return;
      }
      if (this.status === 3) { // verficar transferencia

        if(this.hasDuplicatedPhisycalLocation(this.state.ListadoUnidadDocumental.filter( u => u.estado == 'Ubicada' && !isNullOrUndefined(u.unidadesConservacion)))){

          this._store.dispatch(new PushNotificationAction({severity: 'error',summary:"Existen varias unidades con las misma ubicación física"}));

          return;
        }
        this.State.ActualizarUbicarTransferencia()
          .subscribe(response => {

            if (response.codMensaje === '0000') {

              if( listado.every( u => u.estado == 'Ubicada'))
                this.CompleteTask();
              else
                this._store.dispatch(go(['/' + ROUTES_PATH.workspace]));
            }
          });

        return;
      }

      return;
    }

    this._store.dispatch(new PushNotificationAction({
      severity: 'error',
      summary: 'No hay unidades documentales para actualizar'
    }));
  }

  CompleteTask() {
      this.subscribers.push( combineLatest(
        this._store.select(getSelectedDependencyGroupFuncionario),
        this._store.select(getAuthenticatedFuncionario)
        ).subscribe( ([dependencia,funcionario]) => {

        const params:any = {megaf:1};
        if(this.status == 1){
          params.nombreDependencia = dependencia.nombre;
          params.dependencia = dependencia.codigo;
          params.nroTransferencia  = this.formTransferencia.value.nroTransferencia;
          params.notifyToRol = ARCHIVISTA_AC;
          params.onlyRol = true;
          params.nombreTarea = `Verificar Transferencia ${this.formTransferencia.value.nroTransferencia}`;
          params.remitente = {ideFunci: funcionario.id};
        }

        if(this.status == 2){
          params.codDependencia = dependencia.codigo;
          params.usuario = funcionario.loginName;
        }

        this._taskSandBox.completeBackTaskDispatch({
          idProceso: this.task.idProceso,
          idDespliegue: this.task.idDespliegue,
          idTarea: this.task.idTarea,
          parametros: params
        });
      }));
  }

  save(): Observable<any> {
    return  Observable.of(true).delay(5000);
  }

  guardarEstadoTarea() {

    const  listado = this.state.ListadoUnidadDocumental.map( u => { return { id:u.id,estado:u.estado,observaciones:u.observaciones}});
    const payload: any = JSON.stringify(listado);
          const tareaDto: any = {
            idTareaProceso: this.task.idTarea,
            idInstanciaProceso: this.task.idInstanciaProceso,
            payload: payload
          };
          this._taskSandBox.guardarEstadoTarea(tareaDto).subscribe(() => {});
  }

  AgregarNotas(index: number) {
    this.indexUnidadSeleccionada = index;
    this.abrirNotas = true;
  }

  CerrarNotas() {
    this.abrirNotas = false;
  }

  ngOnDestroy() {
    this.subscribers.forEach(obs => {
      obs.unsubscribe();
    });
  }


  verDetalle(index?:number){

    this.isUbicar = this.status== 3 && !isNullOrUndefined(index);

    if(!isNullOrUndefined(index)){
      this.state.ListadoUnidadDocumental[index].faseArchivo = this.status < 4 ? 'Archivo de Gestión' : 'Archivo Central';
       this.selectedIndex = index;
    }


    this.state.AbrirDetalle = true;
  }

  openUbicarTransferencia(index:number){

    this.unidadDocumetalUbicar = this.state.ListadoUnidadDocumental[index];

  }

  closeUbicarTransferencia(){

    this.unidadDocumetalUbicar = null;
  }

  selectRow(event){

    if(event.data.blocked)
        this.state.unidadesSeleccionadas = this.state.unidadesSeleccionadas.filter(unidad => unidad.id != event.data.id);

  }

  selectAll(event){

    setTimeout( () => {
      this.state.unidadesSeleccionadas = event.filter(unidad => !unidad.blocked);
    },1);
  }

  descriptionSerieSubserie( item){

    if(item.codigoSubSerie)
       return `${item.codigoSerie} - ${item.nombreSerie} / ${item.codigoSubSerie} - ${item.nombreSubSerie}`;

    return `${item.codigoSerie} - ${item.nombreSubSerie}`;
  }

  restore(){

    this.subscribers.push(this._taskSandBox.getTareaPersisted(this.task.idInstanciaProceso,this.task.idTarea).subscribe( res => {

       if(!isNullOrUndefined(res) && !isNullOrUndefined(res.payload)){

         const listado = JSON.parse(res.payload);

         this.state.ListadoUnidadDocumental.forEach( u => {

           const uSaved = listado.find( us => us.id == u.id);

           if(!isNullOrUndefined(uSaved)){
             u.estado = uSaved.estado;
             u.observaciones = uSaved.observaciones;
           }

         });

         this.state.ListadoUnidadDocumental = [... this.state.ListadoUnidadDocumental];

         this._detectChanges.detectChanges();
       }

    }))

  }

  abort() {
    this._taskSandBox.abortTaskDispatch({
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      instanciaProceso: this.task.idInstanciaProceso
    });
  }

  showNoFoundUnidadesConservacionNotification(){
    if(this.status == 4)
    this._store.dispatch(new PushNotificationAction({severity:"info",summary:"No existe espacio en el archivo central, para ubicar unidades de conservación"}));
  }

  hasUnidadesFisicas(){

    return this.state.ListadoUnidadDocumental.some( u => u.soporte != 'Electronico');
  }

  private hasDuplicatedPhisycalLocation(unidadesDoc:UnidadDocumentalDTO[]){

   const unidadesConservacion:UnidadConservacionDTO[] = unidadesDoc.reduce((prev,curr) => {

      if(isNullOrUndefined(curr.unidadesConservacion))
         return prev;
      prev = prev.concat(curr.unidadesConservacion);

      return prev;
    },[]);

   return unidadesConservacion.some( uc =>  unidadesConservacion.some( uco => uco.ideUniDocConserv != uc.ideUniDocConserv && uc.ideBodega== uco.ideBodega && uc.ideUbicFisiBod == uco.ideUbicFisiBod ));

  }

}
