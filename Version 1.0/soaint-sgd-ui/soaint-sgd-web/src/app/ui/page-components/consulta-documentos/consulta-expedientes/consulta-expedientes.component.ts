import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { Observable } from 'rxjs/Observable';
import { Store } from '@ngrx/store';
import { State } from 'app/infrastructure/redux-store/redux-reducers';
import { SerieService } from '../../../../infrastructure/api/serie.service';
import { SerieDTO } from 'app/domain/serieDTO';
import { SubserieDTO } from 'app/domain/subserieDTO';
import { TipologiaDocumentaService } from 'app/infrastructure/api/tipologia-documenta.service';
import { Subscription } from 'rxjs/Subscription';
import { StateUnidadDocumentalService } from 'app/infrastructure/service-state-management/state.unidad.documental';
import { isNullOrUndefined } from 'util';
import { VALIDATION_MESSAGES } from "../../../../shared/validation-messages";
import { ApiConsultaDocumentos } from "../../../../infrastructure/api/api-consulta-documentos";
import { UnidadDocumentalDTO } from "../../../../domain/unidadDocumentalDTO";
import { getAuthenticatedFuncionario } from "../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import { FuncionarioDTO } from "../../../../domain/funcionarioDTO";
import { DependenciaDTO } from "../../../../domain/dependenciaDTO";
import {environment} from "../../../../../environments/environment";
import {Utf8Helper} from "../../../../shared/utf8";
import {ViewerType} from "../../../bussiness-components/documentos-ecm-list/documentos-ecm-list.component";
import {LoadingService} from "../../../../infrastructure/utils/loading.service";
import {DependenciaApiService} from "../../../../infrastructure/api/dependencia.api";
import {InstrumentoApi} from "../../../../infrastructure/api/instrumento.api";
declare var PDFObject: any;
declare var PDFJS: PDFJSStatic;
@Component({
  selector: 'app-consulta-expedientes',
  templateUrl: './consulta-expedientes.component.html',
  styleUrls: ['./consulta-expedientes.component.css']
})
export class ConsultaExpedientesComponent implements OnInit, OnDestroy {

  formExpediente: FormGroup;
  listadoExpedientes$: Observable<UnidadDocumentalDTO[]> ;
  documentSelected$: Observable<any[]>;
  series: Observable<SerieDTO[]>;
  subseries: Observable<SubserieDTO[]>;
  subseriesObservable$:Observable<any[]>;
  dependenciaSelected : DependenciaDTO;
  resultado: any[] = [];
  state: StateUnidadDocumentalService;
  subscribers: Array<Subscription> = [];
  validations: any = {};
  userLog:FuncionarioDTO;
  versionesDialogVisible: Boolean = false;
  showPDF: Boolean = false;
  versiones$:Observable<any[]>;
  readonly  $viewer = ViewerType;
  viewerManager: ViewerType;
  contentHtml:string = "";
  docSrc = '';
  isLoading = false;
  dependencias$:Observable<any[]>;
  first = 0;


  constructor(private _store: Store<State>,
              private serieSubSerieService:SerieService,
              private _unidadDocumentalStateService: StateUnidadDocumentalService,
              private _tipologiaService: TipologiaDocumentaService,
              private  changeDetector:ChangeDetectorRef,
              private _consultaApi: ApiConsultaDocumentos,
              public loadingService: LoadingService,
              private dependenciaApi: InstrumentoApi,
              private fe: FormBuilder ){ this.state = _unidadDocumentalStateService; }

  ngOnInit() {
    this.initFormExpediente();
    this.subscribers.push(this._store.select(getAuthenticatedFuncionario)
        .subscribe( funcionario => this.userLog = funcionario)) ;

    this.dependencias$ = this.dependenciaApi.ListarDependencia();
  }

  initFormExpediente(){
    this.formExpediente =  this.fe.group({
       depP: [""],
      sCode: [""],
      ssCode: [""],
      udId: [""],
      udName: [""],
      desc1: [""],
      desc2: [""]
    });
    this.loadData();
  }

  SetValidationMessages(control: string) {
    const formControl = this.formExpediente.get(control);
    if (formControl.touched && formControl.invalid) {
      const error_keys = Object.keys(formControl.errors);
      const last_error_key = error_keys[error_keys.length - 1];
      this.validations[control] = VALIDATION_MESSAGES[last_error_key];
    } else {
      this.validations[control] = '';
    }
  }

