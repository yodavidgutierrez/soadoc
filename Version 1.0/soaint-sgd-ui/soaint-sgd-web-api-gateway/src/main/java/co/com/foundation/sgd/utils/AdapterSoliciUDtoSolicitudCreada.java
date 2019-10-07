package co.com.foundation.sgd.utils;

import co.com.foundation.sgd.dto.SolicitudTramitadaDTO;
import co.com.soaint.foundation.canonical.correspondencia.SolicitudUnidadDocumentalDTO;

public class AdapterSoliciUDtoSolicitudCreada {

    public static SolicitudTramitadaDTO convertToSolicitudCreada(SolicitudUnidadDocumentalDTO solicitudUnidadDocumentalDTO){

        final SolicitudTramitadaDTO solicitudTramitadaDTO = new SolicitudTramitadaDTO();

        solicitudTramitadaDTO.setAccion(solicitudUnidadDocumentalDTO.getAccion());
        solicitudTramitadaDTO.setCodigoDependencia(solicitudUnidadDocumentalDTO.getCodigoDependencia());
        solicitudTramitadaDTO.setCodigoSede(solicitudUnidadDocumentalDTO.getCodigoSede());
        solicitudTramitadaDTO.setCodigoSerie(solicitudUnidadDocumentalDTO.getCodigoSerie());
        solicitudTramitadaDTO.setCodigoSubSerie(solicitudUnidadDocumentalDTO.getCodigoSubSerie());
        solicitudTramitadaDTO.setDescriptor1(solicitudUnidadDocumentalDTO.getDescriptor1());
        solicitudTramitadaDTO.setDescriptor2(solicitudUnidadDocumentalDTO.getDescriptor2());
        solicitudTramitadaDTO.setId(solicitudUnidadDocumentalDTO.getId());
        solicitudTramitadaDTO.setFechaHora(solicitudUnidadDocumentalDTO.getFechaHora());
        solicitudTramitadaDTO.setEstado(solicitudUnidadDocumentalDTO.getEstado());
        solicitudTramitadaDTO.setIdConstante(solicitudUnidadDocumentalDTO.getIdConstante());
        solicitudTramitadaDTO.setIdSolicitante(solicitudUnidadDocumentalDTO.getIdSolicitante());
        solicitudTramitadaDTO.setMotivo(solicitudUnidadDocumentalDTO.getMotivo());
        solicitudTramitadaDTO.setNombreSerie(solicitudUnidadDocumentalDTO.getNombreSerie());
        solicitudTramitadaDTO.setNombreSubSerie(solicitudUnidadDocumentalDTO.getNombreSubSerie());
        solicitudTramitadaDTO.setNombreUnidadDocumental(solicitudUnidadDocumentalDTO.getNombreUnidadDocumental());
        solicitudTramitadaDTO.setNro(solicitudUnidadDocumentalDTO.getNro());
        solicitudTramitadaDTO.setObservaciones(solicitudUnidadDocumentalDTO.getObservaciones());

        return  solicitudTramitadaDTO;
    }
}
