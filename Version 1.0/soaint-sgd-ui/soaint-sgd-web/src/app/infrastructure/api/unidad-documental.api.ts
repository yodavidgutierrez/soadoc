import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/Observable';
import {UnidadDocumentalDTO} from '../../domain/unidadDocumentalDTO';
import {DisposicionFinalDTO} from '../../domain/DisposicionFinalDTO';
import { MensajeRespuestaDTO } from '../../domain/MensajeRespuestaDTO';
import {isNullOrUndefined} from "util";

@Injectable()
export class UnidadDocumentalApiService {

  constructor(private _api: ApiBase) {
  }

  Listar(payload: UnidadDocumentalDTO): Observable<any> {
    return this._api.post(environment.listar_unidad_documental_endpoint, payload)
    .map((resp) => {
      if (resp.response) {
        return resp.response
      }
        return [];

    });
  }

  ListarUnidadesDocumentalesTranferir(payload:any):Observable<any>{

    return  this._api.list(`${environment.listar_unidades_documentales_transferir}`,payload)
      .map((resp) => {
        if (resp.response) {
          return resp.response
        } else {
          return Observable.of([]);
        }
      });
  }

  ListarUnidadesDocumentalesVerificar(payload):Observable<UnidadDocumentalDTO[]>{

    return  this._api.list(environment.listar_unidades_documentales_verificar,payload)
      .map((resp) => {
        if (resp.response) {
          return resp.response.unidadesDocumentales
        } else {
          return Observable.of([]);
        }
      });
  }

  ListarUnidadesDocumentalesUbicar(payload):Observable<UnidadDocumentalDTO[]>{

    return  this._api.list(environment.listar_unidades_documentales_confirmadas,payload)
      .map((resp) => {
        if (resp.response) {
          return resp.response.unidadesDocumentales
        } else {
          return Observable.of([]);
        }
      });
  }

  GetDetalleUnidadDocumental(payload: string): Observable<UnidadDocumentalDTO> {
    return this._api.list(environment.detalle_unidad_documental_endpoint + payload)
    .map(response => response.response.unidadDocumental);
  }

   crear(unidadDocumental: UnidadDocumentalDTO):Observable<any> {
     return this._api.post(environment.crear_unidad_documental, unidadDocumental);

   }

  gestionarUnidadesDocumentales(payload: any): Observable<MensajeRespuestaDTO> {
    return this._api.post(environment.gestionar_unidades_documentales_endpoint, payload)
  }


  noTramitarUnidadesDocumentales(payload: any){

    //return this._api.post("",payload)

    return Observable.of(true);

  }

  quickSave(payload: any) {
    return this._api.post(environment.salvarCorrespondenciaEntrada_endpoint, payload);
  }

  listarUnidadesDocumentales(payload:any):Observable<any[]>{

    return this._api.post(environment.listar_unidad_documental_endpoint,payload)
      .map( response => response.response.unidadDocumental);
  }

  listarUnidadesDocumentalesDisposicion(payload: DisposicionFinalDTO): Observable<UnidadDocumentalDTO[]>{

        return this._api.list(`${environment.listar_unidades_documentales_disposicion_endpoint}/${payload.unidadDocumentalDTO.codigoDependencia}`, {disposicionFinal: JSON.stringify(payload) })
          .map( response => {
            return  response.response ? response.response.unidadesDocumentales : Observable.of([]);
          });
  }

  aprobarRechazarUDDisposicion(payload: UnidadDocumentalDTO[]): Observable<MensajeRespuestaDTO>{

        return this._api.post(environment.aprobar_rechazar_unidades_documentales_endpoint, payload)
          .map( response => response);
  }

  aprobarRechazarUDAprobarTransferencia(tipoTransferencia:string,payload: UnidadDocumentalDTO[]): Observable<MensajeRespuestaDTO>{

    return this._api.put(`${environment.aprobar_rechazar_transferencia_documentales}/${tipoTransferencia}`,payload);

   /* return Observable.of({
      codMensaje: '0000',
      mensaje: 'Operación Completada',
      response: []
    });*/
  }

  aprobarRechazarUDVerificarTransferencia(payload: UnidadDocumentalDTO[]): Observable<MensajeRespuestaDTO>{

    return this._api.put(environment.confirmar_unidaddes_documentales,payload)
  }

  listarDocumentosPorArchivar(codDependencia):Observable<any>{

    return this._api.list(environment.listar_documentos_archivar+codDependencia);
  }

  archivarDocumento(payload:any):Observable<any>{

    return this._api.post(environment.archivar_documento_endpoint,payload);
  }

  listarDocumentosArchivadosPorDependencia(codDependencia): Observable<any[]>{

    return this._api.list(environment.listar_documentos_archivados+codDependencia)
               .map(response => !isNullOrUndefined(response.response) ? response.response.documentos : []);
  }

  listarDocumentosArchivadosPorUd(idEcm): Observable<any[]>{

    return this._api.list(`${environment.listar_documentos_archivados_ud}${idEcm}`)
      .map(response => !isNullOrUndefined(response.response) ? response.response.documentos : []);
  }

  subirDocumentosParaArchivar(documentos: FormData):Observable<any>{

   return  this._api.sendFile(environment.subir_documentos_por_archivar,documentos,[]);
  }

  obtenerDocumentoPorNoRadicado(nroRadicado):Observable<any>{
    return this._api.list( `${environment.obtenerDocumento_nro_radicado_endpoint}/${nroRadicado}`);
  }

  eliminarDocumento(idEcm):Observable<any>{

   return  this._api.delete(`${environment.pd_gestion_documental.eliminarVersionDocumento}/${idEcm}`);
  }

  buscarUnidadesDocumentales(payload): Observable<any>{

    return  this._api.list(environment.buscar_unidad_documental_endpoint,payload)
      .map( response =>   response.response.unidadDocumental);
  }


}
