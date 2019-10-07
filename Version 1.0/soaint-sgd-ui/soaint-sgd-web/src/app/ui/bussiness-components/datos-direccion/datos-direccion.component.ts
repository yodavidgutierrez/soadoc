
import {
  ChangeDetectorRef,
  Component,
  Input,
  OnDestroy,
  AfterViewInit,
  OnInit,
  Output,
  EventEmitter,
  ViewChild,
  ElementRef,
  ChangeDetectionStrategy, OnChanges, SimpleChanges
} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';

import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {getPrefijoCuadranteArrayData} from 'app/infrastructure/state-management/constanteDTO-state/selectors/prefijo-cuadrante-selectors';
import {getTipoViaArrayData} from 'app/infrastructure/state-management/constanteDTO-state/selectors/tipo-via-selectors';
import {getOrientacionArrayData} from 'app/infrastructure/state-management/constanteDTO-state/selectors/orientacion-selectors';
import {getBisArrayData} from 'app/infrastructure/state-management/constanteDTO-state/selectors/bis-selectors';
import {getTipoComplementoArrayData} from '../../../infrastructure/state-management/constanteDTO-state/selectors/tipo-complemento-selectors';
import {tassign} from 'tassign';
import {VALIDATION_MESSAGES} from '../../../shared/validation-messages';
import {Sandbox as PaisSandbox} from '../../../infrastructure/state-management/paisDTO-state/paisDTO-sandbox';
import {Sandbox as DepartamentoSandbox} from '../../../infrastructure/state-management/departamentoDTO-state/departamentoDTO-sandbox';
import {Sandbox as MunicipioSandbox} from '../../../infrastructure/state-management/municipioDTO-state/municipioDTO-sandbox';
import {PaisDTO} from '../../../domain/paisDTO';
import {DepartamentoDTO} from '../../../domain/departamentoDTO';
import {MunicipioDTO} from '../../../domain/municipioDTO';
import {getArrayData as municipioArrayData} from 'app/infrastructure/state-management/municipioDTO-state/municipioDTO-selectors';
import {getArrayData as paisArrayData} from 'app/infrastructure/state-management/paisDTO-state/paisDTO-selectors';
import {getArrayData as departamentoArrayData} from 'app/infrastructure/state-management/departamentoDTO-state/departamentoDTO-selectors';
import {Subscription} from 'rxjs/Subscription';
import "rxjs/add/operator/filter";
import {AutoComplete} from "primeng/components/autocomplete/autocomplete";
import {isNullOrUndefined} from 'util';
import {PushNotificationAction} from "../../../infrastructure/state-management/notifications-state/notifications-actions";
import {Dropdown} from "primeng/primeng";
import {
  CODIGO_COLOMBIA,
  DATOS_CONTACTO_PRINCIPAL,
  DATOS_CONTACTO_SECUNDARIO
} from "../../../shared/bussiness-properties/radicacion-properties";
import { LocalizacionApiService } from '../../../infrastructure/api/localizacion.api';
import {getTipoComunicacionArrayData} from "../../../infrastructure/state-management/constanteDTO-state/selectors/tipo-comunicacion-selectors";
import {getTipoContactoArrayData} from "../../../infrastructure/state-management/constanteDTO-state/selectors/tipo-contacto-selectors";
import {combineLatest} from "rxjs/observable/combineLatest";
import {FormContextEnum} from "../../../shared/enums/enums";


@Component({
  selector: 'app-datos-direccion',
  templateUrl: './datos-direccion.component.html',
 })
export class DatosDireccionComponent implements OnInit, OnDestroy, AfterViewInit,OnChanges  {

  form: FormGroup;
  display = false;
  @Input() editable = true;

  @Input() contactsDefault: Array<any> = [];

  @Output()
  nuevosContactos = new EventEmitter();

  @Output() loadComponent:EventEmitter<DatosDireccionComponent> = new EventEmitter;

  @ViewChild('paisAutoComplete') paisAutoComplete: AutoComplete;
  @ViewChild('departamentoAutoComplete') departamentoAutoComplete: AutoComplete;
  @ViewChild('municipioAutoComplete') municipioAutoComplete: AutoComplete;


