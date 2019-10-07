import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {ComunicacionOficialDTO} from "../../../../domain/comunicacionOficialDTO";
import {ConstanteDTO} from "../../../../domain/constanteDTO";
import {isNullOrUndefined} from "util";
import {
  COMUNICACION_EXTERNA,
  COMUNICACION_INTERNA_ENVIADA
} from "../../../../shared/bussiness-properties/radicacion-properties";

@Component({
  selector: 'app-detalles-datos-generales',
  templateUrl: './detalles-datos-generales.component.html',
  styleUrls: ['./detalles-datos-generales.component.scss'],
  encapsulation: ViewEncapsulation.None

})
export class DetallesDatosGeneralesComponent implements OnInit {

  @Input()
  comunicacion: ComunicacionOficialDTO;

  @Input()
  constantesList: ConstanteDTO[];

  constructor() {
  }

  ngOnInit() {
    console.log('DETALLES COMUNICACIÓN', this.comunicacion);
  }

  get isComunicacionEntrada(){

    return !isNullOrUndefined(this.comunicacion) && !isNullOrUndefined(this.comunicacion.correspondencia) && this.comunicacion.correspondencia.codTipoCmc == COMUNICACION_EXTERNA;
  }

  get isComunicacionSalidaInterna(){

    return !isNullOrUndefined(this.comunicacion) && !isNullOrUndefined(this.comunicacion.correspondencia) && this.comunicacion.correspondencia.codTipoCmc == COMUNICACION_INTERNA_ENVIADA;
  }

}
