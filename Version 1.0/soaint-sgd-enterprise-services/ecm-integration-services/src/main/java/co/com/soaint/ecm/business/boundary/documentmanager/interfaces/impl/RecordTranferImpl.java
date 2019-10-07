package co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl;

import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.ContentControl;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.RecordTransfer;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.StructureAlfresco;
import co.com.soaint.ecm.domain.entity.*;
import co.com.soaint.ecm.util.ConstantesECM;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import co.com.soaint.foundation.canonical.ecm.UnidadDocumentalDTO;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Log4j2
@Service("recordTransfer")
public class RecordTranferImpl implements RecordTransfer {

    private static final Long serialVersionUID = 201L;

    @Autowired
    private ContentControl contentControl;

    @Autowired
    private RecordServices recordServices;

    @Autowired
    @Qualifier("recordStructure")
    private StructureAlfresco recordStructure;

    @Override
    public MensajeRespuesta listarUnidadesDocumentalesTransferencia(String tipoTransferencia, String dependencyCode) throws SystemException {
        if (StringUtils.isEmpty(dependencyCode) || StringUtils.containsIgnoreCase(dependencyCode.trim(), "null")) {
            throw new SystemException("No se procesa codigo de dependencia = '" + dependencyCode + "'");
        }
        final Folder depFolder = getDepFolder(dependencyCode);
        final TransferType transferType = TransferType.getTransferBy(tipoTransferencia);
        if (transferType != null) {
            final List<Folder> recordFolders = new ArrayList<>();
            findRecordFoldersTransfer(recordFolders, depFolder, transferType);
            final List<UnidadDocumentalDTO> documentalDTOList = transformUdFolders(recordFolders, null);
            final Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("unidadesDocumentales", documentalDTOList);
            responseMap.put("consecutivo", generarNumeroTransferencia(transferType, dependencyCode));
            return MensajeRespuesta.newInstance()
                    .response(responseMap)
                    .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                    .mensaje(ConstantesECM.SUCCESS).build();
        }
        throw new SystemException("No se procesa '" + tipoTransferencia + "'");
    }

