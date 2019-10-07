import {
  ChangeDetectorRef,
  Component,
  Input,
  OnDestroy,
  OnInit,
  ViewChild,
  Output,
  EventEmitter,
  ChangeDetectionStrategy
} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {ProduccionDocumentalApiService} from 'app/infrastructure/api/produccion-documental.api';
import {VALIDATION_MESSAGES} from 'app/shared/validation-messages';
import {getAuthenticatedFuncionario} from 'app/infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {FuncionarioDTO} from 'app/domain/funcionarioDTO';
import {PdMessageService} from '../../providers/PdMessageService';
import {MessagingService} from 'app/shared/providers/MessagingService';
import {TareaDTO} from 'app/domain/tareaDTO';
import {VersionDocumentoDTO} from '../../models/DocumentoDTO';
import {AnexoDTO} from '../../models/DocumentoDTO';
import {Sandbox as DependenciaSandbox} from '../../../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {getActiveTask} from '../../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors';
import {Subscription} from 'rxjs/Subscription';
import {StatusDTO} from '../../models/StatusDTO';
import {PushNotificationAction} from '../../../../../infrastructure/state-management/notifications-state/notifications-actions';
import {DocumentoEcmDTO} from '../../../../../domain/documentoEcmDTO';
import {ConfirmationService, FileUpload} from 'primeng/primeng';
import {DocumentDownloaded} from '../../events/DocumentDownloaded';
import {DocumentUploaded} from '../../events/DocumentUploaded';
import {TASK_PRODUCIR_DOCUMENTO} from "../../../../../infrastructure/state-management/tareasDTO-state/task-properties";
import {getTipoComunicacionArrayData} from "../../../../../infrastructure/state-management/constanteDTO-state/selectors/tipo-comunicacion-selectors";
import {isNullOrUndefined} from "util";
import {FileHelper} from "../../../../../shared/helpers/file.helper";
import {
  TIPOLOGIA_MEMORANDO,
  TIPOLOGIA_OFICIO,
  COMUNICACION_EXTERNA_ENVIADA,
  COMUNICACION_INTERNA_ENVIADA
} from "../../../../../shared/bussiness-properties/radicacion-properties";
import {getSoporteAnexoArrayData} from "../../../../../infrastructure/state-management/constanteDTO-state/selectors/soporte-anexos-selectors";

@Component({
  selector: 'pd-datos-generales',
  templateUrl: './datos-generales.component.html',
  styleUrls: ['./datos-generales.component.css']
})

export class PDDatosGeneralesComponent implements OnInit, OnDestroy {

  form: FormGroup;

  validations: any = {};

  taskData: TareaDTO;
  screenData: any;

  previewPdf = false;

  funcionarioLog: FuncionarioDTO;

  tiposComunicacion$: Observable<ConstanteDTO[]>;
  tiposAnexo$: Observable<ConstanteDTO[]>;
  tiposPlantilla$: ConstanteDTO[];

  documentLoaded = false;
  documentPreview = false;
  documentPreviewUrl: string | Uint8Array = '';
  documentPdfFile: PDFDocumentProxy = null;

  pd_newVersionObj = {
    id: null,
    nombre: '',
    tipo: 'html',
    version: '',
    contenido: '',
    file: null,
    size: 0,
    editable: true
  };
  pd_editarPlantillaVisible = false;
  pd_confirmarVersionadoVisible = false;
  pd_confirmarVersionado = false;
  pd_currentVersionEditable = true;
  pd_currentVersion: VersionDocumentoDTO = Object.assign({contenido: ''}, this.pd_newVersionObj);

  listaVersionesDocumento: VersionDocumentoDTO[] = [];
  listaAnexos: AnexoDTO[] = [];
  minVersionEditable: number;

  fechaCreacion = new Date();
  numeroRadicado = null;
  tipoPlantillaSelected: ConstanteDTO;
  tipoComunicacionSelected: ConstanteDTO;

