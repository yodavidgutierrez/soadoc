/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.foundation.soaint.documentmanager.web.mvc.disposicionFinal;

import co.com.foundation.soaint.documentmanager.business.configuracion.interfaces.DisposicionFinalManagerProxy;
import co.com.foundation.soaint.documentmanager.infrastructure.builder.entity.EntityAdmDisposicionFinalBuilder;
import co.com.foundation.soaint.documentmanager.web.infrastructure.builder.generic.DisposicionFinalVoBuilder;
import co.com.foundation.soaint.documentmanager.web.infrastructure.builder.generic.HTTPResponseBuilder;
import co.com.foundation.soaint.documentmanager.web.infrastructure.builder.generic.TableResponseBuilder;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmDisFinal;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import co.com.foundation.soaint.infrastructure.common.MessageUtil;
import co.com.foundation.soaint.documentmanager.web.infrastructure.common.HTTPResponse;
import co.com.foundation.soaint.documentmanager.web.infrastructure.common.HTTPValidResponse;
import co.com.foundation.soaint.documentmanager.web.infrastructure.common.TableResponse;
import co.com.foundation.soaint.documentmanager.web.domain.DisposicionFinalVO;

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

/**
 *
 * @author ADMIN
 */
@Controller
@Scope("request")
@RequestMapping(value = "/disposicionFinal")
public class DisposicionFinalController {

    @Autowired
    private DisposicionFinalModel model;

    @Autowired
    private DisposicionFinalManagerProxy boundary;

    public DisposicionFinalController() {
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String init() throws BusinessException, SystemException {
        model.clear();

        for (AdmDisFinal admDisFinal : boundary.findAllDisposicionFinal()) {
            DisposicionFinalVO vo = DisposicionFinalVoBuilder.newBuilder()
                    .withIdeDisFinal(admDisFinal.getIdeDisFinal())
                    .withNomDisFinal(admDisFinal.getNomDisFinal())
                    .withDesDisFinal(admDisFinal.getDesDisFinal())
                    .withStaDisFinal(admDisFinal.getStaDisFinal())
                    .withIdeUsuarioCambio(admDisFinal.getIdeUsuarioCambio())
                    .withFecCambio(admDisFinal.getFecCambio())
                    .withNivLectura(admDisFinal.getNivLectura())
                    .withNivEscritura(admDisFinal.getNivEscritura())
                    .withIdeUuid(admDisFinal.getIdeUuid()).build();

            model.getDisposicionFinalList().add(vo);
        }
        return "disposicionFinal/disFinal-crud";

    }

    @ResponseBody
    @RequestMapping(value = "/disposicionFinalList", method = RequestMethod.GET)
    public TableResponse<DisposicionFinalVO> getDisposicionFinalList() throws SystemException, BusinessException {
        int size = model.getDisposicionFinalList().size();

        return TableResponseBuilder.newBuilder()
                .withData(model.getDisposicionFinalList())
                .withiTotalDisplayRecords(size)
                .withiTotalRecords(size)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.POST)
    public HTTPResponse createDisposicionFinal(@RequestBody DisposicionFinalVO disposicionFinalVO)
            throws BusinessException, SystemException {

        AdmDisFinal adf = EntityAdmDisposicionFinalBuilder.newBuilder()
                .withIdeDisFinal(disposicionFinalVO.getIdeDisFinal()).build();

        if (disposicionFinalVO.getIdeDisFinal() != null) {
            AdmDisFinal entityUpdate = EntityAdmDisposicionFinalBuilder.newBuilder()
                    .withIdeDisFinal(disposicionFinalVO.getIdeDisFinal())
                    .withNomDisFinal(disposicionFinalVO.getNomDisFinal().trim())
                    .withDesDisFinal(disposicionFinalVO.getDesDisFinal())
                    .withStaDisFinal(disposicionFinalVO.getStaDisFinal())
                    .withFecCambio(new Date())
                    .build();
            boundary.updateDisposicionFinal(entityUpdate);
            init();
            return HTTPResponseBuilder.newBuilder()
                    .withSuccess(true)
                    .withMessage(MessageUtil.getMessage("disposicionFinal.disposicionFinal_update_successfully"))
                    .build();

        } else {
            AdmDisFinal entity = EntityAdmDisposicionFinalBuilder.newBuilder()
                    .withFecCreacion(new Date())
                    .withNomDisFinal(disposicionFinalVO.getNomDisFinal().trim())
                    .withDesDisFinal(disposicionFinalVO.getDesDisFinal())
                    .withStaDisFinal(disposicionFinalVO.getStaDisFinal())
                    .build();
            boundary.createDisposicionFinal(entity);
            init();
            return HTTPResponseBuilder.newBuilder()
                    .withSuccess(true)
                    .withMessage(MessageUtil.getMessage("disposicionFinal.disposicionFinal_created_successfully"))
                    .build();
        }
    }

    @ResponseBody
    @RequestMapping(value = "/validateNomDisDisposiciones", method = RequestMethod.POST)
    public HTTPValidResponse validateNomMotCreacion(@RequestParam Map<String, String> requestParams) throws SystemException {

        if (!requestParams.get("nomDisFinal").trim().equals("")) {
            String ide = requestParams.get("ideDisFinal");
            return HTTPValidResponse.newInstance(
                    boundary.DisposicionesExistByName(requestParams.get("nomDisFinal").trim(),
                     new BigInteger(StringUtils.isEmpty(ide) ? "0" : ide)));
        } else {
            return HTTPValidResponse.newInstance(true);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/validateDesDisDisposiciones", method = RequestMethod.POST)
    public HTTPValidResponse validateDesMotCreacion(@RequestParam Map<String, String> requestParams) throws SystemException {

        if (!requestParams.get("desDisFinal").trim().equals("")) {
            String ide = requestParams.get("ideDisFinal");
            return HTTPValidResponse.newInstance(
                    boundary.DisposicionesExistByDesc(requestParams.get("desDisFinal").trim(),
                    new BigInteger(StringUtils.isEmpty(ide) ? "0" : ide)));
        } else {
            return HTTPValidResponse.newInstance(true);
        }
    }
}
