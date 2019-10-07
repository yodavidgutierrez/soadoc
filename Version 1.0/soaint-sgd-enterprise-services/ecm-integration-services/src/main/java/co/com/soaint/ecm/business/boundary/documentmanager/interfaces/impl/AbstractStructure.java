package co.com.soaint.ecm.business.boundary.documentmanager.interfaces.impl;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.Connection;
import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.StructureAlfresco;
import co.com.soaint.ecm.domain.entity.FinalDispositionType;
import co.com.soaint.foundation.canonical.ecm.*;
import co.com.soaint.foundation.canonical.ecm.util.StructureUtils;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Setter
abstract class AbstractStructure implements StructureAlfresco {

    private static final Long serialVersionUID = -5164634613054626L;
    final String DISP_VALUE = "disp-value";

    private Connection connection;

    @Override
    public MensajeRespuesta crearEstructura(List<EstructuraTrdDTO> structure) throws SystemException {

        resetAllEcmDependencies();
        StructureUtils.replaceUnsupportCharacters(structure);

        final Map<String, Folder> folderMap = new HashMap<>();

        for (EstructuraTrdDTO estructura : structure) {

            final List<OrganigramaDTO> organigramaList = estructura.getOrganigramaItemList();
            final List<ContenidoDependenciaTrdDTO> trdList = estructura.getContenidoDependenciaList();
            StructureUtils.ordenarListaOrganigrama(organigramaList);
            crearDependencias(organigramaList, folderMap);
            crearSeriesSubSeries(trdList, folderMap);
        }
        return MensajeRespuesta.newInstance()
                .codMensaje("0000")
                .mensaje("Estructura creada correctamente").build();
    }

    @Override
    public List<Folder> findAllUDListBy(UnidadDocumentalDTO dto) throws SystemException {
        if (null == dto) {
            log.error("No se ha especificado la Unidad Documental (null)");
            throw new SystemException("No se ha especificado la Unidad Documental (null)");
        }
        if (StringUtils.isEmpty(dto.getCodigoDependencia()) || "".equals(dto.getCodigoDependencia().trim())) {
            log.error("No se ha especificado el codigo de la dependencia Productora");
            throw new SystemException("No se ha especificado el codigo de la dependencia Productora");
        }
        return new ArrayList<>();
    }

    @Override
    public Session getSession() throws SystemException {
        Session session = connection.getSession();
        if (null == session) {
            log.error("Error!!, No existe conexion con el gestor de contenidos");
            throw new SystemException("No existe conexion con el gestor de contenidos");
        }
        return session;
    }

    abstract Folder crearRootCategory(Map<String, Object> propertyMap) throws SystemException;

    abstract Folder crearSerie(Folder folderFather, ContenidoDependenciaTrdDTO trdDTO) throws SystemException;

    abstract Folder crearSubSerie(Folder folderFather, ContenidoDependenciaTrdDTO trdDTO) throws SystemException;

    abstract Folder getSerieSubSerie(ContenidoDependenciaTrdDTO trdDTO, boolean esSerie) throws SystemException;

    abstract Folder getOrganigrama(OrganigramaDTO organigrama, boolean esUnidadBase) throws SystemException;

    abstract Folder crearNodo(Map<String, Object> propertyMap, Folder folderFather) throws SystemException;

    abstract boolean isSerieLeaf(Folder folder);

    abstract boolean validProperties(Folder object, UnidadDocumentalDTO dto);

    abstract void crearDependencias(List<OrganigramaDTO> organigramaList, Map<String, Folder> folderMap) throws SystemException;

    abstract void resetAllEcmDependencies() throws SystemException;

    boolean renameFolder(Folder folder, String newName) {
        final String folderName = folder.getName();
        final int index;
        final String namePrefix = ((index = folderName.indexOf('_')) != -1) ? folderName.substring(0, index) : "";
        if (newName.startsWith(namePrefix)) {
            if (!newName.equals(folderName)) {
                rename(folder, newName);
                return true;
            }
        } else if (!folderName.endsWith(newName)) {
            rename(folder, namePrefix + "_" + newName);
            return true;
        }
        return false;
    }

