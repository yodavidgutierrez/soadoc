import {Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges} from '@angular/core';
import {FuncionarioDTO} from "../../../../../domain/funcionarioDTO";
import { FormBuilder, FormGroup, Validators} from "@angular/forms";
import {NumericValidator} from "../../../../../shared/validators/numeric.validator";
import {Observable} from "rxjs/Observable";
import {ConstanteDTO} from "../../../../../domain/constanteDTO";
import {Store} from "@ngrx/store";
import {State as RootState} from "../../../../../infrastructure/redux-store/redux-reducers";
import {getTipoDocumentoArrayData} from "../../../../../infrastructure/state-management/constanteDTO-state/selectors/tipo-documento-selectors";
import {TPDOC_NRO_IDENTIFICACION_TRIBUTARIO} from "../../../../../shared/bussiness-properties/radicacion-properties";
import {FuncionariosService} from "../../../../../infrastructure/api/funcionarios.service";
import {getAuthenticatedFuncionario} from "../../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {Sandbox as FuncionarioSandbox} from "../../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-sandbox";
import {isNullOrUndefined} from "util";
import {VALIDATION_MESSAGES} from "../../../../../shared/validation-messages";
import {Subscription} from "rxjs/Subscription";
import {PushNotificationAction} from "../../../../../infrastructure/state-management/notifications-state/notifications-actions";


@Component({
  selector: 'app-profile-edit',
  templateUrl: './profile-edit.component.html',
  styleUrls: ['./profile-edit.component.css']
})
export class ProfileEditComponent implements OnInit,OnChanges,OnDestroy {

  @Input() funcionario:FuncionarioDTO = null;
  tipoDocumentoId$: Observable<ConstanteDTO[]>;

  subscriptions:Subscription[] = [];

  @Output() updateFuncionarioEvent : EventEmitter<FuncionarioDTO> = new EventEmitter;

  validations: any = {};

  form:FormGroup;

  constructor(private fb:FormBuilder,
              private _store:Store<RootState>,
              private _funcionarioApi:FuncionariosService,
              private _funcionarioSanbox:FuncionarioSandbox
              ) {

    this.initForm();
  }

  ngOnInit() {

    this.tipoDocumentoId$ = this._store.select(getTipoDocumentoArrayData)
      .map( tipos => tipos.filter( tipo => tipo.codigo != TPDOC_NRO_IDENTIFICACION_TRIBUTARIO));


  }

  private initForm(){

    this.form = this.fb.group({
      nombre:[null,Validators.required],
      valApellido1:[null,Validators.required],
      valApellido2:[null,Validators.required],
      corrElectronico:[null,Validators.compose([Validators.required,Validators.email])],
      codTipDocIdent:[null],
      nroIdentificacion:[null,NumericValidator.validate],
      cargo:[null],
      firmaPolitica:[null],
      esJefe:[false]
    })

  }

  save(){

    this.funcionario = Object.assign(this.funcionario,this.form.value);

  this.subscriptions.push( this._funcionarioApi.update(this.funcionario).subscribe(_ => {

  this.updateFuncionarioEvent.emit(this.funcionario);

  this.subscriptions.push(
    this._store.select(getAuthenticatedFuncionario).subscribe( funcionario => {
      if(this.funcionario.id == funcionario.id){

        this._funcionarioSanbox.updateFuncinarioDispatch(this.funcionario);
      }

      this._store.dispatch(new PushNotificationAction({severity:'success',summary:`Se han actualizado el perfil de usuario`}))
    })
  );

}))

  }

  listenForBlurEvents(control: string) {

    delete this.validations[control];

    const ac = this.form.get(control);

    const controlKeys = ['nombre','valApellido1','valApellido2','cargo','firmaPolitica'];

    if(controlKeys.some( key => control == key) && !isNullOrUndefined(ac) && !isNullOrUndefined(ac.value))
      ac.setValue(ac.value.toString().trim())

    if (ac.touched && ac.invalid) {
      const error_keys = Object.keys(ac.errors);


      const last_error_key =  error_keys[error_keys.length - 1];
      this.validations[control] =  VALIDATION_MESSAGES[last_error_key];
    }
  }

  ngOnChanges(changes: SimpleChanges): void {

    if(changes.funcionario && !isNullOrUndefined(this.funcionario)){
      this.form.patchValue({
        nombre : this.funcionario.nombre,
        valApellido1 : this.funcionario.valApellido1,
        valApellido2 : this.funcionario.valApellido2,
        corrElectronico : this.funcionario.corrElectronico,
        codTipDocIdent : this.funcionario.codTipDocIdent,
        nroIdentificacion : this.funcionario.nroIdentificacion,
        cargo:this.funcionario.cargo,
        firmaPolitica: this.funcionario.firmaPolitica,
        esJefe: this.funcionario.esJefe
      })
    }
  }

  ngOnDestroy(): void {

    this.subscriptions.forEach(s => s.unsubscribe());
  }

}
