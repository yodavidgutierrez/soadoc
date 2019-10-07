/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.foundation.soaint.documentmanager.web.mvc.asosersubtpg;

import co.com.foundation.soaint.documentmanager.business.asosersubtpg.interfaces.AsoSerSubTpglManagerProxy;
import co.com.foundation.soaint.documentmanager.domain.DataReportCcdVO;
import co.com.foundation.soaint.documentmanager.domain.DataTableCcdVO;
import co.com.foundation.soaint.documentmanager.infrastructure.builder.entity.EntityAdmSerSubserTpgBuilder;
import co.com.foundation.soaint.documentmanager.web.infrastructure.builder.generic.HTTPResponseBuilder;
import co.com.foundation.soaint.documentmanager.web.infrastructure.builder.generic.SerieVoBuilder;
import co.com.foundation.soaint.documentmanager.web.infrastructure.builder.generic.SubserieVoBuilder;
import co.com.foundation.soaint.documentmanager.web.infrastructure.builder.generic.TableResponseBuilder;
import co.com.foundation.soaint.documentmanager.web.infrastructure.builder.generic.TipologiasDocVoBuilder;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmSerSubserTpg;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmSerie;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmSubserie;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmTpgDoc;
import co.com.foundation.soaint.documentmanager.business.series.interfaces.SeriesManagerProxy;
import co.com.foundation.soaint.documentmanager.business.subserie.interfaces.SubserieManagerProxy;
import co.com.foundation.soaint.documentmanager.business.tipologiadocumental.interfaces.TipologiasDocManagerProxy;

import co.com.foundation.soaint.documentmanager.web.infrastructure.reports.ReportProcessor;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import co.com.foundation.soaint.infrastructure.common.MessageUtil;
import co.com.foundation.soaint.documentmanager.web.infrastructure.common.HTTPResponse;
import co.com.foundation.soaint.documentmanager.web.infrastructure.common.TableResponse;
import co.com.foundation.soaint.documentmanager.web.domain.AsociacionGroupVO;
import co.com.foundation.soaint.documentmanager.web.domain.SerieVO;
import co.com.foundation.soaint.documentmanager.web.domain.SubserieVO;
import co.com.foundation.soaint.documentmanager.web.domain.TipologiaDocVO;

import java.math.BigInteger;
import java.util.*;

import org.apache.lucene.util.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author malzate on 27/09/2016
 */
@Controller
@Scope("request")
@RequestMapping(value = "/asociacion")
public class AsociacionController {

    @Autowired
    private AsociacionModel model;

    @Autowired
    private AsoSerSubTpglManagerProxy boundary;

    @Autowired
    private SeriesManagerProxy boundarySerie;

    @Autowired
    private SubserieManagerProxy boundarySubserie;

    @Autowired
    private TipologiasDocManagerProxy boundaryTipologias;

    @Autowired
    ServletContext context;

    @Autowired
    private ReportProcessor<DataReportCcdVO> reportProcessor;

