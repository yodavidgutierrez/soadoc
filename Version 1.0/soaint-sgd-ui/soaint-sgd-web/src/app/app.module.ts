import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HashLocationStrategy, LocationStrategy} from '@angular/common';
import {AppRoutes} from './app.routes';
import 'rxjs/add/operator/toPromise';
import {NgxChartsModule} from '@swimlane/ngx-charts';
import {NgxBarcodeModule} from 'ngx-barcode';
import {ToastrModule} from 'ngx-toastr';
// APP COMPONENTS
import {AppComponent} from './app.component';
import {
  BUSSINESS_COMPONENTS_PROVIDERS,
  LAYOUT_COMPONENTS_PROVIDERS,
  PAGE_COMPONENTS_PROVIDERS,
  UI_COMPONENTS
} from 'app/ui/__ui.include';
// APP MODULES
import {ConfirmationService, PIPES, PRIMENG_MODULES,} from './shared/__shared.include';
// import {PRIMENG_MODULES} from './shared/primeng/__primeng';
// import {PrintDirective} from './shared/directives/print.directive';
import {LocalStorageModule} from 'angular-2-local-storage';
// APP SERVICES
import {API_SERVICES, EFFECTS_MODULES, INFRASTRUCTURE_SERVICES} from './infrastructure/__infrastructure.include';
// Redux Store and Colaterals
import {StoreModule} from '@ngrx/store';
import {StoreDevtoolsModule} from '@ngrx/store-devtools';
import {ReduxStore} from './infrastructure/redux-store/__redux-config';
import {RouterStoreModule} from '@ngrx/router-store';
import {PdfViewerComponent} from 'ng2-pdf-viewer';
import {DistribucionFisicaComponent} from './ui/page-components/distribucion-fisica/distribucion-fisica.component';
import {CargarPlanillasComponent} from './ui/page-components/cargar-planillas/cargar-planillas.component';
import {EditarPlanillaComponent} from './ui/bussiness-components/editar-planilla/editar-planilla.component';
import {PlanillaGeneradaComponent} from './ui/bussiness-components/planilla-generada/planilla-generada.component';
import {DIRECTIVES} from './shared/directives/__directives.include';
import {PIPES_AS_PROVIDERS} from './shared/pipes/__pipes.include';
import {customHttpProvider} from './shared/interceptors/http.provider';
import { OrderModule } from 'ngx-order-pipe';
import { SingleUploadComponent } from './ui/layout-components/presentation/single-upload/single-upload.component';
import { SecurityRoleComponent } from './ui/page-components/security-role/security-role.component';
import { FuncionarioListComponent } from './ui/page-components/security-role/components/funcionario-list/funcionario-list.component';
import { ControlMessagesComponent } from './ui/page-components/security-role/components/control-messages/control-messages.component';
import { UnidadesDocumentalesComponent } from './ui/page-components/unidades-documentales/unidades-documentales.component';
import { GestionarDevolucionesComponent } from './ui/page-components/gestionar-devoluciones/gestionar-devoluciones.component';
import { CorregirRadicacionComponent } from './ui/page-components/corregir-radicacion/corregir-radicacion.component';
import { DocumentoEcmComponent } from './ui/bussiness-components/documento-ecm/documento-ecm.component';
import { DatosRemitenteComponent } from './ui/page-components/radicacion-salida/components/datos-remitente/datos-remitente.component';
import { DatosDestinatarioComponent } from './ui/page-components/radicacion-salida/components/datos-destinatario/datos-destinatario.component';
import { DatosDestinatarioExternoComponent } from './ui/page-components/radicacion-salida/components/datos-destinatario/datos-destinatario-externo/datos-destinatario-externo.component';
import { DatosDestinatarioInternoComponent } from './ui/page-components/radicacion-salida/components/datos-destinatario/datos-destinatario-interno/datos-destinatario-interno.component';
import { DatosRemitentesComponent } from './ui/bussiness-components/datos-remitentes/datos-remitentes.component';
import { DetalleUnidadConservacionComponent } from './ui/page-components/unidades-documentales/components/detalle-unidad-conservacion/detalle-unidad-conservacion.component';
import { RedirectSeleccionarDocumentoComponent } from './ui/page-components/organizacion-archivo/redirect-seleccionar-documento/redirect-seleccionar-documento.component';
import {ProcesoService} from "./infrastructure/api/proceso.service";
import {SolicitudCreacionUdService} from "./infrastructure/api/solicitud-creacion-ud.service";
import { ListaDocumentosArchivadosComponent } from './ui/page-components/organizacion-archivo/archivar-documento/components/seleccionar-unidad-documental/lista-documentos-archivados/lista-documentos-archivados.component';
import { CrearUnidadDocumentalComponent } from './ui/page-components/organizacion-archivo/crear-unidad-documental/crear-unidad-documental.component';
import { ListaSolicitudCrearUdComponent } from './ui/page-components/organizacion-archivo/crear-unidad-documental/lista-solicitud-crear-ud/lista-solicitud-crear-ud.component';
import { NoTramitarCreacionUdComponent } from './ui/page-components/organizacion-archivo/crear-unidad-documental/no-tramitar-creacion-ud/no-tramitar-creacion-ud.component';
import { FormCrearUnidadDocumentalComponent } from './ui/page-components/organizacion-archivo/crear-unidad-documental/form-crear-unidad-documental/form-crear-unidad-documental.component';
import { UdTramitadasComponent } from './ui/page-components/organizacion-archivo/crear-unidad-documental/ud-tramitadas/ud-tramitadas.component';
import { RsTicketRadicadoComponent } from './ui/page-components/radicacion-salida/components/rs-ticket-radicado/rs-ticket-radicado.component';
import {TicketRadicadoComponent} from "./ui/bussiness-components/ticket-radicado/ticket-radicado.component";
import { DistribucionComponent } from './ui/page-components/radicacion-salida/components/distribucion/distribucion.component';
import {DatosGeneralesEditComponent} from "./ui/bussiness-components/datos-generales-edit/datos-generales-edit.component";
import {DatosRemitenteEditComponent} from "./ui/bussiness-components/datos-remitente-edit/datos-remitente-edit.component";
import {DatosDestinatarioEditComponent} from "./ui/bussiness-components/datos-destinatario-edit/datos-destinatario-edit.component";
import {RadicacionSalidaService} from "./infrastructure/api/radicacion-salida.service";
import {UnidadDocumentalApiService} from "./infrastructure/api/unidad-documental.api";
import { AlertComponent } from './ui/bussiness-components/notifications/alert/alert.component';
import { UI_STATE_SERVICES } from 'app/infrastructure/_ui-state-service.include';
import { FormEnvioComponent } from './ui/page-components/radicacion-salida/components/form-envio/form-envio.component';

