import {Component, Input, ViewEncapsulation} from '@angular/core';
import {ConstanteDTO} from '../../../../domain/constanteDTO';
import {AgentDTO} from '../../../../domain/agentDTO';
import {ContactoDTO} from '../../../../domain/contactoDTO';
import {isNullOrUndefined} from "util";
import {ComunicacionOficialDTO} from "../../../../domain/comunicacionOficialDTO";
import {
  COMUNICACION_EXTERNA,
  COMUNICACION_INTERNA_ENVIADA
} from "../../../../shared/bussiness-properties/radicacion-properties";

@Component({
  selector: 'app-detalles-datos-remitente',
  templateUrl: './detalles-datos-remitente.component.html',
  styleUrls: ['./detalles-datos-remitente.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DetallesDatosRemitenteComponent {

  @Input()
  constantesList: ConstanteDTO[];

  @Input()
  municipiosList: any[] = [];

  @Input()
  remitente: AgentDTO;

  @Input()
  contactos: ContactoDTO[];

  @Input() paises: any[];

  @Input() comunicacion:ComunicacionOficialDTO;


  constructor() {


  }

  getPais(codigoPais): string {
    if(this.paises !== undefined){
      const pais = this.paises.find((p) => p.codigo === codigoPais);
      if (pais) {
        return pais.nombre;
      }
    }
    return '';
  }

  getDepartamento(codigoDepartamento): string {
    return this.municipiosList !== undefined ? this.municipiosList.find((municipio) => municipio.departamento.codigo === codigoDepartamento).departamento.nombre: '';
  }

  getMunicipio(codigoMunicipio): string {

    if(isNullOrUndefined(this.municipiosList))
       return '';
    const municipio =  this.municipiosList.find((mun) => mun.codigo === codigoMunicipio);
    return  !isNullOrUndefined(municipio) ? municipio.nombre: '';
  }

  get isComunicacionEntrada(){

    return !isNullOrUndefined(this.comunicacion) && !isNullOrUndefined(this.comunicacion.correspondencia) && this.comunicacion.correspondencia.codTipoCmc == COMUNICACION_EXTERNA;
  }

  get isComunicacionSalidaInterna(){

    return !isNullOrUndefined(this.comunicacion) && !isNullOrUndefined(this.comunicacion.correspondencia) && this.comunicacion.correspondencia.codTipoCmc == COMUNICACION_INTERNA_ENVIADA;
  }

}
