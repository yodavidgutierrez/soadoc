import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {VALIDATION_MESSAGES} from '../../../shared/validation-messages';
import {Sandbox as AsignacionSandbox} from '../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-sandbox';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from '../../../domain/constanteDTO';
import {Store} from '@ngrx/store';
import {getCausalDevolucionArrayData} from '../../../infrastructure/state-management/constanteDTO-state/selectors/causal-devolucion-selectors';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {Sandbox as ConstanteSandbox} from '../../../infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox';
import { isNullOrUndefined } from 'util';


@Component({
  selector: 'app-popup-reject',
  templateUrl: './popup-reject.component.html',
})
export class PopupRejectComponent implements OnInit {

  form: FormGroup;

  validations: any = {};

  // causalesDevolicion$: Observable<ConstanteDTO[]>;
  causalesDevolicion$: ConstanteDTO[];

  @Output()
  onRejectComunication: EventEmitter<any> = new EventEmitter();


  constructor(private formBuilder: FormBuilder, private _asignacionSandbox: AsignacionSandbox,
              private _contantesSandbox: ConstanteSandbox, private _store: Store<State>) {
    this.initForm();
    this.listenForErrors();
  }

  listCauseReturn(validateCause: string) {
    if (validateCause == null) {
      this.causalesDevolicion$ = [
        {
          id: 2,
          codigo: 'DI',
          nombre: 'Datos incorrectos'
        }];

    } else {
      this.causalesDevolicion$ = [
        {
          id: 1,
          codigo: 'CI',
          nombre: 'Calidad Imagen'
        }, {
          id: 2,
          codigo: 'DI',
          nombre: 'Datos incorrectos'
        }];
    }
  }


  ngOnInit(): void {
    // this.causalesDevolicion$ = this._store.select(getCausalDevolucionArrayData);
    this._contantesSandbox.loadCausalDevolucionDispatch();
  }

  initForm() {
    this.form = this.formBuilder.group({
      'observacion': [{value: null, disabled: false}, Validators.compose([Validators.required,Validators.maxLength(500)]) ],
      'causalDevolucion': [{value: null, disabled: false},Validators.required ]
    });
  }

  listenForErrors() {
    this.bindToValidationErrorsOf('causalDevolucion');
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
    this._asignacionSandbox.setVisibleRejectDialogDispatch(false);
  }

  devolverComunicaciones() {
    this.onRejectComunication.emit(this.form.value);
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

}
