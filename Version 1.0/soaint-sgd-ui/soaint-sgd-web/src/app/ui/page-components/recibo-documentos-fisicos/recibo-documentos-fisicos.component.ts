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
import {CorrespondenciaApiService} from "../../../infrastructure/api/correspondencia.api";
import {isNullOrUndefined} from "util";
import {PushNotificationAction} from "../../../infrastructure/state-management/notifications-state/notifications-actions";
import {SOPORTE_FISICO} from "../../../shared/bussiness-properties/radicacion-properties";
import {getTipoAnexosArrayData} from "../../../infrastructure/state-management/constanteDTO-state/selectors/tipo-anexos-selectors";
import {getTipoComunicacionArrayData} from "../../../infrastructure/state-management/constanteDTO-state/selectors/tipo-comunicacion-selectors";

@Component({
  selector: 'app-recibo-documentos-fisicos',
  templateUrl: './recibo-documentos-fisicos.component.html',
  styleUrls: ['./recibo-documentos-fisicos.component.css']
})
export class ReciboDocumentosFisicosComponent implements OnInit, OnDestroy {

  form: FormGroup;

  first = 0;

  comunicaciones$: Observable<ComunicacionOficialDTO[]>;

  selectedComunications: any[];

  start_date: Date = new Date();

  end_date: Date = new Date();

  dependencia: any;

 anexoDialogVisibility = false;

  validations: any = {};

  dependencias:DependenciaDTO[];

  itemForView:any;

  tiposAnexos$: Observable<ConstanteDTO[]>;

  tiposComunicacion$: Observable<ConstanteDTO[]>;

   downloadName: string;

  constructor(protected _store: Store<RootState>,
              protected correspondenciaApi: CorrespondenciaApiService,
              protected _distribucionFisicaApi: DistribucionFisicaSandbox,
              protected _funcionarioSandbox: FuncionarioSandBox,
              protected _constSandbox: ConstanteSandbox,
              protected _dependenciaSandbox: DependenciaSandbox,
              protected _changeDetector: ChangeDetectorRef,
              protected _processSandbox: ProcessSandbox,
              protected formBuilder: FormBuilder) {



    this.start_date.setHours(this.start_date.getHours() - 24);

    this.initForm();
  }

  ngOnInit() {
    this.listarDependencias();

    this.tiposAnexos$ = this._store.select(getTipoAnexosArrayData).share();

    this.tiposComunicacion$ = this._store.select(getTipoComunicacionArrayData);

  }

  getDatosDestinatario(comunicacion): Observable<AgentDTO[]> {
    const radicacionEntradaDTV = new RadicacionEntradaDTV(comunicacion);
    return radicacionEntradaDTV.getDatosDestinatarios();

  }



  ngOnDestroy() {

  }

  initForm() {
    this.form = this.formBuilder.group({
      'anno': [null],
      'dependencia': [null, [Validators.required]],
      'nroRadicado': [null],
      'consecutivo': [null],
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


  findDependency(code): string {
    const result = this.dependencias.find((element) => element.codigo == code);
    return result ? result.nombre : '';
  }



  listarDistribuciones() {

   this.first = 0;

   this.comunicaciones$ =  this.correspondenciaApi.ListarComunicacionesSalidaDistibucionFisica(this.payload).share();
  }

  Finalizar(): void {
    this._store.dispatch(go(['/' + ROUTES_PATH.workspace]));
  }

  get payload(){

    const payload:any =  {
      fecha_ini: this.convertDate(this.start_date),
      fecha_fin: this.convertDate(this.end_date),
      cod_dependencia: this.form.get('dependencia').value ? this.form.get('dependencia').value.codigo : null,
    };

    if( !isNullOrUndefined(this.form.get('nroRadicado').value)   )
      payload.nro_radicado = this.form.get('nroRadicado').value;

    return payload;
  }

  confirmarRecibo(){

    this.correspondenciaApi.recibirDocumentosFisicos(this.comunicacionesParaConfirmar)
        .subscribe(_ => {
          this.comunicaciones$ = this.correspondenciaApi.ListarComunicacionesSalidaDistibucionFisica(this.payload);

          this.selectedComunications = [];

          this._store.dispatch(new PushNotificationAction({severity:'success',summary:"Confirmación de recibo de documentos físicos, exitosa"}));

          this._changeDetector.detectChanges();
        });

  }

  private get comunicacionesParaConfirmar(){

    return this.selectedComunications.map(item => {return {nroRadicado:item.nroRadicado,distribuido:1}});
  }

  get anexosFisicos(){
    if (isNullOrUndefined(this.itemForView))
       return [];
    return this.itemForView.filter( anexo => anexo.codTipoSoporte === SOPORTE_FISICO);
  }

  get dependeciaSelectedName(){

    const dependencia = this.form.get("dependencia").value;

    return dependencia.nombre || "";
  }

  verAnexos(listaAnexos){
    this.anexoDialogVisibility = true;
    this.itemForView = listaAnexos;
  }

  hideDialogAnexo(){
    this.anexoDialogVisibility = false;
    this.itemForView = null;
  }

  hasAnexosFisicos(listaAnexos:any[]){
    if (isNullOrUndefined(listaAnexos) )
       return false;
    return listaAnexos.some( anexo => anexo.codTipoSoporte === SOPORTE_FISICO)
  }





}