  activeTaskUnsubscriber: Subscription;

  tipoSoporte$: Observable<ConstanteDTO[]>;

  nombreSede = '';
  nombreDependencia = '';

  @Output() onChangeTipoPlantillaComunicacion: EventEmitter<any> = new EventEmitter<any>();


  @ViewChild("alertItem") alertItem;

  @Input()
  idecmDocumentoRadicado: string;

  ckEditorConfig;

  constructor(private _store: Store<State>,
              private _produccionDocumentalApi: ProduccionDocumentalApiService,
              private _dependenciaSandbox: DependenciaSandbox,
              private formBuilder: FormBuilder,
              private _changeDetectorRef: ChangeDetectorRef,
              private messagingService: MessagingService,
              private pdMessageService: PdMessageService,
              private _confirmationService: ConfirmationService
  ) {

    this.initForm();
  }

  ngOnInit(): void {

    this.tipoSoporte$ = this._store.select(getSoporteAnexoArrayData);

    this.ckEditorConfig = {
      allowedContent: false,
      extraPlugins: 'divarea',
      forcePasteAsPlainText: true
    };

    this._store.select(getAuthenticatedFuncionario).subscribe((funcionario) => {
      this.funcionarioLog = funcionario;
    });

    this.activeTaskUnsubscriber = this._store.select(getActiveTask)

      .flatMap(activeTask => {
        console.log('TASK -> ', activeTask);
        this.taskData = activeTask;
        return this._dependenciaSandbox.loadDependencies({});
      })
      .subscribe((results) => {


          if (this.taskData && this.taskData.variables) {

            const dep = results.dependencias.find((element) => element.codigo === this.taskData.variables.codDependencia);
            const sede = results.dependencias.find((element) => element.codigo === this.taskData.variables.codigoSede);

            this.taskData.variables.nombreDependencia = !isNullOrUndefined(dep) ? dep.nombre : '';
            this.taskData.variables.nombreSede = !isNullOrUndefined(sede) ? sede.nombre : '';
            this.screenData = Object.assign({}, this.taskData.variables);

            if (!isNullOrUndefined(this.taskData.variables.fechaProd)) {
              const time: number = parseInt(this.taskData.variables.fechaProd);
              this.fechaCreacion = new Date(time);
            }


            const tipoP = this.tiposPlantilla$.find(tp => tp.codigo == this.screenData.codigoTipoPlantilla);

            this.form.get('tipoPlantilla').setValue(tipoP)

            this.tipoPlanillaChange({value: tipoP});


            this._changeDetectorRef.detectChanges();
          }

        }
      );

    this.tiposComunicacion$ = this._store.select(getTipoComunicacionArrayData).map(tipoComunicaciones => tipoComunicaciones.filter(tipo => tipo.codigo[0] == 'S'));
    this.tiposAnexo$ = this._produccionDocumentalApi.getTiposAnexo({});
    this._produccionDocumentalApi.getTiposPlantilla({}).subscribe(tp => this.tiposPlantilla$ = tp);
    this.listenForErrors();

  }

  updateStatus(currentStatus: StatusDTO) {
    if (currentStatus.datosGenerales.tipoComunicacion) {
      this.form.get('tipoComunicacion').setValue(currentStatus.datosGenerales.tipoComunicacion);
      this.form.get('tipoPlantilla').setValue(currentStatus.datosGenerales.tipoPlantilla);
      this.pdMessageService.sendMessage(currentStatus.datosGenerales.tipoComunicacion);
    }
    this.listaVersionesDocumento = [...currentStatus.datosGenerales.listaVersionesDocumento.map(version => {

      version.disabled = version.taskId != this.taskData.idTarea;
      return version;
    })];
    this.listaAnexos = [...currentStatus.datosGenerales.listaAnexos];
    this.refreshView();
  }

