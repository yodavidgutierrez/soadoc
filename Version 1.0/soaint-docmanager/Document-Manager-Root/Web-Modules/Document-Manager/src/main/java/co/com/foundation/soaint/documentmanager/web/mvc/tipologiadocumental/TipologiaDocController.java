package co.com.foundation.soaint.documentmanager.web.mvc.tipologiadocumental;

import co.com.foundation.soaint.documentmanager.business.tipologiadocumental.interfaces.TipologiasDocManagerProxy;
import co.com.foundation.soaint.documentmanager.domain.DataReportCcdVO;
import co.com.foundation.soaint.documentmanager.domain.DataReportTipoDocVO;
import co.com.foundation.soaint.documentmanager.domain.DataTableCcdVO;
import co.com.foundation.soaint.documentmanager.domain.DataTableTipDocVO;
import co.com.foundation.soaint.documentmanager.infrastructure.builder.entity.EntityAdmSoporteBuilder;
import co.com.foundation.soaint.documentmanager.infrastructure.builder.entity.EntityAdmTpgDocBuilder;
import co.com.foundation.soaint.documentmanager.infrastructure.builder.entity.EntityAdmTradDocBuilder;
import co.com.foundation.soaint.documentmanager.web.domain.AsociacionGroupVO;
import co.com.foundation.soaint.documentmanager.web.infrastructure.builder.generic.HTTPResponseBuilder;
import co.com.foundation.soaint.documentmanager.web.infrastructure.builder.generic.TableResponseBuilder;
import co.com.foundation.soaint.documentmanager.web.infrastructure.builder.generic.TipologiasDocVoBuilder;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmSoporte;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmTpgDoc;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmTradDoc;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import co.com.foundation.soaint.infrastructure.common.MessageUtil;
import co.com.foundation.soaint.documentmanager.web.infrastructure.common.HTTPResponse;
import co.com.foundation.soaint.documentmanager.web.infrastructure.common.HTTPValidResponse;
import co.com.foundation.soaint.documentmanager.web.infrastructure.common.TableResponse;
import co.com.foundation.soaint.documentmanager.web.domain.TipologiaDocVO;
import co.com.foundation.soaint.documentmanager.web.infrastructure.util.HTMLUtil;
import co.com.foundation.soaint.documentmanager.web.infrastructure.util.constants.EstadoCaracteristicaEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import co.com.foundation.soaint.documentmanager.web.infrastructure.reports.ReportProcessor;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by jrodriguez on 22/09/2016.
 */
@Controller
@Scope("request")
@RequestMapping(value = "/tipologiasdoc")
public class TipologiaDocController {

    @Autowired
    private TipologiaDocModel model;

    @Autowired
    private TipologiasDocManagerProxy boundary;

    @Autowired
    ServletContext context;

    @Autowired
    private ReportProcessor<DataReportTipoDocVO> reportProcessor;



