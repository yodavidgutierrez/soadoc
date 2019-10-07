import {
  AfterViewInit,
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
  ViewChild
} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';

import {
  getTipoDocumentoArrayData,
  getTipoPersonaArrayData,
} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-selectors';

import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {getArrayData as sedeAdministrativaArrayData} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-selectors';
import {Sandbox as DependenciaGrupoSandbox} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {VALIDATION_MESSAGES} from 'app/shared/validation-messages';
import {
  COMUNICACION_EXTERNA, COMUNICACION_INTERNA, PERSONA_ANONIMA, PERSONA_JURIDICA,
  PERSONA_NATURAL, TPDOC_CEDULA_CIUDADANIA, TPDOC_NRO_IDENTIFICACION_TRIBUTARIO
} from 'app/shared/bussiness-properties/radicacion-properties';
import {getActuaCalidadArrayData} from '../../../infrastructure/state-management/constanteDTO-state/selectors/actua-calidad-selectors';
import {Subscription} from 'rxjs/Subscription';
import {isNullOrUndefined} from "util";
import {DatosContactoApi} from "../../../infrastructure/api/datos-contacto.api";
import {PushNotificationAction} from "../../../infrastructure/state-management/notifications-state/notifications-actions";
import {AgenteApi} from "../../../infrastructure/api/agente.api";
import {combineLatest} from "rxjs/observable/combineLatest";


@Component({
  selector: 'app-datos-remitente',
  templateUrl: './datos-remitente.component.html',
})
export class DatosRemitenteComponent implements OnInit, OnDestroy {

  form: FormGroup;
  validations: any = {};
  visibility: any = {};
  display = false;

  // Observables
  tipoPersonaSuggestions$: Observable<ConstanteDTO[]>;
  tipoDocumentoSuggestions$: Observable<ConstanteDTO[]>;
  actuaCalidadSuggestions$: Observable<ConstanteDTO[]>;
  sedeAdministrativaSuggestions$: Observable<ConstanteDTO[]>;

  // Listas de subscripcion
  contacts: Array<any> = [];
  dependenciasGrupoList: Array<any> = [];
  ideAgente:any = null;

  subscriptionTipoDocumentoPersona: Array<ConstanteDTO> = [];

  subscribers: Array<Subscription> = [];

  @ViewChild('datosContactos') datosContactos;

  @Input() editable = true;
  @Input() tipoComunicacion: any;

  constructor(private _store: Store<State>,
              private formBuilder: FormBuilder,
              private _datosContactoApi:DatosContactoApi,
              private _dependenciaGrupoSandbox: DependenciaGrupoSandbox,
              private _agenteApi:AgenteApi,
              private _changeDetector:ChangeDetectorRef
              ) {
  }

  ngOnInit(): void {
    this.initForm();
    //this.form.disable();
    this.listenForChanges();
    this.listenForErrors();
    this.initLoadTipoComunicacionExterna();
   }

  initLoadTipoComunicacionExterna() {
    this.tipoPersonaSuggestions$ = this._store.select(getTipoPersonaArrayData);
    this.tipoDocumentoSuggestions$ = this._store.select(getTipoDocumentoArrayData);
    this.actuaCalidadSuggestions$ = this._store.select(getActuaCalidadArrayData);
  }

  initLoadTipoComunicacionInterna() {
    this.sedeAdministrativaSuggestions$ = this._store.select(sedeAdministrativaArrayData);
  }

  initForm() {
      this.form = this.formBuilder.group({
        'tipoPersona': [{value: null, disabled: !this.editable}, Validators.required],
        'nit': [{value: null, disabled: !this.editable},Validators.pattern(/^[0-9]+$/)],
        'actuaCalidad': [{value: null, disabled: !this.editable}],
        'tipoDocumento': [{value: null, disabled: !this.editable}],
        'razonSocial': [{value: null, disabled: !this.editable}],
        'nombreApellidos': [{value: null, disabled: !this.editable}],
        'nroDocumentoIdentidad': [{value: null, disabled: !this.editable}],
        'sedeAdministrativa': [{value: null, disabled: !this.editable}],
        'dependenciaGrupo': [{value: null, disabled: !this.editable}],
        });


  }