  initForm() {
    this.form = this.formBuilder.group({
      'tipoComunicacion': [null, Validators.required],
      'tipoPlantilla': [null, Validators.required],
      'elaborarDocumento': [null],
      'soporte': 'electronico',
      'tipoAnexo': [null],
      'descripcion': [null],
    });

    const changeFunction = (value) => {

      const eventValue = {
        tipoComunicacion: !isNullOrUndefined(value.tipoComunicacion) ? value.tipoComunicacion.codigo : null,
        tipoPlantilla: !isNullOrUndefined(value.tipoPlantilla) ? value.tipoPlantilla.codigo : null
      };
      this.onChangeTipoPlantillaComunicacion.emit(eventValue);
    };

    this.form.valueChanges.subscribe(changeFunction);
  }

  resetCurrentVersion() {
    this.pd_currentVersion = Object.assign({}, this.pd_newVersionObj);
    return this.pd_currentVersion;
  }

  attachDocument(event, versionUploader: FileUpload) {
    if (event.files.length === 0) {
      return false;
    }
    const nv: VersionDocumentoDTO = {
      id: this.listaVersionesDocumento.length > 0 ? this.listaVersionesDocumento[this.listaVersionesDocumento.length - 1].id : null,
      nombre: encodeURI(event.files[0].name),
      tipo: 'pdf',
      version: '',
      contenido: '',
      file: event.files[0],
      size: event.files[0].size
    };

    this.uploadVersionDocumento(nv);
    versionUploader.clear();
    versionUploader.basicFileInput.nativeElement.value = '';
  }

  obtenerDocumentoRadicado() {

    // console.log(this.taskData);
    // if (this.documentoRadicadoUrl) {
    this.documentPreview = true;
    // } else {
    //   console.log('No se pudo mostrar el documento del radicado asociado');
    // }
  }

  loadHtmlVersion() {
    this._produccionDocumentalApi.obtenerVersionDocumento({
      id: this.pd_currentVersion.id,
      version: this.pd_currentVersion.version
    }).subscribe(
      res => {
        if ('html' === this.pd_currentVersion.tipo && (200 === res.status || 204 === res.status)) {
          this.pd_currentVersion.contenido = res._body;
          this.pd_editarPlantillaVisible = true;
        } else {
          this._store.dispatch(new PushNotificationAction({severity: 'warn', summary: res.statusText}));
        }
      },
      error => this._store.dispatch(new PushNotificationAction({severity: 'warn', summary: error})),
      () => this.refreshView()
    );
  }

  mostrarVersionDocumento(index: number) {
    if (index === -1) {
      this.pd_currentVersionEditable = true;
      this.pd_editarPlantillaVisible = true;
      return true;
    }

    this.pd_currentVersion = Object.assign({}, this.listaVersionesDocumento[index]);
    this.pd_currentVersion.editable = index === this.listaVersionesDocumento.length - 1;

    if ('pdf' === this.pd_currentVersion.tipo) {
      this.idecmDocumentoRadicado = this.pd_currentVersion.id;
      this.showPdfViewer(this._produccionDocumentalApi.obtenerVersionDocumentoUrl({
        id: this.pd_currentVersion.id,
        version: this.pd_currentVersion.version
      }));

      window.dispatchEvent(new Event("resize"));
    } else {

      //this._produccionDocumentalApi.transformarAPdf(this.pd_currentVersion).subscribe();

      this.loadHtmlVersion();
    }
  }

  eliminarVersionDocumento(index) {

    this._confirmationService.confirm({
      message: `Si elimina el documento principal se borrarán sus anexos. ¿Está seguro que desea borrar este documento?`,
      header: 'Confirmación',
      icon: 'fa fa-question-circle',
      accept: () => {

        this.pd_currentVersion = Object.assign({}, this.listaVersionesDocumento[index]);
        this._produccionDocumentalApi.eliminarVersionDocumento({id: this.pd_currentVersion.id}).subscribe(
          res => {
            this.removeFromList(index, 'listaVersionesDocumento');
            this.resetCurrentVersion();

            if (isNullOrUndefined(this.tipoPlantillaSelected))

              this.tipoPlanillaChange({value: this.tiposPlantilla$[0]});

            else
              this.tipoPlanillaChange({value: this.tipoPlantillaSelected})
          },
          error => this._store.dispatch(new PushNotificationAction({severity: 'error', summary: error})),
          () => this.refreshView()
        );
      },
      reject: () => {

      }
    });


  }