    @Override
    public MensajeRespuesta aprobarRechazarTransferencias(List<UnidadDocumentalDTO> unidadDocumentalDTOS, String tipoTransferencia) throws SystemException {
        recordServices.compruebaIdEstado(unidadDocumentalDTOS, true);
        final TransferType transferType = TransferType.getTransferBy(tipoTransferencia);
        if (transferType == null) {
            throw new SystemException("No se procesa el tipo de transferencia " + tipoTransferencia);
        }

        final Folder getBaseFolder = recordStructure.getBaseFolder();
        final boolean isPrimaryTransfer = transferType == TransferType.PRIMARY;
        for (UnidadDocumentalDTO dto : unidadDocumentalDTOS) {
            final String idUd = dto.getId();

            final Optional<Folder> recordFolderByUdId = recordServices.getRecordFolderByUdId(idUd);
            if (recordFolderByUdId.isPresent()) {

                final Folder recordFolder = recordFolderByUdId.get();

                final String phaseFileStringEcm = recordFolder.getPropertyValue(ConstantesECM.RMC_X_DISPOSICION_FINAL_CARPETA);
                final String stateString = recordFolder.getPropertyValue(isPrimaryTransfer
                        ? ConstantesECM.RMC_X_ESTADO_TRANSFERENCIA : ConstantesECM.RMC_X_ESTADO_DISPOSICION);

                final Map<String, Object> propertyMapRecordFolder = new HashMap<>();
                final Map<String, Object> propertyMapRecordCategory = new HashMap<>();
                final Map<String, Object> propertyMapContent = new HashMap<>();

                StateType stateTypeDto = StateType.getStateBy(dto.getEstado());

                if (stateTypeDto != StateType.REJECTED) {
                    final String consecutiveKey = isPrimaryTransfer
                            ? ConstantesECM.RMC_X_CONSECUTIVO_TP_CATEGORIA : ConstantesECM.RMC_X_CONSECUTIVO_TS_CATEGORIA;
                    propertyMapRecordCategory.put(consecutiveKey, getConsecutive(dto.getConsecutivoTransferencia()));
                    propertyMapRecordFolder.put(ConstantesECM.RMC_X_CONSECUTIVO_CARPETA, dto.getConsecutivoTransferencia());
                }

                final StateType stateTypeRecord = StateType.getStateBy(stateString);

                if (isPrimaryTransfer) {
                    stateTypeDto = stateTypeDto == StateType.APPROVED && stateTypeRecord == StateType.LOCATED
                            ? StateType.LOCATED_APPROVED : stateTypeDto;
                    propertyMapContent.put(ConstantesECM.CMCOR_UD_ESTADO, stateTypeDto.getStateName());
                    propertyMapRecordFolder.put(ConstantesECM.RMC_X_ESTADO_TRANSFERENCIA, stateTypeDto.getStateName());
                    if (stateTypeDto == StateType.APPROVED) {
                        propertyMapContent.put(ConstantesECM.CMCOR_UD_FASE_ARCHIVO, PhaseType.AC.getPhaseName());
                        propertyMapRecordFolder.put(ConstantesECM.RMC_X_FASE_ARCHIVO, PhaseType.AC.getPhaseName());
                        propertyMapRecordFolder.put(ConstantesECM.RMC_X_DISPOSICION_HASTA_FECHA,
                                recordServices.getRetentionDateOf(recordFolder, PhaseType.AC));
                    }
                } else if (PhaseType.getPhaseTypeBy(phaseFileStringEcm) == PhaseType.AC) {

                    if (stateTypeDto == StateType.APPROVED) {
                        propertyMapContent.put(ConstantesECM.CMCOR_UD_FASE_ARCHIVO, PhaseType.AH.getPhaseName());
                        propertyMapRecordFolder.put(ConstantesECM.RMC_X_FASE_ARCHIVO, PhaseType.AH.getPhaseName());
                    } else {
                        propertyMapContent.put(ConstantesECM.CMCOR_UD_ESTADO, stateTypeDto.getStateName());
                        propertyMapRecordFolder.put(ConstantesECM.RMC_X_ESTADO_DISPOSICION, stateTypeDto.getStateName());
                    }
                }
                contentControl.getUDFolderById(idUd).ifPresent(contentFolder -> contentFolder.updateProperties(propertyMapContent));
                Response response = recordServices.modifyRecord(recordFolder.getId(), propertyMapRecordFolder, EcmRecordObjectType.RECORD_FOLDER);
                if (response.getStatus() != 200) {
                    throw ExceptionBuilder.newBuilder()
                            .withMessage(response.getStatus() + "", "Status Error " + response.getStatus())
                            .buildSystemException();
                }
                if (!propertyMapRecordCategory.isEmpty()) {
                    response = recordServices.modifyRecord(getBaseFolder.getId(), propertyMapRecordCategory, EcmRecordObjectType.RECORD_CATEGORY);
                    if (response.getStatus() != 200) {
                        throw ExceptionBuilder.newBuilder()
                                .withMessage(response.getStatus() + "", "Status Error " + response.getStatus())
                                .buildSystemException();
                    }
                }
            }
        }
        return MensajeRespuesta.newInstance()
                .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                .mensaje(ConstantesECM.OPERACION_COMPLETADA_SATISFACTORIAMENTE).build();
    }

    @Override
    public MensajeRespuesta listarUnidadesDocumentalesVerificar(String dependencyCode, String numTransfer) throws SystemException {
        try {
            if (StringUtils.isEmpty(dependencyCode) || StringUtils.containsIgnoreCase(dependencyCode.trim(), "null")) {
                throw new SystemException("No se procesa codigo de dependencia = '" + dependencyCode + "'");
            }
            if (StringUtils.isEmpty(numTransfer) || StringUtils.containsIgnoreCase(numTransfer.trim(), "null")) {
                throw new SystemException("No se procesa numero de transferencia = '" + numTransfer + "'");
            }
            final Folder depFolder = getDepFolder(dependencyCode.trim());
            final List<Folder> recordFolders = new ArrayList<>();
            findRecordFoldersACByTransferNumber(recordFolders, depFolder, numTransfer, StateType.getQueryApprovedRejected());
            final List<UnidadDocumentalDTO> documentalDTOList = transformUdFolders(recordFolders, numTransfer);
            final Map<String, Object> responseMap = new HashMap<>();

            responseMap.put("unidadesDocumentales", documentalDTOList);
            return MensajeRespuesta.newInstance()
                    .response(responseMap)
                    .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                    .mensaje(ConstantesECM.OPERACION_COMPLETADA_SATISFACTORIAMENTE).build();

        } catch (Exception ex) {
            log.error("An error has ocurred {}", ex.getMessage());
            throw new SystemException("An error has ocurred " + ex.getMessage());
        }
    }

