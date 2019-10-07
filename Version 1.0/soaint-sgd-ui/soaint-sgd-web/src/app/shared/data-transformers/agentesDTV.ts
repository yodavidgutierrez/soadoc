import {AgentDTO} from '../../domain/agentDTO';
import {
  COMUNICACION_EXTERNA, COMUNICACION_EXTERNA_ENVIADA,
  COMUNICACION_INTERNA, COMUNICACION_INTERNA_ENVIADA, DESTINATARIO_EXTERNO, DESTINATARIO_PRINCIPAL,
  TIPO_AGENTE_REMITENTE, TIPO_REMITENTE_EXTERNO,
  TIPO_REMITENTE_INTERNO
} from '../bussiness-properties/radicacion-properties';


export interface  AgenteDTV {

  getRemitente(remitente: any): AgentDTO;
}


export class AgenteInternoDTV implements AgenteDTV {

  getRemitente(remitente: any): AgentDTO {

    return {
      ideAgente: remitente.ideAgente || null,
      codTipoRemite: TIPO_REMITENTE_INTERNO,
      codTipoPers: null,
      nombre: null,
      razonSocial: null,
      nit: null,
      codCortesia: null,
      codEnCalidad: null,
      codTipDocIdent: null,
      nroDocuIdentidad: null,
      codSede: remitente.sedeAdministrativa ? remitente.sedeAdministrativa.codigo || remitente.sedeAdministrativa: null,
      codDependencia: remitente.dependenciaGrupo ? remitente.dependenciaGrupo.codigo || remitente.dependenciaGrupo : null,
      fecAsignacion: null,
      codTipAgent: TIPO_AGENTE_REMITENTE,
      codEstado: null,
      indOriginal:remitente.tipoDestinatario ? remitente.tipoDestinatario : null,
      ideFunci:remitente.funcionarioGrupo.id || remitente.funcionarioGrupo
    };
  }
}

export class AgenteExternoDTV implements AgenteDTV {

  getRemitente(remitente: any): AgentDTO {

    return  {
      ideAgente: remitente.ideAgente || null,
      codTipoRemite: TIPO_REMITENTE_EXTERNO,
      codTipoPers: remitente.tipoPersona ? remitente.tipoPersona.codigo || remitente.tipoPersona : null,
      nombre: remitente.nombreApellidos || null,
      razonSocial: remitente.razonSocial || null,
      nit: remitente.nit || null,
      codEnCalidad: remitente.actuaCalidad ? remitente.actuaCalidad.codigo  || remitente.actuaCalidad: null,
      codTipDocIdent: remitente.tipoDocumento ? remitente.tipoDocumento.codigo || remitente.tipoDocumento : null,
      nroDocuIdentidad: remitente.nroDocumentoIdentidad,
      codSede: null,
      codDependencia: null,
      fecAsignacion: null,
      codTipAgent: TIPO_AGENTE_REMITENTE,
      indOriginal: null,
      codEstado: null,
      //datosContactoList: remitente.
    };

  }
}

export class AgenteFactoryDTV {

  static getAgente(tipo: string): AgenteDTV {

    switch (tipo) {

      case COMUNICACION_INTERNA_ENVIADA:
      case COMUNICACION_INTERNA:
      case COMUNICACION_EXTERNA_ENVIADA: return new AgenteInternoDTV();
      case COMUNICACION_EXTERNA : return new AgenteExternoDTV();
    }

    return null;

  }
}