    public AsociacionController() {
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String init() throws BusinessException, SystemException {
        model.clear();
        List<AdmSerSubserTpg> asocList = boundary.findAllAsocGroup();

        for (AdmSerSubserTpg asoc : asocList) {

            //Build Serie
            SerieVO serieVo = SerieVoBuilder.newBuilder()
                    .withIdeSerie(asoc.getIdeSerie().getIdeSerie())
                    .withNomSerie(asoc.getIdeSerie().getNomSerie())
                    .withCodSerie(asoc.getIdeSerie().getCodSerie()).build();
            //Build SubSerie
            //SubserieVO subSerieVo = null;
            //  if(asoc.getIdeSubserie().getIdeSubserie() != null ) {
            SubserieVO subSerieVo = SubserieVoBuilder.newBuilder()
                    .withIdeSubserie(asoc.getIdeSubserie().getIdeSubserie())
                    .withNomSubserie(asoc.getIdeSubserie().getNomSubserie())
                    .withCodSubserie(asoc.getIdeSubserie().getCodSubserie()).build();
            //}
            //Build TipologiasDoc
            TipologiaDocVO tipologiasVo = TipologiasDocVoBuilder.newBuilder()
                    .withIdeTpgDoc(asoc.getIdeTpgDoc().getIdeTpgDoc())
                    .withNomTpgDoc(asoc.getIdeTpgDoc().getNomTpgDoc()).build();

            AsociacionGroupVO group;
            boolean found = false;
            for (AsociacionGroupVO groupModel : model.getAsociacionGroupList()) {
                if (asoc.getIdeSubserie().getIdeSubserie() != null) {
                    if(groupModel.getIdeSubserie().getIdeSubserie() != null) {
                        if (groupModel.getIdeSerie().getIdeSerie().equals(asoc.getIdeSerie().getIdeSerie()) &&
                                groupModel.getIdeSubserie().getIdeSubserie().equals(asoc.getIdeSubserie().getIdeSubserie())) {
                            model.getAsociacionGroupList().get(model.getAsociacionGroupList().indexOf(groupModel)).getIdeTpgDoc().add(tipologiasVo);
                            found = true;
                            break;
                        }
                    }
                } else {

                    if (groupModel.getIdeSerie().getIdeSerie().equals(asoc.getIdeSerie().getIdeSerie())) {
                        model.getAsociacionGroupList().get(model.getAsociacionGroupList().indexOf(groupModel)).getIdeTpgDoc().add(tipologiasVo);
                        found = true;
                        break;
                    }

                }


            }

            if (!found) {
                group = new AsociacionGroupVO();
                group.setIdeSerie(serieVo);
                group.setIdeSubserie(subSerieVo);
                group.getIdeTpgDoc().add(tipologiasVo);
                model.getAsociacionGroupList().add(group);
            }

        }

        return "asoctiposdocumentales/asociacion-crud";
    }

    @ResponseBody
    @RequestMapping(value = "/asociacionList", method = RequestMethod.GET)
    public TableResponse<AsociacionGroupVO> listAsociacion() throws SystemException, BusinessException {
        int size = model.getAsociacionGroupList().size();

        TableResponse table = TableResponseBuilder.newBuilder()
                .withData(model.getAsociacionGroupList())
                .withiTotalDisplayRecords(size)
                .withiTotalRecords(size)
                .build();
        return table;
    }

    @ResponseBody
    @RequestMapping(value = "/tipDocBySerieAndSubSerie/{idSerie}/{idSubSerie}", method = RequestMethod.GET)
    public List<TipologiaDocVO> tipDocBySerieAndSubSerie(@PathVariable BigInteger idSerie, @PathVariable BigInteger idSubSerie) throws SystemException, BusinessException {
        List<TipologiaDocVO> tipoVoList = new ArrayList<>();
        for (AdmTpgDoc admTpgDoc : boundary.findTipBySerAndSubSer(idSerie, idSubSerie)) {
            TipologiaDocVO tipoVo = TipologiasDocVoBuilder.newBuilder()
                    .withIdeTpgDoc(admTpgDoc.getIdeTpgDoc())
                    .withNomTpgDoc(admTpgDoc.getNomTpgDoc())
                    .build();

            tipoVoList.add(tipoVo);
        }
        return tipoVoList;
    }

    @ResponseBody
    @RequestMapping(value = "/tipDocBySerie/{idSerie}", method = RequestMethod.GET)
    public List<TipologiaDocVO> tipDocBySerie(@PathVariable BigInteger idSerie) throws SystemException, BusinessException {
        List<TipologiaDocVO> tipoVoList = new ArrayList<>();
        for (AdmTpgDoc admTpgDoc : boundary.findTipBySer(idSerie)) {
            TipologiaDocVO tipoVo = TipologiasDocVoBuilder.newBuilder()
                    .withIdeTpgDoc(admTpgDoc.getIdeTpgDoc())
                    .withNomTpgDoc(admTpgDoc.getNomTpgDoc())
                    .build();

            tipoVoList.add(tipoVo);
        }
        return tipoVoList;
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.POST)
    public HTTPResponse createOrDeleteAsoc(@RequestBody AsociacionGroupVO asociacionGroup) throws BusinessException, SystemException {

        AdmSerSubserTpg asocCrear;
        AdmSerie serie = boundarySerie.findByIdeSerie(asociacionGroup.getIdeSerie().getIdeSerie());
        List<AdmSubserie> subseries = boundarySubserie.searchSubseriesByIdSerie(asociacionGroup.getIdeSerie().getIdeSerie());
        if(asociacionGroup.getIdeSubserie().getIdeSubserie()==null && !CollectionUtils.isEmpty(subseries)){
            return HTTPResponseBuilder.newBuilder()
                    .withSuccess(false)
                    .withMessage("Debe especificar la subserie")
                    .build();
        }
        AdmSubserie subSerie = null;

        /*List<AdmSerSubserTpg> admSerSubserTpgsList = new ArrayList<>();
        if (asociacionGroup.getIdeSubserie().getIdeSubserie() != null) {
            subSerie = boundarySubserie.searchSubserieById(asociacionGroup.getIdeSubserie().getIdeSubserie());
            admSerSubserTpgsList = boundary.findAsocBySerAndSubSer(serie.getIdeSerie(), subSerie.getIdeSubserie());
        } else {
            admSerSubserTpgsList = boundary.findAsocBySerAndSubServ(serie.getIdeSerie());
        }*/

        if (asociacionGroup.getIdeSubserie().getIdeSubserie() != null) {
            subSerie = boundarySubserie.searchSubserieById(asociacionGroup.getIdeSubserie().getIdeSubserie());
            boundary.removeAsocBySubSerie(subSerie.getIdeSubserie());
        } else {
            boundary.removeAsocBySerie(serie.getIdeSerie());
        }

        for (TipologiaDocVO tipo : asociacionGroup.getIdeTpgDoc()) {
            AdmTpgDoc admTpgDoc = boundaryTipologias.findById(tipo.getIdeTpgDoc());

            asocCrear = EntityAdmSerSubserTpgBuilder.newInstance()
                    .withFecCreacion(new Date())
                    .withIdeSerie(serie)
                    .withIdeSubserie(subSerie)
                    .withOrden(asociacionGroup.getIdeTpgDoc().indexOf(tipo) + 1)
                    .withIdeTpgDoc(admTpgDoc).build();
            boundary.createAsoc(asocCrear);
            init();
        }

        //Se crean las nuevas asociaciones
        /*for (TipologiaDocVO tipo : asociacionGroup.getIdeTpgDoc()) {
            boolean encontro = false;
            for (AdmSerSubserTpg admSerSubserTpg : admSerSubserTpgsList) {
                if (tipo.getIdeTpgDoc().equals(admSerSubserTpg.getIdeTpgDoc().getIdeTpgDoc())) {
                    encontro = true;
                }
            }
            if (!encontro) {
                AdmTpgDoc admTpgDoc = boundaryTipologias.findById(tipo.getIdeTpgDoc());

                asocCrear = EntityAdmSerSubserTpgBuilder.newInstance()
                        .withFecCreacion(new Date())
                        .withIdeSerie(serie)
                        .withIdeSubserie(subSerie)
                        .withIdeTpgDoc(admTpgDoc).build();
                boundary.createAsoc(asocCrear);
                init();
            }
        }

        //Se eliminan las asociaciones
        for (AdmSerSubserTpg admSerSubserTpg : admSerSubserTpgsList) {
            boolean encontro = false;
            for (TipologiaDocVO tipo : asociacionGroup.getIdeTpgDoc()) {
                if (admSerSubserTpg.getIdeTpgDoc().getIdeTpgDoc().equals(tipo.getIdeTpgDoc())) {
                    encontro = true;
                }
            }
            if (!encontro) {
                boundary.removeAsocById(admSerSubserTpg.getIdeRelSst());
            }
        }*/
        init();
        return HTTPResponseBuilder.newBuilder()
                .withSuccess(true)
                .withMessage(MessageUtil.getMessage("generic.asociacion_update_successfully"))
                .build();
    }

    @RequestMapping(value = "/generateExcel", method = RequestMethod.GET)
    public void generateExportExcel(HttpServletResponse response) throws BusinessException, SystemException {

        List<DataReportCcdVO> dataReport =new ArrayList<>();
        String realpath =context.getRealPath("/");

        Map<String, Object> params = new HashMap<String, Object>();
        String pathImagen =System.getProperty("docmanager.app.resources").concat("/img/logo-soaint.png");
        params.put("IMAGEN",pathImagen);

        DataReportCcdVO data = new DataReportCcdVO("", "", new ArrayList<>());
        for (AsociacionGroupVO temp:model.getAsociacionGroupList()) {
            String tipologias = "";
            for (TipologiaDocVO tipo:temp.getIdeTpgDoc()) {
                tipologias += tipologias.length() == 0 ? tipo.getNomTpgDoc() : ", " +tipo.getNomTpgDoc();
            }
            data.getRows().add(new DataTableCcdVO("", "",
                    temp.getIdeSerie().getNomSerie(), temp.getIdeSubserie().getNomSubserie(), tipologias));
        }

        dataReport.add(data);

        reportProcessor.generateReportExcel(response, "RptAsociacionSerSubSerTpgExcel.jrxml", dataReport, params, "ASOC-TPG-SER-SUBSER.xls");
    }
}
