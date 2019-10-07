import {ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, OnDestroy, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SupertypeSeries} from "../../shared/supertype-series";
import  {State as RootState} from "../../../../../infrastructure/redux-store/redux-reducers";
import {Store} from "@ngrx/store";
import {SerieService} from "../../../../../infrastructure/api/serie.service";
import {Observable} from "rxjs/Observable";
import {ConfirmationService} from "primeng/primeng";
import {UnidadDocumentalDTO} from "../../../../../domain/unidadDocumentalDTO";
import {UnidadDocumentalApiService} from "../../../../../infrastructure/api/unidad-documental.api";
import {isNull, isNullOrUndefined} from "util";
import {SolicitudCreacionUdService} from "../../../../../infrastructure/api/solicitud-creacion-ud.service";
import {SolicitudCreacioUdModel} from "../../archivar-documento/models/solicitud-creacio-ud.model";
import {PushNotificationAction} from "../../../../../infrastructure/state-management/notifications-state/notifications-actions";
import {FuncionarioDTO} from "../../../../../domain/funcionarioDTO";
import {Subscription} from "rxjs/Subscription";
import {
  getAuthenticatedFuncionario,
  getSelectedDependencyGroupFuncionario
} from "../../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {combineLatest} from "rxjs/observable/combineLatest";
import {MegafServiceApi} from "../../../../../infrastructure/api/megaf-service.api";
import  * as moment from 'moment';
import {ARCHIVO_GESTION} from "../../../../../shared/bussiness-properties/radicacion-properties";
import {DependenciaDTO} from "../../../../../domain/dependenciaDTO";

@Component({
  selector: 'app-form-crear-unidad-documental',
  templateUrl: './form-crear-unidad-documental.component.html',
})
export class FormCrearUnidadDocumentalComponent extends SupertypeSeries implements  OnChanges,OnDestroy{

  form:FormGroup;

  readonly tipoArchivo = ARCHIVO_GESTION;

  datosUnidadConservacion;

  datosUnidadConservacionIsValid = false;

  archivista:FuncionarioDTO;

  @Input() solicitudModel:SolicitudCreacioUdModel;

  unidadesDocumentales$:Observable<UnidadDocumentalDTO[]>;

  @Output() onCreateUnidadDocumental:EventEmitter<any>  = new EventEmitter;

  unidadDocumentalForCreate:any = {} ;

  subscriptions:Subscription[] = [];

  unidadesConservacion$:Observable<any[]>;

  unidadConservacioSelected ;

  constructor(private fb:FormBuilder,
              store:Store<RootState>,
              serieService:SerieService,
              changeDetector:ChangeDetectorRef,
              private confirmationService:ConfirmationService,
              private udService:UnidadDocumentalApiService,
              private solicitudService:SolicitudCreacionUdService,
              private _megafApi:MegafServiceApi,

              ) {

    super(store,serieService,changeDetector);

    this.formCrearUDInit();

   }

  private formCrearUDInit(){

    this.form = this.fb.group({
      serie:[null,Validators.required],
      subserie:[null],
      identificador:[null,Validators.required],
      nombre:[null,Validators.required],
      descriptor1:[null],
      descriptor2:[null],
      observaciones:[null],
      creada:[0],
      unidadFisica:[true]
    });

    this.listenForChange();
  }

   get unidadFisicaCreada(){ return   this.form.get('creada').value == 1 }

  relacionarUD(){
    this.confirmationService.confirm({
      message: '¿Está seguro que desea relacionar esta unidad documental?',
      header: 'Confirmacion',
      icon: 'fa fa-question-circle',
      accept: () => {
         this.unidadDocumentalForCreate.ubicacionTopografica = this.unidadConservacioSelected.codUbicacion;
         this.changeDetector.detectChanges();
      },
      reject: () => {

      }
    });
  }

