import {Component, OnDestroy, OnInit} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {TareaDTO} from 'app/domain/tareaDTO';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {Store} from '@ngrx/store';
import {getArrayData, getTaskCount} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {Sandbox as TaskDtoSandbox} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import {getSelectedDependencyGroupFuncionario} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {Subscription} from 'rxjs/Subscription';
import {DependenciaDTO} from '../../../domain/dependenciaDTO';

@Component({
  selector: 'app-workspace',
  templateUrl: './workspace.component.html'
})
export class WorkspaceComponent implements OnInit, OnDestroy {

  tasks$: Observable<TareaDTO[]>;

  totalRecords$: Observable<number>;

  currentPage: number;

  readonly pageSize = 10;

  private init = false;

  selectedTask: any;

  globalDependencySubcription: Subscription;

  dependenciaSelected$: Observable<DependenciaDTO>;

  constructor(private _store: Store<RootState>, private _taskSandbox: TaskDtoSandbox) {
    this.tasks$ = this._store.select(getArrayData);
    this.totalRecords$ = this._store.select(getTaskCount);
    this.dependenciaSelected$ = this._store.select(getSelectedDependencyGroupFuncionario);
  }

  ngOnInit() {
    this.globalDependencySubcription = this.dependenciaSelected$.subscribe((result) => {
      this._taskSandbox.loadDispatch({page:0,pageSize: this.pageSize});
    });

    this.totalRecords$.subscribe( e => console.log(e));
  }

  ngOnDestroy() {
    this.globalDependencySubcription.unsubscribe();
  }

  iniciarTarea(task) {
    if (task.estado === 'LISTO') {
      this._taskSandbox.reserveTaskDispatch(task);
    } else {
      this._taskSandbox.startTaskDispatch(task);
    }
  }

  loadTaskPage(evt){

    if(!this.init){

      this.init = true;
      return;
    }

    this._taskSandbox.loadDispatch({page:Math.round(evt.first/evt.rows) ,pageSize: this.pageSize});
  }

}

