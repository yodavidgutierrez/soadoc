import {Component, HostListener, OnInit} from '@angular/core';
import {Store} from '@ngrx/store';
import {State as RootState} from 'app/infrastructure/redux-store/redux-reducers';
import {LoadingService} from './infrastructure/utils/loading.service';
import {Observable} from 'rxjs/Observable';
import {ApiBase} from "./infrastructure/api/api-base";
import {environment} from "../environments/environment";
import {
  LoginAction,
  LoginSuccessAction,
} from './ui/page-components/login/redux-state/login-actions';
import {LoginSandbox} from "./ui/page-components/login/redux-state/login-sandbox";
import {
  LoadSuccessAction as FuncionarioAutenticatedAction,
  SelectDependencyGroupAction
} from "./infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-actions";
import {getSelectedDependencyGroupFuncionario} from "./infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {isNullOrUndefined} from "util";
import {go} from "@ngrx/router-store";
import {StartTaskSuccessAction} from "./infrastructure/state-management/tareasDTO-state/tareasDTO-actions";
import {LoadDatosGeneralesAction} from "./infrastructure/state-management/constanteDTO-state/constanteDTO-actions";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {
  isLoading: boolean;
  loading$: Observable<boolean>;

  constructor(private _store: Store<RootState>, private loading: LoadingService) {
    this.loading$ = this.loading.getLoaderAsObservable();

    this.loading$.subscribe(value => {
      this.isLoading = value;
    });

    this.restore();
    
  }

  ngOnInit() {

     this._store.select(getSelectedDependencyGroupFuncionario)
       .subscribe( dependencia => {

         if(!isNullOrUndefined(dependencia))
          localStorage.setItem("dependencySelected",JSON.stringify(dependencia));
       } )

  }

  private  restore(){

    if(window.performance.navigation.type != 1){

      this.clearStoredData();

      return;
    }

    if(!localStorage.getItem("session") || !localStorage.getItem("lastActivity")){

      this.clearStoredData();

        return;
    }


    const lastActivity = parseInt(localStorage.getItem("lastActivity"));

    const currentTime = new Date().getTime();

    if(currentTime - lastActivity > 300000){

      this.clearStoredData();

      return;
    }
    const sessionData = JSON.parse(localStorage.getItem("session"));

     sessionData.credentials.password = atob(sessionData.credentials.password);

    this._store.dispatch(new LoginSuccessAction({...sessionData,noSaveSession: true}));
    this._store.dispatch(new FuncionarioAutenticatedAction(sessionData.profile));

    if(localStorage.getItem("dependencySelected")){

      const dependencia = JSON.parse(localStorage.getItem("dependencySelected"));
      this._store.dispatch(new SelectDependencyGroupAction(dependencia));
    }

    if(localStorage.getItem('activeTask')){

      const task = JSON.parse(localStorage.getItem('activeTask'));
      this._store.dispatch(new LoadDatosGeneralesAction());
      this._store.dispatch(new StartTaskSuccessAction(task));

    }

  }

  private clearStoredData(){
    localStorage.removeItem("lastActivity");
    localStorage.removeItem("session");
    localStorage.removeItem("dependencySelected");
    localStorage.removeItem("activeTask");
  }


}
