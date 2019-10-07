import {
  ChangeDetectorRef, Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output,
  ViewChild
} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {Subscription} from 'rxjs/Subscription';
import {PdMessageService} from '../../providers/PdMessageService';
import {TareaDTO} from '../../../../../domain/tareaDTO';
import {StatusDTO} from '../../models/StatusDTO';
import {DestinatarioDTO} from '../../../../../domain/destinatarioDTO';
import {ProduccionDocumentalApiService} from '../../../../../infrastructure/api/produccion-documental.api';
import {Sandbox as DependenciaSandbox} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {getArrayData as sedeAdministrativaArrayData} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-selectors';
import {LoadAction as SedeAdministrativaLoadAction} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-actions';

import {getArrayData as municipioArrayData} from 'app/infrastructure/state-management/municipioDTO-state/municipioDTO-selectors';
import {LoadAction as LoadMunicipioAction} from 'app/infrastructure/state-management/municipioDTO-state/municipioDTO-actions';
import {getArrayData as paisArrayData} from 'app/infrastructure/state-management/paisDTO-state/paisDTO-selectors';
import {LoadAction as LoadPaisAction} from 'app/infrastructure/state-management/paisDTO-state/paisDTO-actions';
import {getArrayData as departamentoArrayData} from 'app/infrastructure/state-management/departamentoDTO-state/departamentoDTO-selectors';
import {LoadAction as LoadDepartamentoAction} from 'app/infrastructure/state-management/departamentoDTO-state/departamentoDTO-actions';

import {Sandbox as DepartamentoSandbox} from 'app/infrastructure/state-management/departamentoDTO-state/departamentoDTO-sandbox';
import {Sandbox as MunicipioSandbox} from 'app/infrastructure/state-management/municipioDTO-state/municipioDTO-sandbox';

import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {Store} from '@ngrx/store';
import {isNullOrUndefined} from 'util';
import {
  getTipoDocumentoArrayData,
  getTipoPersonaArrayData,
} from 'app/infrastructure/state-management/constanteDTO-state/constanteDTO-selectors';
import {
  DESTINATARIO_COPIA,
  DESTINATARIO_PRINCIPAL, PERSONA_ANONIMA, PERSONA_JURIDICA, PERSONA_NATURAL,
  TIPO_REMITENTE_EXTERNO,
  TIPO_REMITENTE_INTERNO
} from '../../../../../shared/bussiness-properties/radicacion-properties';
import {Observable} from "rxjs/Observable";
import {ViewFilterHook} from "../../../../../shared/ViewHooksHelper";
import {
  getDestinatarioPrincial,
  getTipoDestinatarioArrayData
} from "../../../../../infrastructure/state-management/constanteDTO-state/selectors/tipo-destinatario-selectors";
import {ConfirmationService} from "primeng/primeng";


@Component({
  selector: 'pd-datos-contacto',
  templateUrl: 'datos-contacto.component.html',
  styleUrls: ['datos-contacto.component.css'],

})

export class PDDatosContactoComponent implements OnInit, OnDestroy,OnChanges {

  disabledCheck = false;
  valueCheck = false;
  resCont : number;
  iDestinado : any;
  form: FormGroup;

  subscriptions: Subscription[] = [];

  operationActive: 'CREATE' | 'UPDATE';

  @Output() onSelectDistribucionElectronica:EventEmitter<boolean> = new EventEmitter;

  validations: any = {};

  @Input() sectionsVisible = {'interno':true,'externo':true};

  @Input() dataDefault:Observable<any> = Observable.empty();

 listaDestinatariosInternos: DestinatarioDTO[] = [];
 listaDestinatariosExternos: DestinatarioDTO[] = [];

  destinatarioInterno: DestinatarioDTO = null;
  destinatarioExterno: DestinatarioDTO = null;

  hasNumberRadicado = false;
  @Input() editable = true;
  defaultDestinatarioTipoComunicacion = '';
  responderRemitente = false;
  issetListDestinatarioBacken = false;
  indexSelectExterno: -1;
  indexSelectInterno: -1;

  destinatarioExternoDialogVisible = false;
  destinatarioInternoDialogVisible = false;

  @ViewChild('datosRemitentesExterno') datosRemitentesExterno;
  @ViewChild('datosRemitentesInterno') datosRemitentesInterno;

  @Input() taskData: TareaDTO;

 private showFormFilter:string;



