import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {Store} from "@ngrx/store";
import {State as RootState} from "../../../infrastructure/redux-store/redux-reducers";
import {Observable} from "rxjs/Observable";
import {TareaDTO} from "../../../domain/tareaDTO";
import {DependenciaDTO} from "../../../domain/dependenciaDTO";
import {Subscription} from "rxjs/Subscription";
import {FuncionariosService} from "../../../infrastructure/api/funcionarios.service";
import {TaskManagerService} from "../../../infrastructure/api/task-manager.service";
import {PushNotificationAction} from "../../../infrastructure/state-management/notifications-state/notifications-actions";
import {isNullOrUndefined} from "util";
import {FuncionarioDTO} from "../../../domain/funcionarioDTO";
import {getSelectedDependencyGroupFuncionario} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {Sandbox as TaskDtoSandbox} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';

@Component({
  selector: 'app-workspace-user',
  templateUrl: './workspace-user.component.html',
  styleUrls: ['./workspace-user.component.css']
})
export class WorkspaceUserComponent implements OnInit,OnDestroy {

  tasks: TareaDTO[] = [] ;

  selectedTasks: TareaDTO[] = [];
  totalRecords$: Observable<number>;
  currentPage: number;
  readonly pageSize = 10;
  private init = false;

  funcionariosOrigen:any[] = [];
  funcionariosDestino:any[] = [];

  funcionarioOrigen:any;

  funcionariosList:FuncionarioDTO[];

  funcionarioDestino:FuncionarioDTO

  subscriptions: Subscription[] = [];

  dependenciaSelected$: Observable<DependenciaDTO>;

   constructor(private _store: Store<RootState>, private _funcionarioApi:FuncionariosService,
               private taskManager: TaskManagerService, private _changeDetector:ChangeDetectorRef) {
     this.dependenciaSelected$ = this._store.select(getSelectedDependencyGroupFuncionario);
    }

  ngOnInit() {

   this._funcionarioApi.getAllFuncionarios().subscribe(funcionarios => {
     this.funcionariosOrigen = funcionarios;
     this.funcionariosList = funcionarios;
     this.funcionariosDestino = funcionarios;
   });

  }

  ngOnDestroy() {
    this.subscriptions.forEach( s => s.unsubscribe());
  }


  reassignTasks(){

 this.taskManager.reassignTasks(this.selectedTasks,this.funcionarioDestino.loginName)
      .subscribe(_ => {
       this._store.dispatch(new PushNotificationAction({severity: 'success', summary: 'La tarea ha sido reasignada'}));
       this.loadTasks();
      },
        _ => {
          this._store.dispatch(new PushNotificationAction({severity: 'error', summary: 'No se ha podido reasignar la tarea'}));

        },
        () => {
        this._changeDetector.detectChanges();
        }
        )

  }

   changeFuncionarioOrigen(){

    this.funcionariosDestino = this.funcionariosList.filter( funcionario => funcionario.loginName != this.funcionarioOrigen);

     if(!isNullOrUndefined(this.funcionarioDestino) && this.funcionarioDestino.loginName == this.funcionarioOrigen)
       this.funcionariosDestino = null;

     this.loadTasks();
   }

   changeFuncionarioDestino(){

     this.loadTasks();
   }

   private loadTasks(){
      this.dependenciaSelected$.switchMap( dep =>
     this.taskManager.getTasksForUser(this.funcionarioOrigen, dep, {page:0, pageSize: this.pageSize}))
       .map( tasks => tasks.filter( task => task.estado != 'Ready'))
         .subscribe( tasks => {
         this.tasks = tasks.filter( task =>  isNullOrUndefined(this.funcionarioDestino) ||
           this.funcionarioDestino.roles.some(rol => rol.rol == task.rol) ||
           this.funcionarioDestino.dependencias.some( dep => dep.codigo == task.codigoDependencia)
         ).map( task => {delete  task.codigoDependencia; return task;});
         this.selectedTasks = this.tasks.filter( task => this.selectedTasks.some(taskSelected => taskSelected.idTarea == task.idTarea));
         this._changeDetector.detectChanges();
       });
   }

   selectAllTasks(){

     this.selectedTasks = [... this.tasks];
   }

  loadTaskPage(evt){

    if(!this.init){

      this.init = true;
      return;
    }

    this.taskManager.getTasksForUser(this.funcionarioOrigen,this.dependenciaSelected$, { page:Math.round(evt.first/evt.rows) ,pageSize: this.pageSize});
  }
}
