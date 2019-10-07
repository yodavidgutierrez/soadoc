import { Component, OnInit, ViewChild } from '@angular/core';
import {FormBuilder, FormGroup, Validators, FormControl} from '@angular/forms';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {Sandbox as ConstanteSandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {Subscription} from 'rxjs/Subscription';
import {Observable} from 'rxjs/Observable';
import {TareaDTO} from '../../../domain/tareaDTO';
import {Sandbox as RadicarComunicacionesSandBox} from 'app/infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-sandbox';
import {Sandbox as TaskSandBox} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import {getActiveTask} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {Sandbox as AsiganacionDTOSandbox} from '../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox';
import {ComunicacionOficialDTO} from '../../../domain/comunicacionOficialDTO';
import {RadicacionEntradaDTV} from '../../../shared/data-transformers/radicacionEntradaDTV';
import {FuncionarioDTO} from '../../../domain/funcionarioDTO';
import {getArrayData as getFuncionarioArrayData, getAuthenticatedFuncionario, getSelectedDependencyGroupFuncionario} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {DependenciaDTO} from '../../../domain/dependenciaDTO';
import {ROUTES_PATH} from '../../../app.route-names';
import {go} from '@ngrx/router-store';
import {Sandbox as DependenciaSandbox} from '../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {afterTaskComplete} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-reducers';
import {CorrespondenciaApiService} from '../../../infrastructure/api/correspondencia.api';
import {
  DESTINATARIO_PRINCIPAL,
  TIPO_REMITENTE_INTERNO
} from '../../../shared/bussiness-properties/radicacion-properties';
import {isNullOrUndefined} from 'util';

@Component({
  selector: 'app-gestionar-devoluciones',
  templateUrl: './gestionar-devoluciones.component.html',
  styleUrls: ['./gestionar-devoluciones.component.scss'],
})
export class GestionarDevolucionesComponent implements OnInit {

  @ViewChild('popupAgregarObservaciones') popupAgregarObservaciones;

  causalDevolucion: any;
  usuariodevuelve: any;
  sedeAdministrativa: any;
  dependencia: any;

  causalText: String;

  sedeCode: String;
  dependenciaCode: String;

  dependencias: DependenciaDTO[] = [];
  disabledDevolucionRechazar: Boolean = false;
  disabledDevolucionAction: Boolean = false;
  hasDocumnts: Boolean = false;

  comunicacion: ComunicacionOficialDTO = {};

  afterTaskCompleteSubscriptor: Subscription;

  task: TareaDTO;
  activeTaskUnsubscriber: Subscription;


  form = new FormGroup({
    causalDevolucion: new FormControl(),
    usuariodevuelve: new FormControl(),
    sedeAdministrativa: new FormControl(),
    dependencia: new FormControl(),
    observacion: new FormControl(),
  });

  constructor(
    private _store: Store<State>, private _dependenciaSandbox: DependenciaSandbox ,
    private _sandbox: RadicarComunicacionesSandBox,
    private _constSandbox: ConstanteSandbox,
    private _taskSandBox: TaskSandBox,
    private formBuilder: FormBuilder,
    private _asiganacionSandbox: AsiganacionDTOSandbox,
    private _correspondenciaApi: CorrespondenciaApiService
    ) {
     this.initForm();
  }

  ngOnInit() {

    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {
      this.task = activeTask;
      this.restore();
    });

    this.verificaVisibilidadRechazar();

    this.afterTaskCompleteSubscriptor =  afterTaskComplete.subscribe( t => this._store.dispatch(go(['/' + ROUTES_PATH.workspace])));

  }

  initForm() {
    this.form = this.formBuilder.group({
      'nroRadicado': [null],
      'causalDevolucion': [null],
      'usuariodevuelve': [null],
      'sedeAdministrativa': [null],
      'dependencia': [null],
      'requiereDigitalizacion': [1]
    });
  }

  restore() {
    if (this.task) {
      console.log(this.task);
      if ('1' === this.task.variables.causalD) {
        this.causalText = 'Calidad Imagen';
      } else if ('2' === this.task.variables.causalD) {
        this.causalText = 'Datos incorrectos';
      } else if ('3' === this.task.variables.causalD) {
        this.causalText = 'Supera los intentos permitidos de Redireccionamiento';
      }

      this.causalDevolucion =  this.causalText;
      this.usuariodevuelve = this.task.variables.funDevuelve;

      this._asiganacionSandbox.obtenerComunicacionPorNroRadicado(this.task.variables.numeroRadicado).subscribe((result) => {

        this.comunicacion = result;

        const ag = this.comunicacion.agenteList.find( agente => agente.codTipoRemite == TIPO_REMITENTE_INTERNO && agente.indOriginal == DESTINATARIO_PRINCIPAL);

        if (!isNullOrUndefined(ag)) {
          this.dependenciaCode = ag.codDependencia;
           this.sedeCode =  ag.codSede;

          this._dependenciaSandbox.loadDependencies({}).subscribe((results) => {

            this.dependencias = results.dependencias;

            const objDependencia  = results.dependencias.find((element) => element.codigo === this.dependenciaCode);
            const objSede  = results.dependencias.find((element) => element.codigo === this.sedeCode);

            this.sedeAdministrativa = objSede ? objSede.nombre : '';
            this.dependencia = objDependencia ? objDependencia.nombre : '';

            this.form.get('dependencia').setValue(objDependencia ? objDependencia.nombre : '');
            this.form.get('sedeAdministrativa').setValue(objSede ? objSede.nombre : '');

          });
        }

        if (this.comunicacion) {

            this.hasDocumnts = (this.comunicacion.ppdDocumentoList[0].ideEcm) ? true : false;
            this.popupAgregarObservaciones.form.reset();
            this.popupAgregarObservaciones.setData({
              idDocumento: this.comunicacion.correspondencia.ideDocumento,
              idFuncionario: this.comunicacion.correspondencia.codFuncRadica,
              codOrg: this.comunicacion.correspondencia.codDependencia,
              isPopup: false
            });
            this.popupAgregarObservaciones.loadObservations();

          this._correspondenciaApi.actualizarInstanciaGestionDevoluciones({
            nroRadicado: this.comunicacion.correspondencia.nroRadicado,
            ideInstancia : this.task.idInstanciaProceso
          }).subscribe();
        }


      });
    }
  }

  handleCountObservaciones(count) {
    if (count == 0) {
      this.disabledDevolucionRechazar = true;
      this.disabledDevolucionAction = true;
    } else {
      this.disabledDevolucionRechazar = false;
      this.disabledDevolucionAction = false;
    }
    this.verificaVisibilidadRechazar();
  }

  verificaVisibilidadRechazar() {
    if ('3' == this.task.variables.causalD) {
      this.disabledDevolucionRechazar = true;
    }
  }

  rechazarDevolucion() {

    this._taskSandBox.completeTaskDispatch({
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      idTarea: this.task.idTarea,
      parametros: {
        devolucion: 1,
        requiereDigitalizacion: this.causalDevolucion == 'Calidad Imagen' ?  this.form.value.requiereDigitalizacion : 2,
      }
    });

    // this._store.dispatch(go(['/' + ROUTES_PATH.workspace]));
  }

  gestionarDevolucion() {

    this._taskSandBox.completeTaskDispatch({
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      idTarea: this.task.idTarea,
      parametros: {
        devolucion: 2,
        requiereDigitalizacion:  this.causalDevolucion == 'Calidad Imagen' ?  this.form.value.requiereDigitalizacion : 2,
      }
    });
    // this._store.dispatch(go(['/' + ROUTES_PATH.workspace]));
  }

  ngOnDestroy() {
    this.activeTaskUnsubscriber.unsubscribe();
    this.afterTaskCompleteSubscriptor.unsubscribe();
  }

}
