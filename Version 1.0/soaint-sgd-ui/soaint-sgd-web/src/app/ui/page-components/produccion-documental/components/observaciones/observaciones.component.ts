import {ChangeDetectorRef, Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {PdObservacionDTO} from "../../../../../domain/pdObservacionDTO";
import {TareaDTO} from "../../../../../domain/tareaDTO";
import {PdObservacionApi} from "../../../../../infrastructure/api/pd-observacion.api";
import {PRODUCCION_DOCUMENTAL_COMPONENTS} from "../../_pd-components.include";
import {Store} from "@ngrx/store";
import  {State as RootState} from "../../../../../infrastructure/redux-store/redux-reducers";
import {Subscription} from "rxjs/Subscription";
import {getAuthenticatedFuncionario} from "../../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {isNullOrUndefined} from "util";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {VALIDATION_MESSAGES} from "../../../../../shared/validation-messages";

@Component({
  selector: 'pd-observaciones',
  templateUrl: './observaciones.component.html',
  styleUrls: ['./observaciones.component.css']
})
export class ObservacionesComponent implements OnInit,OnDestroy {

  observaciones:PdObservacionDTO[] = [];

  validations:any = {};

  form:FormGroup;

  indexObservacion = -1;

  visibleObservacionPopup = false;

  subscriptions:Subscription[] = [];

  @Input() tarea :TareaDTO;

  @Output() updateObservacionesEvent:EventEmitter<PdObservacionDTO[]> = new EventEmitter<PdObservacionDTO[]>();

  constructor(private _pdObsevacionesApi:PdObservacionApi,private _store:Store<RootState>,private fb:FormBuilder,private _changeDetector:ChangeDetectorRef) {
    this.initForm();
  }

  private initForm(){

    this.form = this.fb.group({
      observacion:['',Validators.maxLength(500)]
    });

    const observacionControl = this.form.get('observacion');

      observacionControl.valueChanges.subscribe(_ => {

        delete this.validations.observacion;

         if( observacionControl.invalid){

           const error_keys = Object.keys(observacionControl.errors);
           const last_error_key = error_keys[error_keys.length - 1];
           this.validations.observacion = VALIDATION_MESSAGES[last_error_key];
         }


    })
  }

  ngOnInit() {

    if(this.tarea.nombre == 'Producir Documento' && this.tarea.descripcion.indexOf('Ajustar Documento') === -1 ){

      if(this.tarea.variables.observacion){

        let  observacion = this.tarea.variables.observacion;

         if(this.tarea.variables.observacion1)
            observacion += this.tarea.variables.observacion1;

          this.observaciones = [{
          fechaCreacion:this.tarea.fechaCreada,
          idInstancia: this.tarea.idInstanciaProceso,
          idTarea:'',
          observacion: observacion,
          usuario:this.tarea.variables.funcionario.toString()
        }];

          this.updateObservacionesEvent.emit(this.observaciones);
      }

       return;
    }

     this._pdObsevacionesApi.listar(this.tarea.idInstanciaProceso)
       .subscribe( observaciones => {
         this.observaciones = observaciones;

         this.updateObservacionesEvent.emit(this.observaciones);
       })
  }

  showPopupObservacion(index = -1){

    this.visibleObservacionPopup = true;

    this.indexObservacion = index;

    this.observacionEdit = index == -1 ? '' : this.observacionSelected.observacion;
  }

  private addObservacion(){

    this.subscriptions.push(  this._store.select(getAuthenticatedFuncionario).subscribe( funcionario => {
      this.observaciones = [... this.observaciones,
        {
          idTarea:this.tarea.idTarea,
          idInstancia:this.tarea.idInstanciaProceso,
          usuario: `${funcionario.nombre} ${isNullOrUndefined(funcionario.valApellido1)? '' : funcionario.valApellido1}  ${isNullOrUndefined(funcionario.valApellido2)? '' : funcionario.valApellido2}`,
          observacion: this.observacionEdit,
          fechaCreacion : new Date()
        }];

      this.hidePopupObservacion();

      this.updateObservacionesEvent.emit(this.observaciones);

    }));

  }

  private editObservacion(){

    this.observacionSelected.observacion = this.observacionEdit;
    this.observacionSelected.fechaCreacion = new Date();
    this.hidePopupObservacion();

    this.updateObservacionesEvent.emit(this.observaciones);
  }

  saveObservacion(){

    if(isNullOrUndefined(this.observacionSelected))
       this.addObservacion();
    else
      this.editObservacion();

  }

  removeObservacion(index){

    this.observaciones = this.observaciones.filter( (_,idx) => idx != index);

    this.updateObservacionesEvent.emit(this.observaciones);
  }

  get observacionSelected(){

    return this.observaciones[this.indexObservacion];
  }

  hidePopupObservacion(){

    this.visibleObservacionPopup = false;

    this.indexObservacion = -1;
  }

  ngOnDestroy(): void {

    this.subscriptions.forEach( s => s.unsubscribe());
  }

  get observacionEdit(){
    return this.form.get('observacion').value;
  }

  set observacionEdit(value){
    this.form.get('observacion').setValue(value);
  }
}
