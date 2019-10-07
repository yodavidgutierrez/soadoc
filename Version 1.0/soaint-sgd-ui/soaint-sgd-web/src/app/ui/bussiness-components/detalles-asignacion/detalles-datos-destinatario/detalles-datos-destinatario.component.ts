import {
  AfterContentInit,
  AfterViewChecked,
  AfterViewInit,
  Component,
  Input,
  OnChanges,
  OnInit,
  ViewEncapsulation
} from '@angular/core';
import {AgentDTO} from '../../../../domain/agentDTO';
import {ConstanteDTO} from '../../../../domain/constanteDTO';
import {Store} from "@ngrx/store";
import  {State as RootState} from "../../../../infrastructure/redux-store/redux-reducers";
import {combineLatest} from "rxjs/observable/combineLatest";
import {sedeDestinatarioEntradaSelector} from "../../../../infrastructure/state-management/radicarComunicaciones-state/radicarComunicaciones-selectors";
import {InstrumentoApi} from "../../../../infrastructure/api/instrumento.api";
import {isNullOrUndefined} from "util";
import {Observable} from "rxjs/Observable";
import set = Reflect.set;

@Component({
  selector: 'app-detalles-datos-destinatario',
  templateUrl: './detalles-datos-destinatario.component.html',
  styleUrls: ['./detalles-datos-destinatario.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DetallesDatosDestinatarioComponent implements OnChanges {

  @Input()
  constantesList: ConstanteDTO[] = [];

  @Input()
  destinatarios: Observable<AgentDTO[]>  = Observable.empty();

  destinatarioList: any[] = [];

  constructor(private _store:Store<RootState>,private _instrumentoApi:InstrumentoApi) {


  }

  ngOnChanges(){



    if(isNullOrUndefined(this.destinatarios))
      return;

    this.destinatarios.subscribe( destinatarios => {

      combineLatest(this._store.select(sedeDestinatarioEntradaSelector),this._instrumentoApi.ListarDependencia())
        .subscribe(([sedes,dependencias]) => {

          if(isNullOrUndefined(destinatarios))
            return;

          this.destinatarioList = destinatarios.map( dest => {

            const objTD  = !isNullOrUndefined(this.constantesList) ? this.constantesList.find( constante => constante.codigo == dest.indOriginal): null;
            const objDep  = dependencias.find( dep => dep.codigo == dest.codDependencia);
            const objSede  =  isNullOrUndefined(objDep) ? null : sedes.find( dep => dep.codigo == dest.codSede)


         return {
              tipoDestinatario: isNullOrUndefined(objTD) ? '' : objTD.nombre,
              dependencia :  isNullOrUndefined(objDep) ? '' : objDep.nombre,
              sede :  isNullOrUndefined(objSede) ? '' : objSede.nombre,
            };


          })

        })
    })


  }





}
