import {ConstanteDTO} from '../../../../domain/constanteDTO';
import {AnexoDTO, VersionDocumentoDTO} from './DocumentoDTO';
import {ProyectorDTO} from '../../../../domain/ProyectorDTO';
import {DestinatarioDTO} from '../../../../domain/destinatarioDTO';
import {ObservacionDTO} from './ObservacionDTO';
import {AgentDTO} from "../../../../domain/agentDTO";

export interface StatusDTO {
  datosGenerales: {
    tipoComunicacion: ConstanteDTO,
    tipoPlantilla?: ConstanteDTO,
    listaVersionesDocumento: VersionDocumentoDTO[],
    listaAnexos: AnexoDTO[]
  },
  datosContacto: {
    distFisica: boolean,
    distElectronica:boolean,
    responderRemitente: boolean,
    hasDestinatarioPrincipal: boolean,
    issetListDestinatarioBackend: boolean,
    listaDestinatariosInternos: DestinatarioDTO[],
    listaDestinatariosExternos: DestinatarioDTO[]
  },
  gestionarProduccion: {
    startIndex: number,
    cantObservaciones: number,
    listaProyectores: ProyectorDTO[],
    listaObservaciones: ObservacionDTO[]
  }
}
export interface VariablesTareaDTO {
  aprobado?: number,
  requiereAjustes?: number,
  listaProyector?: string[],
  listaAprobador?: string[],
  listaRevisor?: string[],
  fechaProd?:number,
  notifiable?:boolean,
  destinatario?:AgentDTO,
  remitente?:AgentDTO,
  nroRadicado?:string;
  nombreTarea?:string;
  usuarioProyectorRadicador?:string;
  codDependencia?:string;
}
