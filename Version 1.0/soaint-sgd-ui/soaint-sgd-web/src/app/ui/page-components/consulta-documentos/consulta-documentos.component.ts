import {ChangeDetectorRef, Component, OnDestroy, OnInit, ViewEncapsulation} from '@angular/core';
import {FormGroup, Validators, FormBuilder} from '@angular/forms';
import { Observable } from 'rxjs/Observable';
import { Store } from '@ngrx/store';
import { State } from 'app/infrastructure/redux-store/redux-reducers';
import { ConstanteDTO } from 'app/domain/constanteDTO';
import { SerieService } from '../../../infrastructure/api/serie.service';
import { getTipoComunicacionArrayData, getTipoDocumentoArrayData, getTipologiaDocumentalArrayData } from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-selectors';
import { SerieDTO } from 'app/domain/serieDTO';
import { SubserieDTO } from 'app/domain/subserieDTO';
import { TipologiaDocumentaService } from 'app/infrastructure/api/tipologia-documenta.service';
import { Subscription } from 'rxjs/Subscription';
import { StateUnidadDocumentalService } from 'app/infrastructure/service-state-management/state.unidad.documental';
import { isNullOrUndefined } from 'util';
import {ApiConsultaDocumentos} from "../../../infrastructure/api/api-consulta-documentos";
import {
  getAuthenticatedFuncionario,
  getSelectedDependencyGroupFuncionario
} from "../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {FuncionarioDTO} from "../../../domain/funcionarioDTO";
import { COMUNICACION_EXTERNA,COMUNICACION_EXTERNA_ENVIADA, COMUNICACION_INTERNA_ENVIADA, GENERALES} from "../../../shared/bussiness-properties/radicacion-properties";
import {ModeloConsultaDocumentoDTO} from "../../../domain/ModeloConsultaDocumentoDTO";
import {environment} from "../../../../environments/environment";
import {ViewerType} from "../../bussiness-components/documentos-ecm-list/documentos-ecm-list.component";
import {Utf8Helper} from "../../../shared/utf8";
import {LoadingService} from "../../../infrastructure/utils/loading.service";
import {DependenciaDTO} from "../../../domain/dependenciaDTO";
import {Sandbox as DependenciaSandbox} from "../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox";
import {DomSanitizer} from "@angular/platform-browser";
import {InstrumentoApi} from "../../../infrastructure/api/instrumento.api";

declare var PDFObject: any;
declare var PDFJS: PDFJSStatic;
@Component({
  selector: 'app-consulta-documentos',
  templateUrl: './consulta-documentos.component.html',
  styleUrls: ['./consulta-documentos.component.css']
})
export class ConsultaDocumentosComponent implements OnInit, OnDestroy {

  formDocumento: FormGroup;
  start_date: Date;
  end_date: Date ;
  listadoDocumentos$: Observable<ModeloConsultaDocumentoDTO[]>;
  tiposComunicacion: Observable<ConstanteDTO[]>;
  tipologiaDocumentalSuggestions$: Observable<ConstanteDTO[]>;
  tiposDocumento: Observable<ConstanteDTO[]>;
  series: Observable<SerieDTO[]>;
  subseries: Observable<SubserieDTO[]>;
  subseriesObservable$:Observable<any[]>;
  seriesObservable$:Observable<SerieDTO[]>;
  listaSeries : SerieDTO[];
  showPDF: Boolean = false;
  allSubSeriesObservable$:Observable<SubserieDTO[]>;
  dependenciaSelected$: Observable<DependenciaDTO>;
  dependenciaSelected: DependenciaDTO;
  dependencias: DependenciaDTO[] = [];
  tipoBtn: any;
  readonly  $viewer = ViewerType;
  state: StateUnidadDocumentalService;
  subscribers: Array<Subscription> = [];
  validations: any = {};
  userLog:FuncionarioDTO;
  documentSelected;
  documentSelected$: Observable<any[]>;
  disabled: any = { btnBuscar: true };
  viewerManager: ViewerType;
  isLoading = false;
  contentHtml:string = "";
  docSrc = '';
  tipologiaDoc$: Observable<any>;
  tipologiaPaylod:any;
  dependencias$:Observable<any[]>;
  first:number = 0;

