package com.soaint.xmlsign.web;

import com.soaint.xmlsign.dto.ElementoIndiceDTO;
import com.soaint.xmlsign.dto.IndiceElectronicoDTO;
import com.soaint.xmlsign.dto.ResponseDTO;
import com.soaint.xmlsign.util.ConstantesECM;
import com.sun.org.apache.xml.internal.security.Init;
import com.sun.org.apache.xml.internal.security.signature.XMLSignature;
import com.sun.org.apache.xml.internal.security.transforms.Transforms;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import lombok.extern.log4j.Log4j2;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.util.ContentStreamUtils;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.*;

@Component
@Log4j2
public class ContentControlUtilities {

    private final Connection connection;
    private Environment environment;
    static final String NAMEINDEX = "IndiceElectronico.xml";

    @Autowired
    public ContentControlUtilities(Connection connection, Environment environment) {
        this.connection = connection;
        this.environment = environment;
    }

    /**
     * Imprime en consola las propiedades de un objeto Cmis
     *
     * @param cmisObject Objeto de la libreria de OpenCmis para gestionar ECM
     */
    void getProperties(CmisObject cmisObject) {
        log.info("Imprimiendo propiedades del objeto : " + cmisObject);
        for (Property<?> property : cmisObject.getProperties()) {
            log.info("ID: " + property.getId() + ", Value: " + property.getValue());
        }
    }

    /**
     * Obtiene los documento dentro del folder del ECM
     *
     * @param folder objeto del ECM
     * @return Lista de objetos del ECM
     */
    List<Document> getEcmDocumentsFromFolder(Folder folder) {
        log.info("obteniendo documentos del folder : " + folder);
        final List<Document> documents = new ArrayList<>();
        if (folder == null) {
            log.error("El Folder introducido es null");
            return documents;
        }
        final ItemIterable<CmisObject> children = folder.getChildren();
        for (CmisObject cmisObject : children) {
            if (cmisObject instanceof Document) {
                documents.add(documents.size(), (Document) cmisObject);
            }
        }
        return documents;
    }

