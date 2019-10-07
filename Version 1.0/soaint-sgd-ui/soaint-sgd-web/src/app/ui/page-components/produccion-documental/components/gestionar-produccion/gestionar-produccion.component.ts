import {ChangeDetectorRef, Component, Input, OnDestroy, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from '../../../../../domain/constanteDTO';
import {ProduccionDocumentalApiService} from 'app/infrastructure/api/produccion-documental.api';
import {RolDTO} from '../../../../../domain/rolesDTO';
import {VALIDATION_MESSAGES} from '../../../../../shared/validation-messages';
import {Subscription} from 'rxjs/Subscription';
import {Sandbox as DependenciaGrupoSandbox} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {Sandbox as FuncionarioSandbox} from 'app/infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-sandbox';
import {FuncionarioDTO} from '../../../../../domain/funcionarioDTO';
import {StatusDTO} from '../../models/StatusDTO';
import {ProyectorDTO} from '../../../../../domain/ProyectorDTO';
import {Store} from '@ngrx/store';
import {State as RootState} from '../../../../../infrastructure/redux-store/redux-reducers';
import {DependenciaDTO} from '../../../../../domain/dependenciaDTO';
import {ObservacionDTO} from '../../models/ObservacionDTO';
import {getAuthenticatedFuncionario} from '../../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {FuncionariosService} from "../../../../../infrastructure/api/funcionarios.service";
import {isNullOrUndefined} from "util";
import {APROBADOR, PROYECTOR} from "../../../../../app.roles";

@Component({
  selector: 'pd-gestionar-produccion',
  templateUrl: './gestionar-produccion.component.html'
})

export class PDGestionarProduccionComponent implements OnInit, OnDestroy {

  form: FormGroup;
  validations: any = {};
  funcionarioLog: FuncionarioDTO;

  dependenciaSelected: ConstanteDTO;

  listaProyectores: ProyectorDTO[] = [];
  listaObservaciones: ObservacionDTO[] = [];
  observacionText: string;
  startIndex = 0;
  cantObservaciones = 0;
  @Input() status = 1;

  sedesAdministrativas$: Observable<ConstanteDTO[]>;
  dependencias: Array<any> = [];
  roles$: Observable<RolDTO[]>;
  funcionarios$: Observable<FuncionarioDTO[]>;

  subscribers: Array<Subscription> = [];

  fecha: Date;

  constructor(private _store: Store<RootState>,
              private formBuilder: FormBuilder,
              private _produccionDocumentalApi: ProduccionDocumentalApiService,
              private _changeDetectorRef: ChangeDetectorRef,
              private _dependenciaGrupoSandbox: DependenciaGrupoSandbox,
              private _funcionarioSandBox: FuncionarioSandbox,
              private funcionarioService: FuncionariosService
              ) {
    this.initForm();
  }

  initForm() {
    this.form = this.formBuilder.group({
      'sede': [null, Validators.required],
      'dependencia': [null, Validators.required],
      'rol': [null, Validators.required],
      'funcionario': [null, Validators.required]
    });
  }

    ngOnInit(): void {
        this._store.select(getAuthenticatedFuncionario).subscribe((funcionario) => {
            this.funcionarioLog = funcionario;
        });

        this.sedesAdministrativas$ = this._produccionDocumentalApi.getSedes({});
        this.roles$ = this._produccionDocumentalApi.getRoles({}).map( roles => {
          let filterRoles = [];
          switch (this.status) {
            case 2 : { filterRoles = roles.filter(role => role.rol === 'revisor' || role.rol === 'aprobador').concat(); break}
            case 3 : {  filterRoles = roles.filter(role => role.rol === 'aprobador').concat(); break}
            default : {  filterRoles = roles.filter(role => role.rol !== 'administrador').concat(); break}
          }
         return filterRoles;
        });
        this.listenForErrors();
        this.listenForChanges();
        this.fecha = new Date();
    }



    initProyeccionLista(lista: any[], rol: string) {
        const listaPreProyectores = this.getListaPreProyectoresFromIncomminString(lista);
        if (isNullOrUndefined(listaPreProyectores)) { return false; }
        const loginnames = this.getLoginNamesForFuncionarios(lista);
        if (isNullOrUndefined(loginnames )) { return false; }
        const listaProyeccion = this.listaProyectores;
        console.log(`Looking for Funcionarios from loginnames: ${loginnames} con rol: ${rol}`);
        console.log(listaProyeccion);

        let dependencia: DependenciaDTO = null;
        let pair: {login: string, codigo: string} = null;

        this._produccionDocumentalApi.getFuncionariosByLoginnames(loginnames).subscribe((functionarios: FuncionarioDTO[]) => {
            functionarios.forEach((fun: FuncionarioDTO) => {
                pair = listaPreProyectores.find( el => el.login === fun.loginName);
                dependencia = fun.dependencias.find((dep: DependenciaDTO) => dep.codigo === pair.codigo);
                listaProyeccion.push({
                    funcionario: fun,
                    dependencia: dependencia,
                    sede: {codigo: dependencia.codSede, codPadre: dependencia.codigo, id: dependencia.ideSede, nombre: dependencia.nomSede},
                    rol: this._produccionDocumentalApi.getRoleByRolename(rol),
                    blocked:true
                });
                console.log(`Agregado: ${fun.nombre} como ${rol}`);
                this.listaProyectores = [...listaProyeccion];
                console.log(this.listaProyectores);
                this.startIndex = this.listaProyectores.length;
                this.refreshView();
            });
        });
    }

  updateStatus(currentStatus: StatusDTO) {

    let listaProyectores = currentStatus.gestionarProduccion.listaProyectores;

    if(this.status == 1){
      listaProyectores = listaProyectores.map( (proyector,index) => {
        if (index > 0)
         delete proyector.blocked;
        return proyector;
      })
    }
    this.listaProyectores = [... listaProyectores];
    this.startIndex = currentStatus.gestionarProduccion.startIndex;
    this.listaObservaciones = currentStatus.gestionarProduccion.listaObservaciones && [...currentStatus.gestionarProduccion.listaObservaciones] || [];
    this.cantObservaciones = currentStatus.gestionarProduccion.cantObservaciones;
  }

  dependenciaChange(event) {

      const rolSelected = this.form.get('rol').value;
      this.dependenciaSelected = event.value;
      this.funcionarios$ = this.funcionarioService
        .getFuncionarioBySpecification(funcionario => this.satisfyBy(rolSelected, event.value,funcionario));
  }

   satisfyBy(role:RolDTO,dependencia: DependenciaDTO, funcionario: FuncionarioDTO){

    if(isNullOrUndefined(role) || isNullOrUndefined(dependencia) || isNullOrUndefined(funcionario))
       return false;

     return funcionario.dependencias.some( d =>  d.codigo == dependencia.codigo)
       && funcionario.roles.some(rol => !isNullOrUndefined(rol) &&   rol.rol == role.nombre)
       && this.listaProyectores.every( proyector => proyector.funcionario.id != funcionario.id)
       &&  (role.nombre != APROBADOR || !!funcionario.firmaPolitica)

   }

  eliminarProyector(index) {

      const proyectores = this.listaProyectores;
      proyectores.splice(index, 1);
      this.listaProyectores = [...proyectores];
      const rolSelected = this.form.get('rol').value;
      const dependencia = this.form.get('dependencia').value;

      this.funcionarios$ = this.funcionarioService
        .getFuncionarioBySpecification(funcionario => this.satisfyBy(rolSelected,dependencia,funcionario));
  }

  agregarProyector() {
    if (!this.form.valid) {
      return false;
    }

    const proyectores = this.listaProyectores;
    const proyector = {
      sede: this.form.get('sede').value,
      dependencia: this.form.get('dependencia').value,
      funcionario: this.form.get('funcionario').value,
      rol: this.form.get('rol').value
    };

    if (this.checkProyector(proyector)) {
      console.log('Ya existe el documento');
      return false;
    }

    proyectores.push(proyector);
    this.listaProyectores = [...proyectores];
    this.form.reset();
    this.funcionarios$ = Observable.of([]);
    this.refreshView();

    return true;
  }

  checkProyector(newProyector: ProyectorDTO) {
    let exists = false;
    this.listaProyectores.forEach((current: ProyectorDTO) => {
      if (current.sede.id === newProyector.sede.id &&
        current.dependencia.id === newProyector.dependencia.id &&
        current.funcionario.id === newProyector.funcionario.id &&
        current.rol.id === newProyector.rol.id) {
        exists = true;
      }
    });

    return exists;
  }

    removeFromList(i: any, listname: string) {
        const list = [...this[listname]];
        list.splice(i, 1);
        this[listname] = list;
    }

  listenForChanges() {
      this.subscribers.push(this.form.get('sede').valueChanges.distinctUntilChanged().subscribe((sede) => {
          this.form.get('dependencia').reset();
          this.form.get('funcionario').reset();
          if (sede) {
              const depedenciaSubscription: Subscription = this._dependenciaGrupoSandbox.loadData({codigo: sede.codigo}).subscribe(dependencias => {
                  this.dependencias = dependencias.organigrama;
                  depedenciaSubscription.unsubscribe();
              });
          }
      }));
  }

  listenForErrors() {
    this.bindToValidationErrorsOf('sede');
    this.bindToValidationErrorsOf('dependencia');
    this.bindToValidationErrorsOf('rol');
    this.bindToValidationErrorsOf('funcionario');
  }

  listenForBlurEvents(control: string) {
    const ac = this.form.get(control);
    if (ac.touched && ac.invalid) {
      const error_keys = Object.keys(ac.errors);
      const last_error_key = error_keys[error_keys.length - 1];
      this.validations[control] = VALIDATION_MESSAGES[last_error_key];
    }
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

  ngOnDestroy() {
    this.subscribers.forEach((sub: Subscription) => sub.unsubscribe());
  }

  refreshView() {
    this._changeDetectorRef.detectChanges();
  }


    getListaProyectores(): ProyectorDTO[] {
      return this.listaProyectores;
    }

    agregarObservacion() {
        const lista = this.listaObservaciones;
        const newObservacion = {
            observaciones: this.observacionText,
            funcionario: this.funcionarioLog,
            rol: this._produccionDocumentalApi.getRoleByRolename(this.getRolenameByStatus()),
            fecha: new Date()
        };
        lista.push(newObservacion);
        this.observacionText = null;
        this.listaObservaciones = [...lista];
    }

    getRolenameByStatus() {
      switch (this.status) {
          case 1 : { return 'proyector'; }
          case 2 : { return 'revisor'; }
          case 3 : { return 'aprobador'; }
          default : { return null; }
      }
    }


    protected getListaPreProyectoresFromIncomminString(lista: any[]) {
        const listaPreProyectores: {login: string, codigo: string}[] = [];
        if (lista.length === 0 ){return}
        const matchs = lista[0].split(':');
        if (matchs && matchs.length > 0) {
                listaPreProyectores.push({login: matchs[0], codigo: matchs[1]});
        }

        return listaPreProyectores;
    }

    protected getLoginNamesForFuncionarios(lista: any[]) {
        return lista[0].split(':')[0];
    }

 rolChange(event){

   const dep = this.form.get('dependencia').value;


    const  funcionriosRole= this.funcionarioService
      .getFuncionarioBySpecification(funcionario => this.satisfyBy(event.value,dep,funcionario));

    const authenticated = this._store.select(getAuthenticatedFuncionario);

      this.funcionarios$ = Observable.combineLatest(funcionriosRole,authenticated)
                            .map(([funcList,func])=> {return <FuncionarioDTO[]>funcList.filter( f => f.id != func.id)})

    }

    enableDelete(rol){

     return  rol.rol != 'proyector' && this.funcionarioLog.roles.some(role => role.rol == PROYECTOR);
    }
}

