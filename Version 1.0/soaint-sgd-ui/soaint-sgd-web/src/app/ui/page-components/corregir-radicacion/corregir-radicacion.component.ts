import {
  Component,
  OnInit,
  AfterViewInit,
  ChangeDetectorRef,
  OnDestroy,
  ViewChild
} from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {Sandbox as ConstanteDtoSandbox} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import {Subscription} from 'rxjs/Subscription';
import {Observable} from 'rxjs/Observable';
import {TareaDTO} from '../../../domain/tareaDTO';
import {Sandbox as RadicarComunicacionesSandBox} from 'app/infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-sandbox';
import {Sandbox as TaskSandBox} from 'app/infrastructure/state-management/tareasDTO-state/tareasDTO-sandbox';
import {getActiveTask} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {Sandbox as AsiganacionDTOSandbox} from '../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox';
import {ComunicacionOficialDTO} from '../../../domain/comunicacionOficialDTO';
import {RadicacionEntradaDTV} from '../../../shared/data-transformers/radicacionEntradaDTV';
import {Sandbox as DependenciaGrupoDtoSandbox} from '../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {Sandbox as CominicacionOficialSandbox} from '../../../infrastructure/state-management/comunicacionOficial-state/comunicacionOficialDTO-sandbox';
import {Sandbox as PaisSandbox} from "../../../infrastructure/state-management/paisDTO-state/paisDTO-sandbox";
import {Sandbox as MunicipioSandbox} from "../../../infrastructure/state-management/municipioDTO-state/municipioDTO-sandbox";
import {Sandbox as DepartamentoSandbox} from "../../../infrastructure/state-management/departamentoDTO-state/departamentoDTO-sandbox";
import {ProduccionDocumentalApiService, MessagingService} from '../../../infrastructure/__api.include';
import {VersionDocumentoDTO} from '../produccion-documental/models/DocumentoDTO';
import {PushNotificationAction} from '../../../infrastructure/state-management/notifications-state/notifications-actions';
import {RadicacionEntradaFormInterface} from '../../../shared/interfaces/data-transformers/radicacionEntradaForm.interface';
import {DatosGeneralesStateService} from '../../bussiness-components/datos-generales-edit/datos-generales-state-service';
import {DatosRemitenteStateService} from '../../bussiness-components/datos-remitente-edit/datos-remitente-state-service';
import {DatosDestinatarioStateService} from '../../bussiness-components/datos-destinatario-edit/datos-destinatario-state-service';
import {ComunicacionOficialEntradaDTV} from '../../../shared/data-transformers/comunicacionOficialEntradaDTV';
import {CorrespondenciaApiService} from '../../../infrastructure/api/correspondencia.api';
import {getDestinatarioPrincial} from '../../../infrastructure/state-management/constanteDTO-state/constanteDTO-selectors';
import {afterTaskComplete} from '../../../infrastructure/state-management/tareasDTO-state/tareasDTO-reducers';
import {ConstanteDTO} from '../../../domain/constanteDTO';
import {
  COMUNICACION_SIN_ASIGNAR,
  CORREGIR_RADICACION,
  DESTINATARIO_PRINCIPAL
} from '../../../shared/bussiness-properties/radicacion-properties';
import {LoadDatosRemitenteAction} from '../../../infrastructure/state-management/constanteDTO-state/constanteDTO-actions';
import {isNullOrUndefined} from 'util';
import {
  sedeDestinatarioEntradaSelector,
  tipoDestinatarioEntradaSelector
} from "../../../infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-selectors";
import {getArrayData as DependenciaGrupoSelector} from "../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-selectors";
import {RadicadoDTO} from "../../../domain/radicadoDTO";
import {combineLatest} from "rxjs/observable/combineLatest";
import {Sandbox as SedeAdministrativaDtoSandbox} from "../../../infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-sandbox";
import {getTipoContactoArrayData} from "../../../infrastructure/state-management/constanteDTO-state/selectors/tipo-contacto-selectors";

@Component({
  selector: 'app-corregir-radicacion',
  templateUrl: './corregir-radicacion.component.html',
})
export class CorregirRadicacionComponent implements OnInit, OnDestroy, AfterViewInit {

  tabIndex = 0;
  editable = true;
  radicadoPadre: RadicadoDTO;
  formsTabOrder: Array<any> = [];
  radicacion: ComunicacionOficialDTO;
  task: TareaDTO;
  closedTask: Observable<boolean>;
  activeTaskUnsubscriber: Subscription;
  readonly = false;

  @ViewChild('datosGenerales') datosGenerales;

  @ViewChild('datosRemitente') datosRemitente;

  @ViewChild('datosDestinatario') datosDestinatario;

