package co.com.soaint.foundation.canonical.ecm.util;

import co.com.soaint.foundation.canonical.ecm.ContenidoDependenciaTrdDTO;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.OrganigramaDTO;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StructureUtils implements Serializable {

    private static final long serialVersionUID = 1212L;

    private static final List<Character> unsupportCharacters = new ArrayList<>();

    static {
        unsupportCharacters.add(unsupportCharacters.size(), '*');
        unsupportCharacters.add(unsupportCharacters.size(), '"');
        unsupportCharacters.add(unsupportCharacters.size(), '<');
        unsupportCharacters.add(unsupportCharacters.size(), '>');
        unsupportCharacters.add(unsupportCharacters.size(), '\\');
        unsupportCharacters.add(unsupportCharacters.size(), '|');
        unsupportCharacters.add(unsupportCharacters.size(), ':');
        unsupportCharacters.add(unsupportCharacters.size(), '?');
        unsupportCharacters.add(unsupportCharacters.size(), '/');
    }

    private StructureUtils() {
    }

    /**
     * Metodo que dada una lista de entrada la organiza por ideOrgaAdmin
     *
     * @param organigramaItem Lista a ordenar
     */
    public static void ordenarListaOrganigrama(List<OrganigramaDTO> organigramaItem) {

        organigramaItem.sort(Comparator.comparing(OrganigramaDTO::getIdeOrgaAdmin));
    }

    public static void replaceUnsupportCharacters(List<EstructuraTrdDTO> structure) {

        final char replacement = '-';
        structure.parallelStream().forEach(estructuraTrdDTO -> {
            final List<OrganigramaDTO> organigramaItemList = estructuraTrdDTO.getOrganigramaItemList();
            ordenarListaOrganigrama(organigramaItemList);
            organigramaItemList.forEach(organigramaDTO -> {
                final String orgName = organigramaDTO.getNomOrg();
                unsupportCharacters.forEach(character -> {
                    if (orgName.indexOf(character) != 0) {
                        organigramaDTO.setNomOrg(orgName.replace(character, replacement));
                    }
                });
            });
            final List<ContenidoDependenciaTrdDTO> contenidoDependenciaList = estructuraTrdDTO.getContenidoDependenciaList();
            contenidoDependenciaList.forEach(contenidoDependenciaTrdDTO -> {
                final String sName = contenidoDependenciaTrdDTO.getNomSerie();
                final String ssName = contenidoDependenciaTrdDTO.getNomSubSerie();
                unsupportCharacters.forEach(character -> {
                    if (sName.indexOf(character) != 0) {
                        contenidoDependenciaTrdDTO.setNomSerie(sName.replace(character, replacement));
                    }
                    if (!StringUtils.isEmpty(ssName) && ssName.indexOf(character) != 0) {
                        contenidoDependenciaTrdDTO.setNomSubSerie(ssName.replace(character, replacement));
                    }
                });
            });
        });
    }
}
