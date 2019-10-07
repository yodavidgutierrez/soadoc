import {ChangeDetectorRef, Component, Input, OnDestroy, OnInit} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from '../../../../../domain/constanteDTO';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Store} from '@ngrx/store';
import {State} from 'app/infrastructure/redux-store/redux-reducers';
import {getArrayData as sedeAdministrativaArrayData} from 'app/infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-selectors';
import {getArrayData as DependenciaGrupoSelector} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-selectors';
import {
  getArrayData as getFuncionarioArrayData,
  getAuthenticatedFuncionario, getSelectedDependencyGroupFuncionario
} from 'app/infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {Sandbox as FuncionariosSandbox} from 'app/infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-sandbox';
import {Sandbox as DependenciaGrupoSandbox} from 'app/infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {FuncionarioDTO} from '../../../../../domain/funcionarioDTO';
import {ViewFilterHook} from "../../../../../shared/ViewHooksHelper";
import {combineLatest} from "rxjs/observable/combineLatest";
import {DependenciaApiService} from "../../../../../infrastructure/api/dependencia.api";
import {FuncionariosService} from "../../../../../infrastructure/api/funcionarios.service";
import {isNullOrUndefined} from "util";
import {InstrumentoApi} from "../../../../../infrastructure/api/instrumento.api";
import {Subscription} from "rxjs/Subscription";

@Component({
  selector: 'rs-datos-remitente',
  templateUrl: './datos-remitente.component.html',
  styleUrls: ['./datos-remitente.component.css']
})
export class DatosRemitenteComponent implements OnInit,OnDestroy {

  form: FormGroup;
  validations: any = {};
  visibility: any = {};
  display = false;
  editable: true;
  sedeAdministrativaSuggestions$: Observable<ConstanteDTO[]>;
  dependenciaGrupoSuggestions: ConstanteDTO[] = [];
  funcionariosSuggestions: FuncionarioDTO[]= [];

  subscriptions:Subscription[] = [];


  @Input() loadCurrentUserData = false;


  constructor(private _store: Store<State>,
              private formBuilder: FormBuilder,
              private _funcionarioSandbox: FuncionariosSandbox,
              private _dependenciaGrupoSandbox: DependenciaGrupoSandbox,
              private _intrumentoApi:InstrumentoApi,
              private _funcionarioApi:FuncionariosService,
              private changeDetector:ChangeDetectorRef
              ) {
  }

  ngOnInit() {

    this.sedeAdministrativaSuggestions$ = this._store.select(sedeAdministrativaArrayData).share();
    this.initForm();

    this.subscriptions.push(
      combineLatest(this._store.select(getSelectedDependencyGroupFuncionario),this._store.select(getAuthenticatedFuncionario))
        .subscribe(([dependency,funcionario]) => {
          this.form.get('sedeAdministrativa').setValue(dependency.codSede);


           this._dependenciaGrupoSandbox.loadData({codigo:dependency.codSede})
             .map( dependencias => dependencias.organigrama)
             .subscribe( deps => {
               this.dependenciaGrupoSuggestions = deps;

               const depSelected = this.dependenciaGrupoSuggestions.find( d => d.codigo == dependency.codigo);

               this.form.get("dependenciaGrupo").setValue(depSelected);

               this.changeDetector.detectChanges();
             });

          this._funcionarioApi
            .getFuncionarioBySpecification( funci => funci.dependencias.some( d => d.codigo == dependency.codigo))
            .subscribe( funcionarios => {
              this.funcionariosSuggestions = funcionarios;

              const  funcionarioSelected = this.funcionariosSuggestions.find(func => funcionario.id == func.id);

              this.form.get("funcionarioGrupo").setValue(funcionarioSelected);

              this.changeDetector.detectChanges();
            });


          this.changeDetector.detectChanges();
        })
    );



    this.listenForChanges();
  }

  initForm() {
    this.form = this.formBuilder.group({
      'sedeAdministrativa': [{value: null, disabled: this.editable}, Validators.required],
      'dependenciaGrupo': [{value: null, disabled: this.editable}, Validators.required],
      'funcionarioGrupo': [{value: null, disabled: this.editable}, Validators.required]
    });
  }

  listenForChanges() {
    this.form.get('sedeAdministrativa').valueChanges.subscribe((value) => {
      if (value) {

        this._dependenciaGrupoSandbox.loadData({codigo:value})
          .map( dependencias => dependencias.organigrama)
          .subscribe( deps => {
            this.dependenciaGrupoSuggestions = deps;
            this.changeDetector.detectChanges();
          });



      }
    });

    this.form.get('dependenciaGrupo').valueChanges.subscribe((value) => {

      if (value) {
        this._funcionarioApi
          .getFuncionarioBySpecification( funci => funci.dependencias.some( d => d.codigo == value.codigo))
          .subscribe( funcionarios => {
            this.funcionariosSuggestions = funcionarios;

              this.changeDetector.detectChanges();
          });
        }
    });

    }

  ngOnDestroy(){

    this.subscriptions.forEach( s => s.unsubscribe());
  }
}
