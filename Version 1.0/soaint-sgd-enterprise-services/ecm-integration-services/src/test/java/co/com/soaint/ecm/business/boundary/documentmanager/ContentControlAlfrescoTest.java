package co.com.soaint.ecm.business.boundary.documentmanager;

import co.com.soaint.ecm.business.boundary.documentmanager.interfaces.Connection;
import co.com.soaint.ecm.domain.entity.Carpeta;
import co.com.soaint.ecm.domain.entity.Conexion;
import co.com.soaint.foundation.canonical.ecm.*;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.UnfileObject;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.impl.MimeTypes;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigInteger;
import java.util.*;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/core-config.xml"})
public class ContentControlAlfrescoTest {

    private static final Logger logger = LogManager.getLogger(ContentControlAlfresco.class.getName());

    @Rule
    public ECMConnectionRule ecmConnectionRule = new ECMConnectionRule();

    @Autowired
    private ContentControlAlfresco contentControlAlfresco;

    @Autowired
    private Connection connection;

    private Conexion conexion;
    private ContenidoDependenciaTrdDTO dependenciaTrdDTO;

    @Before
    public void Setup() {
        conexion = ecmConnectionRule.newConexion();
        ecmConnectionRule.usingContentControlAlfresco(contentControlAlfresco);
        // DocTestJUnit, TestMetodoSubirDoc1, TestMetodoSubirDoc2

        //Se crea el objeto que contiene la dependencia de prueba dependenciaTrdDTO
        dependenciaTrdDTO = new ContenidoDependenciaTrdDTO();
        dependenciaTrdDTO.setIdOrgAdm("1000");
        dependenciaTrdDTO.setIdOrgOfc("10001010");
    }

    @After
    public void afterFunct() {

//        try {
//            MensajeRespuesta mensajeRespuesta = ecmConnectionRule.uploadNewDocument("DocTestJUnit");
//            contentControlAlfresco.eliminardocumento(mensajeRespuesta.getDocumentoDTOList().get(0).getIdDocumento(), getSession());
//
//        } catch (SystemException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void testDescargarDocumentoSuccess() {
        //Prueba Existosa para descargar documento
        try {
            MensajeRespuesta mensajeRespuesta = ecmConnectionRule.uploadNewDocument("DocTestJUnittestDescargarDocumentoSuccess");
            assertEquals("0000", contentControlAlfresco.
                    descargarDocumento(mensajeRespuesta.getDocumentoDTOList().get(0)).getCodMensaje());

            //Prueba para descargar documento que no existe
            DocumentoDTO documentoDTO2 = new DocumentoDTO();
            documentoDTO2.setIdDocumento("sdasdasdasd");
            assertEquals("2222", contentControlAlfresco.
                    descargarDocumento(documentoDTO2).getCodMensaje());
        } catch (Exception e) {
            logger.error("Ocurrio un error en el Servidor", e);
        }

    }

    @Test
    public void testDevolverSerieSubSerieSuccess() {
        //Prueba Existosa para devolver serie subserie
        try {
            assertEquals("0000", contentControlAlfresco.
                    devolverSerieSubSerie(dependenciaTrdDTO).getCodMensaje());
        } catch (Exception e) {
            logger.error("Ocurrio un error en el Servidor", e);
        }
        //Prueba para cuadno se pasa vacio el objeto contenidoDependenciaTrdDTO
        ContenidoDependenciaTrdDTO contenidoDependenciaTrdDTO = new ContenidoDependenciaTrdDTO();
        try {
            contentControlAlfresco.devolverSerieSubSerie(contenidoDependenciaTrdDTO);
        } catch (Exception e) {
            assertEquals("No se ha especificado el codigo de la dependencia", e.getMessage());
            logger.error("Ocurrio un error en el Servidor", e);
        }
    }