  listenForChanges() {
    this.subscribers.push(this.form.get('sedeAdministrativa').valueChanges.distinctUntilChanged().subscribe((sede) => {
      if (this.editable && sede) {
        this.form.get('dependenciaGrupo').reset();
        const depedenciaSubscription: Subscription = this._dependenciaGrupoSandbox.loadData({codigo: sede.id}).subscribe(dependencias => {
          this.dependenciasGrupoList = dependencias.organigrama;
          depedenciaSubscription.unsubscribe();
        });
      }
    }));

    this.subscribers.push(this.form.get('tipoPersona').valueChanges.distinctUntilChanged().subscribe(value => {
      if (value !== null) {
        this.form.get("nit").setValue(null);
        this.form.get("nroDocumentoIdentidad").setValue(null);
        this.ideAgente = null;

        this.form.get("nombreApellidos").clearValidators();
        this.form.get("razonSocial").clearValidators();

        if(value == PERSONA_NATURAL)
          this.form.get("nombreApellidos").setValidators(Validators.required);
        if(value == PERSONA_JURIDICA)
          this.form.get("razonSocial").setValidators(Validators.required);

        this.form.updateValueAndValidity();

        this.onSelectTipoPersona(value);
      }
    }));
  }

  listenForErrors() {
    this.bindToValidationErrorsOf('sedeAdministrativa');
    this.bindToValidationErrorsOf('dependenciaGrupo');
    this.bindToValidationErrorsOf('tipoPersona');
  }

  onSelectTipoPersona(value) {
    // const value = event.value;


     if (value === PERSONA_JURIDICA && this.tipoComunicacion === COMUNICACION_EXTERNA) {
      this.visibility['nit'] = true;
      this.visibility['actuaCalidad'] = true;
      this.visibility['razonSocial'] = true;
      this.visibility['nombreApellidos'] = true;
      this.visibility['datosContacto'] = true;
      this.visibility['inactivo'] = true;
      this.visibility['tipoDocumento'] = true;
      this.tipoDocumentoSuggestions$.subscribe(docs => {
        this.subscriptionTipoDocumentoPersona = docs.filter(doc => doc.codigo === TPDOC_NRO_IDENTIFICACION_TRIBUTARIO);

       if(isNullOrUndefined( this.form.get('tipoDocumento').value))
        this.form.get('tipoDocumento').setValue(this.subscriptionTipoDocumentoPersona[0].codigo);
      }).unsubscribe();
      this.visibility['personaJuridica'] = true;
    } else if (value === PERSONA_NATURAL && this.tipoComunicacion === COMUNICACION_EXTERNA) {
      this.visibility['nombreApellidos'] = true;
      this.visibility['departamento'] = true;
      this.visibility['nroDocumentoIdentidad'] = true;
      this.visibility['datosContacto'] = true;
      this.visibility['tipoDocumento'] = true;


      this.tipoDocumentoSuggestions$.subscribe(docs => {
        this.subscriptionTipoDocumentoPersona = docs.filter(doc => doc.codigo !== TPDOC_NRO_IDENTIFICACION_TRIBUTARIO);
        if(isNullOrUndefined( this.form.get('tipoDocumento').value))
        this.form.get('tipoDocumento').setValue(this.subscriptionTipoDocumentoPersona.filter(doc => doc.codigo === TPDOC_CEDULA_CIUDADANIA)[0].codigo);
      }).unsubscribe();

    }

    else if(value== PERSONA_ANONIMA){
       this.visibility = {};
     }
  }

  setTipoComunicacion(value) {

    if (value) {
      this.visibility = {};
      this.tipoComunicacion = value.codigo;
      if (this.tipoComunicacion === COMUNICACION_INTERNA) {
        this.visibility['sedeAdministrativa'] = true;
        this.visibility['dependenciaGrupo'] = true;

        this.form.get('sedeAdministrativa').setValidators(Validators.required);
        this.form.get('dependenciaGrupo').setValidators(Validators.required);

        this.initLoadTipoComunicacionInterna();
      } else {
        this.visibility['tipoPersona'] = true;

        this.form.get('sedeAdministrativa').clearValidators();
        this.form.get('dependenciaGrupo').clearValidators();

        this.initLoadTipoComunicacionExterna();
      }
    }
  }