import { ArchivarDocumentoComponent } from './ui/page-components/organizacion-archivo/archivar-documento/archivar-documento.component';
import {SeleccionarDocumentosComponent} from "./ui/page-components/organizacion-archivo/archivar-documento/components/seleccionar-documentos/seleccionar-documentos.component";
import {DireccionToTextPipe} from './ui/bussiness-components/datos-direccion/direccion-to-text.pipe';
import { DisposicionFinalComponent } from './ui/page-components/disposicion-final/disposicion-final.component';
import {AdjuntarDocumentoComponent} from "./ui/page-components/radicacion-salida/components/adjuntar-documento/adjuntar-documento.component";
import {ArchivarDocumentoApiService} from "./infrastructure/api/archivar-documento.api";
import {FuncionariosService} from "./infrastructure/api/funcionarios.service";
import { TransferenciasDocumentalesComponent } from './ui/page-components/transferencias-documentales/transferencias-documentales.component';
import { PopupUnidadDocumentalNotasComponent } from './ui/bussiness-components/popup-unidad-documental-notas/popup-unidad-documental-notas.component';
import { TipologiaDocumentalPipePipe } from './shared/pipes/tipologia-documental-pipe.pipe';
import { InformacionEnvioComponent } from './ui/bussiness-components/informacion-envio/informacion-envio.component';
import { CargarPlanillaSalidaComponent } from './ui/page-components/cargar-planilla-salida/cargar-planilla-salida.component';
import { PopupUbicarTransferenciaComponent } from './ui/bussiness-components/popup-ubicar-transferencia/popup-ubicar-transferencia.component';
import { WorkspaceUserComponent } from './ui/page-components/workspace-user/workspace-user.component';
import { FuncionarioNombreCompletoPipe } from './shared/pipes/funcionario-nombre-completo.pipe';
import { FuncionarioNombreIdPipe } from './shared/pipes/funcionario-nombre-id.pipe';
import { AsociarRadicadosComponent } from './ui/bussiness-components/asociar-radicados/asociar-radicados.component';
import { DetallarAnexoComponent } from './ui/page-components/detallar-anexo/detallar-anexo.component';
import { DashBuilderComponent } from './ui/page-components/dash-builder/dash-builder.component';
import { CorrespondenciaInternaComponent } from './ui/page-components/correspondencia-interna/correspondencia-interna.component';
import { GenerarPlanillaInternaComponent } from './ui/page-components/generar-planilla-interna/generar-planilla-interna.component';
import { RecibirDocumentosComponent } from './ui/page-components/recibir-documentos/recibir-documentos.component';
import { ProfileEditComponent } from './ui/page-components/security-role/components/profile-edit/profile-edit.component';
import {ObservacionesComponent} from './ui/page-components/produccion-documental/components/observaciones/observaciones.component';
import { DigitalizacionEphehsoftComponent } from './ui/page-components/digitalizacion-ephehsoft/digitalizacion-ephehsoft.component';
import {CKEditorModule} from 'ng2-ckeditor';
import { AdminConstantesComponent } from './ui/page-components/admin-constantes/admin-constantes.component';
import { FormGestionarConstantesComponent } from './ui/page-components/admin-constantes/components/form-gestionar-constantes/form-gestionar-constantes.component';
import { DatosUnidadConservacionComponent } from './ui/bussiness-components/datos-unidad-conservacion/datos-unidad-conservacion.component';
import { ConsultaDocumentosComponent } from './ui/page-components/consulta-documentos/consulta-documentos.component';
import {ApiConsultaDocumentos} from "./infrastructure/api/api-consulta-documentos";
import { ConsultaExpedientesComponent } from './ui/page-components/consulta-documentos/consulta-expedientes/consulta-expedientes.component';
import { CargarPanillaTrasladosInternoComponent } from './ui/page-components/cargar-panilla-traslados-interno/cargar-panilla-traslados-interno.component';


