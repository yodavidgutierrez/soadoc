import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {UnidadDocumentalDTO} from "../../../domain/unidadDocumentalDTO";
import {isNullOrUndefined} from "util";

@Component({
  selector: 'app-popup-ubicar-transferencia',
  templateUrl: './popup-ubicar-transferencia.component.html',
  styleUrls: ['./popup-ubicar-transferencia.component.css']
})
export class PopupUbicarTransferenciaComponent implements OnInit {

  form:FormGroup;

  @Input() unidadDocumental:UnidadDocumentalDTO;

  constructor(private fb:FormBuilder) {

    this.initForm();
  }

  private initForm(){

    this.form = this.fb.group({
         'serie':[null],
         'identificador':[null],
         'unidad':[null],
         'bodega':[null],
         'piso':[null],
         'estante':[null],
         'seccion':[null],
         'caja':[null],
         'carpeta':[null],

    })

  }

  ngOnInit() {

    const serieSubSerie = isNullOrUndefined(this.unidadDocumental) ? '' : `${this.unidadDocumental.nombreSerie}/${this.unidadDocumental.nombreSubSerie}`
    const identificador = isNullOrUndefined(this.unidadDocumental) ? '' : this.unidadDocumental.id;

    this.form.get('serie').setValue(serieSubSerie);
    this.form.get('identificador').setValue(identificador);

  }

}
