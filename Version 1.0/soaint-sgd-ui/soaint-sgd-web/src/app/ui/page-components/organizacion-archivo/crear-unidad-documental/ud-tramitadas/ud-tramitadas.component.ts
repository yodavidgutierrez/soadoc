import {Component, Input, OnInit} from '@angular/core';
import {Observable} from "rxjs/Observable";
import {UnidadDocumentalApiService} from "../../../../../infrastructure/api/unidad-documental.api";
import {SerieDTO} from "../../../../../domain/serieDTO";
import {SubserieDTO} from "../../../../../domain/subserieDTO";
import {Store} from "@ngrx/store";
import {State as RootState} from "../../../../../infrastructure/redux-store/redux-reducers";
import {Subscription} from "rxjs/Subscription";
import {getSelectedDependencyGroupFuncionario} from "../../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {isNullOrUndefined} from "util";
import {SerieService} from "../../../../../infrastructure/api/serie.service";

@Component({
  selector: 'app-ud-tramitadas',
  templateUrl: './ud-tramitadas.component.html',
})
export class UdTramitadasComponent implements OnInit {

  @Input() solicitudesProcesadas$:Observable<any[]>;

  @Input() seriesObservable$:Observable<SerieDTO[]>;

  @Input() allSubSeriesObservable$:Observable<SubserieDTO[]>;

  solicitudSeleccionada:any;

  visibleDetails = false;

  subscriptions:Subscription[] = [];

  constructor() { }

  ngOnInit() {

     }

  verDetalle(solicitud){

    this.solicitudSeleccionada = solicitud;
    this.visibleDetails = true;
  }


  closeDetails(){
    this.solicitudSeleccionada = null;
    this.visibleDetails = false;
  }

}
