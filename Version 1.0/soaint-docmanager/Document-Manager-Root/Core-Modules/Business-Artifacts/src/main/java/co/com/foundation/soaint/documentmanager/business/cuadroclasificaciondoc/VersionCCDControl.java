package co.com.foundation.soaint.documentmanager.business.cuadroclasificaciondoc;

import co.com.foundation.soaint.documentmanager.domain.DataCcdVersionVO;
import co.com.foundation.soaint.documentmanager.infrastructure.builder.common.DataCcdVOBuilder;
import co.com.foundation.soaint.documentmanager.infrastructure.builder.common.DataTableTrdVOBuilder;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmCcd;
import co.com.foundation.soaint.infrastructure.annotations.BusinessControl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * Created by jrodriguez on 13/12/2016.
 */
@BusinessControl
public class VersionCCDControl {

    public DataCcdVOBuilder prepareCddBuilder(DataCcdVersionVO row) {

        DataCcdVOBuilder builder = DataCcdVOBuilder.newBuilder();
        builder.withCodigoNombreSeccion(row.getIdeUnidadAdministrativa().concat("-").concat(row.getNombreUnidadAdministrativa()));
        builder.withCodigoNombreSubseccion(row.getIdeOfcProdCodOrganigrama().concat("-").concat(row.getNombreOfcProdOrganigrama()));
        builder.withCodigoNombreSerie(row.getCodSerie().concat("-").concat(row.getNombreSerie()));
        if (!Objects.isNull(row.getIdSubSerie())) {
            builder.withCodigoNombreSubserie(row.getCodSubSerie().concat("-").concat(row.getNombreSubSerie()));
        }
        builder.withTiposDocumentales("-".concat(row.getNombreTipologia()).concat("\n"));
        return builder;
    }



}
