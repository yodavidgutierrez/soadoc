/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.foundation.soaint.documentmanager.web.mvc.motivosDoc;

import co.com.foundation.soaint.documentmanager.business.configuracion.interfaces.MotivosCreacionManagerProxy;
import co.com.foundation.soaint.documentmanager.infrastructure.builder.entity.EntityAdmMotCreacionBuilder;
import co.com.foundation.soaint.documentmanager.web.infrastructure.builder.generic.HTTPResponseBuilder;
import co.com.foundation.soaint.documentmanager.web.infrastructure.builder.generic.MotivosDocVoBuilder;
import co.com.foundation.soaint.documentmanager.web.infrastructure.builder.generic.TableResponseBuilder;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmMotCreacion;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import co.com.foundation.soaint.infrastructure.common.MessageUtil;
import co.com.foundation.soaint.documentmanager.web.infrastructure.common.HTTPResponse;
import co.com.foundation.soaint.documentmanager.web.infrastructure.common.HTTPValidResponse;
import co.com.foundation.soaint.documentmanager.web.infrastructure.common.TableResponse;
import co.com.foundation.soaint.documentmanager.web.domain.MotivosDocVO;

import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Scope("request")
@RequestMapping(value = "/motivosDoc")
public class MotivosDocController {

    @Autowired
    private MotivosDocModel model;

    @Autowired
    private MotivosCreacionManagerProxy motivosCreacionBoundary;

    public MotivosDocController() {
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String init() throws BusinessException, SystemException {
        model.clear();

        for (AdmMotCreacion admMotCreacion : motivosCreacionBoundary.findAllMotivosCreacion()) {
            MotivosDocVO vo = MotivosDocVoBuilder.newBuilder()
                    .withIdeMotCreacion(admMotCreacion.getIdeMotCreacion())
                    .withFecCambio(admMotCreacion.getFecCambio())
                    .withFecCreacion(admMotCreacion.getFecCreacion())
                    .withIdeUsuarioCambio(admMotCreacion.getIdeUsuarioCambio())
                    .withIdeUuid(admMotCreacion.getIdeUuid())
                    .withNivEscritura(admMotCreacion.getNivEscritura())
                    .withNivLectura(admMotCreacion.getNivLectura())
                    .withNomMotCreacion(admMotCreacion.getNomMotCreacion())
                    .withEstado(admMotCreacion.getEstado())
                    .build();

            model.getMotivosCreacionList().add(vo);
        }

        return "motivosDoc/motivosDoc-crud";
    }

    //[ lista de motivos ]
    @ResponseBody
    @RequestMapping(value = "/motivosCreacionList", method = RequestMethod.GET)
    public TableResponse<MotivosDocVO> getMotivosCreacionList() throws SystemException, BusinessException {
        int size = model.getMotivosCreacionList().size();

        return TableResponseBuilder.newBuilder()
                .withData(model.getMotivosCreacionList())
                .withiTotalDisplayRecords(size)
                .withiTotalRecords(size)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.POST)
    public HTTPResponse createMotivosDoc(@RequestBody MotivosDocVO motivosDocVO)
            throws BusinessException, SystemException {

        AdmMotCreacion motCreacion = EntityAdmMotCreacionBuilder.newBuilder()
                .withIdeMotCreacion(motivosDocVO.getIdeMotCreacion()).build();

        if (motivosDocVO.getIdeMotCreacion() != null) {
            AdmMotCreacion entityUpdate = EntityAdmMotCreacionBuilder.newBuilder()
                    .withIdeMotCreacion(motivosDocVO.getIdeMotCreacion())
                    .withFecCambio(new Date())
                    .withEstado(motivosDocVO.getEstado())
                    .withNomMotCreacion(motivosDocVO.getNomMotCreacion().trim())
                    .build();
            motivosCreacionBoundary.updateMotivosDoc(entityUpdate);
            init();
            return HTTPResponseBuilder.newBuilder()
                    .withSuccess(true)
                    .withMessage(MessageUtil.getMessage("motivosDoc.motivos_update_successfully"))
                    .build();

        } else {
            AdmMotCreacion entity = EntityAdmMotCreacionBuilder.newBuilder()
                    .withFecCreacion(new Date())
                    .withEstado(motivosDocVO.getEstado())
                    .withNomMotCreacion(motivosDocVO.getNomMotCreacion().trim())
                    .build();
            motivosCreacionBoundary.createMotivosDoc(entity);
            init();
            return HTTPResponseBuilder.newBuilder()
                    .withSuccess(true)
                    .withMessage(MessageUtil.getMessage("motivosDoc.motivos_created_successfully"))
                    .build();

        }
    }

    @ResponseBody
    @RequestMapping(value = "/validateNomMotCreacion", method = RequestMethod.POST)
    public HTTPValidResponse validateNomMotCreacion(@RequestParam Map<String, String> requestParams) throws BusinessException, SystemException {
        if (!requestParams.get("nomMotCreacion").trim().equals("")) {
            String id = requestParams.get("ideMotCreacion");
            return HTTPValidResponse.newInstance(motivosCreacionBoundary.motivosDocExistByName(requestParams.get("nomMotCreacion").trim(),
                    new BigInteger(StringUtils.isEmpty(id) ? "0" : id)));
        } else {
            return HTTPValidResponse.newInstance(true);
        }
    }

}
