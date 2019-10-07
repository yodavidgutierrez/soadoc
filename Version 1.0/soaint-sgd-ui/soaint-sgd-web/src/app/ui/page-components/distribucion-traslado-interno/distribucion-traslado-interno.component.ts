import {ChangeDetectorRef, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Observable, Subscription} from "rxjs";
import {ComunicacionOficialDTO} from "../../../domain/comunicacionOficialDTO";
import {PlanillaDTO} from "../../../domain/PlanillaDTO";
import {FuncionarioDTO} from "../../../domain/funcionarioDTO";
import {DependenciaDTO} from "../../../domain/dependenciaDTO";
import {ConstanteDTO} from "../../../domain/constanteDTO";
import {Store} from "@ngrx/store";
import {State as RootState} from "../../../infrastructure/redux-store/redux-reducers";
import {Sandbox as DistribucionFisicaSandbox} from "../../../infrastructure/state-management/distrubucionFisicaDTO-state/distrubucionFisicaDTO-sandbox";
import {Sandbox as FuncionarioSandBox} from "../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-sandbox";
import {Sandbox as ConstanteSandbox} from "../../../infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox";
import {Sandbox as DependenciaSandbox} from "../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox";
import {PlanillasApiService} from "../../../infrastructure/api/planillas.api";
import {Sandbox as ProcessSandbox} from "../../../infrastructure/state-management/procesoDTO-state/procesoDTO-sandbox";
import {
  getArrayData as getFuncionarioArrayData, getAuthenticatedFuncionario,
  getSelectedDependencyGroupFuncionario
} from "../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {getArrayData as DistribucionArrayData} from "../../../infrastructure/state-management/distrubucionFisicaDTO-state/distrubucionFisicaDTO-selectors";
import {getTipologiaDocumentalArrayData} from "../../../infrastructure/state-management/constanteDTO-state/selectors/tipologia-documental-selectors";
import {AgentDTO} from "../../../domain/agentDTO";
import {RadicacionEntradaDTV} from "../../../shared/data-transformers/radicacionEntradaDTV";
import {DocumentoDTO} from "../../../domain/documentoDTO";
import {VALIDATION_MESSAGES} from "../../../shared/validation-messages";
import {PlanAgenDTO} from "../../../domain/PlanAgenDTO";
import {PlanAgentesDTO} from "../../../domain/PlanAgentesDTO";
import {go} from "@ngrx/router-store";
import {ROUTES_PATH} from "../../../app.route-names";
import {trasladoInternoApiService} from "../../../infrastructure/api/traslado-interno.api";
import {ComunicacionOficialSalidaFullDTO} from "../../../domain/comunicacionOficialSalidaFullDTO";
import {CorrespondenciaDTO} from "../../../domain/correspondenciaDTO";
import {getArrayData as ComunicacionesArrayData} from '../../../infrastructure/state-management/comunicacionOficial-state/comunicacionOficialDTO-selectors';

@Component({
  selector: 'app-distribucion-traslado-interno',
  templateUrl: './distribucion-traslado-interno.component.html',
  styleUrls: ['./distribucion-traslado-interno.component.css'],
  providers: [trasladoInternoApiService]
})
export class DistribucionTrasladoInternoComponent implements OnInit, OnDestroy {

  form: FormGroup;

  first = 0;

  comunicaciones$: Observable<ComunicacionOficialDTO[]>;

  actualizarComunicaciones$: Observable<CorrespondenciaDTO[]>;

  listadoComunicaciones: ComunicacionOficialDTO[] = [];

  selectedComunications: ComunicacionOficialSalidaFullDTO[] = [];

  start_date: Date = new Date();

  end_date: Date = new Date();

  dependencia: any;

  planillaGenerada: PlanillaDTO;

  numeroPlanillaDialogVisible: boolean = false;

  funcionariosSuggestions$: Observable<FuncionarioDTO[]>;

  dependenciaSelected$: Observable<DependenciaDTO>;

  dependenciaSelected: DependenciaDTO;

  funcionarioLog: FuncionarioDTO;

  funcionarioSubcription: Subscription;

  comunicacionesSubcription: Subscription;

  tipologiaDocumentalSuggestions$: Observable<ConstanteDTO[]>;

  tipologiasDocumentales: ConstanteDTO[];

  dependencias: DependenciaDTO[] = [];

  comunicacionSeleccionada: ComunicacionOficialSalidaFullDTO;

  validations: any = {};

  @ViewChild('popupjustificaciones') popupjustificaciones;

  @ViewChild('popupAgregarObservaciones') popupAgregarObservaciones;

  @ViewChild('popupReject') popupReject;

  @ViewChild('detallesView') detallesView;


  @ViewChild('popUpPlanillaGenerada') popUpPlanillaGenerada;

  downloadName: string;

