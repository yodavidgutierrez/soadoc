import {ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {
  Sandbox as TaskSandBox,
  Sandbox as TaskSandbox
} from "../../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox";
import {Sandbox as AsignacionSandbox} from "../../../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox";
import {Sandbox as ComunicacionSandbox} from "../../../../../infrastructure/state-management/comunicacionOficial-state/comunicacionOficialDTO-sandbox";
import {TareaDTO} from "../../../../../domain/tareaDTO";
import {Subscription} from "rxjs/Subscription";
import {getActiveTask} from "../../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors";
import {State as RootState} from "../../../../../infrastructure/redux-store/redux-reducers";
import {Store} from "@ngrx/store";
import {Observable} from "rxjs/Observable";
import {afterTaskComplete} from "../../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-reducers";
import {LoadNextTaskPayload} from "../../../../../shared/interfaces/start-process-payload,interface";
import {ScheduleNextTaskAction} from "../../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-actions";
import {isNullOrUndefined} from "util";
import {FuncionarioDTO} from "../../../../../domain/funcionarioDTO";
import {getAuthenticatedFuncionario} from "../../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {AGENTE_INTERNO} from "../../../../../shared/bussiness-properties/radicacion-properties";

@Component({
  selector: 'app-distribucion',
  templateUrl: './distribucion.component.html',
  styleUrls: ['./distribucion.component.css']
})
export class DistribucionComponent implements OnInit,OnDestroy {

  task:TareaDTO;

  activeTaskUnsubscriber:Subscription;

  @ViewChild('formEnvio') formEnvio;

  showButtonSave$:Observable<boolean>;

  funcionarioToNotify:FuncionarioDTO;

  funcionarioLog:FuncionarioDTO;



  constructor(
    private fb:FormBuilder,
    private _taskSandbox:TaskSandbox,
    private _store:Store<RootState>,
    private _asignacionSandbox:AsignacionSandbox,
    private _comunicacionSandbox:ComunicacionSandbox,
    private _changeDetector: ChangeDetectorRef
    ) { }

  ngOnInit() {

    this._store.select(getAuthenticatedFuncionario)
      .subscribe( func => this.funcionarioLog = func)
      .unsubscribe();

    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {

      this.task = activeTask;

      if (this.task) {
        this._taskSandbox.getTareaPersisted(this.task.variables.idInstancia, '0000')
          .map(r =>  JSON.parse(r.payload ))
          .subscribe(resp => { console.log("respuesta",resp);
            this.funcionarioToNotify = resp.gestionarProduccion.listaProyectores[0].funcionario;
          });
      }

      this._store.dispatch(new ScheduleNextTaskAction(null));

    });

    this.showButtonSave$ = afterTaskComplete.mapTo(false).startWith(true);
  }

  save(){

    const noRadicado = this.task.variables.numeroRadicado;

    this._asignacionSandbox
      .obtenerComunicacionPorNroRadicado(noRadicado)
      .switchMap( comunicacion => {

        comunicacion.correspondencia.codModalidadEnvio =  this.formEnvio.form.get('modalidad_correo').value.codigo;
        comunicacion.correspondencia.codClaseEnvio =  this.formEnvio.form.get('clase_envio').value.codigo;


       return this._comunicacionSandbox.actualizarComunicacion(comunicacion);
      })
      .subscribe(() => {

        const radicados = this.task.variables.numeroRadicado.split('--');

        const niceRadicado = radicados.length == 2 ? radicados[1] : this.task.variables.numeroRadicado;

         this._taskSandbox.completeTaskDispatch({
          idProceso: this.task.idProceso,
          idDespliegue: this.task.idDespliegue,
          idTarea: this.task.idTarea,
          parametros: {
            numeroRadicado:this.task.variables.numeroRadicado,
            nroRadicado : this.task.variables.numeroRadicado,
            nombreTarea : `Archivar Documento ${niceRadicado}`,
            destinatario : {ideFunci: this.funcionarioToNotify.id,codTipAgent:AGENTE_INTERNO},
            remitente : {ideFunci: this.funcionarioLog.id,codTipAgent:AGENTE_INTERNO},
            notifiable : true,
          }
        });

         this._changeDetector.detectChanges();
      });



  }

  ngOnDestroy(): void {

    this.activeTaskUnsubscriber.unsubscribe();
  }


}