  crearUD(){

     this.confirmationService.confirm({
      message: '¿Está seguro que desea crear esta unidad documental?',
      header: 'Confirmacion',
      icon: 'fa fa-question-circle',
      accept: () => {

        this.udService.crear(this.UnidadDocumentalRequestDto)
        .subscribe((response) => {

          if(response.codMensaje != '0000'){
            this._store
              .dispatch(new PushNotificationAction({ severity: 'error', summary: response.mensaje}));

            return;
          }

          this.solicitudModel.SolicitudSelected.codigoSerie = this.UnidadDocumentalRequestDto.unidadDocumentalDTO.codigoSerie;
          this.solicitudModel.SolicitudSelected.codigoSubSerie = this.UnidadDocumentalRequestDto.unidadDocumentalDTO.codigoSubSerie;
          this.solicitudModel.SolicitudSelected.id = this.UnidadDocumentalRequestDto.unidadDocumentalDTO.id;
          this.solicitudModel.SolicitudSelected.nombreUnidadDocumental = this.UnidadDocumentalRequestDto.unidadDocumentalDTO.nombreUnidadDocumental;
          this.solicitudModel.SolicitudSelected.descriptor1 = this.UnidadDocumentalRequestDto.unidadDocumentalDTO.descriptor1;
          this.solicitudModel.SolicitudSelected.descriptor2 = this.UnidadDocumentalRequestDto.unidadDocumentalDTO.descriptor2;

          this.solicitudService.actualizarSolicitudes(this.solicitudModel.SolicitudSelected).subscribe( () => this.onCreateUnidadDocumental.emit({action:"Creación UD"}) );

          this.solicitudModel.removeAtIndex();

          this.changeDetector.detectChanges();

        }, error => {});

      },
      reject: () => {
      }
    });
  }

  ngOnChanges(){

    if(this.solicitudModel.SelectedIndex > -1){

      const solicitud = this.solicitudModel.SolicitudSelected;

      this.subscriptions.push( this.dependenciaSelected$.subscribe( dependencia =>{

        if(isNullOrUndefined(this.seriesObservable)){
          this._serieSubserieService
            .getSeriePorDependencia(dependencia.codigo)
            .subscribe( list => {
              list.unshift({codigoSerie:null,nombreSerie:"Seleccione"});

              this.seriesObservable = [... list];

              const serie = this.seriesObservable.find( serie => serie.codigoSerie == solicitud.codigoSerie);
              this.form.get("serie").setValue(serie);

              this.loadSubseries(dependencia.codigo,serie.codigoSerie,serie.nombreSerie);

            })
        }
        else {
          const serie = this.seriesObservable.find( serie => serie.codigoSerie == solicitud.codigoSerie);
          this.loadSubseries(dependencia.codigo,serie.codigoSerie,serie.nombreSerie);
        }

      }));

      if(!isNullOrUndefined(this.seriesObservable))
      this.form.get("serie").setValue(this.seriesObservable.find( serie => serie.codigoSerie == solicitud.codigoSerie));
      this.form.get('identificador').setValue(solicitud.id);
      this.form.get("nombre").setValue(solicitud.nombreUnidadDocumental);
      this.form.get("descriptor1").setValue(solicitud.descriptor1);
      this.form.get("descriptor2").setValue(solicitud.descriptor2);
      this.form.get("observaciones").setValue(solicitud.observaciones);

    }
  }

  private loadSubseries(codigoDependencia,codigoSerie,nombreSerie){

    const solicitud = this.solicitudModel.SolicitudSelected;

    this._serieSubserieService.getSubseriePorDependenciaSerie(codigoDependencia,codigoSerie,nombreSerie)
      .subscribe( list => {
        list.unshift({codigoSubSerie:null,nombreSubSerie:"Seleccione"});
        this.subseriesObservable = list;
        const subserie = this.subseriesObservable.find(s => s.codigoSubSerie == solicitud.codigoSubSerie);
        if(!isNullOrUndefined(subserie))
        this.form.get('subserie').setValue(subserie);
      })
  }