  constructor(private _store: Store<State>,
              private serieSubSerieService:SerieService,
              private _unidadDocumentalStateService: StateUnidadDocumentalService,
              private _tipologiaService: TipologiaDocumentaService,
              private  changeDetector:ChangeDetectorRef,
              private _consultaApi: ApiConsultaDocumentos,
              private _dependenciaSandbox: DependenciaSandbox,
              private fe: FormBuilder,
              private sanitizer:DomSanitizer,
              private instrumento: InstrumentoApi,

              public loadingService: LoadingService,
              private _detectChanges: ChangeDetectorRef ){
    this.state = _unidadDocumentalStateService;
    }

  ngOnInit() {
    this.initFormDocumento();
    this.StateLoadData();
    this.SetListadoSubscriptions();
    this.dependencias$ = this.instrumento.ListarDependencia().map(dependencias => {

      return [{value:null,nombre:'Seleccione una dependencia'},... dependencias];
    });
    this.subscribers.push(this._store.select(getAuthenticatedFuncionario)
       .subscribe( funcionario => this.userLog = funcionario)) ;
    this.tiposComunicacion = this._store.select(getTipoComunicacionArrayData)
      .map(tipoComunicaciones =>{
       const tpComunicaciones = tipoComunicaciones
          .filter(tipoComunicacion => tipoComunicacion.codigo !== 'EI')
        tpComunicaciones.push({codigo:"GN",nombre:"Generales"});
       return tpComunicaciones;
        }
      );
    this.tiposDocumento = this._store.select(getTipoDocumentoArrayData).map(tipos => tipos);
    this.formDocumento.get('tipo_comunicacion').valueChanges.subscribe(tipo => {
      this.tipoBtn = tipo;
    });

  }

  get isExternaRecibida(){
    return this.tipoBtn == COMUNICACION_EXTERNA;
  }
  get isExternaEnviada(){
    return this.tipoBtn == COMUNICACION_EXTERNA_ENVIADA;
  }
  get isInternaRecibida(){
    return this.tipoBtn == COMUNICACION_INTERNA_ENVIADA;
  }
  get isGeneral(){
    return this.tipoBtn == GENERALES;
  }
  baseUrl(item){
    if(isNullOrUndefined(item.nroGuia))
      return "";
    return  this.sanitizer.bypassSecurityTrustResourceUrl(`http://10.20.100.5/~hmurillo/Tramites1/Guias/wsCliente.php?Guia=${item.nroGuia}`)
  }

  initFormDocumento(): void{
    this.formDocumento = this.fe.group({
      tipo_comunicacion: [null, [Validators.required]],
      nro_radicado: [null],
      nro_identificacion: [null],
      dep_origen: [null],
      dep_destino: [null],
      nro_guia: [null],
      //fecha_radicacion: [null],
      nombre: [null],
      asunto: [null],
      //anio: [null],
     // consecutivo: [null],
      solicitante: [null],
      destinatario: [null],
      tipoDocumento: [null],
      tramite: [null],
      evento: [null],
      actuacion: [null],
      sCode: [null],
      ssCode: [null],
      depCode:[null],
      tipologiaDocumental: [null],
    });
  }

  listasDocumentos() {
    this.first = 0;
    this.listadoDocumentos$ = this._consultaApi.getConsultaDocumentos(this.payload);
  }

  SetListadoSubscriptions() {
    this.tipologiaDocumentalSuggestions$ = this._store.select(getTipologiaDocumentalArrayData);
    this.subscribers.push(this.state.ListadoActualizado$.subscribe(()=>{
      this._detectChanges.detectChanges();
    }));
  }

