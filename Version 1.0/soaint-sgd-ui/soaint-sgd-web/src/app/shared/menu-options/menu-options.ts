import {
  ARCHIVISTA_AC,
  ASIGNADOR,
  AUXILIAR_CORRESPONDENCIA,
  BPMMANAGER_ROL, CONSULTA_CLASIFICADO, CONSULTA_KPI, CONSULTA_PUBLICO, CONSULTA_RESERVADO, DIGITALIZADOR,
  RADICADOR_CONTINGENCIA
} from '../../app.roles';
import {ROUTES_PATH} from "../../app.route-names";
export const
  MENU_OPTIONS = [
  {label: 'Vista Corporativa', icon: 'dashboard', routerLink: ['/' + ROUTES_PATH.dashboard]},
  {label: 'Tareas', icon: 'list', routerLink: ['/' + ROUTES_PATH.workspace]},
  {label: 'Procesos', icon: 'work', routerLink: ['/' + ROUTES_PATH.processList]},
  //{label: 'Generar Plantilla Interna', icon: 'subject', routerLink: ['/' + ROUTES_PATH.generarPlanillaInterna]},

];

export  const  GROUPS = {"consulta":[CONSULTA_CLASIFICADO,CONSULTA_PUBLICO,CONSULTA_RESERVADO]}

export let FOR_ROLE_OPTIONS = {

};

FOR_ROLE_OPTIONS[BPMMANAGER_ROL] = [
 // {label: 'Administracion de tarea', icon:'list',routerLink:['/'+ ROUTES_PATH.taskManager]}
];

FOR_ROLE_OPTIONS[ASIGNADOR] = [
  {label: 'Asignar comunicaciones', icon: 'subject', routerLink: ['/' + ROUTES_PATH.asignacionComunicaciones.url]},
];
FOR_ROLE_OPTIONS[RADICADOR_CONTINGENCIA] = [
  {label: 'Radicación Contingencia', icon: 'subject', routerLink: ['/' + ROUTES_PATH.cargaMasiva.url]},
];
FOR_ROLE_OPTIONS[AUXILIAR_CORRESPONDENCIA] = [
  {label: 'Distribución física Entrada', icon: 'subject', routerLink: ['/' + ROUTES_PATH.distribucionFisica.url]},
  {label: 'Distribución física Salida', icon: 'subject', routerLink: ['/' + ROUTES_PATH.distribucionFisicaSalida.url]},
  {label: 'Distribución Traslados Internos', icon: 'subject', routerLink: ['/' + ROUTES_PATH.distribucionTrasladoInterno.url]},
  {label: 'Recibo Documentos Fisicos', icon: 'subject', routerLink: ['/' + ROUTES_PATH.reciboDocumentosFisicos.url]},
];

FOR_ROLE_OPTIONS[ARCHIVISTA_AC] = [
  {label: 'Disposición Final', icon: 'subject', routerLink: ['/' + ROUTES_PATH.disposicionFinal.url]},
];

// FOR_ROLE_OPTIONS[CONSULTA_KPI] = [
//   {label: 'Indicadores y KPIS', icon: 'subject', routerLink: ['/' + ROUTES_PATH.kpis]},
// ];

// FOR_ROLE_OPTIONS[DIGITALIZADOR] = [
//   {label: 'Digitalización',  icon: 'subject',  command:(self) => {
//       const  baseUrl = (<any>window).digitalizacionUrl;

//       const   base64Username =encodeURI(btoa(self.from.app.funcionarioLog));

//       window.open(`${baseUrl}?value=${base64Username}`,'_blank') ;
//     }},
// ];


FOR_ROLE_OPTIONS['consulta'] = [
  {label: 'Consulta Documentos', icon: 'subject', routerLink: ['/' + ROUTES_PATH.consultaDocumentos.url]},
];

export const RADICADORA_ROLES =  [
    AUXILIAR_CORRESPONDENCIA
];


export const PROCESS_OPTION = {
  label: 'Procesos', icon: 'dashboard', expanded: true,
  items: []
};
