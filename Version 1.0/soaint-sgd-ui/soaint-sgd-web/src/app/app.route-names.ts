import {
  APROBADOR, APROBADOR_DEPENDENCIA, ARCHIVISTA, ARCHIVISTA_AC,
  ASIGNADOR,
  AUXILIAR_CORRESPONDENCIA, BPMMANAGER_ROL, CONSULTA_CLASIFICADO, CONSULTA_PUBLICO, CONSULTA_RESERVADO,
  GESTOR_DEVOLUCIONES,
  PROYECTOR,
  RADICADOR,
  RADICADOR_CONTINGENCIA,
  RECEPCTOR, REVISOR
} from "./app.roles";

export const ROUTES_PATH = {
  task: 'task',
  radicarCofEntrada:{ url : 'radicar-comunicaciones', rol: RADICADOR } ,
  radicarCofSalida: {url : 'radicacion-salida', rol: RADICADOR},
  radicarDocumentoSalida: {url : 'radicacion-documento_salida', rol : PROYECTOR},
  digitalizarDocumento: {url:'digitalizar-documentos',rol: RADICADOR},
  adjuntarDocumento: { url :'adjuntar-documentos', rol : RADICADOR},
  gestionarDevoluciones: {url: 'gestionar-devoluciones',rol:GESTOR_DEVOLUCIONES},
  corregirRadicacion: {url: 'corregir-radicacion',rol:GESTOR_DEVOLUCIONES},
  dashboard: 'home',
  login: 'login',
  workspace: 'workspace',
  processList: 'process',
  asignacionComunicaciones: { url : 'asignacion-comunicaciones', rol:ASIGNADOR },
  recibirGestionarComunicaciones:{url: 'recibir-gestionar-comunicaciones', rol: RECEPCTOR},
  documentosTramite: { url : 'documentos-tramite', rol : ASIGNADOR},
  cargaMasiva: {url :'carga-masiva', rol : RADICADOR_CONTINGENCIA},
  cargaMasivaDetails: 'carga-masiva/record/:id',
  distribucionFisica: {url:'distribucion-fisica',rol:AUXILIAR_CORRESPONDENCIA},
  cargarPlanillas: {url :'cargar-planillas', rol : AUXILIAR_CORRESPONDENCIA },
  produccionDocumentalMultiple: {url:'produccion-documental-multiple',rol: PROYECTOR},
  produccionDocumental: { url: 'producir-documento', roles : {"1" : PROYECTOR, "2": REVISOR, "3": APROBADOR }},
  archivarDocumento:  'archivar-documento',
  securityRole: { url : 'security-role' , rol : BPMMANAGER_ROL},
  gestionUnidadDocumental: {url : 'gestion-unidad-documental',rol : ARCHIVISTA},
  crearUnidadDocumental: {url :'crear-unidad-documental',rol : ARCHIVISTA},
  disposicionFinal: { url : 'disposicion-final',rol : ARCHIVISTA_AC},
  completarDatosDistribucion:{ url: "completar-datos-distribucion", rol: RADICADOR},
  transferenciasDocumentales:{url : "transferencia-documentales", roles :{"1":APROBADOR_DEPENDENCIA, "2" : ARCHIVISTA_AC, "3" : ARCHIVISTA_AC, "4" :  ARCHIVISTA_AC} },
  distribucionFisicaSalida:{url :"distribucion-fisica-salida", rol : AUXILIAR_CORRESPONDENCIA},
  cargarPlanillaSalida:{ url:"cargar-planilla-salida", rol : AUXILIAR_CORRESPONDENCIA},
  cargarPlanillaSalidaInterna:{ url:"cargar-planilla-salida-interna", rol : AUXILIAR_CORRESPONDENCIA},
  taskManager:{ url : "administracion-tarea", rol: BPMMANAGER_ROL},
  detallarAnexo:{url :"detallar-anexo",rol : RADICADOR},
  kpis:"reports-kpi",
  generarPlanillaInterna : "generar-planilla-interna",
  recibirDocumentos : "recibir-documentos",
  digitalizarDocumentoEpehsoft: 'digitalizar-documento-epehsoft',
  listaManager:{url : 'admin-listas', rol : BPMMANAGER_ROL},
  consultaDocumentos:{url: 'consulta-documentos', rol:[CONSULTA_CLASIFICADO,CONSULTA_PUBLICO,CONSULTA_RESERVADO]},
  distribucionTrasladoInterno: {url : 'distribucion-traslado-interno', rol: AUXILIAR_CORRESPONDENCIA},
  reciboDocumentosFisicos: {url : 'recibo-documentos-fisicos', rol: AUXILIAR_CORRESPONDENCIA}
};
