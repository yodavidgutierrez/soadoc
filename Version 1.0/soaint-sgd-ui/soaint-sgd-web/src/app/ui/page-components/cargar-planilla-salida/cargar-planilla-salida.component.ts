import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {PlanAgenDTO} from '../../../domain/PlanAgenDTO';
import {RadicacionEntradaDTV} from '../../../shared/data-transformers/radicacionEntradaDTV';
import {AgentDTO} from '../../../domain/agentDTO';
import {Observable} from 'rxjs/Observable';
import {getTipologiaDocumentalArrayData} from '../../../infrastructure/state-management/constanteDTO-state/selectors/tipologia-documental-selectors';
import {FormBuilder, FormGroup} from '@angular/forms';
import {FuncionarioDTO} from '../../../domain/funcionarioDTO';
import {DependenciaDTO} from '../../../domain/dependenciaDTO';
import {Subscription} from 'rxjs/Subscription';
import {ConstanteDTO} from '../../../domain/constanteDTO';
import {SUCCESS_ADJUNTAR_DOCUMENTO} from 'app/shared/lang/es';
import {ERROR_ADJUNTAR_DOCUMENTO} from 'app/shared/lang/es';
import {Store} from '@ngrx/store';
import {PlanillasApiService} from '../../../infrastructure/api/planillas.api';
import {Sandbox as ConstanteSandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {
  getArrayData as getFuncionarioArrayData,
  getAuthenticatedFuncionario,
  getSelectedDependencyGroupFuncionario
} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {
  getAgragarObservacionesDialogVisible,
  getDetailsDialogVisible,
  getJustificationDialogVisible,
  getRejectDialogVisible
} from '../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-selectors';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {Sandbox as FuncionarioSandbox} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-sandbox';
import {Sandbox as DependenciaSandbox} from '../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {Sandbox as taskSandbox} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import {getArrayData as PlanillasArrayData} from '../../../infrastructure/state-management/cargarPlanillasDTO-state/cargarPlanillasDTO-selectors';
import {Sandbox as CargarPlanillasSandbox} from '../../../infrastructure/state-management/cargarPlanillasDTO-state/cargarPlanillasDTO-sandbox';
import {PlanillaDTO} from '../../../domain/PlanillaDTO';
import {PlanAgentesDTO} from '../../../domain/PlanAgentesDTO';
import {environment} from '../../../../environments/environment';
import {correspondenciaEntrada} from '../../../infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-selectors';
import {ApiBase} from '../../../infrastructure/api/api-base';
import {getActiveTask} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {CompleteTaskAction} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-actions';
import {PushNotificationAction} from "../../../infrastructure/state-management/notifications-state/notifications-actions";
import { afterTaskComplete } from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-reducers';
import {FileUpload} from "primeng/primeng";
import {isNullOrUndefined} from "util";
import {CargarPlanillasBase} from "../../../shared/supertypes/cargar-planillas.base";
import {DependenciaApiService} from "../../../infrastructure/api/dependencia.api";


@Component({
  selector: 'app-cargar-planilla-salida',
  templateUrl: './cargar-planilla-salida.component.html',
  styleUrls: ['./cargar-planilla-salida.component.css']
})
export class CargarPlanillaSalidaComponent  extends CargarPlanillasBase{


  data:PlanillaDTO | any = {};

  subscriptions: Subscription[];

  dependencias$:Observable<any[]>;

  closedTask:any;



  constructor(protected _store:Store<RootState>,
              protected _cargarPlanillasApi:CargarPlanillasSandbox,
              protected _funcionarioSandbox:FuncionarioSandbox,
              protected _constSandbox:ConstanteSandbox,
              protected _dependenciaSandbox:DependenciaSandbox,
              protected _planillaService:PlanillasApiService,
              protected _changeDetectorRef:ChangeDetectorRef,
              protected formBuilder:FormBuilder,
              private _dependenciaApi:DependenciaApiService
              ) {

    super(_store,
      _cargarPlanillasApi,
      _funcionarioSandbox,
      _constSandbox,
      _dependenciaSandbox,
      _planillaService,
      _changeDetectorRef,
      formBuilder
    );
  }

  ngOnInit() {

    this.dependencias$ = this._dependenciaApi.Listar().share()
    this.closedTask = afterTaskComplete.map(() => true).startWith(false);

    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {
      this.task = activeTask;
      if (this.task && this.task.variables) {
        this.nroPlanilla = this.task.variables.numPlanilla;
      }
    });

    this._store.select(getAuthenticatedFuncionario)
      .subscribe(funcionario => this.funcionarioLog = funcionario)
      .unsubscribe();

    this.start_date.setHours(this.start_date.getHours() - 24);

    this.listarPlanAgentes();
  }


  ngOnDestroy() {
    this.activeTaskUnsubscriber.unsubscribe();
  }

  listarPlanAgentes() {
    this._cargarPlanillasApi.loadPlanillasSalida({
      nro_planilla: this.nroPlanilla,
    }).subscribe((result) => {
      this.data = result;
      this.planAgentes = [...result.pagentes.pagente];


      this.refreshView();
    });
  }

  process(result){

  }

  actualizarPlanilla() {

    const cloneAgentes = [... this.planAgentes];
    // limpiar agente object
    cloneAgentes.forEach((p) => {
      delete p.usuario;
      delete p['_$visited'];
      delete p.desNuevaDepen;
      delete p.desNuevaSede;
      delete p.agente;
      p.nroRadicado = p.correspondencia.nroRadicado;
      delete p.correspondencia;
    });
    const agentes: PlanAgentesDTO = {
      pagente: cloneAgentes
    };
    const planilla:PlanillaDTO = {
      idePlanilla: null,
      nroPlanilla: null,
      fecGeneracion: null,
      codTipoPlanilla: null,
      codFuncGenera: null,
      codSedeOrigen: null,
      codDependenciaOrigen: null,
      codSedeDestino: null,
      codDependenciaDestino: null,
      codClaseEnvio: null,
      codModalidadEnvio: null,
      pagentes: agentes,
      ideEcm: null
    };

    this._planillaService.cargarPlanillas(planilla).subscribe(() => {
      this._store.dispatch(new CompleteTaskAction(this.getTaskToCompletePayload()));
      this.listarPlanAgentes();
    });
  }
}
