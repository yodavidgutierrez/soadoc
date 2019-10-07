import {ChangeDetectorRef, Component, Input, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {CreateUDActionType, EventChangeActionArgs} from "./crear-unidad-documental";
import {SolicitudCreacionUDDto} from "../../../../domain/solicitudCreacionUDDto";
import {State as RootState} from "../../../../infrastructure/redux-store/redux-reducers";
import {Store} from "@ngrx/store";
import {Sandbox as TaskSandbox} from "../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox";
import {TareaDTO} from "../../../../domain/tareaDTO";
import {getActiveTask} from "../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors";
import {SolicitudCreacionUdService} from "../../../../infrastructure/api/solicitud-creacion-ud.service";
import {Observable} from "rxjs/Observable";
import {FormBuilder, FormGroup} from "@angular/forms";
import {SolicitudCreacioUdModel} from "../archivar-documento/models/solicitud-creacio-ud.model";
import {FuncionariosService} from "../../../../infrastructure/api/funcionarios.service";
import {Subscription} from "rxjs/Subscription";
import {isNullOrUndefined} from "util";
import {afterTaskComplete} from "../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-reducers";
import {ROUTES_PATH} from "../../../../app.route-names";
import {go} from "@ngrx/router-store";
import {Sandbox as SedeSandbox} from "../../../../infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-sandbox";
import {getArrayData as getSedesArrayData} from "../../../../infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-selectors";
import {DependenciaApiService} from "../../../../infrastructure/api/dependencia.api";
import * as moment from 'moment';
import {SerieService} from "../../../../infrastructure/api/serie.service";
import {getSelectedDependencyGroupFuncionario} from "../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {SerieDTO} from "../../../../domain/serieDTO";
import {SubserieDTO} from "../../../../domain/subserieDTO";
import {InstrumentoApi} from "../../../../infrastructure/api/instrumento.api";

@Component({
  selector: 'app-crear-unidad-documental',
  templateUrl: './crear-unidad-documental.component.html',
})
export class CrearUnidadDocumentalComponent implements OnInit, OnDestroy {

  currentAction: CreateUDActionType;

  solicitudModel: SolicitudCreacioUdModel;

  solicitudSelected: SolicitudCreacionUDDto;

  task: TareaDTO;

  visiblePopup: boolean = false;

  solicitudesProcesadas$: Observable<any[]>;

  form: FormGroup;

  subscriptions: Subscription[] = [];

  seriesObservable$: Observable<SerieDTO[]>;

  allSubSeriesObservable$: Observable<SubserieDTO[]>;

  constructor(private _store: Store<RootState>,
              private _taskSandbox: TaskSandbox,
              private _solicitudService: SolicitudCreacionUdService,
              private fb: FormBuilder,
              private _funcionarioService: FuncionariosService,
              private _dependenciaApi: DependenciaApiService,
              private _sedeSandbox: SedeSandbox,
              private changeDetector: ChangeDetectorRef,
              private serieSubSerieService: SerieService,
              private instrumentoApi: InstrumentoApi
  ) {

    this.formInit();

    this.solicitudModel = new SolicitudCreacioUdModel(this._solicitudService);
  }

  ngOnInit() {

    this.subscriptions.push(
      this._store.select(getActiveTask).subscribe(task => {

        if (isNullOrUndefined(task))
          return;
        this.task = task;

        this._dependenciaApi.GetDependenciaPorCodigo(task.variables.codDependencia)
          .map(dependencies => dependencies.dependencias[0])
          .subscribe(dep => {

            if (!isNullOrUndefined(dep))
              this.form.get('dependencia').setValue(dep.nombre);
          });

        this._sedeSandbox.loadData()
          .map(sedes => isNullOrUndefined(sedes.organigrama) ? null : sedes.organigrama.find(s => s.codigo == task.variables.codSede))
          .subscribe(sede => {

            if (!isNullOrUndefined(sede))
              this.form.get('sede').setValue(sede.nombre);
          });


        this.form.get('fechaSolicitud').setValue(moment(task.variables.fechaSolicitud).format('DD/MM/YYYY'));

        this._funcionarioService.getFuncionarioById(task.variables.idSolicitante)
          .subscribe(funcionario => {

              const nombreCompleto = `${funcionario.nombre} ${funcionario.valApellido1 ? funcionario.valApellido1 : ''} ${funcionario.valApellido2 ? funcionario.valApellido2 : ''}`

              this.form.get('solicitante').setValue(nombreCompleto)
            }
          );

        this.actualizarSolicitudesTramitadas();
      })
    );

    this.subscriptions.push(this._store.select(getSelectedDependencyGroupFuncionario).subscribe(dependencia => {

      this.seriesObservable$ = this.serieSubSerieService.getSeriePorDependencia(dependencia.codigo)
        .map(list => {

          if (isNullOrUndefined(list))
            list = [];

          list.unshift({codigoSerie: null, nombreSerie: "Seleccione"});

          return list;
        }).share();

      this.allSubSeriesObservable$ = this.instrumentoApi.getSubseries(dependencia.codigo)
        .map(list => {
          if (isNullOrUndefined(list))
            list = [];

          return list;
        }).share();
    }))


    this.subscriptions.push(afterTaskComplete.subscribe(() => {
      this._store.dispatch(go(['/' + ROUTES_PATH.workspace]));
    }));

  }

  private formInit() {
    this.form = this.fb.group({
      "sede": [{value: null, disabled: true}],
      "dependencia": [{value: null, disabled: true}],
      "solicitante": [{value: null, disabled: true}],
      "fechaSolicitud": [{value: null, disabled: true}]
    });
  }

  selectAction(evt: EventChangeActionArgs) {
    if (this.currentAction == evt.action) {
      this.currentAction = null;
      this.changeDetector.detectChanges();
    }
    this.currentAction = evt.action;
    this.solicitudSelected = {...evt.solicitud};
    this.changeDetector.detectChanges();
  }

  save() {

  }

  closePopup() {
  }

  actualizarSolicitudesTramitadas(event?) {

    this.solicitudesProcesadas$ = this._solicitudService.listarSolicitudesTramitadas({
      codSede: this.task.variables.codSede,
      codDependencia: this.task.variables.codDependencia,
      idSolicitante: this.task.variables.idSolicitante,
      idInstancia: this.task.idInstanciaProceso
    });

    this.currentAction = null;
  }


  finalizar() {

    this._taskSandbox.completeTaskDispatch({
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      idTarea: this.task.idTarea
    });


  }

  ngOnDestroy() {
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }
}
