import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Observable} from "rxjs/Observable";
import {RadicadoDTO} from "../../../domain/radicadoDTO";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ConstanteDTO} from "../../../domain/constanteDTO";
import {getTipologiaDocumentalArrayData} from "../../../infrastructure/state-management/constanteDTO-state/constanteDTO-selectors";
import {Store} from "@ngrx/store";
import {State as RootState} from "../../../infrastructure/redux-store/redux-reducers";
import {RadicadosApi} from "../../../infrastructure/api/radicados.api";
import {getTipoDocumentoArrayData} from "../../../infrastructure/state-management/constanteDTO-state/selectors/tipo-documento-selectors";
import {
  PERSONA_JURIDICA,
  TPDOC_NRO_IDENTIFICACION_TRIBUTARIO
} from "../../../shared/bussiness-properties/radicacion-properties";
import {isNullOrUndefined} from "util";

@Component({
  selector: 'app-asociar-radicados',
  templateUrl: './asociar-radicados.component.html',
  styleUrls: ['./asociar-radicados.component.css']
})
export class AsociarRadicadosComponent implements OnInit {

  radicados$:Observable<RadicadoDTO[]> = Observable.empty();

  form:FormGroup;

  tiposDocumento$: Observable<ConstanteDTO[]>;

  tiposDocumentosList$ : Observable<ConstanteDTO[]>;

  @Input() editable = true

  @Output() asociarRadicadoEvent:EventEmitter<any> = new EventEmitter<any>();

 idEcm:string  = null;


  radicadoSelected;

  constructor(private fb:FormBuilder,private _store:Store<RootState>,private  radicadosApi:RadicadosApi) {

    this.initForm();
  }

   private initForm(){

       this.form = this.fb.group({
         'tipoDocumento':[null],
         "numIdentificacion":[null],
         "anno":[new Date().getFullYear()%100],
         "noRadicado":[null],
         "nombre":[null],
         "noGuia":[null],
       })

     this.form.get('numIdentificacion').valueChanges.subscribe( value => {

       this.form.get('tipoDocumento').clearValidators();

       if(value){
         this.form.get('tipoDocumento').setValidators(Validators.required);
       }

       this.form.get('tipoDocumento').updateValueAndValidity();
     })
   }

  ngOnInit() {
    this.tiposDocumento$ = this._store.select(getTipoDocumentoArrayData);


  }

  buscarRadicados(){

    this.tiposDocumentosList$ = this.tiposDocumento$.delay(30).share();

    this.radicados$ = this.radicadosApi.getRadicados(this.radicadosFormValue);


  }

  get radicadosFormValue(){

    let request = {};

    Object.keys(this.form.value).filter( k => !isNullOrUndefined(this.form.value[k]))
      .forEach( key => {
        request[key] = this.form.value[key]
      });

    return request;
  }

  changeRadicado(evt,index){

    if( !isNullOrUndefined(this.radicadoSelected) && evt.data.codigo  == this.radicadoSelected.codigo)
       this.radicadoSelected = null;
  }

  asociarRadicado(){

    this.asociarRadicadoEvent.emit(this.radicadoSelected);
  }

  isPersonaJuridica(radicado){
    return radicado.codTipoPers == PERSONA_JURIDICA;
  }

  listenForBlur(control:string){

    const value = this.form.get(control).value;

     if(!isNullOrUndefined(value))
        this.form.get(control).setValue(value.toString().trim());

  }

  showDocuments(idEcm){

    this.idEcm  = idEcm;
  }

  hideDocuments(idEcm){
    this.idEcm = null;
  }

  clearDropdown(){
    this.form.get('tipoDocumento').setValue(null);
  }

}
