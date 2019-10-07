package co.com.soaint.ecm.util;

import org.springframework.stereotype.Component;

@Component
public final class ConstantesECM {

    public static final String ERROR_COD_MENSAJE = "1224";

    // Class Properties ECM
    public static final String CLASE_BASE = "claseBase";
    public static final String CLASE_SEDE = "claseSede";
    public static final String CLASE_DEPENDENCIA = "claseDependencia";
    public static final String CLASE_SERIE = "claseSerie";
    public static final String CLASE_SUBSERIE = "claseSubserie";
    public static final String CLASE_UNIDAD_DOCUMENTAL = "claseUnidadDocumental";
    public static final String CMCOR = "cmcor:";
    public static final String CMMIG = "cmmig:";

    //******************
    //* properties ECM *
    //******************

    //CM_Unidad_Base
    public static final String CMCOR_UB = "F:" + CMCOR + "CM_Unidad_Base";
    public static final String CMCOR_UB_CODIGO = CMCOR + "CodigoBase";

    // CM_Unidad_Administrativa
    public static final String CMCOR_DEP = "F:" + CMCOR + "CM_Unidad_Administrativa";
    public static final String CMCOR_DEP_CODIGO = CMCOR + "CodigoDependencia";
    public static final String CMCOR_DEP_CODIGO_PADRE = CMCOR + "CodigoUnidadAdminPadre";
    public static final String CMCOR_DEP_RADICADORA = CMCOR + "Radicadora";
    public static final String CMCOR_DEP_ORG_ACTIVO = CMCOR + "orgActivo";

    //CM_Serie
    public static final String CMCOR_SER = "F:" + CMCOR + "CM_Serie";
    public static final String CMCOR_SER_CODIGO = CMCOR + "CodigoSerie";
    public static final String CMCOR_SER_SECURITY_GROUP = CMCOR + "grupoSeguridad";
    public static final String CMCOR_SER_DISP_FINAL = CMCOR + "dispFinal";
    public static final String CMCOR_SER_RET_AC = CMCOR + "retArchivoCentral";
    public static final String CMCOR_SER_RET_AG = CMCOR + "retArchivoGestion";

    //CM_SubSerie
    public static final String CMCOR_SS = "F:" + CMCOR + "CM_Subserie";
    public static final String CMCOR_SS_CODIGO = CMCOR + "CodigoSubserie";

    //CM_Unidad_Documental
    public static final String CMCOR_UD = "F:" + CMCOR + "CM_Unidad_Documental";
    public static final String CMCOR_UD_ACCION = CMCOR + "accion";
    public static final String CMCOR_UD_FECHA_INICIAL = CMCOR + "fechaInicial";
    public static final String CMCOR_UD_INACTIVO = CMCOR + "inactivo";
    public static final String CMCOR_UD_UBICACION_TOPOGRAFICA = CMCOR + "ubicacionTopografica";
    public static final String CMCOR_UD_FECHA_FINAL = CMCOR + "fechaFinal";
    public static final String CMCOR_UD_FECHA_CIERRE = CMCOR + "fechaCierre";
    public static final String CMCOR_UD_ID = CMCOR + "id";
    public static final String CMCOR_UD_FASE_ARCHIVO = CMCOR + "faseArchivo";
    public static final String CMCOR_UD_SOPORTE = CMCOR + "soporte";
    public static final String CMCOR_UD_AUTOR = CMCOR + "xExpAutor";
    public static final String CMCOR_UD_DESCRIPTOR_2 = CMCOR + "descriptor2";
    public static final String CMCOR_UD_DESCRIPTOR_1 = CMCOR + "descriptor1";
    public static final String CMCOR_UD_CERRADA = CMCOR + "cerrada";
    public static final String CMCOR_UD_OBSERVACIONES = CMCOR + "observaciones";
    public static final String CMCOR_UD_DISPOSICION = CMCOR + "disposicion";
    public static final String CMCOR_UD_ESTADO = CMCOR + "estado";
    public static final String CMCOR_UD_MIGRADO = CMCOR + "migrado";


    //CM_DocumentoPersonalizado
    public static final String CMCOR_DOC_RADICADO = CMCOR + "NroRadicado";
    public static final String CMCOR_DOC_REMITENTE = CMCOR + "NombreRemitente";
    public static final String CMCOR_DOC_TIPO_DOCUMENTAL = CMCOR + "TipologiaDocumental";
    public static final String CMCOR_TIPO_DOCUMENTO = CMCOR + "xTipo";
    public static final String CMCOR_NUMERO_REFERIDO = CMCOR + "xNumeroReferido";
    public static final String CMCOR_ID_DOC_PRINCIPAL = CMCOR + "xIdentificadorDocPrincipal";
    public static final String CMCOR_NOMBRE_PROCESO = CMCOR + "nombreProceso";
    public static final String CMCOR_FECHA_RADICACION = CMCOR + "xFechaRadicacion";
    public static final String CMCOR_DOC_ACTUACION = CMCOR + "actuacion";
    public static final String CMCOR_DOC_EVENTO = CMCOR + "evento";
    public static final String CMCOR_DOC_TRAMITE = CMCOR + "tramite";
    public static final String CMCOR_DOC_DEP_ORIGEN = CMCOR + "dependenciaOrigen";
    public static final String CMCOR_DOC_AUTOR = CMCOR + "xDocAutor";
    public static final String CMCOR_DOC_ID_UD = CMCOR + "xIdExpediente";
    public static final String CMCOR_DOC_ANULADO = CMCOR + "anulado";