  constructor(private formBuilder: FormBuilder,
              private _changeDetectorRef: ChangeDetectorRef,
              private _produccionDocumentalApi: ProduccionDocumentalApiService,
              private pdMessageService: PdMessageService,
              private _dependenciaSandbox: DependenciaSandbox,
              private _departamentoSandbox: DepartamentoSandbox,
              private _municipioSandbox: MunicipioSandbox,
              private _confirmDialog: ConfirmationService,
              private _store: Store<State>) {
    this.resCont = 0;
    this.initForm();
  }

  dispatchSelectDistElectronica(event){

    this.onSelectDistribucionElectronica.emit(event.checked);
  }

  ngOnInit(): void {

    this._store.dispatch(new SedeAdministrativaLoadAction());
    this._store.dispatch(new LoadPaisAction());
    if (this.taskData.variables && this.taskData.variables.numeroRadicado) {
      this.hasNumberRadicado = true;
    }

    if(!this.showForm())
      this.form = null;

    this.dataDefault.subscribe(data =>{  console.log(data.listaDestinatariosInternos);

      this.listaDestinatariosInternos = data.listaDestinatariosInternos;
      this.listaDestinatariosExternos= data.listaDestinatariosExternos;


    })

  }

  ngOnChanges(){

    if(this.taskData){

      this.showFormFilter = this.taskData.nombre+'-datos-contactos-show-form';

      let newControllers:any = ViewFilterHook.applyFilter(this.taskData.nombre+'-dataContact',{});

      Object.keys(newControllers).forEach(key => {

      if(this.form.get(key) === null){

        this.form.addControl(key, new FormControl(newControllers[key][0], newControllers[key].length > 1 ? newControllers[key][1]: null,newControllers[key].length > 2 ? newControllers[key][2] : null));
        }
      });
    }

  }


  updateStatus(currentStatus: StatusDTO) {
    this.listaDestinatariosExternos = [...currentStatus.datosContacto.listaDestinatariosExternos];
    this.listaDestinatariosInternos = [...currentStatus.datosContacto.listaDestinatariosInternos];
    this.responderRemitente = currentStatus.datosContacto.responderRemitente;
    this.form.get('distElectronica').setValue(currentStatus.datosContacto.distElectronica);
    this.form.get('distFisica').setValue(currentStatus.datosContacto.distFisica);
    this.issetListDestinatarioBacken = currentStatus.datosContacto.issetListDestinatarioBackend;
    this.refreshView();
  }

  onChangeResponderRemitente(value) {
    if (value && this.resCont === 0) {
      this.disabledCheck = true;
      this.valueCheck = true;
      if (this.taskData.variables && this.taskData.variables.numeroRadicado) {
        this.resCont++;
        this._produccionDocumentalApi.obtenerContactosDestinatarioExterno({
          nroRadicado: this.taskData.variables.numeroRadicado
        }).subscribe(agente => {
          console.log('Objeto que viene del backen ', agente);
          if (agente) {
            this.defaultDestinatarioTipoComunicacion = agente.codTipoRemite;
            const tempDestinatario = <DestinatarioDTO> {};
            tempDestinatario.interno = false;
            tempDestinatario.tipoDestinatario = this.seachTipoDestinatario(!this.hasDestinatarioPrincipal ?  DESTINATARIO_PRINCIPAL : DESTINATARIO_COPIA);
            tempDestinatario.tipoPersona = this.searchTipoPersona(agente.codTipoPers);
            tempDestinatario.nombre = (agente.nombre) ? agente.nombre : '';
            tempDestinatario.nroDocumentoIdentidad = agente.nroDocuIdentidad;
            tempDestinatario.tipoDocumento = this.searchTipoDocumento(agente.codTipDocIdent);
            tempDestinatario.nit = (agente.nit) ? agente.nit : '';
            tempDestinatario.actuaCalidad = (agente.codEnCalidad) ? agente.codEnCalidad : null;
            tempDestinatario.actuaCalidadNombre = (agente.codEnCalidad) ? agente.codEnCalidad : '';
            tempDestinatario.sede = this.searchSede(agente.codSede);
            tempDestinatario.dependencia = this.searchDependencia(agente.codDependencia) ? agente.codDependencia : null;
            tempDestinatario.funcionario = null;
            tempDestinatario.email = '';
            tempDestinatario.mobile = '';
            tempDestinatario.phone = '';
            tempDestinatario.pais = null;
            tempDestinatario.departamento = null;
            tempDestinatario.municipio = null;
            tempDestinatario.isBacken = true;
            const contactos = [];




            this.transformToDestinatarioContacts(agente.datosContactoList || [])
              .subscribe(contact => {  console.log("Enter here");
                contactos.push(contact);
              }, null, () => {
                tempDestinatario.datosContactoList = contactos;
                if (agente.codTipoRemite === TIPO_REMITENTE_EXTERNO) {
                  this.iDestinado = tempDestinatario.nroDocumentoIdentidad;
                  tempDestinatario.interno = false;
                  this.destinatarioExterno = tempDestinatario;

                  this.listaDestinatariosExternos = [... this.listaDestinatariosExternos,tempDestinatario];

                  this._changeDetectorRef.detectChanges();
                 /* this.datosRemitentesExterno.initFormByDestinatario(this.destinatarioExterno);
                  this.indexSelectExterno = -1;
                  this.destinatarioExternoDialogVisible = true;
                  console.log(this.destinatarioExternoDialogVisible);*/
                }
                else if (agente.codTipoRemite === TIPO_REMITENTE_INTERNO) {
                  this.iDestinado = tempDestinatario.nit;
                  tempDestinatario.interno = true;
                  this.destinatarioInterno = tempDestinatario;
                  this.datosRemitentesInterno.initFormByDestinatario(this.destinatarioInterno);
                  this.indexSelectInterno = -1;
                  this.destinatarioInternoDialogVisible = true;
                }
              });


          }
        });
      }
      this.form.get('responderRemitente').setValue(true);
    } else {
     this.disabledCheck = false;
    }

  }