    @Test
    public void testCrearUnidadDocumentalSuccess() {
        //Crear unidad documental
        try {
            UnidadDocumentalDTO unidadDocumentalDTO = ecmConnectionRule.newUnidadDocumental();
            MensajeRespuesta mensajeRespuesta = contentControlAlfresco.
                    crearUnidadDocumental(unidadDocumentalDTO);
            assertEquals("0000", mensajeRespuesta.getCodMensaje());
            unidadDocumentalDTO = (UnidadDocumentalDTO) mensajeRespuesta.getResponse().get("unidadDocumental");
            contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTO.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarUnidadDocumentalSuccess() {
        try {
            UnidadDocumentalDTO unidadDocumentalDTO = ecmConnectionRule.newUnidadDocumental();
            unidadDocumentalDTO.setAccion("CERRAR");
            unidadDocumentalDTO.setSoporte("PAPEL");
            assertEquals("0000", contentControlAlfresco.
                    listarUnidadDocumental(unidadDocumentalDTO).getCodMensaje());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarUnidadDocumentalSinAccionSuccess() {
        //Se llenan los datos de la unidad documental
        UnidadDocumentalDTO unidadDocumentalDTO1 = new UnidadDocumentalDTO();
        unidadDocumentalDTO1.setInactivo(true);
        //Calendar calendar
        Calendar gregorianCalendar = GregorianCalendar.getInstance();
        unidadDocumentalDTO1.setFechaCierre(gregorianCalendar);
        unidadDocumentalDTO1.setId("1118");
        unidadDocumentalDTO1.setFechaExtremaInicial(gregorianCalendar);
        unidadDocumentalDTO1.setSoporte("electronico");
        unidadDocumentalDTO1.setNombreUnidadDocumental("UnidadDocumentalTest");
        unidadDocumentalDTO1.setFechaExtremaFinal(gregorianCalendar);
        unidadDocumentalDTO1.setCerrada(true);
        unidadDocumentalDTO1.setCodigoSubSerie("02312");
        unidadDocumentalDTO1.setCodigoSerie("0231");
        unidadDocumentalDTO1.setCodigoDependencia("10001040");
        unidadDocumentalDTO1.setDescriptor1("3434");
        unidadDocumentalDTO1.setDescriptor2("454545");
        unidadDocumentalDTO1.setAccion("");
        unidadDocumentalDTO1.setInactivo(false);
        unidadDocumentalDTO1.setCerrada(false);
        unidadDocumentalDTO1.setEstado("Abierto");
        unidadDocumentalDTO1.setDisposicion("SSS");

        //Crear unidad documental
        try {
            MensajeRespuesta mensajeRespuesta = contentControlAlfresco.
                    crearUnidadDocumental(unidadDocumentalDTO1);
            UnidadDocumentalDTO unidadDocumentalDTO = (UnidadDocumentalDTO) mensajeRespuesta.getResponse().get("unidadDocumental");

            assertEquals("0000", contentControlAlfresco.
                    listarUnidadDocumental(unidadDocumentalDTO).getCodMensaje());
            contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTO.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testObtenerDetallesDocumentoDTOSuccess() {

        //Prueba Existosa para obtenerdetalles de documento
        try {
            MensajeRespuesta mensajeRespuesta = ecmConnectionRule.uploadNewDocument("DocTestJUnittestObtenerDetallesDocumentoDTOSuccess");
            assertEquals("0000", contentControlAlfresco.
                    obtenerDetallesDocumentoDTO(mensajeRespuesta.getDocumentoDTOList().get(0).
                            getIdDocumento()).getCodMensaje());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Prueba cuadno viene vacio el idDocumento
        try {
            assertEquals("11111", contentControlAlfresco.
                    obtenerDetallesDocumentoDTO(null).getCodMensaje());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testDetallesUnidadDocumentalSuccess() {
        //Crear unidad documental
        try {
            UnidadDocumentalDTO unidadDocumentalDTO = ecmConnectionRule.newUnidadDocumental();
            MensajeRespuesta mensajeRespuesta = contentControlAlfresco.
                    crearUnidadDocumental(unidadDocumentalDTO);

            unidadDocumentalDTO = (UnidadDocumentalDTO) mensajeRespuesta.getResponse().get("unidadDocumental");
            //Listar detalles
            assertEquals("0000", contentControlAlfresco.
                    detallesUnidadDocumental(unidadDocumentalDTO.getId()).getCodMensaje());
            contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTO.getId());
        } catch (SystemException e) {
            logger.error("Error SystemException: {}", e);
        } catch (Exception e) {
            logger.error("Error Exception: {}", e);
        }
    }

    @Test
    public void testDetallesUnidadDocumentalFail() {
        //Crear unidad documental
        UnidadDocumentalDTO unidadDocumentalDTO = ecmConnectionRule.newUnidadDocumental();
        try {
            contentControlAlfresco.detallesUnidadDocumental(unidadDocumentalDTO.getId()).getCodMensaje();
        } catch (SystemException e) {
            assertEquals("Unidad Documental no encontrada con ID: '" + unidadDocumentalDTO.getId() + "'", e.getMessage());
            logger.error("Error SystemException: {}", e);
        } catch (Exception e) {
            logger.error("Error Exception: {}", e);
        }
    }

    @Test
    public void testSubirDocumentoPrincipalAdjuntoEESuccess() {
        //Probar que sube documento EE correctemante
        DocumentoDTO documentoDTO1 = ecmConnectionRule.newDocumento("TestMetodoSubirDoc1");
        try {
            MensajeRespuesta mensajeRespuesta1 = contentControlAlfresco.subirDocumentoPrincipalAdjunto(documentoDTO1, "EE");
            assertEquals("0000", mensajeRespuesta1.getCodMensaje());

            contentControlAlfresco.eliminardocumento(mensajeRespuesta1.getDocumentoDTOList().get(0).getIdDocumento());
        } catch (SystemException e) {
            logger.error("Error SystemException: {}", e);
        }
    }

    @Test
    public void testSubirDocumentoPrincipalAdjuntoEISuccess() {
        //Probar que sube documento EI correctemante
        DocumentoDTO documentoDTO1 = ecmConnectionRule.newDocumento("TestMetodoSubirDoc1");
        try {
            MensajeRespuesta mensajeRespuesta1 = contentControlAlfresco.
                    subirDocumentoPrincipalAdjunto(documentoDTO1, "EI");
            assertEquals("0000", mensajeRespuesta1.getCodMensaje());

            contentControlAlfresco.
                    eliminardocumento(mensajeRespuesta1.getDocumentoDTOList().get(0).getIdDocumento());
        } catch (SystemException e) {
            logger.error("Error SystemException: {}", e);
        }
    }

    @Test
    public void testSubirDocumentoPrincipalAdjuntoSISuccess() {
        //Probar que sube documento EE correctemante
        DocumentoDTO documentoDTO1 = ecmConnectionRule.newDocumento("TestMetodoSubirDoc1");
        try {
            MensajeRespuesta mensajeRespuesta1 = contentControlAlfresco.subirDocumentoPrincipalAdjunto(documentoDTO1, "SI");
            assertEquals("0000", mensajeRespuesta1.getCodMensaje());

            contentControlAlfresco.eliminardocumento(mensajeRespuesta1.getDocumentoDTOList().get(0).getIdDocumento());
        } catch (SystemException e) {
            logger.error("Error SystemException: {}", e);
        }
    }

    @Test
    public void testSubirDocumentoPrincipalAdjuntoDefaultSuccess() {
        //Probar que sube documento EI correctemante
        DocumentoDTO documentoDTO1 = ecmConnectionRule.newDocumento("TestMetodoSubirDoc1");
        try {
            MensajeRespuesta mensajeRespuesta1 = contentControlAlfresco.
                    subirDocumentoPrincipalAdjunto(documentoDTO1, "OTHER");
            assertEquals("0000", mensajeRespuesta1.getCodMensaje());
            contentControlAlfresco.
                    eliminardocumento(mensajeRespuesta1.getDocumentoDTOList().get(0).getIdDocumento());
        } catch (SystemException e) {
            logger.error("Error SystemException: {}", e);
        }
    }

    @Test
    public void testSubirDocumentoPrincipalAdjuntoSESuccess() {
        //Probar que sube documento EI correctemante
        DocumentoDTO documentoDTO1 = ecmConnectionRule.newDocumento("TestMetodoSubirDoc1");
        try {
            MensajeRespuesta mensajeRespuesta1 = contentControlAlfresco.
                    subirDocumentoPrincipalAdjunto(documentoDTO1, "SE");
            assertEquals("0000", mensajeRespuesta1.getCodMensaje());
            contentControlAlfresco.
                    eliminardocumento(mensajeRespuesta1.getDocumentoDTOList().get(0).getIdDocumento());
        } catch (SystemException e) {
            logger.error("Error SystemException: {}", e);
        }
    }

    @Test
    public void testSubirDocumentoPrincipalAdjuntoPDSuccess() {
        //Probar que sube documento PD correctemante
        DocumentoDTO documentoDTO2 = ecmConnectionRule.newDocumento("TestMetodoSubirDoc2");
        try {
            MensajeRespuesta mensajeRespuesta1 = contentControlAlfresco.
                    subirDocumentoPrincipalAdjunto(documentoDTO2, "PD");
            assertEquals("0000", mensajeRespuesta1.getCodMensaje());
            contentControlAlfresco.
                    eliminardocumento(mensajeRespuesta1.getDocumentoDTOList().get(0).getIdDocumento());
        } catch (SystemException e) {
            logger.error("Error SystemException: {}", e);
        }
    }

    @Test
    public void testGetUDByIdTrueSuccess() throws Exception {
        try {
            UnidadDocumentalDTO unidadDocumentalDTO = ecmConnectionRule.newUnidadDocumental();
            MensajeRespuesta mensajeRespuesta = contentControlAlfresco.
                    crearUnidadDocumental(unidadDocumentalDTO);

            unidadDocumentalDTO = (UnidadDocumentalDTO) mensajeRespuesta.getResponse().get("unidadDocumental");
            //Obtener la unidad documental

            final Optional<UnidadDocumentalDTO> optionalDocumentalDTO = contentControlAlfresco.
                    getUDToCloseById(unidadDocumentalDTO.getId());

            optionalDocumentalDTO.ifPresent(unidadDocumentalDTO1 ->
                    assertNotNull(unidadDocumentalDTO1.getId()));

            contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTO.getId());

        } catch (SystemException e) {
            logger.error("Error SystemException: {}", e);
        } catch (Exception e) {
            logger.error("Error: {}", e);
        }
    }

    @Test
    public void testEliminardocumentoTrueSuccess() {
        DocumentoDTO documentoDTO1 = ecmConnectionRule.newDocumento("TestMetodoSubirDoc1");
        try {
            MensajeRespuesta mensajeRespuesta1 = contentControlAlfresco.subirDocumentoPrincipalAdjunto(documentoDTO1, "EE");
            //Probar documento se sube correctamente
            assertEquals("0000", mensajeRespuesta1.getCodMensaje());
            contentControlAlfresco.eliminardocumento(mensajeRespuesta1.getDocumentoDTOList().get(0).getIdDocumento());
            assertTrue(true);
        } catch (SystemException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testEliminardocumentoFalseSuccess() {
        DocumentoDTO documentoDTO13 = new DocumentoDTO();
        documentoDTO13.setIdDocumento("WEWEWE");
        try {
            contentControlAlfresco.eliminardocumento(documentoDTO13.getIdDocumento());
            fail("Si continua hasta aqui entonces falla");
        } catch (SystemException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testMovDocumentoSuccess() {
        try {
            MensajeRespuesta mensajeRespuesta = ecmConnectionRule.uploadNewDocument("DocTestJUnittestMovDocumentoSuccess");
            assertEquals("0000", contentControlAlfresco.movDocumento(mensajeRespuesta.getDocumentoDTOList().get(0).getIdDocumento(), "Comunicaciones Oficiales Externas Recibidas 2018", "1000.1040_GERENCIA NACIONAL DE GESTION DOCUMENTAL").getCodMensaje());
        } catch (Exception e) {
            logger.error("Error");
        }
    }

    @Test
    public void testMovDocumentoNoExisteDocumento() throws SystemException {
        assertEquals("00006", contentControlAlfresco.movDocumento("idNoValidoDeDocumento", "Comunicaciones Oficiales Externas Recibidas 2018", "1000.1040_GERENCIA NACIONAL DE GESTION DOCUMENTAL").getCodMensaje());

    }

    @Test
    public void testGetUDFolderByIdSuccess() {
        try {
            UnidadDocumentalDTO unidadDocumentalDTO = ecmConnectionRule.newUnidadDocumental();
            MensajeRespuesta mensajeRespuesta = contentControlAlfresco.
                    crearUnidadDocumental(unidadDocumentalDTO);

            unidadDocumentalDTO = (UnidadDocumentalDTO) mensajeRespuesta.getResponse().get("unidadDocumental");
            //Obtener la unidad documental

            final Optional<Folder> optionalDocumentalDTO = contentControlAlfresco.
                    getUDFolderById(unidadDocumentalDTO.getId());

            optionalDocumentalDTO.ifPresent(unidadDocumentalDTO1 ->
                    assertNotNull(unidadDocumentalDTO1.getId()));

            contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTO.getId());

        } catch (Exception e) {
            logger.error("Error: {}", e);
        }
    }

    @Test
    public void testActualizarUnidadDocumentalSuccess() {
        try {
            //Se crea la Unidad Documental
            UnidadDocumentalDTO unidadDocumentalDTO = ecmConnectionRule.newUnidadDocumental();
            MensajeRespuesta mensajeRespuesta = contentControlAlfresco.
                    crearUnidadDocumental(unidadDocumentalDTO);

            UnidadDocumentalDTO unidadDocumentalDTOInsertada = (UnidadDocumentalDTO) mensajeRespuesta.getResponse().get("unidadDocumental");
            //Modificar el valor de la UD
            unidadDocumentalDTOInsertada.setNombreUnidadDocumental("OtroNombreParaProbar");
            //Verifico si se hizo la modificacion
            assertTrue(contentControlAlfresco.actualizarUnidadDocumental(unidadDocumentalDTOInsertada));

            contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTO.getId());

        } catch (SystemException e) {
            logger.error("Error: {}", e);
        } catch (Exception e) {
            logger.error("Error actualizando la UD: {}", e);
        }
    }

    @Test
    public void testObtenerDocumentosAdjuntosSuccess() {
        try {
            MensajeRespuesta mensajeRespuesta = ecmConnectionRule.uploadNewDocument("DocTestJUnittestObtenerDocumentosAdjuntosSuccess");
            DocumentoDTO documentoDTO = mensajeRespuesta.getDocumentoDTOList().get(0);
            DocumentoDTO documentoDTO1 = ecmConnectionRule.newDocumento("TestMetodoSubirDoc1");
            //Probar que sube documento EE correctemante
            //Adicionar como documento hijo del documento de prueba principal

            documentoDTO1.setIdDocumentoPadre(mensajeRespuesta.getDocumentoDTOList().get(0).getIdDocumento());
            MensajeRespuesta mensajeRespuesta1 = contentControlAlfresco.subirDocumentoPrincipalAdjunto(documentoDTO1, "EE");
            assertNotNull(contentControlAlfresco.obtenerDocumentosAdjuntos(documentoDTO).getDocumentoDTOList());

            contentControlAlfresco.eliminardocumento(documentoDTO1.getIdDocumento());
        } catch (SystemException e) {
            logger.error("Error Eliminando: {}", e);
        }

    }

    @Test
    public void testSubirVersionarDocumentoGeneradoSuccess() {
        //Probar obtener Versiones
        //Adicionar documento para version
        try {
            DocumentoDTO documentoDTO = ecmConnectionRule.existingDocumento("DocTestJUnittestSubirVersionarDocumentoGeneradoSuccess");
            assertEquals("0000", contentControlAlfresco.subirVersionarDocumentoGenerado(documentoDTO, "EE").getCodMensaje());

            //Eliminar la version del documento

            contentControlAlfresco.eliminardocumento(documentoDTO.getIdDocumento());
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSubirVersionarDocumentoGeneradoNuevoSuccess() {
        DocumentoDTO documentoDTO4 = new DocumentoDTO();
        documentoDTO4.setTipoDocumento("application/pdf");
        documentoDTO4.setNombreDocumento("testSubirVersionarDocumentoGeneradoNuevoSuccess");
        documentoDTO4.setVersionLabel("1.0");
        documentoDTO4.setNombreRemitente("UserTest");
        documentoDTO4.setNroRadicado("1234567");
        documentoDTO4.setTipologiaDocumental("Principal");
        String documento = "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u78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4KPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNC4yLjEtYzA0MyA1Mi4zNzI3MjgsIDIwMDkvMDEvMTgtMTU6MDg6MDQgICAgICAgICI+CiAgIDxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+CiAgICAgIDxyZGY6RGVzY3JpcHRpb24gcmRmOmFib3V0PSIiCiAgICAgICAgICAgIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyI+CiAgICAgICAgIDxkYzpmb3JtYXQ+YXBwbGljYXRpb24vcGRmPC9kYzpmb3JtYXQ+CiAgICAgICAgIDxkYzpjcmVhdG9yPgogICAgICAgICAgICA8cmRmOlNlcT4KICAgICAgICAgICAgICAgPHJkZjpsaT5jZGFpbHk8L3JkZjpsaT4KICAgICAgICAgICAgPC9yZGY6U2VxPgogICAgICAgICA8L2RjOmNyZWF0b3I+CiAgICAgICAgIDxkYzp0aXRsZT4KICAgICAgICAgICAgPHJkZjpBbHQ+CiAgICAgICAgICAgICAgIDxyZGY6bGkgeG1sOmxhbmc9IngtZGVmYXVsdCI+VGhpcyBpcyBhIHRlc3QgUERGIGZpbGU8L3JkZjpsaT4KICAgICAgICAgICAgPC9yZGY6QWx0PgogICAgICAgICA8L2RjOnRpdGxlPgogICAgICA8L3JkZjpEZXNjcmlwdGlvbj4KICAgICAgPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIKICAgICAgICAgICAgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIj4KICAgICAgICAgPHhtcDpDcmVhdGVEYXRlPjIwMDAtMDYtMjlUMTA6MjE6MDgrMTE6MDA8L3htcDpDcmVhdGVEYXRlPgogICAgICAgICA8eG1wOkNyZWF0b3JUb29sPk1pY3Jvc29mdCBXb3JkIDguMDwveG1wOkNyZWF0b3JUb29sPgogICAgICAgICA8eG1wOk1vZGlmeURhdGU+MjAxMy0xMC0yOFQxNToyNDoxMy0wNDowMDwveG1wOk1vZGlmeURhdGU+CiAgICAgICAgIDx4bXA6TWV0YWRhdGFEYXRlPjIwMTMtMTAtMjhUMTU6MjQ6MTMtMDQ6MDA8L3htcDpNZXRhZGF0YURhdGU+CiAgICAgIDwvcmRmOkRlc2NyaXB0aW9uPgogICAgICA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIgogICAgICAgICAgICB4bWxuczpwZGY9Imh0dHA6Ly9ucy5hZG9iZS5jb20vcGRmLzEuMy8iPgogICAgICAgICA8cGRmOlByb2R1Y2VyPkFjcm9iYXQgRGlzdGlsbGVyIDQuMCBmb3IgV2luZG93czwvcGRmOlByb2R1Y2VyPgogICAgICA8L3JkZjpEZXNjcmlwdGlvbj4KICAgICAgPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIKICAgICAgICAgICAgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iPgogICAgICAgICA8eG1wTU06RG9jdW1lbnRJRD51dWlkOjA4MDVlMjIxLTgwYTgtNDU5ZS1hNTIyLTYzNWVkNWMxZTJlNjwveG1wTU06RG9jdW1lbnRJRD4KICAgICAgICAgPHhtcE1NOkluc3RhbmNlSUQ+dXVpZDo2MmQ2YWU2ZC00M2M0LTQ3MmQtOWIyOC03YzRhZGQ4ZjllNDY8L3htcE1NOkluc3RhbmNlSUQ+CiAgICAgIDwvcmRmOkRlc2NyaXB0aW9uPgogICA8L3JkZjpSREY+CjwveDp4bXBtZXRhPgogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAKICAgICAgICAgICAgICAgICAgICAgICAgICAgCjw/eHBhY2tldCBlbmQ9InciPz4NCmVuZHN0cmVhbQ1lbmRvYmoNMiAwIG9iag08PC9GaWx0ZXIvRmxhdGVEZWNvZGUvRmlyc3QgNC9MZW5ndGggNDgvTiAxL1R5cGUvT2JqU3RtPj5zdHJlYW0NCmjeMlUwULCx0XfOL80rUTDU985MKY62BIoFxeqHVBak6gckpqcW29kBBBgA1ncLgA0KZW5kc3RyZWFtDWVuZG9iag0zIDAgb2JqDTw8L0ZpbHRlci9GbGF0ZURlY29kZS9GaXJzdCA0L0xlbmd0aCAxNjcvTiAxL1R5cGUvT2JqU3RtPj5zdHJlYW0NCmjePMvBCsIwEEXRX5mdDaKdxCpVSqFY3AkuBNexSelA6EAyRfx7A4qPu3znAAhNU3aLTByLwVkKb1Weo7dCPPdWfNGfDOYdzFGj0VivtV4hrn6vrK40RE48Cjw4Oqi3qMoruz/WuwxrvTeV3m2w+uJbZLcMPhZdxk8r0FMSCsFHqLYII0d40Oz4lVR5Jwm+uE+UIGdBfBK49RcYKXjVth8BBgBnZztkDQplbmRzdHJlYW0NZW5kb2JqDTQgMCBvYmoNPDwvRGVjb2RlUGFybXM8PC9Db2x1bW5zIDMvUHJlZGljdG9yIDEyPj4vRmlsdGVyL0ZsYXRlRGVjb2RlL0lEWzw0REM5MUExODc1QTZENzA3QUVDMjAzQkIwMjFDOTNBMD48RjZDOTJCMzY4QThBMTM0MDg0NTdBMUQzOTVBMzdFQjk+XS9JbmZvIDYgMCBSL0xlbmd0aCAzNy9Sb290IDggMCBSL1NpemUgNy9UeXBlL1hSZWYvV1sxIDIgMF0+PnN0cmVhbQ0KaN5iYmBgYGLkPcLEwD+ViYGhh4mBkYWJ8bEkkM0IEGAAKlkDFA0KZW5kc3RyZWFtDWVuZG9iag1zdGFydHhyZWYNCjExNg0KJSVFT0YNCg==";
        documentoDTO4.setDocumento(documento.getBytes());
        documentoDTO4.setSede("1000_VICEPRESIDENCIA ADMINISTRATIVA");
        documentoDTO4.setDependencia("1000.1040_GERENCIA NACIONAL DE GESTION DOCUMENTAL");
        documentoDTO4.setCodigoSede("1000");
        documentoDTO4.setCodigoDependencia("10001040");
        try {
            assertEquals("0000", contentControlAlfresco.subirVersionarDocumentoGenerado(documentoDTO4, "EE").getCodMensaje());
        } catch (SystemException e) {
            e.printStackTrace();
        }

        //Eliminar la version del documento
        try {
            contentControlAlfresco.eliminardocumento(documentoDTO4.getIdDocumento());
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testObtenerVersionesDocumentoSuccess() {
        //Probar obtener Versiones
        //Adicionar documento para version
        try {
            DocumentoDTO documentoDTO = ecmConnectionRule.existingDocumento("DocTestJUnittestObtenerVersionesDocumentoSuccess");
            MensajeRespuesta mensajeRespuesta1 = contentControlAlfresco.subirVersionarDocumentoGenerado(documentoDTO, "EE");

            //Obtener Versiones de documento
            assertEquals("0000", contentControlAlfresco.obtenerVersionesDocumento(documentoDTO.getIdDocumento()).getCodMensaje());
            //Eliminar la version del documento
            contentControlAlfresco.eliminardocumento(documentoDTO.getIdDocumento());
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testModificarMetadatosDocumentoFail() {
        DocumentoDTO documentoDTO12 = new DocumentoDTO();
        documentoDTO12.setIdDocumento("qwqwqwqw");
        try {
            DocumentoDTO documentoDTO = ecmConnectionRule.existingDocumento("DocTestJUnittestModificarMetadatosDocumentoFail");
            /*assertEquals("00006", contentControlAlfresco.modificarMetadatosDocumento(documentoDTO12.getIdDocumento(), "sdsdsd", documentoDTO.getTipologiaDocumental(), "Urbino").getCodMensaje());*/
            assertEquals("00006", contentControlAlfresco.modificarMetadatosDocumento(documentoDTO).getCodMensaje());
        } catch (Exception e) {
            logger.error("Error");
        }
    }

    @Test
    public void testModificarMetadatosDocumentoSuccess() {
        try {
            DocumentoDTO documentoDTO = ecmConnectionRule.existingDocumento("DocTestJUnittestModificarMetadatosDocumentoSuccess");
            /*assertEquals("0000", contentControlAlfresco.modificarMetadatosDocumento(documentoDTO.getIdDocumento(), "sdsdsd", documentoDTO.getTipologiaDocumental(), "Urbino").getCodMensaje());*/
            assertEquals("0000", contentControlAlfresco.modificarMetadatosDocumento(documentoDTO).getCodMensaje());
        } catch (Exception e) {
            logger.error("error");
        }
    }

    @Test
    public void testObtenerPropiedadesDocumentoSuccess() {
        try {
            DocumentoDTO documentoDTO = ecmConnectionRule.existingDocumento("DocTestJUnittestObtenerPropiedadesDocumentoSuccess");
            CmisObject cmisObjectDocument = conexion.getSession().getObject(documentoDTO.getIdDocumento());
            assertNotNull(contentControlAlfresco.obtenerPropiedadesDocumento((Document) cmisObjectDocument));
        } catch (Exception e) {
            logger.error("Error");
        }
    }

    @Test
    public void testObtenerConexionSuccess() {

        try {
            assertNotNull(connection.getSession());
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetDocumentosPorArchivarSuccess() throws Exception {
        assertEquals("0000", contentControlAlfresco.getDocumentosPorArchivar("10001040").getCodMensaje());
    }

    @Test
    public void testObtenerDocumentosArchivadosSuccess() {
        try {
            DocumentoDTO documentoDTO = ecmConnectionRule.existingDocumento("DocTestJUnittestObtenerDocumentosArchivadosSuccess");

            MensajeRespuesta mensajeRespuesta = contentControlAlfresco.subirDocumentoTemporalUD(documentoDTO);

            DocumentoDTO documentoDTOA = (DocumentoDTO) mensajeRespuesta.getResponse().get("documento");


            UnidadDocumentalDTO unidadDocumentalDTO = ecmConnectionRule.newUnidadDocumental();
            MensajeRespuesta mensajeRespuesta1 = contentControlAlfresco.crearUnidadDocumental(unidadDocumentalDTO);

            unidadDocumentalDTO = (UnidadDocumentalDTO) mensajeRespuesta1.getResponse().get("unidadDocumental");
            List<DocumentoDTO> documentoDTOList = new ArrayList<>();
            documentoDTOList.add(documentoDTOA);
            unidadDocumentalDTO.setListaDocumentos(documentoDTOList);
            contentControlAlfresco.subirDocumentosUnidadDocumentalECM(unidadDocumentalDTO);

            assertEquals("0000", contentControlAlfresco.obtenerDocumentosArchivados("10001040").getCodMensaje());

            contentControlAlfresco.eliminardocumento(documentoDTOA.getIdDocumento());


            contentControlAlfresco.obtenerDocumentosArchivados("");
        } catch (Exception e) {
            assertEquals("No se ha especificado el codigo de la dependencia", e.getMessage());
        }

    }

    @Test
    public void testSubirDocumentosTemporalesUDSuccess() {
        ArrayList<DocumentoDTO> listaDocs = new ArrayList<>();
        try {
            DocumentoDTO documentoDTO1 = ecmConnectionRule.newDocumento("TestMetodoSubirDoc1testSubirDocumentosTemporalesUDSuccess");
            DocumentoDTO documentoDTO = ecmConnectionRule.existingDocumento("DocTestJUnittestSubirDocumentosTemporalesUDSuccess");
            listaDocs.add(documentoDTO);

            MensajeRespuesta mensajeRespuesta3 = contentControlAlfresco.subirDocumentoPrincipalAdjunto(documentoDTO1, "EE");
            documentoDTO1.setIdDocumento(mensajeRespuesta3.getDocumentoDTOList().get(0).getIdDocumento());


            listaDocs.add(documentoDTO1);


            MensajeRespuesta mensajeRespuesta = contentControlAlfresco.subirDocumentosTemporalesUD(listaDocs);
            assertEquals("0000", mensajeRespuesta.getCodMensaje());
            contentControlAlfresco.eliminardocumento(documentoDTO.getIdDocumento());
            contentControlAlfresco.eliminardocumento(documentoDTO1.getIdDocumento());

            List<DocumentoDTO> documentoDTOList = mensajeRespuesta.getDocumentoDTOList();
            documentoDTOList.forEach(doc -> {
                try {
                    contentControlAlfresco.eliminardocumento(doc.getIdDocumento());
                } catch (Exception e) {
                    new RuntimeException(e);
                }
            });


//            ArrayList<DocumentoDTO> listaDocs1 = new ArrayList();
//            contentControlAlfresco.subirDocumentosTemporalesUD(listaDocs1);

        } catch (Exception e) {
            assertEquals("La lista de documentos esta vacia", e.getMessage());
        }

    }

    @Test
    public void testGetDocumentsFromFolderSuccess() {
        try {
            UnidadDocumentalDTO unidadDocumentalDTO = ecmConnectionRule.newUnidadDocumental();
            MensajeRespuesta mensajeRespuesta = contentControlAlfresco.crearUnidadDocumental(unidadDocumentalDTO);

            unidadDocumentalDTO = (UnidadDocumentalDTO) mensajeRespuesta.getResponse().get("unidadDocumental");

            //Obtener la unidad documental
            final Optional<Folder> optionalFolder = contentControlAlfresco.
                    getUDFolderById(unidadDocumentalDTO.getId());

            DocumentoDTO documentoDTO = ecmConnectionRule.existingDocumento("DocTestJUnittestGetDocumentsFromFolderSuccess");
            Map<String, Object> properties = new HashMap<>();
            properties.put(PropertyIds.OBJECT_TYPE_ID, "D:cmcor:CM_DocumentoPersonalizado");
            properties.put(PropertyIds.NAME, "Doc Pruba");

            ContentStream contentStream = new ContentStreamImpl(documentoDTO.getNombreDocumento(), BigInteger.valueOf(documentoDTO.getDocumento().length), documentoDTO.getTipoDocumento(), new ByteArrayInputStream(documentoDTO.getDocumento()
            ));

            optionalFolder.ifPresent(optionalFolder1 -> optionalFolder1.createDocument(properties, contentStream, VersioningState.MAJOR));

            optionalFolder.ifPresent(optionalFolder1 -> {
                try {
                    assertNotNull(contentControlAlfresco.getDocumentsFromFolder(optionalFolder1).get(0).getIdDocumento());
                } catch (SystemException e) {
                    e.printStackTrace();
                }
            });

            contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTO.getId());

        } catch (SystemException e) {
            logger.error("Error: {}", e);
        } catch (Exception e) {
            logger.error("Error: {}", e);
        }

    }

    @Test
    public void testGetDocumentsFromFolderFail() {
        Folder foldertest = null;
        try {
            assertNotNull(contentControlAlfresco.getDocumentsFromFolder(foldertest));
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSubirDocumentosUnidadDocumentalECMSuccess() {
        try {
            UnidadDocumentalDTO unidadDocumentalDTO = ecmConnectionRule.newUnidadDocumental();
            DocumentoDTO documentoDTO = ecmConnectionRule.existingDocumento("DocTestJUnittestSubirDocumentosUnidadDocumentalECMSuccess");
            MensajeRespuesta mensajeRespuesta = contentControlAlfresco.crearUnidadDocumental(unidadDocumentalDTO);

            unidadDocumentalDTO = (UnidadDocumentalDTO) mensajeRespuesta.getResponse().get("unidadDocumental");
            List<DocumentoDTO> documentoDTOS = new ArrayList<>();
            documentoDTOS.add(documentoDTO);
            unidadDocumentalDTO.setListaDocumentos(documentoDTOS);
            assertEquals("0000", contentControlAlfresco.subirDocumentosUnidadDocumentalECM(unidadDocumentalDTO).getCodMensaje());
            contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTO.getId());
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testModificarUnidadesDocumentalesSuccess() {


        List<UnidadDocumentalDTO> unidadDocumentalDTOList = new ArrayList<>();

        try {

            UnidadDocumentalDTO unidadDocumentalDTO = ecmConnectionRule.newUnidadDocumental();
            MensajeRespuesta mensajeRespuesta = contentControlAlfresco.crearUnidadDocumental(unidadDocumentalDTO);

            unidadDocumentalDTO = (UnidadDocumentalDTO) mensajeRespuesta.getResponse().get("unidadDocumental");
            unidadDocumentalDTO.setNombreUnidadDocumental("DepPruebaModificarUnidadesDocumentales");
            unidadDocumentalDTOList.add(unidadDocumentalDTO);

            assertEquals("0000", contentControlAlfresco.modificarUnidadesDocumentales(unidadDocumentalDTOList).getCodMensaje());
            contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTO.getId());
        } catch (SystemException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testModificarUnidadesDocumentalesFail() {


        List<UnidadDocumentalDTO> unidadDocumentalDTOList = new ArrayList<>();
        UnidadDocumentalDTO unidadDocumentalDTO = ecmConnectionRule.newUnidadDocumental();

        try {
            assertEquals("0000", contentControlAlfresco.modificarUnidadesDocumentales(unidadDocumentalDTOList).getCodMensaje());
            contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTO.getId());
        } catch (SystemException e) {
            assertEquals("No se ha introducido la unidad documental a modificar", e.getReason());
        }
    }

    @Test
    public void testEliminarUnidadDocumentalSuccess() {
        MensajeRespuesta mensajeRespuesta = null;
        try {
            UnidadDocumentalDTO unidadDocumentalDTO = ecmConnectionRule.newUnidadDocumental();
            unidadDocumentalDTO.setId("12121212");
            mensajeRespuesta = contentControlAlfresco.crearUnidadDocumental(unidadDocumentalDTO);

            unidadDocumentalDTO = (UnidadDocumentalDTO) mensajeRespuesta.getResponse().get("unidadDocumental");
            assertEquals("0000", contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTO.getId()).getCodMensaje());
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEliminarUnidadDocumentalFail() {
        try {
            UnidadDocumentalDTO unidadDocumentalDTOFail = new UnidadDocumentalDTO();
            unidadDocumentalDTOFail.setId("NoId");

            assertEquals("1224", contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTOFail.getId()).getCodMensaje());
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSubirDocumentoTemporalSuccessUD() {
        try {
            DocumentoDTO documentoDTO = ecmConnectionRule.existingDocumento("DocTestJUnittestSubirDocumentoTemporalSuccessUD");
            MensajeRespuesta mensajeRespuesta = contentControlAlfresco.subirDocumentoTemporalUD(documentoDTO);
            assertEquals("0000", mensajeRespuesta.getCodMensaje());

            DocumentoDTO documentoDTOA = (DocumentoDTO) mensajeRespuesta.getResponse().get("documento");

            contentControlAlfresco.eliminardocumento(documentoDTOA.getIdDocumento());
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testListarDependenciaMultipleSedesSuccess() {
        ContenidoDependenciaTrdDTO dependenciaTrdDTO = new ContenidoDependenciaTrdDTO();
//        dependenciaTrdDTO.
        try {
            assertNotNull(contentControlAlfresco.listarDependenciaMultiple(dependenciaTrdDTO).getResponse().get("sedes"));
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testlistarDependenciaMultipleDependenciasSuccess() {
        ContenidoDependenciaTrdDTO dependenciaTrdDTO = new ContenidoDependenciaTrdDTO();
        dependenciaTrdDTO.setIdOrgOfc("1040");
        try {
            assertNotNull(contentControlAlfresco.listarDependenciaMultiple(dependenciaTrdDTO).getResponse().get("dependencias"));
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testlistarDependenciaMultipleSeriesSuccess() {
        ContenidoDependenciaTrdDTO dependenciaTrdDTO = new ContenidoDependenciaTrdDTO();
        dependenciaTrdDTO.setIdOrgOfc("1040");
        dependenciaTrdDTO.setCodSerie("30000");
        try {
            assertNotNull(contentControlAlfresco.listarDependenciaMultiple(dependenciaTrdDTO).getResponse().get("series"));
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testlistarDependenciaMultipleEmptySuccess() {
        ContenidoDependenciaTrdDTO dependenciaTrdDTO = null;

        try {
            assertNotNull(contentControlAlfresco.listarDependenciaMultiple(dependenciaTrdDTO).getResponse().get("sedes"));
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    /*@Test
    public void testListarUdDisposicionFinalNullSuccess() {
        DisposicionFinalDTO disposicionFinalDTO = null;
        try {
            contentControlAlfresco.listarUdDisposicionFinal(disposicionFinalDTO);
        } catch (SystemException e) {
            assertEquals("No se ha identificado la disposicion de la Unidad Documental", e.getMessage());
        }
    }

    @Test
    public void testListarUdDisposicionFinalEmptySuccess() {
        DisposicionFinalDTO disposicionFinalDTO = new DisposicionFinalDTO();
        try {
            contentControlAlfresco.listarUdDisposicionFinal(disposicionFinalDTO);
        } catch (SystemException e) {
            assertEquals("No se ha identificado la Unidad Documental", e.getMessage());
        }
    }*/

    /*@Test
    public void testListarUdDisposicionFinalSuccess() {
        try {
            DisposicionFinalDTO disposicionFinalDTO = new DisposicionFinalDTO();
            UnidadDocumentalDTO unidadDocumentalDTO = ecmConnectionRule.newUnidadDocumental();
            MensajeRespuesta mensajeRespuesta = contentControlAlfresco.crearUnidadDocumental(unidadDocumentalDTO);
            unidadDocumentalDTO = (UnidadDocumentalDTO) mensajeRespuesta.getResponse().get("unidadDocumental");

            disposicionFinalDTO.setUnidadDocumentalDTO(unidadDocumentalDTO);
            List<String> disposicionFinalList = new ArrayList<>();
            disposicionFinalList.add("CT");
            disposicionFinalList.add("M");
            disposicionFinalList.add("S");
            disposicionFinalList.add("E");
            disposicionFinalList.add("D");
            disposicionFinalDTO.setDisposicionFinalList(disposicionFinalList);
            assertEquals("0000", contentControlAlfresco.listarUdDisposicionFinal(disposicionFinalDTO).getCodMensaje());
            contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTO.getId());
        } catch (SystemException e) {
            assertEquals("No se ha identificado la Unidad Documental", e.getMessage());
        }
    }

    @Test
    public void testAprobarRechazarDisposicionesFinalesSuccess() {
        List<UnidadDocumentalDTO> unidadDocumentalDTOList = new ArrayList<>();
        try {
            UnidadDocumentalDTO unidadDocumentalDTOTest;
            //Se llenan los datos de la unidad documental
            unidadDocumentalDTOTest = new UnidadDocumentalDTO();
            unidadDocumentalDTOTest.setInactivo(true);
            //Calendar calendar
            Calendar gregorianCalendar = GregorianCalendar.getInstance();
            unidadDocumentalDTOTest.setFechaCierre(gregorianCalendar);
            unidadDocumentalDTOTest.setId("11181");
            unidadDocumentalDTOTest.setFechaExtremaInicial(gregorianCalendar);
            unidadDocumentalDTOTest.setSoporte("electronico");
            unidadDocumentalDTOTest.setNombreUnidadDocumental("UnidadDocumentalTest");
            unidadDocumentalDTOTest.setFechaExtremaFinal(gregorianCalendar);
            unidadDocumentalDTOTest.setCerrada(true);
            unidadDocumentalDTOTest.setCodigoSubSerie("02312");
            unidadDocumentalDTOTest.setCodigoSerie("0231");
            unidadDocumentalDTOTest.setCodigoDependencia("10001040");
            unidadDocumentalDTOTest.setDescriptor1("3434");
            unidadDocumentalDTOTest.setDescriptor2("454545");
            unidadDocumentalDTOTest.setAccion("");
            unidadDocumentalDTOTest.setCerrada(false);
            unidadDocumentalDTOTest.setEstado("Abierto");
            unidadDocumentalDTOTest.setDisposicion("Eliminar");
            unidadDocumentalDTOTest.setFaseArchivo("archivo central");

            DisposicionFinalDTO disposicionFinalDTO = new DisposicionFinalDTO();
            MensajeRespuesta mensajeRespuesta = contentControlAlfresco.crearUnidadDocumental(unidadDocumentalDTOTest);
            UnidadDocumentalDTO unidadDocumentalDTOTest1 = (UnidadDocumentalDTO) mensajeRespuesta.getResponse().get("unidadDocumental");
            unidadDocumentalDTOList.add(unidadDocumentalDTOTest1);
            disposicionFinalDTO.setUnidadDocumentalDTO(unidadDocumentalDTOTest1);
            List<String> disposicionFinalList = new ArrayList<>();
            disposicionFinalList.add("Conservacion Total");
            disposicionFinalList.add("Microfilmar");
            disposicionFinalList.add("seleccionar");
            disposicionFinalList.add("Eliminar");
            disposicionFinalList.add("Digitalizar");
            disposicionFinalDTO.setDisposicionFinalList(disposicionFinalList);
            assertEquals("0000", contentControlAlfresco.aprobarRechazarDisposicionesFinales(unidadDocumentalDTOList).getCodMensaje());

            contentControlAlfresco.eliminarUnidadDocumental(unidadDocumentalDTOTest1.getId());
        } catch (SystemException e) {
        }
    }

    @Test
    public void testAprobarRechazarDisposicionesFinalesListaVaciaFail() {
        List<UnidadDocumentalDTO> unidadDocumentalDTOList = new ArrayList<>();
        try {
            assertEquals("0000", contentControlAlfresco.aprobarRechazarDisposicionesFinales(unidadDocumentalDTOList).getCodMensaje());
        } catch (SystemException e) {
            assertEquals("La lista de unidades documentales enviada esta vacia", e.getMessage());
        }
    }*/

    @Test
    public void testEstamparEtiquetaRadicacionPDFSuccess() {

        DocumentoDTO documentoDTO = ecmConnectionRule.newDocumento("testEstamparEtiquetaRadicacionPDFSuccess");
        try {
            MensajeRespuesta mensajeRespuesta3 = contentControlAlfresco.subirDocumentoPrincipalAdjunto(documentoDTO, "EE");

            //Obtener arreglo de bytes a partir de la imagen
            String imgPath = "/Imagen.png";
            InputStream io = Class.class.getResourceAsStream(imgPath);
            BufferedImage imBuff = ImageIO.read(io);
//            BufferedImage bufferedImage = ImageIO.read(imBuff);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(imBuff, "png", baos);
//            String imgPath = "/Imagen.png";
//            InputStream io = Class.class.getResourceAsStream(imgPath);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//            byte[] bytes = new byte[2048];
//            int c;
//            while((c = io.read(bytes)) >= 0) {
//                baos.write(bytes, 0, c);
//            }
//
////            ImageIO.write(bufferedImage, "jpg", baos);

            byte[] imageInByte = baos.toByteArray();


            documentoDTO.setDocumento(imageInByte);
            documentoDTO.setIdDocumento(mensajeRespuesta3.getDocumentoDTOList().get(0).getIdDocumento());
            MensajeRespuesta mensajeRespuestaTest = contentControlAlfresco.transformaEstampaPd(documentoDTO);
            documentoDTO = (DocumentoDTO) mensajeRespuestaTest.getResponse().get("documento");
            assertEquals("0000", mensajeRespuestaTest.getCodMensaje());
            contentControlAlfresco.eliminardocumento(documentoDTO.getIdDocumento());

        } catch (SystemException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testEstamparEtiquetaRadicacionHTMLSuccess() {

        DocumentoDTO documentoDTO = ecmConnectionRule.newDocumento("testEstamparEtiquetaRadicacionHTMLSuccess");
        documentoDTO.setDocumento("PCFET0NUWVBFIGh0bWw+CjwhLS0gc2F2ZWQgZnJvbSB1cmw9KDAwNTApaHR0cHM6Ly93d3cuZ29vZ2xlLmNvbS5jdS9fL2Nocm9tZS9uZXd0YWI/aWU9VVRGLTggLS0+CjxodG1sIGxhbmc9ImVzLTQxOSI+CjxoMT5IZWxsbyBXb3JsZCB0ZXN0RXN0YW1wYXJFdGlxdWV0YVJhZGljYWNpb25TdWNjZXNzPC9oMT4KPC9odG1sPg==".getBytes());
        documentoDTO.setTipoDocumento(MimeTypes.getMIMEType("html"));
        try {
            MensajeRespuesta mensajeRespuesta3 = contentControlAlfresco.subirDocumentoPrincipalAdjunto(documentoDTO, "EE");

            documentoDTO.setIdDocumento(mensajeRespuesta3.getDocumentoDTOList().get(0).getIdDocumento());
            //Obtener arreglo de bytes a partir de la imagen
            String imgPath = "src\\test\\resources\\Imagen.png";
            File imgFile = new File(imgPath);
            BufferedImage bufferedImage = ImageIO.read(imgFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", baos);
            byte[] imageInByte = baos.toByteArray();


            documentoDTO.setDocumento(imageInByte);
            MensajeRespuesta mensajeRespuestaTest = contentControlAlfresco.transformaEstampaPd(documentoDTO);
            documentoDTO = (DocumentoDTO) mensajeRespuestaTest.getResponse().get("documento");
            assertEquals("0000", mensajeRespuestaTest.getCodMensaje());
            contentControlAlfresco.eliminardocumento(documentoDTO.getIdDocumento());

        } catch (SystemException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testGenerarArbolSuccess() {
        List<EstructuraTrdDTO> structure = new ArrayList<>();
        List<OrganigramaDTO> organigramaItemList1 = new ArrayList<>();
        List<ContenidoDependenciaTrdDTO> contenidoDependenciaList1 =  new ArrayList<>();

        ContenidoDependenciaTrdDTO contenidoDependenciaTrdDTO1=new ContenidoDependenciaTrdDTO();
        contenidoDependenciaTrdDTO1.setCodSerie("0025633");
        contenidoDependenciaTrdDTO1.setCodSubSerie("");
        contenidoDependenciaTrdDTO1.setDiposicionFinal(1);
        contenidoDependenciaTrdDTO1.setIdOrgAdm("900");
        contenidoDependenciaTrdDTO1.setIdOrgOfc("900910");
        contenidoDependenciaTrdDTO1.setNomSerie("CONTRATOS");
        contenidoDependenciaTrdDTO1.setNomSubSerie("");
        contenidoDependenciaTrdDTO1.setProcedimiento("Se deben conservar los documentos");
        contenidoDependenciaTrdDTO1.setRetArchivoCentral(7L);
        contenidoDependenciaTrdDTO1.setRetArchivoGestion(2L);

        contenidoDependenciaList1.add(contenidoDependenciaTrdDTO1);

        ContenidoDependenciaTrdDTO contenidoDependenciaTrdDTO2=new ContenidoDependenciaTrdDTO();
        contenidoDependenciaTrdDTO2.setCodSerie("100");
        contenidoDependenciaTrdDTO2.setCodSubSerie("1");
        contenidoDependenciaTrdDTO2.setDiposicionFinal(2);
        contenidoDependenciaTrdDTO2.setIdOrgAdm("900");
        contenidoDependenciaTrdDTO2.setIdOrgOfc("900910");
        contenidoDependenciaTrdDTO2.setNomSerie("Créditos");
        contenidoDependenciaTrdDTO2.setNomSubSerie("Créditos Hipotecarios");
        contenidoDependenciaTrdDTO2.setProcedimiento("Eliminar");
        contenidoDependenciaTrdDTO2.setRetArchivoCentral(6L);
        contenidoDependenciaTrdDTO2.setRetArchivoGestion(6L);

        contenidoDependenciaList1.add(contenidoDependenciaTrdDTO2);

        OrganigramaDTO organigramaDTO1 = new OrganigramaDTO();
        organigramaDTO1.setCodOrg("000");
        organigramaDTO1.setIdeOrgaAdmin(46L);
        organigramaDTO1.setNomOrg("000_SOAINT1");
        organigramaDTO1.setTipo("P");
        organigramaItemList1.add(organigramaDTO1);

        OrganigramaDTO organigramaDTO2 = new OrganigramaDTO();
        organigramaDTO2.setCodOrg("900");
        organigramaDTO2.setIdeOrgaAdmin(77L);
        organigramaDTO2.setNomOrg("900_VICEPRESIDENCIA DE TALENTO HUMANO");
        organigramaDTO2.setTipo("H");
        organigramaItemList1.add(organigramaDTO2);

        OrganigramaDTO organigramaDTO3 = new OrganigramaDTO();
        organigramaDTO3.setCodOrg("900910");
        organigramaDTO3.setIdeOrgaAdmin(78L);
        organigramaDTO3.setNomOrg("900.910_GERENCIA NACIONAL DE GESTIÓN DEL TALENTO HUMANO");
        organigramaDTO3.setTipo("H");
        organigramaItemList1.add(organigramaDTO3);

        EstructuraTrdDTO estuEstructuraTrdDTO=new EstructuraTrdDTO();
        estuEstructuraTrdDTO.setContenidoDependenciaList(contenidoDependenciaList1);
        estuEstructuraTrdDTO.setOrganigramaItemList(organigramaItemList1);
        structure.add(estuEstructuraTrdDTO);

        //Segundo elemento de la estructura

        List<OrganigramaDTO> organigramaItemList2 = new ArrayList<>();
        List<ContenidoDependenciaTrdDTO> contenidoDependenciaList2 =  new ArrayList<>();

        ContenidoDependenciaTrdDTO contenidoDependenciaTrdDTO4=new ContenidoDependenciaTrdDTO();
        contenidoDependenciaTrdDTO4.setCodSerie("00256");
        contenidoDependenciaTrdDTO4.setCodSubSerie("1");
        contenidoDependenciaTrdDTO4.setDiposicionFinal(1);
        contenidoDependenciaTrdDTO4.setIdOrgAdm("100");
        contenidoDependenciaTrdDTO4.setIdOrgOfc("100110");
        contenidoDependenciaTrdDTO4.setNomSerie("CONTRATOS");
        contenidoDependenciaTrdDTO4.setNomSubSerie("Contratos de Obra");
        contenidoDependenciaTrdDTO4.setProcedimiento("Una vez cumplido su tiempo proceder con la disposición final");
        contenidoDependenciaTrdDTO4.setRetArchivoCentral(4L);
        contenidoDependenciaTrdDTO4.setRetArchivoGestion(1L);

        contenidoDependenciaList2.add(contenidoDependenciaTrdDTO4);



        ContenidoDependenciaTrdDTO contenidoDependenciaTrdDTO5=new ContenidoDependenciaTrdDTO();
        contenidoDependenciaTrdDTO5.setCodSerie("00256");
        contenidoDependenciaTrdDTO5.setCodSubSerie("");
        contenidoDependenciaTrdDTO5.setDiposicionFinal(2);
        contenidoDependenciaTrdDTO5.setIdOrgAdm("100");
        contenidoDependenciaTrdDTO5.setIdOrgOfc("100110");
        contenidoDependenciaTrdDTO5.setNomSerie("CONTRATOS");
        contenidoDependenciaTrdDTO5.setNomSubSerie("");
        contenidoDependenciaTrdDTO5.setProcedimiento("ok");
        contenidoDependenciaTrdDTO5.setRetArchivoCentral(10L);
        contenidoDependenciaTrdDTO5.setRetArchivoGestion(5L);

        contenidoDependenciaList2.add(contenidoDependenciaTrdDTO5);

        ContenidoDependenciaTrdDTO contenidoDependenciaTrdDTO6=new ContenidoDependenciaTrdDTO();
        contenidoDependenciaTrdDTO6.setCodSerie("00256");
        contenidoDependenciaTrdDTO6.setCodSubSerie("");
        contenidoDependenciaTrdDTO6.setDiposicionFinal(2);
        contenidoDependenciaTrdDTO6.setIdOrgAdm("100");
        contenidoDependenciaTrdDTO6.setIdOrgOfc("100110");
        contenidoDependenciaTrdDTO6.setNomSerie("CONTRATOS");
        contenidoDependenciaTrdDTO6.setNomSubSerie("");
        contenidoDependenciaTrdDTO6.setProcedimiento("ok");
        contenidoDependenciaTrdDTO6.setRetArchivoCentral(10L);
        contenidoDependenciaTrdDTO6.setRetArchivoGestion(5L);

        contenidoDependenciaList2.add(contenidoDependenciaTrdDTO6);

        ContenidoDependenciaTrdDTO contenidoDependenciaTrdDTO7=new ContenidoDependenciaTrdDTO();
        contenidoDependenciaTrdDTO7.setCodSerie("0056");
        contenidoDependenciaTrdDTO7.setCodSubSerie("754");
        contenidoDependenciaTrdDTO7.setDiposicionFinal(1);
        contenidoDependenciaTrdDTO7.setIdOrgAdm("100");
        contenidoDependenciaTrdDTO7.setIdOrgOfc("100110");
        contenidoDependenciaTrdDTO7.setNomSerie("INFORMES");
        contenidoDependenciaTrdDTO7.setNomSubSerie("Informes de gestión");
        contenidoDependenciaTrdDTO7.setProcedimiento("Una vez cumplido su tiempo proceder con la disposición final");
        contenidoDependenciaTrdDTO7.setRetArchivoCentral(1L);
        contenidoDependenciaTrdDTO7.setRetArchivoGestion(2L);

        contenidoDependenciaList2.add(contenidoDependenciaTrdDTO7);

        ContenidoDependenciaTrdDTO contenidoDependenciaTrdDTO8=new ContenidoDependenciaTrdDTO();
        contenidoDependenciaTrdDTO8.setCodSerie("323");
        contenidoDependenciaTrdDTO8.setCodSubSerie("134");
        contenidoDependenciaTrdDTO8.setDiposicionFinal(1);
        contenidoDependenciaTrdDTO8.setIdOrgAdm("100");
        contenidoDependenciaTrdDTO8.setIdOrgOfc("100110");
        contenidoDependenciaTrdDTO8.setNomSerie("ADMINISTRACION LEGAL");
        contenidoDependenciaTrdDTO8.setNomSubSerie("Tutelas");
        contenidoDependenciaTrdDTO8.setProcedimiento("Una vez cumplido su tiempo proceder con la disposición final");
        contenidoDependenciaTrdDTO8.setRetArchivoCentral(4L);
        contenidoDependenciaTrdDTO8.setRetArchivoGestion(1L);

        contenidoDependenciaList2.add(contenidoDependenciaTrdDTO8);

        OrganigramaDTO organigramaDTO4 = new OrganigramaDTO();
        organigramaDTO4.setCodOrg("000");
        organigramaDTO4.setIdeOrgaAdmin(46L);
        organigramaDTO4.setNomOrg("000_SOAINT1");
        organigramaDTO4.setTipo("P");
        organigramaItemList2.add(organigramaDTO4);

        OrganigramaDTO organigramaDTO5 = new OrganigramaDTO();
        organigramaDTO5.setCodOrg("100");
        organigramaDTO5.setIdeOrgaAdmin(80L);
        organigramaDTO5.setNomOrg("100_PRESIDENCIA");
        organigramaDTO5.setTipo("H");
        organigramaItemList2.add(organigramaDTO5);

        OrganigramaDTO organigramaDTO6 = new OrganigramaDTO();
        organigramaDTO6.setCodOrg("100110");
        organigramaDTO6.setIdeOrgaAdmin(81L);
        organigramaDTO6.setNomOrg("100.110_OFICINA NACIONAL DE CONTROL INTERNO");
        organigramaDTO6.setTipo("H");
        organigramaItemList2.add(organigramaDTO6);

        EstructuraTrdDTO estructuraTrdDTO1=new EstructuraTrdDTO();
        estructuraTrdDTO1.setContenidoDependenciaList(contenidoDependenciaList1);
        estructuraTrdDTO1.setOrganigramaItemList(organigramaItemList1);
        structure.add(estructuraTrdDTO1);


        Carpeta carpeta = new Carpeta();
        carpeta.setFolder(conexion.getSession().getRootFolder());
        try {
            assertEquals("0000",contentControlAlfresco.generarArbol(structure,carpeta).getCodMensaje());
        } catch (SystemException e) {
            e.printStackTrace();
        }
        Folder root = conexion.getSession().getRootFolder();
        ItemIterable<CmisObject> children = root.getChildren();
        for (CmisObject o : children) {
           if("000_SOAINT1".equals(o.getName())){
               Folder folder=(Folder)o;
               folder.deleteTree(true, UnfileObject.DELETE, true);
           }
        }
    }
}