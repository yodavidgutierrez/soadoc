import {
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
  ViewChild,
  ViewEncapsulation
} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';

import {
  getMediosRecepcionArrayData,
  getTipoAnexosArrayData,
  getTipoComunicacionArrayData,
  getTipologiaDocumentalArrayData,
  getUnidadTiempoArrayData,
  getSoporteAnexoArrayData
} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-selectors';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { Dropdown } from 'primeng/components/dropdown/dropdown';
import 'rxjs/add/operator/single';
import {VALIDATION_MESSAGES} from 'app/shared/validation-messages';
import {DatosGeneralesApiService} from '../../../infrastructure/api/datos-generales.api';
import {createSelector} from 'reselect';
import {getUnidadTiempoEntities} from '../../../infrastructure/state-management/constanteDTO-state/selectors/unidad-tiempo-selectors';
import {LoadAction as SedeAdministrativaLoadAction} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-actions';
import {
  COMUNICACION_EXTERNA,
  COMUNICACION_EXTERNA_ENVIADA,
  CORREGIR_RADICACION,
  MEDIO_RECEPCION_CORREO_CERTIFICADO,
  RADICACION_DOC_PRODUCIDO,
  RADICACION_ENTRADA,
  RADICACION_INTERNA,
  RADICACION_SALIDA,
  TIPOLOGIA_DERECHO_PETICION
} from '../../../shared/bussiness-properties/radicacion-properties';
import {ViewFilterHook} from "../../../shared/ViewHooksHelper";
import {ExtendValidators} from "../../../shared/validators/custom-validators";
import {isNullOrUndefined} from "util";
import {Subscription} from "rxjs/Subscription";

@Component({
  selector: 'app-datos-generales',
  templateUrl: './datos-generales.component.html',
  styles: [`
    .ui-datalist-header, .ui-datatable-header {
      text-align: left !important;
    }
  `],
  encapsulation: ViewEncapsulation.None
})
export class DatosGeneralesComponent implements OnInit,OnDestroy {

  form: FormGroup;
  visibility: any = {};



  radicadosReferidos: Array<{ nombre: string,blocked?:boolean }> = [];
  descripcionAnexos: Array<{ tipoAnexo: ConstanteDTO, descripcion: string, soporteAnexo: ConstanteDTO }> = [];

  tipoComunicacionSuggestions$: Observable<ConstanteDTO[]>;
  unidadTiempoSuggestions$: Observable<ConstanteDTO[]>;
  tipoAnexosSuggestions$: Observable<ConstanteDTO[]>;
  soporteAnexosSuggestions$: Observable<ConstanteDTO[]>;
  medioRecepcionSuggestions$: Observable<ConstanteDTO[]>;
  tipologiaDocumentalSuggestions$: Observable<ConstanteDTO[]>;
  metricasTiempoTipologia$: Observable<any>;

  tiempoRespuestaMetricaTipologia$: Observable<number> = Observable.of(null);
  codUnidaTiempoMetricaTipologia$: Observable<ConstanteDTO> = Observable.of(null);

  detallarDescripcion = false;

  @Input() dataDefault:Observable<any> = Observable.empty() ;

  @Input()
  documentoProducido= false;

  @Input() fieldsDisabled: any = {};

  @Input()
  medioDocumentoProducido=true;

  @Input()
  editable:any = true;

  // @Input() tipoRadicacion = RADICACION_ENTRADA;
  @Input() tipoRadicacion;

  @Input()
  editmode = false;

  @Input()
  mediosRecepcionInput: ConstanteDTO = null;

  @Input() tipoComunicacion : (tipo:ConstanteDTO)=>boolean = null ;

  @Input() tipologiaDocFilter : (tipo:ConstanteDTO)=>boolean = null;

  @Output()
  onChangeTipoComunicacion: EventEmitter<any> = new EventEmitter();

  @Output() onChangeTipoDistribucion: EventEmitter<boolean> = new EventEmitter;

  subcriptions:Subscription[] = [];

  validations: any = {};


  // @ViewChild('dropDownThing')
  // dropDownThing: Dropdown;


  constructor(private _store: Store<State>, private _apiDatosGenerales: DatosGeneralesApiService, private formBuilder: FormBuilder,private changeDetector:ChangeDetectorRef) {

  }