  constructor(protected _store: Store<RootState>,
              protected _distribucionFisicaApi: DistribucionFisicaSandbox,
              protected _funcionarioSandbox: FuncionarioSandBox,
              protected _constSandbox: ConstanteSandbox,
              protected _dependenciaSandbox: DependenciaSandbox,
              protected _planillaService: PlanillasApiService,
              protected _processSandbox: ProcessSandbox,
              protected trasladoInternoApiService: trasladoInternoApiService,
              private _detectChanges: ChangeDetectorRef,
              protected formBuilder: FormBuilder) {
    this.dependenciaSelected$ = this._store.select(getSelectedDependencyGroupFuncionario);
    this.dependenciaSelected$.subscribe((result) => {
      this.dependenciaSelected = result;
    });
    this.comunicaciones$ = this._store.select(ComunicacionesArrayData);
    this.funcionariosSuggestions$ = this._store.select(getFuncionarioArrayData);
    this.start_date.setHours(this.start_date.getHours() - 24);
    this.funcionarioSubcription = this._store.select(getAuthenticatedFuncionario).subscribe((funcionario) => {
      this.funcionarioLog = funcionario;
    });
    this.comunicacionesSubcription = this._store.select(DistribucionArrayData).subscribe(() => {
      this.selectedComunications = [];
    });
    this.initForm();
  }

  ngOnInit() {
    this.tipologiaDocumentalSuggestions$ = this._store.select(getTipologiaDocumentalArrayData);
    this.tipologiaDocumentalSuggestions$.subscribe((results) => {
      this.tipologiasDocumentales = results;
    });
    this._funcionarioSandbox.loadAllFuncionariosDispatch();
    this._constSandbox.loadDatosGeneralesDispatch();
    this.listarDependencias();
  }

  getDatosRemitente(comunicacion): Observable<AgentDTO> {
    const radicacionEntradaDTV = new RadicacionEntradaDTV(comunicacion);
    return radicacionEntradaDTV.getDatosRemitente();
  }

  getDatosDestinatario(comunicacion): Observable<AgentDTO[]> {
    const radicacionEntradaDTV = new RadicacionEntradaDTV(comunicacion);
    return radicacionEntradaDTV.getDatosDestinatarios();

  }

  getDatosDestinatarioInmediate(comunicacion): AgentDTO[] {
    const radicacionEntradaDTV = new RadicacionEntradaDTV(comunicacion);
    return radicacionEntradaDTV.getDatosDestinatarioInmediate();
  }

  getDatosDocumentos(comunicacion): Observable<DocumentoDTO[]> {
    const radicacionEntradaDTV = new RadicacionEntradaDTV(comunicacion);
    return radicacionEntradaDTV.getDatosDocumento();
  }

  ngOnDestroy() {
    this.funcionarioSubcription.unsubscribe();
    this.comunicacionesSubcription.unsubscribe();
    this._distribucionFisicaApi.clearDispatch();
  }

  initForm() {
    this.form = this.formBuilder.group({
      'funcionario': [null],
      'dependencia': [null],
      'nroRadicado': [null],
      'tipologia': [null],
      'fechaInicial': [null, [Validators.required]],
      'fechaFinal': [null, [Validators.required]]
    });
  }

  OnBlurEvents(control: string) {
    this.SetValidationMessages(control);
  }

  SetValidationMessages(control: string) {
    const formControl = this.form.get(control);
    if (formControl.touched && formControl.invalid) {
      const error_keys = Object.keys(formControl.errors);
      const last_error_key = error_keys[error_keys.length - 1];
      this.validations[control] = VALIDATION_MESSAGES[last_error_key];
    } else {
      this.validations[control] = '';
    }
  }

  listarDependencias() {
    this._dependenciaSandbox.loadDependencies({}).subscribe((results) => {
      this.dependencias = results.dependencias;
      // this.listarDistribuciones();
    });
  }

  convertDate(inputFormat) {
    function pad(s) {
      return (s < 10) ? '0' + s : s;
    }

    const d = new Date(inputFormat);
    return [pad(d.getFullYear()), pad(d.getMonth() + 1), d.getDate()].join('-');
  }

  findTipoDoc(code): string {
    return this.tipologiasDocumentales.find((element) => element.codigo == code).nombre;
  }

  findDependency(code): string {
    const result = this.dependencias.find((element) => element.codigo == code);
    return result ? result.nombre : '';
  }

  findSede(code): string {
    const result = this.dependencias.find((element) => element.codSede == code);
    return result ? result.nomSede : '';
  }

  listarDistribuciones() {

    this._distribucionFisicaApi.loadDispatch({
      fecha_ini: this.convertDate(this.start_date),
      fecha_fin: this.convertDate(this.end_date),
      cod_dependencia: this.form.get('dependencia').value ? this.form.get('dependencia').value.codigo : null,
      cod_tipologia_documental: this.form.get('tipologia').value ? this.form.get('tipologia').value.codigo : null,
      nro_radicado: this.form.get('nroRadicado').value ,
    });
  }

