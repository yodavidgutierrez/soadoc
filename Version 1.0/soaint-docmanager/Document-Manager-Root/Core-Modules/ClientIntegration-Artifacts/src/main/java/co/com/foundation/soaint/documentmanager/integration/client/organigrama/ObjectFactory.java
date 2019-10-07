
package co.com.foundation.soaint.documentmanager.integration.client.organigrama;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the co.com.foundation.soaint.documentmanager.integration.client.organigrama package. 
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
    private final static QName _ConsultarOrganigrama_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "consultarOrganigrama");
    private final static QName _ListarElementosDeNivelInferiorResponse_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "listarElementosDeNivelInferiorResponse");
    private final static QName _ListarDescendientesDirectosDeElementoRayzResponse_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "listarDescendientesDirectosDeElementoRayzResponse");
    private final static QName _BusinessException_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "BusinessException");
    private final static QName _OrganigramaAdministrativoDTO_QNAME = new QName("http://soaint.com/domain-artifacts/paises/1.0.0", "organigramaAdministrativoDTO");
    private final static QName _SystemException_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "SystemException");
    private final static QName _CrearOrganigrama_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "crearOrganigrama");
    private final static QName _OrganigramaItemDTO_QNAME = new QName("http://soaint.com/domain-artifacts/cor-agente/1.0.0", "organigramaItemDTO");
    private final static QName _CrearOrganigramaResponse_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "crearOrganigramaResponse");
    private final static QName _ListarDescendientesDirectosDeElementoRayz_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "listarDescendientesDirectosDeElementoRayz");
    private final static QName _ConsultarOrganigramaResponse_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "consultarOrganigramaResponse");
    private final static QName _ConsultarPadreDeSegundoNivelResponse_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "consultarPadreDeSegundoNivelResponse");
    private final static QName _ConsultarPadreDeSegundoNivel_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "consultarPadreDeSegundoNivel");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: co.com.foundation.soaint.documentmanager.integration.client.organigrama
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link OrganigramaAdministrativoDTO }
     * 
     */
    public OrganigramaAdministrativoDTO createOrganigramaAdministrativoDTO() {
        return new OrganigramaAdministrativoDTO();
    }

    /**
     * Create an instance of {@link ListarDescendientesDirectosDeElementoRayzResponse }
     * 
     */
    public ListarDescendientesDirectosDeElementoRayzResponse createListarDescendientesDirectosDeElementoRayzResponse() {
        return new ListarDescendientesDirectosDeElementoRayzResponse();
    }

    /**
     * Create an instance of {@link ConsultarPadreDeSegundoNivelResponse }
     * 
     */
    public ConsultarPadreDeSegundoNivelResponse createConsultarPadreDeSegundoNivelResponse() {
        return new ConsultarPadreDeSegundoNivelResponse();
    }

    /**
     * Create an instance of {@link BusinessException }
     * 
     */
    public BusinessException createBusinessException() {
        return new BusinessException();
    }

    /**
     * Create an instance of {@link ConsultarPadreDeSegundoNivel }
     * 
     */
    public ConsultarPadreDeSegundoNivel createConsultarPadreDeSegundoNivel() {
        return new ConsultarPadreDeSegundoNivel();
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
     * Create an instance of {@link CrearOrganigramaResponse }
     * 
     */
    public CrearOrganigramaResponse createCrearOrganigramaResponse() {
        return new CrearOrganigramaResponse();
    }

    /**
     * Create an instance of {@link ListarDescendientesDirectosDeElementoRayz }
     * 
     */
    public ListarDescendientesDirectosDeElementoRayz createListarDescendientesDirectosDeElementoRayz() {
        return new ListarDescendientesDirectosDeElementoRayz();
    }

    /**
     * Create an instance of {@link ConsultarOrganigramaResponse }
     * 
     */
    public ConsultarOrganigramaResponse createConsultarOrganigramaResponse() {
        return new ConsultarOrganigramaResponse();
    }

    /**
     * Create an instance of {@link OrganigramaItemDTO }
     * 
     */
    public OrganigramaItemDTO createOrganigramaItemDTO() {
        return new OrganigramaItemDTO();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link ListarDescendientesDirectosDeElementoRayzResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.com.soaint.sgd.correspondencia.service", name = "listarDescendientesDirectosDeElementoRayzResponse")
    public JAXBElement<ListarDescendientesDirectosDeElementoRayzResponse> createListarDescendientesDirectosDeElementoRayzResponse(ListarDescendientesDirectosDeElementoRayzResponse value) {
        return new JAXBElement<ListarDescendientesDirectosDeElementoRayzResponse>(_ListarDescendientesDirectosDeElementoRayzResponse_QNAME, ListarDescendientesDirectosDeElementoRayzResponse.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link OrganigramaAdministrativoDTO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://soaint.com/domain-artifacts/paises/1.0.0", name = "organigramaAdministrativoDTO")
    public JAXBElement<OrganigramaAdministrativoDTO> createOrganigramaAdministrativoDTO(OrganigramaAdministrativoDTO value) {
        return new JAXBElement<OrganigramaAdministrativoDTO>(_OrganigramaAdministrativoDTO_QNAME, OrganigramaAdministrativoDTO.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link CrearOrganigramaResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.com.soaint.sgd.correspondencia.service", name = "crearOrganigramaResponse")
    public JAXBElement<CrearOrganigramaResponse> createCrearOrganigramaResponse(CrearOrganigramaResponse value) {
        return new JAXBElement<CrearOrganigramaResponse>(_CrearOrganigramaResponse_QNAME, CrearOrganigramaResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListarDescendientesDirectosDeElementoRayz }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.com.soaint.sgd.correspondencia.service", name = "listarDescendientesDirectosDeElementoRayz")
    public JAXBElement<ListarDescendientesDirectosDeElementoRayz> createListarDescendientesDirectosDeElementoRayz(ListarDescendientesDirectosDeElementoRayz value) {
        return new JAXBElement<ListarDescendientesDirectosDeElementoRayz>(_ListarDescendientesDirectosDeElementoRayz_QNAME, ListarDescendientesDirectosDeElementoRayz.class, null, value);
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

}
