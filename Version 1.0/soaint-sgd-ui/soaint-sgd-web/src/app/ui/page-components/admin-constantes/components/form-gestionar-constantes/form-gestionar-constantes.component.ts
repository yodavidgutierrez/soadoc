import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {ConstanteDTO} from "../../../../../domain/constanteDTO";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {isNullOrUndefined} from "util";
import {VALIDATION_MESSAGES} from "../../../../../shared/validation-messages";
import {ConstanteApiService} from "../../../../../infrastructure/api/constantes.api";
import {FormContextEnum} from "../../../../../shared/enums/enums";
import {Observable} from "rxjs/Observable";
import {Store} from "@ngrx/store";
import {State as RootState} from "../../../../../infrastructure/redux-store/redux-reducers";
import {PushNotificationAction} from "../../../../../infrastructure/state-management/notifications-state/notifications-actions";

@Component({
  selector: 'app-form-gestionar-constantes',
  templateUrl: './form-gestionar-constantes.component.html',
  styleUrls: ['./form-gestionar-constantes.component.css']
})
export class FormGestionarConstantesComponent implements OnInit,OnChanges {

  form:FormGroup;
  @Input() item:ConstanteDTO;
  validations:any = {};
  tipoListasEditable$:Observable<ConstanteDTO[]>;

  formStatus:FormContextEnum;

 @Output() onCancel:EventEmitter<any> = new EventEmitter;
 @Output() onCreate:EventEmitter<any> = new EventEmitter;
 @Output() onEdit:EventEmitter<any> = new EventEmitter;


  constructor(private fb:FormBuilder,private _constantesApi:ConstanteApiService,private _store:Store<RootState>) {

    this.initForm();
  }

  initForm(){

    this.form = this.fb.group({
      nombre:[null,Validators.required],
      codPadre:[null,Validators.required],
      estado:['A']
    })
  }

  listenForBlur(control:string) {

    const ac = this.form.get(control);
    const value = ac.value;
    if (control == 'nombre' && !isNullOrUndefined(value))
      this.form.get(control).setValue(value.toString().trim());

    if (ac.touched && ac.invalid) {
      const error_keys = Object.keys(ac.errors);

      const last_error_key = error_keys[error_keys.length - 1];
      this.validations[control] = VALIDATION_MESSAGES[last_error_key];
    }
  }

  focusInput(control:string){

    delete this.validations[control];
  }

  save(){
    if(this.formStatus == FormContextEnum.CREATE )
       this.create();
    else
      this.edit();

  }

  private create(){
   this._constantesApi.addConstante(this.form.value)
     .subscribe( _ => {

       this._store.dispatch(new PushNotificationAction({severity:"success",summary:"Se ha creado un nuevo valor de la lista"}));

       this.onCreate.emit(this.form.value);

       this.form.reset();
       this.form.get('codPadre').enable();
       this.form.get('estado').setValue('A');
     });
  }

  private edit(){

    const constante = Object.assign({...this.item},{...this.form.value});
    delete constante.nomPadre;

    this._constantesApi.editConstante(constante)
      .subscribe(_ => {

        this._store.dispatch(new PushNotificationAction({severity:"success",summary:"Se ha actualizado un valor de la lista"}));
        const  $event = {
          old:this.item,
          new:constante,
        };
        this.onEdit.emit($event);
        });
  }

  ngOnInit() {
       this.tipoListasEditable$ = this._constantesApi.getListasEditablesGenericas();
  }

  ngOnChanges(changes: SimpleChanges): void {

    this.formStatus = !isNullOrUndefined(this.item) ? FormContextEnum.SAVE : FormContextEnum.CREATE;

     if(!isNullOrUndefined(this.item)){
      this.form.patchValue({
        nombre:this.item.nombre,
        codPadre:this.item.codPadre,
        estado:this.item.estado
      });
      this.form.get('codPadre').disable();
    }
    else{

       this.form.reset();
       this.form.get('codPadre').enable();
       this.form.get('estado').setValue('A');
     }
    this.form.updateValueAndValidity();
  }

cancel(){
    this.onCancel.emit();
}



}