  tipoRadicacion = CORREGIR_RADICACION;
  tipoDestinatarioSuggestions$: Observable<ConstanteDTO[]>;
  sedeDestinatarioSuggestions$: Observable<ConstanteDTO[]>;
  dependenciaGrupoSuggestions$: Observable<ConstanteDTO[]>;
  tipoDestinatarioDefaultSelection$: Observable<ConstanteDTO>;
  radicacionEntradaFormData: RadicacionEntradaFormInterface = null;
  comunicacion: ComunicacionOficialDTO = {};
  descripcionAnexos: any;
  radicadosReferidos: any;
  agentesDestinatario: any;
  disabled = true;


  fieldDatosGeneralesDisabled = {
    tipoComunicacion: true,
    reqDistFisica: true,
    reqDistElect: true,
    reqDigit: true,
    adjuntarDocumento: true,
    tipoAnexos: true,
    soporteAnexos: true,
    tipoAnexosDescripcion: true,
    medioRecepcion: true,
    empresaMensajeria: true,
    numeroFolio: true
  };

  constructor(
    private _store: Store<State>,
    private _sandbox: RadicarComunicacionesSandBox,
    private _asiganacionSandbox: AsiganacionDTOSandbox,
    private _comunicacionOficialApi: CominicacionOficialSandbox,
    private _taskSandBox: TaskSandBox,
    private formBuilder: FormBuilder,
    private _produccionDocumentalApi: ProduccionDocumentalApiService,
    private messagingService: MessagingService,
    private _changeDetectorRef: ChangeDetectorRef,
    private _correspondenciaService: CorrespondenciaApiService,
    private _depGrupoSandbox: DependenciaGrupoDtoSandbox,
    private _constanteSandbox: ConstanteDtoSandbox,
    private _sedeAdmSandbox: SedeAdministrativaDtoSandbox,
    private _paisSandbox: PaisSandbox,
    private _departSandbox: DepartamentoSandbox,
    private _municipioSandbox: MunicipioSandbox
  ) {

  }

  ngOnInit() {
    this.tipoDestinatarioDefaultSelection$ = this._store.select(getDestinatarioPrincial);
    this.tipoDestinatarioSuggestions$ = this._store.select(tipoDestinatarioEntradaSelector);
    this.sedeDestinatarioSuggestions$ = this._store.select(sedeDestinatarioEntradaSelector);
    this.dependenciaGrupoSuggestions$ = this._store.select(DependenciaGrupoSelector);
    this.Init();
  }

  openNext() {
    this.tabIndex = (this.tabIndex + 1) % 4;
  }

  openPrev() {
    this.tabIndex = (this.tabIndex - 1) % 4;
  }

  updateTabIndex(event) {
    this.tabIndex = event.index;
  }

  Init() {
    this.closedTask = afterTaskComplete.map(() => true).startWith(false);
    this.activeTaskUnsubscriber = this._store.select(getActiveTask).subscribe(activeTask => {
      this.task = activeTask;
      if (!isNullOrUndefined(this.task)) {
        this.restore();
      }

    });
  }

  abort() {
    this._taskSandBox.abortTaskDispatch({
      idProceso: this.task.idProceso,
      idDespliegue: this.task.idDespliegue,
      instanciaProceso: this.task.idInstanciaProceso
    });
  }

  save() {
    const payload: any = {
      //radicadoPadre:this.radicadoPadre,
      destinatario: this.datosDestinatario.form.value,
      generales: this.datosGenerales.form.value,
      remitente: this.datosRemitente.form.value,
      descripcionAnexos: this.datosGenerales.descripcionAnexos,
      radicadosReferidos: this.datosGenerales.radicadosReferidos,
      agentesDestinatario: this.datosDestinatario.agentesDestinatario
    };

    if (this.datosRemitente.datosContactos) {
      payload.datosContactos = this.datosRemitente.datosContactos.contacts;
    }


    const tareaDto: any = {
      idTareaProceso: this.task.idTarea,
      idInstanciaProceso: this.task.idInstanciaProceso,
      payload: JSON.stringify(payload)
    };
    this._sandbox.quickSave(tareaDto).subscribe();

  }

  restore() {
    this._asiganacionSandbox.obtenerComunicacionPorNroRadicado(this.task.variables.numeroRadicado)
      .subscribe((result) => {
        this.comunicacion = result;
        const radicacionEntradaDTV: RadicacionEntradaDTV = new RadicacionEntradaDTV(this.comunicacion);
        this.radicacionEntradaFormData = radicacionEntradaDTV.getRadicacionEntradaFormData();

        this.InitGenerales();
        this.InitRemitente();
        this.InitDestinatario();
        setTimeout(() => {
          this._changeDetectorRef.detectChanges();
        }, 0);
      });
  }