   validations: any = {};
  visibility: any = {};

  paisSuggestions$: Observable<PaisDTO[]>;
  departamentoSuggestions$: Observable<DepartamentoDTO[]>;
  municipioSuggestions$: Observable<MunicipioDTO[]>;
  prefijoCuadranteSuggestions$: Observable<ConstanteDTO[]>;
  tipoViaSuggestions$: Observable<ConstanteDTO[]>;
  orientacionSuggestions$: Observable<ConstanteDTO[]>;
  bisSuggestons$: Observable<ConstanteDTO[]>;
  tipoComplementoSuggestions$: Observable<ConstanteDTO[]>;
  paises$: Observable<PaisDTO[]>;
  departamentos$: Observable<DepartamentoDTO[]>;
  municipios$: Observable<MunicipioDTO[]>;
  tipoContacto$:Observable<ConstanteDTO[]>;

  contacts: Array<any> = [];
  paises: Array<any> = [];
  contactPrincial = false;
  showDireccionForm = false;
  showCheckDireccionForm = false;
  showContactForm = false;
  formContext: FormContextEnum;
  editIndexContext: number;

  subscribers: Array<Subscription> = [];


  constructor(private _store: Store<State>,
              private _paisSandbox: PaisSandbox,
              private _departamentoSandbox: DepartamentoSandbox,
              private _municipioSandbox: MunicipioSandbox,
              private formBuilder: FormBuilder,
              private _changeDetectorRef: ChangeDetectorRef,
              private _localizacionService: LocalizacionApiService
            ) {

    this.initForm();
    this.listenForChanges();
    this.listenForErrors();
  }


  ngOnInit(): void {
    this._paisSandbox.loadDispatch();
    this.prefijoCuadranteSuggestions$ = this._store.select(getPrefijoCuadranteArrayData);
    this.tipoViaSuggestions$ = this._store.select(getTipoViaArrayData);
    this.orientacionSuggestions$ = this._store.select(getOrientacionArrayData);
    this.bisSuggestons$ = this._store.select(getBisArrayData);
    this.tipoComplementoSuggestions$ = this._store.select(getTipoComplementoArrayData);
    this.municipios$ = this._store.select(municipioArrayData);
    this.departamentos$ = this._store.select(departamentoArrayData);
    this.paises$ = this._store.select(paisArrayData);
    this.tipoContacto$ = this._store.select(getTipoContactoArrayData);

    this.contacts = this.contactsDefault;

    this.addColombiaByDefault();

    this.paisSuggestions$ = this.paisAutoComplete.completeMethod
      .combineLatest(this.paises$, (event: any, paises) => paises.filter(pais => pais.nombre.toLowerCase().indexOf(event.query.toLowerCase()) >= 0 ));

    this.departamentoSuggestions$ = this.departamentoAutoComplete.completeMethod
      .combineLatest(this.departamentos$, (event: any, departamentos) => departamentos.filter(departamento => departamento.nombre.toLowerCase().indexOf(event.query.toLowerCase()) >= 0 ));

    this.municipioSuggestions$ = this.municipioAutoComplete.completeMethod
      .combineLatest(this.municipios$, (event: any, municipios) => municipios.filter(municipio => municipio.nombre.toLowerCase().indexOf(event.query.toLowerCase()) >= 0 ));

    this.loadComponent.emit(this);

    // para el caso de lista con datos incompletos
    if (this.contactsDefault && this.contactsDefault.length > 0) {

           this.CompletarDatosContacto();
    }

  }

  initForm() {
    this.form = this.formBuilder.group({
      'tipoVia': [null],
      'noViaPrincipal': [null],
      'prefijoCuadrante': [null],
      'bis': [null],
      'orientacion': [null],
      'noVia': [null],
      'prefijoCuadrante_se': [null],
      'placa': [null],
      'orientacion_se': [null],
      'complementoTipo': [null],
      'complementoAdicional': [null],
      'celular': [null],
      'numeroTel': [null],
      'correoEle': [null],
      'pais': [null],
      'departamento': [null],
      'municipio': [null],
      'principal': [null],
      'provinciaEstado': [null],
      'direccionText': [null],
      'ciudad': [null],
      'tipoContacto':[null]
    });
  }

