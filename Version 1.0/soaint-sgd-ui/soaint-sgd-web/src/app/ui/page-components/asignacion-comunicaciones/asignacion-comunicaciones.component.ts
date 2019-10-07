import {Component, OnDestroy, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {Sandbox as CominicacionOficialSandbox} from '../../../infrastructure/state-management/comunicacionOficial-state/comunicacionOficialDTO-sandbox';
import {Observable} from 'rxjs/Observable';
import {Store} from '@ngrx/store';
import {State as RootState, State} from 'app/infrastructure/redux-store/redux-reducers';
import {FuncionarioDTO} from '../../../domain/funcionarioDTO';
import {FormBuilder, FormGroup} from '@angular/forms';
import {createSelector} from 'reselect';
import {getArrayData as getFuncionarioArrayData, getAuthenticatedFuncionario, getSelectedDependencyGroupFuncionario} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {getArrayData as ComunicacionesArrayData} from '../../../infrastructure/state-management/comunicacionOficial-state/comunicacionOficialDTO-selectors';
import {Sandbox as FuncionariosSandbox} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-sandbox';
import {Sandbox as AsignacionSandbox} from '../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox';
import {AsignacionDTO} from '../../../domain/AsignacionDTO';
import {ComunicacionOficialDTO} from '../../../domain/comunicacionOficialDTO';
import {Subscription} from 'rxjs/Subscription';
import {AgentDTO} from '../../../domain/agentDTO';
import {OrganigramaDTO} from '../../../domain/organigramaDTO';
import {getAgragarObservacionesDialogVisible, getDetailsDialogVisible, getJustificationDialogVisible, getRejectDialogVisible} from 'app/infrastructure/state-management/asignacionDTO-state/asignacionDTO-selectors';
import {DependenciaDTO} from '../../../domain/dependenciaDTO';
import {RedireccionDTO} from '../../../domain/redireccionDTO';
import {DroolsRedireccionarCorrespondenciaApi} from '../../../infrastructure/api/drools-redireccionar-correspondencia.api';
import {PushNotificationAction} from 'app/infrastructure/state-management/notifications-state/notifications-actions';
import {WARN_REDIRECTION} from 'app/shared/lang/es';
import {SUCCESS_REASIGNACION} from 'app/shared/lang/es';
import 'rxjs/add/operator/toArray';
import 'rxjs/add/observable/from';
import 'rxjs/add/operator/concatMap';
import 'rxjs/add/observable/forkJoin';
import {tassign} from 'tassign';
import {DevolverDTO} from '../../../domain/devolverDTO';
import {FuncionariosService} from '../../../infrastructure/api/funcionarios.service';
import {combineLatest} from 'rxjs/observable/combineLatest';
import {isNullOrUndefined} from "util";
import {loadedComunicacionesEvent} from "../../../infrastructure/state-management/comunicacionOficial-state/comunicacionOficialDTO-effects";
import {
  COMUNICACION_ASIGNADA, COMUNICACION_EXTERNA,
  COMUNICACION_SIN_ASIGNAR, DESTINATARIO_PRINCIPAL
} from "../../../shared/bussiness-properties/radicacion-properties";
import {correspondenciaEntrada} from "../../../infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-selectors";

@Component({
  selector: 'app-asignacion-comunicaciones',
  templateUrl: './asignacion-comunicaciones.component.html',
  styleUrls: ['./asignacion-comunicaciones.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class AsignarComunicacionesComponent implements OnInit, OnDestroy {

  estadoAsignacion: any;

  form: FormGroup;

  comunicaciones$: Observable<ComunicacionOficialDTO[]>;

  estadosCorrespondencia: Array<{ label: string, value: string }>;

  selectedComunications: ComunicacionOficialDTO[] = [];

  selectedFuncionarios: FuncionarioDTO[] = [];

  asignationType = 'auto';

  start_date: Date = new Date();

  end_date: Date = new Date();

  estadoCorrespondencia: any;

  subscriptions: Subscription[] = [];

  funcionariosSuggestions$: Observable<FuncionarioDTO[]>;

  funcionariosAsignar$: Observable<FuncionarioDTO[]>;

  funcionariosDependencia$: Observable<FuncionarioDTO[]>;

  justificationDialogVisible$: Observable<boolean>;

  detailsDialogVisible$: Observable<boolean>;

  agregarObservacionesDialogVisible$: Observable<boolean>;

  rejectDialogVisible$: Observable<boolean>;

  dependenciaSelected$: Observable<DependenciaDTO>;

  dependenciaSelected: DependenciaDTO;

  funcionarioSelected: FuncionarioDTO;

  funcionarioLog: FuncionarioDTO;

  funcionarios$: Observable<FuncionarioDTO[]>;

   globalDependencySubcription: Subscription;

  redireccionesFallidas: Array<AgentDTO>;

  visibility: any = {funcionario: false, colFuncionario: false};

  json = JSON;

  first = 0;

  @ViewChild('popupjustificaciones') popupjustificaciones;

  @ViewChild('popupAgregarObservaciones') popupAgregarObservaciones;

  @ViewChild('popupReject') popupReject;

  @ViewChild('detallesView') detallesView;

  authPayload: { usuario: string, pass: string } | {};
  authPayloadUnsubscriber: Subscription;

  constructor(private _store: Store<RootState>,
              private _comunicacionOficialApi: CominicacionOficialSandbox,
              private _asignacionSandbox: AsignacionSandbox,
              private _funcionarioApi: FuncionariosService,
              private ruleCheckRedirectionNumber: DroolsRedireccionarCorrespondenciaApi,
              private formBuilder: FormBuilder) {
    this.dependenciaSelected$ = this._store.select(getSelectedDependencyGroupFuncionario);

    this.comunicaciones$ = this._store.select(ComunicacionesArrayData);

     this.justificationDialogVisible$ = this._store.select(getJustificationDialogVisible);
    this.detailsDialogVisible$ = this._store.select(getDetailsDialogVisible);
    this.agregarObservacionesDialogVisible$ = this._store.select(getAgragarObservacionesDialogVisible);
    this.rejectDialogVisible$ = this._store.select(getRejectDialogVisible);
    this.start_date.setHours(this.start_date.getHours() - 24);
    this.subscriptions.push(this._store.select(getAuthenticatedFuncionario).subscribe((funcionario) => {
      this.funcionarioLog = funcionario;
    })) ;
    this.subscriptions.push( this._store.select(ComunicacionesArrayData).subscribe(() => {
      this.selectedComunications = [];
    }));

    this.subscriptions.push(this._store.select(createSelector((s: State) => s.auth.profile, (profile) => {
      return profile ? {usuario: profile.username, pass: profile.password} : {};
    })).subscribe((value) => {
      this.authPayload = value;
    }));
    this.initForm();
  }

  getCodEstadoLabel(codEstado: string): string {

    if(isNullOrUndefined(codEstado))
       return '';
    const estado = this.estadosCorrespondencia.find((item) => {
      return item.value === codEstado;
    });
    return estado.label;
  }

  findAgente(agente:AgentDTO): boolean{

    return !isNullOrUndefined(agente.indOriginal) && agente.codDependencia == this.dependenciaSelected.codigo;
  }

  ngOnInit() {
    this.funcionarios$ = this._funcionarioApi.getAllFuncionarios().share();

    this.funcionariosSuggestions$ = this.funcionarios$
      .map( funcionarios => funcionarios.filter(funcionario => funcionario.roles.some(r =>  r.rol == 'Receptor')));

    this.funcionariosAsignar$ = combineLatest(this._store.select(getAuthenticatedFuncionario), this.funcionariosSuggestions$)
      .map(([funcAuthenticated,funcionarioList]) =>  funcionarioList.filter( f => f.id != funcAuthenticated.id))

    this.funcionariosDependencia$ = combineLatest(this.funcionarios$,this._store.select(getSelectedDependencyGroupFuncionario))
                                     .map(([funcionarios,dependencia]) => funcionarios.filter( funcionario  => funcionario.dependencias.some( dep => dep.codigo == dependencia.codigo)))
                                  ;

    this.subscriptions.push(loadedComunicacionesEvent.subscribe(_ => {
      this.funcionarios$ =  this._funcionarioApi.getAllFuncionarios().share();
    } ));

    this.subscriptions.push(this.dependenciaSelected$.subscribe((result) => {
      this.dependenciaSelected = result;

      this.form.get('estadoCorrespondencia').valueChanges.subscribe(val => {


        if (val === COMUNICACION_ASIGNADA) {
          this.visibility.funcionario = true;
        } else {
          this.visibility.funcionario = false;
        }
      });

      this.listarComunicaciones();
    }));




    this.llenarEstadosCorrespondencias();
    this.listarComunicaciones();

    // this.estadoAsignacion = this.form.get('estadoCorrespondencia');
  }

  ngOnDestroy() {
     this.subscriptions.forEach( s => s.unsubscribe());
  }

  initForm() {
    this.form = this.formBuilder.group({
      'funcionario': [null],
      'estadoCorrespondencia': [null],
      'anio': [null],
      'nroRadicado': [null],
      'consecutivo': [null]
    });
  }

  llenarEstadosCorrespondencias() {
    this.estadosCorrespondencia = [
      {
        label: 'SIN ASIGNAR',
        value:COMUNICACION_SIN_ASIGNAR
      },
      {
        label: 'ASIGNADA',
        value: COMUNICACION_ASIGNADA
      }
    ];
    this.form.get('estadoCorrespondencia').setValue(this.estadosCorrespondencia[0].value);
  }

  convertDate(inputFormat) {
    function pad(s) {
      return (s < 10) ? '0' + s : s;
    }

    const d = new Date(inputFormat);
    return [pad(d.getFullYear()), pad(d.getMonth() + 1), d.getDate()].join('-');
  }

  assignComunications() {

    if(this.selectedFuncionarios.length > this.selectedComunications.length) {

      this._store.dispatch(new PushNotificationAction({
        severity: 'error',
        summary: 'La cantidad de registros seleccionados es menor a la cantidad de funcionarios para realizar la asignación.'
      }));

      return;
    }

    this._asignacionSandbox.assignDispatch({
      asignaciones: {
        asignaciones: this.asignationType === 'auto' ? this.createAsignacionesAuto() : this.createAsignaciones()
      },
      traza: {
        ideFunci: this.funcionarioLog.id
      }
    });
  }

  reassignComunications() {
    this._asignacionSandbox.reassignDispatch(tassign({
      asignaciones: {
        asignaciones: this.asignationType === 'auto' ? this.createAsignacionesAuto() : this.createAsignaciones()
      },
      idFunc: this.funcionarioLog.id
    }, this.authPayload));

    this.selectedComunications.forEach((value, index) => {

      const nroRadicadoParts =  value.correspondencia.nroRadicado.split('--');
      const  niceRadicado = nroRadicadoParts.length == 2 ? nroRadicadoParts[1]: value.correspondencia.nroRadicado;
      this._store.dispatch(new PushNotificationAction({
        severity: 'success',
        summary: SUCCESS_REASIGNACION +niceRadicado
      }));

    });

  }

  redirectComunications(justificationValues: { justificacion: string, sedeAdministrativa: OrganigramaDTO, dependenciaGrupo: OrganigramaDTO }) {

    this.checkRedirectionsAgentes('numRedirecciones', justificationValues).subscribe(checks => {

      const failChecks = [];
      const agentesSuccess = [];

      checks.forEach(value => {
        console.log(value);
        if (!value.success) {
          failChecks.push(value.agente);
        } else {
           agentesSuccess.push(value.agente);
        }
      });

      if (failChecks.length > 0) {

        //this._store.dispatch(new PushNotificationAction({
        //  severity: 'warn',
        //  summary: WARN_REDIRECTION
        //}));

        this.redireccionesFallidas = failChecks;

        const selectedComunications = this.selectedComunications.filter((com) => {
          const agente = com.agenteList.find(ag => this.findAgente(ag));
          const index = this.redireccionesFallidas.findIndex((red) => red.ideAgente === agente.ideAgente);
          return index >= 0;
        });

        this.rejectComunicationsAction(selectedComunications, null, '3', 'Ha vencido el numero maximo de redirecciones');

      } else {
        this._asignacionSandbox.redirectDispatch(this.createRecursiveRedirectsPayload(agentesSuccess, justificationValues));
      }
    });

  }

  sendRedirect() {
    this.popupjustificaciones.redirectComunications();
  }

  processComunications() {
    this._asignacionSandbox.assignDispatch({
      asignaciones: {
        asignaciones: this.createAsignaciones(this.funcionarioLog.id, this.funcionarioLog.loginName),
      },
      traza: {
        ideFunci: this.funcionarioLog.id
      }
    });
  }

  showRedirectDialog() {

    const form = this.popupjustificaciones.form;

    this.popupjustificaciones.dependenciaGrupoSuggestions$ = Observable.of([]);

      form.get("sedeAdministrativa").enable();
      form.get("dependenciaGrupo").enable();
      form.reset();

    this._asignacionSandbox.setVisibleJustificationDialogDispatch(true);
  }

  hideJustificationPopup() {
    this._asignacionSandbox.setVisibleJustificationDialogDispatch(false);
  }


  showDetailsDialog(nroRadicado: string): void {
    this.detallesView.setNroRadicado(nroRadicado);
    this.detallesView.loadComunication();
  }

  hideDetailsDialog() {
    this._asignacionSandbox.setVisibleDetailsDialogDispatch(false);
  }

  showAddObservationsDialog(idDocuemento: number) {
    this.popupAgregarObservaciones.form.reset();
    this.popupAgregarObservaciones.setData({
      idDocumento: idDocuemento,
      idFuncionario: this.funcionarioLog.id,
      codDependencia: this.dependenciaSelected.codigo
    });
    this.popupAgregarObservaciones.loadObservations();
    this._asignacionSandbox.setVisibleAddObservationsDialogDispatch(true);
  }
  hideAddObservationsPopup() {
    this._asignacionSandbox.setVisibleAddObservationsDialogDispatch(false);
  }

  showRejectDialog() {
    this.popupReject.listCauseReturn(this.selectedComunications[0].correspondencia.reqDigita);

    this.popupReject.form.reset();
    this._asignacionSandbox.setVisibleRejectDialogDispatch(true);
  }

  hideRejectDialog() {
    this._asignacionSandbox.setVisibleRejectDialogDispatch(false);
  }

  createAsignacionesAuto(): any {
    const asignaciones: AsignacionDTO[] = [];
    let funcIndex = 0;
    this.selectedComunications.forEach((value, index) => {
      console.log(value);
      if (!this.selectedFuncionarios[funcIndex]) {
        funcIndex = 0;
      }

      const agentInt = value.agenteList.find(agent => this.findAgente(agent));

      console.log(this.selectedFuncionarios[funcIndex]);

      asignaciones.push({
        ideAsignacion: null,
        fecAsignacion: null,
        ideFunci: this.selectedFuncionarios[funcIndex].id,
        codDependencia: agentInt.codDependencia,
        codTipAsignacion: 'TA',
        observaciones: null,
        codTipCausal: null,
        codTipProceso: null,
        ideAsigUltimo: null,
        nivLectura: null,
        nivEscritura: null,
        fechaVencimiento: null,
        alertaVencimiento: null,
        idInstancia: null,
        ideAgente: agentInt.ideAgente,
        ideDocumento: value.correspondencia.ideDocumento,
        nroRadicado: value.correspondencia.nroRadicado,
        fecRadicado: value.correspondencia.fecRadicado,
        loginName: this.selectedFuncionarios[funcIndex].loginName
      });
      funcIndex++;
    });
    console.log(asignaciones);
    return asignaciones
  }

  createAsignaciones(idFuncionario?, loginNameFuncionario?): AsignacionDTO[] {
    const asignaciones: AsignacionDTO[] = [];
    this.selectedComunications.forEach((value) => {

      const agentInt =  value.agenteList.find( agente => this.findAgente(agente));


      asignaciones.push({
        ideAsignacion: null,
        fecAsignacion: null,
        alertaVencimiento: null,
        ideFunci: idFuncionario || this.funcionarioSelected.id,
        codDependencia: !isNullOrUndefined(agentInt)? agentInt.codDependencia : null,
        codTipAsignacion: 'TA',
        observaciones: null,
        codTipCausal: null,
        codTipProceso: null,
        ideAsigUltimo: null,
        nivLectura: null,
        nivEscritura: null,
        fechaVencimiento: null,
        idInstancia: null,
        ideAgente:  !isNullOrUndefined(agentInt)? agentInt.ideAgente : null,
        ideDocumento: value.correspondencia.ideDocumento,
        nroRadicado: value.correspondencia.nroRadicado,
        fecRadicado: value.correspondencia.fecRadicado,
        loginName: loginNameFuncionario || this.funcionarioSelected.loginName
      })
    });
    return asignaciones;
  }

  createAgentes(justificationValues: { justificacion: string, sedeAdministrativa: OrganigramaDTO, dependenciaGrupo: OrganigramaDTO }): AgentDTO[] {
    const agentes: AgentDTO[] = [];
    this.selectedComunications.forEach((value) => {
      const agente = value.agenteList.find(ag => this.findAgente(ag));
      if(!isNullOrUndefined(agente)) {
        agente.codSede = justificationValues.sedeAdministrativa.codigo;
        agente.codDependencia = justificationValues.dependenciaGrupo.codigo;
        delete agente['_$visited'];
      }
      agentes.push(agente)
    });
    return agentes;
  }

  checkRedirectionsAgentes(key, justification): Observable<any[]> {

    const failChecks = [];
    const successChecks = [];

    return Observable.from(this.selectedComunications)
      .flatMap(value => {
        const agente = value.agenteList.find(ag => this.findAgente(ag));
        if(!isNullOrUndefined(agente)) {
          agente.codSede = justification.sedeAdministrativa.codigo;
          agente.codDependencia = justification.dependenciaGrupo.codigo;
          agente.nombre = value.correspondencia.descripcion;
          delete agente['_$visited'];
        }
        return this.ruleCheckRedirectionNumber.check(agente[key]).map(ruleCheck => {
          return {
            success: ruleCheck,
            agente: agente
          };
        });
      }).toArray();
  }

  checkDevolucionesAgentes(key): Observable<any[]> {
    return Observable.from(this.comunicacionesEntradaSelected )
      .flatMap(value => {
        const agente = value.agenteList.find(ag => this.findAgente(ag));
        if(!isNullOrUndefined(agente))
        delete agente['_$visited'];
        return this.ruleCheckRedirectionNumber.check(agente[key]).map(ruleCheck => {
          return {
            success: ruleCheck,
            radicacion: value,
            agente: agente
          };
        });
      }).toArray();
  }

  createRecursiveRedirectsPayload(agentes, justification): RedireccionDTO {
  return {
      agentes: [...agentes.map( a => { a.codDependencia = justification.dependenciaGrupo; return a;})],
      traza: {
        id: null,
        fecha: null,
        observacion: justification.justificacion,
        ideFunci: this.funcionarioLog.id,
        estado: null,
        ideDocumento: null,
        codDependencia: this.dependenciaSelected.codigo
      }
    };


  }

  hideAndCleanDevolucionesFallidasDialog() {
    this.redireccionesFallidas = null;
  }

  get formValid(){

    return !isNullOrUndefined(this.start_date) && !isNullOrUndefined(this.end_date) && this.form.valid;
  }


  listarComunicaciones() {
    let numRadicado = '';
    if (this.form.get('anio').value && this.form.get('radicado').value && this.form.get('consecutivo').value) {
      numRadicado = `${this.form.get('anio').value}-${this.form.get('radicado').value}-${this.form.get('consecutivo').value}`;
    }
    this.estadoAsignacion = this.form.get('estadoCorrespondencia').value;

    if (this.form.get('estadoCorrespondencia').value === COMUNICACION_ASIGNADA) {
      this.visibility.colFuncionario = true;
    } else {
      this.visibility.colFuncionario = false;
    }

    const payload: any = {
      fecha_ini: this.convertDate(this.start_date),
      fecha_fin: this.convertDate(this.end_date),
      cod_estado: this.form.get('estadoCorrespondencia').value,
      nro_radicado: numRadicado ? numRadicado : ''
    };

    if(payload.cod_estado == COMUNICACION_ASIGNADA && !isNullOrUndefined( this.form.get('funcionario').value))
      payload.id_funcionario =  this.form.get('funcionario').value;

    this.first = 0;
    this._comunicacionOficialApi.loadDispatch(payload);
  }


  rejectComunications($event) {
    this.checkDevolucionesAgentes('numDevoluciones').subscribe(checks => {
      const failChecks = [];
      const agentesSuccess = [];

      checks.forEach(value => {
        if (!value.success) {
          failChecks.push(value.agente);
        } else {
          agentesSuccess.push(value.agente);
        }
      });
      if (failChecks.length > 0) {
        this.redireccionesFallidas = failChecks;
      } else {
        this.rejectComunicationsAction(this.comunicacionesEntradaSelected, $event);
      }
    });
  }

  rejectComunicationsAction(selectedComunications, payload, cause?: string, observation?: string) {
    console.log('selectedComunications');
    console.log(selectedComunications);
    console.log(payload);
    this._asignacionSandbox.rejectComunicationsAsignacion(this.rejectPayload(selectedComunications, payload, cause, observation)).subscribe(result => {
      this.listarComunicaciones();
      this.hideRejectDialog();
      this.hideJustificationPopup();
    });
  }

  rejectPayload(agentes, payload, cause?: string, observation?: string): DevolverDTO {
    return {
      itemsDevolucion: this.getItemsDevolucion(agentes, payload, cause),
      traza: {
        id: null,
        observacion: observation || payload.observacion,
        ideFunci: this.funcionarioLog.id,
        codDependencia: this.dependenciaSelected.codigo,
        estado: null,
        fecha: new Date().getTime().toString(),
        ideDocumento: agentes[0].correspondencia.ideDocumento
      }
    };
  }

  getItemsDevolucion(agentes: any[], payload, cause?: string): any[] {
    const items = [];
    agentes.forEach(ag => {
      const a = ag.agenteList.find(agente => this.findAgente(agente));
      if(!isNullOrUndefined(a)) {
        a.nroDocuIdentidad = ag.correspondencia.nroRadicado;
        a.codDependencia = this.dependenciaSelected.codigo;
        delete a._$visited;
      }
      delete ag.correspondencia['_$visited'];
      items.push({
        agente: a,
        causalDevolucion: cause || payload.causalDevolucion.id,
        funDevuelve: this.funcionarioLog.loginName,
        correspondencia: ag.correspondencia
      });
    });
    return items;
  }

  sendReject() {
    this.popupReject.devolverComunicaciones();
  }

  devolverOrigenRedireccionFallida() {

  }

  sortByFuncionario(){}

  get comunicacionesEntradaSelected() {
   return this.selectedComunications.filter( comunicacion => comunicacion.correspondencia.codTipoCmc == COMUNICACION_EXTERNA)
  }

  getDestinatarioPrincipal(agenteList:AgentDTO[]){

    return agenteList.find(agente => agente.indOriginal == DESTINATARIO_PRINCIPAL);
  }
}