  StateLoadData() {
    this.state.GetListadoSedes();
    this.state.ListadoUnidadDocumental = [];
    this.dependenciaSelected$ = this._store.select(getSelectedDependencyGroupFuncionario);
    this.dependenciaSelected$.subscribe((result) => {
      this.dependenciaSelected = result;
    });
    this._dependenciaSandbox.loadDependencies({})
      .subscribe((results) => {
        this.dependencias = results.dependencias;
      });
  }

  selectDependencia(event){

    if(!isNullOrUndefined(event)){
      this.seriesObservable$ = this
        .serieSubSerieService
        .getSeriePorDependencia(event.value)
        .map(list => {
          if (isNullOrUndefined(list))
            list = [];
          this.listaSeries = list;
          list.unshift({codigoSerie: null, nombreSerie: "Seleccione"});
          return list;
        }).share();
      this.allSubSeriesObservable$ = this.serieSubSerieService.getSubSeriePorDependencia(event.value).share();
    }
    else{
      this.seriesObservable$ = Observable.of([]);
    }
    this.subseriesObservable$ = Observable.of([]);

  }

  convertDate(inputFormat) {
    function pad(s) {
      return (s < 10) ? '0' + s : s;
    }

    const d = new Date(inputFormat);
    return [pad(d.getFullYear()), pad(d.getMonth() + 1), d.getDate()].join('-');
  }

  cleanForm() {
    this.formDocumento.reset();
    this.start_date = null;
    this.end_date = null;
    this.listadoDocumentos$ = Observable.of([]);
    this.documentSelected$ = Observable.of([]);
    this.seriesObservable$ = Observable.of([]);
    this.subseriesObservable$ = Observable.of([]);
    this.tipologiaDoc$ = Observable.of([]);
  }

  get payload(){
    const request:any = {
      usuario: this.userLog.loginName
    };

    if(!isNullOrUndefined(this.start_date)){
      request.fecha_ini = this.convertDate(this.start_date);

    }
    if(!isNullOrUndefined(this.end_date)){
      request.fecha_fin = this.convertDate(this.end_date);
    }

    Object.keys(this.formDocumento.value)
      .forEach( k => {
        if(!isNullOrUndefined(this.formDocumento.value[k]))
          switch (k){
            case 'sCode': request[k] = this.formDocumento.value[k].codigoSerie;
             break;
            case 'ssCode': request[k] = this.formDocumento.value[k].codigoSubSerie;
              break;
            default:  request[k] = this.formDocumento.value[k].codigo ||  this.formDocumento.value[k] ;
              break;

          }
      });

    if(request.tipo_comunicacion == 'GN' && (!isNullOrUndefined(request.series) || !isNullOrUndefined(request.subseries)))
      request.dep_code = this.dependenciaSelected.codigo;
    return request;
  }

  selectSerie(evt) {
    this.subseriesObservable$ = evt ?
      this
        .serieSubSerieService
        .getSubseriePorDependenciaSerie(this.formDocumento.get("depCode").value,evt.value.codigoSerie,evt.value.nombreSerie)
        .map(list => {
          list.unshift({codigoSubSerie:null,nombreSubSerie:"Seleccione"});
          return list;
        })
      : Observable.empty();

      this.tipologiaPaylod = {
        codSerie: evt.value.codigoSerie,
        codSubserie: '',
      };

      this.tipologiaDoc$ = this.instrumento.getTiplogiasDocumentales(this.tipologiaPaylod );
  }

  selectSubserie(event){

    this.tipologiaPaylod.codSubserie = event.value.codigoSubSerie;

    this.tipologiaDoc$ = this.instrumento.getTiplogiasDocumentales(this.tipologiaPaylod );
  }

  selectDocumento(evt){
    this.documentSelected$ = this._consultaApi.getDetalleDocumento(evt.data.idDocumento)
                                 .map(doc =>  doc.historialVersiones).share();
  }

  showDocument(documento: any) {
    this.showPDF= true;
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

  get versiones(){
    return isNullOrUndefined(this.documentSelected) ? [] : [...[this.documentSelected],...(this.documentSelected.historialVersiones || [])];
  }
  ngOnDestroy() {
    this.subscribers.forEach(obs => {
      obs.unsubscribe();
    });
  }
}
