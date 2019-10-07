package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.TvsOrganigramaAdministrativo;
import co.com.soaint.foundation.canonical.correspondencia.OrganigramaAdministrativoDTO;
import co.com.soaint.foundation.canonical.correspondencia.OrganigramaItemDTO;
import co.com.soaint.foundation.canonical.ecm.EstructuraTrdDTO;
import co.com.soaint.foundation.canonical.ecm.OrganigramaDTO;
import co.com.soaint.foundation.canonical.ecm.util.StructureUtils;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.persistence.NoResultException;
import java.math.BigInteger;
import java.util.*;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 28-Jun-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
@Log4j2
public class OrganigramaAdministrativoControl extends GenericControl<TvsOrganigramaAdministrativo> {

    // [fields] -----------------------------------
    @Autowired
    private CorrespondenciaControl correspondenciaControl;

    @Autowired
    private FuncionariosControl funcionariosControl;

    @Autowired
    private TvsOrgaAdminXFunciPkControl adminXFunciPkControl;

    public OrganigramaAdministrativoControl() {
        super(TvsOrganigramaAdministrativo.class);
    }

    // ----------------------

    /**
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaItemDTO> listarDescendientesDirectosDeElementoRayz() throws SystemException {
        try {
            final List<OrganigramaItemDTO> itemDTOS = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarElementoRayz", OrganigramaItemDTO.class)
                    .getResultList();
            if (!itemDTOS.isEmpty()) {
                final OrganigramaItemDTO raiz = itemDTOS.get(0);
                return em.createNamedQuery("TvsOrganigramaAdministrativo.consultarDescendientesDirectos", OrganigramaItemDTO.class)
                        .setParameter("COD_ORG_PADRE", raiz.getCodOrg())
                        .setHint("org.hibernate.cacheable", true)
                        .getResultList();
            }
            return new ArrayList<>();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }


    /**
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<OrganigramaItemDTO> consultarOrganigrama() throws SystemException {
        try {
            final List<OrganigramaItemDTO> itemDTOS = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarElementoRayz", OrganigramaItemDTO.class)
                    .getResultList();
            if (!itemDTOS.isEmpty()) {
                OrganigramaItemDTO raiz = itemDTOS.get(0);
                final List<OrganigramaItemDTO> organigramaItemDTOList = listarElementosDeNivelInferior(new ArrayList<>(), raiz.getCodOrg());
                organigramaItemDTOList.add(organigramaItemDTOList.size(), raiz);
                return organigramaItemDTOList;
            }
            return new ArrayList<>();

        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param data
     * @param codOrganigramaPadre
     */
    public List<OrganigramaItemDTO> listarElementosDeNivelInferior(final List<OrganigramaItemDTO> data, String codOrganigramaPadre) {

        if (StringUtils.isEmpty(codOrganigramaPadre)) {
            return data;
        }

        log.info("********** codOrganigramaPadre ******* "+codOrganigramaPadre);
        List<OrganigramaItemDTO> organigramaItemDTOS = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarDescendientesDirectos", OrganigramaItemDTO.class)
                .setParameter("COD_ORG_PADRE", codOrganigramaPadre)
                .setHint("org.hibernate.cacheable", true)
                .getResultList();
        data.addAll(organigramaItemDTOS);

        for (OrganigramaItemDTO item : organigramaItemDTOS) {
            listarElementosDeNivelInferior(data, item.getCodOrg());
        }
        if (!data.isEmpty()) {
            data.sort(Comparator.comparing(OrganigramaItemDTO::getCodOrg));
        }
        return data;
    }

    public List<OrganigramaItemDTO> listarElementosCheck(final List<OrganigramaItemDTO> data, String codOrganigramaPadre) {

        if (StringUtils.isEmpty(codOrganigramaPadre)) {
            return data;
        }

        log.info("********** codOrganigramaPadre ******* "+codOrganigramaPadre);
        List<OrganigramaItemDTO> organigramaItemDTOS = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarDescendientesDirectos", OrganigramaItemDTO.class)
                .setParameter("COD_ORG_PADRE", codOrganigramaPadre)
                .setHint("org.hibernate.cacheable", true)
                .getResultList();
        data.addAll(organigramaItemDTOS);

        if (!data.isEmpty()) {
            data.sort(Comparator.comparing(OrganigramaItemDTO::getCodOrg));
        }
        return data;
    }