  transformToDestinatarioContacts(contacts): Observable<any[]> {

    //console.log(contacts);

    return  Observable.create(obs => {
      contacts.forEach(contact => obs.next(contact));
      obs.complete();
    })
      .flatMap(contact => {

        let obs = Observable.combineLatest(
          Observable.of(contact),
          this.searchPais(contact.codPais).take(1),
          this.searchDepartamento(contact.codDepartamento).take(1),
          this.searchMunicipio(contact.codMunicipio).take(1),
          (contact, pais, dpto, mncpio) =>{

            console.log("contact",contact);

            return {
              pais: pais,
              departamento: dpto,
              municipio: mncpio,
              numeroTel: isNullOrUndefined(contact.telFijo) ? '' : contact.telFijo,
              celular: isNullOrUndefined(contact.celular) ? '' : contact.celular,
              correoEle: isNullOrUndefined(contact.corrElectronico) ? '' : contact.corrElectronico,
              direccion: isNullOrUndefined(contact.direccion) ? '' : contact.direccion,
              principal:contact.principal
            };
          }
        );

        return obs;
      });

  }

  seachTipoDestinatario(indOriginal) {
    let result = null;
    if (!isNullOrUndefined(indOriginal)) {
      this._store.select(getTipoDestinatarioArrayData).subscribe(values => {
        result = values.find((element) => element.codigo === indOriginal)
      });
    }
    return isNullOrUndefined(result) ? null : result;
  }

  searchTipoPersona(codTipoPers) {
    let result = null;
    if (!isNullOrUndefined(codTipoPers)) {
      this._store.select(getTipoPersonaArrayData).subscribe(values => {
        result = values.find((element) => element.codigo === codTipoPers)
      });
    }
    return isNullOrUndefined(result) ? null : result;
  }

  searchTipoDocumento(codTipDocIdent) {
    let result = null;
    if (!isNullOrUndefined(codTipDocIdent)) {
      this._store.select(getTipoDocumentoArrayData).subscribe(values => {
        result = values.find((element) => element.codigo === codTipDocIdent)
      });
    }
    return isNullOrUndefined(result) ? null : result;
  }

  searchSede(codSede) {
    let result = null;
    if (!isNullOrUndefined(codSede)) {
      this._store.select(sedeAdministrativaArrayData).subscribe(values => {
        result = values.find((element) => element.codigo === codSede)
      });
    }
    return isNullOrUndefined(result) ? null : result;
  }

  searchDependencia(codDependencia) {
    let result = null;
    if (!isNullOrUndefined(codDependencia)) {
      this._produccionDocumentalApi.getDependencias({}).subscribe(values => {
        result = values.find((element) => element.codigo === codDependencia)
      });
    }
    return isNullOrUndefined(result) ? null : result;
  }

  searchPais(codPais) {
    if (isNullOrUndefined(codPais)) return Observable.of(null);
    this._departamentoSandbox.loadDispatch({codPais: codPais});
    return this._store.select(paisArrayData)
      .map(values => values.find((element) => element.codigo === codPais));
  }

  searchDepartamento(codDepto) {
    if (isNullOrUndefined(codDepto)) return Observable.of(null);
    this._municipioSandbox.loadDispatch({codDepar: codDepto});
    return this._store.select(departamentoArrayData).map(values =>
      values.find((element) => element.codigo === codDepto)
    );
  }