  generarDatosExportar(): PlanillaDTO {
    const agensDTO: PlanAgenDTO[] = [];

    this.selectedComunications.forEach((element: ComunicacionOficialSalidaFullDTO) => {
      const agenDTO: PlanAgenDTO = {
        idePlanAgen: null,
        estado: null,
        varPeso: element.peso,
        varValor: element.valorEnvio,
        varNumeroGuia: element.nroGuia,
        fecObservacion: null,
        codNuevaSede: null,
        codNuevaDepen: null,
        observaciones: null,
        usuario: null,
        codCauDevo: null,
        fecCarguePla: new Date().getTime(),
        ideAgente: element.idAgente,
        ideDocumento: element.idDocumento,
        nroRadicado: element.nroRadicado,
        tipoPersona: null,
        tipologiaDocumental: null,
        nit: null,
        nroDocuIdentidad: element.nroIdentificacion,
        nombre: element.nombre,
        razonSocial: null,
        folios: element.folios,
        anexos: element.anexos
      };
      delete agenDTO.usuario;
      agensDTO.push(agenDTO);
    });

    const agentes: PlanAgentesDTO = {
      pagente: agensDTO
    };

    const dependencia =  this.form.get('dependencia').value;

    const planilla: PlanillaDTO = {
      idePlanilla: null,
      nroPlanilla: null,
      fecGeneracion: null,
      codTipoPlanilla: null,
      codFuncGenera: this.funcionarioLog.id.toString(),
      codSedeOrigen: this.dependenciaSelected.codSede,
      codDependenciaOrigen: this.dependenciaSelected.codigo,
      codSedeDestino: this.form.get('dependencia').value.codSede,
      codDependenciaDestino: this.form.get('dependencia').value.codigo,
      codClaseEnvio: null,
      codModalidadEnvio: null,
      pagentes: agentes,
      ideEcm: null

    };

    return planilla;
  };

  generarPlanilla() {

    const dependenciaDestinoArray= [];
    const sedeDestinoArray= [];
    const planilla = this.generarDatosExportar();

    this._planillaService.generarPlanillaInterna(planilla).subscribe((result) => {

      this.selectedComunications.forEach((element) => {
        dependenciaDestinoArray.push(element.dependencia);
        // sedeDestinoArray.push(element.codSede);
      });
      this.popUpPlanillaGenerada.setDependenciaDestino(this.dependenciaSelected.nombre);
      this.popUpPlanillaGenerada.setSedeDestino(this.dependenciaSelected.nomSede);
      this.planillaGenerada = result;
      this.numeroPlanillaDialogVisible = true;
      this.listarDistribuciones();
      this._detectChanges.detectChanges();
    });
  }

  exportarPlanilla(formato) {
    this._planillaService.exportarPlanillaInterna({
        nroPlanilla: this.planillaGenerada.nroPlanilla,
        formato: formato
      }
    ).subscribe((result) => {

      const pdf = 'data:application/octet-stream;base64,' + result.base64EncodedFile;
      const dlnk: any = document.getElementById('dwnldLnk');
      dlnk.href = pdf;
      dlnk.download = 'planilla.' + formato.toLowerCase();
      dlnk.click();

      // window.open('data:application/pdf;base64,' + atob(result.base64EncodedFile));
    });
  }

  hideNumeroPlanillaDialog() {
    this.numeroPlanillaDialogVisible = false;
  }

  Finalizar(): void {
    this._store.dispatch(go(['/' + ROUTES_PATH.workspace]));
  }

  GetPayload(): any {
    const payload: any = {
      fecha_ini: this.convertDate(this.start_date),
      fecha_fin: this.convertDate(this.end_date),
    };

    if (this.form.get('dependencia').value != null){
      payload.cod_dep = this.form.get('dependencia').value.codigo;
    }

    //payload.tipologia = this.form.get('tipologia').value.codigo;
    if(this.form.get('nroRadicado').value != null){
      payload.numeroRadicado = this.form.get('nroRadicado').value;
    }


    //payload.startDate = this.form.get('fechaInicial').value;
    //payload.endDate = this.form.get('fechaFinal').value;
    return payload;
  }


  listarTrasladoInterno() {
    this.first = 0;
    this.comunicaciones$ = this.trasladoInternoApiService.listarComunicaciones(this.GetPayload());
    this.comunicaciones$.subscribe(response => {
      this.listadoComunicaciones = [...response];
      this.selectedComunications = [];
      this._detectChanges.detectChanges();
    });
  }

  actualizarEnvioCorrespondencia() {

    /*this.trasladoInternoApiService.actualizarEnvioInterno(this.selectedComunications.map(co => {
      return {nroRadicado: co.nroRadicado, distribuido: co.distribuido = 1}
    })).subscribe();*/
    this.generarPlanilla();
    this.listarTrasladoInterno();

  }
}