    //CM_MigracionModelo
    public static final String CMMIG_DOC_CONTROL_RADICACION = CMMIG + "controlRadicacion";
    public static final String CMMIG_DOC_FECHA_ACTO = CMMIG + "fechaActo";
    public static final String CMMIG_DOC_ID_REGISTRO = CMMIG + "idRegistro";
    public static final String CMMIG_DOC_NUMERO_ACTO = CMMIG + "numeroActo";
    public static final String CMMIG_DOC_SECUENCIA_EVENTO = CMMIG + "secuenciaEvento";
    public static final String CMMIG_DOC_TIPO_ACTO = CMMIG + "tipoActo";
    public static final String CMMIG_DOC_TIPO_COMUNICACION = CMMIG + "tipoComunicacion";

    // ECM sms Error
    public static final String ECM_ERROR = "ECM_ERROR";
    public static final String ECM_ERROR_DUPLICADO = "ECM ERROR DUPLICADO";
    public static final String EXISTE_CARPETA = "Existe la Carpeta: ";

    public static final String ERROR_TIPO_EXCEPTION = "### Error tipo Exception----------------------------- :";
    public static final String ERROR_TIPO_IO = "### Error tipo IO----------------------------- :";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    public static final String DOCUMENTO = "documento";
    public static final String DOCUMENTOS_APOYO = "DOCUMENTOS DE APOYO ";
    public static final String AVISO_CREA_DOC = "### Se va a crear el documento..";
    public static final String AVISO_CREA_DOC_ID = "### Documento creado con id ";
    public static final String SEPARADOR = "---";
    public static final String SUCCESS_COD_MENSAJE = "0000";
    public static final String FAIL_RADICADO_COD_MENSAJE = "4004";
    public static final String OPERACION_COMPLETADA_SATISFACTORIAMENTE = "Operacion completada satisfactoriamente";
    public static final String SUCCESS = "SUCCESS";
    public static final String NO_RESULT_MATCH = "Ningun resultado coincide con el criterio de busqueda";
    public static final String P_APP_LINKED = "P:app:linked";

    //Propiedades record Carpeta
    public static final String RMA_IS_CLOSED = "rma:isClosed";
    public static final String RMC_X_AUTO_CIERRE = "rmc:xAutoCierre";
    public static final String RMC_X_FASE_ARCHIVO = "rmc:xFaseArchivo";
    public static final String RMC_X_IDENTIFICADOR = "rmc:xIdentificador";
    public static final String RMC_X_ESTADO_DISPOSICION = "rmc:xEstadoDisposicion";
    public static final String RMC_X_ESTADO_TRANSFERENCIA = "rmc:xEstadoTransferencia";
    public static final String RMC_X_DISPOSICION_HASTA_FECHA = "rmc:xDisposicionHastaFecha";
    public static final String RMC_X_DISPOSICION_FINAL_CARPETA = "rmc:xDisposicionFinalCarpeta";
    public static final String RMC_X_CODIGO_DEPENDENCIA_CARPETA = "rmc:xCodigoDependenciaCarpeta";
    public static final String RMC_X_CONSECUTIVO_CARPETA = "rmc:xConsecutivoTransferenciaCarpeta";

    //Propiedades record Categoria
    public static final String RMC_X_COD_SERIE = "rmc:xCodSerie";
    public static final String RMC_X_COD_SUB_SERIE = "rmc:xCodSubSerie";
    public static final String RMC_X_DISPOSICION_FINAL_CATEGORIA = "rmc:xDisposicionFinalCategoria";
    public static final String RMC_X_CONSECUTIVO_TP_CATEGORIA = "rmc:xConsecutivoTPCategoria";
    public static final String RMC_X_CONSECUTIVO_TS_CATEGORIA = "rmc:xConsecutivoTSCategoria";
    public static final String RMC_X_FONDO = "rmc:xFondo";
    public static final String RMC_X_RET_ARCHIVO_CENTRAL = "rmc:xRetArchivoCentral";
    public static final String RMC_X_RET_ARCHIVO_GESTION = "rmc:xRetArchivoGestion";
    public static final String RMC_X_SECCION = "rmc:xSeccion";
    public static final String RMC_X_SERIE = "rmc:xSerie";
    public static final String RMC_X_SUBSERIE = "rmc:xSubserie";
    public static final String RMC_X_ORG_ACTIVO = "rmc:xOrgActivo";
    //public static final String RMC_X_SUB_SECCION = "rmc:xSubseccion";
    //public static final String RMC_X_SUB_FONDO = "rmc:xSubFondo";

    private ConstantesECM() {
    }
}