@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutes,
    HttpModule,
    BrowserAnimationsModule,
    NgxChartsModule,
    NgxBarcodeModule,
    OrderModule,
    CKEditorModule,
    ToastrModule.forRoot({
      closeButton: true, // show close button
      timeOut: 3000, // time to live
      enableHtml: true, // allow html in message. (UNSAFE)
      extendedTimeOut: 1000, // time to close after a user hovers over toast
      progressBar: true, // show progress bar
      newestOnTop: true, // new toast placement
      // preventDuplicates: true,
      // toastClass: string = 'toast'; // class on toast
      // positionClass: string = 'toast-top-right'; // class on toast
      // titleClass: string = 'toast-title'; // class inside toast on title
      // messageClass: string = 'toast-message';
    }),
    // PrimeNG Modules => view components
    ...PRIMENG_MODULES,
    // third party libs
    LocalStorageModule.withConfig({
      prefix: 'my-app',
      //  storageType: 'localStorage'
      storageType: 'sessionStorage'
    }),
    /**
     * StoreModule.provideStore is imported once in the root module, accepting a reducer
     * function or object map of reducer functions. If passed an object of
     * reducers, combineReducers will be run creating your application
     * meta-reducer. This returns all providers for an @ngrx/store
     * based application.
     */
    StoreModule.provideStore(ReduxStore, {
      router: {
        path: window.location.pathname + window.location.search
      }
    }),
    /**
     * @ngrx/router-store keeps router state up-to-date in the store and uses
     * the store as the single source of truth for the router's state.
     */
    RouterStoreModule.connectRouter(),
    /**
     * Store devtools instrument the store retaining past versions of state
     * and recalculating new states. This enables powerful time-travel
     * debugging.
     *
     * To use the debugger, install the Redux Devtools extension for either
     * Chrome or Firefox
     *
     * See: https://github.com/zalmoxisus/redux-devtools-extension
     */
    StoreDevtoolsModule.instrumentOnlyWithExtension(),
    /**
     * EffectsModule.run() sets up the effects class to be initialized
     * immediately when the application starts.
     *
     * See: https://github.com/ngrx/effects/blob/master/docs/api.md#run
     */
    ...EFFECTS_MODULES


  ],
  declarations: [
    AppComponent,
    PdfViewerComponent,
    ...DIRECTIVES,
    ...UI_COMPONENTS,
    ...PIPES,
    DireccionToTextPipe,
    DistribucionFisicaComponent,
    CargarPlanillasComponent,
    EditarPlanillaComponent,
    PlanillaGeneradaComponent,
    SingleUploadComponent,
    SecurityRoleComponent,
    FuncionarioListComponent,
    ControlMessagesComponent,
    UnidadesDocumentalesComponent,
    GestionarDevolucionesComponent,
    CorregirRadicacionComponent,
    DocumentoEcmComponent,
    DatosRemitenteComponent,
    DatosDestinatarioComponent,
    DatosDestinatarioExternoComponent,
    DatosDestinatarioInternoComponent,
    DatosRemitentesComponent,
    DetalleUnidadConservacionComponent,
    RedirectSeleccionarDocumentoComponent,
    ListaDocumentosArchivadosComponent,
    CrearUnidadDocumentalComponent,
    ListaSolicitudCrearUdComponent,
    NoTramitarCreacionUdComponent,
    FormCrearUnidadDocumentalComponent,
    UdTramitadasComponent,
    AdjuntarDocumentoComponent,
    RsTicketRadicadoComponent,
    TicketRadicadoComponent,
    DistribucionComponent,
    DisposicionFinalComponent,
    DatosGeneralesEditComponent,
    DatosRemitenteEditComponent,
    DatosDestinatarioEditComponent,
    AlertComponent,
    FormEnvioComponent,
    ArchivarDocumentoComponent,
    SeleccionarDocumentosComponent,
    TransferenciasDocumentalesComponent,
    PopupUnidadDocumentalNotasComponent,
    TipologiaDocumentalPipePipe,
    InformacionEnvioComponent,
    CargarPlanillaSalidaComponent,
    PopupUbicarTransferenciaComponent,
    WorkspaceUserComponent,
    FuncionarioNombreCompletoPipe,
    FuncionarioNombreIdPipe,
    AsociarRadicadosComponent,
    DetallarAnexoComponent,
    DashBuilderComponent,
    CorrespondenciaInternaComponent,
    GenerarPlanillaInternaComponent,
    RecibirDocumentosComponent,
    ProfileEditComponent,
    ObservacionesComponent,
    DigitalizacionEphehsoftComponent,
    AdminConstantesComponent,
    FormGestionarConstantesComponent,
    DatosUnidadConservacionComponent,
    ConsultaDocumentosComponent,
    ConsultaExpedientesComponent,
    CargarPanillaTrasladosInternoComponent,
   ],
  providers: [
    {provide: LocationStrategy, useClass: HashLocationStrategy},
    ...INFRASTRUCTURE_SERVICES,
    ...API_SERVICES,
    ...PAGE_COMPONENTS_PROVIDERS,
    ...LAYOUT_COMPONENTS_PROVIDERS,
    ...BUSSINESS_COMPONENTS_PROVIDERS,
    ...PIPES_AS_PROVIDERS,
    ...UI_STATE_SERVICES,
    ConfirmationService,
    ProcesoService,
    SolicitudCreacionUdService,
    RadicacionSalidaService,
    UnidadDocumentalApiService,
    ArchivarDocumentoApiService,
    ApiConsultaDocumentos,
    FuncionariosService,
    customHttpProvider()
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