  ngOnInit(){

   super.ngOnInit();

    this.form.get("subserie").valueChanges.subscribe(v => {

      if(isNullOrUndefined(v))
        this.form.get("subserie").setErrors({required:true});

    });


    combineLatest(this._store.select(getSelectedDependencyGroupFuncionario),this._store.select(getAuthenticatedFuncionario))
      .subscribe(([dependency,funcionario]) => {
        this.unidadDocumentalForCreate.codigoSede =dependency.codSede ;
        this.unidadDocumentalForCreate.codigoDependencia =dependency.codigo ;

          this.archivista = funcionario;
      })
      .unsubscribe();
  }

  selectSerie(evt)
  {

    if(evt){
      this
        ._serieSubserieService
        .getSubseriePorDependenciaSerie(this.dependenciaSelected.codigo,evt.value.codigoSerie,evt.value.nombreSerie)
        .map(list => {
          list.unshift({codigoSubSerie:null,nombreSubSerie:"Seleccione"});
          return list;
        }).subscribe(list => {

          this.subseriesObservable = list;


        this.form.get("subserie").clearValidators();

        if(list.length  > 1)
          this.form.get("subserie").setValidators(Validators.required);

        this.form.get("subserie").updateValueAndValidity();

        this.changeDetector.detectChanges();
      })
    }


  }

  updateDatosUnidadConservacion(event){

    const formValue = event.value;
    this.datosUnidadConservacionIsValid = event.isValid;
    if(isNullOrUndefined(this.datosUnidadConservacion))
       this.initDatosConservacion(this.form.value);
    this.datosUnidadConservacion.ideUbiCarpeta = !isNullOrUndefined( formValue.carpeta)?  formValue.carpeta.ide: null;
    this.datosUnidadConservacion.ideBodega = !isNullOrUndefined( formValue.bodega)?  formValue.bodega.ide: null ;
    }

 private  listenForChange(){

    this.form.valueChanges.subscribe( formValue => {

      if(!isNullOrUndefined( formValue.serie)) {
        this.unidadDocumentalForCreate.codigoSerie = formValue.serie.codigoSerie;
        this.unidadDocumentalForCreate.nombreSerie = formValue.serie.nombreSerie;
      }
      this.unidadDocumentalForCreate.codigoSubSerie = !isNullOrUndefined( formValue.subserie) ?  formValue.subserie.codigoSubSerie : null;
      this.unidadDocumentalForCreate.nombreSubSerie = !isNullOrUndefined( formValue.subserie) ?  formValue.subserie.nombreSubSerie : null;
      this.unidadDocumentalForCreate.id = formValue.identificador;
      this.unidadDocumentalForCreate.nombreUnidadDocumental = formValue.nombre;
      this.unidadDocumentalForCreate.descriptor1 = formValue.descriptor1;
      this.unidadDocumentalForCreate.descriptor2 = formValue.descriptor2;
      this.unidadDocumentalForCreate.observaciones = formValue.observaciones;

      if(!this.unidadFisicaCreada && this.haveUnidadFisica){

        this.initDatosConservacion(formValue);
        }
      else {
        this.datosUnidadConservacion = null;
        this.unidadDocumentalForCreate.ubicacionTopografica = null;
      }
    })
  }

  private initDatosConservacion(formValue){


    this.datosUnidadConservacion = {
      codUnidadDocumental: formValue.identificador,
      nomUnidadDocumental: formValue.nombre,
      descriptor1:!isNullOrUndefined(formValue.descriptor1) ? formValue.descriptor1 : " ",
      descriptor2:!isNullOrUndefined(formValue.descriptor2) ? formValue.descriptor2 : "",
      descripcion:formValue.observaciones
    };

    if( !isNullOrUndefined(this.unidadDocumentalForCreate)){
      this.datosUnidadConservacion.codOrganigrama = this.unidadDocumentalForCreate.codigoDependencia;
      if(!isNullOrUndefined(formValue.serie)){

        let codigos = `${this.unidadDocumentalForCreate.codigoDependencia}-${formValue.serie.codigoSerie}`;

        if(!isNullOrUndefined(formValue.subserie))
          codigos = `${codigos}-${formValue.subserie.codigoSubSerie}`;

        this.datosUnidadConservacion.codigos = codigos ;

      }
    }

  }

