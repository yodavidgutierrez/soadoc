import {ChangeDetectorRef, Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {VALIDATION_MESSAGES} from '../../../../../../shared/validation-messages';
import {Observable} from "rxjs/Observable";
import {State as RootState} from "../../../../../../infrastructure/redux-store/redux-reducers";
import {Store} from "@ngrx/store";
import {SerieService} from "../../../../../../infrastructure/api/serie.service";


import {
  getAuthenticatedFuncionario,
  getSelectedDependencyGroupFuncionario
} from "../../../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {DependenciaDTO} from "../../../../../../domain/dependenciaDTO";
import {Subscription} from "rxjs/Subscription";
import {SolicitudCreacionUdService} from "../../../../../../infrastructure/api/solicitud-creacion-ud.service";
import {SerieDTO} from "../../../../../../domain/serieDTO";
import {UnidadDocumentalDTO} from "../../../../../../domain/unidadDocumentalDTO";
import {UnidadDocumentalApiService} from "../../../../../../infrastructure/api/unidad-documental.api";
import {ArchivarDocumentoModel} from "../../models/archivar-documento.model";
import {SolicitudCreacioUdModel} from "../../models/solicitud-creacio-ud.model";
import {isNullOrUndefined} from "util";
import {Guid} from "../../../../../../infrastructure/utils/guid-generator";
import {ConfirmationService} from "primeng/primeng";
import {SubserieDTO} from "../../../../../../domain/subserieDTO";
import {LoadingService} from "../../../../../../infrastructure/utils/loading.service";
import {FuncionariosService} from "../../../../../../infrastructure/api/funcionarios.service";
import {FuncionarioDTO} from "../../../../../../domain/funcionarioDTO";
import {observable} from "rxjs/symbol/observable";
import {combineLatest} from "rxjs/observable/combineLatest";
import {getActiveTask} from "../../../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors";
import {TareaDTO} from "../../../../../../domain/tareaDTO";



@Component({
  selector: 'app-seleccionar-unidad-documental',
  templateUrl: './seleccionar-unidad-documental.component.html',
  styleUrls: ['./seleccionar-unidad-documental.component.scss'],
  providers:[ConfirmationService]
})
export class SeleccionarUnidadDocumentalComponent implements OnInit, OnDestroy {

  @Input() archivarDocumentoModel:ArchivarDocumentoModel;
  @Input() solicitudModel:SolicitudCreacioUdModel;

  @Output() onChangeSection:EventEmitter<string> = new EventEmitter;

  @Output() onSelectUD:EventEmitter<any> = new EventEmitter;

  @Output() onDeselectUD:EventEmitter<any> = new EventEmitter;

  @Input() nroRadicado;

  first = 0;

  form: FormGroup;

  operation:string = "bUnidadDocumental";

  seriesObservable$:Observable<SerieDTO[]>;

  listaSeries : SerieDTO[];

  allSubSeriesObservable$:Observable<SubserieDTO[]>;

  unidadesDocumentales$:Observable<UnidadDocumentalDTO[]>;

  subscriptions:Subscription[] = [];

  globalDependencySubscriptor:Subscription;

   subseriesObservable$:Observable<any[]>;

  dependenciaSelected$ : Observable<any>;

  dependenciaSelected : DependenciaDTO;

  documentos: any[] = [];

  validations: any = {};

  idEcm;

  documentPreview:boolean = false;

   constructor(
     private formBuilder: FormBuilder
     , private serieSubSerieService:SerieService
     ,private _store:Store<RootState>
     ,private _solicitudUDService:SolicitudCreacionUdService
     ,private _udService:UnidadDocumentalApiService
     ,private _confirmationService:ConfirmationService
     ,private  changeDetector:ChangeDetectorRef
     ,private loadingService:LoadingService
       ) {

    this.dependenciaSelected$ = this._store.select(getSelectedDependencyGroupFuncionario);

     this.initForm();
  }

  initForm() {
    this.form = this.formBuilder.group({
      'serie': [null, Validators.required],
      'subserie': [null],
      'identificador': [null, Validators.required],
      'nombre': [null, Validators.required],
      'descriptor1': [null],
      'descriptor2': [null],
      'observaciones': [null,Validators.required],
     // 'operation'  :["bUnidadDocumental"],
    });

    this.form.get('serie').valueChanges.subscribe(value => this.selectSerie(!isNullOrUndefined(value) ? {value: value} : null));
  }

  ngOnDestroy(): void {

     this.subscriptions.forEach( subscription => subscription.unsubscribe());

     }

  ngOnInit(): void {
       this.subscriptions.push(this.dependenciaSelected$.subscribe(result => {
         this.seriesObservable$ = this
           .serieSubSerieService
           .getSeriePorDependencia(result.codigo)
           .map(list => {

             if (isNullOrUndefined(list))
               list = [];

             this.listaSeries = list;

             list.unshift({codigoSerie: null, nombreSerie: "Seleccione"});

             return list;
           }).share();

         this.allSubSeriesObservable$ = this.serieSubSerieService.getSubSeriePorDependencia(result.codigo).share();

         this.dependenciaSelected = result;
         if(!isNullOrUndefined(this.nroRadicado))  {

           this._udService.obtenerDocumentoPorNoRadicado(this.nroRadicado)
             .map( r => r.documentoDTOList.map( doc => {
               doc.identificador = !isNullOrUndefined(doc.nroRadicado) && doc.nroRadicado != 'null' ? doc.nroRadicado : doc.idDocumento;
               return doc;
             }))
             .subscribe( docs =>  {
               this.documentos = docs;
               this.changeDetector.detectChanges();
             });
         }
       }));


   this.form.get("subserie").valueChanges.subscribe(v => {

     if(isNullOrUndefined(v))
       this.form.get("subserie").setErrors({required:true});

   })

  }

  addSolicitud(){

     if (this.form.valid) {

       this.subscriptions.push(
         combineLatest(this._store.select(getAuthenticatedFuncionario), this._store.select(getActiveTask))
         .subscribe( ([funcionario, task]) => {

           const subserie = this.getControlValue('subserie');

           this.solicitudModel.Solicitudes = [ ... this.solicitudModel.Solicitudes,{
             codigoSede:this.dependenciaSelected.codSede,
             codigoDependencia:this.dependenciaSelected.codigo,
             codigoSerie: this.getControlValue("serie").codigoSerie,
             nombreSerie: this.getControlValue("serie").nombreSerie,
             codigoSubSerie: !isNullOrUndefined(subserie) ? subserie.codigoSubSerie : null,
             nombreSubSerie: !isNullOrUndefined(subserie) ? subserie.nombreSubSerie : null,
             descriptor1: this.getControlValue("descriptor1"),
             descriptor2: this.getControlValue("descriptor2"),
             id: this.getControlValue("identificador"),
             nombreUnidadDocumental: this.getControlValue("nombre"),
             observaciones: this.getControlValue("observaciones"),
             nro:  Guid.next(),
             estado: '',
             idSolicitante: funcionario.id.toString(),
             idConstante: isNullOrUndefined(task.idInstanciaProceso.toString()) ? '' : task.idInstanciaProceso.toString()
           }];


           this.unidadesDocumentales$ = Observable.of(this.solicitudModel.Solicitudes);

           this.changeDetector.detectChanges();

           this.form.reset();
         })
     );



     }


  }

  private getControlValue(identificador:string):any{

     const value = this.form.get(identificador).value;

    return  isNullOrUndefined(value) ? '' : value;
  }

  listenForErrors() {
    this.bindToValidationErrorsOf('tipoComunicacion');
    this.bindToValidationErrorsOf('tipologiaDocumental');
    this.bindToValidationErrorsOf('numeroFolio');
    this.bindToValidationErrorsOf('asunto');
    this.bindToValidationErrorsOf('tipoAnexosDescripcion');
    this.bindToValidationErrorsOf('empresaMensajeria');
    this.bindToValidationErrorsOf('numeroGuia');
  }

  listenForBlurEvents(control: string) {
     const ac = this.form.get(control);

     if(control !== 'serie' && control != 'subserie' )
       if(!isNullOrUndefined(ac) && !isNullOrUndefined(ac.value))
         ac.setValue(ac.value.toString().trim());
    this.validations[control] = null;
    if (ac.touched && ac.invalid) {
      const error_keys = Object.keys(ac.errors);
      let last_error_key = error_keys[error_keys.length - 1];

      if(last_error_key == 'pattern')
       last_error_key = 'required'
      this.validations[control] = [...VALIDATION_MESSAGES[last_error_key]];
    }
  }

  bindToValidationErrorsOf(control: string) {
    const ac = this.form.get(control);
    ac.valueChanges.subscribe(() => {
      if ((ac.touched || ac.dirty) && ac.errors) {
        const error_keys = Object.keys(ac.errors);
        const last_error_key = error_keys[error_keys.length - 1];
        this.validations[control] = VALIDATION_MESSAGES[last_error_key];
      } else {
        delete this.validations[control];
      }
    });
  }

   selectSerie(evt)
  {

    this.subseriesObservable$ = evt ?
      this
        .serieSubSerieService
        .getSubseriePorDependenciaSerie(this.dependenciaSelected.codigo,evt.value.codigoSerie,evt.value.nombreSerie)
        .map(list => {
          list.unshift({codigoSubSerie:null,nombreSubSerie:"Seleccione"});
          return list;
        }).do(list => {

           if(list.length  == 1)
              this.form.get("subserie").setErrors(null);
           else{
             const v = this.form.get("subserie").value;

             if(isNullOrUndefined(v))
               this.form.get("subserie").setErrors({required:true});
           }

          this.changeDetector.detectChanges();
      })
      : Observable.empty();
  }

     buscarUnidadDocumental(){

    //  this.visiblePopup = true;

       this.first = 0;

       const subserie = this.form.get('subserie').value;
       const serie = this.form.get('serie').value;

       const observable = this._store.select(getSelectedDependencyGroupFuncionario).switchMap( dependencia => this._udService.Listar({
         codigoDependencia : dependencia.codigo,
         codigoSerie: !isNullOrUndefined(serie) ? serie.codigoSerie : undefined,
         codigoSubSerie: !isNullOrUndefined(subserie) ? subserie.codigoSubSerie : undefined,
         id: !isNullOrUndefined(this.form.get('identificador').value) ? this.form.get('identificador').value : undefined ,
         nombreUnidadDocumental:!isNullOrUndefined(this.form.get('nombre').value) ? this.form.get('nombre').value : undefined,
         descriptor1: !isNullOrUndefined(this.form.get('descriptor1').value) ? this.form.get('descriptor1').value : undefined  ,
         descriptor2: !isNullOrUndefined(this.form.get('descriptor2').value) ? this.form.get('descriptor2').value : undefined  ,
       })).map( response => response.unidadDocumental).share();

      this.unidadesDocumentales$ = observable;

      this.subscriptions.push( observable.subscribe( uds => {

        if( isNullOrUndefined(uds) || uds.length ==0){
          this._confirmationService.confirm({
            message: 'El sistema no encuentra la unidad documental que está buscando.\n Por favor, solicite su creación',
            header: 'Resultados no encontrados',
            icon: 'fa fa-question-circle',
            accept: () => {

              this.operation = 'solicitarUnidadDocumental';

             this.changeSection('solicitarUnidadDocumental');
            },
            reject: () => {}
          });
        }
      }))

    }

    limpiar(){
      this.form.reset();
      this.unidadesDocumentales$ = Observable.of();
    }

    changeSection( section:string){

      Observable.timer(100).subscribe(_ => {
        this.loadingService.dismissLoading();
      });

      this.solicitudModel.Solicitudes = [];

      this.unidadesDocumentales$ = Observable.empty();

      this.onChangeSection.emit(section);
    }


    selectUnidadDocumental(evt){

     this.archivarDocumentoModel.UnidadDocumental = evt.data;



     this.onSelectUD.emit(evt.data);
    }

  deselectUnidadDocumental(evt){

     this.onDeselectUD.emit(evt.data);
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

  removeSolicitud(index:number){

     this.solicitudModel.removeAtIndex(index);

     this.unidadesDocumentales$ = Observable.of(this.solicitudModel.Solicitudes);

  }




}

