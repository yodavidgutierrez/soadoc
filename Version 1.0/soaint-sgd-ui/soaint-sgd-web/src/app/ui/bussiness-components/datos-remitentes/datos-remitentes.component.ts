import {
  ChangeDetectionStrategy,
  ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output,
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
import {DestinatarioDTO} from '../../../domain/destinatarioDTO';
import {isNullOrUndefined} from 'util';
import {LoadDatosRemitenteAction} from '../../../infrastructure/state-management/constanteDTO-state/constanteDTO-actions';
import {LoadDatosGeneralesAction} from '../../../infrastructure/state-management/constanteDTO-state/constanteDTO-actions';
import {LoadAction as SedeAdministrativaLoadAction} from '../../../infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-actions';
import {tipoDestinatarioEntradaSelector} from '../../../infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-selectors';
import {PushNotificationAction} from '../../../infrastructure/state-management/notifications-state/notifications-actions';
import {
  DATOS_CONTACTO_PRINCIPAL,
  DESTINATARIO_PRINCIPAL
} from '../../../shared/bussiness-properties/radicacion-properties';
import {ConfirmationService} from 'primeng/components/common/api';
import {Sandbox as FuncionariosSandbox} from 'app/infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-sandbox';
import {getArrayData as getFuncionarioArrayData} from 'app/infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {FuncionarioDTO} from '../../../domain/funcionarioDTO';
import {ViewFilterHook} from "../../../shared/ViewHooksHelper";
import {DatosDireccionComponent} from "../datos-direccion/datos-direccion.component";
import {ExtendValidators} from "../../../shared/validators/custom-validators";
import {ValidateFn} from "codelyzer/walkerFactory/walkerFn";
import {createChangeDetectorRef} from "@angular/core/src/view/refs";
import {FuncionariosService} from "../../../infrastructure/api/funcionarios.service";
import {DatosContactoApi} from "../../../infrastructure/api/datos-contacto.api";
import {getTipoDestinatarioArrayData} from "../../../infrastructure/state-management/constanteDTO-state/selectors/tipo-destinatario-selectors";
import {combineLatest} from "rxjs/observable/combineLatest";
import {AgenteApi} from "../../../infrastructure/api/agente.api";
import {checkPort} from "@angular/cli/utilities/check-port";
import {setValueOnPath} from "@angular/platform-browser/src/browser/browser_adapter";

@Component({
  selector: 'app-datos-remitentes',
  templateUrl: './datos-remitentes.component.html',
  changeDetection:ChangeDetectionStrategy.OnPush
})
export class DatosRemitentesComponent implements OnInit, OnDestroy {

  @Input() readOnly = false;

  form: FormGroup ;
  validations: any = {};
  visibility: any = {};
  display = false;

  @Input() operation: 'CREATE'|'UPDATE';

  // Observables
  tipoPersonaSuggestions$: Observable<ConstanteDTO[]>;
  tipoDocumentoSuggestions$: Observable<ConstanteDTO[]>;
  actuaCalidadSuggestions$: Observable<ConstanteDTO[]>;
  sedeAdministrativaSuggestions$: Observable<ConstanteDTO[]>;
  tipoDestinatarioSuggestions$: Observable<ConstanteDTO[]>;
  funcionariosSuggestions$: Observable<FuncionarioDTO[]>;


  dependenciasGrupoList: Array<any> = [];
  subscriptionTipoDocumentoPersona: Array<ConstanteDTO> = [];
  subscribers: Array<Subscription> = [];

  editable = true;
  editDestinatario = false;
  @Input() principal = false;
  destinatario: DestinatarioDTO;
  destinatariosContactos: Array<any> = [];
  @Output() onCreateDestinatario: EventEmitter<any> = new EventEmitter<any>();
  @Output() onUpdateDestinatario: EventEmitter<any> = new EventEmitter<any>();
  @Output() formDataContactShown: EventEmitter<FormGroup> = new EventEmitter<any>();
  @ViewChild('destinatarioDatosContactos') destinatarioDatosContactos;


  @Input('tipoComunicacion') tipoComunicacion: string;
  @Output() onChangeSedeAdministrativa: EventEmitter<any> = new EventEmitter();

  constructor(private _store: Store<State>,
              private formBuilder: FormBuilder,
              private _dependenciaGrupoSandbox: DependenciaGrupoSandbox,
              private _funcionarioSandbox: FuncionariosSandbox,
              private _funcionarioApi:FuncionariosService,
              private confirmationService: ConfirmationService,
              private _changeDetectorRef: ChangeDetectorRef,
              private _datosContactoApi:DatosContactoApi,
              private _agenteApi:AgenteApi
              ) {
    this.initStores();
  }

  ngOnInit(): void {
    this.internalInit();

    this.editable = !this.readOnly;

    }

  internalInit(): void {
    this.initForm();
    this.initByTipoComunicacion();
    this.form.enable();
    this.listenForChanges();
    this.listenForErrors();
  }

  initByTipoComunicacion() {
    if (isNullOrUndefined(this.tipoComunicacion)) {
      this.tipoComunicacion = COMUNICACION_EXTERNA;
    }
    if (this.tipoComunicacion === COMUNICACION_INTERNA) {
      this.visibility['tipoPersona'] = false;
      this.visibility['sede'] = true;
      this.visibility['dependencia'] = true;
      this.visibility['funcionario'] = true;
      this.initByTipoComunicacionInterna();
    } else {
      this.visibility['tipoPersona'] = true;
      this.initByTipoComunicacionExterna();
    }
  }

  initStores() {
    this._store.dispatch(new LoadDatosRemitenteAction());
    this._store.dispatch(new LoadDatosGeneralesAction());
  }

  initByTipoComunicacionExterna() {
    this.tipoPersonaSuggestions$ = this._store.select(
      getTipoPersonaArrayData).map(tps => tps.filter(tp => tp.codigo !== PERSONA_ANONIMA));
    this.tipoDocumentoSuggestions$ = this._store.select(getTipoDocumentoArrayData);
    this.actuaCalidadSuggestions$ = this._store.select(getActuaCalidadArrayData);
    this.tipoDestinatarioSuggestions$ = this._store.select(getTipoDestinatarioArrayData);
  }


  initByTipoComunicacionInterna() {
    this.sedeAdministrativaSuggestions$ = this._store.select(sedeAdministrativaArrayData);
    this.tipoDestinatarioSuggestions$ = this._store.select(tipoDestinatarioEntradaSelector);
    this.funcionariosSuggestions$ = this._store.select(getFuncionarioArrayData);
  }

  initForm() {

     this.form = this.formBuilder.group({
      'tipoPersona': [{value: null, disabled: !this.editable}, this.tipoComunicacion == COMUNICACION_EXTERNA ? Validators.required:null],
      'nit': [{value: null, disabled: !this.editable}],
      'actuaCalidad': [{value: null, disabled: !this.editable}],
      'tipoDocumento': [{value: null, disabled: !this.editable}],
      'razonSocial': [{value: null, disabled: !this.editable}],
      'nombre': [{value: null, disabled: !this.editable}],
      'nroDocumentoIdentidad': [{value: null, disabled: !this.editable}],
      'sede': [{value: null, disabled: !this.editable}, this.tipoComunicacion == COMUNICACION_INTERNA ? Validators.required: null],
      'dependencia': [{value: null, disabled: !this.editable},this.tipoComunicacion == COMUNICACION_INTERNA ?  Validators.required: null],
      'funcionario': [{value: null, disabled: !this.editable}],
      'tipoDestinatario': [{value: null, disabled: !this.editable}, Validators.required],
      'principal': [null],
      'ideAgente': [null],
    });
 }

  initFormByDestinatario(destinatario) {
    if (!isNullOrUndefined(destinatario)) {
      this.editDestinatario = true;
      this.destinatario = destinatario;

      this.form.get('tipoPersona').setValue(this.destinatario.tipoPersona);
      this.form.get('nit').setValue(this.destinatario.nit);
      this.form.get('actuaCalidad').setValue(this.destinatario.actuaCalidad);
      this.form.get('tipoDocumento').setValue(this.destinatario.tipoDocumento);

      this.form.get('razonSocial').setValue(this.destinatario.razonSocial);
      this.form.get('nombre').setValue(this.destinatario.nombre);
      this.form.get('nroDocumentoIdentidad').setValue(this.destinatario.nroDocumentoIdentidad);
      this.form.get('sede').setValue(this.destinatario.sede);
      this.form.get('dependencia').setValue(this.destinatario.dependencia);
      this.form.get('funcionario').setValue(this.destinatario.funcionario);
      this.form.get('tipoDestinatario').setValue(this.destinatario.tipoDestinatario);

      if (!this.destinatario.interno) {
        this.visibility['datosContacto'] = true;
      }

      if (!isNullOrUndefined(this.destinatarioDatosContactos)) {
        const newList1 = (!isNullOrUndefined(this.destinatario.datosContactoList) ? this.destinatario.datosContactoList : []);
        const newList2 = this.transformToDestinatarioContacts(newList1);  console.log("datos contacto", newList2);
        this.destinatarioDatosContactos.contacts = [...newList2];
        this._changeDetectorRef.detectChanges();
      }

    }
  }

  transformToDestinatarioContacts(contacts) {

   const  checkJsonParse = (text:string) => {

      try{
        JSON.parse(text);

        return true;
      }
      catch (e) {
        return false;
      }
    };

    return contacts.map(c => { console.log("datos contacto destinatario",c);
      return {
        direccion: isNullOrUndefined(c.direccion) ? '' : c.direccion,
        direccionText: !checkJsonParse(c.direccion) ? c.direccion : '',
        pais: isNullOrUndefined(c.pais) ? '' : c.pais,
        departamento: isNullOrUndefined(c.departamento) ? null : c.departamento,
        municipio: isNullOrUndefined(c.municipio) ? null : c.municipio,
        numeroTel: isNullOrUndefined(c.numeroTel) ? '' : c.numeroTel,
        celular: isNullOrUndefined(c.celular) ? '' : c.celular,
        correoEle: isNullOrUndefined(c.correoEle) ? '' : c.correoEle,
        principal:c.principal == DATOS_CONTACTO_PRINCIPAL,
        tipoContacto:c.tipoContacto,
        ciudad:c.ciudad,
        provinciaEstado:c.provinciaEstado
      }; });
  }

  listenForChanges() {
    this.subscribers.push(this.form.get('sede').valueChanges.distinctUntilChanged().subscribe((sede) => {
      if (this.editable && sede) {
        this.form.get('dependencia').reset();
        const depedenciaSubscription: Subscription = this._dependenciaGrupoSandbox.loadData({codigo: sede.codigo}).subscribe(dependencias => {
          this.dependenciasGrupoList = dependencias.organigrama;
          depedenciaSubscription.unsubscribe();
        });
      }
    }));

  /*  this.subscribers.push(this.form.get('dependencia').valueChanges.subscribe((value) => {
      if (value) {
       this.funcionariosSuggestions$ =  this._funcionarioApi.getFuncionarioBySpecification( funcionario => {
          return funcionario.dependencias.some( dep => dep.codigo == value.codigo)
        })
      }
    }));*/

    this.subscribers.push(this.form.get('tipoPersona').valueChanges.distinctUntilChanged().subscribe(value => {
      if (value !== null) {

        this.form.get("nombre").clearValidators();
        this.form.get("razonSocial").clearValidators();

        switch (value.codigo){
          case PERSONA_NATURAL :
              this.form.get("nombre").setValidators(Validators.required);
              break;
          case PERSONA_JURIDICA :
            this.form.get("razonSocial").setValidators(Validators.required);
            break;
        }

        this.form.get("nombre").updateValueAndValidity();
        this.form.get("razonSocial").updateValueAndValidity();

        this.onSelectTipoPersona(value);
      }
    }));

  }


  listenForErrors() {
    this.bindToValidationErrorsOf('sede');
    this.bindToValidationErrorsOf('dependencia');
    this.bindToValidationErrorsOf('funcionario');
    this.bindToValidationErrorsOf('tipoPersona');
  }

  onSelectTipoPersona(value) {
    // const value = event.value;
    if (!this.visibility.tipoPersona) {
      return;
    } else {
      this.visibility = {
        tipoPersona: true
      };
    }

    this.visibility['datosContacto'] = true;

    if (value.codigo === PERSONA_ANONIMA) {
      this.visibility['tipoPersona'] = true;
      this.visibility['datosContacto'] = false;

    } else if (value.codigo === PERSONA_JURIDICA && this.tipoComunicacion === COMUNICACION_EXTERNA) {
      this.visibility['nit'] = true;
      this.visibility['actuaCalidad'] = true;
      this.visibility['razonSocial'] = true;
      this.visibility['nombre'] = true;
      this.visibility['inactivo'] = true;
      this.visibility['tipoDocumento'] = true;
      this.tipoDocumentoSuggestions$.subscribe(docs => {
        this.subscriptionTipoDocumentoPersona = docs.filter(doc => doc.codigo === TPDOC_NRO_IDENTIFICACION_TRIBUTARIO);
        this.form.get('tipoDocumento').setValue(this.subscriptionTipoDocumentoPersona[0]);
      }).unsubscribe();
      this.visibility['personaJuridica'] = true;
    } else if (value.codigo === PERSONA_NATURAL && this.tipoComunicacion === COMUNICACION_EXTERNA) {
      this.visibility['nombre'] = true;
      this.visibility['departamento'] = true;
      this.visibility['nroDocumentoIdentidad'] = true;
      this.visibility['tipoDocumento'] = true;

      this.tipoDocumentoSuggestions$.subscribe(docs => {
        this.subscriptionTipoDocumentoPersona = docs.filter(doc => doc.codigo !== TPDOC_NRO_IDENTIFICACION_TRIBUTARIO);
        this.form.get('tipoDocumento').setValue(this.subscriptionTipoDocumentoPersona.filter(doc => doc.codigo === TPDOC_CEDULA_CIUDADANIA)[0]);
      }).unsubscribe();

    }
    this.refreshView();
  }

  listenForBlurEvents(control: string,eventType?:string) {
    const ac = this.form.get(control);

    delete this.validations[control];

    if(eventType == 'blur'){

      const inputsToTrim = ['nombre','nroDocumentoIdentidad','nit','razonSocial'];

      if(inputsToTrim.some( name => name == control))
        if(!isNullOrUndefined(ac) && !isNullOrUndefined(ac.value))
          ac.setValue(ac.value.toString().trim());
    }

    if (ac.touched && ac.invalid) {
      const error_keys = Object.keys(ac.errors);
      const last_error_key = error_keys[error_keys.length - 1];
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

  updateDestinatarioContacts(event) {
    this.destinatariosContactos = event;
  }

   ShowFormContactData(component:DatosDireccionComponent){

       this.formDataContactShown.emit(component.form);
  }

  saveRemitente() {

   const dest: DestinatarioDTO = this.form.value;
    dest.isBacken = !isNullOrUndefined(this.destinatario) && this.destinatario.isBacken ? true : false;
    dest.interno = this.tipoComunicacion === COMUNICACION_INTERNA ? true : false;

    let observable$:Observable<any> = Observable.of({});

     if (!dest.interno) {
      this.visibility['datosContacto'] = true;
      if (isNullOrUndefined(this.destinatarioDatosContactos)) {
        dest.datosContactoList = [];
      }
      else {
        const newList1 = this.destinatarioDatosContactos.contacts;
        dest.datosContactoList = [...newList1];
        this.destinatarioDatosContactos.contacts = [];
        this.destinatarioDatosContactos.form.reset();
      }

      if(isNullOrUndefined(dest.ideAgente)){
        if(!!dest.nit || !!dest.nroDocumentoIdentidad){

          const nroIdent = dest.tipoPersona.codigo == PERSONA_NATURAL ? dest.nroDocumentoIdentidad : dest.nit;

          observable$ = this._agenteApi.getAgenteByNroIdentificacion(nroIdent,dest.tipoPersona.codigo);

        }
      }
      else {
        observable$ = Observable.of({ideAgente:dest.ideAgente});
      }
    }

    observable$.subscribe( agente => {

      if(!dest.interno)
        dest.ideAgente = agente.ideAgente || null;

      if(dest.tipoDestinatario  && dest.tipoDestinatario.codigo === DESTINATARIO_PRINCIPAL && this.principal){

        this.confirmationService.confirm({
          message: `<p style="text-align: center">¿Está seguro desea substituir el destinatario principal?</p>`,
          accept: () => {

            this.emitSaveEvent(dest);
            this.form.reset();
            this.reset();
          },
          reject: () => {
            this._store.dispatch(new PushNotificationAction({
              severity: 'warn',
              summary: 'Debe cambiar el tipo de Destinatario principal'
            }));

          }
        });
      } else {

        this.emitSaveEvent(dest);
        this.form.reset();
        this.reset();
      }
    });



    //this.destinatario = null;
  }

  private emitSaveEvent(dest){
    if(this.operation == 'CREATE')
      this.onCreateDestinatario.emit(dest);
    else if (this.operation == 'UPDATE')
      this.onUpdateDestinatario.emit(dest);
  }

  reset() {
    this.visibility['tipoPersona'] = false;
    this.visibility['nit'] = false;
    this.visibility['actuaCalidad'] = false;
    this.visibility['razonSocial'] = false;
    this.visibility['nombre'] = false;
    this.visibility['datosContacto'] = false;
    this.visibility['inactivo'] = false;
    this.visibility['tipoDocumento'] = false;
    this.visibility['nombre'] = false;
    this.visibility['departamento'] = false;
    this.visibility['nroDocumentoIdentidad'] = false;
    this.visibility['tipoDocumento'] = false;
    this.visibility['sede'] = false;
    this.visibility['dependencia'] = false;
    this.visibility['funcionario'] = false;
    this.internalInit();
    this.refreshView();
  }

  refreshView() {
    this._changeDetectorRef.detectChanges();
  }

  disabledButtonAgregar():boolean{

     return ViewFilterHook.applyFilter('datos-remitente-'+this.tipoComunicacion, isNullOrUndefined(this.form)  || this.form.invalid);
  }

  buscarContactos(){

    const tipoPersona = this.form.get('tipoPersona').value;

    const  nroIdentificacion = tipoPersona.codigo == PERSONA_NATURAL ? this.form.get('nroDocumentoIdentidad').value : this.form.get('nit').value

    let tipoDocumentoIdentificacion =   this.form.get("tipoDocumento").value;

    if(isNullOrUndefined(nroIdentificacion)){

      const field =   tipoPersona.codigo == PERSONA_NATURAL ?  'No. Documento identidad' : 'No Identificacion Tributario';

      this._store.dispatch(new PushNotificationAction({severity:'error',summary: `Debe de seleccionar el ${field}`}));

      return;
    }

    if(isNullOrUndefined(tipoDocumentoIdentificacion) && tipoPersona.codigo == PERSONA_NATURAL){
      this._store.dispatch(new PushNotificationAction({severity:'error',summary: `Debe de seleccionar el tipo de Documento`}));

        return;
    }

    if(!isNullOrUndefined(tipoDocumentoIdentificacion))
      tipoDocumentoIdentificacion = tipoDocumentoIdentificacion.codigo;

    this.subscribers.push();

    combineLatest(this._datosContactoApi.getDatosContactoByIdentificaciob(tipoPersona.codigo,nroIdentificacion,tipoDocumentoIdentificacion),
      this._agenteApi.getAgenteByNroIdentificacion(nroIdentificacion,tipoPersona.codigo))
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

          this.destinatarioDatosContactos.contacts = [...contacts];

          this.destinatarioDatosContactos.CompletarDatosContacto();

           this._changeDetectorRef.detectChanges();

        }
        if(isNullOrUndefined(agente)){
          this.form.get('ideAgente').setValue(null);
          return;
        }

        this.form.get('ideAgente').setValue(agente.ideAgente);
        this.form.get('nombre').setValue(agente.nombre);

        if(tipoPersona.codigo == PERSONA_JURIDICA){

          this.form.get('razonSocial').setValue(agente.razonSocial);

          this.subscribers.push(this._store.select(getActuaCalidadArrayData)
            .map( ac =>  ac.find(a => a.codigo == agente.codEnCalidad))
            .subscribe( actCal => {

              if(!isNullOrUndefined(actCal)){
                this.form.get('actuaCalidad').setValue(actCal);
              }

              this.form.updateValueAndValidity();

              this._changeDetectorRef.detectChanges();
            })
          );
        }
        else{

          this.form.updateValueAndValidity();

          this._changeDetectorRef.detectChanges();
        }

      });



  }

  buttonSearchContacts(){

    if(isNullOrUndefined(this.form))
       return false;

    const tipoPersona = this.form.get('tipoPersona').value;

    if(isNullOrUndefined(tipoPersona))
      return false;

    return tipoPersona.codigo == PERSONA_NATURAL || tipoPersona.codigo ==PERSONA_JURIDICA
  }

  buttonSearchContactsEnable():boolean{

    if(isNullOrUndefined(this.form))
      return false;

    const tipoPersona = this.form.get('tipoPersona').value;

    if(isNullOrUndefined(tipoPersona))
      return false;

    if(tipoPersona.codigo == PERSONA_NATURAL)
       return this.form.get('nroDocumentoIdentidad').value;

    if(tipoPersona.codigo == PERSONA_JURIDICA)
       return this.form.get('nit').value;


    return false;

  }


}