  get UnidadDocumentalRequestDto(){

    const unidadDocumentalRequestDto:any = {
      unidadDocumentalDTO:this.unidadDocumentalForCreate,
      idSolicitante : this.solicitudModel.SolicitudSelected.idSolicitante,
      idArchivista : this.archivista.id
    };

    if(!this.unidadFisicaCreada && this.haveUnidadFisica){
      this.datosUnidadConservacion.codOrganigrama = this.unidadDocumentalForCreate.codigoDependencia;
      this.datosUnidadConservacion.fechaInicial = moment().format("DD-MM-YYYY");
      this.datosUnidadConservacion.fechaFinal = moment().format("DD-MM-YYYY");

       unidadDocumentalRequestDto.unidadConservacion = this.datosUnidadConservacion;
    }


    return unidadDocumentalRequestDto;
  }

  get haveUnidadFisica(){

    return this.form.get('unidadFisica').value;
  }

  get dataValid(){

    const validations = [this.form.valid];

    if(this.haveUnidadFisica) {

      if (!this.unidadFisicaCreada)
        validations.push(this.datosUnidadConservacionIsValid);
      else
        validations.push(!isNullOrUndefined(this.unidadDocumentalForCreate.ubicacionTopografica));
    }

    return validations.every( valid => valid);
  }

  buscar(){
    this.unidadesConservacion$ = combineLatest(
      this._megafApi.getUnidadesFisicas(this.requestToList),
      this._store.select(getSelectedDependencyGroupFuncionario)
      )
      .map( ([list,dep]) => {

        let idUnidadDocumental = "";

        return  (<any[]> list)
                .filter( uc => uc.codOrg == dep.codigo)
                .reduce((prev,curr) => {

          const partsIdUniDoc = curr.idUniDoc.split('-');

          const currIdUniDoc = partsIdUniDoc[0];

          if(prev.length === 0|| currIdUniDoc != idUnidadDocumental){
            curr.idUniDoc = currIdUniDoc;
              prev.push(curr);
              idUnidadDocumental = currIdUniDoc;
          }
          else{
            prev[prev.length - 1].codUbicacion = `${prev[prev.length - 1].codUbicacion},${curr.codUbicacion}`;
            prev[prev.length - 1].direccion = `${prev[prev.length - 1].direccion},${curr.direccion}`;
          }
          return prev;
        },[]);


      });


  }

 private get requestToList(){

    const  formValue = this.form.value;

    const request:any = {};


    Object.keys(formValue)
      .filter( key => formValue[key])
      .forEach( k => {

         switch (k){
           case 'serie' : request.idSerie = formValue.serie.codigoSerie;
            break;
           case 'subserie' :if(!isNullOrUndefined(formValue.subserie))
                             request.idSubserie = formValue.subserie.codigoSubSerie;
             break;
           case 'identificador' : request.idUniDoc = formValue.identificador;
             break;
           case 'nombre' : request.nomUnidadDoc = formValue.nombre.toUpperCase();
             break;
           case 'descriptor1' : request.desc1 = formValue.descriptor1;
             break;
           case 'descriptor2' : request.desc2 = formValue.descriptor2;
             break;
         }
      });



    return request;

  }

  buttonSearchIsEnable(){

    const validations = [this.form.get('serie').valid,this.form.get('subserie').valid];

    return validations.every(v => v);
  }

  ngOnDestroy(): void {

    this.globalDependencySubscriptor.unsubscribe();

    this.subscriptions.forEach(s => s.unsubscribe());
  }



}