  InitGenerales() {

    if (!isNullOrUndefined(this.radicacionEntradaFormData.generales))
      this.datosGenerales.form.patchValue(this.radicacionEntradaFormData.generales);

    this.datosGenerales.radicadosReferidos = this.radicacionEntradaFormData.radicadosReferidos || [];
    this.datosGenerales.ppdDocumentoList = this.comunicacion.ppdDocumentoList;

    const payloadSop = {
      key: 'soporteAnexo'
    }
    const payloadTip = {
      key: 'tipoAnexos'
    };
    combineLatest(
      this._constanteSandbox.loadData(payloadSop).map((r: any) => isNullOrUndefined(r) ? [] : r.constantes),
      this._constanteSandbox.loadData(payloadTip).map((r: any) => isNullOrUndefined(r) ? [] : r.constantes)
    ).subscribe(([tipAnexos, sopAnexos]) => {
      this.radicacionEntradaFormData.descripcionAnexos.forEach(anexos => {

        const tipanexos = tipAnexos.find(value => !isNullOrUndefined(value) && value.codigo === anexos.soporteAnexo.codigo);
        if (!isNullOrUndefined(tipanexos)) {
          anexos.soporteAnexo = tipanexos;
        }
        const sopanexos = sopAnexos.find(value => !isNullOrUndefined(value) && value.codigo === anexos.tipoAnexo.codigo);
        if (!isNullOrUndefined(sopanexos)) {
          anexos.tipoAnexo = sopanexos;
        }
      });
      this.datosGenerales.descripcionAnexos = [...this.radicacionEntradaFormData.descripcionAnexos || []];
      this._changeDetectorRef.detectChanges();
    });
  }

  InitRemitente() {
    this.datosRemitente.form.patchValue(this.radicacionEntradaFormData.remitente);
    console.log("DATOS REMITENTES: ", this.datosRemitente.form.value);
    if (this.radicacionEntradaFormData.datosContactos) {

      const datosContacto = this.radicacionEntradaFormData.datosContactos;

      combineLatest(this._store.select(getTipoContactoArrayData), this._paisSandbox.loadData(null))
        .switchMap(([tiposContacto, paises]) => {

          datosContacto.forEach(contacto => {

            const pais = paises.paises.find(p => p.codigo == contacto.pais.codigo);

            if (!isNullOrUndefined(pais))
              contacto.pais = pais;

            const tipoContacto = tiposContacto.find(tc => tc.codigo == contacto.tipoContacto.codigo);

            if (!isNullOrUndefined(tipoContacto))
              contacto.tipoContacto = tipoContacto;

          });

          const observables = datosContacto
            .filter(contacto => !isNullOrUndefined(contacto.pais) && !isNullOrUndefined(contacto.departamento))
            .map((contacto: any) => this._departSandbox.loadData({codPais: contacto.pais.codigo})
              .switchMap(departs => {

                const depart = departs.departamentos.find(d => d.codigo == contacto.departamento.codigo);

                if (!isNullOrUndefined(depart)) {
                  contacto.departamento = depart;
                  return this._municipioSandbox.loadData({codDepar: depart.codigo});
                }
                return Observable.of([]);
              })
              .switchMap((municipios: any) => {
                const municipio = municipios.municipios.find((m: any) => m.codigo === contacto.municipio.codigo);

                if (!isNullOrUndefined(municipio)) {
                  contacto.municipio = municipio;
                }
                return Observable.of(true);
              })
            );

          return combineLatest(...observables);

        })
        .subscribe(_ => {
          const retry = setInterval(() => {
            if (typeof this.datosRemitente.datosContactos !== 'undefined') {
              this.datosRemitente.datosContactos.contacts = [...this.radicacionEntradaFormData.datosContactos];
              clearInterval(retry);
            }
          }, 400);
        });
    }
  }

  InitDestinatario() {
    const payload = {
      key: 'tipoDestinatario'
    };
    combineLatest(
      this._constanteSandbox.loadData(payload),
      this._sedeAdmSandbox.loadData(),
      this._depGrupoSandbox.loadDependencies()
    ).subscribe(([tipoDest, sedeDest, depGroup]) => {
      this.radicacionEntradaFormData.agentesDestinatario.forEach(agente => {


        const tipodest = tipoDest.constantes.find(value => !isNullOrUndefined(value) && value.codigo === agente.tipoDestinatario.codigo);
        if (!isNullOrUndefined(tipodest)) {
          agente.tipoDestinatario = tipodest;
        }
        ;
        const sededest = sedeDest.organigrama.find(value => !isNullOrUndefined(value) && value.codigo === agente.sedeAdministrativa.codigo);
        if (!isNullOrUndefined(sededest)) {
          agente.sedeAdministrativa = sededest;
        }
        ;
        const depgroup = depGroup.dependencias.find(value => !isNullOrUndefined(value) && value.codigo === agente.dependenciaGrupo.codigo);
        if (!isNullOrUndefined(depgroup)) {
          agente.dependenciaGrupo = depgroup;
        }
        ;
      });
      this.datosDestinatario.agentesDestinatario = [...this.radicacionEntradaFormData.agentesDestinatario];
    });
  }

