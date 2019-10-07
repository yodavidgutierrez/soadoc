import {ComunicacionOficialDTO} from '../../domain/comunicacionOficialDTO';
import {Observable} from 'rxjs/Observable';
import {AgentDTO} from '../../domain/agentDTO';
import {ContactoDTO} from '../../domain/contactoDTO';
import {DocumentoDTO} from '../../domain/documentoDTO';
import {RadicacionEntradaFormInterface} from '../interfaces/data-transformers/radicacionEntradaForm.interface';
import {
  DATOS_CONTACTO_PRINCIPAL, TIPO_AGENTE_REMITENTE,
  TIPO_AGENTE_DESTINATARIO, COMUNICACION_INTERNA
} from '../bussiness-properties/radicacion-properties';
import {AnexoDTO} from '../../domain/anexoDTO';
import { DireccionDTO } from '../../domain/DireccionDTO';
import {isNullOrUndefined} from "util";

export class RadicacionEntradaDTV {

  constructor(private source: ComunicacionOficialDTO) {

  }

  getDatosRemitente(): Observable<AgentDTO> {
    return Observable.of(this.source.agenteList.find(value => value.codTipAgent === 'TP-AGEE' || value.codTipoRemite === 'EXT'));
    // return Observable.of(this.source.agenteList.find(value => value.codTipoRemite === 'EXT'));
  }

  getDatosContactos(): Observable<ContactoDTO[]> {
    return Observable.of(this.getDatosContactoFormList());
  }

  getDatosDocumento(): Observable<DocumentoDTO[]> {
    return Observable.of(this.source.ppdDocumentoList);
  }

  getDatosDestinatarios(): Observable<AgentDTO[]> {
    // return Observable.of(this.source.agenteList.filter(value => value.codTipAgent === 'TP-AGEI'));
    return Observable.of(this.source.agenteList.filter(value => value.codTipoRemite === 'INT' && value.codTipAgent === 'TP-AGEI'));
  }

  getDatosDestinatarioInmediate(): AgentDTO[] {

    return !isNullOrUndefined(this.source.agenteList)? this.source.agenteList.filter(value => value.codTipAgent === 'TP-AGEI') : null;
  }

  getRadicacionEntradaFormData(): RadicacionEntradaFormInterface {

    return {
      generales: {
        fechaRadicacion: this.source.correspondencia.fecRadicado,
        nroRadicado: this.source.correspondencia.nroRadicado,
        tipoComunicacion: this.source.correspondencia.codTipoCmc,
        medioRecepcion:  this.source.correspondencia.codMedioRecepcion,
        empresaMensajeria: this.source.correspondencia.codEmpMsj,
        numeroGuia: this.source.correspondencia.nroGuia,
        tipologiaDocumental:  this.source.correspondencia.codTipoDoc,
        unidadTiempo:  this.source.correspondencia.codUnidadTiempo,
        numeroFolio: this.source.ppdDocumentoList.length > 0 ? this.source.ppdDocumentoList[0].nroFolios : null,
        inicioConteo: this.source.correspondencia.inicioConteo,
        reqDistFisica: this.source.correspondencia.reqDistFisica,
        reqDigit: this.source.correspondencia.reqDigita,
        reqDigitInmediata: null,
        tiempoRespuesta: this.source.correspondencia.tiempoRespuesta,
        asunto: this.source.ppdDocumentoList[0].asunto,
        radicadoReferido: null,
        tipoAnexos: null,
        tipoAnexosDescripcion: null,
        hasAnexos: null,
        ideDocumento: this.source.correspondencia.ideDocumento,
        idePpdDocumento: this.source.ppdDocumentoList[0].idePpdDocumento,
      },
      datosContactos: this.getDatosContactoFormList(),
      radicadosReferidos: this.getRadicadosReferidosFormList(),
      remitente: this.getRemitenteForm(),
      descripcionAnexos: this.getAnexosFormList(),
      agentesDestinatario: this.getDestinatarioFormList()
    }
  }

  getDatosContactoFormList() {
    const contactos = [];
    this.source.datosContactoList.forEach(contacto => {
    const direccion: DireccionDTO = this.GetDireccion(contacto);
    contactos.push({
        tipoVia: (direccion) ? direccion.tipoVia : null,
        noViaPrincipal: contacto.nroViaGeneradora,
        prefijoCuadrante: (direccion) ? direccion.prefijoCuadrante : null,
        bis: (direccion) ? direccion.bis : null,
        orientacion: (direccion) ? direccion.orientacion : null,
        direccion: contacto.direccion || null,
        noVia: (direccion) ? direccion.noVia : null,
        prefijoCuadrante_se: (direccion) ? direccion.prefijoCuadrante_se : null,
        placa: contacto.nroPlaca,
        orientacion_se: (direccion) ? direccion.orientacion_se : null,
        complementoTipo: (direccion) ? direccion.complementoTipo : null,
        complementoAdicional: (direccion) ? direccion.complementoAdicional : null,
        celular: contacto.celular,
        numeroTel: contacto.telFijo,
        correoEle: contacto.corrElectronico,
        pais:  {codigo: contacto.codPais},
        departamento: {codigo: contacto.codDepartamento},
        municipio: {codigo: contacto.codMunicipio},
        tipoContacto:{codigo:contacto.tipoContacto},
        principal: contacto.principal === DATOS_CONTACTO_PRINCIPAL ? true : false,
        ciudad: contacto.ciudad,
        provEstado : contacto.provEstado

      });
    });

    return contactos;
  }