    @RequestMapping(value = "", method = RequestMethod.GET)
    public String init() throws BusinessException, SystemException {

        model.clear();

        for (AdmTpgDoc tpgDoc : boundary.findAllTipologiasDoc()) {

            TipologiaDocVO tipologiaDocVO = TipologiasDocVoBuilder.newBuilder()
                    .withIdeTpgDoc(tpgDoc.getIdeTpgDoc())
                    .withCarAdministrativa(tpgDoc.getCarAdministrativa().equals(EstadoCaracteristicaEnum.ON.getId())?
                            EstadoCaracteristicaEnum.ON.getName():EstadoCaracteristicaEnum.OFF.getName())
                    .withCarLegal(tpgDoc.getCarLegal().equals(EstadoCaracteristicaEnum.ON.getId())?
                            EstadoCaracteristicaEnum.ON.getName():EstadoCaracteristicaEnum.OFF.getName())
                    .withCarTecnica(tpgDoc.getCarTecnica().equals(EstadoCaracteristicaEnum.ON.getId())?
                            EstadoCaracteristicaEnum.ON.getName():EstadoCaracteristicaEnum.OFF.getName())
                    .withCarJuridico(tpgDoc.getCarJuridico().equals(EstadoCaracteristicaEnum.ON.getId())?
                            EstadoCaracteristicaEnum.ON.getName():EstadoCaracteristicaEnum.OFF.getName())
                    .withCarContable(tpgDoc.getCarContable().equals(EstadoCaracteristicaEnum.ON.getId())?
                            EstadoCaracteristicaEnum.ON.getName():EstadoCaracteristicaEnum.OFF.getName())
                    .withCodTpgDoc(tpgDoc.getCodTpgDoc())
                    .withEstTpgDoc(HTMLUtil.generateStatusColumn(tpgDoc.getEstTpgDoc()))
                    .withNomTpgDoc(tpgDoc.getNomTpgDoc())
                    .withNotAlcance(tpgDoc.getNotAlcance())
                    .withFueBibliografica(tpgDoc.getFueBibliografica())
                    .withIdSoporte(tpgDoc.getIdeSoporte().getIdeSoporte())
                    .withNomSoport(tpgDoc.getIdeSoporte().getNomSoporte())
                    .withIdTraDocumental(tpgDoc.getIdeTraDocumental().getIdeTradDoc())
                    .withNomTraDocumental(tpgDoc.getIdeTraDocumental().getNomTradDoc())
                    .withEstadoTpgDocValue(tpgDoc.getEstTpgDoc())
                    .build();
            model.getTipologiaDocList().add(tipologiaDocVO);
        }
        return "tipologiasdoc/tipologiadoc-crud";
    }

    @ResponseBody
    @RequestMapping(value = "/tipologiadocumentalList", method = RequestMethod.GET)
    public TableResponse<TipologiaDocVO> listSeries() throws SystemException, BusinessException {
        int size = model.getTipologiaDocList().size();
        return TableResponseBuilder.newBuilder()
                .withData(model.getTipologiaDocList())
                .withiTotalDisplayRecords(size)
                .withiTotalRecords(size)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.POST)
    public HTTPResponse createTipologiaDoc(@RequestBody TipologiaDocVO tipologiaDoc) throws BusinessException, SystemException {

        AdmTradDoc  tradicionDoc = tipologiaDoc.getIdTraDocumental() == null ? null : EntityAdmTradDocBuilder.newBuilder()
                .withIdeTradDoc(tipologiaDoc.getIdTraDocumental())
                .build();

        AdmSoporte  soporte = EntityAdmSoporteBuilder.newBuilder()
                .withIdeSoporte(tipologiaDoc.getIdSoporte())
                .build();

        EntityAdmTpgDocBuilder builder = EntityAdmTpgDocBuilder.newBuilder()
                .withetIdeTpgDoc(tipologiaDoc.getIdeTpgDoc())
                .withetCarLegal(tipologiaDoc.getCarLegal())
                .withetCarTecnica(tipologiaDoc.getCarTecnica())
                .withetCarAdministrativa(tipologiaDoc.getCarAdministrativa())
                .withetCarJuridico(tipologiaDoc.getCarJuridico())
                .withetCarContable(tipologiaDoc.getCarContable())
                .withetNotAlcance(tipologiaDoc.getNotAlcance())
                .withetFueBibliografica(tipologiaDoc.getFueBibliografica())
                .withetIdeTraDocumental(tradicionDoc)
                .withetIdeSoporte(soporte)
                .withetEstTpgDoc(Integer.valueOf(tipologiaDoc.getEstTpgDoc()))
                .withetNomTpgDoc(tipologiaDoc.getNomTpgDoc().trim());

        if(tipologiaDoc.getIdeTpgDoc() !=null) {

            AdmTpgDoc entityUpdate = builder
                    .withetFecCambio(new Date())
                    .withetIdeTpgDoc(tipologiaDoc.getIdeTpgDoc())
                    .build();

            boundary.updateTpgDoc(entityUpdate);
            init();
            return HTTPResponseBuilder.newBuilder()
                    .withSuccess(true)
                    .withMessage(MessageUtil.getMessage("tipologias.tpgdoc_update_successfully"))
                    .build();
        }else {

            AdmTpgDoc entity = builder
                    .withetFecCreacion(new Date())
                    .build();

            boundary.createTpgDoc(entity);
            init();
            return HTTPResponseBuilder.newBuilder()
                    .withSuccess(true)
                    .withMessage(MessageUtil.getMessage("tipologias.tpgdoc_created_successfully"))
                    .build();

        }
    }