    Map<String, Object> getPropertyMap(Folder folderFather, ContenidoDependenciaTrdDTO trdDTO, boolean esSerie) throws SystemException {
        try {
            final int dispFinalInteger = trdDTO.getDiposicionFinal();
            final Map<String, Object> propertyMap = new HashMap<>();
            final FinalDispositionType type = FinalDispositionType.getDispositionBy(String.valueOf(dispFinalInteger));
            if (type == null) {
                log.error("No se ha especificado una disposision valida Value: " + dispFinalInteger);
                throw new SystemException("No se ha especificado una disposision valida Value: " + dispFinalInteger);
            }
            final String nameFolder = getSerieSubSerieName(trdDTO, esSerie);
            propertyMap.put(PropertyIds.NAME, nameFolder);
            propertyMap.put(DISP_VALUE, type);
            return propertyMap;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new SystemException(e.getMessage());
        }
    }

    final String getSerieSubSerieName(ContenidoDependenciaTrdDTO trdDTO, boolean esSerie) {

        final String idOrgOfc = trdDTO.getIdOrgOfc();
        String prefix = idOrgOfc + "." + trdDTO.getCodSerie();

        if (esSerie) {
            return trdDTO.getNomSerie().startsWith(idOrgOfc) ? trdDTO.getNomSerie() : prefix + "_" + trdDTO.getNomSerie();
        }

        final String codSubSerie = trdDTO.getCodSubSerie();
        prefix = prefix + "." + codSubSerie + "_";
        return trdDTO.getNomSubSerie().startsWith(idOrgOfc) ? trdDTO.getNomSubSerie() : prefix + trdDTO.getNomSubSerie();
    }

    void changeNode(Folder folder, OrganigramaDTO organigrama) throws SystemException {
        try {
            boolean renameFolder = renameFolder(folder, organigrama.getNomOrg());
            log.info(renameFolder ? "Renaming folder success ==> " + folder.getName() : "Renaming folder fail");
            System.out.println(renameFolder ? "Renaming folder success ==> " + folder.getName() : "Renaming folder fail");
        } catch (Exception ex) {
            log.error("An error has occurred {}", ex);
            throw new SystemException("An error has occurred " + ex.getMessage());
        }
    }

    void changedNode(Folder folder, ContenidoDependenciaTrdDTO trdDTO, boolean esSerie) throws SystemException {
        try {
            boolean renameFolder = renameFolder(folder, esSerie ? trdDTO.getNomSerie() : trdDTO.getNomSubSerie());
            log.info(renameFolder ? "Renaming folder success ==> " + folder.getName() : "Renaming folder fail");
        } catch (Exception ex) {
            log.error("An error has occurred {}", ex);
            throw new SystemException("An error has occurred " + ex.getMessage());
        }
    }

    final void rename(Folder folder, String newName) {
        final String folderName = folder.getName();
        final CmisObject cmisObject = folder.rename(newName);
        log.info("Renaming folder ----> {Name: {} === New Name: {} === Type: {} }", folderName , cmisObject.getName(), cmisObject.getType().getId());
        System.out.printf("Renaming folder ----> {Name: %s === New Name: %s === Type: %s }", folderName, cmisObject.getName(), cmisObject.getType().getId());
        System.out.println();
        cmisObject.refresh();
    }

    private void crearSeriesSubSeries(List<ContenidoDependenciaTrdDTO> trdList,
                                      Map<String, Folder> folderMap) throws SystemException {
        for (ContenidoDependenciaTrdDTO trdDTO : trdList) {
            if (!StringUtils.isEmpty(trdDTO.getCodSerie()) && folderMap.containsKey(trdDTO.getIdOrgOfc())) {
                crearSerieSubSerie(folderMap, trdDTO);
            }
        }
    }

    private void crearSerieSubSerie(Map<String, Folder> folderMap, ContenidoDependenciaTrdDTO trdDTO) throws SystemException {
        try {

            Folder serieFolder = getSerieSubSerie(trdDTO, true);

            if (serieFolder == null) {
                serieFolder = crearSerie(folderMap.get(trdDTO.getIdOrgOfc()), trdDTO);
            } else {
                changedNode(serieFolder, trdDTO, true);
            }

            if (!StringUtils.isEmpty(trdDTO.getCodSubSerie())) {
                final Folder subSerieFolder = getSerieSubSerie(trdDTO, false);
                if (null == subSerieFolder) {
                    Folder crearSubSerie = crearSubSerie(serieFolder, trdDTO);
                    log.info("Folder '{}' was created", crearSubSerie.getName());
                } else {
                    changedNode(subSerieFolder, trdDTO, false);
                }
            }
        } catch (Exception ex) {
            log.error("An error has occurred {}", ex);
            throw new SystemException("An error has occurred " + ex.getMessage());
        }
    }
}