  confirmarVersionDocumento() {
    const blob = new Blob([this.pd_currentVersion.contenido], {type: 'text/html'});
    const nv = {
      id: this.pd_currentVersion.id,
      nombre: this.pd_currentVersion.nombre,
      tipo: 'html',
      version: '1.0',
      contenido: this.pd_currentVersion.contenido,
      file: blob,
      size: blob.size
    };
    this.pd_confirmarVersionado = false;
    this.pd_confirmarVersionadoVisible = false;
    this.pd_editarPlantillaVisible = false;
    this.pd_currentVersionEditable = false;
    this.uploadVersionDocumento(nv);
  }

  uploadVersionDocumento(doc: VersionDocumentoDTO) {
    const formData = new FormData();
    formData.append('documento', doc.file, encodeURI(doc.nombre));
    if (doc.id) {
      formData.append('idDocumento', doc.id);
    }
    formData.append('nombreDocumento', encodeURI(doc.nombre));
    formData.append('tipoDocumento', doc.tipo);
    if (this.taskData !== null) {
      formData.append('sede', this.screenData.nombreSede);
      formData.append('dependencia', this.screenData.nombreDependencia);
      formData.append('codigoDependencia', this.screenData.codDependencia);
      formData.append('nroRadicado', this.screenData.numeroRadicado || null);
      formData.append("selector", 'PD');
    }

    let docEcmResp: DocumentoEcmDTO = null;
    this._produccionDocumentalApi.subirVersionDocumento(formData).subscribe(
      resp => {
        if ('0000' === resp.codMensaje) {
          docEcmResp = resp.documentoDTOList[0];
          const versiones = [...this.listaVersionesDocumento];
          console.log(versiones);
          doc.id = docEcmResp && docEcmResp.idDocumento || (new Date()).toTimeString();
          doc.version = docEcmResp && docEcmResp.versionLabel || '1.0';
          doc.taskId = this.taskData.idTarea;
          versiones.push(doc);

          this.listaVersionesDocumento = [...versiones];

          //  this.form.get('tipoPlantilla').reset();
          this.resetCurrentVersion();
          this.messagingService.publish(new DocumentUploaded(docEcmResp));
          this._changeDetectorRef.detectChanges();
        } else {
          this._store.dispatch(new PushNotificationAction({severity: 'error', summary: resp.mensaje}));
        }
      },
      error => this._store.dispatch(new PushNotificationAction({severity: 'error', summary: error}))
    );
  }

  getListaVersiones(): VersionDocumentoDTO[] {
    return this.listaVersionesDocumento;
  }

  selectAnexo() {

    if (this.listaVersionesDocumento.length === 0) {
      this.alertItem.ShowMessage('Debe agregar el documento principal')
      return false;
    }

    if (!this.form.get('tipoAnexo').value) {
      this.alertItem.ShowMessage('Debe de seleccionar un tipo de anexo');
      return false;
    }
  }

