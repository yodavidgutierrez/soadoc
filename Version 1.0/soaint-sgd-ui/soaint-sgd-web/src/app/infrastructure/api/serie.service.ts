import { Injectable } from '@angular/core';
import {SerieSubserieApiService} from "./serie-subserie.api";
import {Observable} from "rxjs/Observable";
import {SerieDTO} from "../../domain/serieDTO";

@Injectable()
export class SerieService {

  constructor(private serieSubserieService :SerieSubserieApiService) {  }

  getSeriePorDependencia(codDependencia){

    return this
      .serieSubserieService
      .ListarSerieSubserie({idOrgOfc:codDependencia})
      .map(response =>  response.listaSerie);
  }

  getSubSeriePorDependencia(codDependencia){

    return this
      .serieSubserieService
      .ListarSerieSubserie({idOrgOfc:codDependencia})
      .map(response =>  response.listaSubSerie);
  }

  getSubseriePorDependenciaSerie(codDependencia,codSerie,nomSerie){

    return this
      .serieSubserieService
      .ListarSerieSubserie({idOrgOfc:codDependencia,codSerie:codSerie,nomSerie:nomSerie})
      .map(response => response.listaSubSerie);
  }



}

