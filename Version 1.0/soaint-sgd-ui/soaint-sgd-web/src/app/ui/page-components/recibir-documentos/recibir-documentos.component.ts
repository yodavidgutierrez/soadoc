import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {InstrumentoApi} from "../../../infrastructure/api/instrumento.api";
import {Observable} from "rxjs/Observable";
import {DependenciaDTO} from "../../../domain/dependenciaDTO";
import {ComunicacionOficialDTO} from "../../../domain/comunicacionOficialDTO";

@Component({
  selector: 'app-recibir-documentos',
  templateUrl: './recibir-documentos.component.html',
  styleUrls: ['./recibir-documentos.component.css']
})
export class RecibirDocumentosComponent implements OnInit {

  form:FormGroup;

  dependencias$: Observable<DependenciaDTO[]>;

  comunicaciones : Observable<ComunicacionOficialDTO[]>

  constructor(private fb:FormBuilder,private _instrumentoApi:InstrumentoApi) {

    this.initForm();
  }

  private initForm(){

     this.form = this.fb.group({
       anno:[null],
       noRadicado:[null],
       consecutivo:[null],
       codDependencia:[null],
       fechaIni:[null],
       fechaFin:[null],

     })

  }

  ngOnInit() {

    this.dependencias$ = this._instrumentoApi.ListarDependencia();
  }

}
