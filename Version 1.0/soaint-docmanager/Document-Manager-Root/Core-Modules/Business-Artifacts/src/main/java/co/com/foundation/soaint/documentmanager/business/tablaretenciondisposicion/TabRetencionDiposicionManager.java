package co.com.foundation.soaint.documentmanager.business.tablaretenciondisposicion;

import co.com.foundation.soaint.documentmanager.business.tablaretenciondisposicion.interfaces.TabRetencionDiposicionManagerProxy;
import co.com.foundation.soaint.documentmanager.domain.DataTabRetDispVO;
import co.com.foundation.soaint.documentmanager.domain.DataTrdVO;
import co.com.foundation.soaint.documentmanager.domain.TRDTableVO;
import co.com.foundation.soaint.documentmanager.infrastructure.builder.common.DataTrdVOBuilder;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmTabRetDoc;
import co.com.foundation.soaint.infrastructure.annotations.BusinessBoundary;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.ExceptionBuilder;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static co.com.foundation.soaint.documentmanager.business.tablaretenciondisposicion.TabRetencionDiposicionControl.*;

/**
 * Created by jrodriguez on 10/11/2016.
 */
@BusinessBoundary
public class TabRetencionDiposicionManager implements TabRetencionDiposicionManagerProxy {

    private static Logger LOGGER = LogManager.getLogger(TabRetencionDiposicionManager.class.getName());

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private TabRetencionDiposicionControl control;

    public TabRetencionDiposicionManager() {
    }

    public void createTabRetencionDisposicion(AdmTabRetDoc admTabRetDoc) throws SystemException, BusinessException {
        try {
            em.persist(admTabRetDoc);
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public void updateTabRetencionDisposicion(AdmTabRetDoc admTabRetDoc) throws SystemException, BusinessException {
        try {
            em.merge(admTabRetDoc);
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public AdmTabRetDoc searchByUniAdminAndOfcProdAndIdSerieAndSubSerie(DataTabRetDispVO data) throws SystemException, BusinessException {
        try {
            if (data.getCodUniAmt() != null && data.getCodOfcProd() != null && data.getIdSerie() != null && data.getIdSubserie() != null) {
                return em.createNamedQuery("AdmTabRetDoc.searchByUniAdminAndOfcProdAndIdSerieAndSubSerie", AdmTabRetDoc.class)
                        .setParameter("ID_UNI_AMT", data.getCodUniAmt())
                        .setParameter("ID_OFC_PROD", data.getCodOfcProd())
                        .setParameter("ID_SERIE", data.getIdSerie())
                        .setParameter("ID_SUBSERIE", data.getIdSubserie())
                        .getSingleResult();
            } else {

                return em.createNamedQuery("AdmTabRetDoc.searchByUniAdminAndOfcProdAndIdSerie", AdmTabRetDoc.class)
                        .setParameter("ID_UNI_AMT", data.getCodUniAmt())
                        .setParameter("ID_OFC_PROD", data.getCodOfcProd())
                        .setParameter("ID_SERIE", data.getIdSerie())
                        .getSingleResult();

            }

        } catch (NonUniqueResultException n) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("retencion.documental.no_data")
                    .withRootException(n)
                    .buildBusinessException();


        } catch (NoResultException n) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("retencion.documental.no_data")
                    .withRootException(n)
                    .buildBusinessException();
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }

    }


        public TRDTableVO trdConfByOfcProdList(String idOfcProd) throws SystemException, BusinessException {

        try {
            List<Object[]> data = em.createNamedQuery("AdmTabRetDoc.generateTRDTable")
                    .setParameter("ID_OFC_PROD", idOfcProd)
                    .getResultList();

            List<DataTrdVO> table = new ArrayList<>();
            DataTrdVO current = null;
            DataTrdVOBuilder builder = null;
            boolean newSerie = true;
            String serie = "";
            boolean newSubSerie = false;
            String subSerie = "";
            String unidadAdministrativa = "";
            String oficinaProductora = "";
            boolean hayDatos = false;

            for (Object[] row : data) {

                hayDatos = true;
                unidadAdministrativa = row[NOMBRE_UNIDAD_ADMINISTRATIVA].toString();
                oficinaProductora = row[NOMBRE_OFICINA_PRODUCTORA].toString();

                newSerie = (!StringUtils.equals(serie, row[ID_SERIE].toString()));
                if (newSerie)
                    serie = row[ID_SERIE].toString();

                newSubSerie = (!StringUtils.equals(subSerie, String.valueOf(row[ID_SUBSERIE])));

                if (newSerie || newSubSerie) {
                    subSerie = String.valueOf(row[ID_SUBSERIE]);
                    builder = control.prepareTrdBuilder(row);
                    builder.withSoporte(!String.valueOf(row[ID_SUBSERIE]).equals("null") ? "<br/>" : "");
                    current = builder.build();
                    table.add(current);
                }

                if (!newSerie && !newSubSerie && !Objects.isNull(current))
                    current.addInstrumento("<br><span data-toggle=\"tooltip\" title=\" " + row[NOMBRE_TIPO_DOCUMENTAL].toString() + "\">".concat(row[NOMBRE_TIPO_DOCUMENTAL].toString()).concat("</span>"));
                    current.addSoporte("<br/><span data-toggle=\"tooltip\" title=\" " + row[SOPORTE].toString() + "\">".concat(row[SOPORTE].toString()).concat("</span>"));
            }

            return new TRDTableVO(unidadAdministrativa, oficinaProductora, table, hayDatos);

        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

}