    /**
     * @param codOrgaAdmin
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public OrganigramaItemDTO listarPadreDeSegundoNivel(String codOrgaAdmin) throws BusinessException, SystemException {
        try {
            OrganigramaItemDTO organigramaItem = consultarPadreDeSegundoNivel(codOrgaAdmin);
            if (organigramaItem == null) {
                throw ExceptionBuilder.newBuilder()
                        .withMessage("organigrama.no_padre_segundo_nivel")
                        .buildBusinessException();
            }
            return organigramaItem;
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("organigrama.no_data")
                    .withRootException(n)
                    .buildBusinessException();
        } catch (BusinessException e) {
            throw e;
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public TvsOrganigramaAdministrativo consultarOrganigramaCodigo(String codigo){

        List<TvsOrganigramaAdministrativo> cod_org = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarOrganigramaCodigo", TvsOrganigramaAdministrativo.class)
                .setParameter("COD_ORG", codigo)
                .getResultList();

        if (ObjectUtils.isEmpty(cod_org)) {
            return new TvsOrganigramaAdministrativo();
        }
        return cod_org.get(0);
    }
    /**
     * @param codOrganigramaPadre
     * @return
     */
    OrganigramaItemDTO consultarPadreDeSegundoNivel(String codOrganigramaPadre) {
        log.info(codOrganigramaPadre);
        OrganigramaItemDTO organigramaItem;
        List<OrganigramaItemDTO> lista = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarDescendientesDirectos", OrganigramaItemDTO.class)
                .setParameter("COD_ORG_PADRE", codOrganigramaPadre)
                .getResultList();
        if (!ObjectUtils.isEmpty(lista)) {
            organigramaItem = lista.get(0);
        } else {
            return OrganigramaItemDTO.newInstance().build();
        }
        log.info(organigramaItem.getIdOrgaAdminPadre());
        boolean esPadreSegundoNivel = false;

        while (!esPadreSegundoNivel) {
            if (!StringUtils.isEmpty(organigramaItem.getCodigoOrganigramaPadre())) {
                OrganigramaItemDTO padre = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarElementoByCodOrgaAdmin", OrganigramaItemDTO.class)
                        .setParameter("COD_PADRE", organigramaItem.getCodigoOrganigramaPadre())
                        .setHint("org.hibernate.cacheable", true)
                        .getSingleResult();
                if (padre.getIdOrgaAdminPadre() == null) {
                    esPadreSegundoNivel = true;
                } else {
                    organigramaItem = padre;
                }
            } else {
                esPadreSegundoNivel = true;
            }
        }
        return organigramaItem;
    }

    /**
     * @param codOrg
     * @return
     */
    OrganigramaItemDTO consultarElementoByCodOrg(String codOrg) {
        final List<OrganigramaItemDTO> resultList = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarElementoByCodOrg", OrganigramaItemDTO.class)
                .setParameter("COD_ORG", codOrg).getResultList();
        return !resultList.isEmpty() ? resultList.get(0) : OrganigramaItemDTO.newInstance().build();
    }

    /**
     * @param codOrg
     * @return
     */
    String consultarNombreElementoByCodOrg(String codOrg) {
        if (!StringUtils.isEmpty(codOrg)) {
            try {
                List<String> resultList = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarNombreElementoByCodOrg", String.class)
                        .setParameter("COD_ORG", codOrg)
                        .getResultList();
                return !resultList.isEmpty() ? resultList.get(0) : null;
            } catch (Exception e) {
                log.error("Business Control - a system error has occurred {}", e);
            }
        }
        return null;
    }

    /**
     * @param codigosOrg
     * @return
     */
    List<OrganigramaItemDTO> consultarElementosByCodOrg(String[] codigosOrg) {
        return em.createNamedQuery("TvsOrganigramaAdministrativo.consultarElementosByCodigosOrg", OrganigramaItemDTO.class)
                .setParameter("CODIGOS_ORG", Arrays.asList(codigosOrg))
                .getResultList();
    }