  listenForBlurEvents(control: string,eventType?:string ) {
     const ac = this.form.get(control);

     delete this.validations[control];

     if(eventType == 'blur'){

       const controlTrims = ['nit','nroDocumentoIdentidad','nombreApellidos','razonSocial'];

       if(controlTrims.some( c => c == control)){

         if(!isNullOrUndefined(ac.value))
           ac.setValue(ac.value.toString().trim())
       }
     }

    if (ac.touched && ac.invalid) {
      const error_keys = Object.keys(ac.errors);
      const last_error_key = error_keys[error_keys.length - 1];

      if(last_error_key == 'pattern'){
        switch (control){

          case 'nit': this.validations[control] = 'Este campo solo permite números';
           break;
        }

      }
      else
      this.validations[control] = VALIDATION_MESSAGES[last_error_key];
    }


     if(!isNullOrUndefined(this[`blur_${control}`]) && eventType == 'blur')
       this[`blur_${control}`].call(this);
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

  ngOnDestroy() {
    this.subscribers.forEach(subscriber => {
      subscriber.unsubscribe();
    });
  }

  buscarContactos(){

    const tipoPersona = this.form.get('tipoPersona').value;

    const  nroIdentificacion = tipoPersona == PERSONA_NATURAL ? this.form.get('nroDocumentoIdentidad').value : this.form.get('nit').value;

    const tipoDocumentoIdentificacion =   this.form.get("tipoDocumento").value;

     if(isNullOrUndefined(nroIdentificacion)){

       const field =   tipoPersona == PERSONA_NATURAL ?  'No. Documento identidad' : 'No Identificacion Tributario';

       this._store.dispatch(new PushNotificationAction({severity:'error',summary: `Debe de seleccionar el ${field}`}));

       return;
     }

     if(isNullOrUndefined(tipoDocumentoIdentificacion) && tipoPersona == PERSONA_NATURAL){
       this._store.dispatch(new PushNotificationAction({severity:'error',summary: `Debe de seleccionar el tipo de Documento`}));

       return;
     }

     this.subscribers.push( combineLatest(this._datosContactoApi.getDatosContactoByIdentificaciob(tipoPersona,nroIdentificacion,tipoDocumentoIdentificacion),
       this._agenteApi.getAgenteByNroIdentificacion(nroIdentificacion,tipoPersona))
       .subscribe(([datosContactos,agente]) => {

         if(datosContactos.length === 0 && isNullOrUndefined(agente)){

           this._store.dispatch(new PushNotificationAction({severity:'info',summary: `No se encontraron datos de contacto `}));
           return;

         }

         if(datosContactos.length > 0){

           const contacts =   datosContactos.map(c => {

             const contactAdpated:any = {};

             Object.keys(c).forEach( key => {
               switch (key){
                 case 'corrElectronico': contactAdpated.correoEle = c[key];
                   break;
                 case 'codMunicipio': contactAdpated.municipio = {codigo:c[key]};
                  break;
                 case 'codDepartamento': contactAdpated.departamento = {codigo:c[key]};
                   break;
                   case 'codPais': contactAdpated.pais = {codigo:c[key]};
                   break;
                 case 'tipoContacto':  contactAdpated.tipoContacto = {codigo: c[key]};
                   break;
                 case 'telFijo' : contactAdpated.numeroTel = c[key];
                  break;
                 default : contactAdpated[key] = c[key];
                   break;
               }
             });

             return contactAdpated;

           });

           this.datosContactos.contacts = [...contacts];

           this.datosContactos.CompletarDatosContacto();

           this._changeDetector.detectChanges();

         }


         if(isNullOrUndefined(agente) || isNullOrUndefined(agente.ideAgente))
           return;

         this.form.get('nombreApellidos').setValue(agente.nombre);
         this.ideAgente = agente.ideAgente || null;;

         console.log("datos remitente",this.form.value)

         if(tipoPersona == PERSONA_JURIDICA){

           this.form.get('razonSocial').setValue(agente.razonSocial);

           this.subscribers.push(this._store.select(getActuaCalidadArrayData)
             .map( ac =>  ac.find(a => a.codigo == agente.codEnCalidad))
             .subscribe( actCal => {

               if(!isNullOrUndefined(actCal)){
                 this.form.get('actuaCalidad').setValue(actCal);
               }

               this._changeDetector.detectChanges();
             })
           );
         }



       }));

  }

  buttonSearchContacts(){

    const tipoPersona = this.form.get('tipoPersona').value;
    const tipoDocumento = this.form.get("tipoDocumento").value;

    if(isNullOrUndefined(tipoPersona) || isNullOrUndefined(tipoDocumento))
       return false;

    return tipoPersona == PERSONA_NATURAL || tipoPersona ==PERSONA_JURIDICA
  }

  buttonSearchContactsEnable():boolean{

    if(isNullOrUndefined(this.form))
      return false;

    const tipoPersona = this.form.get('tipoPersona').value;

    if(isNullOrUndefined(tipoPersona))
      return false;

    let noIdentificacion;

    if(tipoPersona == PERSONA_NATURAL)
     noIdentificacion = this.form.get('nroDocumentoIdentidad').value

    if(tipoPersona == PERSONA_JURIDICA)
    noIdentificacion = this.form.get('nit').value;


    return !isNullOrUndefined(noIdentificacion) && noIdentificacion !=='';

  }


}
