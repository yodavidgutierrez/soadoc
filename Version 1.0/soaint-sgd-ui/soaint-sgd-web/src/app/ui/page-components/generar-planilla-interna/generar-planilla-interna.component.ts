import { Component, OnInit } from '@angular/core';
import {DistribucionFisicaComponent} from "../distribucion-fisica/distribucion-fisica.component";
import {Sandbox as DependenciaSandbox} from "../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox";
import {Sandbox as ConstanteSandbox} from "../../../infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox";
import {Sandbox as DistribucionFisicaSandbox} from "../../../infrastructure/state-management/distrubucionFisicaDTO-state/distrubucionFisicaDTO-sandbox";
import {Sandbox as ProcessSandbox} from "../../../infrastructure/state-management/procesoDTO-state/procesoDTO-sandbox";
import {State as RootState} from "../../../infrastructure/redux-store/redux-reducers";
import {PlanillasApiService} from "../../../infrastructure/api/planillas.api";
import {Store} from "@ngrx/store";
import {Sandbox as FuncionarioSandBox} from "../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-sandbox";
import {FormBuilder} from "@angular/forms";

@Component({
  selector: 'app-generar-planilla-interna',
  templateUrl: './generar-planilla-interna.component.html',
  styleUrls: ['./generar-planilla-interna.component.css']
})
export class GenerarPlanillaInternaComponent extends DistribucionFisicaComponent{

  constructor(protected _store: Store<RootState>,
              protected _distribucionFisicaApi: DistribucionFisicaSandbox,
              protected _funcionarioSandbox: FuncionarioSandBox,
              protected _constSandbox: ConstanteSandbox,
              protected _dependenciaSandbox: DependenciaSandbox,
              protected _planillaService: PlanillasApiService,
              protected _processSandbox: ProcessSandbox,
              protected formBuilder: FormBuilder) {

    super(_store,_distribucionFisicaApi,_funcionarioSandbox,_constSandbox,_dependenciaSandbox,_planillaService,_processSandbox,formBuilder);
  }

  ngOnInit() {
  }

}
