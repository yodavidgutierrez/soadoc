package co.com.foundation.sgd.apigateway.apis;

import co.com.foundation.sgd.apigateway.apis.delegator.ECMClient;
import co.com.foundation.sgd.apigateway.apis.delegator.ECMUtils;
import co.com.foundation.sgd.apigateway.apis.delegator.FuncionarioClient;
import co.com.foundation.sgd.apigateway.security.annotations.JWTTokenSecurity;
import co.com.soaint.foundation.canonical.correspondencia.FuncionarioDTO;
import co.com.soaint.foundation.canonical.ecm.DocumentoDTO;
import co.com.soaint.foundation.canonical.ecm.MensajeRespuesta;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.*;

@Path("/digitalizar-documento-gateway-api")
@Log4j2
public class DigitalizarDocumentoGatewayApi {


    @Autowired
    private FuncionarioClient clientFuncionario;

    @Autowired
    private ECMClient client;


    public DigitalizarDocumentoGatewayApi() {
        super();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }
    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA +";charset=UTF-8")
    public Response digitalizar(MultipartFormDataInput formDataInput) {
        log.info("ProduccionDocumentalGatewayApi - [content] : ");

        List<String> ecmIds = new ArrayList<>();
        Map<String, InputPart> _files = ECMUtils.findFiles(formDataInput);
        log.info(_files.size() + " -> Document Size");
        _files.forEach((s, inputPart) -> {
            try {
                log.info("Key: " + s + ", Value: " + inputPart.getBodyAsString());
            } catch (IOException e) {
                e.printStackTrace();
                log.error(e);
            }
        });
        DocumentoDTO documentoECMDTO = new DocumentoDTO();
        MensajeRespuesta parentResponse = null;
        Boolean canUploadAnexos = true;

        String ideEcmPrincipal = "";

        try {
            String dependencia = formDataInput.getFormDataPart("dependencia", String.class, null);
            String sede = formDataInput.getFormDataPart("sede", String.class, null);
            String tipoComunicacion = formDataInput.getFormDataPart("tipoComunicacion", String.class, null);
            String nroRadicado = formDataInput.getFormDataPart("nroRadicado", String.class, null);
            String codigoDependencia = formDataInput.getFormDataPart("codigoDependencia", String.class, null);
            String autor = formDataInput.getFormDataPart("autor",String.class, null);
            String remitente = formDataInput.getFormDataPart("nombreRemitente", String.class, null);
            InputStream inputStream = formDataInput.getFormDataPart("documento", InputStream.class, null);
            List<String> referidoList = new ArrayList<>();
            if (null != formDataInput.getFormDataMap().get("referidoList")) {
                for (InputPart referido : formDataInput.getFormDataMap().get("referidoList")) {
                    referidoList.add(referido.getBodyAsString());
                }
            }

            log.info("DepCode: " + codigoDependencia);
            log.info("NumRad: " + nroRadicado);
            log.info("TipoComunicacion: " + tipoComunicacion);
            log.info("formDataInput.getFormDataMap().containsKey(\"principalFileName\"): " + formDataInput.getFormDataMap().containsKey("principalFileName"));


            String[] referidoArray = Arrays.copyOf(referidoList.toArray(), referidoList.size(), String[].class);

            InputStream result = null;

            if(formDataInput.getFormDataMap().containsKey("principalFileName")){
                byte[] principalFileName = formDataInput.getFormDataPart("principalFileName", String.class, null).getBytes("UTF-8");
                log.info("************** VALOR DEL UTF-8 ANTES:::::"+ principalFileName);
                String principalFileNameUTF8 = new String(principalFileName, "UTF-8" );
                principalFileNameUTF8 = URLDecoder.decode(principalFileNameUTF8,"UTF-8");
                log.info("************** VALOR DEL UTF-8 COVERTIDO:::::"+ principalFileNameUTF8);
                InputPart parent = _files.get(principalFileNameUTF8);

                result = parent.getBody(InputStream.class, null);
                documentoECMDTO.setDocumento(IOUtils.toByteArray(result));
                documentoECMDTO.setDependencia(dependencia);
                documentoECMDTO.setSede(sede);
                documentoECMDTO.setTipoDocumento("application/pdf");
                documentoECMDTO.setNombreDocumento(principalFileNameUTF8);
                documentoECMDTO.setNroRadicado(nroRadicado);
                documentoECMDTO.setCodigoDependencia(codigoDependencia);
                documentoECMDTO.setNroRadicadoReferido(referidoArray);
                documentoECMDTO.setNombreRemitente(remitente);
                documentoECMDTO.setDocAutor(autor);
                parentResponse = client.uploadDocument(documentoECMDTO, tipoComunicacion);
                _files.remove(principalFileNameUTF8);

                canUploadAnexos = "0000".equals(parentResponse.getCodMensaje());
                List<DocumentoDTO> documentoDTO = parentResponse.getDocumentoDTOList();
                if (null == documentoDTO || documentoDTO.isEmpty())
                    canUploadAnexos = false;
                else
                    ideEcmPrincipal = documentoDTO.get(0).getIdDocumento();
            }
            else if(formDataInput.getFormDataMap().containsKey("ideEcmPrincipal")){
               parentResponse = new MensajeRespuesta("Se han subido los documentos exitosamente","0000");
              ideEcmPrincipal = formDataInput.getFormDataPart("ideEcmPrincipal",String.class,null);
            }
            else
                canUploadAnexos = false;



            List<String> firmas = new ArrayList<>();
            firmas.add("3");
            /// invocar servicio de espampa de documeto
            MensajeRespuesta pruebam = client.estamparEtiquetaRadicacion(DocumentoDTO.newInstance()
                    .codigoDependencia(dependencia)
                    .nroRadicado(nroRadicado)
                    .idDocumento(ideEcmPrincipal)
                    .documento(IOUtils.toByteArray(inputStream))
                    .build());
            log.info("*******---------***** NR-RADICADO:"+nroRadicado);

            if(canUploadAnexos){
                ecmIds.add(ideEcmPrincipal);
                if (!_files.isEmpty()) {
                    client.uploadDocumentsAsociates(ideEcmPrincipal, _files, sede, dependencia, tipoComunicacion, nroRadicado, referidoArray).forEach(mensajeRespuesta -> {
                        if ("0000".equals(mensajeRespuesta.getCodMensaje())) {
                            List<DocumentoDTO> documentoDTO1 = mensajeRespuesta.getDocumentoDTOList();
                            ecmIds.add(documentoDTO1.get(0).getIdDocumento());
                        }
                    });
                }
            }

        } catch (Exception e) {
            log.error("Error al subir documentos: ", e);
        }



        return Response.status(Response.Status.OK).entity(parentResponse).build();
    }

    @POST
    @Path("/versionar-documento")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @JWTTokenSecurity
    public Response versionarDocumento(MultipartFormDataInput formDataInput) {

        log.info("DigitalizarDocumentoComunicacionGatewayApi - [content] : Subir version documento");

        MensajeRespuesta clientResponse = null;
        DocumentoDTO documentoDTO = new DocumentoDTO();
        try {
            InputStream inputStream = formDataInput.getFormDataPart("documento", InputStream.class, null);
            documentoDTO.setDocumento(IOUtils.toByteArray(inputStream));

            if (null != formDataInput.getFormDataPart("idDocumento", String.class, null)) {
                documentoDTO.setIdDocumento(formDataInput.getFormDataPart("idDocumento", String.class, null));
            }

            documentoDTO.setNombreDocumento(formDataInput.getFormDataPart("nombreDocumento", String.class, null));
            documentoDTO.setTipoDocumento(formDataInput.getFormDataPart("tipoDocumento", String.class, null));
            documentoDTO.setSede(formDataInput.getFormDataPart("sede", String.class, null));
            documentoDTO.setDependencia(formDataInput.getFormDataPart("dependencia", String.class, null));
            String selector = formDataInput.getFormDataPart("selector", String.class, null);
            if (0 < formDataInput.getFormDataPart("nroRadicado", String.class, null).length()) {
                documentoDTO.setNroRadicado(formDataInput.getFormDataPart("nroRadicado", String.class, null));
            }

            clientResponse = this.client.uploadVersionDocumento(documentoDTO, selector);
            log.info(clientResponse);

        } catch (Exception ex) {
            return this.EcmErrorMessage(ex);
        }

        return Response.status(Response.Status.OK).entity(clientResponse).build();
    }

    private Response EcmErrorMessage(@NotNull Exception ex) {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("codMensaje", "9999");
        jsonResponse.put("mensaje", ex.getMessage());

        return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse.toJSONString()).build();
    }


    @GET
    @Path("/obtener-documento/{idDocumento}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response constantes(@PathParam("idDocumento") String idDocumento) {
        log.info("DigitalizarDocumentoGatewayApi - [trafic] - obteniendo Documento desde el ecm: " + idDocumento);
        Response response = client.findByIdDocument(idDocumento);
        InputStream responseObject = response.readEntity(InputStream.class);
//        response.ok(responseObject).header ("Content-Type", "application/pdf");
        return Response.status(Response.Status.OK).entity(responseObject).build();
    }

    @GET
    @Path("/obtener-documentos-asociados/{idDocumento}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerdocumentosasociadosID(@PathParam("idDocumento") String idDocumento) {
        log.info("DigitalizarDocumentoGatewayApi - [trafic] - obteniendo Documento asociados desde el ecm: " + idDocumento);
        MensajeRespuesta mensajeRespuesta = client.findDocumentosAsociadosID(idDocumento);
        return Response.status(Response.Status.OK).entity(mensajeRespuesta).build();
    }

    @GET
    @Path("/obtener-documentos-asociados-radicado/{nroRadicado}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerdocumentosasociadosNroRadicado(@PathParam("nroRadicado") String nroRadicado) {
        log.info("DigitalizarDocumentoGatewayApi - [trafic] - obteniendo Documento asociados desde el ecm broRadicado: " + nroRadicado);
        MensajeRespuesta mensajeRespuesta = client.findDocumentosAsociadosRadicado(nroRadicado);
        return Response.status(Response.Status.OK).entity(mensajeRespuesta).build();
    }

    @POST
    @Path("/eliminarprincipal/{documentId}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response deleteDocumentById(@PathParam("documentId") String documentId) {
        Response response = client.deleteDocumentById(documentId);
        String removed = response.readEntity(String.class);
        return Response.status(response.getStatus()).entity(removed).build();
    }


    @POST
    @Path("/estampar-etiqueta-radicacion-reintentar/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @JWTTokenSecurity
    public Response estamparEtiquetaRadicacionReintentar(MultipartFormDataInput formDataInput) {


        try {
            final String idDocumento = formDataInput.getFormDataPart("idDocumento", String.class, null);

            if (idDocumento.equals("01")){
                log.error("El docuemento fue firmado con exito");
                MensajeRespuesta respuesta = MensajeRespuesta.newInstance()
                        .codMensaje("0000")
                        .mensaje("El docuemento fue firmado con exito")
                        .build();
                return Response.status(Response.Status.OK).entity(respuesta).build();
            }else{
                log.error("No se pudo firmar el documento, vuelva a intentarlo, y/o consulte con el administrador");
                MensajeRespuesta respuesta = MensajeRespuesta.newInstance()
                        .codMensaje("4004")
                        .mensaje("No se pudo firmar el documento, vuelva a intentarlo, y/o consulte con el administrador")
                        .build();
                return Response.status(Response.Status.OK).entity(respuesta).build();
            }
        } catch (IOException e) {
            log.error("Error del Sistema {}", e.getMessage());
            MensajeRespuesta respuesta = MensajeRespuesta.newInstance()
                    .codMensaje("1223")
                    .mensaje(e.getMessage())
                    .build();
            return Response.status(Response.Status.OK).entity(respuesta).build();
        }
    }


    @POST
    @Path("/estampar-etiqueta-radicacion/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @JWTTokenSecurity
    public Response estamparEtiquetaRadicacion(MultipartFormDataInput formDataInput) {
        if (null == formDataInput) {
            log.error("Esta vacio el Multipart");
            return Response.serverError().build();
        }
        try {


            log.info("Procesando la informacion del multipart");
            final String dependencyCode = formDataInput.getFormDataPart("codigoDependencia", String.class, null);
            final String nroRadicado = formDataInput.getFormDataPart("nroRadicado", String.class, null);
            final String idDocumento = formDataInput.getFormDataPart("idDocumento", String.class, null);
            InputStream inputStream = formDataInput.getFormDataPart("documento", InputStream.class, null);
            final String firmasJson = formDataInput.getFormDataPart("firmas",  String.class,null);
            final String reintentar = formDataInput.getFormDataPart("reintentar", String.class, null );

            ObjectMapper mapper = new ObjectMapper();

            List<String> firmas = mapper.readValue( firmasJson ,TypeFactory.defaultInstance().constructCollectionType(List.class, String.class));
            List<String> listFirmas = new ArrayList<>();
            String mesanejeFirmaPolitica = null;
            for (String loginFirma : firmas){

                try{
                    log.info("*******------- loginFirma :::: loginFirma]::::: " + loginFirma );

                    mesanejeFirmaPolitica = clientFuncionario.buscarFirmaLogin(loginFirma);

                    log.info("*******------- Respuesta de servicio de listarFuncionariosByLoginnames" + mesanejeFirmaPolitica );

                    listFirmas.add(mesanejeFirmaPolitica.isEmpty() ? "010203040506070809" : mesanejeFirmaPolitica);
                }catch (Exception e){
                    log.error("Error del Sistema ---Servicio obtenerFuncionario -{}", e.getMessage());
                    listFirmas.add("010203040506070809");
                }

            }
            log.info("********------- List de listFirmas ::: "+listFirmas);
            DocumentoDTO estampaDoc = new DocumentoDTO();
            estampaDoc.setCodigoDependencia(dependencyCode);
            estampaDoc.setNroRadicado(nroRadicado);
            estampaDoc.setIdDocumento(idDocumento);
            log.info("valor de reinetentar "+ reintentar );
            if (!reintentar.equals("true")){
                estampaDoc.setDocumento(IOUtils.toByteArray(inputStream));
                log.info("valor de reinetentar dentro del IF  "+ reintentar );
            }
            estampaDoc.setPoliticaFirma(listFirmas);
            estampaDoc.setReintentar(reintentar);

            /*MensajeRespuesta mensajeRespuesta = client.estamparEtiquetaRadicacion(DocumentoDTO.newInstance()
                    .codigoDependencia(dependencyCode)
                    .nroRadicado(nroRadicado)
                    .idDocumento(idDocumento)
                    .documento(IOUtils.toByteArray(inputStream))
                    .politicaFirma(listFirmas)
                    .reintentar(reintentar)
                    .build());*/
            MensajeRespuesta mensajeRespuesta = client.estamparEtiquetaRadicacion(estampaDoc);
            return Response.status(Response.Status.OK).entity(mensajeRespuesta).build();

        } catch (Exception e) {
            log.error("Error del Sistema {}", e.getMessage());
            MensajeRespuesta respuesta = MensajeRespuesta.newInstance()
                    .codMensaje("1223")
                    .mensaje(e.getMessage())
                    .build();
            return Response.status(Response.Status.OK).entity(respuesta).build();
        }
    }

}