  descriptionSerieSubserie(item){
    if(isNullOrUndefined(item.codigoSubSerie) && isNullOrUndefined(item.codigoSerie) )
      return "";
    if(item.codigoSubSerie)
      return `${item.codigoSerie} - ${item.nombreSerie} / ${item.codigoSubSerie} - ${item.nombreSubSerie}`;
      return `${item.codigoSerie} - ${item.nombreSerie}`;

  }

  OnBlurEvents(control: string) {
    const ac  = this.formExpediente.get(control);
    const inputTextFields = ['udId','udName','desc1','desc2'];
    if(inputTextFields.some(c => c == control)){
      ac.setValue(ac.value.toString().trim())
    }
    this.SetValidationMessages(control);
  }

  listasExpediente() {
    this.first = 0;
    this.listadoExpedientes$ = this._consultaApi.getConsultaExpedientes(this.PayloadExpediente);
  }

  cleanFormExpediente() {
    this.formExpediente.reset();

    this.listadoExpedientes$ = Observable.of([]);

    this.documentSelected$ = Observable.of([]);

    this.subseriesObservable$ = Observable.of([]);
  }

  loadData() {
    this.subscribers.push(
      this.formExpediente.get('depP').valueChanges.subscribe(value => {
        this.formExpediente.controls['sCode'];
        this.state.ListadoSeries = [];
        this.subseriesObservable$ = Observable.of([]);
        if(value) {
          this.state.GetListadosSeries(value);
        }
      }));
  }


  selectSerie(evt){
    this.subseriesObservable$ = evt ?
      this.serieSubSerieService
        .getSubseriePorDependenciaSerie(this.formExpediente.get('depP')
          .value,evt.value.codigoSerie,evt.value.nombreSerie)
          .map(list => {
          if(isNullOrUndefined(list))
          list = [];
          list.unshift({codigoSubSerie:null,nombreSubSerie:"Seleccione"});
          return list;
        }).do(list => {
        if(list.length  == 1)
          this.formExpediente.get("sCode").setErrors(null);
        else{
          const v = this.formExpediente.get("ssCode").value;
          if(isNullOrUndefined(v))
            this.formExpediente.get("sCode");
        }
        this.changeDetector.detectChanges();
      })
      : Observable.empty();
  }

  selectDocumento(evt){
    this.documentSelected$ = this._consultaApi.getDocumentoPorExpediente(evt.data.ecmObjId);
  }

  showDocument(documento: any) {
    this.showPDF = true;
    switch (documento.tipoDocumento) {

      case 'application/pdf':
        this.loadingService.presentLoading();
        this.docSrc = environment.obtenerDocumento + documento.idDocumento;
        PDFJS.getDocument(this.docSrc).then(doc => {
          doc.getData().then(array => {
            const blob = new Blob([array], { type: 'application/pdf' });
            const reader = new FileReader();
            reader.readAsDataURL(blob);
            reader.onloadend = () => {
              const fileURL = URL.createObjectURL(blob);
              PDFObject.embed(fileURL, '#pdf-embeded');
              this.loadingService.dismissLoading();
            }
          })
        })
        this.viewerManager = ViewerType.PDF;
        break;
      case 'text/html':

        this.contentHtml = Utf8Helper.decode(atob(documento.documento));
        console.log("contenido html", this.contentHtml);
        this.viewerManager = ViewerType.HTML;

    }

  }

  hideDocument() {
     this.viewerManager = null;
     this.showPDF = false;
    }
    docLoaded() { console.log("Probando");
      this.loadingService.dismissLoading();
    }

    versionesDocumentosDialog(row){
    this.versiones$ = this._consultaApi.getDetalleDocumento(row.idDocumento)
                           .map(doc =>  doc.historialVersiones);
    this.versionesDialogVisible = true;
  }

  hideVersionesDialog(){
    this.versionesDialogVisible = false;
  }

  get PayloadExpediente(){
    const request:any = {
      usuario: this.userLog.loginName,
    };
    Object.keys(this.formExpediente.value)
      .forEach( k => {
        if(!isNullOrUndefined(this.formExpediente.value[k]))
          request[k] = this.formExpediente.value[k].codigoSerie ||  this.formExpediente.value[k] ;
      });
    return request;
  }

  ngOnDestroy() {
    this.subscribers.forEach(obs => {
      obs.unsubscribe();
    });
  }

}
