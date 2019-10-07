package co.com.foundation.soaint.documentmanager.business.cuadroclasificaciondoc;

import co.com.foundation.soaint.documentmanager.business.cuadroclasificaciondoc.interfaces.VersionCuadroClasificacionDocManagerProxy;
import co.com.foundation.soaint.documentmanager.domain.DataCcdVersionVO;
import co.com.foundation.soaint.documentmanager.domain.DataReportCcdVO;
import co.com.foundation.soaint.documentmanager.domain.DataTableCcdVO;
import co.com.foundation.soaint.documentmanager.infrastructure.builder.common.DataCcdVOBuilder;
import co.com.foundation.soaint.infrastructure.annotations.BusinessBoundary;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by ADMIN on 30/11/2016.
 */
@BusinessBoundary
public class VersionCCDManager implements VersionCuadroClasificacionDocManagerProxy {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private VersionCCDControl controlCcd;

    @Autowired CuadroClasificacionDocControl controlCcdVersion;

    public VersionCCDManager() {
    }

    public DataReportCcdVO versionCcdByOfcProdList(String valVersion) throws SystemException, BusinessException {


        List<DataCcdVersionVO> ccdList = em.createNamedQuery("AdmCcd.findVersionCcdByValVersion", DataCcdVersionVO.class)
                .setParameter("VAL_VERSION", valVersion)
                .getResultList();
        ccdList.addAll(em.createNamedQuery("AdmCcd.findVersionCcdSerieByValVersion", DataCcdVersionVO.class)
                .setParameter("VAL_VERSION", valVersion)
                .getResultList());

        ArrayList<DataTableCcdVO> table = new ArrayList<>();
        DataTableCcdVO current = null;
        DataCcdVOBuilder builder = null;
        String fechaVersion = "";
        String version = "";
        boolean newSerie = true;
        String serie = "";
        boolean newSubSerie = false;
        String subSerie = "";
        Boolean newOfiProd =false;
        String ofiProd="";


        for (DataCcdVersionVO ccd : ccdList) {

            newOfiProd=(!StringUtils.equals(ofiProd,ccd.getIdeOfcProdCodOrganigrama()));
            if(newOfiProd)
                ofiProd=ccd.getIdeOfcProdCodOrganigrama().toString();

            newSerie = (!StringUtils.equals(serie, ccd.getIdeSerie().toString()));
            if (newSerie)
                serie = ccd.getIdeSerie().toString();

            newSubSerie = (!StringUtils.equals(subSerie, String.valueOf(ccd.getIdSubSerie())));

            if (newSerie || newSubSerie || newOfiProd) {
                subSerie = String.valueOf(ccd.getIdSubSerie());
                builder = controlCcd.prepareCddBuilder(ccd);
                current = builder.build();
                table.add(current);
            }

            if (!newSerie && !newSubSerie && !newOfiProd && !Objects.isNull(current))
                current.addTipoDocumental("-".concat(ccd.getNombreTipologia()).concat("\n"));

            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy h:mm a");

            fechaVersion = formatoFecha.format(ccd.getFecCreacion());

            version = ccd.getValVersion();
        }

        if(version.equals("TOP")){
            int versionCcd =controlCcdVersion.consultarVersionActualDeCcd();
            version ="V."+versionCcd;
        }

        return new DataReportCcdVO(fechaVersion, version, table);
    }


}


