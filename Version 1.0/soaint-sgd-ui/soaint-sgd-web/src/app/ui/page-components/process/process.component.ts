import {Component, OnDestroy, OnInit} from '@angular/core';
import {State as RootState} from '../../../infrastructure/redux-store/redux-reducers';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {getArrayData as ProcesoDtoArrayData} from '../../../infrastructure/state-management/procesoDTO-state/procesoDTO-selectors';
import {ProcesoDTO} from '../../../domain/procesoDTO';
import {Sandbox as ProcessDtoSandbox} from '../../../infrastructure/state-management/procesoDTO-state/procesoDTO-sandbox';
import {process_info} from '../../../../environments/environment';
import {ProcesoService} from "../../../infrastructure/api/proceso.service";
import {getSelectedDependencyGroupFuncionario} from "../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {Subscription} from "rxjs/Subscription";
import {combineLatest} from "rxjs/observable/combineLatest";

@Component({
  selector: 'app-workspace',
  templateUrl: './process.component.html'
})
export class ProcessComponent implements OnInit,OnDestroy {

  procesos$: Observable<Array<ProcesoDTO>>;

  selectedProcess: ProcesoDTO;

  subscriptions:Subscription[] = [];

   constructor(private procesoService:ProcesoService, private _processSandbox:ProcessDtoSandbox,private _store:Store<RootState>) {

    this.procesos$ = combineLatest( this._store.select(getSelectedDependencyGroupFuncionario), this.procesoService.getProcess())
                           .map(([dependencia,procesos]) => {

                           return procesos.filter( p => dependencia.radicadora || (p.codigoProceso != 'proceso.correspondencia-entrada' && p.codigoProceso != 'proceso.correspondencia-salida'));
                           });
  }

  ngOnInit() {

   }

  iniciarProceso(process) {

    console.log(process);
    this._processSandbox.initProcessDispatch(process);
  }

  getProcessDisplayName(proceso) {
    return process_info[proceso.codigoProceso] ? process_info[proceso.codigoProceso].displayValue : proceso.nombreProceso
  }

  ngOnDestroy(): void {

    this.subscriptions.forEach(s => s.unsubscribe());
  }

}