  agregarAnexo(event?, anexoUploader?: FileUpload) {

    const anexo: AnexoDTO = {
      id: (new Date()).toTimeString(), descripcion: this.form.get('descripcion').value,
      soporte: this.form.get('soporte').value, tipo: this.form.get('tipoAnexo').value
    };

    if (event && anexoUploader) {


      anexo.file = event.files[0];
      const formData = new FormData();
      formData.append('documento', anexo.file, encodeURI(anexo.file.name));
      formData.append('nombreDocumento', encodeURI(anexo.file.name));
      formData.append('tipoDocumento', anexo.file.type);
      formData.append('idDocumentoPadre', this.listaVersionesDocumento[0].id);
      if (this.taskData !== null) {
        formData.append('sede', this.screenData.nombreSede);
        formData.append('codigoDependencia', this.screenData.codDependencia);
        formData.append('dependencia', this.screenData.nombreDependencia);
        formData.append('nroRadicado', this.screenData.numeroRadicado || null);
        formData.append('selector', this.taskData.nombre === TASK_PRODUCIR_DOCUMENTO ? 'PD' : 'Otra cosa');
      }
      console.log('Nonbre anexo');
      console.log(formData.get('documento'));
      console.log(formData.get('nombreDocumento'));

      let docEcmResp: DocumentoEcmDTO = null;
      this._produccionDocumentalApi.subirAnexo(formData).subscribe(
        resp => {
          if ('0000' === resp.codMensaje) {
            docEcmResp = resp.documentoDTOList[0];
            anexo.id = docEcmResp && docEcmResp.idDocumento || (new Date()).toTimeString();
            if (!anexo.descripcion && !!docEcmResp)
              anexo.descripcion = docEcmResp.nombreDocumento;
            this.addAnexoToList(anexo);
            this.messagingService.publish(new DocumentUploaded(docEcmResp));
            this.clearAnexoSection();
          } else {
            this._store.dispatch(new PushNotificationAction({severity: 'error', summary: resp.mensaje}));
          }
        },
        error => this._store.dispatch(new PushNotificationAction({severity: 'error', summary: error}))
      );
      anexoUploader.clear();
      anexoUploader.basicFileInput.nativeElement.value = '';
    } else {
      this.addAnexoToList(anexo);
      this.clearAnexoSection();
    }

  }

  addAnexoToList(anexo: AnexoDTO) {
    this.listaAnexos = [
      ...this.listaAnexos,
      ...[anexo]
    ];

  }

  private clearAnexoSection() {

    this.form.get('soporte').setValue('electronico');
    this.form.get('descripcion').setValue(null);
    this.form.get('tipoAnexo').setValue(null);
  }

  mostrarAnexo(index: number) {
    const anexo = this.listaAnexos[index];
    this.idecmDocumentoRadicado = anexo.id;
    this.showPdfViewer(this._produccionDocumentalApi.obtenerDocumentoUrl({id: anexo.id}));

    // window.open(this._produccionDocumentalApi.obtenerDocumentoUrl({id: anexo.id}));
  }

  eliminarAnexo(i) {
    const anexo = this.listaAnexos[i];
    if (anexo.soporte === 'fisico') {
      this.removeFromList(i, 'listaAnexos');
    } else {
      this._produccionDocumentalApi.eliminarAnexo({id: anexo.id}).subscribe(
        res => {
          this.removeFromList(i, 'listaAnexos');
          this.refreshView();
        },
        error => this._store.dispatch(new PushNotificationAction({severity: 'error', summary: error}))
      );
    }
  }

  showPdfViewer(pdfUrl: string | Uint8Array) {
    this.documentPreviewUrl = pdfUrl;
    this.previewPdf = true;

  }

  tipoComunicacionChange(event) {
    this.tipoComunicacionSelected = event.value;

    if (this.tipoComunicacionSelected.codigo == COMUNICACION_INTERNA_ENVIADA) {
      const tp = this.tiposPlantilla$.find(t => t.codigo == TIPOLOGIA_MEMORANDO)
      this.form.get('tipoPlantilla').setValue(tp);

    } else if (this.tipoComunicacionSelected.codigo == COMUNICACION_EXTERNA_ENVIADA) {
      const tp = this.tiposPlantilla$.find(t => t.codigo == TIPOLOGIA_OFICIO)
      this.form.get('tipoPlantilla').setValue(tp);
    }
    this.pdMessageService.sendMessage(event.value);
  }

