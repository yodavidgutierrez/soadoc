import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {ConstanteDTO} from "../../../domain/constanteDTO";
import {TareaDTO} from "../../../domain/tareaDTO";
import {Store} from "@ngrx/store";
import {State as RootState} from "../../../infrastructure/redux-store/redux-reducers";
import {getActiveTask} from "../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors";
import {Subscription} from "rxjs/Subscription";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Sandbox as AsignacionSandbox} from "../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox";
import {combineLatest} from "rxjs/observable/combineLatest";
import {getTipoAnexosArrayData} from "../../../infrastructure/state-management/constanteDTO-state/selectors/tipo-anexos-selectors";
import {getSoporteAnexoArrayData} from "../../../infrastructure/state-management/constanteDTO-state/selectors/soporte-anexos-selectors";
import {AnexoApi} from "../../../infrastructure/api/anexo.api";
import {afterTaskComplete} from "../../../infrastructure/state-management/tareasDTO-state/tareasDTO-reducers";
import {ROUTES_PATH} from "../../../app.route-names";
import {go} from "@ngrx/router-store";
import {Sandbox as Tasksandbox} from "../../../infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox";
import {PushNotificationAction} from "../../../infrastructure/state-management/notifications-state/notifications-actions";
import {isNullOrUndefined} from "util";
import {ComunicacionOficialDTO} from "../../../domain/comunicacionOficialDTO";
import {ViewFilterHook} from "../../../shared/ViewHooksHelper";
import {
  ROL_ASIGNADOR,
  TIPO_AGENTE_DESTINATARIO,
  TIPO_REMITENTE_INTERNO
} from "../../../shared/bussiness-properties/radicacion-properties";

@Component({
  selector: 'app-detallar-anexo',
  templateUrl: './detallar-anexo.component.html',
  styleUrls: ['./detallar-anexo.component.css']
})
export class DetallarAnexoComponent implements OnInit,OnDestroy {

  descripcionAnexos:Array<{ tipoAnexo: ConstanteDTO, descripcion: string, soporteAnexo: ConstanteDTO,ideAnexo:number }> = [];

  tipoAnexoDescription;

  anexoSelected:number = -1;

  comunicacion:ComunicacionOficialDTO;

  task:TareaDTO;

  subscriptions:Subscription[] = [];

  validations:any;

  form:FormGroup;

  constructor(private _store:Store<RootState>,
              private  _asignacionSanbox:AsignacionSandbox,
              private _changeDetector:ChangeDetectorRef,
              private fb:FormBuilder,
              private anexoApi:AnexoApi,
              private  _taskSandbox:Tasksandbox
              ) {


    this.initForm();
  }

  private initForm(){

    this.form = this.fb.group({
      description: [null,Validators.maxLength(1000)]
    })
  }

  ngOnInit() {

    this.subscriptions.push( this._store.select(getActiveTask).subscribe( task => {

      this.task = task;

      if(isNullOrUndefined(this.task))
         return;

      switch (this.task.idProceso){
        case "proceso.correspondencia-entrada" : ViewFilterHook.addFilter(`detallar-anexo-${this.task.idProceso}`,
          (parameters) => {
            return Object.assign(parameters,{
              notifyToRol: ROL_ASIGNADOR,
              notifyToDependenciaList:  this.comunicacion.agenteList.filter( agente => agente.codTipoRemite == TIPO_REMITENTE_INTERNO).map( agente => agente.codDependencia),
              remitente :{ideFunci: this.comunicacion.correspondencia.codFuncRadica} ,
              nroRadicado:this.comunicacion.correspondencia.nroRadicado
            })
          })
      }

        this.subscriptions.push(
        this._asignacionSanbox.obtenerComunicacionPorNroRadicado(this.task.variables.numeroRadicado)
          .map(comunicacion => {
            this.comunicacion = comunicacion;
            return comunicacion.anexoList;
          })
          .switchMap( anexoList => {

            return  combineLatest(this._store.select(getTipoAnexosArrayData),this._store.select(getSoporteAnexoArrayData))
              .map(([tipos,soporteTipo]) => {

                return anexoList.map( anexo => {
                  return {
                    descripcion: anexo.descripcion,
                    tipoAnexo:tipos.find(t => t.codigo == anexo.codAnexo),
                    soporteAnexo : soporteTipo.find( s => s.codigo == anexo.codTipoSoporte),
                    ideAnexo:anexo.ideAnexo,
                    nombreTarea : "Asignar Comunicaciones"
                  }
                })
              } )
          }).subscribe(aList => {

          this.descripcionAnexos = aList;

          this._changeDetector.detectChanges();
        })
      );


    }));

    this.subscriptions.push(afterTaskComplete.subscribe(_ => {
      this._store.dispatch(go(['/' + ROUTES_PATH.workspace]));
    }))
  }

  selectAnexo(i:number){

    this.anexoSelected = i;

    this.AnexoDescription = this.descripcionAnexos[i].descripcion;

  }

  changeDescription(){

    if(!this.form.valid){

      this.validations.tipoAnexosDescripcion = 'No debe de introducir mas mil caractéres';

      return;

    }

    this.descripcionAnexos[this.anexoSelected].descripcion = this.AnexoDescription;

    this.anexoSelected = -1;
  }

  ngOnDestroy(): void {

    this.subscriptions.forEach( s => s.unsubscribe());
  }

  get AnexoDescription(){

    return this.form.get('description').value;
  }

  set AnexoDescription(value:string){

    this.form.get('description').setValue(value)
  }

  save(finishTask:boolean = false){

    if(this.form.invalid){

      this._store.dispatch(new PushNotificationAction({severity:'error',summary:"La descripción no debe de tener mas 1000 caractéres"}))
    }

    const payload = this.descripcionAnexos.map( a => {

      return {
        ideAnexo:a.ideAnexo,
        descripcion:a.descripcion,
        codAnexo:a.tipoAnexo.codigo,
        codTipoSoporte:a.soporteAnexo.codigo
      }
    });

    this.subscriptions.push(this.anexoApi.updateAnexos(payload).subscribe( _ => {
        this._store.dispatch(new PushNotificationAction({severity:'success',summary:"Se han guardado las descripciones de los anexos"}))

        if(finishTask){
          this.finalizar();
        }
      },
      _ => { this._store.dispatch(new PushNotificationAction({severity:'error',summary:"No se han podido guardar las descripciones de los anexos"})) }));


  }

 private finalizar(){

    this._taskSandbox.completeTaskDispatch(
      {
        idProceso: this.task.idProceso,
        idDespliegue: this.task.idDespliegue,
        idTarea: this.task.idTarea,
        parametros:{
        }
      }
    )

  }

  trimDescription(){

    const control = this.form.get('description');

    if(!isNullOrUndefined(control.value))
       control.setValue(control.value.toString().trim());
  }


}
