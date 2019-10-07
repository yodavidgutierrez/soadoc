package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorPlanAgen;
import co.com.soaint.correspondencia.domain.entity.CorPlanillas;
import co.com.soaint.correspondencia.domain.entity.TvsOrganigramaAdministrativo;
import co.com.soaint.foundation.canonical.correspondencia.*;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoDistribucionFisicaEnum;
import co.com.soaint.foundation.canonical.correspondencia.constantes.EstadoPlanillaEnum;
import co.com.soaint.foundation.canonical.correspondencia.constantes.FormatoDocEnum;
import co.com.soaint.foundation.canonical.correspondencia.constantes.TipoRemitenteEnum;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.components.util.ExceptionBuilder;
import co.com.soaint.foundation.framework.exceptions.BusinessException;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * SGD Enterprise Services
 * Created: 04-Sep-2017
 * Author: esanchez
 * Type: JAVA class Artifact
 * Purpose: CONTROL - business component services
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 */
@BusinessControl
@Log4j2
public class PlanillasControl {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private PlanAgenControl planAgenControl;

    @Autowired
    private CorrespondenciaControl correspondenciaControl;

    @Autowired
    private PpdDocumentoControl ppdDocumentoControl;

    @Autowired
    private AgenteControl agenteControl;

    @Autowired
    private ConstantesControl constantesControl;

    @Autowired
    private DependenciaControl dependenciaControl;

    @Autowired
    private FuncionariosControl funcionariosControl;

    @Autowired
    private DatosContactoControl datosContactoControl;

    @Value("${radicado.reports.path}")
    private String reportsPath;

    @Value("${radicado.reports.logo}")
    private String reportsLogo;

    @Value("${radicado.planilla.distribucion.report}")
    private String planillaDistribucionReport;
    @Value("${radicado.planilla.distribucion.report.salida}")
    private String planillaDistribucionReportSalida;
    @Value("${radicado.planilla.distribucion.report.salida.interna}")
    private String planillaDistribucionReportSalidaInterna;

    @Value("${radicado.tipo.persona.juridica}")
    private String tipoPersonaJuridica;