  initForm() {

    const asuntoValidators= [Validators.maxLength(500)];

     if(this.isRadicacionSalida())
        asuntoValidators.push(Validators.required);

    let tipoComunicacionDefault = null;

    if(this.isRadicacionEntrada()){
     tipoComunicacionDefault = COMUNICACION_EXTERNA;
    }

    if(this.tipoRadicacion == RADICACION_SALIDA){
     tipoComunicacionDefault = COMUNICACION_EXTERNA_ENVIADA;
    }

    let fields:any = {
      'fechaRadicacion': [null],
      'nroRadicado': [null],
      'tipoComunicacion': [{value: null, disabled: !this.isEditable("tipoComunicacion") }, Validators.required],
      'medioRecepcion': [{value: null, disabled: !this.isEditable("medioRecepcion")}, this.isRadicacionEntrada() ? Validators.required: null],
      'empresaMensajeria': [{value: null, disabled: true}],
      'numeroGuia': [{value: null, disabled: true}, Validators.compose([Validators.required, Validators.maxLength(20)])],
      'tipologiaDocumental': [{value: null, disabled:!this.isEditable("tipologiaDocumental") }, Validators.required],
      'unidadTiempo': [null],
      'numeroFolio': [{value: null, disabled:!this.isEditable("numeroFolio") }, this.tipoRadicacion != RADICACION_DOC_PRODUCIDO ?  Validators.required : null],
      'inicioConteo': [null],
      'reqDistFisica': [{value: true, disabled: !this.isEditable("reqDistFisica")}],
      'reqDistElect': [{value: null, disabled: !this.isEditable("reqDistElect")}],
      'reqDigit': [{value: null, disabled: !this.isEditable("reqDigit")}],
      'adjuntarDocumento':[{value:null,disabled:!this.isEditable("adjuntarDocumento")}],
      'asunto': [{value: null, disabled: !this.isEditable("asunto")}, Validators.compose(asuntoValidators)],
      'radicadoReferido': [{value: null, disabled: !this.isEditable("radicadoReferido")}],
      'detallarDescripcion':[{value: null, disabled: !this.isEditable("detallarDescripcion")}],
      'tipoAnexos': [{value: null, disabled: !this.isEditable("tipoAnexos")}],
      'soporteAnexos': [{value: null, disabled: !this.isEditable("soporteAnexos")}],
      'tipoAnexosDescripcion': [{value: null, disabled: !this.isEditable("tipoAnexosDescripcion")}, Validators.maxLength(300)],
      'hasAnexos': [null]
    };


    if(this.isRadicacionEntrada())
      fields.tiempoRespuesta = [{value: null, disabled: !this.editable}];

      this.form = this.formBuilder.group(fields);

    if(this.isRadicacionSalida()){
      this.form.setValidators((fr:FormGroup) => {

        if(isNullOrUndefined(fr.get('reqDistFisica').value) && isNullOrUndefined(fr.get('reqDistElect').value))
           return {
             reqDistFisicaOrReqDistElect:{
               valid:false
             }
           };
        return null;
          });
    }

    if(this.isRadicacionEntrada()){

      this.form.get('tipologiaDocumental').valueChanges.subscribe( value => {

        this.form.get('asunto').clearValidators();

         if(value == TIPOLOGIA_DERECHO_PETICION){
           this.form.get('asunto').setValidators([Validators.required,Validators.maxLength(500)]);
         }
          else{
          this.form.get('asunto').setValidators(Validators.maxLength(500));
         }

         this.listenForBlurEvents('asunto');
         this.form.get('asunto').updateValueAndValidity();
        this.onSelectTipologiaDocumental(value);
      })
    }

   if(!isNullOrUndefined(tipoComunicacionDefault))
     this.form.get("tipoComunicacion").setValue(tipoComunicacionDefault);

    this.form.get('tipoComunicacion').valueChanges.subscribe((value) => {
      this.onSelectTipoComunicacion(value);
    });

    this.form.get('medioRecepcion').valueChanges.subscribe((value) => {
      if (value === MEDIO_RECEPCION_CORREO_CERTIFICADO) {
        this.visibility.empresaMensajeria = true;
        this.visibility.numeroGuia = true;
      } else if (this.visibility.empresaMensajeria && this.visibility.numeroGuia) {
        delete this.visibility.empresaMensajeria;
        delete this.visibility.numeroGuia;
      }
    });



    if(this.isRadicarDocProducido()){

      this.form.get('reqDistFisica').valueChanges.subscribe( value => {

        const control = this.form.get('numeroFolio');

        control.clearValidators();

        if(value)
           control.setValidators(Validators.required);

      })
    }

  }

