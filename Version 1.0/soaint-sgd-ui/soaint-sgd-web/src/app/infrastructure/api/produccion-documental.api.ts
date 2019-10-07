import {Injectable} from '@angular/core';
import {ApiBase} from './api-base';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs/Observable';
import {ConstanteDTO} from '../../domain/constanteDTO';
import {RolDTO} from '../../domain/rolesDTO';
import {SUCCESS_ADJUNTAR_DOCUMENTO} from '../../shared/lang/es';
import {PushNotificationAction} from '../state-management/notifications-state/notifications-actions';
import {DocumentoEcmDTO} from "../../domain/documentoEcmDTO";
import {VersionDocumentoDTO} from "../../ui/page-components/produccion-documental/models/DocumentoDTO";
import {TransformVersionDocumentoToDocumentoEcm} from "../../shared/data-transformers/transform-version-documento-to-documento-ecm";

@Injectable()
export class ProduccionDocumentalApiService {

    roles: RolDTO[] = [
        {'id': 1, 'rol': 'administrador', 'nombre': 'Administrador'},
        {'id': 2, 'rol': 'proyector', 'nombre': 'Proyector'},
        {'id': 3, 'rol': 'revisor', 'nombre': 'Revisor'},
        {'id': 4, 'rol': 'aprobador', 'nombre': 'Aprobador'}
    ];

  constructor(private _api: ApiBase,private _adapter:TransformVersionDocumentoToDocumentoEcm) {
  }

  guardarEstadoTarea(payload: any) {
    return this._api.post(environment.taskStatus_endpoint, payload).map(response => response);
  }

  obtenerEstadoTarea(payload: {idInstanciaProceso: string, idTareaProceso: string}) {
    return this._api.list(`${environment.taskStatus_endpoint}/${payload.idInstanciaProceso}/${payload.idTareaProceso}`, {}).map(response => response.payload);
  }

  obtenerContactosDestinatarioExterno(payload: {nroRadicado: string}) {
    return this._api.list(`${environment.obtenerContactoDestinatarioExterno_endpoint}/${payload.nroRadicado}`, {});
  }

  obtenerDatosDocXnroRadicado(payload: {id: string}) {
      return this._api.list(`${environment.pd_obtenerDatosDocXnroRadicado}/${payload.id}`, {});
  }

  subirVersionDocumento(formData: FormData) {
      return this._api.sendFile( environment.pd_gestion_documental.subirDocumentoVersionado, formData, [] );
  }

  obtenerVersionDocumento(payload: {id: string, version: string}) {
    return this._api.list(environment.pd_gestion_documental.obtenerVersionDocumento, {identificadorDoc: payload.id, version: payload.version});
  }

  obtenerVersionDocumentoUrl(payload: {id: string, version: string}) {
      return `${environment.pd_gestion_documental.obtenerVersionDocumento}?identificadorDoc=${payload.id}&version=${payload.version}`;
  }

  transformarAPdf(htmlContent:string,tipoPlantilla):Observable<any> {

   return  this._api.put(`${environment.pdVersionPdfEndPoint}/${tipoPlantilla}`,htmlContent)
               .map( res => res.response.documento);
  }


  eliminarVersionDocumento(payload: {id: string}) {
    return this._api.delete(`${environment.pd_gestion_documental.eliminarVersionDocumento}/${payload.id}`, {});
  }

  obtenerDocumentoUrl(payload: {id: string}) {
    return `${environment.pd_gestion_documental.obtenerDocumentoPorId}?identificadorDoc=${payload.id}`;
  }

  subirAnexo(formData: FormData) {
    return this._api.sendFile( environment.pd_gestion_documental.subirAnexo, formData, []);
  }

  eliminarAnexo(payload: {id: string}) {
      return this._api.delete(`${environment.pd_gestion_documental.eliminarAnexo}/${payload.id}`, {});
  }


  getFuncionariosByLoginnames(loginnames: string) {
      return this._api.list(`${environment.obtenerFuncionario_endpoint}/funcionarios/listar-by-loginnames/`, {loginNames: loginnames}).map(res => res.funcionarios);
  }


  ejecutarProyeccionMultiple(payload: {}) {
    return this._api.post(environment.pd_ejecutar_proyeccion_multiple, payload);
  }

  getTiposComunicacion(payload: {}) {
    return this._api.list(environment.tipoComunicacion_endpoint, payload).map(res => res.constantes);
  }

  getTiposComunicacionSalida(payload: {}) {
    return this._api.list(environment.tipoComunicacionSalida_endpoint, payload).map(res => res.constantes);
  }

  getFuncionariosPorDependenciaRol(payload) {
    return this._api.list(environment.listarFuncionarios_endpoint + '/' + payload.codDependencia, payload);
  }

  getTiposAnexo(payload: {}) {
    return this._api.list(environment.tipoAnexos_endpoint, payload).map(res => res.constantes);
  }

  getTiposDestinatario(payload: {}) {
    return this._api.list(environment.tipoDestinatario_endpoint, payload).map(res => res.constantes);
  }

  getTiposDocumento(payload: {}) {
    return this._api.list(environment.tipoDocumento_endpoint, payload).map(res => res.constantes);
  }

  getTiposPersona(payload: {}) {
    return this._api.list(environment.tipoPersona_endpoint, payload).map(res => res.constantes);
  }

  getActuaEnCalidad(payload: {}) {
    return this._api.list(environment.actuaCalidad_endpoint, payload).map(res => res.constantes);
  }

  getSedes(payload: {}) {
    return this._api.list(environment.sedeAdministrativa_endpoint, payload).map(res => res.organigrama);
  }

  getDependencias(payload: {}) {
    return this._api.list(environment.dependencias_endpoint, payload).map(res => res.dependencias);
  }

  getTipoPlantilla(payload): Observable<string> {
      return this._api.list(`${environment.tipoPlantilla_endpoint}/obtener/${payload.codigo}`, payload).map(res => res.text);
  }

  generarPdf(payload): Observable<{success: boolean, text: string}> {
    return this._api.post(`${environment.tipoPlantilla_endpoint}/generar-pdf`, payload).map(res => res);
  }

  getTiposPlantilla(payload: {}): Observable<ConstanteDTO[]> {
    return Observable.of(JSON.parse(`[
          {"codigo":"TL-DOCOF","nombre":"Oficio","codPadre":"TL-DOC","id":49},
          {"codigo":"TL-DOCM","nombre":"Memorando","codPadre":"TL-DOC","id":61}
        ]`));
  }

  getRoles(payload: {}): Observable<RolDTO[]> {
    return Observable.of(this.roles);
  }

  getRoleByRolename(rolname: string): RolDTO {
    return this.roles.find((el: RolDTO) => el.rol === rolname);
  }

}
