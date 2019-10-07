
import {Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SolicitudCreacionUDDto} from "../../../../../domain/solicitudCreacionUDDto";
import {ConfirmationService} from "primeng/primeng";
import {isNullOrUndefined} from "util";
import {State as RootState} from "../../../../../infrastructure/redux-store/redux-reducers";
import {Store} from "@ngrx/store";
import {Subscription} from "rxjs/Subscription";
import {
  getAuthenticatedFuncionario,
  getSelectedDependencyGroupFuncionario
} from "../../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {DependenciaDTO} from "../../../../../domain/dependenciaDTO";
import {SolicitudCreacionUdService} from "../../../../../infrastructure/api/solicitud-creacion-ud.service";
import {ConstanteDTO} from "../../../../../domain/constanteDTO";
import {Observable} from "rxjs/Observable";
import  {Sandbox as ConstanteSandbox} from "../../../../../infrastructure/state-management/constanteDTO-state/constanteDTO-sandbox";
import {SolicitudCreacioUdModel} from "../../archivar-documento/models/solicitud-creacio-ud.model";
import {SupertypeSeries} from "../../shared/supertype-series";
import {getMotivoNoCreacionUDArrayData} from "../../../../../infrastructure/state-management/constanteDTO-state/selectors/motivo-no-creacion-ud-selectors";
import {VALIDATION_MESSAGES} from "../../../../../shared/validation-messages";
import {MNT_YA_EXISTE} from "../../../../../shared/bussiness-properties/radicacion-properties";
import {FuncionarioDTO} from "../../../../../domain/funcionarioDTO";

@Component({
  selector: 'app-no-tramitar-creacion-ud',
  templateUrl: './no-tramitar-creacion-ud.component.html',
  providers:[ConfirmationService]
})
export class NoTramitarCreacionUdComponent  implements OnInit,OnDestroy {

  form:FormGroup;

  dependenciaSelected:DependenciaDTO;

  motivo$:Observable<ConstanteDTO[]>;

  subscriptions:Subscription[] = [];

  archivista:FuncionarioDTO;

  @Input() solicitudModel:SolicitudCreacioUdModel;

  @Output() onNoTramitarUnidadDocumental:EventEmitter<any> = new EventEmitter;


  constructor( private fb:FormBuilder
              ,private _solicitudService:SolicitudCreacionUdService
              ,private _confirmationService:ConfirmationService
              ,private  _store:Store<RootState>
              ,private  _sandbox: ConstanteSandbox
               ) {

    this.form = fb.group({
      "identificador":[{value:null,disabled:true}],
      "nombre":[{value:null,disabled:true}],
      "descriptor1":[{value:null,disabled:true}],
      "descriptor2":[{value:null,disabled:true}],
      "motivo":[MNT_YA_EXISTE,Validators.required],
      "observaciones":[null,Validators.required]
    });
  }


 noTramitarCreacionUnidadesDocumentales(){

   this._confirmationService.confirm({
     message: '¿Está seguro que desea no gestionar esta unidad documental?',
     header: 'Confirmacion',
     icon: 'fa fa-question-circle',
     accept: () => {

       const solicitud = this.solicitudModel.SolicitudSelected;

       if(!isNullOrUndefined(this.form.get('motivo')))
         solicitud.motivo =  this.form.get('motivo').value;

         solicitud.observaciones = this.form.get('observaciones').value;

       solicitud.accion = "No Tramitar UD";

       const noTramiteDto ={
         solicitudUnidadDocumentalDTO:solicitud,
         idArchivista: this.archivista.id
       };

       this.subscriptions.push(
         this._solicitudService.noTramitarCreacionSolicitudUd(noTramiteDto)
           .subscribe(() => {

             this.onNoTramitarUnidadDocumental.emit({action:"No crear UD"});

             this.solicitudModel.removeAtIndex();
           }, error => {})
       );


     },
     reject: () => {}
   });
 }
  ngOnInit(): void {

     this._sandbox.loadMotivoNoCreacionUdDispatch();

     this.motivo$ = this._store.select(getMotivoNoCreacionUDArrayData);

    this.subscriptions.push(
      this._store.select(getSelectedDependencyGroupFuncionario).subscribe( dependencia => this.dependenciaSelected = dependencia )
    ) ;

    this.subscriptions.push(this._store.select(getAuthenticatedFuncionario).subscribe( funcionario => this.archivista = funcionario));

    if(this.solicitudModel.SelectedIndex == -1)
      return;

    const solicitud = this.solicitudModel.SolicitudSelected;

    this.form.get('identificador').setValue(solicitud.id);
    this.form.get("nombre").setValue(solicitud.nombreUnidadDocumental);
    this.form.get("descriptor1").setValue(solicitud.descriptor1);
    this.form.get("descriptor2").setValue(solicitud.descriptor2);
  }

  ngOnDestroy(): void {

    this.subscriptions.forEach( subscription => subscription.unsubscribe());

  }

  listenForBlurEvents(control: string) {
    const ac = this.form.get(control);

    if(control == 'observaciones' )
      if(!isNullOrUndefined(ac) && !isNullOrUndefined(ac.value))
        ac.setValue(ac.value.toString().trim());

  }

}
