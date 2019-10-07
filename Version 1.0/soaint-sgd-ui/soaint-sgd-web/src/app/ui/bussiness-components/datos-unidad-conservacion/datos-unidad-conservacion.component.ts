import {ChangeDetectorRef, Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MegafServiceApi} from "../../../infrastructure/api/megaf-service.api";
import {Observable} from "rxjs/Observable";
import {Store} from "@ngrx/store";
import  {State as RootState} from "../../../infrastructure/redux-store/redux-reducers";
import {getSelectedDependencyGroupFuncionario} from "../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {Subscription} from "rxjs/Subscription";
import {ARCHIVO_GESTION} from "../../../shared/bussiness-properties/radicacion-properties";
import {PushNotificationAction} from "../../../infrastructure/state-management/notifications-state/notifications-actions";


@Component({
  selector: 'app-datos-unidad-conservacion',
  templateUrl: './datos-unidad-conservacion.component.html',
  styleUrls: ['./datos-unidad-conservacion.component.css']
})
export class DatosUnidadConservacionComponent implements OnInit,OnDestroy {

  bodegaObservable$:Observable<any[]>;

  @Input() tipoArchivo;

  piso:any[] = [] ;

  estante :any[] = [];

  seccion : any[] = [];

  cajas : any[] = [];

  carpeta : any[] = [];

  visibility = {
    estante:true,
    seccion:true,
    caja:true
  };

  subscriptions: Subscription[] = [];
  form:FormGroup;

 @Output() onChange:EventEmitter<any> = new EventEmitter<any>();
 @Output() onNoFound:EventEmitter<any> = new EventEmitter<any>();

  constructor( private  fb:FormBuilder
              ,private _megafApi:MegafServiceApi
              ,private _store:Store<RootState>
              ,private detectChange:ChangeDetectorRef) {

    this.formInit();
  }

  ngOnInit() {

    this.bodegaObservable$ =   this._store.select(getSelectedDependencyGroupFuncionario).
                                switchMap(dependencia =>  this._megafApi.getDirectChilds(dependencia.codigo,this.tipoArchivo))
                               .map( response => response.lista)
                               .do( lista => {
                                 if(lista.length === 0)
                                  this.onNoFound.emit();
                               });


  }

  private formInit(){

    this.form = this.fb.group({
      bodega:[null,Validators.required],
      piso:[null],
      estante:[null],
      seccion:[null],
      caja:[null],
      carpeta:[null,Validators.required],
    });

    this.form.valueChanges.subscribe(value => {

       this.onChange.emit({value:value,isValid:this.form.valid});
    });
  }

  selectHandler(event,controlName){

    this.subscriptions.push( this._store.select(getSelectedDependencyGroupFuncionario)
      .switchMap( dependencia => this._megafApi.getDirectChilds(dependencia.codigo,this.tipoArchivo,event.value.ide))
      .subscribe( response => {

        let controlChild;

        switch (response.nomUbicacion){
          case 'PISO': controlChild = 'piso';
            break;
          case 'ESTANTE': controlChild = 'estante';
            break;
          case 'ENTREPAÑO' :  controlChild = 'seccion';
            break;
          case 'CAJA' : controlChild = 'caja';
            break;
          case 'ROLLO':
          case 'CARPETA' : controlChild = 'carpeta';
            break;
        }

        this[controlChild] = response.lista;

        let flag;
        let keyCompare = controlName;

        Object.keys(this.form.controls).forEach(key => {

          if(keyCompare == key){
            flag = flag === undefined ? false :  !flag;
            keyCompare = controlChild;
            this.visibility[key] = true;
          }
          else{
            if(flag !== undefined)
              this.visibility[key] = flag;
          }
        });

        const index = Object.keys(this.form.controls).findIndex( control => control == controlName);

        Object.keys(this.form.controls)
          .filter((_,idx) => idx > index)
          .map(controlKey => {
            if(controlKey != controlChild){
              this[controlKey] = [];
            }
            return this.form.get(controlKey);
          })
          .forEach( control => {
            control.setValue(null);

          });

        this.detectChange.detectChanges();

      }));

  }

  ngOnDestroy(): void {

     this.subscriptions.forEach(s => s.unsubscribe());
  }



}