    /**
     * @param planilla
     * @return
     * @throws SystemException
     */
    public PlanillaDTO generarPlanilla(PlanillaDTO planilla) throws SystemException {
        try {
            CorPlanillas corPlanillas = corPlanillasTransform(planilla);
            corPlanillas.setFecGeneracion(new Date());
            corPlanillas.setNroPlanilla(generarNumeroPlanilla(corPlanillas.getCodSedeOrigen(), corPlanillas.getFecGeneracion()));
            corPlanillas.setCorPlanAgenList(new ArrayList<>());
            for (PlanAgenDTO planAgenDTO : planilla.getPAgentes().getPAgente()) {
                CorPlanAgen corPlanAgen = planAgenControl.corPlanAgenTransform(planAgenDTO);
                corPlanAgen.setEstado(EstadoPlanillaEnum.DISTRIBUCION.getCodigo());
                corPlanAgen.setCorPlanillas(corPlanillas);
                corPlanillas.getCorPlanAgenList().add(corPlanAgen);
                agenteControl.actualizarEstadoDistribucion(planAgenDTO.getIdeAgente(), EstadoDistribucionFisicaEnum.EMPLANILLADO.getCodigo());
            }
            log.info("generarPlanilla antes de persistir----------------- {}", corPlanillas);

            em.persist(corPlanillas);
            em.flush();
            log.info("listarPlanillasByNroPlanilla request----------------- {},{}", corPlanillas.getNroPlanilla(), EstadoPlanillaEnum.DISTRIBUCION.getCodigo());
            return listarPlanillasByNroPlanilla(corPlanillas.getNroPlanilla(), EstadoPlanillaEnum.DISTRIBUCION.getCodigo());
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex.getMessage());
            throw new SystemException("system.generic.error --- " + ex.getMessage());
        }
    }

    /**
     * @param planilla
     * @throws SystemException
     */
    public void cargarPlanilla(PlanillaDTO planilla) throws SystemException {
        try {
            em.createNamedQuery("CorPlanillas.updateReferenciaEcm")
                    .setParameter("IDE_ECM", planilla.getIdeEcm())
                    .setParameter("IDE_PLANILLA", planilla.getIdePlanilla())
                    .executeUpdate();
            for (PlanAgenDTO planAgenDTO : planilla.getPAgentes().getPAgente()) {
                planAgenControl.updateEstadoDistribucion(planAgenDTO);
            }
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param nroPlanilla
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public PlanillaDTO listarPlanillasByNroPlanilla(String nroPlanilla, String estado) throws SystemException {
        try {
            List<PlanillaDTO> planillas = em.createNamedQuery("CorPlanillas.findByNroPlanilla", PlanillaDTO.class)
                    .setParameter("NRO_PLANILLA", nroPlanilla)
                    .getResultList();
            log.info("este es el resultado de la query  que acabo de hacer{}", planillas);
            if (!ObjectUtils.isEmpty(planillas)) {
                PlanillaDTO planilla = planillas.get(0);
                planilla.setPAgentes(PlanAgentesDTO.newInstance()
                        .pAgente(planAgenControl.listarAgentesByIdePlanilla(planilla.getIdePlanilla(), estado))
                        .build());
                planilla.setNroPlanilla(nroPlanilla);
                return planilla;
            } else throw new SystemException("no se encontró planilla por ese número");
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw new SystemException("Business Control - a system error has occurred {}" + ex.getMessage(), ex);
        }
    }

    /**
     * @param nroPlanilla
     * @return
     * @throws BusinessException
     * @throws SystemException
     */
    public PlanillaSalidaDTO listarPlanillasSalidaByNroPlanilla(String nroPlanilla, String estado) throws BusinessException, SystemException {
        try {
            PlanillaDTO planilla = em.createNamedQuery("CorPlanillas.findByNroPlanilla", PlanillaDTO.class)
                    .setParameter("NRO_PLANILLA", nroPlanilla)
                    .getSingleResult();

            PlanillaSalidaDTO planillaSalida = PlanillaDTOTransformToPlanillaSalidaDTO(planilla);
            planillaSalida.setPAgentes(PlanAgentesSalidaDTO.newInstance()
                    .pAgente(planAgenControl.listarAgentesSalidaByIdePlanilla(planilla.getIdePlanilla(), estado))
                    .build());
            log.info("la planilla salida dto------------------------------{}",planillaSalida);
            return planillaSalida;
        } catch (NoResultException n) {
            log.error("Business Control - a business error has occurred", n);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("planillas.planilla_not_exist_by_nroPlanilla")
                    .withRootException(n)
                    .buildBusinessException();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred", ex);
            throw ExceptionBuilder.newBuilder()
                    .withMessage("system.generic.error")
                    .withRootException(ex)
                    .buildSystemException();
        }
    }

    /**
     * @param nroPlanilla
     * @param formato
     * @return
     * @throws SystemException
     */
    public ReportDTO exportarPlanilla(String nroPlanilla, String formato) throws SystemException {
        return exportar(nroPlanilla, formato, "EE");
    }

    public ReportDTO exportarPlanillaSalida(String nroPlanilla, String formato, String tipoComunicacion) throws SystemException {
        return exportar(nroPlanilla, formato, tipoComunicacion);
    }

    private ReportDTO exportar(String nroPlanilla, String formato, String tipoComunicacion) throws SystemException {
        try {
            PlanillaDTO planilla = listarPlanillasByNroPlanilla(nroPlanilla, null);
            log.info("######### iniciando exportar planillas  para ver la planilla{}", planilla);
            planilla.setNroPlanilla(nroPlanilla);
            JasperReport report;
            if ("SE".equals(tipoComunicacion)) {
                log.info("Entro en la salida externa----------------------------------------SE");
                report = JasperCompileManager.compileReport(reportsPath + planillaDistribucionReportSalida);
            }else
            if ("SI".equals(tipoComunicacion)) {
                log.info("Entro en la salida interna----------------------------------------SI");
                report = JasperCompileManager.compileReport(reportsPath + planillaDistribucionReportSalidaInterna);
            }
            else {
                log.info("Entro en la entrada externa----------------------------------------EE");
                report = JasperCompileManager.compileReport(reportsPath + planillaDistribucionReport);
            }
            byte[] bytes;
            if (FormatoDocEnum.PDF.getCodigo().equals(formato)) {
                bytes = getPdfReport(report, planilla, tipoComunicacion);
            } else {
                bytes = getXlsReport(report, planilla, tipoComunicacion);
            }
            return ReportDTO.newInstance()
                    .base64EncodedFile(Base64.getEncoder().encodeToString(bytes))
                    .formato(formato)
                    .build();
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw new SystemException("Business Control - a system error has occurred {} " + ex.getMessage(), ex);
        }
    }

    private byte[] getPdfReport(JasperReport report, PlanillaDTO planilla, String tipoComunicacion) throws SystemException {
        try {
            return JasperRunManager.runReportToPdf(report, getReportParameters(planilla), getReportDataSource(planilla, tipoComunicacion));
        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw new SystemException("Business Control - a system error has occurred {} " + ex.getMessage(), ex);
        }
    }

    private byte[] getXlsReport(JasperReport report, PlanillaDTO planilla, String tipoComunicacion) throws SystemException, IOException {
        FileInputStream fileInputStream = null;
        try {
            File fileTmp = File.createTempFile(planilla.getNroPlanilla() + ".xslx", "");
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, getReportParameters(planilla), getReportDataSource(planilla, tipoComunicacion));
            JRXlsxExporter xlsxExporter = new JRXlsxExporter();
            xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(fileTmp.getPath()));
            SimpleXlsxReportConfiguration xlsxReportConfiguration = new SimpleXlsxReportConfiguration();
            xlsxReportConfiguration.setOnePagePerSheet(false);
            xlsxReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
            xlsxReportConfiguration.setRemoveEmptySpaceBetweenColumns(true);
            xlsxReportConfiguration.setDetectCellType(false);
            xlsxReportConfiguration.setWhitePageBackground(false);
            xlsxReportConfiguration.setWrapText(true);
            xlsxExporter.setConfiguration(xlsxReportConfiguration);
            xlsxExporter.exportReport();

            fileInputStream = new FileInputStream(fileTmp);

            fileTmp.deleteOnExit();
            return IOUtils.toByteArray(fileInputStream);

        } catch (Exception ex) {
            log.error("Business Control - a system error has occurred {}", ex);
            throw new SystemException("Business Control - a system error has occurred {} " + ex.getMessage(), ex);
        } finally {
            if (fileInputStream != null)
                fileInputStream.close();
        }
    }

    private Map<String, Object> getReportParameters(PlanillaDTO planilla) throws BusinessException, SystemException, IOException, ParseException {
        DependenciaDTO dependenciaOrigen = dependenciaControl.listarDependenciaByCodigo(planilla.getCodDependenciaOrigen());
        OrganigramaItemDTO dependenciaRadicadora = dependenciaControl.obtenerDependenciaRadicadora();
        BufferedImage image = ImageIO.read(new FileImageInputStream(new File(reportsPath + reportsLogo)));
        //TODO: puede ser que el primer o segundo apellido venga vacio, revisar como proceder.
        log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% esta es la planilla: {}", planilla);
        FuncionarioDTO funcionario = funcionariosControl.consultarFuncionarioByIdeFunci(new BigInteger(planilla.getCodFuncGenera()));
        String nombreCompleto = funcionario.getNomFuncionario() + " " + (StringUtils.isEmpty(funcionario.getValApellido1()) ? "" : funcionario.getValApellido1())
                + " " + (StringUtils.isEmpty(funcionario.getValApellido2()) ? "" : funcionario.getValApellido2());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateSring = format.format(planilla.getFecGeneracion());
        Date fecha = format.parse(dateSring);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("lugarAdministrativo", dependenciaOrigen.getNomSede());
        parameters.put("dependenciaDestino", dependenciaControl.listarDependenciaByCodigo(planilla.getCodDependenciaDestino()).getNomDependencia());
       // parameters.put("responsable", dependenciaOrigen.getNomDependencia());
        parameters.put("responsable", dependenciaRadicadora.getNomOrg());
        parameters.put("nroPlanilla", planilla.getNroPlanilla());
        parameters.put("fecGeneracion", fecha);
        parameters.put("funcGenera", nombreCompleto);
        parameters.put("modalidadCorreo", planilla.getCodModalidadEnvio());
        parameters.put("claseEnvio", planilla.getCodClaseEnvio());
        parameters.put("logo", image);
        return parameters;
    }

    private JRBeanCollectionDataSource getReportDataSource(PlanillaDTO planilla, String tipoComunicacion) throws BusinessException, SystemException {
        List<ItemReportPlanillaDTO> itemPlanillaList = new ArrayList<>();
        FuncionarioDTO funcionario = funcionariosControl.consultarFuncionarioByIdeFunci(new BigInteger(planilla.getCodFuncGenera()));
        log.info("Este es el funcionario que se va a enviar------------------------------------------------------{}", funcionario);
        for (PlanAgenDTO planAgen : planilla.getPAgentes().getPAgente()) {
            itemPlanillaList.add(transformToItemReport(planAgen, tipoComunicacion, funcionario));
        }
        List<ItemsReportPlanillaDTO> dataSource = new ArrayList<>();
        dataSource.add(ItemsReportPlanillaDTO.newInstance().itemsPlanilla(itemPlanillaList).build());
        return new JRBeanCollectionDataSource(dataSource, false);
    }

    private ItemReportPlanillaDTO transformToItemReport(PlanAgenDTO planAgen, String tipoComunicacion, FuncionarioDTO funcionario) throws BusinessException, SystemException {
        log.info("planAgen-----------------------------------------------------------------------------{}", planAgen);
        CorrespondenciaDTO correspondencia = correspondenciaControl.consultarCorrespondenciaByIdeDocumento(planAgen.getIdeDocumento());
        PpdDocumentoDTO documento = ppdDocumentoControl.consultarPpdDocumentosByCorrespondencia(correspondencia.getIdeDocumento()).get(0);
        AgenteDTO remitente = agenteControl.listarRemitentesByIdeDocumento(correspondencia.getIdeDocumento()).get(0);
        List<DatosContactoDTO> contactoDTOList = datosContactoControl.consultarDatosContactoByIdAgente(planAgen.getIdeAgente());
        AgenteDTO destinatario = agenteControl.consultarAgenteByIdeAgente(planAgen.getIdeAgente());
        log.info("***************************************************** este es el destinatario: {}", destinatario);
        log.info("----------------------------------------------------- este es el remitente: {}", remitente);
        log.info("///////////////////////////////////////////////////// esta es la correspondencia: {}", correspondencia);
        log.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++ este es el documento: {}", documento);
        log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ este es el funcionario: {}", funcionario);
        String nombreRemitente = "";
        String nroDocumento = "";
        String pais = "";
        String departamento = "";
        String municipio = "";


        if (TipoRemitenteEnum.EXTERNO.getCodigo().equals(remitente.getCodTipoRemite())) {
            nroDocumento = tipoPersonaJuridica.equals(remitente.getCodTipoPers()) ? remitente.getNit() : remitente.getNroDocuIdentidad();
            nombreRemitente = tipoPersonaJuridica.equals(remitente.getCodTipoPers()) ? remitente.getRazonSocial() : remitente.getNombre();
        }
        String nroRadicado = correspondencia.getNroRadicado();
        if (nroRadicado.contains("--")){
        String[] radicados = nroRadicado.split("--");
        nroRadicado = radicados[1];
        }
        ConstanteDTO constanteDTO = new ConstanteDTO();
        if ("EE".equalsIgnoreCase(tipoComunicacion)) {
            if (!ObjectUtils.isEmpty(documento) && !ObjectUtils.isEmpty(documento.getCodTipoDoc())) {
                constanteDTO = constantesControl.consultarConstanteByCodigo(documento.getCodTipoDoc());
            }

            return ItemReportPlanillaDTO.newInstance()
                    .nroRadicado(nroRadicado)
                    .fecRadicado(correspondencia.getFecRadicado())
                    .indOriginal(StringUtils.isEmpty(destinatario.getIndOriginal()) ? "" : constantesControl.consultarConstanteByCodigo(destinatario.getIndOriginal()).getNombre())
                    .nroDocumento(nroDocumento)
                    .nombreRemitente(nombreRemitente)
                    .dependenciaOrigen(constanteDTO.getNombre())
                    .asunto(StringUtils.isEmpty(documento.getAsunto()) ? "" : documento.getAsunto())
                    .nroFolios(String.valueOf(documento.getNroFolios()))
                    .nroAnexos(String.valueOf(documento.getNroAnexos()))
                    .build();
        }
        if ("SE".equalsIgnoreCase(tipoComunicacion)) {
            String direccion = "";
            if (!ObjectUtils.isEmpty(contactoDTOList)) {
                JSONObject jsonObject = new JSONObject(contactoDTOList.get(0).getDireccion());

                String tipoVia = "";
                String noViaPrincipal = "";
                String orientacion = "";
                String prefijoCuadrante_se = "";
                String placa = "";
                String complementoTipo = "";
                String complementoAdicional = "";
                String noVia = "";
                String BIS = "";
                if (jsonObject.has("tipoVia")) {
                    JSONObject tipoViaObject = jsonObject.getJSONObject("tipoVia");
                    tipoVia = tipoViaObject.getString("nombre");
                    tipoVia = StringUtils.isEmpty(tipoVia) ? "" : " " + tipoVia;
                }
                if (jsonObject.has("noViaPrincipal")) {
                    noViaPrincipal = jsonObject.getString("noViaPrincipal");
                    noViaPrincipal = StringUtils.isEmpty(noViaPrincipal) ? "" : " " + noViaPrincipal;
                }
                if (jsonObject.has("orientacion")) {
                    JSONObject orientacionObject = jsonObject.getJSONObject("orientacion");
                    orientacion = orientacionObject.getString("nombre");
                    orientacion = StringUtils.isEmpty(orientacion) ? "" : " " + orientacion;
                }
                if (jsonObject.has("noVia")) {
                    noVia = jsonObject.getString("noVia");
                    noVia = StringUtils.isEmpty(noVia) ? "" : " " + noVia;
                }
                if (jsonObject.has("prefijoCuadrante_se")) {
                    JSONObject prefijoCuadrante_seObject = jsonObject.getJSONObject("prefijoCuadrante_se");
                    prefijoCuadrante_se = prefijoCuadrante_seObject.getString("nombre");
                    prefijoCuadrante_se = StringUtils.isEmpty(prefijoCuadrante_se) ? "" : " " + prefijoCuadrante_se;
                }
                if (jsonObject.has("placa")) {
                    placa = jsonObject.getString("placa");
                    placa = StringUtils.isEmpty(placa) ? "" : " " + placa;
                }
                if (jsonObject.has("BIS")) {
                    BIS = jsonObject.getString("BIS");
                    BIS = StringUtils.isEmpty(BIS) ? "" : " " + BIS;
                }
                if (jsonObject.has("complementoTipo")) {
                    JSONObject complementoTipoObject = jsonObject.getJSONObject("complementoTipo");
                    complementoTipo = complementoTipoObject.getString("nombre");
                    complementoTipo = StringUtils.isEmpty(complementoTipo) ? "" : " " + complementoTipo;
                }
                if (jsonObject.has("complementoAdicional")) {
                    complementoAdicional = jsonObject.getString("complementoAdicional");
                    complementoAdicional = StringUtils.isEmpty(complementoAdicional) ? "" : " " + complementoAdicional;
                }

                direccion = "".concat(tipoVia).concat(noViaPrincipal).concat(prefijoCuadrante_se).concat(BIS).concat(orientacion).concat(noVia).concat(placa).concat(complementoTipo).concat(complementoAdicional);

                log.info("++++++++++++++++++ esta es la direccion: {}", direccion);

                if (!StringUtils.isEmpty(contactoDTOList.get(0).getCodPais())) {
                    List<String> paises = em.createNamedQuery("TvsPais.findPaisByCodigo", String.class)
                            .setParameter("COD_PAIS", contactoDTOList.get(0).getCodPais())
                            .getResultList();
                    if (!ObjectUtils.isEmpty(paises)) {
                        pais = paises.get(0);
                    }
                }
                if (!StringUtils.isEmpty(contactoDTOList.get(0).getCodDepartamento())) {
                    List<String> departamentos = em.createNamedQuery("TvsDepartamento.findByCodDepart", String.class)
                            .setParameter("COD_DEPART", contactoDTOList.get(0).getCodDepartamento())
                            .getResultList();
                    if (!ObjectUtils.isEmpty(departamentos)) {
                        departamento = departamentos.get(0);
                    }

                }
                if (!StringUtils.isEmpty(contactoDTOList.get(0).getCodMunicipio())) {
                    List<String> municipios = em.createNamedQuery("TvsMunicipio.findByCodMunic", String.class)
                            .setParameter("COD_MUNI", contactoDTOList.get(0).getCodMunicipio())
                            .getResultList();
                    if (!ObjectUtils.isEmpty(municipios)) {
                        municipio = municipios.get(0);
                    }

                }
            }

            return ItemReportPlanillaDTO.newInstance()
                    .nroRadicado(nroRadicado)
                    .fecRadicado(correspondencia.getFecRadicado())
                    .indOriginal(StringUtils.isEmpty(documento.getCodTipoDoc()) ? "" : constantesControl.consultarConstanteByCodigo(documento.getCodTipoDoc()).getNombre())
                    .nroDocumento(destinatario.getNroDocuIdentidad())
                    .nombreRemitente(destinatario.getNombre())
                    .dependenciaOrigen(dependenciaControl.listarDependenciaByCodigo(remitente.getCodDependencia()).getNomDependencia())
                    .asunto(StringUtils.isEmpty(documento.getAsunto()) ? "" : documento.getAsunto())
                    .nroFolios(String.valueOf(documento.getNroFolios()))
                    .nroAnexos(String.valueOf(documento.getNroAnexos()))
                    .pais(StringUtils.isEmpty(pais) ? "" : pais)
                    .departamento(StringUtils.isEmpty(departamento) ? "" : departamento)
                    .municipio(StringUtils.isEmpty(pais) ? "" : municipio)
                    .direccion(direccion)
                    .build();

        }
        if ("SI".equalsIgnoreCase(tipoComunicacion)) {
            if (!ObjectUtils.isEmpty(documento) && !ObjectUtils.isEmpty(documento.getCodTipoDoc())) {
                constanteDTO = constantesControl.consultarConstanteByCodigo(documento.getCodTipoDoc());
            }

            return ItemReportPlanillaDTO.newInstance()
                    .nroRadicado(nroRadicado)
                    .fecRadicado(correspondencia.getFecRadicado())
                    .indOriginal(StringUtils.isEmpty(destinatario.getIndOriginal()) ? "" : constantesControl.consultarConstanteByCodigo(destinatario.getIndOriginal()).getNombre())
                    .depProductora(ObjectUtils.isEmpty(correspondencia.getCodDependencia()) ? "" : dependenciaControl.consultarOrganigramaCodigo(correspondencia.getCodDependencia()).getNomOrg())
                    .nombreRemitente(ObjectUtils.isEmpty(funcionario) ? "" : funcionario.getNomFuncionario())
                    .dependenciaOrigen(constanteDTO.getNombre())
                    .asunto(StringUtils.isEmpty(documento.getAsunto()) ? "" : documento.getAsunto())
                    .nroFolios(documento.getNroFolios() == null ? "" : String.valueOf(documento.getNroFolios()))
                    .nroAnexos(StringUtils.isEmpty(String.valueOf(documento.getNroAnexos())) ? "" : String.valueOf(documento.getNroAnexos()))
                    .build();
        }
        throw new SystemException("El tipo de comunicación no es correcto ( SE, SI, EE)");
    }
    /**
     * @param planilla
     * @return
     */
    public CorPlanillas corPlanillasTransform(PlanillaDTO planilla) {
        return CorPlanillas.newInstance()
                .idePlanilla(planilla.getIdePlanilla())
                .nroPlanilla(planilla.getNroPlanilla())
                .fecGeneracion(planilla.getFecGeneracion())
                .codTipoPlanilla(planilla.getCodTipoPlanilla())
                .codFuncGenera(planilla.getCodFuncGenera())
                .codSedeOrigen(planilla.getCodSedeOrigen())
                .codDependenciaOrigen(planilla.getCodDependenciaOrigen())
                .codSedeDestino(planilla.getCodSedeDestino())
                .codDependenciaDestino(planilla.getCodDependenciaDestino())
                .codClaseEnvio(planilla.getCodClaseEnvio())
                .codModalidadEnvio(planilla.getCodModalidadEnvio())
                .ideEcm(planilla.getIdeEcm())
                .build();
    }

    /**
     * @param planilla
     * @return
     */
    public PlanillaSalidaDTO PlanillaDTOTransformToPlanillaSalidaDTO(PlanillaDTO planilla) {
        return PlanillaSalidaDTO.newInstance()
                .idePlanilla(planilla.getIdePlanilla())
                .nroPlanilla(planilla.getNroPlanilla())
                .fecGeneracion(planilla.getFecGeneracion())
                .codTipoPlanilla(planilla.getCodTipoPlanilla())
                .codFuncGenera(planilla.getCodFuncGenera())
                .codSedeOrigen(planilla.getCodSedeOrigen())
                .codDependenciaOrigen(planilla.getCodDependenciaOrigen())
                .codSedeDestino(planilla.getCodSedeDestino())
                .codDependenciaDestino(planilla.getCodDependenciaDestino())
                .codClaseEnvio(planilla.getCodClaseEnvio())
                .codModalidadEnvio(planilla.getCodModalidadEnvio())
                .ideEcm(planilla.getIdeEcm())
                .build();
    }

    private String generarNumeroPlanilla(String codSede, Date fechaGeneracion) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fechaGeneracion);
        String anno = String.valueOf(calendario.get(Calendar.YEAR));

        String nroPlanilla = em.createNamedQuery("CorPlanillas.findMaxNroPlanillaByCodSede", String.class)
                .setParameter("COD_SEDE", codSede)
                .getSingleResult();

        int consecutivo = 0;
        if (nroPlanilla != null) {
            String annoP = nroPlanilla.substring(codSede.length(), codSede.length() + anno.length());
            if (anno.equals(annoP))
                consecutivo = Integer.parseInt(nroPlanilla.substring(codSede.length() + annoP.length()));
        }
        consecutivo++;
        return conformarNroPlanilla(codSede, anno, consecutivo);
    }

    private String conformarNroPlanilla(String codSede, String anno, int consecutivo) {
        String nro = codSede + anno;
        String formato = "%0" + 6 + "d";
        return nro.concat(String.format(formato, consecutivo));
    }
}