    /**
     * Genera un indice electronico y retorna un objeto con el indice
     *
     * @param idFolder id de la ubicacion donde se guardará el documento
     * @return objeto con el indice electronico en formato xml creado
     */
    ResponseDTO generateIndice(final String idFolder) {
        log.info("Generando indice en formato xml del id del folder " + idFolder);
        //declara variables
        String existeXml = "0";
        Integer i = 1;
        String ms = "";
        Document xml = null;
        ResponseDTO response = new ResponseDTO();
        IndiceElectronicoDTO indiceElectronico = new IndiceElectronicoDTO();
        List<ElementoIndiceDTO> elementoIndiceDTOS = new ArrayList<>();

        //crea la coneccion con alfresco
        Session session = connection.getSession();
        //consulta el folder donde se creará el indice
        Folder folder = (Folder) session.getObject(idFolder);
        //consulta los documentos del folder
        List<Document> documentList = getEcmDocumentsFromFolder(folder);

        if (!documentList.isEmpty()) {

            //crear indice electronico
            indiceElectronico.setFechaIndiceElectronico(Calendar.getInstance());
            indiceElectronico.setIdentificadorIndiceElectronico(UUID.randomUUID().toString());
            Calendar calendar = folder.getPropertyValue("cmis:creationDate");
            indiceElectronico.setFechaCreacionCarpeta(calendar.getTime().toString());

            //crear la lista con toda la informacion
            for (Document doc : documentList) {
                if (!doc.getName().equals(NAMEINDEX)) {
                    log.info(doc.getName());
                    ElementoIndiceDTO elemento = new ElementoIndiceDTO();
                    elemento.setNombreDocumento(doc.getName());
                    elemento.setTipologiaDocumental(doc.getPropertyValue(ConstantesECM.CMCOR_DOC_TIPO_DOCUMENTAL));
                    elemento.setFechaCreacionDocumento(doc.getPropertyValue(ConstantesECM.CMCOR_FECHA_RADICACION));
                    elemento.setFechaIncorporacionExpediente(doc.getPropertyValue(ConstantesECM.CMCOR_FECHA_RADICACION));
                    elemento.setValorHuella(UUID.randomUUID().toString());
                    elemento.setFuncionResumen(UUID.randomUUID().toString());
                    elemento.setOrdenDocumentoExpediente(i.toString());
                    elemento.setPaginaInicio("1");
                    elemento.setPaginaFin("latest");
                    elemento.setFormato(doc.getPropertyValue("cmis:contentStreamMimeType"));
                    elemento.setTamano("156");
                    i++;
                    elementoIndiceDTOS.add(elemento);
                } else {
                    existeXml = "1";
                    xml = doc;
                }
            }
            indiceElectronico.setElementos(elementoIndiceDTOS);

            //convierte un objeto en xml
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(IndiceElectronicoDTO.class);
                Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                jaxbMarshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI);
                jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "utf-8");
                jaxbMarshaller.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "IndiceElectronico.xsd");
                StringWriter indiceXml = new StringWriter();
                jaxbMarshaller.marshal(indiceElectronico, indiceXml);

                response.setFlagExiste(existeXml);
                response.setFolder(folder);
                response.setXml(xml);
                response.setMs(ms);
                response.setIndiceXml(indiceXml.toString());
                response.setVacio(false);

            } catch (JAXBException e) {
                log.error("Error generando el indice electronico " + e);
            }
        } else {
            response.setVacio(true);
        }
        return response;
    }

    /**
     * Convierte un objeto de Xml a Document de la libreria de DOM
     *
     * @param filePath ubicacion del archivo
     * @return objeto documento de la libreria de DOM
     */
    org.w3c.dom.Document convertXMLFileToXMLDocument(String filePath) {
        log.info("Convirtiendo a Document un xml ubicado en " + filePath);
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            return builder.parse(new File(filePath));
        } catch (Exception e) {
            log.error("Error convirtiendo xml a Document " + e);
        }
        return null;
    }

    /**
     * Firma documento y lo almacena un directorio
     *
     * @param doc documento para firmar
     */
    void firmarXml(org.w3c.dom.Document doc) {
        log.info("Metodo que firma el documento a partir del keystore " + doc);

        try {
            Init.init();
            final String KEYSTORE_TYPE = "JKS";
            final String KEYSTORE_FILE = "/usr/lib/jvm/java-1.8.0-openjdk-amd64/bin/keystore.jks";  //"C:\\Varios\\Certificados\\keystore.jks";
            final String KEYSTORE_PASSWORD = "admin123";
            final String PRIVATE_KEY_PASSWORD = "admin123";
            final String PRIVATE_KEY_ALIAS = "mykey";

            //  Constants.setSignatureSpecNSprefix("");	// Sino, pone por defecto como prefijo: "ns"
            String replace = Constants.SignatureSpecNS.replace("ns", "");

            // Cargamos el almacen de claves
            KeyStore ks = KeyStore.getInstance(KEYSTORE_TYPE);
            ks.load(new FileInputStream(KEYSTORE_FILE), KEYSTORE_PASSWORD.toCharArray());

            // Obtenemos la clave privada, pues la necesitaremos para encriptar.
            PrivateKey privateKey = (PrivateKey) ks.getKey(PRIVATE_KEY_ALIAS, PRIVATE_KEY_PASSWORD.toCharArray());
            //File signatureFile = new File("/sdo/app/wildfly-10.0.0.Final/tmp/xmls/xmlSign.xml");
            File signatureFile = new File("/sdo/app/wildfly-10.0.0.Final/tmp/xmls/xmlSign.xml");
            String baseURI = signatureFile.toURL().toString();    // BaseURI para las URL Relativas.

            // Instanciamos un objeto XMLSignature desde el Document. El algoritmo de firma será DSA
            XMLSignature xmlSignature = new XMLSignature(doc, baseURI, XMLSignature.ALGO_ID_SIGNATURE_DSA);

            // Añadimos el nodo de la firma a la raiz antes de firmar.
            // Observe que ambos elementos pueden ser mezclados en una forma con referencias separadas
            doc.getDocumentElement().appendChild(xmlSignature.getElement());

            // Creamos el objeto que mapea: Document/Reference
            /*Transforms transforms = new Transforms(doc);
            transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
            transforms.addTransform(Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS);
            log.info("transformacion es " + transforms);
            // Añadimos lo anterior Documento / Referencia
            xmlSignature.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);
            log.info("referencia del documento " + xmlSignature.getBaseLocalName());*/

            // Añadimos el KeyInfo del certificado cuya clave privada usamos
            X509Certificate cert = (X509Certificate) ks.getCertificate(PRIVATE_KEY_ALIAS);
            xmlSignature.addKeyInfo(cert);
            xmlSignature.addKeyInfo(cert.getPublicKey());

            // Realizamos la firma
            xmlSignature.sign(privateKey);

            // Guardamos archivo de firma en disco
            outputDocToFile(doc, signatureFile);
        } catch (Exception e) {
            log.info("Error firmando el documento " + e);
        }

    }

    /**
     * Convierte un objeto de Documento Dom en un File y lo guarda en un directorio
     *
     * @param doc  Doc objeto firmado
     * @param file documento donde se almacena el xml firmado
     */
    void outputDocToFile(org.w3c.dom.Document doc, File file) {
        try {
            FileOutputStream f = new FileOutputStream(file);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(new DOMSource(doc), new StreamResult(f));
            f.close();
        } catch (Exception e) {
            log.info("Erroc convirtiendo el Documento DOM en file " + e);
        }
    }

    /**
     * Metodo que convierte documentos en un array de bytes
     *
     * @param contentStream objeto con el contenido paara convertir
     * @return arreglo con el contenido
     */
    byte[] getDocumentBytes(ContentStream contentStream) {
        log.info("Convirtiendo contenido en un arreglo de bytes " + contentStream);
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ContentStreamUtils.writeContentStreamToOutputStream(contentStream, outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("An error has occurred {}", e.getMessage());
            return null;
        }
    }

    /**
     * metodo para convertir  objeto file en un arreglo de bytes
     *
     * @param archivo archivo para convertir
     * @return arreglo con el archivo convertido
     */
    byte[] fileToByte(File archivo) {
        log.info("Convirtiendo file a arreglo de bytes " + archivo);
        try {
            byte[] fileContent = Files.readAllBytes(archivo.toPath());
            return fileContent;
        } catch (IOException ex) {
            log.error("Error convirtiendo file a arreglo de bytes");
            return null;
        }
    }


}
