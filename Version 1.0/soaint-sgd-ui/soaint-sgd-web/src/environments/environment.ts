// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.

/*const host = 'http://192.168.1.81:28080/soaint-sgd-web-api-gateway/apis';
const ecmHost = 'http://192.168.1.81:28080/ecm-integration-services/apis/ecm';*/

/*const host = 'http://192.168.1.181:28080/soaint-sgd-web-api-gateway/apis';
const ecmHost = 'http://192.168.1.181:28080/ecm-integration-services/apis/ecm';*/

const host = (<any>window).host;
const ecmHost = (<any>window).ecmHost;
const baseUrl = (<any>window).base_url;

export const megaf = false;

export const environment = {
  production: false,
  base_url: baseUrl,
  security_endpoint: `${host}/securidad-gateway-api`,
  product_endpoint: `${host}/productos-gateway-api`,
  tipoDestinatario_endpoint: `${host}/tipo-destinatario-gateway-api`,
  pais_endpoint: `${host}/pais-gateway-api`,
  unidadTiempo_endpoint: `${host}/unidad-tiempo-gateway-api`,
  tipologiaDocumental_endpoint: `${host}/tipologia-documental-gateway-api`,
  tipoTelefono_endpoint: `${host}/tipo-telefono-gateway-api`,
  tipoPersona_endpoint: `${host}/tipo-persona-gateway-api`,
  tipoPlantilla_endpoint: `${host}/tipo-plantilla-gateway-api`,
  tipoDocumento_endpoint: `${host}/tipo-documento-gateway-api`,
  tipoComunicacion_endpoint: `${host}/tipo-comunicacion-gateway-api`,
  tipoComunicacionSalida_endpoint: `${host}/tipo-comunicacion-salida-gateway-api`,
  tipoAnexos_endpoint: `${host}/tipo-anexos-gateway-api`,
  anexoUpdate: `${host}/tipo-anexos-gateway-api/updateAnexos`,
  soporteAnexo_endpoint: `${host}/soporte-anexos-gateway-api`,
  modalidadCorreo_endpoint: `${host}/modalidad-correo-gateway-api`,
  claseEnvio_endpoint: `${host}/clase-envio-gateway-api`,
  sedeAdministrativa_endpoint: `${host}/sede-administrativa-gateway-api`,
  mediosRecepcion_endpoint: `${host}/medios-recepcion-gateway-api`,
  dependenciaGrupo_endpoint: `${host}/dependencia-grupo-gateway-api`,
  dependencias_endpoint: `${host}/dependencia-grupo-gateway-api/all-dependencias`,
  tratamientoCortesia_endpoint: `${host}/tratamiento-cortesia-gateway-api`,
  actuaCalidad_endpoint: `${host}/actua-calidad-gateway-api`,
  departamento_endpoint: `${host}/departamento-gateway-api`,
  municipio_endpoint: `${host}/municipio-gateway-api`,
  proceso_endpoint: `${host}/proceso-gateway-api`,
  startProcess_endpoint: `${host}/proceso-gateway-api/iniciar`,
  instancesProcess_endpoint: `${host}/proceso-gateway-api/listar-instancias`,
  tasksInsideProcess_endpoint: `${host}/proceso-gateway-api/listar/estados-instancia`,
  tasksForStatus_endpoint: `${host}/proceso-gateway-api/tareas/listar/estados`,
  tasksForUser_endpoint: `${host}/proceso-gateway-api/tareas/listarporusuario`,
  reasignTask_endpoint:`${host}/proceso-gateway-api/tareas/reasignar`,
  reasignTasks_endpoint:`${host}/proceso-gateway-api/tareas/reasignarVarias`,
  tasksStats_endpoint: `${host}/proceso-gateway-api/tareas/listar/usuario`,
  tasksStartProcess: `${host}/proceso-gateway-api/tareas/iniciar/`,
  tasksReserveProcess: `${host}/proceso-gateway-api/tareas/reservar`,
  tasksCompleteProcess: `${host}/proceso-gateway-api/tareas/completar/`,
  tasksAbortProcess: `${host}/proceso-gateway-api/tareas/abortar/`,
  taskStatus_endpoint: `${host}/tarea-gateway-api/tarea`,
  taskStatistic_endpoint: `${host}/proceso-gateway-api/tareas/estadisticas/`,
  bis_endpoint: `${host}/bis-gateway-api`,
  tipoComplemento_endpoint: `${host}/tipo-complemento-gateway-api`,
  prefijoCuadrante_endpoint: `${host}/prefijo-cuadrante-gateway-api`,
  orientacion_endpoint: `${host}/orientacion-gateway-api`,
  tipoVia_endpoint: `${host}/tipo-via-gateway-api`,
  motivoNoCreacionUd_endpoint:`${host}/motivo-no-creacion-ud-gateway-api`,
  radicarComunicacion_endpoint: `${host}/correspondencia-gateway-api/radicar`,
  radicarSalida_endpoint:`${host}/correspondencia-gateway-api/radicar_salida`,
  radicarDocProducido_endpoint:`${host}/correspondencia-gateway-api/radicar-documento-producido`,
  listarCorrespondencia_endpoint: `${host}/correspondencia-gateway-api/listar-comunicaciones`,
  listarCorrespondencia_salida_distribucion_fisica_externa_endpoint: `${host}/correspondencia-gateway-api/listar-comunicaciones-salida-externa-distribucion-fisica`,
  listarCorrespondencia_salida_distribucion_fisica_endpoint: `${host}/correspondencia-gateway-api/listar-comunicaciones-distribucion-fisica-salida`,
  obtenerFuncionario_endpoint: `${host}/funcionario-gateway-api`,
  listarFuncionarios_endpoint: `${host}/funcionario-gateway-api/funcionarios`,
  updateFuncionarios_roles_endpoint: `${host}/funcionario-gateway-api/funcionarios`,
  updateFuncionarios_endpoint: `${host}/funcionario-gateway-api/actualizar-funcionario`,
  obtenerFuncionarios_roles_endpoint: `${host}/funcionario-gateway-api/funcionarios/roles`,
  buscarFuncionarios_endpoint: `${host}/funcionario-gateway-api/funcionarios/buscar`,
  redireccionarComunicaciones_endpoint: `${host}/correspondencia-gateway-api/redireccionar`,
  devolverComunicaciones_endpoint: `${host}/correspondencia-gateway-api/devolver`,
  devolverComunicacionesAsigancion_endpoint: `${host}/correspondencia-gateway-api/devolver/asignacion`,
  digitalizar_doc_upload_endpoint: `${host}/digitalizar-documento-gateway-api`,
  metricasTiempoRadicacion_rule_endpoint: `${host}/correspondencia-gateway-api/metricasTiempo`,
  verificarRedirecciones_rule_endpoint: `${host}/correspondencia-gateway-api/verificar-redirecciones`,
  digitalizacion_epehsoft_endponint: `${host}/digitalizacion-gateway-api/digitalizar`,
  reintentarFirmaDigital_endpoint: `${host}/digitalizar-documento-gateway-api/estampar-etiqueta-radicacion-reintentar`,

  // Carga masiva endpoint

  carga_masiva_endpoint_listar: `${host}/carga-masiva-gateway-api/listadocargamasiva`,
  carga_masiva_endpoint_estado: `${host}/carga-masiva-gateway-api/estadocargamasiva`,
  carga_masiva_endpoint_upload: `${host}/carga-masiva-gateway-api/cargar-fichero`,

  // Asignacion Enpoints
  asignarComunicaciones_endpoint: `${host}/correspondencia-gateway-api/asignar`,
  reasignarComunicaciones_endpoint: `${host}/correspondencia-gateway-api/reasignar`,
  obtenerObservaciones_endpoint: `${host}/correspondencia-gateway-api/obtenerObservaciones/`,
  obtenerDocumento_endpoint: `${host}/digitalizar-documento-gateway-api/obtener-documento/`,
  obtenerDocumento_asociados_endpoint: `${host}/digitalizar-documento-gateway-api/obtener-documentos-asociados`,
  obtenerDocumento_nro_radicado_endpoint: `${host}/digitalizar-documento-gateway-api/obtener-documentos-asociados-radicado`,
  obtenerComunicacion_endpoint: `${host}/correspondencia-gateway-api/obtener-comunicacion/`,
  obtenerComunicacionfull_endpoint: `${host}/correspondencia-gateway-api/obtener-comunicacion/full`,
  obtenerContactoDestinatarioExterno_endpoint: `${host}/correspondencia-gateway-api/contactos-destinatario-externo`,
  registrarObservaciones_endpoint: `${host}/correspondencia-gateway-api/registrarObservacion`,
  obtenerConstantesPorCodigo_endpoint: `${host}/correspondencia-gateway-api/constantes`,
  obtenerDependenciasPorCodigo_endpoint: `${host}/dependencia-grupo-gateway-api/dependencias`,
  obtenerMunicipiosPorCodigo_endpoint: `${host}/municipio-gateway-api/municipios`,
  obtenerDocumento: `${ecmHost}/descargarDocumentoECM/?identificadorDoc=`,
  descargarPdf: `${host}/consulta-documentos-gateway-api/descargar-pdf`,
  obtenerDocumentosAdjuntos: `${ecmHost}/obtenerDocumentosAdjuntosECM`,
  obtenerVariablesTarea: `${host}/proceso-gateway-api/tareas/obtener-variables`,
  listarDistrubucion_endpoint: `${host}/correspondencia-gateway-api/listar-distribucion`,
  listarPlanillas_endpoint: `${host}/correspondencia-gateway-api/listar-planillas`,
  listarPlanillasSalida_endpoint: `${host}/correspondencia-gateway-api/listar-planillas-salida`,
  exportarPlanilla_endpoint: `${host}/correspondencia-gateway-api/exportar-plantilla/`,
  exportarPlanillaSalida_endpoint: `${host}/correspondencia-gateway-api/exportar-plantilla-salida-externa/`,
  exportarPlanillaInterna_endpoint: `${host}/correspondencia-gateway-api/exportar-plantilla-salida-interna/`,
  generarPlanilla_endpoint: `${host}/correspondencia-gateway-api/generar-planilla`,
  generarPlanillaSalida_endpoint: `${host}/correspondencia-gateway-api/generar-planilla-salida-externa`,
  generarPlanillaSalidaInterna_endpoint: `${host}/correspondencia-gateway-api/generar-planilla-salida-interna`,
  cargarPlanilla_endpoint: `${host}/correspondencia-gateway-api/cargar-plantilla`,
  recibirDocumentosFisicos_endpoint: `${host}/correspondencia-gateway-api/confirmar-recibo-fisico`,
  salvarCorrespondenciaEntrada_endpoint: `${host}/correspondencia-gateway-api/salvar-correspondencia-entrada`,

  actualizarComunicacion_endpoint: `${host}/correspondencia-gateway-api/actualizar-comunicacion`,
  restablecerCorrespondenciaEntrada_endpoint: `${host}/correspondencia-gateway-api/restablecer_correspondencia_entrada`,
  listarAnexos_endpoint: `${host}/correspondencia-gateway-api/listar-anexos/`,
    // Produccion Documental
  pd_ejecutar_proyeccion_multiple: `${host}/produccion-documental-gateway-api/ejecutar-proyeccion-multiple`,
  pd_obtenerDatosDocXnroRadicado: `${host}/produccion-documental-gateway-api/datos-documento`,
  pd_gestion_documental : {
      subirAnexo: `${host}/produccion-documental-gateway-api/agregar-anexo`,
      eliminarAnexo: `${host}/produccion-documental-gateway-api/eliminar-anexo`,
      obtenerVersionesDocumento : `${host}/produccion-documental-gateway-api/obtener-versiones-documento`,
      subirDocumentoVersionado : `${host}/produccion-documental-gateway-api/versionar-documento`,
      eliminarVersionDocumento : `${host}/produccion-documental-gateway-api/eliminar-version`,
      // ECM Endpoints
      obtenerVersionDocumento : `${ecmHost}/descargarDocumentoVersionECM`,
      obtenerDocumentoPorId: `${ecmHost}/descargarDocumentoECM`,
  },
  listar_serie_subserie: `${host}/unidad-documental-gateway-api/listado-serie-subserie`,

  // Archivar Documento

  ad_obtener_serie_subserie : `${ecmHost}/devolverSerieOSubserie`,
    // http://192.168.1.81:28080/ecm-integration-services/apis/ecm/descargarDocumentoECM/?identificadorDoc=02f2f035-b791-4ec3-b6c0-714dc3dfe95f
  crear_unidad_documental : `${host}/unidad-documental-gateway-api/crear-unidad-documental`,
  listar_unidad_documental_endpoint: `${host}/unidad-documental-gateway-api/listar-unidad-documental`,
  buscar_unidad_documental_endpoint: `${host}/unidad-documental-gateway-api/buscar-unidades-documentales`,
  archivar_documento_endpoint: `${host}/unidad-documental-gateway-api/subir-documentos-unidad-documental`,
  gestionar_unidades_documentales_endpoint: `${host}/unidad-documental-gateway-api/gestionar-unidades-documentales`,
  detalle_unidad_documental_endpoint: `${host}/unidad-documental-gateway-api/detalle-unidad-documental/`,
  listar_documentos_archivar:  `${host}/unidad-documental-gateway-api/listar-documentos-por-archivar/`,
  listar_documentos_archivados: `${host}/unidad-documental-gateway-api/listar-documentos-archivados/`,
  listar_documentos_archivados_ud: `${host}/unidad-documental-gateway-api/obtener-documentos-ud/`,
  crear_solicitud_ud: `${host}/correspondencia-gateway-api/crear-solicitud-unidad-documental/`,
  listar_solicitud_ud_no_tramitadas: `${host}/correspondencia-gateway-api/listar-solicitud-ud-no-tramitadas/`,
  listar_solicitud_ud_tramitadas: `${host}/unidad-documental-gateway-api/listar-solicitud-ud-tramitadas/`,
  actualizar_solicitud_ud: `${host}/unidad-documental-gateway-api/actualizar-solicitud-unidad-documental/`,
  no_tramitar_solicitud_ud: `${host}/unidad-documental-gateway-api/no-tramitar-unidad-documental/`,
  restablecer_archivar_documento: `${host}/unidad-documental-gateway-api/restablecer-archivar-documento-task/`,
  subir_documentos_por_archivar: `${host}/unidad-documental-gateway-api/subir-documentos-por-archivar`,
  guardar_transferencia_documental_endpoint: `${host}/unidad-documental-gateway-api/salvar-transferencia-documental`,
  listar_unidades_documentales_disposicion_endpoint: `${host}/unidad-documental-gateway-api/listar-unidades-documentales-disposicion`,
  aprobar_rechazar_unidades_documentales_endpoint: `${host}/unidad-documental-gateway-api/aprobar-rechazar-disposiciones-finales`,
  upload_template:`${host}/digitalizar-documento-gateway-api/estampar-etiqueta-radicacion`,
  aprobar_rechazar_transferencia_documentales: `${host}/unidad-documental-gateway-api/aprobar-rechazar-transferencias`,
  listar_unidades_documentales_transferir: `${host}/unidad-documental-gateway-api/listar-unidades-documentales-transferir`,
  listar_unidades_documentales_verificar: `${host}/unidad-documental-gateway-api/listar-unidades-documentales-verificar`,
  listar_unidades_documentales_confirmadas:`${host}/unidad-documental-gateway-api/listar-unidades-documentales-confirmadas`,
  confirmar_unidaddes_documentales:`${host}/unidad-documental-gateway-api/confirmar-unidades-documentales-transferidas`,
  datos_contacto: `${host}/correspondencia-gateway-api/obtener-datos-contacto-agente/`,
  datos_contacto_id: `${host}/correspondencia-gateway-api/obtener-datos-contacto-identificacion/`,
  radicados_padre: `${host}/correspondencia-gateway-api/obtener-radicados-padres/`,
  tipoContactoEndPoint:`${host}/tipo-contacto-gateway-api/`,
  listarDependneciaEndPoint:`${host}/instrumento-gateway-api/list-dependencias`,
  listarTipologiasDocumentalesEndPoint:`${host}/instrumento-gateway-api/tipologias-documentales`,
  listarSubseriesEndPoint:`${host}/instrumento-gateway-api/subseries-por-dependencia`,
  obtenerDependenciaPorCodigoEndPoint:`${host}/instrumento-gateway-api/consultar-dependencias`,
  asociarFuncionarioDependneciasEndPoint:`${host}/instrumento-gateway-api/asociar-funcionario-dependnecias`,
  pdObservacionesListarEndPoint:`${host}/pd-observaciones-gateway-api/listar/`,
  pdObservacionesActualizarEndPoint:`${host}/pd-observaciones-gateway-api/actualizar-observaciones/`,
  pdVersionPdfEndPoint:`${host}/unidad-documental-gateway-api/transformar-documento-html-pdf`,
  consultarDocumentos:`${host}/consulta-documentos-gateway-api/consultar-documentos`,
  consultarExpediente:`${host}/consulta-documentos-gateway-api/consultar-expediente`,
  detalleDcoumento:`${host}/consulta-documentos-gateway-api/ver-detalles-documento`,
  consultarDocumentosPorExpediente:`${host}/consulta-documentos-gateway-api/documentos-por-expediente`,
  agenteByNroIdentificacionEndPoint: `${host}/agente-gateway-api/obtener-agente-por-identificacion/`,
  listarComunicacionesSalidaDistribucionInterna: `${host}/correspondencia-gateway-api/listar-comunicaciones-salida-distribucion-interna`,
  actualizarDatosEnvioInterno: `${host}/correspondencia-gateway-api/atualizar-datos-envio-interno`,

  listas:{
    getListasGenericas:`${host}/listas-gateway-api/listas-editables-genericas`,
    getListas:`${host}/listas-gateway-api/listas-editables`,
    create:`${host}/listas-gateway-api/adicionar`,
    edit:`${host}/listas-gateway-api/editar`,
    delete:`${host}/listas-gateway-api/eliminar`
  },
  estadoEntregaSalidaEndPoint:`${host}/estado-entrega-gateway-api/salida`,
  estadoEntregaEntradaEndPoint:`${host}/estado-entrega-gateway-api/entrada`,
  megaf:{
    hijosDirectos:`${host}/megaf-gateway-api/hijos-directos`,
    unidadesConservacion: `${host}/megaf-gateway-api/unidades-fisicas`
  },
  actualizarInstanciaDevolucion:`${host}/correspondencia-gateway-api/atualizar-instancia-gestion-devolucion`,
  dependenciaRadicadoraEndpoint:`${host}/instrumento-gateway-api/dependencia-radicadora`
};

export const process_info = {
  'proceso.correspondencia-entrada': {
    displayValue: 'Correspondencia de entrada',
    show: true
  },
  'proceso.correspondencia-salida': {
    displayValue: 'Correspondencia de salida',
    show: true
  },
  'proceso.gestion-planillas': {
    displayValue: 'Gestion de planillas',
    show: false
  },
  'proceso.gestor-devoluciones': {
    displayValue: 'Gestor de devoluciones',
    show: false
  },
  'proceso.produccion-documental': {
    displayValue: 'Producción documental',
    show: false
  },
  'proceso.produccion-multiples-documentos': {
    displayValue: 'Producción de múltiples documentos',
    show: true
  },
  'proceso.recibir-gestionar-doc': {
    displayValue: 'Recibir y gestionar documento',
    show: false
  },
  'proceso.gestion-unidades-documentales': {
    displayValue: 'Gestión de unidades documentales',
    show: true
  },
  'process.archivar-documento': {
    displayValue : 'Organización y Archivo',
    show: true,
  },
  'proceso.transferencia-documentales': {
    displayValue : 'Transferencias documentales',
    show: true,
  },
  'proceso.gestion-planillas-salida': {
    displayValue : 'Gestión de planillas de salida',
    show: false,
  }

};
