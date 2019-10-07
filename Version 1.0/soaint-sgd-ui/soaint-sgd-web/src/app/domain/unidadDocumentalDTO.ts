import {UnidadConservacionDTO} from "./UnidadConservacionDTO";

export interface  UnidadDocumentalDTO {
    id?: string;
    descriptor2?: string;
    fechaCierre?: Date;
    fechaExtremaInicial?: Date;
    fechaExtremaFinal?: Date;
    soporte?: string;
    inactivo?: boolean;
    estado?: string;
    ubicacionTopografica?: string;
    faseArchivo?: string;
    descriptor1?: string;
    codigoSubSerie?: string;
    nombreSubSerie?: string;
    codigoSerie?: string;
    nombreSerie?: string;
    nombreUnidadDocumental?: string;
    codigoUnidadDocumental?: string;
    codigoDependencia?: string;
    nombreDependencia?: string;
    codigoSede?: string;
    nombreSede?: string;
    cerrada?: boolean;
    disposicion?: string;
    accion?: string;
    seleccionado?: boolean;
    observaciones?: string;
    listaDocumentos?: any;
    blocked?:boolean;
    idUnidadConservacion?:string;
    ubicacionFisica?:string;
    entidadRecibe?:string;
    unidadesConservacion?:UnidadConservacionDTO[];
    consecutivoTransferencia?:string;
    ecmObjId?: string;
}
