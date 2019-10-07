import {ChangeDetectorRef, Component,  ViewChild} from '@angular/core';
import {PlanAgenDTO} from '../../../domain/PlanAgenDTO';
import {getTipologiaDocumentalArrayData} from '../../../infrastructure/state-management/constanteDTO-state/selectors/tipologia-documental-selectors';
import {FormBuilder} from '@angular/forms';
import {Store} from '@ngrx/store';
import {PlanillasApiService} from '../../../infrastructure/api/planillas.api';
import {Sandbox as ConstanteSandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {Sandbox as FuncionarioSandbox} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-sandbox';
import {Sandbox as DependenciaSandbox} from '../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {getArrayData as PlanillasArrayData} from '../../../infrastructure/state-management/cargarPlanillasDTO-state/cargarPlanillasDTO-selectors';
import {Sandbox as CargarPlanillasSandbox} from '../../../infrastructure/state-management/cargarPlanillasDTO-state/cargarPlanillasDTO-sandbox';
import {PlanillaDTO} from '../../../domain/PlanillaDTO';
import {PlanAgentesDTO} from '../../../domain/PlanAgentesDTO';
import {CompleteTaskAction} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-actions';
import {CargarPlanillasBase} from "../../../shared/supertypes/cargar-planillas.base";
import {
  getAuthenticatedFuncionario,
  getSelectedDependencyGroupFuncionario,
  getArrayData as getFuncionarioArrayData
} from "../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {
  getAgragarObservacionesDialogVisible,
  getDetailsDialogVisible,
  getJustificationDialogVisible, getRejectDialogVisible
} from "../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-selectors";
import {getActiveTask} from "../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors";
import {PushNotificationAction} from "../../../infrastructure/state-management/notifications-state/notifications-actions";
import {isNullOrUndefined} from "util";
import {FuncionariosService} from "../../../infrastructure/api/funcionarios.service";
import {PERSONA_NATURAL} from "../../../shared/bussiness-properties/radicacion-properties";

@Component({
  selector: 'app-cargar-planillas',
  templateUrl: './cargar-planillas.component.html',
  styleUrls: ['./cargar-planillas.component.css'],
  })
export class CargarPlanillasComponent extends  CargarPlanillasBase{

  @ViewChild('popupEditar') popupEditar;
  funcionarioAprobador:any;

  constructor(protected _store:Store<RootState>,
              protected _cargarPlanillasApi:CargarPlanillasSandbox,
              protected _funcionarioSandbox:FuncionarioSandbox,
              protected _constSandbox:ConstanteSandbox,
              protected _dependenciaSandbox:DependenciaSandbox,
              protected _planillaService:PlanillasApiService,
              protected _changeDetectorRef:ChangeDetectorRef,
              protected formBuilder:FormBuilder,
              private _funcionarioApi:FuncionariosService
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

    this.dependenciaSelected$ = this._store.select(getSelectedDependencyGroupFuncionario);
    this.dependenciaSelected$.subscribe((result) => {
      this.dependenciaSelected = result;
    });
    this.funcionariosSuggestions$ = this._store.select(getFuncionarioArrayData);
    this.justificationDialogVisible$ = this._store.select(getJustificationDialogVisible);
    this.detailsDialogVisible$ = this._store.select(getDetailsDialogVisible);
    this.agregarObservacionesDialogVisible$ = this._store.select(getAgragarObservacionesDialogVisible);
    this.rejectDialogVisible$ = this._store.select(getRejectDialogVisible);
    this.start_date.setHours(this.start_date.getHours() - 24);
    this.funcionarioSubcription = this._store.select(getAuthenticatedFuncionario).subscribe((funcionario) => {
      this.funcionarioLog = funcionario;
    });
    this.comunicacionesSubcription = this._store.select(PlanillasArrayData).subscribe((result) => {
      this.selectedComunications = [];
    });

    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {
      this.task = activeTask;
      if (this.task && this.task.variables) {
        this.nroPlanilla = this.task.variables.numPlanilla;
      }
    });

    this.initForm();
  }

  ngOnInit() {
    this.tipologiaDocumentalSuggestions$ = this._store.select(getTipologiaDocumentalArrayData);
    this.tipologiaDocumentalSuggestions$.subscribe((results) => {
      this.tipologiasDocumentales = results;
    });

    this._funcionarioSandbox.loadAllFuncionariosDispatch();
    this._constSandbox.loadDatosGeneralesDispatch();
    this.listarDependencias();
  }
  ngOnDestroy() {
    this.funcionarioSubcription.unsubscribe();
    this.comunicacionesSubcription.unsubscribe();
    this.activeTaskUnsubscriber.unsubscribe();
  }

  findDependency(code):string {
    const result = this.dependencias.find((element) => element.codigo === code);
    return result ? result.nombre : '';
  }

  findSede(code):string {
    const result = this.dependencias.find((element) => element.codSede === code);
    return result ? result.nomSede : '';
  }

  listarDependencias() {
    this._dependenciaSandbox.loadDependencies({}).subscribe((results) => {
      this.dependencias = results.dependencias;
      this.listarDistribuciones();
    });
  }

  listarDistribuciones() {
    this._cargarPlanillasApi.loadData({
      nro_planilla: this.nroPlanilla,
    }).subscribe((result) => {
      this.data = result;
      this.planAgentes = [...result.pagentes.pagente];

      if(isNullOrUndefined(this.funcionarioAprobador)){

        this._funcionarioApi.getFuncionarioById(result.codFuncGenera)
          .subscribe( funcionario => this.funcionarioAprobador = funcionario);
      }

      this.refreshView();
    });
  }

  actualizarPlanilla() {

    if(!this.canUpdatePlanilla()){

      this._store.dispatch(new PushNotificationAction({severity:'error',summary:"Existen comunicaciones sin registrar información de entrega"}));

      return;
    }

    this.planAgentes.forEach((p) => {
      delete p.usuario;
      delete p['_$visited'];
    });
    const agentes:PlanAgentesDTO = {
      pagente: this.planAgentes
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
      this.listarDistribuciones();
    });

  }

  protected  customizePlanAgenteDTO(agente:PlanAgenDTO,formValue){

      agente.codNuevaSede =  this.data.codSedeDestino;
     // agente.desNuevaSede =  this.findSede(this.data.codSedeDestino);
      agente.codNuevaDepen =  this.data.codDependenciaDestino;
     // agente.desNuevaDepen =  this.findDependency(this.data.codDependenciaDestino);

      return agente;
  }

  showNombre(correspondencia){

    if(isNullOrUndefined(correspondencia.tipoPersona))
       return '';

    return correspondencia.tipoPersona.codigo == PERSONA_NATURAL ? correspondencia.nombre : correspondencia.razonSocial;
  }


}