  changeTipoDistribucion(evt:boolean){

    this.onChangeTipoDistribucion.emit(evt);
  }

  listenForErrors() {
    this.bindToValidationErrorsOf('tipoComunicacion');
    this.bindToValidationErrorsOf('tipologiaDocumental');
    this.bindToValidationErrorsOf('numeroFolio');
    this.bindToValidationErrorsOf('asunto');
    this.bindToValidationErrorsOf('tipoAnexosDescripcion');
    this.bindToValidationErrorsOf('empresaMensajeria');
    this.bindToValidationErrorsOf('numeroGuia');
    this.bindToValidationErrorsOf('medioRecepcion');
  }

  ngOnInit(): void {



    this.tipoComunicacionSuggestions$ = this._store.select(getTipoComunicacionArrayData)
                                            .map(tipo_comunicaciones =>  isNullOrUndefined(this.tipoComunicacion) ? tipo_comunicaciones :  tipo_comunicaciones.filter(this.tipoComunicacion));

    this.unidadTiempoSuggestions$ = this._store.select(getUnidadTiempoArrayData);
    this.tipoAnexosSuggestions$ = this._store.select(getTipoAnexosArrayData);
    this.medioRecepcionSuggestions$ = this._store.select(getMediosRecepcionArrayData);
    this.tipologiaDocumentalSuggestions$ = this._store.select(getTipologiaDocumentalArrayData)
                                               .map( tipologias => isNullOrUndefined(this.tipologiaDocFilter) ? tipologias : tipologias.filter(this.tipologiaDocFilter));

    this.soporteAnexosSuggestions$ = this._store.select(getSoporteAnexoArrayData);

    this._store.dispatch(new SedeAdministrativaLoadAction());

    this.initForm();


    this.dataDefault.subscribe( datosGenerales => {  console.log('datosGenerales',datosGenerales);

      this.form.get("tipoComunicacion").setValue(datosGenerales.tipoComunicacion);
      if(datosGenerales.tipologiaDocumental)
      this.form.get("tipologiaDocumental").setValue(datosGenerales.tipologiaDocumental);

      if(this.isRadicacionEntrada()) {
        this.form.get("unidadTiempo").setValue(datosGenerales.unidadTiempo);
        this.form.get("tiempoRespuesta").setValue(datosGenerales.tiempoRespuesta);
      }
      this.form.get("medioRecepcion").setValue(datosGenerales.medioRecepcion);
      this.form.get("empresaMensajeria").setValue(datosGenerales.empresaMensajeria);
      this.form.get("numeroGuia").setValue(datosGenerales.numeroGuia);
      this.form.get("inicioConteo").setValue(datosGenerales.inicioConteo);
      this.form.get("asunto").setValue(datosGenerales.asunto);
      this.form.get("reqDigit").setValue(datosGenerales.reqDigit);
      this.form.get("reqDistFisica").setValue(datosGenerales.reqDistFisica);
      this.form.get("reqDistElect").setValue(datosGenerales.reqDistElect);

      this._store.select(getSoporteAnexoArrayData).subscribe( soportes => {

        this.descripcionAnexos = datosGenerales.listaAnexos.map( anexo => {

          return {
            tipoAnexo : anexo.tipo,
            soporteAnexo : soportes.find( soporte => soporte.codigo == anexo.soporte),
            descripcion: anexo.descripcion}
        })
      });


      /*this.descripcionAnexos = datosGenerales.listaAnexos.map( anexo => {
        return { tipoAnexo : anexo.tipo, soporteAnexo : { nombre : anexo.soporte},descripcion: anexo.descripcion}
      });*/
      if(datosGenerales.radicadosReferidos) {
        this.radicadosReferidos = datosGenerales.radicadosReferidos.map(data => {
          return Object.assign(data, {blocked: true})
        });
      }
    });

    this.listenForErrors();
  }

