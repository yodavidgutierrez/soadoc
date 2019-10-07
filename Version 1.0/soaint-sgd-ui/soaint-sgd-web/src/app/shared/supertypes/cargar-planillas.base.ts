import {FuncionarioDTO} from "../../domain/funcionarioDTO";
import {DependenciaDTO} from "../../domain/dependenciaDTO";
import {Subscription} from "rxjs/Subscription";
import {Observable} from "rxjs/Observable";
import {PlanillaDTO} from "../../domain/PlanillaDTO";
import {FormBuilder, FormGroup} from "@angular/forms";
import {PlanAgenDTO} from "../../domain/PlanAgenDTO";
import {ConstanteDTO} from "../../domain/constanteDTO";
import {State as RootState} from "../../infrastructure/redux-store/redux-reducers";
import {Sandbox as CargarPlanillasSandbox} from "../../infrastructure/state-management/cargarPlanillasDTO-state/cargarPlanillasDTO-sandbox";
import {Sandbox as ConstanteSandbox} from "../../infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox";
import {ApiBase} from "../../infrastructure/api/api-base";
import {Store} from "@ngrx/store";
import {Sandbox as FuncionarioSandbox} from "../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-sandbox";
import {Sandbox as DependenciaSandbox} from "../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox";
import {PlanillasApiService} from "../../infrastructure/api/planillas.api";
import {ChangeDetectorRef, OnDestroy, OnInit} from "@angular/core";
import {EditarPlanillaComponent} from "../../ui/bussiness-components/editar-planilla/editar-planilla.component";
import {isNullOrUndefined} from "util";

export abstract class CargarPlanillasBase implements OnInit,OnDestroy{

  form:FormGroup;

  planAgentes:PlanAgenDTO[] = [];

  data:PlanillaDTO | any = {};

  selectedComunications:PlanAgenDTO[] = [];

  start_date:Date = new Date();

  editarPlanillaDialogVisible = false;

  dependencia:any;

  funcionariosSuggestions$:Observable<FuncionarioDTO[]>;

  justificationDialogVisible$:Observable<boolean>;

  detailsDialogVisible$:Observable<boolean>;

  agregarObservacionesDialogVisible$:Observable<boolean>;

  rejectDialogVisible$:Observable<boolean>;

  dependenciaSelected$:Observable<DependenciaDTO>;

  dependenciaSelected:DependenciaDTO;

  funcionarioLog:FuncionarioDTO;

  funcionarioSubcription:Subscription;

  comunicacionesSubcription:Subscription;

  tipologiaDocumentalSuggestions$:Observable<ConstanteDTO[]>;

  tipologiasDocumentales:ConstanteDTO[];

  dependencias:DependenciaDTO[] = [];

  activeTaskUnsubscriber:Subscription;



  task:any;

  nroPlanilla:string;

  estadoLabel:string;

  constructor(protected _store:Store<RootState>,
              protected _cargarPlanillasApi:CargarPlanillasSandbox,
              protected _funcionarioSandbox:FuncionarioSandbox,
              protected _constSandbox:ConstanteSandbox,
              protected _dependenciaSandbox:DependenciaSandbox,
              protected _planillaService:PlanillasApiService,
              protected _changeDetectorRef:ChangeDetectorRef,
              protected formBuilder:FormBuilder){

  }

  initForm() {
    this.form = this.formBuilder.group({
      'numeroPlanilla': [null]
    });
  }

  showEditarPlantillaDialog(popupEdit:EditarPlanillaComponent){
    if(!isNullOrUndefined(popupEdit))
       popupEdit.resetData();
    this.editarPlanillaDialogVisible = true;
  }

  hideEditarPlanillaDialog() {
    this.editarPlanillaDialogVisible = false;
  }

  createAgents(formValue):PlanAgenDTO[] {
   return  this.selectedComunications.map( item => {
      item.observaciones = formValue.observaciones;
      item.estado = formValue.estadoEntrega.codigo;
      item.fecCarguePla= formValue.fechaEntrega;

      return this.customizePlanAgenteDTO(item,formValue);
    });
  }

   protected  customizePlanAgenteDTO(agente:PlanAgenDTO,formValue){

     return agente;
   }

   abstract  actualizarPlanilla();


  getTaskToCompletePayload() {
    return {
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      idTarea: this.task.idTarea,
      parametros:{}
    }
  }

  canUpdatePlanilla():boolean {
    return this.planAgentes.length > 0 && this.planAgentes.every((e) => e.estado && e.estado !== '');
  }

  refreshView() {
    this._changeDetectorRef.detectChanges();
  }

  editarPlanilla(formValue) {

    this.estadoLabel = formValue.estadoEntrega.nombre;
    const agents:PlanAgenDTO[] = this.createAgents(formValue);
    const coms = [...this.planAgentes];
    agents.forEach((element) => {
      const index = coms.findIndex((el) => el.idePlanAgen === element.idePlanAgen);
      if (index > -1) {
        coms[index] = element;
      }
    });
    this.planAgentes = [...coms];
    this.selectedComunications = [];
    this.hideEditarPlanillaDialog();
    this.refreshView();

  }

  abstract ngOnDestroy(): void;

  abstract ngOnInit(): void;

}