    @Transactional
    public boolean insertarOrganigrama(OrganigramaAdministrativoDTO organigramaAdministrativoDTO) {
        try {
            if (!ObjectUtils.isEmpty(organigramaAdministrativoDTO.getOrganigrama()) && organigramaAdministrativoDTO.getOrganigrama().size() > 1) {
                List<OrganigramaItemDTO> organigramas = organigramaAdministrativoDTO.getOrganigrama();
                clearDbForNewOrganigrama();
                for (OrganigramaItemDTO organigrama : organigramas) {
                    if (!StringUtils.isEmpty(organigrama.getCodOrg()) && !StringUtils.isEmpty(organigrama.getNomOrg()) && !StringUtils.isEmpty(organigrama.getDescOrg())
                            && null != organigrama.getIdOrgaAdminPadre() && !StringUtils.isEmpty(organigrama.getNivel())) {
                        TvsOrganigramaAdministrativo organigramaAdministrativo = TvsOrganigramaAdministrativo.newInstance()
                                .codOrg(StringUtils.isEmpty(organigrama.getCodOrg()) ? null : organigrama.getCodOrg().trim())
                                .nomOrg(StringUtils.isEmpty(organigrama.getNomOrg()) ? null : organigrama.getNomOrg().trim())
                                .descOrg(StringUtils.isEmpty(organigrama.getDescOrg()) ? null : organigrama.getDescOrg().trim())
                                .indEsActivo("1")
                                .ideOrgaAdminPadre(organigrama.getIdOrgaAdminPadre())
                                .codNivel(organigrama.getNivel())
                                .fecCreacion(new Date())
                                .ideUsuarioCambio(new BigInteger("0"))
                                .nivelEscritura(0)
                                .nivelLectura(0)
                                .valVersion("TOP")
                                .codOrganigramaPadre(StringUtils.isEmpty(organigrama.getCodigoOrganigramaPadre()) ? null : organigrama.getCodigoOrganigramaPadre().trim())
                                .build();
                        em.persist(organigramaAdministrativo);
                    }
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    @Transactional
    public boolean generarEstructuraDatabase(List<EstructuraTrdDTO> structure) throws SystemException {
        StructureUtils.replaceUnsupportCharacters(structure);
        final String codOrgAdmin = getCodOrgaAdmin(structure);
        final List<OrganigramaItemDTO> resultList = em
                .createNamedQuery("TvsOrganigramaAdministrativo.consultarElementoRayz", OrganigramaItemDTO.class).getResultList();
        if (!resultList.isEmpty()) {
            final OrganigramaItemDTO organigramaItemDTO = resultList.get(0);
            if (!codOrgAdmin.equalsIgnoreCase(organigramaItemDTO.getCodOrg())) {
                removeAllEntities();
            } else {
                inactiveAll();
            }
        }
        final Map<String, TvsOrganigramaAdministrativo> orgMap = new HashMap<>();
        BigInteger ideOrgaAdminPadre = BigInteger.ZERO;
        for (EstructuraTrdDTO estructura : structure) {
            List<OrganigramaDTO> organigramaList = estructura.getOrganigramaItemList();
            StructureUtils.ordenarListaOrganigrama(organigramaList);
            crearDependencias(organigramaList, orgMap, ideOrgaAdminPadre = ideOrgaAdminPadre.add(BigInteger.ONE));
        }
        em.flush();
        return true;
    }

    private String getCodOrgaAdmin(List<EstructuraTrdDTO> structure) throws SystemException {
        String codOrgAdmin = null;
        for (EstructuraTrdDTO aStructure : structure) {
            final List<OrganigramaDTO> organigramaList = aStructure.getOrganigramaItemList();
            for (final OrganigramaDTO organigramaDTO : organigramaList) {
                final String orgType = organigramaDTO.getTipo();
                final String codOrg = organigramaDTO.getCodOrg();
                if ("P".equalsIgnoreCase(orgType)) {
                    if (codOrgAdmin == null) {
                        codOrgAdmin = codOrg;
                        continue;
                    }
                    if (!codOrgAdmin.equals(codOrg)) {
                        throw new SystemException("La estructura contiene distintos codigos del Elemento raiz");
                    }
                }
            }
        }
        if (null == codOrgAdmin) {
            throw new SystemException("La estructura no contiene codigo para el elemento raiz");
        }
        return codOrgAdmin;
    }

    private void clearDbForNewOrganigrama() throws SystemException {
        log.info("Start deleting Organigrama administrativo");
        removeAllEntities();
        log.info("End deleting Organigrama administrativo");
        log.info("Start deleting Organigrama CorCorrespondencia");
        correspondenciaControl.removeAllEntities();
        log.info("End deleting Organigrama CorCorrespondencia");
        log.info("Start deleting Organigrama Funcionarion");
        funcionariosControl.removeAllEntities();
        log.info("End deleting Organigrama Funcionarios");
        log.info("Start deleting Organigrama AdminXFunci");
        adminXFunciPkControl.removeAllEntities();
        log.info("End deleting Organigrama AdminXFunci");
    }

    private void crearDependencias(List<OrganigramaDTO> organigramaList, Map<String, TvsOrganigramaAdministrativo> orgMap, BigInteger ideOrgaAdminPadre) throws SystemException {
        try {
            TvsOrganigramaAdministrativo currentOrganigramaPadre = null;

            for (OrganigramaDTO organigrama : organigramaList) {
                final String codOrg = StringUtils.isEmpty(organigrama.getCodOrg()) ? "" : organigrama.getCodOrg().trim();
                final String nomOrg = StringUtils.isEmpty(organigrama.getNomOrg()) ? "" : organigrama.getNomOrg().trim();
                if ("".equals(nomOrg)) {
                    throw new SystemException("El Nombre del Organigrama " + organigrama + " es nulo");
                }
                if ("".equals(codOrg)) {
                    throw new SystemException("El Codigo del Organigrama " + organigrama + " es nulo");
                }
                if (!orgMap.containsKey(codOrg)) {
                    TvsOrganigramaAdministrativo organigramaDb = getOrganigramaByCode(organigrama.getCodOrg());
                    if (null == organigramaDb) {
                        final String nameOrg = StringUtils.isEmpty(organigrama.getNomOrg()) ? "" : organigrama.getNomOrg().trim();
                        final String codOrgPadre = null == currentOrganigramaPadre ? null : currentOrganigramaPadre.getCodOrg().trim();
                        final String codNivel = null == currentOrganigramaPadre
                                ? "0" : String.valueOf((Integer.parseInt(currentOrganigramaPadre.getCodNivel()) + 1));
                        organigramaDb = TvsOrganigramaAdministrativo.newInstance()
                                .codOrg(codOrg)
                                .nomOrg(nameOrg)
                                .descOrg(nameOrg)
                                .indEsActivo("1")
                                .ideOrgaAdminPadre(ideOrgaAdminPadre)
                                .codNivel(codNivel)
                                .fecCreacion(GregorianCalendar.getInstance().getTime())
                                .ideUsuarioCambio(BigInteger.ZERO)
                                .nivelEscritura(0)
                                .nivelLectura(0)
                                .valVersion("TOP")
                                .codOrganigramaPadre(codOrgPadre)
                                .abreviatura(organigrama.getAbreviatura())
                                .radicadora(organigrama.isRadicadora())
                                .build();
                    } else {
                        organigramaDb.setCodOrg(organigrama.getCodOrg());
                        organigramaDb.setNomOrg(organigrama.getNomOrg());
                        organigramaDb.setDescOrg(organigrama.getNomOrg());
                        organigramaDb.setAbreviatura(organigrama.getAbreviatura());
                        organigramaDb.setRadicadora(organigrama.isRadicadora());
                        organigramaDb.setIdeOrgaAdmin(organigramaDb.getIdeOrgaAdmin());
                        organigramaDb.setOrgActivo(true);
                    }
                    orgMap.put(codOrg, em.merge(organigramaDb));
                }
                currentOrganigramaPadre = orgMap.get(codOrg);
            }
        } catch (Exception ex) {
            log.error("Error ocurrido al generar el organigrama en la base de datos {}", ex.getMessage());
            throw new SystemException("Error ocurrido al generar el organigrama en la base de datos " + ex.getMessage());
        }
    }

    private TvsOrganigramaAdministrativo getOrganigramaByCode(final String codOrg) {
        final List<TvsOrganigramaAdministrativo> itemDTOS = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarByOrgCode", TvsOrganigramaAdministrativo.class)
                .setParameter("COD_ORG", codOrg)
                .getResultList();
        return itemDTOS.isEmpty() ? null : itemDTOS.get(0);
    }

    public List<OrganigramaItemDTO> listarDescendientesPadres() throws SystemException {
        try {
            return em.createNamedQuery("TvsOrganigramaAdministrativo.consultarDescendientesPadres", OrganigramaItemDTO.class)
                    .getResultList();
        } catch (Exception ex) {
            log.error("Error occurred {}", ex);
            throw new SystemException("Error occurred " + ex);
        }
    }

    private void inactiveAll() {
        final int executeUpdate = em.createNamedQuery("TvsOrganigramaAdministrativo.inactiveAllOrg")
                .executeUpdate();
        log.info("Inactivating All Org --> Rows affected {}", executeUpdate);
    }
}
