import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from 'app/domain/constanteDTO';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';

import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {getArrayData as dependenciaGrupoArrayData} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-selectors';
import {getArrayData as sedeAdministrativaArrayData} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-selectors';
import {Sandbox as DependenciaGrupoSandbox} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {VALIDATION_MESSAGES} from '../../../shared/validation-messages';
import {LoadAction as SedeAdministrativaLoadAction} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-actions';
import {OrganigramaDTO} from '../../../domain/organigramaDTO';
import { isNullOrUndefined } from 'util';
import {Subscription} from "rxjs/Subscription";
import {getSelectedDependencyGroupFuncionario} from "../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {InstrumentoApi} from "../../../infrastructure/api/instrumento.api";
import {getJustificationDialogVisible} from "../../../infrastructure/state-management/asignacionDTO-state/asignacionDTO-selectors";

@Component({
  selector: ' app-popup-justificacion',
  templateUrl: './popup-justificacion.component.html',
})
export class PopupJustificacionComponent implements OnInit,OnDestroy {

  form: FormGroup;

  validations: any = {};

  sedeAdministrativaSuggestions$: Observable<ConstanteDTO[]>;

  dependenciaGrupoSuggestions$: Observable<ConstanteDTO[]>;

  subscriptions:Subscription[] = [];

  @Output()
  onChangeSedeAdministrativa: EventEmitter<any> = new EventEmitter();

  @Output()
  onRedirectComunication: EventEmitter<{ justificacion: string, sedeAdministrativa: OrganigramaDTO, dependenciaGrupo: OrganigramaDTO }> = new EventEmitter();

  constructor(private _store: Store<State>,
              private _instrumentoApi: InstrumentoApi,
              private formBuilder: FormBuilder) {
    this.initForm();
    this.listenForChanges();
    this.listenForErrors();
  }


  ngOnInit(): void {
    this.sedeAdministrativaSuggestions$ = this._store.select(sedeAdministrativaArrayData);
    this.dependenciaGrupoSuggestions$ = this._instrumentoApi.ListarDependencia();
    this._store.dispatch(new SedeAdministrativaLoadAction());
  /*  this.subscriptions.push(this._store.select(getSelectedDependencyGroupFuncionario).subscribe( dependencia => {
      this.form.get('sedeAdministrativa').setValue(dependencia.codSede);
      this.form.get('dependenciaGrupo').setValue(dependencia.codigo);
    }));*/


  }

  initForm() {
    this.form = this.formBuilder.group({
      'justificacion': [{value: null, disabled: false}, Validators.required],
      'sedeAdministrativa': [{value: null, disabled: true}, Validators.required],
      'dependenciaGrupo': [{value: null, disabled: true}, Validators.required],
    });
  }

  fillDependenciaOptions( filter?:(dependencia:ConstanteDTO) => boolean){

    this.dependenciaGrupoSuggestions$ = this._instrumentoApi.ListarDependencia()
      .map(dependencias => dependencias.filter( dependncy => isNullOrUndefined(filter) || filter(dependncy)));
  }

  listenForChanges() {
   this.form.get('sedeAdministrativa').valueChanges.subscribe((value) => {
     const control = this.form.get('sedeAdministrativa');
      if (value  && control.enabled) {
        this.onChangeSedeAdministrativa.emit(value);
        this.form.get('dependenciaGrupo').setValue(null);
        this.dependenciaGrupoSuggestions$ = this._instrumentoApi.ListDependences({value});
      }
    })
  }

  listenForErrors() {
    this.bindToValidationErrorsOf('sedeAdministrativa');
    this.bindToValidationErrorsOf('dependenciaGrupo');
    this.bindToValidationErrorsOf('justificacion');
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

  listenForBlurEvents(control: string) {
    const ac = this.form.get(control);
    if(control === 'justificacion')
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

  redirectComunications() {
    this.onRedirectComunication.emit({
      justificacion: this.form.get('justificacion').value,
      sedeAdministrativa: this.form.get('sedeAdministrativa').value,
      dependenciaGrupo: this.form.get('dependenciaGrupo').value,
    });
  }

  ngOnDestroy(): void {

    this.subscriptions.forEach(s => s.unsubscribe())
  }
}
