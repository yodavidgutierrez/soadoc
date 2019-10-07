package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.KpiReporte;
import co.com.soaint.foundation.canonical.ecm.DocumentoDTO;
import co.com.soaint.foundation.canonical.ecm.UnidadDocumentalDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Log4j2
@Transactional
@BusinessControl
public class KpiReporteControl extends KpiControl<KpiReporte> implements Serializable {

    private static final Long serialVersionUID = 879464646464612L;

    private final String repNumber7 = "reporte7";
    private final String repNumber8 = "reporte8";
    private final String repNumber9 = "reporte9";

    private final String findByUdIDAndDocNameAndDepCode = "KpiReporte.findByUdIDAndDocNameAndDepCode";
    private final String findByUdIDAndAndDepCode = "KpiReporte.findByUdIDAndAndDepCode";

    private final String nQfindMaxId = "KpiReporte.findMaxId";

    public KpiReporteControl() {
        super(KpiReporte.class);
    }

    @PostConstruct
    public void init() {
        initRep7();
        initRep8();
        initRep9();
        namedQueryMap.put(nQfindMaxId, new HashMap<>());
    }

    public Response processRep7(List<DocumentoDTO> dtoList) throws SystemException {

        if (ObjectUtils.isEmpty(dtoList)) {
            return Response.status(Response.Status.OK).build();
        }
        try {
            KpiReporte kpiReporte = KpiReporte.newInstance()
                    .nombre(kpiHelperMap.get(repNumber7).getKpiName())
                    .procesoNombre(kpiHelperMap.get(repNumber7).getProcessName())
                    .dataX01(repNumber7)
                    .build();

            final Map<String, Object> parameters = namedQueryMap.get(findByUdIDAndDocNameAndDepCode);

            boolean flush = false;
            for (DocumentoDTO documentoDTO : dtoList) {
                parameters.put("UD_ID", documentoDTO.getIdUnidadDocumental());
                parameters.put("DEP_CODE", documentoDTO.getCodigoDependencia());
                parameters.put("DOC_NAME", documentoDTO.getNombreDocumento());
                final List<KpiReporte> listKpi = getListKpi(findByUdIDAndDocNameAndDepCode);
                if (listKpi.isEmpty()) {

                    final String radicado = StringUtils.isEmpty(documentoDTO.getNroRadicado()) ? "" : documentoDTO.getNroRadicado();
                    String[] radicadoSplit = radicadoSplit(radicado);

                    kpiReporte.setDataN01(StringUtils.isEmpty(radicadoSplit[0])
                            ? null : new BigInteger(radicadoSplit[0]));

                    kpiReporte.setDataN02(StringUtils.isEmpty(radicadoSplit[1])
                            ? null : new BigInteger(radicadoSplit[1]));

                    kpiReporte.setDataN03(StringUtils.isEmpty(radicadoSplit[2])
                            ? null : new BigInteger(radicadoSplit[2]));

                    final Date fechaRadicacion = documentoDTO.getFechaRadicacion();
                    if (!ObjectUtils.isEmpty(fechaRadicacion)) {
                        final Timestamp radicationDate = Timestamp.from(fechaRadicacion.toInstant());
                        kpiReporte.setDataTS01(radicationDate);
                    }
                    kpiReporte.setDataX02(radicado);
                    kpiReporte.setDataX03(documentoDTO.getNombreDocumento());
                    kpiReporte.setDataX04(documentoDTO.getCodigoDependencia());
                    kpiReporte.setDataX05(documentoDTO.getDependencia());

                    kpiReporte.setDataX06(documentoDTO.getSerie());
                    kpiReporte.setDataX07(documentoDTO.getSubSerie());
                    kpiReporte.setDataX08(documentoDTO.getIdUnidadDocumental());
                    kpiReporte.setDataX09(documentoDTO.getNombreUnidadDocumental());
                    kpiReporte.setDataX12(documentoDTO.getTipologiaDocumental());
                    kpiReporte.setDataX13("Archivado");


                    kpiReporte.setReporteId(generateId(nQfindMaxId));
                    em.persist(kpiReporte);
                    kpiReporte = (KpiReporte) kpiReporte.clone();
                    flush = true;
                }
            }
            if (flush) {
                em.flush();
                return Response.status(Response.Status.OK).build();
            }
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception ex) {
            log.error("An error has occurred {}", ex.getMessage());
            throw new SystemException("An error has occurred " + ex.getMessage());
        }
    }

