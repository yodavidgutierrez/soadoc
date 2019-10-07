import {Component, OnInit, ChangeDetectionStrategy, OnDestroy, HostListener} from '@angular/core';
import {SessionService, WebModel} from 'app/infrastructure/utils/session.service';

import {LoginSandbox} from './redux-state/login-sandbox';
import {Observable} from 'rxjs/Observable';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import * as actions from './redux-state/login-actions';
import {environment} from "../../../../environments/environment";
import {Store} from "@ngrx/store";
import {State as RootState} from "../../../infrastructure/redux-store/redux-reducers";
import {getAuthenticatedFuncionario} from "../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {isNullOrUndefined} from "util";
import {Subscription} from "rxjs/Subscription";
import { JsonPipe } from '@angular/common';
import {LoginAction} from './redux-state/login-actions';


@Component({
  selector: 'app-login',
  // providers: [LoginSandbox],
  templateUrl: './login.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class LoginComponent implements OnInit,OnDestroy {

  public loading$: Observable<boolean>;
  public error$: Observable<string>;
  public form: FormGroup;

  subscriptions: Subscription[] = [];

  public readonly baseUrl = environment.base_url;

  constructor(private _sandbox: LoginSandbox,
              private _formBuilder: FormBuilder,
              private _store:Store<RootState>
  ) {
    this.initForm();

   /* var eventMethod = window.addEventListener ? "addEventListener" : "attachEvent";
    var eventer = window[eventMethod];
    var messageEvent = eventMethod == "attachEvent" ? "onmessage" : "message";
    eventer(messageEvent, (e) => {
      this.loading$ = this._sandbox.selectorLoading();
        if (String(e.data).includes('soaint2')) {
      setTimeout(() => {

        let data = String(e.data).replace('soaint2','');
        var bt = JSON.parse(atob(data));
        console.log(bt)
        this.form.controls['username'].setValue(bt['username']);
        this.form.controls['password'].setValue(bt['password']);
        this.submit();
      }, 100);
        }
    }, false);*/

  }

  ngOnInit() {

    this.subscriptions.push(
      this._store.select(getAuthenticatedFuncionario).subscribe( funcionario => {

        if(!isNullOrUndefined(funcionario)){
          window.history.forward();
          return
        }


        this.loading$ = this._sandbox.selectorLoading();
        this.error$ = this._sandbox.selectorError();

      })
    );
  }

  @HostListener('window:message', ['$event'])
  login(evt){
    if(evt.origin === (<any>window).toolboxOrigin){
      const data = evt.data? evt.data: (!isNullOrUndefined(evt.originalEvent) ? evt.originalEvent.data : null);
      if(!isNullOrUndefined(data) && !isNullOrUndefined(data.user) && !isNullOrUndefined(data.password))
      this._store.dispatch(new LoginAction({username: data.user, password: data.password}))
    }
  }

  initForm(){
    this.form = this._formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  /**
   * Submit the authentication form.
   * @method submit
   */
  public submit() {
    // get email and password values
    const username: string = this.form.get('username').value;
    const password: string = this.form.get('password').value;


    // trim values
    username.trim();
    password.trim();

    // set payload
    const payload = {
      username: username,
      password: password
    };

    this._sandbox.loginDispatch(payload);
  }

  ngOnDestroy(): void {

    this.subscriptions.forEach(s => s.unsubscribe());
  }


}