    @Override
    public MensajeRespuesta confirmarUnidadesDocumentalesTransferidas(List<UnidadDocumentalDTO> unidadDocumentalDTOS) throws SystemException {
        recordServices.compruebaIdEstado(unidadDocumentalDTOS, true);
        for (UnidadDocumentalDTO documentalDTO : unidadDocumentalDTOS) {
            confirmarUnidadDocumental(documentalDTO);
        }
        return MensajeRespuesta.newInstance().
                codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                .mensaje(ConstantesECM.OPERACION_COMPLETADA_SATISFACTORIAMENTE)
                .build();
    }

    @Override
    public MensajeRespuesta listarUnidadesDocumentalesConfirmadas(String dependencyCode, String numTransfer) throws SystemException {
        try {
            if (StringUtils.isEmpty(dependencyCode) || StringUtils.containsIgnoreCase(dependencyCode.trim(), "null")) {
                throw new SystemException("No se procesa codigo de dependencia = '" + dependencyCode + "'");
            }
            if (StringUtils.isEmpty(numTransfer) || StringUtils.containsIgnoreCase(numTransfer.trim(), "null")) {
                throw new SystemException("No se procesa numero de transferencia = '" + numTransfer + "'");
            }
            final Folder depFolder = getDepFolder(dependencyCode.trim());
            final List<Folder> recordFolders = new ArrayList<>();
            findRecordFoldersACByTransferNumber(recordFolders, depFolder, numTransfer, StateType.APPROVED_CONFIRMED.getStateName());
            final List<UnidadDocumentalDTO> documentalDTOList = transformUdFolders(recordFolders, numTransfer);

            contentControl.formatoListaUnidadDocumental(documentalDTOList);
            final Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("unidadesDocumentales", documentalDTOList);
            return MensajeRespuesta.newInstance()
                    .response(responseMap)
                    .codMensaje(ConstantesECM.SUCCESS_COD_MENSAJE)
                    .mensaje(ConstantesECM.SUCCESS).build();

        } catch (Exception ex) {
            log.error("An error has ocurred {}", ex.getMessage());
            throw new SystemException("An error has ocurred " + ex.getMessage());
        }
    }

    private Folder getDepFolder(String depCode) throws SystemException {
        try {
            final Session session = contentControl.getSession();
            String queryPrincipal = "SELECT * FROM rmc:rmarecordCategoryCustomProperties" +
                    " WHERE " + ConstantesECM.RMC_X_SECCION + " = '" + depCode + "'" +
                    " AND " + PropertyIds.OBJECT_TYPE_ID + " = 'F:rma:recordCategory'" +
                    " AND (" + ConstantesECM.RMC_X_COD_SERIE + " IS NULL OR " + ConstantesECM.RMC_X_COD_SERIE + " = '')";

            final ItemIterable<QueryResult> queryResults = session.query(queryPrincipal, false);
            final Iterator<QueryResult> iterator = queryResults.iterator();

            if (iterator.hasNext()) {
                final QueryResult next = iterator.next();
                String objectId = next.getPropertyValueById(PropertyIds.OBJECT_ID);
                return (Folder) session.getObject(objectId);
            }
            throw new SystemException("La estructura no contiene el codigo de dependencia " + depCode);

        } catch (Exception ex) {
            log.error("An error has occurred {}", ex);
            throw new SystemException("An error has occurred " + ex.getMessage());
        }
    }