  listenForErrors() {
    this.bindToValidationErrorsOf('tipoVia');
    this.bindToValidationErrorsOf('noViaPrincipal');
    this.bindToValidationErrorsOf('noVia');
    this.bindToValidationErrorsOf('placa');
    this.bindToValidationErrorsOf('correoEle');
  }

  listenForChanges() {
    const paisControl = this.form.get('pais');
    const departamentoControl = this.form.get('departamento');
    const municipioControl = this.form.get('municipio');

    this.subscribers.push(paisControl.valueChanges.subscribe(value => {
      if (this.editable && value) {
        departamentoControl.enable();
      } else {
        departamentoControl.reset();
        departamentoControl.disable();
      }
    }));

    paisControl.valueChanges.subscribe(value => {
      this.visibility.selectedColombia = true;
      if (value && value.codigo &&(value.codigo.toUpperCase() !== 'CO')) {
        this.visibility.selectedColombia = false;
        this.showCheckDireccionForm = false;
        this.showCheckDireccionForm = false;
        departamentoControl.reset();
        departamentoControl.disable();
        municipioControl.reset();
        municipioControl.disable();
      } else {
        this.showCheckDireccionForm = true;
      }

    });

    this.subscribers.push(departamentoControl.valueChanges.subscribe(value => {
      if (this.editable && value) {
        municipioControl.enable();
      } else {
        municipioControl.reset();
        municipioControl.disable();
      }
    }));

  }


  onDropdownClickPais(event?) {
    this._paisSandbox.loadDispatch();
  }

  onDropdownClickDepartamento($event) {
    const pais = this.form.get('pais').value;
    if (pais) {
      this._departamentoSandbox.loadDispatch({codPais: pais.codigo});
      this.form.get('municipio').reset();
    }
  }

  onDropdownClickMunicipio($event) {
    const departamento = this.form.get('departamento').value;
    if (departamento) {
      this._municipioSandbox.loadDispatch({codDepar: departamento.codigo});
    }
  }

