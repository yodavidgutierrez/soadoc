import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {Sandbox as ConstanteSandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {Sandbox as TaskDtoSandbox} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import {Observable} from 'rxjs/Observable';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {Store} from '@ngrx/store';
import {getArrayData as ProcessArrayData} from 'app/infrastructure/state-management/procesoDTO-state/procesoDTO-selectors';
import {
  getArrayData, getTasksStadistics, getInProgressTasksArrayData
} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {TareaDTO} from 'app/domain/tareaDTO';
import 'rxjs/add/operator/withLatestFrom';
import {GetTaskStatsAction} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-actions';
import {TaskManagerService} from "../../../infrastructure/api/task-manager.service";
import {combineLatest} from "rxjs/observable/combineLatest";
import {Subscription} from "rxjs/Subscription";
import {DependenciaDTO} from '../../../domain/dependenciaDTO';
import {getSelectedDependencyGroupFuncionario} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import { LoginSandbox } from '../login/__login.include';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit,OnDestroy {

  tasksStadistics: any[];
   subscriptions:Subscription[] = [];

  globalDependencySubcription: Subscription[] = [];
  completedTasks = 0;
  progressTasks = 0;
  readyTasks = 0;

  dependenciaSelected$: Observable<DependenciaDTO>;
  percents;
  isLoading: boolean;


  constructor(private _store: Store<RootState>, private _sandbox: LoginSandbox, private _constSandbox: ConstanteSandbox,private _taskSandbox: TaskDtoSandbox,private taskManager:TaskManagerService,private _changeDetector: ChangeDetectorRef) {
    this.dependenciaSelected$ = this._store.select(getSelectedDependencyGroupFuncionario);

    this.subscriptions.push(
      this.dependenciaSelected$.switchMap(dep =>
        combineLatest(this.taskManager.getStadistics('completadas', dep)
        ,this.taskManager.getStadistics('enprogreso', dep),
        this.taskManager.getStadistics('ready', dep),

      ))
      .subscribe(([completadas,progreso,ready]) => {

        this.completedTasks = completadas.length;
        this.readyTasks = ready.length;
        this.progressTasks = progreso.length;

        const total = this.completedTasks + this.progressTasks + this.readyTasks;

        this.percents = {
          complete: total === 0 ? 0 : Math.round(100*this.completedTasks/total),
          progress: total === 0 ? 0 : Math.round(100*this.progressTasks/total),
          ready: total === 0 ? 0 : Math.round(100*this.readyTasks/total),
        };

        this.tasksStadistics = [
          {name:'COMPLETADO', value:this.completedTasks},
          {name:'ENPROGRESO', value:this.progressTasks},
          {name:'LISTO', value:this.readyTasks},
        ];

        this._changeDetector.detectChanges();

      })
    );



  }

  ngOnInit() {
    this._constSandbox.loadDatosGeneralesDispatch();
  }

  ngOnDestroy(): void {

    this.subscriptions.forEach( s => s.unsubscribe());
  }

 /* showRadicadoTicket(event) {
    event.preventDefault();
    this.visibleRadicadoTicket = true;

  }*/

}