    private List<UnidadDocumentalDTO> transformUdFolders(List<Folder> folderList, String numTransfer) throws SystemException {
        final List<UnidadDocumentalDTO> documentalDTOList = new ArrayList<>();
        for (Folder folder : folderList) {
            final String idUD = folder.getPropertyValue(ConstantesECM.RMC_X_IDENTIFICADOR);
            final Optional<UnidadDocumentalDTO> udById = contentControl.getUDById(idUD);
            udById.ifPresent(unidadDocumentalDTO -> {
                unidadDocumentalDTO.setConsecutivoTransferencia(numTransfer);
                final StateType stateType = StateType.getStateBy(unidadDocumentalDTO.getEstado());
                if (stateType == StateType.APPROVED_CONFIRMED) {
                    unidadDocumentalDTO.setEstado(StateType.CONFIRMED.getStateName());
                }
                documentalDTOList.add(documentalDTOList.size(), unidadDocumentalDTO);
            });
        }
        return documentalDTOList;
    }

    private void findRecordFoldersTransfer(List<Folder> recordFolders, Folder depFolder, TransferType transferType) {
        final ItemIterable<CmisObject> children = depFolder.getChildren();
        for (CmisObject cmisObject : children) {
            if (cmisObject instanceof Folder) {
                if (cmisObject.getType().getId().endsWith("recordFolder")) {

                    final String xFaseArchivo = cmisObject.getPropertyValue(ConstantesECM.RMC_X_FASE_ARCHIVO);
                    final PhaseType phaseType = PhaseType.getPhaseTypeBy(xFaseArchivo);
                    if (null == phaseType)
                        continue;

                    final String xDispDateEcm = cmisObject.getPropertyValue(ConstantesECM.RMC_X_DISPOSICION_HASTA_FECHA);
                    final LocalDateTime xDispDateTimeEcm = Utilities.getDispDateTimeEcm(xDispDateEcm);

                    if (transferType == TransferType.PRIMARY && PhaseType.AG == phaseType) {
                        final String dispositionActionName = cmisObject.getPropertyValue("rma:recordSearchDispositionActionName");
                        if (!dispositionActionName.equalsIgnoreCase("retain") || Utilities.isEqualOrAfterDate(xDispDateTimeEcm)) {
                            recordFolders.add(recordFolders.size(), (Folder) cmisObject);
                        }
                    } else if (transferType == TransferType.SECONDARY && PhaseType.AC == phaseType) {
                        final String xDispFinalFolderEcm = cmisObject.getPropertyValue(ConstantesECM.RMC_X_DISPOSICION_FINAL_CARPETA);
                        final FinalDispositionType dispositionTypeEcm = FinalDispositionType.getDispositionBy(xDispFinalFolderEcm);
                        if (dispositionTypeEcm != FinalDispositionType.CT) {
                            continue;
                        }
                        final String xDispositionState = cmisObject.getPropertyValue(ConstantesECM.RMC_X_ESTADO_DISPOSICION);
                        final StateType stateType = StateType.getStateBy(xDispositionState);
                        final Calendar dispActionDate = cmisObject.getPropertyValue("rma:recordSearchDispositionActionAsOf");

                        if ((stateType == StateType.REJECTED || stateType == StateType.APPROVED)
                                && (Utilities.isEqualOrAfterDate(dispActionDate) || Utilities.isEqualOrAfterDate(xDispDateTimeEcm))) {
                            recordFolders.add(recordFolders.size(), (Folder) cmisObject);
                        }
                    }
                    continue;
                }
                findRecordFoldersTransfer(recordFolders, (Folder) cmisObject, transferType);
            }
        }
    }