  listenForBlurEvents(control: string) {
    const ac = this.form.get(control);

    const keyFields = ['noViaPrincipal','noVia','placa','complementoAdicional'];

    if(keyFields.some( k => k == control))
      if(!isNullOrUndefined(ac) && !isNullOrUndefined(ac.value))
        ac.setValue(ac.value.toString().trim());

    // if (ac.touched && ac.invalid) {
    //   const error_keys = Object.keys(ac.errors);
    //   const last_error_key = error_keys[error_keys.length - 1];
    //   this.validations[control] = VALIDATION_MESSAGES[last_error_key];
    // }
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

  saveAndRetriveContact(): any {

    const pais = this.form.get('pais');
    const departamento = this.form.get('departamento');
    const municipio = this.form.get('municipio');
    const numeroTel = this.form.get('numeroTel');
    const celular = this.form.get('celular');
    const email = this.form.get('correoEle');
    const provinciaEstado = this.form.get('provinciaEstado');
    const ciudad = this.form.get('ciudad');
    const direccionText = this.form.get('direccionText');
    const principal = this.form.get('principal');
    const tipoContacto = this.form.get('tipoContacto');
    const toSave = tassign({
      tipoContacto:tipoContacto.value,
      pais: pais.value,
      departamento: departamento.value,
      municipio: municipio.value,
      numeroTel: numeroTel.value,
      celular: celular.value,
      correoEle: email.value,
      provinciaEstado: provinciaEstado.value,
      ciudad: ciudad.value,
      direccionText: direccionText.value,
      principal: (principal.value === true ? true : false)
    }, this.saveDireccionData());

    pais.reset();
    provinciaEstado.reset();
    ciudad.reset();
    direccionText.reset();
    departamento.reset();
    municipio.reset();
    numeroTel.reset();
    celular.reset();
    email.reset();
    principal.reset();

    this.showContactForm = false;
    this.showDireccionForm = false;

    return toSave;

  }

  saveDireccionData() {
    const tipoVia = this.form.get('tipoVia');
    const noViaPrincipal = this.form.get('noViaPrincipal');
    const prefijoCuadrante = this.form.get('prefijoCuadrante');
    const bis = this.form.get('bis');
    const orientacion = this.form.get('orientacion');
    const noVia = this.form.get('noVia');
    const prefijoCuadrante_se = this.form.get('prefijoCuadrante_se');
    const placa = this.form.get('placa');
    const orientacion_se = this.form.get('orientacion_se');
    const tipoComplemento = this.form.get('complementoTipo');
    const complementoAdicional = this.form.get('complementoAdicional');


    const pais = this.form.get('pais');
    const direccionText = this.form.get('direccionText');

    const value = {};

    if (pais.value && pais.value.codigo.toUpperCase() === 'CO') {

      if (tipoVia.value) {
           value['tipoVia'] = tipoVia.value;

        tipoVia.reset();
      }
      if (noViaPrincipal.value) {
           value['noViaPrincipal'] = noViaPrincipal.value;

        noViaPrincipal.reset();
      }
      if (prefijoCuadrante.value) {
           value['prefijoCuadrante'] = prefijoCuadrante.value;

        prefijoCuadrante.reset();
      }
      if (bis.value) {
            value['bis'] = bis.value;

        bis.reset();
      }
      if (orientacion.value) {
              value['orientacion'] = orientacion.value;

        orientacion.reset();
      }
      if (noVia.value) {
            value['noVia'] = noVia.value;

        noVia.reset();
      }
      if (prefijoCuadrante_se.value) {
           value['prefijoCuadrante_se'] = prefijoCuadrante_se.value;


        prefijoCuadrante_se.reset();
      }
      if (placa.value) {
            value['placa'] = placa.value;

        placa.reset();
      }
      if (orientacion_se.value) {
            value['orientacion_se'] = orientacion_se.value;


        orientacion_se.reset();
      }
      if (tipoComplemento.value) {
           value['complementoTipo'] = tipoComplemento.value;

        tipoComplemento.reset();
      }
      if (complementoAdicional.value) {
          value['complementoAdicional'] = complementoAdicional.value;


        complementoAdicional.reset();
      }

      value['direccion'] = JSON.stringify(value);

    } else {
      value['direccion'] = direccionText.value;
      this.showContactForm = false;
    }
    return value;
  }

  deleteContact(index) {
    const radref = [...this.contacts];
    radref.splice(index, 1);
    this.contacts = radref;
    this.nuevosContactos.emit(this.contacts);
  }

  editContact(index) {

    const addressKeys = ['tipoVia','noViaPrincipal','prefijoCuadrante','bis','orientacion','noVia',
      'prefijoCuadrante_se','placa','orientacion_se','complementoTipo','complementoAdicional','pais','direccion'];

    const values = this.contacts[index];
    this.form.patchValue(values);
    this.showContactForm =true;
    this.showCheckDireccionForm = (isNullOrUndefined(values.pais) ||  values.pais.codigo.toString().toUpperCase() == 'CO');
    this.showDireccionForm = this.showCheckDireccionForm  && addressKeys.some(key => values[key]);
    this.formContext = FormContextEnum.SAVE;
    this.editIndexContext = index;
    if(this.showDireccionForm)
       this.toggleDireccionForm(true);
  }

  isActive;
  addContact() {
    console.log("Add contact")
    this.isActive  = true;
    this.showContactForm = true;
    this.formContext = FormContextEnum.CREATE;
    this.addColombiaByDefault();
  }

  hasDireccionPrincipal(){

    return  this.contacts.some( (contact,index) => contact.principal === true && index !== this.editIndexContext)

  }
  onFilterPais(event) {

    //this.paisSuggestions$ = this.paisAutoComplete.completeMethod
    //  .combineLatest(this.paises$, (event: any, paises) => paises.filter(pais => pais.nombre.toLowerCase().indexOf(event.query.toLowerCase()) >= 0 ));


    //this.paisSuggestions$.map(paises => {
    //  paises.filter(pais => pais.nombre.toLowerCase().indexOf(event.query.toLowerCase()) >= 0 );
    //})
    /*this.paisSuggestions$.take(2).subscribe((values) => {
      console.log(values);
    })
    console.log("Lista ", this.paisSuggestions$);
    console.log(event);*/
  }

  onFilterDepartamento(event){

  }

  onFilterMinicipio(event){

  }

  save() {
    if (this.form.valid) {

      const principal = this.form.get('principal');
      if(principal.value === true && this.hasDireccionPrincipal() === true ){

        this._store.dispatch(new PushNotificationAction({
          severity: 'warn',
          summary: 'Recuerde que únicamente puede existir una dirección principal.Por lo cual se tomara esta dirección como la principal'
        }));

       this.contacts.forEach( contact => {

         contact.principal = false;
       });

      }else {
        if (this.formContext === FormContextEnum.CREATE) {
          this.contacts = [this.saveAndRetriveContact(), ...this.contacts];
        } else {
          const temp = [...this.contacts];
          temp[this.editIndexContext] = this.saveAndRetriveContact();
          this.contacts = temp;
        }
        this.formContext = null;
        this.editIndexContext = null;

        this.nuevosContactos.emit(this.contacts);
      }
    }
  }

  toggleDireccionVisibility() {
    this.showDireccionForm = !this.showDireccionForm;
  }

  ngOnDestroy() {
    this.subscribers.forEach(subscriber => {
      subscriber.unsubscribe();
    });
  }

  ngAfterViewInit() {
    this.refreshView();
  }

  addColombiaByDefault() {
    const subscription = this.paises$

     .filter(values => values.length > 0)

     .subscribe((values) => {
       this.paises = values;
       this.form.get('pais').setValue(values.find(value => value.codigo === 'CO'));
       setTimeout(() => subscription.unsubscribe());
     });

    this._paisSandbox.loadDispatch();

    this.visibility.selectedColombia = true;
  }


  refreshView() {
    this._changeDetectorRef.detectChanges();
  }

  toggleDireccionForm(checked:boolean){

    if(!checked || this.formContext != FormContextEnum.SAVE)
      return ;

    console.log(this.contacts[this.editIndexContext]);

    this.form.get("principal").setValue(this.contacts[this.editIndexContext].principal);

    try{
      let direccionData = JSON.parse(this.contacts[this.editIndexContext].direccion);

      if(isNullOrUndefined(direccionData))
        return;

      Object.keys(direccionData).forEach( key => {

        if(isNullOrUndefined(direccionData[key]))
          return;

        let control = this.form.get(key);

        if(control) {

          control.setValue(direccionData[key]);
        }

      });
    }
    catch (e) {
    }

  }

  CompletarDatosContacto() {

    this.subscribers.push( combineLatest(this._store.select(getTipoContactoArrayData), this._localizacionService.ListarMunicipiosActivos({})).subscribe(([tiposContacto, result]) => {

      this.contacts = this.contacts
        .reduce((_listado, _contact) => {
          if (result) {
            if (!isNullOrUndefined(_contact.municipio)) {
              const municipio = result.find(_item => _item.codigo === _contact.municipio.codigo);
              if (municipio) {
                const departamento = (<any>municipio).departamento;
                const pais = departamento.pais;
                if (!isNullOrUndefined(_contact.municipio))
                  _contact.municipio.nombre = _contact.municipio.nombre ? _contact.municipio.nombre : (municipio) ? municipio.nombre : '';
                if (!isNullOrUndefined(_contact.departamento))
                  _contact.departamento.nombre = _contact.departamento.nombre ? _contact.departamento.nombre : (departamento) ? departamento.nombre : '';
                if (!isNullOrUndefined(_contact.pais))
                  _contact.pais.nombre = _contact.pais.nombre ? _contact.pais.nombre : (pais) ? pais.nombre : '';
              }
            }

            if (!isNullOrUndefined(_contact.tipoContacto)) {
              _contact.tipoContacto = tiposContacto.find(tc => tc.codigo == _contact.tipoContacto.codigo);
            }
            _listado.push(_contact);
            return _listado;
          } else {
            return this.contacts;
          }
        }, []);

      this._changeDetectorRef.detectChanges();

    }))
  }

  isColombiaCodigo(codigo){
    return codigo == CODIGO_COLOMBIA;
  }

  ngOnChanges(changes: SimpleChanges): void {

    console.log("cambios:",changes);
  }


}
