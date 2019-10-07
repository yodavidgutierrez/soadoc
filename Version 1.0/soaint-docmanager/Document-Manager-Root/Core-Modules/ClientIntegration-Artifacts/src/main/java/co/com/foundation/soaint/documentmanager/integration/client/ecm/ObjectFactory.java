
package co.com.foundation.soaint.documentmanager.integration.client.ecm;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the co.com.foundation.soaint.documentmanager.integration.client.ecm package. 
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

    private final static QName _CreateStructureRecords_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "createStructureRecords");
    private final static QName _CreateStructureRecordsResponse_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "createStructureRecordsResponse");
    private final static QName _ECMIntegrationException_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "ECMIntegrationException");
    private final static QName _CreateStructureECM_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "createStructureECM");
    private final static QName _CreateStructureContent_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "createStructureContent");
    private final static QName _CreateStructureContentResponse_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "createStructureContentResponse");
    private final static QName _CreateStructureECMResponse_QNAME = new QName("http://co.com.soaint.sgd.correspondencia.service", "createStructureECMResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: co.com.foundation.soaint.documentmanager.integration.client.ecm
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CreateStructureECM }
     * 
     */
    public CreateStructureECM createCreateStructureECM() {
        return new CreateStructureECM();
    }

    /**
     * Create an instance of {@link ECMIntegrationException }
     * 
     */
    public ECMIntegrationException createECMIntegrationException() {
        return new ECMIntegrationException();
    }

    /**
     * Create an instance of {@link CreateStructureRecords }
     * 
     */
    public CreateStructureRecords createCreateStructureRecords() {
        return new CreateStructureRecords();
    }

    /**
     * Create an instance of {@link CreateStructureRecordsResponse }
     * 
     */
    public CreateStructureRecordsResponse createCreateStructureRecordsResponse() {
        return new CreateStructureRecordsResponse();
    }

    /**
     * Create an instance of {@link CreateStructureContentResponse }
     * 
     */
    public CreateStructureContentResponse createCreateStructureContentResponse() {
        return new CreateStructureContentResponse();
    }

    /**
     * Create an instance of {@link CreateStructureECMResponse }
     * 
     */
    public CreateStructureECMResponse createCreateStructureECMResponse() {
        return new CreateStructureECMResponse();
    }

    /**
     * Create an instance of {@link CreateStructureContent }
     * 
     */
    public CreateStructureContent createCreateStructureContent() {
        return new CreateStructureContent();
    }

    /**
     * Create an instance of {@link OrganigramaVO }
     * 
     */
    public OrganigramaVO createOrganigramaVO() {
        return new OrganigramaVO();
    }

    /**
     * Create an instance of {@link EstructuraTrdVO }
     * 
     */
    public EstructuraTrdVO createEstructuraTrdVO() {
        return new EstructuraTrdVO();
    }

    /**
     * Create an instance of {@link MessageResponse }
     * 
     */
    public MessageResponse createMessageResponse() {
        return new MessageResponse();
    }

    /**
     * Create an instance of {@link ContenidoDependenciaTrdVO }
     * 
     */
    public ContenidoDependenciaTrdVO createContenidoDependenciaTrdVO() {
        return new ContenidoDependenciaTrdVO();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateStructureRecords }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.com.soaint.sgd.correspondencia.service", name = "createStructureRecords")
    public JAXBElement<CreateStructureRecords> createCreateStructureRecords(CreateStructureRecords value) {
        return new JAXBElement<CreateStructureRecords>(_CreateStructureRecords_QNAME, CreateStructureRecords.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateStructureRecordsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.com.soaint.sgd.correspondencia.service", name = "createStructureRecordsResponse")
    public JAXBElement<CreateStructureRecordsResponse> createCreateStructureRecordsResponse(CreateStructureRecordsResponse value) {
        return new JAXBElement<CreateStructureRecordsResponse>(_CreateStructureRecordsResponse_QNAME, CreateStructureRecordsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ECMIntegrationException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.com.soaint.sgd.correspondencia.service", name = "ECMIntegrationException")
    public JAXBElement<ECMIntegrationException> createECMIntegrationException(ECMIntegrationException value) {
        return new JAXBElement<ECMIntegrationException>(_ECMIntegrationException_QNAME, ECMIntegrationException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateStructureECM }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.com.soaint.sgd.correspondencia.service", name = "createStructureECM")
    public JAXBElement<CreateStructureECM> createCreateStructureECM(CreateStructureECM value) {
        return new JAXBElement<CreateStructureECM>(_CreateStructureECM_QNAME, CreateStructureECM.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateStructureContent }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.com.soaint.sgd.correspondencia.service", name = "createStructureContent")
    public JAXBElement<CreateStructureContent> createCreateStructureContent(CreateStructureContent value) {
        return new JAXBElement<CreateStructureContent>(_CreateStructureContent_QNAME, CreateStructureContent.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateStructureContentResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.com.soaint.sgd.correspondencia.service", name = "createStructureContentResponse")
    public JAXBElement<CreateStructureContentResponse> createCreateStructureContentResponse(CreateStructureContentResponse value) {
        return new JAXBElement<CreateStructureContentResponse>(_CreateStructureContentResponse_QNAME, CreateStructureContentResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateStructureECMResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://co.com.soaint.sgd.correspondencia.service", name = "createStructureECMResponse")
    public JAXBElement<CreateStructureECMResponse> createCreateStructureECMResponse(CreateStructureECMResponse value) {
        return new JAXBElement<CreateStructureECMResponse>(_CreateStructureECMResponse_QNAME, CreateStructureECMResponse.class, null, value);
    }

}
