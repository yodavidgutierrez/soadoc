package co.com.foundation.soaint.documentmanager.web.mvc.trd;

import co.com.foundation.soaint.documentmanager.business.organigrama.interfaces.OrganigramaManagerProxy;
import co.com.foundation.soaint.documentmanager.business.tablaretenciondisposicion.interfaces.TabRetencionDiposicionManagerProxy;
import co.com.foundation.soaint.documentmanager.business.tablaretenciondisposicion.interfaces.VersionesTabRetDocManagerProxy;
import co.com.foundation.soaint.documentmanager.domain.OrganigramaItemVO;
import co.com.foundation.soaint.documentmanager.domain.TRDTableVO;
import co.com.foundation.soaint.documentmanager.persistence.entity.TvsConfigOrgAdministrativo;
import co.com.foundation.soaint.documentmanager.persistence.entity.TvsOrganigramaAdministrativo;
import co.com.foundation.soaint.documentmanager.web.domain.TrdVo;
import co.com.foundation.soaint.documentmanager.web.infrastructure.builder.generic.HTTPResponseBuilder;
import co.com.foundation.soaint.documentmanager.web.infrastructure.builder.generic.SelectItemBuilder;
import co.com.foundation.soaint.documentmanager.web.infrastructure.common.HTTPResponse;
import co.com.foundation.soaint.documentmanager.web.infrastructure.common.SelectItem;
import co.com.foundation.soaint.infrastructure.common.MessageUtil;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by jrodriguez on 20/11/2016.
 */

@Controller
@Scope("request")
@RequestMapping(value = "/trd")
public class TrdController {

    @Autowired
    private TrdModel model;
    @Autowired
    private OrganigramaManagerProxy organigramaBoundary;
    @Autowired
    private TabRetencionDiposicionManagerProxy trdBoundary;
    @Autowired
    private VersionesTabRetDocManagerProxy versionTrdBoundary;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String init() throws BusinessException, SystemException {
        return "trd/trd-ui";
    }

    // ------------------------
    @ResponseBody
    @RequestMapping(value = "/generarTableTRD/{dependencia:.+}", method = RequestMethod.GET)
    public TRDTableVO generarTableTRD(@PathVariable String dependencia) throws SystemException, BusinessException {
        return trdBoundary.trdConfByOfcProdList(dependencia);
    }

    // ------------------------
    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.POST)
    public HTTPResponse publicarVersionTrd(@RequestBody TrdVo trd) throws BusinessException, SystemException {
        versionTrdBoundary.publicarTablaRetencionDocumental(trd.getCodUniAmt(),trd.getCodOfcProd(), trd.getNombreComite(),
                trd.getNumActa(), trd.getFechaActa());
        return HTTPResponseBuilder.newBuilder()
                .withSuccess(true)
                .withMessage(MessageUtil.getMessage("trd.versionamiento.public.successfully"))
                .build();
    }


}
