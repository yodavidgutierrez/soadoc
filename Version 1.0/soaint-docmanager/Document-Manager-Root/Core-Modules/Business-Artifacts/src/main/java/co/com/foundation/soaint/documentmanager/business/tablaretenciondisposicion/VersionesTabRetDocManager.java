package co.com.foundation.soaint.documentmanager.business.tablaretenciondisposicion;

import co.com.foundation.soaint.documentmanager.business.series.interfaces.SeriesManagerProxy;
import co.com.foundation.soaint.documentmanager.business.subserie.interfaces.SubserieManagerProxy;
import co.com.foundation.soaint.documentmanager.business.tablaretenciondisposicion.interfaces.VersionesTabRetDocManagerProxy;
import co.com.foundation.soaint.documentmanager.domain.*;
import co.com.foundation.soaint.documentmanager.infrastructure.builder.common.DataTableTrdVOBuilder;
import co.com.foundation.soaint.documentmanager.infrastructure.builder.common.DataTrdVOBuilder;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmSerie;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmSubserie;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmVersionTrd;
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
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static co.com.foundation.soaint.documentmanager.business.tablaretenciondisposicion.TabRetencionDiposicionControl.*;

/**
 * Created by jrodriguez on 24/11/2016.
 */

@BusinessBoundary
public class VersionesTabRetDocManager implements VersionesTabRetDocManagerProxy {

