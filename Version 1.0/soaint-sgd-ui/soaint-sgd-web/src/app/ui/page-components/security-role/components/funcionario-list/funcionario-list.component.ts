import {ChangeDetectorRef, Component, OnDestroy, OnInit, Optional} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {FuncionarioDTO} from '../../../../../domain/funcionarioDTO';
import {Store} from '@ngrx/store';
import {State as RootState, State} from 'app/infrastructure/redux-store/redux-reducers';
import {ApiBase} from '../../../../../infrastructure/api/api-base';
import {environment} from '../../../../../../environments/environment';
import {RolDTO} from '../../../../../domain/rolesDTO';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {DependenciaDTO} from "../../../../../domain/dependenciaDTO";
import {InstrumentoApi} from "../../../../../infrastructure/api/instrumento.api";
import {PushNotificationAction} from "../../../../../infrastructure/state-management/notifications-state/notifications-actions";
import {FuncionariosService} from "../../../../../infrastructure/api/funcionarios.service";
import {Sandbox as FuncionarioSandbox} from "../../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-sandbox";
import {Sandbox as ProcesoSandbox} from "../../../../../infrastructure/state-management/procesoDTO-state/procesoDTO-sandbox";
import {getAuthenticatedFuncionario} from "../../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {Subscription} from "rxjs/Subscription";
import {isNullOrUndefined} from "util";

@Component({
  selector: 'funcionario-role-list',
  templateUrl: './funcionario-list.component.html',
  styleUrls: ['./funcionario-list.component.css']
})
export class FuncionarioListComponent implements OnInit,OnDestroy {

  form: FormGroup;


  funcionarios: Observable<FuncionarioDTO[]> = new Observable<FuncionarioDTO[]>();
  funcionarioEdit: FuncionarioDTO;
  funcionarioForEdit:FuncionarioDTO;
  funcionarioSelected: FuncionarioDTO;
  funcionarioEditDialog: Boolean = false;
  funcionarioEditPopup: boolean = false;
  funcionarioBusqueda = { nombre : '', valApellido1: '', valApellido2: '' };
  funcionarioPassword: '';
  subscriptions: Subscription[] = [];
  first = 0;

  selectedFuncionarios: string[];

  roles: Observable<RolDTO[]> = new Observable<RolDTO[]>();
  dependencias$:Observable<DependenciaDTO[]>;
  depenciaPopupVisible = false;

  roleSelected: any;
  dependenciasSelected:DependenciaDTO[];

  constructor(private _store: Store<RootState>,
              private _api: ApiBase,
              private formBuilder: FormBuilder,
              private _instrumentoApi:InstrumentoApi,
              private funcioanrioApi:FuncionariosService,
              private _funcionarioSandbox:FuncionarioSandbox,
              private _procesoSandbox:ProcesoSandbox,
              private _changeDetctor:ChangeDetectorRef
              ) {
    this.initForm();
  }

  ngOnInit() {
    /*const payload = {cod_dependencia: '10401040'};
    const endpoint = `${environment.listarFuncionarios_endpoint}` + '/' + payload.cod_dependencia;
    this.funcionarios = this._api.list(endpoint).map(data => data.funcionarios);*/
    this.searchFuncionarios();
    this.loadAllRoles();

    this.dependencias$ = this._instrumentoApi.ListarDependencia().share();
  }

  initForm() {
    this.form = this.formBuilder.group({
      'nombre': [null,Validators.minLength(3)],
      'primerApellido': [null, Validators.minLength(3)],
      'segundoApellido': [null, Validators.minLength(3)],
    });
  }

  loadAllRoles() {
    const endpoint = `${environment.obtenerFuncionarios_roles_endpoint}`;
    this.roles = this._api.list(endpoint);
  }

  searchFuncionarios() {

    this.first = 0;

   const    specification = (funcionario:FuncionarioDTO) => {

     this.funcionarioBusqueda.nombre = this.form.get('nombre').value;
     this.funcionarioBusqueda.valApellido1 = this.form.get('primerApellido').value;
     this.funcionarioBusqueda.valApellido2 = this.form.get('segundoApellido').value;

     return Object.keys(this.funcionarioBusqueda).every(key => {

       if (!this.funcionarioBusqueda[key])
         return true;

       const data = this.funcionarioBusqueda[key].toString().trim();

       if (!data)
         return true;

       if (!funcionario[key])
         return false;

       const checkData = funcionario[key].toString().trim();

       if (!checkData)
         return false;

       return data.toLowerCase() === checkData.toLowerCase();


     });

   };

   this.funcionarios = this.funcioanrioApi.getFuncionarioBySpecification(specification);
  }

