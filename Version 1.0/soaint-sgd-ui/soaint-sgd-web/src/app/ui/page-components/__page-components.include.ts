import {LoginComponent} from './login/login.component';
import {HomeComponent} from './home/home.component';
import {WorkspaceComponent} from './workspace/workspace.component';
import {DigitalizarDocumentoComponent} from './digitalizar-documento/digitalizar-documento.component';
import {RadicarComunicacionesComponent} from './radicar-comunicaciones/radicar-comunicaciones.component';
import {ProcessComponent} from './process/process.component';
import {DatosDireccionComponent} from '../bussiness-components/datos-direccion/datos-direccion.component';
import {AsignarComunicacionesComponent} from './asignacion-comunicaciones/asignacion-comunicaciones.component';
import {PopupJustificacionComponent} from '../bussiness-components/popup-justificacion/popup-justificacion.component';
import {PopupAgregarObservacionesComponent} from '../bussiness-components/popup-agregar-observaciones/popup-agregar-observaciones.component';
import {PopupRejectComponent} from '../bussiness-components/popup-reject/popup-reject.component';
import {GestionarComunicacionComponent} from '../bussiness-components/gestionar-comunicacion/gestionar-comunicacion.component';
import {RadicarSalidaComponent} from './radicacion-salida/radicar-salida.component';
import {CARGA_MASIVA_COMPONENTS} from './carga-masiva/__cm-components.include';
import {DistribucionFisicaComponent} from './distribucion-fisica/distribucion-fisica.component';
import {CargarPlanillasComponent} from './cargar-planillas/cargar-planillas.component';
import {EditarPlanillaComponent} from '../bussiness-components/editar-planilla/editar-planilla.component';
import {PRODUCCION_DOCUMENTAL_COMPONENTS} from './produccion-documental/_pd-components.include';
import {PlanillaGeneradaComponent} from '../bussiness-components/planilla-generada/planilla-generada.component';
import {SeleccionarUnidadDocumentalComponent} from './organizacion-archivo/archivar-documento/components/seleccionar-unidad-documental/seleccionar-unidad-documental.component';
import { DisposicionFinalComponent } from './disposicion-final/disposicion-final.component';
import {RadicarDocumentoProducidoComponent} from "./radicacion-salida/radicar-documento-producido.component";
import { DistribucionFisicaSalidaComponent } from './distribucion-fisica-salida/distribucion-fisica-salida.component';
import {DistribucionTrasladoInternoComponent} from "./distribucion-traslado-interno/distribucion-traslado-interno.component";
import {ReciboDocumentosFisicosComponent} from "./recibo-documentos-fisicos/recibo-documentos-fisicos.component";

/**
 * All state updates are handled through dispatched actions in 'container'
 * components. This provides a clear, reproducible history of state
 * updates and user interaction through the life of our
 * application.
 *
 * Note: Container components are also reusable. Whether or not
 * a component is a presentation component or a container
 * component is an implementation detail.
 */
export const PAGE_COMPONENTS = [
  HomeComponent,
  LoginComponent,
  // ProductosComponent,
  RadicarComunicacionesComponent,
  DigitalizarDocumentoComponent,
  WorkspaceComponent,
  ProcessComponent,
  DatosDireccionComponent,
  AsignarComunicacionesComponent,
  PopupJustificacionComponent,
  PopupAgregarObservacionesComponent,
  PopupRejectComponent,
  GestionarComunicacionComponent,
  RadicarSalidaComponent,
  DistribucionFisicaComponent,
  CargarPlanillasComponent,
  EditarPlanillaComponent,
  PlanillaGeneradaComponent,
  SeleccionarUnidadDocumentalComponent,
  DisposicionFinalComponent,
  RadicarDocumentoProducidoComponent,
  DistribucionFisicaSalidaComponent,
  DistribucionTrasladoInternoComponent,
  ReciboDocumentosFisicosComponent,
  ...CARGA_MASIVA_COMPONENTS,
  ...PRODUCCION_DOCUMENTAL_COMPONENTS
];

export * from './__page-providers.include';