    @ResponseBody
    @RequestMapping(value = "/removetpgdoc/{idTpgdoc}", method = RequestMethod.DELETE)
    public HTTPResponse removeTipoDoc(@PathVariable long idTpgdoc) throws BusinessException, SystemException {

        String mensaje = null;
        boolean error = false;

        boolean tipoDocExistInCcd = boundary.tipoDocExistByIdInCcd(BigInteger.valueOf(idTpgdoc));
        if (!tipoDocExistInCcd) {

        boundary.removeTpgDoc(BigInteger.valueOf(idTpgdoc));
            mensaje = MessageUtil.getMessage("tipologias.tpgdoc_remote_successfully");
            error = true;
        } else {
            mensaje = MessageUtil.getMessage("tipologias.tpgdoc_exist_version");
            error = false;
        }

        //Para refrescar la lista al eliminar
        init();
        return HTTPResponseBuilder.newBuilder()
                .withSuccess(error)
                .withMessage(mensaje)
                .build();
    }
    /**
     * Validar que el nombre de la tipologia no se encuentre repetido en base de datos
     * version 0.1
     * mlara
     * *
     */
    @ResponseBody
    @RequestMapping(value = "/validateNomTpcDoc", method = RequestMethod.POST)
    public HTTPValidResponse validateNomTpcDoc(@RequestParam Map<String, String> requestParams) throws BusinessException, SystemException {

        if (requestParams.get("ideTpgDoc").trim().equals("")) {
            return HTTPValidResponse.newInstance(!boundary.tpcDocExistByName(requestParams.get("nomTpgDoc").trim()));
        } else {
            return HTTPValidResponse.newInstance(true);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/downloadExcel", method = RequestMethod.GET)
    public ResponseEntity downloadExcel() {
        String url = System.getProperty("docmanager.app.resources").concat("/tipologias-doc-massive-file-new.csv");
        File file = new File(url);
        Path routeFile = Paths.get(file.getAbsolutePath());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Content-Type","text/csv;charset=utf-8");
        headers.add("Content-Disposition", "attachment; filename="+url+"");

        try {
            ByteArrayResource res = new ByteArrayResource(Files.readAllBytes(routeFile));
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .body(res);
        } catch (IOException e) {
            e.printStackTrace();
            return  ResponseEntity.notFound().build();
        }

    }

    @RequestMapping(value = "/generateExcel", method = RequestMethod.GET)
    public void generateExportExcel(HttpServletResponse response) throws BusinessException, SystemException {

        List<DataReportTipoDocVO> dataReport = new ArrayList<>();

        List<DataTableTipDocVO> rows = new ArrayList<>();

        Map<String, Object> params = new HashMap<String, Object>();

        String pathImagen =System.getProperty("docmanager.app.resources").concat("/img/logo-soaint.png");

        params.put("IMAGEN",pathImagen);

        DataReportTipoDocVO  data = new DataReportTipoDocVO();

        for (TipologiaDocVO temp: model.getTipologiaDocList()){
            DataTableTipDocVO info;
            info = new DataTableTipDocVO(temp.getNomTpgDoc(),temp.getEstadoTpgDocValue(),temp.getNomTraDocumental(), temp.getNomSoport(),
                                         temp.getCarAdministrativaValue(), temp.getCarLegalValue(), temp.getCarTecnicaValue(),
                                          temp.getCarJuridicoValue(),temp.getCarContableValue());
            rows.add(info);
        }
        data.setRows(rows);
        dataReport.add(data);

        reportProcessor.generateReportExcel(response, "RptTipologiaDocumental.jrxml", dataReport, params, "TABLA_TIPOLOGIA_DOCUMENTOS.xls");
    }

}