    // [fields] -----------------------------------
    private static Logger LOGGER = LogManager.getLogger(VersionesTabRetDocManager.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private VesionesTablaRetencionDocControl control;

    @Autowired
    private TabRetencionDiposicionControl controlTrd;

    @Autowired
    private SeriesManagerProxy seriesManager;

    @Autowired
    private SubserieManagerProxy subserieManager;


    public VersionesTabRetDocManager() {
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public synchronized void publicarTablaRetencionDocumental(String idUniAmd, String idOfcProd, String nombreComite,
                                                              String numActa, String fechaActa) throws SystemException {

        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            BigInteger version = control.consultarVersionActualDeTrdPorOfcProd(idUniAmd, idOfcProd);
            if (version.intValue() > 0) {
                control.updateVersionTrd(idUniAmd, idOfcProd, version, "V." + version, nombreComite, numActa, formatoFecha.parse(fechaActa));
                version = version.add(BigInteger.ONE);
            } else {
                version = BigInteger.ONE;
            }
            AdmVersionTrd admVersionTrd = control.publicarVersionTRD(idUniAmd, idOfcProd, version, nombreComite, numActa, formatoFecha.parse(fechaActa));

            for (DataVersionTrdVO data : control.dataVersionTrd(idUniAmd, idOfcProd)) {

                AdmSerie serie = seriesManager.searchSerieById(data.getIdeSerie());

                AdmSubserie subserie = new AdmSubserie();

                if(data.getIdeSubserie()!=null) {
                    subserie = subserieManager.searchSubserieById(data.getIdeSubserie());
                }


                control.createTabRetDocOrg(data, admVersionTrd.getIdeVersion(), serie, subserie);
            }
        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public List<AdmVersionTrd> findAllVersionPorOfcProd(String idOfcProd) throws SystemException, BusinessException {
        try {
            return em.createNamedQuery("AdmVersionTrd.findAllVersionPorOfcProd", AdmVersionTrd.class)
                    .setParameter("ID_OFC_PROD", idOfcProd)
                    .getResultList();
        } catch (NoResultException n) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("trd.versionamiento.no_data")
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


    public AdmVersionTrd consulVersionOfcProdTop(String idOfcProd) throws SystemException, BusinessException {
        try {
            return em.createNamedQuery("AdmVersionTrd.consulVersionOfcProdTop", AdmVersionTrd.class)
                    .setParameter("ID_OFC_PROD", idOfcProd)
                    .getSingleResult();
        } catch (NoResultException n) {
            throw ExceptionBuilder.newBuilder()
                    .withMessage("trd.versionamiento.no_data")
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

    public TRDTableVO versionTrdByOfcProdList(String idOfcProd, BigInteger idVersion, String valVersionOrg) throws SystemException, BusinessException {

        try {
            List<Object[]> data = em.createNamedQuery("AdmTabRetDocOrg.versionTRDTable")
                    .setParameter("VAL_VERSION", idVersion)
                    .setParameter("ID_OFC_PROD", idOfcProd)
                    .setParameter("VAL_VERSION_ORG", valVersionOrg)
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
            String nomComite = "";
            String numActa = "";
            Date fechaActa = null;
            boolean hayDatos = false;

            for (Object[] row : data) {

                hayDatos = true;
                unidadAdministrativa = row[NOMBRE_UNIDAD_ADMINISTRATIVA].toString();
                oficinaProductora = row[NOMBRE_OFICINA_PRODUCTORA].toString();
                nomComite = row[NOMBRE_COMITE].toString();
                numActa = row[NUM_ACTA].toString();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                fechaActa = formatter.parse(row[FECHA_ACTA].toString());

                newSerie = (!StringUtils.equals(serie, row[ID_SERIE].toString()));
                if (newSerie)
                    serie = row[ID_SERIE].toString();

                newSubSerie = (!StringUtils.equals(subSerie, String.valueOf(row[ID_SUBSERIE])));

                if (newSerie || newSubSerie) {
                    subSerie = String.valueOf(row[ID_SUBSERIE]);
                    builder = controlTrd.prepareTrdBuilder(row);
                    builder.withSoporte(!String.valueOf(row[ID_SUBSERIE]).equals("null") ? "<br/>" : "");
                    current = builder.build();
                    table.add(current);
                }

                if (!newSerie && !newSubSerie && !Objects.isNull(current))
                    current.addInstrumento("<br><span data-toggle=\"tooltip\" title=\" " + row[NOMBRE_TIPO_DOCUMENTAL].toString() + "\">".concat(row[NOMBRE_TIPO_DOCUMENTAL].toString()).concat("</span>"));
                    current.addSoporte("<br/><span data-toggle=\"tooltip\" title=\" " + row[SOPORTE].toString() + "\">".concat(row[SOPORTE].toString()).concat("</span>"));
            }

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            return new TRDTableVO(unidadAdministrativa, oficinaProductora, nomComite.toUpperCase(), numActa, formatter.format(fechaActa), table, hayDatos);

        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    public DataReportTrdVO dataTrdByOfcProdList(String idOfcProd, BigInteger idVersion,String valVersionOrg) throws SystemException, BusinessException {

        try {
            AdmVersionTrd admVersionTrd = em.createNamedQuery("AdmVersionTrd.consultVersionPorId", AdmVersionTrd.class)
                    .setParameter("ID_VERSION", idVersion)
                    .getSingleResult();

            List<Object[]> data = em.createNamedQuery("AdmTabRetDocOrg.versionTRDTable")
                    .setParameter("VAL_VERSION", idVersion)
                    .setParameter("ID_OFC_PROD", idOfcProd)
                    .setParameter("VAL_VERSION_ORG", valVersionOrg)
                    .getResultList();

            ArrayList<DataTableTrdVO> table = new ArrayList<>();
            DataTableTrdVO current = null;
            DataTableTrdVOBuilder builder = null;
            boolean newSerie = true;
            String serie = "";
            boolean newSubSerie = false;
            String subSerie = "";
            String unidadAdministrativa = "";
            String oficinaProductora = "";
            String nomComite = "";
            String numActa = "";
            Date fechaActa = null;
            boolean hayDatos = false;

            for (Object[] row : data) {

                hayDatos = true;
                unidadAdministrativa = row[NOMBRE_UNIDAD_ADMINISTRATIVA].toString();
                oficinaProductora = row[NOMBRE_OFICINA_PRODUCTORA].toString();
                nomComite = row[NOMBRE_COMITE].toString().toUpperCase();
                numActa = row[NUM_ACTA].toString();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                fechaActa = formatter.parse(row[FECHA_ACTA].toString());

                newSerie = (!StringUtils.equals(serie, row[ID_SERIE].toString()));
                if (newSerie)
                    serie = row[ID_SERIE].toString();

                newSubSerie = (!StringUtils.equals(subSerie, String.valueOf(row[ID_SUBSERIE])));

                if (newSerie || newSubSerie) {
                    subSerie = String.valueOf(row[ID_SUBSERIE]);
                    builder = controlTrd.prepareTrdTableBuilder(row);
                    builder.withSoporte(!String.valueOf(row[ID_SUBSERIE]).equals("null") ? "\n" : "");
                    current = builder.build();
                    table.add(current);
                }

                if (!newSerie && !newSubSerie && !Objects.isNull(current))
                    current.addInstrumento("\n-".concat(row[NOMBRE_TIPO_DOCUMENTAL].toString()));
                    current.addSoporte("\n-".concat(row[SOPORTE].toString()));
            }

            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy h:mm a");

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            return ((admVersionTrd == null || CollectionUtils.isEmpty(data)) ? new DataReportTrdVO() : new DataReportTrdVO(formatoFecha.format(admVersionTrd.getFecVersion()),
                    "V.".concat(admVersionTrd.getNumVersion().toString()), unidadAdministrativa, oficinaProductora,
                    nomComite, numActa, formatoFecha.format(fechaActa), table));

        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }

    }


    public List<DataTrdEcmVO> dataTrdByOfcProdListEcm(String idOfcProd, BigInteger idVersion) throws SystemException, BusinessException {

        List<DataTrdEcmVO> listDataTrdEcm =new ArrayList<>();
        try {
            List<Object[]> data = em.createNamedQuery("AdmTabRetDocOrg.versionTRDTableECM")
                    .setParameter("VAL_VERSION", idVersion)
                    .setParameter("ID_OFC_PROD", idOfcProd)
                    .getResultList();

            for (Object[] row : data) {
                DataTrdEcmVO trdEcm =new DataTrdEcmVO();
                trdEcm.setUnidadAdministrativa(row[1].toString());
                trdEcm.setOficinaProductora(row[4].toString());
                trdEcm.setCodSerie(row[7].toString());
                trdEcm.setNomSerie(row[8].toString());
                trdEcm.setCodSubSerie(row[10] == null ? "" : row[10].toString());
                trdEcm.setNomSubSerie(row[11] == null ? "" : row[11].toString());
                trdEcm.setArchivoGestion(Long.parseLong(row[12].toString()));
                trdEcm.setArchivoCentral(Long.parseLong(row[13].toString()));
                trdEcm.setProcedimientos(row[14].toString());
                trdEcm.setDiposicion(Math.round(Float.parseFloat(row[15].toString())));
                trdEcm.setRadicadora(row[17] == null ? false : row[17].toString().equals("1"));

                if (StringUtils.isEmpty(trdEcm.getCodSubSerie())){
                    trdEcm.setGrupoSeguridad(row[18].toString().equals("1") ? "C" : "" );
                    trdEcm.setGrupoSeguridad(row[19].toString().equals("1") ? "P" : trdEcm.getGrupoSeguridad() );
                    trdEcm.setGrupoSeguridad(row[20].toString().equals("1") ? "R" : trdEcm.getGrupoSeguridad() );
                } else {
                    trdEcm.setGrupoSeguridad(row[21].toString().equals("1") ? "C" : "" );
                    trdEcm.setGrupoSeguridad(row[22].toString().equals("1") ? "P" : trdEcm.getGrupoSeguridad() );
                    trdEcm.setGrupoSeguridad(row[23].toString().equals("1") ? "R" : trdEcm.getGrupoSeguridad() );
                }

                listDataTrdEcm.add(trdEcm);
            }

            return listDataTrdEcm;

        } catch (Throwable ex) {
            LOGGER.error("Business Boundary - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }

    }


}
