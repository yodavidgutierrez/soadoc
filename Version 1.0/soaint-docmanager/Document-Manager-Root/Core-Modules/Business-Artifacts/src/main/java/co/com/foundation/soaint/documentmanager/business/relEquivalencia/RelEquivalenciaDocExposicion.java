package co.com.foundation.soaint.documentmanager.business.relEquivalencia;

import co.com.foundation.soaint.documentmanager.business.relEquivalencia.interfaces.RelEquivalenciaDocExposicionProxy;
import co.com.foundation.soaint.documentmanager.domain.CcdItemVO;
import co.com.foundation.soaint.documentmanager.domain.ItemVO;
import co.com.foundation.soaint.documentmanager.domain.OrganigramaItemVO;
import co.com.foundation.soaint.documentmanager.domain.RelEquiItemVO;
import co.com.foundation.soaint.documentmanager.infrastructure.util.EstadoOrganigramaEnum;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmCcd;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmRelEqOrigen;
import co.com.foundation.soaint.infrastructure.annotations.BusinessBoundary;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADMIN on 12/12/2016.
 */
@BusinessBoundary
public class RelEquivalenciaDocExposicion implements RelEquivalenciaDocExposicionProxy{

    @PersistenceContext
    private EntityManager em;

    public RelEquivalenciaDocExposicion() {
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<RelEquiItemVO> listUniAdminOrigen(String numVersionOrg) throws BusinessException, SystemException {

        List<RelEquiItemVO> listUnidadAdmin =   em.createNamedQuery("AdmRelEqOrigen.consultarUnidadAdministrativaExistByOrigen", RelEquiItemVO.class)
                .setParameter("NUM_VERSION_ORG", new BigInteger(numVersionOrg))
                .setHint("org.hibernate.cacheable", true)
                .getResultList();
        List<RelEquiItemVO> listUnidadAdminFInal = new ArrayList<>();
        for(RelEquiItemVO auxList: listUnidadAdmin){
            OrganigramaItemVO elementoRaiz = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarElementoRayzVersion", OrganigramaItemVO.class)
                    .setParameter("VAL_VERSION", auxList.getVersion())
                     .getSingleResult();
            RelEquiItemVO rel = new RelEquiItemVO();
            rel.setLabel(elementoRaiz.getNomOrg()+"/"+auxList.getLabel());
            rel.setValue(auxList.getValue());
            rel.setAux(auxList.getAux());
            rel.setVersion(auxList.getVersion());
            listUnidadAdminFInal.add(rel);
        }

        return listUnidadAdminFInal;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<RelEquiItemVO> listOfiProdByCodUniAdmOrigen(String codUniAdmin) throws BusinessException, SystemException {

        return em.createNamedQuery("AdmRelEqOrigen.consultarOficinaProdExistInOrigenByCodUniAmt", RelEquiItemVO.class)
                .setParameter("COD_UNI_ADMIN", codUniAdmin)
                .setHint("org.hibernate.cacheable", true)
                .getResultList();

    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<RelEquiItemVO> listSeriesInOrigenByCodUniAdmAndCodOfPro(String codUniAdmin, String codOfiProd) throws BusinessException, SystemException {

        return em.createNamedQuery("AdmRelEqOrigen.serieExistInOrigen", RelEquiItemVO.class)
                .setParameter("COD_UNI_ADMIN", codUniAdmin)
                .setParameter("COD_OFI_PROD", codOfiProd)
                .setHint("org.hibernate.cacheable", true)
                .getResultList();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<RelEquiItemVO> listSubSeriesInOrigenByCodUniAdmAndCodOfProAndIdSer(String codUniAdmin, String codOfiProd, String idSerie) throws BusinessException, SystemException {

        return em.createNamedQuery("AdmRelEqOrigen.subSerieExistInOrigenBySerie", RelEquiItemVO.class)
                .setParameter("COD_UNI_ADMIN", codUniAdmin)
                .setParameter("COD_OFI_PROD", codOfiProd)
                .setParameter("SERIE", idSerie)
                .setHint("org.hibernate.cacheable", true)
                .getResultList();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<RelEquiItemVO> listUniAdminDestino(String codUniAdmin, String codOfiProd, String codSerie, String codSubserie) throws BusinessException, SystemException {
        StringBuilder builderBor = new StringBuilder();
        builderBor.append("SELECT NEW co.com.foundation.soaint.documentmanager.domain.RelEquiItemVO (d.ideUniAmt, t.nomOrg, t.valVersion) FROM AdmRelEqOrigen o "
                + "INNER JOIN AdmRelEqDestino d ON d.admRelEqOrigen.ideRelOrigen = o.ideRelOrigen "
                + "INNER JOIN TvsOrganigramaAdministrativo t ON d.ideUniAmt = t.codOrg "
                + "INNER JOIN AdmSerie serie ON serie.ideSerie = o.admSerie ");
        if(!codSubserie.equals("0")){
            builderBor.append("INNER JOIN AdmSubserie subserie ON subserie.ideSubserie = o.admSubserie ");
        }
        builderBor.append("WHERE o.ideUniAmt ="+ codUniAdmin + " AND o.ideOfcProd = '"+codOfiProd+"' AND t.valVersion = 'TOP' " );
        if (!codSerie.equals("0")) {
            builderBor.append(" AND serie.codSerie = "+codSerie + " ");
        }
        if (!codSubserie.equals("0")) {
            builderBor.append(" AND subserie.codSubserie = "+codSubserie +" ");
        }
        builderBor.append("GROUP BY d.ideUniAmt, t.nomOrg, t.valVersion ");
        TypedQuery<RelEquiItemVO> query = em.createQuery(builderBor.toString(), RelEquiItemVO.class);
        List<RelEquiItemVO> listUnidadAdminDestinoFinal = new ArrayList<>();
        for(RelEquiItemVO listAux: query.getResultList()){
            OrganigramaItemVO elementoRaiz = em.createNamedQuery("TvsOrganigramaAdministrativo.consultarElementoRayzVersion", OrganigramaItemVO.class)
                    .setParameter("VAL_VERSION", listAux.getVersion())
                    .getSingleResult();
            RelEquiItemVO rel = new RelEquiItemVO();
            rel.setLabel(elementoRaiz.getNomOrg()+"/"+listAux.getLabel());
            rel.setVersion(listAux.getVersion());
            rel.setAux(listAux.getAux());
            rel.setValue(listAux.getValue());
            listUnidadAdminDestinoFinal.add(rel);
        }
        return listUnidadAdminDestinoFinal;

    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<RelEquiItemVO> listOfiProdByCodUniAdminDestino(String codUniAdminOr, String codOfiProdOr, String codSerie, String codSubserie, String codUniAdminDe) throws BusinessException, SystemException{
        StringBuilder builderBor = new StringBuilder();
        builderBor.append("SELECT NEW co.com.foundation.soaint.documentmanager.domain.RelEquiItemVO (d.ideOfcProd, t.nomOrg) FROM AdmRelEqOrigen o "
                + "INNER JOIN AdmRelEqDestino d ON d.admRelEqOrigen.ideRelOrigen = o.ideRelOrigen "
                + "INNER JOIN TvsOrganigramaAdministrativo t ON d.ideOfcProd = t.codOrg "
                + "INNER JOIN AdmSerie serie ON serie.ideSerie = o.admSerie ");
        if(!codSubserie.equals("0")){
            builderBor.append("INNER JOIN AdmSubserie subserie ON subserie.ideSubserie = o.admSubserie ");
        }
        builderBor.append("WHERE o.ideUniAmt ="+ codUniAdminOr + " AND o.ideOfcProd = '"+codOfiProdOr+"' ");
        if (!codSerie.equals("0")) {
            builderBor.append(" AND serie.codSerie = "+codSerie + " ");
        }
        if (!codSubserie.equals("0")) {
            builderBor.append(" AND subserie.codSubserie = "+codSubserie +" ");
        }
        if (!codUniAdminDe.equals("0")) {
            builderBor.append(" AND d.ideUniAmt = "+codUniAdminDe +" ");
        }
        builderBor.append("GROUP BY d.ideOfcProd, t.nomOrg");
        TypedQuery<RelEquiItemVO> query = em.createQuery(builderBor.toString(), RelEquiItemVO.class);
        return query.getResultList();

    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<RelEquiItemVO> listSeriesInDestinoByCodUniAdmAndCodOfPro(String codUniAdminDe, String codOfiProdOr, String codSerie, String codSubserie, String codOfiProdDe) throws BusinessException, SystemException{
        StringBuilder builderBor = new StringBuilder();
        builderBor.append("SELECT NEW co.com.foundation.soaint.documentmanager.domain.RelEquiItemVO (serie.codSerie, serie.nomSerie, serie.ideSerie) FROM AdmRelEqOrigen o "
                + "INNER JOIN AdmRelEqDestino d ON d.admRelEqOrigen.ideRelOrigen = o.ideRelOrigen "
                + "INNER JOIN AdmSerie serie ON serie.ideSerie = d.admSerie "
                + "WHERE o.ideUniAmt ="+ codUniAdminDe + " AND o.ideOfcProd = '"+codOfiProdOr+"'" );
        if (!codSerie.equals("0")) {
            builderBor.append(" AND o.admSerie = "+codSerie + " ");
        }
        if (!codSubserie.equals("0")) {
            builderBor.append(" AND o.admSubserie = "+codSubserie +" ");
        }
        if (!codOfiProdDe.equals("0")) {
            builderBor.append(" AND d.ideOfcProd = '"+codOfiProdDe+"'");
        }
        builderBor.append("GROUP BY serie.codSerie, serie.nomSerie, serie.ideSerie");
        TypedQuery<RelEquiItemVO> query = em.createQuery(builderBor.toString(), RelEquiItemVO.class);
        return query.getResultList();
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<RelEquiItemVO> listSubSeriesInDestinoByCodUniAdmAndCodOfProAndIdSer(String codUniAdminDe, String codOfiProdDe, String codSerie, String codSubserie, String serieDe) throws BusinessException, SystemException{
        StringBuilder builderBor = new StringBuilder();
        builderBor.append("SELECT NEW co.com.foundation.soaint.documentmanager.domain.RelEquiItemVO (subserie.codSubserie, subserie.nomSubserie) FROM AdmRelEqOrigen o "
                + "INNER JOIN AdmRelEqDestino d ON d.admRelEqOrigen.ideRelOrigen = o.ideRelOrigen "
                + "LEFT  OUTER JOIN AdmSubserie subserie ON subserie.ideSubserie = d.admSubserie "
                + "WHERE o.ideUniAmt ="+ codUniAdminDe + " AND o.ideOfcProd = '"+codOfiProdDe+"' " );
        if (!codSerie.equals("0")) {
            builderBor.append(" AND o.admSerie = "+codSerie + " ");
        }
        if (!codSubserie.equals("0")) {
            builderBor.append(" AND o.admSubserie = "+codSubserie +" ");
        }
        if (!serieDe.equals("0")) {
            builderBor.append(" AND d.admSerie = "+serieDe +" ");
        }
        builderBor.append("GROUP BY subserie.codSubserie, subserie.nomSubserie");
        TypedQuery<RelEquiItemVO> query = em.createQuery(builderBor.toString(), RelEquiItemVO.class);
        return query.getResultList();

    }


}