    public Response processRep8(List<UnidadDocumentalDTO> dtoList) throws SystemException {
        if (ObjectUtils.isEmpty(dtoList)) {
            return Response.status(Response.Status.OK).build();
        }
        try {
            KpiReporte kpiReporte = KpiReporte.newInstance()
                    .nombre(kpiHelperMap.get(repNumber8).getKpiName())
                    .procesoNombre(kpiHelperMap.get(repNumber8).getProcessName())
                    .dataX01(repNumber8)
                    .build();

            final Map<String, Object> parameters = namedQueryMap.get(findByUdIDAndAndDepCode);

            boolean flush = false;
            for (UnidadDocumentalDTO dto : dtoList) {
                parameters.put("UD_ID", dto.getId());
                parameters.put("DEP_CODE", dto.getCodigoDependencia());
                final List<KpiReporte> listKpi = getListKpi(findByUdIDAndAndDepCode);
                if (listKpi.isEmpty()) {
                    kpiReporte.setDataX02(dto.getCodigoDependencia());
                    kpiReporte.setDataX03(dto.getNombreDependencia());
                    kpiReporte.setDataX04(dto.getNombreSerie());
                    kpiReporte.setDataX05(dto.getNombreSubSerie());
                    kpiReporte.setDataX06(dto.getId());
                    kpiReporte.setDataX07(dto.getNombreUnidadDocumental());
                    kpiReporte.setDataX08(dto.getDescriptor1());
                    kpiReporte.setDataX09(dto.getDescriptor2());
                    kpiReporte.setDataX10("Electrónico");
                    kpiReporte.setDataX11(dto.getAccion());

                    final Calendar creationDate = dto.getFechaCreacion();
                    if (!ObjectUtils.isEmpty(creationDate)) {
                        LocalDateTime ldt = LocalDateTime.ofInstant(creationDate.toInstant(), ZoneId.systemDefault());
                        Timestamp timestamp = Timestamp.valueOf(ldt);
                        kpiReporte.setDataTS01(timestamp);
                    }
                    final Calendar initialDate = dto.getFechaExtremaInicial();
                    if (!ObjectUtils.isEmpty(initialDate)) {
                        LocalDateTime ldt = LocalDateTime.ofInstant(initialDate.toInstant(), ZoneId.systemDefault());
                        Timestamp timestamp = Timestamp.valueOf(ldt);
                        kpiReporte.setDataTS02(timestamp);
                    }
                    final Calendar finalizeDate = dto.getFechaExtremaFinal();
                    if (!ObjectUtils.isEmpty(finalizeDate)) {
                        LocalDateTime ldt = LocalDateTime.ofInstant(finalizeDate.toInstant(), ZoneId.systemDefault());
                        Timestamp timestamp = Timestamp.valueOf(ldt);
                        kpiReporte.setDataTS03(timestamp);
                    }
                    final Calendar closingDate = dto.getFechaCierre();
                    if (!ObjectUtils.isEmpty(closingDate)) {
                        LocalDateTime ldt = LocalDateTime.ofInstant(closingDate.toInstant(), ZoneId.systemDefault());
                        Timestamp timestamp = Timestamp.valueOf(ldt);
                        kpiReporte.setDataTS04(timestamp);
                    }
                    kpiReporte.setReporteId(generateId(nQfindMaxId));
                    em.persist(kpiReporte);
                    kpiReporte = (KpiReporte) kpiReporte.clone();
                    flush = true;
                }
            }
            if (flush) {
                em.flush();
                return Response.status(Response.Status.OK).build();
            }
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception ex) {
            log.error("An error has occurred {}", ex.getMessage());
            throw new SystemException("An error has occurred " + ex.getMessage());
        }
    }

    public Response processRep9(List<UnidadDocumentalDTO> dtoList) {
        log.info("Implement {}", repNumber9);
        return Response.status(Response.Status.OK).build();
    }

    private String[] radicadoSplit(String nroRadicado) {
        final String[] response = new String[]{"", "", ""};
        if (StringUtils.isEmpty(nroRadicado)) {
            return response;
        }
        String separator = "--";
        String radicado = null;
        int index;
        if ((index = nroRadicado.lastIndexOf(separator)) >= 0) {
            radicado = nroRadicado.substring(index + separator.length());
        }
        if (StringUtils.isEmpty(radicado) || radicado.indexOf('-') < 0) {
            return response;
        }
        String[] split = radicado.split("-");
        final int until = split.length >= response.length ? response.length : split.length;
        System.arraycopy(split, 0, response, 0, until);
        return response;
    }

    private void initRep7() {
        final KpiHelper helper = new KpiHelper();
        helper.setProcessName("Organización y archivo");
        helper.setKpiName("Documentos archivados y pendientes por archivar");
        kpiHelperMap.put(repNumber7, helper);

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("REP_NUMBER", repNumber7);
        namedQueryMap.put(findByUdIDAndDocNameAndDepCode, parameters);
    }

    private void initRep8() {
        final KpiHelper helper = new KpiHelper();
        helper.setProcessName("Organización y archivo y Gestión de unidades documentales");
        helper.setKpiName("Unidades documentales creadas y su estado: cerradas abiertas reactivadas");
        kpiHelperMap.put(repNumber8, helper);

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("REP_NUMBER", repNumber8);
        namedQueryMap.put(findByUdIDAndAndDepCode, parameters);
    }

    private void initRep9() {
        final KpiHelper helper = new KpiHelper();
        helper.setProcessName("Transferencias documentales y disposición final");
        helper.setKpiName("Transferencias documentales realizadas por dependencia");
        kpiHelperMap.put(repNumber9, helper);

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("REP_NUMBER", repNumber9);
        namedQueryMap.put("", parameters);
    }
}