  addRadicadosReferidos() {
    const insertVal = [{nombre: this.form.get('radicadoReferido').value}];
    this.radicadosReferidos = [ ...this.radicadosReferidos.filter(value => value.nombre !== this.form.get('radicadoReferido').value),...insertVal];
    this.form.get('radicadoReferido').reset();
  }

  addTipoAnexosDescripcion() {
    const tipoAnexo = this.form.get('tipoAnexos').value;
    const soporteAnexo = this.form.get('soporteAnexos').value;
    const descripcion = this.form.get('tipoAnexosDescripcion').value;

    if (!tipoAnexo) {
      return;
    }
    const newAnexo = [{tipoAnexo: tipoAnexo, descripcion: descripcion, soporteAnexo: soporteAnexo}];
    this.descripcionAnexos = [
      ...newAnexo,
      ...this.descripcionAnexos.filter(
        value =>
          value.tipoAnexo.nombre !== tipoAnexo.nombre ||
          // value.descripcion !== descripcion &&
          value.soporteAnexo.nombre !== soporteAnexo.nombre
      )
    ];
    this.form.get('hasAnexos').setValue(this.descripcionAnexos.length);
    this.form.get('tipoAnexos').reset();
    this.form.get('soporteAnexos').reset();
    this.form.get('tipoAnexosDescripcion').reset();
  }

  deleteRadicadoReferido(index) {
    const removeVal = [...this.radicadosReferidos];
    removeVal.splice(index, 1);
    this.radicadosReferidos = removeVal;
  }

  deleteTipoAnexoDescripcion(index) {
    const removeVal = [...this.descripcionAnexos];
    removeVal.splice(index, 1);
    if (removeVal.length === 0) {
      this.form.get('hasAnexos').reset();
    }
    this.descripcionAnexos = removeVal;
  }

  onSelectTipologiaDocumental(codigoTipologia) {

    if(this.isRadicacionEntrada()){
      this.metricasTiempoTipologia$ = this._apiDatosGenerales.loadMetricasTiempo(codigoTipologia);
      this.metricasTiempoTipologia$.subscribe(metricas => {

        if(isNullOrUndefined(metricas))
           return;

        this.form.get('inicioConteo').setValue(metricas.inicioConteo);
        this.form.get('tiempoRespuesta').setValue(metricas.tiempoRespuesta);
        this.form.get('unidadTiempo').setValue(metricas.codUnidaTiempo);
        this.changeDetector.detectChanges();

      });
      this.changeDetector.detectChanges();
    }
  }

  onSelectTipoComunicacion(value) {
    this.onChangeTipoComunicacion.emit(value);
  }

  listenForBlurEvents(control: string) {
    const ac = this.form.get(control);

    if((control == 'asunto' || control == 'tipoAnexosDescripcion' || 'radicadoReferido') && !isNullOrUndefined(ac) && !isNullOrUndefined(ac.value))
        ac.setValue(ac.value.toString().trim())

    if (ac.touched && ac.invalid) {
      const error_keys = Object.keys(ac.errors);

     if(control != "radicadoReferido"){
       const last_error_key =  error_keys[error_keys.length - 1];
       this.validations[control] =  VALIDATION_MESSAGES[last_error_key];
     }

    }
  }
  // resetCarFilter() {
  //  console.log(this.dropDownThing);
  //  this.dropDownThing.selectedOption = null;
  // }

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

  showBlockDistribucionDig(){

    return ViewFilterHook.applyFilter("app-datos-direccion-show-block-dist-dig",true);
  }

  isRadicacionEntrada(){
    return this.tipoRadicacion == RADICACION_ENTRADA || this.tipoRadicacion == CORREGIR_RADICACION;
  }

  isRadicacionSalida(){

    return this.tipoRadicacion == RADICACION_SALIDA || this.tipoRadicacion == RADICACION_DOC_PRODUCIDO;
  }

  isRadicacionInterna(){
    return this.tipoRadicacion == RADICACION_INTERNA;
  }

  isRadicarDocProducido(){
    return this.tipoRadicacion == RADICACION_DOC_PRODUCIDO;
  }

  ngOnDestroy(): void {

    this.subcriptions.forEach( s => s.unsubscribe());
  }

  private  isEditable(field){
    return  !this.fieldsDisabled[field] && this.editable;
  }

  get reqDistFisica(){
    return this.form.get("reqDistFisica").value
  }

}
