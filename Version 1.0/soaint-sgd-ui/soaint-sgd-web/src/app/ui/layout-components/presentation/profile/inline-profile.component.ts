import {
  Component,
  Output,
  OnInit,
  EventEmitter, ChangeDetectionStrategy, Input,
} from '@angular/core';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {Store} from "@ngrx/store";
import {State as RootState} from "../../../../infrastructure/redux-store/redux-reducers";
import {Observable} from "rxjs/Observable";
import {getAuthenticatedFuncionario} from "../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {isNullOrUndefined} from "util";
import {BPMMANAGER_ROL} from "../../../../app.roles";



@Component({
  selector: 'inline-profile',
  templateUrl: './inline-profile.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  animations: [
    trigger('menu', [
      state('hidden', style({
        height: '0px'
      })),
      state('visible', style({
        height: '*'
      })),
      transition('visible => hidden', animate('400ms cubic-bezier(0.86, 0, 0.07, 1)')),
      transition('hidden => visible', animate('400ms cubic-bezier(0.86, 0, 0.07, 1)'))
    ])
  ]
})
export class InlineProfileComponent {

  active: boolean;

  @Input() username: string;

  @Output() onSignOffUser: EventEmitter<any> = new EventEmitter();
  @Output() onSecurityRole: EventEmitter<any> = new EventEmitter();


  constructor(private _store:Store<RootState>) {
  }

  public logout(event): void {
    this.onSignOffUser.emit(null);
    event.preventDefault();
  }

  public _onSecurityRole(event): void {
    this.onSecurityRole.emit();
    event.preventDefault();
  }

  public onClick(event): void {
    this.active = !this.active;
    event.preventDefault();
  }

  isBpmManager():Observable<boolean>{

    return this._store.select(getAuthenticatedFuncionario).map( f => !isNullOrUndefined(f) && !isNullOrUndefined(f.roles) && f.roles.some( r => r.rol == BPMMANAGER_ROL))
  }
}
