import {ChangeDetectorRef, Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {State} from '../../../infrastructure/redux-store/redux-reducers';
import {Store} from '@ngrx/store';
import {Sandbox as FuncionariosSandbox} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-sandbox';
import {getArrayData as getFuncionarioArrayData} from '../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors';
import {ProduccionDocumentalApiService} from '../../../infrastructure/api/produccion-documental.api';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from '../../../domain/constanteDTO';
import {FuncionarioDTO} from '../../../domain/funcionarioDTO';
import {DESTINATARIO_PRINCIPAL} from '../../../shared/bussiness-properties/radicacion-properties';
import {LoadAction as SedeAdministrativaLoadAction} from '../../../infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-actions';
import {getArrayData as dependenciaGrupoArrayData} from '../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-selectors';
import {getArrayData as sedeAdministrativaArrayData} from '../../../infrastructure/state-management/sedeAdministrativaDTO-state/sedeAdministrativaDTO-selectors';
import {Sandbox as DependenciaGrupoSandbox} from '../../../infrastructure/state-management/dependenciaGrupoDTO-state/dependenciaGrupoDTO-sandbox';
import {VALIDATION_MESSAGES} from '../../../shared/validation-messages';
import {DestinatarioDTO} from '../../../domain/destinatarioDTO';
import {ConfirmationService} from 'primeng/components/common/api';
import {StatusDTO} from "../produccion-documental/models/StatusDTO";

@Component({
    selector: 'datos-destinatario-interno',
    templateUrl: 'datos-destinatario-interno.component.html'
})
export class DatosDestinatarioInternoComponent implements OnInit, OnDestroy {
    form: FormGroup;

    sedesAdministrativas$: Observable<ConstanteDTO[]>;
    dependencias$: Observable<ConstanteDTO[]>;
    tiposDestinatario$: Observable<ConstanteDTO[]>;
    funcionarios$: Observable<FuncionarioDTO[]>;

    // @Output() onChangeSedeAdministrativa: EventEmitter<any> = new EventEmitter();
    @Input() listaDestinatarios: DestinatarioDTO[];

    validations: any = {};

    canInsert = false;


    constructor(private formBuilder: FormBuilder,
                private _store: Store<State>,
                private confirmationService: ConfirmationService,
                private _funcionarioSandbox: FuncionariosSandbox,
                private _dependenciaGrupoSandbox: DependenciaGrupoSandbox,
                private _changeDetectorRef: ChangeDetectorRef,
                private _produccionDocumentalApi: ProduccionDocumentalApiService) {

        this.funcionarios$ = this._store.select(getFuncionarioArrayData);

        this.initForm();
        this.listenForChanges();
        this.listenForErrors();

        Observable.combineLatest(
            this.form.get('tipoDestinatario').valueChanges,
            this.form.get('sede').valueChanges,
            this.form.get('dependencia').valueChanges,
            this.form.get('funcionario').valueChanges
        ).do(() => this.canInsert = false)
        .filter(([tipo, sede, grupo, func]) => tipo && sede && grupo && func)
        .zip(([tipo, sede, grupo, func]) => {
            return {tipo: tipo, sede: sede, grupo: grupo, funcionario: func}
        })
        .subscribe(() => { this.canInsert = true });

    }

    initForm() {
      this.form = this.formBuilder.group({
        'tipoDestinatario': [null, Validators.required],
        'sede': [null, Validators.required],
        'dependencia': [null, Validators.required],
        'funcionario': [null, Validators.required],
      });
    }

    checkDestinatarioInList(newone: DestinatarioDTO, lista: DestinatarioDTO[]) {
        return lista.filter(current => {
            return current.funcionario.id === newone.funcionario.id
                && current.dependencia.id === newone.dependencia.id
                && current.sede.id === newone.sede.id;
        }).length > 0;
    }

    adicionarDestinatario() {
        if (!this.form.valid) {
            return false;
        }

        const destinatarios = this.listaDestinatarios;
        const newone: DestinatarioDTO = this.form.value;
        newone.interno = true;

        if (this.checkDestinatarioInList(newone, destinatarios)) {
            return false;
        }

        if (newone.tipoDestinatario.codigo === DESTINATARIO_PRINCIPAL
            && destinatarios.filter(value => value.tipoDestinatario.codigo === DESTINATARIO_PRINCIPAL).length > 0) {
            this.confirmationService.confirm({
                message: `<p style="text-align: center">¿Está seguro desea substituir el destinatario principal?</p>`,
                accept: () => {
                    const newList = destinatarios.filter(value => value.tipoDestinatario.codigo !== DESTINATARIO_PRINCIPAL);
                    newList.unshift(newone);
                    this.listaDestinatarios = [...newList];
                    this.form.reset();
                }
            });
            return true;
        }

        if (newone.tipoDestinatario.codigo === DESTINATARIO_PRINCIPAL) {
            destinatarios.unshift(newone);
        } else {
            destinatarios.push(newone);
        }

        this.listaDestinatarios = [...destinatarios];
        this.form.reset();

        return true;
    }

    eliminarDestinatario(index: number) {
        if (index > -1) {
            const destinatarios = this.listaDestinatarios;
            destinatarios.splice(index, 1);
            this.listaDestinatarios = [...destinatarios];
        }
    }

    listenForErrors() {
        this.bindToValidationErrorsOf('sede');
        this.bindToValidationErrorsOf('dependencia');
        this.bindToValidationErrorsOf('tipoDestinatario');
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

    listenForChanges() {
        this.form.get('sede').valueChanges.subscribe((value) => {
            if (value) {
                this.form.get('dependencia').reset();
                this._dependenciaGrupoSandbox.loadDispatch({codigo: value.id});
            }
        });

        /*this.form.get('dependencia').valueChanges.subscribe((value) => {
            if (value) {
                this.form.get('funcionario').reset();
                this._funcionarioSandbox.loadAllFuncionariosDispatch({codDependencia: value.codigo});
            }
        });*/
    }

    listenForBlurEvents(control: string) {
        const ac = this.form.get(control);
        if (ac.touched && ac.invalid) {
            const error_keys = Object.keys(ac.errors);
            const last_error_key = error_keys[error_keys.length - 1];
            this.validations[control] = VALIDATION_MESSAGES[last_error_key];
        }
    }

    ngOnDestroy(): void {}

    ngOnInit(): void {
        this.sedesAdministrativas$ = this._store.select(sedeAdministrativaArrayData);
        this.dependencias$ = this._store.select(dependenciaGrupoArrayData);
        this._store.dispatch(new SedeAdministrativaLoadAction());

        this._funcionarioSandbox.loadAllFuncionariosByRolDispatch({rol: 'RECEPTOR'});

        this.tiposDestinatario$ = this._produccionDocumentalApi.getTiposDestinatario({});

        this.tiposDestinatario$.subscribe((results) => {
          if (results.length > 0) {
            this.form.get('tipoDestinatario').setValue(results.find((tipoDestinatario) => {
              return tipoDestinatario.codigo === DESTINATARIO_PRINCIPAL;
            }));
          }
        });
        this.refreshView();
    }

    refreshView() {
      this._changeDetectorRef.detectChanges();
    }
}
