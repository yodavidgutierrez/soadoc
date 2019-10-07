package com.soaint.xmlsign.web;

import com.soaint.xmlsign.dto.ResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.impl.MimeTypes;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class ContentControlAlfresco {

    private final ContentControlUtilities contentControlUtilities;
    private final Connection connection;

    @Autowired
    public ContentControlAlfresco(ContentControlUtilities contentControlUtilities, Connection connection) {
        this.contentControlUtilities = contentControlUtilities;
        this.connection = connection;
    }


    //private final String pathXmlUnsign = "/sdo/app/wildfly-10.0.0.Final/tmp/xmls/xmlUnsign.xml";
    //private final String pathXmlSign = "/sdo/app/wildfly-10.0.0.Final/tmp/xmls/xmlSign.xml";
    private final String pathXmlUnsign = "C:\\Varios\\firmado\\xmlUnsign.xml";
    private final String pathXmlSign = "C:\\Varios\\firmado\\xmlSign.xml";
    static final String NAMEINDEX = "IndiceElectronico.xml";

    /**
     * Metodo para crear el indice electronico en alfresco
     *
     * @param idFolder identificador unico de la carpeta donde se creará el indice
     * @return mensaje informativo sobre el estado del proceso
     */
    public String createXmlByIdFolder(final String idFolder) {
        log.info("Creando xml del folder : " + idFolder);

        //declaración de variables
        Document pwc;
        Document xml;
        String ms;

        //crear conexion con alfresco
        Session session = connection.getSession();

        //propiedades para el xml
        final Map<String, Object> map = new HashMap<>();
        map.put(PropertyIds.NAME, NAMEINDEX);
        map.put(PropertyIds.OBJECT_TYPE_ID, "D:cmcor:CM_DocumentoPersonalizado");
        map.put(PropertyIds.CONTENT_STREAM_FILE_NAME, NAMEINDEX);

        //genera el indice electronico
        ResponseDTO response = contentControlUtilities.generateIndice(idFolder);

        if (!response.isVacio()) {

            //crea un ducumento para subir
            String indice = response.getIndiceXml();
            Folder folder = response.getFolder();
            String existeXml = response.getFlagExiste();
            xml = response.getXml();

            //construyendo documento para subir
            InputStream inputStream = new ByteArrayInputStream(indice.getBytes());
            ContentStream contentStream = new ContentStreamImpl(NAMEINDEX,
                    new BigInteger(indice.getBytes()),
                    MimeTypes.getMIMEType("xml"),
                    inputStream);

            //versiona o sube el documento xml al alfresco
            if (existeXml.equals("1")) {
                try {
                    xml.updateProperties(map, true);
                    ObjectId id =  xml.checkOut();
                    pwc = (Document) session.getObject(id);
                    pwc.checkIn(false, map, contentStream, "nueva versión");
                    //doc.checkIn(false, map, contentStream, "nueva versión");
                    ms = "INDICE ACTUALIZADO CORRECTAMENTE";
                } catch (Exception e) {
                    log.error("Error generando el la nueva version del indice electronico");
                    ms = "ERROR GENERANDO LA NUEVA VERSIÓN DEL INDICE ELECTRONICO";
                }
            } else {
                folder.createDocument(map, contentStream, VersioningState.MAJOR);
                ms = "INDICE CREADO CORRECTAMENTE";
            }
        } else {
            ms = "EL EXPEDIENTE NO TIENE DOCUMENTOS";
        }
        return ms;
    }

    /**
     * Metodo que descarga, firma y vuelve a subir el xml a alfresco
     *
     * @param idFolder identificador unico de la carpeta donde se encuentra el xml
     * @return mensaje informativo del estado del proceso
     */
    public String signXmlByIdFolder(final String idFolder) {
        log.info("Firmando xml del folder : " + idFolder);

        //definir variables
        Document xmlUnsign = null;
        boolean existeIndice = false;
        String ms;
        Document pwc;

        try {
            //conectarse al alfresco
            final Session session = connection.getSession();
            Folder folder = (Folder) session.getObject(idFolder);
            List<Document> documentList = contentControlUtilities.getEcmDocumentsFromFolder(folder);

            if (!documentList.isEmpty()) {

                //buscar el indice electronico
                for (Document doc : documentList) {
                    if (doc.getName().equals(NAMEINDEX)) {
                        xmlUnsign = doc;
                        existeIndice = true;
                        break;
                    }
                }

                if (existeIndice) {

                    byte[] bytes = contentControlUtilities.getDocumentBytes(xmlUnsign.getContentStream());
                    File file = new File(pathXmlUnsign);
                    OutputStream os = new FileOutputStream(file);
                    os.write(bytes);
                    os.close();

                    //creando el file con la informacion del xml
                    org.w3c.dom.Document doc = contentControlUtilities.convertXMLFileToXMLDocument(pathXmlUnsign);
                    contentControlUtilities.firmarXml(doc);

                    //propiedades para el xml
                    final Map<String, Object> map = new HashMap<>();
                    map.put(PropertyIds.NAME, NAMEINDEX);
                    map.put(PropertyIds.OBJECT_TYPE_ID, "D:cmcor:CM_DocumentoPersonalizado");
                    map.put(PropertyIds.CONTENT_STREAM_FILE_NAME, NAMEINDEX);

                    //subiendo el xml firmado al alfresco
                    File fileSing = new File(pathXmlSign);
                    byte[] xmlSingToUp = contentControlUtilities.fileToByte(fileSing);
                    InputStream inputStream = new ByteArrayInputStream(xmlSingToUp);
                    ContentStream contentStream = new ContentStreamImpl(NAMEINDEX,
                            new BigInteger(xmlSingToUp),
                            MimeTypes.getMIMEType("xml"),
                            inputStream);

                    xmlUnsign.updateProperties(map, true);
                    pwc = (Document) session.getObject(xmlUnsign.checkOut());
                    pwc.checkIn(false, map, contentStream, "nueva version");

                    ms = "INDICE ELECTRONICO FIRMADO CORRECTAMENTE";

                    if (file.delete()) {
                        log.info("se elimino el documento sin firmar termporal");
                    } else {
                        log.info("no se pudo elminar el documento sin firmar temporal");
                    }
                    if (fileSing.delete()) {
                        log.info("se elimino el documento firmado termporal");
                    } else {
                        log.info("no se pudo elminar el documento firmado temporal");
                    }

                } else {
                    ms = "NO EXISTE INDICE ELECTRONICO PARA FIRMAR";
                }
            } else {
                ms = "NO HAY DOCUMENTOS EN ESTE EXPEDIENTE";
            }

        } catch (Exception e) {
            log.error("Error firmado el documento " + e);
            ms = "ERROR FIRMANDO EL XML DE INDICE ELECTRONICO";
        }
        return ms;
    }


}
