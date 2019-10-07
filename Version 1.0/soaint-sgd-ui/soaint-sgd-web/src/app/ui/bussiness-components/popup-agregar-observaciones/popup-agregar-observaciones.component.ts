import {
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  Input,
  Output,
  EventEmitter,
  OnInit,
  OnDestroy
} from '@angular/core';

import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {VALIDATION_MESSAGES} from '../../../shared/validation-messages';
import {Sandbox as AsignacionSandbox} from '../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox';
import {ObservacionDTO} from '../../../domain/observacionDTO';
import { isNullOrUndefined } from 'util';
import {Store} from "@ngrx/store";
import {State as RootState} from "../../../infrastructure/redux-store/redux-reducers";
import {getSelectedDependencyGroupFuncionario} from "../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {Subscription} from "rxjs/Subscription";


@Component({
  selector: 'app-popup-agregar-observaciones',
  templateUrl: './popup-agregar-observaciones.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PopupAgregarObservacionesComponent implements OnInit,OnDestroy {
  form: FormGroup;

  subscriptions: Subscription[] = [];

  validations: any = {};

  observaciones: ObservacionDTO[];

  @Output()
  countObservaciones = new EventEmitter();

  @Input()
  idDocumento: number;

  @Input()
  idFuncionario: number;

  @Input()
  codOrgaAdmin: string;

  @Input()
  isPopup: Boolean;

  constructor(
    private formBuilder: FormBuilder,
    private _asignacionSandbox: AsignacionSandbox,
    private _store:Store<RootState>,
    private _changeDetectorRef: ChangeDetectorRef) {
    this.initForm();
    this.listenForErrors();
  }

  ngOnInit(): void {
  }

  initForm() {
    this.form = this.formBuilder.group({
      'observacion': [{value: null, disabled: false}, Validators.required]
    });
  }

  loadObservations() {
    this._asignacionSandbox.obtenerObservaciones(this.idDocumento).subscribe((results) => {
      let observacionesResponse: ObservacionDTO[] =  results.observaciones;
      observacionesResponse =  observacionesResponse.reduce((_listado, _current) => { // ajustar valApellido2 = null
        _current.valApellido2 = _current.valApellido2 || '';
        _listado.push(_current)
        return _listado;
      }, []);
      this.observaciones = [...observacionesResponse];
      this.countObservaciones.emit(this.observaciones.length);
      this._changeDetectorRef.detectChanges();
    });
  }

  listenForErrors() {
    this.bindToValidationErrorsOf('observacion');
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

  hideDialog() {
    this._asignacionSandbox.setVisibleAddObservationsDialogDispatch(false);
  }

  setData(data: any) {
    this.idDocumento = data.idDocumento;
    this.idFuncionario = data.idFuncionario;
    this.codOrgaAdmin = data.codOrgaAdmin;
    this.isPopup = data.isPopup == false ? false : true;
  }

  addObservation() {
    if (this.form.valid) {

      this.subscriptions.push(
        this._store.select(getSelectedDependencyGroupFuncionario)
          .switchMap(dep => {
            let observacion: ObservacionDTO = {
              observacion: this.form.get('observacion').value,
              ideFunci: this.idFuncionario,
              ideDocumento: this.idDocumento,
              codDependencia: dep.codigo,
              nomFuncionario: dep.nombre,
              nomDependencia: null,
              valApellido1: null,
              valApellido2: null,
              corrElectronico: null,
              loginName: null,
              id: null,
              fecha: null,
              estado: null,
            };

            return  this._asignacionSandbox.registrarObservacion(observacion);
          })
          .subscribe(() => {
            this.form.get('observacion').reset();
            this.loadObservations();
          })
      );
    }
  }

  listenForBlurEvents(control: string) {
    const ac = this.form.get(control);
    if(control == 'observacion')
     if(!isNullOrUndefined(ac) && !isNullOrUndefined(ac.value))
       ac.setValue(ac.value.toString().trim());
       this.validations[control] = null;
    if (ac.touched && ac.invalid) {
      const error_keys = Object.keys(ac.errors);
      let last_error_key = error_keys[error_keys.length - 1];

      if(last_error_key == 'pattern')
        last_error_key = 'required';
      this.validations[control] = [...VALIDATION_MESSAGES[last_error_key]];
    }
  }

  nombreCompleto(observacion:ObservacionDTO){

   return  `${observacion.nomFuncionario} ${!isNullOrUndefined(observacion.valApellido1) ? observacion.valApellido1: ''} ${!isNullOrUndefined(observacion.valApellido2) ? observacion.valApellido2 : ''}`;

  }

  ngOnDestroy(): void {

     this.subscriptions.forEach(s => s.unsubscribe());
  }
}