  getRadicadosReferidosFormList() {
    const referidos = [];
    this.source.referidoList.forEach(referido => {
      referidos.push({ideReferido: referido.ideReferido, nombre: referido.nroRadRef});
    });

    return referidos;
  }

  getAnexosFormList() {
    const anexos = [];
    this.source.anexoList.forEach((anexo: AnexoDTO) => {
      anexos.push({
        ideAnexo: anexo.ideAnexo,
        tipoAnexo: {codigo: anexo.codAnexo},
        soporteAnexo: {codigo: anexo.codTipoSoporte},
        descripcion: anexo.descripcion
      });
    });

    return anexos;
  }

  getRemitenteInternoForm(remitente: AgentDTO) {
    return {
      ideAgente: remitente.ideAgente,
      tipoPersona: null,
      nit: null,
      actuaCalidad: null,
      tipoDocumento: null,
      razonSocial: null,
      nombreApellidos: null,
      nroDocumentoIdentidad: null,
      sedeAdministrativa: {codigo: remitente.codSede},
      dependenciaGrupo: {codigo: remitente.codDependencia}
    }
  }

  getRemitenteExternoForm(remitente: AgentDTO) {
    return {
      ideAgente: remitente.ideAgente,
      tipoPersona: remitente.codTipoPers,
      nit: remitente.nit,
      actuaCalidad: remitente.codEnCalidad,
      tipoDocumento: remitente.codTipDocIdent,
      razonSocial: remitente.razonSocial,
      nombreApellidos: remitente.nombre,
      nroDocumentoIdentidad: remitente.nroDocuIdentidad,
      sedeAdministrativa: null,
      dependenciaGrupo: null
    }
  }

  getRemitenteForm() {
    const isRemitenteInterno =  this.source.correspondencia.codTipoCmc === COMUNICACION_INTERNA;
    const agente = this.source.agenteList.find((agente: AgentDTO) => agente.codTipAgent === TIPO_AGENTE_REMITENTE);
    if (isRemitenteInterno) {
      return this.getRemitenteInternoForm(agente);
    } else {
      return this.getRemitenteExternoForm(agente);
    }

  }

  getDestinatarioFormList() {
    const destinatarios = [];
    this.source.agenteList.filter((agente: AgentDTO) => agente.codTipAgent === TIPO_AGENTE_DESTINATARIO).forEach((destinatario: AgentDTO) => {
      destinatarios.push({
        ideAgente: destinatario.ideAgente,
        tipoDestinatario: {codigo: destinatario.indOriginal},
        sedeAdministrativa: {codigo: destinatario.codSede},
        dependenciaGrupo: {codigo: destinatario.codDependencia}
      });
    });
    return destinatarios;
  }

  GetDireccion(contact): DireccionDTO {
    let direccion: DireccionDTO  = {};
    try {
      direccion =  JSON.parse(contact.direccion);
    } catch (e) {
      direccion = null;
    }
    if (direccion) {
      direccion.tipoVia = (direccion.tipoVia) ? direccion.tipoVia : null;
      direccion.noViaPrincipal = (direccion.noViaPrincipal) ? direccion.noViaPrincipal : null;
      direccion.prefijoCuadrante = (direccion.prefijoCuadrante) ? direccion.prefijoCuadrante : null;
      direccion.bis = (direccion.bis) ? direccion.bis : null;
      direccion.orientacion = (direccion.orientacion) ? direccion.orientacion : null;
      direccion.noVia = (direccion.noVia) ? direccion.noVia : null;
      direccion.prefijoCuadrante_se = (direccion.prefijoCuadrante_se) ? direccion.prefijoCuadrante_se : null;
      direccion.placa = (direccion.placa) ? direccion.placa : null;
      direccion.orientacion_se = (direccion.orientacion_se) ? direccion.orientacion_se : null;
      direccion.complementoTipo = (direccion.complementoTipo) ? direccion.complementoTipo : null;
      direccion.complementoAdicional = (direccion.complementoAdicional) ? direccion.complementoAdicional : null;
    }
    return direccion;
  }


}