    private void findRecordFoldersACByTransferNumber(List<Folder> recordFolders, Folder depFolder, String numTransfer, String transferStateParam) {
        final ItemIterable<CmisObject> children = depFolder.getChildren();
        for (CmisObject cmisObject : children) {
            if (cmisObject instanceof Folder) {
                if (cmisObject.getType().getId().endsWith("recordFolder")) {

                    final String transferState = cmisObject.getPropertyValue(ConstantesECM.RMC_X_ESTADO_TRANSFERENCIA);
                    final String phaseFile = cmisObject.getPropertyValue(ConstantesECM.RMC_X_FASE_ARCHIVO);
                    final String transferNumber = cmisObject.getPropertyValue(ConstantesECM.RMC_X_CONSECUTIVO_CARPETA);
                    final PhaseType phaseType = PhaseType.getPhaseTypeBy(phaseFile);

                    if (StringUtils.containsIgnoreCase(transferStateParam, transferState)
                            && phaseType == PhaseType.AC && StringUtils.equalsIgnoreCase(numTransfer, transferNumber)) {
                        recordFolders.add(recordFolders.size(), (Folder) cmisObject);
                        continue;
                    }
                }
                findRecordFoldersACByTransferNumber(recordFolders, (Folder) cmisObject, numTransfer, transferStateParam);
            }
        }
    }

    private void confirmarUnidadDocumental(UnidadDocumentalDTO dto) throws SystemException {
        final Optional<Folder> recordFolderByUdId = recordServices.getRecordFolderByUdId(dto.getId());
        if (recordFolderByUdId.isPresent()) {
            final Folder recordFolder = recordFolderByUdId.get();
            StateType stateType = StateType.getStateBy(dto.getEstado());

            if (stateType != StateType.CONFIRMED && stateType != StateType.REJECTED) {
                throw new SystemException("No se procesa el estado = " + stateType);
            }

            final Map<String, Object> propertyMapRecord = new HashMap<>();
            final Map<String, Object> propertyMapContent = new HashMap<>();

            if (stateType == StateType.CONFIRMED) {
                final String stateString = recordFolder.getPropertyValue(ConstantesECM.RMC_X_ESTADO_TRANSFERENCIA);
                stateType = StateType.getStateBy(stateString);
                stateType = stateType == StateType.APPROVED_CONFIRMED ?
                        StateType.LOCATED : StateType.APPROVED_CONFIRMED;
            }
            propertyMapRecord.put(ConstantesECM.RMC_X_ESTADO_TRANSFERENCIA, stateType.getStateName());
            propertyMapContent.put(ConstantesECM.CMCOR_UD_ESTADO, stateType.getStateName());

            contentControl.getUDFolderById(dto.getId()).ifPresent(contentFolder -> contentFolder.updateProperties(propertyMapContent));

            final Response response = recordServices.modifyRecord(recordFolder.getId(), propertyMapRecord, EcmRecordObjectType.RECORD_FOLDER);
            if (response.getStatus() != 200 && response.getStatus() != 201) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("Error Ocurred Status: " + response.getStatus() + ", Info: " + response.getStatusInfo())
                        .buildSystemException();
            }
        }
    }

    private String generarNumeroTransferencia(TransferType transferType, String dependencyCode) throws SystemException {
        final Folder baseFolder = recordStructure.getBaseFolder();
        final String consecutive = baseFolder.getPropertyValue(transferType == TransferType.SECONDARY
                ? ConstantesECM.RMC_X_CONSECUTIVO_TS_CATEGORIA : ConstantesECM.RMC_X_CONSECUTIVO_TP_CATEGORIA);
        final LocalDate localDate = LocalDate.now();
        return dependencyCode + "_" + localDate.getYear() + "_"
                + incrementarConsecutivo(StringUtils.isEmpty(consecutive) ? "0" : consecutive);
    }

    private String incrementarConsecutivo(String consecutivo) throws SystemException {
        try {
            return String.valueOf(Integer.parseInt(consecutivo) + 1);
        } catch (Exception e) {
            log.error("An error has occurred {}", e.getMessage());
            throw new SystemException("An error has occurred " + e.getMessage());
        }
    }

    private String getConsecutive(String fullConsecutive) throws SystemException {
        int index = fullConsecutive.lastIndexOf('_');
        if (index == -1) {
            throw new SystemException("El numero de trasnferencia esta mal conformado num = '" + fullConsecutive + "'");
        }
        return fullConsecutive.substring(index + 1);
    }
}
