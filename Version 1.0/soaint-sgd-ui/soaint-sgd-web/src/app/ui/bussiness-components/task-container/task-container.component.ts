import {
  AfterViewInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component, EventEmitter, Input, OnChanges,
  OnDestroy,
  OnInit, Output, SimpleChange, SimpleChanges,
  ViewEncapsulation
} from '@angular/core';
import {State as RootState} from '../../../infrastructure/redux-store/redux-reducers';
import {Store} from '@ngrx/store';
import {Subscription} from 'rxjs/Subscription';
import {getEntities as getProcessEntities} from '../../../infrastructure/state-management/procesoDTO-state/procesoDTO-selectors';
import {Observable} from 'rxjs/Observable';
import {getActiveTask, getNextTask} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {TareaDTO} from '../../../domain/tareaDTO';
import {go} from '@ngrx/router-store';
import {ContinueWithNextTaskAction, ResetTaskAction} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-actions';
import {ROUTES_PATH} from '../../../app.route-names';
import {process_info} from '../../../../environments/environment';
import {isNullOrUndefined} from "util";
import { Sandbox as TaskSandBox } from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import {RadicarSalidaComponent} from "../../page-components/radicacion-salida/radicar-salida.component";


@Component({
  selector: 'app-task-container',
  templateUrl: './task-container.component.html',
  styleUrls: ['./task-container.component.css'],
  encapsulation: ViewEncapsulation.None,
  // changeDetection: ChangeDetectionStrategy.OnPush
})
export class TaskContainerComponent implements OnInit, OnDestroy {

  task: TareaDTO = null;
 @Input() processName = '';
 @Input() taskName = "";
 @Input() isActive = true;
 @Input() forceHiddenButtons = false;
 @Input() hideDefaultButton = false;
 @Input() hidden: boolean;

 ocultar:boolean;
   hasToContinue: boolean  = false;

  activeTaskUnsubscriber: Subscription;
  infoUnsubscriber: Subscription;

  @Output() onFinalizar:EventEmitter<any> = new EventEmitter;
  @Output() onContinueTask:EventEmitter<any> = new EventEmitter;


  constructor(private _store: Store<RootState>,
              private _changeDetector: ChangeDetectorRef,
              private _taskSandBox: TaskSandBox) {

  }



  ngOnInit() {

    this.ocultar = this.hidden;

    this.infoUnsubscriber =  this._store.select(getActiveTask).subscribe( tarea =>{
      this.task = tarea;
      if(!isNullOrUndefined(this.task)) {
        this.taskName = this.task.descripcion;
        this.processName = this.task.nombreProceso;
      }
    });
   /* if(!this.processName && !this.taskName)
    this.infoUnsubscriber = Observable.combineLatest(
      this._store.select(getActiveTask),
      this._store.select(getProcessEntities)
    ).take(1).subscribe(([activeTask, procesos]) => {
      if (activeTask) {
        this.task = activeTask;
        this.taskName = this.task.nombre;
        this.processName = (process_info[procesos[activeTask.idProceso].codigoProceso]) ? process_info[procesos[activeTask.idProceso].codigoProceso].displayValue : '';
        this._changeDetector.detectChanges();
      }
    });*/

      this.activeTaskUnsubscriber = Observable.combineLatest(
      this._store.select(getActiveTask),
      this._store.select(getNextTask)
    ).distinctUntilChanged()
      .subscribe(([activeTask, hasNextTask]) => {

          this.isActive = activeTask !== null;
          this.hasToContinue = hasNextTask !== null;
          this._changeDetector.detectChanges();

      });
  }


  navigateBack() {

    this.onFinalizar.emit();

     this._store.dispatch(go(['/' + ROUTES_PATH.workspace]));
  }


  navigateToNextTask() {
    this.onContinueTask.emit();
    this._store.dispatch(new ContinueWithNextTaskAction());
  }

  ngOnDestroy() {
    if(!isNullOrUndefined(this.infoUnsubscriber))
    this.infoUnsubscriber.unsubscribe();
    this.activeTaskUnsubscriber.unsubscribe();
    this._store.dispatch(new ResetTaskAction());

  }
}
