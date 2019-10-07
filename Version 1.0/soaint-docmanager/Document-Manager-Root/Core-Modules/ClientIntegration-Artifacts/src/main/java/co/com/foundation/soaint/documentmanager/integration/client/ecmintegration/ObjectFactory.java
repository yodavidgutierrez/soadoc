
package co.com.foundation.soaint.documentmanager.integration.client.ecmintegration;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the co.com.foundation.soaint.documentmanager.integration.client.ecmintegration package. 
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

    private final static QName _CreateStructureECMResponse_QNAME = new QName("http://co.com.soaint.ecm.integration.service.ws", "createStructureECMResponse");
    private final static QName _SerieDTO_QNAME = new QName("http://soaint.com/domain-artifacts/ecm/serie/1.0.0", "serieDTO");
    private final static QName _SubSerieDTO_QNAME = new QName("http://soaint.com/domain-artifacts/ecm/sub-serie/1.0.0", "subSerieDTO");
    private final static QName _DocumentoDTO_QNAME = new QName("http://soaint.com/domain-artifacts/cor-agente/1.0.0", "documentoDTO");
    private final static QName _OrganigramaDTO_QNAME = new QName("http://soaint.com/domain-artifacts/ecm/organigrama/1.0.0", "organigramaDTO");
    private final static QName _EstructuraTrdDTO_QNAME = new QName("http://soaint.com/domain-artifacts/ecm/estructuraTrd/1.0.0", "estructuraTrdDTO");
    private final static QName _ContenidoDependenciaTrdDTO_QNAME = new QName("http://soaint.com/domain-artifacts/ecm/contenidoDependencia/1.0.0", "contenidoDependenciaTrdDTO");
    private final static QName _BaseDTO_QNAME = new QName("http://soaint.com/domain-artifacts/ecm/base/1.0.0", "baseDTO");
    private final static QName _CreateStructureECM_QNAME = new QName("http://co.com.soaint.ecm.integration.service.ws", "createStructureECM");
    private final static QName _SedeDTO_QNAME = new QName("http://soaint.com/domain-artifacts/ecm/sede/1.0.0", "sedeDTO");
    private final static QName _MensajeRespuesta_QNAME = new QName("http://soaint.com/domain-artifacts/ecm/mensajeRespuesta/1.0.0", "mensajeRespuesta");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: co.com.foundation.soaint.documentmanager.integration.client.ecmintegration
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link MensajeRespuesta }
     * 
     */
    public MensajeRespuesta createMensajeRespuesta() {
        return new MensajeRespuesta();
    }

    /**
     * Create an instance of {@link MensajeRespuesta.Response }
     * 
     */
    public MensajeRespuesta.Response createMensajeRespuestaResponse() {
        return new MensajeRespuesta.Response();
    }

    /**
     * Create an instance of {@link SedeDTO }
     * 
     */
    public SedeDTO createSedeDTO() {
        return new SedeDTO();
    }

    /**
     * Create an instance of {@link CreateStructureECM }
     * 
     */
    public CreateStructureECM createCreateStructureECM() {
        return new CreateStructureECM();
    }

    /**
     * Create an instance of {@link CreateStructureECMResponse }
     * 
     */
    public CreateStructureECMResponse createCreateStructureECMResponse() {
        return new CreateStructureECMResponse();
    }

    /**
     * Create an instance of {@link EstructuraTrdDTO }
     * 
     */
    public EstructuraTrdDTO createEstructuraTrdDTO() {
        return new EstructuraTrdDTO();
    }

    /**
     * Create an instance of {@link BaseDTO }
     * 
     */
    public BaseDTO createBaseDTO() {
        return new BaseDTO();
    }

    /**
     * Create an instance of {@link SubSerieDTO }
     * 
     */
    public SubSerieDTO createSubSerieDTO() {
        return new SubSerieDTO();
    }

    /**
     * Create an instance of {@link DocumentoDTO }
     * 
     */
    public DocumentoDTO createDocumentoDTO() {
        return new DocumentoDTO();
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
     * Create an instance of {@link ContenidoDependenciaTrdDTO }
     * 
     */
    public ContenidoDependenciaTrdDTO createContenidoDependenciaTrdDTO() {
        return new ContenidoDependenciaTrdDTO();
    }

    /**
     * Create an instance of {@link MensajeRespuesta.Response.Entry }
     * 
     */
    public MensajeRespuesta.Response.Entry createMensajeRespuestaResponseEntry() {
        return new MensajeRespuesta.Response.Entry();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateStructureECMResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.com.soaint.ecm.integration.service.ws", name = "createStructureECMResponse")
    public JAXBElement<CreateStructureECMResponse> createCreateStructureECMResponse(CreateStructureECMResponse value) {
        return new JAXBElement<CreateStructureECMResponse>(_CreateStructureECMResponse_QNAME, CreateStructureECMResponse.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link SubSerieDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soaint.com/domain-artifacts/ecm/sub-serie/1.0.0", name = "subSerieDTO")
    public JAXBElement<SubSerieDTO> createSubSerieDTO(SubSerieDTO value) {
        return new JAXBElement<SubSerieDTO>(_SubSerieDTO_QNAME, SubSerieDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DocumentoDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soaint.com/domain-artifacts/cor-agente/1.0.0", name = "documentoDTO")
    public JAXBElement<DocumentoDTO> createDocumentoDTO(DocumentoDTO value) {
        return new JAXBElement<DocumentoDTO>(_DocumentoDTO_QNAME, DocumentoDTO.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link EstructuraTrdDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soaint.com/domain-artifacts/ecm/estructuraTrd/1.0.0", name = "estructuraTrdDTO")
    public JAXBElement<EstructuraTrdDTO> createEstructuraTrdDTO(EstructuraTrdDTO value) {
        return new JAXBElement<EstructuraTrdDTO>(_EstructuraTrdDTO_QNAME, EstructuraTrdDTO.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link BaseDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soaint.com/domain-artifacts/ecm/base/1.0.0", name = "baseDTO")
    public JAXBElement<BaseDTO> createBaseDTO(BaseDTO value) {
        return new JAXBElement<BaseDTO>(_BaseDTO_QNAME, BaseDTO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateStructureECM }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.com.soaint.ecm.integration.service.ws", name = "createStructureECM")
    public JAXBElement<CreateStructureECM> createCreateStructureECM(CreateStructureECM value) {
        return new JAXBElement<CreateStructureECM>(_CreateStructureECM_QNAME, CreateStructureECM.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link MensajeRespuesta }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soaint.com/domain-artifacts/ecm/mensajeRespuesta/1.0.0", name = "mensajeRespuesta")
    public JAXBElement<MensajeRespuesta> createMensajeRespuesta(MensajeRespuesta value) {
        return new JAXBElement<MensajeRespuesta>(_MensajeRespuesta_QNAME, MensajeRespuesta.class, null, value);
    }

}
