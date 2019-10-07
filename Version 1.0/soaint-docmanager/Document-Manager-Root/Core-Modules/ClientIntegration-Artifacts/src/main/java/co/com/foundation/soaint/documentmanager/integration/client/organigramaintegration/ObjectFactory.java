
package co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ListarElementosDeNivelInferior_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "listarElementosDeNivelInferior");
    private final static QName _SerieDTO_QNAME = new QName("http://soaint.com/domain-artifacts/ecm/serie/1.0.0", "serieDTO");
    private final static QName _ConsultarOrganigrama_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "consultarOrganigrama");
    private final static QName _ListarElementosDeNivelInferiorResponse_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "listarElementosDeNivelInferiorResponse");
    private final static QName _SubSerieDTO_QNAME = new QName("http://soaint.com/domain-artifacts/ecm/sub-serie/1.0.0", "subSerieDTO");
    private final static QName _EstructuraTrdDTO_QNAME = new QName("http://soaint.com/domain-artifacts/ecm/estructuraTrd/1.0.0", "estructuraTrdDTO");
    private final static QName _BaseDTO_QNAME = new QName("http://soaint.com/domain-artifacts/ecm/base/1.0.0", "baseDTO");
    private final static QName _ListarDescendientesPadresResponse_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "listarDescendientesPadresResponse");
    private final static QName _ContenidoDependenciaTrdDTO_QNAME = new QName("http://soaint.com/domain-artifacts/ecm/contenidoDependencia/1.0.0", "contenidoDependenciaTrdDTO");
    private final static QName _BusinessException_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "BusinessException");
    private final static QName _SystemException_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "SystemException");
    private final static QName _OrganigramaAdministrativoDTO_QNAME = new QName("http://soaint.com/domain-artifacts/paises/1.0.0", "organigramaAdministrativoDTO");
    private final static QName _SedeDTO_QNAME = new QName("http://soaint.com/domain-artifacts/ecm/sede/1.0.0", "sedeDTO");
    private final static QName _CrearOrganigrama_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "crearOrganigrama");
    private final static QName _OrganigramaItemDTO_QNAME = new QName("http://soaint.com/domain-artifacts/cor-agente/1.0.0", "organigramaItemDTO");
    private final static QName _GenerarEstructura_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "generarEstructura");
    private final static QName _CrearOrganigramaResponse_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "crearOrganigramaResponse");
    private final static QName _ListarDescendientesPadres_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "listarDescendientesPadres");
    private final static QName _ConsultarOrganigramaResponse_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "consultarOrganigramaResponse");
    private final static QName _OrganigramaDTO_QNAME = new QName("http://soaint.com/domain-artifacts/ecm/organigrama/1.0.0", "organigramaDTO");
    private final static QName _ConsultarPadreDeSegundoNivelResponse_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "consultarPadreDeSegundoNivelResponse");
    private final static QName _ConsultarPadreDeSegundoNivel_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "consultarPadreDeSegundoNivel");
    private final static QName _GenerarEstructuraResponse_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "generarEstructuraResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: co.com.foundation.soaint.documentmanager.integration.client.organigramaintegration
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BaseDTO }
     * 
     */
    public BaseDTO createBaseDTO() {
        return new BaseDTO();
    }

    /**
     * Create an instance of {@link ConsultarPadreDeSegundoNivelResponse }
     * 
     */
    public ConsultarPadreDeSegundoNivelResponse createConsultarPadreDeSegundoNivelResponse() {
        return new ConsultarPadreDeSegundoNivelResponse();
    }

    /**
     * Create an instance of {@link ConsultarPadreDeSegundoNivel }
     * 
     */
    public ConsultarPadreDeSegundoNivel createConsultarPadreDeSegundoNivel() {
        return new ConsultarPadreDeSegundoNivel();
    }

    /**
     * Create an instance of {@link GenerarEstructuraResponse }
     * 
     */
    public GenerarEstructuraResponse createGenerarEstructuraResponse() {
        return new GenerarEstructuraResponse();
    }

    /**
     * Create an instance of {@link CrearOrganigramaResponse }
     * 
     */
    public CrearOrganigramaResponse createCrearOrganigramaResponse() {
        return new CrearOrganigramaResponse();
    }

    /**
     * Create an instance of {@link ListarDescendientesPadres }
     * 
     */
    public ListarDescendientesPadres createListarDescendientesPadres() {
        return new ListarDescendientesPadres();
    }

    /**
     * Create an instance of {@link GenerarEstructura }
     * 
     */
    public GenerarEstructura createGenerarEstructura() {
        return new GenerarEstructura();
    }

    /**
     * Create an instance of {@link ConsultarOrganigramaResponse }
     * 
     */
    public ConsultarOrganigramaResponse createConsultarOrganigramaResponse() {
        return new ConsultarOrganigramaResponse();
    }

    /**
     * Create an instance of {@link BusinessException }
     * 
     */
    public BusinessException createBusinessException() {
        return new BusinessException();
    }

    /**
     * Create an instance of {@link ListarDescendientesPadresResponse }
     * 
     */
    public ListarDescendientesPadresResponse createListarDescendientesPadresResponse() {
        return new ListarDescendientesPadresResponse();
    }

    /**
     * Create an instance of {@link SystemException }
     * 
     */
    public SystemException createSystemException() {
        return new SystemException();
    }

    /**
     * Create an instance of {@link CrearOrganigrama }
     * 
     */
    public CrearOrganigrama createCrearOrganigrama() {
        return new CrearOrganigrama();
    }

    /**
     * Create an instance of {@link ConsultarOrganigrama }
     * 
     */
    public ConsultarOrganigrama createConsultarOrganigrama() {
        return new ConsultarOrganigrama();
    }

    /**
     * Create an instance of {@link ListarElementosDeNivelInferior }
     * 
     */
    public ListarElementosDeNivelInferior createListarElementosDeNivelInferior() {
        return new ListarElementosDeNivelInferior();
    }

    /**
     * Create an instance of {@link ListarElementosDeNivelInferiorResponse }
     * 
     */
    public ListarElementosDeNivelInferiorResponse createListarElementosDeNivelInferiorResponse() {
        return new ListarElementosDeNivelInferiorResponse();
    }

    /**
     * Create an instance of {@link OrganigramaItemDTO }
     * 
     */
    public OrganigramaItemDTO createOrganigramaItemDTO() {
        return new OrganigramaItemDTO();
    }

    /**
     * Create an instance of {@link OrganigramaAdministrativoDTO }
     * 
     */
    public OrganigramaAdministrativoDTO createOrganigramaAdministrativoDTO() {
        return new OrganigramaAdministrativoDTO();
    }

    /**
     * Create an instance of {@link ContenidoDependenciaTrdDTO }
     * 
     */
    public ContenidoDependenciaTrdDTO createContenidoDependenciaTrdDTO() {
        return new ContenidoDependenciaTrdDTO();
    }

    /**
     * Create an instance of {@link EstructuraTrdDTO }
     * 
     */
    public EstructuraTrdDTO createEstructuraTrdDTO() {
        return new EstructuraTrdDTO();
    }

    /**
     * Create an instance of {@link SubSerieDTO }
     * 
     */
    public SubSerieDTO createSubSerieDTO() {
        return new SubSerieDTO();
    }

    /**
     * Create an instance of {@link SerieDTO }
     * 
     */
    public SerieDTO createSerieDTO() {
        return new SerieDTO();
    }

    /**
     * Create an instance of {@link OrganigramaDTO }
     * 
     */
    public OrganigramaDTO createOrganigramaDTO() {
        return new OrganigramaDTO();
    }

    /**
     * Create an instance of {@link SedeDTO }
     * 
     */
    public SedeDTO createSedeDTO() {
        return new SedeDTO();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListarElementosDeNivelInferior }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.com.soaint.sgd.correspondencia.service", name = "listarElementosDeNivelInferior")
    public JAXBElement<ListarElementosDeNivelInferior> createListarElementosDeNivelInferior(ListarElementosDeNivelInferior value) {
        return new JAXBElement<ListarElementosDeNivelInferior>(_ListarElementosDeNivelInferior_QNAME, ListarElementosDeNivelInferior.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SerieDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soaint.com/domain-artifacts/ecm/serie/1.0.0", name = "serieDTO")
    public JAXBElement<SerieDTO> createSerieDTO(SerieDTO value) {
        return new JAXBElement<SerieDTO>(_SerieDTO_QNAME, SerieDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarOrganigrama }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.com.soaint.sgd.correspondencia.service", name = "consultarOrganigrama")
    public JAXBElement<ConsultarOrganigrama> createConsultarOrganigrama(ConsultarOrganigrama value) {
        return new JAXBElement<ConsultarOrganigrama>(_ConsultarOrganigrama_QNAME, ConsultarOrganigrama.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListarElementosDeNivelInferiorResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.com.soaint.sgd.correspondencia.service", name = "listarElementosDeNivelInferiorResponse")
    public JAXBElement<ListarElementosDeNivelInferiorResponse> createListarElementosDeNivelInferiorResponse(ListarElementosDeNivelInferiorResponse value) {
        return new JAXBElement<ListarElementosDeNivelInferiorResponse>(_ListarElementosDeNivelInferiorResponse_QNAME, ListarElementosDeNivelInferiorResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubSerieDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soaint.com/domain-artifacts/ecm/sub-serie/1.0.0", name = "subSerieDTO")
    public JAXBElement<SubSerieDTO> createSubSerieDTO(SubSerieDTO value) {
        return new JAXBElement<SubSerieDTO>(_SubSerieDTO_QNAME, SubSerieDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EstructuraTrdDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soaint.com/domain-artifacts/ecm/estructuraTrd/1.0.0", name = "estructuraTrdDTO")
    public JAXBElement<EstructuraTrdDTO> createEstructuraTrdDTO(EstructuraTrdDTO value) {
        return new JAXBElement<EstructuraTrdDTO>(_EstructuraTrdDTO_QNAME, EstructuraTrdDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BaseDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soaint.com/domain-artifacts/ecm/base/1.0.0", name = "baseDTO")
    public JAXBElement<BaseDTO> createBaseDTO(BaseDTO value) {
        return new JAXBElement<BaseDTO>(_BaseDTO_QNAME, BaseDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListarDescendientesPadresResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.com.soaint.sgd.correspondencia.service", name = "listarDescendientesPadresResponse")
    public JAXBElement<ListarDescendientesPadresResponse> createListarDescendientesPadresResponse(ListarDescendientesPadresResponse value) {
        return new JAXBElement<ListarDescendientesPadresResponse>(_ListarDescendientesPadresResponse_QNAME, ListarDescendientesPadresResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ContenidoDependenciaTrdDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soaint.com/domain-artifacts/ecm/contenidoDependencia/1.0.0", name = "contenidoDependenciaTrdDTO")
    public JAXBElement<ContenidoDependenciaTrdDTO> createContenidoDependenciaTrdDTO(ContenidoDependenciaTrdDTO value) {
        return new JAXBElement<ContenidoDependenciaTrdDTO>(_ContenidoDependenciaTrdDTO_QNAME, ContenidoDependenciaTrdDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BusinessException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.com.soaint.sgd.correspondencia.service", name = "BusinessException")
    public JAXBElement<BusinessException> createBusinessException(BusinessException value) {
        return new JAXBElement<BusinessException>(_BusinessException_QNAME, BusinessException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SystemException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.com.soaint.sgd.correspondencia.service", name = "SystemException")
    public JAXBElement<SystemException> createSystemException(SystemException value) {
        return new JAXBElement<SystemException>(_SystemException_QNAME, SystemException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrganigramaAdministrativoDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soaint.com/domain-artifacts/paises/1.0.0", name = "organigramaAdministrativoDTO")
    public JAXBElement<OrganigramaAdministrativoDTO> createOrganigramaAdministrativoDTO(OrganigramaAdministrativoDTO value) {
        return new JAXBElement<OrganigramaAdministrativoDTO>(_OrganigramaAdministrativoDTO_QNAME, OrganigramaAdministrativoDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SedeDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soaint.com/domain-artifacts/ecm/sede/1.0.0", name = "sedeDTO")
    public JAXBElement<SedeDTO> createSedeDTO(SedeDTO value) {
        return new JAXBElement<SedeDTO>(_SedeDTO_QNAME, SedeDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CrearOrganigrama }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.com.soaint.sgd.correspondencia.service", name = "crearOrganigrama")
    public JAXBElement<CrearOrganigrama> createCrearOrganigrama(CrearOrganigrama value) {
        return new JAXBElement<CrearOrganigrama>(_CrearOrganigrama_QNAME, CrearOrganigrama.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrganigramaItemDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soaint.com/domain-artifacts/cor-agente/1.0.0", name = "organigramaItemDTO")
    public JAXBElement<OrganigramaItemDTO> createOrganigramaItemDTO(OrganigramaItemDTO value) {
        return new JAXBElement<OrganigramaItemDTO>(_OrganigramaItemDTO_QNAME, OrganigramaItemDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerarEstructura }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.com.soaint.sgd.correspondencia.service", name = "generarEstructura")
    public JAXBElement<GenerarEstructura> createGenerarEstructura(GenerarEstructura value) {
        return new JAXBElement<GenerarEstructura>(_GenerarEstructura_QNAME, GenerarEstructura.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CrearOrganigramaResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.com.soaint.sgd.correspondencia.service", name = "crearOrganigramaResponse")
    public JAXBElement<CrearOrganigramaResponse> createCrearOrganigramaResponse(CrearOrganigramaResponse value) {
        return new JAXBElement<CrearOrganigramaResponse>(_CrearOrganigramaResponse_QNAME, CrearOrganigramaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListarDescendientesPadres }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.com.soaint.sgd.correspondencia.service", name = "listarDescendientesPadres")
    public JAXBElement<ListarDescendientesPadres> createListarDescendientesPadres(ListarDescendientesPadres value) {
        return new JAXBElement<ListarDescendientesPadres>(_ListarDescendientesPadres_QNAME, ListarDescendientesPadres.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarOrganigramaResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.com.soaint.sgd.correspondencia.service", name = "consultarOrganigramaResponse")
    public JAXBElement<ConsultarOrganigramaResponse> createConsultarOrganigramaResponse(ConsultarOrganigramaResponse value) {
        return new JAXBElement<ConsultarOrganigramaResponse>(_ConsultarOrganigramaResponse_QNAME, ConsultarOrganigramaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrganigramaDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soaint.com/domain-artifacts/ecm/organigrama/1.0.0", name = "organigramaDTO")
    public JAXBElement<OrganigramaDTO> createOrganigramaDTO(OrganigramaDTO value) {
        return new JAXBElement<OrganigramaDTO>(_OrganigramaDTO_QNAME, OrganigramaDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarPadreDeSegundoNivelResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.com.soaint.sgd.correspondencia.service", name = "consultarPadreDeSegundoNivelResponse")
    public JAXBElement<ConsultarPadreDeSegundoNivelResponse> createConsultarPadreDeSegundoNivelResponse(ConsultarPadreDeSegundoNivelResponse value) {
        return new JAXBElement<ConsultarPadreDeSegundoNivelResponse>(_ConsultarPadreDeSegundoNivelResponse_QNAME, ConsultarPadreDeSegundoNivelResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsultarPadreDeSegundoNivel }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.com.soaint.sgd.correspondencia.service", name = "consultarPadreDeSegundoNivel")
    public JAXBElement<ConsultarPadreDeSegundoNivel> createConsultarPadreDeSegundoNivel(ConsultarPadreDeSegundoNivel value) {
        return new JAXBElement<ConsultarPadreDeSegundoNivel>(_ConsultarPadreDeSegundoNivel_QNAME, ConsultarPadreDeSegundoNivel.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerarEstructuraResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.com.soaint.sgd.correspondencia.service", name = "generarEstructuraResponse")
    public JAXBElement<GenerarEstructuraResponse> createGenerarEstructuraResponse(GenerarEstructuraResponse value) {
        return new JAXBElement<GenerarEstructuraResponse>(_GenerarEstructuraResponse_QNAME, GenerarEstructuraResponse.class, null, value);
    }

}