  actualizarComunicacion(): void {
    const payload = this.GetComunicacionPayload();
    this._correspondenciaService.actualizarComunicacion(payload)
      .subscribe(response => {
        this.disableEditionOnForms();
        this._taskSandBox.completeTaskDispatch({
          idProceso: this.task.idProceso,
          idDespliegue: this.task.idDespliegue,
          idTarea: this.task.idTarea
          /* parametros: {
           }*/
        });
      });


  }

  GetComunicacionPayload() {
    const radicacionEntradaFormPayload: any = {
      destinatario: this.datosDestinatario.form.value,
      generales: Object.assign(this.radicacionEntradaFormData.generales, this.datosGenerales.form.value),
      remitente: Object.assign(this.radicacionEntradaFormData.remitente, this.datosRemitente.form.value),
      descripcionAnexos: this.datosGenerales.descripcionAnexos,
      radicadosReferidos: this.datosGenerales.radicadosReferidos,
      agentesDestinatario: this.datosDestinatario.agentesDestinatario,
      task: this.task
    };
    if (this.datosRemitente.datosContactos) {
      radicacionEntradaFormPayload.datosContactos = this.datosRemitente.datosContactos.contacts;
    }
    if (this.datosGenerales.tipoComunicacion) {
      radicacionEntradaFormPayload.generales = this.datosGenerales.tipoComunicacion;
    }
    if (this.radicadoPadre)
      radicacionEntradaFormPayload.radicadoPadre = this.radicadoPadre.numeroRadicado.split('-')[1];
    const comunicacionOficialDTV = new ComunicacionOficialEntradaDTV(radicacionEntradaFormPayload, this._store);
    // this.radicacion = comunicacionOficialDTV.getComunicacionOficial();

    const comunicacion = comunicacionOficialDTV.getComunicacionOficial();

    comunicacion.correspondencia.codEstado = COMUNICACION_SIN_ASIGNAR;

    return comunicacion;
  }

  disableEditionOnForms() {
    this.disabled = true;
    this.datosGenerales.form.disable();
    this.datosRemitente.form.disable();
    this.datosDestinatario.form.disable();
  }

  HasDocuments() {
    if (this.comunicacion && this.comunicacion.ppdDocumentoList) {

      return !isNullOrUndefined(this.comunicacion.ppdDocumentoList[0].ideEcm);
    }
    return false;
  }

  IsDisabled() {
    return (this.formsTabOrder[this.tabIndex] && this.formsTabOrder[this.tabIndex].valid);
  }

  EsInvalido() {
    if (this.formsTabOrder.length) {
      const formularioInvalido = this.formsTabOrder.find(_form => !isNullOrUndefined(_form) && _form.valid === false);
      if (formularioInvalido) {
        return true;
      }
    }
    return false;
  }

  uploadVersionDocumento(doc: VersionDocumentoDTO) {
    const formData = new FormData();
    formData.append('documento', doc.file, doc.nombre);
    if (doc.id) {
      formData.append('idDocumento', doc.id);
    }
    formData.append('selector', 'PD');
    formData.append('nombreDocumento', doc.nombre);
    formData.append('tipoDocumento', doc.tipo);
    formData.append('sede', this.task.variables.nombreSede);
    formData.append('dependencia', this.task.variables.nombreDependencia);
    formData.append('nroRadicado', this.task.variables && this.task.variables.numeroRadicado || null);
    this._produccionDocumentalApi.subirVersionDocumento(formData).subscribe(
      resp => {
        if ('0000' === resp.codMensaje) {
          this._store.dispatch(new PushNotificationAction({severity: 'success', summary: resp.mensaje}))
          return resp.documentoDTOList[0];
        } else {
          this._store.dispatch(new PushNotificationAction({severity: 'error', summary: resp.mensaje}));
          return doc
        }
      },
      error => this._store.dispatch(new PushNotificationAction({severity: 'error', summary: error}))
    );
  }

  enableActualizar() {
    const conditions: boolean[] = [
      this.datosGenerales.form.valid,
      this.datosRemitente.form.valid,
      this.datosDestinatario.agentesDestinatario.some(agente => agente.tipoDestinatario.codigo == DESTINATARIO_PRINCIPAL)
    ];

    return conditions.every(c => c);
  }

  ngOnDestroy() {
    this.activeTaskUnsubscriber.unsubscribe();
    // this._changeDetectorRef.detach();
  }

  ngAfterViewInit(): void {
    this._store.dispatch(new LoadDatosRemitenteAction());
  }


}