  editFuncionario(func: FuncionarioDTO): void {
    this.funcionarioEdit = func;
    this.funcionarioPassword = '';
    this.selectedFuncionarios = (isNullOrUndefined(func.roles) ) ? [] :
      func.roles.reduce((listRoles,currRol)=>{

        if(!(<any[]>listRoles).some( rol => rol.rol == currRol.rol))
          (<any[]>listRoles).push(currRol);

        return listRoles;
      },[])
        .map( rol => rol.rol).concat();
    this.funcionarioEditDialog = true;
  }

  loadDependenciaPopup(func:FuncionarioDTO){

    this.depenciaPopupVisible = true;
    this.funcionarioSelected = func;

  this.dependencias$
    .map( deps => deps.filter(d => this.funcionarioSelected.dependencias.some( dep =>  dep.codigo == d.codigo)))
    .subscribe( deps => {
      this.dependenciasSelected = deps;
      this._changeDetctor.detectChanges();
      console.log("dependencia seleccionadas",this.dependenciasSelected);
    });
  }
  updateFuncionarioRol(): void {
    this.funcionarioEdit.roles = this.selectedFuncionarios.map(role => {
      return {rol: role};
    }).concat();
    this.funcionarioEdit.password = this.funcionarioPassword;
    delete (<any>this.funcionarioEdit)._$visited;
    this.funcionarioEdit.roles = this.funcionarioEdit.roles.map( rol => {
      delete (<any>rol)._$visited;
      return rol;
    });
    this.funcionarioEdit.dependencias = this.funcionarioEdit.dependencias.map( dep => {
      delete (<any>dep)._$visited;
      return dep;
    });
    this.funcioanrioApi.updateRoles(this.funcionarioEdit).subscribe(state => {
      console.log(state);

   this.subscriptions.push( this._store.select(getAuthenticatedFuncionario).subscribe(funAuthenticated => {

     if(funAuthenticated.id == this.funcionarioEdit.id){
       this._procesoSandbox.loadDispatch({loginName:this.funcionarioEdit.loginName});
       this._funcionarioSandbox.updateFuncinarioDispatch(this.funcionarioEdit);
     }
   }));
    });
    this.hideEditFuncionario();
  }

  hideEditFuncionario() {
    this.funcionarioEditDialog = false;
  }

  bindDepedenciaFuncionario(){

    const payload = {
    idFuncionario: this.funcionarioSelected.id,
    codigos: this.dependenciasSelected.map( depencia => depencia.codigo)
    };

    this._instrumentoApi.AsociarFuncionarioDependencias(payload)
      .subscribe( r => {

          this.funcionarios = this.funcioanrioApi.getFuncionarioBySpecification();

        this.subscriptions.push( this._store.select(getAuthenticatedFuncionario)
          .subscribe(funcionario => {

            if(funcionario.id == this.funcionarioSelected.id){

              this.funcionarioSelected.dependencias = this.dependenciasSelected;

              this._funcionarioSandbox.updateFuncinarioDispatch(this.funcionarioSelected);

             }
               this._store.dispatch(new PushNotificationAction({severity:'success',summary:`Se han actualizado las dependencias`}))

          }));

         },
        _ =>{
          this._store.dispatch(new PushNotificationAction({'severity':'error','summary':'No se ha podido  asociar el funcionario con sus dependencias'}))
        })

  }

  loadFuncionarioEditPopup(funcionario:FuncionarioDTO){

    this.funcionarioForEdit = funcionario;
    this.funcionarioEditPopup = true;
  }

  hideFuncionarioForEdit(){
    this.funcionarioForEdit = null;
  }

  toggleAll(event){

    if(event.checked)
     this.dependencias$.subscribe( deps => this.dependenciasSelected = [... deps]);
    else
      this.dependenciasSelected = [];
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
  }

}
