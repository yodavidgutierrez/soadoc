package co.com.soaint.ecm.domain.entity;

import co.com.soaint.ecm.business.boundary.documentmanager.configuration.Utilities;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

@Getter
@Log4j2
public enum CommunicationType implements Serializable {

    PD("PRODUCCION_DOCUMENTAL", "PRODUCCION DOCUMENTAL", "PD"),
    PA("DOCUMENTOS POR ARCHIVAR", "DOCUMENTOS POR ARCHIVAR", "PA"),
    EE("CORRESPONDENCIA_ENTRADA", "COMUNICACION OFICIAL ENTRADA", "EN"),
    SE("CORRESPONDENCIA_SALIDA", "COMUNICACION OFICIAL SALIDA", "SA"),
    SI("CORRESPONDENCIA_INTERNA", "COMUNICACION OFICIAL INTERNA", "TR"),
    GN("CORRESPONDENCIA_GENERAL", "GENERAL", "GN");

    private static final Long serialVersionUID = 121L;

    private final String processName;
    private final String communicationName;
    private final String secondaryType;

    private String folderName = "";

    CommunicationType(String processName, String communicationName, String secondaryType) {
        this.processName = processName;
        this.communicationName = communicationName;
        this.secondaryType = secondaryType;
    }

    public static CommunicationType getSelectorBy(String typeName) {
        log.info("Searching for the selector of communication");
        if (StringUtils.isEmpty(typeName)) {
            return null;
        }
        final Stream<CommunicationType> stream = Arrays.stream(CommunicationType.values());
        final Optional<CommunicationType> optionalType = stream
                .filter(type -> StringUtils.containsIgnoreCase(typeName.trim(), type.name())
                        || type.getSecondaryType().equalsIgnoreCase(typeName.trim())
                        || StringUtils.equalsIgnoreCase(type.getCommunicationName(), Utilities.reemplazarCaracteresRaros(typeName.trim())))
                .findFirst();
        return optionalType.orElse(null);
    }

    public final String generateId(String depCode) {
        if (StringUtils.isEmpty(depCode)) {
            return name() + "-" + LocalDate.now().getYear();
        }
        return name() + "-" + depCode + "-" + LocalDate.now().getYear();
    }

    public final String getContains() {
        switch (this) {
            case EE:
                return "entrada";
            case SE:
                return "salida";
            case SI:
                return "interna";
            default:
                return null;
        }
    }

    public final void setNameFolder(String nameFolder) {
        final LocalDate localDate = LocalDate.now();
        this.folderName = (nameFolder + " " + localDate.getYear()).toUpperCase();
    }
}
