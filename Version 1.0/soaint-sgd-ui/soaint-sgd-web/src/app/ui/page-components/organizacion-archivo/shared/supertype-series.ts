import {ChangeDetectorRef, OnInit} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {DependenciaDTO} from "../../../../domain/dependenciaDTO";
import {SerieDTO} from "../../../../domain/serieDTO";
import {State as RootState} from "../../../../infrastructure/redux-store/redux-reducers";
import {Store} from "@ngrx/store";
import {SerieService} from "../../../../infrastructure/api/serie.service";
import {getSelectedDependencyGroupFuncionario} from "../../../../infrastructure/state-management/funcionarioDTO-state/funcionarioDTO-selectors";
import {Subscription} from "rxjs/Subscription";
import {isNullOrUndefined} from "util";

export abstract class SupertypeSeries implements OnInit{

  dependenciaSelected$ : Observable<any>;

  dependenciaSelected : DependenciaDTO;

  subseriesObservable:any[];

  seriesObservable:SerieDTO[];

  globalDependencySubscriptor:Subscription;

  listaSeries:SerieDTO[];




  constructor(protected _store:Store<RootState>,
              protected _serieSubserieService:SerieService,
              protected changeDetector:ChangeDetectorRef){

        this.dependenciaSelected$ = this._store.select(getSelectedDependencyGroupFuncionario);
  }

  ngOnInit(){

    this.globalDependencySubscriptor =  this.dependenciaSelected$.subscribe(result =>{
       this
        ._serieSubserieService
        .getSeriePorDependencia(result.codigo)
        .subscribe(list => {

          list.unshift({
          codigoSerie:null,nombreSerie:"Seleccione"});

          this.seriesObservable = [... list];

          this.changeDetector.detectChanges();
        });
      this.dependenciaSelected = result;
    });
  }


  getSerieNombre(codSerie){

    const serie = this.listaSeries.find(s => s.codigoSerie == codSerie);

    return (isNullOrUndefined(serie)) ? '' : serie.nombreSerie;
  }

  selectSerie(evt)
  {

    if(evt){
      this
        ._serieSubserieService
        .getSubseriePorDependenciaSerie(this.dependenciaSelected.codigo,evt.value,this.getSerieNombre(evt.value))
        .subscribe(list => {
          list.unshift({codigoSubSerie:null,nombreSubSerie:"Seleccione"});
        this.subseriesObservable =  list;
        })
    }


  }
}
