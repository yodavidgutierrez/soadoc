import {ChangeDetectorRef, Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {ArchivarDocumentoModel} from "../../models/archivar-documento.model";
import {ConfirmationService, FileUpload} from "primeng/primeng";
import {UnidadDocumentalApiService} from "../../../../../../infrastructure/api/unidad-documental.api";
import {Observable} from "rxjs/Observable";
import {Store} from "@ngrx/store";
import  {State as RootState} from "../../../../../../infrastructure/redux-store/redux-reducers";
import {
  getAuthenticatedFuncionario,
  getSelectedDependencyGroupFuncionario
} from "../../../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {isNullOrUndefined} from "util";
import {TASK_PRODUCIR_DOCUMENTO} from "../../../../../../infrastructure/state-management/tareasDTO-state/task-properties";
import {AnexoDTO} from "../../../../produccion-documental/models/DocumentoDTO";
import {combineLatest} from "rxjs/observable/combineLatest";
import {Subscription} from "rxjs/Subscription";
import {DependenciaDTO} from "../../../../../../domain/dependenciaDTO";
import {environment} from "../../../../../../../environments/environment";
import {TipologiaDocumentaService} from "../../../../../../infrastructure/api/tipologia-documenta.service";
import {oa_dataSource} from "../../../data-source";
import {PushNotificationAction} from "../../../../../../infrastructure/state-management/notifications-state/notifications-actions";
import {InstrumentoApi} from "../../../../../../infrastructure/api/instrumento.api";
import {Utils} from "../../../../../../shared/helpers";
import {Utf8Helper} from "../../../../../../shared/utf8";
import {ALLOWEDTYPES} from "../../../../../../shared/bussiness-properties/mime-types";

@Component({
  selector: 'app-seleccionar-documentos',
  templateUrl: './seleccionar-documentos.component.html',
  styleUrls: ['./seleccionar-documentos.component.css'],
  providers:[ConfirmationService]
})
export class SeleccionarDocumentosComponent implements OnInit,OnDestroy {

  @Input() archivarDocumentoModel:ArchivarDocumentoModel;

  @Input() nroRadicado ;

    documentos:any[] = [];

  documentosArchivados:any[];

  documentosAdjuntos:any[] = [];

  documentPreview:boolean = false;

  dependenciaSelected:DependenciaDTO;

  idEcm:any;

  nombreDocumento:string;

  idDocumento:string;

  tipologiaDocumental:string;

  subscriptions:Subscription[] = [];

  tipologiaDocumentales$:Observable<any[]>;

  documentoRadicadoArchivado = false;

  hasDefaultDocumentForSave = false;

  @Output() onAfterInit:EventEmitter<any> = new EventEmitter<any>();
  @Output() onArchiveDone: EventEmitter<any> = new EventEmitter<any>();


  constructor(private _confirmationService:ConfirmationService,
              private _udService:UnidadDocumentalApiService,
              private _store:Store<RootState>,
              private instrumento: InstrumentoApi,
              private changeDetectorRef:ChangeDetectorRef
  ) { }

  ngOnInit() {
    const payload:any = {
      codSerie: this.archivarDocumentoModel.UnidadDocumental.codigoSerie,
      codSubserie: !isNullOrUndefined( this.archivarDocumentoModel.UnidadDocumental.codigoSubSerie) ? this.archivarDocumentoModel.UnidadDocumental.codigoSubSerie:''
    };

    this.FillListasDocumentos();

    const observable = this.instrumento.getTiplogiasDocumentales(payload).share();

    this.tipologiaDocumentales$ = observable;

    observable.subscribe(() => {
      this.onAfterInit.emit(this);
      this.changeDetectorRef.detectChanges();
    })
  }

  confirmArchivarDocumentos(){
    this._confirmationService.confirm({
      message: `¿Está seguro de archivar el documento en la carpeta ${this.archivarDocumentoModel.UnidadDocumental.nombreUnidadDocumental}?`,
      header: 'Confirmacion',
      icon: 'fa fa-question-circle',
      accept: () => {

        if(  this.archivarDocumentoModel.Documentos.length == 0){
          this._store.dispatch(new PushNotificationAction({
            severity: 'error', summary: 'Debe seleccionar algun documento para archivar'
          }));

          return;
        }

        const validDocuments = [this.archivarDocumentoModel
                               .Documentos
                               .every( document => !isNullOrUndefined(document.tipologiaDocumental))

                               ];


        if(!validDocuments.every( r => r)){

              this._store.dispatch(new PushNotificationAction({
            severity: 'error', summary: 'Algun documento que desea archivar no tiene la tipologia documental. Revise e agregela'
          }));
           return ;
        }

         if (this.documentos.length > this.archivarDocumentoModel.Documentos.length){
            this._store.dispatch(new PushNotificationAction({
              severity: 'error', summary: 'Debe seleccionar todos los documentos'
            }));
            return ;
          }


      this._udService.archivarDocumento(this.archivarDocumentoModel.getUnidadDocumentalParaSalvar())
          .subscribe(response  =>{
              if(response.codMensaje == '0000'){
                if(!isNullOrUndefined(this.nroRadicado) )
                   this.documentoRadicadoArchivado = true;
                this.FillListasDocumentos();

                if(this.hasDefaultDocumentForSave){
                  this.onArchiveDone.emit();
                  this.hasDefaultDocumentForSave = false;

                }
              }
              else{
                this._store.dispatch(new PushNotificationAction({ severity: 'error', summary: response.mensaje}));
              }
          });
      },
      reject: () => {

      }
    });
  }

  private FillListasDocumentos(){

    const observable = this._store.select(getSelectedDependencyGroupFuncionario);

    this.setDocumentosPorArvichar(observable);

    this.subscriptions.push(
      this._udService
        .listarDocumentosArchivadosPorUd(this.archivarDocumentoModel.UnidadDocumental.ecmObjId)
        .subscribe( docs => {
          this.documentosArchivados = docs;

          this.changeDetectorRef.detectChanges();
        })
    );

    this.changeDetectorRef.detectChanges();
  }

  private setDocumentosPorArvichar(observable:Observable<DependenciaDTO>){

    let newObservable:Observable<any[]>;

    if(!isNullOrUndefined(this.nroRadicado)){

     newObservable= this.documentoRadicadoArchivado ? Observable.of([]) :  combineLatest(observable.switchMap(dependencia =>  this._udService.listarDocumentosArchivadosPorDependencia(dependencia.codigo)),
                                   this._udService.obtenerDocumentoPorNoRadicado(this.nroRadicado).map( r => r.documentoDTOList.map(
                                   doc => {
                                       doc.identificador = !isNullOrUndefined(doc.nroRadicado) && doc.nroRadicado != 'null' ? doc.nroRadicado : doc.idDocumento;
                                       doc.isAttachment = false;
                                       return doc;
                                     }
                                   )))
                            .map(([archivados,radicados]) => { console.log("archivados",archivados); console.log("radicados",radicados) ;return radicados.filter( d => archivados.every( a => a.idDocumento != d.identificador))});

    }

    else{
      newObservable = Observable.of([]);

        /*observable.switchMap(dependencia => {

        return this._udService.listarDocumentosPorArchivar(dependencia.codigo)
          .map( response =>  isNullOrUndefined(response.documentoDTOList) ? [] : response.documentoDTOList.map( doc => {
            doc.identificador = !isNullOrUndefined(doc.nroRadicado) && doc.nroRadicado != 'null' ? doc.nroRadicado : doc.idDocumento;
            doc.isAttachment = false;
            return doc;
          }),(outer,inner) => inner);
      });*/
    }


    this.subscriptions.push( newObservable.subscribe(documentos => {
      this.documentos = documentos;
      this.changeDetectorRef.detectChanges();
    }));
    }

  toggleDocuments(event:any,checked?:boolean){


    if(!isNullOrUndefined(event.checked)){
      checked = event.checked;
    }

    if(checked){
      if(!isNullOrUndefined(event.data)){
        const index = this.archivarDocumentoModel.Documentos.findIndex( document => document.idDocumento == event.data.idDcoumento);

         if(isNullOrUndefined(index) || index == -1)
         this.archivarDocumentoModel.Documentos.push(event.data);
        else
          this.archivarDocumentoModel.Documentos[index] = event.data;
      }
      else
        this.archivarDocumentoModel.Documentos = this.documentos;
      }
    else{

      if(!isNullOrUndefined(event.data)){
        const index  = this.archivarDocumentoModel.Documentos
          .findIndex( document => document.idDocumento == event.data.idDocumento);

        this.archivarDocumentoModel.Documentos.splice(index,1);
      }
      else
        this.archivarDocumentoModel.Documentos = [];

    }
    console.log("documentos por archivar", this.archivarDocumentoModel.Documentos);
  }

  showDocumento(idDocumento,evento?){

    if(!isNullOrUndefined(evento))
      evento.preventDefault();

    this.idEcm = idDocumento;

    this.documentPreview = true;
  }

  hidePdf(){

    this.documentPreview = false;
  }

  uploadDocuments(event:any,uploader:FileUpload){

      this.subscriptions.push(
        combineLatest( this._store.select(getSelectedDependencyGroupFuncionario),this._store.select(getAuthenticatedFuncionario))
       .subscribe( ([dependecia,funcionario])=> {

        const formData = new FormData();

        event.files.forEach( file => {
          formData.append('file',file,encodeURI(file.name));
        });


        formData.append("codigoDependencia",dependecia.codigo);
        formData.append("autor",Utils.funcionarioFunllName(funcionario));

        this.subscriptions.push(
          this._udService.subirDocumentosParaArchivar(formData)
            //.mapTo(oa_dataSource.documentos_adjuntos_por_archivar)
            .subscribe( documents => {

              if(documents.codMensaje == 1224){

                this._store.dispatch(new PushNotificationAction({
                  severity: 'error', summary: 'DOCUMENTO DUPLICADO, NO PUEDE ADJUNTAR EL DOCUMENTO'
                }));
              }

              else{
                const docs = documents.documentoDTOList;

                if(!isNullOrUndefined(docs) && docs.length > 0)
                  this.documentosAdjuntos =  this.documentosAdjuntos.concat(docs);

                this.agregarAdjuntos();

                this.changeDetectorRef.detectChanges();
              }

            },null,() => {
              uploader.clear();
              this.changeDetectorRef.detectChanges();
            }));
      })
    );


  }

  eliminarDocumento(idEcm,index){

    this.subscriptions.push(this._udService.eliminarDocumento(idEcm).subscribe( () => {

      this.documentosAdjuntos.splice(index,1);
      this.documentosAdjuntos = [... this.documentosAdjuntos];

      this.changeDetectorRef.detectChanges();

    }));


  }

  agregarAdjuntos(){

  const docAttached = this.documentosAdjuntos
                    .map( doc => Object.assign({isAttachment:true,identificador:doc.idDocumento},doc));

  console.log(docAttached);

  if(!isNullOrUndefined(this.documentos) && this.documentos.length > 0)
     this.documentos =  docAttached.concat(this.documentos);
  else
  this.documentos =  docAttached;

  this.documentosAdjuntos = [];

  this.changeDetectorRef.detectChanges();
  }

  onErrorUpload(event){


  }

  updateDocumentMeta(row,evento){

    const dIndex = this.archivarDocumentoModel.Documentos
                      .findIndex( document => document.idDocumento ==  row.idDocumento);

    this.archivarDocumentoModel.Documentos[dIndex]= row;

    console.log("documentos por archivar dropdowns",this.archivarDocumentoModel.Documentos);

    if(!isNullOrUndefined(evento.originalEvent))
      evento.originalEvent.stopPropagation();

  }

  DropSubscriptions(){
    this.subscriptions.forEach( subscription => subscription.unsubscribe());

  }

  ngOnDestroy(): void {

    this.DropSubscriptions();
  }

  IsSelected(id):boolean{

    return this.archivarDocumentoModel.Documentos.some( document => document.idDocumento == id);
  }

  get allowedTypes(){

    return ALLOWEDTYPES.join(',');
  }

}
