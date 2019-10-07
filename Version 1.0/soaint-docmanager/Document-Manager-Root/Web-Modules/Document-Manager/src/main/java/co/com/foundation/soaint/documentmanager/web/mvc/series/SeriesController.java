package co.com.foundation.soaint.documentmanager.web.mvc.series;

import co.com.foundation.soaint.documentmanager.business.series.interfaces.SeriesManagerProxy;
import co.com.foundation.soaint.documentmanager.domain.DataReportSeriesVO;
import co.com.foundation.soaint.documentmanager.domain.DataTableSerieVO;
import co.com.foundation.soaint.documentmanager.infrastructure.builder.entity.EntityAdmMotCreacionBuilder;
import co.com.foundation.soaint.documentmanager.infrastructure.builder.entity.EntityAdmSerieBuilder;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmMotCreacion;
import co.com.foundation.soaint.documentmanager.persistence.entity.AdmSerie;
import co.com.foundation.soaint.documentmanager.web.domain.SerieVO;
import co.com.foundation.soaint.documentmanager.web.infrastructure.builder.generic.HTTPResponseBuilder;
import co.com.foundation.soaint.documentmanager.web.infrastructure.builder.generic.SerieVoBuilder;
import co.com.foundation.soaint.documentmanager.web.infrastructure.builder.generic.TableResponseBuilder;
import co.com.foundation.soaint.documentmanager.web.infrastructure.common.HTTPResponse;
import co.com.foundation.soaint.documentmanager.web.infrastructure.common.HTTPValidResponse;
import co.com.foundation.soaint.documentmanager.web.infrastructure.common.TableResponse;
import co.com.foundation.soaint.documentmanager.web.infrastructure.util.HTMLUtil;
import co.com.foundation.soaint.documentmanager.web.infrastructure.util.constants.EstadoCaracteristicaEnum;
import co.com.foundation.soaint.infrastructure.common.MessageUtil;
import co.com.foundation.soaint.infrastructure.exceptions.BusinessException;
import co.com.foundation.soaint.infrastructure.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import co.com.foundation.soaint.documentmanager.web.infrastructure.reports.ReportProcessor;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by jprodriguez on 02/09/2016.
 */
@Controller
@Scope("request")
@RequestMapping(value = "/series")
public class SeriesController {

    @Autowired
    private SeriesModel model;

    @Autowired
    private SeriesManagerProxy boundary;

    @Autowired
    ServletContext context;

    @Autowired
    ReportProcessor<DataReportSeriesVO> reportProcessor;