  searchMunicipio(codMunicipio) {
    if (isNullOrUndefined(codMunicipio)) return Observable.of(null)
    return this._store.select(municipioArrayData).map(values =>
      values.find((element) => element.codigo === codMunicipio));
  }

  initForm() {
      this.form = this.formBuilder.group({
        // Datos destinatario
        'responderRemitente': [{value: false, disabled: this.issetListDestinatarioBacken}],
        'distFisica': [null],
        'distElectronica': [true],

      });

  }

  showAddDestinatarioExternoPopup() {
    this.datosRemitentesExterno.reset();
    this.indexSelectExterno = -1;
    this.destinatarioExternoDialogVisible = true;
    this.operationActive = 'CREATE';
  }

  showAddDestinatarioInternoPopup() {
    this.datosRemitentesInterno.reset();
    this.indexSelectInterno = -1;
    this.destinatarioInternoDialogVisible = true;
    this.operationActive = 'CREATE';
  }

  hideAddDestinatarioExternoPopup() {
    this.destinatarioExternoDialogVisible = false;

  }

  hideAddDestinatarioInternoPopup() {
    this.destinatarioInternoDialogVisible = false;
  }



  addDestinatario(newDestinatario,tipo:string,force = false) {

    if(newDestinatario.tipoDestinatario.codigo == DESTINATARIO_PRINCIPAL  && this.hasDestinatarioPrincipal){

      if(!force ){
        this._confirmDialog.confirm({
          message: `¿Está seguro que desea substituir el destinatario principal?`,
          header: 'Confirmacion',
          icon: 'fa fa-question-circle',
          accept: () => this.addDestinatario(newDestinatario,tipo,true),
          reject:() => {
                this.subscriptions.push(
                  this._store.select(getTipoDestinatarioArrayData)
                  .map(tipoDestinatario => tipoDestinatario.find( td => td.codigo == DESTINATARIO_COPIA))
                  .subscribe(td => {
                    newDestinatario.tipoDestinatario =td;
                    this.pushDestinatario(newDestinatario,tipo);
                  })
                )

          }
        });

        return;
      }
      this.subscriptions.push(
        this._store.select(getTipoDestinatarioArrayData)
          .map(tipoDestinatario => tipoDestinatario.find( td => td.codigo == DESTINATARIO_COPIA))
          .subscribe( destinatarioCopia => {
            this.listaDestinatariosInternos.forEach(destinatario => {

              destinatario.tipoDestinatario = destinatarioCopia;
            });

            this.listaDestinatariosExternos.forEach(destinatario => {

              destinatario.tipoDestinatario = destinatarioCopia;
            });

            this.pushDestinatario(newDestinatario, tipo)
          })
      );


        return ;
    }

    this.pushDestinatario(newDestinatario, tipo);
  }

  private  pushDestinatario(newDestinatario,tipo:string){
  switch (tipo){
  case 'interno': this.listaDestinatariosInternos = [... this.listaDestinatariosInternos,newDestinatario];
    break;
  case 'externo' : this.listaDestinatariosExternos = [... this.listaDestinatariosExternos,newDestinatario];
    break;
  }

  this.hidePopues();

}

  private get  hasDestinatarioPrincipal():boolean{

  return  this.listaDestinatariosInternos.some(destinatario => destinatario.tipoDestinatario.codigo == DESTINATARIO_PRINCIPAL)
           || this.listaDestinatariosExternos.some(destinatario => destinatario.tipoDestinatario.codigo == DESTINATARIO_PRINCIPAL);

  }

 private get destinatarioPrincipalIndex(){

    const idxE = this.listaDestinatariosExternos.findIndex(destinatario => destinatario.tipoDestinatario.codigo == DESTINATARIO_PRINCIPAL);
    const idxI = this.listaDestinatariosInternos.findIndex(destinatario => destinatario.tipoDestinatario.codigo == DESTINATARIO_PRINCIPAL);

   if(idxE > -1){
     return {td: 'externo', index: idxE};
   }
   else if(idxI > -1){

     return {td:'interno', index: idxI};
   }

   return null;
  }

  private hidePopues(){
    this.hideAddDestinatarioExternoPopup();
    this.hideAddDestinatarioInternoPopup();

    this._changeDetectorRef.detectChanges();
  }

