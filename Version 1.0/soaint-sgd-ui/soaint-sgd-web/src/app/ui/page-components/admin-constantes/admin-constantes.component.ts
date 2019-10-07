import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {ConstanteDTO} from "../../../domain/constanteDTO";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Observable} from "rxjs/Observable";
import {ConstanteApiService} from "../../../infrastructure/api/constantes.api";
import {combineLatest} from "rxjs/observable/combineLatest";
import {isNullOrUndefined} from "util";
import {ConfirmationService} from "primeng/primeng";
import {State as RootState} from "../../../infrastructure/redux-store/redux-reducers";
import {PushNotificationAction} from "../../../infrastructure/state-management/notifications-state/notifications-actions";
import {Store} from "@ngrx/store";

@Component({
  selector: 'app-admin-constantes',
  templateUrl: './admin-constantes.component.html',
  styleUrls: ['./admin-constantes.component.css']
})
export class AdminConstantesComponent implements OnInit {

  form:FormGroup;

  listasEditable$:Observable<any[]>;
  tipoListasEditable$:Observable<ConstanteDTO[]>;

  first;

  constructor(private fb:FormBuilder,
              private _listaApi:ConstanteApiService,
              private _confirmService:ConfirmationService,
              private _store:Store<RootState>,
              private _detectChange:ChangeDetectorRef
  ) {

    this.initForm();
  }

  initForm(){

    this.form = this.fb.group({
      nombre:[null],
      codigos:[null]
    })
  }

  headerFormGestionar:string;

  constanteForEdit : ConstanteDTO = null;

  visibleFormEdit  = false;

  ngOnInit() {
    this.tipoListasEditable$ = this._listaApi.getListasEditablesGenericas();
     this.buscar();
  }

  showFormForCreate(){
    this.headerFormGestionar = "Crear nuevo nomenclador";
    this.constanteForEdit = null;
    this.visibleFormEdit = true;
    }

  showFormForEdit(nomenclador:ConstanteDTO){
    this.headerFormGestionar = `Editar nomenclador ${nomenclador.nombre}`;
    this.constanteForEdit = nomenclador;
    this.visibleFormEdit = true;
  }

  hideDialogForm(){
    this.visibleFormEdit = false;
  }

  buscar(){

    const payload = {};

    const formValue = this.form.value;

    Object.keys(formValue).forEach( key => {

     if(!isNullOrUndefined(formValue[key]))
        payload[key] = formValue[key];
    });

    this.first = 0;

    this.listasEditable$ = combineLatest( this._listaApi.getListasEditables(payload),this.tipoListasEditable$)
      .map(([hijos,padres]) => {

        return hijos.map( r => {
          const padre = padres.find( item => item.codigo == r.codPadre);
          r.nomPadre = padre.nombre;
          return r;
        })
      })
  }

  eliminarConstante(idConstante){

    this._confirmService.confirm({
      message: "No se podrá recuperar este valor.¿Está seguro que desea eliminarlo?",
      header: "Confirmación",
      icon : 'fa fa-question-circle',
      key:"delete-constant-confirm",
       accept: _ => {
        this._listaApi.deleteConstante(idConstante).subscribe(_ => {
          this._store.dispatch(new PushNotificationAction({severity:"success",summary:"Se ha eliminado un valor de la lista definitivamente"}));
          this.buscar();
          this._detectChange.detectChanges();
        });
        return;
      },
      reject: _ => {}
    })
  }

  saveHandler(){

    this.hideDialogForm();
    this.buscar();
  }




}