    public SeriesController() {
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String init() throws BusinessException, SystemException {
        model.clear();

        for (AdmSerie series : boundary.findAllSeries()) {

            SerieVO vo = SerieVoBuilder.newBuilder()
                    .withIdeSerie(series.getIdeSerie())
                    .withCodSerie(series.getCodSerie())
                    .withNomSerie(series.getNomSerie())
                    .withCarLegal(series.getCarLegal().equals(EstadoCaracteristicaEnum.ON.getId())
                            ? EstadoCaracteristicaEnum.ON.getName() : EstadoCaracteristicaEnum.OFF.getName())
                    .withCarAdministrativa(series.getCarAdministrativa().equals(EstadoCaracteristicaEnum.ON.getId())
                            ? EstadoCaracteristicaEnum.ON.getName() : EstadoCaracteristicaEnum.OFF.getName())
                    .withCarTecnica(series.getCarTecnica().equals(EstadoCaracteristicaEnum.ON.getId())
                            ? EstadoCaracteristicaEnum.ON.getName() : EstadoCaracteristicaEnum.OFF.getName())
                    .withCarContable(series.getCarContable().equals(EstadoCaracteristicaEnum.ON.getId())
                            ? EstadoCaracteristicaEnum.ON.getName() : EstadoCaracteristicaEnum.OFF.getName())
                    .withCarJuridica(series.getCarJuridica().equals(EstadoCaracteristicaEnum.ON.getId())
                            ? EstadoCaracteristicaEnum.ON.getName() : EstadoCaracteristicaEnum.OFF.getName())
                    .withConPublica(series.getConPublica().equals(EstadoCaracteristicaEnum.ON.getId())
                            ? EstadoCaracteristicaEnum.ON.getName() : EstadoCaracteristicaEnum.OFF.getName())
                    .withConClasificada(series.getConClasificada().equals(EstadoCaracteristicaEnum.ON.getId())
                            ? EstadoCaracteristicaEnum.ON.getName() : EstadoCaracteristicaEnum.OFF.getName())
                    .withConReservada(series.getConReservada().equals(EstadoCaracteristicaEnum.ON.getId())
                            ? EstadoCaracteristicaEnum.ON.getName() : EstadoCaracteristicaEnum.OFF.getName())
                    .withActAdministrativo(series.getActAdministrativo())
                    .withIdMotivo(series.getIdeMotCreacion().getIdeMotCreacion())
                    .withNombreMotCreacion(series.getIdeMotCreacion().getNomMotCreacion())
                    .withNotAlcance(series.getNotAlcance())
                    .withFueBibliografica(series.getFueBibliografica())
                    .withEstSerie(HTMLUtil.generateStatusColumn(series.getEstSerie()))
                    .withEstSerieValue(series.getEstSerie())
                    .withIndUnidadCorValue(series.getIdeUuid())
                    .build();
            model.getSeriesList().add(vo);
        }
        return "series/series-crud";
    }

    @ResponseBody
    @RequestMapping(value = "/seriesList", method = RequestMethod.GET)
    public TableResponse<SerieVO> listSeries() throws SystemException, BusinessException {

        int size = model.getSeriesList().size();

        return TableResponseBuilder.newBuilder()
                .withData(model.getSeriesList())
                .withiTotalDisplayRecords(size)
                .withiTotalRecords(size)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.POST)
    public HTTPResponse processSeries(@RequestBody SerieVO serie) throws BusinessException, SystemException {

        AdmMotCreacion motCreacion = EntityAdmMotCreacionBuilder.newBuilder()
                .withIdeMotCreacion(serie.getIdMotivo())
                .build();

        EntityAdmSerieBuilder builder = EntityAdmSerieBuilder.newInstance()
                .withActAdministrativo(serie.getActAdministrativo())
                .withCarAdministrativa(serie.getCarAdministrativa())
                .withCarLegal(serie.getCarLegal())
                .withCarTecnica(serie.getCarTecnica())
                .withCarContable(serie.getCarContable())
                .withCarJuridica(serie.getCarJuridica())
                .withConPublica(serie.getConPublica())
                .withConClasificada(serie.getConClasificada())
                .withConReservada(serie.getConReservada())
                .withCodSerie(serie.getCodSerie())
                .withEstSerie(Integer.valueOf(serie.getEstSerie()))
                .withFueBibliografica(serie.getFueBibliografica())
                .withNomSerie(serie.getNomSerie().trim())
                .withNotAlcance(serie.getNotAlcance())
                .withIdeMotCreacion(motCreacion)
                .withIdeUuid(serie.getIndUnidadCor());


        if (serie.getIdeSerie() != null) {
            AdmSerie entityUpdate = builder
                    .withFecCambio(new Date())
                    .withIdeSerie(serie.getIdeSerie())
                    .build();
            boundary.updateSerie(entityUpdate);
            init();
            return HTTPResponseBuilder.newBuilder()
                    .withSuccess(true)
                    .withMessage(MessageUtil.getMessage("series.series_update_successfully"))
                    .build();
        } else {
            AdmSerie entity = builder
                    .withFecCreacion(new Date())
                    .build();

            boundary.createSeries(entity);
            init();
            return HTTPResponseBuilder.newBuilder()
                    .withSuccess(true)
                    .withMessage(MessageUtil.getMessage("series.series_created_successfully"))
                    .build();
        }

    }

    @ResponseBody
    @RequestMapping(value = "/removeSerie/{ideSerie}", method = RequestMethod.DELETE)
    public HTTPResponse removeSerie(@PathVariable long ideSerie) throws BusinessException, SystemException {

        String mensaje = null;
        boolean error = false;

        boolean serieExistInCcd = boundary.serieExistByIdInCcd(BigInteger.valueOf(ideSerie));

        if (!serieExistInCcd) {
            boundary.removeSerie(BigInteger.valueOf(ideSerie));
            mensaje = MessageUtil.getMessage("series.series_remote_successfully");
            error = true;
        } else {
            mensaje = MessageUtil.getMessage("series.series_exist_version");
            error = false;
        }

        //Para refrescar la lista al eliminar
        init();
        return HTTPResponseBuilder.newBuilder()
                .withSuccess(error)
                .withMessage(mensaje)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/validateCodSerie", method = RequestMethod.POST)
    public HTTPValidResponse validateCodSerie(@RequestParam Map<String, String> requestParams) throws BusinessException, SystemException {

        if (requestParams.get("ideSerie").equals("")) {
            return HTTPValidResponse.newInstance(!boundary.serieExistByCode(requestParams.get("codSerie")));
        } else {
            return HTTPValidResponse.newInstance(true);
        }
    }

    /**
     * Validar que el nombre de la serie no se encuentre repetido en base de datos
     * version 0.1
     * mlara
     * *
     */
    @ResponseBody
    @RequestMapping(value = "/validateNomSerie", method = RequestMethod.POST)
    public HTTPValidResponse validateNomSerie(@RequestParam Map<String, String> requestParams) throws BusinessException, SystemException {
        if (!requestParams.get("ideSerie").equals("")) {
            return HTTPValidResponse.newInstance(true);
        } else {
            return HTTPValidResponse.newInstance(!boundary.serieExistByName(requestParams.get("nomSerie").trim()));
        }
    }

    @ResponseBody
    @RequestMapping(value = "/downloadExcel", method = RequestMethod.GET)
    public ResponseEntity downloadExcel() {
        String url = System.getProperty("docmanager.app.resources").concat("/series-massive-file-new.csv");
        File file = new File(url);
        Path routeFile = Paths.get(file.getAbsolutePath());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Content-Type","text/csv;charset=utf-8");
        headers.add("Content-Disposition", "attachment; filename="+url+"");

        try {
            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(routeFile));
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
            //        .contentType(MediaType.parseMediaType("text/csv;charset=utf-8"))
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return  ResponseEntity.notFound().build();
        }

    }


    @RequestMapping(value = "/generateExcel", method = RequestMethod.GET)
    public void generateExportExcel(HttpServletResponse response) throws BusinessException, SystemException {

        List<DataReportSeriesVO> dataReport = new ArrayList<>();

        List<DataTableSerieVO> rows = new ArrayList<>();

        Map<String, Object> params = new HashMap<String, Object>();
        String pathImagen = System.getProperty("docmanager.app.resources").concat("/img/logo-soaint.png");
        params.put("IMAGEN", pathImagen);

        DataReportSeriesVO data = new DataReportSeriesVO();

        for (SerieVO temp : model.getSeriesList()){

            DataTableSerieVO info;

            info = new DataTableSerieVO(temp.getCodSerie(), temp.getNomSerie(), temp.getActAdministrativo(), temp.getNombreMotCreacion(),
                    temp.getEstSerieValue(),temp.getCarAdministrativaValue(), temp.getCarLegalValue(), temp.getCarContableValue(),temp.getCarJuridicaValue(),
                    temp.getCarTecnicaValue(), temp.getConPublicaValue(), temp.getConClasificadaValue(), temp.getConReservadaValue());

            rows.add(info);
        }

        data.setRows(rows);
        dataReport.add(data);
        reportProcessor.generateReportExcel(response, "RptSeries.jrxml", dataReport, params, "TABLA_SERIES.xls");
    }
}