  updateDestinatario(newDestinatario,index:number,tipo:string, force = false){

    const destinatarioPrincipalIndex = this.destinatarioPrincipalIndex;

   const conditions: boolean[] = [
      !isNullOrUndefined(destinatarioPrincipalIndex) && !(destinatarioPrincipalIndex.index== index && tipo == destinatarioPrincipalIndex.td),
      newDestinatario.tipoDestinatario.codigo == DESTINATARIO_PRINCIPAL
    ];

    if(!force &&  conditions.every(c => c)){

        this._confirmDialog.confirm({
          message: `¿Está seguro que desea substituir el destinatario principal?`,
          header: 'Confirmacion',
          icon: 'fa fa-question-circle',
          accept:() => {
            this.subscriptions.push(
              this._store.select(getTipoDestinatarioArrayData)
                .map(tipoDestinatario => tipoDestinatario.find( td => td.codigo == DESTINATARIO_COPIA))
                .subscribe( destinatarioCopia => {
                  this.listaDestinatariosInternos.forEach(destinatario => {

                    destinatario.tipoDestinatario = destinatarioCopia;
                  });

                  this.listaDestinatariosExternos.forEach(destinatario => {

                    destinatario.tipoDestinatario = destinatarioCopia;
                  });

                  switch (tipo){
                    case 'externo':  this.listaDestinatariosExternos[index] = newDestinatario;
                      break;
                    case 'interno' : this.listaDestinatariosInternos[index] = newDestinatario;
                      break;
                  }

                  this.listaDestinatariosInternos = [... this.listaDestinatariosInternos];
                  this.listaDestinatariosExternos = [... this.listaDestinatariosExternos];

                  this.hidePopues();
                })
            );
          },
          reject : () => {

            if(tipo == 'interno')
             newDestinatario.tipoDestinatario = this.listaDestinatariosInternos[index].tipoDestinatario;
            else
              newDestinatario.tipoDestinatario = this.listaDestinatariosExternos[index].tipoDestinatario;

            this.updateDestinatario(newDestinatario,index,tipo,true);
          }

        });

        return;
     }

    switch (tipo){
        case 'externo':  this.listaDestinatariosExternos[index] = newDestinatario;
                         this.listaDestinatariosExternos = [... this.listaDestinatariosExternos];
          break;
        case 'interno' : this.listaDestinatariosInternos[index] = newDestinatario;
                         this.listaDestinatariosInternos = [... this.listaDestinatariosInternos];
          break;
    }

    this.hidePopues();

  }

  editDestinatario(index, op) {

    this.operationActive = 'UPDATE';

    if (index > -1 && op === TIPO_REMITENTE_EXTERNO) {
      this.indexSelectExterno = index;
      this.datosRemitentesExterno.initFormByDestinatario(this.listaDestinatariosExternos[index]);
      this.destinatarioExternoDialogVisible = true;

    } else if (index > -1 && op === TIPO_REMITENTE_INTERNO) {
      this.indexSelectInterno = index;
      this.datosRemitentesInterno.initFormByDestinatario(this.listaDestinatariosInternos[index]);
      this.destinatarioInternoDialogVisible = true;

    }

  }

  deleteDestinatario(index, op, destinado?) {
    if (isNullOrUndefined(destinado)) {
      destinado = [];
    }
    if (this.iDestinado === destinado.nit || this.iDestinado === destinado.nroDocumentoIdentidad) {
      this.resCont = 0;
      this.disabledCheck = false;
      this.valueCheck = false;
    }
    if (index > -1 && op === TIPO_REMITENTE_EXTERNO) {
      this.issetListDestinatarioBacken = (this.listaDestinatariosExternos[index].isBacken) ? false : true;
      const radref = [...this.listaDestinatariosExternos];
      radref.splice(index, 1);
      this.listaDestinatariosExternos = radref;

    } else if (index > -1 && op === TIPO_REMITENTE_INTERNO) {
      this.issetListDestinatarioBacken = (this.listaDestinatariosInternos[index].isBacken) ? false : true;
      const radref = [...this.listaDestinatariosInternos];
      radref.splice(index, 1);
      this.listaDestinatariosInternos = radref;
    }
  }

  ngOnDestroy() {

    this.subscriptions.forEach(s => s.unsubscribe());
  }

  refreshView() {
    this._changeDetectorRef.detectChanges();
  }

  showForm(): boolean{

    return ViewFilterHook.applyFilter(this.showFormFilter,true);
  }

  destinatarioNombre(destinatario:DestinatarioDTO){

    if (destinatario.tipoPersona.codigo == PERSONA_NATURAL)
       return  destinatario.nombre
  return destinatario.tipoPersona.codigo == PERSONA_JURIDICA ? destinatario.razonSocial : '';

  }


}
