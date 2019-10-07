import {DocumentoDTO} from "./documentoDTO";

export interface ModeloConsultaDocumentoDTO extends DocumentoDTO{
  idDocDb: number;
  destinatario: string;
  estado: string;
  funcionario: string;
  fechaVencimiento: Date;
  nroGuia: string;
  estadoEntrega: string;
  tipoCMC: string;

}