  tipoPlanillaChange(event) {
    this.tipoPlantillaSelected = event.value;
    this.pd_currentVersion.nombre = event.value.nombre + '_';
    this._produccionDocumentalApi.getTipoPlantilla({codigo: this.tipoPlantillaSelected.nombre.toLowerCase()}).subscribe(
      result => this.pd_currentVersion.contenido = result
    );
    if (this.tipoPlantillaSelected.codigo == TIPOLOGIA_MEMORANDO) {
      this._store.select(getTipoComunicacionArrayData)
        .subscribe(tipoComunicaciones => {
          const tipo = tipoComunicaciones.find(tipoComunicaciones => tipoComunicaciones.codigo == COMUNICACION_INTERNA_ENVIADA);
          this.form.get('tipoComunicacion').setValue(tipo);
        });
    } else if (this.tipoPlantillaSelected.codigo !== TIPOLOGIA_MEMORANDO) {
      this._store.select(getTipoComunicacionArrayData)
        .subscribe(tipoComunicaciones => {
          const tipo = tipoComunicaciones.find(tipoComunicaciones => tipoComunicaciones.codigo == COMUNICACION_EXTERNA_ENVIADA);
          this.form.get('tipoComunicacion').setValue(tipo);
        });
    }

  }


  hideVersionesDocumentoDialog() {
    this.pd_confirmarVersionadoVisible = false;
    this.pd_confirmarVersionado = false;
  }

  removeFromList(i: any, listname: string) {
    const list = [...this[listname]];
    list.splice(i, 1);
    this[listname] = list;
  }

  addToList(el: any, listname: string) {
    const list = [...this[listname]];
    list.push(el);
    this[listname] = [...list];
  }


  listenForErrors() {
    this.bindToValidationErrorsOf('tipoPlantilla');
    this.bindToValidationErrorsOf('tipoComunicacion');
  }

  listenForBlurEvents(control: string) {
    const ac = this.form.get(control);

    if ((control == 'descripcion') && !isNullOrUndefined(ac) && !isNullOrUndefined(ac.value))
      ac.setValue(ac.value.toString().trim())

    if (ac.touched && ac.invalid) {
      const error_keys = Object.keys(ac.errors);
      const last_error_key = error_keys[error_keys.length - 1];
      this.validations[control] = VALIDATION_MESSAGES[last_error_key];
    }
  }

  bindToValidationErrorsOf(control: string) {
    const ac = this.form.get(control);
    ac.valueChanges.subscribe(value => {
      if ((ac.touched || ac.dirty) && ac.errors) {
        const error_keys = Object.keys(ac.errors);
        const last_error_key = error_keys[error_keys.length - 1];
        this.validations[control] = VALIDATION_MESSAGES[last_error_key];
      } else {
        delete this.validations[control];
      }
    });
  }

  afterLoadComplete(pdf: PDFDocumentProxy) {
    this.documentLoaded = true;
    this.documentPdfFile = pdf;
  }

  hidePdf() {
    this.documentPreviewUrl = '';
    this.documentLoaded = false;
    this.documentPreview = false;
  }

  previewDocument(file) {
    window.open(URL.createObjectURL(file), '_blank');
  }

  documentViewer(url) {
    const win = window.open(url);
    win.focus();
  }

  refreshView() {
    this._changeDetectorRef.detectChanges();
  }

  ngOnDestroy() {
    this.activeTaskUnsubscriber.unsubscribe();
  }

  isValid(): boolean {
    return this.listaVersionesDocumento.length > 0;
  }

  viewPdf(versionDocumento: VersionDocumentoDTO) {

    const tipoPlantilla = this.form.get('tipoPlantilla').value.codigo == TIPOLOGIA_OFICIO ? 'oficio' : 'memorando';

    this._produccionDocumentalApi.transformarAPdf(versionDocumento.contenido, tipoPlantilla)
      .subscribe(response => {

          const url = `data:application/pdf;base64,${response}`;


          this.showPdfViewer(url);

          this.refreshView();
        }
      );

  }

  onErrorUpload(event) {
    console.log(event);
  }


}

