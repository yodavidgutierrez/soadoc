import {DatosGeneralesComponent} from './datos-generales/datos-generales.component';
import {DatosDestinatarioComponent} from './datos-destinatario/datos-destinatario.component';
import {DatosRemitenteComponent} from './datos-remitente/datos-remitente.component';
import {TicketRadicadoComponent} from './ticket-radicado/ticket-radicado.component';
import {TaskContainerComponent} from './task-container/task-container.component';
import {DetallesAsignacionComponent} from './detalles-asignacion/detalles-asignacion.component';
import {DetallesDatosGeneralesComponent} from './detalles-asignacion/detalles-datos-generales/detalles-datos-generales.component';
import {DetallesDatosRemitenteComponent} from './detalles-asignacion/detalles-datos-remitente/detalles-datos-remitente.component';
import {DetallesDatosDestinatarioComponent} from './detalles-asignacion/detalles-datos-destinatario/detalles-datos-destinatario.component';
import {DocumentosTramiteComponent} from '../page-components/documentos-tramite/documentos-tramite.component';
import {DestinatarioSalidaComponent} from './../bussiness-components/gestionar-comunicacion/destinatario-salida/destinatario-salida.component';
import {ActualizarDatosGeneralesComponent} from './actualizar-datos-generales/actualizar-datos-generales.component';
import {DocumentosECMListComponent} from './documentos-ecm-list/documentos-ecm-list.component';
import {RedireccionesRecursivasTrazaComponent} from './redirecciones-recursivas-traza/redirecciones-recursivas-traza.component';
import {PdfViewerComponent} from "./pdf-viewer/pdf-viewer.component";

/**
 * Presentational components receieve data through @Input() and communicate events
 * through @Output() but generally maintain no internal state of their
 * own. All decisions are delegated to 'container', or 'smart'
 * components before data updates flow back down.
 *
 * More on 'smart' and 'presentational' components: https://gist.github.com/btroncone/a6e4347326749f938510#utilizing-container-components
 */

export const BUSSINESS_COMPONENTS = [
  DatosGeneralesComponent,
  DatosRemitenteComponent,
  DatosDestinatarioComponent,
  TicketRadicadoComponent,
  TaskContainerComponent,
  DetallesAsignacionComponent,
  DetallesDatosGeneralesComponent,
  DetallesDatosRemitenteComponent,
  DetallesDatosDestinatarioComponent,
  DocumentosTramiteComponent,
  DestinatarioSalidaComponent,
  RedireccionesRecursivasTrazaComponent,
  ActualizarDatosGeneralesComponent,
  DocumentosECMListComponent,
  PdfViewerComponent
];

export * from './__bussiness-providers.include';
