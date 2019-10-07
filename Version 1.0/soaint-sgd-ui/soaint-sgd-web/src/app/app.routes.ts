import {RouterModule, Routes} from '@angular/router';
import {ModuleWithProviders} from '@angular/core';
import {HomeComponent} from './ui/page-components/home/home.component';
import {AuthenticatedGuard, LoginComponent} from './ui/page-components/login/__login.include';
import {RadicarComunicacionesComponent} from './ui/page-components/radicar-comunicaciones/radicar-comunicaciones.component';
import {WorkspaceComponent} from './ui/page-components/workspace/workspace.component';
import {ProcessComponent} from './ui/page-components/process/process.component';
import {AsignarComunicacionesComponent} from './ui/page-components/asignacion-comunicaciones/asignacion-comunicaciones.component';
import {DigitalizarDocumentoComponent} from './ui/page-components/digitalizar-documento/digitalizar-documento.component';
import {CargaMasivaComponent} from './ui/page-components/carga-masiva/carga-masiva.component';
import {DocumentosTramiteComponent} from './ui/page-components/documentos-tramite/documentos-tramite.component';
import {RadicarSalidaComponent} from './ui/page-components/radicacion-salida/radicar-salida.component';
import {CargaMasivaDetailsComponent} from './ui/page-components/carga-masiva/components/cm-details.component';
import {DistribucionFisicaComponent} from './ui/page-components/distribucion-fisica/distribucion-fisica.component';
import {CargarPlanillasComponent} from './ui/page-components/cargar-planillas/cargar-planillas.component';
import {ProduccionDocumentalComponent} from './ui/page-components/produccion-documental/produccion-documental.component';
import {ProduccionDocumentalMultipleComponent} from './ui/page-components/produccion-documental/produccion-documental-multiple.component';
import {SecurityRoleComponent} from './ui/page-components/security-role/security-role.component';
import {UnidadesDocumentalesComponent} from './ui/page-components/unidades-documentales/unidades-documentales.component';
import {GestionarDevolucionesComponent} from './ui/page-components/gestionar-devoluciones/gestionar-devoluciones.component';
import {CorregirRadicacionComponent} from './ui/page-components/corregir-radicacion/corregir-radicacion.component';
import {RedirectSeleccionarDocumentoComponent} from "./ui/page-components/organizacion-archivo/redirect-seleccionar-documento/redirect-seleccionar-documento.component";
import {CrearUnidadDocumentalComponent} from "./ui/page-components/organizacion-archivo/crear-unidad-documental/crear-unidad-documental.component";
import {DisposicionFinalComponent} from './ui/page-components/disposicion-final/disposicion-final.component';
import {RadicarDocumentoProducidoComponent} from "./ui/page-components/radicacion-salida/radicar-documento-producido.component";
import {ArchivarDocumentoComponent} from "./ui/page-components/organizacion-archivo/archivar-documento/archivar-documento.component";
import {DistribucionComponent} from "./ui/page-components/radicacion-salida/components/distribucion/distribucion.component";
import {TransferenciasDocumentalesComponent} from './ui/page-components/transferencias-documentales/transferencias-documentales.component'
import { DistribucionFisicaSalidaComponent } from './ui/page-components/distribucion-fisica-salida/distribucion-fisica-salida.component';
import { CargarPlanillaSalidaComponent } from './ui/page-components/cargar-planilla-salida/cargar-planilla-salida.component';
import {WorkspaceUserComponent} from "./ui/page-components/workspace-user/workspace-user.component";
import {BpmmanagerGuard} from "./shared/guards/bpmmanager.guard";
import {DetallarAnexoComponent} from "./ui/page-components/detallar-anexo/detallar-anexo.component";
import {DashBuilderComponent} from "./ui/page-components/dash-builder/dash-builder.component";
import {GenerarPlanillaInternaComponent} from "./ui/page-components/generar-planilla-interna/generar-planilla-interna.component";
import {RecibirDocumentosComponent} from "./ui/page-components/recibir-documentos/recibir-documentos.component";
import {ROUTES_PATH} from "./app.route-names";
import {DigitalizacionEphehsoftComponent} from './ui/page-components/digitalizacion-ephehsoft/digitalizacion-ephehsoft.component'
import {AdminConstantesComponent} from "./ui/page-components/admin-constantes/admin-constantes.component";
import {DistribucionTrasladoInternoComponent} from "./ui/page-components/distribucion-traslado-interno/distribucion-traslado-interno.component";
import {ReciboDocumentosFisicosComponent} from "./ui/page-components/recibo-documentos-fisicos/recibo-documentos-fisicos.component";
import {ConsultaDocumentosComponent} from "./ui/page-components/consulta-documentos/consulta-documentos.component";
import {CargarPanillaTrasladosInternoComponent} from "./ui/page-components/cargar-panilla-traslados-interno/cargar-panilla-traslados-interno.component";

