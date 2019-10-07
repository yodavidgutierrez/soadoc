import {Component, Inject, OnInit, Input, forwardRef, Output, EventEmitter} from '@angular/core';
import {AdminLayoutComponent} from '../../container/admin-layout/admin-layout.component';
import {Store} from "@ngrx/store";
import {State as RootState} from "../../../../infrastructure/redux-store/redux-reducers";
import {Observable} from "rxjs/Observable";
import {getAuthenticatedFuncionario} from "../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {isNullOrUndefined} from "util";
import {MenuItem} from "primeng/primeng";
import {AdminLayoutSandbox} from "../../container/admin-layout/redux-state/admin-layout-sandbox";
import {ROUTES_PATH} from "../../../../app.route-names";
import {BPMMANAGER_ROL} from "../../../../app.roles";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Subscription} from "rxjs/Subscription";
import {isOpenTask} from "../../../../infrastructure/state-management/tareasDTO-state/tareasDTO-selectors";
import {TareaDTO} from "../../../../domain/tareaDTO";

@Component({
  selector: 'app-topbar',
  templateUrl: './topbar.component.html'
})
export class TopBarComponent implements OnInit {

  @Input() isAuthenticated: boolean;
  form: FormGroup;
  formSubscription: Subscription;
  @Input() dependencias: Array<any> = [];
  @Input() dependenciaSelected: any;
  @Output() onSelectDependencia: EventEmitter<any> = new EventEmitter();
  activeTaskUnsubscriber: Subscription;

  isOpenTask = false;
  task: TareaDTO;
   routerLinks: MenuItem[];


  constructor(@Inject(forwardRef(() => AdminLayoutComponent)) public app: AdminLayoutComponent
              , private _store:Store<RootState>,private _sanbox:AdminLayoutSandbox, private formBuilder: FormBuilder) {

    this.routerLinks = [
      {label: 'Administración de tareas', icon:'settings',routerLink:['/'+ ROUTES_PATH.taskManager.url]},
      // {label:'Administración de usuarios',icon:'supervisor_account',command: _ => {this._sanbox.dispatchRoutingSecurityRole()}},
      {label: 'Administración de listas', icon:'settings',routerLink:['/'+ ROUTES_PATH.listaManager.url]},
    ];

  }

  ngOnInit() {
    this.form = this.formBuilder.group({
      'dependencia': [null]
    });

    this.formSubscription = this.form.get('dependencia').valueChanges.distinctUntilChanged().subscribe(value => {
      this.onSelectDependencia.emit(value);
    });

    this.activeTaskUnsubscriber = this._store.select(isOpenTask)
      .subscribe(inTask => {
        this.isOpenTask = inTask;
      });
  }

  clickLink(item:MenuItem){

     if(!isNullOrUndefined(item.command))
     item.command.call(this);

     this.app.activeTopbarItem = null;

  }

  showAdministration():Observable<boolean>{

    return this._store.select(getAuthenticatedFuncionario)
               .map( funcionario =>  !isNullOrUndefined(funcionario) && !isNullOrUndefined(funcionario.roles) && funcionario.roles.some( rol => rol.rol == BPMMANAGER_ROL));
  }

}
