import {AgentDTO} from '../../domain/agentDTO';
import {RadicacionBase} from './radicacionBase';
import {
  DATOS_CONTACTO_PRINCIPAL,
  DATOS_CONTACTO_SECUNDARIO, DESTINATARIO_COPIA, DESTINATARIO_EXTERNO, DESTINATARIO_INTERNO,
  TIPO_AGENTE_DESTINATARIO, TIPO_REMITENTE_EXTERNO, TIPO_REMITENTE_INTERNO
} from '../bussiness-properties/radicacion-properties';
import {RadicacionSalidaFormInterface} from '../interfaces/data-transformers/radicacionSalidaForm.interface';
import {CorrespondenciaDTO} from "../../domain/correspondenciaDTO";
import {RadicacionEntradaFormInterface} from "../interfaces/data-transformers/radicacionEntradaForm.interface";
import {ContactoDTO} from "../../domain/contactoDTO";
import {isNullOrUndefined} from "util";
import {ComunicacionOficialDTO} from "../../domain/comunicacionOficialDTO";

export class RadicacionSalidaDTV extends  RadicacionBase {

  hasError:boolean | {error:string} = false;

  getCorrespondencia():CorrespondenciaDTO{

    const datosEnvio = (<RadicacionSalidaFormInterface>this.source).datosEnvio;

    let correspondencia = super.getCorrespondencia();

    correspondencia.reqDistFisica = this.source.generales.reqDistFisica == 1 ? '1' : '0';

    if(datosEnvio !== undefined){

      correspondencia.codClaseEnvio = datosEnvio.clase_envio.codigo;
      correspondencia.codModalidadEnvio = datosEnvio.modalidad_correo.codigo;
    }

     return correspondencia;
  }

  getAgentesDestinatario(): Array<AgentDTO> {

    const agentes = [];

    agentes.push(this.getRemitente());

    (<RadicacionSalidaFormInterface>this.source).destinatarioInterno.forEach(agenteInt => {
      const tipoAgente: AgentDTO = {
        ideAgente: null,
        codTipoRemite: TIPO_REMITENTE_INTERNO,
        codTipoPers: null,
        nombre: null,
        razonSocial: null,
        nit: null,
        codCortesia: null,
        codEnCalidad: null,
        codTipDocIdent: null,
        nroDocuIdentidad: null,
        codSede: agenteInt.sede ? agenteInt.sede.codigo : null,
        codDependencia: agenteInt.dependencia ? agenteInt.dependencia.codigo : null,
        fecAsignacion: null,
        codTipAgent: TIPO_AGENTE_DESTINATARIO,
        codEstado: null,
        indOriginal: agenteInt.tipoDestinatario ? agenteInt.tipoDestinatario.codigo : DESTINATARIO_INTERNO,
        ideFunci: !isNullOrUndefined(agenteInt.funcionario) ? agenteInt.funcionario.id : null,

      };
      agentes.push(tipoAgente);
    });

    (<RadicacionSalidaFormInterface>this.source).destinatarioExt.forEach(agenteExt => {

      const datosContactos = this.transformContactData(agenteExt.datosContactoList);

      if(!this.hasError && this.source.generales.reqDistElect){

        if( datosContactos.length === 0 || datosContactos.some( contact => isNullOrUndefined(contact.corrElectronico)))
           this.hasError = {error : "Es probable que exista un destinarario externo que no tenga correo."};
      }

      if(!this.hasError && this.source.generales.reqDistFisica){

       const hasError = datosContactos.length === 0 ||  datosContactos.some(contact => {

          if(isNullOrUndefined(contact.direccion))
             return false;
          try{
            const direccion = JSON.parse(contact.direccion);

            return   Object.keys(direccion).length === 0;

          } catch (e) {

            return false;
          }

        });

       if(hasError)
         this.hasError = { error: "Es probable que exista un destinarario externo sin dirección."}
      }

      console.log("agente Externo",agenteExt);

      const remitente = this.getRemitente();

      const tipoAgente: AgentDTO = {
        ideAgente: agenteExt.ideAgente,
        codTipoRemite: TIPO_REMITENTE_EXTERNO,
        codTipoPers: agenteExt.tipoPersona.codigo,
        nombre: agenteExt.nombre,
        razonSocial: agenteExt.razonSocial || null,
        nit: agenteExt.nit || null,
        codCortesia: null,
        codEnCalidad: isNullOrUndefined(agenteExt.actuaCalidad )? null : agenteExt.actuaCalidad.codigo,
        codTipDocIdent: isNullOrUndefined(agenteExt.tipoDocumento) ? null : agenteExt.tipoDocumento.codigo,
        nroDocuIdentidad: agenteExt.nroDocumentoIdentidad || null,
        codSede:  null,
        codDependencia: remitente.codDependencia,
        fecAsignacion: null,
        codTipAgent: TIPO_AGENTE_DESTINATARIO,
        codEstado: null,
        indOriginal: agenteExt.tipoDestinatario ? agenteExt.tipoDestinatario.codigo : DESTINATARIO_COPIA,
        datosContactoList:datosContactos
      };
      agentes.push(tipoAgente);
    });

    return agentes;
  }

  getComunicacionOficial():ComunicacionOficialDTO{

    let comunicacion = super.getComunicacionOficial();

    comunicacion.esRemitenteReferidoDestinatario = this.source.generales.responderRemitente;

    return comunicacion;
  }


}