export const routes: Routes = [
  {path: 'dashboard', component: HomeComponent},
  {path: '', redirectTo: ROUTES_PATH.dashboard, pathMatch: 'full'},
  {path: ROUTES_PATH.login, component: LoginComponent},
  {path: ROUTES_PATH.dashboard, component: HomeComponent, canActivate: [AuthenticatedGuard]},
  {
    path: ROUTES_PATH.task,
    canActivate: [AuthenticatedGuard],
    children: [
      {
        path: ROUTES_PATH.radicarCofEntrada.url,
        component: RadicarComunicacionesComponent,
        canActivate: [BpmmanagerGuard]
      },
      {
        path: ROUTES_PATH.cargarPlanillas.url,
        component: CargarPlanillasComponent,
        canActivate: [BpmmanagerGuard]
      },
      {
        path: ROUTES_PATH.cargarPlanillaSalida.url,
        component: CargarPlanillaSalidaComponent,
        canActivate: [BpmmanagerGuard]
      },
      {
        path: ROUTES_PATH.cargarPlanillaSalidaInterna.url,
        component: CargarPanillaTrasladosInternoComponent,
        canActivate: [BpmmanagerGuard]
      },
      {
        path: ROUTES_PATH.detallarAnexo.url,
        component: DetallarAnexoComponent,
        canActivate: [BpmmanagerGuard]
      },
      {
        path: ROUTES_PATH.radicarCofSalida.url,
        component: RadicarSalidaComponent,
        canActivate: [BpmmanagerGuard]
      },
      {
        path: ROUTES_PATH.radicarDocumentoSalida.url,
        component: RadicarDocumentoProducidoComponent,
        canActivate: [BpmmanagerGuard]
      },
      {
        path: ROUTES_PATH.digitalizarDocumento.url,
        component: DigitalizarDocumentoComponent,
        canActivate: [BpmmanagerGuard],

      },
      {
        path: ROUTES_PATH.adjuntarDocumento.url,
        component: DigitalizarDocumentoComponent,
        canActivate: [BpmmanagerGuard],

      },
      {
        path: ROUTES_PATH.documentosTramite.url,
        component: DocumentosTramiteComponent,
        canActivate: [BpmmanagerGuard]
      },
      {
        path: ROUTES_PATH.produccionDocumentalMultiple.url,
        component: ProduccionDocumentalMultipleComponent,
        canActivate: [BpmmanagerGuard]
      },
      {
        path: ROUTES_PATH.produccionDocumental.url + '/:status',
        component: ProduccionDocumentalComponent,
        canActivate: [BpmmanagerGuard]
      },
      {
        path: ROUTES_PATH.gestionarDevoluciones.url,
        component: GestionarDevolucionesComponent,
        canActivate: [BpmmanagerGuard]
      },
      {
        path: ROUTES_PATH.corregirRadicacion.url,
        component: CorregirRadicacionComponent,
        canActivate: [BpmmanagerGuard]
      },

      {
        path: ROUTES_PATH.gestionUnidadDocumental.url,
        component: UnidadesDocumentalesComponent,
        canActivate: [BpmmanagerGuard]
      },
      {
        path: ROUTES_PATH.archivarDocumento,
        component: ArchivarDocumentoComponent,
        canActivate:[BpmmanagerGuard]
      },
      {
        path: ROUTES_PATH.crearUnidadDocumental.url,
        component: CrearUnidadDocumentalComponent,
        canActivate:[BpmmanagerGuard]
      },
      {
        path: ROUTES_PATH.completarDatosDistribucion.url,
        component: DistribucionComponent,
        canActivate: [BpmmanagerGuard]
      },
      {
        path: ROUTES_PATH.transferenciasDocumentales.url + '/:status',
        component: TransferenciasDocumentalesComponent,
        canActivate: [BpmmanagerGuard]
      },
      {
        path: ROUTES_PATH.digitalizarDocumentoEpehsoft ,
        component: DigitalizacionEphehsoftComponent ,
        canActivate: [AuthenticatedGuard]
      },
    ]
  },
  {
    path: ROUTES_PATH.kpis,
    component: DashBuilderComponent,
    canActivate: [AuthenticatedGuard]
  },
  {path: ROUTES_PATH.workspace, component: WorkspaceComponent, canActivate: [AuthenticatedGuard]},
  {path: ROUTES_PATH.processList, component: ProcessComponent, canActivate: [AuthenticatedGuard]},
  {
    path: ROUTES_PATH.asignacionComunicaciones.url,
    component: AsignarComunicacionesComponent,
    canActivate: [BpmmanagerGuard]
  },
  {
    path: ROUTES_PATH.radicarCofSalida.url,
    component: RadicarSalidaComponent,
    canActivate: [BpmmanagerGuard]
  },
  {
    path: ROUTES_PATH.cargaMasiva.url,
    canActivate: [BpmmanagerGuard],
    component: CargaMasivaComponent
  },
  {
    path: ROUTES_PATH.cargaMasivaDetails,
    component: CargaMasivaDetailsComponent,
    canActivate: [BpmmanagerGuard]
  },
  {
    path: ROUTES_PATH.distribucionFisica.url,
    component: DistribucionFisicaComponent,
    canActivate: [BpmmanagerGuard]
  },
  {
    path: ROUTES_PATH.distribucionFisicaSalida.url,
    component: DistribucionFisicaSalidaComponent,
    canActivate: [BpmmanagerGuard]
  },
  {
    path: ROUTES_PATH.archivarDocumento,
    component: RedirectSeleccionarDocumentoComponent,
    canActivate: [AuthenticatedGuard]
  },
  {
    path: ROUTES_PATH.securityRole.url,
    component: SecurityRoleComponent,
    canActivate:[BpmmanagerGuard]
  },
  {
    path: ROUTES_PATH.disposicionFinal.url,
    component: DisposicionFinalComponent,
    canActivate: [BpmmanagerGuard]
  },
  {
    path: ROUTES_PATH.taskManager.url,
    component: WorkspaceUserComponent,
    canActivate: [BpmmanagerGuard]
  },
  {
    path: ROUTES_PATH.listaManager.url,
    component: AdminConstantesComponent,
    canActivate: [BpmmanagerGuard]
  },
  {
    path: ROUTES_PATH.generarPlanillaInterna,
    component: GenerarPlanillaInternaComponent,
    canActivate: [AuthenticatedGuard]
  },
  {
    path: ROUTES_PATH.recibirDocumentos,
    component: RecibirDocumentosComponent,
    canActivate: [AuthenticatedGuard]
  },
  {
    path: ROUTES_PATH.digitalizarDocumentoEpehsoft ,
    component: DigitalizacionEphehsoftComponent ,
    canActivate: [AuthenticatedGuard]
  },
  {
    path: ROUTES_PATH.distribucionTrasladoInterno.url,
    component: DistribucionTrasladoInternoComponent,
    canActivate: [BpmmanagerGuard]
  },
  {
    path: ROUTES_PATH.reciboDocumentosFisicos.url,
    component: ReciboDocumentosFisicosComponent,
    canActivate: [BpmmanagerGuard]
  },
  {
    path: ROUTES_PATH.consultaDocumentos.url,
    component: ConsultaDocumentosComponent ,
    canActivate: [BpmmanagerGuard]
  }
];

export const AppRoutes: ModuleWithProviders = RouterModule.forRoot(routes);

