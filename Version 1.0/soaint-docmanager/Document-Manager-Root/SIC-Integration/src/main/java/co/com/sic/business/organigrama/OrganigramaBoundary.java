package co.com.sic.business.organigrama;

import co.com.foundation.soaint.documentmanager.integration.DependenciaINT;
import co.com.foundation.soaint.documentmanager.integration.OrganigramaINT;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrganigramaBoundary {
    private static final Logger LOGGER = LogManager.getLogger(OrganigramaBoundary.class);

    @Autowired
    private OrganigramaControl organigramaControl;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public OrganigramaINT findById(Long idOrg) throws SystemException {
        LOGGER.info("OrganigramaBoundary. Consultando organigrama por id: {}", idOrg);
        return organigramaControl.findById(idOrg);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public OrganigramaINT findByCodigo(String codOrg) throws SystemException {
        LOGGER.info("OrganigramaBoundary. Consultando organigrama por id: {}", codOrg);
        return organigramaControl.findByCodigo(codOrg);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<DependenciaINT> listDependencias() throws SystemException {
        LOGGER.info("OrganigramaBoundary. Listar todas las dependencias.");
        List<DependenciaINT> lista = organigramaControl.listDependencias();

        for (DependenciaINT dependenciaINT :lista) {
            dependenciaINT.setPadres(obtenerJerarquiaPorNivel(dependenciaINT.getIdPadre(), 1, new ArrayList<>()));
        }

        return lista;
    }

    private List<DependenciaINT> obtenerJerarquiaPorNivel(Long id, int nivel, List<DependenciaINT> lista) throws SystemException{
        OrganigramaINT organigramaINT = organigramaControl.findById(id);
        if (organigramaINT.getCodNivel() >= nivel){
            lista.add(DependenciaINT.builder()
                    .codigo(organigramaINT.getCodOrg())
                    .estado(organigramaINT.getIndEsActivo())
                    .id(organigramaINT.getIdeOrgaAdmin())
                    .idPadre(organigramaINT.getIdeOrgaAdminPadre())
                    .nivel(organigramaINT.getCodNivel())
                    .nombre(organigramaINT.getNomOrg())
                    .build());

            lista = obtenerJerarquiaPorNivel(organigramaINT.getIdeOrgaAdminPadre(), nivel, lista);
        }

        return lista;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaINT> getOrganigramasbyIdPadre(Long idOrg) throws SystemException {
        LOGGER.info("OrganigramaBoundary. Listando organigramas por id padre: {}", idOrg);
        return organigramaControl.getOrganigramasbyIdPadre(idOrg);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaINT> getOrganigramasbyCodPadre(String codOrg) throws SystemException {
        LOGGER.info("OrganigramaBoundary. Listando organigramas por cod padre: {}", codOrg);
        return organigramaControl.getOrganigramasbyCod(codOrg);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public OrganigramaINT obtenerArbolOrganigrama() throws SystemException {
        LOGGER.info("OrganigramaBoundary. Obtener arbol de organigrama.");
        OrganigramaINT organigramaINT = organigramaControl.obtenerPrimerNivel();
        organigramaINT.setHijos(consultarOrganigramaRecursivo(organigramaINT.getIdeOrgaAdmin()));
        return organigramaINT;
    }

    private List<OrganigramaINT> consultarOrganigramaRecursivo (Long idOrg) throws SystemException {
        List<OrganigramaINT> lista = organigramaControl.getOrganigramasbyIdPadre(idOrg);

        for (OrganigramaINT organigramaINT:lista) {
            organigramaINT.setHijos(consultarOrganigramaRecursivo(organigramaINT.getIdeOrgaAdmin()));
        }

        return lista;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaINT> listOrganigramaJerarquicoPorPadre(Long id, Long idOrgExc) throws SystemException {
        LOGGER.info("OrganigramaBoundary. Listar organigramas de forma jerarquica dado padre: {}", id);
        OrganigramaINT organigramaINT = organigramaControl.findById(id);

        List<OrganigramaINT> lista = new ArrayList<>();
        if (!id.equals(idOrgExc)) {
            lista.add(organigramaINT);
        }
        lista.addAll(organigramaJerarquicoPorPadre(id, idOrgExc));

        return lista;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaINT> listOrganigramaJerarquicoPorCod(String cod, String codOrgExc) throws SystemException {
        LOGGER.info("OrganigramaBoundary. Listar organigramas de forma jerarquica dado padre: {}", cod);
        OrganigramaINT organigramaINT = organigramaControl.findByCodigo(cod);

        List<OrganigramaINT> lista = new ArrayList<>();
        if (!cod.equals(codOrgExc)) {
            lista.add(organigramaINT);
        }
        lista.addAll(organigramaJerarquicoPorCod(cod, codOrgExc));

        return lista;
    }

    private List<OrganigramaINT> organigramaJerarquicoPorPadre(Long id, Long idOrgExc) throws SystemException{

        List<OrganigramaINT> lista = organigramaControl.getOrganigramasbyIdPadre(id);
        List<OrganigramaINT> hijos = new ArrayList<>();
        OrganigramaINT organigramaINT = null;
        for (OrganigramaINT org:lista) {
            if (org.getIdeOrgaAdmin().equals(idOrgExc)){
                organigramaINT = org;
            }
            hijos.addAll(organigramaJerarquicoPorPadre(org.getIdeOrgaAdmin(), idOrgExc));
        }
        if (organigramaINT != null)
        {
            lista.remove(organigramaINT);
        }
        lista.addAll(hijos);
        return lista;
    }

    private List<OrganigramaINT> organigramaJerarquicoPorCod(String cod, String codOrgExc) throws SystemException{

        List<OrganigramaINT> lista = organigramaControl.getOrganigramasbyCod(cod);
        List<OrganigramaINT> hijos = new ArrayList<>();
        OrganigramaINT organigramaINT = null;
        for (OrganigramaINT org:lista) {
            if (org.getCodOrg().equals(codOrgExc)){
                organigramaINT = org;
            }
            hijos.addAll(organigramaJerarquicoPorCod(org.getCodOrg(), codOrgExc));
        }
        if (organigramaINT != null)
        {
            lista.remove(organigramaINT);
        }
        lista.addAll(hijos);
        return lista;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public OrganigramaINT obtenerPadre() throws SystemException {
        LOGGER.info("OrganigramaBoundary. Obtener organigrama principal.");
        return organigramaControl.obtenerPrimerNivel();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaINT> listarCodHijos() throws SystemException {
        LOGGER.info("OrganigramaBoundary. Obtener organigrama principal.");
        OrganigramaINT org = organigramaControl.obtenerPrimerNivel();
        return organigramaControl.obtenerHijos(org.getIdeOrgaAdmin());
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public OrganigramaINT obtenerPadrePorCodHijo(String codOrg) throws SystemException {
        LOGGER.info("OrganigramaBoundary. Consultar padre por codigo de organigrama.");
        return organigramaControl.obtenerPadrePorCodHijo(codOrg);
    }
}
